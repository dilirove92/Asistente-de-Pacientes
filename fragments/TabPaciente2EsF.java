package com.Notifications.patientssassistant.fragments;


import com.Notifications.patientssassistant.*;
import com.Notifications.patientssassistant.dialogos.*;
import com.Notifications.patientssassistant.tables.*;
import com.orm.query.Condition;
import com.orm.query.Select;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class TabPaciente2EsF extends Fragment {
	
	public final static String KEY_REG_TEXT1 = "IdPaciente";
	public final static String KEY_REG_TEXT2 = "ControlT";	
	private static Long vItemIdPaciente;
	private static Boolean vControlT;
	private int request_code = 1;
	
	//VARIABLES AUXILIARES
	private FragmentIterationListener mCallback = null;
	private static Boolean banderaNuevo=false;
	private static Boolean banderaEditar=false;	
	private TblEstadoSalud estado_salud_pac;
	private Intent intent;
		
	//VARIABLES DE LOS ELEMENTOS DE LA IU
	TextView TxtTipoSangP;
	TextView TxtFacuMentP;
	TextView TxtEnfermedadesP;
	TextView TxtCirugiasP;
	TextView TxtTomaMediP;
	TextView TxtPadeDiscP;
	TextView TxtTipoDiscP;
	TextView TxtGradoDiscP;
	TextView TxtImplementosP;


	public interface FragmentIterationListener{
        public void onFragmentIteration(Bundle parameters);
    }
	
	public TabPaciente2EsF() {super();}
	
	public static TabPaciente2EsF newInstance(Long c_idPaciente, Boolean c_controlT) {
		TabPaciente2EsF frag = new TabPaciente2EsF();
		Bundle args = frag.getArguments();
		if (args == null)
			args = new Bundle();
		args.putLong(KEY_REG_TEXT1, c_idPaciente);
		args.putBoolean(KEY_REG_TEXT2, c_controlT);
		frag.setArguments(args);
		return frag;
	}
	
	public void RecogerParametrosFragTabPac2() {	
		vItemIdPaciente = getArguments().getLong(KEY_REG_TEXT1);
		vControlT = getArguments().getBoolean(KEY_REG_TEXT2);
	}
	
	public void EnviarParametrosFragTabPacEs() {
		TblPacientes datos_paciente= Select.from(TblPacientes.class).where(Condition.prop("id_paciente").eq(vItemIdPaciente)).first();
		intent.putExtra("varIdPaciente", vItemIdPaciente);
		intent.putExtra("varNombreP", datos_paciente.getNombreApellidoP());	
		intent.putExtra("varFotoP", datos_paciente.getFotoP());	
	}
	
	public void EnviarParametrosFragTabPacEs2() { 
		EnviarParametrosFragTabPacEs();			
		intent.putExtra("varTipoSangre", estado_salud_pac.getTipoSangre());
		intent.putExtra("varFacultadMental", estado_salud_pac.getFacultadMental());
		intent.putExtra("varEnfermedad", estado_salud_pac.getEnfermedad());		
		intent.putExtra("varDescEnfermedad", estado_salud_pac.getDescEnfermedad());
		intent.putExtra("varCirugias", estado_salud_pac.getCirugias()); 
		intent.putExtra("varDescCirugias", estado_salud_pac.getDescCirugias());
		intent.putExtra("varMedicamentos", estado_salud_pac.getMedicamentos());     		
		intent.putExtra("varDescMedicamentos", estado_salud_pac.getDescMedicamentos());		
		intent.putExtra("varDiscapacidad", estado_salud_pac.getDiscapacidad());		
		intent.putExtra("varTipoDiscapacidad", estado_salud_pac.getTipoDiscapacidad());
		intent.putExtra("varGradoDiscapacidad", estado_salud_pac.getGradoDiscapacidad());     		
		intent.putExtra("varImplementos", estado_salud_pac.getImplementos());	
	}
	
	//METODO QUE RETORNA "Nuevo" 
	public static Boolean EsNuevo() {
		Boolean resultado1=banderaNuevo;		
		return resultado1;
	}
	
	//METODO QUE RETORNA "Editar"
	public static Boolean EsEditar() {
		Boolean resultado2=banderaEditar;		
		return resultado2;
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
        				
        View rootView = inflater.inflate(R.layout.tab_paciente_es, container, false);
        if(rootView != null){
        	TxtTipoSangP=(TextView)rootView.findViewById(R.id.txtTipoSangP);
        	TxtFacuMentP=(TextView)rootView.findViewById(R.id.txtFacuMentP);
        	TxtEnfermedadesP=(TextView)rootView.findViewById(R.id.txtEnfermedadesP);
        	TxtCirugiasP=(TextView)rootView.findViewById(R.id.txtCirugiasP);
        	TxtTomaMediP=(TextView)rootView.findViewById(R.id.txtTomaMediP);
        	TxtPadeDiscP=(TextView)rootView.findViewById(R.id.txtPadeDiscP);
        	TxtTipoDiscP=(TextView)rootView.findViewById(R.id.txtTipoDiscP);
        	TxtGradoDiscP=(TextView)rootView.findViewById(R.id.txtGradoDiscP);
        	TxtImplementosP=(TextView)rootView.findViewById(R.id.txtImplementosP);        	
	    }
        return rootView;
    }		
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {		
		super.onViewCreated(view, savedInstanceState);
		
		RecogerParametrosFragTabPac2();
		CargarDatosEnElementos();
		ActDesElementos();
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

	public void ActDesElementos() {
		if (vControlT.equals(true)) { setHasOptionsMenu(true); }  //HABILITAR EL BOTON PARA EDITAR DATOS PERSONALES DEL PACIENTE
		if (vControlT.equals(false)) { setHasOptionsMenu(false); }  //DESHABILITAR EL BOTON PARA EDITAR DATOS PERSONALES DEL PACIENTE			
	}
	
	public void CargarDatosEnElementos() {
		//COMPROBAMOS SI YA EXISTE ALGUN REGISTRO SOBRE EL ESTADO DE SALUD DEL PACIENTE
		estado_salud_pac = Select.from(TblEstadoSalud.class).where(Condition.prop("id_paciente").eq(vItemIdPaciente),
																   Condition.prop("Eliminado").eq(0)).first();		
		if (estado_salud_pac!=null) {
			TxtTipoSangP.setText(estado_salud_pac.getTipoSangre());
	    	TxtFacuMentP.setText(estado_salud_pac.getFacultadMental());
	    	
			if (estado_salud_pac.getEnfermedad().equals(true)) {
				TxtEnfermedadesP.setText(getString(R.string.Si)+", "+estado_salud_pac.getDescEnfermedad());				
			}else{
				TxtEnfermedadesP.setText(R.string.No);			
			}
			
			if (estado_salud_pac.getCirugias().equals(true)) {
				TxtCirugiasP.setText(getString(R.string.Si)+", "+estado_salud_pac.getDescCirugias());				
			}else{
				TxtCirugiasP.setText(R.string.No);			
			}
			
			if (estado_salud_pac.getMedicamentos().equals(true)) {
				TxtTomaMediP.setText(getString(R.string.Si)+", "+estado_salud_pac.getDescMedicamentos());				
			}else{
				TxtTomaMediP.setText(R.string.No);			
			}
			
			if (estado_salud_pac.getDiscapacidad().equals(true)) {
				TxtPadeDiscP.setText(R.string.Si) ;
				TxtTipoDiscP.setText(estado_salud_pac.getTipoDiscapacidad());
				TxtGradoDiscP.setText(estado_salud_pac.getGradoDiscapacidad());
				TxtImplementosP.setText(estado_salud_pac.getImplementos());
			}else{
				TxtPadeDiscP.setText(R.string.No);			
				TxtTipoDiscP.setText(R.string.Ninguna);
				TxtGradoDiscP.setText(R.string.Ninguno);
				TxtImplementosP.setText(R.string.Ninguno);
			}			
		}
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		menu.findItem(R.id.opcIngresar).setVisible(true);
		menu.findItem(R.id.opcEditar).setVisible(true);
		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
        case R.id.opcIngresar:        							
			if (estado_salud_pac==null) {
	        	//INICIA LA ACTIVIDAD PARA INGRESAR DATOS
	        	intent = new Intent(getActivity(), NewEditEstadoSaludActivity.class);
	        	EnviarParametrosFragTabPacEs();    		     		
	        	startActivityForResult(intent, request_code);	
	            banderaNuevo=true;
			}else {		
				FragmentManager fragmentManager2 = getFragmentManager();
				DFSiEstadoSalud dialogo2 = new DFSiEstadoSalud();
		        dialogo2.show(fragmentManager2, "tagAlerta");
			}            
            return true;
        case R.id.opcEditar:					
			if (estado_salud_pac==null) {
				FragmentManager fragmentManager1 = getFragmentManager();
				DFNoEstadoSalud dialogo1 = new DFNoEstadoSalud();
		        dialogo1.show(fragmentManager1, "tagAlerta");
			}else {
	        	//INICIA LA ACTIVIDAD PARA EDITAR DATOS
	        	intent = new Intent(getActivity(), NewEditEstadoSaludActivity.class);
	        	EnviarParametrosFragTabPacEs2();    		     		
	        	startActivityForResult(intent, request_code);
	            banderaEditar=true;
			}
			return true;			
        default:
            return super.onOptionsItemSelected(item);
		}		
	}
    
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if ((requestCode == request_code) && (resultCode == Activity.RESULT_OK)){
			banderaNuevo=false;
			banderaEditar=false;
			tareaLarga();
			CargarDatosEnElementos();
		}
	}

	private void tareaLarga()
	{
		try {
			Thread.sleep(2000);
		} catch(InterruptedException e) {}
	}


}