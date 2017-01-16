package com.Notifications.patientssassistant;


import com.Notifications.patientssassistant.adapters.*;
import com.Notifications.patientssassistant.tables.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import com.orm.query.Condition;
import com.orm.query.Select;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabWidget;
import android.widget.TextView;


public class TabAsistenteActivity extends Activity implements TabContentFactory {
	
	private Long vIdCuidador;
	private static SimpleDateFormat sdfTime =  new  SimpleDateFormat ("HH:mm");	
		
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.tab_asistente);
	    
	    RecogerParametros();
	
	    final TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
	    tabHost.setup();
	
	    Resources res = getResources();
	    Configuration cfg = res.getConfiguration();
	    boolean hor = cfg.orientation == Configuration.ORIENTATION_PORTRAIT;
	
	    if (hor) {
	        TabWidget tw = tabHost.getTabWidget();
	        tw.setOrientation(LinearLayout.VERTICAL);
	    }
	    
	    tabHost.addTab(tabHost.newTabSpec("Domingo")
	           .setIndicator(createIndicatorView(tabHost, getString(R.string.Dom)))
	           .setContent(TabAsistenteActivity.this));
	    tabHost.addTab(tabHost.newTabSpec("Lunes")
	           .setIndicator(createIndicatorView(tabHost, getString(R.string.Lun)))
	           .setContent(TabAsistenteActivity.this));
	    tabHost.addTab(tabHost.newTabSpec("Martes")
	           .setIndicator(createIndicatorView(tabHost, getString(R.string.Mar)))
	           .setContent(TabAsistenteActivity.this));
	    tabHost.addTab(tabHost.newTabSpec("Miercoles")
	           .setIndicator(createIndicatorView(tabHost, getString(R.string.Mie)))
	           .setContent(TabAsistenteActivity.this));
	    tabHost.addTab(tabHost.newTabSpec("Jueves")
	           .setIndicator(createIndicatorView(tabHost, getString(R.string.Jue)))
	           .setContent(TabAsistenteActivity.this));
	    tabHost.addTab(tabHost.newTabSpec("Viernes")
	           .setIndicator(createIndicatorView(tabHost, getString(R.string.Vie)))
	           .setContent(TabAsistenteActivity.this));
	    tabHost.addTab(tabHost.newTabSpec("Sabado")
	           .setIndicator(createIndicatorView(tabHost, getString(R.string.Sab)))
	           .setContent(TabAsistenteActivity.this));
	}
	
	
	private View createIndicatorView(TabHost tabHost, CharSequence label) {
	    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View tabIndicator = inflater.inflate(R.layout.adaptador_tab_indicator, tabHost.getTabWidget(), false); 
	    
	    final TextView tv = (TextView)tabIndicator.findViewById(R.id.txtTitle);
	    tv.setText(label);
	    
	    return tabIndicator;
	}
	
	
	@Override
	public View createTabContent(String tag) {
		View vista = null;				
		String queDia, queFecha, horario;
		
		for (int i = 0; i < 7; i++) {			
			List<ListaAsistente> listaAsistente = new ArrayList<ListaAsistente>();
			
			Calendar calendario=new GregorianCalendar();
			calendario.getTime();
			calendario.add(Calendar.DAY_OF_YEAR, i);
			Date fecha=calendario.getTime();
						
			queDia=BuscarDiaSemana(fecha);
			queFecha=MetodosValidacionesExtras.CalcularLaFecha(TabAsistenteActivity.this, fecha);			
			horario=DevolverHorarioHoy(vIdCuidador, queDia);
			
			if (tag.equals(queDia)) {
				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				vista = inflater.inflate(R.layout.ver_asistente, null, false);
				
				TextView TxtDia = (TextView)vista.findViewById(R.id.txtDia);
				TextView TxtHorario = (TextView)vista.findViewById(R.id.txtHorario);
				ListView Lista = (ListView)vista.findViewById(android.R.id.list);
				ImageView Image = (ImageView)vista.findViewById(R.id.imageView1);
				TextView TxtNoDatos = (TextView)vista.findViewById(R.id.txtNoHayDatos);
				
				TxtDia.setText(queFecha);
				TxtHorario.setText(getString(R.string.HoraTrab)+":\n "+"  "+horario);
				
				listaAsistente=DevolverLista(vIdCuidador, fecha);
				
				if (!listaAsistente.isEmpty()) {
					AdapterListaAsistente miAdapter = new AdapterListaAsistente(TabAsistenteActivity.this, listaAsistente);
					Lista.setAdapter(miAdapter);
					Image.setBackground(null);
					TxtNoDatos.setText("");
				}else {
					Lista.setAdapter(null);
					Image.setBackground(getResources().getDrawable(R.drawable.agenda2));
					TxtNoDatos.setText(R.string.NoHayAgenda);
				}			
			}
		}				
	    return vista;
	}
	
	
	public void RecogerParametros() {
		vIdCuidador = getIntent().getExtras().getLong("varIdeCuidador");		
	}	
	
	
	public List<ListaAsistente> DevolverLista (Long idCuidador, Date date)
	{
		List<ListaAsistente> listaAsistente = new ArrayList<ListaAsistente>();
		
		Calendar calendario=new GregorianCalendar();
		calendario.setTime(date);
		Date fecha=calendario.getTime();
		int anio, dia, mes;
		anio=calendario.get(Calendar.YEAR);
		mes=calendario.get(Calendar.MONTH);
		dia=calendario.get(Calendar.DAY_OF_MONTH);
		String DiaDeSemana=BuscarDiaSemana(fecha);
		
		
		//BUSCAR LOS EVENTOS DEL CUIDADOR DEL DIA
		List<TblEventosCuidadores> listaEventosCuidador = Select.from(TblEventosCuidadores.class)
				.where(Condition.prop("id_cuidador").eq(idCuidador),
					   Condition.prop("anio_e").eq(anio),
					   Condition.prop("mes_e").eq(mes),
					   Condition.prop("dia_e").eq(dia),
					   Condition.prop("Alarma").eq(1),
					   Condition.prop("Eliminado").eq(0)).list();
    	
		if (!listaEventosCuidador.isEmpty()) {
			try {
				//AGREGAR  LA LISTA LOS EVENTOS DEL CUIDADOR DEL DIA
				for (int i = 0; i < listaEventosCuidador.size(); i++) {
					ListaAsistente listaEC= new ListaAsistente();
					TblEventosCuidadores EvenC= listaEventosCuidador.get(i);				
					
					TblActividades laActividad = Select.from(TblActividades.class)
							.where(Condition.prop("id_actividad").eq(EvenC.getIdActividad()),
								   Condition.prop("Eliminado").eq(0)).first();

					listaEC.setColor("anaranjado1");
					listaEC.setActividad(getString(R.string.MiEvento));
					listaEC.setDetalle(laActividad.getNombreActividad());
					Date horaMostrar=sdfTime.parse(EvenC.getHoraE()+":"+EvenC.getMinutosE());								
					listaEC.setHora(horaMostrar);
					listaAsistente.add(listaEC);
				}
			}catch (Exception e) {
				// TODO: handle exception
			}		
		}
    	
		
		//BUSCAR LAS RUTINAS DEL CUIDADOR DEL DIA
		List<TblRutinasCuidadores> listarRutinaCuidador = Select.from(TblRutinasCuidadores.class)
				.where(Condition.prop("id_cuidador").eq(idCuidador),
					   Condition.prop(DiaDeSemana).eq(1),
					   Condition.prop("Alarma").eq(1),
					   Condition.prop("Eliminado").eq(0)).list();
    	
		if (!listarRutinaCuidador.isEmpty()) {	
			try {
				//AGREGAR  LA LISTA DE LAS RUTINAS DEL CUIDADOR DEL DIA
				for (int i = 0; i < listarRutinaCuidador.size(); i++) {
					ListaAsistente listaRC= new ListaAsistente();
					TblRutinasCuidadores rutCui= listarRutinaCuidador.get(i);
					
					TblActividades laActividad = Select.from(TblActividades.class)
							.where(Condition.prop("id_actividad").eq(rutCui.getIdActividad()),
								   Condition.prop("Eliminado").eq(0)).first();
									
					listaRC.setColor("anaranjado2");
					listaRC.setActividad(getString(R.string.MiRutina));
					listaRC.setDetalle(laActividad.getNombreActividad());
					Date horaMostrar=sdfTime.parse(rutCui.getHora()+":"+rutCui.getMinutos());									
					listaRC.setHora(horaMostrar);
					listaAsistente.add(listaRC);
				}				
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		
		//BUSCAR LOS PACIENTES QUE EL CUIDADOR TIENE A SU CARGO
		List<TblPermisos> listarPermisos = Select.from(TblPermisos.class)
				.where(Condition.prop("id_cuidador").eq(idCuidador),
					   Condition.prop("notifi_alarma").eq(1),
					   Condition.prop("Eliminado").eq(0)).list();
		
		if (!listarPermisos.isEmpty()) {			
			for (int i = 0; i < listarPermisos.size(); i++) {				
				long idPaciente = listarPermisos.get(i).getIdPaciente();
				
				//BUSCAR LOS EVENTOS DEL DIA DEL PACIENTE A SU CARGO
				List<TblEventosPacientes> listaEventosPacientes = Select.from(TblEventosPacientes.class)
						.where(Condition.prop("id_paciente").eq(idPaciente),
							   Condition.prop("anio_e").eq(anio),
							   Condition.prop("mes_e").eq(mes),
							   Condition.prop("dia_e").eq(dia),
							   Condition.prop("Alarma").eq(1),
							   Condition.prop("Eliminado").eq(0)).list();
				
				TblPacientes paciente = Select.from(TblPacientes.class)
						.where(Condition.prop("id_paciente").eq(idPaciente),
							   Condition.prop("Eliminado").eq(0)).first();
		    	
				if (!listaEventosPacientes.isEmpty()) {	
					try {
						//AGREGAR A LA LISTA LOS EVENTOS DEL DIA DEL PACIENTE A SU CARGO
						for (int j = 0; j < listaEventosPacientes.size(); j++) {
							ListaAsistente listaEP= new ListaAsistente();
							TblEventosPacientes EvenP= listaEventosPacientes.get(j);
													
							TblActividades laActividad = Select.from(TblActividades.class)
									.where(Condition.prop("id_actividad").eq(EvenP.getIdActividad()),
										   Condition.prop("Eliminado").eq(0)).first();
													
							listaEP.setColor("verde1");
							listaEP.setActividad(getString(R.string.Evento)+": "+paciente.getNombreApellidoP());
							listaEP.setDetalle(laActividad.getNombreActividad());
							Date horaMostrar=sdfTime.parse(EvenP.getHoraE()+":"+EvenP.getMinutosE());
							listaEP.setHora(horaMostrar);
							listaAsistente.add(listaEP);
						}						
					}catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
			
			
			for (int i = 0; i < listarPermisos.size(); i++) {				
				long idPaciente = listarPermisos.get(i).getIdPaciente();
				
				//BUSCAR LAS RUTINAS DEL DIA DEL PACIENTE A SU CARGO
				List<TblRutinasPacientes> listarRutinaPacientes = Select.from(TblRutinasPacientes.class)
						.where(Condition.prop("id_paciente").eq(idPaciente),
							   Condition.prop(DiaDeSemana).eq(1),
							   Condition.prop("Alarma").eq(1),
							   Condition.prop("Eliminado").eq(0)).list();
		    	
				TblPacientes paciente = Select.from(TblPacientes.class)
						.where(Condition.prop("id_paciente").eq(idPaciente),
							   Condition.prop("Eliminado").eq(0)).first();
				
				if (!listarRutinaPacientes.isEmpty()) {	
					try {
						//AGREGAR  LA LISTA DE LAS RUTINAS DEL CUIDADOR DEL DIA
						for (int j = 0; j < listarRutinaPacientes.size(); j++) {
							ListaAsistente listaRP= new ListaAsistente();
							TblRutinasPacientes rutPac= listarRutinaPacientes.get(j);
							
							TblActividades laActividad = Select.from(TblActividades.class)
									.where(Condition.prop("id_actividad").eq(rutPac.getIdActividad()),
										   Condition.prop("Eliminado").eq(0)).first();
							
							listaRP.setColor("verde2");
							listaRP.setActividad(getString(R.string.Rutina)+": "+paciente.getNombreApellidoP());
							listaRP.setDetalle(laActividad.getNombreActividad());							
							Date horaMostrar=sdfTime.parse(rutPac.getHora()+":"+rutPac.getMinutos());
							listaRP.setHora(horaMostrar);
							listaAsistente.add(listaRP);
						}						
					}catch (Exception e) {
						// TODO: handle exception
					}
				}				
			}
		}
		
		
		//BUSCAR LOS PACIENTES QUE EL CUIDADOR TIENE QUE CONTROLARLE LA MEDICINA
		List<TblPermisos> listarPermisos1 = Select.from(TblPermisos.class)
				.where(Condition.prop("id_cuidador").eq(idCuidador),
					   Condition.prop("cont_medicina").eq(1),
					   Condition.prop("Eliminado").eq(0)).list();
		
		for (int i = 0; i < listarPermisos1.size(); i++) {			
			long idPaciente = listarPermisos1.get(i).getIdPaciente();
			
			//BUSCAR LOS EVENTOS DEL DIA DEL PACIENTE A SU CARGO
			List<TblControlMedicina> listaContMed = Select.from(TblControlMedicina.class)
					.where(Condition.prop("id_paciente").eq(idPaciente),
						   Condition.prop("Eliminado").eq(0)).list();
			
			TblPacientes elPaciente = Select.from(TblPacientes.class)
					.where(Condition.prop("id_paciente").eq(idPaciente),
						   Condition.prop("Eliminado").eq(0)).first();
	    	
			if (!listaContMed.isEmpty()) {				
				//AGREGAR A LA LISTA LOS EVENTOS DEL DIA DEL PACIENTE A SU CARGO
				for (int j = 0; j < listaContMed.size(); j++) {					
					TblControlMedicina contMed= new TblControlMedicina();
					contMed=listaContMed.get(j);
					
					//BUSCAR LOS EVENTOS DEL DIA DEL PACIENTE A SU CARGO
					List<TblHorarioMedicina> listaHorMed = Select.from(TblHorarioMedicina.class)
							.where(Condition.prop("id_control_medicina").eq(contMed.getIdControlMedicina()),
								   Condition.prop(DiaDeSemana).eq(1),
								   Condition.prop("act_des_alarma").eq(1),
								   Condition.prop("Eliminado").eq(0)).list();
					
					if (!listaHorMed.isEmpty()) {
						try {
							//AGREGAR A LA LISTA LAS MEDICINAS A DAR A LOS PACIENTES
							for (int k = 0; k < listaHorMed.size(); k++) {							
								ListaAsistente listaMed= new ListaAsistente();
								TblHorarioMedicina horMed= listaHorMed.get(k);
								listaMed.setColor("morado1");
								listaMed.setActividad(getString(R.string.Medicina)+": "+elPaciente.getNombreApellidoP());
								listaMed.setDetalle(contMed.getMedicamento());								
								Date horaMostrar=sdfTime.parse(horMed.getHora()+":"+horMed.getMinutos());
								listaMed.setHora(horaMostrar);
								listaAsistente.add(listaMed);							
							}							
						}catch (Exception e) {
							// TODO: handle exception
						}
					}
				}
			}
		}		
		
		Collections.sort(listaAsistente, new Comparator<ListaAsistente>(){
			@Override
			public int compare(ListaAsistente d1, ListaAsistente d2) {							
				return d1.getHora().toString().compareToIgnoreCase(d2.getHora().toString());
			}
        });

		return listaAsistente;
	}
		
	
	public String BuscarDiaSemana(Date fecha)
    {
    	String diaSemana="";
    	Calendar cal = new GregorianCalendar();
    	cal.setTime(fecha);
    	
    	int dia = cal.get(Calendar.DAY_OF_WEEK);
    	
    	switch (dia) {
			case 1:diaSemana="Domingo"; break;
			case 2:diaSemana="Lunes"; break;
			case 3:diaSemana="Martes"; break;
			case 4:diaSemana="Miercoles"; break;
			case 5:diaSemana="Jueves"; break;
			case 6:diaSemana="Viernes"; break;
			case 7:diaSemana="Sabado"; break;			
			default:diaSemana=""; break;
		}    	
    	return diaSemana;
    }
		
	
	public String DevolverHorarioHoy(Long idC, String dia)
	{
		String horario="";
		
		List<TblHorarios> losHorarios = Select.from(TblHorarios.class)
				.where(Condition.prop("id_cuidador").eq(idC),
					   Condition.prop(dia).eq(1),
					   Condition.prop("Eliminado").eq(0)).list();
		
		if (!losHorarios.isEmpty()) {
			for (int i = 0; i < losHorarios.size(); i++) {
				TblHorarios elHT = new TblHorarios();
				elHT = losHorarios.get(i);
				
				horario+= String.valueOf(elHT.getHoraIni())+":"+String.valueOf(elHT.getMinutosIni())+" - "+
						  String.valueOf(elHT.getHoraFin())+":"+String.valueOf(elHT.getMinutosFin())+"\n";				
			}			
		}		
		return horario;
	} 	


}