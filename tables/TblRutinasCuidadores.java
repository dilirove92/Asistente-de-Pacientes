package com.Notifications.patientssassistant.tables;


import com.Notifications.patientssassistant.R;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;


public class TblRutinasCuidadores extends SugarRecord<TblRutinasCuidadores> {
	
	private Long IdRutinaC;
	private Long IdCuidador;
	private Long IdActividad;
	private int Hora;		
	private int Minutos;
	private Boolean Domingo;
	private Boolean Lunes;
	private Boolean Martes;
	private Boolean Miercoles;
	private Boolean Jueves;
	private Boolean Viernes;
	private Boolean Sabado;
	private String Tono;
	private String Descripcion;
	private Boolean Alarma;
	private Boolean Eliminado;
	public final List<TblRutinasCuidadores> Children = new ArrayList<TblRutinasCuidadores>();
	
	public TblRutinasCuidadores() {super();}
		
	public TblRutinasCuidadores(Long idRutinaC, Long idCuidador, Long idActividad, int hora, int minutos, 
								Boolean domingo, Boolean lunes, Boolean martes, Boolean miercoles, Boolean jueves,
								Boolean viernes, Boolean sabado, String tono, String descripcion, Boolean alarma, Boolean eliminado) {
		super();
		this.IdRutinaC = idRutinaC;
		this.IdCuidador = idCuidador;
		this.IdActividad = idActividad;		
		this.Hora = hora;
		this.Minutos = minutos;
		this.Domingo = domingo;
		this.Lunes = lunes;
		this.Martes = martes;
		this.Miercoles = miercoles;
		this.Jueves = jueves;
		this.Viernes = viernes;
		this.Sabado = sabado;
		this.Tono = tono;
		this.Descripcion = descripcion;
		this.Alarma = alarma;
		this.Eliminado = eliminado;
	}

	public TblRutinasCuidadores(Long idRutinaC, Long idActividad, int hora, int minutos, Boolean domingo, Boolean lunes, 
								Boolean martes, Boolean miercoles, Boolean jueves, Boolean viernes, Boolean sabado, Boolean alarma) {
		super();
		this.IdRutinaC = idRutinaC;
		this.IdActividad = idActividad;
		this.Hora = hora;
		this.Minutos = minutos;
		this.Domingo = domingo;
		this.Lunes = lunes;
		this.Martes = martes;
		this.Miercoles = miercoles;
		this.Jueves = jueves;
		this.Viernes = viernes;
		this.Sabado = sabado;
		this.Alarma = alarma;
	}
	
	public TblRutinasCuidadores(String descripcion, String tono) {
		super();
		this.Descripcion = descripcion;
		this.Tono = tono;
	}

	public void EliminarPorIdRutinaRegTblRutinasCuidadores(Long idRutinaC) {		
		try {			
			//ELIMINAR LA RUTINA DEL CUIDADOR
			TblRutinasCuidadores laRutCui = Select.from(TblRutinasCuidadores.class).where(Condition.prop("ID_RUTINA_C").eq(idRutinaC)).first();
			laRutCui.delete();			
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarRutCui), e.getMessage());
		}		
	}
	
	public void EliminarPorIdCuidadorRegTblRutinasCuidadores(Long idCuidador) {
		try {
			//GENERAR UNA LISTA DE LAS RUTINAS/CUIDADORES A ELIMINAR POR ID_CUIDADOR
			List<TblRutinasCuidadores> laRutCui = Select.from(TblRutinasCuidadores.class).where(Condition.prop("ID_CUIDADOR").eq(idCuidador)).list();
			TblRutinasCuidadores eliminar_laRutCui = new TblRutinasCuidadores();
			for (int i = 0; i < laRutCui.size(); i++) {
				TblRutinasCuidadores registro_laRutCui=laRutCui.get(i);	
				//LLAMAR AL METODO "EliminarPorIdRutinaRegTblRutinasCuidadores" EL CUAL ELIMINA LA RUTINA/CUIDADOR POR ID_RUTINA_C
				eliminar_laRutCui.EliminarPorIdRutinaRegTblRutinasCuidadores(registro_laRutCui.getIdRutinaC());	
			}
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarRutsCui), e.getMessage());
		}
	}	
	
	public void EliminarPorIdActividadRegTblRutinasCuidadores(Long idActividad) {
		try {
			//GENERAR UNA LISTA DE LAS RUTINAS/CUIDADORES A ELIMINAR POR ID_ACTIVIDAD
			List<TblRutinasCuidadores> laRutCui = Select.from(TblRutinasCuidadores.class).where(Condition.prop("ID_ACTIVIDAD").eq(idActividad)).list();
			TblRutinasCuidadores eliminar_laRutCui = new TblRutinasCuidadores();
			for (int i = 0; i < laRutCui.size(); i++) {
				TblRutinasCuidadores registro_laRutCui=laRutCui.get(i);	
				//LLAMAR AL METODO "EliminarPorIdRutinaRegTblRutinasCuidadores" EL CUAL ELIMINA LA RUTINA/CUIDADOR POR ID_RUTINA_C
				eliminar_laRutCui.EliminarPorIdRutinaRegTblRutinasCuidadores(registro_laRutCui.getIdRutinaC());	
			}
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarRutsCui), e.getMessage());
		}
	}	
	
	public Long getIdRutinaC() {
		return IdRutinaC;
	}
	
	public void setIdRutinaC(Long idRutinaC) {
		IdRutinaC = idRutinaC;
	}
	
	public Long getIdCuidador() {
		return IdCuidador;
	}
	
	public void setIdCuidador(Long idCuidador) {
		IdCuidador = idCuidador;
	}
	
	public Long getIdActividad() {
		return IdActividad;
	}
	
	public void setIdActividad(Long idActividad) {
		IdActividad = idActividad;
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
		
	public String getTono() {
		return Tono;
	}

	public void setTono(String tono) {
		Tono = tono;
	}

	public String getDescripcion() {
		return Descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		Descripcion = descripcion;
	}
	
	public Boolean getAlarma() {
		return Alarma;
	}
	
	public void setAlarma(Boolean alarma) {
		Alarma = alarma;
	}
	
	public Boolean getEliminado() {
		return Eliminado;
	}

	public void setEliminado(Boolean eliminado) {
		Eliminado = eliminado;
	}

	public static void EliminarDatos(){
		TblRutinasCuidadores.executeQuery("delete from "+TblRutinasCuidadores.getTableName(TblRutinasCuidadores.class));
		if(TblRutinasCuidadores.count(TblRutinasCuidadores.class)==0) {
			Log.i("TBLRUTINASCUIDADORES","------------->Eliminado");
		}else{
			Log.i("TBLRUTINASCUIDADORES","-------------> NOOOOOO Eliminado :'(");
		}
	}
}