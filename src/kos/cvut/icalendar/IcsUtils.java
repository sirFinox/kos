package kos.cvut.icalendar;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IcsUtils {
    private IcsUtils() {}
    
    /**
     * Parses an RFC 5545 UTC offset and returns milliseconds.
     * 
     * Grammar:
     *        utc-offset = time-numzone
     *        time-numzone = ("+" / "-") time-hour time-minute [time-second]
     */
    public static int parseUtcOffset(String value) {
        final Pattern utcOffsetPattern = Pattern.compile("([-+])(\\d{1,2})(\\d{2})(?:\\d{2})?");
        final Matcher matcher = utcOffsetPattern.matcher(value);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(value);
        }
        int sign = matcher.group(1).equals("+") ? 1 : -1;
        int hours = Integer.parseInt(matcher.group(2));
        int minutes = Integer.parseInt(matcher.group(3));
        int offsetSeconds = sign * (hours*60 + minutes)*60;
        return offsetSeconds * 1000;
    }
    
   
    
    /**
     * Returns the constant from java.util.Calendar corresponding to the given two-letter day of the week.
     */
    public static int parseDayName(String twoLetterDayName) {
        if (twoLetterDayName.equals("SU")) {
            return Calendar.SUNDAY;
        } else if (twoLetterDayName.equals("MO")) {
            return Calendar.MONDAY;
        } else if (twoLetterDayName.equals("TU")) {
            return Calendar.TUESDAY;
        } else if (twoLetterDayName.equals("WE")) {
            return Calendar.WEDNESDAY;
        } else if (twoLetterDayName.equals("TH")) {
            return Calendar.THURSDAY;
        } else if (twoLetterDayName.equals("FR")) {
            return Calendar.FRIDAY;
        } else if (twoLetterDayName.equals("SA")) {
            return Calendar.SATURDAY;
        } else {
            throw new IllegalArgumentException(twoLetterDayName);
        }
    }
    
    /**
     * Removes escaping from an RFC 5545 "text".
     * 
     *        ESCAPED-CHAR = ("\\" / "\;" / "\," / "\N" / "\n")
     *        ; \\ encodes \
     *        ; \N or \n encodes newline
     *        ; \; encodes ;
     *        ; \, encodes ,
     */
    public static String unescape(String s) {
        if (s.indexOf('\\') == -1) {
            return s;
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < s.length(); ++i) {
            char ch = s.charAt(i);
            if (ch == '\\' && i < s.length() - 1) {
                char ch2 = s.charAt(++i);
                if (ch2 == 'n' || ch2 == 'N') {
                    ch2 = '\n';
                }
                result.append(ch2);
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }
    
  
    
    /**
     * Returns the value of the first property with name 'propertyName' in 'component', or null.
     * The value will have had any escaped characters unescaped.
     */
    public static String getFirstPropertyText(ICalendar.Component component, String propertyName) {
        ICalendar.Property property = component.getFirstProperty(propertyName);
        return (property != null) ? IcsUtils.unescape(property.getValue()) : null;
    }
}