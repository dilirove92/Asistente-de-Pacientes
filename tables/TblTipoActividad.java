package com.Notifications.patientssassistant.tables;


import com.Notifications.patientssassistant.R;
import android.util.Log;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;


public class TblTipoActividad extends SugarRecord<TblTipoActividad>{
	
	private Long IdTipoActividad;
	private String TipoActividad;
	private String DescripcionTipoAct;
	private Boolean Eliminado;
	
	public TblTipoActividad() {super();}
	
	public TblTipoActividad(Long idTipoActividad, String tipoActividad, String descripcionTipoAct, Boolean eliminado) {
		super();
		this.IdTipoActividad = idTipoActividad;
		this.TipoActividad = tipoActividad;
		this.DescripcionTipoAct = descripcionTipoAct;
		this.Eliminado = eliminado;
	}
	
	public void EliminarPorIdTipoActividadRegTblTipoActividad(Long idTipoActividad) {		
		try {
			//ELIMINAR ACTIVIDADES POR ID_TIPO_ACTIVIDAD
			TblActividades laAct=new TblActividades();
			laAct.EliminarPorIdTipoActividadRegTblActividades(idTipoActividad);			
			//FINALMENTE SE ELIMINA EL TIPO DE ACTIVIDAD
			TblTipoActividad elTipoActividad = Select.from(TblTipoActividad.class).where(Condition.prop("ID_TIPO_ACTIVIDAD").eq(idTipoActividad)).first();
			elTipoActividad.delete();			
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarTipAct), e.getMessage());
		}		
	}
	
	public Long getIdTipoActividad() {
		return IdTipoActividad;
	}
	
	public void setIdTipoActividad(Long idTipoActividad) {
		IdTipoActividad = idTipoActividad;
	}
	
	public String getTipoActividad() {
		return TipoActividad;
	}
	
	public void setTipoActividad(String tipoActividad) {
		TipoActividad = tipoActividad;
	}
	
	public String getDescripcionTipoAct() {
		return DescripcionTipoAct;
	}
	
	public void setDescripcionTipoAct(String descripcionTipoAct) {
		DescripcionTipoAct = descripcionTipoAct;
	}
	
	public Boolean getEliminado() {
		return Eliminado;
	}

	public void setEliminado(Boolean eliminado) {
		Eliminado = eliminado;
	}

	public static void EliminarDatos(){
		TblTipoActividad.executeQuery("delete from "+TblTipoActividad.getTableName(TblTipoActividad.class));
		if(TblTipoActividad.count(TblTipoActividad.class)==0) {
			Log.i("TBLTIPOACTIVIDAD","------------->Eliminado");
		}else{
			Log.i("TBLTIPOACTIVIDAD","-------------> NOOOOOO Eliminado :'(");
		}
	}
}