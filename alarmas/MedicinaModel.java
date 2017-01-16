package com.Notifications.patientssassistant.alarmas;


public class MedicinaModel {
	
	public static final int SUNDAY = 0;
	public static final int MONDAY = 1;
	public static final int TUESDAY = 2;
	public static final int WEDNESDAY = 3;
	public static final int THURSDAY = 4;
	public static final int FRIDAY = 5;
	public static final int SATURDAY = 6;
		
	public long Id = -1;
	public long IdUser;
	public String User;
	public String Medicine;
	public String Reason;
	public String Doses;
	public int Number;	
	public int TimeHour;
	public int TimeMinute;
	private boolean RepeatingDays[];
	public String AlarmTone;
	public boolean IsEnabled;
	
	public MedicinaModel() {
		RepeatingDays = new boolean[7];
	}
	
	public void setRepeatingDay(int dayOfWeek, boolean value) {
		RepeatingDays[dayOfWeek] = value;
	}
	
	public boolean getRepeatingDay(int dayOfWeek) {
		return RepeatingDays[dayOfWeek];
	}		

	
}