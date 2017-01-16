package com.Notifications.patientssassistant.tables;


import com.Notifications.patientssassistant.R;
import java.util.List;
import android.util.Log;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;


public class TblCuidadorS extends SugarRecord<TblCuidadorS> {
	
	private Long IdCuidador;
	private Long DependeDe;
	private Boolean Eliminado;
	
	public TblCuidadorS() {super();}

	public TblCuidadorS(Long idCuidador, Long dependeDe, Boolean eliminado) {
		super();
		this.IdCuidador = idCuidador;
		this.DependeDe = dependeDe;
		this.Eliminado = eliminado;
	}
	
	public void EliminarPorIdCuidadorRegTblCuidadorS(Long idCuidador) {		
		try {
			TblCuidadorS elCuiS = Select.from(TblCuidadorS.class).where(Condition.prop("ID_CUIDADOR").eq(idCuidador)).first();
			elCuiS.delete();			
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarCuiS), e.getMessage());
		}		
	}
	
	public void EliminarPorDependeDeRegTblCuidadorS(Long dependeDe) {
		try {
			//GENERAR UNA LISTA DE LOS CUIDADORES SECUNDARIOS A ELIMINAR POR DEPENDE_DE
			List<TblCuidadorS> elCuiS = Select.from(TblCuidadorS.class).where(Condition.prop("DEPENDE_DE").eq(dependeDe)).list();
			TblCuidadorS eliminar_elCuiS = new TblCuidadorS();
			for (int i = 0; i < elCuiS.size(); i++) {
				TblCuidadorS registro_elCuiS=elCuiS.get(i);
				//LLAMAR AL METODO "EliminarPorIdCuidadorRegTblCuidadorS" EL CUAL ELIMINA EL CUIDADOR SECUNDARIO POR ID_CUIDADOR
				eliminar_elCuiS.EliminarPorIdCuidadorRegTblCuidadorS(registro_elCuiS.getIdCuidador());				
			}
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarCuisS), e.getMessage());
		}
	}

	public Long getIdCuidador() {
		return IdCuidador;
	}

	public void setIdCuidador(Long idCuidador) {
		IdCuidador = idCuidador;
	}

	public Long getDependeDe() {
		return DependeDe;
	}

	public void setDependeDe(Long dependeDe) {
		DependeDe = dependeDe;
	}
	
	public Boolean getEliminado() {
		return Eliminado;
	}

	public void setEliminado(Boolean eliminado) {
		Eliminado = eliminado;
	}

	public static void EliminarDatos(){
		TblCuidadorS.executeQuery("delete from "+TblCuidadorS.getTableName(TblCuidadorS.class));
		if(TblCuidadorS.count(TblCuidadorS.class)==0) {
			Log.i("TBLCUIDADORS","------------->Eliminado");
		}else{
			Log.i("TBLCUIDADORS","-------------> NOOOOOO Eliminado :'(");
		}
	}
}