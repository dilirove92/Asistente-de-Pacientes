package com.Notifications.patientssassistant.tables;


import com.Notifications.patientssassistant.R;
import java.util.List;
import android.util.Log;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;


public class TblHorarioMedicina extends SugarRecord<TblHorarioMedicina> {
	
	private Long IdHorarioMedicina;
	private Long IdControlMedicina;	
	private int Hora;	
	private int Minutos;
	private Boolean Domingo;
	private Boolean Lunes;
	private Boolean Martes;
	private Boolean Miercoles;
	private Boolean Jueves;
	private Boolean Viernes;
	private Boolean Sabado;
	private Boolean ActDesAlarma;
	private Boolean Eliminado;
	
	public TblHorarioMedicina() {super();}
	
	public TblHorarioMedicina(Long idHorarioMedicina, Long idControlMedicina, int hora, int minutos, Boolean domingo, Boolean lunes, 
			                  Boolean martes, Boolean miercoles, Boolean jueves, Boolean viernes, Boolean sabado, 
			                  Boolean actDesAlarma, Boolean eliminado) {
		super();
		this.IdHorarioMedicina = idHorarioMedicina;
		this.IdControlMedicina = idControlMedicina;
		this.Hora = hora;
		this.Minutos = minutos;
		this.Domingo = domingo;
		this.Lunes = lunes;
		this.Martes = martes;
		this.Miercoles = miercoles;
		this.Jueves = jueves;
		this.Viernes = viernes;
		this.Sabado = sabado;
		this.ActDesAlarma = actDesAlarma;
		this.Eliminado = eliminado;
	}	
	
	public TblHorarioMedicina(int hora, int minutos, Boolean domingo, Boolean lunes, Boolean martes, Boolean miercoles, Boolean jueves, 
							  Boolean viernes, Boolean sabado, Boolean actDesAlarma) {
		super();
		this.Hora = hora;
		this.Minutos = minutos;
		this.Domingo = domingo;
		this.Lunes = lunes;
		this.Martes = martes;
		this.Miercoles = miercoles;
		this.Jueves = jueves;
		this.Viernes = viernes;
		this.Sabado = sabado;
		this.ActDesAlarma = actDesAlarma;
	}
	
	public void EliminarPorIdHorarioMedicinaRegTblHorarioMedicina(Long idHorarioMedicina) {		
		try {
			//ELIMINAR EL HORARIO DE MEDICINA DEL CUIDADOR
			TblHorarioMedicina elHorMed = Select.from(TblHorarioMedicina.class).where(Condition.prop("ID_HORARIO_MEDICINA").eq(idHorarioMedicina)).first();
			elHorMed.delete();			
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarHorMed), e.getMessage());
		}		
	}
	
	public void EliminarPorIdControlMedicinaRegTblHorarioMedicina(Long idControlMedicina) {
		try {
			//GENERAR UNA LISTA DE LOS HORARIOS DE MEDICINA A ELIMINAR POR ID_CONTROL_MEDICINA
			List<TblHorarioMedicina> elHorMed = Select.from(TblHorarioMedicina.class).where(Condition.prop("ID_CONTROL_MEDICINA").eq(idControlMedicina)).list();
			TblHorarioMedicina eliminar_elHorMed = new TblHorarioMedicina();
			for (int i = 0; i < elHorMed.size(); i++) {
				TblHorarioMedicina registro_elHorMed=elHorMed.get(i);
				//LLAMAR AL METODO "EliminarPorIdHorarioMedicinaRegTblHorarioMedicina" EL CUAL ELIMINA EL HORARIO DE MEDICINA POR ID_HORARIO_MEDICINA
				eliminar_elHorMed.EliminarPorIdHorarioMedicinaRegTblHorarioMedicina(registro_elHorMed.getIdHorarioMedicina());	
			}
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarHorsMed), e.getMessage());
		}
	}		
	
	public Long getIdHorarioMedicina() {
		return IdHorarioMedicina;
	}
	
	public void setIdHorarioMedicina(Long idHorarioMedicina) {
		IdHorarioMedicina = idHorarioMedicina;
	}
	
	public Long getIdControlMedicina() {
		return IdControlMedicina;
	}
	
	public void setIdControlMedicina(Long idControlMedicina) {
		IdControlMedicina = idControlMedicina;
	}
	
	public int getHora() {
		return Hora;
	}
	
	public void setHora(int hora) {
		Hora = hora;
	}
	
	public int getMinutos() {
		return Minutos;
	}

	public void setMinutos(int minutos) {
		Minutos = minutos;
	}

	public Boolean getDomingo() {
		return Domingo;
	}
	
	public void setDomingo(Boolean domingo) {
		Domingo = domingo;
	}
	
	public Boolean getLunes() {
		return Lunes;
	}
	
	public void setLunes(Boolean lunes) {
		Lunes = lunes;
	}
	
	public Boolean getMartes() {
		return Martes;
	}
	
	public void setMartes(Boolean martes) {
		Martes = martes;
	}
	
	public Boolean getMiercoles() {
		return Miercoles;
	}
	
	public void setMiercoles(Boolean miercoles) {
		Miercoles = miercoles;
	}
	
	public Boolean getJueves() {
		return Jueves;
	}
	
	public void setJueves(Boolean jueves) {
		Jueves = jueves;
	}
	
	public Boolean getViernes() {
		return Viernes;
	}
	
	public void setViernes(Boolean viernes) {
		Viernes = viernes;
	}
	
	public Boolean getSabado() {
		return Sabado;
	}
	
	public void setSabado(Boolean sabado) {
		Sabado = sabado;
	}
	
	public Boolean getActDesAlarma() {
		return ActDesAlarma;
	}
	
	public void setActDesAlarma(Boolean actDesAlarma) {
		ActDesAlarma = actDesAlarma;
	}
	
	public Boolean getEliminado() {
		return Eliminado;
	}

	public void setEliminado(Boolean eliminado) {
		Eliminado = eliminado;
	}

	public static void EliminarDatos(){
		TblHorarioMedicina.executeQuery("delete from "+TblHorarioMedicina.getTableName(TblHorarioMedicina.class));
		if(TblHorarioMedicina.count(TblHorarioMedicina.class)==0) {
			Log.i("TBLHORARIOMEDICINA","------------->Eliminado");
		}else{
			Log.i("TBLHORARIOMEDICINA","-------------> NOOOOOO Eliminado :'(");
		}
	}
}