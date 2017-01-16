package com.Notifications.patientssassistant.fragments;


import com.Notifications.patientssassistant.*;
import com.Notifications.patientssassistant.adapters.*;
import com.Notifications.patientssassistant.tables.*;
import com.Notifications.patientssassistant.asynctask.*;
import java.text.SimpleDateFormat;
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
import android.view.MenuItem.OnMenuItemClickListener;
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


public class TabAP2RutinasPacF extends Fragment {
	
	public final static String KEY_REG_TEXT1 = "IdCuidador";
	public final static String KEY_REG_TEXT2 = "IdPaciente";
	public final static String KEY_REG_TEXT3 = "ControlT";
	public final static String KEY_REG_TEXT4 = "NotiAlar";
	private static Long vIdCuidador;
	private static Long vItemIdPaciente;
	private static Boolean vControlT;
	private static Boolean vNotiAlar;
	private int request_code = 1;
			
	//VARIABLES AUXILIARES
	private FragmentIterationListener mCallback = null;
	private static Boolean banderaNuevo=false;
	private static Boolean banderaEditar=false;
	private SparseArray<TblRutinasPacientes> Grupos = new SparseArray<TblRutinasPacientes>();
	private List<TblRutinasPacientes> lista_rp;
	private AdapterListaRutinasPacientes miAdapter;
	private TblRutinasPacientes datos;
	private TblRutinasPacientes titulo;
	private Intent intent;
	private int position;
	private static SimpleDateFormat sdfTime =  new  SimpleDateFormat ("HH:mm");
		
	//VARIABLES DE LOS ELEMENTOS DE LA IU
	Button BtnNuevoRP;
	ExpandableListView ListRP;
	ImageView ImageFondo;
	TextView TxtNoHayDatos;
	
		
	public interface FragmentIterationListener{
        public void onFragmentIteration(Bundle parameters);
    }
	
	public TabAP2RutinasPacF() {super();}
	
	public static TabAP2RutinasPacF newInstance(Long c_idCuidador, Long c_idPaciente, Boolean c_controlT, Boolean c_notiAlar) {
		TabAP2RutinasPacF frag = new TabAP2RutinasPacF();
		Bundle args = frag.getArguments();
		if (args == null)
			args = new Bundle();
		args.putLong(KEY_REG_TEXT1, c_idCuidador);
		args.putLong(KEY_REG_TEXT2, c_idPaciente);
		args.putBoolean(KEY_REG_TEXT3, c_controlT);
		args.putBoolean(KEY_REG_TEXT4, c_notiAlar);
		frag.setArguments(args);
		return frag;
	}
	
	public void RecogerParametrosFragTabAgePac2() {	
		vIdCuidador = getArguments().getLong(KEY_REG_TEXT1);
		vItemIdPaciente = getArguments().getLong(KEY_REG_TEXT2);
		vControlT = getArguments().getBoolean(KEY_REG_TEXT3);
		vNotiAlar = getArguments().getBoolean(KEY_REG_TEXT4);
	}
	
	public void EnviarParametrosFragTabRP() {
		TblPacientes datos_paciente= Select.from(TblPacientes.class).where(Condition.prop("id_paciente").eq(vItemIdPaciente)).first();
		intent.putExtra("varIdCuidador", vIdCuidador);
		intent.putExtra("varItemIdPaciente", vItemIdPaciente);
		intent.putExtra("varNombrePaciente", datos_paciente.getNombreApellidoP());	
		intent.putExtra("varFotoPaciente", datos_paciente.getFotoP());	
	}
	
	public void EnviarParametrosFragTabRP2() { 
		EnviarParametrosFragTabRP();
		intent.putExtra("varIdRutinaP", datos.getIdRutinaP());
		intent.putExtra("varIdActividad", datos.getIdActividad()); 
		intent.putExtra("varHora", datos.getHora());
		intent.putExtra("varMinutos", datos.getMinutos());
		intent.putExtra("varDomingo", datos.getDomingo());
		intent.putExtra("varLunes", datos.getLunes()); 
		intent.putExtra("varMartes", datos.getMartes());
		intent.putExtra("varMiercoles", datos.getMiercoles());	
		intent.putExtra("varJueves", datos.getJueves()); 
		intent.putExtra("varViernes", datos.getViernes());
		intent.putExtra("varSabados", datos.getSabado());
		intent.putExtra("varTono", datos.getTono());
		intent.putExtra("varDescripcion", datos.getDescripcion());
		intent.putExtra("varAlarma", datos.getAlarma());
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
        	BtnNuevoRP=(Button)rootView.findViewById(R.id.btnNuevoRegistro);
        	ListRP=(ExpandableListView)rootView.findViewById(R.id.listViewExp);     
        	ImageFondo=(ImageView)rootView.findViewById(R.id.imageView1);
        	TxtNoHayDatos=(TextView)rootView.findViewById(R.id.txtNoHayDatos);
	    }
        return rootView;
    }		
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {		
		super.onViewCreated(view, savedInstanceState);

		RecogerParametrosFragTabAgePac2();
		BtnNuevoRP();		
		//CREANDO LA LISTA EXPANDIBLE
		ListRP.setGroupIndicator(null);	
		ListaRutinasPaciente tarea1 = new ListaRutinasPaciente();
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
		if (vControlT.equals(true) || vNotiAlar.equals(true)) { registerForContextMenu(ListRP); }  //ACTIVA EL MENU CONTEXTUAL
		if (vControlT.equals(false) && vNotiAlar.equals(false))  { BtnNuevoRP.setEnabled(false); }  //DESHABILITAR EL BOTON PARA AGREGAR NUEVA RUTINA
	}

	public void CargarListaRutinasPaciente() {		
		lista_rp= Select.from(TblRutinasPacientes.class).where(Condition.prop("id_paciente").eq(vItemIdPaciente),
															   Condition.prop("Eliminado").eq(0)).list();	
		//LISTA CON DATOS	
		if (!lista_rp.isEmpty()) {
			Collections.sort(lista_rp, new Comparator<TblRutinasPacientes>(){  //ORDENA LA LISTA
				@Override
				public int compare(TblRutinasPacientes d1, TblRutinasPacientes d2) {
					Date h1=null, h2=null;					
					try {
						h1=sdfTime.parse(d1.getHora()+":"+d1.getMinutos());
						h2=sdfTime.parse(d2.getHora()+":"+d2.getMinutos());						
					} catch (Exception e) { }
					return h1.toString().compareToIgnoreCase(h2.toString());				
				}
	        });			
							
			int i=0;
			while(lista_rp.size() > i) {
				TblRutinasPacientes unaRegistro=new TblRutinasPacientes();
				unaRegistro=lista_rp.get(i);			
				TblRutinasPacientes GrupoItem=new TblRutinasPacientes(unaRegistro.getIdRutinaP(), unaRegistro.getIdActividad(), unaRegistro.getHora(), unaRegistro.getMinutos(),
																	  unaRegistro.getDomingo(), unaRegistro.getLunes(), unaRegistro.getMartes(), unaRegistro.getMiercoles(), 
																	  unaRegistro.getJueves(), unaRegistro.getViernes(), unaRegistro.getSabado(), unaRegistro.getAlarma());
				TblRutinasPacientes GrupoSubItem=new TblRutinasPacientes(unaRegistro.getDescripcion(), unaRegistro.getTono());								
				GrupoItem.Children.add(GrupoSubItem);				
				Grupos.append(i, GrupoItem);
				i++;				
			}
			
			miAdapter = new AdapterListaRutinasPacientes(getActivity(), Grupos);				
		}
	}
	
	public void BtnNuevoRP() {
		BtnNuevoRP.setText(R.string.NuevaR);
		BtnNuevoRP.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					intent = new Intent(getActivity(), NewEditRutinaPacienteActivity.class);
					EnviarParametrosFragTabRP();
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
    	    titulo=(TblRutinasPacientes)ListRP.getExpandableListAdapter().getGroup(position);
    	    menu.setHeaderTitle(R.string.Rutina); 
    	    menu.setHeaderIcon(R.drawable.alarm);
    	}
    	OnMenuItemClickListener listener = new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onContextItemSelected(item);
                return true;
            }
        };
        for (int i = 0, n = menu.size(); i < n; i++) {
        	menu.getItem(i).setOnMenuItemClickListener(listener);        	
        }            
    	super.onCreateContextMenu(menu, v, menuInfo);
    }
	
    public boolean onContextItemSelected(MenuItem item) {
    	datos=Select.from(TblRutinasPacientes.class).where(Condition.prop("id_rutina_p").eq(titulo.getIdRutinaP())).first();    	 	
    	switch(item.getItemId()) {
    	case R.id.Editar:  
    		intent = new Intent(getActivity(), NewEditRutinaPacienteActivity.class);
    		EnviarParametrosFragTabRP2();    		     		
    		startActivityForResult(intent, request_code);
            banderaEditar=true;
            return true;    	
    	case R.id.Eliminar:   
    		FragmentManager fragmentManager = getFragmentManager();
			DFEliminarRuP dialogo1 = new DFEliminarRuP();
		    dialogo1.show(fragmentManager, "tagConfirmacion");    		  		
    		return true;    	
    	default:
    		return super.onContextItemSelected(item);
    	}
    }

	@SuppressLint("ValidFragment")
	public class DFEliminarRuP extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage(R.string.EliminarRu)
					.setTitle(R.string.ConfEliminacion)
					.setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_alert))
					.setPositiveButton(R.string.Aceptar, new DialogInterface.OnClickListener()  {
						public void onClick(DialogInterface dialog, int id) {
							//VERIFICAR SI EL CUIDADOR QUE ELIMINA EL REGISTRO DE RUTINA TIENE EL PERMISO DE ALARMA ACTIVADO
							//SI ESTA ACTIVADO ELIMINAR DEL CELL LA ALARMA
							TblPermisos verPermiso = Select.from(TblPermisos.class).where(Condition.prop("id_cuidador").eq(vIdCuidador),
																						Condition.prop("id_paciente").eq(datos.getIdPaciente()),
																						Condition.prop("notifi_alarma").eq(1),
																						Condition.prop("Eliminado").eq(0)).first();
							if (verPermiso!=null) {
								//ELIMINAR DEL CELL ALARMA DE RUTINA
								MiTblRutina.EliminarAlarmaRutina(getActivity(), datos.getIdRutinaP(), "P");
							}
							//ELIMINAR RUTINA/PACIENTE DE LA TABLA
							ATRutinasPacientes rutP=new ATRutinasPacientes();
							rutP.new EliminarRutinasPacientes().execute(datos.getIdRutinaP().toString());
							Grupos.removeAt(Grupos.indexOfKey(position));
							Grupos.clear();
							//ACTUALIZAR LISTA
							ListaRutinasPaciente tarea1 = new ListaRutinasPaciente();
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
			ListaRutinasPaciente tarea1 = new ListaRutinasPaciente();
	        tarea1.execute();
		}
	}
	
	
	//ASYNCTASK
	
	
	private class ListaRutinasPaciente extends AsyncTask<Void, Void, AdapterListaRutinasPacientes>{		
		@Override
		protected AdapterListaRutinasPacientes doInBackground(Void... arg0) {		
			try{				
				CargarListaRutinasPaciente();
			}catch(Exception ex){
				ex.printStackTrace();
			}						
			return miAdapter;
		}

		@Override
		protected void onPostExecute(AdapterListaRutinasPacientes result) {		
			super.onPostExecute(result);			
			//LISTA CON DATOS
			if (!lista_rp.isEmpty()) {
				ListRP.setAdapter(miAdapter);
				TxtNoHayDatos.setText("");	
				ImageFondo.setImageDrawable(null);
			}
			//LISTA VACIA
			if (lista_rp.isEmpty()) {
				miAdapter=null;
				ListRP.setAdapter(miAdapter);
				TxtNoHayDatos.setText(R.string.NoHayRutinas);
				ImageFondo.setImageDrawable(getResources().getDrawable(R.drawable.alarm2));
			}		
		}		
	}		
	

}