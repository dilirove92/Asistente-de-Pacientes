package com.Notifications.patientssassistant.alarmas;


import java.util.Calendar;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class RutinaManagerHelper extends BroadcastReceiver {

	public static final String ID = "Id";
	public static final String IDUSER = "IdUser";
	public static final String TIPOUSER = "TipoUser";
	public static final String USER = "User";
	public static final String NAME = "Name";
	public static final String TIME_HOUR = "TimeHour";
	public static final String TIME_MINUTE = "TimeMinute";
	public static final String TONE = "AlarmTone";
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		setAlarms(context);
	}
	
	public static void setAlarms(Context context) {
		cancelAlarms(context);
		
		RutinaDBHelper dbHelper = new RutinaDBHelper(context);

		List<RutinaModel> alarms =  dbHelper.getAlarms();
		
		for (RutinaModel alarm : alarms) {
			if (alarm.IsEnabled) {

				PendingIntent pIntent = createPendingIntent(context, alarm);

				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.HOUR_OF_DAY, alarm.TimeHour);
				calendar.set(Calendar.MINUTE, alarm.TimeMinute);
				calendar.set(Calendar.SECOND, 00);

				//Find next time to set
				final int nowDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
				final int nowHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
				final int nowMinute = Calendar.getInstance().get(Calendar.MINUTE);
				boolean alarmSet = false;
				
				//First check if it's later in the week
				for (int dayOfWeek = Calendar.SUNDAY; dayOfWeek <= Calendar.SATURDAY; ++dayOfWeek) {
					if (alarm.getRepeatingDay(dayOfWeek - 1) && dayOfWeek >= nowDay && !(dayOfWeek == nowDay && alarm.TimeHour < nowHour) &&
					   !(dayOfWeek == nowDay && alarm.TimeHour == nowHour && alarm.TimeMinute <= nowMinute)) {
						
						calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
						
						setAlarm(context, calendar, pIntent);
						alarmSet = true;
						break;
					}
				}
				
				//Else check if it's earlier in the week
				if (!alarmSet) {
					for (int dayOfWeek = Calendar.SUNDAY; dayOfWeek <= Calendar.SATURDAY; ++dayOfWeek) {
						if (alarm.getRepeatingDay(dayOfWeek - 1) && dayOfWeek <= nowDay) {
							calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
							calendar.add(Calendar.WEEK_OF_YEAR, 1);
							
							setAlarm(context, calendar, pIntent);
							alarmSet = true;
							break;
						}
					}
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
		RutinaDBHelper dbHelper = new RutinaDBHelper(context);
		
		List<RutinaModel> alarms =  dbHelper.getAlarms();
		
 		if (alarms != null) {
			for (RutinaModel alarm : alarms) {
				if (alarm.IsEnabled) {
					PendingIntent pIntent = createPendingIntent(context, alarm);
	
					AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
					alarmManager.cancel(pIntent);
				}
			}
 		}
	}

	private static PendingIntent createPendingIntent(Context context, RutinaModel model) {
		Intent intent = new Intent(context, RutinaService.class);
		intent.putExtra(ID, model.Id);
		intent.putExtra(IDUSER, model.IdUser);
		intent.putExtra(TIPOUSER, model.TipoUser);
		intent.putExtra(USER, model.User);
		intent.putExtra(NAME, model.Name);
		intent.putExtra(TIME_HOUR, model.TimeHour);
		intent.putExtra(TIME_MINUTE, model.TimeMinute);
		intent.putExtra(TONE, model.AlarmTone.toString());
		
		return PendingIntent.getService(context, (int) model.Id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}

	
}