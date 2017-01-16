package com.Notifications.patientssassistant;


import com.Notifications.patientssassistant.adapters.*;
import com.Notifications.patientssassistant.alarmas.*;
import com.Notifications.patientssassistant.dialogos.*;
import com.Notifications.patientssassistant.fragments.*;
import com.Notifications.patientssassistant.tables.*;
import com.Notifications.patientssassistant.asynctask.*;
import com.orm.query.Condition;
import com.orm.query.Select;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;
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
import android.os.Bundle;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;


public class NewEditControlMedicinaActivity extends Activity {
	
	//VARIABLES PARA NUEVO REGISTRO "CONTROL DE MEDICINAS"
	private Long vIdCuidador;
	private Long vIdPaciente;
	private String vNombrePaciente;
	private String vFotoPaciente;	 	
		
	//VARIABLES PARA EDITAR REGISTRO "CONTROL DE MEDICINAS"
	private Long vIdControlMedicina;	
	private String vMedicamento;
	private String vDescripcion;
	private String vMotivoMedicacion;
	private String vTiempo;
	private String vDosis;
	private int vNdeVeces;
	private String vTono;
	
	//VARIABLES QUE ALMACENAN LOS DATOS A GUARDAR "CONTROL DE MEDICINAS"		
	private String campo_medicamento;
	private String campo_descripcion;
	private String campo_motivoMedicacion;
	private String campo_tiempo;
	private String campo_dosis;
	private Integer campo_nDeVeces;
	private String nDeVeces;
	private String campo_tono;	
	
	//VARIABLES DE LOS ELEMENTOS DE LA IU "CONTROL DE MEDICINAS"		
	ImageView ImagenFotoP;
	TextView TxtNombreP;
	EditText EdtNewMedicamento;
	EditText EdtNewDescrMedica;	
	EditText EdtNewMotiMedi;	
	Spinner CmbNewTipoTrata;
	EditText EdtNewDosis;
	EditText EdtNewNveces;
	RelativeLayout RingtoneContainer;
	TextView TxtTono;
	Button BtnNewAgregarHM;
	ViewGroup LayoutCM;
	Button BtnNuevoEditar;

	//VARIABLES PARA EDITAR REGISTRO "HORARIOS DE MEDICINA"
	private Long [] vIdHorarioMedicina=new Long[' '];
	
	//VARIABLES QUE ALMACENAN LOS DATOS A GUARDAR "HORARIOS DE MEDICINA"
	private Long campo_idCM;
	private int campo_Hora;
	private int campo_Minutos;
	private Boolean campo_ActDesAlarma;
	private Boolean campo_Domingo;
	private Boolean campo_Lunes;
	private Boolean campo_Martes;
	private Boolean campo_Miercoles;
	private Boolean campo_Jueves;
	private Boolean campo_Viernes;
	private Boolean campo_Sabados;
			
	//VARIABLES DE LOS ELEMENTOS DE LA IU "HORARIOS DE MEDICINA"
	ImageButton BtnEliminar;
	ToggleButton ToggAlarma;
	ToggleButton ToggleD;
	ToggleButton ToggleL;
	ToggleButton ToggleMa;
	ToggleButton ToggleMi;
	ToggleButton ToggleJ;
	ToggleButton ToggleV;
	ToggleButton ToggleS;

	//VARIABLES PARA TIMEPICKER
	private int hours;
	private int minutes;
	private TimePickerDialog timePickerDialog;
	private TimePickerDialog.OnTimeSetListener onTimeSetListener;
	
	//VARIABLES EXTRAS
	private Boolean banderaGuardar=false;
	private int i=1;
	private int idBtnNewHora;
	private int hm=0;	
	private int idLayout;
	private Boolean [] vecLayout=new Boolean[' '];
	private int eliminados=0;
	private int continua;
	private int idx;
	private int l;
	private int f;
	private String usuario;
	private String seleccion;
	FragmentManager fragmentManager = getFragmentManager();
	private MediaPlayer mPlayer;
	private Boolean banderaTono=false;

	
	public NewEditControlMedicinaActivity() { super(); }

	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nuevo_control_medicina); 
		
		ImagenFotoP=(ImageView)findViewById(R.id.imagenFotoP);
		TxtNombreP=(TextView)findViewById(R.id.txtNombreP);
		EdtNewMedicamento=(EditText)findViewById(R.id.edtNewMedicamento);
		EdtNewDescrMedica=(EditText)findViewById(R.id.edtNewDescrMedica);
		EdtNewMotiMedi=(EditText)findViewById(R.id.edtNewMotiMedi);		
		CmbNewTipoTrata=(Spinner)findViewById(R.id.cmbNewTipoTrata);
		EdtNewDosis=(EditText)findViewById(R.id.edtNewDosis);
		EdtNewNveces=(EditText)findViewById(R.id.edtNewNveces);
		RingtoneContainer=(RelativeLayout)findViewById(R.id.ringtoneContainer);
		TxtTono=(TextView)findViewById(R.id.txtTono);
		BtnNewAgregarHM=(Button)findViewById(R.id.btnNewAgregarHM);
		LayoutCM = (ViewGroup) findViewById(R.id.LayoutContenedor); 
		BtnNuevoEditar=(Button)findViewById(R.id.btnNewGuardar);
			
		NuevoVsEditar();      	
	}
	
	//METODO QUE SERVIRA PARA IR A NUEVO O EDITAR REGISTRO DEL CONTROL DE MEDICINA
	public void NuevoVsEditar() {		
		Boolean opcion1 = MenPacControlMedicinasF.EsNuevo();		
		Boolean opcion2 = MenPacControlMedicinasF.EsEditar();
		
		if (opcion1.equals(true)) {
			NewEditControlMedicinaActivity.this.setTitle(R.string.NewMedicina);
			RecogerParametrosFragMenPacCM();
			CargarFotoNombrePaciente();
			CargarOpcSpinner();
			SeleccionarTonoAlarma();
			BtnNuevoCM();			
		}
		if (opcion2.equals(true)) {
			NewEditControlMedicinaActivity.this.setTitle(R.string.EditMedicina);
			RecogerParametrosFragMenPacCM2();
			CargarDatosEnLosElementos();			
			SpinnerTouch();
			SeleccionarTonoAlarma();
			BtnEditarCM();
		}		
		BtnNuevoHorario();
	}
		
	//----METODOS PARA UN NUEVO REGISTRO DE CONTROL DE MEDICINAS
	
	public void RecogerParametrosFragMenPacCM() {
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
	
	public void CargarOpcSpinner() {
		String[] OpcTipoTratamiento=getResources().getStringArray(R.array.opc_tratamiento);		
    	CmbNewTipoTrata.setAdapter(new AdapterSpinnerSimple(NewEditControlMedicinaActivity.this, R.layout.adaptador_spinner, OpcTipoTratamiento));
	}
	
	public void BtnNuevoCM(){
		BtnNuevoEditar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(estaConectado()) {
					try {
						banderaGuardar = true;
						Validaciones();
						if (banderaGuardar.equals(true)) {
							//GUARDAR DATOS EN LA TABLA CONTROL DE MEDICINAS
							ATControlMedicina contMed = new ATControlMedicina();
							Long idCM = contMed.new InsertarControlMedicina().execute("0", String.valueOf(vIdPaciente), campo_medicamento, campo_descripcion, campo_motivoMedicacion,
									campo_tiempo, campo_dosis, campo_nDeVeces.toString(), campo_tono, "false").get().longValue();

							campo_idCM = idCM;

							//GUARDAR DATOS EN LA TABLA HORARIOS DE MEDICINAS
							if (idCM > 0) {
								GuardarHorariosMedicina1();
							}

							LimpiarElementos();
							Toast.makeText(NewEditControlMedicinaActivity.this, R.string.GuardadoR, Toast.LENGTH_SHORT).show();
							Intent data = new Intent();
							setResult(RESULT_OK, data);
							finish();
						}
					} catch (Exception ex) {
						Toast.makeText(NewEditControlMedicinaActivity.this, getString(R.string.ErrorGuardar) + ex.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}
	
	public void GuardarHorariosMedicina1() {
		l=1;	
		for (int k = 0; k < hm; k++) {	
			f=k;
			GuaHorMed();
		}			
	}
	
	//----METODOS PARA EDITAR UN REGISTRO DE CONTROL DE MEDICINAS
	
	public void RecogerParametrosFragMenPacCM2() {		
		RecogerParametrosFragMenPacCM();
		vIdControlMedicina = getIntent().getExtras().getLong("varIdControlMedicina");
		vMedicamento = getIntent().getExtras().getString("varMedicamento");
		vDescripcion = getIntent().getExtras().getString("varDescripcion");
		vMotivoMedicacion = getIntent().getExtras().getString("varMotivoMedicacion");
		vTiempo = getIntent().getExtras().getString("varTipoTratamiento");		
		vDosis = getIntent().getExtras().getString("varDosis");
		vNdeVeces = getIntent().getExtras().getInt("varNdeVeces");
		vTono = getIntent().getExtras().getString("varTono");
		campo_idCM=vIdControlMedicina;		
	}
	
	public void CargarDatosEnLosElementos() {
		CargarFotoNombrePaciente();
		EdtNewMedicamento.setText(vMedicamento);
		EdtNewDescrMedica.setText(vDescripcion);
		EdtNewMotiMedi.setText(vMotivoMedicacion);
		EdtNewDosis.setText(vDosis);
		EdtNewNveces.setText(Integer.toString(vNdeVeces));
		TxtTono.setText(vTono);
	
		//CARGANDO EL DATO GUARDADO EN EL SPINNER
    	String[] opcTipoTratamiento={vTiempo};
    	CmbNewTipoTrata.setAdapter(new AdapterSpinnerSimple(NewEditControlMedicinaActivity.this, R.layout.adaptador_spinner, opcTipoTratamiento));
    
    	//CARGAR DATOS EN LOS HORARIOS DE MEDICINA    	
    	List<TblHorarioMedicina> lista_hm = Select.from(TblHorarioMedicina.class).where(Condition.prop("id_control_medicina").eq(vIdControlMedicina),
    																				    Condition.prop("Eliminado").eq(0)).orderBy("act_des_alarma desc").list();
    	for (int k = 0; k < lista_hm.size(); k++) {     		
			//CREAMOS LOS NUEVOS ELEMENTOS DINAMICOS (adaptador_lista_hm)
			LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View addView = layoutInflater.inflate (R.layout.adaptador_lista_hm, null);
			
			//BUSCAR LOS ELEMENTOS POR ID
			final Button BtnNewHora = (Button)addView.findViewById(R.id.btnNewHora);				
			BtnEliminar = (ImageButton)addView.findViewById(R.id.btnEliminar);
			ToggAlarma = (ToggleButton)addView.findViewById(R.id.toggAlarma);
			ToggleD = (ToggleButton)addView.findViewById(R.id.toggleD);
			ToggleL = (ToggleButton)addView.findViewById(R.id.toggleL);
			ToggleMa = (ToggleButton)addView.findViewById(R.id.toggleMa);
			ToggleMi = (ToggleButton)addView.findViewById(R.id.toggleMi);
			ToggleJ = (ToggleButton)addView.findViewById(R.id.toggleJ);
			ToggleV = (ToggleButton)addView.findViewById(R.id.toggleV);
			ToggleS = (ToggleButton)addView.findViewById(R.id.toggleS);						
			
			//CAMBIAR EL ID DE LOS ELEMENTOS
			BtnNewHora.setId(i);
			final int indexBtnNewHora = i;
			BtnEliminar.setId(i++);				
			ToggAlarma.setId(i++);
			ToggleD.setId(i++);
			ToggleL.setId(i++);
			ToggleMa.setId(i++);
			ToggleMi.setId(i++);
			ToggleJ.setId(i++);
			ToggleV.setId(i++);
			ToggleS.setId(i++);
			
			//LLENAR LOS ELEMENTOS CON LOS DATOS
			TblHorarioMedicina tabla_hm = lista_hm.get(k);				
			vIdHorarioMedicina[k] = tabla_hm.getIdHorarioMedicina();
			BtnNewHora.setText(String.format("%02d:%02d", tabla_hm.getHora(), tabla_hm.getMinutos()));
			ToggAlarma.setChecked(tabla_hm.getActDesAlarma());
			ToggleD.setChecked(tabla_hm.getDomingo());
			ToggleL.setChecked(tabla_hm.getLunes());
			ToggleMa.setChecked(tabla_hm.getMartes());
			ToggleMi.setChecked(tabla_hm.getMiercoles());
			ToggleJ.setChecked(tabla_hm.getJueves());
			ToggleV.setChecked(tabla_hm.getViernes());
			ToggleS.setChecked(tabla_hm.getSabado());
			
			//AGREGAR LA VISTA AL LAYOUTCM
			LayoutCM.addView(addView);
			i++;
			final int indexLayout = hm;
			vecLayout[indexLayout] = true;
			hm++;
																	
			//BOTON HORA DE LA MEDICINA
	 		BtnNewHora.setOnClickListener(new OnClickListener() {	 			
				@Override
				public void onClick(View v) {				
					idBtnNewHora=indexBtnNewHora;						 				
					DialogoTimePicker();
				}
			});		 		
	 		onTimeSetListener = new OnTimeSetListener() {
	 			@Override
	 			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
	 				Button BtnHora=(Button)findViewById(idBtnNewHora);
 					BtnHora.setText(String.format("%02d:%02d", hourOfDay, minute)); 							
	 			}
	 		};
	 				 				 		
	 		//BOTON ELIMINAR HORARIO DE LA MEDICINA
	 		BtnEliminar.setOnClickListener(new OnClickListener() {					
				@Override
				public void onClick(View v) {						
					idLayout=indexLayout;
					vecLayout[idLayout]=false;
					LayoutCM.removeView(addView);						
					eliminados++;	
				}
			});
		}    	
    	continua=hm;
    	idx=i;
	}
	
	public void SpinnerTouch() {		
		CmbNewTipoTrata.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (CmbNewTipoTrata.isPressed()) {
					CargarOpcSpinner();
				}
				return false;
			}
		});			
	}
	
	public void BtnEditarCM(){
		BtnNuevoEditar.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if(estaConectado()) {
					try {
						banderaGuardar = true;
						Validaciones();
						if (banderaGuardar.equals(true)) {
							//EDITAR DATOS EN LA TABLA CONTROL DE MEDICINAS
							ATControlMedicina contMed = new ATControlMedicina();
							contMed.new ActualizarControlMedicina().execute(String.valueOf(vIdControlMedicina), String.valueOf(vIdPaciente), campo_medicamento, campo_descripcion,
									campo_motivoMedicacion, campo_tiempo, campo_dosis, String.valueOf(campo_nDeVeces), campo_tono, "false");

							//EDITAR DATOS EN LA TABLA HORARIOS DE MEDICINAS
							EditarHorariosMedicina();

							//GUARDAR NUEVOS HORARIOS EN LA TABLA HORARIOS DE MEDICINAS
							GuardarHorariosMedicina2();

							LimpiarElementos();
							Toast.makeText(NewEditControlMedicinaActivity.this, R.string.EditadoR, Toast.LENGTH_SHORT).show();
							Intent data = new Intent();
							setResult(RESULT_OK, data);
							finish();
						}
					} catch (Exception ex) {
						Toast.makeText(NewEditControlMedicinaActivity.this, getString(R.string.ErrorEditar) + ex.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}
	
	public void EditarHorariosMedicina() {		
		int l=1;	
		for (int k = 0; k < continua; k++) {			
			if (vecLayout[k].equals(true)) {


				//BUSCAR LOS ELEMENTOS POR ID
				Button BHora=(Button)findViewById(l);
				l++; //Corresponde al boton eliminar...
				ToggleButton SAlarma=(ToggleButton)findViewById(l++);
				ToggleButton TDo=(ToggleButton)findViewById(l++);
				ToggleButton TLu=(ToggleButton)findViewById(l++);
				ToggleButton TMa=(ToggleButton)findViewById(l++);
				ToggleButton TMi=(ToggleButton)findViewById(l++);
				ToggleButton TJu=(ToggleButton)findViewById(l++);
				ToggleButton TVi=(ToggleButton)findViewById(l++);
				ToggleButton TSa=(ToggleButton)findViewById(l++);
				l++;
						
				//TOMAR EL VALOR INGRESADO EN LOS ELEMENTOS
				String HoraMinutos=BHora.getText().toString();
				String hora=HoraMinutos.substring(0, HoraMinutos.length()-3);
				String minutos=HoraMinutos.substring(3, HoraMinutos.length());			
				campo_Hora=Integer.parseInt(hora);
				campo_Minutos=Integer.parseInt(minutos);
				campo_ActDesAlarma=SAlarma.isChecked();
				campo_Domingo=TDo.isChecked();
				campo_Lunes=TLu.isChecked();
				campo_Martes=TMa.isChecked();
				campo_Miercoles=TMi.isChecked();
				campo_Jueves=TJu.isChecked();
				campo_Viernes=TVi.isChecked();
				campo_Sabados=TSa.isChecked();
				
				//EDITAR DATOS EN LA TABLA HORARIOS DE MEDICINA
				ATHorarioMedicinas horMed = new ATHorarioMedicinas();
				horMed.new ActualizarHorarioMedicina().execute(vIdHorarioMedicina[k].toString(), String.valueOf(campo_idCM), String.valueOf(campo_Hora), String.valueOf(campo_Minutos),  
															campo_Domingo.toString(), campo_Lunes.toString(), campo_Martes.toString(), campo_Miercoles.toString(), 
															campo_Jueves.toString(), campo_Viernes.toString(), campo_Sabados.toString(), campo_ActDesAlarma.toString(), "false");
				
				
				//VERIFICAR SI EL CUIDADOR QUE EDITA EL REGISTRO DE MEDICINA TIENE EL PERMISO DE MEDICINAS ACTIVADO
				//SI ESTA ACTIVADO SE LE CREARA LA ALARMA EN EL CELL
				TblPermisos verPermiso = Select.from(TblPermisos.class).where(Condition.prop("id_cuidador").eq(vIdCuidador),
																			  Condition.prop("id_paciente").eq(vIdPaciente),
																			  Condition.prop("cont_medicina").eq(1),
																			  Condition.prop("Eliminado").eq(0)).first();
				if (verPermiso!=null) {
					MiTblMedicina miHM = Select.from(MiTblMedicina.class).where(Condition.prop("id_horario_medicina").eq(vIdHorarioMedicina[k])).first();
					
					MiTblMedicina.EditarAlarmaHM(NewEditControlMedicinaActivity.this, miHM.getIdAlarmaClock(), usuario, campo_medicamento, campo_motivoMedicacion, campo_dosis, campo_nDeVeces, campo_Hora, 
												 campo_Minutos, campo_Domingo, campo_Lunes, campo_Martes, campo_Miercoles, campo_Jueves, campo_Viernes, campo_Sabados, campo_tono, campo_ActDesAlarma);						
				}				
			}else {
				l+=10;
								
				//VERIFICAR SI EL CUIDADOR QUE ELIMINA EL REGISTRO DE HM TIENE EL PERMISO DE MEDICINAS ACTIVADO
				//SI ESTA ACTIVADO SE ELIMINARA LA ALARMA EN EL CELL
				TblPermisos verPermiso = Select.from(TblPermisos.class).where(Condition.prop("id_cuidador").eq(vIdCuidador),
																			  Condition.prop("id_paciente").eq(vIdPaciente),
																			  Condition.prop("cont_medicina").eq(1),
																			  Condition.prop("Eliminado").eq(0)).first();
				if (verPermiso!=null) {
            		//ELIMINAR DEL CELL ALARMA DE HM 
            		MiTblMedicina.EliminarAlarmaHM(NewEditControlMedicinaActivity.this, vIdHorarioMedicina[k]);						
				}
				
				//FINALMENTE ELIMINAR EL REGISTRO DE LA TABLA HORARIO DE MEDICINA
				ATHorarioMedicinas horMed = new ATHorarioMedicinas();
        		horMed.new EliminarHorarioMedicina().execute(vIdHorarioMedicina[k].toString());
			}
		}			
	}	
	
	public void GuardarHorariosMedicina2() {
		l=idx;
		for (int k = continua; k < hm; k++) {
			f=k;
			GuaHorMed();
		}			
	}	
	
	//----METODOS PARA UN NUEVO REGISTRO DE HORARIOS DE MEDICINAS
		
	public void BtnNuevoHorario() {
		BtnNewAgregarHM.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				//CREAMOS LOS NUEVOS ELEMENTOS DINAMICOS (adaptador_lista_hm)
				LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				final View addView = layoutInflater.inflate (R.layout.adaptador_lista_hm, null);
				
				//BUSCAR LOS ELEMENTOS POR ID
				final Button BtnNewHora = (Button)addView.findViewById(R.id.btnNewHora);				
				BtnEliminar = (ImageButton)addView.findViewById(R.id.btnEliminar);
				ToggAlarma = (ToggleButton)addView.findViewById(R.id.toggAlarma);
				ToggleD = (ToggleButton)addView.findViewById(R.id.toggleD);
				ToggleL = (ToggleButton)addView.findViewById(R.id.toggleL);
				ToggleMa = (ToggleButton)addView.findViewById(R.id.toggleMa);
				ToggleMi = (ToggleButton)addView.findViewById(R.id.toggleMi);
				ToggleJ = (ToggleButton)addView.findViewById(R.id.toggleJ);
				ToggleV = (ToggleButton)addView.findViewById(R.id.toggleV);
				ToggleS = (ToggleButton)addView.findViewById(R.id.toggleS);						
							
				//CAMBIAR EL ID DE LOS ELEMENTOS
				BtnNewHora.setId(i);
				final int indexBtnNewHora = i;			
				BtnEliminar.setId(i++);
				ToggAlarma.setId(i++);
				ToggleD.setId(i++);
				ToggleL.setId(i++);
				ToggleMa.setId(i++);
				ToggleMi.setId(i++);
				ToggleJ.setId(i++);
				ToggleV.setId(i++);
				ToggleS.setId(i++);
				
				//AGREGAR LA VISTA AL LAYOUTCM
				LayoutCM.addView(addView);
				i++;
				final int indexLayout = hm;
				vecLayout[indexLayout] = true;
				hm++;							
															
				//BOTON HORA DE LA MEDICINA
		 		BtnNewHora.setOnClickListener(new OnClickListener() {	 			
					@Override
					public void onClick(View v) {				
						idBtnNewHora=indexBtnNewHora;						 				
						DialogoTimePicker();
					}
				});		 		
		 		onTimeSetListener = new OnTimeSetListener() {
		 			@Override
		 			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		 				Button BtnHora=(Button)findViewById(idBtnNewHora);
	 					BtnHora.setText(String.format("%02d:%02d", hourOfDay, minute));	 					  		
		 			}
		 		};
		 				 				 		
		 		//BOTON ELIMINAR HORARIO DE LA MEDICINA
		 		BtnEliminar.setOnClickListener(new OnClickListener() {					
					@Override
					public void onClick(View v) {						
						idLayout=indexLayout;
						vecLayout[idLayout]=false;
						LayoutCM.removeView(addView);						
						eliminados++;					
					}
				});		 				 		
			}
		});
	}
	
	public void GuaHorMed() {
		if (vecLayout[f].equals(true)) {			
			//BUSCAR LOS ELEMENTOS POR ID
			Button BHora=(Button)findViewById(l);
			l++; //Corresponde al boton eliminar...
			ToggleButton SAlarma=(ToggleButton)findViewById(l++);	
			ToggleButton TDo=(ToggleButton)findViewById(l++);	
			ToggleButton TLu=(ToggleButton)findViewById(l++);	
			ToggleButton TMa=(ToggleButton)findViewById(l++);	
			ToggleButton TMi=(ToggleButton)findViewById(l++);	
			ToggleButton TJu=(ToggleButton)findViewById(l++);	
			ToggleButton TVi=(ToggleButton)findViewById(l++);	
			ToggleButton TSa=(ToggleButton)findViewById(l++);	
			l++;
					
			//TOMAR EL VALOR INGRESADO EN LOS ELEMENTOS
			String HoraMinutos=BHora.getText().toString();
			String hora=HoraMinutos.substring(0, HoraMinutos.length()-3);
			String minutos=HoraMinutos.substring(3, HoraMinutos.length());			
			campo_Hora=Integer.parseInt(hora);
			campo_Minutos=Integer.parseInt(minutos);			
			campo_ActDesAlarma=SAlarma.isChecked();
			campo_Domingo=TDo.isChecked();
			campo_Lunes=TLu.isChecked();
			campo_Martes=TMa.isChecked();
			campo_Miercoles=TMi.isChecked();
			campo_Jueves=TJu.isChecked();
			campo_Viernes=TVi.isChecked();
			campo_Sabados=TSa.isChecked();
			
			//GUARDAR DATOS EN LA TABLA HORARIOS DE MEDICINA
			ATHorarioMedicinas horMed = new ATHorarioMedicinas();
			Long idHora=0L;
			try {
				idHora=horMed.new InsertarHorarioMedicinas().execute("0", String.valueOf(campo_idCM), String.valueOf(campo_Hora), String.valueOf(campo_Minutos),  
															campo_Domingo.toString(), campo_Lunes.toString(), campo_Martes.toString(), campo_Miercoles.toString(), 
															campo_Jueves.toString(), campo_Viernes.toString(), campo_Sabados.toString(), campo_ActDesAlarma.toString(), 
															"false").get().longValue();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}

			//VERIFICAR SI EL CUIDADOR QUE CREA EL REGISTRO DE CONTROL DE MEDICINA TIENE EL PERMISO DE CONT. DE MEDICINAS ACTIVADO
			//SI ESTA ACTIVADO SE LE CREARA LA ALARMA EN EL CELL
			TblPermisos verPermiso = Select.from(TblPermisos.class).where(Condition.prop("id_cuidador").eq(vIdCuidador),
																		  Condition.prop("id_paciente").eq(vIdPaciente),
																		  Condition.prop("cont_medicina").eq(1),
																		  Condition.prop("Eliminado").eq(0)).first();
			if (verPermiso!=null) {
				//CREAR EN EL CELL ALARMA DE HM
				MiTblMedicina.CrearAlarmaHM(NewEditControlMedicinaActivity.this, idHora, vIdPaciente, usuario, campo_medicamento, campo_motivoMedicacion, campo_dosis, campo_nDeVeces, campo_Hora,
											campo_Minutos, campo_Domingo, campo_Lunes, campo_Martes, campo_Miercoles, campo_Jueves, campo_Viernes, campo_Sabados, campo_tono, campo_ActDesAlarma);
			}
		}else {
			l+=10;
		}
	}
			
	//----METODOS TANTO PARA NUEVO O EDITAR REGISTROS
	
	public void Validaciones() {		
		//TOMAR DATOS DE LOS ELEMENTOS
		campo_medicamento=EdtNewMedicamento.getText().toString().trim();
		campo_descripcion=EdtNewDescrMedica.getText().toString().trim();
		campo_motivoMedicacion=EdtNewMotiMedi.getText().toString().trim();
		campo_tiempo=CmbNewTipoTrata.getSelectedItem().toString();			
		campo_dosis=EdtNewDosis.getText().toString().trim();
		campo_tono=TxtTono.getText().toString().trim();
			
		nDeVeces=EdtNewNveces.getText().toString().trim();
		if (!nDeVeces.equals("")) {
			campo_nDeVeces=Integer.parseInt(EdtNewNveces.getText().toString().trim());				
		}
		
		//VALIDAR CAMPOS VACIOS
		if((campo_medicamento.equals(""))||(campo_descripcion.equals(""))||(campo_motivoMedicacion.equals(""))||(campo_tiempo.equals(""))||
		   (campo_dosis.equals(""))||(nDeVeces.equals(""))||(campo_tono.equals(getString(R.string.SeleccioneTono)))) 
		{	
			FragmentManager fragmentManager1 = getFragmentManager();
			DFIngresarDatos dialogo1 = new DFIngresarDatos();
	        dialogo1.show(fragmentManager1, "tagAlerta");
	        banderaGuardar=false;
		}else {									
			//VALIDAR SI EXISTE UN HORARIO DE MEDICINA 
			int n=hm-eliminados;					
			if (n==0) {			
				FragmentManager fragmentManager5 = getFragmentManager();
				DFCrearHorario dialogo5 = new DFCrearHorario();
		        dialogo5.show(fragmentManager5, "tagAlerta");
		        banderaGuardar=false;
			}else {
				//VALIDAR DIAS DEL HORARIO DE MEDICINA
				int l=1;	
				for (int k = 0; k < hm; k++) {			
					if (vecLayout[k].equals(true)) {						
						//BUSCAR LOS ELEMENTOS POR ID
						l+=2; 					
						ToggleButton TDo=(ToggleButton)findViewById(l++); 
						ToggleButton TLu=(ToggleButton)findViewById(l++);
						ToggleButton TMa=(ToggleButton)findViewById(l++);
						ToggleButton TMi=(ToggleButton)findViewById(l++);
						ToggleButton TJu=(ToggleButton)findViewById(l++);
						ToggleButton TVi=(ToggleButton)findViewById(l++);
						ToggleButton TSa=(ToggleButton)findViewById(l++);
						l++;
								
						//TOMAR EL VALOR INGRESADO EN LOS ELEMENTOS						
						campo_Domingo=TDo.isChecked();
						campo_Lunes=TLu.isChecked();
						campo_Martes=TMa.isChecked();
						campo_Miercoles=TMi.isChecked();
						campo_Jueves=TJu.isChecked();
						campo_Viernes=TVi.isChecked();
						campo_Sabados=TSa.isChecked();	
						
						//VALIDAR LA SELECCION DE UN DIA
						if(campo_Domingo.equals(false) && campo_Lunes.equals(false) && campo_Martes.equals(false) && campo_Miercoles.equals(false) &&
					       campo_Jueves.equals(false) && campo_Viernes.equals(false) && campo_Sabados.equals(false))		    		 
			    		{    			
			    			FragmentManager fragmentManager6 = getFragmentManager();
							DFSeleccioneUnDia dialogo6 = new DFSeleccioneUnDia();
			    	        dialogo6.show(fragmentManager6, "tagAlerta");
			    	        banderaGuardar=false;
			    	        k=hm;
						}
					}else {
						l+=10;
					}
				}
			}					
		}
	}
	
	public void DialogoTimePicker() {
		final Calendar calendar = Calendar.getInstance(); 		
		hours = calendar.get(Calendar.HOUR);
		minutes = calendar.get(Calendar.MINUTE);
		
		timePickerDialog = new TimePickerDialog(NewEditControlMedicinaActivity.this, onTimeSetListener, hours, minutes, true);
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
	
    public void	SeleccionarTonoAlarma() {
		RingtoneContainer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DFTonos1 dialogo = new DFTonos1();
				dialogo.show(fragmentManager, "tagAlerta");
			}
		});
    }

	public AlertDialog SeleccionarTono() {
		final CharSequence[] listaTonos=getResources().getStringArray(R.array.opc_tonos);

		AlertDialog.Builder builder = new AlertDialog.Builder(NewEditControlMedicinaActivity.this);
		builder.setTitle(R.string.tonos_prompt)
				.setSingleChoiceItems(listaTonos, 0, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						//INICIA EL TONO LA PRIMERA VEZ O DETIENE EL TONO ANTERIOR
						if (banderaTono.equals(false)) {
							banderaTono = true;
						} else {
							mPlayer.stop();
						}
						//GENERA EL SONIDO
						seleccion = (String) listaTonos[arg1];
						if (seleccion != null && !seleccion.equals("")) {
							int resultado = TonosClass.BuscarIdTono(seleccion);
							mPlayer = MediaPlayer.create(NewEditControlMedicinaActivity.this, resultado);
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
		EdtNewMedicamento.getText().clear();
		EdtNewDescrMedica.getText().clear();
		EdtNewMotiMedi.getText().clear();	
		EdtNewDosis.getText().clear();
		EdtNewNveces.getText().clear();
		TxtTono.setText(R.string.SeleccioneTono);
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
				Toast.makeText(NewEditControlMedicinaActivity.this, "No hay Conexion a Internet", Toast.LENGTH_SHORT).show();
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