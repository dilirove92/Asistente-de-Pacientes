package com.Notifications.patientssassistant.alarmas;


import android.provider.BaseColumns;


public final class EventoContract {
	
	public EventoContract() {}
	
	public static abstract class Alarm implements BaseColumns {
		public static final String TABLE_NAME = "Eventos";
		public static final String COLUMN_NAME_ALARM_YEAR = "Year";
		public static final String COLUMN_NAME_ALARM_MONTH = "Month";
		public static final String COLUMN_NAME_ALARM_DAY = "Day";
		public static final String COLUMN_NAME_ALARM_HOUR = "Hour";
		public static final String COLUMN_NAME_ALARM_MINUTES = "Minutes";
		public static final String COLUMN_NAME_ALARM_IDUSER = "Iduser";
		public static final String COLUMN_NAME_ALARM_TIPOUSER = "Tipouser";
		public static final String COLUMN_NAME_ALARM_USER = "User";
		public static final String COLUMN_NAME_ALARM_NAME = "Name";
		public static final String COLUMN_NAME_ALARM_DATE = "Date";
		public static final String COLUMN_NAME_ALARM_LOCATION = "Location";
		public static final String COLUMN_NAME_ALARM_DESCRIPTION = "Description";		
		public static final String COLUMN_NAME_ALARM_TONE = "Tone";
		public static final String COLUMN_NAME_ALARM_ENABLED = "Enabled";
	}	

	
}