package com.Notifications.patientssassistant.fragments;


import com.Notifications.patientssassistant.*;
import com.Notifications.patientssassistant.tables.TblControlMedicina;
import com.Notifications.patientssassistant.tables.TblPermisos;
import com.orm.query.Condition;
import com.orm.query.Select;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TableRow;


public class MenuPacientesF extends Fragment {
	
	public final static String KEY_REG_TEXT1 = "IdCuidador";
	public final static String KEY_REG_TEXT2 = "IdPaciente";
	public final static String KEY_REG_TEXT3 = "ControlT";
	private static Long vIdCuidador;
	private static Long vItemIdPaciente;
	private static Boolean vControlT;
	private static Boolean vNotiAlar;
	private static Boolean vMedicina;
		
	//VARIABLES AUXILIARES
	private FragmentIterationListener mCallback = null;				
	
	//VARIABLES DE LOS ELEMENTOS DE LA IU
	TableRow btnRegistros;
	TableRow btnSegMedico;
	TableRow btnMedicinas;
	TableRow btnDietas;
	TableRow btnAgenda;
	TableRow btnObservacion;
	TableRow btnPerPantalla;	
		
	
	public interface FragmentIterationListener{
        public void onFragmentIteration(Bundle parameters);
    }
		
	public MenuPacientesF() {super();}

	public static MenuPacientesF newInstance(Long c_idCuidador, Long c_idPaciente, Boolean c_controlT) {
		MenuPacientesF frag = new MenuPacientesF();
		Bundle args = frag.getArguments();
		if (args == null)
			args = new Bundle();
		args.putLong(KEY_REG_TEXT1, c_idCuidador);
		args.putLong(KEY_REG_TEXT2, c_idPaciente);
		args.putBoolean(KEY_REG_TEXT3, c_controlT);
		frag.setArguments(args);
		return frag;
	}
	
	public void RecogerParametrosFragMpP() {
		vIdCuidador = getArguments().getLong(KEY_REG_TEXT1);
		vItemIdPaciente = getArguments().getLong(KEY_REG_TEXT2);
		vControlT = getArguments().getBoolean(KEY_REG_TEXT3);
		vNotiAlar=false;
		vMedicina=false;

		TblPermisos permisos = Select.from(TblPermisos.class).where(Condition.prop("id_cuidador").eq(vIdCuidador),
							   									Condition.prop("id_paciente").eq(vItemIdPaciente),
																Condition.prop("Eliminado").eq(0)).first();
		if (permisos!=null){
			vNotiAlar = permisos.getNotifiAlarma();
			vMedicina = permisos.getContMedicina();
		}
	}

	public void EnviarParametrosFragMenuPac1() { 
        TabPacientesF fragment = TabPacientesF.newInstance(vItemIdPaciente, vControlT);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment, "TabPacientesF");
        ft.addToBackStack(null);
        ft.commit();
	}
	
	public void EnviarParametrosFragMenuPac2() { 
		MenPacSeguimientoMedicoF fragment = MenPacSeguimientoMedicoF.newInstance(vItemIdPaciente, vControlT);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment, "MenPacSeguimientoMedicoF");
        ft.addToBackStack(null);
        ft.commit();
	}
	
	public void EnviarParametrosFragMenuPac3() {
		MenPacControlMedicinasF fragment = MenPacControlMedicinasF.newInstance(vIdCuidador, vItemIdPaciente, vControlT, vMedicina);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment, "MenPacControlMedicinasF");
        ft.addToBackStack(null);
        ft.commit();
	}
	
	public void EnviarParametrosFragMenuPac4() { 
		MenPacControlDietaF fragment = MenPacControlDietaF.newInstance(vItemIdPaciente, vControlT);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment, "MenPacControlDietaF");
        ft.addToBackStack(null);
        ft.commit();
	}
	
	public void EnviarParametrosFragMenuPac5() { 
		TabAgendaPacientesF fragment = TabAgendaPacientesF.newInstance(vIdCuidador, vItemIdPaciente, vControlT, vNotiAlar);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment, "TabAgendaPacientesF");
        ft.addToBackStack(null);
        ft.commit();
	}
	
	public void EnviarParametrosFragMenuPac6() { 
		MenPacObservacionesF fragment = MenPacObservacionesF.newInstance(vItemIdPaciente, vControlT);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment, "MenPacObservacionesF");
        ft.addToBackStack(null);
        ft.commit();
	}
	
	public void EnviarParametrosFragMenuPac7() { 
		MenPacPersonalizarPantF fragment = MenPacPersonalizarPantF.newInstance(vItemIdPaciente, vControlT);
	    FragmentTransaction ft = getFragmentManager().beginTransaction();
	    ft.replace(R.id.content_frame, fragment, "MenPacPersonalizarPantF");
	    ft.addToBackStack(null);
	    ft.commit();
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	    try{
	        mCallback = (FragmentIterationListener)activity;
	    }catch(Exception ex){
	        Log.e(String.valueOf(R.string.ExampleFragment), String.valueOf(R.string.FragmentoImplementarFIL));
	    }
	}	

	@Override
	public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
	}	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { 
		super.onCreateView(inflater, container, savedInstanceState); 
        				
        View rootView = inflater.inflate(R.layout.menu_pacientes, container, false);
        if(rootView != null){        	
        	btnRegistros = (TableRow)rootView.findViewById(R.id.btnRegistros);
        	btnSegMedico = (TableRow)rootView.findViewById(R.id.btnSegMedico);
        	btnMedicinas = (TableRow)rootView.findViewById(R.id.btnMedicinas);
        	btnDietas = (TableRow)rootView.findViewById(R.id.btnDietas);
        	btnAgenda = (TableRow)rootView.findViewById(R.id.btnAgenda);
        	btnObservacion = (TableRow)rootView.findViewById(R.id.btnObservacion);
        	btnPerPantalla = (TableRow)rootView.findViewById(R.id.btnPerPantalla);        	
	    }
        return rootView;
    }		
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {		
		super.onViewCreated(view, savedInstanceState);
		
		RecogerParametrosFragMpP();
		SeleccionarOpcion();
	}
	
	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {		
		super.onViewStateRestored(savedInstanceState);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {		
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onDetach() {
		mCallback = null;
	    super.onDetach();
	}
	
	public void SeleccionarOpcion() {
		//PANTALLA REGISTROS DEL PACIENTE
		btnRegistros.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
		    	EnviarParametrosFragMenuPac1();				
			}
		});
		
		//PANTALLA SEGUIMIENTO MEDICO	
		btnSegMedico.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {				
		    	EnviarParametrosFragMenuPac2();			
			}
		});	
		
		//PANTALLA CONTROL DE MEDICINAS
		btnMedicinas.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {				
		    	EnviarParametrosFragMenuPac3();			
			}
		});
		
		//PANTALLA CONTROL DE DIETAS
		btnDietas.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {				
		    	EnviarParametrosFragMenuPac4();			
			}
		});	
		
		//PANTALLA AGENDA DEL PACIENTE	
		btnAgenda.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {				
		    	EnviarParametrosFragMenuPac5();			
			}
		});	
		
		//PANTALLA OBSERVACIONES
		btnObservacion.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {				
		    	EnviarParametrosFragMenuPac6();			
			}
		});
		
		//PANTALLA PERSONALIZAR PANTALLAS
		btnPerPantalla.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {				
		    	EnviarParametrosFragMenuPac7();			
			}
		});
	}
	
    
}