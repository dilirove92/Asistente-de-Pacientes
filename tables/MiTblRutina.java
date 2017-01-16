package com.Notifications.patientssassistant.tables;


import com.Notifications.patientssassistant.R;
import com.Notifications.patientssassistant.alarmas.*;
import java.util.List;
import android.content.Context;
import android.util.Log;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;


public class MiTblRutina extends SugarRecord<MiTblRutina>{

	private Long IdRutina;
	private Long IdAlarmaClock;
	private String TipoUser;
	
	
	public MiTblRutina() {
		super();
	}
	
	public MiTblRutina(Long idRutina, Long idAlarmaClock, String tipoUser) {
		super();
		this.IdRutina = idRutina;
		this.IdAlarmaClock = idAlarmaClock;
		this.TipoUser = tipoUser;
	}
	
	//METODO PARA CREAR EN EL CELL ALARMA DE RUTINA
	public static void CrearAlarmaRutina(Context context, Long idRutina, int hora, int minutos, Long idUser, String tipoUser, String usuario, String actividad, Boolean domingo,
										 Boolean lunes, Boolean martes, Boolean miercoles, Boolean jueves, Boolean viernes, Boolean sabado, String tono, Boolean alarma)
	{
		RutinaDBHelper dbHelper = new RutinaDBHelper(context);	
		RutinaModel rutinaDetails = new RutinaModel();
				
		rutinaDetails.TimeHour = hora;
		rutinaDetails.TimeMinute = minutos;
		rutinaDetails.IdUser = idUser;
		rutinaDetails.TipoUser = tipoUser;
		rutinaDetails.User = usuario;
		rutinaDetails.Name = actividad;		
		rutinaDetails.setRepeatingDay(RutinaModel.SUNDAY, domingo);	
		rutinaDetails.setRepeatingDay(RutinaModel.MONDAY, lunes);	
		rutinaDetails.setRepeatingDay(RutinaModel.TUESDAY, martes);
		rutinaDetails.setRepeatingDay(RutinaModel.WEDNESDAY, miercoles);	
		rutinaDetails.setRepeatingDay(RutinaModel.THURSDAY, jueves);
		rutinaDetails.setRepeatingDay(RutinaModel.FRIDAY, viernes);
		rutinaDetails.setRepeatingDay(RutinaModel.SATURDAY, sabado);
		rutinaDetails.AlarmTone = tono;
		rutinaDetails.IsEnabled = true;
		
		RutinaManagerHelper.cancelAlarms(context);
		Long IdAlarma = dbHelper.createAlarm(rutinaDetails);
		RutinaManagerHelper.setAlarms(context);
		
		//DESACTIVAMOS LA ALARMA DE RUTINA SI EL USUARIO ASI LO DESEA
		if (alarma.equals(false)) {
			RutinaManagerHelper.cancelAlarms(context);							
			RutinaModel model = dbHelper.getAlarm(IdAlarma);
			model.IsEnabled = false;
			dbHelper.updateAlarm(model);							
			RutinaManagerHelper.setAlarms(context);							
		}
		
		//GUARDAR DATOS EN MI TABLA RUTINA
		MiTblRutina datos = new MiTblRutina(idRutina, IdAlarma, tipoUser);
		datos.save();		
	}
	
	//METODO PARA CREAR EN EL CELL ALARMAS DE RUTINAS (POR ID_PACIENTE)
	public static void CrearAlarmasRutinasPac(Context context, Long idPaciente) {
		try {
			//GENERAR UNA LISTA DE LOS RUTINAS/PACIENTE
			List<TblRutinasPacientes> listRutPac = Select.from(TblRutinasPacientes.class).where(Condition.prop("ID_PACIENTE").eq(idPaciente)).list();
			
			//NOMBRE DEL PACIENTE
			TblPacientes elPaciente= Select.from(TblPacientes.class).where(Condition.prop("ID_PACIENTE").eq(idPaciente)).first();
			String nPaciente=elPaciente.getNombreApellidoP();
						
			for (int i = 0; i < listRutPac.size(); i++) {
				TblRutinasPacientes laRutPac=listRutPac.get(i);
				
				TblActividades laActividad= Select.from(TblActividades.class).where(Condition.prop("ID_ACTIVIDAD").eq(laRutPac.getIdActividad())).first();
				String nActividad=laActividad.getNombreActividad();
				
				CrearAlarmaRutina(context, laRutPac.getIdRutinaP(), laRutPac.getHora(), laRutPac.getMinutos(), idPaciente, "P", nPaciente, nActividad, laRutPac.getDomingo(), laRutPac.getLunes(),
								  laRutPac.getMartes(), laRutPac.getMiercoles(), laRutPac.getJueves(), laRutPac.getViernes(), laRutPac.getSabado(), laRutPac.getTono(), laRutPac.getAlarma());					
			}
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.Error), e.getMessage());
		}
	}

	//METODO PARA CREAR EN EL CELL ALARMAS DE RUTINAS (POR ID_CUIDADOR)
	public static void CrearAlarmasRutinasCui(Context context, Long idCuidador) {
		try {
			//GENERAR UNA LISTA DE LOS RUTINAS/CUIDADOR
			List<TblRutinasCuidadores> listRutCui = Select.from(TblRutinasCuidadores.class).where(Condition.prop("ID_CUIDADOR").eq(idCuidador)).list();

			//NOMBRE DEL CUIDADOR
			TblCuidador elCuidador= Select.from(TblCuidador.class).where(Condition.prop("ID_CUIDADOR").eq(idCuidador)).first();
			String nCuidador=elCuidador.getNombreC();

			for (int i = 0; i < listRutCui.size(); i++) {
				TblRutinasCuidadores laRutCui=listRutCui.get(i);

				TblActividades laActividad= Select.from(TblActividades.class).where(Condition.prop("ID_ACTIVIDAD").eq(laRutCui.getIdActividad())).first();
				String nActividad=laActividad.getNombreActividad();

				CrearAlarmaRutina(context, laRutCui.getIdRutinaC(), laRutCui.getHora(), laRutCui.getMinutos(), idCuidador, "C", nCuidador, nActividad, laRutCui.getDomingo(), laRutCui.getLunes(),
						          laRutCui.getMartes(), laRutCui.getMiercoles(), laRutCui.getJueves(), laRutCui.getViernes(), laRutCui.getSabado(), laRutCui.getTono(), laRutCui.getAlarma());
			}
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.Error), e.getMessage());
		}
	}

	//METODO PARA EDITAR EN EL CELL ALARMA DE RUTINA
	public static void EditarAlarmaRutina(Context context, Long idAlarma, int hora, int minutos, String usuario, String actividad, Boolean domingo, Boolean lunes, 
										  Boolean martes, Boolean miercoles, Boolean jueves, Boolean viernes, Boolean sabado, String tono, Boolean alarma) 
	{
		RutinaDBHelper dbHelper = new RutinaDBHelper(context);	
		RutinaModel rutinaDetails = dbHelper.getAlarm(idAlarma);
				
		rutinaDetails.TimeHour = hora;
		rutinaDetails.TimeMinute = minutos;
		rutinaDetails.User = usuario;	
		rutinaDetails.Name = actividad;		
		rutinaDetails.setRepeatingDay(RutinaModel.SUNDAY, domingo);	
		rutinaDetails.setRepeatingDay(RutinaModel.MONDAY, lunes);	
		rutinaDetails.setRepeatingDay(RutinaModel.TUESDAY, martes);
		rutinaDetails.setRepeatingDay(RutinaModel.WEDNESDAY, miercoles);	
		rutinaDetails.setRepeatingDay(RutinaModel.THURSDAY, jueves);
		rutinaDetails.setRepeatingDay(RutinaModel.FRIDAY, viernes);
		rutinaDetails.setRepeatingDay(RutinaModel.SATURDAY, sabado);
		rutinaDetails.AlarmTone = tono;
		rutinaDetails.IsEnabled = true;
								
		RutinaManagerHelper.cancelAlarms(context);
		dbHelper.updateAlarm(rutinaDetails);
		RutinaManagerHelper.setAlarms(context);
		
		//DESACTIVAMOS LA ALARMA DE RUTINA SI EL USUARIO ASI LO DESEA
		if (alarma.equals(false)) {
			RutinaManagerHelper.cancelAlarms(context);							
			RutinaModel model = dbHelper.getAlarm(idAlarma);
			model.IsEnabled = false;
			dbHelper.updateAlarm(model);							
			RutinaManagerHelper.setAlarms(context);							
		}	
	}
		
	//METODO PARA ELIMINAR DEL CELL ALARMA DE RUTINA (POR ID_RUTINA Y TIPO_USER)
	public static void EliminarAlarmaRutina(Context context, Long idRutina, String tipoUser) {
		try {
			RutinaDBHelper dbHelper = new RutinaDBHelper(context);
     		MiTblRutina miRutina = Select.from(MiTblRutina.class).where(Condition.prop("ID_RUTINA").eq(idRutina),
     																	Condition.prop("TIPO_USER").eq(tipoUser)).first();
    		//ELIMINAR ALARMA DE RUTINA DEL CELL
			RutinaManagerHelper.cancelAlarms(context);    				
			dbHelper.deleteAlarm(miRutina.getIdAlarmaClock());    				
			RutinaManagerHelper.setAlarms(context);
			//ELIMINAR REGISTRO DE MI TABLA RUTINA DEL CELL
			miRutina.delete();
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarRutina), e.getMessage());
		}
	}	
	
	//METODO PARA ELIMINAR DEL CELL TODAS LAS ALARMAS DE RUTINAS
	public static void EliminarAlarmasRutinas(Context context) {		
		try {
			List<MiTblRutina> listaR = Select.from(MiTblRutina.class).list();
			for (int i = 0; i < listaR.size(); i++) {
				MiTblRutina registro = listaR.get(i);
				EliminarAlarmaRutina(context, registro.getIdRutina(), registro.getTipoUser());								
			}		
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarRutina), e.getMessage());
		}		
	}
	
	//METODO PARA ELIMINAR DEL CELL ALARMAS DE RUTINAS (POR ID_PACIENTE)
	public static void EliminarAlarmasRutinasPac(Context context, Long idPaciente) {
		try {
			//GENERAR UNA LISTA DE LOS RUTINAS/PACIENTES A ELIMINAR POR ID_PACIENTE
			List<TblRutinasPacientes> laRutPac = Select.from(TblRutinasPacientes.class).where(Condition.prop("ID_PACIENTE").eq(idPaciente)).list();
			for (int i = 0; i < laRutPac.size(); i++) {
				TblRutinasPacientes registro_laRutPac=laRutPac.get(i);
				EliminarAlarmaRutina(context, registro_laRutPac.getIdRutinaP(), "P");	
			}
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarRutsPac), e.getMessage());
		}
	}
	
	//METODO PARA ELIMINAR DEL CELL ALARMAS DE RUTINAS  (POR ID_CUIDADOR)
	public static void EliminarAlarmasRutinasCui(Context context, Long idCuidador) {
		try {
			//GENERAR UNA LISTA DE LAS RUTINAS/CUIDADOR A ELIMINAR POR ID_CUIDADOR
			List<TblRutinasCuidadores> laRutCui = Select.from(TblRutinasCuidadores.class).where(Condition.prop("ID_CUIDADOR").eq(idCuidador)).list();
			for (int i = 0; i < laRutCui.size(); i++) {
				TblRutinasCuidadores registro_laRutCui=laRutCui.get(i);
				EliminarAlarmaRutina(context, registro_laRutCui.getIdRutinaC(), "C");	
			}
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarRutsCui), e.getMessage());
		}
	}		
	
	public Long getIdRutina() {
		return IdRutina;
	}
	
	public void setIdRutina(Long idRutina) {
		IdRutina = idRutina;
	}
	
	public Long getIdAlarmaClock() {
		return IdAlarmaClock;
	}
	
	public void setIdAlarmaClock(Long idAlarmaClock) {
		IdAlarmaClock = idAlarmaClock;
	}

	public String getTipoUser() {
		return TipoUser;
	}

	public void setTipoUser(String tipoUser) {
		TipoUser = tipoUser;
	}

	public static void EliminarDatos(){
		MiTblRutina.executeQuery("delete from "+MiTblRutina.getTableName(MiTblRutina.class));
		if(MiTblRutina.count(MiTblRutina.class)==0) {
			Log.i("MiTblRutina","------------->Eliminado");
		}else{
			Log.i("MiTblRutina","-------------> NOOOOOO Eliminado :'(");
		}
	}


}