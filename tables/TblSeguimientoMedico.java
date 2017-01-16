package com.Notifications.patientssassistant.tables;


import com.Notifications.patientssassistant.R;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;


public class TblSeguimientoMedico extends SugarRecord<TblSeguimientoMedico> {
	
	private Long IdSeguimientoMedico;
	private Long IdPaciente;	
	private int Anio;
	private int Mes;
	private int Dia;
	private String UnidadMedica;
	private String Doctor;
	private String Diagnostico;
	private String TratamientoMedico;
	private String AlimentacionSugerida;
	private Boolean Eliminado;
	public final List<TblSeguimientoMedico> Children = new ArrayList<TblSeguimientoMedico>();
	
	public TblSeguimientoMedico() {super();}
	
	public TblSeguimientoMedico(Long idSeguimientoMedico, Long idPaciente, int anio, int mes, int dia, String unidadMedica, String doctor, 
								String diagnostico, String tratamientoMedico, String alimentacionSugerida, Boolean eliminado) {
		super();
		this.IdSeguimientoMedico = idSeguimientoMedico;
		this.IdPaciente = idPaciente;
		this.Anio = anio;
		this.Mes = mes;
		this.Dia = dia;
		this.UnidadMedica = unidadMedica;
		this.Doctor = doctor;
		this.Diagnostico = diagnostico;
		this.TratamientoMedico = tratamientoMedico;
		this.AlimentacionSugerida = alimentacionSugerida;
		this.Eliminado = eliminado;
	}	
	
	public TblSeguimientoMedico(Long idSeguimientoMedico, int anio, int mes, int dia, String doctor) {
		super();
		this.IdSeguimientoMedico = idSeguimientoMedico;
		this.Anio = anio;
		this.Mes = mes;
		this.Dia = dia;
		this.Doctor = doctor;
	}
	
	public TblSeguimientoMedico(String unidadMedica, String diagnostico, String tratamientoMedico, String alimentacionSugerida) {
		super();
		this.UnidadMedica = unidadMedica;
		this.Diagnostico = diagnostico;
		this.TratamientoMedico = tratamientoMedico;
		this.AlimentacionSugerida = alimentacionSugerida;
	}
	
	public void EliminarPorIdSeguimientoMedicoRegTblSeguimientoMedico(Long idSeguimientoMedico) {		
		try {
			TblSeguimientoMedico elSegMed = Select.from(TblSeguimientoMedico.class).where(Condition.prop("ID_SEGUIMIENTO_MEDICO").eq(idSeguimientoMedico)).first();
			elSegMed.delete();			
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarSegMed), e.getMessage());
		}		
	}
	
	public void EliminarPorIdPacienteRegTblSeguimientoMedico(Long idPaciente) {
		try {
			//GENERAR UNA LISTA DE LOS SEGUIMIENTOS MEDICOS A ELIMINAR POR ID_PACIENTE
			List<TblSeguimientoMedico> elSegMed = Select.from(TblSeguimientoMedico.class).where(Condition.prop("ID_PACIENTE").eq(idPaciente)).list();
			TblSeguimientoMedico eliminar_elSegMed = new TblSeguimientoMedico();
			for (int i = 0; i < elSegMed.size(); i++) {
				TblSeguimientoMedico registro_elSegMed=elSegMed.get(i);
				//LLAMAR AL METODO "EliminarPorIdSeguimientoMedicoRegTblSeguimientoMedico" EL CUAL ELIMINA EL SEGUIMIENTO MEDICO POR ID_SEGUIMIENTO_MEDICO
				eliminar_elSegMed.EliminarPorIdSeguimientoMedicoRegTblSeguimientoMedico(registro_elSegMed.getIdSeguimientoMedico());				
			}
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarSegsMed), e.getMessage());
		}
	}
	
	public Long getIdSeguimientoMedico() {
		return IdSeguimientoMedico;
	}
	
	public void setIdSeguimientoMedico(Long idSeguimientoMedico) {
		IdSeguimientoMedico = idSeguimientoMedico;
	}
	
	public Long getIdPaciente() {
		return IdPaciente;
	}
	
	public void setIdPaciente(Long idPaciente) {
		IdPaciente = idPaciente;
	}
		
	public int getAnio() {
		return Anio;
	}

	public void setAnio(int anio) {
		Anio = anio;
	}

	public int getMes() {
		return Mes;
	}

	public void setMes(int mes) {
		Mes = mes;
	}

	public int getDia() {
		return Dia;
	}

	public void setDia(int dia) {
		Dia = dia;
	}

	public String getUnidadMedica() {
		return UnidadMedica;
	}
	
	public void setUnidadMedica(String unidadMedica) {
		UnidadMedica = unidadMedica;
	}
	
	public String getDoctor() {
		return Doctor;
	}
	
	public void setDoctor(String doctor) {
		Doctor = doctor;
	}
	
	public String getDiagnostico() {
		return Diagnostico;
	}
	
	public void setDiagnostico(String diagnostico) {
		Diagnostico = diagnostico;
	}
	
	public String getTratamientoMedico() {
		return TratamientoMedico;
	}
	
	public void setTratamientoMedico(String tratamientoMedico) {
		TratamientoMedico = tratamientoMedico;
	}
	
	public String getAlimentacionSugerida() {
		return AlimentacionSugerida;
	}
	
	public void setAlimentacionSugerida(String alimentacionSugerida) {
		AlimentacionSugerida = alimentacionSugerida;
	}
	
	public Boolean getEliminado() {
		return Eliminado;
	}

	public void setEliminado(Boolean eliminado) {
		Eliminado = eliminado;
	}

	public static void EliminarDatos(){
		TblSeguimientoMedico.executeQuery("delete from "+TblSeguimientoMedico.getTableName(TblSeguimientoMedico.class));
		if(TblSeguimientoMedico.count(TblSeguimientoMedico.class)==0) {
			Log.i("TBLSEGUIMIENTOMEDICO","------------->Eliminado");
		}else{
			Log.i("TBLSEGUIMIENTOMEDICO","-------------> NOOOOOO Eliminado :'(");
		}
	}
}