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


public class MenPacSeguimientoMedicoF extends Fragment {
	
	public final static String KEY_REG_TEXT1 = "IdPaciente";
	public final static String KEY_REG_TEXT2 = "ControlT";	
	private static Long vItemIdPaciente;
	private static Boolean vControlT;
	private int request_code = 1;
			
	//VARIABLES AUXILIARES
	private FragmentIterationListener mCallback = null;
	private static Boolean banderaNuevo=false;
	private static Boolean banderaEditar=false;
	private SparseArray<TblSeguimientoMedico> Grupos = new SparseArray<TblSeguimientoMedico>();	
	private List<TblSeguimientoMedico> lista_sm;
	private AdapterListaSeguimientoMedico miAdapter;	
	private TblSeguimientoMedico datos;
	private TblSeguimientoMedico titulo;	
	private Intent intent;
	private int position;
	private static SimpleDateFormat sdfDate =  new  SimpleDateFormat ("dd/MM/yyyy");
			
	//VARIABLES DE LOS ELEMENTOS DE LA IU
	Button BtnNuevoSM;
	ExpandableListView ListSM;
	ImageView ImageFondo;
	TextView TxtNoHayDatos;
	
			
	public interface FragmentIterationListener{
        public void onFragmentIteration(Bundle parameters);
    }
	
	public MenPacSeguimientoMedicoF() {super();}
	
	public static MenPacSeguimientoMedicoF newInstance(Long c_idPaciente, Boolean c_controlT) {
		MenPacSeguimientoMedicoF frag = new MenPacSeguimientoMedicoF();
		Bundle args = frag.getArguments();
		if (args == null)
			args = new Bundle();
		args.putLong(KEY_REG_TEXT1, c_idPaciente);
		args.putBoolean(KEY_REG_TEXT2, c_controlT);
		frag.setArguments(args);
		return frag;
	}
	
	public void RecogerParametrosFragMenuPac2() {	
		vItemIdPaciente = getArguments().getLong(KEY_REG_TEXT1);
		vControlT = getArguments().getBoolean(KEY_REG_TEXT2);
	}
	
	public void EnviarParametrosFragMenPacSM() {
		TblPacientes datos_paciente= Select.from(TblPacientes.class).where(Condition.prop("id_paciente").eq(vItemIdPaciente)).first();
		intent.putExtra("varItemIdPaciente", vItemIdPaciente);
		intent.putExtra("varNombrePaciente", datos_paciente.getNombreApellidoP());	
		intent.putExtra("varFotoPaciente", datos_paciente.getFotoP());	
	}
	
	public void EnviarParametrosFragMenPacSM2() { 
		EnviarParametrosFragMenPacSM();
		intent.putExtra("varIdSeguimientoMedico", datos.getIdSeguimientoMedico());
		intent.putExtra("varAnio", datos.getAnio());
		intent.putExtra("varMes", datos.getMes());
		intent.putExtra("varDia", datos.getDia());
		intent.putExtra("varUnidadMedica", datos.getUnidadMedica());
		intent.putExtra("varDoctor", datos.getDoctor()); 
		intent.putExtra("varDiagnostico", datos.getDiagnostico());
		intent.putExtra("varTratamientoMedico", datos.getTratamientoMedico()); 
		intent.putExtra("varAlimentacionSugerida", datos.getAlimentacionSugerida());
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
        	BtnNuevoSM=(Button)rootView.findViewById(R.id.btnNuevoRegistro);
        	ListSM=(ExpandableListView)rootView.findViewById(R.id.listViewExp);
        	ImageFondo=(ImageView)rootView.findViewById(R.id.imageView1);
        	TxtNoHayDatos=(TextView)rootView.findViewById(R.id.txtNoHayDatos);
	    }
        return rootView;
    }		
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {		
		super.onViewCreated(view, savedInstanceState);
		
		RecogerParametrosFragMenuPac2();
		BtnNuevoSM();		
		//CREANDO LA LISTA EXPANDIBLE
		ListSM.setGroupIndicator(null);
		ListaSeguimientoMedico tarea1 = new ListaSeguimientoMedico();
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
		if (vControlT.equals(true)) { registerForContextMenu(ListSM); }  //ACTIVA EL MENU CONTEXTUAL
		if (vControlT.equals(false)) { BtnNuevoSM.setEnabled(false); }  //DESHABILITAR EL BOTON PARA AGREGAR NUEVO SEGUIMIENTO MEDICO
	}
	
	public void CargarListaSeguimientoMedico() {		
		lista_sm= Select.from(TblSeguimientoMedico.class).where(Condition.prop("id_paciente").eq(vItemIdPaciente),
																Condition.prop("Eliminado").eq(0)).list();	
		//LISTA CON DATOS
		if (!lista_sm.isEmpty()) {
			Collections.sort(lista_sm, new Comparator<TblSeguimientoMedico>(){  //ORDENA LA LISTA
				@Override
				public int compare(TblSeguimientoMedico d1, TblSeguimientoMedico d2) {
					Long f1=null, f2=null;					
					try {							
						f1=sdfDate.parse(d1.getDia()+"/"+d1.getMes()+"/"+d1.getAnio()).getTime();
						f2=sdfDate.parse(d2.getDia()+"/"+d2.getMes()+"/"+d2.getAnio()).getTime();							
					} catch (Exception e) {
						// TODO: handle exception
					}
					return f1.toString().compareToIgnoreCase(f2.toString());									
				}
	        });				
			Collections.reverse(lista_sm); //ORDENA DESCENDENTE EN LA LISTA
			
			int i=0;	
			while(lista_sm.size() > i) {
				TblSeguimientoMedico unaRegistro=new TblSeguimientoMedico();
				unaRegistro=lista_sm.get(i);			
				TblSeguimientoMedico GrupoItem=new TblSeguimientoMedico(unaRegistro.getIdSeguimientoMedico(), unaRegistro.getAnio(), unaRegistro.getMes(),
																		unaRegistro.getDia(), unaRegistro.getDoctor());
				TblSeguimientoMedico GrupoSubItem=new TblSeguimientoMedico(unaRegistro.getUnidadMedica(), unaRegistro.getDiagnostico(), 
																		   unaRegistro.getTratamientoMedico(), unaRegistro.getAlimentacionSugerida());
				GrupoItem.Children.add(GrupoSubItem);				
				Grupos.append(i, GrupoItem);
				i++;				
			}
			
			miAdapter = new AdapterListaSeguimientoMedico(getActivity(), Grupos);									
		}	
	}
	
	public void BtnNuevoSM() {
		BtnNuevoSM.setText(R.string.NuevoSM);
		BtnNuevoSM.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					intent = new Intent(getActivity(), NewEditSeguimientoMedicoActivity.class);
					EnviarParametrosFragMenPacSM();
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
    	    titulo=(TblSeguimientoMedico)ListSM.getExpandableListAdapter().getGroup(position); 
    	    menu.setHeaderTitle(getResources().getString(R.string.SeguimientoMedico));
    	    menu.setHeaderIcon(R.drawable.salud);
    	}
    	super.onCreateContextMenu(menu, v, menuInfo);
    }
	
    public boolean onContextItemSelected(MenuItem item) {
    	datos=Select.from(TblSeguimientoMedico.class).where(Condition.prop("id_seguimiento_medico").eq(titulo.getIdSeguimientoMedico())).first();   	
    	switch(item.getItemId()) {
    	case R.id.Editar:  
    		intent = new Intent(getActivity(), NewEditSeguimientoMedicoActivity.class);
    		EnviarParametrosFragMenPacSM2();    		     		
    		startActivityForResult(intent, request_code);
            banderaEditar=true;
            return true;    	
    	case R.id.Eliminar:  
    		FragmentManager fragmentManager = getFragmentManager();
			DFEliminarSM dialogo1 = new DFEliminarSM();
		    dialogo1.show(fragmentManager, "tagConfirmacion");	
		    return true;    	
    	default:
    		return super.onContextItemSelected(item);
    	}
    }

	@SuppressLint("ValidFragment")
	public class DFEliminarSM extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage(R.string.EliminarSM)
					.setTitle(R.string.ConfEliminacion)
					.setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_alert))
					.setPositiveButton(R.string.Aceptar, new DialogInterface.OnClickListener()  {
						public void onClick(DialogInterface dialog, int id) {
							//ELIMINAR REGISTRO
							ATSeguimientoMedico segMed = new ATSeguimientoMedico();
							segMed.new EliminarSeguimientoMedico().execute(datos.getIdSeguimientoMedico().toString());
							Grupos.removeAt(Grupos.indexOfKey(position));
							Grupos.clear();
							//ACTUALIZAR LISTA
							ListaSeguimientoMedico tarea1 = new ListaSeguimientoMedico();
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
    		ListaSeguimientoMedico tarea1 = new ListaSeguimientoMedico();
	        tarea1.execute();
		}
	}
	
	
	//ASYNCTASK
	
	
	private class ListaSeguimientoMedico extends AsyncTask<Void, Void, AdapterListaSeguimientoMedico>{		
		@Override
		protected AdapterListaSeguimientoMedico doInBackground(Void... arg0) {		
			try{				
				CargarListaSeguimientoMedico();
			}catch(Exception ex){
				ex.printStackTrace();
			}						
			return miAdapter;
		}

		@Override
		protected void onPostExecute(AdapterListaSeguimientoMedico result) {		
			super.onPostExecute(result);			
			//LISTA CON DATOS
			if (!lista_sm.isEmpty()) {
				ListSM.setAdapter(miAdapter);
				TxtNoHayDatos.setText("");
				ImageFondo.setImageDrawable(null);					
			}
			//LISTA VACIA
			if (lista_sm.isEmpty()) {
				miAdapter=null;
				ListSM.setAdapter(miAdapter);
				TxtNoHayDatos.setText(R.string.NoHaySeguimientosMedicos);
				ImageFondo.setImageDrawable(getResources().getDrawable(R.drawable.salud2));
			}						
		}		
	}	

	    
}