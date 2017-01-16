package com.Notifications.patientssassistant.tables;


import com.Notifications.patientssassistant.R;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;


public class TblControlDieta extends SugarRecord<TblControlDieta> {
	
	private Long IdControlDieta;
	private Long IdPaciente;
	private String Motivo;
	private String AlimentosRecomendados;
	private String AlimentosNoAdecuados;
	private Boolean Eliminado;
	public final List<TblControlDieta> Children = new ArrayList<TblControlDieta>();
	
	public TblControlDieta() {super();}
	
	public TblControlDieta(Long idControlDieta, Long idPaciente, String motivo, String alimentosRecomendados, 
               			   String alimentosNoAdecuados, Boolean eliminado) {
		super();
		this.IdControlDieta = idControlDieta;
		this.IdPaciente = idPaciente;
		this.Motivo = motivo;
		this.AlimentosRecomendados = alimentosRecomendados;
		this.AlimentosNoAdecuados = alimentosNoAdecuados;
		this.Eliminado = eliminado;
	}
	
	public TblControlDieta(Long idControlDieta, String motivo) {
		super();
		this.IdControlDieta = idControlDieta;
		this.Motivo = motivo;
	}
	
	public TblControlDieta(String alimentosRecomendados, String alimentosNoAdecuados) {
		super();
		this.AlimentosRecomendados = alimentosRecomendados;
		this.AlimentosNoAdecuados = alimentosNoAdecuados;
	}
	
	public void EliminarPorIdControlDietaRegTblControlDieta(Long idControlDieta) {		
		try {
			TblControlDieta elConDie = Select.from(TblControlDieta.class).where(Condition.prop("ID_CONTROL_DIETA").eq(idControlDieta)).first();
			elConDie.delete();			
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarConDie), e.getMessage());
		}		
	}
	
	public void EliminarPorIdPacienteRegTblControlDieta(Long idPaciente) {
		try {
			//GENERAR UNA LISTA DE LOS CONTROLES DE DIETA A ELIMINAR POR ID_PACIENTE
			List<TblControlDieta> elConDie = Select.from(TblControlDieta.class).where(Condition.prop("ID_PACIENTE").eq(idPaciente)).list();
			TblControlDieta eliminar_elConDie = new TblControlDieta();
			for (int i = 0; i < elConDie.size(); i++) {
				TblControlDieta registro_elConDie=elConDie.get(i);
				//LLAMAR AL METODO "EliminarPorIdControlDietaRegTblControlDieta" EL CUAL ELIMINA EL CONTROL DE DIETA POR ID_CONTROL_DIETA
				eliminar_elConDie.EliminarPorIdControlDietaRegTblControlDieta(registro_elConDie.getIdControlDieta());				
			}
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarConsDie), e.getMessage());
		}
	}
	
	public Long getIdControlDieta() {
		return IdControlDieta;
	}
	
	public void setIdControlDieta(Long idControlDieta) {
		IdControlDieta = idControlDieta;
	}
	
	public Long getIdPaciente() {
		return IdPaciente;
	}
	
	public void setIdPaciente(Long idPaciente) {
		IdPaciente = idPaciente;
	}
	
	public String getMotivo() {
		return Motivo;
	}
	
	public void setMotivo(String motivo) {
		Motivo = motivo;
	}
	
	public String getAlimentosRecomendados() {
		return AlimentosRecomendados;
	}
	
	public void setAlimentosRecomendados(String alimentosRecomendados) {
		AlimentosRecomendados = alimentosRecomendados;
	}
	
	public String getAlimentosNoAdecuados() {
		return AlimentosNoAdecuados;
	}
	
	public void setAlimentosNoAdecuados(String alimentosNoAdecuados) {
		AlimentosNoAdecuados = alimentosNoAdecuados;
	}
	
	public Boolean getEliminado() {
		return Eliminado;
	}

	public void setEliminado(Boolean eliminado) {
		Eliminado = eliminado;
	}

	public static void EliminarDatos(){
		TblControlDieta.executeQuery("delete from "+TblControlDieta.getTableName(TblControlDieta.class));
		if(TblControlDieta.count(TblControlDieta.class)==0) {
			Log.i("TBLCONTROLDIETA","------------->Eliminado");
		}else{
			Log.i("TBLCONTROLDIETA","-------------> NOOOOOO Eliminado :'(");
		}
	}
}