package com.Notifications.patientssassistant;


import com.Notifications.patientssassistant.adapters.*;
import com.Notifications.patientssassistant.dialogos.*;
import com.Notifications.patientssassistant.fragments.*;
import com.Notifications.patientssassistant.tables.*;
import com.Notifications.patientssassistant.asynctask.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.orm.query.Condition;
import com.orm.query.Select;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class NewEditPermisoActivity extends Activity {

	//VARIABLES PARA NUEVO REGISTRO
	private Long vItemIdCuidador;
	private String vNombres;
	private String vFoto;
	private Long vIdCuidador;
	private Long vDependeDe;
		
	//VARIABLES PARA EDITAR REGISTRO
	private Long vIdPermiso;
	private Long vIdPaciente;
	private Boolean vNotifiAlarma;	
	private Boolean vContMedicina;
			
	//VARIABLES QUE ALMACENAN LOS DATOS A GUARDAR
	private Long campo_idPaciente;
	private Boolean campo_notifiAlarma;	
	private Boolean campo_contMedicina;
	
	//VARIABLES DE LOS ELEMENTOS DE LA IU
	ImageView ImagenFotoCu;
	TextView TxtNombreCu;
	Spinner CmbNewPaciente;
	CheckedTextView ChckNotiAlar;	
	CheckedTextView ChckContMedi;
	Button BtnNuevoEditar;
	
	//VARIABLES EXTRAS
	private Boolean banderaGuardar=false;
	private AdapterSpinnerPacientes miAdapter;	
	private List<TblPermisos> lista_pacientes;
	private FragmentManager fragmentManager = getFragmentManager();
	
		
	public NewEditPermisoActivity() { super(); }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nuevo_permiso);
		
		ImagenFotoCu=(ImageView)findViewById(R.id.imagenFotoCu);
		TxtNombreCu=(TextView)findViewById(R.id.txtNombreCu);
		CmbNewPaciente=(Spinner)findViewById(R.id.cmbNewPaciente);
		ChckNotiAlar=(CheckedTextView)findViewById(R.id.chckNotiAlar);
		ChckContMedi=(CheckedTextView)findViewById(R.id.chckContMedi);
		BtnNuevoEditar=(Button)findViewById(R.id.btnNewGuardar);	
		
		NuevoVsEditar();
	}
	
	//METODO QUE SERVIRA PARA IR A NUEVO O EDITAR REGISTRO DE PERMISO
	public void NuevoVsEditar() {		
		Boolean opcion1= TabCuidador3Pp2F.EsNuevo();
		Boolean opcion2= TabCuidador3Pp2F.EsEditar();
		Boolean opcion3= TabCuidador3Pp1F.EsEditar();
		
		if (opcion1.equals(true)) {			
			NewEditPermisoActivity.this.setTitle(R.string.NewPermiso);
	    	RecogerParametrosFragTabCuiPp();
			CargarFotoNombreCuidador();
			CargarSpinnerPacientes();   	    	
	    	BtnNuevoPermiso();
		}
		if (opcion2.equals(true) || opcion3.equals(true)) {
			NewEditPermisoActivity.this.setTitle(R.string.EditPermiso);
	    	RecogerParametrosFragTabCuiPp2();
			CargarDatosEnLosElementos();   	    	
	    	BtnEditarPermiso();
		}
	}
	
	//----METODOS PARA UN NUEVO REGISTRO
	
	public void RecogerParametrosFragTabCuiPp() {
		vItemIdCuidador = getIntent().getExtras().getLong("varItemIdCuidador");
		vNombres = getIntent().getExtras().getString("varNombreCuidador");
		vFoto = getIntent().getExtras().getString("varFotoCuidador");
		vIdCuidador = getIntent().getExtras().getLong("varIdCuidador");
		vDependeDe = getIntent().getExtras().getLong("varDependeDe");		
	}
	
	public void CargarFotoNombreCuidador() {
		if (vFoto.equals("")) {
			ImagenFotoCu.setImageResource(R.drawable.user_foto);				
		}else{
			byte[] b = Base64.decode(vFoto, Base64.DEFAULT);	        	
			Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        	ImagenFotoCu.setImageBitmap(bitmap);
		} 	    	
		TxtNombreCu.setText(vNombres);
	}
	
	public void BtnNuevoPermiso() {
		BtnNuevoEditar.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if(estaConectado()) {
					try {
						VerificarDatos1();
						if (banderaGuardar.equals(true)) {
							//NUEVO PERMISO DE UN CUIDADOR SECUNDARIO
							ATPermisos newPermiso = new ATPermisos();
							newPermiso.new InsertarPermiso().execute("0", vItemIdCuidador.toString(), campo_idPaciente.toString(), campo_notifiAlarma.toString(), "false", campo_contMedicina.toString(), "false");

							//VERIFICAR SI EL CUIDADOR QUE INICIO SESION ES A QUIEN SE LE AGREGA EL PERMISO
							if (vIdCuidador.equals(vItemIdCuidador)) {
								//CREAR LAS ALARMAS DE LOS EVENTOS Y RUTINAS DEL PACIENTE A SU CARGO
								CrearAlarmasEventosRutinasP();
								//CREAR LAS ALARMAS DE LAS MEDICINAS DEL PACIENTE A SU CARGO
								CrearAlarmasMedicinasP();
							}

							LimpiarElementos();
							Toast.makeText(NewEditPermisoActivity.this, R.string.GuardadoR, Toast.LENGTH_SHORT).show();
							Intent data = new Intent();
							setResult(RESULT_OK, data);
							finish();
						}
					} catch (Exception ex) {
						Toast.makeText(NewEditPermisoActivity.this, getString(R.string.ErrorGuardar) + ex.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}	
	
	public Boolean VerificarDatos1() {
		campo_notifiAlarma=ChckNotiAlar.isChecked();
		campo_contMedicina=ChckContMedi.isChecked();
		
		//VALIDAR CAMPOS VACIOS						    		       			    		
		if((campo_notifiAlarma.equals(false)) && (campo_contMedicina.equals(false)))
		{
			DFSeleccioneUnPermiso dialogo1 = new DFSeleccioneUnPermiso();
	        dialogo1.show(fragmentManager, "tagAlerta");
		}else {
			//PERMITE GUARDAR EL PERMISO
			banderaGuardar=true;
		}
		return banderaGuardar;
	}
	

	//----METODOS PARA EDITAR UN REGISTRO
	
	public void RecogerParametrosFragTabCuiPp2() {
		RecogerParametrosFragTabCuiPp();		
		vIdPermiso = getIntent().getExtras().getLong("varIdPermiso");
		vIdPaciente = getIntent().getExtras().getLong("varIdPaciente");
		vNotifiAlarma = getIntent().getExtras().getBoolean("varNotifiAlarma");
		vContMedicina = getIntent().getExtras().getBoolean("varContMedicina");
		campo_idPaciente=vIdPaciente;
	}
	
	public void CargarDatosEnLosElementos() {
		CargarFotoNombreCuidador();	
		ChckNotiAlar.setChecked(vNotifiAlarma);
		ChckContMedi.setChecked(vContMedicina);			
    	//CARGANDO EL DATO GUARDADO EN EL SPINNER 
		List<TblPermisos> el_paciente = Select.from(TblPermisos.class).where(Condition.prop("id_paciente").eq(vIdPaciente),
																			 Condition.prop("id_cuidador").eq(vDependeDe)).list();
		miAdapter=new AdapterSpinnerPacientes(NewEditPermisoActivity.this, el_paciente);
	    CmbNewPaciente.setAdapter(miAdapter);
	    CmbNewPaciente.setClickable(false);
	    ActCheckedTextView();
	}
		
	public void BtnEditarPermiso() {		
		BtnNuevoEditar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (estaConectado()) {
					try {
						VerificarDatos1();
						if (banderaGuardar.equals(true)) {
							//EDITAR PERMISO DE UN CUIDADOR PRIMARIO O SECUNDARIO
							ATPermisos newPermiso = new ATPermisos();
							newPermiso.new ActualizarPermiso().execute(vIdPermiso.toString(), campo_notifiAlarma.toString(), campo_contMedicina.toString());

							//VERIFICAR SI EL CUIDADOR QUE INICIO SESION ES A QUIEN SE LE AGREGA EL PERMISO
							if (vIdCuidador.equals(vItemIdCuidador)) {

								if (!vNotifiAlarma.equals(campo_notifiAlarma)) {
									//CREAR LAS ALARMAS DE LOS EVENTOS Y RUTINAS DEL PACIENTE A SU CARGO
									CrearAlarmasEventosRutinasP();
									//ELIMINAR LAS ALARMAS DE LOS EVENTOS Y RUTINAS DEL PACIENTE A SU CARGO
									if (campo_notifiAlarma.equals(false)) {
										MiTblEvento.EliminarAlarmasEventosPac(NewEditPermisoActivity.this, campo_idPaciente);
										MiTblRutina.EliminarAlarmasRutinasPac(NewEditPermisoActivity.this, campo_idPaciente);
									}
								}

								if (!vContMedicina.equals(campo_contMedicina)) {
									//CREAR LAS ALARMAS DE LAS MEDICINAS DEL PACIENTE A SU CARGO
									CrearAlarmasMedicinasP();
									//ELIMINAR LAS ALARMAS DE LAS MEDICINAS DEL PACIENTE A SU CARGO
									if (campo_contMedicina.equals(false)) {
										MiTblMedicina.EliminarAlarmasMedicinasPac(NewEditPermisoActivity.this, campo_idPaciente);
									}
								}
							}

							LimpiarElementos();
							Toast.makeText(NewEditPermisoActivity.this, R.string.EditadoR, Toast.LENGTH_SHORT).show();
							Intent data = new Intent();
							setResult(RESULT_OK, data);
							finish();
						}
					} catch (Exception ex) {
						Toast.makeText(NewEditPermisoActivity.this, getString(R.string.ErrorEditar) + ex.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}


	//----METODOS TANTO PARA NUEVO O EDITAR REGISTRO	
	
	public void CargarSpinnerPacientes() {
	   	lista_pacientes= Select.from(TblPermisos.class).where(Condition.prop("id_cuidador").eq(vDependeDe),
	   														  Condition.prop("Eliminado").eq(0)).list();	   	
		//LISTA CON DATOS
		if (!lista_pacientes.isEmpty()) {	   		
	   		miAdapter=new AdapterSpinnerPacientes(NewEditPermisoActivity.this, lista_pacientes);
	    	CmbNewPaciente.setAdapter(miAdapter);
	    	ActCheckedTextView();
	    	SelectItemSpinner();
	    	
			Collections.sort(lista_pacientes, new Comparator<TblPermisos>(){  //ORDENA LA LISTA
				@Override
				public int compare(TblPermisos d1, TblPermisos d2) {
					TblPacientes paciente_clase1=Select.from(TblPacientes.class).where(Condition.prop("id_paciente").eq(d1.getIdPaciente())).first();
					TblPacientes paciente_clase2=Select.from(TblPacientes.class).where(Condition.prop("id_paciente").eq(d2.getIdPaciente())).first();
					return paciente_clase1.getNombreApellidoP().compareToIgnoreCase(paciente_clase2.getNombreApellidoP());				
				}
	        });
		}
		//LISTA VACIA
		if (lista_pacientes.isEmpty()) {
			String[] ListVacia={getResources().getString(R.string.NoHayPacientes)};
			CmbNewPaciente.setAdapter(new AdapterSpinnerSimple(NewEditPermisoActivity.this, R.layout.adaptador_spinner, ListVacia));
			CmbNewPaciente.setClickable(false);
			ChckNotiAlar.setClickable(false);
			ChckContMedi.setClickable(false);	    	   	
	    	BtnNuevoEditar.setClickable(false);
	    	BtnNuevoEditar.setEnabled(false);	 
		}
	}
	
	public void ActCheckedTextView() {
		ChckNotiAlar.setClickable(true);		    	
		ChckNotiAlar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ChckNotiAlar.toggle();
			}
		});
		ChckContMedi.setClickable(true);    	    	
    	ChckContMedi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ChckContMedi.toggle();
			}
		});		
	}
	
	public void SelectItemSpinner() {
		CmbNewPaciente.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				ChckNotiAlar.setChecked(false);
				ChckContMedi.setChecked(false);
				TblPermisos registro = (TblPermisos) parent.getItemAtPosition(position);
				campo_idPaciente = registro.getIdPaciente();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}

	public void CrearAlarmasEventosRutinasP() {
		if(campo_notifiAlarma.equals(true)) {
			List<TblEventosPacientes> listaEventosP= Select.from(TblEventosPacientes.class).where(Condition.prop("id_paciente").eq(String.valueOf(campo_idPaciente)),
																								Condition.prop("Alarma").eq(1),
																								Condition.prop("Eliminado").eq(0)).list();
			if (!listaEventosP.isEmpty()) {
				for (int j = 0; j < listaEventosP.size(); j++) {
					TblEventosPacientes evenP=listaEventosP.get(j);

					String fechaHoraE = String.valueOf(evenP.getDiaE())+"/"+String.valueOf(evenP.getMesE())+"/"+
										String.valueOf(evenP.getAnioE())+" "+String.valueOf(evenP.getHoraE())+":"+
										String.valueOf(evenP.getDiaE());

					TblPacientes paciente = Select.from(TblPacientes.class).where(Condition.prop("id_paciente").eq(String.valueOf(campo_idPaciente))).first();

					MiTblEvento.CrearAlarmaEvento(NewEditPermisoActivity.this, evenP.getIdEventoP(),
												evenP.getAnioR(), evenP.getMesR(), evenP.getDiaR(), evenP.getHoraR(),
												evenP.getMinutosR(), campo_idPaciente, "P", paciente.getNombreApellidoP(), "Evento", fechaHoraE,
												evenP.getLugar(), evenP.getDescripcion(), evenP.getTono(), evenP.getAlarma());
				}
			}

			List<TblRutinasPacientes> listaRutinasP= Select.from(TblRutinasPacientes.class).where(Condition.prop("id_paciente").eq(String.valueOf(campo_idPaciente)),
																								Condition.prop("Alarma").eq(1),
																								Condition.prop("Eliminado").eq(0)).list();
			if (!listaRutinasP.isEmpty()) {
				for (int j = 0; j < listaRutinasP.size(); j++) {
					TblRutinasPacientes rutP=listaRutinasP.get(j);

					TblPacientes paciente = Select.from(TblPacientes.class).where(Condition.prop("id_paciente").eq(String.valueOf(campo_idPaciente))).first();

					MiTblRutina.CrearAlarmaRutina(NewEditPermisoActivity.this, rutP.getIdRutinaP(), rutP.getHora(),
												rutP.getMinutos(), campo_idPaciente, "P", paciente.getNombreApellidoP(), "Rutina", rutP.getDomingo(),
												rutP.getLunes(), rutP.getMartes(), rutP.getMiercoles(), rutP.getJueves(),
												rutP.getViernes(), rutP.getSabado(), rutP.getTono(), rutP.getAlarma());
				}
			}
		}
	}

	public void CrearAlarmasMedicinasP() {
		if(campo_contMedicina.equals(true)) {
			List<TblControlMedicina> listaControlMed= Select.from(TblControlMedicina.class).where(Condition.prop("id_paciente").eq(String.valueOf(campo_idPaciente)),
																								Condition.prop("Eliminado").eq(0)).list();
			for (int j = 0; j < listaControlMed.size(); j++) {
				TblControlMedicina contMed=listaControlMed.get(j);

				List<TblHorarioMedicina> listaHorarioMed= Select.from(TblHorarioMedicina.class).where(Condition.prop("id_control_medicina").eq(String.valueOf(contMed.getIdControlMedicina())),
																									Condition.prop("act_des_alarma").eq(1),
																									Condition.prop("Eliminado").eq(0)).list();

				TblPacientes paciente = Select.from(TblPacientes.class).where(Condition.prop("id_paciente").eq(String.valueOf(campo_idPaciente))).first();

				for (int k = 0; k < listaHorarioMed.size(); k++) {
					TblHorarioMedicina horMed=listaHorarioMed.get(k);

					MiTblMedicina.CrearAlarmaHM(NewEditPermisoActivity.this, horMed.getIdControlMedicina(), campo_idPaciente, paciente.getNombreApellidoP(),
												contMed.getMedicamento(), contMed.getMotivoMedicacion(), contMed.getDosis(),
												contMed.getNdeVeces(), horMed.getHora(), horMed.getMinutos(), horMed.getDomingo(),
												horMed.getLunes(), horMed.getMartes(), horMed.getMiercoles(), horMed.getJueves(),
												horMed.getViernes(), horMed.getSabado(), contMed.getTono(), horMed.getActDesAlarma());
				}
			}
		}
	}
	
	public void LimpiarElementos() {
		ChckNotiAlar.setChecked(false);		
		ChckContMedi.setChecked(false);
		CargarSpinnerPacientes();
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
				Toast.makeText(NewEditPermisoActivity.this, "No hay Conexion a Internet", Toast.LENGTH_SHORT).show();
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