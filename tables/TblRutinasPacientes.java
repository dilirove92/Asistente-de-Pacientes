package com.Notifications.patientssassistant.tables;


import com.Notifications.patientssassistant.R;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;


public class TblRutinasPacientes extends SugarRecord<TblRutinasPacientes> {
	
	private Long IdRutinaP;
	private Long IdPaciente;
	private Long IdActividad;
	private int Hora;		
	private int Minutos;
	private Boolean Domingo;
	private Boolean Lunes;
	private Boolean Martes;
	private Boolean Miercoles;
	private Boolean Jueves;
	private Boolean Viernes;
	private Boolean Sabado;
	private String Tono;
	private String Descripcion;
	private Boolean Alarma;
	private Boolean Eliminado;
	public final List<TblRutinasPacientes> Children = new ArrayList<TblRutinasPacientes>();
	
	public TblRutinasPacientes() {super();}
	
	public TblRutinasPacientes(Long idRutinaP, Long idPaciente, Long idActividad, int hora, int minutos,  
							   Boolean domingo, Boolean lunes, Boolean martes, Boolean miercoles, Boolean jueves,
							   Boolean viernes, Boolean sabado, String tono, String descripcion, Boolean alarma, Boolean eliminado) {
		super();
		this.IdRutinaP = idRutinaP;
		this.IdPaciente = idPaciente;
		this.IdActividad = idActividad;		
		this.Hora = hora;
		this.Minutos = minutos;
		this.Domingo = domingo;
		this.Lunes = lunes;
		this.Martes = martes;
		this.Miercoles = miercoles;
		this.Jueves = jueves;
		this.Viernes = viernes;
		this.Sabado = sabado;
		this.Tono = tono;
		this.Descripcion = descripcion;
		this.Alarma = alarma;
		this.Eliminado = eliminado;
	}

	public TblRutinasPacientes(Long idRutinaP, Long idActividad, int hora, int minutos, Boolean domingo, Boolean lunes, 
							   Boolean martes, Boolean miercoles, Boolean jueves, Boolean viernes, Boolean sabado, Boolean alarma) {
		super();
		this.IdRutinaP = idRutinaP;
		this.IdActividad = idActividad;
		this.Hora = hora;
		this.Minutos = minutos;
		this.Domingo = domingo;
		this.Lunes = lunes;
		this.Martes = martes;
		this.Miercoles = miercoles;
		this.Jueves = jueves;
		this.Viernes = viernes;
		this.Sabado = sabado;
		this.Alarma = alarma;
	}

	public TblRutinasPacientes(String descripcion, String tono) {
		super();
		this.Descripcion = descripcion;
		this.Tono = tono;
	}
	
	public void EliminarPorIdRutinaRegTblRutinasPacientes(Long idRutinaP) {		
		try {
			//ELIMINAR LA RUTINA DEL PACIENTE
			TblRutinasPacientes laRutPac = Select.from(TblRutinasPacientes.class).where(Condition.prop("ID_RUTINA_P").eq(idRutinaP)).first();
			laRutPac.delete();			
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarRutPac), e.getMessage());
		}		
	}
	
	public void EliminarPorIdPacienteRegTblRutinasPacientes(Long idPaciente) {
		try {
			//GENERAR UNA LISTA DE LAS RUTINAS/PACIENTES A ELIMINAR POR ID_PACIENTE
			List<TblRutinasPacientes> laRutPac = Select.from(TblRutinasPacientes.class).where(Condition.prop("ID_PACIENTE").eq(idPaciente)).list();
			TblRutinasPacientes eliminar_laRutPac = new TblRutinasPacientes();
			for (int i = 0; i < laRutPac.size(); i++) {
				TblRutinasPacientes registro_laRutPac=laRutPac.get(i);
				//LLAMAR AL METODO "EliminarPorIdRutinaRegTblRutinasPacientes" EL CUAL ELIMINA LA RUTINA/CUIDADOR POR ID_RUTINA_P
				eliminar_laRutPac.EliminarPorIdRutinaRegTblRutinasPacientes(registro_laRutPac.getIdRutinaP());					
			}
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarRutsPac), e.getMessage());
		}
	}	
	
	public void EliminarPorIdActividadRegTblRutinasPacientes(Long idActividad) {
		try {
			//GENERAR UNA LISTA DE LAS RUTINAS/PACIENTES A ELIMINAR POR ID_ACTIVIDAD
			List<TblRutinasPacientes> laRutPac = Select.from(TblRutinasPacientes.class).where(Condition.prop("ID_ACTIVIDAD").eq(idActividad)).list();
			TblRutinasPacientes eliminar_laRutPac = new TblRutinasPacientes();
			for (int i = 0; i < laRutPac.size(); i++) {
				TblRutinasPacientes registro_laRutPac=laRutPac.get(i);
				//LLAMAR AL METODO "EliminarPorIdRutinaRegTblRutinasPacientes" EL CUAL ELIMINA LA RUTINA/CUIDADOR POR ID_RUTINA_P
				eliminar_laRutPac.EliminarPorIdRutinaRegTblRutinasPacientes(registro_laRutPac.getIdRutinaP());					
			}
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarRutsPac), e.getMessage());
		}
	}	
	
	public Long getIdRutinaP() {
		return IdRutinaP;
	}
	
	public void setIdRutinaP(Long idRutinaP) {
		IdRutinaP = idRutinaP;
	}
	
	public Long getIdPaciente() {
		return IdPaciente;
	}
	
	public void setIdPaciente(Long idPaciente) {
		IdPaciente = idPaciente;
	}
	
	public Long getIdActividad() {
		return IdActividad;
	}
	
	public void setIdActividad(Long idActividad) {
		IdActividad = idActividad;
	}
	
	public int getHora() {
		return Hora;
	}

	public void setHora(int hora) {
		Hora = hora;
	}

	public int getMinutos() {
		return Minutos;
	}

	public void setMinutos(int minutos) {
		Minutos = minutos;
	}

	public Boolean getDomingo() {
		return Domingo;
	}
	
	public void setDomingo(Boolean domingo) {
		Domingo = domingo;
	}
	
	public Boolean getLunes() {
		return Lunes;
	}
	
	public void setLunes(Boolean lunes) {
		Lunes = lunes;
	}
	
	public Boolean getMartes() {
		return Martes;
	}
	
	public void setMartes(Boolean martes) {
		Martes = martes;
	}
	
	public Boolean getMiercoles() {
		return Miercoles;
	}
	
	public void setMiercoles(Boolean miercoles) {
		Miercoles = miercoles;
	}
	
	public Boolean getJueves() {
		return Jueves;
	}
	
	public void setJueves(Boolean jueves) {
		Jueves = jueves;
	}
	
	public Boolean getViernes() {
		return Viernes;
	}
	
	public void setViernes(Boolean viernes) {
		Viernes = viernes;
	}
	
	public Boolean getSabado() {
		return Sabado;
	}
	
	public void setSabado(Boolean sabado) {
		Sabado = sabado;
	}
	
	public String getTono() {
		return Tono;
	}

	public void setTono(String tono) {
		Tono = tono;
	}

	public String getDescripcion() {
		return Descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		Descripcion = descripcion;
	}
	
	public Boolean getAlarma() {
		return Alarma;
	}
	
	public void setAlarma(Boolean alarma) {
		Alarma = alarma;
	}
	
	public Boolean getEliminado() {
		return Eliminado;
	}

	public void setEliminado(Boolean eliminado) {
		Eliminado = eliminado;
	}

	public static void EliminarDatos(){
		TblRutinasPacientes.executeQuery("delete from "+TblRutinasPacientes.getTableName(TblRutinasPacientes.class));
		if(TblRutinasPacientes.count(TblRutinasPacientes.class)==0) {
			Log.i("TBLRUTINASPACIENTES","------------->Eliminado");
		}else{
			Log.i("TBLRUTINASPACIENTES","-------------> NOOOOOO Eliminado :'(");
		}
	}
}