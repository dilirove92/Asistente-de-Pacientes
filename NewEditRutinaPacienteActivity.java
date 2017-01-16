package com.Notifications.patientssassistant;


import com.Notifications.patientssassistant.adapters.*;
import com.Notifications.patientssassistant.alarmas.*;
import com.Notifications.patientssassistant.dialogos.*;
import com.Notifications.patientssassistant.fragments.*;
import com.Notifications.patientssassistant.asynctask.*;
import com.Notifications.patientssassistant.tables.*;
import java.util.Calendar;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.orm.query.Condition;
import com.orm.query.Select;


public class NewEditRutinaPacienteActivity extends Activity {
						
	//VARIABLES PARA NUEVO REGISTRO
	private Long vIdCuidador;
	private Long vIdPaciente;
	private String vNombrePaciente;
	private String vFotoPaciente;	 	
		
	//VARIABLES PARA EDITAR REGISTRO
	private Long vIdRutinaP;
	private Long vIdActividad;
    private int vHora;
    private int vMinutos;
	private Boolean vDomingo;
	private Boolean vLunes;
	private Boolean vMartes;
	private Boolean vMiercoles;	
	private Boolean vJueves;
	private Boolean vViernes;
	private Boolean vSabados;
	private String vTono;
	private String vDescripcion;
	private Boolean vAlarma;

	//VARIABLES PARA LOS TIMEPICKER	
	private int hours;
	private int minutes;
	private TimePickerDialog timePickerDialog;
		
	//VARIABLES QUE ALMACENAN LOS DATOS A GUARDAR
	private Long campo_idActividad;
	private int campo_hora=0;
	private int campo_minutos=0;
	private Boolean campo_domingo;
	private Boolean campo_lunes;
	private Boolean campo_martes;
	private Boolean campo_miercoles;	
	private Boolean campo_jueves;
	private Boolean campo_viernes;
	private Boolean campo_sabados;
	private String campo_tono;
	private String campo_descripcion;
	private Boolean campo_alarma;
	private String actividad;
	private String usuario;
		
	//VARIABLES DE LOS ELEMENTOS DE LA IU	
	ImageView ImagenFotoP;
	TextView TxtNombreP;
	ImageButton BtnAgregarNR;
	Spinner CmbNewTipoRutina;
	Button BtnNewHora;	
	ToggleButton ToggleD;
	ToggleButton ToggleL;
	ToggleButton ToggleMa;
	ToggleButton ToggleMi;
	ToggleButton ToggleJ;
	ToggleButton ToggleV;
	ToggleButton ToggleS;	
	EditText EdtNewDescripcion;
	Switch SwitchAlarma;
	RelativeLayout RingtoneContainer;
	TextView TxtTono;
	Button BtnNuevoEditar;
		
	//VARIABLES EXTRAS
	private int request_code = 1;
	private static Boolean banderaEsRutina=false;
	private Boolean banderaGuardar=false;
	private TblTipoActividad tipoActividad;
	private TblActividades laActividad;
	private List<TblActividadPaciente> list_ActPac;
	private FragmentManager fragmentManager = getFragmentManager(); 
	private String seleccion;
	private MediaPlayer mPlayer;
	private Boolean banderaTono=false;
	private AdapterSpinnerSimple miAdapter;
	private int tamano;
	
	
	public NewEditRutinaPacienteActivity() { super(); }

	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nueva_rutina_paciente); 
		
		ImagenFotoP=(ImageView)findViewById(R.id.imagenFotoP);
		TxtNombreP=(TextView)findViewById(R.id.txtNombreP);
		BtnAgregarNR=(ImageButton)findViewById(R.id.btnAgregarNR);				
		CmbNewTipoRutina=(Spinner)findViewById(R.id.cmbNewTipoRutina);
		BtnNewHora=(Button)findViewById(R.id.btnNewHora);		
		ToggleD=(ToggleButton)findViewById(R.id.toggleD);
		ToggleL=(ToggleButton)findViewById(R.id.toggleL);
		ToggleMa=(ToggleButton)findViewById(R.id.toggleMa);
		ToggleMi=(ToggleButton)findViewById(R.id.toggleMi);
		ToggleJ=(ToggleButton)findViewById(R.id.toggleJ);
		ToggleV=(ToggleButton)findViewById(R.id.toggleV);
		ToggleS=(ToggleButton)findViewById(R.id.toggleS);
		EdtNewDescripcion=(EditText)findViewById(R.id.edtNewDescripcion);
		SwitchAlarma=(Switch)findViewById(R.id.switchAlarma);
		RingtoneContainer=(RelativeLayout)findViewById(R.id.ringtoneContainer);
		TxtTono=(TextView)findViewById(R.id.txtTono);
		BtnNuevoEditar=(Button)findViewById(R.id.btnNewGuardar);
					
		NuevoVsEditar();		    	
	}
	
	//METODO QUE RETORNA "EsRutina"
	public static Boolean EsRutina() {
		Boolean resultado=banderaEsRutina;		
		return resultado;
	}
	
	//METODO QUE SERVIRA PARA IR A NUEVO O EDITAR REGISTRO RUTINA DEL PACIENTE
	public void NuevoVsEditar() {		
		Boolean opcion1= TabAP2RutinasPacF.EsNuevo();
		Boolean opcion2= TabAP2RutinasPacF.EsEditar();
		
		if (opcion1.equals(true)) {
			NewEditRutinaPacienteActivity.this.setTitle(R.string.NewRutina);
			RecogerParametrosFragTabRP();			
			CargarFotoNombrePaciente();
			ListOpcSpinner tarea1 = new ListOpcSpinner();
	        tarea1.execute();		
	 		BtnNuevaRP();
	 		//DATOS PARA EL TIMEPICKERDIALOG
			final Calendar calendar = Calendar.getInstance(); 		
			hours = calendar.get(Calendar.HOUR);
			minutes = calendar.get(Calendar.MINUTE);
		}
		if (opcion2.equals(true)) {
			NewEditRutinaPacienteActivity.this.setTitle(R.string.EditRutina);
			RecogerParametrosFragTabRP2();			
			CargarDatosEnLosElementos();
			SpinnerTouch();			
			BtnEditarRP();
			//DATOS PARA EL TIMEPICKERDIALOG				
			hours = vHora;
			minutes = vMinutos;	
		}
		SeleccionarTonoAlarma();
		BtnNuevaRutina();
		BtnTimePicker();
	}
	
	//----METODOS PARA UN NUEVO REGISTRO
	
	public void RecogerParametrosFragTabRP() {
		vIdCuidador = getIntent().getExtras().getLong("varIdCuidador");
		vIdPaciente = getIntent().getExtras().getLong("varItemIdPaciente");
		vNombrePaciente = getIntent().getExtras().getString("varNombrePaciente");
		vFotoPaciente = getIntent().getExtras().getString("varFotoPaciente");	
		usuario=vNombrePaciente;
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
	
	public void BtnNuevaRP() {		
		BtnNuevoEditar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (estaConectado()) {
					try {
						ValidarDatos();
						if (banderaGuardar.equals(true)) {
							//GUARDAR DATOS EN LA TABLA RUTINAS DEL PACIENTE
							ATRutinasPacientes objRutPac = new ATRutinasPacientes();
							Long idRP = objRutPac.new InsertarRutinasPacientes().execute("0", vIdPaciente.toString(), campo_idActividad.toString(), String.valueOf(campo_hora), String.valueOf(campo_minutos),
									campo_domingo.toString(), campo_lunes.toString(), campo_martes.toString(), campo_miercoles.toString(), campo_jueves.toString(),
									campo_viernes.toString(), campo_sabados.toString(), campo_tono, campo_descripcion, campo_alarma.toString(), "false").get().longValue();

							//VERIFICAR SI EL CUIDADOR QUE CREA EL REGISTRO DE RUTINA TIENE EL PERMISO DE ALARMA ACTIVADO
							//SI ESTA ACTIVADO SE LE CREARA LA ALARMA EN EL CELL
							TblPermisos verPermiso = Select.from(TblPermisos.class).where(Condition.prop("id_cuidador").eq(vIdCuidador),
									Condition.prop("id_paciente").eq(vIdPaciente),
									Condition.prop("notifi_alarma").eq(1),
									Condition.prop("Eliminado").eq(0)).first();
							if (verPermiso != null) {
								//CREAR EN EL CELL ALARMA DE RUTINA
								MiTblRutina.CrearAlarmaRutina(NewEditRutinaPacienteActivity.this, idRP, campo_hora, campo_minutos, vIdPaciente, "P", usuario, actividad, campo_domingo,
										campo_lunes, campo_martes, campo_miercoles, campo_jueves, campo_viernes, campo_sabados, campo_tono, campo_alarma);
							}

							LimpiarElementos();
							Toast.makeText(NewEditRutinaPacienteActivity.this, R.string.GuardadoR, Toast.LENGTH_SHORT).show();
							Intent data = new Intent();
							setResult(RESULT_OK, data);
							finish();
						}
					} catch (Exception ex) {
						Toast.makeText(NewEditRutinaPacienteActivity.this, getString(R.string.ErrorGuardar) + ex.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}
	
	//----METODOS PARA EDITAR UN REGISTRO	
	
	public void RecogerParametrosFragTabRP2() {
		RecogerParametrosFragTabRP();		
		vIdRutinaP = getIntent().getExtras().getLong("varIdRutinaP");
		vIdActividad = getIntent().getExtras().getLong("varIdActividad");
		vHora = getIntent().getExtras().getInt("varHora");
		vMinutos = getIntent().getExtras().getInt("varMinutos");
		vDomingo = getIntent().getExtras().getBoolean("varDomingo");
		vLunes = getIntent().getExtras().getBoolean("varLunes");
		vMartes = getIntent().getExtras().getBoolean("varMartes");
		vMiercoles = getIntent().getExtras().getBoolean("varMiercoles");
		vJueves = getIntent().getExtras().getBoolean("varJueves");
		vViernes = getIntent().getExtras().getBoolean("varViernes");
		vSabados = getIntent().getExtras().getBoolean("varSabados");
		vTono = getIntent().getExtras().getString("varTono");
		vDescripcion = getIntent().getExtras().getString("varDescripcion");
		vAlarma = getIntent().getExtras().getBoolean("varAlarma");		
	}
	
	public void CargarDatosEnLosElementos() {		
		CargarFotoNombrePaciente(); 
		BtnNewHora.setText(String.format("%02d:%02d", vHora, vMinutos));
		ToggleD.setChecked(vDomingo);
		ToggleL.setChecked(vLunes);
		ToggleMa.setChecked(vMartes);
		ToggleMi.setChecked(vMiercoles);
		ToggleJ.setChecked(vJueves);
		ToggleV.setChecked(vViernes);
		ToggleS.setChecked(vSabados);
		TxtTono.setText(vTono);
		EdtNewDescripcion.setText(vDescripcion);
		SwitchAlarma.setChecked(vAlarma);								    	
    	//CARGANDO EL DATO GUARDADO EN EL SPINNER  	
		TblActividades laActividad= Select.from(TblActividades.class).where(Condition.prop("id_actividad").eq(vIdActividad)).first();
		String[] opcGuardada={laActividad.getNombreActividad()};
		CmbNewTipoRutina.setAdapter(new AdapterSpinnerSimple(this, R.layout.adaptador_spinner, opcGuardada));	
		campo_hora=vHora;
		campo_minutos=vMinutos;
	}
	
	public void SpinnerTouch() {	
		CmbNewTipoRutina.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (CmbNewTipoRutina.isPressed()) {
					ListOpcSpinner tarea1 = new ListOpcSpinner();
			        tarea1.execute();
				}
				return false;
			}
		});		
	}
	
	public void BtnEditarRP(){
		BtnNuevoEditar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (estaConectado()) {
					try {
						ValidarDatos();
						if (banderaGuardar.equals(true)) {
							//EDITAR DATOS EN LA TABLA RUTINAS DEL PACIENTE
							ATRutinasPacientes objRutPac = new ATRutinasPacientes();
							objRutPac.new ActualizarRutinasPacientes().execute(vIdRutinaP.toString(), vIdPaciente.toString(), campo_idActividad.toString(), String.valueOf(campo_hora), String.valueOf(campo_minutos),
									campo_domingo.toString(), campo_lunes.toString(), campo_martes.toString(), campo_miercoles.toString(), campo_jueves.toString(),
									campo_viernes.toString(), campo_sabados.toString(), campo_tono, campo_descripcion, campo_alarma.toString(), "false");

							//VERIFICAR SI EL CUIDADOR QUE EDITA EL REGISTRO DE RUTINA TIENE EL PERMISO DE ALARMA ACTIVADO
							//SI ESTA ACTIVADO SE LE CREARA LA ALARMA EN EL CELL
							TblPermisos verPermiso = Select.from(TblPermisos.class).where(Condition.prop("id_cuidador").eq(vIdCuidador),
									Condition.prop("id_paciente").eq(vIdPaciente),
									Condition.prop("notifi_alarma").eq(1),
									Condition.prop("Eliminado").eq(0)).first();
							if (verPermiso != null) {
								MiTblRutina miRutina = Select.from(MiTblRutina.class).where(Condition.prop("id_rutina").eq(vIdRutinaP)).first();

								//EDITAR EN EL CELL ALARMA DE RUTINA
								MiTblRutina.EditarAlarmaRutina(NewEditRutinaPacienteActivity.this, miRutina.getIdAlarmaClock(), campo_hora, campo_minutos, usuario, actividad, campo_domingo,
										campo_lunes, campo_martes, campo_miercoles, campo_jueves, campo_viernes, campo_sabados, campo_tono, campo_alarma);
							}

							LimpiarElementos();
							Toast.makeText(NewEditRutinaPacienteActivity.this, R.string.EditadoR, Toast.LENGTH_SHORT).show();
							Intent data = new Intent();
							setResult(RESULT_OK, data);
							finish();
						}
					} catch (Exception ex) {
						Toast.makeText(NewEditRutinaPacienteActivity.this, getString(R.string.ErrorEditar) + ex.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}	
			
	//----METODOS TANTO PARA NUEVO O EDITAR REGISTRO
	
	public Boolean ValidarDatos() {
		campo_domingo=ToggleD.isChecked();
		campo_lunes=ToggleL.isChecked();
		campo_martes=ToggleMa.isChecked();
		campo_miercoles=ToggleMi.isChecked();
		campo_jueves=ToggleJ.isChecked();
		campo_viernes=ToggleV.isChecked();
		campo_sabados=ToggleS.isChecked();
		campo_tono=TxtTono.getText().toString().trim();
		campo_descripcion=EdtNewDescripcion.getText().toString().trim();
		campo_alarma=SwitchAlarma.isChecked();
		
		//TOMAMOS EL ID DE ACTIVIDAD PARA GUARDARLO
		actividad=CmbNewTipoRutina.getSelectedItem().toString();
		TblActividades laActividad=Select.from(TblActividades.class).where(Condition.prop("nombre_actividad").eq(actividad)).first();
		campo_idActividad=laActividad.getIdActividad();
		
		if(actividad.equals(getString(R.string.NoHayRutinas)) || campo_tono.equals(getString(R.string.SeleccioneTono)) || campo_descripcion.equals("") || 
		  (campo_domingo.equals(false) && campo_lunes.equals(false) && campo_martes.equals(false) && campo_miercoles.equals(false) && 
		   campo_jueves.equals(false) && campo_viernes.equals(false) && campo_sabados.equals(false)))
		{
			if (campo_descripcion.equals("")) {
				DFIngresarDatos dialogo1 = new DFIngresarDatos();
		        dialogo1.show(fragmentManager, "tagAlerta");
			}else {
				if (campo_domingo.equals(false) && campo_lunes.equals(false) && campo_martes.equals(false) && campo_miercoles.equals(false) && 
					campo_jueves.equals(false) && campo_viernes.equals(false) && campo_sabados.equals(false)) {
					DFSeleccioneUnDia dialogo2 = new DFSeleccioneUnDia();
			        dialogo2.show(fragmentManager, "tagAlerta");
				}else {
					if (campo_tono.equals(getString(R.string.SeleccioneTono))) {
						DFSeleccionarTono dialogo3 = new DFSeleccionarTono();
				        dialogo3.show(fragmentManager, "tagAlerta");
					}else {
						if (actividad.equals(getString(R.string.NoHayRutinas))) {
							DFCrearRutinasPac dialogo4 = new DFCrearRutinasPac();
					        dialogo4.show(fragmentManager, "tagAlerta");		        
						}
					}
				}
			}
		}else {
			banderaGuardar=true;												
		}
		return banderaGuardar;
	}
	
	public void CargarOpcSpinner() { 
		tipoActividad = Select.from(TblTipoActividad.class).where(Condition.prop("tipo_actividad").eq("RUTINA"),
																  Condition.prop("Eliminado").eq(0)).first();		
		list_ActPac = Select.from(TblActividadPaciente.class).where(Condition.prop("id_paciente").eq(vIdPaciente),
				   												    Condition.prop("Eliminado").eq(0)).orderBy("id_actividad asc").list();
		int i=0;
		tamano=0;	
		while (list_ActPac.size() > i) {
			TblActividadPaciente tablaActPac = new TblActividadPaciente();
			tablaActPac = list_ActPac.get(i);			
			laActividad = Select.from(TblActividades.class).where(Condition.prop("id_actividad").eq(tablaActPac.getIdActividad()),
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
			String[] VectorRutinas=new String[tamano];		
			while (list_ActPac.size() > j) {
				TblActividadPaciente tablaActPac = new TblActividadPaciente();
				tablaActPac = list_ActPac.get(j);			
				laActividad = Select.from(TblActividades.class).where(Condition.prop("id_actividad").eq(tablaActPac.getIdActividad()),
																	  Condition.prop("id_tipo_actividad").eq(tipoActividad.getIdTipoActividad()),
																	  Condition.prop("Eliminado").eq(0)).first();
				if (laActividad!=null) {
					VectorRutinas[posicion]=laActividad.getNombreActividad();
					posicion++;				
				}			
				j++;
			}
			miAdapter = new AdapterSpinnerSimple(NewEditRutinaPacienteActivity.this, R.layout.adaptador_spinner, VectorRutinas);	    			
		}	   	
	}
	
	public void BtnTimePicker() {
		//TIMEPICKER PARA HORA	       
        BtnNewHora.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) { DialogoTimePicker(); } 
		});
	}
	
	public void DialogoTimePicker() {
 		timePickerDialog = new TimePickerDialog(NewEditRutinaPacienteActivity.this, onTimeSetListener1, hours, minutes, true);
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
	
    public void	SeleccionarTonoAlarma() {
		RingtoneContainer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DFTonos6 dialogo = new DFTonos6();
				dialogo.show(fragmentManager, "tagAlerta");
			}			
		});
    }

	public AlertDialog SeleccionarTono() {
		final CharSequence[] listaTonos=getResources().getStringArray(R.array.opc_tonos);

		AlertDialog.Builder builder = new AlertDialog.Builder(NewEditRutinaPacienteActivity.this);
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
							mPlayer=MediaPlayer.create(NewEditRutinaPacienteActivity.this, resultado);
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

	public void BtnNuevaRutina() {	  	
		BtnAgregarNR.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				try {
					Intent intent=new Intent(NewEditRutinaPacienteActivity.this, NewEveRutPacienteActivity.class);
					intent.putExtra("varIdPaciente", vIdPaciente);
					startActivityForResult(intent, request_code);	
					banderaEsRutina=true;
				} catch (Exception ex) {
					Toast.makeText(NewEditRutinaPacienteActivity.this, getString(R.string.Error) + ex.getMessage(), Toast.LENGTH_SHORT).show();
				}				
			}
		});	
	}
	
	public void LimpiarElementos() {			
		BtnNewHora.setText(R.string.FijarHora);
		ToggleD.setChecked(false);
		ToggleL.setChecked(false);
		ToggleMa.setChecked(false);
		ToggleMi.setChecked(false);
		ToggleJ.setChecked(false);
		ToggleV.setChecked(false);
		ToggleS.setChecked(false);
		TxtTono.setText(R.string.SeleccioneTono);
		EdtNewDescripcion.getText().clear();
		SwitchAlarma.setChecked(false);	
		ListOpcSpinner tarea1 = new ListOpcSpinner();
        tarea1.execute();	
	}
    
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {				
		if ((requestCode == request_code)&&(resultCode == NewEditRutinaPacienteActivity.RESULT_OK)) {	
			banderaEsRutina=false;				
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
				CmbNewTipoRutina.setAdapter(miAdapter);			
			}
			//LISTA VACIA		
			if (tamano==0) {
				String[] VectorVacio={getResources().getString(R.string.NoHayRutinas)};		
				CmbNewTipoRutina.setAdapter(new AdapterSpinnerSimple(NewEditRutinaPacienteActivity.this, R.layout.adaptador_spinner, VectorVacio));								
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
				Toast.makeText(NewEditRutinaPacienteActivity.this, "No hay Conexion a Internet", Toast.LENGTH_SHORT).show();
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