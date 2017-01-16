package com.Notifications.patientssassistant.tables;

import android.util.Log;

import com.orm.SugarRecord;

public class TblHilos extends SugarRecord<TblHilos>{

	private String Nombre;
	private int Anio;
	private int Mes;
	private  int Dia;
	private int Hora;
	private int Minutos;
	private Boolean Ejecutado;

	public TblHilos() {
		super();
	}

	public TblHilos(String nombre, int anio, int mes, int dia, int hora, int min, Boolean ejecutado) {
		super();
		this.Nombre = nombre;
		this.Anio=anio;
		this.Mes=mes;
		this.Dia=dia;
		this.Hora=hora;
		this.Minutos=min;
		this.Ejecutado = ejecutado;
	}

	public String getNombre() {
		return Nombre;
	}

	public void setNombre(String nombre) {
		Nombre = nombre;
	}

	public int getAnio() {
		return Anio;
	}

	public void setAnio(int anio) {
		this.Anio = anio;
	}

	public int getMes() {
		return Mes;
	}

	public void setMes(int mes) {
		this.Mes = mes;
	}

	public int getDia() {
		return Dia;
	}

	public void setDia(int dia) {
		this.Dia = dia;
	}

	public int getHora() {
		return Hora;
	}

	public void setHora(int hora) {
		this.Hora = hora;
	}

	public int getMinutos() {
		return Minutos;
	}

	public void setMinutos(int minutos) {
		this.Minutos = minutos;
	}

	public Boolean getEjecutado() {
		return Ejecutado;
	}

	public void setEjecutado(Boolean ejecutado) {
		Ejecutado = ejecutado;
	}

	public static void EliminarDatos(){
		TblHilos.executeQuery("delete from "+TblHilos.getTableName(TblHilos.class));
		if(TblHilos.count(TblHilos.class)==0) {
			Log.i("TBLHILOS","------------->Eliminado");
		}else{
			Log.i("TBLHILOS", "-------------> NOOOOOO Eliminado :'(");
		}
	}
}
