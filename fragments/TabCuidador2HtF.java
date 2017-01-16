package com.Notifications.patientssassistant.fragments;


import com.Notifications.patientssassistant.*;
import com.Notifications.patientssassistant.tables.*;
import com.Notifications.patientssassistant.adapters.*;
import com.Notifications.patientssassistant.asynctask.*;
import com.orm.query.Condition;
import com.orm.query.Select;
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
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.annotation.SuppressLint;


public class TabCuidador2HtF extends Fragment {
	
	public final static String KEY_REG_TEXT1 = "IdCuidador";
	public final static String KEY_REG_TEXT2 = "ControlT";	
	private static Long vItemIdCuidador;
	private static Boolean vControlT;
	private int request_code = 1;
		
	//VARIABLES AUXILIARES
	private FragmentIterationListener mCallback = null;
	private static Boolean banderaNuevo=false;
	private static Boolean banderaEditar=false;	
	private AdapterListaHT miAdapter;
	private List<TblHorarios> lista_ht;
	private TblHorarios datos;
	private TblHorarios titulo;
	private Intent intent;
		
	//VARIABLES DE LOS ELEMENTOS DE LA IU
	Button BtnNuevoHT;
	ListView ListHT;
	ImageView ImageFondo;
	TextView TxtNoHayDatos;
		
	
	public interface FragmentIterationListener{
        public void onFragmentIteration(Bundle parameters);
    }
	
	public TabCuidador2HtF() {super();}
	
	public static TabCuidador2HtF newInstance(Long c_idCuidador, Boolean c_controlT) {
		TabCuidador2HtF frag = new TabCuidador2HtF();
		Bundle args = frag.getArguments();
		if (args == null)
			args = new Bundle();
		args.putLong(KEY_REG_TEXT1, c_idCuidador);
		args.putBoolean(KEY_REG_TEXT2, c_controlT);
		frag.setArguments(args);
		return frag;
	}
	
	public void RecogerParametrosFragTabCui2() {	
		vItemIdCuidador = getArguments().getLong(KEY_REG_TEXT1);
		vControlT = getArguments().getBoolean(KEY_REG_TEXT2);
	}
	
	public void EnviarParametrosFragTabCuiHt() {
		TblCuidador datos_cuidador= Select.from(TblCuidador.class).where(Condition.prop("id_cuidador").eq(vItemIdCuidador)).first();
		intent.putExtra("varItemIdCuidador", vItemIdCuidador);
		intent.putExtra("varNombreCuidador", datos_cuidador.getNombreC());	
		intent.putExtra("varFotoCuidador", datos_cuidador.getFotoC());	
	}
	
	public void EnviarParametrosFragTabCuiHt2() { 
		EnviarParametrosFragTabCuiHt();
		intent.putExtra("varIdHorario", datos.getIdHorario());
		intent.putExtra("varTipoHorario", datos.getTipoHorario());
		intent.putExtra("varHoraIni", datos.getHoraIni());
		intent.putExtra("varMinutosIni", datos.getMinutosIni());
		intent.putExtra("varHoraFin", datos.getHoraFin()); 
		intent.putExtra("varMinutosFin", datos.getMinutosFin());
		intent.putExtra("varDom", datos.getDomingo());
		intent.putExtra("varLun", datos.getLunes()); 
		intent.putExtra("varMar", datos.getMartes());
		intent.putExtra("varMie", datos.getMiercoles());     		
		intent.putExtra("varJue", datos.getJueves()); 
		intent.putExtra("varVie", datos.getViernes());
		intent.putExtra("varSab", datos.getSabado());        
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
        				
        View rootView = inflater.inflate(R.layout.pantalla_boton_lista, container, false);
        if(rootView != null){
        	BtnNuevoHT=(Button)rootView.findViewById(R.id.btnNuevoRegistro);
        	ListHT=(ListView)rootView.findViewById(android.R.id.list); 
        	ImageFondo=(ImageView)rootView.findViewById(R.id.imageView1);
        	TxtNoHayDatos=(TextView)rootView.findViewById(R.id.txtNoHayDatos);
	    }
        return rootView;
    }		
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {		
		super.onViewCreated(view, savedInstanceState);
		
		RecogerParametrosFragTabCui2();
		ListaHorarios tarea1 = new ListaHorarios();
        tarea1.execute();
		BtnNuevoHT();
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
		if (vControlT.equals(true)) { registerForContextMenu(ListHT); }  //ACTIVA EL MENU CONTEXTUAL
		if (vControlT.equals(false)) { BtnNuevoHT.setEnabled(false); }  //DESHABILITAR EL BOTON PARA NUEVO HORARIO DE TRABAJO	
	}
	
	public void CargarListaHorarios() {
		lista_ht= Select.from(TblHorarios.class).where(Condition.prop("id_cuidador").eq(vItemIdCuidador),
													   Condition.prop("Eliminado").eq(0)).orderBy("tipo_horario asc").list();
		//LISTA CON DATOS
		if (!lista_ht.isEmpty()) {
			miAdapter=new AdapterListaHT(getActivity(), lista_ht);		
		}
	}
		
	public void BtnNuevoHT() {
		BtnNuevoHT.setText(R.string.NuevoHT);
		BtnNuevoHT.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					intent = new Intent(getActivity(), NewEditHorarioTrabajoActivity.class);
					EnviarParametrosFragTabCuiHt();
					startActivityForResult(intent, request_code);
					banderaNuevo = true;
				} catch (Exception ex) {
					Toast.makeText(getActivity(), getString(R.string.Error) + ex.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}
		});	
	}
	
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {    	
    	if (v.getId() == android.R.id.list) {
    	    int position = ((AdapterContextMenuInfo) menuInfo).position;
    	    this.getActivity().getMenuInflater().inflate(R.menu.menu_contextual_ed_el, menu);
    	    titulo=(TblHorarios)ListHT.getItemAtPosition(position);
    	    menu.setHeaderTitle(titulo.getTipoHorario());
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
    	int position = ((AdapterContextMenuInfo) item.getMenuInfo()).position;
    	datos=(TblHorarios)ListHT.getItemAtPosition(position);    	
    	switch(item.getItemId()) {
    	case R.id.Editar:  
    		intent = new Intent(getActivity(), NewEditHorarioTrabajoActivity.class);
    		EnviarParametrosFragTabCuiHt2();    		     		
    		startActivityForResult(intent, request_code);
            banderaEditar=true;
            return true;    	
    	case R.id.Eliminar:    	
            FragmentManager fragmentManager = getFragmentManager();
			DFEliminarHT dialogo1 = new DFEliminarHT();
		    dialogo1.show(fragmentManager, "tagConfirmacion");	
    		return true;    	
    	default:
    		return super.onContextItemSelected(item);
    	}
    }

	@SuppressLint("ValidFragment")
	public class DFEliminarHT extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage(R.string.EliminarHT)
					.setTitle(R.string.ConfEliminacion)
					.setPositiveButton(R.string.Aceptar, new DialogInterface.OnClickListener()  {
						public void onClick(DialogInterface dialog, int id) {
							//ELIMINAR REGISTRO
							ATHorarios horario = new ATHorarios();
							horario.new EliminarHorario().execute(datos.getIdHorario().toString());
							//ACTUALIZAR LISTA
							ListaHorarios tarea1 = new ListaHorarios();
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
			ListaHorarios tarea1 = new ListaHorarios();
	        tarea1.execute();
		}
	}
	
	
	//ASYNCTASK
	
	
	private class ListaHorarios extends AsyncTask<Void, Void, AdapterListaHT>{		
		@Override
		protected AdapterListaHT doInBackground(Void... arg0) {		
			try{				
				CargarListaHorarios();
			}catch(Exception ex){
				ex.printStackTrace();
			}						
			return miAdapter;
		}

		@Override
		protected void onPostExecute(AdapterListaHT result) {		
			super.onPostExecute(result);
			//LISTA CON DATOS
			if (!lista_ht.isEmpty()) {				
				ListHT.setAdapter(miAdapter);
				TxtNoHayDatos.setText("");
				ImageFondo.setImageDrawable(null);
			}
			//LISTA VACIA
			if (lista_ht.isEmpty()) {
				ListHT.setAdapter(null);
				TxtNoHayDatos.setText(R.string.NoHayHorariosTrabajo);
				ImageFondo.setImageDrawable(getResources().getDrawable(R.drawable.horario2));
			}			
		}		
	}
	
	
}