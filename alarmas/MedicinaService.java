package com.Notifications.patientssassistant.alarmas;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


public class MedicinaService extends Service {

	public static String TAG = MedicinaService.class.getSimpleName();
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		Intent alarmIntent = new Intent(getBaseContext(), MedicinaScreen.class);
		alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		alarmIntent.putExtras(intent);
		getApplication().startActivity(alarmIntent);
		
		MedicinaManagerHelper.setAlarms(this);
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	
}