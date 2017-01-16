package com.Notifications.patientssassistant.alarmas;


import com.Notifications.patientssassistant.IniciarSesionActivity;
import com.Notifications.patientssassistant.MyGcmListenerService;
import com.Notifications.patientssassistant.R;
import com.Notifications.patientssassistant.adapters.AdapterListaMedicinasHorarios;
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


public class MedicinaScreen extends Activity {
	
	public final String TAG = this.getClass().getSimpleName();

	private WakeLock mWakeLock;
	private MediaPlayer mPlayer;
	private int resultado=0;
	private Bitmap bitmap;
	private static final int WAKELOCK_TIMEOUT = 60 * 1000;
	private static final int WAKELOCK_TIMEOUT2 = 30 * 1000;
	private static boolean playing = false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);				
		this.setContentView(R.layout.screen_medicina);

		playing = false;
		Long idUser = getIntent().getLongExtra(MedicinaManagerHelper.IDUSER, 0);
		String user = getIntent().getStringExtra(MedicinaManagerHelper.USER);
		int timeHour = getIntent().getIntExtra(MedicinaManagerHelper.TIME_HOUR, 0);
		int timeMinute = getIntent().getIntExtra(MedicinaManagerHelper.TIME_MINUTE, 0);
		String medicine = getIntent().getStringExtra(MedicinaManagerHelper.MEDICINE);
		String doses = getIntent().getStringExtra(MedicinaManagerHelper.DOSES);		
		String tone = getIntent().getStringExtra(MedicinaManagerHelper.TONE);

		ImageView ImgUser = (ImageView)findViewById(R.id.imageUser);
		TextView TxtUser = (TextView)findViewById(R.id.txtUser);
		TextView TxtTime = (TextView)findViewById(R.id.txtTime);
		TextView TxtMedicamento = (TextView)findViewById(R.id.txtMedicamento);
		TextView TxtDosis = (TextView)findViewById(R.id.txtDosis);

		TblPacientes regPac = Select.from(TblPacientes.class).where(Condition.prop("id_paciente").eq(idUser),
																	Condition.prop("Eliminado").eq(0)).first();
		if (regPac.getFotoP().equals("")) {
			bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.user_foto);
		}else{
			byte[] b = Base64.decode(regPac.getFotoP(), Base64.DEFAULT);
			bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
		}

		ImgUser.setImageBitmap(bitmap);
		TxtUser.setText(user);
		TxtTime.setText(String.format("%02d : %02d", timeHour, timeMinute));
		TxtMedicamento.setText(medicine);
		TxtDosis.setText(doses);
						
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
				mPlayer=MediaPlayer.create(MedicinaScreen.this, resultado);
				mPlayer.setLooping(true);
				mPlayer.start();
				playing = true;
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}

		Intent intent = new Intent(MedicinaScreen.this, AdapterListaMedicinasHorarios.class);
		intent.putExtra("user", user);
		intent.putExtra("medicina", medicine);
		intent.putExtra("hora", timeHour);
		intent.putExtra("minutos", timeMinute);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivity(MedicinaScreen.this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
		Uri otherUri = Uri.withAppendedPath(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, tone);

		long[] vibrate = {0,100,200,300};
		String message=user + " - " + timeHour + ":" + timeMinute + "\n" + doses;
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.ic_logo)
				.setLargeIcon(bitmap)
				.setContentTitle(medicine + " (Medicina)")
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