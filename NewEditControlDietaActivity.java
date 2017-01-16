package com.Notifications.patientssassistant;


import com.Notifications.patientssassistant.dialogos.*;
import com.Notifications.patientssassistant.fragments.*;
import com.Notifications.patientssassistant.asynctask.*;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class NewEditControlDietaActivity extends Activity {
	
	//VARIABLES PARA NUEVO REGISTRO
	private Long vIdPaciente;
	private String vNombrePaciente;
	private String vFotoPaciente;	
		
	//VARIABLES PARA EDITAR REGISTRO	
	private Long vIdControlDieta;
	private String vMotivo;
	private String vAlimentosRecomendados;
	private String vAlimentosNoAdecuados;
	
	//VARIABLES QUE ALMACENAN LOS DATOS A GUARDAR
	private String campo_motivo;  
	private String campo_alimRec;  
	private String campo_alimNoAde;
	
	//VARIABLES DE LOS ELEMENTOS DE LA IU	
	ImageView ImagenFotoP;
	TextView TxtNombreP;
	EditText EdtNewMotivoDie;
	EditText EdtNewAlimentosSi;
	EditText EdtNewAlimentosNo;
	Button BtnNuevoEditar;
	
	//VARIABLES EXTRAS
	private Boolean banderaGuardar=false;
	private FragmentManager fragmentManager = getFragmentManager(); 
	
	
	public NewEditControlDietaActivity() { super(); }

	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nuevo_control_dieta); 
		
		ImagenFotoP=(ImageView)findViewById(R.id.imagenFotoP);
		TxtNombreP=(TextView)findViewById(R.id.txtNombreP);
		EdtNewMotivoDie=(EditText)findViewById(R.id.edtNewMotivoDie);
		EdtNewAlimentosSi=(EditText)findViewById(R.id.edtNewAlimentosSi);
		EdtNewAlimentosNo=(EditText)findViewById(R.id.edtNewAlimentosNo);		
		BtnNuevoEditar=(Button)findViewById(R.id.btnNewGuardar);
			
		NuevoVsEditar();    	
	}
	
	//METODO QUE SERVIRA PARA IR A NUEVO O EDITAR REGISTRO DE CONTROL DE DIETA
	public void NuevoVsEditar() {		
		Boolean opcion1 = MenPacControlDietaF.EsNuevo();		
		Boolean opcion2 = MenPacControlDietaF.EsEditar();
		
		if (opcion1.equals(true)) {
			NewEditControlDietaActivity.this.setTitle(R.string.NewDieta);
			RecogerParametrosFragMenPacCD();
			CargarFotoNombrePaciente();
			BtnNuevoCD(); 
		}
		if (opcion2.equals(true)) {
			NewEditControlDietaActivity.this.setTitle(R.string.EditDieta);
			RecogerParametrosFragMenPacCD2();
			CargarDatosEnLosElementos();
			BtnEditarCD();  	
		}		
	}
		
	//----METODOS PARA UN NUEVO REGISTRO
	
	public void RecogerParametrosFragMenPacCD() {
		vIdPaciente = getIntent().getExtras().getLong("varItemIdPaciente");
		vNombrePaciente = getIntent().getExtras().getString("varNombrePaciente");
		vFotoPaciente = getIntent().getExtras().getString("varFotoPaciente");	
	}
	
	public void CargarFotoNombrePaciente() {
		if (vFotoPaciente.equals("")) {
			ImagenFotoP.setImageResource(R.drawable.user_foto);				
		}else{
			byte[] b = Base64.decode(vFotoPaciente, Base64.DEFAULT);
			Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
			ImagenFotoP.setImageBitmap(bitmap);
		} 	    	
		TxtNombreP.setText(vNombrePaciente);
	}
			 			
	public void BtnNuevoCD(){
		BtnNuevoEditar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (estaConectado()) {
					try {
						ValidarDatos();
						if (banderaGuardar.equals(true)) {
							//GUARDAR DATOS EN LA TABLA DE CONTROL DE DIETA
							ATControlDieta dieta = new ATControlDieta();
							dieta.new InsertarControlDieta().execute("0", vIdPaciente.toString(), campo_motivo, campo_alimRec, campo_alimNoAde, "false");

							LimpiarElementos();
							Toast.makeText(NewEditControlDietaActivity.this, R.string.GuardadoR, Toast.LENGTH_SHORT).show();
							Intent data = new Intent();
							setResult(RESULT_OK, data);
							finish();
						}
					} catch (Exception ex) {
						Toast.makeText(NewEditControlDietaActivity.this, getString(R.string.ErrorGuardar) + ex.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}
	
	//----METODOS PARA EDITAR UN REGISTRO
	
	public void RecogerParametrosFragMenPacCD2() {		
		RecogerParametrosFragMenPacCD();
		vIdControlDieta = getIntent().getExtras().getLong("varIdControlDieta");
		vMotivo = getIntent().getExtras().getString("varMotivo");
		vAlimentosRecomendados = getIntent().getExtras().getString("varAlimentosRecomendados");
		vAlimentosNoAdecuados = getIntent().getExtras().getString("varAlimentosNoAdecuados");
	}
	
	public void CargarDatosEnLosElementos() {
		CargarFotoNombrePaciente();
		EdtNewMotivoDie.setText(vMotivo);
		EdtNewAlimentosSi.setText(vAlimentosRecomendados);
		EdtNewAlimentosNo.setText(vAlimentosNoAdecuados);
	}
			 			
	public void BtnEditarCD(){
		BtnNuevoEditar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (estaConectado()) {
					try {
						ValidarDatos();
						if (banderaGuardar.equals(true)) {
							//EDITANDO DATOS EN LA TABLA DE CONTROL DE DIETA
							ATControlDieta dieta = new ATControlDieta();
							dieta.new ActualizarControlDieta().execute(vIdControlDieta.toString(), vIdPaciente.toString(), campo_motivo, campo_alimRec, campo_alimNoAde, "false");

							LimpiarElementos();
							Toast.makeText(NewEditControlDietaActivity.this, R.string.EditadoR, Toast.LENGTH_SHORT).show();
							Intent data = new Intent();
							setResult(RESULT_OK, data);
							finish();
						}
					} catch (Exception ex) {
						Toast.makeText(NewEditControlDietaActivity.this, getString(R.string.ErrorEditar) + ex.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}
	
	//----METODOS TANTO PARA NUEVO O EDITAR REGISTRO
	
	public Boolean ValidarDatos() {
		campo_motivo=EdtNewMotivoDie.getText().toString().trim();  
		campo_alimRec=EdtNewAlimentosSi.getText().toString().trim();  
		campo_alimNoAde=EdtNewAlimentosNo.getText().toString().trim();
		
		if((campo_motivo.equals(""))||((campo_alimRec.equals(""))&&(campo_alimNoAde.equals("")))) 
		{
			DFNueConDie dialogo1 = new DFNueConDie();
	        dialogo1.show(fragmentManager, "tagAlerta");		    			
		}else {
			banderaGuardar=true;
		}	
		return banderaGuardar;
	}
	
	public void LimpiarElementos() {
		EdtNewMotivoDie.getText().clear();
		EdtNewAlimentosSi.getText().clear();
		EdtNewAlimentosNo.getText().clear();
	}

    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {	
    	if (keyCode == KeyEvent.KEYCODE_BACK) {
    		Intent data = new Intent();
			setResult(RESULT_OK, data);
			finish();
    		return true;    		
    	} 
		return super.onKeyDown(keyCode, event);
	}

	//VERIFICAR LA CONEXION DE INTERNET
	public Boolean estaConectado(){
		if(conectadoWifi()){
			return true;
		}else{
			if(conectadoRedMovil()){
				return true;
			}else{
				Toast.makeText(NewEditControlDietaActivity.this, "No hay Conexion a Internet", Toast.LENGTH_SHORT).show();
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