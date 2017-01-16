package com.Notifications.patientssassistant.fragments;


import com.Notifications.patientssassistant.*;
import com.Notifications.patientssassistant.adapters.*;
import com.Notifications.patientssassistant.tables.*;
import com.Notifications.patientssassistant.asynctask.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.orm.query.Condition;
import com.orm.query.Select;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.annotation.SuppressLint;


public class MpPacientesF extends Fragment implements OnQueryTextListener {
	
	public final static String KEY_REG_TEXT1 = "IdCuidador";
	public final static String KEY_REG_TEXT2 = "TipoCuidador";
	public final static String KEY_REG_TEXT3 = "ControlT";
	public final static String KEY_REG_TEXT4 = "DependeDe";
	private static Long vIdCuidador;
	private static String vTipoCuidador;
	private static Boolean vControlT;
	private static Long vDependeDe;	
	private int request_code = 1;
	
	//VARIABLES AUXILIARES
	private FragmentIterationListener mCallback = null;
	private static Boolean banderaNuevo=false;
	private static Boolean banderaEditar=false;
	private AdapterListaP miAdapter;
	private List<TblPermisos> lista_pacientes = new ArrayList<TblPermisos>();
	private Long vItem_IdPaciente;	
	private TblPacientes datos;
	private Intent intent;
	private Filter filter;
	private String query="";
	
	//VARIABLES DE LOS ELEMENTOS DE LA IU
	ImageButton BtnAgregarP;
	SearchView BusquedaP;
	ListView LstPacientes;
	ImageView ImageFondo;
	TextView TxtNoHayDatos;
	
		
	public interface FragmentIterationListener{
        public void onFragmentIteration(Bundle parameters);
	}
		
	public MpPacientesF() { super(); }

	public static MpPacientesF newInstance(Long c_idCuidador, String c_tipoCuidador, Boolean c_controlT, Long c_dependeDe) {
		MpPacientesF frag = new MpPacientesF();
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
	
	public void RecogerParametrosActMP3() {	
		vIdCuidador = getArguments().getLong(KEY_REG_TEXT1);
		vTipoCuidador = getArguments().getString(KEY_REG_TEXT2);
		vControlT = getArguments().getBoolean(KEY_REG_TEXT3);
		vDependeDe = getArguments().getLong(KEY_REG_TEXT4);
	}
	
	public void EnviarParametrosFragMpP() { 
        MenuPacientesF fragment = MenuPacientesF.newInstance(vIdCuidador, vItem_IdPaciente, vControlT);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment, "MenuPacientesF");
        ft.addToBackStack(null);
        ft.commit();
	}
	
	public void EnviarParametrosFragMpP2() { 
        intent.putExtra("varDependeDe", vDependeDe);         
	}
	
	public void EnviarParametrosFragMpP3() { 
		intent.putExtra("varIdPaciente", datos.getIdPaciente());
		intent.putExtra("varUsuarioP", datos.getUsuarioP());
		intent.putExtra("varCiP", datos.getCiP());
		intent.putExtra("varNombreApellidoP", datos.getNombreApellidoP());
		intent.putExtra("varAnio", datos.getAnio());
		intent.putExtra("varMes", datos.getMes());
		intent.putExtra("varDia", datos.getDia());
		intent.putExtra("varEdad", datos.getEdad());
		intent.putExtra("varEstadoCivilP", datos.getEstadoCivilP()); 
		intent.putExtra("varNivelEstudioP", datos.getNivelEstudioP());
		intent.putExtra("varMotivoIngresoP", datos.getMotivoIngresoP());     		
		intent.putExtra("varTipoPacienteP", datos.getTipoPacienteP()); 
		intent.putExtra("varFotoP", datos.getFotoP());
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
        				
        View rootView = inflater.inflate(R.layout.mp_cuid_paci, container, false);
        if(rootView != null){
			LstPacientes = (ListView)rootView.findViewById(android.R.id.list);
			BusquedaP = (SearchView)rootView.findViewById(R.id.busquedaCoP);
			BtnAgregarP = (ImageButton)rootView.findViewById(R.id.btnAgregarCoP);
			ImageFondo=(ImageView)rootView.findViewById(R.id.imageView1);
			TxtNoHayDatos = (TextView)rootView.findViewById(R.id.txtNoHayDatos);
	    }
        return rootView;
    }		
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {		
		super.onViewCreated(view, savedInstanceState);
				
		RecogerParametrosActMP3();
		ListaPacientes tarea1 = new ListaPacientes();
        tarea1.execute();
		BusquedaP.setOnQueryTextListener(MpPacientesF.this);
		BusquedaP.setSubmitButtonEnabled(true);
		SeleccionarItemListaPacientes();
		BtnNuevoPaciente();
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

	@Override
	public boolean onQueryTextSubmit(String query) {		
		return false;
	}
	
	@Override
	public boolean onQueryTextChange(String newText) {	
		if (!lista_pacientes.isEmpty()) {
			filter.filter(newText);
			query=newText;
        }
		return true;
	}
	
	public void ActDesElementos() {
		//ACTIVAMOS EL MENU CONTEXTUAL PARA LA LISTA DE PACIENTES
		if (vControlT.equals(true)) {
			registerForContextMenu(LstPacientes);
		}
		//DESHABILITAR EL BOTON PARA AGREGAR NUEVO PACIENTE
		if (vControlT.equals(false)) {
			BtnAgregarP.setEnabled(false);
			BtnAgregarP.setImageResource(R.drawable.add_user2);
		}
	}

	public void CargarListaPacientes() {
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
		//LISTA CON DATOS
		if (!lista_pacientes.isEmpty()) {
			List<TblPacientes> lista_nombresP = new ArrayList<TblPacientes>();
			TblPermisos permiso_clase;
			lista_nombresP.clear();  //LIMPIAR PARA NO REPETIR LOS DATOS
	        
	        for (int i = 0; i < lista_pacientes.size(); i++) {
	        	permiso_clase = lista_pacientes.get(i);
	        	TblPacientes buscar_paciente= Select.from(TblPacientes.class).where(Condition.prop("id_paciente").eq(permiso_clase.getIdPaciente())).first();	        	
	        	lista_nombresP.add(new TblPacientes(buscar_paciente.getIdPaciente(), buscar_paciente.getUsuarioP(), buscar_paciente.getCiP(), buscar_paciente.getNombreApellidoP(), buscar_paciente.getAnio(), buscar_paciente.getMes(), buscar_paciente.getDia(), 
	        									    buscar_paciente.getEstadoCivilP(), buscar_paciente.getNivelEstudioP(), buscar_paciente.getMotivoIngresoP(), buscar_paciente.getTipoPacienteP(), buscar_paciente.getEdad(), buscar_paciente.getFotoP(), buscar_paciente.getEliminado()));
			}
					
			miAdapter=new AdapterListaP(getActivity(), lista_nombresP);
									
			Collections.sort(lista_nombresP, new Comparator<TblPacientes>() {  //ORDENA LA LISTA
				@Override
				public int compare(TblPacientes d1, TblPacientes d2) {
					return d1.getNombreApellidoP().compareToIgnoreCase(d2.getNombreApellidoP());
				}
			});
		}					
	}
		
	public void SeleccionarItemListaPacientes() {
		LstPacientes.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				try {
					TblPacientes dato_Paciente = (TblPacientes) parent.getItemAtPosition(position);
					vItem_IdPaciente = dato_Paciente.getIdPaciente();
					EnviarParametrosFragMpP();
				} catch (Exception ex) {
					Toast.makeText(getActivity(), getString(R.string.Error) + ex.getMessage(), Toast.LENGTH_SHORT).show();
				}  
			}			
		});
	}
	
	public void BtnNuevoPaciente() {
		BtnAgregarP.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					intent = new Intent(getActivity(), NewEditPacienteActivity.class);
					EnviarParametrosFragMpP2();
					startActivityForResult(intent, request_code);
					banderaNuevo = true;
				} catch (Exception ex) {
					Toast.makeText(getActivity(), getString(R.string.Error) + ex.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {    	
    	if (v.getId() == android.R.id.list) {
    	    int position = ((AdapterContextMenuInfo) menuInfo).position;
    	    this.getActivity().getMenuInflater().inflate(R.menu.menu_contextual_ed_el, menu);
    	    datos=(TblPacientes)LstPacientes.getItemAtPosition(position);    	        	    
    	    menu.setHeaderTitle(datos.getNombreApellidoP());   
    	    menu.setHeaderIcon(R.drawable.user_circle); 
    	}
    	super.onCreateContextMenu(menu, v, menuInfo);
    }
	
    public boolean onContextItemSelected(MenuItem item) {    	
    	switch(item.getItemId()) {
    	case R.id.Editar:  
    		intent = new Intent(getActivity(), NewEditPacienteActivity.class);
    		EnviarParametrosFragMpP3();    		     		
    		startActivityForResult(intent, request_code);
            banderaEditar=true;
            return true;    	
    	case R.id.Eliminar:    	   
    		FragmentManager fragmentManager = getFragmentManager();
			DFEliminarPaciente dialogo1 = new DFEliminarPaciente();
		    dialogo1.show(fragmentManager, "tagConfirmacion");	
    		return true;    	
    	default:
    		return super.onContextItemSelected(item);
    	}
    }

	@SuppressLint("ValidFragment")
	public class DFEliminarPaciente extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage(R.string.EliminarPaciente)
					.setTitle(R.string.ConfEliminacion)
					.setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_alert))
					.setPositiveButton(R.string.Aceptar, new DialogInterface.OnClickListener()  {
						public void onClick(DialogInterface dialog, int id) {
							//VERIFICAR SI EL CUIDADOR QUE ELIMINA EL REGISTRO TIENE PERMISOS ACTIVADOS
							//ELIMINAR DEL CELL TODAS LAS ALARMAS DEL PACIENTE
							EliminarAlarmasPaciente();
							//FINALMENTE ELIMINAR TODOS LOS REGISTROS VINCULADOS A ESE PACIENTE
							ATPacientes paciente = new ATPacientes();
							paciente.new EliminarPaciente().execute(datos.getIdPaciente().toString());
							//ACTUALIZAR LA LISTA
							ListaPacientes tarea1 = new ListaPacientes();
							tarea1.execute();
							dialog.cancel();
						}
					})
					.setNegativeButton(R.string.Cancelar, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
			AlertDialog alert=builder.create();
			setCancelable(false);
			alert.setCanceledOnTouchOutside(false);
			return alert;
		}
	}

    public void EliminarAlarmasPaciente() {
    	//VERIFICAR SI EL CUIDADOR QUE ELIMINA EL REGISTRO DE PACIENTE TIENE EL PERMISO DE ALARMA ACTIVADO
		//SI ESTA ACTIVADO SE LE ELIMINARA DEL CELL LAS ALARMAS DE EVENTOS Y RUTINAS 
		TblPermisos verPermiso1 = Select.from(TblPermisos.class).where(Condition.prop("id_cuidador").eq(vIdCuidador),
																	   Condition.prop("id_paciente").eq(datos.getIdPaciente()),
																	   Condition.prop("notifi_alarma").eq(1),
																	   Condition.prop("Eliminado").eq(0)).first();
		if (verPermiso1!=null) {
			//ELIMINAR DEL CELL LAS ALARMAS DE EVENTOS Y RUTINAS
    		MiTblEvento.EliminarAlarmasEventosPac(getActivity(), datos.getIdPaciente());
    		MiTblRutina.EliminarAlarmasRutinasPac(getActivity(), datos.getIdPaciente());
    	}
		
		//VERIFICAR SI EL CUIDADOR QUE ELIMINA EL REGISTRO DE PACIENTE TIENE EL PERMISO DE MEDICINAS ACTIVADO
		//SI ESTA ACTIVADO SE LE ELIMINARA DEL CELL LAS ALARMAS DE MEDICINAS 
		TblPermisos verPermiso2 = Select.from(TblPermisos.class).where(Condition.prop("id_cuidador").eq(vIdCuidador),
																	   Condition.prop("id_paciente").eq(datos.getIdPaciente()),
																	   Condition.prop("cont_medicina").eq(1),
																	   Condition.prop("Eliminado").eq(0)).first();
		if (verPermiso2!=null) {
			//ELIMINAR DEL CELL ALARMAS DE MEDICINAS 
    		MiTblMedicina.EliminarAlarmasMedicinasPac(getActivity(), datos.getIdPaciente());	            		
    	}
	}
    
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if ((requestCode == request_code) && (resultCode == Activity.RESULT_OK)){
			banderaNuevo=false;
			banderaEditar=false;	
			ListaPacientes tarea1 = new ListaPacientes();
            tarea1.execute();
		}
	}
			
	
	//ASYNCTASK
	
	
	private class ListaPacientes extends AsyncTask<Void, Void, AdapterListaP>{		
		@Override
		protected AdapterListaP doInBackground(Void... arg0) {		
			try{				
				CargarListaPacientes();
			}catch(Exception ex){
				ex.printStackTrace();
			}						
			return miAdapter;
		}

		@Override
		protected void onPostExecute(AdapterListaP result) {		
			super.onPostExecute(result);
			//LISTA CON DATOS
			if (!lista_pacientes.isEmpty()) {
				LstPacientes.setAdapter(miAdapter);			
				TxtNoHayDatos.setText("");			
				ImageFondo.setImageDrawable(null);
				filter = miAdapter.getFilter();  //FILTRO DE LA LISTA
				onQueryTextChange(query);
			}
			//LISTA VACIA
			if (lista_pacientes.isEmpty()) {
				LstPacientes.setAdapter(null);
				TxtNoHayDatos.setText(R.string.NoHayPacientes);		
				ImageFondo.setImageDrawable(getResources().getDrawable(R.drawable.pacientes2));
			}
		}		
	}

	
}