package com.Notifications.patientssassistant;


import com.Notifications.patientssassistant.alarmas.*;
import com.Notifications.patientssassistant.dialogos.*;
import com.Notifications.patientssassistant.fragments.*;
import com.Notifications.patientssassistant.tables.*;
import com.Notifications.patientssassistant.asynctask.*;
import java.io.ByteArrayOutputStream;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.orm.query.Condition;
import com.orm.query.Select;


public class NewEditPeticionActivity extends Activity {
							
	//VARIABLES PARA NUEVO REGISTRO
	private Long vIdPaciente;	

	//VARIABLES PARA EDITAR REGISTRO
	private Long vIdActividad;
	private String vNombreActividad;
	private String vDetalleActividad;
	private String vImagenActividad;
	private String vTonoActividad;	
	
    //VARIABLES PARA FOTO
	private static int TAKE_PICTURE = 1;
	private static int SELECT_PICTURE = 2;
	private String name = "";
	private Uri selectedImage;
	private Bitmap bitmap1, bitmap2;
	private String campo_imagenActividad=""; 
	
	//VARIABLES QUE ALMACENAN LOS DATOS A GUARDAR
	private Long campo_idTipoActividad;
	private String campo_nombreActividad;
	private String campo_detalleActividad;
	private String campo_tonoActividad;
			
	//VARIABLES DE LOS ELEMENTOS DE LA IU
	EditText EdtNewNombrePeticion;
	EditText EdtNewDescripcionPeticion;
	ImageView ImageCargarImagenPeticion;
	RelativeLayout RingtoneContainer;
	TextView TxtTono;
	Button BtnNuevoEditar;
		
	//VARIABLES EXTRAS
	private Boolean banderaGuardar=false;	
	private TblTipoActividad tipoActividad;
	private TblActividades editar_act;
	private String seleccion;
	private MediaPlayer mPlayer;
	private Boolean banderaTono=false;
	private FragmentManager fragmentManager = getFragmentManager(); 
			
	
	public NewEditPeticionActivity() { super(); }

	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nueva_peticion); 
		
		EdtNewNombrePeticion=(EditText)findViewById(R.id.edtNewNombrePeticion);
		EdtNewDescripcionPeticion=(EditText)findViewById(R.id.edtNewDescripcionPeticion);
		ImageCargarImagenPeticion=(ImageView)findViewById(R.id.imageCargarImagenPeticion);
		RingtoneContainer=(RelativeLayout)findViewById(R.id.ringtoneContainer);
		TxtTono=(TextView)findViewById(R.id.txtTono);
		BtnNuevoEditar=(Button)findViewById(R.id.btnNewGuardar);
					
		NuevoVsEditar();		    	
	}
	
	//METODO QUE SERVIRA PARA IR A NUEVO O EDITAR REGISTRO 
	public void NuevoVsEditar() {		
		//PETICIONES		
		Boolean opcion3= MenPacPersonalizarPantF.EsNuevo();
		Boolean opcion4= MenPacPersonalizarPantF.EsEditar();
		if (opcion3.equals(true)) {
			NewEditPeticionActivity.this.setTitle(R.string.NewPeticion);
			RecogerParametros();
			SeleccionarTonoAlarma();
			BtnSubirImagen();
			BtnNuevaPeticion();
		}		
		if (opcion4.equals(true)) {
			NewEditPeticionActivity.this.setTitle(R.string.EditPeticion);
			RecogerParametrosFragMenPacPerP2();
			CargarDatosEnLosElementos();
			SeleccionarTonoAlarma();
			BtnSubirImagen();
			BtnEditarPeticion();			
		}
	}
		
	//----METODOS PARA UNA NUEVA PETICION DEL PACIENTE  
	
	public void BtnNuevaPeticion() {		
		BtnNuevoEditar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(estaConectado()) {
					try {
						ValidarDatos2();
						if (banderaGuardar.equals(true)) {
							//GUARDAR DATOS EN LA TABLA ACTIVIDADES
							ATActividades objActividad = new ATActividades();
							Long idAct = objActividad.new InsertarActividades().execute("0", String.valueOf(5), campo_nombreActividad,
									campo_detalleActividad, campo_imagenActividad, campo_tonoActividad, "false").get().longValue();

							//GUARDAR DATOS EN LA TABLA ACTIVIDAD/PACIENTE
							ATActividadPaciente objActPac = new ATActividadPaciente();
							objActPac.new InsertarActividadPacientes().execute(String.valueOf(vIdPaciente), String.valueOf(idAct), "false");

							LimpiarElementos();
							Toast.makeText(NewEditPeticionActivity.this, R.string.GuardadoR, Toast.LENGTH_SHORT).show();
							Intent data = new Intent();
							setResult(RESULT_OK, data);
							finish();
						}
					} catch (Exception ex) {
						Toast.makeText(NewEditPeticionActivity.this, getString(R.string.ErrorGuardar) + ex.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}
	
	//----METODOS PARA EDITAR PETICION DEL PACIENTE  
	
	public void BtnEditarPeticion() {		
		BtnNuevoEditar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(estaConectado()) {
					try {
						tipoActividad = Select.from(TblTipoActividad.class).where(Condition.prop("tipo_actividad").eq("PETICION"),
								Condition.prop("Eliminado").eq(0)).first();

						campo_idTipoActividad = tipoActividad.getIdTipoActividad();
						campo_nombreActividad = EdtNewNombrePeticion.getText().toString().trim();
						campo_detalleActividad = EdtNewDescripcionPeticion.getText().toString().trim();
						campo_tonoActividad = TxtTono.getText().toString().trim();

						if ((campo_nombreActividad.equals("")) || (campo_detalleActividad.equals("")) || (campo_imagenActividad.equals("")) || (campo_tonoActividad.equals(getString(R.string.SeleccioneTono)))) {
							DFIngresarDatos dialogo1 = new DFIngresarDatos();
							dialogo1.show(fragmentManager, "tagAlerta");
						} else {
							editar_act = Select.from(TblActividades.class).where(Condition.prop("id_actividad").eq(vIdActividad)).first();

							if (editar_act.getNombreActividad().equals(campo_nombreActividad)) {
								EditandoRegistros();
							}
							if (!editar_act.getNombreActividad().equals(campo_nombreActividad)) {
								VerificarNombreAct();
								if (banderaGuardar.equals(true)) {
									EditandoRegistros();
								}
							}
						}
					} catch (Exception ex) {
						Toast.makeText(NewEditPeticionActivity.this, getString(R.string.ErrorEditar) + ex.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}

	public void EditandoRegistros() {
		//EDITAR DATOS EN LA TABLA DE ACTIVIDADES
		ATActividades objActividad = new ATActividades();
		objActividad.new ActualizarActividades().execute(String.valueOf(vIdActividad), String.valueOf(campo_idTipoActividad), 
						campo_nombreActividad, campo_detalleActividad, campo_imagenActividad, campo_tonoActividad, "false");

		LimpiarElementos();
		Toast.makeText(NewEditPeticionActivity.this, R.string.EditadoR, Toast.LENGTH_SHORT).show();							
		Intent data = new Intent();
		setResult(RESULT_OK, data);
		finish();
	}
	
	public void RecogerParametrosFragMenPacPerP2() {		
		vIdActividad = getIntent().getExtras().getLong("varIdActividad");
		vNombreActividad = getIntent().getExtras().getString("varNombreActividad");
		vDetalleActividad = getIntent().getExtras().getString("varDetalleActividad");
		vImagenActividad = getIntent().getExtras().getString("varImagenActividad");
		vTonoActividad = getIntent().getExtras().getString("varTonoActividad");
		campo_imagenActividad=vImagenActividad;
	}
	
	public void CargarDatosEnLosElementos() {		
		if (vImagenActividad.equals("")) {
			ImageCargarImagenPeticion.setImageResource(R.drawable.picture_peticion);				
		}else{
			byte[] b = Base64.decode(vImagenActividad, Base64.DEFAULT);
			bitmap1 = BitmapFactory.decodeByteArray(b, 0, b.length);
			ImageCargarImagenPeticion.setImageBitmap(bitmap1);
		}			
		EdtNewNombrePeticion.setText(vNombreActividad);
		EdtNewDescripcionPeticion.setText(vDetalleActividad);
		TxtTono.setText(vTonoActividad);
	}
		
	//----OTROS METODOS
			
	public void RecogerParametros() {
		vIdPaciente = getIntent().getExtras().getLong("varIdPaciente");		
	}
		
	public Boolean ValidarDatos2() {
		tipoActividad = Select.from(TblTipoActividad.class).where(Condition.prop("tipo_actividad").eq("PETICION"),
				Condition.prop("Eliminado").eq(0)).first();

		campo_idTipoActividad=tipoActividad.getIdTipoActividad();
		campo_nombreActividad=EdtNewNombrePeticion.getText().toString().trim();
		campo_detalleActividad=EdtNewDescripcionPeticion.getText().toString().trim();
		campo_tonoActividad=TxtTono.getText().toString().trim();
				    		
		if((campo_nombreActividad.equals(""))||(campo_detalleActividad.equals(""))||(campo_imagenActividad.equals(""))||
			campo_tonoActividad.equals(getString(R.string.SeleccioneTono)))		    		 
		{
			DFIngresarDatos dialogo1 = new DFIngresarDatos();
			dialogo1.show(fragmentManager, "tagAlerta");
		}else {
			//VALIDAMOS QUE EL NOMBRE DE LA ACTIVIDAD NO EXISTA
			VerificarNombreAct();		
		}				
		return banderaGuardar;
	}
	
	public void VerificarNombreAct() {
		banderaGuardar=true;
		//BUSCAMOS SI YA EXISTE EL NOMBRE DE LA ACTIVIDAD A GUARDAR
		List<TblActividadPaciente>list_ActPac = Select.from(TblActividadPaciente.class).where(Condition.prop("id_paciente").eq(vIdPaciente),
																							  Condition.prop("Eliminado").eq(0)).list();
		int i=0;
		while (list_ActPac.size() > i) {
			TblActividadPaciente tablaActPac = new TblActividadPaciente();
			tablaActPac = list_ActPac.get(i);			
			TblActividades laActividad = Select.from(TblActividades.class).where(Condition.prop("id_actividad").eq(tablaActPac.getIdActividad()),
																				 Condition.prop("id_tipo_actividad").eq(campo_idTipoActividad),
																				 Condition.prop("nombre_actividad").eq(campo_nombreActividad),
																				 Condition.prop("Eliminado").eq(0)).first();
			if (laActividad!=null) {				
				banderaGuardar=false;
				i=list_ActPac.size();
				DFPeticionRepetida dialogo2 = new DFPeticionRepetida();
		        dialogo2.show(fragmentManager, "tagAlerta");
			}			
			i++;
		}			
	}
		
	public void BtnSubirImagen() {
		name = Environment.getExternalStorageDirectory() + "/test.jpg"; 		
		ImageCargarImagenPeticion.setOnClickListener(new View.OnClickListener() { 			
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
		if ((requestCode == SELECT_PICTURE)&&(resultCode == NewEditPeticionActivity.RESULT_OK)) {
			selectedImage = data.getData();    		
			try {
				bitmap1=MetodosValidacionesExtras.decodeSampledBitmapFromResource(selectedImage, NewEditPeticionActivity.this, 300, 300);
				ImageCargarImagenPeticion.setImageBitmap(MetodosValidacionesExtras.redimensionarImagenMaximo(bitmap1, 300, 300));

				bitmap2 = ((BitmapDrawable)ImageCargarImagenPeticion.getDrawable()).getBitmap();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, baos);  //PNG
				byte[] b = baos.toByteArray();
				campo_imagenActividad = Base64.encodeToString(b, Base64.DEFAULT);
			} catch (Exception e) {}
		}		
	}	
    
    public void	SeleccionarTonoAlarma() {
		RingtoneContainer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DFTonos4 dialogo = new DFTonos4();
				dialogo.show(fragmentManager, "tagAlerta");
			}			
		});
    }

	public AlertDialog SeleccionarTono() {
		final CharSequence[] listaTonos=getResources().getStringArray(R.array.opc_tonosPet);

		AlertDialog.Builder builder = new AlertDialog.Builder(NewEditPeticionActivity.this);
		builder.setTitle(R.string.tonosPet_prompt)
				.setSingleChoiceItems(listaTonos, 0, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						//INICIA EL TONO LA PRIMERA VEZ O DETIENE EL TONO ANTERIOR
						if (banderaTono.equals(false)) {
							banderaTono=true;
						}else {
							mPlayer.stop();
						}
						//GENERA EL SONIDO
						seleccion=(String)listaTonos[arg1];
						if (seleccion != null && !seleccion.equals("")) {
							int resultado=TonosClass.BuscarIdTonoNotificacion(seleccion);
							mPlayer=MediaPlayer.create(NewEditPeticionActivity.this, resultado);
							mPlayer.setLooping(false);
							mPlayer.start();
						}
					}
				})
				.setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						mPlayer.stop();
						TxtTono.setText(seleccion);
						dialog.cancel();
					}
				})
				.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						mPlayer.stop();
						dialog.cancel();
					}
				});
		AlertDialog alert=builder.create();
		alert.setCancelable(false);
		alert.setCanceledOnTouchOutside(false);
		return alert;
	}

	public void LimpiarElementos() {			
		EdtNewNombrePeticion.getText().clear();
		EdtNewDescripcionPeticion.getText().clear();
		TxtTono.setText(R.string.SeleccioneTono);
		ImageCargarImagenPeticion.setImageResource(R.drawable.picture_peticion);			
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
				Toast.makeText(NewEditPeticionActivity.this, "No hay Conexion a Internet", Toast.LENGTH_SHORT).show();
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