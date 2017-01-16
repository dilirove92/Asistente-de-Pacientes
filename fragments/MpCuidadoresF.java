package com.Notifications.patientssassistant.fragments;


import com.Notifications.patientssassistant.*;
import com.Notifications.patientssassistant.dialogos.*;
import com.Notifications.patientssassistant.tables.*;
import com.Notifications.patientssassistant.adapters.*;
import com.Notifications.patientssassistant.asynctask.*;
import java.util.ArrayList;
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
import android.app.FragmentTransaction;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;
import android.annotation.SuppressLint;


public class MpCuidadoresF extends Fragment implements OnQueryTextListener {

	public final static String KEY_REG_TEXT1 = "IdCuidador";
	public final static String KEY_REG_TEXT2 = "ControlT";
	public final static String KEY_REG_TEXT3 = "DependeDe";
	private static Long vIdCuidador;
	private static Boolean vControlT;
	private static Long vDependeDe;
	private int request_code = 1;

	//VARIABLES AUXILIARES
	private FragmentIterationListener mCallback = null;
	private static Boolean banderaNuevo = false;
	private static Boolean banderaEditar = false;
	private List<TblCuidadorS> lista_cuidadoresS;
	private AdapterListaC miAdapter;
	private Long vItem_IdCuidador;
	private TblCuidador datos;
	private Intent intent;
	private Filter filter;
	private String query = "";

	//VARIABLES DE LOS ELEMENTOS DE LA IU
	ImageButton BtnAgregarC;
	SearchView BusquedaC;
	ListView LstCuidadores;
	ImageView ImageFondo;
	TextView TxtNoHayDatos;

	public interface FragmentIterationListener {
		public void onFragmentIteration(Bundle parameters);
	}

	public MpCuidadoresF() {
		super();
	}

	public static MpCuidadoresF newInstance(Long c_idCuidador, Boolean c_controlT, Long c_dependeDe) {
		MpCuidadoresF frag = new MpCuidadoresF();
		Bundle args = frag.getArguments();
		if (args == null)
			args = new Bundle();
		args.putLong(KEY_REG_TEXT1, c_idCuidador);
		args.putBoolean(KEY_REG_TEXT2, c_controlT);
		args.putLong(KEY_REG_TEXT3, c_dependeDe);
		frag.setArguments(args);
		return frag;
	}

	public void RecogerParametrosActMP2() {
		vIdCuidador = getArguments().getLong(KEY_REG_TEXT1);
		vControlT = getArguments().getBoolean(KEY_REG_TEXT2);
		vDependeDe = getArguments().getLong(KEY_REG_TEXT3);
	}

	public void EnviarParametrosFragMpC() {
		TabCuidadoresF fragment = TabCuidadoresF.newInstance(vItem_IdCuidador, vIdCuidador, vDependeDe, vControlT);
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.content_frame, fragment, "TabCuidadoresF");
		ft.addToBackStack(null);
		ft.commit();
	}

	public void EnviarParametrosFragMpC2() {
		intent.putExtra("varDependeDe", vDependeDe);
	}

	public void EnviarParametrosFragMpC3() {
		intent.putExtra("varItemIdCuidador", datos.getIdCuidador());
		intent.putExtra("varUsuarioC", datos.getUsuarioC());
		intent.putExtra("varNombreC", datos.getNombreC());
		intent.putExtra("varCiRucC", datos.getCiRucC());
		intent.putExtra("varCelularC", datos.getCelularC());
		intent.putExtra("varTelefonoC", datos.getTelefonoC());
		intent.putExtra("varEmailC", datos.getEmailC());
		intent.putExtra("varDireccionC", datos.getDireccionC());
		intent.putExtra("varCargoC", datos.getCargoC());
		intent.putExtra("varControlTotal", datos.getControlTotal());
		intent.putExtra("varFotoC", datos.getFotoC());
		intent.putExtra("varIdCuidador", vIdCuidador);
	}

	//METODO QUE RETORNA "Nuevo" 
	public static Boolean EsNuevo() {
		Boolean resultado1 = banderaNuevo;
		return resultado1;
	}

	//METODO QUE RETORNA "Editar"
	public static Boolean EsEditar() {
		Boolean resultado2 = banderaEditar;
		return resultado2;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallback = (FragmentIterationListener) activity;
		} catch (Exception ex) {
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

		View rootView = inflater.inflate(R.layout.mp_cuid_paci, container, false);
		if (rootView != null) {
			LstCuidadores = (ListView) rootView.findViewById(android.R.id.list);
			BusquedaC = (SearchView) rootView.findViewById(R.id.busquedaCoP);
			BtnAgregarC = (ImageButton) rootView.findViewById(R.id.btnAgregarCoP);
			ImageFondo = (ImageView) rootView.findViewById(R.id.imageView1);
			TxtNoHayDatos = (TextView) rootView.findViewById(R.id.txtNoHayDatos);
		}

		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		RecogerParametrosActMP2();
		ListaCuidadores tarea1 = new ListaCuidadores();
		tarea1.execute();
		BusquedaC.setOnQueryTextListener(MpCuidadoresF.this);
		BusquedaC.setSubmitButtonEnabled(true);
		registerForContextMenu(LstCuidadores);
		SeleccionarItemListaCuidadores();
		BtnNuevoCuidador();
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

	@Override
	public boolean onQueryTextSubmit(String query) {
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		if (!lista_cuidadoresS.isEmpty()) {
			filter.filter(newText);
			query = newText;
		}
		return true;
	}

	public void CargarListaCuidadores() {
		lista_cuidadoresS = Select.from(TblCuidadorS.class).where(Condition.prop("depende_de").eq(vDependeDe),
																  Condition.prop("Eliminado").eq(0)).list();
		//LISTA CON DATOS
		if (!lista_cuidadoresS.isEmpty()) {
			List<TblCuidador> lista_nombresCS = new ArrayList<TblCuidador>();
			TblCuidadorS cuidadorS_clase;
			lista_nombresCS.clear();  //LIMPIAR PARA NO REPETIR LOS DATOS

			for (int i = 0; i < lista_cuidadoresS.size(); i++) {
				cuidadorS_clase = lista_cuidadoresS.get(i);
				TblCuidador buscar_cuidador = Select.from(TblCuidador.class).where(Condition.prop("id_cuidador").eq(cuidadorS_clase.getIdCuidador())).first();
				lista_nombresCS.add(new TblCuidador(buscar_cuidador.getIdCuidador(), buscar_cuidador.getUsuarioC(), buscar_cuidador.getNombreC(), buscar_cuidador.getCiRucC(), buscar_cuidador.getCelularC(), buscar_cuidador.getTelefonoC(),
									buscar_cuidador.getEmailC(), buscar_cuidador.getDireccionC(), buscar_cuidador.getCargoC(), buscar_cuidador.getControlTotal(), buscar_cuidador.getFotoC(), buscar_cuidador.getEliminado()));
			}

			miAdapter = new AdapterListaC(getActivity(), lista_nombresCS);

			Collections.sort(lista_nombresCS, new Comparator<TblCuidador>() {  //ORDENA LA LISTA
				@Override
				public int compare(TblCuidador d1, TblCuidador d2) {
					return d1.getNombreC().compareToIgnoreCase(d2.getNombreC());
				}
			});
		}
	}

	public void SeleccionarItemListaCuidadores() {
		LstCuidadores.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				try {
					TblCuidador dato_Cuidador = (TblCuidador) parent.getItemAtPosition(position);
					vItem_IdCuidador = dato_Cuidador.getIdCuidador();
					EnviarParametrosFragMpC();
				} catch (Exception ex) {
					Toast.makeText(getActivity(), getString(R.string.Error) + ex.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	public void BtnNuevoCuidador() {
		BtnAgregarC.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					intent = new Intent(getActivity(), NewEditCuidadorActivity.class);
					EnviarParametrosFragMpC2();
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
			datos = (TblCuidador) LstCuidadores.getItemAtPosition(position);
			menu.setHeaderTitle(datos.getNombreC());
			menu.setHeaderIcon(R.drawable.user_circle);
		}
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.Editar:
				intent = new Intent(getActivity(), NewEditCuidadorActivity.class);
				EnviarParametrosFragMpC3();
				startActivityForResult(intent, request_code);
				banderaEditar = true;
				return true;
			case R.id.Eliminar:
				FragmentManager fragmentManager1 = getFragmentManager();
				DFEliminarCuidador dialogo1 = new DFEliminarCuidador();
				dialogo1.show(fragmentManager1, "tagConfirmacion");
				return true;
			default:
				return super.onContextItemSelected(item);
		}
	}

	@SuppressLint("ValidFragment")
	public class DFEliminarCuidador extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage(R.string.EliminarCuidador)
					.setTitle(R.string.ConfEliminacion)
					.setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_alert))
					.setPositiveButton(R.string.Aceptar, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							//EN EL CASO DE INICIAR SESION CON CUIDADOR SECUNDARIO NO PERMITIR QUE ESTE SE ELIMINE A SI MISMO
							if (vIdCuidador.equals(datos.getIdCuidador())) {
								FragmentManager fragmentManager2 = getFragmentManager();
								DFNoEliminarCS dialogo2 = new DFNoEliminarCS();
								dialogo2.show(fragmentManager2, "tagConfirmacion");
							} else {
								//ELIMINAR TODOS LOS REGISTROS VINCULADOS AL CUIDADOR
								ATCuidador cuidador = new ATCuidador();
								cuidador.new EliminarCuidador().execute(datos.getIdCuidador().toString());
								//ACTUALIZAR LISTA
								ListaCuidadores tarea1 = new ListaCuidadores();
								tarea1.execute();
							}
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
		if ((requestCode == request_code) && (resultCode == Activity.RESULT_OK)) {
			banderaNuevo = false;
			banderaEditar = false;
			ListaCuidadores tarea1 = new ListaCuidadores();
			tarea1.execute();
		}
	}


	//ASYNCTASK
	
	
	public class ListaCuidadores extends AsyncTask<Void, Void, AdapterListaC>{
		@Override
		protected AdapterListaC doInBackground(Void... arg0) {		
			try{				
				CargarListaCuidadores();
			}catch(Exception ex){
				ex.printStackTrace();
			}						
			return miAdapter;
		}

		@Override
		protected void onPostExecute(AdapterListaC result) {		
			super.onPostExecute(result);
			//LISTA CON DATOS
			if (!lista_cuidadoresS.isEmpty()) {
				LstCuidadores.setAdapter(miAdapter);
				TxtNoHayDatos.setText("");			
				ImageFondo.setImageDrawable(null);
				filter = miAdapter.getFilter();  //FILTRO DE LA LISTA
				onQueryTextChange(query);
			}
			//LISTA VACIA
			if (lista_cuidadoresS.isEmpty()) {
				LstCuidadores.setAdapter(null);
				TxtNoHayDatos.setText(R.string.NoHayCuidadores);	
				ImageFondo.setImageDrawable(getResources().getDrawable(R.drawable.cuidadores2));
			}
		}		
	}


}