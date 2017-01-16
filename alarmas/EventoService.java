package com.Notifications.patientssassistant.alarmas;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


public class EventoService extends Service {

	public static String TAG = EventoService.class.getSimpleName();
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		Intent alarmIntent = new Intent(getBaseContext(), EventoScreen.class);
		alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		alarmIntent.putExtras(intent);
		getApplication().startActivity(alarmIntent);
		
		EventoManagerHelper.setAlarms(EventoService.this);
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	
}