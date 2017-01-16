package com.Notifications.patientssassistant.fragments;


import com.Notifications.patientssassistant.*;
import com.Notifications.patientssassistant.adapters.*;
import com.Notifications.patientssassistant.dialogos.*;
import com.Notifications.patientssassistant.tables.*;
import com.orm.query.Condition;
import com.orm.query.Select;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;


public class MpFamiliaresF extends Fragment {
	
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
	private List<TblPermisos> lista_pacientes;	
    private	TblFamiliaresPacientes titulo;
    			
	//VARIABLES DE LOS ELEMENTOS DE LA IU
	Spinner CmbPacientes;
	ListView ListFamiliares;
	ImageView ImageFondo;
	TextView TxtNoHayDatos;
		
	
	public interface FragmentIterationListener{
        public void onFragmentIteration(Bundle parameters);
    }
		
	public MpFamiliaresF() {super();}

	public static MpFamiliaresF newInstance(Long c_idCuidador, String c_tipoCuidador, Boolean c_controlT, Long c_dependeDe) {
		MpFamiliaresF frag = new MpFamiliaresF();
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
	
	public void RecogerParametrosActMP4() {	
		vIdCuidador = getArguments().getLong(KEY_REG_TEXT1);
		vTipoCuidador = getArguments().getString(KEY_REG_TEXT2);
		vControlT = getArguments().getBoolean(KEY_REG_TEXT3);
		vDependeDe = getArguments().getLong(KEY_REG_TEXT4);
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
        				
        View rootView = inflater.inflate(R.layout.mp_familiares, container, false);
        if(rootView != null){
        	CmbPacientes = (Spinner)rootView.findViewById(R.id.cmbPacientes);
        	ListFamiliares = (ListView)rootView.findViewById(android.R.id.list);
        	ImageFondo=(ImageView)rootView.findViewById(R.id.imageView1);
        	TxtNoHayDatos = (TextView)rootView.findViewById(R.id.txtNoHayDatos);
	    }
        return rootView;
    }		
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {		
		super.onViewCreated(view, savedInstanceState);
		
		RecogerParametrosActMP4();			
		CargarSpinnerPacientes();
		registerForContextMenu(ListFamiliares);
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
	
	public void CargarSpinnerPacientes() {
		//ES CUIDADOR PRIMARIO O SECUNDARIO CON PERMISOS
		if (vTipoCuidador.equals("CP") || (vTipoCuidador.equals("CS")&&vControlT.equals(true))) {
			lista_pacientes = Select.from(TblPermisos.class).where(Condition.prop("id_cuidador").eq(vDependeDe),
					  											   Condition.prop("Eliminado").eq(0)).list();			
		}
		//ES CUIDADOR SECUNDARIO SIN PERMISOS
		if (vTipoCuidador.equals("CS") && vControlT.equals(false)) {
			lista_pacientes = Select.from(TblPermisos.class).where(Condition.prop("id_cuidador").eq(vIdCuidador),
																   Condition.prop("Eliminado").eq(0)).list();			
		}
		ListaLLenaVacia();				
	}
	
	public void ListaLLenaVacia() {
		//LISTA CON DATOS
		if (!lista_pacientes.isEmpty()) {	   		
	   		AdapterSpinnerPacientes miAdapter=new AdapterSpinnerPacientes(getActivity(), lista_pacientes);
	   		CmbPacientes.setAdapter(miAdapter);
	   		
	   		Collections.sort(lista_pacientes, new Comparator<TblPermisos>(){  //ORDENA LA LISTA
				@Override
				public int compare(TblPermisos d1, TblPermisos d2) {	
					TblPacientes paciente_clase1=Select.from(TblPacientes.class).where(Condition.prop("id_paciente").eq(d1.getIdPaciente())).first();
					TblPacientes paciente_clase2=Select.from(TblPacientes.class).where(Condition.prop("id_paciente").eq(d2.getIdPaciente())).first();
					return paciente_clase1.getNombreApellidoP().compareToIgnoreCase(paciente_clase2.getNombreApellidoP());				
				}
	        });	
	   		
	   		CargarListaContactosFamiliares();
		}
		//LISTA VACIA
		if (lista_pacientes.isEmpty()) {					
			String[] ListVacia={getResources().getString(R.string.NoHayPacientes)};
			CmbPacientes.setAdapter(new AdapterSpinnerSimple(getActivity(), R.layout.adaptador_spinner, ListVacia));
			CmbPacientes.setClickable(false);
			CmbPacientes.setEnabled(false);
		}
	}
	
	public void CargarListaContactosFamiliares() {
		CmbPacientes.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {			
				//TOMAMOS EL ID_PACIENTE 	    		
				TblPermisos el_paciente=(TblPermisos)parent.getItemAtPosition(position);				
				List<TblFamiliaresPacientes> lista_fp= Select.from(TblFamiliaresPacientes.class).where(Condition.prop("id_paciente").eq(el_paciente.getIdPaciente()),
																									   Condition.prop("Eliminado").eq(0)).orderBy("nombre_contacto asc").list();
				//LISTA CON DATOS
				if (!lista_fp.isEmpty()) {
					AdapterListaFamilia miAdapter2=new AdapterListaFamilia(getActivity(), lista_fp);
					ListFamiliares.setAdapter(miAdapter2);
					TxtNoHayDatos.setText("");
					ImageFondo.setImageDrawable(null);
				}
				//LISTA VACIA
				if (lista_fp.isEmpty()) {
					ListFamiliares.setAdapter(null);
					TxtNoHayDatos.setText(R.string.NoHayFamiliares);
					ImageFondo.setImageDrawable(getResources().getDrawable(R.drawable.familia2));
				}																			
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> parent) { }			
		});
	}
	
	public void Llamada(String telefono) {
	    Uri numero = Uri.parse("tel:"+telefono);
	    Intent intent = new Intent(Intent.ACTION_CALL, numero);
	    startActivity(intent);
	}
	
	public void Sms(String destinatario, String celular) {
		Intent intent = new Intent(getActivity(), EnviarSmsActivity.class);
		intent.putExtra("Destinatario", destinatario);
        intent.putExtra("Celular", celular);
        startActivity(intent);
	}

	public void Reporte(Long idFamiliar, Long idPaciente, Long idCuidador) { //añade el ultimo parametro
		Intent intent = new Intent(getActivity(), EnviarReporteActivity.class);
		intent.putExtra("IdFamiliar", idFamiliar);
        intent.putExtra("IdPaciente", idPaciente);
        intent.putExtra("IdCuidador", idCuidador);//this too
        startActivity(intent);
	}	
	
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {    	
    	if (v.getId() == android.R.id.list) {
    	    int position = ((AdapterContextMenuInfo) menuInfo).position;
    	    this.getActivity().getMenuInflater().inflate(R.menu.menu_contextual_fp, menu);
    	    titulo=(TblFamiliaresPacientes)ListFamiliares.getItemAtPosition(position);
    	    menu.setHeaderTitle(titulo.getNombreContacto());
    	    menu.setHeaderIcon(R.drawable.user_circle);  
    	}
    	super.onCreateContextMenu(menu, v, menuInfo);
    }
	
    public boolean onContextItemSelected(MenuItem item) { 
    	switch(item.getItemId()) {
    	case R.id.Celular:  
    		if (!titulo.getCelular().equals("")) {
				Llamada(titulo.getCelular().toString());
			}else {
				FragmentManager fragmentManager1 = getFragmentManager();
				DFNoCelular dialogo1 = new DFNoCelular();
			    dialogo1.show(fragmentManager1, "tagConfirmacion");					
			}
            return true;    	
    	case R.id.Convencional:
    		if (!titulo.getTelefono().equals("")) {
				Llamada(titulo.getTelefono().toString());
			}else {
				FragmentManager fragmentManager2 = getFragmentManager();
				DFNoConvencional dialogo2 = new DFNoConvencional();
			    dialogo2.show(fragmentManager2, "tagConfirmacion");					
			}			
    		return true;   
    	case R.id.Sms:
    		if (!titulo.getCelular().equals("")) {
    			Sms(titulo.getNombreContacto(), titulo.getCelular());
			}else {
				FragmentManager fragmentManager3 = getFragmentManager();
				DFNoCelular dialogo3 = new DFNoCelular();
			    dialogo3.show(fragmentManager3, "tagConfirmacion");					
			}
    		return true;  
    	case R.id.Reporte:
    		if (!titulo.getEmail().equals("")) {
				Reporte(titulo.getIdFamiliarPaciente(), titulo.getIdPaciente(), vIdCuidador); //añade el ultimo parametro
			}else {
				FragmentManager fragmentManager4 = getFragmentManager();
				DFNoEmail dialogo4 = new DFNoEmail();
			    dialogo4.show(fragmentManager4, "tagConfirmacion");					
			}   			
    		return true;      		
    	default:
    		return super.onContextItemSelected(item);
    	}
    }

	
}