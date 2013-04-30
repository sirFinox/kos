package com.kos.cvut.fragments;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;

import kos.cvut.getdata.SqlConnector;
import kos.cvut.icalendar.ICalendar;
import kos.cvut.icalendar.IcsTime;
import kos.cvut.icalendar.IcsTimeZones;
import kos.cvut.icalendar.IcsUtils;
import net.fortuna.ical4j.model.DateTime;
import net.qmsource.android.cvut.kos.auth.KosHttpClient;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.CalendarContract.Calendars;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.kos.R;
import com.kos.authentification.authenticator.AuthenticatorActivity;
import com.kos.cvut.Constants;

public class SyncCalFragment extends SherlockFragment {
	// private static final String USERNAME="kosandr";
	// private static final String PASSWORD="nAtras9UpruCRuza";
	static HttpClient client;
	static HttpUriRequest request;
	private String TAG = "kos.cvut";
	public static String[] EVENT_PROJECTION = new String[] { Calendars._ID, // 0
			Calendars.ACCOUNT_NAME, // 1
			Calendars.CALENDAR_DISPLAY_NAME, // 2
			Calendars.OWNER_ACCOUNT // 3
	};
	// The indices for the projection array above.
	private static final int PROJECTION_ID_INDEX = 0;
	private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
	private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
	private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;
	private Spinner mCalendarsSpinner;
	private Spinner reminder;
	private CheckBox reminder_checkbox;
	private ICalendar.Component mCalendar;
	private static String username2 = "";
	private int chosenCalendar;
	private String[] calNames;
	private int calIds;
	private SqlConnector sql;
	private LinearLayout ll;
	private String calendar;
	// Secrets we shouldn't know about Calendar's database schema...
	private static final String DISPLAY_NAME_COLUMN = "displayName";
	private static final String _ID_COLUMN = "_id";
	private static final String SELECTED_COLUMN = "selected";
	private static final String ACCESS_LEVEL_COLUMN = "access_level";
	private static final int CONTRIBUTOR_ACCESS = 500;
	private static final int STATUS_CONFIRMED = 1;
	// Constants from Calendar.EventsColumns...
	private static final String ALL_DAY = "allDay";
	private static final String CALENDAR_ID = "calendar_id";
	private static final String DESCRIPTION = "description";
	private static final String DTEND = "dtend";
	private static final String DTSTART = "dtstart";
	private static final String DURATION = "duration";
	private static final String EVENT_LOCATION = "eventLocation";
	private static final String EVENT_TIMEZONE = "eventTimezone";
	private static final String STATUS = "eventStatus";
	private static final String TITLE = "title";
	// Intent extra keys, from Calendar...
	private static final String EVENT_BEGIN_TIME = "beginTime";
	private static final String EVENT_END_TIME = "endTime";
	// "content:" uri authorities, from different versions of Calendar...
	private static final String[] AUTHORITIES = new String[] { "calendar",
			"com.android.calendar" };

	// The value from AUTHORITIES appropriate for the build we're running on.
	private String mAuthority;

	private static class CalendarInfo {
		public final long id;
		public final String name;

		public CalendarInfo(long id, String name) {
			this.id = id;
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ll = (LinearLayout) inflater.inflate(R.layout.ics, null);
		sql = SqlConnector.getInstance(inflater.getContext());
		connect();
		getCal();

		return ll;
	}

	/*
	 * type = 0 --> not defined (for subtype) type = 1 --> Branches type = 2 -->
	 * Courses type = 3 --> CoursesGroups type = 4 --> Programmes type = 5 -->
	 * Semesters type = 6 --> StudyPlans type = 7 --> Teachers type = 8 -->
	 * Units type = 9 --> Walkthroughs
	 */

	public void getCal() {
		// TextView t=(TextView) findViewById(R.id.textView1);
		// t.setText("start\n");
		connect();
		Log.i("SyncCalActivity", "ok run getCal()");
		if (true) {

			String str = sql.getClassification();
			if (str.endsWith("s")) {
				str = str.substring(0, str.lastIndexOf("s"));
			}
			String URL = "https://kosapi.feld.cvut.cz/api/2/people/"
					+ sql.getUsername() + "/" + str + "/calendar.ics";
			request = new HttpGet(URL);
			// t.setText(t.getText()+request.getURI().toString()+"\n");
			/*
			 * Toast.makeText(this.getApplicationContext(), URL,
			 * Toast.LENGTH_SHORT).show();
			 */
			Log.i("Sync",
					"start downloading() from "
							+ "https://kosapi.feld.cvut.cz/api/2/people/"
							+ sql.getUsername() + "/student/calendar.ics");
			InputStream data = null;
			Reader r = null;
			int id = chosenCalendar;
			try {
				if (android.os.Build.VERSION.SDK_INT > 9) {
					StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
							.permitAll().build();
					StrictMode.setThreadPolicy(policy);
				}
				HttpResponse response = client.execute(request);
				data = response.getEntity().getContent();
				r = new InputStreamReader(data);
				char[] buf = new char[1024];
				StringBuffer fileData = new StringBuffer(1000);
				int numRead = 0;

				while ((numRead = r.read(buf)) != -1) {
					String readData = String.valueOf(buf, 0, numRead);
					// t.setText(t.getText()+readData+"\n");
					// Log.i("Sync",readData +" numRead: " +
					// Integer.toString(numRead));
					fileData.append(readData);
					buf = new char[1024];
				}
				r.close();
				data.close();

				StringReader myStringReader = new StringReader(
						fileData.toString());
				calendar = myStringReader.toString();
				if (calendar == null) {
					return;
				}
				//test
				calendar = fileData.toString();
				// System.err.println(data);

				// TODO: show a UI with the calendar-choice spinner if the user
				// has more
				// than one writable calendar?
				populateCalendarSpinner();

				ll.findViewById(R.id.cancel_button).setOnClickListener(
						new View.OnClickListener() {
							public void onClick(View v) {
								// FINISH!!!
							}
						});
				// TODO: summarize the events and offer "Add All", "Add & Edit",
				// and
				// "Cancel"?
				ll.findViewById(R.id.import_button).setOnClickListener(
						new View.OnClickListener() {
							public void onClick(View v) {
								// TODO: get and parse the data off the UI
								// thread.
								parseCalendar(calendar, false);
							}
						});
				reminder = (Spinner) ll.findViewById(R.id.spinner1);
				reminder.setClickable(false);
				reminder.setEnabled(false);
				reminder_checkbox = (CheckBox) ll.findViewById(R.id.checkBox1);
				reminder_checkbox.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if (reminder_checkbox.isChecked()) {
							reminder.setClickable(true);
							reminder.setEnabled(true);
						} else {
							reminder.setClickable(false);
							reminder.setEnabled(false);
						}
					}

				});

			} catch (ClientProtocolException e) {
				Log.e(TAG, "todo error handlig", e);
			} catch (IOException e) {
				Log.e(TAG, "todo error handlig", e);
			} catch (NullPointerException e) {
				Log.e(TAG, "todo error handlig", e);
			} catch (Exception e) {
				Log.e(TAG, "EXCEPTION!!!!", e);
			}

			finally {
				try {
					r.close();
					data.close();
				} catch (IOException e) {
					Log.e(TAG, "todo error handlig", e);
				}
			}
		}
	}

	private void connect() {
		// TODO Auto-generated method stub
		Intent myIntent = new Intent(getSherlockActivity().getBaseContext(),
				AuthenticatorActivity.class);
		startActivity(myIntent);
		String usr = myIntent.getStringExtra("mUsername");
		Log.d("GetKosData - connect", "usr:" + usr);
		client = new KosHttpClient(Constants.getUsername(),
				Constants.getPassword());
	}

	private void populateCalendarSpinner() {
		mCalendarsSpinner = (Spinner) ll.findViewById(R.id.calendars);

		String[] columns = new String[] { _ID_COLUMN, DISPLAY_NAME_COLUMN,
				SELECTED_COLUMN, ACCESS_LEVEL_COLUMN };
		String query = SELECTED_COLUMN + "=1 AND " + ACCESS_LEVEL_COLUMN + ">="
				+ CONTRIBUTOR_ACCESS;
		// When Calendar was unbundled for Froyo, the authority changed. To
		// support old and new builds, we need to try both.
		Cursor c = null;
		// for (String authority : AUTHORITIES) {
		// Uri uri = Uri.parse(String.format("content://%s/calendars",
		// authority));
		// c = getContentResolver().query(uri, columns, query, null, null /*
		// sort order */);
		// if (c != null) {
		// mAuthority = authority;
		// break;
		// }
		// }
		Uri uri = Calendars.CONTENT_URI;
		String selection = "((" + Calendars.ACCOUNT_NAME + " = ?) AND ("
				+ Calendars.ACCOUNT_TYPE + " = ?) AND ("
				+ Calendars.OWNER_ACCOUNT + " = ?))";
		String[] selectionArgs = new String[] { "sampleuser@gmail.com",
				"com.google", "sampleuser@gmail.com" };
		c = getSherlockActivity().getContentResolver().query(uri,
				EVENT_PROJECTION, null, null, null);

		ArrayList<CalendarInfo> items = new ArrayList<CalendarInfo>();
		try {
			// TODO: write a custom adapter that wraps the cursor?
			int idColumn = c.getColumnIndex(_ID_COLUMN);
			int nameColumn = c.getColumnIndex(DISPLAY_NAME_COLUMN);
			while (c.moveToNext()) {
				long id = c.getLong(PROJECTION_ID_INDEX);
				String name = c.getString(PROJECTION_DISPLAY_NAME_INDEX);
				items.add(new CalendarInfo(id, name));
			}
		} finally {
			c.deactivate();
		}

		ArrayAdapter<CalendarInfo> adapter = new ArrayAdapter<CalendarInfo>(
				getSherlockActivity(), android.R.layout.simple_spinner_item,
				items);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mCalendarsSpinner.setAdapter(adapter);
	}

	private int parseCalendar(String data, boolean dryRun) {
		try {
			mCalendar = ICalendar.parseCalendar(data);
		} catch (ICalendar.FormatException fe) {
			toastAndLog("Couldn't parse calendar data", fe);
			return 0;
		}

		final IcsTimeZones timeZones = new IcsTimeZones(mCalendar);

		int eventCount = 0;
		int importedEventCount = 0;
		if (mCalendar.getComponents() != null) {
			for (ICalendar.Component component : mCalendar.getComponents()) {
				if (component.getName().equals(ICalendar.Component.VEVENT)) {
					++eventCount;
					if (!dryRun && insertVEvent(component, timeZones)) {
						++importedEventCount;
					}
				}
			}
		}
		if (eventCount == 0) {
			toastAndLog("No events found in calendar data");
		}
		if (importedEventCount != eventCount) {
			toastAndLog("Not all events were added");
		}
		return eventCount;
	}

	private boolean insertVEvent(ICalendar.Component event,
			IcsTimeZones timeZones) {
		ContentValues values = new ContentValues();

		// title
		String title = IcsUtils.getFirstPropertyText(event, "SUMMARY");
		while (title.contains(":")) {
			title = title.substring(title.indexOf(":") + 1);
		}
		if (TextUtils.isEmpty(title)) {
			toastAndLog("Can't import an untitled event");
			return false;
		}
		values.put(TITLE, title);

		// status
		values.put(STATUS, STATUS_CONFIRMED);

		// description
		String description = IcsUtils
				.getFirstPropertyText(event, "DESCRIPTION");
		if (!TextUtils.isEmpty(description)) {
			values.put(DESCRIPTION, description);
		}

		String tzid = IcsUtils.getFirstPropertyText(event, "TZID");
		if (!TextUtils.isEmpty(tzid)) {
			values.put(EVENT_TIMEZONE, tzid);
		}

		// where
		String where = IcsUtils.getFirstPropertyText(event, "LOCATION");
		while (where.contains("\"")) {
			where = where.substring(where.indexOf("\"") + 2);
		}
		if (!TextUtils.isEmpty(where)) {
			values.put(EVENT_LOCATION, where);
		}

		// Which calendar should we insert into?
		CalendarInfo calInfo = (CalendarInfo) mCalendarsSpinner
				.getSelectedItem();
		long calendarId = calInfo.id;
		values.put(CALENDAR_ID, calendarId);

		// The time span, defined by dtStart...
		IcsTime dtStart = timeZones.parseTimeProperty(event, "DTSTART");
		if (dtStart == null) {
			toastAndLog("Can't import events that don't have a start time");
			return false;
		}
		Date dat = new Date(0);
		dat.setHours(2);
		long dtstart = dtStart.utcMillis - dat.getTime();
		Log.i("IcsBot-dtstart", Long.toString(dtstart));
		values.put(DTSTART, dtstart);
		if (dtStart.allDay) {
			values.put(ALL_DAY, 1);
		}

		// rrule
		String rrule = IcsUtils.getFirstPropertyText(event, "RRULE");
		rrule = rrule.concat("Z");
		if (!TextUtils.isEmpty(rrule)) {
			values.put("rrule", rrule);
		}

		// ...and either dtEnd or duration.
		IcsTime dtEnd = timeZones.parseTimeProperty(event, "DTEND");
		// if (dtEnd != null) {
		dat.setHours(1);
		if (dtEnd != null) {
			if (rrule != null) {
				long duration = dtEnd.utcMillis - dat.getTime()
						- dtStart.utcMillis;
				DateTime dt = new DateTime(duration); // current time
				int hours = dt.getHours() - 1;
				int minutes = dt.getMinutes();
				String durat = "PT";
				if (hours > 0) {
					durat = durat.concat(Integer.toString(hours) + "H");
				}
				if (minutes > 0) {
					durat = durat.concat(Integer.toString(minutes) + "M");
				}
				values.put(DURATION, durat);
			} else {
				values.put(DTEND, dtEnd.utcMillis);
			}
		} else {
			// look for a duration
			ICalendar.Property durationProp = event
					.getFirstProperty("DURATION");
			if (durationProp != null) {
				String duration = durationProp.getValue();
				if (TextUtils.isEmpty(duration)) {
					toastAndLog("Can't import events that have neither an end time nor a duration");
					return false;
				}
				// TODO: check that it is valid?
				values.put(DURATION, duration);
			}
		}

		/*
		 * if (!RecurrenceSet.populateContentValues(event, values)) { return
		 * null; }
		 */

		// FIXME: use the VALARM data to set a reminder.

		// Insert the event...
		Uri uri = getSherlockActivity().getContentResolver().insert(
				getCalendarURI(true), values);
		if (uri == null) {
			toastAndLog("Couldn't import event '" + title + "'");
			return false;
		}

		if (reminder_checkbox.isChecked()) {
			String reminderUriString = "content://com.android.calendar/reminders";

			ContentValues reminderValues = new ContentValues();

			reminderValues.put("event_id", uri.getLastPathSegment());
			reminderValues.put(
					"minutes",
					Integer.valueOf(reminder
							.getSelectedItem()
							.toString()
							.substring(
									0,
									reminder.getSelectedItem().toString()
											.indexOf(" ")))); // Default value
																// of the
			// system. Minutes is a
			// integer
			reminderValues.put("method", 1); // Alert Methods: Default(0),
												// Alert(1), Email(2),
												// SMS(3)

			Uri reminderUri = getSherlockActivity().getContentResolver()
					.insert(Uri.parse(reminderUriString), reminderValues);
		}
		return true;
	}

	private Uri getCalendarURI(boolean eventUri) {
		Uri calendarURI = null;

		if (android.os.Build.VERSION.SDK_INT <= 7) {
			calendarURI = (eventUri) ? Uri.parse("content://calendar/events")
					: Uri.parse("content://calendar/calendars");
		} else {
			calendarURI = (eventUri) ? Uri
					.parse("content://com.android.calendar/events") : Uri
					.parse("content://com.android.calendar/calendars");
		}
		return calendarURI;
	}

	private void toastAndLog(String message, Throwable th) {
		Toast.makeText(getSherlockActivity(), message + ": " + th.getMessage(),
				Toast.LENGTH_SHORT).show();
		Log.w(TAG, message, th);
	}

	private void toastAndLog(String message) {
		Toast.makeText(getSherlockActivity(), message, Toast.LENGTH_SHORT)
				.show();
		Log.w(TAG, message);
	}

}
