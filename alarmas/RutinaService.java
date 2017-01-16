package com.Notifications.patientssassistant.alarmas;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


public class RutinaService extends Service {

	public static String TAG = RutinaService.class.getSimpleName();
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		Intent alarmIntent = new Intent(getBaseContext(), RutinaScreen.class);
		alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		alarmIntent.putExtras(intent);
		getApplication().startActivity(alarmIntent);
		
		RutinaManagerHelper.setAlarms(this);
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	
}