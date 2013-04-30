package kos.cvut.getdata;

import android.content.Context;

import com.actionbarsherlock.app.SherlockActivity;
import com.kos.R;
import com.kos.cvut.Timetable;

public class TimetableSlot{
	private int day = 0;
	private int duration = 0;
	private int firstHour = 0;
	private String parity = "";
	private String room = "";

	public TimetableSlot() {
	}

	public TimetableSlot(int day, int duration, int firstHour, String parity,
			String room) {
		this.day = day;
		this.duration = duration;
		this.firstHour = firstHour;
		this.parity = parity;
		this.room = room;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getFirstHour() {
		return firstHour;
	}

	public void setFirstHour(int firstHour) {
		this.firstHour = firstHour;
	}

	public String getParity() {
		return parity;
	}

	public void setParity(String parity) {
		this.parity = parity;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String toString(Context ctx) {
		Timetable time = new Timetable();
		if (day > 0 && firstHour > 0 && duration > 0) {
			return getDay(day,ctx) + " " + getHour(firstHour, duration);
		}
		return "";
	}

	public String getDay(int day, Context ctx) {
		switch (day) {
		case 1:
			return ctx.getResources().getString(R.string.monday);
		case 2:
			return ctx.getApplicationContext().getResources().getString(R.string.tuesday);
		case 3:
			return ctx.getResources().getString(R.string.wednesday);
		case 4:
			return ctx.getResources().getString(R.string.thursday);
		case 5:
			return ctx.getResources().getString(R.string.friday);
		case 6:
			return ctx.getResources().getString(R.string.saturday);
		case 7:
			return ctx.getResources().getString(R.string.sunday);
		default:
			break;
		}
		return null;
	}
	
	public String getHour(int firstHour, int duration) {
		String hour = "";
		int start_hour = 0;
		int start_minutes = 0;
		
		switch (firstHour) {
		case 1:
			hour = "7:30";
			start_hour = 7;
			start_minutes = 30;
			break;
		case 2:
			hour = "8:15";
			start_hour = 8;
			start_minutes = 15;
			break;
		case 3:
			hour = "9:15";
			start_hour = 9;
			start_minutes = 15;
			break;
		case 4:
			hour = "10:00";
			start_hour = 10;
			start_minutes = 0;
			break;
		case 5:
			hour = "11:00";
			start_hour = 11;
			start_minutes = 0;
			break;
		case 6:
			hour = "11:45";
			start_hour = 11;
			start_minutes = 45;
			break;
		case 7:
			hour = "12:45";
			start_hour = 12;
			start_minutes = 45;
			break;
		case 8:
			hour = "13:30";
			start_hour = 13;
			start_minutes = 30;
			break;
		case 9:
			hour = "14:30";
			start_hour = 14;
			start_minutes = 30;
			break;
		case 10:
			hour = "15:15";
			start_hour = 15;
			start_minutes = 15;
			break;
		case 11:
			hour = "16:15";
			start_hour = 16;
			start_minutes = 15;
			break;
		case 12:
			hour = "17:00";
			start_hour = 17;
			start_minutes = 00;
			break;
		case 13:
			hour = "18:00";
			start_hour = 18;
			start_minutes = 00;
			break;
		case 14:
			hour = "18:45";
			start_hour = 18;
			start_minutes = 45;
			break;
		case 15:
			hour = "20:30";
			start_hour = 20;
			start_minutes = 30;
			break;
		default:
			break;
		}
		hour = hour.concat(" - ");
		int hours = 0;
		int minutes = 0;
		if (duration <= 2) {
			if (firstHour % 2 == 0) {
				if(duration < 2){
					minutes = duration * 45;
				}else{
					minutes = duration * 45 + 15;
				}
			}else{
				minutes = duration * 45;
			}
		} else {
			if (firstHour % 2 == 0) {
				minutes = duration * 45 + 15 * (firstHour/2);
			}else{
				minutes = (int) (duration * 45 + 15 * (Math.round(firstHour/2) - 0.5));
			}
		}
		while ((start_minutes+minutes) > 60){
			minutes -= 60;
			hours++;
		}
		hour = hour.concat(String.valueOf(start_hour+hours)+":"+String.valueOf(start_minutes+minutes));
		return hour;
	}
}
