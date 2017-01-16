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


public class MpTabMiPerfilF extends Fragment {
	
	public final static String KEY_REG_TEXT1 = "IdCuidador";
	public final static String KEY_REG_TEXT2 = "TipoCuidador";
	public final static String KEY_REG_TEXT3 = "ControlT";
	public final static String KEY_REG_TEXT4 = "DependeDe";
	private static Long vIdCuidador;
	private static String vTipoCuidador;
	private static Boolean vControlT;
	private static Long vDependeDe;
	
	//VARIABLES AUXILIARES
	private FragmentIterationListener mCallback = null;
	private AdapterTabMiPerfil tAdapter;	
	
	//VARIABLES DE LOS ELEMENTOS DE LA IU
	ViewPager vPager;
		
	
	public interface FragmentIterationListener{
        public void onFragmentIteration(Bundle parameters);
    }
	
	public MpTabMiPerfilF() {super();}
	
	public static MpTabMiPerfilF newInstance(Long c_idCuidador, String c_tipoCuidador, Boolean c_controlT, Long c_dependeDe) {
		MpTabMiPerfilF frag = new MpTabMiPerfilF();
		Bundle args = frag.getArguments();
		if (args == null)
			args = new Bundle();
		args.putLong(KEY_REG_TEXT1, c_idCuidador);
		args.putString(KEY_REG_TEXT2, c_tipoCuidador);
		args.putBoolean(KEY_REG_TEXT3, c_controlT);
		args.putLong(KEY_REG_TEXT4, c_dependeDe);
		frag.setArguments(args);
		return frag;
	}
	
	public void RecogerParametrosActMP6() {	
		vIdCuidador = getArguments().getLong(KEY_REG_TEXT1);		
		vTipoCuidador = getArguments().getString(KEY_REG_TEXT2);
		vControlT = getArguments().getBoolean(KEY_REG_TEXT3);
		vDependeDe = getArguments().getLong(KEY_REG_TEXT4);
	}
	
	public Fragment EnviarParametrosFragMpMiPefil1() {
		return TabCuidador1DpF.newInstance(vIdCuidador, vIdCuidador, vTipoCuidador, vControlT);
	}
	
	public Fragment EnviarParametrosFragMpMiPefil2() {
		return TabCuidador2HtF.newInstance(vIdCuidador, vControlT);
	}
	
	public Fragment EnviarParametrosFragMpMiPefil3() {
		Fragment TabPP = new Fragment();
		if (vTipoCuidador.equals("CP")) {
			 TabPP = TabCuidador3Pp1F.newInstance(vIdCuidador);
		}
		if (vTipoCuidador.equals("CS")) {
			TabPP = TabCuidador3Pp2F.newInstance(vIdCuidador, vIdCuidador, vDependeDe, vControlT);
		}
		return TabPP;
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
		
		RecogerParametrosActMP6();		
		
		tAdapter = new AdapterTabMiPerfil(getChildFragmentManager(), getActivity());
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