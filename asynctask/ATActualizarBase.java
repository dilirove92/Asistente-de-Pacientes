package com.Notifications.patientssassistant.asynctask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.R.id;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.Notifications.patientssassistant.tables.MiTblEvento;
import com.Notifications.patientssassistant.tables.MiTblRutina;
import com.Notifications.patientssassistant.tables.TblActividadCuidador;
import com.Notifications.patientssassistant.tables.TblActividadPaciente;
import com.Notifications.patientssassistant.tables.TblActividades;
import com.Notifications.patientssassistant.tables.TblBuzon;
import com.Notifications.patientssassistant.tables.TblControlDieta;
import com.Notifications.patientssassistant.tables.TblControlMedicina;
import com.Notifications.patientssassistant.tables.TblCuidador;
import com.Notifications.patientssassistant.tables.TblCuidadorPr;
import com.Notifications.patientssassistant.tables.TblCuidadorS;
import com.Notifications.patientssassistant.tables.TblEstadoSalud;
import com.Notifications.patientssassistant.tables.TblEventosCuidadores;
import com.Notifications.patientssassistant.tables.TblEventosPacientes;
import com.Notifications.patientssassistant.tables.TblFamiliaresPacientes;
import com.Notifications.patientssassistant.tables.TblHorarioMedicina;
import com.Notifications.patientssassistant.tables.TblHorarios;
import com.Notifications.patientssassistant.tables.TblInicioSesion;
import com.Notifications.patientssassistant.tables.TblObservaciones;
import com.Notifications.patientssassistant.tables.TblPacientes;
import com.Notifications.patientssassistant.tables.TblPermisos;
import com.Notifications.patientssassistant.tables.TblRutinasCuidadores;
import com.Notifications.patientssassistant.tables.TblRutinasPacientes;
import com.Notifications.patientssassistant.tables.TblSeguimientoMedico;
import com.Notifications.patientssassistant.tables.TblTipoActividad;
import com.orm.query.Condition;
import com.orm.query.Select;

public class ATActualizarBase {
	
	//private String Cuidador="Primario";
	//private String idCuidador;
	
	public Boolean ActualizarBD (String TipoCuidador, long IdCuidador){//Traer variables
		
		Boolean actualizado= true;
		String idCuidador=String.valueOf(IdCuidador);
		
		try {
			if(TipoCuidador.equals("CS")){
				
				ATCuidador cuidador = new ATCuidador();
				cuidador.new BuscarUnCuidador().execute(idCuidador);
				Select elCuidador = Select.from(TblCuidador.class)
						.where(Condition.prop("ID_ACTIVIDAD").eq(idCuidador));
		    	TblCuidador edit_Cuidador=(TblCuidador)elCuidador.first();
		    	
		    	if(edit_Cuidador.getControlTotal().equals("true")){
		    		
		    		ATCuidadorS cuidadorS = new ATCuidadorS();
		    		cuidadorS.new BuscarUnCuidadorS().execute(idCuidador);
		    		Select elCuidadorS = Select.from(TblCuidadorS.class)
							.where(Condition.prop("ID_ACTIVIDAD").eq(idCuidador));
			    	TblCuidadorS edit_CuidadorS=(TblCuidadorS)elCuidadorS.first();
			    	idCuidador= edit_CuidadorS.getDependeDe().toString();
		    	}
		    	
		    	EjecutarAsyncTasks(idCuidador);
			}
			if(TipoCuidador.equals("CP")){
				EjecutarAsyncTasks(idCuidador);
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("Problemas en Actualizar la BD", "Error!", e);
			actualizado=false;
		}
		
		return actualizado; 
	}
	
	public Boolean EjecutarAsyncTasks(String idCuidador){
		
		Boolean realizado=true;
		
		try {
			//ACTIVIDADES
			//TblActividades.deleteAll(TblActividades.class);
			ATActividades actividad = new ATActividades();
			actividad.new BuscarAllActividades().execute(idCuidador);
			
			//ACTIVIDADES CUIDADORES
			//TblActividadCuidador.deleteAll(TblActividadCuidador.class);
			ATActividadCuidadores actividadC = new ATActividadCuidadores();
			actividadC.new BuscarAllActividadCuidadores().execute(idCuidador);
			
			//ACTIVIDADES PACIENTES
			//TblActividadPaciente.deleteAll(TblActividadPaciente.class);
			ATActividadPaciente actividadP = new ATActividadPaciente();
			actividadP.new BuscarAllActividadCuidadores().execute(idCuidador);
			
			//BUZON
			//TblBuzon.deleteAll(TblBuzon.class);
			ATBuzon buzon = new ATBuzon();
			buzon.new BuscarAllBuzonesXCuidadores().execute(idCuidador);
			
			//CONTROL DIETA
			//TblControlDieta.deleteAll(TblControlDieta.class);
			ATControlDieta controlDieta = new ATControlDieta();
			controlDieta.new BuscarAllControlDietaCuidadores().execute(idCuidador);
			
			//CONTROL MEDICINA
			//TblControlMedicina.deleteAll(TblControlMedicina.class);
			ATControlMedicina controlMedicina = new ATControlMedicina();
			controlMedicina.new BuscarAllControlMedicinaXCuidadores().execute(idCuidador);
			
			//CUIDADOR
			//TblCuidador.deleteAll(TblCuidador.class);
			ATCuidador cuidador = new ATCuidador();
			cuidador.new  BuscarAllCuidadores().execute(idCuidador);
			
			//CUIDADORPR
			//TblCuidadorPr.deleteAll(TblCuidadorPr.class);
			ATCuidadorPr cuidadorPr = new ATCuidadorPr();
			cuidadorPr.new BuscarAllCuidadoresPr().execute(idCuidador);
			
			//CUIDADORS
			//TblCuidadorS.deleteAll(TblCuidadorS.class);
			ATCuidadorS cuidadorS = new ATCuidadorS();
			cuidadorS.new BuscarAllCuidadoresS().execute(idCuidador);
			
			//ESTADO DE SALUD
			//TblEstadoSalud.deleteAll(TblEstadoSalud.class);
			ATEstadoSalud estadoSalud= new ATEstadoSalud();
			estadoSalud.new BuscarAllEstadosSaludCuidadores().execute(idCuidador);
			
			//EVENTOS CUIDADORES
			//TblEventosCuidadores.deleteAll(TblEstadoSalud.class);
			ATEventosCuidador eventosCuidaores = new ATEventosCuidador();
			eventosCuidaores.new BuscarAllEventosCuidadores().execute(idCuidador);
			
			//EVENTOS PACIENTES
			//TblEventosPacientes.deleteAll(TblEventosPacientes.class);
			ATEventosPaciente eventosPacientes = new ATEventosPaciente();
			eventosPacientes.new BuscarAllEventosCuidadores().execute(idCuidador);
			
			//FAMILIARES PACIENTES
			//TblFamiliaresPacientes.deleteAll(TblFamiliaresPacientes.class);
			ATFamiliaresPacientes familiares = new ATFamiliaresPacientes();
			familiares.new BuscarAllFamiliaresPacientesCuidadores().execute(idCuidador);
			
			//HORARIOS MEDICINAS
			//TblHorarioMedicina.deleteAll(TblHorarioMedicina.class);
			ATHorarioMedicinas horariosMed = new ATHorarioMedicinas();
			horariosMed.new BuscarAllHorarioMedicinaXCuidadores().execute(idCuidador);
			
			//HORARIOS
			//TblHorarios.deleteAll(TblHorarios.class);
			ATHorarios horarios = new ATHorarios();
			horarios.new BuscarAllHorariosXCuidadores().execute(idCuidador);
			
			//OBSERVACIONES
			//TblObservaciones.deleteAll(TblObservaciones.class);
			ATObservaciones observaciones = new ATObservaciones();
			observaciones.new BuscarAllObservacionesCuidadores().execute(idCuidador);
			
			//PACIENTES
			//TblPacientes.deleteAll(TblPacientes.class);
			ATPacientes pacientes = new ATPacientes();
			pacientes.new BuscarAllPacientes().execute(idCuidador);
			
			//PERMISOS
			//TblPermisos.deleteAll(TblPermisos.class);
			ATPermisos permisos = new ATPermisos();
			permisos.new BuscarAllPermisosXCuidadores().execute(idCuidador);
			
			//RUTINAS CUIDADORES
			//TblRutinasCuidadores.deleteAll(TblRutinasCuidadores.class);
			ATRutinasCuidadores rutinasC = new ATRutinasCuidadores();
			rutinasC.new BuscarAllRutinasCuidadores().execute(idCuidador);
			
			//RUTINAS PACIENTES
			//TblRutinasPacientes.deleteAll(TblRutinasPacientes.class);
			ATRutinasPacientes rutinasP = new ATRutinasPacientes();
			rutinasP.new BuscarAllRutinasCuidadores().execute(idCuidador);
			
			//SEGUIMIENTO MEDICO
			//TblSeguimientoMedico.deleteAll(TblSeguimientoMedico.class);
			ATSeguimientoMedico seguimientoMed = new ATSeguimientoMedico();
			seguimientoMed.new BuscarAllSeguimientoMedicoXCuidadores().execute(idCuidador);
			
			//TIPO ACTIVIDAD
			//TblTipoActividad.deleteAll(TblTipoActividad.class);
			ATTipoActividad tipoActividad = new ATTipoActividad();
			tipoActividad.new BuscarAllTipoActividad().execute(idCuidador);
			
		} catch (Exception ex) {
			// TODO: handle exception
			Log.e("Problemas en un AsyncTask", "Error!", ex);
			realizado=false;
		}
		return realizado;
	}

	
	public class ActualizarBaseAsynctask extends AsyncTask<String, Integer, Boolean>
    {
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			Boolean realizado=true;
			String TipoCuidador=params[0];
			String idCuidador=params[1];
			
			if(TipoCuidador.equals("CS")){
				
				ATCuidador cuidador = new ATCuidador();
				cuidador.new BuscarUnCuidador().execute(idCuidador);
				Select elCuidador = Select.from(TblCuidador.class)
						.where(Condition.prop("ID_ACTIVIDAD").eq(idCuidador));
		    	TblCuidador edit_Cuidador=(TblCuidador)elCuidador.first();
		    	
		    	if(edit_Cuidador.getControlTotal().equals("true")){
		    		
		    		ATCuidadorS cuidadorS = new ATCuidadorS();
		    		cuidadorS.new BuscarUnCuidadorS().execute(idCuidador);
		    		Select elCuidadorS = Select.from(TblCuidadorS.class)
							.where(Condition.prop("ID_ACTIVIDAD").eq(idCuidador));
			    	TblCuidadorS edit_CuidadorS=(TblCuidadorS)elCuidadorS.first();
			    	idCuidador= edit_CuidadorS.getDependeDe().toString();
		    	}	
			}
			
			try {
				//ACTIVIDADES
				//TblActividades.deleteAll(TblActividades.class);
				ATActividades actividad = new ATActividades();
				actividad.new BuscarAllActividades().execute(idCuidador);
				
				//ACTIVIDADES CUIDADORES
				//TblActividadCuidador.deleteAll(TblActividadCuidador.class);
				ATActividadCuidadores actividadC = new ATActividadCuidadores();
				actividadC.new BuscarAllActividadCuidadores().execute(idCuidador);;
				
				//ACTIVIDADES PACIENTES
				//TblActividadPaciente.deleteAll(TblActividadPaciente.class);
				ATActividadPaciente actividadP = new ATActividadPaciente();
				actividadP.new BuscarAllActividadCuidadores().execute(idCuidador);
				
				//BUZON
				//TblBuzon.deleteAll(TblBuzon.class);
				ATBuzon buzon = new ATBuzon();
				buzon.new BuscarAllBuzonesXCuidadores().execute(idCuidador);
				
				//CONTROL DIETA
				//TblControlDieta.deleteAll(TblControlDieta.class);
				ATControlDieta controlDieta = new ATControlDieta();
				controlDieta.new BuscarAllControlDietaCuidadores().execute(idCuidador);
				
				//CONTROL MEDICINA
				//TblControlMedicina.deleteAll(TblControlMedicina.class);
				ATControlMedicina controlMedicina = new ATControlMedicina();
				controlMedicina.new BuscarAllControlMedicinaXCuidadores().execute(idCuidador);
				
				//CUIDADOR
				//TblCuidador.deleteAll(TblCuidador.class);
				ATCuidador cuidador = new ATCuidador();
				cuidador.new  BuscarAllCuidadores().execute(idCuidador);
				
				//CUIDADORPR
				//TblCuidadorPr.deleteAll(TblCuidadorPr.class);
				ATCuidadorPr cuidadorPr = new ATCuidadorPr();
				cuidadorPr.new BuscarAllCuidadoresPr().execute(idCuidador);
				
				//CUIDADORS
				//TblCuidadorS.deleteAll(TblCuidadorS.class);
				ATCuidadorS cuidadorS = new ATCuidadorS();
				cuidadorS.new BuscarAllCuidadoresS().execute(idCuidador);
				
				//ESTADO DE SALUD
				//TblEstadoSalud.deleteAll(TblEstadoSalud.class);
				ATEstadoSalud estadoSalud= new ATEstadoSalud();
				estadoSalud.new BuscarAllEstadosSaludCuidadores().execute(idCuidador);
				
				//EVENTOS CUIDADORES
				//TblEventosCuidadores.deleteAll(TblEstadoSalud.class);
				ATEventosCuidador eventosCuidaores = new ATEventosCuidador();
				eventosCuidaores.new BuscarAllEventosCuidadores().execute(idCuidador);
				
				//EVENTOS PACIENTES
				//TblEventosPacientes.deleteAll(TblEventosPacientes.class);
				ATEventosPaciente eventosPacientes = new ATEventosPaciente();
				eventosPacientes.new BuscarAllEventosCuidadores().execute(idCuidador);
				
				//FAMILIARES PACIENTES
				//TblFamiliaresPacientes.deleteAll(TblFamiliaresPacientes.class);
				ATFamiliaresPacientes familiares = new ATFamiliaresPacientes();
				familiares.new BuscarAllFamiliaresPacientesCuidadores().execute(idCuidador);
				
				//HORARIOS MEDICINAS
				//TblHorarioMedicina.deleteAll(TblHorarioMedicina.class);
				ATHorarioMedicinas horariosMed = new ATHorarioMedicinas();
				horariosMed.new BuscarAllHorarioMedicinaXCuidadores().execute(idCuidador);
				
				//HORARIOS
				//TblHorarios.deleteAll(TblHorarios.class);
				ATHorarios horarios = new ATHorarios();
				horarios.new BuscarAllHorariosXCuidadores().execute(idCuidador);
				
				//OBSERVACIONES
				//TblObservaciones.deleteAll(TblObservaciones.class);
				ATObservaciones observaciones = new ATObservaciones();
				observaciones.new BuscarAllObservacionesCuidadores().execute(idCuidador);
				
				//PACIENTES
				//TblPacientes.deleteAll(TblPacientes.class);
				ATPacientes pacientes = new ATPacientes();
				pacientes.new BuscarAllPacientes().execute(idCuidador);
				
				//PERMISOS
				//TblPermisos.deleteAll(TblPermisos.class);
				ATPermisos permisos = new ATPermisos();
				permisos.new BuscarAllPermisosXCuidadores().execute(idCuidador);
				
				//RUTINAS CUIDADORES
				//TblRutinasCuidadores.deleteAll(TblRutinasCuidadores.class);
				ATRutinasCuidadores rutinasC = new ATRutinasCuidadores();
				rutinasC.new BuscarAllRutinasCuidadores().execute(idCuidador);
				
				//RUTINAS PACIENTES
				//TblRutinasPacientes.deleteAll(TblRutinasPacientes.class);
				ATRutinasPacientes rutinasP = new ATRutinasPacientes();
				rutinasP.new BuscarAllRutinasCuidadores().execute(idCuidador);
				
				//SEGUIMIENTO MEDICO
				//TblSeguimientoMedico.deleteAll(TblSeguimientoMedico.class);
				ATSeguimientoMedico seguimientoMed = new ATSeguimientoMedico();
				seguimientoMed.new BuscarAllSeguimientoMedicoXCuidadores().execute(idCuidador);
				
				//TIPO ACTIVIDAD
				//TblTipoActividad.deleteAll(TblTipoActividad.class);
				ATTipoActividad tipoActividad = new ATTipoActividad();
				tipoActividad.new BuscarAllTipoActividad().execute(idCuidador);
				
			} catch (Exception ex) {
				// TODO: handle exception
				Log.e("Problemas en un AsyncTask", "Error!", ex);
				realizado=false;
			}
			return realizado;
		}
		
		@Override
		protected void onPostExecute(Boolean resul ) {
			// TODO Auto-generated method stub
		}
		
    }  
	
    public Long ActualizarBaseAndroid (String TipoCuidador, long IdCuidador){//Traer variables
		
		String idCuidador=String.valueOf(IdCuidador);
		
		try {
			if(TipoCuidador.equals("CS")){
				
				ATCuidador cuidador = new ATCuidador();
				cuidador.new BuscarUnCuidador().execute(idCuidador);
				Select elCuidador = Select.from(TblCuidador.class)
						.where(Condition.prop("ID_ACTIVIDAD").eq(idCuidador));
		    	TblCuidador edit_Cuidador=(TblCuidador)elCuidador.first();
		    	
		    	if(edit_Cuidador.getControlTotal().equals("true")){
		    		
		    		ATCuidadorS cuidadorS = new ATCuidadorS();
		    		cuidadorS.new BuscarUnCuidadorS().execute(idCuidador);
		    		Select elCuidadorS = Select.from(TblCuidadorS.class)
							.where(Condition.prop("ID_ACTIVIDAD").eq(idCuidador));
			    	TblCuidadorS edit_CuidadorS=(TblCuidadorS)elCuidadorS.first();
			    	idCuidador= edit_CuidadorS.getDependeDe().toString();
		    	}
		    	
		    	EjecutarAsyncTasks(idCuidador);
			}
			if(TipoCuidador.equals("CP")){
				EjecutarAsyncTasks(idCuidador);
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("Problemas en Actualizar la BD", "Error!", e);
		}
		
		return Long.parseLong(idCuidador); 
	}
	
	
	public BlockingQueue<Runnable> DevolverListaDeTareas(String idCuid){
		
	    final BlockingQueue<Runnable> mDecodeWorkQueue = new LinkedBlockingQueue<Runnable>();
		final String idCuidador=idCuid;
	    
        mDecodeWorkQueue.add(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//ACTIVIDADES
				//TblActividades.deleteAll(TblActividades.class);
				ATActividades actividad = new ATActividades();
				actividad.new BuscarAllActividades().execute(idCuidador);
				
			}
		});
		
		mDecodeWorkQueue.add(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//ACTIVIDADES CUIDADORES
				//TblActividadCuidador.deleteAll(TblActividadCuidador.class);
				ATActividadCuidadores actividadC = new ATActividadCuidadores();
				actividadC.new BuscarAllActividadCuidadores().execute(idCuidador);;
				
			}
		});

		mDecodeWorkQueue.add(new Runnable() {
	
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//ACTIVIDADES PACIENTES
				//TblActividadPaciente.deleteAll(TblActividadPaciente.class);
				ATActividadPaciente actividadP = new ATActividadPaciente();
				actividadP.new BuscarAllActividadCuidadores().execute(idCuidador);
				
			}
		});
		
		mDecodeWorkQueue.add(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//BUZON
				//TblBuzon.deleteAll(TblBuzon.class);
				ATBuzon buzon = new ATBuzon();
				buzon.new BuscarAllBuzonesXCuidadores().execute(idCuidador);
				
			}
		});
		
mDecodeWorkQueue.add(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//CONTROL DIETA
				//TblControlDieta.deleteAll(TblControlDieta.class);
				ATControlDieta controlDieta = new ATControlDieta();
				controlDieta.new BuscarAllControlDietaCuidadores().execute(idCuidador);

			}
		});
		
		mDecodeWorkQueue.add(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//CONTROL MEDICINA
				//TblControlMedicina.deleteAll(TblControlMedicina.class);
				ATControlMedicina controlMedicina = new ATControlMedicina();
				controlMedicina.new BuscarAllControlMedicinaXCuidadores().execute(idCuidador);
				
			}
		});

		mDecodeWorkQueue.add(new Runnable() {
	
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//CUIDADOR
				//TblCuidador.deleteAll(TblCuidador.class);
				ATCuidador cuidador = new ATCuidador();
				cuidador.new  BuscarAllCuidadores().execute(idCuidador);
				
			}
		});
		
		mDecodeWorkQueue.add(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//CUIDADORPR
				//TblCuidadorPr.deleteAll(TblCuidadorPr.class);
				ATCuidadorPr cuidadorPr = new ATCuidadorPr();
				cuidadorPr.new BuscarAllCuidadoresPr().execute(idCuidador);
				
			}
		});
		
		
mDecodeWorkQueue.add(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//CUIDADORS
				//TblCuidadorS.deleteAll(TblCuidadorS.class);
				ATCuidadorS cuidadorS = new ATCuidadorS();
				cuidadorS.new BuscarAllCuidadoresS().execute(idCuidador);

			}
		});
		
		mDecodeWorkQueue.add(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//ESTADO DE SALUD
				//TblEstadoSalud.deleteAll(TblEstadoSalud.class);
				ATEstadoSalud estadoSalud= new ATEstadoSalud();
				estadoSalud.new BuscarAllEstadosSaludCuidadores().execute(idCuidador);
				
			}
		});

		mDecodeWorkQueue.add(new Runnable() {
	
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//EVENTOS CUIDADORES
				//TblEventosCuidadores.deleteAll(TblEstadoSalud.class);
				ATEventosCuidador eventosCuidaores = new ATEventosCuidador();
				eventosCuidaores.new BuscarAllEventosCuidadores().execute(idCuidador);
				
			}
		});
		
		mDecodeWorkQueue.add(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//EVENTOS PACIENTES
				//TblEventosPacientes.deleteAll(TblEventosPacientes.class);
				ATEventosPaciente eventosPacientes = new ATEventosPaciente();
				eventosPacientes.new BuscarAllEventosCuidadores().execute(idCuidador);
				
			}
		});
	    
	    mDecodeWorkQueue.add(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//FAMILIARES PACIENTES
				//TblFamiliaresPacientes.deleteAll(TblFamiliaresPacientes.class);
				ATFamiliaresPacientes familiares = new ATFamiliaresPacientes();
				familiares.new BuscarAllFamiliaresPacientesCuidadores().execute(idCuidador);

			}
		});
		
		mDecodeWorkQueue.add(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//HORARIOS MEDICINAS
				//TblHorarioMedicina.deleteAll(TblHorarioMedicina.class);
				ATHorarioMedicinas horariosMed = new ATHorarioMedicinas();
				horariosMed.new BuscarAllHorarioMedicinaXCuidadores().execute(idCuidador);
				
			}
		});
		
		mDecodeWorkQueue.add(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//HORARIOS
				//TblHorarios.deleteAll(TblHorarios.class);
				ATHorarios horarios = new ATHorarios();
				horarios.new BuscarAllHorariosXCuidadores().execute(idCuidador);
				
			}
		});
		
		mDecodeWorkQueue.add(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//OBSERVACIONES
				//TblObservaciones.deleteAll(TblObservaciones.class);
				ATObservaciones observaciones = new ATObservaciones();
				observaciones.new BuscarAllObservacionesCuidadores().execute(idCuidador);
				
			}
		});
		
		mDecodeWorkQueue.add(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//PACIENTES
				//TblPacientes.deleteAll(TblPacientes.class);
				ATPacientes pacientes = new ATPacientes();
				pacientes.new BuscarAllPacientes().execute(idCuidador);
				
			}
		});
		
		mDecodeWorkQueue.add(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//PERMISOS
				//TblPermisos.deleteAll(TblPermisos.class);
				ATPermisos permisos = new ATPermisos();
				permisos.new BuscarAllPermisosXCuidadores().execute(idCuidador);
				
			}
		});
		
		
		mDecodeWorkQueue.add(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//RUTINAS CUIDADORES
				//TblRutinasCuidadores.deleteAll(TblRutinasCuidadores.class);
				ATRutinasCuidadores rutinasC = new ATRutinasCuidadores();
				rutinasC.new BuscarAllRutinasCuidadores().execute(idCuidador);
				
			}
		});
		
		mDecodeWorkQueue.add(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//RUTINAS PACIENTES
				//TblRutinasPacientes.deleteAll(TblRutinasPacientes.class);
				ATRutinasPacientes rutinasP = new ATRutinasPacientes();
				rutinasP.new BuscarAllRutinasCuidadores().execute(idCuidador);
				
			}
		});
		
		mDecodeWorkQueue.add(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//SEGUIMIENTO MEDICO
				//TblSeguimientoMedico.deleteAll(TblSeguimientoMedico.class);
				ATSeguimientoMedico seguimientoMed = new ATSeguimientoMedico();
				seguimientoMed.new BuscarAllSeguimientoMedicoXCuidadores().execute(idCuidador);
				
			}
		});
		
		mDecodeWorkQueue.add(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//TIPO ACTIVIDAD
				//TblTipoActividad.deleteAll(TblTipoActividad.class);
				ATTipoActividad tipoActividad = new ATTipoActividad();
				tipoActividad.new BuscarAllTipoActividad().execute(idCuidador);
				
			}
		});
		
		
		return mDecodeWorkQueue;
		
	}
	
}

