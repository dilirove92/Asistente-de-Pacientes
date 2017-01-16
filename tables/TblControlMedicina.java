package com.Notifications.patientssassistant.tables;


import com.Notifications.patientssassistant.R;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;


public class TblControlMedicina extends SugarRecord<TblControlMedicina> {
		
	private Long IdControlMedicina;
	private Long IdPaciente;
	private String Medicamento;
	private String Descripcion;
	private String MotivoMedicacion;  
	private String Tiempo;  
	private String Dosis;	
	private int NdeVeces;
	private String Tono;	
	private Boolean Eliminado;
	public ArrayList<TblHorarioMedicina> HorariosList = new ArrayList<TblHorarioMedicina>();
	
	public TblControlMedicina() { super(); }
		
	public TblControlMedicina(Long idControlMedicina, Long idPaciente, String medicamento, String descripcion, 
							  String motivoMedicacion, String tiempo, String dosis, int ndeVeces, String tono,
							  Boolean eliminado) {
		super();
		this.IdControlMedicina = idControlMedicina;
		this.IdPaciente = idPaciente;
		this.Medicamento = medicamento;
		this.Descripcion = descripcion;
		this.MotivoMedicacion = motivoMedicacion;
		this.Tiempo = tiempo;
		this.Dosis = dosis;
		this.NdeVeces = ndeVeces;
		this.Tono = tono;
		this.Eliminado = eliminado;
	}

	public TblControlMedicina(Long idControlMedicina, String medicamento, String tiempo,  
							  String dosis, int ndeVeces, ArrayList<TblHorarioMedicina> horariosList) {
		super();
		this.IdControlMedicina = idControlMedicina;
		this.Medicamento = medicamento;
		this.Tiempo = tiempo;
		this.Dosis = dosis;
		this.NdeVeces = ndeVeces;
		this.HorariosList = horariosList;
	}
	
	public void EliminarPorIdControlMedicinaRegTblControlMedicina(Long idControlMedicina) {		
		try {
			//ELIMINAR HORARIOS DE MEDICINA POR ID_CONTROL_MEDICINA
			TblHorarioMedicina losHorMed=new TblHorarioMedicina();
			losHorMed.EliminarPorIdControlMedicinaRegTblHorarioMedicina(idControlMedicina);			
			//FINALMENTE SE ELIMINA EL CONTROL DE MEDICINA
			TblControlMedicina elConMed = Select.from(TblControlMedicina.class).where(Condition.prop("ID_CONTROL_MEDICINA").eq(idControlMedicina)).first();
			elConMed.delete();			
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarConMed), e.getMessage());
		}		
	}
	
	public void EliminarPorIdPacienteRegTblControlMedicina(Long idPaciente) {
		try {
			//GENERAR UNA LISTA DE LOS CONTROLES DE MEDICINA A ELIMINAR POR ID_PACIENTE
			List<TblControlMedicina> elFamPac = Select.from(TblControlMedicina.class).where(Condition.prop("ID_PACIENTE").eq(idPaciente)).list();
			TblControlMedicina eliminar_elFamPac = new TblControlMedicina();
			for (int i = 0; i < elFamPac.size(); i++) {
				TblControlMedicina registro_elFamPac=elFamPac.get(i);
				//LLAMAR AL METODO "EliminarPorIdControlMedicinaRegTblControlMedicina" EL CUAL ELIMINA EL CONTROL DE MEDICINA POR ID_CONTROL_MEDICINA
				eliminar_elFamPac.EliminarPorIdControlMedicinaRegTblControlMedicina(registro_elFamPac.getIdControlMedicina());				
			}
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarConsMed), e.getMessage());
		}
	}
	
	public Long getIdControlMedicina() {
		return IdControlMedicina;
	}
	
	public void setIdControlMedicina(Long idControlMedicina) {
		IdControlMedicina = idControlMedicina;
	}
	
	public Long getIdPaciente() {
		return IdPaciente;
	}
	
	public void setIdPaciente(Long idPaciente) {
		IdPaciente = idPaciente;
	}
	
	public String getMedicamento() {
		return Medicamento;
	}
	
	public void setMedicamento(String medicamento) {
		Medicamento = medicamento;
	}
	
	public String getDescripcion() {
		return Descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		Descripcion = descripcion;
	}
	
	public String getMotivoMedicacion() {
		return MotivoMedicacion;
	}
	
	public void setMotivoMedicacion(String motivoMedicacion) {
		MotivoMedicacion = motivoMedicacion;
	}
	
	public String getTiempo() {
		return Tiempo;
	}

	public void setTiempo(String tiempo) {
		Tiempo = tiempo;
	}

	public String getDosis() {
		return Dosis;
	}
	
	public void setDosis(String dosis) {
		Dosis = dosis;
	}
	
	public int getNdeVeces() {
		return NdeVeces;
	}
	
	public void setNdeVeces(int ndeVeces) {
		NdeVeces = ndeVeces;
	}
	
	public String getTono() {
		return Tono;
	}

	public void setTono(String tono) {
		Tono = tono;
	}

	public Boolean getEliminado() {
		return Eliminado;
	}

	public void setEliminado(Boolean eliminado) {
		Eliminado = eliminado;
	}
	
	public ArrayList<TblHorarioMedicina> getHorariosList() {
		return HorariosList;
	}

	public void setHorariosList(ArrayList<TblHorarioMedicina> horariosList) {
		HorariosList = horariosList;
	}

	public static void EliminarDatos(){
		TblControlMedicina.executeQuery("delete from "+TblControlMedicina.getTableName(TblControlMedicina.class));
		if(TblControlMedicina.count(TblControlMedicina.class)==0) {
			Log.i("TBLCONTROLMEDICINA","------------->Eliminado");
		}else{
			Log.i("TBLCONTROLMEDICINA","-------------> NOOOOOO Eliminado :'(");
		}
	}
}