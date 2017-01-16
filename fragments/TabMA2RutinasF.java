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


public class TabMA2RutinasF extends Fragment {
	
	public final static String KEY_REG_TEXT = "IdCuidador";	
	private static Long vIdCuidador;
	private int request_code = 1;
			
	//VARIABLES AUXILIARES
	private FragmentIterationListener mCallback = null;
	private static Boolean banderaNuevo=false;
	private static Boolean banderaEditar=false;
	private SparseArray<TblRutinasCuidadores> Grupos = new SparseArray<TblRutinasCuidadores>();	
	private List<TblRutinasCuidadores> lista_rc;
	private AdapterListaRutinasCuidadores miAdapter;
	private TblRutinasCuidadores datos;
	private TblRutinasCuidadores titulo;	
	private Intent intent;
	private int position;
	private static SimpleDateFormat sdfTime =  new  SimpleDateFormat ("HH:mm");
			
	//VARIABLES DE LOS ELEMENTOS DE LA IU
	Button BtnNuevoRC;
	ExpandableListView ListRC;
	ImageView ImageFondo;
	TextView TxtNoHayDatos;
		
	
	public interface FragmentIterationListener{
        public void onFragmentIteration(Bundle parameters);
    }
	
	public TabMA2RutinasF() {super();}
	
	public static TabMA2RutinasF newInstance(Long c_idCuidador) {
		TabMA2RutinasF frag = new TabMA2RutinasF();
		Bundle args = frag.getArguments();
		if (args == null)
			args = new Bundle();
		args.putLong(KEY_REG_TEXT, c_idCuidador);
		frag.setArguments(args);
		return frag;
	}
	
	public void RecogerParametrosFragTabMiAge2() {	
		vIdCuidador = getArguments().getLong(KEY_REG_TEXT);
	}
	
	public void EnviarParametrosFragTabRC() {
		intent.putExtra("varIdCuidador", vIdCuidador);			
	}
	
	public void EnviarParametrosFragTabRC2() { 
		EnviarParametrosFragTabRC();
		intent.putExtra("varIdRutinaC", datos.getIdRutinaC());
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
		
		MiTblRutina miRutina = Select.from(MiTblRutina.class).where(Condition.prop("id_rutina").eq(datos.getIdRutinaC())).first();
		intent.putExtra("varIdAlarma", miRutina.getIdAlarmaClock());
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
        	BtnNuevoRC=(Button)rootView.findViewById(R.id.btnNuevoRegistro);
        	ListRC=(ExpandableListView)rootView.findViewById(R.id.listViewExp); 
        	ImageFondo=(ImageView)rootView.findViewById(R.id.imageView1);
        	TxtNoHayDatos=(TextView)rootView.findViewById(R.id.txtNoHayDatos);
	    }
        return rootView;
    }		
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {		
		super.onViewCreated(view, savedInstanceState);
				
		RecogerParametrosFragTabMiAge2();
		BtnNuevaRutina();		
		//CREANDO LA LISTA EXPANDIBLE		
		ListRC.setGroupIndicator(null);
		registerForContextMenu(ListRC);
		MiListaRutinas tarea1 = new MiListaRutinas();
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
	
	public void CargarMiListaRutinas() {
		lista_rc = Select.from(TblRutinasCuidadores.class).where(Condition.prop("id_cuidador").eq(vIdCuidador),
															     Condition.prop("Eliminado").eq(0)).list();	
		//LISTA CON DATOS	
		if (!lista_rc.isEmpty()) {
			Collections.sort(lista_rc, new Comparator<TblRutinasCuidadores>(){  //ORDENA LA LISTA
				@Override
				public int compare(TblRutinasCuidadores d1, TblRutinasCuidadores d2) {
					Date h1=null, h2=null;					
					try {
						h1=sdfTime.parse(d1.getHora()+":"+d1.getMinutos());
						h2=sdfTime.parse(d2.getHora()+":"+d2.getMinutos());						
					} catch (Exception e) {	}
					return h1.toString().compareToIgnoreCase(h2.toString());				
				}
	        });				
			
			int i=0;
			while(lista_rc.size() > i) {
				TblRutinasCuidadores unaRegistro=new TblRutinasCuidadores();
				unaRegistro=lista_rc.get(i);			
				TblRutinasCuidadores GrupoItem=new TblRutinasCuidadores(unaRegistro.getIdRutinaC(), unaRegistro.getIdActividad(), unaRegistro.getHora(), unaRegistro.getMinutos(), 
																		unaRegistro.getDomingo(), unaRegistro.getLunes(), unaRegistro.getMartes(), unaRegistro.getMiercoles(), 
																		unaRegistro.getJueves(), unaRegistro.getViernes(), unaRegistro.getSabado(), unaRegistro.getAlarma());
				TblRutinasCuidadores GrupoSubItem=new TblRutinasCuidadores(unaRegistro.getDescripcion(), unaRegistro.getTono());								
				GrupoItem.Children.add(GrupoSubItem);				
				Grupos.append(i, GrupoItem);
				i++;				
			}
			
			miAdapter = new AdapterListaRutinasCuidadores(getActivity(), Grupos);				
		}	
	}
	
	public void BtnNuevaRutina() {
		BtnNuevoRC.setText(R.string.NuevaR);
		BtnNuevoRC.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					intent = new Intent(getActivity(), NewEditRutinaCuidadorActivity.class);
					EnviarParametrosFragTabRC();
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
    	    titulo=(TblRutinasCuidadores)ListRC.getExpandableListAdapter().getGroup(position); 
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
    	datos=Select.from(TblRutinasCuidadores.class).where(Condition.prop("id_rutina_c").eq(titulo.getIdRutinaC())).first();
    	switch(item.getItemId()) {
    	case R.id.Editar:  
    		intent = new Intent(getActivity(), NewEditRutinaCuidadorActivity.class);
    		EnviarParametrosFragTabRC2();    		     		
    		startActivityForResult(intent, request_code);
            banderaEditar=true;
            return true;    	
    	case R.id.Eliminar:   
    		FragmentManager fragmentManager = getFragmentManager();
			DFEliminarRuC dialogo1 = new DFEliminarRuC();
		    dialogo1.show(fragmentManager, "tagConfirmacion");    		  		
    		return true;    	
    	default:
    		return super.onContextItemSelected(item);
    	}
    }

	@SuppressLint("ValidFragment")
	public class DFEliminarRuC extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage(R.string.EliminarRu)
					.setTitle(R.string.ConfEliminacion)
					.setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_alert))
					.setPositiveButton(R.string.Aceptar, new DialogInterface.OnClickListener()  {
						public void onClick(DialogInterface dialog, int id) {
							//ELIMINAR DEL CELL ALARMA DE RUTINA
							MiTblRutina.EliminarAlarmaRutina(getActivity(), datos.getIdRutinaC(), "C");
							//ELIMINAR RUTINA/CUIDADOR DE LA TABLA
							ATRutinasCuidadores rutC = new ATRutinasCuidadores();
							rutC.new EliminarRutinasCuidador().execute(datos.getIdRutinaC().toString());
							Grupos.removeAt(Grupos.indexOfKey(position));
							Grupos.clear();
							//ACTUALIZAR LISTA
							MiListaRutinas tarea1 = new MiListaRutinas();
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
			MiListaRutinas tarea1 = new MiListaRutinas();
            tarea1.execute();
		}
	}
		
	
	//ASYNCTASK
	
	
	private class MiListaRutinas extends AsyncTask<Void, Void, AdapterListaRutinasCuidadores>{		
		@Override
		protected AdapterListaRutinasCuidadores doInBackground(Void... arg0) {		
			try{				
				CargarMiListaRutinas();
			}catch(Exception ex){
				ex.printStackTrace();
			}						
			return miAdapter;
		}

		@Override
		protected void onPostExecute(AdapterListaRutinasCuidadores result) {		
			super.onPostExecute(result);			
			//LISTA CON DATOS
			if (!lista_rc.isEmpty()) {
				ListRC.setAdapter(miAdapter);
				TxtNoHayDatos.setText("");	
				ImageFondo.setImageDrawable(null);
			}
			//LISTA VACIA
			if (lista_rc.isEmpty()) {
				miAdapter=null;
				ListRC.setAdapter(miAdapter);
				TxtNoHayDatos.setText(R.string.NoHayRutinas);
				ImageFondo.setImageDrawable(getResources().getDrawable(R.drawable.alarm2));
			}					
		}		
	}		
    

}