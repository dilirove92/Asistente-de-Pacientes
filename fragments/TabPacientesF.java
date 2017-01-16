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


public class TabPacientesF extends Fragment {
	
	public final static String KEY_REG_TEXT1 = "IdPaciente";
	public final static String KEY_REG_TEXT2 = "ControlT";	
	private static Long vItemIdPaciente;
	private static Boolean vControlT;
		
	//VARIABLES AUXILIARES
	private FragmentIterationListener mCallback = null;
	private AdapterTabPacientes tAdapter;
	
	//VARIABLES DE LOS ELEMENTOS DE LA IU
	ViewPager vPager;	
			
	
	public interface FragmentIterationListener{
        public void onFragmentIteration(Bundle parameters);
    }
	
	public TabPacientesF() {super();}
	
	public static TabPacientesF newInstance(Long c_idPaciente, Boolean c_controlT) {
		TabPacientesF frag = new TabPacientesF();
		Bundle args = frag.getArguments();
		if (args == null)
			args = new Bundle();
		args.putLong(KEY_REG_TEXT1, c_idPaciente);
		args.putBoolean(KEY_REG_TEXT2, c_controlT);
		frag.setArguments(args);
		return frag;
	}
	
	public void RecogerParametrosFragMenuPac1() {	
		vItemIdPaciente = getArguments().getLong(KEY_REG_TEXT1);
		vControlT = getArguments().getBoolean(KEY_REG_TEXT2);
	}
	
	public Fragment EnviarParametrosFragTabPac1() {
		return TabPaciente1DpF.newInstance(vItemIdPaciente, vControlT);
	}
	
	public Fragment EnviarParametrosFragTabPac2() {
		return TabPaciente2EsF.newInstance(vItemIdPaciente, vControlT);
	}
	
	public Fragment EnviarParametrosFragTabPac3() {
		return TabPaciente3DfF.newInstance(vItemIdPaciente, vControlT);
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
		
		RecogerParametrosFragMenuPac1();
		
		tAdapter = new AdapterTabPacientes(getChildFragmentManager(), getActivity());				
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