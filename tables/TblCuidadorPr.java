package com.Notifications.patientssassistant.tables;


import com.Notifications.patientssassistant.R;
import android.util.Log;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;


public class TblCuidadorPr extends SugarRecord<TblCuidadorPr> {
	
	private Long IdCuidador;
	private String Contrasena;	
	private String TipoResidencia;
	private String PassVinculacion;
	private Boolean Eliminado;
	
	public TblCuidadorPr() {super();}

	public TblCuidadorPr(Long idCuidador, String contrasena, String tipoResidencia, String passVinculacion, Boolean eliminado) {
		super();
		this.IdCuidador = idCuidador;
		this.Contrasena = contrasena;
		this.TipoResidencia = tipoResidencia;
		this.PassVinculacion = passVinculacion;
		this.Eliminado = eliminado;
	}
	
	public void EliminarPorIdCuidadorRegTblCuidadorPr(Long idCuidador) {		
		try {
			TblCuidadorPr elCuiP = Select.from(TblCuidadorPr.class).where(Condition.prop("ID_CUIDADOR").eq(idCuidador)).first();
			elCuiP.delete();			
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarCuiP), e.getMessage());
		}		
	}

	public Long getIdCuidador() {
		return IdCuidador;
	}

	public void setIdCuidador(Long idCuidador) {
		IdCuidador = idCuidador;
	}

	public String getContrasena() {
		return Contrasena;
	}

	public void setContrasena(String contrasena) {
		Contrasena = contrasena;
	}

	public String getTipoResidencia() {
		return TipoResidencia;
	}

	public void setTipoResidencia(String tipoResidencia) {
		TipoResidencia = tipoResidencia;
	}

	public String getPassVinculacion() {
		return PassVinculacion;
	}

	public void setPassVinculacion(String passVinculacion) {
		PassVinculacion = passVinculacion;
	}
	
	public Boolean getEliminado() {
		return Eliminado;
	}

	public void setEliminado(Boolean eliminado) {
		Eliminado = eliminado;
	}

	public static void EliminarDatos(){
		TblCuidadorPr.executeQuery("delete from "+TblCuidadorPr.getTableName(TblCuidadorPr.class));
		if(TblCuidadorPr.count(TblCuidadorPr.class)==0) {
			Log.i("TBLCUIDADORPR","------------->Eliminado");
		}else{
			Log.i("TBLCUIDADORPR","-------------> NOOOOOO Eliminado :'(");
		}
	}
}