package com.Notifications.patientssassistant.tables;


import com.Notifications.patientssassistant.R;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;


public class TblObservaciones extends SugarRecord<TblObservaciones> {
	
	private Long IdObservacion;
	private Long IdPaciente;
	private int Anio;
	private int Mes;
	private int Dia;
	private int Hora;
	private int Minutos;
	private String Observacion;
	private Boolean Eliminado;
	public final List<TblObservaciones> Children = new ArrayList<TblObservaciones>();
	
	public TblObservaciones() {super();}
	
	public TblObservaciones(Long idObservacion, Long idPaciente, int anio, int mes, int dia, int hora, int minutos,
							String observacion, Boolean eliminado) {
		super();
		this.IdObservacion = idObservacion;
		this.IdPaciente = idPaciente;
		this.Anio = anio;
		this.Mes = mes;
		this.Dia = dia;
		this.Hora = hora;
		this.Minutos = minutos;
		this.Observacion = observacion;
		this.Eliminado = eliminado;
	}
	
	public TblObservaciones(Long idObservacion, int anio, int mes, int dia, int hora, int minutos) {
		super();
		this.IdObservacion = idObservacion;
		this.Anio = anio;
		this.Mes = mes;
		this.Dia = dia;
		this.Hora = hora;
		this.Minutos = minutos;
	}
	
	public TblObservaciones(String observacion) {
		super();
		this.Observacion = observacion;
	}
	
	public void EliminarPorIdObservacionRegTblObservaciones(Long idObservacion) {		
		try {
			TblObservaciones laObs = Select.from(TblObservaciones.class).where(Condition.prop("ID_OBSERVACION").eq(idObservacion)).first();
			laObs.delete();			
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarObs), e.getMessage());
		}		
	}
	
	public void EliminarPorIdPacienteRegTblObservaciones(Long idPaciente) {
		try {
			//GENERAR UNA LISTA DE LAS OBSERVACIONES A ELIMINAR POR ID_PACIENTE
			List<TblObservaciones> laObs = Select.from(TblObservaciones.class).where(Condition.prop("ID_PACIENTE").eq(idPaciente)).list();
			TblObservaciones eliminar_laObs = new TblObservaciones();
			for (int i = 0; i < laObs.size(); i++) {
				TblObservaciones registro_laObs=laObs.get(i);
				//LLAMAR AL METODO "EliminarPorIdObservacionRegTblObservaciones" EL CUAL ELIMINA LA OBSERVACION POR ID_OBSERVACION
				eliminar_laObs.EliminarPorIdObservacionRegTblObservaciones(registro_laObs.getIdObservacion());				
			}
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarObss), e.getMessage());
		}
	}
	
	public Long getIdObservacion() {
		return IdObservacion;
	}
	
	public void setIdObservacion(Long idObservacion) {
		IdObservacion = idObservacion;
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

	public String getObservacion() {
		return Observacion;
	}
	
	public void setObservacion(String observacion) {
		Observacion = observacion;
	}

	public Boolean getEliminado() {
		return Eliminado;
	}

	public void setEliminado(Boolean eliminado) {
		Eliminado = eliminado;
	}

	public static void EliminarDatos(){
		TblObservaciones.executeQuery("delete from " + TblObservaciones.getTableName(TblObservaciones.class));
		if(TblObservaciones.count(TblObservaciones.class)==0) {
			Log.i("TBLOBSERVACIONES","------------->Eliminado");
		}else{
			Log.i("TBLOBSERVACIONES","-------------> NOOOOOO Eliminado :'(");
		}
	}
}