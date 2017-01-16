package com.Notifications.patientssassistant;


import com.Notifications.patientssassistant.dialogos.*;
import com.Notifications.patientssassistant.fragments.*;
import com.Notifications.patientssassistant.tables.*;
import com.Notifications.patientssassistant.asynctask.*;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.orm.query.Condition;
import com.orm.query.Select;


public class NewEditCuidadorActivity extends Activity {
		
	//VARIABLES PARA NUEVO REGISTRO
	private Long vDependeDeCuidador;

    //VARIABLES PARA EDITAR REGISTRO
    private Long vItemIdCuidador;
    private String vUsuarioC;
    private String vNombreC;
    private String vCiRucC;
    private String vCelularC;    
    private String vTelefonoC;
    private String vEmailC;
    private String vDireccionC;    
    private String vCargoC;
    private Boolean vControlTotal;
    private String vFotoC;
    private Long vIdCuidador;
    
    //VARIABLES QUE ALMACENAN LOS DATOS A GUARDAR
    private String campo_usuarioC;
    private String campo_nombreC;	
    private String campo_ciRucC;
    private String campo_celularC;
    private String campo_telefonoC;
    private String campo_emailC;
    private String campo_direccionC;
    private String campo_cargoC;					
    private Boolean campo_controlTotal;
    	
    //VARIABLES PARA FOTO
	private static int TAKE_PICTURE = 1;
	private static int SELECT_PICTURE = 2;
	private String name = "";
	private Uri selectedImage;
	private Bitmap bitmap1, bitmap2;
	private byte[] b;
	private String campo_fotoC=""; 
		
	//VARIABLES DE LOS ELEMENTOS DE LA IU
	ImageView ImageFotoC;
	EditText EdtNewUsuario;
	EditText EdtNewNombres;
	EditText EdtNewCI;
	EditText EdtNewCelular;
	EditText EdtNewTelefono;
	EditText EdtNewCorreo;
	EditText EdtNewDireccion;
	EditText EdtNewLabor;
	CheckedTextView ChkPermiso;
	Button BtnNuevoEditar;
	
	//VARIABLES EXTRAS	
	private Boolean banderaGuardar=false;
	private TblCuidador editar_cuidador;
	private FragmentManager fragmentManager = getFragmentManager();


	public NewEditCuidadorActivity() { super(); }

	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nuevo_cuidador);

		ImageFotoC=(ImageView)findViewById(R.id.imageFotoC);
		EdtNewUsuario=(EditText)findViewById(R.id.edtNewUsuario);
		EdtNewNombres=(EditText)findViewById(R.id.edtNewNombres);
		EdtNewCI=(EditText)findViewById(R.id.edtNewCI);
		EdtNewCelular=(EditText)findViewById(R.id.edtNewCelular);
		EdtNewTelefono=(EditText)findViewById(R.id.edtNewTelefono);
		EdtNewCorreo=(EditText)findViewById(R.id.edtNewCorreo);
		EdtNewDireccion=(EditText)findViewById(R.id.edtNewDireccion);
		EdtNewLabor=(EditText)findViewById(R.id.edtNewLabor);		
		ChkPermiso=(CheckedTextView)findViewById(R.id.chkPermiso);
		BtnNuevoEditar=(Button)findViewById(R.id.btnNewGuardar);

		NuevoVsEditar();
	}
	
	//METODO QUE SERVIRA PARA IR A NUEVO O EDITAR REGISTRO DE CUIDADOR
	public void NuevoVsEditar() {
		Boolean opcion1= MpCuidadoresF.EsNuevo();	
		Boolean opcion2= MpCuidadoresF.EsEditar();
		Boolean opcion3= TabCuidador1DpF.EsEditar();
		
		if (opcion1.equals(true)) {
			NewEditCuidadorActivity.this.setTitle(R.string.NewCuidador);
			RecogerParametrosFragMpC2();				
			BtnNuevoCS();
		}
		if ((opcion2.equals(true))||(opcion3.equals(true))) {
			NewEditCuidadorActivity.this.setTitle(R.string.EditCuidador);
			RecogerParametrosFragMpC3yTabCuiDpyTabMiPerfilDp2();
			CargarDatosEnLosElementos();			
			BtnEditarCS();
		}		
		ActDesCheckedTextView();
		ImageBtnSubirFoto();
	}
		
	//----METODOS PARA UN NUEVO REGISTRO
	
	public void RecogerParametrosFragMpC2() {
		vDependeDeCuidador = getIntent().getExtras().getLong("varDependeDe");
	}
	
	public void BtnNuevoCS(){		
		BtnNuevoEditar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(estaConectado()) {
					try {
						GetDatosDeLosElementos();
						if ((campo_usuarioC.equals("")) || (campo_nombreC.equals("")) || (campo_emailC.equals("")) || ((campo_celularC.equals("")) && (campo_telefonoC.equals("")))) {
							DFNueCui dialogo1 = new DFNueCui();
							dialogo1.show(fragmentManager, "tagAlerta");
						} else {
							VerificarUserEmail();
							if (banderaGuardar.equals(true)) {
								//GUARDAR DATOS EN LA TABLA DE CUIDADOR
								ATCuidador newCuidador = new ATCuidador();
								Long idC = newCuidador.new InsertarCuidador().execute("0", campo_usuarioC, campo_nombreC, campo_ciRucC, campo_celularC, campo_telefonoC,
										campo_emailC, campo_direccionC, campo_cargoC, campo_controlTotal.toString(), campo_fotoC, "false").get().longValue();

								//GUARDAR DATOS EN LA TABLA DE CUIDADOR SECUNDARIO
								ATCuidadorS newCuidadorS = new ATCuidadorS();
								newCuidadorS.new InsertarCuidadorS().execute(String.valueOf(idC), vDependeDeCuidador.toString(), "false");

								LimpiarElementos();
								Toast.makeText(NewEditCuidadorActivity.this, R.string.GuardadoR, Toast.LENGTH_SHORT).show();
								Intent data = new Intent();
								setResult(RESULT_OK, data);
								finish();
							}
						}
					} catch (Exception ex) {
						Toast.makeText(NewEditCuidadorActivity.this, getString(R.string.ErrorGuardar) + ex.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}
	
	//----METODOS PARA EDITAR UN REGISTRO

	public void RecogerParametrosFragMpC3yTabCuiDpyTabMiPerfilDp2() {		
		vItemIdCuidador = getIntent().getExtras().getLong("varItemIdCuidador");
		vUsuarioC = getIntent().getExtras().getString("varUsuarioC");
		vNombreC = getIntent().getExtras().getString("varNombreC");
		vCiRucC = getIntent().getExtras().getString("varCiRucC");
		vCelularC = getIntent().getExtras().getString("varCelularC");
		vTelefonoC = getIntent().getExtras().getString("varTelefonoC");
		vEmailC = getIntent().getExtras().getString("varEmailC");
		vDireccionC = getIntent().getExtras().getString("varDireccionC");
		vCargoC = getIntent().getExtras().getString("varCargoC");
		vControlTotal = getIntent().getExtras().getBoolean("varControlTotal");
		vFotoC = getIntent().getExtras().getString("varFotoC");	
		vIdCuidador = getIntent().getExtras().getLong("varIdCuidador");
		campo_fotoC=vFotoC;
	}
	
	public void CargarDatosEnLosElementos() {		
		if (vFotoC.equals("")) {
			ImageFotoC.setImageResource(R.drawable.user_foto);				
		}else{
			b = Base64.decode(vFotoC, Base64.DEFAULT);
			bitmap1 = BitmapFactory.decodeByteArray(b, 0, b.length);
        	ImageFotoC.setImageBitmap(bitmap1);
		}		
		EdtNewUsuario.setText(vUsuarioC);
		EdtNewNombres.setText(vNombreC);
		EdtNewCI.setText(vCiRucC);
		EdtNewCelular.setText(vCelularC);
		EdtNewTelefono.setText(vTelefonoC);
		EdtNewCorreo.setText(vEmailC);
		EdtNewDireccion.setText(vDireccionC);
		EdtNewLabor.setText(vCargoC);		
		ChkPermiso.setChecked(vControlTotal);
		//SI SE INICIO SESION COMO CUIDADOR SECUNDARIO ESTE NO PODRA EDITAR SU PROPIO PERMISO
		if (vIdCuidador.equals(vItemIdCuidador)) {
			ChkPermiso.setEnabled(false);
			ChkPermiso.setClickable(false);
		}
	}
	
	public void BtnEditarCS(){		
		BtnNuevoEditar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(estaConectado()) {
					try {
						GetDatosDeLosElementos();
						if ((campo_usuarioC.equals("")) || (campo_nombreC.equals("")) || (campo_emailC.equals("")) || ((campo_celularC.equals("")) && (campo_telefonoC.equals("")))) {
							DFNueCui dialogo1 = new DFNueCui();
							dialogo1.show(fragmentManager, "tagAlerta");
						} else {
							editar_cuidador = Select.from(TblCuidador.class).where(Condition.prop("id_cuidador").eq(vItemIdCuidador)).first();

							if (editar_cuidador.getUsuarioC().equals(campo_usuarioC) && editar_cuidador.getEmailC().equals(campo_emailC)) {
								EditandoRegistros();
							} else {
								if (!editar_cuidador.getUsuarioC().equals(campo_usuarioC) && !editar_cuidador.getEmailC().equals(campo_emailC)) {
									VerificarUserEmail();
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
									if (!editar_cuidador.getEmailC().equals(campo_emailC)) {
										VerificarEmail();
										if (banderaGuardar.equals(true)) {
											EditandoRegistros();
										}
									}
								}
							}
						}
					} catch (Exception ex) {
						Toast.makeText(NewEditCuidadorActivity.this, getString(R.string.ErrorEditar) + ex.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}
	
	public void EditandoRegistros() {
		//EDITAR DATOS EN LA TABLA DE CUIDADOR
		ATCuidador newCuidador = new ATCuidador();
		newCuidador.new ActualizarCuidador().execute(String.valueOf(vItemIdCuidador), campo_usuarioC, campo_nombreC, campo_ciRucC, campo_celularC, campo_telefonoC,
				 campo_emailC, campo_direccionC, campo_cargoC, campo_controlTotal.toString(), campo_fotoC, "false");
		
		//VERIFICAR SI EL CUIDADOR SECUNDARIO QUE SE EDITA ES QUIEN HA INICIADO SESION 
		if (vIdCuidador.equals(vItemIdCuidador)) {
			//ALMACENAR PREFERENCIA
			SharedPreferences settings = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = settings.edit();		
			editor.putString("usuario", campo_usuarioC);
			editor.putString("fotoCoP", campo_fotoC);		
			//CONFIRMAR EL ALMACENAMIENTO
			editor.commit();
		}
		
		LimpiarElementos();
		Toast.makeText(NewEditCuidadorActivity.this, R.string.EditadoR, Toast.LENGTH_SHORT).show();
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
				banderaGuardar=true;
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
		boolean valid = MetodosValidacionesExtras.validateEmail(campo_emailC);
		Boolean existeEmail=false;
		ATCuidador cuidador = new ATCuidador();

		if (valid==true) {
			try {
				existeEmail = cuidador.new ExisteEmail().execute(campo_emailC).get().booleanValue();
				if (existeEmail == false) {
					banderaGuardar = true;
				} else {
					DFCorreoRepetido dialogo3 = new DFCorreoRepetido();
					dialogo3.show(fragmentManager, "tagAlerta");
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		} else {
			DFCorreoNoVa dialogo5 = new DFCorreoNoVa();
			dialogo5.show(fragmentManager, "tagAlerta");
		}
		return banderaGuardar;
	}

	//----METODOS TANTO PARA NUEVO O EDITAR REGISTRO	
	
	public void GetDatosDeLosElementos() {		
		campo_usuarioC=EdtNewUsuario.getText().toString().trim();
		campo_nombreC=EdtNewNombres.getText().toString().trim();	
		campo_ciRucC=EdtNewCI.getText().toString().trim();
		campo_celularC=EdtNewCelular.getText().toString().trim();
		campo_telefonoC=EdtNewTelefono.getText().toString().trim();
		campo_emailC=EdtNewCorreo.getText().toString().trim();
		campo_direccionC=EdtNewDireccion.getText().toString().trim();
		campo_cargoC=EdtNewLabor.getText().toString().trim();					
		campo_controlTotal=ChkPermiso.isChecked();		
	}
	
	public Boolean VerificarUserEmail() {
		boolean valid = MetodosValidacionesExtras.validateEmail(campo_emailC);

		Boolean existeEmail=false;
		Boolean existeUsuario=false;

		ATCuidador cuidador = new ATCuidador();

		if (valid==true) {
			try {
				existeUsuario=cuidador.new ExisteUsuario().execute(campo_usuarioC).get().booleanValue();
				existeEmail=cuidador.new ExisteEmail().execute(campo_emailC).get().booleanValue();

				if (existeUsuario==false && existeEmail==false) {
					banderaGuardar=true;
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
	
	public void ActDesCheckedTextView() {
		ChkPermiso.setClickable(true);		    	
		ChkPermiso.setOnClickListener(new OnClickListener() {				
			@Override
			public void onClick(View v) { ChkPermiso.toggle(); }
		});
	}
	
	public void ImageBtnSubirFoto() {
		name = Environment.getExternalStorageDirectory() + "/test.jpg";
		ImageFotoC.setOnClickListener(new View.OnClickListener() {
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
    	if ((requestCode == SELECT_PICTURE)&&(resultCode == NewEditCuidadorActivity.RESULT_OK)) {
			selectedImage = data.getData();
			try {
				bitmap1=MetodosValidacionesExtras.decodeSampledBitmapFromResource(selectedImage, NewEditCuidadorActivity.this, 120, 120);
				ImageFotoC.setImageBitmap(MetodosValidacionesExtras.redimensionarImagenMaximo(bitmap1, 120, 120));

				bitmap2 = ((BitmapDrawable)ImageFotoC.getDrawable()).getBitmap();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, baos);  //PNG
				byte[] b = baos.toByteArray();
				campo_fotoC = Base64.encodeToString(b, Base64.DEFAULT);
			}catch (Exception e) {}
		}
	}

	public void LimpiarElementos() {
		ImageFotoC.setImageResource(R.drawable.user_foto);	
		EdtNewUsuario.getText().clear();
		EdtNewNombres.getText().clear();
		EdtNewCI.getText().clear();
		EdtNewCelular.getText().clear();
		EdtNewTelefono.getText().clear();
		EdtNewCorreo.getText().clear();
		EdtNewDireccion.getText().clear();		
		EdtNewLabor.getText().clear();
		ChkPermiso.setChecked(false);			
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
				Toast.makeText(NewEditCuidadorActivity.this, "No hay Conexion a Internet", Toast.LENGTH_SHORT).show();
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