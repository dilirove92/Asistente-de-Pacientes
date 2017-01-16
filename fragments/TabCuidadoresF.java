package com.Notifications.patientssassistant.fragments;


import com.Notifications.patientssassistant.R;
import com.Notifications.patientssassistant.adapters.*;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class TabCuidadoresF extends Fragment {
	
	public final static String KEY_REG_TEXT1 = "ItemIdCuidador";
	public final static String KEY_REG_TEXT2 = "IdCuidador";
	public final static String KEY_REG_TEXT3 = "DependeDe";
	public final static String KEY_REG_TEXT4 = "ControlT";
	private static Long vItemIdCuidador;
	private static Long vIdCuidador;
	private static Long vDependeDe;
	private static Boolean vControlT;
	private static String vTipoCuidador="CS";
				
	//VARIABLES AUXILIARES
	private FragmentIterationListener mCallback = null;
	private AdapterTabCuidadores tAdapter;
	
	//VARIABLES DE LOS ELEMENTOS DE LA IU
	ViewPager vPager;	
		
	
	public interface FragmentIterationListener{
        public void onFragmentIteration(Bundle parameters);
    }
	
	public TabCuidadoresF() {super();}
	
	public static TabCuidadoresF newInstance(Long c_itemIdCuidador, Long c_idCuidador, Long c_dependeDe, Boolean c_controlT) {
		TabCuidadoresF frag = new TabCuidadoresF();
		Bundle args = frag.getArguments();
		if (args == null)
			args = new Bundle();
		args.putLong(KEY_REG_TEXT1, c_itemIdCuidador);
		args.putLong(KEY_REG_TEXT2, c_idCuidador);
		args.putLong(KEY_REG_TEXT3, c_dependeDe);
		args.putBoolean(KEY_REG_TEXT4, c_controlT);
		frag.setArguments(args);
		return frag;
	}
	
	public void RecogerParametrosFragMpC() {	
		vItemIdCuidador = getArguments().getLong(KEY_REG_TEXT1);
		vIdCuidador = getArguments().getLong(KEY_REG_TEXT2);
		vDependeDe = getArguments().getLong(KEY_REG_TEXT3);	
		vControlT = getArguments().getBoolean(KEY_REG_TEXT4);
	}
	
	public Fragment EnviarParametrosFragTabCui1() {		
		return TabCuidador1DpF.newInstance(vItemIdCuidador, vIdCuidador, vTipoCuidador, vControlT);
	}
	
	public Fragment EnviarParametrosFragTabCui2() {
		return TabCuidador2HtF.newInstance(vItemIdCuidador, vControlT);
	}
	
	public Fragment EnviarParametrosFragTabCui3() {  
		return TabCuidador3Pp2F.newInstance(vItemIdCuidador, vIdCuidador, vDependeDe, vControlT);
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
		
		RecogerParametrosFragMpC();
		
		tAdapter = new AdapterTabCuidadores(getChildFragmentManager(), getActivity());			
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