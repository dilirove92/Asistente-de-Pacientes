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


public class MpTabMiAgendaF extends Fragment {
	
	public final static String KEY_REG_TEXT = "IdCuidador";	
	private static Long vIdCuidador;
	
	//VARIABLES AUXILIARES
	private FragmentIterationListener mCallback = null;
	private AdapterTabMiAgenda tAdapter;
		
	//VARIABLES DE LOS ELEMENTOS DE LA IU
	ViewPager vPager;	
	
	
	public interface FragmentIterationListener{
        public void onFragmentIteration(Bundle parameters);
    }
	
	public MpTabMiAgendaF() {super();}
	
	public static MpTabMiAgendaF newInstance(Long c_idCuidador) {
		MpTabMiAgendaF frag = new MpTabMiAgendaF();
		Bundle args = frag.getArguments();
		if (args == null)
			args = new Bundle();
		args.putLong(KEY_REG_TEXT, c_idCuidador);
		frag.setArguments(args);
		return frag;
	}
	
	public void RecogerParametrosActMP5() {	
		vIdCuidador = getArguments().getLong(KEY_REG_TEXT);		
	}
	
	public Fragment EnviarParametrosFragTabMiAge1() {
		return TabMA1EventosF.newInstance(vIdCuidador);
	}
	
	public Fragment EnviarParametrosFragTabMiAge2() {
		return TabMA2RutinasF.newInstance(vIdCuidador);
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
		
		RecogerParametrosActMP5();
		
		tAdapter = new AdapterTabMiAgenda(getChildFragmentManager(), getActivity());	
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