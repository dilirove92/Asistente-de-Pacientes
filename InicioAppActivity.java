package com.Notifications.patientssassistant;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;


public class InicioAppActivity extends Activity {
	
	private final int WELCOME=25;
	private TextView linea_ayuda;
	private ProgressBar mProgressBar;
	private int progreso=0;
	private int paso = 500;	
	
	public InicioAppActivity() {super();}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inicio_app);
        mProgressBar=(ProgressBar)findViewById(R.id.ProBarInicio);
        linea_ayuda=(TextView)findViewById(R.id.txtCargandoDatos);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		cuentaAtras(3000);
	}
	
	private void cuentaAtras(long milisegundos) {
		CountDownTimer mCountDownTimer;
		mProgressBar.setMax((int)milisegundos);
		mProgressBar.setProgress(paso);
		
		mCountDownTimer=new CountDownTimer(milisegundos, paso) {
	        @Override
	        public void onTick(long millisUntilFinished) {
	            Log.v("Log_tag", "Tick of Progress"+ progreso+ millisUntilFinished);
	            progreso+=paso; 
	            mProgressBar.setProgress(progreso);
	            linea_ayuda.setText(R.string.Iniciando);
	        }
	        @Override
	        public void onFinish() {
	        	progreso+=paso;
	            mProgressBar.setProgress(progreso);
	            Intent i = new Intent ("com.Notifications.patientssassistant.INICIAR_SESION");
				startActivityForResult(i, WELCOME);
	        }
		};		
		mCountDownTimer.start();		   
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode==WELCOME)
			finish();   
		else
			super.onActivityResult(requestCode, resultCode, data);
	}

	
}