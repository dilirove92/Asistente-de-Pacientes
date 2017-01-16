package com.Notifications.patientssassistant.alarmas;


import android.provider.BaseColumns;


public final class RutinaContract {
	
	public RutinaContract() {}
	
	public static abstract class Alarm implements BaseColumns {
		public static final String TABLE_NAME = "Rutinas";
		public static final String COLUMN_NAME_ALARM_IDUSER = "Iduser";
		public static final String COLUMN_NAME_ALARM_TIPOUSER = "Tipouser";
		public static final String COLUMN_NAME_ALARM_USER = "User";
		public static final String COLUMN_NAME_ALARM_NAME = "Name";
		public static final String COLUMN_NAME_ALARM_TIME_HOUR = "Hour";
		public static final String COLUMN_NAME_ALARM_TIME_MINUTE = "Minute";
		public static final String COLUMN_NAME_ALARM_REPEAT_DAYS = "Days";		
		public static final String COLUMN_NAME_ALARM_TONE = "Tone";
		public static final String COLUMN_NAME_ALARM_ENABLED = "Enabled";
	}
	
	
}