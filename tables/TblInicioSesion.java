package com.Notifications.patientssassistant.tables;


import android.util.Log;

import java.util.Date;
import com.orm.SugarRecord;


public class TblInicioSesion extends SugarRecord<TblInicioSesion> {
	
	private Long IdIniSes;
	private Long IdCuiPac;
	private String TipoUser;
	private String Usuario;
	private Integer AnioIni;
	private Integer MesIni;
	private Integer DiaIni;
	private Integer HoraIni;
	private Integer MinutosIni;
	private Integer AnioFin;		
	private Integer MesFin;
	private Integer DiaFin;		
	private Integer HoraFin;
	private Integer MinutosFin;
	private String IdReGCM;
	private Boolean Eliminado;
	
	public TblInicioSesion() {super();}

	public TblInicioSesion(Long idIniSes, Long idCuiPac, String tipoUser, String usuario, Integer anioIni, Integer mesIni,
							Integer diaIni, Integer horaIni, Integer minutosIni, Integer anioFin, Integer mesFin,
							Integer diaFin, Integer horaFin, Integer minutosFin, String idReGCM, Boolean eliminado) {
		super();
		this.IdIniSes = idIniSes;
		this.IdCuiPac = idCuiPac;
		this.TipoUser = tipoUser;
		this.Usuario = usuario;
		this.AnioIni = anioIni;
		this.MesIni = mesIni;
		this.DiaIni = diaIni;
		this.HoraIni = horaIni;
		this.MinutosIni = minutosIni;
		this.AnioFin = anioFin;
		this.MesFin = mesFin;
		this.DiaFin = diaFin;		
		this.HoraFin = horaFin;	
		this.MinutosFin = minutosFin;
		this.IdReGCM = idReGCM;
		this.Eliminado = eliminado;
	}
	
	public Long getIdIniSes() {
		return IdIniSes;
	}

	public void setIdIniSes(Long idIniSes) {
		IdIniSes = idIniSes;
	}

	public Long getIdCuiPac() {
		return IdCuiPac;
	}

	public void setIdCuiPac(Long idCuiPac) {
		IdCuiPac = idCuiPac;
	}
	
	public String getTipoUser() {
		return TipoUser;
	}

	public void setTipoUser(String tipoUser) {
		TipoUser = tipoUser;
	}
	
	public String getUsuario() {
		return Usuario;
	}

	public Integer getAnioIni() {
		return AnioIni;
	}

	public void setAnioIni(Integer anioIni) {
		AnioIni = anioIni;
	}

	public Integer getMesIni() {
		return MesIni;
	}

	public void setMesIni(Integer mesIni) {
		MesIni = mesIni;
	}

	public Integer getDiaIni() {
		return DiaIni;
	}

	public void setDiaIni(Integer diaIni) {
		DiaIni = diaIni;
	}

	public Integer getHoraIni() {
		return HoraIni;
	}

	public void setHoraIni(Integer horaIni) {
		HoraIni = horaIni;
	}

	public Integer getMinutosIni() {
		return MinutosIni;
	}

	public void setMinutosIni(Integer minutosIni) {
		MinutosIni = minutosIni;
	}

	public Integer getAnioFin() {
		return AnioFin;
	}

	public void setAnioFin(Integer anioFin) {
		AnioFin = anioFin;
	}

	public Integer getMesFin() {
		return MesFin;
	}

	public void setMesFin(Integer mesFin) {
		MesFin = mesFin;
	}

	public Integer getDiaFin() {
		return DiaFin;
	}

	public void setDiaFin(Integer diaFin) {
		DiaFin = diaFin;
	}

	public Integer getHoraFin() {
		return HoraFin;
	}

	public void setHoraFin(Integer horaFin) {
		HoraFin = horaFin;
	}

	public Integer getMinutosFin() {
		return MinutosFin;
	}

	public void setMinutosFin(Integer minutosFin) {
		MinutosFin = minutosFin;
	}

	public void setUsuario(String usuario) {
		Usuario = usuario;
	}
	
	public String getIdReGCM() {
		return IdReGCM;
	}

	public void setIdReGCM(String idReGCM) {
		IdReGCM = idReGCM;
	}

	public Boolean getEliminado() {
		return Eliminado;
	}

	public void setEliminado(Boolean eliminado) {
		Eliminado = eliminado;
	}

	public static void EliminarDatos(){
		TblInicioSesion.executeQuery("delete from "+TblInicioSesion.getTableName(TblInicioSesion.class));
		if(TblInicioSesion.count(TblInicioSesion.class)==0) {
			Log.i("TBLINICIOSESION", "------------->Eliminado");
		}else{
			Log.i("TBLINICIOSESION", "-------------> NOOOOOO Eliminado :'(");
		}
	}
	
}