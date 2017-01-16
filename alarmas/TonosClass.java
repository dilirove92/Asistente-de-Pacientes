package com.Notifications.patientssassistant.alarmas;


import com.Notifications.patientssassistant.R;


public class TonosClass {
	
	static int resultado;
	
	public static int BuscarIdTono(String elTono) {			
		if (elTono.equals("Afternoon Walk")) {
			resultado=R.raw.afternoon_walk;
		}
		if (elTono.equals("Alarm")) {
			resultado=R.raw.alarm;
		}
		if (elTono.equals("Clock Work")) {
			resultado=R.raw.clock_work;
		}
		if (elTono.equals("Country Road")) {
			resultado=R.raw.country_road;
		}
		if (elTono.equals("Cowoy")) {
			resultado=R.raw.cowoy;
		}
		if (elTono.equals("Dione")) {
			resultado=R.raw.dione;
		}
		if (elTono.equals("Glissando Tone")) {
			resultado=R.raw.glissando_tone;
		}
		if (elTono.equals("Harmonica")) {
			resultado=R.raw.harmonica;
		}
		if (elTono.equals("Laser")) {
			resultado=R.raw.laser;
		}
		if (elTono.equals("Luna")) {
			resultado=R.raw.luna;
		}
		if (elTono.equals("Ring Digital")) {
			resultado=R.raw.ring_digital;
		}
		if (elTono.equals("Rise Up")) {
			resultado=R.raw.rise_up;
		}
		if (elTono.equals("Rolling Tone")) {
			resultado=R.raw.rolling_tone;
		}			
		if (elTono.equals("Rush")) {
			resultado=R.raw.rush;
		}
		if (elTono.equals("Serene Morning")) {
			resultado=R.raw.serene_morning;
		}
		if (elTono.equals("Soft Harp")) {
			resultado=R.raw.soft_harp;
		}
		if (elTono.equals("Timer")) {
			resultado=R.raw.timer;
		}	
		if (elTono.equals("Ukulele")) {
			resultado=R.raw.ukulele;
		}		
		return resultado;
	}

	public static int BuscarIdTonoNotificacion(String elTono) {
		if (elTono.equals("Amazing")) {
			resultado=R.raw.amazing;
		}
		if (elTono.equals("Beber")) {
			resultado=R.raw.beber;
		}
		if (elTono.equals("Cancion de Cuna")) {
			resultado=R.raw.cancion_de_cuna;
		}
		if (elTono.equals("Comer")) {
			resultado=R.raw.comer;
		}
		if (elTono.equals("Dormir")) {
			resultado=R.raw.dormir;
		}
		if (elTono.equals("Gota")) {
			resultado=R.raw.gota;
		}
		if (elTono.equals("Gotas")) {
			resultado=R.raw.gotas;
		}
		if (elTono.equals("Luz")) {
			resultado=R.raw.luz;
		}
		if (elTono.equals("Popo")) {
			resultado=R.raw.popo;
		}
		if (elTono.equals("Puerta")) {
			resultado=R.raw.puerta;
		}
		if (elTono.equals("SOS")) {
			resultado=R.raw.sos;
		}
		if (elTono.equals("TV")) {
			resultado=R.raw.television;
		}
		return resultado;
	}

}