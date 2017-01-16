package com.Notifications.patientssassistant.tables;


import com.Notifications.patientssassistant.R;
import com.Notifications.patientssassistant.alarmas.*;
import java.util.List;
import android.content.Context;
import android.util.Log;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;


public class MiTblMedicina extends SugarRecord<MiTblMedicina>{

	private Long IdHorarioMedicina;
	private Long IdAlarmaClock;
	
	
	public MiTblMedicina() {
		super();
	}
	
	public MiTblMedicina(Long idHorarioMedicina, Long idAlarmaClock) {
		super();
		this.IdHorarioMedicina = idHorarioMedicina;
		this.IdAlarmaClock = idAlarmaClock;
	}
	
	//METODO PARA CREAR EN EL CELL ALARMA DE HM
	public static void CrearAlarmaHM(Context context, Long idHM, Long idUser, String usuario, String medicamento, String motivo, String dosis, int nVeces, int hora, int minutos, Boolean domingo,
									 Boolean lunes, Boolean martes, Boolean miercoles, Boolean jueves, Boolean viernes, Boolean sabado, String tono, Boolean alarma) 
	{
		MedicinaDBHelper dbHelper = new MedicinaDBHelper(context);
		MedicinaModel medicinaDetails = new MedicinaModel();

		medicinaDetails.IdUser = idUser;
		medicinaDetails.User = usuario;	
		medicinaDetails.Medicine = medicamento;
		medicinaDetails.Reason = motivo;
		medicinaDetails.Doses = dosis;
		medicinaDetails.Number = nVeces;
		medicinaDetails.TimeHour = hora;
		medicinaDetails.TimeMinute = minutos;
		medicinaDetails.setRepeatingDay(RutinaModel.SUNDAY, domingo);	
		medicinaDetails.setRepeatingDay(RutinaModel.MONDAY, lunes);	
		medicinaDetails.setRepeatingDay(RutinaModel.TUESDAY, martes);
		medicinaDetails.setRepeatingDay(RutinaModel.WEDNESDAY, miercoles);	
		medicinaDetails.setRepeatingDay(RutinaModel.THURSDAY, jueves);
		medicinaDetails.setRepeatingDay(RutinaModel.FRIDAY, viernes);
		medicinaDetails.setRepeatingDay(RutinaModel.SATURDAY, sabado);
		medicinaDetails.AlarmTone = tono;
		medicinaDetails.IsEnabled = true;		
		
		MedicinaManagerHelper.cancelAlarms(context);
		Long IdAlarma = dbHelper.createAlarm(medicinaDetails);
		MedicinaManagerHelper.setAlarms(context);
		
		//DESACTIVAMOS LA ALARMA DE HM SI EL USUARIO ASI LO DESEA
		if (alarma.equals(false)) {
			MedicinaManagerHelper.cancelAlarms(context);							
			MedicinaModel model = dbHelper.getAlarm(IdAlarma);
			model.IsEnabled = false;
			dbHelper.updateAlarm(model);							
			MedicinaManagerHelper.setAlarms(context);							
		}
		
		//GUARDAR DATOS EN MI TABLA MEDICINA
		MiTblMedicina datos = new MiTblMedicina(idHM, IdAlarma);
		datos.save();		
	}
	
	//METODO PARA CREAR EN EL CELL LAS ALARMAS DE LAS MEDICINAS  (POR ID_PACIENTE)
	public static void CrearAlarmasMedicinasPac(Context context, Long idPaciente) {
		try {
			//GENERAR UNA LISTA DE LAS MEDICINAS DEL PACIENTE
			List<TblControlMedicina> listMed = Select.from(TblControlMedicina.class).where(Condition.prop("ID_PACIENTE").eq(idPaciente)).list();
			
			//BUSCAR EL NOMBRE DEL PACIENTE
			TblPacientes elPaciente= Select.from(TblPacientes.class).where(Condition.prop("ID_PACIENTE").eq(idPaciente)).first();
			String nPaciente=elPaciente.getNombreApellidoP();
			
			for (int i = 0; i < listMed.size(); i++) {
				TblControlMedicina laMed=listMed.get(i);
				
				//BUSCAMOS LOS HORARIOS DE LA MEDICINA
				List<TblHorarioMedicina> listHM = Select.from(TblHorarioMedicina.class).where(Condition.prop("ID_CONTROL_MEDICINA").eq(laMed.getIdControlMedicina())).list();
				
				for (int j = 0; j < listHM.size(); j++) {
					TblHorarioMedicina elHM = listHM.get(i);
					CrearAlarmaHM(context, elHM.getIdHorarioMedicina(), idPaciente, nPaciente, laMed.getMedicamento(), laMed.getMotivoMedicacion(), laMed.getDosis(), laMed.getNdeVeces(), elHM.getHora(), elHM.getMinutos(),
								  elHM.getDomingo(), elHM.getLunes(), elHM.getMartes(), elHM.getMiercoles(), elHM.getJueves(), elHM.getViernes(), elHM.getSabado(), laMed.getTono(), elHM.getActDesAlarma());
				}					
			}
		}catch (Exception e) {
			Log.e(String.valueOf(R.string.Error), e.getMessage());
		}
	}	
	
	//METODO PARA EDITAR EN EL CELL ALARMA DE HM
	public static void EditarAlarmaHM(Context context, Long idAlarma, String usuario, String medicamento, String motivo, String dosis, int nVeces, int hora, int minutos, Boolean domingo, 
			 						  Boolean lunes, Boolean martes, Boolean miercoles, Boolean jueves, Boolean viernes, Boolean sabado, String tono, Boolean alarma)
	{
		MedicinaDBHelper dbHelper = new MedicinaDBHelper(context);
		MedicinaModel medicinaDetails = dbHelper.getAlarm(idAlarma);
		
		medicinaDetails.User = usuario;	
		medicinaDetails.Medicine = medicamento;
		medicinaDetails.Reason = motivo;
		medicinaDetails.Doses = dosis;
		medicinaDetails.Number = nVeces;
		medicinaDetails.TimeHour = hora;
		medicinaDetails.TimeMinute = minutos;
		medicinaDetails.setRepeatingDay(RutinaModel.SUNDAY, domingo);	
		medicinaDetails.setRepeatingDay(RutinaModel.MONDAY, lunes);	
		medicinaDetails.setRepeatingDay(RutinaModel.TUESDAY, martes);
		medicinaDetails.setRepeatingDay(RutinaModel.WEDNESDAY, miercoles);	
		medicinaDetails.setRepeatingDay(RutinaModel.THURSDAY, jueves);
		medicinaDetails.setRepeatingDay(RutinaModel.FRIDAY, viernes);
		medicinaDetails.setRepeatingDay(RutinaModel.SATURDAY, sabado);
		medicinaDetails.AlarmTone = tono;
		medicinaDetails.IsEnabled = true;	
								
		MedicinaManagerHelper.cancelAlarms(context);
		dbHelper.updateAlarm(medicinaDetails);
		MedicinaManagerHelper.setAlarms(context);
		
		//DESACTIVAMOS LA ALARMA DE HM SI EL USUARIO ASI LO DESEA
		if (alarma.equals(false)) {
			MedicinaManagerHelper.cancelAlarms(context);							
			MedicinaModel model = dbHelper.getAlarm(idAlarma);
			model.IsEnabled = false;
			dbHelper.updateAlarm(model);							
			MedicinaManagerHelper.setAlarms(context);							
		}
	}
	
	//METODO PARA ELIMINAR DEL CELL ALARMA DE UN HM (POR ID_HORARIO_MEDICINA)
	public static void EliminarAlarmaHM(Context context, Long idHM) {
		try {
			MedicinaDBHelper dbHelper = new MedicinaDBHelper(context);
     		MiTblMedicina miMedicina = Select.from(MiTblMedicina.class).where(Condition.prop("ID_HORARIO_MEDICINA").eq(idHM)).first();
    		//ELIMINAR DEL CELL ALARMA DE HM 
			MedicinaManagerHelper.cancelAlarms(context);    				
			dbHelper.deleteAlarm(miMedicina.getIdAlarmaClock());    				
			MedicinaManagerHelper.setAlarms(context);
			//ELIMINAR DEL CELL REGISTRO DE MI TABLA MEDICINA 
			miMedicina.delete();
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarHM), e.getMessage());
		}
	}
	
	//METODO PARA ELIMINAR DEL CELL TODAS LAS ALARMAS DE HMS
	public static void EliminarAlarmasHMS(Context context) {		
		try {
			List<MiTblMedicina> listaM = Select.from(MiTblMedicina.class).list();
			for (int i = 0; i < listaM.size(); i++) {
				MiTblMedicina registro = listaM.get(i);
				EliminarAlarmaHM(context, registro.getIdHorarioMedicina());								
			}		
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarHM), e.getMessage());
		}		
	}	
	
	//METODO PARA ELIMINAR DEL CELL ALARMAS DE UNA MEDICINA  (POR ID_CONTROL_MEDICINA)
	public static void EliminarAlarmasMedicina(Context context, Long idCM) {
		try {
			//GENERAR UNA LISTA DE LOS HORARIOS DE MEDICINA A ELIMINAR POR ID_CONTROL_MEDICINA
			List<TblHorarioMedicina> elHorMed = Select.from(TblHorarioMedicina.class).where(Condition.prop("ID_CONTROL_MEDICINA").eq(idCM)).list();
			for (int i = 0; i < elHorMed.size(); i++) {
				TblHorarioMedicina registro_elHorMed=elHorMed.get(i);			
				EliminarAlarmaHM(context, registro_elHorMed.getIdHorarioMedicina());	
			}
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarConMed), e.getMessage());
		}
	}	
		
	//METODO PARA ELIMINAR DEL CELL TODAS LAS ALARMAS DE LAS MEDICINAS  (POR ID_PACIENTE)
	public static void EliminarAlarmasMedicinasPac(Context context, Long idPaciente) {
		try {
			//GENERAR UNA LISTA DE LAS MEDICINAS A ELIMINAR POR ID_PACIENTE
			List<TblControlMedicina> laMed = Select.from(TblControlMedicina.class).where(Condition.prop("ID_PACIENTE").eq(idPaciente)).list();
			for (int i = 0; i < laMed.size(); i++) {
				TblControlMedicina registro_laMed=laMed.get(i);
				EliminarAlarmasMedicina(context, registro_laMed.getIdControlMedicina());	
			}
		}catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarConsMed), e.getMessage());
		}
	}	
	
	public Long getIdHorarioMedicina() {
		return IdHorarioMedicina;
	}

	public void setIdHorarioMedicina(Long idHorarioMedicina) {
		IdHorarioMedicina = idHorarioMedicina;
	}

	public Long getIdAlarmaClock() {
		return IdAlarmaClock;
	}
	
	public void setIdAlarmaClock(Long idAlarmaClock) {
		IdAlarmaClock = idAlarmaClock;
	}

	public static void EliminarDatos(){
		MiTblMedicina.executeQuery("delete from "+MiTblMedicina.getTableName(MiTblMedicina.class));
		if(MiTblMedicina.count(MiTblMedicina.class)==0) {
			Log.i("MiTblMedicina","------------->Eliminado");
		}else{
			Log.i("MiTblMedicina","-------------> NOOOOOO Eliminado :'(");
		}
	}


}