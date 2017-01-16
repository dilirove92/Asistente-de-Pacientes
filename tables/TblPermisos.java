package com.Notifications.patientssassistant.tables;


import com.Notifications.patientssassistant.R;
import java.util.List;
import android.util.Log;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;


public class TblPermisos extends SugarRecord<TblPermisos> {
	
	private Long IdPermiso;
	private Long IdCuidador;
	private Long IdPaciente;
	private Boolean NotifiAlarma;	
	private Boolean ContMedicina;
	private Boolean RegCreador;
	private Boolean Eliminado;
	
	public TblPermisos() {super();}
	
	public TblPermisos(Long idPermiso, Long idCuidador, Long idPaciente, Boolean notifiAlarma, Boolean contMedicina, 
					   Boolean regCreador, Boolean eliminado) {
		super();
		this.IdPermiso = idPermiso;
		this.IdCuidador = idCuidador;
		this.IdPaciente = idPaciente;
		this.NotifiAlarma = notifiAlarma;
		this.RegCreador = regCreador;
		this.ContMedicina = contMedicina;
		this.Eliminado = eliminado;
	}
	
	public void EliminarPorIdPermisoRegTblPermisos(Long idPermiso) {		
		try {
			TblPermisos elPermiso = Select.from(TblPermisos.class).where(Condition.prop("ID_PERMISO").eq(idPermiso)).first();
			elPermiso.delete();			
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarPer), e.getMessage());
		}		
	}
	
	public void EliminarPorIdCuidadorRegTblPermisos(Long idCuidador) {
		try {
			//GENERAR UNA LISTA DE LOS PERMISOS A ELIMINAR POR ID_CUIDADOR
			List<TblPermisos> elPermiso = Select.from(TblPermisos.class).where(Condition.prop("ID_CUIDADOR").eq(idCuidador)).list();
			TblPermisos eliminar_elPermiso = new TblPermisos();
			for (int i = 0; i < elPermiso.size(); i++) {
				TblPermisos registro_elPermiso=elPermiso.get(i);	
				//LLAMAR AL METODO "EliminarPorIdPermisoRegTblPermisos" EL CUAL ELIMINA EL PERMISO POR ID_PERMISO
				eliminar_elPermiso.EliminarPorIdPermisoRegTblPermisos(registro_elPermiso.getIdPermiso());	
			}
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarPers), e.getMessage());
		}
	}
	
	public void EliminarPorIdPacienteRegTblPermisos(Long idPaciente) {
		try {
			//GENERAR UNA LISTA DE LOS PERMISOS A ELIMINAR POR ID_PACIENTE
			List<TblPermisos> elPermiso = Select.from(TblPermisos.class).where(Condition.prop("ID_PACIENTE").eq(idPaciente)).list();
			TblPermisos eliminar_elPermiso = new TblPermisos();
			for (int i = 0; i < elPermiso.size(); i++) {
				TblPermisos registro_elPermiso=elPermiso.get(i);	
				//LLAMAR AL METODO "EliminarPorIdPermisoRegTblPermisos" EL CUAL ELIMINA EL PERMISO POR ID_PERMISO
				eliminar_elPermiso.EliminarPorIdPermisoRegTblPermisos(registro_elPermiso.getIdPermiso());
			}
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarPers), e.getMessage());
		}
	}
		
	public Long getIdPermiso() {
		return IdPermiso;
	}

	public void setIdPermiso(Long idPermiso) {
		IdPermiso = idPermiso;
	}

	public Long getIdCuidador() {
		return IdCuidador;
	}
	
	public void setIdCuidador(Long idCuidador) {
		IdCuidador = idCuidador;
	}
	
	public Long getIdPaciente() {
		return IdPaciente;
	}
	
	public void setIdPaciente(Long idPaciente) {
		IdPaciente = idPaciente;
	}
	
	public Boolean getNotifiAlarma() {
		return NotifiAlarma;
	}
	
	public void setNotifiAlarma(Boolean notifiAlarma) {
		NotifiAlarma = notifiAlarma;
	}
	
	public Boolean getContMedicina() {
		return ContMedicina;
	}
	
	public void setContMedicina(Boolean contMedicina) {
		ContMedicina = contMedicina;
	}
	
	public Boolean getRegCreador() {
		return RegCreador;
	}
	
	public void setRegCreador(Boolean regCreador) {
		RegCreador = regCreador;
	}
	
	public Boolean getEliminado() {
		return Eliminado;
	}

	public void setEliminado(Boolean eliminado) {
		Eliminado = eliminado;
	}

	public static void EliminarDatos(){
		TblPermisos.executeQuery("delete from "+TblPermisos.getTableName(TblPermisos.class));
		if(TblPermisos.count(TblPermisos.class)==0) {
			Log.i("TBLPERMISOS","------------->Eliminado");
		}else{
			Log.i("TBLPERMISOS","-------------> NOOOOOO Eliminado :'(");
		}
	}
}