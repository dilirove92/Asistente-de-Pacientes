package com.Notifications.patientssassistant.alarmas;


import java.util.Calendar;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class EventoManagerHelper extends BroadcastReceiver {

	public static final String ID = "Id";
	public static final String YEAR = "Year";	
	public static final String MONTH = "Month";	
	public static final String DAY = "Day";	
	public static final String HOUR = "Hour";	
	public static final String MINUTES = "Minutes";
	public static final String IDUSER = "IdUser";
	public static final String TIPOUSER = "TipoUser";
	public static final String USER = "User";
	public static final String NAME = "Name";
	public static final String DATE = "Date";
	public static final String LOCATION = "Location";
	public static final String DESCRIPTION = "Description";	
	public static final String TONE = "AlarmTone";
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		setAlarms(context);
	}
	
	public static void setAlarms(Context context) {
		cancelAlarms(context);
		
		EventoDBHelper dbHelper = new EventoDBHelper(context);

		List<EventoModel> alarms =  dbHelper.getAlarms();
		
		for (EventoModel alarm : alarms) {
			if (alarm.IsEnabled) {

				PendingIntent pIntent = createPendingIntent(context, alarm);

				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.YEAR, alarm.Year);
				calendar.set(Calendar.MONTH, alarm.Month);
				calendar.set(Calendar.DAY_OF_MONTH, alarm.Day);
				calendar.set(Calendar.HOUR_OF_DAY, alarm.Hour);
				calendar.set(Calendar.MINUTE, alarm.Minutes);
				calendar.set(Calendar.SECOND, 00);
				
				int nowYear = Calendar.getInstance().get(Calendar.YEAR);
				int nowMonth = Calendar.getInstance().get(Calendar.MONTH);
				int nowDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);				
				int nowHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);				
				int nowMinute = Calendar.getInstance().get(Calendar.MINUTE);
								
				if (alarm.Year >= nowYear && alarm.Month >= nowMonth && alarm.Day >= nowDay && 
					alarm.Hour >= nowHour && alarm.Minutes > nowMinute) 
				{
					setAlarm(context, calendar, pIntent);					
					break;
				}						
			}
		}
	}
	
	@SuppressLint("NewApi")
	private static void setAlarm(Context context, Calendar calendar, PendingIntent pIntent) {
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
			alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
		} else {
			alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
		}
	}
	
	public static void cancelAlarms(Context context) {
		EventoDBHelper dbHelper = new EventoDBHelper(context);
		
		List<EventoModel> alarms =  dbHelper.getAlarms();
		
 		if (alarms != null) {
			for (EventoModel alarm : alarms) {
				if (alarm.IsEnabled) {
					PendingIntent pIntent = createPendingIntent(context, alarm);
	
					AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
					alarmManager.cancel(pIntent);
					pIntent.cancel();
				}
			}
 		}
	}

	private static PendingIntent createPendingIntent(Context context, EventoModel model) {
		Intent intent = new Intent(context, EventoService.class);
		intent.putExtra(ID, model.Id);
		intent.putExtra(YEAR, model.Year);
		intent.putExtra(MONTH, model.Month);
		intent.putExtra(DAY, model.Day);
		intent.putExtra(HOUR, model.Hour);
		intent.putExtra(MINUTES, model.Minutes);
		intent.putExtra(IDUSER, model.IdUser);
		intent.putExtra(TIPOUSER, model.TipoUser);
		intent.putExtra(USER, model.User);
		intent.putExtra(NAME, model.Name);
		intent.putExtra(DATE, model.Date);
		intent.putExtra(LOCATION, model.Location);
		intent.putExtra(DESCRIPTION, model.Description);
		intent.putExtra(TONE, model.AlarmTone.toString());

		return PendingIntent.getService(context, (int) model.Id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}
	
	
}