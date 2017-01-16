package com.Notifications.patientssassistant.tables;


import com.Notifications.patientssassistant.R;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;


public class TblEventosCuidadores extends SugarRecord<TblEventosCuidadores> {
	
	private Long IdEventoC;
	private Long IdCuidador;	
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
	public final List<TblEventosCuidadores> Children = new ArrayList<TblEventosCuidadores>();

	
	public TblEventosCuidadores() {super();}

	public TblEventosCuidadores(Long idEventoC, Long idCuidador, Long idActividad, int anioE, int mesE, int diaE, int horaE,
								int minutosE, int anioR, int mesR, int diaR, int horaR, int minutosR, String lugar, 
								String descripcion, String tono, Boolean alarma, Boolean eliminado) {
		super();
		this.IdEventoC = idEventoC;
		this.IdCuidador = idCuidador;
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
	
	public TblEventosCuidadores(Long idEventoC, Long idActividad, int anioE, int mesE, int diaE, int horaE, int minutosE, Boolean alarma) {
		super();
		this.IdEventoC = idEventoC;
		this.IdActividad = idActividad;
		this.AnioE = anioE;
		this.MesE = mesE;
		this.DiaE = diaE;
		this.HoraE = horaE;
		this.MinutosE = minutosE;
		this.Alarma = alarma;
	}
	
	public TblEventosCuidadores(int anioR, int mesR, int diaR, int horaR, int minutosR, String lugar, String descripcion, 
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

	public void EliminarPorIdEventoRegTblEventosCuidadores(Long idEventoC) {
		try {
			//ELIMINAR EL EVENTO DEL CUIDADOR
			TblEventosCuidadores elEveCui = Select.from(TblEventosCuidadores.class).where(Condition.prop("ID_EVENTO_C").eq(idEventoC)).first();
			elEveCui.delete();			
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarEveCui), e.getMessage());
		}	
	}
	
	public void EliminarPorIdCuidadorRegTblEventosCuidadores(Long idCuidador) {
		try {
			//GENERAR UNA LISTA DE LOS EVENTOS/CUIDADORES A ELIMINAR POR ID_CUIDADOR
			List<TblEventosCuidadores> elEveCui = Select.from(TblEventosCuidadores.class).where(Condition.prop("ID_CUIDADOR").eq(idCuidador)).list();
			TblEventosCuidadores eliminar_elEveCui = new TblEventosCuidadores();
			for (int i = 0; i < elEveCui.size(); i++) {
				TblEventosCuidadores registro_elEveCui=elEveCui.get(i);
				//LLAMAR AL METODO "EliminarPorIdEventoRegTblEventosCuidadores" EL CUAL ELIMINA EL EVENTO/CUIDADOR POR ID_EVENTO_C
				eliminar_elEveCui.EliminarPorIdEventoRegTblEventosCuidadores(registro_elEveCui.getIdEventoC());	
			}
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarEvesCui), e.getMessage());
		}	
	}
	
	public void EliminarPorIdActividadRegTblEventosCuidadores(Long idActividad) {
		try {
			//GENERAR UNA LISTA DE LOS EVENTOS/CUIDADORES A ELIMINAR POR ID_ACTIVIDAD
			List<TblEventosCuidadores> elEveCui = Select.from(TblEventosCuidadores.class).where(Condition.prop("ID_ACTIVIDAD").eq(idActividad)).list();
			TblEventosCuidadores eliminar_elEveCui = new TblEventosCuidadores();
			for (int i = 0; i < elEveCui.size(); i++) {
				TblEventosCuidadores registro_elEveCui=elEveCui.get(i);	
				//LLAMAR AL METODO "EliminarPorIdEventoRegTblEventosCuidadores" EL CUAL ELIMINA EL EVENTO/CUIDADOR POR ID_EVENTO_C
				eliminar_elEveCui.EliminarPorIdEventoRegTblEventosCuidadores(registro_elEveCui.getIdEventoC());	
			}
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarEvesCui), e.getMessage());
		}
	}	
	
	public Long getIdEventoC() {
		return IdEventoC;
	}
	
	public void setIdEventoC(Long idEventoC) {
		IdEventoC = idEventoC;
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
		TblEventosCuidadores.executeQuery("delete from "+TblEventosCuidadores.getTableName(TblEventosCuidadores.class));
		if(TblEventosCuidadores.count(TblEventosCuidadores.class)==0) {
			Log.i("TBLEVENTOSCUIDADORES","------------->Eliminado");
		}else{
			Log.i("TBLEVENTOSCUIDADORES","-------------> NOOOOOO Eliminado :'(");
		}
	}
}