package com.Notifications.patientssassistant;


import android.app.Activity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class EnviarSmsActivity extends Activity {
	
	//VARIABLES DE LOS ELEMENTOS DE LA IU
	TextView TxtPara;
	EditText EdtCelular;
	EditText EdtMensaje;
	TextView TxtNDato;
	Button BtnEnviar;
	Button BtnCancelar;
	
	//VARIABLES EXTRAS
	private String vDestinatario;
	private String vCelular;
		
		
	public EnviarSmsActivity() { super(); }

	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.enviar_sms);
		
		TxtPara = (TextView)findViewById(R.id.txtPara);
		EdtCelular = (EditText)findViewById(R.id.edtCelular);
		EdtMensaje = (EditText)findViewById(R.id.edtMensaje);
		TxtNDato = (TextView)findViewById(R.id.txtNDato);
		BtnEnviar = (Button)findViewById(R.id.btnEnviar);
		BtnCancelar = (Button)findViewById(R.id.btnCancelar);
		
		RecogerParametros();
		CargarDatos();
		BtnEnviarSms();
		BtnCancelar();
	}	
	
	public void RecogerParametros() {
		vDestinatario = getIntent().getExtras().getString("Destinatario");
		vCelular = getIntent().getExtras().getString("Celular");
	}
	
	public void CargarDatos() {
		TxtPara.setText(getString(R.string.Para)+" "+vDestinatario);
		EdtCelular.setText(vCelular);
		EdtCelular.setEnabled(false);
	}
	
	public void BtnEnviarSms() {
		BtnEnviar.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {								
				String mensaje=EdtMensaje.getText().toString().trim();
				
				if (mensaje.equals("")) {	
					TxtNDato.setText(R.string.SmsVacio);
				}else {	
					try {
						SmsManager smsManager = SmsManager.getDefault();
						smsManager.sendTextMessage(vCelular, null, mensaje, null, null);
						Toast.makeText(EnviarSmsActivity.this, getString(R.string.SmsEnviado), Toast.LENGTH_LONG).show();
						LimpiarElementos();
						finish();
					}catch (Exception ex) {
						Toast.makeText(EnviarSmsActivity.this, getString(R.string.SmsFallido), Toast.LENGTH_LONG).show();
						ex.printStackTrace();
					}	
				}
			}
		});		
	}
	
	public void BtnCancelar() {
		BtnCancelar.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				finish();				
			}
		});
	}
	
	public void LimpiarElementos() {		
		EdtMensaje.getText().clear();
		TxtNDato.setText("");
	}
	
	
}