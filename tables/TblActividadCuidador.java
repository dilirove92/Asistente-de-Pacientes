package com.Notifications.patientssassistant.tables;


import com.Notifications.patientssassistant.R;
import java.util.List;
import android.util.Log;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;


public class TblActividadCuidador extends SugarRecord<TblActividadCuidador> {
	
	private Long IdCuidador;
	private Long IdActividad;
	private Boolean Eliminado;
	
	public TblActividadCuidador() {super();}
	
	public TblActividadCuidador(Long idCuidador, Long idActividad, Boolean eliminado) {
		super();
		this.IdCuidador = idCuidador;
		this.IdActividad = idActividad;
		this.Eliminado = eliminado;
	}
	
	public void EliminarPorIdCuidadorRegTblActividadCuidador(Long idCuidador) {
		try {
			List<TblActividadCuidador> laActCui = Select.from(TblActividadCuidador.class).where(Condition.prop("ID_CUIDADOR").eq(idCuidador)).list();						
			for (int i = 0; i < laActCui.size(); i++) {
				TblActividadCuidador registro_laActCui=laActCui.get(i);
				registro_laActCui.delete();	
			}
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarActsCui), e.getMessage());
		}
	}
	
	public void EliminarPorIdActividadRegTblActividadCuidador(Long idActividad) {
		try {
			List<TblActividadCuidador> laActCui = Select.from(TblActividadCuidador.class).where(Condition.prop("ID_ACTIVIDAD").eq(idActividad)).list();						
			for (int i = 0; i < laActCui.size(); i++) {
				TblActividadCuidador registro_laActCui=laActCui.get(i);
				registro_laActCui.delete();	
			}
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarActsCui), e.getMessage());
		}
	}	
	
	public Long getIdCuidador() {
		return IdCuidador;
	}
	
	public void setIdCuidador(Long idCuidador) {
		IdCuidador = idCuidador;
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
		TblActividadCuidador.executeQuery("delete from "+TblActividadCuidador.getTableName(TblActividadCuidador.class));
		if(TblActividadCuidador.count(TblActividadCuidador.class)==0) {
			Log.i("TBLACTIVIDADCUIDADOR","------------->Eliminado");
		}else{
			Log.i("TBLACTIVIDADCUIDADOR","-------------> NOOOOOO Eliminado :'(");
		}
	}
	
}