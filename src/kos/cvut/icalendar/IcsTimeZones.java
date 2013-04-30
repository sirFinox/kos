package kos.cvut.icalendar;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Learns about all the time zones represented in a .ics file, and parses RFC 5545 DATE or DATE-TIME values appropriately.
 */
public class IcsTimeZones {
    // The custom time zones defined by our .ics file.
    private final Map<String, TimeZone> mTimeZones = new HashMap<String, TimeZone>();
    
    /**
     * Constructs a new IcsTimeZones from the given 'calendar'.
     */
    public IcsTimeZones(ICalendar.Component calendar) {
        if (calendar.getComponents() != null) {
            parseTimeZones(calendar);
        }
    }
    
    /**
     * Parses all the VTIMEZONE components in the given 'calendar'.
     */
    private void parseTimeZones(ICalendar.Component calendar) {
        for (ICalendar.Component component : calendar.getComponents()) {
            if (component.getName().equals(ICalendar.Component.VTIMEZONE)) {
                parseTimeZone(component);
            }
        }
    }
    
    /**
     * Describes one variant ("STANDARD" or "DAYLIGHT") of a VTIMEZONE.
     */
    private static class TzInfo {
        int offset;
        int day;
        int dayOfWeek;
        int month;
        
        static TzInfo fromComponent(String name, ICalendar.Component c, boolean requiresRule) {
            TzInfo result = new TzInfo();
            ICalendar.Property offsetProperty = c.getFirstProperty("TZOFFSETTO");
            if (offsetProperty == null || offsetProperty.getValue() == null) {
                throw new IllegalArgumentException("missing TZOFFSETTO in '" + name + "' variant '" + c.getName() + "'");
            }
            result.offset = IcsUtils.parseUtcOffset(offsetProperty.getValue());

            ICalendar.Property rule = c.getFirstProperty("RRULE");
            if (rule == null) {
                if (requiresRule) {
                    throw new IllegalArgumentException(name + ", variant " + c.getName() + " has no RRULE!");
                } else {
                    return result;
                }
            }
            // RRULE:FREQ=YEARLY;BYMINUTE=0;BYHOUR=2;BYDAY=1SU;BYMONTH=11
            for (String param : rule.getValue().split(";")) {
                String[] keyValue = param.split("=");
                String key = keyValue[0];
                String value = keyValue[1];
                if (key.equals("FREQ")) {
                    if (!value.equals("YEARLY")) {
                        throw new IllegalArgumentException(rule.getValue() + ": FREQ not YEARLY!");
                    }
                } else if (key.equals("BYDAY")) {
                    Matcher m = Pattern.compile("(-?\\d+)(..)").matcher(value);
                    if (!m.matches()) {
                        throw new IllegalArgumentException(rule.getValue() + ": can't parse BYDAY!");
                    }
                    result.day = Integer.parseInt(m.group(1));
                    result.dayOfWeek = IcsUtils.parseDayName(m.group(2));
                } else if (key.equals("BYMONTH")) {
                    result.month = Integer.parseInt(value) - 1;
                } else {
                    System.err.println("warning: ignoring key " + key + " (value " + value + ") for " + name + ", variant " + c.getName());
                }
            }
            return result;
        }
    }
    
    /**
     * Translates the given VTIMEZONE component 'timeZone' into an entry in the TimeZone map.
     */
    private void parseTimeZone(ICalendar.Component timeZone) {
        final String name = IcsUtils.getFirstPropertyText(timeZone, "TZID");
        final List<ICalendar.Component> variants = timeZone.getComponents();
        final TimeZone newTimeZone;
        if (variants.size() == 2) {
            // We have a daylight savings schedule.
            final boolean standardFirst = variants.get(0).getName().equals("STANDARD");
            final TzInfo standard = TzInfo.fromComponent(name, variants.get(standardFirst ? 0 : 1), true);
            final TzInfo daylight = TzInfo.fromComponent(name, variants.get(standardFirst ? 1 : 0), true);
            newTimeZone = new SimpleTimeZone(standard.offset, name, daylight.month, daylight.day, daylight.dayOfWeek, 7200000, standard.month, standard.day, standard.dayOfWeek, 7200000, Math.abs(daylight.offset - standard.offset));
        } else if (variants.size() == 1) {
            // No daylight savings.
            TzInfo tzInfo = TzInfo.fromComponent(name, variants.get(0), false);
            newTimeZone = new SimpleTimeZone(tzInfo.offset, name);
        } else {
            throw new IllegalArgumentException("time zone '" + name + "' has " + variants.size() + " variants: " + timeZone);
        }
        mTimeZones.put(name, newTimeZone);
        System.err.println("added '" + name + "' as " + newTimeZone);
    }
    
    public IcsTime parseTimeProperty(ICalendar.Component event, String propertyName) {
        final ICalendar.Property property = event.getFirstProperty(propertyName);
        if (property == null) {
            return null;
        }
        
        final String value = property.getValue();
        if (value == null || value.length() == 0) {
            return null;
        }
        
        final ICalendar.Parameter tzIdParam = property.getFirstParameter("TZID");
        // FIXME: shouldn't ICalendar automatically remove the quotes for us?
        final String tzId = (tzIdParam != null && tzIdParam.value != null) ? tzIdParam.value.replaceFirst("^\"(.*)\"$", "$1") : null;
        final TimeZone tz = (tzId != null) ? mTimeZones.get(tzId) : TimeZone.getTimeZone("GMT");
        
        final Calendar calendar = Calendar.getInstance(tz);
        return parseDateTime(calendar, value);
    }
    
    private static IcsTime parseDateTime(Calendar calendar, String dateTime) {
        if (dateTime.length() == 8) {
            // Just a date.
            Matcher m = Pattern.compile("(....)(..)(..)").matcher(dateTime);
            if (!m.matches()) {
                throw new IllegalArgumentException(dateTime);
            }
            final int year = Integer.parseInt(m.group(1));
            final int month = Integer.parseInt(m.group(2)) - 1;
            final int day = Integer.parseInt(m.group(3));
            calendar.clear();
            calendar.set(year, month, day);
            System.err.println(dateTime + " -> " + calendar);
            return new IcsTime(calendar.getTimeInMillis(), true);
        }
        
        // TODO: if we see Z, check that the calendar's time zone is GMT.
        Matcher m = Pattern.compile("(....)(..)(..)T(..)(..)(..)Z?").matcher(dateTime);
        if (!m.matches()) {
            throw new IllegalArgumentException(dateTime);
        }
        final int year = Integer.parseInt(m.group(1));
        final int month = Integer.parseInt(m.group(2)) - 1;
        final int day = Integer.parseInt(m.group(3));
        final int hour = Integer.parseInt(m.group(4));
        final int minute = Integer.parseInt(m.group(5));
        final int second = Integer.parseInt(m.group(6));
        calendar.clear();
        calendar.set(year, month, day, hour, minute, second);
        System.err.println(dateTime + " -> " + calendar);
        return new IcsTime(calendar.getTimeInMillis(), false);
    }
    
   
}