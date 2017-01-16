package com.Notifications.patientssassistant;


import com.Notifications.patientssassistant.dialogos.*;
import com.Notifications.patientssassistant.asynctask.*;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class RecuperarContrasenaActivity extends Activity {

	//VARIABLES DE LOS ELEMENTOS DE LA IU		
	EditText EdtIngrCorreo;
	TextView TxtCorreoNV;
	Button BtnEnviar;
	Button BtnCancelar;

	//VARIABLES EXTRAS
	private FragmentManager fragmentManager = getFragmentManager();


	public RecuperarContrasenaActivity() { super(); }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recuperar_contrasena);

		EdtIngrCorreo = (EditText)findViewById(R.id.edtIngrCorreo);
		TxtCorreoNV = (TextView)findViewById(R.id.txtCorreoNV);
		BtnEnviar = (Button)findViewById(R.id.btnEnviar);
		BtnCancelar = (Button)findViewById(R.id.btnCancelar);

		BtnEnviarCorreo();
		BtnCancelar();
	}

	public void BtnEnviarCorreo() {
		BtnEnviar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(estaConectado()) {
					try {
						String campo_ingrCorreo = EdtIngrCorreo.getText().toString().trim();

						if (campo_ingrCorreo.equals("")) {
							TxtCorreoNV.setText(R.string.OlvidoCorreo);
						} else {
							boolean valid = MetodosValidacionesExtras.validateEmail(campo_ingrCorreo);
							if (valid == true) {
								ATCuidador cuidador = new ATCuidador();
								boolean buscar_email = cuidador.new ExisteEmail().execute(campo_ingrCorreo).get().booleanValue();

								if (buscar_email == false) {
									DFNoExisteCorreo dialogo1 = new DFNoExisteCorreo();
									dialogo1.show(fragmentManager, "tagAlerta");
								} else {
									ATRecuperacionContrasena objRecCon = new ATRecuperacionContrasena();
									objRecCon.new EnviarCorreo().execute(campo_ingrCorreo);

									DFInformacionAlCorreo dialogo2 = new DFInformacionAlCorreo();
									dialogo2.show(fragmentManager, "tagAlerta");
								}
							} else {
								DFCorreoNoVa dialogo3 = new DFCorreoNoVa();
								dialogo3.show(fragmentManager, "tagAlerta");
							}
						}
					} catch (Exception e) {
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
		EdtIngrCorreo.getText().clear();
		TxtCorreoNV.setText("");
	}

	//VERIFICAR LA CONEXION DE INTERNET
	public Boolean estaConectado(){
		if(conectadoWifi()){
			return true;
		}else{
			if(conectadoRedMovil()){
				return true;
			}else{
				Toast.makeText(RecuperarContrasenaActivity.this, "No hay Conexion a Internet", Toast.LENGTH_SHORT).show();
				return false;
			}
		}
	}

	//VERIFICAR CONEXION POR WIFI
	protected Boolean conectadoWifi(){
		ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (info != null) { if (info.isConnected()) { return true;} }
		}
		return false;
	}

	//VERIFICAR CONEXION POR DATOS MOVILES
	protected Boolean conectadoRedMovil(){
		ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (info != null) { if (info.isConnected()) { return true; } }
		}
		return false;
	}
}