package com.Notifications.patientssassistant.adapters;


import java.util.Date;


public class ListaAsistente {
	
	private String Color;
	private Date Hora;
	private String Actividad;
	private String Detalle;
	
	
	public ListaAsistente() {
		super();
	}

	public ListaAsistente(String color, Date hora, String actividad, String detalle) {
		super();
		this.Color = color;
		this.Hora = hora;
		this.Actividad = actividad;
		this.Detalle = detalle;
	}

	public String getColor() {
		return Color;
	}

	public void setColor(String color) {
		this.Color = color;
	}

	public Date getHora() {
		return Hora;
	}

	public void setHora(Date hora) {
		this.Hora = hora;
	}
	
	public String getActividad() {
		return Actividad;
	}

	public void setActividad(String actividad) {
		this.Actividad = actividad;
	}

	public String getDetalle() {
		return Detalle;
	}

	public void setDetalle(String detalle) {
		this.Detalle = detalle;
	}
	
		
}