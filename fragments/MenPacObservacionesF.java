package com.Notifications.patientssassistant.fragments;


import com.Notifications.patientssassistant.*;
import com.Notifications.patientssassistant.adapters.*;
import com.Notifications.patientssassistant.tables.*;
import com.Notifications.patientssassistant.asynctask.*;
import java.text.SimpleDateFormat;
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


public class MenPacObservacionesF extends Fragment {
	
	public final static String KEY_REG_TEXT1 = "IdPaciente";
	public final static String KEY_REG_TEXT2 = "ControlT";	
	private static Long vItemIdPaciente;
	private static Boolean vControlT;
	private int request_code = 1;
	
	//VARIABLES AUXILIARES		
	private FragmentIterationListener mCallback = null;
	private static Boolean banderaNuevo=false;
	private static Boolean banderaEditar=false;
	private SparseArray<TblObservaciones> Grupos = new SparseArray<TblObservaciones>();	
	private List<TblObservaciones> lista_o;
	private AdapterListaObservaciones miAdapter;
	private TblObservaciones datos;
	private TblObservaciones titulo;	
	private Intent intent;
	private int position;
	private static SimpleDateFormat sdfDateTime =  new  SimpleDateFormat ("dd/MM/yyyy HH:mm");
		
	//VARIABLES DE LOS ELEMENTOS DE LA IU
	Button BtnNuevoO;
	ExpandableListView ListO;
	ImageView ImageFondo;
	TextView TxtNoHayDatos;
	
		
	public interface FragmentIterationListener{
        public void onFragmentIteration(Bundle parameters);
    }
	
	public MenPacObservacionesF() {super();}
	
	public static MenPacObservacionesF newInstance(Long c_idPaciente, Boolean c_controlT) {
		MenPacObservacionesF frag = new MenPacObservacionesF();
		Bundle args = frag.getArguments();
		if (args == null)
			args = new Bundle();
		args.putLong(KEY_REG_TEXT1, c_idPaciente);
		args.putBoolean(KEY_REG_TEXT2, c_controlT);
		frag.setArguments(args);
		return frag;
	}
	
	public void RecogerParametrosFragMenuPac6() {	
		vItemIdPaciente = getArguments().getLong(KEY_REG_TEXT1);
		vControlT = getArguments().getBoolean(KEY_REG_TEXT2);
	}
	
	public void EnviarParametrosFragMenPacO() {
		TblPacientes datos_paciente= Select.from(TblPacientes.class).where(Condition.prop("id_paciente").eq(vItemIdPaciente)).first();
		intent.putExtra("varItemIdPaciente", vItemIdPaciente);
		intent.putExtra("varNombrePaciente", datos_paciente.getNombreApellidoP());	
		intent.putExtra("varFotoPaciente", datos_paciente.getFotoP());	
	}
	
	public void EnviarParametrosFragMenPacO2() { 
		EnviarParametrosFragMenPacO();
		intent.putExtra("varIdObservacion", datos.getIdObservacion());		
		intent.putExtra("varAnio", datos.getAnio());
		intent.putExtra("varMes", datos.getMes());
		intent.putExtra("varDia", datos.getDia());
		intent.putExtra("varHora", datos.getHora());
		intent.putExtra("varMinutos", datos.getMinutos());		
		intent.putExtra("varObservacion", datos.getObservacion());	
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
        	BtnNuevoO=(Button)rootView.findViewById(R.id.btnNuevoRegistro);
        	ListO=(ExpandableListView)rootView.findViewById(R.id.listViewExp);  
        	ImageFondo=(ImageView)rootView.findViewById(R.id.imageView1);
        	TxtNoHayDatos=(TextView)rootView.findViewById(R.id.txtNoHayDatos);
	    }
        return rootView;
    }		
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {		
		super.onViewCreated(view, savedInstanceState);
			
		RecogerParametrosFragMenuPac6();
		BtnNuevoO();		
		//CREANDO LA LISTA EXPANDIBLE
		ListO.setGroupIndicator(null);
		ListaObservaciones tarea1 = new ListaObservaciones();
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
		if (vControlT.equals(true)) { registerForContextMenu(ListO); }  //ACTIVA EL MENU CONTEXTUAL
		if (vControlT.equals(false)) { BtnNuevoO.setEnabled(false); }  //DESHABILITAR EL BOTON PARA AGREGAR NUEVA OBSERVACION
	}
	
	public void CargarListaObservaciones() {		
		lista_o= Select.from(TblObservaciones.class).where(Condition.prop("id_paciente").eq(vItemIdPaciente),
														   Condition.prop("Eliminado").eq(0)).list();	
		//LISTA CON DATOS
		if (!lista_o.isEmpty()) {
			Collections.sort(lista_o, new Comparator<TblObservaciones>(){  //ORDENA LA LISTA
				@Override
				public int compare(TblObservaciones d1, TblObservaciones d2) {
					Long fecha1=null, fecha2=null;					
					try {							
						fecha1=sdfDateTime.parse(d1.getDia()+"/"+d1.getMes()+"/"+d1.getAnio()+" "+d1.getHora()+":"+d1.getMinutos()).getTime();
						fecha2=sdfDateTime.parse(d2.getDia()+"/"+d2.getMes()+"/"+d2.getAnio()+" "+d2.getHora()+":"+d2.getMinutos()).getTime();							
					} catch (Exception e) { }
					return fecha1.toString().compareToIgnoreCase(fecha2.toString());									
				}
	        });				
			Collections.reverse(lista_o); //ORDENA DESCENDENTE EN LA LISTA				
			
			int i=0;
			while(lista_o.size() > i) {
				TblObservaciones unaRegistro=new TblObservaciones();
				unaRegistro=lista_o.get(i);			
				TblObservaciones GrupoItem=new TblObservaciones(unaRegistro.getIdObservacion(), unaRegistro.getAnio(), unaRegistro.getMes(), 
																unaRegistro.getDia(), unaRegistro.getHora(), unaRegistro.getMinutos());
				TblObservaciones GrupoSubItem=new TblObservaciones(unaRegistro.getObservacion());								
				GrupoItem.Children.add(GrupoSubItem);				
				Grupos.append(i, GrupoItem);
				i++;				
			}
			
			miAdapter = new AdapterListaObservaciones(getActivity(), Grupos);
		}				
	}
	
	public void BtnNuevoO() {
		BtnNuevoO.setText(R.string.NuevaO);
		BtnNuevoO.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					intent = new Intent(getActivity(), NewEditObservacionActivity.class);
					EnviarParametrosFragMenPacO();
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
    	    titulo=(TblObservaciones)ListO.getExpandableListAdapter().getGroup(position);   	    
    	    menu.setHeaderTitle(getString(R.string.Observacion));
    	}
    	super.onCreateContextMenu(menu, v, menuInfo);
    }
	
    public boolean onContextItemSelected(MenuItem item) {   	
    	datos=Select.from(TblObservaciones.class).where(Condition.prop("id_observacion").eq(titulo.getIdObservacion())).first();
    	switch(item.getItemId()) {
    	case R.id.Editar:  
    		intent = new Intent(getActivity(), NewEditObservacionActivity.class);
    		EnviarParametrosFragMenPacO2();    		     		
    		startActivityForResult(intent, request_code);
            banderaEditar=true;
            return true;    	
    	case R.id.Eliminar:  
    		FragmentManager fragmentManager = getFragmentManager();
			DFEliminarO dialogo1 = new DFEliminarO();
		    dialogo1.show(fragmentManager, "tagConfirmacion");
    		return true;    	
    	default:
    		return super.onContextItemSelected(item);
    	}
    }

	@SuppressLint("ValidFragment")
	public class DFEliminarO extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage(R.string.EliminarO)
					.setTitle(R.string.ConfEliminacion)
					.setPositiveButton(R.string.Aceptar, new DialogInterface.OnClickListener()  {
						public void onClick(DialogInterface dialog, int id) {
							//ELIMINAR REGISTRO
							ATObservaciones observa = new ATObservaciones();
							observa.new EliminarObservacion().execute(datos.getIdObservacion().toString());
							Grupos.removeAt(Grupos.indexOfKey(position));
							Grupos.clear();
							//ACTUALIZAR LISTA
							ListaObservaciones tarea1 = new ListaObservaciones();
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
			ListaObservaciones tarea1 = new ListaObservaciones();
	        tarea1.execute();
		}
	}
		
	
	//ASYNCTASK
	
	
	private class ListaObservaciones extends AsyncTask<Void, Void, AdapterListaObservaciones>{		
		@Override
		protected AdapterListaObservaciones doInBackground(Void... arg0) {		
			try{				
				CargarListaObservaciones();
			}catch(Exception ex){
				ex.printStackTrace();
			}						
			return miAdapter;
		}

		@Override
		protected void onPostExecute(AdapterListaObservaciones result) {		
			super.onPostExecute(result);			
			//LISTA CON DATOS
			if (!lista_o.isEmpty()) {
				ListO.setAdapter(miAdapter);			
				TxtNoHayDatos.setText("");
				ImageFondo.setImageDrawable(null);
			}
			//LISTA VACIA
			if (lista_o.isEmpty()) {
				miAdapter=null;
				ListO.setAdapter(miAdapter);
				TxtNoHayDatos.setText(R.string.NoHayObservaciones);
				ImageFondo.setImageDrawable(getResources().getDrawable(R.drawable.observar2));
			}					
		}		
	}		
	

}