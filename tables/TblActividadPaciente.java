package com.Notifications.patientssassistant.tables;


import com.Notifications.patientssassistant.R;
import java.util.List;
import android.util.Log;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;


public class TblActividadPaciente extends SugarRecord<TblActividadPaciente> {
	
	private Long IdPaciente;
	private Long IdActividad;
	private Boolean Eliminado;
	
	public TblActividadPaciente() {super();}
	
	public TblActividadPaciente(Long idPaciente, Long idActividad, Boolean eliminado) {
		super();
		this.IdPaciente = idPaciente;
		this.IdActividad = idActividad;
		this.Eliminado = eliminado;
	}
	
	public void EliminarPorIdPacienteRegTblActividadPaciente(Long idPaciente) {
		try {
			List<TblActividadPaciente> laActPac = Select.from(TblActividadPaciente.class).where(Condition.prop("ID_PACIENTE").eq(idPaciente)).list();						
			for (int i = 0; i < laActPac.size(); i++) {
				TblActividadPaciente registro_laActPac=laActPac.get(i);
				registro_laActPac.delete();	
			}
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarActsPac), e.getMessage());
		}
	}	
	
	public void EliminarPorIdActividadRegTblActividadPaciente(Long idActividad) {
		try {
			List<TblActividadPaciente> laActPac = Select.from(TblActividadPaciente.class).where(Condition.prop("ID_ACTIVIDAD").eq(idActividad)).list();						
			for (int i = 0; i < laActPac.size(); i++) {
				TblActividadPaciente registro_laActPac=laActPac.get(i);
				registro_laActPac.delete();	
			}
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarActsPac), e.getMessage());
		}
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
	
	public Boolean getEliminado() {
		return Eliminado;
	}

	public void setEliminado(Boolean eliminado) {
		Eliminado = eliminado;
	}

	public static void EliminarDatos(){
		TblActividadPaciente.executeQuery("delete from "+TblActividadPaciente.getTableName(TblActividadPaciente.class));
		if(TblActividadPaciente.count(TblActividadPaciente.class)==0) {
			Log.i("TBLACTIVIDADPACIENTE","------------->Eliminado");
		}else{
			Log.i("TBLACTIVIDADPACIENTE","-------------> NOOOOOO Eliminado :'(");
		}
	}
}