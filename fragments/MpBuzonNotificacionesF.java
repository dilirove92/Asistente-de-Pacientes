package com.Notifications.patientssassistant.fragments;


import com.Notifications.patientssassistant.R;
import com.Notifications.patientssassistant.adapters.*;
import com.Notifications.patientssassistant.tables.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.annotation.SuppressLint;


public class MpBuzonNotificacionesF extends Fragment {
	
	public final static String KEY_REG_TEXT = "IdCuidador";	
	private static Long vIdCuidador;

	//VARIABLES AUXILIARES
	private FragmentIterationListener mCallback = null;
	private ArrayList<TblBuzon> BuzonList = new ArrayList<TblBuzon>();
	private ArrayList<TblBuzon> BuzonListLimpia = new ArrayList<TblBuzon>();
	private AdapterListaBuzon miAdapter;
	private List<TblBuzon> lista_buzon;
	private TblBuzon datos;

	//VARIABLES DE LOS ELEMENTOS DE LA IU
	Spinner CmbTipoOrdenar;	
	ListView LstBuzon;
	ImageView ImageFondo;
	TextView TxtNoHayDatos;
	ImageButton BtnActualizar;
		
	
	public interface FragmentIterationListener{
        public void onFragmentIteration(Bundle parameters);
    }
	
	public MpBuzonNotificacionesF() {super();}
	
	public static MpBuzonNotificacionesF newInstance(Long c_idCuidador) {
		MpBuzonNotificacionesF frag = new MpBuzonNotificacionesF();
		Bundle args = frag.getArguments();
		if (args == null)
			args = new Bundle();
		args.putLong(KEY_REG_TEXT, c_idCuidador);
		frag.setArguments(args);
		return frag;
	}
		
	public void RecogerParametrosActMP1() {		
		vIdCuidador = getArguments().getLong(KEY_REG_TEXT);	
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
		
		View rootView = inflater.inflate(R.layout.mp_buzon_notificaciones, container, false);	    
		if(rootView != null){
			CmbTipoOrdenar=(Spinner)rootView.findViewById(R.id.cmbTipoOrdenar);			
			LstBuzon=(ListView)rootView.findViewById(android.R.id.list);
			ImageFondo=(ImageView)rootView.findViewById(R.id.imageView1);
			TxtNoHayDatos=(TextView)rootView.findViewById(R.id.txtNoHayDatos);
			BtnActualizar=(ImageButton)rootView.findViewById(R.id.btnActualizar);
	    }		
        return rootView;
    }

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {		
		super.onViewCreated(view, savedInstanceState);
		
		RecogerParametrosActMP1();   
		CargarOpcSpinner();		
		SpinnerOrdenarPor();
		registerForContextMenu(LstBuzon);
		ActualizarBuzon();
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
	
	public void CargarOpcSpinner() {
		String[] OpcTipoOrden=getResources().getStringArray(R.array.opc_tipoOrdenar);						 
		CmbTipoOrdenar.setAdapter(new AdapterSpinnerSimple(getActivity(), R.layout.adaptador_spinner, OpcTipoOrden));
	}

	public void CargarAdaptador() {
		//AGRUPAR EN LISTA Y CONTAR ACTIVIDADES/PACIENTES REPETIDAS
		lista_buzon= Select.from(TblBuzon.class).where(Condition.prop("id_cuidador").eq(vIdCuidador),
				                                       Condition.prop("Eliminado").eq(0)).list();
		if (!lista_buzon.isEmpty()) {
			TblBuzon registro = new TblBuzon();
			TblBuzon Buzon;
			BuzonList.clear(); //PARA NO REPETIR DATOS

			for (int i = 0; i < lista_buzon.size()-1; i++) {
				int k = i + 1;
				int n = i;
				int contador = 0;
				while (n < lista_buzon.size()-1) {
					try{
						if ((lista_buzon.get(i).getIdPaciente().equals(lista_buzon.get(k).getIdPaciente())) &&
							(lista_buzon.get(i).getIdActividad().equals(lista_buzon.get(k).getIdActividad())))
						{
							contador = contador + 1;
						}
						k++;
						n++;
					} catch (Exception e) {
						Log.e("Error", e.getMessage());
					}
				}

				if (contador>0) {
					contador = contador + 1;
				}

				registro = lista_buzon.get(i);
				Buzon = new TblBuzon(registro.getIdCuidador(), registro.getIdPaciente(), registro.getIdActividad(), registro.getAnio(), registro.getMes(), registro.getDia(),
									 registro.getHoras(), registro.getMinutos(), registro.getEliminado(), contador, registro.getPostergar());
				BuzonList.add(Buzon);
			}
		}

		//ORDENAR LISTA TOMANDO LA ULTIMA FECHA
		if (!BuzonList.isEmpty()) {
			Boolean bandera = false;
			BuzonListLimpia.clear();  //PARA NO REPETIR DATOS
			BuzonListLimpia.add(BuzonList.get(0));

			for (int i = 0; i < BuzonList.size(); i++) {
				bandera = false;

				for (int k = 0; k < BuzonListLimpia.size(); k++) {

					if ((BuzonList.get(i).getIdPaciente().equals(BuzonListLimpia.get(k).getIdPaciente())) &&
						(BuzonList.get(i).getIdActividad().equals(BuzonListLimpia.get(k).getIdActividad())))
					{
						bandera = true;
						if (BuzonList.get(i).getContador() > (BuzonListLimpia.get(k).getContador()))
						{
							BuzonListLimpia.get(k).setContador(BuzonList.get(i).getContador());
						}

						try {
							final Calendar calendar1 = new GregorianCalendar();
							calendar1.set(BuzonList.get(i).getAnio(), BuzonList.get(i).getMes(), BuzonList.get(i).getDia(), BuzonList.get(i).getHoras(), BuzonList.get(i).getMinutos());
							Date FechaHora1 = calendar1.getTime();
							final Calendar calendar2 = new GregorianCalendar();
							calendar2.set(BuzonListLimpia.get(k).getAnio(), BuzonListLimpia.get(k).getMes(), BuzonListLimpia.get(k).getDia(), BuzonListLimpia.get(k).getHoras(), BuzonListLimpia.get(k).getMinutos());
							Date FechaHora2 = calendar2.getTime();

							if (FechaHora1.after(FechaHora2))
							{
								BuzonListLimpia.get(k).setAnio(BuzonList.get(i).getAnio());
								BuzonListLimpia.get(k).setMes(BuzonList.get(i).getMes());
								BuzonListLimpia.get(k).setDia(BuzonList.get(i).getDia());
								BuzonListLimpia.get(k).setHoras(BuzonList.get(i).getHoras());
								BuzonListLimpia.get(k).setMinutos(BuzonList.get(i).getMinutos());
							}
						}catch (Exception e){
							Log.e("Error ",e.getMessage());
						}
					}
				}

				if (bandera.equals(false))
				{
					BuzonListLimpia.add(BuzonList.get(i));
				}
			}

			miAdapter=new AdapterListaBuzon(getActivity(), BuzonListLimpia);
		}
	}

	public void ActualizarBuzon() {
		BtnActualizar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ListaBuzonPorFecha tarea1 = new ListaBuzonPorFecha();
				tarea1.execute();
			}
		});
	}

	public void OrdenarListaBuzonPorFecha() {				
		if (!BuzonListLimpia.isEmpty()) {
			Collections.sort(BuzonListLimpia, new Comparator<TblBuzon>() {  //ORDENA LA LISTA
				@Override
				public int compare(TblBuzon d1, TblBuzon d2) {
					Long fh1 = null, fh2 = null;
					try {
						final Calendar calendar1 = new GregorianCalendar();
						calendar1.set(d1.getAnio(), d1.getMes(), d1.getDia(), d1.getHoras(), d1.getMinutos());
						fh1 = calendar1.getTimeInMillis(); //gettime

						final Calendar calendar2 = new GregorianCalendar();
						calendar2.set(d2.getAnio(), d2.getMes(), d2.getDia(), d2.getHoras(), d2.getMinutos());
						fh2 = calendar2.getTimeInMillis(); //gettime
					} catch (Exception e) {
						// TODO: handle exception
					}
					return fh1.toString().compareToIgnoreCase(fh2.toString());
				}
			});
			Collections.reverse(BuzonListLimpia); //ORDENA DESCENDENTE EN LA LISTA
		}
	}

	public void OrdenarListaBuzonPorPaciente() {		
		if (!BuzonListLimpia.isEmpty()) {
			Collections.sort(BuzonListLimpia, new Comparator<TblBuzon>() {  //ORDENA LA LISTA
				@Override
				public int compare(TblBuzon d1, TblBuzon d2) {
					TblPacientes buscar_paciente1 = Select.from(TblPacientes.class).where(Condition.prop("id_paciente").eq(d1.getIdPaciente())).first();
					TblPacientes buscar_paciente2 = Select.from(TblPacientes.class).where(Condition.prop("id_paciente").eq(d2.getIdPaciente())).first();
					return buscar_paciente1.getNombreApellidoP().compareToIgnoreCase(buscar_paciente2.getNombreApellidoP());
				}
			});
		}
	}
	
	public void OrdenarListaBuzonPorPeticion() {		
		if (!BuzonListLimpia.isEmpty()) {
			Collections.sort(BuzonListLimpia, new Comparator<TblBuzon>() {  //ORDENA LA LISTA
				@Override
				public int compare(TblBuzon d1, TblBuzon d2) {
					TblActividades buscar_peticion1 = Select.from(TblActividades.class).where(Condition.prop("id_actividad").eq(d1.getIdActividad())).first();
					TblActividades buscar_peticion2 = Select.from(TblActividades.class).where(Condition.prop("id_actividad").eq(d2.getIdActividad())).first();
					return buscar_peticion1.getNombreActividad().compareToIgnoreCase(buscar_peticion2.getNombreActividad());
				}
			});
		}
	}	
		
	public void CargarLista() {
		//LISTA CON DATOS
		if (!BuzonListLimpia.isEmpty()) {
			LstBuzon.setAdapter(miAdapter);
			TxtNoHayDatos.setText("");
			ImageFondo.setImageDrawable(null);
		}
		//LISTA VACIA
		if (BuzonListLimpia.isEmpty()) {
			LstBuzon.setAdapter(null);
			TxtNoHayDatos.setText(R.string.NoHayNotificaciones);
			ImageFondo.setImageDrawable(getResources().getDrawable(R.drawable.notificacion2));
		}		
	}

	public void SpinnerOrdenarPor() {
		CmbTipoOrdenar.setOnItemSelectedListener(new OnItemSelectedListener() {			
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {				
				int selOpc=(int) parent.getItemIdAtPosition(position);
				switch (selOpc) {
				case 0://POR FECHA
			        ListaBuzonPorFecha tarea1 = new ListaBuzonPorFecha();
			        tarea1.execute();	
					break;
				case 1://POR PACIENTE					
					ListaBuzonPorPaciente tarea2 = new ListaBuzonPorPaciente();
					tarea2.execute();
					break;
				case 2://POR PETICION
					ListaBuzonPorPeticion tarea3 = new ListaBuzonPorPeticion();
					tarea3.execute();
					break;
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
	}

	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		if (v.getId() == android.R.id.list) {
			int position = ((AdapterView.AdapterContextMenuInfo) menuInfo).position;
			this.getActivity().getMenuInflater().inflate(R.menu.menu_contextual_buzon, menu);
			datos = (TblBuzon) LstBuzon.getItemAtPosition(position);
			menu.setHeaderTitle(R.string.Buzon);
			menu.setHeaderIcon(R.drawable.user_circle);
		}
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.Realizada:
				//SE ELIMINA DE LA LISTA DE PETICIONES
				FragmentManager fragmentManager1 = getFragmentManager();
				DFEliminarPeticion dialogo1 = new DFEliminarPeticion();
				dialogo1.show(fragmentManager1, "tagConfirmacion");
				return true;
			default:
				return super.onContextItemSelected(item);
		}
	}

	@SuppressLint("ValidFragment")
	public class DFEliminarPeticion extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage(R.string.EliminarPet)
					.setTitle(R.string.ConfEliminacion)
					.setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_alert))
					.setPositiveButton(R.string.Aceptar, new DialogInterface.OnClickListener()  {
						public void onClick(DialogInterface dialog, int id) {
							//ELIMINAR LOS REGISTROS DE ESE PACIENTE/ACTIVIDAD
							TblBuzon registro_pac=new TblBuzon();
							registro_pac.EliminarPorIdPacienteActividadRegTblBuzon(datos.getIdPaciente(), datos.getIdActividad());
							//ACTUALIZAR LA LISTA
							ListaBuzonPorFecha tarea1 = new ListaBuzonPorFecha();
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
	
	
	//ASYNCTASK
	
	
	private class ListaBuzonPorFecha extends AsyncTask<Void, Void, AdapterListaBuzon>{		
		@Override
		protected AdapterListaBuzon doInBackground(Void... arg0) {		
			try{				
				CargarAdaptador();
				OrdenarListaBuzonPorFecha();	
			}catch(Exception ex){
				ex.printStackTrace();
			}						
			return miAdapter;
		}

		@Override
		protected void onPostExecute(AdapterListaBuzon result) {		
			super.onPostExecute(result);
			CargarLista();			
		}		
	}

	private class ListaBuzonPorPaciente extends AsyncTask<Void, Void, AdapterListaBuzon>{		
		@Override
		protected AdapterListaBuzon doInBackground(Void... arg0) {		
			try{				
				CargarAdaptador();
				OrdenarListaBuzonPorPaciente();	
			}catch(Exception ex){
				ex.printStackTrace();
			}						
			return miAdapter;
		}
		
		@Override
		protected void onPostExecute(AdapterListaBuzon result) {		
			super.onPostExecute(result);
			CargarLista();
		}		
	}	
	
	private class ListaBuzonPorPeticion extends AsyncTask<Void, Void, AdapterListaBuzon>{		
		@Override
		protected AdapterListaBuzon doInBackground(Void... arg0) {		
			try{				
				CargarAdaptador();
				OrdenarListaBuzonPorPeticion();	
			}catch(Exception ex){
				ex.printStackTrace();
			}						
			return miAdapter;
		}
		
		@Override
		protected void onPostExecute(AdapterListaBuzon result) {		
			super.onPostExecute(result);
			CargarLista();
		}		
	}


}