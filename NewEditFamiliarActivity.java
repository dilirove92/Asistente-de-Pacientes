package com.Notifications.patientssassistant;


import com.Notifications.patientssassistant.adapters.*;
import com.Notifications.patientssassistant.dialogos.*;
import com.Notifications.patientssassistant.fragments.*;
import com.Notifications.patientssassistant.asynctask.*;
import java.io.ByteArrayOutputStream;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class NewEditFamiliarActivity extends Activity {
			
	//VARIABLES PARA NUEVO REGISTRO
	private Long vIdPaciente;
	private String vNombrePaciente;
	private String vFotoPaciente;
	
	//VARIABLES PARA EDITAR REGISTRO
	private Long vIdFamiliarPaciente;
	private String vNombreContacto;
	private String vCiContacto;
	private String vParentezco;
	private String vCelular;
	private String vTelefono;
	private String vDireccion;		
	private String vObservacion;
	private String vCorreo;
	private Boolean vEnviarReportes;
	private String vFotoContacto;
	       	
	//VARIABLES PARA CARGAR IMAGEN
	private static int TAKE_PICTURE = 1;
	private static int SELECT_PICTURE = 2;
	private String name = "";
	private Uri selectedImage;
	private Bitmap bitmap1, bitmap2;
	private byte[] b;
	private String campo_fotoF=""; 
	
	//VARIABLES QUE ALMACENAN LOS DATOS A GUARDAR
	private String campo_nombreC;	
	private String campo_ciC;
	private String campo_parentezco;
	private String campo_celular;
	private String campo_telefono;
	private String campo_direccion;
	private String campo_observacion;
	private String campo_correo;
	private Boolean campo_enviarReportes;
	
	//VARIABLES DE LOS ELEMENTOS DE LA IU		
	ImageView ImagenFotoP;
	TextView TxtNombreP;
	ImageView ImageFotoF;
	EditText EdtNewNombF;
	EditText EdtNewCiF;
	Spinner CmbNewParentesco;
	EditText EdtNewCelularF;
	EditText EdtNewTelefonoF;
	EditText EdtNewDireccionF;
	EditText EdtNewObservacionF;
	EditText EdtNewCorreoF;
	CheckedTextView ChckEnviarReportes;
	Button BtnNuevoEditar;
	
	//VARIABLES EXTRAS
	private Boolean banderaGuardar=false;
	private FragmentManager fragmentManager = getFragmentManager(); 
	
	
	public NewEditFamiliarActivity() { super(); }

	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nuevo_familiar);
		
		ImagenFotoP=(ImageView)findViewById(R.id.imagenFotoP);
		TxtNombreP=(TextView)findViewById(R.id.txtNombreP);
		ImageFotoF=(ImageView)findViewById(R.id.imageFotoF);
		EdtNewNombF=(EditText)findViewById(R.id.edtNewNombF);
		EdtNewCiF=(EditText)findViewById(R.id.edtNewCiF);
		CmbNewParentesco=(Spinner)findViewById(R.id.cmbNewParentesco);
		EdtNewCelularF=(EditText)findViewById(R.id.edtNewCelularF);
		EdtNewTelefonoF=(EditText)findViewById(R.id.edtNewTelefonoF);
		EdtNewDireccionF=(EditText)findViewById(R.id.edtNewDireccionF);
		EdtNewObservacionF=(EditText)findViewById(R.id.edtNewObservacionF);			
		EdtNewCorreoF=(EditText)findViewById(R.id.edtNewCorreoF);
		ChckEnviarReportes=(CheckedTextView)findViewById(R.id.chckEnviarReportes);
		BtnNuevoEditar=(Button)findViewById(R.id.btnNewGuardar);
		
		NuevoVsEditar();	
	}
	
	//METODO QUE SERVIRA PARA IR A NUEVO O EDITAR REGISTRO DE FAMILIAR
	public void NuevoVsEditar() {		
		Boolean opcion1= TabPaciente3DfF.EsNuevo();
		Boolean opcion2= TabPaciente3DfF.EsEditar();
		
		if (opcion1.equals(true)) {
			NewEditFamiliarActivity.this.setTitle(R.string.NewFamiliar);
			RecogerParametrosFragTabPacDf();
			CargarFotoNombrePaciente();
			CargarOpcSpinner();			
			BtnNuevoDF();
		}
		if (opcion2.equals(true)) {
			NewEditFamiliarActivity.this.setTitle(R.string.EditFamiliar);
			RecogerParametrosFragTabPacDf2();
			CargarDatosEnLosElementos();
			SpinnerTouch();				
			BtnEditarDF();
		}
		ActDesCheckedTextView();
		ImageBtnSubirFotoF();
	}
	
	//----METODOS PARA UN NUEVO REGISTRO
	
	public void RecogerParametrosFragTabPacDf() {
		vIdPaciente = getIntent().getExtras().getLong("varIdPaciente");
		vNombrePaciente = getIntent().getExtras().getString("varNombrePaciente");
		vFotoPaciente = getIntent().getExtras().getString("varFotoPaciente");
	}
	
	public void CargarFotoNombrePaciente() {		
		if (vFotoPaciente.equals("")) {
			ImagenFotoP.setImageResource(R.drawable.user_foto);				
		}else{
			byte[] b = Base64.decode(vFotoPaciente, Base64.DEFAULT);
			bitmap1 = BitmapFactory.decodeByteArray(b, 0, b.length);
			ImagenFotoP.setImageBitmap(bitmap1);
		}	
		TxtNombreP.setText(vNombrePaciente);		    	
	}
		
	public void BtnNuevoDF(){		
		BtnNuevoEditar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (estaConectado()) {
					try {
						ValidarDatos();
						if (banderaGuardar.equals(true)) {
							//GUARDAR DATOS EN LA TABLA DE FAMILIARES PACIENTES
							ATFamiliaresPacientes newFamPac = new ATFamiliaresPacientes();
							newFamPac.new InsertarFamiliaresPacientes().execute("0", vIdPaciente.toString(), campo_nombreC, campo_ciC, campo_parentezco, campo_celular, campo_telefono,
									campo_direccion, campo_observacion, campo_enviarReportes.toString(), campo_fotoF, campo_correo, "false");

							LimpiarElementos();
							Toast.makeText(NewEditFamiliarActivity.this, R.string.GuardadoR, Toast.LENGTH_SHORT).show();
							Intent data = new Intent();
							setResult(RESULT_OK, data);
							finish();
						}
					} catch (Exception ex) {
						Toast.makeText(NewEditFamiliarActivity.this, getString(R.string.ErrorGuardar) + ex.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}
	
	//----METODOS PARA EDITAR UN REGISTRO
	
	public void RecogerParametrosFragTabPacDf2() {		
		RecogerParametrosFragTabPacDf();
		vIdFamiliarPaciente = getIntent().getExtras().getLong("varIdFamiliarPaciente");
		vNombreContacto = getIntent().getExtras().getString("varNombreContacto");
		vCiContacto = getIntent().getExtras().getString("varCiContacto");
		vParentezco = getIntent().getExtras().getString("varParentezco");
		vCelular = getIntent().getExtras().getString("varCelular");
		vTelefono = getIntent().getExtras().getString("varTelefono");
		vDireccion = getIntent().getExtras().getString("varDireccion");		
		vObservacion = getIntent().getExtras().getString("varObservacion");
		vCorreo = getIntent().getExtras().getString("varEmail");
		vEnviarReportes = getIntent().getExtras().getBoolean("varEnviarReportes");		
		vFotoContacto = getIntent().getExtras().getString("varFotoContacto");	
		campo_fotoF=vFotoContacto;
	}
	
	public void CargarDatosEnLosElementos() {
		//DATOS DEL PACIENTE
		CargarFotoNombrePaciente();
		//DATOS DEL FAMILIAR
		if (vFotoContacto.equals("")) {
			ImageFotoF.setImageResource(R.drawable.user_foto);				
		}else{
			byte[] b = Base64.decode(vFotoContacto, Base64.DEFAULT);
			bitmap1 = BitmapFactory.decodeByteArray(b, 0, b.length);
			ImageFotoF.setImageBitmap(bitmap1);
		}	
		EdtNewNombF.setText(vNombreContacto);		
		EdtNewCiF.setText(vCiContacto);
		EdtNewCelularF.setText(vCelular);
		EdtNewTelefonoF.setText(vTelefono);
		EdtNewDireccionF.setText(vDireccion);
		EdtNewObservacionF.setText(vObservacion);
		EdtNewCorreoF.setText(vCorreo);
		ChckEnviarReportes.setChecked(vEnviarReportes);    	
		//CARGANDO EL DATO GUARDADO EN EL SPINNER 
    	String[] opcParentesco={vParentezco};
    	CmbNewParentesco.setAdapter(new AdapterSpinnerSimple(NewEditFamiliarActivity.this, R.layout.adaptador_spinner, opcParentesco));
	}
	
	public void SpinnerTouch() {			
		CmbNewParentesco.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (CmbNewParentesco.isPressed()) {
					CargarOpcSpinner();
				}
				return false;
			}
		});			
	}
	
	public void BtnEditarDF(){		
		BtnNuevoEditar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (estaConectado()) {
					try {
						ValidarDatos();
						if (banderaGuardar.equals(true)) {
							//EDITAR DATOS EN LA TABLA DE FAMILIARES PACIENTES
							ATFamiliaresPacientes newFamPac = new ATFamiliaresPacientes();
							newFamPac.new ActualizarFamiliaresPacientes().execute(vIdFamiliarPaciente.toString(), vIdPaciente.toString(), campo_nombreC, campo_ciC, campo_parentezco, campo_celular, campo_telefono,
									campo_direccion, campo_observacion, campo_enviarReportes.toString(), campo_fotoF, campo_correo, "false");

							LimpiarElementos();
							Toast.makeText(NewEditFamiliarActivity.this, R.string.EditadoR, Toast.LENGTH_SHORT).show();
							Intent data = new Intent();
							setResult(RESULT_OK, data);
							finish();
						}
					} catch (Exception ex) {
						Toast.makeText(NewEditFamiliarActivity.this, getString(R.string.ErrorEditar) + ex.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}
	
	//----METODOS TANTO PARA NUEVO O EDITAR REGISTRO
	
	public Boolean ValidarDatos() {
		campo_nombreC=EdtNewNombF.getText().toString().trim();	
		campo_ciC=EdtNewCiF.getText().toString().trim();
		campo_parentezco=CmbNewParentesco.getSelectedItem().toString();
		campo_celular=EdtNewCelularF.getText().toString().trim();
		campo_telefono=EdtNewTelefonoF.getText().toString().trim();
		campo_direccion=EdtNewDireccionF.getText().toString().trim();
		campo_observacion=EdtNewObservacionF.getText().toString().trim();
		campo_correo=EdtNewCorreoF.getText().toString().trim();
		campo_enviarReportes=ChckEnviarReportes.isChecked();
		
		if((campo_nombreC.equals(""))||((campo_celular.equals(""))&&(campo_telefono.equals("")))) {
			DFNueFam dialogo1 = new DFNueFam();
	        dialogo1.show(fragmentManager, "tagAlerta");
		}else{
			if (campo_enviarReportes.equals(true)) {
				if (!campo_correo.equals("")) {
					boolean valid = MetodosValidacionesExtras.validateEmail(campo_correo);
					if (valid==true) {
						banderaGuardar=true;
					}else {
						DFCorreoNoVa dialogo2 = new DFCorreoNoVa();
				        dialogo2.show(fragmentManager, "tagAlerta");
					}
				}else {
					DFReportesEmail dialogo3 = new DFReportesEmail();
			        dialogo3.show(fragmentManager, "tagAlerta");
				}				
			}else {
				if (!campo_correo.equals("")) {
					boolean valid = MetodosValidacionesExtras.validateEmail(campo_correo);
					if (valid==true) {
						banderaGuardar=true;
					}else {
						DFCorreoNoVa dialogo2 = new DFCorreoNoVa();
				        dialogo2.show(fragmentManager, "tagAlerta");
					}				
				}else {
					banderaGuardar=true;
				}				
			}
		}
		return banderaGuardar;
	}
	
	public void CargarOpcSpinner() {
		String[] OpcParentescoP=getResources().getStringArray(R.array.opc_parentesco);		
		CmbNewParentesco.setAdapter(new AdapterSpinnerSimple(NewEditFamiliarActivity.this, R.layout.adaptador_spinner, OpcParentescoP));		
	}
	
    public void ActDesCheckedTextView() {
    	ChckEnviarReportes.setClickable(true);
    	ChckEnviarReportes.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) { ChckEnviarReportes.toggle(); }
		}); 
    }
	
	public void ImageBtnSubirFotoF() {
		name = Environment.getExternalStorageDirectory() + "/test.jpg"; 		
		ImageFotoF.setOnClickListener(new View.OnClickListener() { 			
		Intent intent =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
		int code = TAKE_PICTURE;
				
			@Override
			public void onClick(View v) {
				intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
				code = SELECT_PICTURE;	
				startActivityForResult(intent, code);
			}			
		}); 
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
		if ((requestCode == SELECT_PICTURE)&&(resultCode == NewEditFamiliarActivity.RESULT_OK)) {
			selectedImage = data.getData();    		
			try {
				bitmap1=MetodosValidacionesExtras.decodeSampledBitmapFromResource(selectedImage, NewEditFamiliarActivity.this, 120, 120);
				ImageFotoF.setImageBitmap(MetodosValidacionesExtras.redimensionarImagenMaximo(bitmap1, 120, 120));

				bitmap2 = ((BitmapDrawable)ImageFotoF.getDrawable()).getBitmap();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, baos);  //PNG
				byte[] b = baos.toByteArray();
		    	campo_fotoF = Base64.encodeToString(b, Base64.DEFAULT);		    	
			} catch (Exception e) {}
		}
	}
	
	public void LimpiarElementos() {
		ImageFotoF.setImageResource(R.drawable.user_foto);
		EdtNewNombF.getText().clear();
		EdtNewCiF.getText().clear();		
		EdtNewCelularF.getText().clear();
		EdtNewTelefonoF.getText().clear();
		EdtNewDireccionF.getText().clear();
		EdtNewObservacionF.getText().clear();
		ChckEnviarReportes.setChecked(false);
		CargarOpcSpinner();
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
				Toast.makeText(NewEditFamiliarActivity.this, "No hay Conexion a Internet", Toast.LENGTH_SHORT).show();
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