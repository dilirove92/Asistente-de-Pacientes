package com.Notifications.patientssassistant;


import com.Notifications.patientssassistant.dialogos.*;
import com.Notifications.patientssassistant.fragments.*;
import com.Notifications.patientssassistant.asynctask.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
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
import android.widget.TimePicker;
import android.widget.Toast;


public class NewEditObservacionActivity extends Activity {
		
	//VARIABLES PARA NUEVO REGISTRO
	private Long vIdPaciente;
	private String vNombrePaciente;
	private String vFotoPaciente;	 	
		
	//VARIABLES PARA EDITAR REGISTRO
	private Long vIdObservacion;
    private int vAnio;
    private int vMes;
    private int vDia;
    private int vHora;
    private int vMinutos;    
	private String vObservacion;
		
	//VARIABLES PARA DATEPICKER
	private int year;
	private int month;
	private int day;	
	private DatePickerDialog datePickerDialog;
			
	//VARIABLES PARA LOS TIMEPICKER	
	private int hours;
	private int minutes;
	private TimePickerDialog timePickerDialog;
	
	//VARIABLES QUE ALMACENAN LOS DATOS A GUARDAR
	private int campo_anio;
	private int campo_mes;
	private int campo_dia;  
	private int campo_hora=0;
	private int campo_minutos=0;	  
	private String campo_observacion;
	private String fn;
	
	//VARIABLES DE LOS ELEMENTOS DE LA IU		
	ImageView ImagenFotoP;
	TextView TxtNombreP;
	Button BtnNewFecha;
	Button BtnNewHora;	
	EditText EdtNewObservacion;	
	Button BtnNuevoEditar;
	
	//VARIABLES EXTRAS
	private Boolean banderaGuardar=false;
	private FragmentManager fragmentManager = getFragmentManager(); 
	
	
	public NewEditObservacionActivity() { super(); }

	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nueva_observacion); 
		
		ImagenFotoP=(ImageView)findViewById(R.id.imagenFotoP);
		TxtNombreP=(TextView)findViewById(R.id.txtNombreP);
		BtnNewFecha=(Button)findViewById(R.id.btnNewFecha);
		BtnNewHora=(Button)findViewById(R.id.btnNewHora);
		EdtNewObservacion=(EditText)findViewById(R.id.edtNewObservacion);		
		BtnNuevoEditar=(Button)findViewById(R.id.btnNewGuardar);
			
		NuevoVsEditar();      	
	}
	
	//METODO QUE SERVIRA PARA IR A NUEVO O EDITAR REGISTRO DE OBSERVACIONES
	public void NuevoVsEditar() {		
		Boolean opcion1 = MenPacObservacionesF.EsNuevo();		
		Boolean opcion2 = MenPacObservacionesF.EsEditar();
		
		if (opcion1.equals(true)) {
			NewEditObservacionActivity.this.setTitle(R.string.NewObservacion);
			RecogerParametrosFragMenPacO();
			CargarFotoNombrePaciente();			
			BtnNuevoO(); 
			//DATOS PARA EL DATEPICKERDIALOG Y TIMEPICKERDIALOG
			final Calendar calendar = new GregorianCalendar(); 		
	 		year = calendar.get(Calendar.YEAR);
	 		month = calendar.get(Calendar.MONTH);
	 		day = calendar.get(Calendar.DAY_OF_MONTH);
	 		hours = calendar.get(Calendar.HOUR);
			minutes = calendar.get(Calendar.MINUTE);
		}
		if (opcion2.equals(true)) {
			NewEditObservacionActivity.this.setTitle(R.string.EditObservacion);
			RecogerParametrosFragMenPacO2();
			CargarDatosEnLosElementos();
			BtnEditarO(); 
			//DATOS PARA EL DATEPICKERDIALOG Y TIMEPICKERDIALOG
	 		year = vAnio;
	 		month = vMes;
	 		day = vDia;
	 		hours = vHora;
			minutes = vMinutos;
		}	
		BtnDatePickerO();
		BtnTimePickerO();
	}
		
	//----METODOS PARA UN NUEVO REGISTRO
	
	public void RecogerParametrosFragMenPacO() {
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

	public void BtnNuevoO(){
		BtnNuevoEditar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (estaConectado()) {
					try {
						ValidarDatos();
						if (banderaGuardar.equals(true)) {
							//GUARDAR DATOS EN LA TABLA DE OBSERVACIONES
							ATObservaciones newObs = new ATObservaciones();
							newObs.new InsertarObservacion().execute("0", vIdPaciente.toString(), String.valueOf(campo_anio), String.valueOf(campo_mes),
									String.valueOf(campo_dia), String.valueOf(campo_hora), String.valueOf(campo_minutos), campo_observacion, "false");

							LimpiarElementos();
							Toast.makeText(NewEditObservacionActivity.this, R.string.GuardadoR, Toast.LENGTH_SHORT).show();
							Intent data = new Intent();
							setResult(RESULT_OK, data);
							finish();
						}
					} catch (Exception ex) {
						Toast.makeText(NewEditObservacionActivity.this, getString(R.string.ErrorGuardar) + ex.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}
	
	//----METODOS PARA EDITAR UN REGISTRO
	
	public void RecogerParametrosFragMenPacO2() {		
		RecogerParametrosFragMenPacO();
		vIdObservacion = getIntent().getExtras().getLong("varIdObservacion");
		vAnio = getIntent().getExtras().getInt("varAnio");
		vMes = getIntent().getExtras().getInt("varMes");
		vDia = getIntent().getExtras().getInt("varDia");
		vHora = getIntent().getExtras().getInt("varHora");
		vMinutos = getIntent().getExtras().getInt("varMinutos");		
		vObservacion = getIntent().getExtras().getString("varObservacion");
	}
	
	public void CargarDatosEnLosElementos() {
		CargarFotoNombrePaciente();
		BtnNewFecha.setText(String.format("%02d/%02d/%02d", vDia, vMes + 1, vAnio));
		BtnNewHora.setText(String.format("%02d:%02d", vHora, vMinutos));
		EdtNewObservacion.setText(vObservacion);
		campo_anio=vAnio;
		campo_mes=vMes;
		campo_dia=vDia;
		campo_hora=vHora;
		campo_minutos=vMinutos;
	}
	
	public void BtnEditarO(){
		BtnNuevoEditar.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if(estaConectado()) {
					try {
						ValidarDatos();
						if (banderaGuardar.equals(true)) {
							//EDITANDO DATOS EN LA TABLA DE OBSERVACIONES
							ATObservaciones newObs = new ATObservaciones();
							newObs.new ActualizarObservacion().execute(String.valueOf(vIdObservacion), String.valueOf(vIdPaciente), String.valueOf(campo_anio), String.valueOf(campo_mes),
									String.valueOf(campo_dia), String.valueOf(campo_hora), String.valueOf(campo_minutos), campo_observacion, "false");

							LimpiarElementos();
							Toast.makeText(NewEditObservacionActivity.this, R.string.EditadoR, Toast.LENGTH_SHORT).show();
							Intent data = new Intent();
							setResult(RESULT_OK, data);
							finish();
						}
					} catch (Exception ex) {
						Toast.makeText(NewEditObservacionActivity.this, getString(R.string.ErrorEditar) + ex.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}
		
	//----METODOS TANTO PARA NUEVO O EDITAR REGISTRO
	
	public Boolean ValidarDatos() {					 
		campo_observacion=EdtNewObservacion.getText().toString().trim(); 
		fn=BtnNewFecha.getText().toString();
		
		if((fn.equals(getString(R.string.FijarFecha)))||(campo_observacion.equals(""))) 
		{
			DFNueObs dialogo1 = new DFNueObs();
	        dialogo1.show(fragmentManager, "tagAlerta");		    			
		}else{					
			banderaGuardar=true;
		}
		
		return banderaGuardar;
	}
	
	public void BtnDatePickerO() {		
 		BtnNewFecha.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) { DialogoDatePicker(); }
		});
	}
	
	public void DialogoDatePicker() {
 		datePickerDialog = new DatePickerDialog(NewEditObservacionActivity.this, onDateSetListener1, year, month, day);
 		datePickerDialog.setTitle(R.string.Fecha);
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
			BtnNewFecha.setText(String.format("%02d/%02d/%02d", dayOfMonth, monthOfYear+1, yearr));
			//DATOS PARA EL DATEPICKERDIALOG
			year=yearr;  month=monthOfYear;  day=dayOfMonth;
			//DATOS PARA GUARDAR EN LA TABLA		
			campo_anio=yearr;  campo_mes=monthOfYear;  campo_dia=dayOfMonth;
		}		
	};	
	
	public void BtnTimePickerO() {
 		//TIMEPICKER PARA HORA	       
        BtnNewHora.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v)  { DialogoTimePicker(); }
		});
	}
	
	public void DialogoTimePicker() {	
		timePickerDialog = new TimePickerDialog(NewEditObservacionActivity.this, onTimeSetListener1, hours, minutes, true);
 		timePickerDialog.setTitle(R.string.Hora);
 		timePickerDialog.setIcon(R.drawable.reloj);
 		timePickerDialog.setCanceledOnTouchOutside(false);	
		
		timePickerDialog.setOnShowListener(new OnShowListener(){	
		    @Override
		    public void onShow(DialogInterface arg0) {  }});
	        
		timePickerDialog.setOnCancelListener(new OnCancelListener(){	
		    @Override
		   	public void onCancel(DialogInterface arg0) {  }});
	       
		timePickerDialog.setOnDismissListener(new OnDismissListener(){	
		    @Override
		    public void onDismiss(DialogInterface arg0) {  }});
 		
		timePickerDialog.show();		
	}	
	
	OnTimeSetListener onTimeSetListener1 = new OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			BtnNewHora.setText(String.format("%02d:%02d", hourOfDay, minute));	
			//DATOS PARA EL TIMEPICKERDIALOG
			hours=hourOfDay;  minutes=minute;
			//DATOS PARA GUARDAR EN LA TABLA		
			campo_hora=hourOfDay;  campo_minutos=minute;  
		}
	};	
	
	public void LimpiarElementos() {
		BtnNewFecha.setText(R.string.FijarFecha);
		BtnNewHora.setText(R.string.FijarHora);
		EdtNewObservacion.getText().clear();		
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
				Toast.makeText(NewEditObservacionActivity.this, "No hay Conexion a Internet", Toast.LENGTH_SHORT).show();
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