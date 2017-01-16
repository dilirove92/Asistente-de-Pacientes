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


public class TabMA1EventosF extends Fragment {
	
	public final static String KEY_REG_TEXT = "IdCuidador";	
	private static Long vIdCuidador;
	private int request_code = 1;
			
	//VARIABLES AUXILIARES	
	private FragmentIterationListener mCallback = null;
	private static Boolean banderaNuevo=false;
	private static Boolean banderaEditar=false;	
	private SparseArray<TblEventosCuidadores> Grupos = new SparseArray<TblEventosCuidadores>();	
	private List<TblEventosCuidadores> lista_ec;
	private AdapterListaEventosCuidadores miAdapter;
	private TblEventosCuidadores datos;
	private TblEventosCuidadores titulo;	
	private Intent intent;
	private int position;
	private static SimpleDateFormat sdfDateTime =  new  SimpleDateFormat ("dd/MM/yyyy HH:mm");
			
	//VARIABLES DE LOS ELEMENTOS DE LA IU
	Button BtnNuevoE;
	ExpandableListView ListE;
	ImageView ImageFondo;
	TextView TxtNoHayDatos;
		
	
	public interface FragmentIterationListener{
        public void onFragmentIteration(Bundle parameters);
    }
	
	public TabMA1EventosF() {super();}
	
	public static TabMA1EventosF newInstance(Long c_idCuidador) {
		TabMA1EventosF frag = new TabMA1EventosF();
		Bundle args = frag.getArguments();
		if (args == null)
			args = new Bundle();
		args.putLong(KEY_REG_TEXT, c_idCuidador);
		frag.setArguments(args);
		return frag;
	}
	
	public void RecogerParametrosFragTabMiAge1() {	
		vIdCuidador = getArguments().getLong(KEY_REG_TEXT);
	}
	
	public void EnviarParametrosFragTabEC() {		
		intent.putExtra("varIdCuidador", vIdCuidador);
	}
	
	public void EnviarParametrosFragTabEC2() { 
		EnviarParametrosFragTabEC();
		intent.putExtra("varIdEventoC", datos.getIdEventoC());
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
		
		MiTblEvento miEvento = Select.from(MiTblEvento.class).where(Condition.prop("id_evento").eq(datos.getIdEventoC())).first();
		intent.putExtra("varIdAlarma", miEvento.getIdAlarmaClock());
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
        	BtnNuevoE=(Button)rootView.findViewById(R.id.btnNuevoRegistro);
        	ListE=(ExpandableListView)rootView.findViewById(R.id.listViewExp);  
        	ImageFondo=(ImageView)rootView.findViewById(R.id.imageView1);
        	TxtNoHayDatos=(TextView)rootView.findViewById(R.id.txtNoHayDatos);
	    }
        return rootView;
    }		
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {		
		super.onViewCreated(view, savedInstanceState);
		
		RecogerParametrosFragTabMiAge1();
		BtnNuevoEvento();		
		//CREANDO LA LISTA EXPANDIBLE		
		ListE.setGroupIndicator(null);
		registerForContextMenu(ListE);
		MiListaEventos tarea1 = new MiListaEventos();
        tarea1.execute();
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
	
	public void CargarMiListaEventos() {		
		lista_ec= Select.from(TblEventosCuidadores.class).where(Condition.prop("id_cuidador").eq(vIdCuidador),
		 													    Condition.prop("Eliminado").eq(0)).list();	
		//LISTA CON DATOS	
		if (!lista_ec.isEmpty()) {
			Collections.sort(lista_ec, new Comparator<TblEventosCuidadores>(){  //ORDENA LA LISTA
				@Override
				public int compare(TblEventosCuidadores d1, TblEventosCuidadores d2) {
					Long fh1=null, fh2=null;					
					try {							
						fh1=sdfDateTime.parse(d1.getDiaE()+"/"+d1.getMesE()+"/"+d1.getAnioE()+" "+d1.getHoraE()+":"+d1.getMinutosE()).getTime();
						fh2=sdfDateTime.parse(d2.getDiaE()+"/"+d2.getMesE()+"/"+d2.getAnioE()+" "+d2.getHoraE()+":"+d2.getMinutosE()).getTime();					
					}catch (Exception e) { }
					return fh1.toString().compareToIgnoreCase(fh2.toString());				
				}
	        });
							
			int i=0;
			while(lista_ec.size() > i) {
				TblEventosCuidadores unaRegistro=new TblEventosCuidadores();
				unaRegistro=lista_ec.get(i);			
				TblEventosCuidadores GrupoItem=new TblEventosCuidadores(unaRegistro.getIdEventoC(), unaRegistro.getIdActividad(), unaRegistro.getAnioE(), unaRegistro.getMesE(), 
						  												unaRegistro.getDiaE(), unaRegistro.getHoraE(), unaRegistro.getMinutosE(), unaRegistro.getAlarma());
				TblEventosCuidadores GrupoSubItem=new TblEventosCuidadores(unaRegistro.getAnioR(), unaRegistro.getMesR(), unaRegistro.getDiaR(), unaRegistro.getHoraR(), 
																		   unaRegistro.getMinutosR(), unaRegistro.getLugar(), unaRegistro.getDescripcion(),
																		   unaRegistro.getTono());								
				GrupoItem.Children.add(GrupoSubItem);				
				Grupos.append(i, GrupoItem);
				i++;				
			}
			
			miAdapter = new AdapterListaEventosCuidadores(getActivity(), Grupos);
		}
	}
	
	public void BtnNuevoEvento() {
		BtnNuevoE.setText(R.string.NuevoE);
		BtnNuevoE.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					intent = new Intent(getActivity(), NewEditEventoCuidadorActivity.class);
					EnviarParametrosFragTabEC();
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
    	    titulo=(TblEventosCuidadores)ListE.getExpandableListAdapter().getGroup(position); 
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
    	datos=Select.from(TblEventosCuidadores.class).where(Condition.prop("id_evento_c").eq(titulo.getIdEventoC())).first();
    	switch(item.getItemId()) {
    	case R.id.Editar:  
    		intent = new Intent(getActivity(), NewEditEventoCuidadorActivity.class);
    		EnviarParametrosFragTabEC2();    		     		
    		startActivityForResult(intent, request_code);
            banderaEditar=true;
            return true;    	
    	case R.id.Eliminar:  
    		FragmentManager fragmentManager = getFragmentManager();
			DFEliminarEvC dialogo1 = new DFEliminarEvC();
		    dialogo1.show(fragmentManager, "tagConfirmacion");
		    return true;    	
    	default:
    		return super.onContextItemSelected(item);
    	}
    }

	@SuppressLint("ValidFragment")
	public class DFEliminarEvC extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage(R.string.EliminarEv)
					.setTitle(R.string.ConfEliminacion)
					.setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_alert))
					.setPositiveButton(R.string.Aceptar, new DialogInterface.OnClickListener()  {
						public void onClick(DialogInterface dialog, int id) {
							//ELIMINAR DEL CELL ALARMA DE EVENTO
							MiTblEvento.EliminarAlarmaEvento(getActivity(), datos.getIdEventoC(), "C");
							//ELIMINAR EVENTO/CUIDADOR DE LA TABLA
							ATEventosCuidador eventC= new ATEventosCuidador();
							eventC.new EliminarEventoCuidador().execute(datos.getIdEventoC().toString());
							Grupos.removeAt(Grupos.indexOfKey(position));
							Grupos.clear();
							//ACTUALIZAR LISTA
							MiListaEventos tarea1 = new MiListaEventos();
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
			MiListaEventos tarea1 = new MiListaEventos();
	        tarea1.execute();
		}
	}
	
	
	//ASYNCTASK
	
	
	private class MiListaEventos extends AsyncTask<Void, Void, AdapterListaEventosCuidadores>{		
		@Override
		protected AdapterListaEventosCuidadores doInBackground(Void... arg0) {		
			try{				
				CargarMiListaEventos();
			}catch(Exception ex){
				ex.printStackTrace();
			}						
			return miAdapter;
		}

		@Override
		protected void onPostExecute(AdapterListaEventosCuidadores result) {		
			super.onPostExecute(result);			
			//LISTA CON DATOS
			if (!lista_ec.isEmpty()) {
				ListE.setAdapter(miAdapter);
				TxtNoHayDatos.setText("");
				ImageFondo.setImageDrawable(null);
			}
			//LISTA VACIA
			if (lista_ec.isEmpty()) {
				miAdapter=null;
				ListE.setAdapter(miAdapter);
				TxtNoHayDatos.setText(R.string.NoHayEventos);
				ImageFondo.setImageDrawable(getResources().getDrawable(R.drawable.date_time2));
			}						
		}		
	}		
    

}