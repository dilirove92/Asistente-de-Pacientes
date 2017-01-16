package com.Notifications.patientssassistant.fragments;


import com.Notifications.patientssassistant.*;
import com.Notifications.patientssassistant.adapters.*;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class TabAgendaPacientesF extends Fragment {
	
	public final static String KEY_REG_TEXT1 = "IdCuidador";
	public final static String KEY_REG_TEXT2 = "IdPaciente";
	public final static String KEY_REG_TEXT3 = "ControlT";
	public final static String KEY_REG_TEXT4 = "NotiAlar";
	private static Long vIdCuidador;
	private static Long vItemIdPaciente;
	private static Boolean vControlT;
	private static Boolean vNotiAlar;
		
	//VARIABLES AUXILIARES
	private FragmentIterationListener mCallback = null;
	private AdapterTabAgendaPaciente tAdapter;
	
	//VARIABLES DE LOS ELEMENTOS DE LA IU
	ViewPager vPager;	
			
	
	public interface FragmentIterationListener{
        public void onFragmentIteration(Bundle parameters);
    }
	
	public TabAgendaPacientesF() {super();}
	
	public static TabAgendaPacientesF newInstance(Long c_idCuidador, Long c_idPaciente, Boolean c_controlT, Boolean c_notiAlar) {
		TabAgendaPacientesF frag = new TabAgendaPacientesF();
		Bundle args = frag.getArguments();
		if (args == null)
			args = new Bundle();
		args.putLong(KEY_REG_TEXT1, c_idCuidador);
		args.putLong(KEY_REG_TEXT2, c_idPaciente);
		args.putBoolean(KEY_REG_TEXT3, c_controlT);
		args.putBoolean(KEY_REG_TEXT4, c_notiAlar);
		frag.setArguments(args);
		return frag;
	}
	
	public void RecogerParametrosFragMenuPac5() {
		vIdCuidador = getArguments().getLong(KEY_REG_TEXT1);
		vItemIdPaciente = getArguments().getLong(KEY_REG_TEXT2);
		vControlT = getArguments().getBoolean(KEY_REG_TEXT3);
		vNotiAlar = getArguments().getBoolean(KEY_REG_TEXT4);
	}
	
	public Fragment EnviarParametrosFragTabAgePac1() {
		return TabAP1EventosPacF.newInstance(vIdCuidador, vItemIdPaciente, vControlT, vNotiAlar);
	}
	
	public Fragment EnviarParametrosFragTabAgePac2() {
		return TabAP2RutinasPacF.newInstance(vIdCuidador, vItemIdPaciente, vControlT, vNotiAlar);
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
        				
        View rootView = inflater.inflate(R.layout.adaptador_cargar_fragmentos, container, false);
        if(rootView != null){			
			vPager = (ViewPager)rootView.findViewById(R.id.pager);			
	    }
        return rootView;
    }		
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {		
		super.onViewCreated(view, savedInstanceState);	
		
		RecogerParametrosFragMenuPac5();
		
		tAdapter = new AdapterTabAgendaPaciente(getChildFragmentManager(), getActivity());		
		vPager.setAdapter(tAdapter);		
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
	
	
}