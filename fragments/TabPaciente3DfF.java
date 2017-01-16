package com.Notifications.patientssassistant.fragments;


import com.Notifications.patientssassistant.*;
import com.Notifications.patientssassistant.adapters.*;
import com.Notifications.patientssassistant.tables.*;
import com.Notifications.patientssassistant.asynctask.*;
import com.orm.query.Condition;
import com.orm.query.Select;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
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
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.SearchView.*;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.annotation.SuppressLint;


public class TabPaciente3DfF extends Fragment implements OnQueryTextListener, OnCloseListener {
	
	public final static String KEY_REG_TEXT1 = "IdPaciente";
	public final static String KEY_REG_TEXT2 = "ControlT";	
	private static Long vItemIdPaciente;
	private static Boolean vControlT;
	private int request_code = 1;
	
	//VARIABLES AUXILIARES
	private FragmentIterationListener mCallback = null;	
	private static Boolean banderaNuevo=false;
	private static Boolean banderaEditar=false;
	private ArrayList<TblFamiliaresPacientes> FamiliaList = new ArrayList<TblFamiliaresPacientes>();
	private List<TblFamiliaresPacientes> lista_fam_pac;
	private AdapterListaFamiliar miAdapter;	
	private TblFamiliaresPacientes titulo;	
	private TblFamiliaresPacientes datos;
	private Intent intent;
	private int position;
	private String query="";
				
	//VARIABLES DE LOS ELEMENTOS DE LA IU
	ImageButton BtnNuevoDF;
	SearchView BusquedaF;
	ExpandableListView ListFamiliares;
	ImageView ImageFondo;
	TextView TxtNoHayDatos;
	SearchManager searchManager;
	
				
	public interface FragmentIterationListener{
        public void onFragmentIteration(Bundle parameters);
    }
	
	public TabPaciente3DfF() {super();}
	
	public static TabPaciente3DfF newInstance(Long c_idPaciente, Boolean c_controlT) {
		TabPaciente3DfF frag = new TabPaciente3DfF();
		Bundle args = frag.getArguments();
		if (args == null)
			args = new Bundle();
		args.putLong(KEY_REG_TEXT1, c_idPaciente);
		args.putBoolean(KEY_REG_TEXT2, c_controlT);
		frag.setArguments(args);
		return frag;
	}
	
	public void RecogerParametrosFragTabPac3() {	
		vItemIdPaciente = getArguments().getLong(KEY_REG_TEXT1);
		vControlT = getArguments().getBoolean(KEY_REG_TEXT2);
	}
	
	public void EnviarParametrosFragTabPacDf() {
		TblPacientes datos_paciente= Select.from(TblPacientes.class).where(Condition.prop("id_paciente").eq(vItemIdPaciente)).first();
		intent.putExtra("varIdPaciente", vItemIdPaciente);
		intent.putExtra("varNombrePaciente", datos_paciente.getNombreApellidoP());	
		intent.putExtra("varFotoPaciente", datos_paciente.getFotoP());	
	}
	
	public void EnviarParametrosFragTabPacDf2() { 
		EnviarParametrosFragTabPacDf();
		intent.putExtra("varIdFamiliarPaciente", datos.getIdFamiliarPaciente());
		intent.putExtra("varNombreContacto", datos.getNombreContacto());
		intent.putExtra("varCiContacto", datos.getCiContacto());
		intent.putExtra("varParentezco", datos.getParentezco());
		intent.putExtra("varCelular", datos.getCelular());
		intent.putExtra("varTelefono", datos.getTelefono()); 
		intent.putExtra("varDireccion", datos.getDireccion());
		intent.putExtra("varObservacion", datos.getObservacion());
		intent.putExtra("varEmail", datos.getEmail()); 
		intent.putExtra("varEnviarReportes", datos.getEnviarReportes());		
		intent.putExtra("varFotoContacto", datos.getFotoContacto());
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
        				
        View rootView = inflater.inflate(R.layout.tab_paciente_df, container, false);
        if(rootView != null){
        	BtnNuevoDF=(ImageButton)rootView.findViewById(R.id.btnAgregarF); 
        	searchManager = (SearchManager)getActivity().getSystemService(Context.SEARCH_SERVICE);
        	BusquedaF = (SearchView)rootView.findViewById(R.id.busquedaF);
        	ListFamiliares=(ExpandableListView)rootView.findViewById(R.id.listViewExp); 
        	ImageFondo=(ImageView)rootView.findViewById(R.id.imageView1);
        	TxtNoHayDatos=(TextView)rootView.findViewById(R.id.txtNoHayDatos);
	    }
        return rootView;
    }	
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {		
		super.onViewCreated(view, savedInstanceState);		
					
		RecogerParametrosFragTabPac3();		
		BusquedaF.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
		BusquedaF.setIconifiedByDefault(false);
		BusquedaF.setOnQueryTextListener(TabPaciente3DfF.this);
		BusquedaF.setOnCloseListener(TabPaciente3DfF.this);
		BtnNuevoDF();		
		//CREANDO LA LISTA EXPANDIBLE
		ListFamiliares.setGroupIndicator(null);		
		ListaFamiliares tarea1 = new ListaFamiliares();
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
	
	private void CloseListaExpandible() {
		int count = miAdapter.getGroupCount();
		for (int i = 0; i < count; i++){
			ListFamiliares.collapseGroup(i);
		}
	}
	
	@Override
	public boolean onClose() {
		if (!lista_fam_pac.isEmpty()) {
			miAdapter.filterData("");
			CloseListaExpandible();			
		}		
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		if (!lista_fam_pac.isEmpty()) {
			miAdapter.filterData(query);
		}
		return false;		
	}

	@Override
	public boolean onQueryTextChange(String query) {
		if (!lista_fam_pac.isEmpty()) {
			miAdapter.filterData(query);
			this.query=query;
		}
		return false;
	}
	
	public void ActDesElementos() {
		//ACTIVA EL MENU CONTEXTUAL
		if (vControlT.equals(true)) {
			registerForContextMenu(ListFamiliares);
		}
		//DESHABILITAR EL BOTON PARA AGREGAR NUEVO FAMILIAR
		if (vControlT.equals(false)) {
			BtnNuevoDF.setEnabled(false);
			BtnNuevoDF.setImageResource(R.drawable.add_user2);
		}
	}
	
	public void CargarListaFamiliares() {			
		lista_fam_pac = Select.from(TblFamiliaresPacientes.class).where(Condition.prop("id_paciente").eq(vItemIdPaciente),
																		Condition.prop("Eliminado").eq(0)).orderBy("nombre_contacto asc").list();
		//LISTA CON DATOS
		if (!lista_fam_pac.isEmpty()) {
			int i=0;	
			FamiliaList.clear();  //LIMPIAR PARA NO REPETIR LOS DATOS
			
			TblFamiliaresPacientes unaRegistro = new TblFamiliaresPacientes();
			ArrayList<TblFamiliaresPacientes> ChildrenList;
			TblFamiliaresPacientes Children;
			TblFamiliaresPacientes Grupo;
			
			while(lista_fam_pac.size() > i) {					
				unaRegistro = lista_fam_pac.get(i);
				
				ChildrenList = new ArrayList<TblFamiliaresPacientes>();
				Children = new TblFamiliaresPacientes(unaRegistro.getCiContacto(), unaRegistro.getParentezco(), unaRegistro.getCelular(), unaRegistro.getTelefono(), 
						   							  unaRegistro.getDireccion(), unaRegistro.getObservacion(), unaRegistro.getEmail(), unaRegistro.getEnviarReportes());
				ChildrenList.add(Children);
				
				Grupo = new TblFamiliaresPacientes(unaRegistro.getIdFamiliarPaciente(), unaRegistro.getNombreContacto(), unaRegistro.getFotoContacto(), ChildrenList);
				FamiliaList.add(Grupo);					
				i++;					
			}				
			miAdapter = new AdapterListaFamiliar(getActivity(), FamiliaList);
		}			
	}
	
	public void BtnNuevoDF() {
		BtnNuevoDF.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					intent = new Intent(getActivity(), NewEditFamiliarActivity.class);
					EnviarParametrosFragTabPacDf();
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
    	    titulo=(TblFamiliaresPacientes)ListFamiliares.getExpandableListAdapter().getGroup(position);    	
    	    menu.setHeaderTitle(titulo.getNombreContacto());
    	    menu.setHeaderIcon(R.drawable.user_circle);
    	}
    	super.onCreateContextMenu(menu, v, menuInfo);
    }
	
    public boolean onContextItemSelected(MenuItem item) {
    	datos=Select.from(TblFamiliaresPacientes.class).where(Condition.prop("id_familiar_paciente").eq(titulo.getIdFamiliarPaciente())).first();
    	switch(item.getItemId()) {
    	case R.id.Editar:  
    		intent = new Intent(getActivity(), NewEditFamiliarActivity.class);
    		EnviarParametrosFragTabPacDf2();    		     		
    		startActivityForResult(intent, request_code);
            banderaEditar=true;
            return true;    	
    	case R.id.Eliminar: 
    		FragmentManager fragmentManager = getFragmentManager();
			DFEliminarDF dialogo1 = new DFEliminarDF();
		    dialogo1.show(fragmentManager, "tagConfirmacion");	    		 		
    		return true;    	
    	default:
    		return super.onContextItemSelected(item);
    	}
    }

	@SuppressLint("ValidFragment")
	public class DFEliminarDF extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage(R.string.EliminarDF)
					.setTitle(R.string.ConfEliminacion)
					.setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_alert))
					.setPositiveButton(R.string.Aceptar, new DialogInterface.OnClickListener()  {
						public void onClick(DialogInterface dialog, int id) {
							//ELIMINAR REGISTRO
							ATFamiliaresPacientes famPac = new ATFamiliaresPacientes();
							famPac.new EliminarFamiliaPaciente().execute(datos.getIdFamiliarPaciente().toString());
							//ACTUALIZAR LISTA
							ListaFamiliares tarea1 = new ListaFamiliares();
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
			ListaFamiliares tarea1 = new ListaFamiliares();
	        tarea1.execute();
		}
	}
		
	
	//ASYNCTASK
	
	
	private class ListaFamiliares extends AsyncTask<Void, Void, AdapterListaFamiliar>{		
		@Override
		protected AdapterListaFamiliar doInBackground(Void... arg0) {		
			try{				
				CargarListaFamiliares();
			}catch(Exception ex){
				ex.printStackTrace();
			}						
			return miAdapter;
		}

		@Override
		protected void onPostExecute(AdapterListaFamiliar result) {		
			super.onPostExecute(result);			
			//LISTA CON DATOS
			if (!lista_fam_pac.isEmpty()) {
				ListFamiliares.setAdapter(miAdapter);
				TxtNoHayDatos.setText("");
				ImageFondo.setImageDrawable(null);	
				onQueryTextChange(query);
			}
			//LISTA VACIA
			if (lista_fam_pac.isEmpty()) {
				miAdapter=null;
				ListFamiliares.setAdapter(miAdapter);
				TxtNoHayDatos.setText(R.string.NoHayFamiliares);
				ImageFondo.setImageDrawable(getResources().getDrawable(R.drawable.familia2));
			}						
		}		
	}	
			
	
}