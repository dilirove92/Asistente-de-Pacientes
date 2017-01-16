package com.Notifications.patientssassistant.tables;


import com.Notifications.patientssassistant.R;
import java.util.List;
import android.util.Log;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;


public class TblHorarios extends SugarRecord<TblHorarios> {
	
	private Long IdHorario;
	private Long IdCuidador;
	private String TipoHorario;
	private int HoraIni;
	private int MinutosIni;
	private int HoraFin;
	private int MinutosFin;
	private Boolean Domingo;
	private Boolean Lunes;
	private Boolean Martes;
	private Boolean Miercoles;
	private Boolean Jueves;
	private Boolean Viernes;
	private Boolean Sabado;
	private Boolean Eliminado;
		
	public TblHorarios() {super();}

	public TblHorarios(Long idHorario, Long idCuidador, String tipoHorario, int horaIni, int minutosIni, int horaFin, int minutosFin, 
					   Boolean domingo, Boolean lunes, Boolean martes, Boolean miercoles, Boolean jueves, Boolean viernes, Boolean sabado, Boolean eliminado) {
		super();
		this.IdHorario = idHorario;
		this.IdCuidador = idCuidador;
		this.TipoHorario = tipoHorario;
		this.HoraIni = horaIni;
		this.MinutosIni = minutosIni;
		this.HoraFin = horaFin;
		this.MinutosFin = minutosFin;
		this.Domingo = domingo;
		this.Lunes = lunes;
		this.Martes = martes;
		this.Miercoles = miercoles;
		this.Jueves = jueves;
		this.Viernes = viernes;
		this.Sabado = sabado;
		this.Eliminado = eliminado;
	}
	
	public void EliminarPorIdHorarioRegTblHorarios(Long idHorario) {		
		try {
			TblHorarios elHorario = Select.from(TblHorarios.class).where(Condition.prop("ID_HORARIO").eq(idHorario)).first();
			elHorario.delete();			
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarHor), e.getMessage());
		}		
	}
	
	public void EliminarPorIdCuidadorRegTblHorarios(Long idCuidador) {
		try {
			//GENERAR UNA LISTA DE HORARIOS A ELIMINAR POR ID_CUIDADOR
			List<TblHorarios> elHorario = Select.from(TblHorarios.class).where(Condition.prop("ID_CUIDADOR").eq(idCuidador)).list();
			TblHorarios eliminar_elHorario = new TblHorarios();
			for (int i = 0; i < elHorario.size(); i++) {
				TblHorarios registro_elHorario=elHorario.get(i);	
				//LLAMAR AL METODO "EliminarPorIdHorarioRegTblHorarios" EL CUAL ELIMINA EL HORARIO POR ID_HORARIO
				eliminar_elHorario.EliminarPorIdHorarioRegTblHorarios(registro_elHorario.getIdHorario());					
			}
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarHors), e.getMessage());
		}
	}	
	
	public Long getIdHorario() {
		return IdHorario;
	}

	public void setIdHorario(Long idHorario) {
		IdHorario = idHorario;
	}

	public Long getIdCuidador() {
		return IdCuidador;
	}

	public void setIdCuidador(Long idCuidador) {
		IdCuidador = idCuidador;
	}

	public String getTipoHorario() {
		return TipoHorario;
	}

	public void setTipoHorario(String tipoHorario) {
		TipoHorario = tipoHorario;
	}
	
	public int getHoraIni() {
		return HoraIni;
	}

	public void setHoraIni(int horaIni) {
		HoraIni = horaIni;
	}

	public int getMinutosIni() {
		return MinutosIni;
	}

	public void setMinutosIni(int minutosIni) {
		MinutosIni = minutosIni;
	}

	public int getHoraFin() {
		return HoraFin;
	}

	public void setHoraFin(int horaFin) {
		HoraFin = horaFin;
	}

	public int getMinutosFin() {
		return MinutosFin;
	}

	public void setMinutosFin(int minutosFin) {
		MinutosFin = minutosFin;
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
	
	public Boolean getEliminado() {
		return Eliminado;
	}

	public void setEliminado(Boolean eliminado) {
		Eliminado = eliminado;
	}

	public static void EliminarDatos(){
		TblHorarios.executeQuery("delete from "+TblHorarios.getTableName(TblHorarios.class));
		if(TblHorarios.count(TblHorarios.class)==0) {
			Log.i("TBLHORARIOS","------------->Eliminado");
		}else{
			Log.i("TBLHORARIOS","-------------> NOOOOOO Eliminado :'(");
		}
	}
}