package com.Notifications.patientssassistant;


import com.Notifications.patientssassistant.dialogos.*;
import com.Notifications.patientssassistant.tables.*;
import com.Notifications.patientssassistant.asynctask.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.orm.query.Condition;
import com.orm.query.Select;


public class NewEveRutCuidadorActivity extends Activity {

	//VARIABLES PARA NUEVO REGISTRO
	private Long IdActividad;
	private Long vIdCuidador;	

	//VARIABLES QUE ALMACENAN LOS DATOS A GUARDAR
	private Long campo_idTipoActividad;
	private String campo_nombreActividad;
				
	//VARIABLES DE LOS ELEMENTOS DE LA IU
	TextView TxtNombre;
	EditText EdtNombre;	
	Button BtnGuardar;
	Button BtnCancelar;
		
	//VARIABLES EXTRAS
	private Boolean banderaGuardar=false;
	private TblTipoActividad tipoActividad;
	private FragmentManager fragmentManager = getFragmentManager(); 
			
	
	public NewEveRutCuidadorActivity() { super(); }

	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nueva_actividad_e_r); 
		
		TxtNombre=(TextView)findViewById(R.id.txtNombre);
		EdtNombre=(EditText)findViewById(R.id.edtNombre);
		BtnGuardar=(Button)findViewById(R.id.btnGuardar);
		BtnCancelar=(Button)findViewById(R.id.btnCancelar);
					
		NuevoVsEditarActividades();		    	
	}
	
	//METODO QUE SERVIRA PARA IR A NUEVO O EDITAR REGISTRO DE ACTIVIDADES
	public void NuevoVsEditarActividades() {
		//EVENTOS		
		Boolean opcion1= NewEditEventoCuidadorActivity.EsEvento();	
		if (opcion1.equals(true)) {
			NewEveRutCuidadorActivity.this.setTitle(R.string.NewEvento);
			TxtNombre.setText(R.string.NombreEvento);		
			RecogerParametros();
			BtnNuevoEvento();
			BtnCancelar();
		}		
		//RUTINAS		
		Boolean opcion2= NewEditRutinaCuidadorActivity.EsRutina();
		if (opcion2.equals(true)) {
			NewEveRutCuidadorActivity.this.setTitle(R.string.NewRutina);
			TxtNombre.setText(R.string.NombreRutina);
			RecogerParametros();
			BtnNuevaRutina();
			BtnCancelar();
		}		
	}
		
	//----METODOS PARA UN NUEVO EVENTO DEL CUIDADOR
				
	public void BtnNuevoEvento() {		
		BtnGuardar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(estaConectado()) {
					try {
						tipoActividad = Select.from(TblTipoActividad.class).where(Condition.prop("tipo_actividad").eq("EVENTO"),
								Condition.prop("Eliminado").eq(0)).first();
						ValidarDatos1();

						if (banderaGuardar.equals(true)) {
							//GUARDAR DATOS EN LA TABLA ACTIVIDADES
							ATActividades actividad = new ATActividades();
							Long idAct = 0L;
							try {
								idAct = actividad.new InsertarActividades().execute("0", String.valueOf(3), campo_nombreActividad, campo_nombreActividad, " ", " ", "false").get().longValue();
							} catch (InterruptedException e) {
								e.printStackTrace();
							} catch (ExecutionException e) {
								e.printStackTrace();
							}

							IdActividad = idAct;

							//GUARDAR DATOS EN LA TABLA ACTIVIDAD/CUIDADOR
							ATActividadCuidadores actC = new ATActividadCuidadores();
							actC.new InsertarActividadCuidadores().execute(String.valueOf(vIdCuidador), String.valueOf(IdActividad), "false");

							LimpiarElementos();
							Toast.makeText(NewEveRutCuidadorActivity.this, R.string.GuardadoR, Toast.LENGTH_SHORT).show();
							Intent data = new Intent();
							setResult(RESULT_OK, data);
							finish();
						}
					} catch (Exception ex) {
						Toast.makeText(NewEveRutCuidadorActivity.this, getString(R.string.ErrorGuardar) + ex.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}	
	
	//----METODOS PARA UNA NUEVA RUTINA DEL CUIDADOR
	
	public void BtnNuevaRutina() {		
		BtnGuardar.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if(estaConectado()) {
					try {
						tipoActividad = Select.from(TblTipoActividad.class).where(Condition.prop("tipo_actividad").eq("RUTINA"),
								Condition.prop("Eliminado").eq(0)).first();
						ValidarDatos1();
						if (banderaGuardar.equals(true)) {
							//GUARDAR DATOS EN LA TABLA ACTIVIDADES
							ATActividades actividad = new ATActividades();
							Long idAct = 0L;
							try {
								idAct = actividad.new InsertarActividades().execute("0", String.valueOf(4), campo_nombreActividad, campo_nombreActividad, " ", " ", "false").get().longValue();
							} catch (InterruptedException e) {
								e.printStackTrace();
							} catch (ExecutionException e) {
								e.printStackTrace();
							}

							IdActividad = idAct;

							//GUARDAR DATOS EN LA TABLA ACTIVIDAD/CUIDADOR
							ATActividadCuidadores actC = new ATActividadCuidadores();
							actC.new InsertarActividadCuidadores().execute(String.valueOf(vIdCuidador), String.valueOf(IdActividad), "false");

							LimpiarElementos();
							Toast.makeText(NewEveRutCuidadorActivity.this, R.string.GuardadoR, Toast.LENGTH_SHORT).show();
							Intent data = new Intent();
							setResult(RESULT_OK, data);
							finish();
						}
					} catch (Exception ex) {
						Toast.makeText(NewEveRutCuidadorActivity.this, getString(R.string.ErrorGuardar) + ex.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}
		
	//----METODOS TANTO PARA EVENTOS Y RUTINAS
	
	public void RecogerParametros() {
		vIdCuidador = getIntent().getExtras().getLong("varIdeCuidador");		
	}

	public Boolean ValidarDatos1() {		
		campo_idTipoActividad=tipoActividad.getIdTipoActividad();
		campo_nombreActividad=EdtNombre.getText().toString().trim();
		
		if(campo_nombreActividad.equals(""))		    		 
		{
			DFNueAct dialogo1 = new DFNueAct();
	        dialogo1.show(fragmentManager, "tagAlerta");
		}else {
			banderaGuardar=true;
			//VALIDAMOS QUE EL NOMBRE DE LA ACTIVIDAD NO EXISTA
			//---------------------> servidor
			List<TblActividadCuidador>list_ActCui = Select.from(TblActividadCuidador.class).where(Condition.prop("id_cuidador").eq(vIdCuidador),
																								  Condition.prop("Eliminado").eq(0)).list();
			int i=0;
			while (list_ActCui.size() > i) {
				TblActividadCuidador tablaActCui = new TblActividadCuidador();
				tablaActCui = list_ActCui.get(i);			
				TblActividades laActividad = Select.from(TblActividades.class).where(Condition.prop("id_actividad").eq(tablaActCui.getIdActividad()),
																					 Condition.prop("id_tipo_actividad").eq(campo_idTipoActividad),
																					 Condition.prop("nombre_actividad").eq(campo_nombreActividad),
																					 Condition.prop("Eliminado").eq(0)).first();
				if (laActividad!=null) {				
					banderaGuardar=false;
					i=list_ActCui.size();
					if (tipoActividad.getTipoActividad().equals("EVENTO")) {
						DFEventoRepetido dialogo2 = new DFEventoRepetido();
				        dialogo2.show(fragmentManager, "tagAlerta");						
					}
					if (tipoActividad.getTipoActividad().equals("RUTINA")) {
						DFRutinaRepetida dialogo3 = new DFRutinaRepetida();
				        dialogo3.show(fragmentManager, "tagAlerta");						
					}					
				}			
				i++;
			}			
		}				
		return banderaGuardar;
	}

	public void LimpiarElementos() {			
		EdtNombre.getText().clear();		
	}	
	
	public void BtnCancelar() {
		BtnCancelar.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				finish();				
			}
		});
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
				Toast.makeText(NewEveRutCuidadorActivity.this, "No hay Conexion a Internet", Toast.LENGTH_SHORT).show();
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