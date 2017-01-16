package com.Notifications.patientssassistant.tables;


import com.Notifications.patientssassistant.R;
import com.Notifications.patientssassistant.alarmas.*;
import java.util.List;
import android.content.Context;
import android.util.Log;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;


public class MiTblEvento extends SugarRecord<MiTblEvento> {
	
	private Long IdEvento;
	private Long IdAlarmaClock;
	private String TipoUser;
	
	
	public MiTblEvento() { super(); }
	
	public MiTblEvento(Long idEvento, Long idAlarmaClock, String tipoUser) {
		super();
		this.IdEvento = idEvento;
		this.IdAlarmaClock = idAlarmaClock;
		this.TipoUser = tipoUser;
	}
	
	//METODO PARA CREAR EN EL CELL ALARMA DE EVENTO
	public static void CrearAlarmaEvento(Context context, Long idEvento, int anioR, int mesR, int diaR, int horaR, int minutosR, Long idUser, String tipoUser,
										 String usuario, String actividad, String fechaHoraE, String lugar, String descripcion, String tono, Boolean alarma)
	{
		EventoDBHelper dbHelper = new EventoDBHelper(context);
		EventoModel eventoDetails = new EventoModel();
		
		eventoDetails.Year = anioR;
		eventoDetails.Month = mesR;
		eventoDetails.Day = diaR;
		eventoDetails.Hour = horaR;
		eventoDetails.Minutes = minutosR;
		eventoDetails.IdUser = idUser;
		eventoDetails.TipoUser = tipoUser;
		eventoDetails.User = usuario;		
		eventoDetails.Name = actividad;
		eventoDetails.Date = fechaHoraE;
		eventoDetails.Location = lugar;
		eventoDetails.Description = descripcion;
		eventoDetails.AlarmTone = tono;
		eventoDetails.IsEnabled = true;
								
		EventoManagerHelper.cancelAlarms(context);
		Long IdAlarma = dbHelper.createAlarm(eventoDetails);
		EventoManagerHelper.setAlarms(context);
		
		//DESACTIVAMOS LA ALARMA DE EVENTO SI EL USUARIO ASI LO DESEA
		if (alarma.equals(false)) {
			EventoManagerHelper.cancelAlarms(context);							
			EventoModel model = dbHelper.getAlarm(IdAlarma);
			model.IsEnabled = false;
			dbHelper.updateAlarm(model);							
			EventoManagerHelper.setAlarms(context);							
		}
		
		//GUARDAR DATOS EN MI TABLA EVENTO
		MiTblEvento datos = new MiTblEvento(idEvento, IdAlarma, tipoUser);
		datos.save();	
	}
	
	//METODO PARA CREAR EN EL CELL ALARMAS DE EVENTOS (POR ID_PACIENTE)
	public static void CrearAlarmasEventosPac(Context context, Long idPaciente) {
		try {
			//GENERAR UNA LISTA DE LOS EVENTOS/PACIENTE 
			List<TblEventosPacientes> listEvePac = Select.from(TblEventosPacientes.class).where(Condition.prop("ID_PACIENTE").eq(idPaciente)).list();

			//NOMBRE DEL PACIENTE
			TblPacientes elPaciente= Select.from(TblPacientes.class).where(Condition.prop("ID_PACIENTE").eq(idPaciente)).first();
			String nPaciente=elPaciente.getNombreApellidoP();
			
			for (int i = 0; i < listEvePac.size(); i++) {
				TblEventosPacientes elEvePac=listEvePac.get(i);

				TblActividades laActividad= Select.from(TblActividades.class).where(Condition.prop("ID_ACTIVIDAD").eq(elEvePac.getIdActividad())).first();
				String nActividad=laActividad.getNombreActividad();				
				
				String fe = (String.format("%02d/%02d/%02d", elEvePac.getDiaE(), elEvePac.getMesE()+1, elEvePac.getAnioE()));
				String he = (String.format("%02d:%02d", elEvePac.getHoraE(), elEvePac.getMinutosE()));
				String fechaHoraE = fe+" "+he;
				
				CrearAlarmaEvento(context, elEvePac.getIdEventoP(), elEvePac.getAnioR(), elEvePac.getMesR(), elEvePac.getDiaR(), elEvePac.getHoraR(), elEvePac.getMinutosR(),
								  idPaciente, "P", nPaciente, nActividad, fechaHoraE, elEvePac.getLugar(), elEvePac.getDescripcion(), elEvePac.getTono(), elEvePac.getAlarma());
			}
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.Error), e.getMessage());
		}
	}

	//METODO PARA CREAR EN EL CELL ALARMAS DE EVENTOS (POR ID_CUIDADOR)
	public static void CrearAlarmasEventosCui(Context context, Long idCuidador) {
		try {
			//GENERAR UNA LISTA DE LOS EVENTOS/CUIDADOR
			List<TblEventosCuidadores> listEveCui = Select.from(TblEventosCuidadores.class).where(Condition.prop("ID_CUIDADOR").eq(idCuidador)).list();

			//NOMBRE DEL CUIDADOR
			TblCuidador elCuidador = Select.from(TblCuidador.class).where(Condition.prop("ID_CUIDADOR").eq(idCuidador)).first();
			String nCuidador=elCuidador.getNombreC();

			for (int i = 0; i < listEveCui.size(); i++) {
				TblEventosCuidadores elEveCui=listEveCui.get(i);

				TblActividades laActividad= Select.from(TblActividades.class).where(Condition.prop("ID_ACTIVIDAD").eq(elEveCui.getIdActividad())).first();
				String nActividad=laActividad.getNombreActividad();

				String fe = (String.format("%02d/%02d/%02d", elEveCui.getDiaE(), elEveCui.getMesE()+1, elEveCui.getAnioE()));
				String he = (String.format("%02d:%02d", elEveCui.getHoraE(), elEveCui.getMinutosE()));
				String fechaHoraE = fe+" "+he;

				CrearAlarmaEvento(context, elEveCui.getIdEventoC(), elEveCui.getAnioR(), elEveCui.getMesR(), elEveCui.getDiaR(), elEveCui.getHoraR(), elEveCui.getMinutosR(),
								  idCuidador, "C", nCuidador, nActividad, fechaHoraE, elEveCui.getLugar(), elEveCui.getDescripcion(), elEveCui.getTono(), elEveCui.getAlarma());
			}
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.Error), e.getMessage());
		}
	}

	//METODO PARA EDITAR EN EL CELL ALARMA DE EVENTO
	public static void EditarAlarmaEvento(Context context, Long idAlarma, int anioR, int mesR, int diaR, int horaR, int minutosR, String usuario, String actividad,
									      String fechaHoraE, String lugar, String descripcion, String tono, Boolean alarma) 
	{
		EventoDBHelper dbHelper = new EventoDBHelper(context);
		EventoModel eventoDetails = dbHelper.getAlarm(idAlarma);
				
		eventoDetails.Year = anioR;
		eventoDetails.Month = mesR;
		eventoDetails.Day = diaR;
		eventoDetails.Hour = horaR;
		eventoDetails.Minutes = minutosR;
		eventoDetails.User = usuario;		
		eventoDetails.Name = actividad;
		eventoDetails.Date = fechaHoraE;
		eventoDetails.Location = lugar;
		eventoDetails.Description = descripcion;
		eventoDetails.AlarmTone = tono;
		eventoDetails.IsEnabled = true;
						
		EventoManagerHelper.cancelAlarms(context);
		dbHelper.updateAlarm(eventoDetails);
		EventoManagerHelper.setAlarms(context);
		
		//DESACTIVAMOS LA ALARMA DE EVENTO SI EL USUARIO ASI LO DESEA
		if (alarma.equals(false)) {
			EventoManagerHelper.cancelAlarms(context);							
			EventoModel model = dbHelper.getAlarm(idAlarma);
			model.IsEnabled = false;
			dbHelper.updateAlarm(model);							
			EventoManagerHelper.setAlarms(context);							
		}	
	}	
	
	//METODO PARA ELIMINAR DEL CELL ALARMA DE EVENTO  (POR ID_EVENTO Y TIPO_USER)
	public static void EliminarAlarmaEvento(Context context, Long idEvento, String tipoUser) {
		try {
			EventoDBHelper dbHelper = new EventoDBHelper(context);
			MiTblEvento miEvento = Select.from(MiTblEvento.class).where(Condition.prop("ID_EVENTO").eq(idEvento),
																		Condition.prop("TIPO_USER").eq(tipoUser)).first();
			//ELIMINAR DEL CELL ALARMA DE EVENTO
			EventoManagerHelper.cancelAlarms(context);    				
			dbHelper.deleteAlarm(miEvento.getIdAlarmaClock());    				
			EventoManagerHelper.setAlarms(context);
			//ELIMINAR DEL CELL REGISTRO DE MI TABLA EVENTO
			miEvento.delete();
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarEvento), e.getMessage());
		}
	}
	
	//METODO PARA ELIMINAR DEL CELL TODAS LAS ALARMAS DE EVENTOS
	public static void EliminarAlarmasEventos(Context context) {		
		try {
			List<MiTblEvento> listaE = Select.from(MiTblEvento.class).list();
			for (int i = 0; i < listaE.size(); i++) {
				MiTblEvento registro = listaE.get(i);
				EliminarAlarmaEvento(context, registro.getIdEvento(), registro.getTipoUser());								
			}
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarEvento), e.getMessage());
		}		
	}
			
	//METODO PARA ELIMINAR DEL CELL ALARMAS DE EVENTOS (POR ID_PACIENTE)
	public static void EliminarAlarmasEventosPac(Context context, Long idPaciente) {
		try {
			//GENERAR UNA LISTA DE LOS EVENTOS/PACIENTES A ELIMINAR POR ID_PACIENTE
			List<TblEventosPacientes> elEvePac = Select.from(TblEventosPacientes.class).where(Condition.prop("ID_PACIENTE").eq(idPaciente)).list();
			for (int i = 0; i < elEvePac.size(); i++) {
				TblEventosPacientes registro_elEvePac=elEvePac.get(i);
				EliminarAlarmaEvento(context, registro_elEvePac.getIdEventoP(), "P");	
			}
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarEvesPac), e.getMessage());
		}
	}
	
	//METODO PARA ELIMINAR DEL CELL ALARMAS DE EVENTOS  (POR ID_CUIDADOR)
	public static void EliminarAlarmasEventosCui(Context context, Long idCuidador) {
		try {
			//GENERAR UNA LISTA DE LOS EVENTOS/CUIDADOR A ELIMINAR POR ID_CUIDADOR
			List<TblEventosCuidadores> elEveCui = Select.from(TblEventosCuidadores.class).where(Condition.prop("ID_CUIDADOR").eq(idCuidador)).list();
			for (int i = 0; i < elEveCui.size(); i++) {
				TblEventosCuidadores registro_elEveCui=elEveCui.get(i);	
				EliminarAlarmaEvento(context, registro_elEveCui.getIdEventoC(), "C");	
			}
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarEvesCui), e.getMessage());
		}
	}	
	
	public Long getIdEvento() {
		return IdEvento;
	}
	
	public void setIdEvento(Long idEvento) {
		IdEvento = idEvento;
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
		MiTblEvento.executeQuery("delete from "+MiTblEvento.getTableName(MiTblEvento.class));
		if(MiTblEvento.count(MiTblEvento.class)==0) {
			Log.i("MiTblEvento","------------->Eliminado");
		}else{
			Log.i("MiTblEvento","-------------> NOOOOOO Eliminado :'(");
		}
	}


}