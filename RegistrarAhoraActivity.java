package com.Notifications.patientssassistant;


import com.Notifications.patientssassistant.adapters.*;
import com.Notifications.patientssassistant.dialogos.*;
import com.Notifications.patientssassistant.fragments.*;
import com.Notifications.patientssassistant.tables.*;
import com.Notifications.patientssassistant.asynctask.*;
import com.orm.query.Condition;
import com.orm.query.Select;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.ExecutionException;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


public class RegistrarAhoraActivity extends Activity {

	//VARIABLES PARA EDITAR REGISTRO  
    private Long vIdCuidador;
    private String vUsuarioC;
    private String vNombreC;
    private String vCiRucC;
    private String vCelularC;    
    private String vTelefonoC;
    private String vEmailC;
    private String vDireccionC;    
    private String vCargoC;
    private String vFotoC;    
    private String vContrasenaC;
    private String vTipoResidenciaC;
    private String vClaveVinculacionC;
	
	//VARIABLES QUE ALMACENAN LOS DATOS A GUARDAR
	private String campo_usuarioC;  
	private String campo_nombresC;
	private String campo_ciRucC;    		    		    		  		
	private String campo_celularC;
	private String campo_telefonoC;
	private String campo_correoC;
	private String campo_direccionC;
	private String campo_laborC;
	private String campo_tipoResidenciaC;
	private String campo_contrasenaC;
	private String campo_claveVinculacionC;
	
	//VARIABLES PARA FOTO
	private static int TAKE_PICTURE = 1;
	private static int SELECT_PICTURE = 2;
	private String name = "";
	private Uri selectedImage;
	private Bitmap bitmap1, bitmap2;
	private byte[] b;
	private String campo_fotoC="";
		
	//VARIABLES DE LOS ELEMENTOS DE LA IU	
	ImageView ImageFotoCP;
	EditText EdtNewUsuario;
	EditText EdtNewPerFun;
	EditText EdtNewCiRuc;
	EditText EdtNewCelular;
	EditText EdtNewTelefono;	
	EditText EdtNewCorreo;
	EditText EdtNewDireccion;
	EditText EdtNewLabor;
	Spinner CmbNewTipoReside;
	EditText EdtNewContrasena;	
	EditText EdtNewClaveVincu;
	Button BtnNuevoEditar;
	
	//VARIABLES EXTRAS
	private Boolean banderaGuardar=false;
	private TblCuidador editar_cuidador;
	private TblCuidadorPr editar_cuidadorP;
	private FragmentManager fragmentManager = getFragmentManager(); 
	
		
	public RegistrarAhoraActivity() { super(); }

	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registrar_ahora);
		
		ImageFotoCP = (ImageView)findViewById(R.id.imageFotoCP);
		EdtNewUsuario = (EditText)findViewById(R.id.edtNewUsuario);
		EdtNewPerFun = (EditText)findViewById(R.id.edtNewPerFun);
		EdtNewCiRuc = (EditText)findViewById(R.id.edtNewCiRuc);
		EdtNewCelular = (EditText)findViewById(R.id.edtNewCelular);
		EdtNewTelefono = (EditText)findViewById(R.id.edtNewTelefono);
		EdtNewCorreo = (EditText)findViewById(R.id.edtNewCorreo);
		EdtNewDireccion = (EditText)findViewById(R.id.edtNewDireccion);		
		EdtNewLabor = (EditText)findViewById(R.id.edtNewLabor);
		EdtNewContrasena = (EditText)findViewById(R.id.edtNewContrasena);
		EdtNewClaveVincu = (EditText)findViewById(R.id.edtNewClaveVincu);
		CmbNewTipoReside = (Spinner)findViewById(R.id.cmbNewTipoReside);
		BtnNuevoEditar=(Button)findViewById(R.id.btnNewGuardar);
				
		NuevoVsEditar();		
	}

	private void tareaLarga()
	{
		try {
			Thread.sleep(1000);
		} catch(InterruptedException e) {}
	}

	//METODO PARA IR A NUEVO O EDITAR CUIDADOR PRIMARIO
	public void NuevoVsEditar() {		
		Boolean opcion1= IniciarSesionActivity.EsNuevo();
		Boolean opcion2= TabCuidador1DpF.EsEditar();	

		if (opcion1.equals(true)) {			
			CargarOpcSpinner();
			BtnNuevoCP();
		}
		if (opcion2.equals(true)) {			
			RecogerParametrosFragTabCuiDp1();
			CargarDatosEnLosElementos();
			SpinnersTouch();			
			BtnEditarCP();
		}
		ImageBtnSubirFoto();
	}
	
	//----METODOS PARA UN NUEVO REGISTRO
	
	public void BtnNuevoCP() {
		BtnNuevoEditar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (estaConectado()) {
					try {
						GetDatosDeLosElementos();
						if ((campo_usuarioC.equals("")) || (campo_nombresC.equals("")) || (campo_correoC.equals("")) || (campo_tipoResidenciaC.equals("")) || (campo_contrasenaC.equals("")) || (campo_claveVinculacionC.equals("")) || ((campo_celularC.equals("")) && (campo_telefonoC.equals("")))) {
							DFRegAho dialogo1 = new DFRegAho();
							dialogo1.show(fragmentManager, "tagAlerta");
						} else {
							Validaciones();
							if (banderaGuardar.equals(true)) {
								ATCuidador objCuidador = new ATCuidador();
								Long idC = objCuidador.new InsertarCuidador().execute("0", campo_usuarioC, campo_nombresC, campo_ciRucC, campo_celularC, campo_telefonoC,
										campo_correoC, campo_direccionC, campo_laborC, "true", campo_fotoC, "false").get().longValue();
								tareaLarga();
								if (idC > 0) {
									ATCuidadorPr objCuidadorPr = new ATCuidadorPr();
									objCuidadorPr.new InsertarCuidadorPr().execute(String.valueOf(idC.longValue()), campo_contrasenaC, campo_tipoResidenciaC, campo_claveVinculacionC, "false");
								}

								TblCuidador.deleteAll(TblCuidador.class);

								LimpiarElementos();
								Toast.makeText(RegistrarAhoraActivity.this, R.string.GuardadoR, Toast.LENGTH_SHORT).show();
								Intent data = new Intent();
								setResult(RESULT_OK, data);
								finish();
							}
						}
					} catch (Exception ex) {
						Toast.makeText(RegistrarAhoraActivity.this, getString(R.string.ErrorGuardar) + ex.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}
	
	//----METODOS PARA EDITAR UN REGISTRO
	
	public void RecogerParametrosFragTabCuiDp1() {		
		vIdCuidador = getIntent().getExtras().getLong("varIdCuidador");
		vUsuarioC = getIntent().getExtras().getString("varUsuarioC");
		vNombreC = getIntent().getExtras().getString("varNombreC");
		vCiRucC = getIntent().getExtras().getString("varCiRucC");
		vCelularC = getIntent().getExtras().getString("varCelularC");
		vTelefonoC = getIntent().getExtras().getString("varTelefonoC");
		vEmailC = getIntent().getExtras().getString("varEmailC");
		vDireccionC = getIntent().getExtras().getString("varDireccionC");
		vCargoC = getIntent().getExtras().getString("varCargoC");		
		vFotoC = getIntent().getExtras().getString("varFotoC");
		vContrasenaC = getIntent().getExtras().getString("varContrasenaC");	   
		vTipoResidenciaC = getIntent().getExtras().getString("varTipoResidenciaC");
		vClaveVinculacionC = getIntent().getExtras().getString("varClaveVinculacionC");
		campo_fotoC=vFotoC;
	}
	
	public void CargarDatosEnLosElementos() {		
		if (vFotoC.equals("")) {
			ImageFotoCP.setImageResource(R.drawable.user_foto);				
		}else {
			b = Base64.decode(vFotoC, Base64.DEFAULT);
			bitmap1 = BitmapFactory.decodeByteArray(b, 0, b.length);
        	ImageFotoCP.setImageBitmap(bitmap1);
		}		
		EdtNewUsuario.setText(vUsuarioC);
		EdtNewPerFun.setText(vNombreC);
		EdtNewCiRuc.setText(vCiRucC);
		EdtNewCelular.setText(vCelularC);
		EdtNewTelefono.setText(vTelefonoC);
		EdtNewCorreo.setText(vEmailC);
		EdtNewDireccion.setText(vDireccionC);
		EdtNewLabor.setText(vCargoC);		
		EdtNewContrasena.setText(vContrasenaC);
		EdtNewClaveVincu.setText(vClaveVinculacionC);
		//CARGANDO LOS DATOS GUARDADOS EN LOS SPINNERS    	
    	String[] opcGuardadaTipRes={vTipoResidenciaC}; 	
    	CmbNewTipoReside.setAdapter(new AdapterSpinnerSimple(RegistrarAhoraActivity.this, R.layout.adaptador_spinner, opcGuardadaTipRes));
	}
	
	public void SpinnersTouch() {		
		CmbNewTipoReside.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (CmbNewTipoReside.isPressed()) {
					CargarOpcSpinner();
				}
				return false;
			}
		});			
	}
	
	public void BtnEditarCP(){		
		BtnNuevoEditar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(estaConectado()) {
					try {
						GetDatosDeLosElementos();
						if ((campo_usuarioC.equals("")) || (campo_nombresC.equals("")) || (campo_correoC.equals("")) || (campo_tipoResidenciaC.equals("")) ||
								(campo_contrasenaC.equals("")) || (campo_claveVinculacionC.equals("")) || ((campo_celularC.equals("")) && (campo_telefonoC.equals("")))) {
							DFRegAho dialogo1 = new DFRegAho();
							dialogo1.show(fragmentManager, "tagAlerta");
						} else {
							editar_cuidador = Select.from(TblCuidador.class).where(Condition.prop("id_cuidador").eq(vIdCuidador)).first();
							editar_cuidadorP = Select.from(TblCuidadorPr.class).where(Condition.prop("id_cuidador").eq(vIdCuidador)).first();

							if (editar_cuidador.getUsuarioC().equals(campo_usuarioC) && editar_cuidador.getEmailC().equals(campo_correoC)) {
								if (editar_cuidadorP.getContrasena().equals(campo_contrasenaC) && editar_cuidadorP.getPassVinculacion().equals(campo_claveVinculacionC)) {
									EditandoRegistros();
								} else {
									VerificarPass();
									if (banderaGuardar.equals(true)) {
										EditandoRegistros();
									}
								}
							} else {
								if (!editar_cuidador.getUsuarioC().equals(campo_usuarioC) && !editar_cuidador.getEmailC().equals(campo_correoC)) {
									Validaciones();
									if (banderaGuardar.equals(true)) {
										EditandoRegistros();
									}
								} else {
									if (!editar_cuidador.getUsuarioC().equals(campo_usuarioC)) {
										VerificarUser();
										if (banderaGuardar.equals(true)) {
											EditandoRegistros();
										}
									}
									if (!editar_cuidador.getEmailC().equals(campo_correoC)) {
										VerificarEmail();
										if (banderaGuardar.equals(true)) {
											EditandoRegistros();
										}
									}
								}
							}
						}
					} catch (Exception ex) {
						Toast.makeText(RegistrarAhoraActivity.this, getString(R.string.ErrorEditar) + ex.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}
	
	public void EditandoRegistros() {
		ATCuidador objCuidador= new ATCuidador();
		objCuidador.new ActualizarCuidador().execute(String.valueOf(vIdCuidador), campo_usuarioC, campo_nombresC, campo_ciRucC, campo_celularC, campo_telefonoC,
				campo_correoC, campo_direccionC, campo_laborC, "true", campo_fotoC, "false");

		ATCuidadorPr objCuidadorPr = new ATCuidadorPr();
		objCuidadorPr.new ActualizarCuidadorPr().execute(String.valueOf(vIdCuidador), campo_contrasenaC, campo_tipoResidenciaC, campo_claveVinculacionC, "false");
				
		//ALMACENAR PREFERENCIA
		SharedPreferences settings = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();		
		editor.putString("usuario", campo_usuarioC);
		editor.putString("fotoCoP", campo_fotoC);		
		//CONFIRMAR EL ALMACENAMIENTO
		editor.commit();	
		
		LimpiarElementos();
		Toast.makeText(RegistrarAhoraActivity.this, R.string.EditadoR, Toast.LENGTH_SHORT).show();
		Intent data = new Intent();
		setResult(RESULT_OK, data);
		finish();
	}	
	
	public Boolean VerificarUser() {
		//BUSCAMOS SI YA EXISTE EL USUARIO A GUARDAR
		Boolean existeUsuario=false;
		ATCuidador cuidador = new ATCuidador();

		try {
			existeUsuario=cuidador.new ExisteUsuario().execute(campo_usuarioC).get().booleanValue();
			if (existeUsuario==false) {
				VerificarPass();
			}else {
				DFUsuarioRepetido dialogo2 = new DFUsuarioRepetido();
				dialogo2.show(fragmentManager, "tagAlerta");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return banderaGuardar;
	}
	
	public Boolean VerificarEmail() {
		//BUSCAMOS SI YA EXISTE EL CORREO A GUARDAR
		boolean valid = MetodosValidacionesExtras.validateEmail(campo_correoC);
		Boolean existeEmail=false;
		ATCuidador cuidador = new ATCuidador();

		if (valid==true) {
			try {
				existeEmail = cuidador.new ExisteEmail().execute(campo_correoC).get().booleanValue();
				if (existeEmail == false) {
					VerificarPass();
				} else {
					DFCorreoRepetido dialogo3 = new DFCorreoRepetido();
					dialogo3.show(fragmentManager, "tagAlerta");
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		return banderaGuardar;
	}

	public Boolean VerificarPass() {
		if((campo_contrasenaC.length() < 6 || campo_contrasenaC.length() > 12) || (campo_claveVinculacionC.length() < 6 || campo_claveVinculacionC.length() > 12)) {
			DFValidarPass dialogo5 = new DFValidarPass();
			dialogo5.show(fragmentManager, "tagAlerta");
		}else{
			banderaGuardar=true;
		}
		return banderaGuardar;
	}
			
	//----METODOS TANTO PARA NUEVO O EDITAR REGISTRO
	
	public void GetDatosDeLosElementos() {
		campo_usuarioC=EdtNewUsuario.getText().toString().trim();  
		campo_nombresC=EdtNewPerFun.getText().toString().trim();
		campo_ciRucC=EdtNewCiRuc.getText().toString().trim();    		    		    		  		
		campo_celularC=EdtNewCelular.getText().toString().trim();
		campo_telefonoC=EdtNewTelefono.getText().toString().trim();
		campo_correoC=EdtNewCorreo.getText().toString().trim();
		campo_direccionC=EdtNewDireccion.getText().toString().trim();
		campo_laborC=EdtNewLabor.getText().toString().trim();
		campo_tipoResidenciaC=CmbNewTipoReside.getSelectedItem().toString();
		campo_contrasenaC=EdtNewContrasena.getText().toString().trim();
		campo_claveVinculacionC=EdtNewClaveVincu.getText().toString().trim();
	}
	
	public void CargarOpcSpinner() {
		String[] OpcTipoResidencia=getResources().getStringArray(R.array.opc_tipoResidencia);						 
		CmbNewTipoReside.setAdapter(new AdapterSpinnerSimple(RegistrarAhoraActivity.this, R.layout.adaptador_spinner, OpcTipoResidencia));
	}

	public Boolean Validaciones() {
		boolean valid = MetodosValidacionesExtras.validateEmail(campo_correoC);

		Boolean existeEmail=false;
		Boolean existeUsuario=false;
		
		ATCuidador cuidador = new ATCuidador();

		if (valid==true) {
			try {
				existeUsuario=cuidador.new ExisteUsuario().execute(campo_usuarioC).get().booleanValue();
				existeEmail=cuidador.new ExisteEmail().execute(campo_correoC).get().booleanValue();

				if (existeUsuario==false && existeEmail==false) {
					VerificarPass();
				}else {
					if (existeUsuario==true && existeEmail==true) {
						DFUCRepetido dialogo4 = new DFUCRepetido();
						dialogo4.show(fragmentManager, "tagAlerta");
					}else {
						if (existeUsuario==true) {
							DFUsuarioRepetido dialogo2 = new DFUsuarioRepetido();
							dialogo2.show(fragmentManager, "tagAlerta");
						}
						if (existeEmail==true) {
							DFCorreoRepetido dialogo3 = new DFCorreoRepetido();
							dialogo3.show(fragmentManager, "tagAlerta");
						}
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}else {
			DFCorreoNoVa dialogo5 = new DFCorreoNoVa();
			dialogo5.show(fragmentManager, "tagAlerta");
		}
		return banderaGuardar;
	}
	
	public void ImageBtnSubirFoto() {		
		name = Environment.getExternalStorageDirectory() + "/test1.jpg";
		ImageFotoCP.setOnClickListener(new View.OnClickListener() {					
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
    	if ((requestCode == SELECT_PICTURE)&&(resultCode == RegistrarAhoraActivity.RESULT_OK)) {
        	selectedImage = data.getData();		    		
    		try {
				bitmap1=MetodosValidacionesExtras.decodeSampledBitmapFromResource(selectedImage, RegistrarAhoraActivity.this, 120, 120);
				ImageFotoCP.setImageBitmap(MetodosValidacionesExtras.redimensionarImagenMaximo(bitmap1, 120, 120));

				bitmap2 = ((BitmapDrawable)ImageFotoCP.getDrawable()).getBitmap();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, baos);  //PNG
				byte[] b = baos.toByteArray();
				campo_fotoC = Base64.encodeToString(b, Base64.DEFAULT);
    		} catch (Exception e) {}
    	}
	}	
	
	public void LimpiarElementos() {
		ImageFotoCP.setImageResource(R.drawable.user_foto);	
		EdtNewUsuario.getText().clear();
		EdtNewPerFun.getText().clear();
		EdtNewCiRuc.getText().clear();
		EdtNewCelular.getText().clear();
		EdtNewTelefono.getText().clear();
		EdtNewCorreo.getText().clear();
		EdtNewDireccion.getText().clear();		
		EdtNewLabor.getText().clear();
		EdtNewContrasena.getText().clear();
		EdtNewClaveVincu.getText().clear();	
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
				Toast.makeText(RegistrarAhoraActivity.this, "No hay Conexion a Internet", Toast.LENGTH_SHORT).show();
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