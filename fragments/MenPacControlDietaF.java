package com.Notifications.patientssassistant.fragments;


import com.Notifications.patientssassistant.*;
import com.Notifications.patientssassistant.adapters.*;
import com.Notifications.patientssassistant.tables.*;
import com.Notifications.patientssassistant.asynctask.*;
import java.util.List;
import com.orm.query.Condition;
import com.orm.query.Select;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.annotation.SuppressLint;


public class MenPacControlDietaF extends Fragment {
	
	public final static String KEY_REG_TEXT1 = "IdPaciente";
	public final static String KEY_REG_TEXT2 = "ControlT";	
	private static Long vItemIdPaciente;
	private static Boolean vControlT;
	private int request_code = 1;
			
	//VARIABLES AUXILIARES
	private FragmentIterationListener mCallback = null;
	private static Boolean banderaNuevo=false;
	private static Boolean banderaEditar=false;
	private SparseArray<TblControlDieta> Grupos = new SparseArray<TblControlDieta>();
	private List<TblControlDieta> lista_cd;
	private AdapterListaControlDieta miAdapter;
	private TblControlDieta datos;
	private TblControlDieta titulo;	
	private Intent intent;
	private int position;
			
	//VARIABLES DE LOS ELEMENTOS DE LA IU
	Button BtnNuevoCD;
	ExpandableListView ListCD;
	ImageView ImageFondo;
	TextView TxtNoHayDatos;
	
			
	public interface FragmentIterationListener{
        public void onFragmentIteration(Bundle parameters);
    }
	
	public MenPacControlDietaF() {super();}
	
	public static MenPacControlDietaF newInstance(Long c_idPaciente, Boolean c_controlT) {
		MenPacControlDietaF frag = new MenPacControlDietaF();
		Bundle args = frag.getArguments();
		if (args == null)
			args = new Bundle();
		args.putLong(KEY_REG_TEXT1, c_idPaciente);
		args.putBoolean(KEY_REG_TEXT2, c_controlT);
		frag.setArguments(args);
		return frag;
	}
	
	public void RecogerParametrosFragMenuPac4() {	
		vItemIdPaciente = getArguments().getLong(KEY_REG_TEXT1);
		vControlT = getArguments().getBoolean(KEY_REG_TEXT2);
	}
	
	public void EnviarParametrosFragMenPacCD() {
		TblPacientes datos_paciente= Select.from(TblPacientes.class).where(Condition.prop("id_paciente").eq(vItemIdPaciente)).first();
		intent.putExtra("varItemIdPaciente", vItemIdPaciente);
		intent.putExtra("varNombrePaciente", datos_paciente.getNombreApellidoP());	
		intent.putExtra("varFotoPaciente", datos_paciente.getFotoP());	
	}
	
	public void EnviarParametrosFragMenPacCD2() { 
		EnviarParametrosFragMenPacCD();
		intent.putExtra("varIdControlDieta", datos.getIdControlDieta());		
		intent.putExtra("varMotivo", datos.getMotivo());
		intent.putExtra("varAlimentosRecomendados", datos.getAlimentosRecomendados()); 
		intent.putExtra("varAlimentosNoAdecuados", datos.getAlimentosNoAdecuados());		
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
        				
        View rootView = inflater.inflate(R.layout.pantalla_boton_lista_expandible, container, false);
        if(rootView != null){
        	BtnNuevoCD=(Button)rootView.findViewById(R.id.btnNuevoRegistro);
        	ListCD=(ExpandableListView)rootView.findViewById(R.id.listViewExp);
        	ImageFondo=(ImageView)rootView.findViewById(R.id.imageView1);
        	TxtNoHayDatos=(TextView)rootView.findViewById(R.id.txtNoHayDatos);
	    }
        return rootView;
    }		
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {		
		super.onViewCreated(view, savedInstanceState);
			
		RecogerParametrosFragMenuPac4();
		BtnNuevoCD();		
		//CREANDO LA LISTA EXPANDIBLE
		ListCD.setGroupIndicator(null);
		ListaControlDieta tarea1 = new ListaControlDieta();
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
		if (vControlT.equals(true)) { registerForContextMenu(ListCD); }  //ACTIVA EL MENU CONTEXTUAL
		if (vControlT.equals(false)) { BtnNuevoCD.setEnabled(false); }  //DESHABILITAR EL BOTON PARA AGREGAR NUEVO CONTROL DE DIETA
	}
	
	public void CargarListaControlDieta() {		
		lista_cd = Select.from(TblControlDieta.class).where(Condition.prop("id_paciente").eq(vItemIdPaciente),
															Condition.prop("Eliminado").eq(0)).orderBy("id_control_dieta asc").list();	
		//LISTA CON DATOS			
		if (!lista_cd.isEmpty()) {
			int i=0;
			while(lista_cd.size() > i) {
				TblControlDieta unaRegistro=new TblControlDieta();
				unaRegistro=lista_cd.get(i);			
				TblControlDieta GrupoItem=new TblControlDieta(unaRegistro.getIdControlDieta(), unaRegistro.getMotivo());
				TblControlDieta GrupoSubItem=new TblControlDieta(unaRegistro.getAlimentosRecomendados(), unaRegistro.getAlimentosNoAdecuados());								
				GrupoItem.Children.add(GrupoSubItem);				
				Grupos.append(i, GrupoItem);
				i++;				
			}			
			
			miAdapter = new AdapterListaControlDieta(getActivity(), Grupos);				
		}						
	}
	
	public void BtnNuevoCD() {
		BtnNuevoCD.setText(R.string.NuevoCD);
		BtnNuevoCD.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					intent = new Intent(getActivity(), NewEditControlDietaActivity.class);
					EnviarParametrosFragMenPacCD();
					startActivityForResult(intent, request_code);
					banderaNuevo = true;
				} catch (Exception ex) {
					Toast.makeText(getActivity(), getString(R.string.Error) + ex.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}
		});	
	}
	
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {    	
    	if (v.getId() == R.id.listViewExp) {
    		ExpandableListContextMenuInfo info=(ExpandableListContextMenuInfo)menuInfo;
            position=ExpandableListView.getPackedPositionGroup(info.packedPosition);
    	    this.getActivity().getMenuInflater().inflate(R.menu.menu_contextual_ed_el, menu);
    	    titulo=(TblControlDieta)ListCD.getExpandableListAdapter().getGroup(position); 
    	    menu.setHeaderTitle(R.string.ContDiet);
    	    menu.setHeaderIcon(R.drawable.dieta);
    	}
    	super.onCreateContextMenu(menu, v, menuInfo);
    }
	
    public boolean onContextItemSelected(MenuItem item) {
    	datos=Select.from(TblControlDieta.class).where(Condition.prop("id_control_dieta").eq(titulo.getIdControlDieta())).first();    	    	
    	switch(item.getItemId()) {
    	case R.id.Editar:  
    		intent = new Intent(getActivity(), NewEditControlDietaActivity.class);
    		EnviarParametrosFragMenPacCD2();    		     		
    		startActivityForResult(intent, request_code);
            banderaEditar=true;
            return true;    	
    	case R.id.Eliminar:
    		FragmentManager fragmentManager = getFragmentManager();
			DFEliminarCD dialogo1 = new DFEliminarCD();
		    dialogo1.show(fragmentManager, "tagConfirmacion");    		  		
    		return true;    	
    	default:
    		return super.onContextItemSelected(item);
    	}
    }

	@SuppressLint("ValidFragment")
	public class DFEliminarCD extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage(R.string.EliminarCD)
					.setTitle(R.string.ConfEliminacion)
					.setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_alert))
					.setPositiveButton(R.string.Aceptar, new DialogInterface.OnClickListener()  {
						public void onClick(DialogInterface dialog, int id) {
							//ELIMINAR REGISTRO
							ATControlDieta contDieta= new ATControlDieta();
							contDieta.new EliminarControlDieta().execute(datos.getIdControlDieta().toString());
							Grupos.removeAt(Grupos.indexOfKey(position));
							Grupos.clear();
							//ACTUALIZAR LISTA
							ListaControlDieta tarea1 = new ListaControlDieta();
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
			ListaControlDieta tarea1 = new ListaControlDieta();
	        tarea1.execute();
		}
	}
		
	
	//ASYNCTASK
	
	
	private class ListaControlDieta extends AsyncTask<Void, Void, AdapterListaControlDieta>{		
		@Override
		protected AdapterListaControlDieta doInBackground(Void... arg0) {		
			try{				
				CargarListaControlDieta();
			}catch(Exception ex){
				ex.printStackTrace();
			}						
			return miAdapter;
		}

		@Override
		protected void onPostExecute(AdapterListaControlDieta result) {		
			super.onPostExecute(result);			
			//LISTA CON DATOS
			if (!lista_cd.isEmpty()) {
				ListCD.setAdapter(miAdapter);
				TxtNoHayDatos.setText("");
				ImageFondo.setImageDrawable(null);
			}
			//LISTA VACIA
			if (lista_cd.isEmpty()) {
				miAdapter=null;
				ListCD.setAdapter(miAdapter);
				TxtNoHayDatos.setText(R.string.NoHayControlesDietas);
				ImageFondo.setImageDrawable(getResources().getDrawable(R.drawable.dieta2));
			}						
		}		
	}

}