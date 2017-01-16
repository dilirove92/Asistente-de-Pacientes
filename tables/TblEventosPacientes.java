package com.Notifications.patientssassistant.tables;


import com.Notifications.patientssassistant.R;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;


public class TblEventosPacientes extends SugarRecord<TblEventosPacientes> {
	
	private Long IdEventoP;
	private Long IdPaciente;
	private Long IdActividad;
	private int AnioE;
	private int MesE;
	private int DiaE;
	private int HoraE;
	private int MinutosE;
	private int AnioR;
	private int MesR;
	private int DiaR;
	private int HoraR;
	private int MinutosR;
	private String Lugar;
	private String Descripcion;
	private String Tono;
	private Boolean Alarma;
	private Boolean Eliminado;
	public final List<TblEventosPacientes> Children = new ArrayList<TblEventosPacientes>();
		
	
	public TblEventosPacientes() {super();}

	public TblEventosPacientes(Long idEventoP, Long idPaciente, Long idActividad, int anioE, int mesE, int diaE, int horaE,
							   int minutosE, int anioR, int mesR, int diaR, int horaR, int minutosR, String lugar, 
							   String descripcion, String tono, Boolean alarma, Boolean eliminado) {
		super();
		this.IdEventoP = idEventoP;
		this.IdPaciente = idPaciente;
		this.IdActividad = idActividad;
		this.AnioE = anioE;
		this.MesE = mesE;
		this.DiaE = diaE;
		this.HoraE = horaE;
		this.MinutosE = minutosE;
		this.AnioR = anioR;
		this.MesR = mesR;
		this.DiaR = diaR;
		this.HoraR = horaR;
		this.MinutosR = minutosR;
		this.Lugar = lugar;
		this.Descripcion = descripcion;
		this.Tono = tono;
		this.Alarma = alarma;
		this.Eliminado = eliminado;
	}

	public TblEventosPacientes(Long idEventoP, Long idActividad, int anioE, int mesE, int diaE, int horaE, int minutosE, Boolean alarma) {
		super();
		this.IdEventoP = idEventoP;
		this.IdActividad = idActividad;
		this.AnioE = anioE;
		this.MesE = mesE;
		this.DiaE = diaE;
		this.HoraE = horaE;
		this.MinutosE = minutosE;
		this.Alarma = alarma;
	}

	public TblEventosPacientes(int anioR, int mesR, int diaR, int horaR, int minutosR, String lugar, String descripcion, 
							   String tono) {
		super();
		this.AnioR = anioR;
		this.MesR = mesR;
		this.DiaR = diaR;
		this.HoraR = horaR;
		this.MinutosR = minutosR;
		this.Lugar = lugar;
		this.Descripcion = descripcion;
		this.Tono = tono;
	}

	public void EliminarPorIdEventoRegTblEventosPacientes(Long idEventoP) {		
		try {
			//ELIMINAR EL EVENTO DEL PACIENTE
			TblEventosPacientes elEvePac = Select.from(TblEventosPacientes.class).where(Condition.prop("ID_EVENTO_P").eq(idEventoP)).first();
			elEvePac.delete();			
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarEvePac), e.getMessage());
		}		
	}
	
	public void EliminarPorIdPacienteRegTblEventosPacientes(Long idPaciente) {
		try {
			//GENERAR UNA LISTA DE LAS EVENTOS/PACIENTES A ELIMINAR POR ID_PACIENTE
			List<TblEventosPacientes> elEvePac = Select.from(TblEventosPacientes.class).where(Condition.prop("ID_PACIENTE").eq(idPaciente)).list();
			TblEventosPacientes eliminar_elEvePac = new TblEventosPacientes();
			for (int i = 0; i < elEvePac.size(); i++) {
				TblEventosPacientes registro_elEvePac=elEvePac.get(i);	
				//LLAMAR AL METODO "EliminarPorIdEventoRegTblEventosPacientes" EL CUAL ELIMINA EL EVENTO/PACIENTE POR ID_EVENTO_P
				eliminar_elEvePac.EliminarPorIdEventoRegTblEventosPacientes(registro_elEvePac.getIdEventoP());	
			}
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarEvesPac), e.getMessage());
		}
	}
	
	public void EliminarPorIdActividadRegTblEventosPacientes(Long idActividad) {
		try {
			//GENERAR UNA LISTA DE LAS EVENTOS/PACIENTES A ELIMINAR POR ID_ACTIVIDAD
			List<TblEventosPacientes> elEvePac = Select.from(TblEventosPacientes.class).where(Condition.prop("ID_ACTIVIDAD").eq(idActividad)).list();
			TblEventosPacientes eliminar_elEvePac = new TblEventosPacientes();
			for (int i = 0; i < elEvePac.size(); i++) {
				TblEventosPacientes registro_elEvePac=elEvePac.get(i);	
				//LLAMAR AL METODO "EliminarPorIdEventoRegTblEventosPacientes" EL CUAL ELIMINA EL EVENTO/PACIENTE POR ID_EVENTO_P
				eliminar_elEvePac.EliminarPorIdEventoRegTblEventosPacientes(registro_elEvePac.getIdEventoP());	
			}
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarEvesPac), e.getMessage());
		}
	}
	
	public Long getIdEventoP() {
		return IdEventoP;
	}

	public void setIdEventoP(Long idEventoP) {
		IdEventoP = idEventoP;
	}

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
		
	public int getAnioE() {
		return AnioE;
	}

	public void setAnioE(int anioE) {
		AnioE = anioE;
	}

	public int getMesE() {
		return MesE;
	}

	public void setMesE(int mesE) {
		MesE = mesE;
	}

	public int getDiaE() {
		return DiaE;
	}

	public void setDiaE(int diaE) {
		DiaE = diaE;
	}

	public int getHoraE() {
		return HoraE;
	}

	public void setHoraE(int horaE) {
		HoraE = horaE;
	}

	public int getMinutosE() {
		return MinutosE;
	}

	public void setMinutosE(int minutosE) {
		MinutosE = minutosE;
	}

	public int getAnioR() {
		return AnioR;
	}

	public void setAnioR(int anioR) {
		AnioR = anioR;
	}

	public int getMesR() {
		return MesR;
	}

	public void setMesR(int mesR) {
		MesR = mesR;
	}

	public int getDiaR() {
		return DiaR;
	}

	public void setDiaR(int diaR) {
		DiaR = diaR;
	}

	public int getHoraR() {
		return HoraR;
	}

	public void setHoraR(int horaR) {
		HoraR = horaR;
	}

	public int getMinutosR() {
		return MinutosR;
	}

	public void setMinutosR(int minutosR) {
		MinutosR = minutosR;
	}

	public String getLugar() {
		return Lugar;
	}

	public void setLugar(String lugar) {
		Lugar = lugar;
	}

	public String getDescripcion() {
		return Descripcion;
	}

	public void setDescripcion(String descripcion) {
		Descripcion = descripcion;
	}
	
	public String getTono() {
		return Tono;
	}

	public void setTono(String tono) {
		Tono = tono;
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
		TblEventosPacientes.executeQuery("delete from "+TblEventosPacientes.getTableName(TblEventosPacientes.class));
		if(TblEventosPacientes.count(TblEventosPacientes.class)==0) {
			Log.i("TBLEVENTOSPACIENTES","------------->Eliminado");
		}else{
			Log.i("TBLEVENTOSPACIENTES","-------------> NOOOOOO Eliminado :'(");
		}
	}
}