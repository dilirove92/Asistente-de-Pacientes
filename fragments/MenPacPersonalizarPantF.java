package com.Notifications.patientssassistant.fragments;


import com.Notifications.patientssassistant.*;
import com.Notifications.patientssassistant.adapters.*;
import com.Notifications.patientssassistant.dialogos.*;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.annotation.SuppressLint;


public class MenPacPersonalizarPantF extends Fragment {
	
	public final static String KEY_REG_TEXT1 = "IdPaciente";
	public final static String KEY_REG_TEXT2 = "ControlT";	
	private static Long vItemIdPaciente;
	private static Boolean vControlT;
	private int request_code = 1;
	
	//VARIABLES AUXILIARES		
	private FragmentIterationListener mCallback = null;	
	private static Boolean banderaNuevo=false;
	private static Boolean banderaEditar=false;
	private List<TblActividades> list_acts;
	private AdapterListPersPant miAdapter;
	private TblActividades datos;
	private Intent intent;
				
	//VARIABLES DE LOS ELEMENTOS DE LA IU
	Button BtnNuevaPet;
	Button BtnVistaPrevia;
	ListView ListPet;	
	
		
	public interface FragmentIterationListener{
        public void onFragmentIteration(Bundle parameters);
    }
	
	public MenPacPersonalizarPantF() {super();}
	
	public static MenPacPersonalizarPantF newInstance(Long c_idPaciente, Boolean c_controlT) {
		MenPacPersonalizarPantF frag = new MenPacPersonalizarPantF();
		Bundle args = frag.getArguments();
		if (args == null)
			args = new Bundle();
		args.putLong(KEY_REG_TEXT1, c_idPaciente);
		args.putBoolean(KEY_REG_TEXT2, c_controlT);
		frag.setArguments(args);
		return frag;
	}
	
	public void RecogerParametrosFragMenuPac7() {	
		vItemIdPaciente = getArguments().getLong(KEY_REG_TEXT1);
		vControlT = getArguments().getBoolean(KEY_REG_TEXT2);
	}
	
	public void EnviarParametrosFragMenPacPerP() {
		intent.putExtra("varIdPaciente", vItemIdPaciente);			
	}
	
	public void EnviarParametrosFragMenPacPerP2() { 
		EnviarParametrosFragMenPacPerP();
		intent.putExtra("varIdActividad", datos.getIdActividad());
		intent.putExtra("varNombreActividad", datos.getNombreActividad()); 
		intent.putExtra("varDetalleActividad", datos.getDetalleActividad());	
		intent.putExtra("varImagenActividad", datos.getImagenActividad());
		intent.putExtra("varTonoActividad", datos.getTonoActividad());
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
        				
        View rootView = inflater.inflate(R.layout.pantalla_personalizar, container, false);
        if(rootView != null){
        	BtnNuevaPet=(Button)rootView.findViewById(R.id.btnNuevaPeticion);
        	BtnVistaPrevia=(Button)rootView.findViewById(R.id.btnVistaPrevia);
        	ListPet=(ListView)rootView.findViewById(android.R.id.list);  
	    }
        return rootView;
    }		
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {		
		super.onViewCreated(view, savedInstanceState);
		
		RecogerParametrosFragMenuPac7();
		BtnNuevaPet();
		BtnVistaPre();
		ListaPeticiones tarea1 = new ListaPeticiones();
		tarea1.execute();
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
		if (vControlT.equals(true)) { registerForContextMenu(ListPet); }  //ACTIVA EL MENU CONTEXTUAL
		if (vControlT.equals(false)) { BtnNuevaPet.setEnabled(false); }  //DESHABILITAR EL BOTON PARA AGREGAR NUEVA PETICION		
	}
	
	public void CargarListaPeticiones() {
		List<TblActividadPaciente> list_actPac = Select.from(TblActividadPaciente.class).where(Condition.prop("id_paciente").eq(vItemIdPaciente),
		                                                            						   Condition.prop("Eliminado").eq(0)).list();
		TblTipoActividad tipoActividad = Select.from(TblTipoActividad.class).where(Condition.prop("tipo_actividad").eq("PETICION"),
																				   Condition.prop("Eliminado").eq(0)).first();
		list_acts=new ArrayList<TblActividades>();
		TblActividadPaciente registro=new TblActividadPaciente();			
		
		for (int i = 0; i < list_actPac.size(); i++) {				
			registro=list_actPac.get(i);
			
			TblActividades Acts = Select.from(TblActividades.class).where(Condition.prop("id_actividad").eq(registro.getIdActividad()),
																		  Condition.prop("id_tipo_actividad").eq(tipoActividad.getIdTipoActividad()),
																		  Condition.prop("Eliminado").eq(0)).first();
			if (Acts!=null) {					
				list_acts.add(Acts);
			}								
		}
		//LISTA CON DATOS (La lista jamas estara vacia, debido a que hay opciones fijas de peticiones)
		if(!list_acts.isEmpty()) {
			miAdapter = new AdapterListPersPant(getActivity(), list_acts);
							
			Collections.sort(list_acts, new Comparator<TblActividades>() {  //ORDENA LA LISTA
				@Override
				public int compare(TblActividades d1, TblActividades d2) {
					return d1.getNombreActividad().compareToIgnoreCase(d2.getNombreActividad());
				}
			});
		}	
	}
	
	public void BtnNuevaPet() {		
		BtnNuevaPet.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				int maximo=12;  //NUMERO MAXIMO DE PETICIONES
				if (ListPet.getCount() == maximo) { 
					FragmentManager fragmentManager2 = getFragmentManager();
					DFMaximo12 dialogo2 = new DFMaximo12();
	    	        dialogo2.show(fragmentManager2, "tagAlerta");
				}else{
					intent=new Intent(getActivity(), NewEditPeticionActivity.class);
					EnviarParametrosFragMenPacPerP();
					startActivityForResult(intent, request_code);	
					banderaNuevo=true;
				}				
			}
		});	
	}
	
	public void BtnVistaPre() {
		BtnVistaPrevia.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(getActivity(), VistaPreviaActivity.class);
				EnviarParametrosFragMenPacPerP();
				startActivity(intent);
			}
		});	
	}	
	
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {    	
    	if (v.getId() == android.R.id.list) {
			int position = ((AdapterContextMenuInfo) menuInfo).position;
			this.getActivity().getMenuInflater().inflate(R.menu.menu_contextual_ed_el, menu);
			datos=(TblActividades)ListPet.getItemAtPosition(position);
			menu.setHeaderTitle(getString(R.string.ChildrenPeticion) +" "+ datos.getNombreActividad());
			if (datos.getIdActividad()<=8) {
				menu.findItem(R.id.Editar).setVisible(false);
				menu.findItem(R.id.Eliminar).setVisible(true);
			} else {
				menu.findItem(R.id.Editar).setVisible(true);
				menu.findItem(R.id.Eliminar).setVisible(true);
			}
    	}
    	super.onCreateContextMenu(menu, v, menuInfo);
    }
	
    public boolean onContextItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    	case R.id.Editar:  
    		intent = new Intent(getActivity(), NewEditPeticionActivity.class);
    		EnviarParametrosFragMenPacPerP2();    		     		
    		startActivityForResult(intent, request_code);
            banderaEditar=true;
            return true;    	
    	case R.id.Eliminar:
		    int minimo=8;  //NUMERO MINIMO DE PETICIONES
			if (ListPet.getCount() == minimo) {		
				FragmentManager fragmentManager1 = getFragmentManager();
				DFMinimo8 dialogo1 = new DFMinimo8();
    	        dialogo1.show(fragmentManager1, "tagAlerta");
			}else{				
				FragmentManager fragmentManager3 = getFragmentManager();
				DFEliminarPet dialogo3 = new DFEliminarPet();
			    dialogo3.show(fragmentManager3, "tagConfirmacion");
			}
    		return true;    	
    	default:
    		return super.onContextItemSelected(item);
    	}
    }

	@SuppressLint("ValidFragment")
	public class DFEliminarPet extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage(R.string.EliminarPet)
					.setTitle(R.string.ConfEliminacion)
					.setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_alert))
					.setPositiveButton(R.string.Aceptar, new DialogInterface.OnClickListener()  {
						public void onClick(DialogInterface dialog, int id) {
							//ELIMINAR REGISTRO
							ATActividades actividad = new ATActividades();
							actividad.new EliminarActividades().execute(datos.getIdActividad().toString());
							//ACTUALIZAR LISTA
							ListaPeticiones tarea1 = new ListaPeticiones();
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if ((requestCode == request_code) && (resultCode == Activity.RESULT_OK)){
			banderaNuevo=false;
			banderaEditar=false;	
			ListaPeticiones tarea1 = new ListaPeticiones();
	        tarea1.execute();
		}
	}
	
	
	//ASYNCTASK
	
	
	private class ListaPeticiones extends AsyncTask<Void, Void, AdapterListPersPant>{		
		@Override
		protected AdapterListPersPant doInBackground(Void... arg0) {		
			try{				
				CargarListaPeticiones();
			}catch(Exception ex){
				ex.printStackTrace();
			}						
			return miAdapter;
		}

		@Override
		protected void onPostExecute(AdapterListPersPant result) {		
			super.onPostExecute(result);			
			//LISTA CON DATOS (La lista jamas estara vacia, debido a que hay opciones fijas de peticiones)
			if(!list_acts.isEmpty()) {				
				ListPet.setAdapter(miAdapter);
			}				
		}		
	}	
	

}