package com.Notifications.patientssassistant.fragments;


import com.Notifications.patientssassistant.*;
import com.Notifications.patientssassistant.adapters.*;
import com.Notifications.patientssassistant.tables.*;
import com.Notifications.patientssassistant.asynctask.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
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
import com.orm.query.Condition;
import com.orm.query.Select;
import android.annotation.SuppressLint;


public class MenPacControlMedicinasF extends Fragment {
	
	public final static String KEY_REG_TEXT1 = "IdCuidador";
	public final static String KEY_REG_TEXT2 = "IdPaciente";
	public final static String KEY_REG_TEXT3 = "ControlT";
	public final static String KEY_REG_TEXT4 = "Medicina";
	private static Long vIdCuidador;
	private static Long vItemIdPaciente;
	private static Boolean vControlT;
	private static Boolean vMedicina;
	private int request_code = 1;
			
	//VARIABLES AUXILIARES
	private FragmentIterationListener mCallback = null;		
	private static Boolean banderaNuevo=false;
	private static Boolean banderaEditar=false;
	private SparseArray<TblControlMedicina> medicinasList = new SparseArray<TblControlMedicina>();
	private ArrayList<TblHorarioMedicina> horariosList = new ArrayList<TblHorarioMedicina>();
	private List<TblControlMedicina> lista_medicinas;
	private AdapterListaMedicinasHorarios miAdapter;
	private TblControlMedicina datos;
	private TblControlMedicina titulo;	
	private Intent intent;
	private int position;
	private static SimpleDateFormat sdfTime =  new  SimpleDateFormat ("HH:mm");
		
	//VARIABLES DE LOS ELEMENTOS DE LA IU
	Button BtnNuevoCM;
	ExpandableListView ListCM;
	ImageView ImageFondo;
	TextView TxtNoHayDatos;
	
		
	public interface FragmentIterationListener{
        public void onFragmentIteration(Bundle parameters);
    }
	
	public MenPacControlMedicinasF() {super();}
	
	public static MenPacControlMedicinasF newInstance(Long c_idCuidador, Long c_idPaciente, Boolean c_controlT, Boolean c_medicina) {
		MenPacControlMedicinasF frag = new MenPacControlMedicinasF();
		Bundle args = frag.getArguments();
		if (args == null)
			args = new Bundle();
		args.putLong(KEY_REG_TEXT1, c_idCuidador);
		args.putLong(KEY_REG_TEXT2, c_idPaciente);
		args.putBoolean(KEY_REG_TEXT3, c_controlT);
		args.putBoolean(KEY_REG_TEXT4, c_medicina);
		frag.setArguments(args);
		return frag;
	}
	
	public void RecogerParametrosFragMenuPac3() {
		vIdCuidador = getArguments().getLong(KEY_REG_TEXT1);
		vItemIdPaciente = getArguments().getLong(KEY_REG_TEXT2);
		vControlT = getArguments().getBoolean(KEY_REG_TEXT3);
		vMedicina = getArguments().getBoolean(KEY_REG_TEXT4);
	}
	
	public void EnviarParametrosFragMenPacCM() {
		TblPacientes datos_paciente= Select.from(TblPacientes.class).where(Condition.prop("id_paciente").eq(vItemIdPaciente)).first();
		intent.putExtra("varIdCuidador", vIdCuidador);
		intent.putExtra("varItemIdPaciente", vItemIdPaciente);
		intent.putExtra("varNombrePaciente", datos_paciente.getNombreApellidoP());	
		intent.putExtra("varFotoPaciente", datos_paciente.getFotoP());	
	}
	
	public void EnviarParametrosFragMenPacCM2() { 
		EnviarParametrosFragMenPacCM();
		intent.putExtra("varIdControlMedicina", datos.getIdControlMedicina());		
		intent.putExtra("varMedicamento", datos.getMedicamento());
		intent.putExtra("varDescripcion", datos.getDescripcion()); 
		intent.putExtra("varMotivoMedicacion", datos.getMotivoMedicacion());		
		intent.putExtra("varTipoTratamiento", datos.getTiempo());		
		intent.putExtra("varDosis", datos.getDosis()); 
		intent.putExtra("varNdeVeces", datos.getNdeVeces());
		intent.putExtra("varTono", datos.getTono());
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
        	BtnNuevoCM=(Button)rootView.findViewById(R.id.btnNuevoRegistro);
        	ListCM=(ExpandableListView)rootView.findViewById(R.id.listViewExp);  
        	ImageFondo=(ImageView)rootView.findViewById(R.id.imageView1);
        	TxtNoHayDatos=(TextView)rootView.findViewById(R.id.txtNoHayDatos);
	    }
        return rootView;
    }		
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {		
		super.onViewCreated(view, savedInstanceState);
		
		RecogerParametrosFragMenuPac3();
		BtnNuevoCM();		
		//CREANDO LA LISTA EXPANDIBLE
		ListCM.setGroupIndicator(null);
		ListaMedicinasHorarios tarea1 = new ListaMedicinasHorarios();
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
		if (vControlT.equals(true) || vMedicina.equals(true)) { registerForContextMenu(ListCM); }  //ACTIVA EL MENU CONTEXTUAL
		if (vControlT.equals(false) && vMedicina.equals(false))  { BtnNuevoCM.setEnabled(false); }  //DESHABILITAR EL BOTON PARA AGREGAR NUEVA MEDICINA
	}

	public void CargarListaMedicinasHorarios() {		
		lista_medicinas = Select.from(TblControlMedicina.class).where(Condition.prop("id_paciente").eq(vItemIdPaciente),
																	  Condition.prop("Eliminado").eq(0)).orderBy("Medicamento asc").list();
		//LISTA CON DATOS
		if (!lista_medicinas.isEmpty()) {
			
			for (int i = 0; i < lista_medicinas.size(); i++) {
				TblControlMedicina tabla_medicinas = lista_medicinas.get(i);				
				horariosList = new ArrayList<TblHorarioMedicina>();
				
				List<TblHorarioMedicina> lista_horarios= Select.from(TblHorarioMedicina.class).where(Condition.prop("id_control_medicina").eq(tabla_medicinas.getIdControlMedicina()),
																									 Condition.prop("Eliminado").eq(0)).list();		
				Collections.sort(lista_horarios, new Comparator<TblHorarioMedicina>(){  //ORDENA LA LISTA
					@Override
					public int compare(TblHorarioMedicina d1, TblHorarioMedicina d2) {
						Date h1=null, h2=null;					
						try {
							h1=sdfTime.parse(d1.getHora()+":"+d1.getMinutos());
							h2=sdfTime.parse(d2.getHora()+":"+d2.getMinutos());						
						} catch (Exception e) { }
						return h1.toString().compareToIgnoreCase(h2.toString());				
					}
		        });					
									
				for (int j = 0; j < lista_horarios.size(); j++) {
					TblHorarioMedicina tabla_horarios = lista_horarios.get(j);					
					TblHorarioMedicina childrenHorarios = new TblHorarioMedicina(tabla_horarios.getHora(), tabla_horarios.getMinutos(), tabla_horarios.getDomingo(), tabla_horarios.getLunes(), 
																				 tabla_horarios.getMartes(), tabla_horarios.getMiercoles(), tabla_horarios.getJueves(), tabla_horarios.getViernes(), 
																				 tabla_horarios.getSabado(), tabla_horarios.getActDesAlarma());        		
			        horariosList.add(childrenHorarios);					
				}		
				TblControlMedicina grupoMedicinas = new TblControlMedicina(tabla_medicinas.getIdControlMedicina(), tabla_medicinas.getMedicamento(), tabla_medicinas.getTiempo(), 
																		   tabla_medicinas.getDosis(), tabla_medicinas.getNdeVeces(), horariosList);
				medicinasList.append(i, grupoMedicinas);					
			}			
			miAdapter = new AdapterListaMedicinasHorarios(getActivity(), medicinasList);		    
		}
	}
	
	public void BtnNuevoCM() {
		BtnNuevoCM.setText(R.string.NuevoCM);
		BtnNuevoCM.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					intent = new Intent(getActivity(), NewEditControlMedicinaActivity.class);
					EnviarParametrosFragMenPacCM();
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
    	    titulo=(TblControlMedicina)ListCM.getExpandableListAdapter().getGroup(position);   	    
    	    menu.setHeaderTitle(getString(R.string.Medicamento)+" "+titulo.getMedicamento());
    	    menu.setHeaderIcon(R.drawable.medicina);
    	}
    	super.onCreateContextMenu(menu, v, menuInfo);
    }
	
    public boolean onContextItemSelected(MenuItem item) {    	
    	datos=Select.from(TblControlMedicina.class).where(Condition.prop("id_control_medicina").eq(titulo.getIdControlMedicina())).first();   	
    	switch(item.getItemId()) {
    	case R.id.Editar:  
    		intent = new Intent(getActivity(), NewEditControlMedicinaActivity.class);
    		EnviarParametrosFragMenPacCM2();    		     		
    		startActivityForResult(intent, request_code);
            banderaEditar=true;
            return true;    	
    	case R.id.Eliminar:  
    		FragmentManager fragmentManager = getFragmentManager();
			DFEliminarCM dialogo1 = new DFEliminarCM();
		    dialogo1.show(fragmentManager, "tagConfirmacion");
    		return true;    	
    	default:
    		return super.onContextItemSelected(item);
    	}
    }

	@SuppressLint("ValidFragment")
	public class DFEliminarCM extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage(R.string.EliminarCM)
					.setTitle(R.string.ConfEliminacion)
					.setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_alert))
					.setPositiveButton(R.string.Aceptar, new DialogInterface.OnClickListener()  {
						public void onClick(DialogInterface dialog, int id) {
							//VERIFICAR SI EL CUIDADOR QUE ELIMINA EL REGISTRO DE MEDICINA TIENE EL PERMISO DE MEDICINAS ACTIVADO
							//SI ESTA ACTIVADO SE LE ELIMINARA DEL CELL LAS ALARMAS DE LA MEDICINA
							TblPermisos verPermiso = Select.from(TblPermisos.class).where(Condition.prop("id_cuidador").eq(vIdCuidador),
																						Condition.prop("id_paciente").eq(datos.getIdPaciente()),
																						Condition.prop("cont_medicina").eq(1),
																						Condition.prop("Eliminado").eq(0)).first();
							if (verPermiso!=null) {
								//ELIMINAR DEL CELL ALARMAS DE LA MEDICINA
								MiTblMedicina.EliminarAlarmasMedicina(getActivity(), datos.getIdControlMedicina());
							}

							//FINALMENTE ELIMINAR LOS REGISTROS DE LA TABLA CONTROL DE MEDICINA
							ATControlMedicina contMed = new ATControlMedicina();
							contMed.new EliminarControlMedicina().execute(datos.getIdControlMedicina().toString());
							medicinasList.removeAt(medicinasList.indexOfKey(position));
							medicinasList.clear();
							//ACTUALIZAR LISTA
							ListaMedicinasHorarios tarea1 = new ListaMedicinasHorarios();
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
			ListaMedicinasHorarios tarea1 = new ListaMedicinasHorarios();
	        tarea1.execute();
		}
	}
		
	
	//ASYNCTASK
	
	
	private class ListaMedicinasHorarios extends AsyncTask<Void, Void, AdapterListaMedicinasHorarios>{		
		@Override
		protected AdapterListaMedicinasHorarios doInBackground(Void... arg0) {		
			try{				
				CargarListaMedicinasHorarios();
			}catch(Exception ex){
				ex.printStackTrace();
			}						
			return miAdapter;
		}

		@Override
		protected void onPostExecute(AdapterListaMedicinasHorarios result) {		
			super.onPostExecute(result);			
			//LISTA CON DATOS
			if (!lista_medicinas.isEmpty()) {
				ListCM.setAdapter(miAdapter);
			    TxtNoHayDatos.setText("");
			    ImageFondo.setImageDrawable(null);
			}			
			//LISTA VACIA
			if (lista_medicinas.isEmpty()) {
				miAdapter=null;
				ListCM.setAdapter(miAdapter);
				TxtNoHayDatos.setText(R.string.NoHayControlesMedicinas);
				ImageFondo.setImageDrawable(getResources().getDrawable(R.drawable.medicina2));
			}						
		}		
	}		
    

}