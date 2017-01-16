package com.Notifications.patientssassistant.alarmas;


import com.Notifications.patientssassistant.IniciarSesionActivity;
import com.Notifications.patientssassistant.R;
import com.Notifications.patientssassistant.adapters.AdapterListaEventosCuidadores;
import com.Notifications.patientssassistant.adapters.AdapterListaEventosPacientes;
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
import android.text.style.TtsSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class EventoScreen extends Activity {
	
	public final String TAG = this.getClass().getSimpleName();
	private WakeLock mWakeLock;
	private MediaPlayer mPlayer;
	private int resultado=0;
	private Bitmap bitmap;
	private static Long idAlarma=0L;
	private static String tipoUser;

	private static final int WAKELOCK_TIMEOUT = 60 * 1000;
	private static final int WAKELOCK_TIMEOUT2 = 30 * 1000;
	private static boolean playing = false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);				
		this.setContentView(R.layout.screen_evento);

		playing = false;
		idAlarma = getIntent().getLongExtra(EventoManagerHelper.ID, 0);
		Long idUser = getIntent().getLongExtra(EventoManagerHelper.IDUSER, 0);
		tipoUser = getIntent().getStringExtra(EventoManagerHelper.TIPOUSER);
		String user = getIntent().getStringExtra(EventoManagerHelper.USER);
		String name = getIntent().getStringExtra(EventoManagerHelper.NAME);
		String date = getIntent().getStringExtra(EventoManagerHelper.DATE);
		String location = getIntent().getStringExtra(EventoManagerHelper.LOCATION);
		String description = getIntent().getStringExtra(EventoManagerHelper.DESCRIPTION);
		String tone = getIntent().getStringExtra(EventoManagerHelper.TONE);

		ImageView ImgUser = (ImageView)findViewById(R.id.imageUser);
		TextView TxtUser = (TextView)findViewById(R.id.txtUser);
		TextView TxtEvent = (TextView)findViewById(R.id.txtEvent);
		TextView TxtDate = (TextView)findViewById(R.id.txtDate);
		TextView TxtLocation = (TextView)findViewById(R.id.txtLocation);
		TextView TxtDescription = (TextView)findViewById(R.id.txtDescription);

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
		TxtUser.setText(user);
		TxtEvent.setText(name);
		TxtDate.setText(date);
		TxtLocation.setText(location);
		TxtDescription.setText(description);
						
		Button dismissButton = (Button) findViewById(R.id.btnOk);
		dismissButton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View view) {
				FinalizarAlarmaEvento();
			}
		});

		//Play alarm tone
		mPlayer = new MediaPlayer();
		try {
			if (tone != null && !tone.equals("")) {
				resultado=TonosClass.BuscarIdTono(tone);
				mPlayer=MediaPlayer.create(EventoScreen.this, resultado);
				mPlayer.setLooping(true);
				mPlayer.start();
				playing = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Intent intent=null;
		if (tipoUser.equals("C")){intent = new Intent(EventoScreen.this, AdapterListaEventosCuidadores.class);}
		if (tipoUser.equals("P")){intent = new Intent(EventoScreen.this, AdapterListaEventosPacientes.class);}
		intent.putExtra("user", user);
		intent.putExtra("evento", name);
		intent.putExtra("fecha", date);
		intent.putExtra("lugar", location);
		intent.putExtra("descripcion", description);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivity(EventoScreen.this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
		Uri otherUri = Uri.withAppendedPath(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, tone);

		long[] vibrate = {0,100,200,300};
		String message=user+"\n"+description+"\nLugar: "+location+"\n"+date;
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.ic_logo)
				.setLargeIcon(bitmap)
				.setContentTitle(name+" (Evento)")
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
				FinalizarAlarmaEvento();
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
				FinalizarAlarmaEvento();
			//} catch (Exception e) {e.printStackTrace();}
		//}
	}

	public void FinalizarAlarmaEvento() {
		try {
			MiTblEvento miEvento=Select.from(MiTblEvento.class).where(Condition.prop("id_alarma_clock").eq(idAlarma)).first();

			if (tipoUser.equals("P")) {
				TblEventosPacientes eventoPaciente=Select.from(TblEventosPacientes.class).where(Condition.prop("id_evento_p").eq(miEvento.getIdEvento())).first();
				if (eventoPaciente!=null) {
					TblEventosPacientes eliminarEventoP=new TblEventosPacientes();
					eliminarEventoP.EliminarPorIdEventoRegTblEventosPacientes(eventoPaciente.getIdEventoP());
				}
			}else{
				if (tipoUser.equals("C")) {
					TblEventosCuidadores eventoCuidador=Select.from(TblEventosCuidadores.class).where(Condition.prop("id_evento_c").eq(miEvento.getIdEvento())).first();
					if (eventoCuidador!=null) {
						TblEventosCuidadores eliminarEventoC=new TblEventosCuidadores();
						eliminarEventoC.EliminarPorIdEventoRegTblEventosCuidadores(eventoCuidador.getIdEventoC());
					}
				}
			}

			mPlayer.stop();
			playing = false;
			finish();
		}catch (Exception e) { }
	}


}