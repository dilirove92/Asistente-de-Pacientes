package com.Notifications.patientssassistant.tables;


import com.Notifications.patientssassistant.R;
import android.util.Log;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;


public class TblEstadoSalud extends SugarRecord<TblEstadoSalud> {
	
	private Long IdPaciente;
	private String TipoSangre;
	private String FacultadMental;
	private Boolean Enfermedad;
	private String DescEnfermedad;
	private Boolean Cirugias;
	private String DescCirugias;
	private Boolean Medicamentos;
	private String DescMedicamentos;
	private Boolean Discapacidad;
	private String TipoDiscapacidad;
	private String GradoDiscapacidad;
	private String Implementos;
	private Boolean Eliminado;
	
	public TblEstadoSalud() {super();}
	
	public TblEstadoSalud(Long idPaciente, String tipoSangre, String facultadMental, Boolean enfermedad, 
						  String descEnfermedad, Boolean cirugias, String descCirugias, Boolean medicamentos,
						  String descMedicamentos, Boolean discapacidad, String tipoDiscapacidad, 
						  String gradoDiscapacidad, String implementos, Boolean eliminado) {
		super();
		this.IdPaciente = idPaciente;
		this.TipoSangre = tipoSangre;
		this.FacultadMental = facultadMental;
		this.Enfermedad = enfermedad;
		this.DescEnfermedad = descEnfermedad;
		this.Cirugias = cirugias;
		this.DescCirugias = descCirugias;
		this.Medicamentos = medicamentos;
		this.DescMedicamentos = descMedicamentos;
		this.Discapacidad = discapacidad;
		this.TipoDiscapacidad = tipoDiscapacidad;
		this.GradoDiscapacidad = gradoDiscapacidad;
		this.Implementos = implementos;
		this.Eliminado = eliminado;
	}
	
	public void EliminarPorIdPacienteRegTblEstadoSalud(Long idPaciente) {		
		try {
			TblEstadoSalud elEstSal = Select.from(TblEstadoSalud.class).where(Condition.prop("ID_PACIENTE").eq(idPaciente)).first();
			elEstSal.delete();			
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarEstSal), e.getMessage());
		}		
	}
	
	public Long getIdPaciente() {
		return IdPaciente;
	}
	
	public void setIdPaciente(Long idPaciente) {
		IdPaciente = idPaciente;
	}
	
	public String getTipoSangre() {
		return TipoSangre;
	}
	
	public void setTipoSangre(String tipoSangre) {
		TipoSangre = tipoSangre;
	}
	
	public String getFacultadMental() {
		return FacultadMental;
	}
	
	public void setFacultadMental(String facultadMental) {
		FacultadMental = facultadMental;
	}
	
	public Boolean getEnfermedad() {
		return Enfermedad;
	}
	
	public void setEnfermedad(Boolean enfermedad) {
		Enfermedad = enfermedad;
	}
	
	public String getDescEnfermedad() {
		return DescEnfermedad;
	}
	
	public void setDescEnfermedad(String descEnfermedad) {
		DescEnfermedad = descEnfermedad;
	}
	
	public Boolean getCirugias() {
		return Cirugias;
	}
	
	public void setCirugias(Boolean cirugias) {
		Cirugias = cirugias;
	}
	
	public String getDescCirugias() {
		return DescCirugias;
	}
	
	public void setDescCirugias(String descCirugias) {
		DescCirugias = descCirugias;
	}
	
	public Boolean getMedicamentos() {
		return Medicamentos;
	}
	
	public void setMedicamentos(Boolean medicamentos) {
		Medicamentos = medicamentos;
	}
	
	public String getDescMedicamentos() {
		return DescMedicamentos;
	}
	
	public void setDescMedicamentos(String descMedicamentos) {
		DescMedicamentos = descMedicamentos;
	}
	
	public Boolean getDiscapacidad() {
		return Discapacidad;
	}
	
	public void setDiscapacidad(Boolean discapacidad) {
		Discapacidad = discapacidad;
	}
	
	public String getTipoDiscapacidad() {
		return TipoDiscapacidad;
	}
	
	public void setTipoDiscapacidad(String tipoDiscapacidad) {
		TipoDiscapacidad = tipoDiscapacidad;
	}
	
	public String getGradoDiscapacidad() {
		return GradoDiscapacidad;
	}
	
	public void setGradoDiscapacidad(String gradoDiscapacidad) {
		GradoDiscapacidad = gradoDiscapacidad;
	}
	
	public String getImplementos() {
		return Implementos;
	}
	
	public void setImplementos(String implementos) {
		Implementos = implementos;
	}
	
	public Boolean getEliminado() {
		return Eliminado;
	}

	public void setEliminado(Boolean eliminado) {
		Eliminado = eliminado;
	}

	public static void EliminarDatos(){
		TblEstadoSalud.executeQuery("delete from "+TblEstadoSalud.getTableName(TblEstadoSalud.class));
		if(TblEstadoSalud.count(TblEstadoSalud.class)==0) {
			Log.i("TBLACTIVIDADES","------------->Eliminado");
		}else{
			Log.i("TBLACTIVIDADES","-------------> NOOOOOO Eliminado :'(");
		}
	}
}