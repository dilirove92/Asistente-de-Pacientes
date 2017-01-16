package com.Notifications.patientssassistant.adapters;

public class ListaItemDrawer {
		
	private String titulo;
	private int icono;
	
	public ListaItemDrawer(String title, int icon) {
		  this.titulo = title;
	      this.icono = icon;		    
	}	
	
    public String getTitulo() {
		return titulo;
	}
    
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public int getIcono() {
		return icono;
	}
	
	public void setIcono(int icono) {
		this.icono = icono;
	} 

		
}