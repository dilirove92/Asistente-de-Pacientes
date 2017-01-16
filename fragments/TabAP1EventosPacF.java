package com.Notifications.patientssassistant.fragments;


import com.Notifications.patientssassistant.*;
import com.Notifications.patientssassistant.adapters.*;
import com.Notifications.patientssassistant.tables.*;
import com.Notifications.patientssassistant.asynctask.*;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
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
import android.view.MenuItem.OnMenuItemClickListener;
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


public class TabAP1EventosPacF extends Fragment {
	
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
	private SparseArray<TblEventosPacientes> Grupos = new SparseArray<TblEventosPacientes>();	
	private List<TblEventosPacientes> lista_ep;
	private AdapterListaEventosPacientes miAdapter;
	private TblEventosPacientes datos;
	private TblEventosPacientes titulo;
	private Intent intent;
	private int position;	
	private static SimpleDateFormat sdfDateTime =  new  SimpleDateFormat ("dd/MM/yyyy HH:mm");
		
	//VARIABLES DE LOS ELEMENTOS DE LA IU
	Button BtnNuevoEP;
	ExpandableListView ListEP;
	ImageView ImageFondo;
	TextView TxtNoHayDatos;
	
	
	public interface FragmentIterationListener{
        public void onFragmentIteration(Bundle parameters);
    }
	
	public TabAP1EventosPacF() {super();}
	
	public static TabAP1EventosPacF newInstance(Long c_idCuidador, Long c_idPaciente, Boolean c_controlT, Boolean c_notiAlar) {
		TabAP1EventosPacF frag = new TabAP1EventosPacF();
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
	
	public void RecogerParametrosFragTabAgePac1() {
		vIdCuidador = getArguments().getLong(KEY_REG_TEXT1);
		vItemIdPaciente = getArguments().getLong(KEY_REG_TEXT2);
		vControlT = getArguments().getBoolean(KEY_REG_TEXT3);
		vNotiAlar = getArguments().getBoolean(KEY_REG_TEXT4);
	}
	
	public void EnviarParametrosFragTabEP() {
		TblPacientes datos_paciente= Select.from(TblPacientes.class).where(Condition.prop("id_paciente").eq(vItemIdPaciente)).first();
		intent.putExtra("varIdCuidador", vIdCuidador);
		intent.putExtra("varItemIdPaciente", vItemIdPaciente);
		intent.putExtra("varNombrePaciente", datos_paciente.getNombreApellidoP());	
		intent.putExtra("varFotoPaciente", datos_paciente.getFotoP());
	}
	
	public void EnviarParametrosFragTabEP2() { 
		EnviarParametrosFragTabEP();
		intent.putExtra("varIdEventoP", datos.getIdEventoP());
		intent.putExtra("varIdActividad", datos.getIdActividad());
		intent.putExtra("varAnioE", datos.getAnioE());
		intent.putExtra("varMesE", datos.getMesE());
		intent.putExtra("varDiaE", datos.getDiaE());
		intent.putExtra("varHoraE", datos.getHoraE());
		intent.putExtra("varMinutosE", datos.getMinutosE());
		intent.putExtra("varAnioR", datos.getAnioR());
		intent.putExtra("varMesR", datos.getMesR());
		intent.putExtra("varDiaR", datos.getDiaR());
		intent.putExtra("varHoraR", datos.getHoraR());
		intent.putExtra("varMinutosR", datos.getMinutosR());
		intent.putExtra("varLugar", datos.getLugar());
		intent.putExtra("varDescripcion", datos.getDescripcion());	
		intent.putExtra("varTono", datos.getTono());
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
        	BtnNuevoEP=(Button)rootView.findViewById(R.id.btnNuevoRegistro);
        	ListEP=(ExpandableListView)rootView.findViewById(R.id.listViewExp);  
        	ImageFondo=(ImageView)rootView.findViewById(R.id.imageView1);
        	TxtNoHayDatos=(TextView)rootView.findViewById(R.id.txtNoHayDatos);
	    }
        return rootView;
    }		
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {		
		super.onViewCreated(view, savedInstanceState);
		
		RecogerParametrosFragTabAgePac1();
		BtnNuevoEP();		
		//CREANDO LA LISTA EXPANDIBLE
		ListEP.setGroupIndicator(null);
		ListaEventosPaciente tarea1 = new ListaEventosPaciente();
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
		if (vControlT.equals(true) || vNotiAlar.equals(true)) { registerForContextMenu(ListEP); }  //ACTIVA EL MENU CONTEXTUAL
		if (vControlT.equals(false) && vNotiAlar.equals(false))  { BtnNuevoEP.setEnabled(false); }  //DESHABILITAR EL BOTON PARA AGREGAR NUEVO EVENTO
	}

	public void CargarListaEventosPaciente() {		
			lista_ep= Select.from(TblEventosPacientes.class).where(Condition.prop("id_paciente").eq(vItemIdPaciente),
																   Condition.prop("Eliminado").eq(0)).list();
			//LISTA CON DATOS	
			if (!lista_ep.isEmpty()) {
				Collections.sort(lista_ep, new Comparator<TblEventosPacientes>(){  //ORDENA LA LISTA
					@Override
					public int compare(TblEventosPacientes d1, TblEventosPacientes d2) {
						Long fh1=null, fh2=null;					
						try {							
							fh1=sdfDateTime.parse(d1.getDiaE()+"/"+d1.getMesE()+"/"+d1.getAnioE()+" "+d1.getHoraE()+":"+d1.getMinutosE()).getTime();
							fh2=sdfDateTime.parse(d2.getDiaE()+"/"+d2.getMesE()+"/"+d2.getAnioE()+" "+d2.getHoraE()+":"+d2.getMinutosE()).getTime();					
						} catch (Exception e) { }
						return fh1.toString().compareToIgnoreCase(fh2.toString());				
					}
		        });
				
				int i=0;
				while(lista_ep.size() > i) {					
					TblEventosPacientes unaRegistro=new TblEventosPacientes();
					unaRegistro=lista_ep.get(i);			
					TblEventosPacientes GrupoItem=new TblEventosPacientes(unaRegistro.getIdEventoP(), unaRegistro.getIdActividad(), unaRegistro.getAnioE(), unaRegistro.getMesE(), 
																		  unaRegistro.getDiaE(), unaRegistro.getHoraE(), unaRegistro.getMinutosE(), unaRegistro.getAlarma());
					TblEventosPacientes GrupoSubItem=new TblEventosPacientes(unaRegistro.getAnioR(), unaRegistro.getMesR(), unaRegistro.getDiaR(), unaRegistro.getHoraR(), 
																			 unaRegistro.getMinutosR(), unaRegistro.getLugar(), unaRegistro.getDescripcion(), unaRegistro.getTono());								
					GrupoItem.Children.add(GrupoSubItem);				
					Grupos.append(i, GrupoItem);
					i++;				
				}
				
				miAdapter = new AdapterListaEventosPacientes(getActivity(), Grupos);				
			}
	}
	
	public void BtnNuevoEP() {
		BtnNuevoEP.setText(R.string.NuevoE);
		BtnNuevoEP.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					intent = new Intent(getActivity(), NewEditEventoPacienteActivity.class);
					EnviarParametrosFragTabEP();
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
    	    titulo=(TblEventosPacientes)ListEP.getExpandableListAdapter().getGroup(position);
    	    menu.setHeaderTitle(R.string.Evento);
    	    menu.setHeaderIcon(R.drawable.date_time);
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
    	datos=Select.from(TblEventosPacientes.class).where(Condition.prop("id_evento_p").eq(titulo.getIdEventoP())).first();    	
    	switch(item.getItemId()) {
    	case R.id.Editar:  
    		intent = new Intent(getActivity(), NewEditEventoPacienteActivity.class);
    		EnviarParametrosFragTabEP2();    		     		
    		startActivityForResult(intent, request_code);
            banderaEditar=true;
            return true;    	
    	case R.id.Eliminar:  
    		FragmentManager fragmentManager = getFragmentManager();
			DFEliminarEvP dialogo1 = new DFEliminarEvP();
		    dialogo1.show(fragmentManager, "tagConfirmacion");
		    return true;    	
    	default:
    		return super.onContextItemSelected(item);
    	}
    }

	@SuppressLint("ValidFragment")
	public class DFEliminarEvP extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage(R.string.EliminarEv)
					.setTitle(R.string.ConfEliminacion)
					.setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_alert))
					.setPositiveButton(R.string.Aceptar, new DialogInterface.OnClickListener()  {
						public void onClick(DialogInterface dialog, int id) {
							//VERIFICAR SI EL CUIDADOR QUE ELIMINA EL REGISTRO DE EVENTO TIENE EL PERMISO DE ALARMA ACTIVADO
							//SI ESTA ACTIVADO ELIMINAR DEL CELL LA ALARMA
							TblPermisos verPermiso = Select.from(TblPermisos.class).where(Condition.prop("id_cuidador").eq(vIdCuidador),
																						  Condition.prop("id_paciente").eq(datos.getIdPaciente()),
																						  Condition.prop("notifi_alarma").eq(1),
																						  Condition.prop("Eliminado").eq(0)).first();
							if (verPermiso!=null) {
								//ELIMINAR DEL CELL ALARMA DE EVENTO
								MiTblEvento.EliminarAlarmaEvento(getActivity(), datos.getIdEventoP(), "P");
							}
							//ELIMINAR EVENTO/PACIENTE DE LA TABLA
							ATEventosPaciente eventP = new ATEventosPaciente();
							eventP.new EliminarEventoCuidador().execute(datos.getIdEventoP().toString());
							Grupos.removeAt(Grupos.indexOfKey(position));
							Grupos.clear();
							//ACTUALIZAR LISTA
							ListaEventosPaciente tarea1 = new ListaEventosPaciente();
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
			ListaEventosPaciente tarea1 = new ListaEventosPaciente();
	        tarea1.execute();
		}
	}
		
	
	//ASYNCTASK
	
	
	private class ListaEventosPaciente extends AsyncTask<Void, Void, AdapterListaEventosPacientes>{		
		@Override
		protected AdapterListaEventosPacientes doInBackground(Void... arg0) {		
			try{				
				CargarListaEventosPaciente();
			}catch(Exception ex){
				ex.printStackTrace();
			}						
			return miAdapter;
		}

		@Override
		protected void onPostExecute(AdapterListaEventosPacientes result) {		
			super.onPostExecute(result);			
			//LISTA CON DATOS
			if (!lista_ep.isEmpty()) {
				ListEP.setAdapter(miAdapter);
				TxtNoHayDatos.setText("");	
				ImageFondo.setImageDrawable(null);
			}
			//LISTA VACIA
			if (lista_ep.isEmpty()) {
				miAdapter=null;
				ListEP.setAdapter(miAdapter);
				TxtNoHayDatos.setText(R.string.NoHayEventos);
				ImageFondo.setImageDrawable(getResources().getDrawable(R.drawable.date_time2));
			}		
		}		
	}		
    

}