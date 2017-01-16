package com.Notifications.patientssassistant.tables;


import com.Notifications.patientssassistant.R;
import java.util.List;
import android.util.Log;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;


public class TblBuzon extends SugarRecord<TblBuzon> {
		
	private Long IdCuidador;
	private Long IdPaciente;
	private Long IdActividad;
	private int Anio;
	private int Mes;
	private int Dia;
	private int Horas;
	private int Minutos;
	private Boolean Eliminado;
	private Integer Contador;
	private Boolean Postergar;

		
	public TblBuzon() {super();}

	public TblBuzon(Long idCuidador, Long idPaciente, Long idActividad, int anio, int mes, int dia, int horas, int minutos, Boolean eliminado, Boolean postergar) {
		super();
		this.IdCuidador = idCuidador;
		this.IdPaciente = idPaciente;
		this.IdActividad = idActividad;
		this.Anio =  anio;
		this.Mes = mes;
		this.Dia = dia;
		this.Horas = horas;
		this.Minutos = minutos;
		this.Eliminado = eliminado;
		this.Postergar = postergar;
	}

	public TblBuzon(Long idCuidador, Long idPaciente, Long idActividad, int anio, int mes, int dia, int horas, int minutos, Boolean eliminado, Integer contador, Boolean postergar) {
		super();
		this.IdCuidador = idCuidador;
		this.IdPaciente = idPaciente;
		this.IdActividad = idActividad;
		this.Anio =  anio;
		this.Mes = mes;
		this.Dia = dia;
		this.Horas = horas;
		this.Minutos = minutos;
		this.Eliminado = eliminado;
		this.Contador = contador;
		this.Postergar = postergar;
	}
	
	public void EliminarPorIdCuidadorRegTblBuzon(Long idCuidador) {
		try {
			List<TblBuzon> elBuzon = Select.from(TblBuzon.class).where(Condition.prop("ID_CUIDADOR").eq(idCuidador)).list();						
			for (int i = 0; i < elBuzon.size(); i++) {
				TblBuzon registro_elBuzon=elBuzon.get(i);	
				registro_elBuzon.delete();	
			}
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarBuzon), e.getMessage());
		}
	}
	
	public void EliminarPorIdPacienteRegTblBuzon(Long idPaciente) {
		try {
			List<TblBuzon> elBuzon = Select.from(TblBuzon.class).where(Condition.prop("ID_PACIENTE").eq(idPaciente)).list();						
			for (int i = 0; i < elBuzon.size(); i++) {
				TblBuzon registro_elBuzon=elBuzon.get(i);
				registro_elBuzon.delete();	
			}
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarBuzon), e.getMessage());
		}
	}
	
	public void EliminarPorIdActividadRegTblBuzon(Long idActividad) {
		try {
			List<TblBuzon> elBuzon = Select.from(TblBuzon.class).where(Condition.prop("ID_ACTIVIDAD").eq(idActividad)).list();						
			for (int i = 0; i < elBuzon.size(); i++) {
				TblBuzon registro_elBuzon=elBuzon.get(i);	
				registro_elBuzon.delete();	
			}
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarBuzon), e.getMessage());
		}
	}

	public void EliminarPorIdPacienteActividadRegTblBuzon(Long idPaciente, Long idActividad) {
		try {
			List<TblBuzon> elBuzon = Select.from(TblBuzon.class).where(Condition.prop("ID_PACIENTE").eq(idPaciente),
																	   Condition.prop("ID_ACTIVIDAD").eq(idActividad)).list();
			for (int i = 0; i < elBuzon.size(); i++) {
				TblBuzon registro_elBuzon=elBuzon.get(i);
				registro_elBuzon.delete();
			}
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarBuzon), e.getMessage());
		}
	}

	public Long getIdCuidador() {
		return IdCuidador;
	}

	public void setIdCuidador(Long idCuidador) { IdCuidador = idCuidador; }

	public Long getIdPaciente() {
		return IdPaciente;
	}

	public void setIdPaciente(Long idPaciente) {
		IdPaciente = idPaciente;
	}

	public Long getIdActividad() {
		return IdActividad;
	}

	public void setIdActividad(Long idActividad) {
		IdActividad = idActividad;
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

	public int getHoras() {
		return Horas;
	}

	public void setHoras(int horas) {
		Horas = horas;
	}

	public int getMinutos() {
		return Minutos;
	}

	public void setMinutos(int minutos) {
		Minutos = minutos;
	}

	public Boolean getEliminado() {
		return Eliminado;
	}

	public void setEliminado(Boolean eliminado) {
		Eliminado = eliminado;
	}

	public Integer getContador() { return Contador; }

	public void setContador(Integer contador) { Contador = contador; }

	public Boolean getPostergar() { return Postergar; }

	public void setPostergar(Boolean postergar) { Postergar = postergar; }

	public static void EliminarDatos(){
		TblBuzon.executeQuery("delete from "+TblBuzon.getTableName(TblBuzon.class));
		if(TblBuzon.count(TblBuzon.class)==0) {
			Log.i("TBLBUZON","------------->Eliminado");
		}else{
			Log.i("TBLBUZON","-------------> NOOOOOO Eliminado :'(");
		}
	}

}