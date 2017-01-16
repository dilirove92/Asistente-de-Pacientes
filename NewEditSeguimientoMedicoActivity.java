package com.Notifications.patientssassistant;


import com.Notifications.patientssassistant.dialogos.*;
import com.Notifications.patientssassistant.fragments.*;
import com.Notifications.patientssassistant.asynctask.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class NewEditSeguimientoMedicoActivity extends Activity {
	
	//VARIABLES PARA NUEVO REGISTRO
	private Long vIdPaciente;
	private String vNombrePaciente;
	private String vFotoPaciente;	 	
		
    //VARIABLES PARA EDITAR REGISTRO
	private Long vIdSeguimientoMedico;
    private int vAnio;
    private int vMes;
    private int vDia;
	private String vUnidadMedica;
	private String vDoctor;
	private String vDiagnostico;
	private String vTratamientoMedico;
	private String vAlimentacionSugerida;
	
    //VARIABLES PARA DATEPICKER
	private int year;
	private int month;
	private int day;	
	private DatePickerDialog datePickerDialog;
			
	//VARIABLES QUE ALMACENAN LOS DATOS A GUARDAR
	private int campo_anio;
	private int campo_mes;
	private int campo_dia;  		    		    		  		
	private String campo_uniMed;  
	private String campo_doctor;  
	private String campo_diagnostico;  
	private String campo_tratMed;  
	private String campo_alimSug;
	private String fecha;
	
	//VARIABLES DE LOS ELEMENTOS DE LA IU	
	ImageView ImagenFotoP;
	TextView TxtNombreP;
	Button BtnNewFechaCM;
	EditText EdtNewUnidMedi;
	EditText EdtNewDoctor;
	EditText EdtNewDiagnostico;
	EditText EdtNewTrataMedi;
	EditText EdtNewAlimSuge;
	Button BtnNuevoEditar;
	
	//VARIABLES EXTRAS
	private Boolean banderaGuardar=false;
	private FragmentManager fragmentManager = getFragmentManager(); 
	
	
	public NewEditSeguimientoMedicoActivity() { super(); }

	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nuevo_seguimiento_medico); 
		
		ImagenFotoP=(ImageView)findViewById(R.id.imagenFotoP);
		TxtNombreP=(TextView)findViewById(R.id.txtNombreP);		
		BtnNewFechaCM=(Button)findViewById(R.id.btnNewFechaCM);
		EdtNewUnidMedi=(EditText)findViewById(R.id.edtNewUnidMedi);
		EdtNewDoctor=(EditText)findViewById(R.id.edtNewDoctor);
		EdtNewDiagnostico=(EditText)findViewById(R.id.edtNewDiagnostico);
		EdtNewTrataMedi=(EditText)findViewById(R.id.edtNewTrataMedi);
		EdtNewAlimSuge=(EditText)findViewById(R.id.edtNewAlimSuge);
		BtnNuevoEditar=(Button)findViewById(R.id.btnNewGuardar);
					
		NuevoVsEditar();
	}
	
	//METODO QUE SERVIRA PARA IR A NUEVO O EDITAR REGISTRO DEL SEGUIMIENTO MEDICO DEL PACIENTE
	public void NuevoVsEditar() {		
		Boolean opcion1 = MenPacSeguimientoMedicoF.EsNuevo();		
		Boolean opcion2 = MenPacSeguimientoMedicoF.EsEditar();
		
		if (opcion1.equals(true)) {
			NewEditSeguimientoMedicoActivity.this.setTitle(R.string.NewSegMedico);
			RecogerParametrosFragMenPacSM();
			CargarFotoNombrePaciente();			
			BtnNuevoSM(); 
			//DATOS PARA EL DATEPICKERDIALOG
			final Calendar calendar = new GregorianCalendar();
	 		year = calendar.get(Calendar.YEAR);
	 		month = calendar.get(Calendar.MONTH);
	 		day = calendar.get(Calendar.DAY_OF_MONTH);
		}
		if (opcion2.equals(true)) {
			NewEditSeguimientoMedicoActivity.this.setTitle(R.string.EditSegMedico);
			RecogerParametrosFragMenPacSM2();		
			CargarDatosEnLosElementos();			
			BtnEditarSM();	
			//DATOS PARA EL DATEPICKERDIALOG			 		
	 		year = vAnio;
	 		month = vMes;
	 		day = vDia;
		}
		BtnDatePickerF();
	}
		
	//----METODOS PARA UN NUEVO REGISTRO
	
	public void RecogerParametrosFragMenPacSM() {
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
	
	public void BtnNuevoSM(){		
		BtnNuevoEditar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (estaConectado()) {
					try {
						ValidarDatos();
						if (banderaGuardar.equals(true)) {
							//GUARDAR DATOS EN LA TABLA DE SEGUIMIENTO MEDICO
							ATSeguimientoMedico segMed = new ATSeguimientoMedico();
							segMed.new InsertarSeguimientoMedico().execute("0", vIdPaciente.toString(), String.valueOf(campo_anio), String.valueOf(campo_mes),
									String.valueOf(campo_dia), campo_uniMed, campo_doctor, campo_diagnostico,
									campo_tratMed, campo_alimSug, "false");

							LimpiarElementos();
							Toast.makeText(NewEditSeguimientoMedicoActivity.this, R.string.GuardadoR, Toast.LENGTH_SHORT).show();
							Intent data = new Intent();
							setResult(RESULT_OK, data);
							finish();
						}
					} catch (Exception ex) {
						Toast.makeText(NewEditSeguimientoMedicoActivity.this, getString(R.string.ErrorGuardar) + ex.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}
	
	//----METODOS PARA EDITAR UN REGISTRO
	
	public void RecogerParametrosFragMenPacSM2() {		
		RecogerParametrosFragMenPacSM();
		vIdSeguimientoMedico = getIntent().getExtras().getLong("varIdSeguimientoMedico");
		vAnio = getIntent().getExtras().getInt("varAnio");
		vMes = getIntent().getExtras().getInt("varMes");
		vDia = getIntent().getExtras().getInt("varDia");
		vUnidadMedica = getIntent().getExtras().getString("varUnidadMedica");
		vDoctor = getIntent().getExtras().getString("varDoctor");
		vDiagnostico = getIntent().getExtras().getString("varDiagnostico");
		vTratamientoMedico = getIntent().getExtras().getString("varTratamientoMedico");
		vAlimentacionSugerida = getIntent().getExtras().getString("varAlimentacionSugerida");
	}
	
	public void CargarDatosEnLosElementos() {		
		CargarFotoNombrePaciente();		
		BtnNewFechaCM.setText(String.format("%02d/%02d/%02d", vDia, vMes+1, vAnio));
		EdtNewUnidMedi.setText(vUnidadMedica);
		EdtNewDoctor.setText(vDoctor);
		EdtNewDiagnostico.setText(vDiagnostico);
		EdtNewTrataMedi.setText(vTratamientoMedico);
		EdtNewAlimSuge.setText(vAlimentacionSugerida);	
		campo_anio=vAnio;
		campo_mes=vMes;
		campo_dia=vDia;
	}
	
	public void BtnEditarSM(){		
		BtnNuevoEditar.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if(estaConectado()) {
					try {
						ValidarDatos();
						if (banderaGuardar.equals(true)) {
							//EDITANDO DATOS EN LA TABLA DE SEGUIMIENTO MEDICO
							ATSeguimientoMedico segMed = new ATSeguimientoMedico();
							segMed.new ActualizarSeguimientoMedico().execute(vIdSeguimientoMedico.toString(), vIdPaciente.toString(), String.valueOf(campo_anio), String.valueOf(campo_mes),
									String.valueOf(campo_dia), campo_uniMed, campo_doctor, campo_diagnostico,
									campo_tratMed, campo_alimSug, "false");

							LimpiarElementos();
							Toast.makeText(NewEditSeguimientoMedicoActivity.this, R.string.EditadoR, Toast.LENGTH_SHORT).show();
							Intent data = new Intent();
							setResult(RESULT_OK, data);
							finish();
						}
					} catch (Exception ex) {
						Toast.makeText(NewEditSeguimientoMedicoActivity.this, getString(R.string.ErrorEditar) + ex.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}
		
	//----METODOS TANTO PARA NUEVO O EDITAR REGISTRO
	
	public Boolean ValidarDatos() {					
		campo_uniMed=EdtNewUnidMedi.getText().toString().trim();  
		campo_doctor=EdtNewDoctor.getText().toString().trim();  
		campo_diagnostico=EdtNewDiagnostico.getText().toString().trim();  
		campo_tratMed=EdtNewTrataMedi.getText().toString().trim();  
		campo_alimSug=EdtNewAlimSuge.getText().toString().trim();
		fecha=BtnNewFechaCM.getText().toString();
		
		if((fecha.equals(getString(R.string.FijarFecha)))||(campo_uniMed.equals(""))||
		   (campo_doctor.equals(""))||(campo_diagnostico.equals("")))		    		 
		{
			DFNueSM dialogo1 = new DFNueSM();
	        dialogo1.show(fragmentManager, "tagAlerta");
		}else{
			banderaGuardar=true;
		}
		
		return banderaGuardar;
	}
	
	public void BtnDatePickerF() {
		BtnNewFechaCM.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) { DialogoDatePicker(); }
		});
	}
	
	public void DialogoDatePicker() {		
 		datePickerDialog = new DatePickerDialog(NewEditSeguimientoMedicoActivity.this, onDateSetListener1, year, month, day);
 		datePickerDialog.setTitle(R.string.FechaCM);
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
			BtnNewFechaCM.setText(String.format("%02d/%02d/%02d", dayOfMonth, monthOfYear+1, yearr));
			//DATOS PARA EL DATEPICKERDIALOG
			year=yearr;  month=monthOfYear;  day=dayOfMonth;
			//DATOS PARA GUARDAR EN LA TABLA		
			campo_anio=yearr;  campo_mes=monthOfYear;  campo_dia=dayOfMonth;
		}		
	};
		
	public void LimpiarElementos() {			
		BtnNewFechaCM.setText(R.string.FijarFecha);
		EdtNewUnidMedi.getText().clear();
		EdtNewDoctor.getText().clear();
		EdtNewDiagnostico.getText().clear();
		EdtNewTrataMedi.getText().clear();
		EdtNewAlimSuge.getText().clear();
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
				Toast.makeText(NewEditSeguimientoMedicoActivity.this, "No hay Conexion a Internet", Toast.LENGTH_SHORT).show();
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