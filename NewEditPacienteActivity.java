package com.Notifications.patientssassistant;


import com.Notifications.patientssassistant.adapters.*;
import com.Notifications.patientssassistant.dialogos.*;
import com.Notifications.patientssassistant.fragments.*;
import com.Notifications.patientssassistant.tables.*;
import com.Notifications.patientssassistant.asynctask.*;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ExecutionException;
import com.orm.query.Condition;
import com.orm.query.Select;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


public class NewEditPacienteActivity extends Activity {
		  
	//VARIABLES PARA NUEVO REGISTRO
	private Long vDependeDeCuidador;
        
    //VARIABLES PARA EDITAR REGISTRO    
    private Long vIdPaciente;
    private String vUsuarioP;		
    private String vCiP;
    private String vNombreApellidoP;
    private int vAnio;
    private int vMes;
    private int vDia;
    private int vEdad;
    private String vEstadoCivilP;
    private String vNivelEstudioP;
    private String vMotivoIngresoP;
    private String vTipoPacienteP;    
    private String vFotoP;
      
    //VARIABLES PARA DATEPICKER
	private int year;
	private int month;
	private int day;	
	private DatePickerDialog datePickerDialog;
    	
    //VARIABLES PARA FOTO
	private static int TAKE_PICTURE = 1;
	private static int SELECT_PICTURE = 2;
	private String name = "";
	private Uri selectedImage;
	private Bitmap bitmap1, bitmap2;
	private String campo_fotoP=""; 
	
	//VARIABLES QUE ALMACENAN LOS DATOS A GUARDAR
	private String campo_usuarioP;
	private String campo_nombresP;	
	private String campo_ciP;	
	private int campo_anio;
	private int campo_mes;
	private int campo_dia;
	private int campo_edad;
	private String campo_estaCiviP;
	private String campo_nivEstuP;
	private String campo_motiIngrP;
	private String campo_tipoPaciP;	
	private String fn;

	//VARIABLES DE LOS ELEMENTOS DE LA IU
	ImageView ImageFotoP;
	EditText EdtNewUsuario;
	EditText EdtNewNombresApellidosP;
	EditText EdtNewCiP;
	Button BtnNewFecNac;
	EditText EdtEdad;
	Spinner CmbNewEstaCiviP;
	Spinner CmbNewNivEstuP;
	EditText EdtNewMotiIngrP;
	Spinner CmbNewTipoPaciP;
	Button BtnNuevoEditar;
	
	//VARIABLES EXTRAS
	private TblPacientes editar_paciente;
	private FragmentManager fragmentManager = getFragmentManager(); 

	
	public NewEditPacienteActivity() { super(); }

	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nuevo_paciente);
		
		ImageFotoP=(ImageView)findViewById(R.id.imageFotoP);
		EdtNewUsuario=(EditText)findViewById(R.id.edtNewUsuario);
		EdtNewNombresApellidosP=(EditText)findViewById(R.id.edtNewNombresApellidosP);
		EdtNewCiP=(EditText)findViewById(R.id.edtNewCiP);
		BtnNewFecNac=(Button)findViewById(R.id.btnNewFecNac);
		EdtEdad=(EditText)findViewById(R.id.edtEdad);
		CmbNewEstaCiviP=(Spinner)findViewById(R.id.cmbNewEstaCiviP);
		CmbNewNivEstuP=(Spinner)findViewById(R.id.cmbNewNivEstuP);
		EdtNewMotiIngrP=(EditText)findViewById(R.id.edtNewMotiIngrP);
		CmbNewTipoPaciP=(Spinner)findViewById(R.id.cmbNewTipoPaciP);
		BtnNuevoEditar=(Button)findViewById(R.id.btnNewGuardar);
		
		NuevoVsEditar();
	}
	
	//METODO QUE SERVIRA PARA IR A NUEVO O EDITAR REGISTRO DE PACIENTE
	public void NuevoVsEditar() {
		Boolean opcion1= MpPacientesF.EsNuevo();
		Boolean opcion2= MpPacientesF.EsEditar();		
		Boolean opcion3= TabPaciente1DpF.EsEditar();
		
		if (opcion1.equals(true)) {
			NewEditPacienteActivity.this.setTitle(R.string.NewPaciente);
			RecogerParametrosFragMpP2();
			CargarOpcSpinnerEC();
			CargarOpcSpinnerNE();
			CargarOpcSpinnerTP();
			BtnNuevoPac();
			//DATOS PARA EL DATEPICKERDIALOG
			final Calendar calendar = new GregorianCalendar(); 		
	 		year = calendar.get(Calendar.YEAR);
	 		month = calendar.get(Calendar.MONTH);
	 		day = calendar.get(Calendar.DAY_OF_MONTH);
		}
		if ((opcion2.equals(true))||(opcion3.equals(true))) {
			NewEditPacienteActivity.this.setTitle(R.string.EditPaciente);
			RecogerParametrosFragMpP3yTabPacDp();
			CargarDatosEnLosElementos();
			SpinnersTouch();
			BtnEditarPac();
			//DATOS PARA EL DATEPICKERDIALOG			 		
	 		year = vAnio;
	 		month = vMes;
	 		day = vDia;
		}	
		BtnDatePickerFN();
		ImageBtnSubirFoto();
	}
		
	//----METODOS PARA UN NUEVO REGISTRO
	
	public void RecogerParametrosFragMpP2() {
		vDependeDeCuidador = getIntent().getExtras().getLong("varDependeDe");
	}
    
	public void BtnNuevoPac(){		
		BtnNuevoEditar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(estaConectado()) {
					try {
						GetDatosDeLosElementos();
						if ((campo_usuarioP.equals("")) || (campo_nombresP.equals("")) || (campo_nivEstuP.equals("")) || (campo_motiIngrP.equals("")) || (campo_tipoPaciP.equals(""))) {
							DFNuePac dialogo1 = new DFNuePac();
							dialogo1.show(fragmentManager, "tagAlerta");
						} else {
							Boolean banderaGuardar = VerificarUser();
							if (banderaGuardar.equals(true)) {
								//GUARDAR DATOS EN LA TABLA DE PACIENTES
								ATPacientes paciente = new ATPacientes();
								long idP = paciente.new InsertarPacientes().execute("0", campo_usuarioP, campo_ciP, campo_nombresP, String.valueOf(campo_anio), String.valueOf(campo_mes), String.valueOf(campo_dia),
										campo_estaCiviP, campo_nivEstuP, campo_motiIngrP, campo_tipoPaciP, String.valueOf(campo_edad), campo_fotoP, "false").get().longValue();

								//GUARDAR DATOS EN LA TABLA DE PERMISOS
								ATPermisos permiso = new ATPermisos();
								permiso.new InsertarPermiso().execute("0", String.valueOf(vDependeDeCuidador), String.valueOf(idP), "true", "true", "true", "false");

								LimpiarElementos();
								Toast.makeText(NewEditPacienteActivity.this, R.string.GuardadoR, Toast.LENGTH_SHORT).show();
								Intent data = new Intent();
								data.putExtra("resp", true);
								setResult(RESULT_OK, data);
								finish();
							}
						}
					} catch (Exception ex) {
						Toast.makeText(NewEditPacienteActivity.this, getString(R.string.ErrorGuardar) + ex.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}
	
	//----METODOS PARA EDITAR UN REGISTRO
	
	public void RecogerParametrosFragMpP3yTabPacDp() {
		vIdPaciente = getIntent().getExtras().getLong("varIdPaciente");
		vUsuarioP = getIntent().getExtras().getString("varUsuarioP");		
		vCiP = getIntent().getExtras().getString("varCiP");
		vNombreApellidoP = getIntent().getExtras().getString("varNombreApellidoP");		
		vAnio = getIntent().getExtras().getInt("varAnio");
		vMes = getIntent().getExtras().getInt("varMes");
		vDia = getIntent().getExtras().getInt("varDia");
		vEdad = getIntent().getExtras().getInt("varEdad");
		vEstadoCivilP = getIntent().getExtras().getString("varEstadoCivilP");
		vNivelEstudioP = getIntent().getExtras().getString("varNivelEstudioP");
		vMotivoIngresoP = getIntent().getExtras().getString("varMotivoIngresoP");
		vTipoPacienteP = getIntent().getExtras().getString("varTipoPacienteP");
		vFotoP = getIntent().getExtras().getString("varFotoP");	
		campo_fotoP=vFotoP;
		campo_anio=vAnio;
		campo_mes=vMes;
		campo_dia=vDia;
	}
	
	public void CargarDatosEnLosElementos() {	
		try {
			if (vFotoP.equals("")) {
				ImageFotoP.setImageResource(R.drawable.user_foto);				
			}else{
				byte[] b = Base64.decode(vFotoP, Base64.DEFAULT);
				bitmap1 = BitmapFactory.decodeByteArray(b, 0, b.length);
				ImageFotoP.setImageBitmap(bitmap1);
			}		
	    	EdtNewUsuario.setText(vUsuarioP);
			EdtNewNombresApellidosP.setText(vNombreApellidoP);
			EdtNewCiP.setText(vCiP);
			EdtNewMotiIngrP.setText(vMotivoIngresoP);
			if(vDia==0 && vMes==0 && vAnio==0){
				BtnNewFecNac.setText(R.string.FijarFecha);
			}else{
				BtnNewFecNac.setText(String.format("%02d/%02d/%02d", vDia, vMes+1, vAnio));
			}
			EdtEdad.setText(Integer.toString(vEdad));
			
	    	//CARGANDO LOS DATOS GUARDADOS EN LOS SPINNERS    	
	    	String[] opcGuardadaEstCiv={vEstadoCivilP}; 	
	    	CmbNewEstaCiviP.setAdapter(new AdapterSpinnerSimple(NewEditPacienteActivity.this, R.layout.adaptador_spinner, opcGuardadaEstCiv)); 
	    	String[] opcGuardadaNivEst={vNivelEstudioP}; 	
	    	CmbNewNivEstuP.setAdapter(new AdapterSpinnerSimple(NewEditPacienteActivity.this, R.layout.adaptador_spinner, opcGuardadaNivEst));    	
	       	String[] opcGuardadaTipPac={vTipoPacienteP}; 	
	    	CmbNewTipoPaciP.setAdapter(new AdapterSpinnerSimple(NewEditPacienteActivity.this, R.layout.adaptador_spinner, opcGuardadaTipPac));			
		} catch (Exception e) { }
	}
	
	public void SpinnersTouch() {
		CmbNewEstaCiviP.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (CmbNewEstaCiviP.isPressed()) {
					CargarOpcSpinnerEC();
				}
				return false;
			}
		});
		CmbNewNivEstuP.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (CmbNewNivEstuP.isPressed()) {
					CargarOpcSpinnerNE();
				}
				return false;
			}
		});	
		CmbNewTipoPaciP.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (CmbNewTipoPaciP.isPressed()) {
					CargarOpcSpinnerTP();
				}
				return false;
			}
		});			
	}
	
	public void BtnEditarPac(){		
		BtnNuevoEditar.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if(estaConectado()) {
					try {
						GetDatosDeLosElementos();
						if ((campo_usuarioP.equals("")) || (campo_nombresP.equals("")) || (campo_nivEstuP.equals("")) || (campo_motiIngrP.equals("")) || (campo_tipoPaciP.equals(""))) {
							DFNuePac dialogo1 = new DFNuePac();
							dialogo1.show(fragmentManager, "tagAlerta");
						} else {
							editar_paciente = Select.from(TblPacientes.class).where(Condition.prop("id_paciente").eq(vIdPaciente)).first();

							if (editar_paciente.getUsuarioP().equals(campo_usuarioP)) {
								EditandoRegistros();
							} else {
								if (!editar_paciente.getUsuarioP().equals(campo_usuarioP)) {
									Boolean banderaGuardar = VerificarUser();
									if (banderaGuardar.equals(true)) {
										EditandoRegistros();
									}
								}
							}
						}
					} catch (Exception ex) {
						Toast.makeText(NewEditPacienteActivity.this, getString(R.string.ErrorEditar) + ex.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}
	
	public void EditandoRegistros() {
		//EDITANDO DATOS EN LA TABLA DE PACIENTES
		Boolean respu=false;
		ATPacientes paciente = new ATPacientes();
		try {
			respu=paciente.new ActualizarPaciente().execute(String.valueOf(vIdPaciente), campo_usuarioP, campo_ciP, campo_nombresP, String.valueOf(campo_anio), String.valueOf(campo_mes), String.valueOf(campo_dia), 
															campo_estaCiviP, campo_nivEstuP, campo_motiIngrP, campo_tipoPaciP, String.valueOf(campo_edad), campo_fotoP, "false").get().booleanValue();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		LimpiarElementos();
		Toast.makeText(NewEditPacienteActivity.this, R.string.EditadoR, Toast.LENGTH_SHORT).show();
		Intent data = new Intent();
		setResult(RESULT_OK, data);
		finish();
	}
				
	//----METODOS TANTO PARA NUEVO O EDITAR REGISTRO
	
	public void GetDatosDeLosElementos() {
		try {
			campo_usuarioP=EdtNewUsuario.getText().toString().trim();
			campo_nombresP=EdtNewNombresApellidosP.getText().toString().trim();	
			campo_ciP=EdtNewCiP.getText().toString().trim();			
			campo_estaCiviP=CmbNewEstaCiviP.getSelectedItem().toString();
			campo_nivEstuP=CmbNewNivEstuP.getSelectedItem().toString();
			campo_motiIngrP=EdtNewMotiIngrP.getText().toString().trim().trim();
			campo_tipoPaciP=CmbNewTipoPaciP.getSelectedItem().toString();
			campo_edad=Integer.parseInt(EdtEdad.getText().toString().trim());
			fn=BtnNewFecNac.getText().toString();
		}catch (Exception e) { }
	}

	public Boolean VerificarUser() {
		//BUSCAMOS SI YA ESTA EN USO EL NOMBRE DEL USUARIO A GUARDAR
		Boolean banderaGuardar=false;
		Boolean usuarioRepetido=false;
		ATPacientes pac= new ATPacientes();
		try {
			usuarioRepetido=pac.new ExisteUserPaciente().execute(campo_usuarioP).get().booleanValue();
			if (usuarioRepetido.equals(false)) {
				banderaGuardar=true;
			} else {
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

	public void CargarOpcSpinnerEC() {
		String[] OpcEstadoCivil=getResources().getStringArray(R.array.opc_estadoCivil);		
    	CmbNewEstaCiviP.setAdapter(new AdapterSpinnerSimple(NewEditPacienteActivity.this, R.layout.adaptador_spinner, OpcEstadoCivil));
	}
	
	public void CargarOpcSpinnerNE() {
		String[] OpcNivelEstudio=getResources().getStringArray(R.array.opc_nivelEstudio);		
    	CmbNewNivEstuP.setAdapter(new AdapterSpinnerSimple(NewEditPacienteActivity.this, R.layout.adaptador_spinner, OpcNivelEstudio));
	}
	
	public void CargarOpcSpinnerTP() {
		String[] OpcTipoPaciente=getResources().getStringArray(R.array.opc_tipoPaciente);		
    	CmbNewTipoPaciP.setAdapter(new AdapterSpinnerSimple(NewEditPacienteActivity.this, R.layout.adaptador_spinner, OpcTipoPaciente));
	}
	
	public void BtnDatePickerFN() {
 		BtnNewFecNac.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogoDatePicker();
			}
		});	 		
	}
		
	public void DialogoDatePicker() {
 		datePickerDialog = new DatePickerDialog(NewEditPacienteActivity.this, onDateSetListener1, year, month, day);
 		datePickerDialog.setTitle(R.string.FecNac);
 		datePickerDialog.setIcon(R.drawable.calendario);
 		datePickerDialog.setCanceledOnTouchOutside(false);
		
 		datePickerDialog.setOnShowListener(new OnShowListener(){	
		    @Override
		    public void onShow(DialogInterface arg0) {  }});
	        
 		datePickerDialog.setOnCancelListener(new OnCancelListener(){	
		    @Override
		   	public void onCancel(DialogInterface arg0) {  }});
	       
 		datePickerDialog.setOnDismissListener(new OnDismissListener(){	
		    @Override
		    public void onDismiss(DialogInterface arg0) {  }});
		
		datePickerDialog.show();
	}
	
    OnDateSetListener onDateSetListener1 = new OnDateSetListener(){
		@Override
		public void onDateSet(DatePicker view, int yearr, int monthOfYear, int dayOfMonth) {
			BtnNewFecNac.setText(String.format("%02d/%02d/%02d", dayOfMonth, monthOfYear+1, yearr));
			//DATOS PARA EL DATEPICKERDIALOG
			year=yearr;  month=monthOfYear;  day=dayOfMonth;
			//DATOS PARA GUARDAR EN LA TABLA		
			campo_anio=yearr;  campo_mes=monthOfYear;  campo_dia=dayOfMonth;
			//LLAMAR A METODO PARA CALCULAR EDAD
			int edad = calcularEdad(BtnNewFecNac.getText().toString().trim());
			EdtEdad.setText(Integer.toString(edad));
		}		
	};
	
	public static Integer calcularEdad(String fecha){
		Date fechaNac=null;
		int anio=0;
		try {
			/*Se puede cambiar la mascara por el formato de la fecha que se quiera recibir, por ejemplo 
			 año mes dia "yyyy-MM-dd" en este caso es dia mes año*/
			fechaNac = new SimpleDateFormat("dd/MM/yyyy").parse(fecha);
			Calendar fechaNacimiento = new GregorianCalendar();
			//Se crea un objeto con la fecha actual
			Calendar fechaActual = new GregorianCalendar();
			//Se asigna la fecha recibida a la fecha de nacimiento.
			fechaNacimiento.setTime(fechaNac);
			fechaActual.getTime();
			//Se restan la fecha actual y la fecha de nacimiento
			anio = fechaActual.get(Calendar.YEAR) - fechaNacimiento.get(Calendar.YEAR);
			int mes = fechaActual.get(Calendar.MONTH) - fechaNacimiento.get(Calendar.MONTH);
			int dia = fechaActual.get(Calendar.DATE) - fechaNacimiento.get(Calendar.DATE);
			//Se ajusta el año dependiendo el mes y el dia
			if(mes<0 || (mes==0 && dia<0)){
				anio--;
			}			
		}catch (Exception ex) {
			System.out.println("Error:"+ex);
		}		
		//Regresa la edad en base a la fecha de nacimiento
		return anio;
	}	
	
	public void ImageBtnSubirFoto() {
		name = Environment.getExternalStorageDirectory() + "/test.jpg"; 		
		ImageFotoP.setOnClickListener(new View.OnClickListener() { 			
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
    	if ((requestCode == SELECT_PICTURE)&&(resultCode == NewEditPacienteActivity.RESULT_OK)){
			selectedImage = data.getData();    		
			try {
				bitmap1=MetodosValidacionesExtras.decodeSampledBitmapFromResource(selectedImage, NewEditPacienteActivity.this, 120, 120);
				ImageFotoP.setImageBitmap(MetodosValidacionesExtras.redimensionarImagenMaximo(bitmap1, 120, 120));

				bitmap2 = ((BitmapDrawable)ImageFotoP.getDrawable()).getBitmap();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, baos);  //PNG
				byte[] b = baos.toByteArray();
				campo_fotoP = Base64.encodeToString(b, Base64.DEFAULT);
			} catch (Exception e) {}
    	}
	}
  
	public void LimpiarElementos() {
		ImageFotoP.setImageResource(R.drawable.user_foto);	
		EdtNewUsuario.getText().clear();
		EdtNewNombresApellidosP.getText().clear();
		EdtNewCiP.getText().clear();
		BtnNewFecNac.setText(R.string.FijarFecha);
		EdtEdad.getText().clear();
		EdtNewMotiIngrP.getText().clear();			
		CargarOpcSpinnerEC();	
		CargarOpcSpinnerNE();
		CargarOpcSpinnerTP();
	}

    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {	
    	if (keyCode == KeyEvent.KEYCODE_BACK) {
    		Intent data = new Intent();
    		data.putExtra("resp", true);
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
				Toast.makeText(NewEditPacienteActivity.this, "No hay Conexion a Internet", Toast.LENGTH_SHORT).show();
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