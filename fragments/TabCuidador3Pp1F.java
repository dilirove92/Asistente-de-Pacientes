package com.Notifications.patientssassistant.fragments;


import com.Notifications.patientssassistant.*;
import com.Notifications.patientssassistant.adapters.*;
import com.Notifications.patientssassistant.asynctask.*;
import com.Notifications.patientssassistant.tables.*;
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
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.orm.query.Condition;
import com.orm.query.Select;
import android.annotation.SuppressLint;


public class TabCuidador3Pp1F extends Fragment {
	
	public final static String KEY_REG_TEXT1 = "IdCuidador";
	private static Long vIdCuidador;
	private int request_code = 1;
			
	//VARIABLES AUXILIARES
	private FragmentIterationListener mCallback = null;
	private static Boolean banderaEditar=false;
	private AdapterListaPermisos miAdapter;
	private List<TblPermisos> lista_pp;
	private TblPermisos datos;
	private Intent intent;
	private int position;
			
	//VARIABLES DE LOS ELEMENTOS DE LA IU
	TextView TxtPacientes;
	TextView TxtVNota;
	ListView ListPP;
	ImageView ImageFondo;
	TextView TxtNoHayDatos;
		
	
	public interface FragmentIterationListener{
        public void onFragmentIteration(Bundle parameters);
    }
	
	public TabCuidador3Pp1F() {super();}
	
	public static TabCuidador3Pp1F newInstance(Long c_idCuidador) {
		TabCuidador3Pp1F frag = new TabCuidador3Pp1F();
		Bundle args = frag.getArguments();
		if (args == null)
			args = new Bundle();
		args.putLong(KEY_REG_TEXT1, c_idCuidador);
		frag.setArguments(args);
		return frag;
	}
	
	public void RecogerParametrosFragMpMiPefil3() {	
		vIdCuidador = getArguments().getLong(KEY_REG_TEXT1);		
	}

	public void EnviarParametrosFragTabCuiPp() {
		TblCuidador datos_cuidador= Select.from(TblCuidador.class).where(Condition.prop("id_cuidador").eq(vIdCuidador)).first();
		intent.putExtra("varIdCuidador", vIdCuidador);
		intent.putExtra("varNombreCuidador", datos_cuidador.getNombreC());
		intent.putExtra("varFotoCuidador", datos_cuidador.getFotoC());
		intent.putExtra("varIdPermiso", datos.getIdPermiso());
		intent.putExtra("varIdPaciente", datos.getIdPaciente());
		intent.putExtra("varNotifiAlarma", datos.getNotifiAlarma());
		intent.putExtra("varContMedicina", datos.getContMedicina());
		intent.putExtra("varItemIdCuidador", vIdCuidador);
		intent.putExtra("varDependeDe", vIdCuidador);
	}

	//METODO QUE RETORNA "Editar"
	public static Boolean EsEditar() {
		Boolean resultado=banderaEditar;
		return resultado;
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
        				
        View rootView = inflater.inflate(R.layout.tab_cuidador_pp1, container, false);
        if(rootView != null){  
        	TxtPacientes=(TextView)rootView.findViewById(R.id.txtPacientes);
        	TxtVNota=(TextView)rootView.findViewById(R.id.txtVNota);
        	ListPP=(ListView)rootView.findViewById(android.R.id.list);
        	ImageFondo=(ImageView)rootView.findViewById(R.id.imageView1);
        	TxtNoHayDatos=(TextView)rootView.findViewById(R.id.txtNoHayDatos);
	    }
        return rootView;
    }		
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {		
		super.onViewCreated(view, savedInstanceState);
		
		RecogerParametrosFragMpMiPefil3();
		ListaPermisos tarea1 = new ListaPermisos();
		tarea1.execute();
		registerForContextMenu(ListPP);
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
			
	public void CargarListaPermisos() {			
		lista_pp = Select.from(TblPermisos.class).where(Condition.prop("id_cuidador").eq(vIdCuidador),
														Condition.prop("Eliminado").eq(0)).list();
		//LISTA CON DATOS
		if (!lista_pp.isEmpty()) {			
			miAdapter=new AdapterListaPermisos(getActivity(), lista_pp);

			Collections.sort(lista_pp, new Comparator<TblPermisos>(){  //ORDENA LA LISTA
				@Override
				public int compare(TblPermisos d1, TblPermisos d2) {						
					TblPacientes buscar_paciente1= Select.from(TblPacientes.class).where(Condition.prop("id_paciente").eq(d1.getIdPaciente())).first();
					TblPacientes buscar_paciente2= Select.from(TblPacientes.class).where(Condition.prop("id_paciente").eq(d2.getIdPaciente())).first();
					return buscar_paciente1.getNombreApellidoP().compareToIgnoreCase(buscar_paciente2.getNombreApellidoP());				
				}
	        });
		}
	}

	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		if (v.getId() == android.R.id.list) {
			position = ((AdapterView.AdapterContextMenuInfo) menuInfo).position;
			this.getActivity().getMenuInflater().inflate(R.menu.menu_contextual_edp_elp, menu);
			menu.setHeaderTitle(R.string.Permiso);
			menu.setHeaderIcon(R.drawable.permisos);
			datos=(TblPermisos)ListPP.getItemAtPosition(position);
		}
		MenuItem.OnMenuItemClickListener listener = new MenuItem.OnMenuItemClickListener() {
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
		switch(item.getItemId()) {
			case R.id.Editar:
				intent = new Intent(getActivity(), NewEditPermisoActivity.class);
				EnviarParametrosFragTabCuiPp();
				startActivityForResult(intent, request_code);
				banderaEditar=true;
				return true;
			case R.id.Eliminar:
				FragmentManager fragmentManager = getFragmentManager();
				DFEliminarPP dialogo1 = new DFEliminarPP();
				dialogo1.show(fragmentManager, "tagConfirmacion");
				return true;
			default:
				return super.onContextItemSelected(item);
		}
	}

	@SuppressLint("ValidFragment")
	public class DFEliminarPP extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage(R.string.EliminarPP)
					.setTitle(R.string.ConfEliminacion)
					.setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_alert))
					.setPositiveButton(R.string.Aceptar, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							//VERIFICAR SI EL CUIDADOR QUE ELIMINA EL REGISTRO TIENE EL PERMISO DE ALARMAS ACTIVADO
							//SI ESTA ACTIVADO SE ELIMINARAN LAS ALARMAS DE EVENTOS Y RUTINAS DEL CELL
							TblPermisos permisoAla = Select.from(TblPermisos.class).where(Condition.prop("id_cuidador").eq(vIdCuidador),
																						Condition.prop("id_paciente").eq(datos.getIdPaciente()),
																						Condition.prop("reg_creador").eq(1),
																						Condition.prop("notifi_alarma").eq(1),
																						Condition.prop("Eliminado").eq(0)).first();
							if (permisoAla!=null) {
								MiTblEvento.EliminarAlarmasEventosPac(getActivity(), datos.getIdPaciente());
								MiTblRutina.EliminarAlarmasRutinasPac(getActivity(), datos.getIdPaciente());
							}

							//VERIFICAR SI EL CUIDADOR QUE ELIMINA EL REGISTRO TIENE EL PERMISO DE MEDICINAS ACTIVADO
							//SI ESTA ACTIVADO SE ELIMINARAN LAS ALARMAS DE MEDICINAS DEL CELL
							TblPermisos permisoMed = Select.from(TblPermisos.class).where(Condition.prop("id_cuidador").eq(vIdCuidador),
																						Condition.prop("id_paciente").eq(datos.getIdPaciente()),
																						Condition.prop("reg_creador").eq(1),
																						Condition.prop("cont_medicina").eq(1),
																						Condition.prop("Eliminado").eq(0)).first();
							if (permisoMed!=null) {
								MiTblMedicina.EliminarAlarmasMedicinasPac(getActivity(), datos.getIdPaciente());
							}

							//EDITAR PERMISO DEL CUIDADOR PRIMARIO *OJO NO SE DEBE ELIMINAR ESE REGISTRO
							ATPermisos newPermiso= new ATPermisos();
							newPermiso.new ActualizarPermiso().execute(datos.getIdPermiso().toString(), "false", "false");

							//ACTUALIZAR LISTA
							ListaPermisos tarea1 = new ListaPermisos();
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
			banderaEditar=false;
			ListaPermisos tarea1 = new ListaPermisos();
			tarea1.execute();
		}
	}


	//ASYNCTASK


	private class ListaPermisos extends AsyncTask<Void, Void, AdapterListaPermisos> {
		@Override
		protected AdapterListaPermisos doInBackground(Void... arg0) {
			try{
				CargarListaPermisos();
			}catch(Exception ex){
				ex.printStackTrace();
			}
			return miAdapter;
		}

		@Override
		protected void onPostExecute(AdapterListaPermisos result) {
			super.onPostExecute(result);
			//LISTA CON DATOS
			if (!lista_pp.isEmpty()) {
				ListPP.setAdapter(miAdapter);
				TxtNoHayDatos.setText("");
				ImageFondo.setImageDrawable(null);
				//MOSTRAR TITULOS VERTICALES
				TxtVNota.setText(R.string.PermisosDesc);
				TxtPacientes.setText(R.string.Pacientes);
			}
			//LISTA VACIA
			if (lista_pp.isEmpty()) {
				ListPP.setAdapter(null);
				TxtNoHayDatos.setText(R.string.NoHayPermisos);
				ImageFondo.setImageDrawable(getResources().getDrawable(R.drawable.permisos2));
				//QUITAR TITULOS VERTICALES
				TxtVNota.setText("");
				TxtPacientes.setText("");
			}
		}
	}


}