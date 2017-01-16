package com.Notifications.patientssassistant;


import com.Notifications.patientssassistant.adapters.*;
import com.Notifications.patientssassistant.asynctask.*;
import com.Notifications.patientssassistant.alarmas.*;
import com.Notifications.patientssassistant.dialogos.*;
import com.Notifications.patientssassistant.fragments.*;
import com.Notifications.patientssassistant.tables.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.orm.query.Condition;
import com.orm.query.Select;


public class NewEditEventoCuidadorActivity extends Activity {
						
	//VARIABLES PARA NUEVO REGISTRO
	private Long vIdCuidador;
		
	//VARIABLES PARA EDITAR REGISTRO
	private Long vIdEventoC;
	private Long vIdActividad;
    private int vAnioE;
    private int vMesE;
    private int vDiaE;
    private int vHoraE;
    private int vMinutosE;    
    private int vAnioR;
    private int vMesR;
    private int vDiaR;
    private int vHoraR;
    private int vMinutosR;    
	private String vLugar;
	private String vDescripcion;
	private String vTono;
	private Boolean vAlarma;	
	private Long vIdAlarma;
	
	//VARIABLES PARA DATEPICKER
	private static SimpleDateFormat sdfFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private int yearE, monthE, dayE;
	private int yearR, monthR, dayR;
	private DatePickerDialog datePickerDialog;
			
	//VARIABLES PARA LOS TIMEPICKER	
	private int hoursE, minutesE;
	private int hoursR, minutesR;
	private TimePickerDialog timePickerDialog;
			
	//VARIABLES QUE ALMACENAN LOS DATOS A GUARDAR	
	private Long campo_idActividad;
	private int campo_anioE;
	private int campo_mesE;
	private int campo_diaE;  
	private int campo_horaE=0;
	private int campo_minutosE=0;
	private int campo_anioR;
	private int campo_mesR;
	private int campo_diaR;  
	private int campo_horaR=0;
	private int campo_minutosR=0;
	private String campo_lugar;
	private String campo_descripcion;
	private String campo_tono;
	private Boolean campo_alarma;	
	private String actividad;
	private String usuario;
	private String fr;
	private String hr;
	private String fe;
	private String he;
	private String setFechaHoraE;
	private Date FechaHoraE;
	private Date FechaHoraR;	
			
	//VARIABLES DE LOS ELEMENTOS DE LA IU	
	Button BtnNewFechaE;
	Button BtnNewHoraE;	
	Button BtnNewFechaR;
	Button BtnNewHoraR;
	ImageButton BtnAgregarNE;
	Spinner CmbTipoEvento;
	EditText EdtNewLugar;
	EditText EdtNewDescripcion;
	RelativeLayout RingtoneContainer;
	TextView TxtTono;
	Switch SwitchAlarma;	
	Button BtnNuevoEditar;
	Button BtnAsistente;
	
	//VARIABLES EXTRAS	
	private int request_code = 1;
	private static Boolean banderaEsEvento=false;
	private Boolean banderaGuardar=false;	
	private TblTipoActividad tipoActividad;
	private TblActividades laActividad;	
	private List<TblActividadCuidador> list_ActCui;
	private FragmentManager fragmentManager = getFragmentManager();
	private String seleccion;
	private MediaPlayer mPlayer;
	private Boolean banderaTono=false;
	private AdapterSpinnerSimple miAdapter;
	private int tamano;
	
		
	public NewEditEventoCuidadorActivity() { super(); }

	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nuevo_evento_cuidador); 
				
		BtnNewFechaE=(Button)findViewById(R.id.btnNewFechaEmP);
		BtnNewHoraE=(Button)findViewById(R.id.btnNewHoraEmP);
		BtnNewFechaR=(Button)findViewById(R.id.btnNewFechaRmP);
		BtnNewHoraR=(Button)findViewById(R.id.btnNewHoraRmP);
		CmbTipoEvento=(Spinner)findViewById(R.id.cmbTipoEventomP);
		BtnAgregarNE=(ImageButton)findViewById(R.id.btnAgregarNEmP);
		EdtNewLugar=(EditText)findViewById(R.id.edtNewLugarmP);
		EdtNewDescripcion=(EditText)findViewById(R.id.edtNewDescripcionmP);
		RingtoneContainer=(RelativeLayout)findViewById(R.id.ringtoneContainermP);
		TxtTono=(TextView)findViewById(R.id.txtTonomP);
		SwitchAlarma=(Switch)findViewById(R.id.switchAlarmamP);
		BtnNuevoEditar=(Button)findViewById(R.id.btnNewGuardar);
		BtnAsistente=(Button)findViewById(R.id.btnAsistente);
					
		NuevoVsEditar();		    	
	}
	
	//METODO QUE RETORNA "EsEvento"
	public static Boolean EsEvento() {
		Boolean resultado=banderaEsEvento;		
		return resultado;
	}
	
	//METODO QUE SERVIRA PARA IR A NUEVO O EDITAR REGISTRO EVENTO DEL CUIDADOR
	public void NuevoVsEditar() {		
		Boolean opcion1= TabMA1EventosF.EsNuevo();
		Boolean opcion2= TabMA1EventosF.EsEditar();
		
		if (opcion1.equals(true)) {
			NewEditEventoCuidadorActivity.this.setTitle(R.string.NewEvento);
			RecogerParametrosFragTabEC();			
			ListOpcSpinner tarea1 = new ListOpcSpinner();
	        tarea1.execute();				
	 		BtnNuevoEC();
			//DATOS PARA EL DATEPICKERDIALOG Y TIMEPICKERDIALOG
			final Calendar calendar = new GregorianCalendar(); 		
	 		yearE = calendar.get(Calendar.YEAR);
	 		monthE = calendar.get(Calendar.MONTH);
	 		dayE = calendar.get(Calendar.DAY_OF_MONTH);
	 		hoursE = calendar.get(Calendar.HOUR);
			minutesE = calendar.get(Calendar.MINUTE);
			yearR = calendar.get(Calendar.YEAR);
	 		monthR = calendar.get(Calendar.MONTH);
	 		dayR = calendar.get(Calendar.DAY_OF_MONTH);
	 		hoursR = calendar.get(Calendar.HOUR);
			minutesR = calendar.get(Calendar.MINUTE);
		}
		if (opcion2.equals(true)) {
			NewEditEventoCuidadorActivity.this.setTitle(R.string.EditEvento);
			RecogerParametrosFragTabEC2();			
			CargarDatosEnLosElementos();
			SpinnerTouch();			
			BtnEditarEC();
			//DATOS PARA EL DATEPICKERDIALOG Y TIMEPICKERDIALOG
	 		yearE = vAnioE;
	 		monthE = vMesE;
	 		dayE = vDiaE;
	 		hoursE = vHoraE;
			minutesE = vMinutosE;
			yearR = vAnioR;
	 		monthR = vMesR;
	 		dayR = vDiaR;
	 		hoursR = vHoraR;
			minutesR = vMinutosR;
		}
		SeleccionarTonoAlarma();
		BtnNuevoEvento();
		BtnsDatePicker();
		BtnsTimePicker();
		BtnAsistente();
	}
	
	//----METODOS PARA UN NUEVO REGISTRO
	
	public void RecogerParametrosFragTabEC() {
		vIdCuidador = getIntent().getExtras().getLong("varIdCuidador");	
		
		TblCuidador user = Select.from(TblCuidador.class).where(Condition.prop("id_cuidador").eq(vIdCuidador)).first();
		usuario = user.getNombreC();		
	}
		
	public void BtnNuevoEC() {		
		BtnNuevoEditar.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if(estaConectado()) {
					try {
						ValidarDatos();
						if (banderaGuardar.equals(true)) {
							//GUARDAR DATOS EN LA TABLA EVENTOS DEL CUIDADOR
							ATEventosCuidador newEventoC = new ATEventosCuidador();
							Long idEvento = newEventoC.new InsertarEventosCuidador().execute("0", String.valueOf(vIdCuidador), String.valueOf(campo_idActividad), String.valueOf(campo_anioE), String.valueOf(campo_mesE),
									String.valueOf(campo_diaE), String.valueOf(campo_horaE), String.valueOf(campo_minutosE), String.valueOf(campo_anioR),
									String.valueOf(campo_mesR), String.valueOf(campo_diaR), String.valueOf(campo_horaR), String.valueOf(campo_minutosR),
									campo_lugar, campo_descripcion, campo_tono.toString(), campo_alarma.toString(), "false").get().longValue();

							vIdEventoC = idEvento;
							//CREAR EN EL CELL ALARMA DE EVENTO
							MiTblEvento.CrearAlarmaEvento(NewEditEventoCuidadorActivity.this, idEvento, campo_anioE, campo_mesR, campo_diaR, campo_horaR, campo_minutosR,
									vIdCuidador, "C", usuario, actividad, setFechaHoraE, campo_lugar, campo_descripcion, campo_tono, campo_alarma);

							LimpiarElementos();
							Toast.makeText(NewEditEventoCuidadorActivity.this, R.string.GuardadoR, Toast.LENGTH_SHORT).show();
							Intent data = new Intent();
							setResult(RESULT_OK, data);
							finish();
						}
					} catch (Exception ex) {
						Toast.makeText(NewEditEventoCuidadorActivity.this, getString(R.string.ErrorGuardar) + ex.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}
	
	//----METODOS PARA EDITAR UN REGISTRO	
	
	public void RecogerParametrosFragTabEC2() {
		RecogerParametrosFragTabEC();
		vIdEventoC = getIntent().getExtras().getLong("varIdEventoC");
		vIdActividad = getIntent().getExtras().getLong("varIdActividad");		
		vAnioE = getIntent().getExtras().getInt("varAnioE");
		vMesE = getIntent().getExtras().getInt("varMesE");
		vDiaE = getIntent().getExtras().getInt("varDiaE");
		vHoraE = getIntent().getExtras().getInt("varHoraE");
		vMinutosE = getIntent().getExtras().getInt("varMinutosE");
		vAnioR = getIntent().getExtras().getInt("varAnioR");
		vMesR = getIntent().getExtras().getInt("varMesR");
		vDiaR = getIntent().getExtras().getInt("varDiaR");
		vHoraR = getIntent().getExtras().getInt("varHoraR");
		vMinutosR = getIntent().getExtras().getInt("varMinutosR");
		vLugar = getIntent().getExtras().getString("varLugar");
		vDescripcion = getIntent().getExtras().getString("varDescripcion");	
		vTono = getIntent().getExtras().getString("varTono");
		vAlarma = getIntent().getExtras().getBoolean("varAlarma");
		vIdAlarma = getIntent().getExtras().getLong("varIdAlarma");		
	}
	
	public void CargarDatosEnLosElementos() {
		BtnNewFechaE.setText(String.format("%02d/%02d/%02d", vDiaE, vMesE + 1, vAnioE));
		BtnNewHoraE.setText(String.format("%02d:%02d", vHoraE, vMinutosE));
		BtnNewFechaR.setText(String.format("%02d/%02d/%02d", vDiaR, vMesR+1, vAnioR));
		BtnNewHoraR.setText(String.format("%02d:%02d", vHoraR, vMinutosR));		
		EdtNewLugar.setText(vLugar);
		EdtNewDescripcion.setText(vDescripcion);
		TxtTono.setText(vTono);
		SwitchAlarma.setChecked(vAlarma);	
    	//CARGANDO EL DATO GUARDADO EN EL SPINNER  	
		TblActividades laActividad = Select.from(TblActividades.class).where(Condition.prop("id_actividad").eq(vIdActividad)).first();
		String[] opcGuardada={laActividad.getNombreActividad()};
		CmbTipoEvento.setAdapter(new AdapterSpinnerSimple(this, R.layout.adaptador_spinner, opcGuardada));
		campo_anioE=vAnioE;
		campo_mesE=vMesE;
		campo_diaE=vDiaE;
		campo_horaE=vHoraE;
		campo_minutosE=vMinutosE;
		campo_anioR=vAnioR;
		campo_mesR=vMesR;
		campo_diaR=vDiaR;
		campo_horaR=vHoraR;
		campo_minutosR=vMinutosR;
	}
	
	public void SpinnerTouch() {
		CmbTipoEvento.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (CmbTipoEvento.isPressed()) {
					ListOpcSpinner tarea1 = new ListOpcSpinner();
			        tarea1.execute();
				}
				return false;
			}
		});			
	}
	
	public void BtnEditarEC(){
		BtnNuevoEditar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (estaConectado()) {
					try {
						ValidarDatos();
						if (banderaGuardar.equals(true)) {
							//EDITAR DATOS EN LA TABLA EVENTOS DEL PACIENTE
							ATEventosCuidador newEventoC = new ATEventosCuidador();
							newEventoC.new ActualizarEventosCuidador().execute(String.valueOf(vIdEventoC), String.valueOf(vIdCuidador), String.valueOf(campo_idActividad), String.valueOf(campo_anioE), String.valueOf(campo_mesE),
									String.valueOf(campo_diaE), String.valueOf(campo_horaE), String.valueOf(campo_minutosE), String.valueOf(campo_anioR),
									String.valueOf(campo_mesR), String.valueOf(campo_diaR), String.valueOf(campo_horaR), String.valueOf(campo_minutosR),
									campo_lugar, campo_descripcion, campo_tono.toString(), campo_alarma.toString(), "false");

							//EDITAR EN EL CELL ALARMA DE EVENTO
							MiTblEvento.EditarAlarmaEvento(NewEditEventoCuidadorActivity.this, vIdAlarma, campo_anioR, campo_mesR, campo_diaR, campo_horaR, campo_minutosR,
									usuario, actividad, setFechaHoraE, campo_lugar, campo_descripcion, campo_tono, campo_alarma);

							LimpiarElementos();
							Toast.makeText(NewEditEventoCuidadorActivity.this, R.string.EditadoR, Toast.LENGTH_SHORT).show();
							Intent data = new Intent();
							setResult(RESULT_OK, data);
							finish();
						}
					} catch (Exception ex) {
						Toast.makeText(NewEditEventoCuidadorActivity.this, getString(R.string.ErrorEditar) + ex.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}	
			
	//----METODOS TANTO PARA NUEVO O EDITAR REGISTRO
	
	public Boolean ValidarDatos() {
		try {								
			campo_lugar=EdtNewLugar.getText().toString().trim();
			campo_descripcion=EdtNewDescripcion.getText().toString().trim();
			campo_tono=TxtTono.getText().toString().trim();
			campo_alarma=SwitchAlarma.isChecked();
			
			//TOMAMOS EL ID DE ACTIVIDAD PARA GUARDARLO
			actividad=CmbTipoEvento.getSelectedItem().toString();
			TblActividades laActividad= Select.from(TblActividades.class).where(Condition.prop("nombre_actividad").eq(actividad)).first();
			campo_idActividad=laActividad.getIdActividad();
			
			fr=BtnNewFechaR.getText().toString();
			hr=BtnNewHoraR.getText().toString();
			fe=BtnNewFechaE.getText().toString();			
			he=BtnNewHoraE.getText().toString();
			setFechaHoraE=fe+" "+he;	
			
			//VALIDANDO CAMPOS VACIOS 
			if (actividad.equals(getString(R.string.NoHayEventos)) || campo_tono.equals(getString(R.string.SeleccioneTono)) || 
				campo_lugar.equals("") || campo_descripcion.equals("") ||
				fe.equals(getString(R.string.FijarFecha)) || fr.equals(getString(R.string.FijarFecha))) {
				
				if (campo_lugar.equals("") || campo_descripcion.equals("")) {
					DFIngresarDatos dialogo1 = new DFIngresarDatos();
			        dialogo1.show(fragmentManager, "tagAlerta");
				}else {
					if (fe.equals(getString(R.string.FijarFecha)) || fr.equals(getString(R.string.FijarFecha))) {
						DFIngresarFechaRecEve dialogo2 = new DFIngresarFechaRecEve();
				        dialogo2.show(fragmentManager, "tagAlerta");			        
					}else {
						if (campo_tono.equals(getString(R.string.SeleccioneTono))) {
							DFSeleccionarTono dialogo6 = new DFSeleccionarTono();
					        dialogo6.show(fragmentManager, "tagAlerta");
						}else {
							if (actividad.equals(getString(R.string.NoHayEventos))) {
								DFCrearEventosCui dialogo4 = new DFCrearEventosCui();
						        dialogo4.show(fragmentManager, "tagAlerta");				        
							}							
						}							
					}
				}				
			}else {
				//VALIDANDO FECHAS
				Date nowFecha = new Date();
				FechaHoraE = sdfFecha.parse(fe+" "+he);
				FechaHoraR = sdfFecha.parse(fr+" "+hr);
				
				if (FechaHoraE.after(nowFecha) && FechaHoraR.after(nowFecha)) {
					if (FechaHoraE.after(FechaHoraR)) {	
						banderaGuardar=true;
					}else {
						DFFechaMayor2 dialogo3 = new DFFechaMayor2();
				        dialogo3.show(fragmentManager, "tagAlerta");
				    }				
				}else {
					DFFechasInvalidas dialogo5 = new DFFechasInvalidas();
			        dialogo5.show(fragmentManager, "tagAlerta");		        
				}
			}
		}catch (Exception e) { }
		return banderaGuardar;
	}
	
	public void CargarOpcSpinner() { 
		tipoActividad = Select.from(TblTipoActividad.class).where(Condition.prop("tipo_actividad").eq("EVENTO"),
																  Condition.prop("Eliminado").eq(0)).first();		
		list_ActCui = Select.from(TblActividadCuidador.class).where(Condition.prop("id_cuidador").eq(vIdCuidador),
				   												    Condition.prop("Eliminado").eq(0)).orderBy("id_actividad asc").list();
		int i=0;
		tamano=0;		
		while (list_ActCui.size() > i) {
			TblActividadCuidador tablaActCui = new TblActividadCuidador();
			tablaActCui = list_ActCui.get(i);			
			laActividad = Select.from(TblActividades.class).where(Condition.prop("id_actividad").eq(tablaActCui.getIdActividad()),
																  Condition.prop("id_tipo_actividad").eq(tipoActividad.getIdTipoActividad()),
																  Condition.prop("Eliminado").eq(0)).first();
			if (laActividad!=null) {				
				tamano++;			
			}			
			i++;
		}
		//LISTA CON DATOS
		if (tamano!=0) {
			int j=0, posicion=0;
			String[] VectorEventos=new String[tamano];
			while (list_ActCui.size() > j) {
				TblActividadCuidador tablaActCui = new TblActividadCuidador();
				tablaActCui = list_ActCui.get(j);			
				laActividad = Select.from(TblActividades.class).where(Condition.prop("id_actividad").eq(tablaActCui.getIdActividad()),
																	  Condition.prop("id_tipo_actividad").eq(tipoActividad.getIdTipoActividad()),
																	  Condition.prop("Eliminado").eq(0)).first();
				if (laActividad!=null) {				
					VectorEventos[posicion]=laActividad.getNombreActividad();				
					posicion++;			
				}			
				j++;
			}
			miAdapter = new AdapterSpinnerSimple(NewEditEventoCuidadorActivity.this, R.layout.adaptador_spinner, VectorEventos);			
		}   	
	}
	
	public void BtnsTimePicker() {
       	//TIMEPICKER PARA HORA RECORDATORIO	       
        BtnNewHoraR.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) { DialogoTimePicker(); }
		}); 		
 		//TIMEPICKER PARA HORA EVENTO
 		BtnNewHoraE.setOnClickListener(new OnClickListener() {			
 			@Override
 			public void onClick(View v) { DialogoTimePicker(); }
 		});
	}
	
	public void DialogoTimePicker() {		
 		if (BtnNewHoraR.isPressed()) {
 			timePickerDialog = new TimePickerDialog(NewEditEventoCuidadorActivity.this, onTimeSetListener1, hoursR, minutesR, true);
 			timePickerDialog.setTitle(R.string.HoraR);
 		}
		if (BtnNewHoraE.isPressed()) {
			timePickerDialog = new TimePickerDialog(NewEditEventoCuidadorActivity.this, onTimeSetListener2, hoursE, minutesE, true);
			timePickerDialog.setTitle(R.string.HoraEv);
		}
		
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
			BtnNewHoraR.setText(String.format("%02d:%02d", hourOfDay, minute));	
			//DATOS PARA EL TIMEPICKERDIALOG
			hoursR=hourOfDay;  minutesR=minute;
			//DATOS PARA GUARDAR EN LA TABLA		
			campo_horaR=hourOfDay;  campo_minutosR=minute; 
		}
	};
	
	OnTimeSetListener onTimeSetListener2 = new OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			BtnNewHoraE.setText(String.format("%02d:%02d", hourOfDay, minute));	
			//DATOS PARA EL TIMEPICKERDIALOG
			hoursE=hourOfDay;  minutesE=minute;
			//DATOS PARA GUARDAR EN LA TABLA		
			campo_horaE=hourOfDay;  campo_minutosE=minute; 
		}
	};

	public void BtnsDatePicker() {
 		//DATEPICKER PARA FECHA RECORDATORIO	 
 		BtnNewFechaR.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) { DialogoDatePicker(); }
		}); 				
 		//DATEPICKER PARA FECHA EVENTO	 
 		BtnNewFechaE.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) { DialogoDatePicker(); }
		});
	}
	
	public void DialogoDatePicker() {
		if (BtnNewFechaR.isPressed()) {
 			datePickerDialog = new DatePickerDialog(NewEditEventoCuidadorActivity.this, onDateSetListener1, yearR, monthR, dayR);
 			datePickerDialog.setTitle(R.string.FechaR);
 		}
		if (BtnNewFechaE.isPressed()) {
 			datePickerDialog = new DatePickerDialog(NewEditEventoCuidadorActivity.this, onDateSetListener2, yearE, monthE, dayE);
 			datePickerDialog.setTitle(R.string.FechaEv);
		}		
	
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
			BtnNewFechaR.setText(String.format("%02d/%02d/%02d", dayOfMonth, monthOfYear+1, yearr));
			//DATOS PARA EL DATEPICKERDIALOG
			yearR=yearr;  monthR=monthOfYear;  dayR=dayOfMonth;
			//DATOS PARA GUARDAR EN LA TABLA		
			campo_anioR=yearr;  campo_mesR=monthOfYear;  campo_diaR=dayOfMonth;		
		}		
	};	
	
    OnDateSetListener onDateSetListener2 = new OnDateSetListener(){
		@Override
		public void onDateSet(DatePicker view, int yearr, int monthOfYear, int dayOfMonth) {
			BtnNewFechaE.setText(String.format("%02d/%02d/%02d", dayOfMonth, monthOfYear+1, yearr));
			//DATOS PARA EL DATEPICKERDIALOG
			yearE=yearr;  monthE=monthOfYear;  dayE=dayOfMonth;
			//DATOS PARA GUARDAR EN LA TABLA		
			campo_anioE=yearr;  campo_mesE=monthOfYear;  campo_diaE=dayOfMonth;
		}		
	};
	
	public void	SeleccionarTonoAlarma() {
		RingtoneContainer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DFTonos2 dialogo = new DFTonos2();
				dialogo.show(fragmentManager, "tagAlerta");
			}
		});	
	}

	public AlertDialog SeleccionarTono() {
		final CharSequence[] listaTonos=getResources().getStringArray(R.array.opc_tonos);

		AlertDialog.Builder builder = new AlertDialog.Builder(NewEditEventoCuidadorActivity.this);
		builder.setTitle(R.string.tonos_prompt)
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
							int resultado=TonosClass.BuscarIdTono(seleccion);
							mPlayer=MediaPlayer.create(NewEditEventoCuidadorActivity.this, resultado);
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

	public void BtnNuevoEvento() {		
		BtnAgregarNE.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				try {
					Intent intent=new Intent(NewEditEventoCuidadorActivity.this, NewEveRutCuidadorActivity.class);
					intent.putExtra("varIdeCuidador", vIdCuidador);
					startActivityForResult(intent, request_code);			
					banderaEsEvento=true;
				} catch (Exception ex) {
					Toast.makeText(NewEditEventoCuidadorActivity.this, getString(R.string.Error) + ex.getMessage(), Toast.LENGTH_SHORT).show();
				}				
			}
		});	
	}
	
	public void BtnAsistente() {		
		BtnAsistente.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				try {
					Intent intent=new Intent(NewEditEventoCuidadorActivity.this, TabAsistenteActivity.class);
					intent.putExtra("varIdeCuidador", vIdCuidador);
					startActivity(intent);
				} catch (Exception ex) {
					Toast.makeText(NewEditEventoCuidadorActivity.this, getString(R.string.Error) + ex.getMessage(), Toast.LENGTH_SHORT).show();
				}				
			}
		});	
	}	

	public void LimpiarElementos() {			
		BtnNewFechaE.setText(R.string.FijarFecha);
		BtnNewHoraE.setText(R.string.FijarHora);
		BtnNewFechaR.setText(R.string.FijarFecha);
		BtnNewHoraR.setText(R.string.FijarHora);
		EdtNewLugar.getText().clear();
		EdtNewDescripcion.getText().clear();
		TxtTono.setText(R.string.SeleccioneTono);
		SwitchAlarma.setChecked(false);	
		ListOpcSpinner tarea1 = new ListOpcSpinner();
        tarea1.execute();	
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {		
		if ((requestCode == request_code) && (resultCode == NewEditEventoCuidadorActivity.RESULT_OK)) {	       
        	banderaEsEvento=false;				
			ListOpcSpinner tarea1 = new ListOpcSpinner();
	        tarea1.execute();
		}
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
    
    
	//ASYNCTASK
	
	
	private class ListOpcSpinner extends AsyncTask<Void, Void, AdapterSpinnerSimple>{		
		@Override
		protected AdapterSpinnerSimple doInBackground(Void... arg0) {		
			try{				
				CargarOpcSpinner();
			}catch(Exception ex){
				ex.printStackTrace();
			}						
			return miAdapter;
		}

		@Override
		protected void onPostExecute(AdapterSpinnerSimple result) {		
			super.onPostExecute(result);
			//LISTA CON DATOS
			if (tamano!=0) {				
		    	CmbTipoEvento.setAdapter(miAdapter);			
			}
			//LISTA VACIA		
			if (tamano==0) {
				String[] VectorVacio={getResources().getString(R.string.NoHayEventos)};		
				CmbTipoEvento.setAdapter(new AdapterSpinnerSimple(NewEditEventoCuidadorActivity.this, R.layout.adaptador_spinner, VectorVacio));								
			}			
		}		
	}

	//VERIFICAR LA CONEXION DE INTERNET
	public Boolean estaConectado(){
		if(conectadoWifi()){
			return true;
		}else{
			if(conectadoRedMovil()){
				return true;
			}else{
				Toast.makeText(NewEditEventoCuidadorActivity.this, "No hay Conexion a Internet", Toast.LENGTH_SHORT).show();
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