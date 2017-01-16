package com.Notifications.patientssassistant.alarmas;


import com.Notifications.patientssassistant.R;
import com.Notifications.patientssassistant.adapters.AdapterListaEventosCuidadores;
import com.Notifications.patientssassistant.adapters.AdapterListaEventosPacientes;
import com.Notifications.patientssassistant.adapters.AdapterListaRutinasCuidadores;
import com.Notifications.patientssassistant.adapters.AdapterListaRutinasPacientes;
import com.Notifications.patientssassistant.tables.*;
import com.orm.query.Condition;
import com.orm.query.Select;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class RutinaScreen extends Activity {
	
	public final String TAG = this.getClass().getSimpleName();

	private WakeLock mWakeLock;
	private MediaPlayer mPlayer;
	private int resultado=0;
	private Bitmap bitmap;
	private static String tipoUser;
	private static final int WAKELOCK_TIMEOUT = 60 * 1000;
	private static final int WAKELOCK_TIMEOUT2 = 30 * 1000;
	private static boolean playing = false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.screen_rutina);

		playing = false;
		Long idUser = getIntent().getLongExtra(RutinaManagerHelper.IDUSER, 0);
		tipoUser = getIntent().getStringExtra(RutinaManagerHelper.TIPOUSER);
		String user = getIntent().getStringExtra(RutinaManagerHelper.USER);
		String name = getIntent().getStringExtra(RutinaManagerHelper.NAME);
		int timeHour = getIntent().getIntExtra(RutinaManagerHelper.TIME_HOUR, 0);
		int timeMinute = getIntent().getIntExtra(RutinaManagerHelper.TIME_MINUTE, 0);
		String tone = getIntent().getStringExtra(RutinaManagerHelper.TONE);

		ImageView ImgUser = (ImageView)findViewById(R.id.imageUser);
		TextView TxtRutina = (TextView)findViewById(R.id.txtRutina);
		TextView TxtTime = (TextView)findViewById(R.id.txtTime);		
		TextView TxtUser = (TextView)findViewById(R.id.txtUser);

		if (tipoUser.equals("P")) {
			TblPacientes regPac = Select.from(TblPacientes.class).where(Condition.prop("id_paciente").eq(idUser),
																		Condition.prop("Eliminado").eq(0)).first();
			if (regPac.getFotoP().equals("")) {
				bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.user_foto);
			}else{
				byte[] b = Base64.decode(regPac.getFotoP(), Base64.DEFAULT);
				bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
			}
		}else{
			if (tipoUser.equals("C")) {
				TblCuidador regCui = Select.from(TblCuidador.class).where(Condition.prop("id_cuidador").eq(idUser),
																		  Condition.prop("Eliminado").eq(0)).first();
				if (regCui.getFotoC().equals("")) {
					bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.user_foto);
				}else{
					byte[] b = Base64.decode(regCui.getFotoC(), Base64.DEFAULT);
					bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
				}
			}
		}

		ImgUser.setImageBitmap(bitmap);
		TxtRutina.setText(name);
		TxtUser.setText(user);
		TxtTime.setText(String.format("%02d : %02d", timeHour, timeMinute));
				
		Button dismissButton = (Button) findViewById(R.id.btnOk);
		dismissButton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View view) {
				mPlayer.stop();
				playing = false;
				finish();
			}
		});

		//Play alarm tone
		mPlayer = new MediaPlayer();
		try {
			if (tone != null && !tone.equals("")) {
				resultado=TonosClass.BuscarIdTono(tone);
				mPlayer=MediaPlayer.create(RutinaScreen.this, resultado);
				mPlayer.setLooping(true);
				mPlayer.start();
				playing = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Intent intent=null;
		if (tipoUser.equals("C")){intent = new Intent(RutinaScreen.this, AdapterListaRutinasCuidadores.class);}
		if (tipoUser.equals("P")){intent = new Intent(RutinaScreen.this, AdapterListaRutinasPacientes.class);}
		intent.putExtra("user", user);
		intent.putExtra("rutina", name);
		intent.putExtra("hora", timeHour);
		intent.putExtra("minutos", timeMinute);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivity(RutinaScreen.this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
		Uri otherUri = Uri.withAppendedPath(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, tone);

		long[] vibrate = {0,100,200,300};
		String message=user + "-" + timeHour + ":" + timeMinute;
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.ic_logo)
				.setLargeIcon(bitmap)
				.setContentTitle(name + " (Rutina)")
				.setStyle(new NotificationCompat.BigTextStyle().bigText(message))
				.setContentText(message)
				.setAutoCancel(true)
				.setSound(otherUri)
				.setVibrate(vibrate)
				.setContentIntent(pendingIntent);

		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(0, notificationBuilder.build());

		//Ensure wakelock release
		Runnable releaseWakelock = new Runnable() {
			@Override
			public void run() {
				getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
				getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
				getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
				getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

				if (mWakeLock != null && mWakeLock.isHeld()) {
					mWakeLock.release();
				}
			}
		};

		Runnable temporizador = new Runnable() {
			@Override
			public void run() {
				mPlayer.stop();
				playing = false;
				finish();
			}
		};

		new Handler().postDelayed(releaseWakelock, WAKELOCK_TIMEOUT);
		new Handler().postDelayed(temporizador, WAKELOCK_TIMEOUT2);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();

		// Set the window to keep screen on
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

		// Acquire wakelock
		PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
		if (mWakeLock == null) {
			mWakeLock = pm.newWakeLock((PowerManager.FULL_WAKE_LOCK | PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), TAG);
		}

		if (!mWakeLock.isHeld()) {
			mWakeLock.acquire();
			Log.i(TAG, "Wakelock aquired!!");
		}

	}

	@Override
	protected void onPause() {
		super.onPause();

		if (mWakeLock != null && mWakeLock.isHeld()) {
			mWakeLock.release();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();

		//if (playing) {
		//try {
			mPlayer.stop();
			playing = false;
			finish();
		//} catch (Exception e) {e.printStackTrace();}
		//}
	}

	
}