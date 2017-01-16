package com.Notifications.patientssassistant;


import com.Notifications.patientssassistant.adapters.*;
import com.Notifications.patientssassistant.asynctask.*;
import com.Notifications.patientssassistant.dialogos.*;
import com.Notifications.patientssassistant.fragments.*;
import com.Notifications.patientssassistant.tables.*;
import com.Notifications.patientssassistant.volleyscalls.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.orm.query.Condition;
import com.orm.query.Select;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class MenuPrincipalRestringidoActivity extends Activity {
    
    //VARIABLES PARA RECOGER PARAMETROS
    private Long vIdCuidador;
    private String vFotoC;
    private String vUsuario;		
    private String vTipoUsuario;
    private String vTU;
    private Long vDependeDe;
    private Boolean vControlT;
    private Long vIdIS;

	//VARIABLES AUXILIARES
    private String[] titulos;
    private ArrayList<ListaItemDrawer> NavItms;
    private TypedArray NavIcons;
    private CharSequence mTitle;
    private FragmentTransaction ft;
    private Fragment fragment;
    private FragmentManager fragmentManager = getFragmentManager();
	private ProgressDialog progressDialogo;
            
    //VARIABLES DE LOS ELEMENTOS DE LA IU
    DrawerLayout NavDrawerLayout;
    ListView NavList;
    ActionBarDrawerToggle mDrawerToggle;    
    AdapterDrawerList NavAdapter;
    ImageView ImagenFotoU;
    TextView TxtNombreU;
    TextView TxtTipoU;

	//VARIABLES PARA CONEXION DE DATOS
	public String jsonResponse;
	public Context context;

	JsonObjectRequest request;
	android.content.Context Context;
	final String TAG = "MenuPrincipalRestringidoActivity";
	private static String ip= VarEstatic.ObtenerIP();
    
	public MenuPrincipalRestringidoActivity() {super();}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_principal);
		context=getApplicationContext();

		RecogerParametrosActIS();
		
		//DRAWER LAYOUT
		NavDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        NavList=(ListView)findViewById(android.R.id.list);
        
        //ADAPTADOR HEADER
        View header=getLayoutInflater().inflate(R.layout.adaptador_header, null);
        ImagenFotoU=(ImageView)header.findViewById(R.id.imagenFotoU);
        TxtNombreU=(TextView)header.findViewById(R.id.txtNombreU);
        TxtTipoU=(TextView)header.findViewById(R.id.txtTipoU);

		if (vFotoC.equals("")) {
			ImagenFotoU.setImageResource(R.drawable.user_foto);				
		}else{
			byte[] b = Base64.decode(vFotoC, Base64.DEFAULT);
			Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
	    	ImagenFotoU.setImageBitmap(bitmap);				
		}         
        TxtNombreU.setText(vUsuario);
        TxtTipoU.setText(vTipoUsuario); 
        
        NavList.addHeaderView(header);
        NavIcons = getResources().obtainTypedArray(R.array.Iconos_menu_principal_restringido);
        titulos = getResources().getStringArray(R.array.Menu_principal_restringido);
        
        NavItms = new ArrayList<ListaItemDrawer>();        	      
        NavItms.add(new ListaItemDrawer(titulos[0], NavIcons.getResourceId(0, -1)));        
        NavItms.add(new ListaItemDrawer(titulos[1], NavIcons.getResourceId(1, -1)));        
        NavItms.add(new ListaItemDrawer(titulos[2], NavIcons.getResourceId(2, -1)));        
        NavItms.add(new ListaItemDrawer(titulos[3], NavIcons.getResourceId(3, -1)));        
        NavItms.add(new ListaItemDrawer(titulos[4], NavIcons.getResourceId(4, -1)));
		NavItms.add(new ListaItemDrawer(titulos[5], NavIcons.getResourceId(5, -1)));
		NavItms.add(new ListaItemDrawer(titulos[6], NavIcons.getResourceId(6, -1)));
        
        NavAdapter= new AdapterDrawerList(MenuPrincipalRestringidoActivity.this,NavItms);
        NavList.setAdapter(NavAdapter);	
       
        mDrawerToggle = new ActionBarDrawerToggle(MenuPrincipalRestringidoActivity.this, NavDrawerLayout,        
        					R.drawable.tcgtheme1_ic_navigation_drawer, R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerClosed(View view) {
            	getActionBar().setTitle(mTitle);
            }
           
            public void onDrawerOpened(View drawerView) {
            	getActionBar().setTitle(R.string.NombreApp);
            }
        };	        
        
        NavDrawerLayout.setDrawerListener(mDrawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        NavList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
            	MostrarFragment(position);
            }
        });       

        MostrarFragment(1);
		miHiloActualizarBD1();
	}


	//HILO DE ACTUALIZACION DE BD CADA 8 HORAS
	protected void miHiloActualizarBD1() {
		if(estaConectado()) {
			progressDialogo = new ProgressDialog(MenuPrincipalRestringidoActivity.this);
			progressDialogo.setCancelable(false);
			progressDialogo.setMessage(getResources().getString(R.string.DescargandoDatos));
			progressDialogo.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDialogo.setProgress(0);
			progressDialogo.setMax((int) 100);
			progressDialogo.show();

			Thread t = new Thread() {
				public void run() {
					Boolean ejec = false;
					try {
						TimeHilos th1 = new TimeHilos();
						ejec = th1.EjecutarONo("HiloBD");
						int progreso = 0;

						if (ejec) {
							TraerDatos();
							while (progreso <= 96) {
								progreso += 2;
								Thread.sleep(1000);
								progressHandlerInc.sendMessage(progressHandlerInc.obtainMessage());
							}
							Thread.sleep(15000);
							ActualizarAlarmas(vIdCuidador);
							progressHandlerMed.sendMessage(progressHandlerInc.obtainMessage());
							progressHandlerFin.sendMessage(progressHandlerFin.obtainMessage());
						} else {
							progressHandlerFin.sendMessage(progressHandlerFin.obtainMessage());
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
			t.start();
		}
	}

	protected void ActualizarDatos(){
		if(estaConectado()) {
			if (estaConectado()) {
				progressDialogo = new ProgressDialog(MenuPrincipalRestringidoActivity.this);
				progressDialogo.setCancelable(false);
				progressDialogo.setMessage(getResources().getString(R.string.DescargandoDatos));
				progressDialogo.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				progressDialogo.setProgress(0);
				progressDialogo.setMax((int) 100);
				progressDialogo.show();

				Thread t = new Thread() {
					public void run() {
						try {
							int progreso = 0;
							TraerDatos();
							while (progreso <= 96) {
								progreso += 2;
								Thread.sleep(1000);
								progressHandlerInc.sendMessage(progressHandlerInc.obtainMessage());
							}
							Thread.sleep(15000);
							ActualizarAlarmas(vIdCuidador);
							progressHandlerMed.sendMessage(progressHandlerInc.obtainMessage());
							progressHandlerFin.sendMessage(progressHandlerFin.obtainMessage());
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				};
				t.start();
			}
		}
	}

	Handler progressHandlerInc = new Handler() {
		public void handleMessage(Message msg) {
			progressDialogo.incrementProgressBy(2);
		}
	};

	Handler progressHandlerMed = new Handler() {
		public void handleMessage(Message msg) {
			progressDialogo.incrementProgressBy(3);
		}
	};

	Handler progressHandlerFin = new Handler() {
		public void handleMessage(Message msg) {
			progressDialogo.dismiss();
		}
	};

	public void TraerDatos(){
		EliminarAlarmas();
		Boolean ejecutado=MetodosActualizacionBaseVolley(vIdCuidador);
		do{
			if(ejecutado)
			{
				TblHilos rh= Select.from(TblHilos.class).where(Condition.prop("NOMBRE").eq("HiloBD"),
															   Condition.prop("EJECUTADO").eq(0)).first();
				if(rh!=null){
					rh.setEjecutado(true);
					rh.save();
				}
				Log.e("Descarga ", "Terminada!");
			}
		}while(!ejecutado);
	}

	//HILO DE ACTUALIZACION DE BD CADA 8 HORAS
	final Handler mHandler = new Handler();

	protected void miHiloActualizarBD(){
		Thread t = new Thread(){
			public void run(){
				Boolean ejec=false;
				try {
					Long time=1000L;
					TimeHilos th1= new TimeHilos();
					time=th1.TiempoEjecutarHiloDe("HiloBD");
					Thread.sleep(time);
					Log.e("Time de HiloBD", " es: " + time);
					ejec=th1.EjecutarONo("HiloBD");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(!ejec) {
					mHandler.post(ejecutarActualizarBD);
				}
			}
		};
		t.start();
	}

	final Runnable ejecutarActualizarBD = new Runnable() {
		@Override
		public void run() {
			EliminarAlarmas();
			Boolean ejecutado=MetodosActualizacionBaseVolley(vIdCuidador);
			do{
				if(ejecutado)
				{
					TimeHilos th2= new TimeHilos();
					th2.CalcularTiempoProximoHilo("HiloBD");
					ActualizarAlarmas(vIdCuidador);
					TblHilos rh= Select.from(TblHilos.class).where(Condition.prop("NOMBRE").eq("HiloBD"),
																	Condition.prop("EJECUTADO").eq(0)).first();
					if(rh!=null){
						rh.setEjecutado(true);
						rh.save();
					}
					miHiloActualizarBD();
				}
			}while(!ejecutado);
		}
	};

	//HILO PARA BORRAR LAS NOTIFICACIONES ANTIGUAS DE HACE UN MES EN LA BD
	final Handler mHandler3 = new Handler();

	protected void miHiloBorrarBuzonBD(){
		Thread t3 = new Thread(){
			public void run(){
				try {
					Long time=0L;
					TimeHilos th1= new TimeHilos();
					time=th1.TiempoEjecutarHiloDe("HiloBBBD");
					Thread.sleep(time);//cada mes
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				mHandler3.post(ejecutarBorrarBuzonBD);
			}
		};
		t3.start();
	}

	final Runnable ejecutarBorrarBuzonBD = new Runnable() {
		@Override
		public void run() {
			//cambiar a volley
			ATBuzon buzon = new ATBuzon();
			buzon.new  BorrarBuzonBD().execute(String.valueOf(vIdCuidador));//Enviar idCuidador
			Toast.makeText(MenuPrincipalRestringidoActivity.this, "Borrando registros viejos de Buzon", Toast.LENGTH_SHORT).show();
			TimeHilos th2= new TimeHilos();
			th2.CalcularTiempoProximoHilo("HiloBBBD");
			miHiloBorrarBuzonBD();
		}
	};

	public void ActualizarAlarmas(long idCuidador)
	{
		//VARIABLES TIPO LISTAS
		List<TblPermisos> listaPermisos = new ArrayList<TblPermisos>();
		List<TblEventosCuidadores>  listaEventosC = new ArrayList<TblEventosCuidadores>();
		List<TblEventosPacientes> listaEventosP = new ArrayList<TblEventosPacientes>();
		List<TblRutinasCuidadores> listaRutinasC = new ArrayList<TblRutinasCuidadores>();
		List<TblRutinasPacientes> listaRutinasP = new ArrayList<TblRutinasPacientes>();
		List<TblControlMedicina> listaControlMed = new ArrayList<TblControlMedicina>();
		List<TblHorarioMedicina> listaHorarioMed = new ArrayList<TblHorarioMedicina>();

		MiTblEvento.deleteAll(MiTblEvento.class);
		MiTblRutina.deleteAll(MiTblRutina.class);
		MiTblMedicina.deleteAll(MiTblMedicina.class);

		//LISTAR LOS EVENTOS DEL CUIDADOR PARA CREAR ALARMA
		listaEventosC= Select.from(TblEventosCuidadores.class).where(Condition.prop("id_cuidador").eq(String.valueOf(idCuidador)),
																	Condition.prop("Alarma").eq(1),
																	Condition.prop("Eliminado").eq(0)).list();
		if (!listaEventosC.isEmpty()) {
			for (int i = 0; i < listaEventosC.size(); i++) {

				TblEventosCuidadores evenC=listaEventosC.get(i);
				String fechaHoraE = String.valueOf(evenC.getDiaE())+"/"+String.valueOf(evenC.getMesE())+"/"+
									String.valueOf(evenC.getAnioE())+" "+String.valueOf(evenC.getHoraE())+":"+
									String.valueOf(evenC.getDiaE());

				TblCuidador cuidador = Select.from(TblCuidador.class).where(Condition.prop("id_cuidador").eq(String.valueOf(idCuidador))).first();

				MiTblEvento.CrearAlarmaEvento(MenuPrincipalRestringidoActivity.this, evenC.getIdEventoC(),
						evenC.getAnioR(), evenC.getMesR(), evenC.getDiaR(), evenC.getHoraR(),
						evenC.getMinutosR(), idCuidador, "C", cuidador.getNombreC(), "Evento", fechaHoraE,
						evenC.getLugar(), evenC.getDescripcion(), evenC.getTono(), evenC.getAlarma());

				Log.e("creado MiTblEvento ", "cuidador");
			}
		}

		//LISTAR LAS RUTINAS DEL CUIDADOR PARA CREAR ALARMA
		listaRutinasC= Select.from(TblRutinasCuidadores.class).where(Condition.prop("id_cuidador").eq(String.valueOf(idCuidador)),
																	Condition.prop("Alarma").eq(1),
																	Condition.prop("Eliminado").eq(0)).list();
		if (!listaRutinasC.isEmpty()) {
			for (int i = 0; i < listaRutinasC.size(); i++) {
				TblRutinasCuidadores rutC=listaRutinasC.get(i);

				TblCuidador cuidador = Select.from(TblCuidador.class).where(Condition.prop("id_cuidador").eq(String.valueOf(idCuidador))).first();

				MiTblRutina.CrearAlarmaRutina(MenuPrincipalRestringidoActivity.this, rutC.getIdRutinaC(), rutC.getHora(),
											rutC.getMinutos(), idCuidador, "C", cuidador.getNombreC(), "Rutina", rutC.getDomingo(),
											rutC.getLunes(), rutC.getMartes(), rutC.getMiercoles(), rutC.getJueves(),
											rutC.getViernes(), rutC.getSabado(), rutC.getTono(), rutC.getAlarma());

				Log.e("creado MiTblRutina ", "cuidador");
			}
		}

		//LISTAR LOS PACIENTES DE LOS QUE TIENE QUE RECIBIR NOTIFICACIONES
		listaPermisos= Select.from(TblPermisos.class).where(Condition.prop("id_cuidador").eq(String.valueOf(idCuidador)),
															Condition.prop("notifi_alarma").eq(1),
															Condition.prop("Eliminado").eq(0)).list();

		for (int i = 0; i < listaPermisos.size(); i++) {
			long idPaciente=listaPermisos.get(i).getIdPaciente();

			//LISTAR LOS EVENTOS DE LOS PACIENTES A SU CARGO
			listaEventosP= Select.from(TblEventosPacientes.class).where(Condition.prop("id_paciente").eq(String.valueOf(idPaciente)),
																		Condition.prop("Alarma").eq(1),
																		Condition.prop("Eliminado").eq(0)).list();
			if (!listaEventosP.isEmpty()) {
				for (int j = 0; j < listaEventosP.size(); j++) {
					TblEventosPacientes evenP=listaEventosP.get(j);

					String fechaHoraE = String.valueOf(evenP.getDiaE())+"/"+String.valueOf(evenP.getMesE())+"/"+
										String.valueOf(evenP.getAnioE())+" "+String.valueOf(evenP.getHoraE())+":"+
										String.valueOf(evenP.getDiaE());

					TblPacientes paciente = Select.from(TblPacientes.class).where(Condition.prop("id_paciente").eq(String.valueOf(idPaciente))).first();

					MiTblEvento.CrearAlarmaEvento(MenuPrincipalRestringidoActivity.this, evenP.getIdEventoP(),
												evenP.getAnioR(), evenP.getMesR(), evenP.getDiaR(), evenP.getHoraR(),
												evenP.getMinutosR(), idPaciente, "P", paciente.getNombreApellidoP(), "Evento", fechaHoraE,
												evenP.getLugar(), evenP.getDescripcion(), evenP.getTono(), evenP.getAlarma());

					Log.e("creado MiTblEvento ", "paciente");
				}
			}

			//LISTAR LAS RUTINAS DE LOS PACIENTES A SU CARGO
			listaRutinasP= Select.from(TblRutinasPacientes.class).where(Condition.prop("id_paciente").eq(String.valueOf(idPaciente)),
																		Condition.prop("Alarma").eq(1),
																		Condition.prop("Eliminado").eq(0)).list();
			if (!listaRutinasP.isEmpty()) {
				for (int j = 0; j < listaRutinasP.size(); j++) {
					TblRutinasPacientes rutP=listaRutinasP.get(j);

					TblPacientes paciente = Select.from(TblPacientes.class).where(Condition.prop("id_paciente").eq(String.valueOf(idPaciente))).first();

					MiTblRutina.CrearAlarmaRutina(MenuPrincipalRestringidoActivity.this, rutP.getIdRutinaP(), rutP.getHora(),
												rutP.getMinutos(), idPaciente, "P", paciente.getNombreApellidoP(), "Rutina", rutP.getDomingo(),
												rutP.getLunes(), rutP.getMartes(), rutP.getMiercoles(), rutP.getJueves(),
												rutP.getViernes(), rutP.getSabado(), rutP.getTono(), rutP.getAlarma());

					Log.e("creado MiTblRutina ", "paciente");
				}
			}
		}

		//LISTAR LOS PACIENTES A LOS QUE TIENE QUE DAR MEDICACION
		listaPermisos= Select.from(TblPermisos.class).where(Condition.prop("id_cuidador").eq(String.valueOf(idCuidador)),
															Condition.prop("cont_medicina").eq(1),
															Condition.prop("Eliminado").eq(0)).list();

		for (int i = 0; i < listaPermisos.size(); i++) {
			long idPaciente=listaPermisos.get(i).getIdPaciente();

			TblPacientes paciente = Select.from(TblPacientes.class).where(Condition.prop("id_paciente").eq(String.valueOf(idPaciente))).first();

			//LISTAR LAS MEDICINAS QUE TIENE QUE SUMINISTRAR A LOS PACIENTES
			listaControlMed= Select.from(TblControlMedicina.class).where(Condition.prop("id_paciente").eq(String.valueOf(idPaciente)),
																		 Condition.prop("Eliminado").eq(0)).list();

			for (int j = 0; j < listaControlMed.size(); j++) {
				TblControlMedicina contMed=listaControlMed.get(j);

				//LISTAR LOS HORARIOS DE LAS MEDICINAS A SUMINISTRAR
				listaHorarioMed= Select.from(TblHorarioMedicina.class).where(Condition.prop("id_control_medicina").eq(String.valueOf(contMed.getIdControlMedicina())),
																			Condition.prop("act_des_alarma").eq(1),
																			Condition.prop("Eliminado").eq(0)).list();
				for (int k = 0; k < listaHorarioMed.size(); k++) {
					TblHorarioMedicina horMed=listaHorarioMed.get(k);

					MiTblMedicina.CrearAlarmaHM(MenuPrincipalRestringidoActivity.this, horMed.getIdControlMedicina(), idPaciente, paciente.getNombreApellidoP(),
												contMed.getMedicamento(), contMed.getMotivoMedicacion(), contMed.getDosis(),
												contMed.getNdeVeces(), horMed.getHora(), horMed.getMinutos(), horMed.getDomingo(),
												horMed.getLunes(), horMed.getMartes(), horMed.getMiercoles(), horMed.getJueves(),
												horMed.getViernes(), horMed.getSabado(), contMed.getTono(), horMed.getActDesAlarma());

					Log.e("creado MiTblMedicina ", "paciente");
				}
			}
		}
	}
	
	public void RecogerParametrosActIS() {
		vIdCuidador = getIntent().getExtras().getLong("varIdCoP");
		vFotoC = getIntent().getExtras().getString("varFotoC");
		vUsuario = getIntent().getExtras().getString("varUsuario");		
		vTipoUsuario = getIntent().getExtras().getString("varTipoUsuario");		
		vTU = getIntent().getExtras().getString("varTU");
		vDependeDe = getIntent().getExtras().getLong("varDependeDe");
		vControlT = getIntent().getExtras().getBoolean("varControlT");
		vIdIS = getIntent().getExtras().getLong("varIdIS");
	}
	
	public void EnviarParametrosActMP1() { 
		fragment = MpBuzonNotificacionesF.newInstance(vIdCuidador);
		ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment, "MpBuzonNotificacionesF");
		ft.addToBackStack(null);
		ft.commit();
	}
	
	public void EnviarParametrosActMP2() { 
		fragment = MpPacientesF.newInstance(vIdCuidador, vTU, vControlT, vDependeDe);
        ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment, "MpPacientesF");
        ft.addToBackStack(null);
        ft.commit();
	}	
	
	public void EnviarParametrosActMP3() { 
		fragment = MpTabMiAgendaF.newInstance(vIdCuidador);
        ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment, "MpTabMiAgendaF");
        ft.addToBackStack(null);
        ft.commit();
	}
	
	public void EnviarParametrosActMP4() { 
		fragment = MpTabMiPerfilF.newInstance(vIdCuidador, vTU, vControlT, vDependeDe);
        ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment, "MpTabMiPerfilF");
        ft.addToBackStack(null);
        ft.commit();
	}

	public void Informacion() {
		fragment = new MpInformacionF();
		ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.content_frame, fragment, "MpInformacionF");
		ft.addToBackStack(null);
		ft.commit();
	}

	public void CerrarSesion() {
		DFCerrarSesion3 dialogo1 = new DFCerrarSesion3();
	    dialogo1.show(fragmentManager, "tagConfirmacion");
	}
	
	//METODO QUE ELIMINARA TODAS LAS ALARMAS DEL CELL
	public void EliminarAlarmas() {
		//ELIMINAR LAS ALARMAS DE LOS EVENTOS DEL CUIDADOR Y DE LOS PACIENTES		
		MiTblEvento.EliminarAlarmasEventos(MenuPrincipalRestringidoActivity.this);
		//ELIMINAR LAS ALARMAS DE LAS RUTINAS DEL CUIDADOR Y DE LOS PACIENTES
		MiTblRutina.EliminarAlarmasRutinas(MenuPrincipalRestringidoActivity.this);
		//ELIMINAR LAS ALARMAS DE LAS MEDICINAS DE LOS PACIENTES
		MiTblMedicina.EliminarAlarmasHMS(MenuPrincipalRestringidoActivity.this);
	}

	//METODO QUE ACTUALIZA EL CIERRE DE SESION
	public void GuardarFinSesion(){
		try {
			Calendar cal = new GregorianCalendar();
			cal.getTime();
			int anio = cal.get(Calendar.YEAR);
			int mes = cal.get(Calendar.MONTH);
			int dia = cal.get(Calendar.DAY_OF_MONTH);
			int hora = cal.get(Calendar.HOUR_OF_DAY);
			int minutos = cal.get(Calendar.MINUTE);

			//ATInicioSesion iniSesion=new ATInicioSesion();
			//iniSesion.new ActualizarInicioSesion().execute(String.valueOf(vIdIS),  String.valueOf(anio), String.valueOf(mes),
			//											   String.valueOf(dia), String.valueOf(hora),String.valueOf(minutos), "", "true");

			String urlJson = "http://"+ip+"/ADP/InicioSesion/InicioSesionActualizarObject";

			HashMap<String, String> dato = new HashMap<String, String>();
			dato.put("IdIniSes", String.valueOf(vIdIS));
			dato.put("AnioFin",String.valueOf(anio));
			dato.put("MesFin",String.valueOf(mes));
			dato.put("DiaFin",String.valueOf(dia));
			dato.put("HoraFin",String.valueOf(hora));
			dato.put("MinutosFin",String.valueOf(minutos));
			dato.put("IdReGCM","");
			dato.put("Eliminado","true");

			request = new JsonObjectRequest(Request.Method.PUT, urlJson, new JSONObject(dato), new Response.Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					try {
						Boolean respuesta=response.getBoolean("Done");
						if (respuesta) {
							Log.e("¿Actualizado? =>", respuesta.toString());
							Log.e("Datos Actualizados =>", "  SERVER");
							EliminarAlarmas();
							Log.e("Pasaba por Aqui =>", " DELETE ALARMAS");
							EliminarDatos.EliminarDatosDB();
							Log.e("Pasaba por Aqui =>", " DELETE TABLAS");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					VolleyLog.d(TAG, "Error: " + error.getMessage());
				}
			})
			{
				@Override
				public Map<String, String> getHeaders() throws AuthFailureError {
					HashMap<String, String> headers = new HashMap<String, String>();
					headers.put("Content-Type", "application/json; charset=utf-8");
					return headers;
				}
			};
			VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

		} catch (Exception ex) {
			Toast.makeText(MenuPrincipalRestringidoActivity.this, getString(R.string.Error) + ex.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}
	
	private void MostrarFragment(int position) {	
	    fragment = null;
	    switch (position) {
	    case 1:
	    	//PANTALLA: BUZON DE NOTIFICACIONES
	    	EnviarParametrosActMP1();
	        break;	   
	    case 2:
	    	//PANTALLA: PACIENTES
	    	EnviarParametrosActMP2();
	        break;
	    case 3:
	    	//PANTALLA: MI AGENDA (CUIDADOR)        
	        EnviarParametrosActMP3();
	        break;
	    case 4:
	    	//PANTALLA: MI PERFIL (CUIDADOR)	        
	        EnviarParametrosActMP4();
	        break;
	    case 5:
			//PANTALLA: ACTUALIZAR DATOS
			ActualizarDatos();
	        break;
		case 6:
			//PANTALLA: INFORMACION
			Informacion();
			break;
		case 7:
			//PANTALLA: CERRAR SESION
			CerrarSesion();
			break;
		}
	    	    
	    if (fragment!=null) {	        
	        NavList.setItemChecked(position, true);
	        NavList.setSelection(position);	      
	        setTitle(titulos[position-1]);	        
	        NavDrawerLayout.closeDrawer(NavList);
	        mTitle = getTitle(); 
	    } else {
	        Log.e(String.valueOf(R.string.Error), String.valueOf(R.string.MostrarFragmento) + position);
	    }
	}
	  
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
	    super.onPostCreate(savedInstanceState);	   
	    mDrawerToggle.syncState();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    if (mDrawerToggle.onOptionsItemSelected(item)) {
	    	Log.e(String.valueOf(R.string.DrawerTogglePushed), String.valueOf(R.string.X));
	    	return true;
	    }	
	    return super.onOptionsItemSelected(item);
	}

	public Boolean MetodosActualizacionBaseVolley(Long idC){
		String idCuidador=String.valueOf(idC);
		Boolean ejecutado=false;

		EliminarDatos ed1= new EliminarDatos();
		ed1.EliminarDatosDB();

		try{
			VCActividadCuidadores objActCui= new VCActividadCuidadores(getApplicationContext());
			objActCui.BuscarAllActividadCuidadores(idCuidador);

			VCActividades objAct= new VCActividades(getApplicationContext());
			objAct.ListaDeActividades(idCuidador);

			VCActividadPaciente objActPac= new VCActividadPaciente(getApplicationContext());
			objActPac.BuscarAllActividadPacientes(idCuidador);

			VCBuzon objBuzon = new VCBuzon(getApplicationContext());
			objBuzon.BuscarAllBuzonesXCuidador(idCuidador);

			VCControlDieta objContDie= new VCControlDieta(getApplicationContext());
			objContDie.BuscarAllControlDietaCuidadores(idCuidador);

			VCControlMedicina objContMed= new VCControlMedicina(getApplicationContext());
			objContMed.BuscarAllControlMedicinaXCuidadores(idCuidador);

			VCCuidador objCuidador= new VCCuidador(getApplicationContext());
			objCuidador.ListaDeCuidadores(idCuidador);

			VCCuidadorPr objCuiPr= new VCCuidadorPr(getApplicationContext());
			objCuiPr.BuscarAllCuidadoresPr(idCuidador);

			VCCuidadorS objCuiS= new VCCuidadorS(getApplicationContext());
			objCuiS.BuscarAllCuidadoresS(idCuidador);

			VCEstadoSalud objEstSal= new VCEstadoSalud(getApplicationContext());
			objEstSal.BuscarAllEstadosSaludCuidadores(idCuidador);

			VCEventosCuidador objEvenCui= new VCEventosCuidador(getApplicationContext());
			objEvenCui.BuscarAllEventosCuidadores(idCuidador);

			VCEventosPaciente objEvenPac= new VCEventosPaciente(getApplicationContext());
			objEvenPac.BuscarAllEventosXCuidadores(idCuidador);

			VCFamiliaresPacientes objFamPac= new VCFamiliaresPacientes(getApplicationContext());
			objFamPac.ListaDeFamiliares(idCuidador);

			VCHorarioMedicinas objHorMed= new VCHorarioMedicinas(getApplicationContext());
			objHorMed.BuscarAllHorarioMedicinaXCuidadores(idCuidador);

			VCHorarios objHorario= new VCHorarios(getApplicationContext());
			objHorario.BuscarAllHorariosXCuidadores(idCuidador);

			VCInicioSesion objIniSes= new VCInicioSesion(getApplicationContext());
			objIniSes.BuscarAllIniciosSesion(idCuidador);

			VCObservaciones objObs= new VCObservaciones(getApplicationContext());
			objObs.BuscarAllObservacionesCuidadores(idCuidador);

			VCPacientes objPacientes= new VCPacientes(getApplicationContext());
			objPacientes.ListaDePacientes(idCuidador);

			VCPermisos objPermiso= new VCPermisos(getApplicationContext());
			objPermiso.BuscarAllPermisosXCuidadores(idCuidador);

			VCRutinasCuidadores objRutCui= new VCRutinasCuidadores(getApplicationContext());
			objRutCui.BuscarAllRutinasCuidadores(idCuidador);

			VCRutinasPacientes objRutPac= new VCRutinasPacientes(getApplicationContext());
			objRutPac.BuscarAllRutinasCuidadores(idCuidador);

			VCSeguimientoMedico objSegMed= new VCSeguimientoMedico(getApplicationContext());
			objSegMed.BuscarAllSeguimientoMedicoXCuidadores(idCuidador);

			VCTipoActividad objTipoAct= new VCTipoActividad(getApplicationContext());
			objTipoAct.BuscarAllTipoActividad();

			ejecutado=true;

		}catch(Exception err){
			ejecutado=false;
			Log.e("Descarga de datos", "Faaalllooo!", err);
		}
		return ejecutado;
	}

	//VERIFICAR LA CONEXION DE INTERNET
	public Boolean estaConectado(){
		if(conectadoWifi()){
			return true;
		}else{
			if(conectadoRedMovil()){
				return true;
			}else{
				Toast.makeText(MenuPrincipalRestringidoActivity.this, "No hay Conexion a Internet", Toast.LENGTH_SHORT).show();
				return false;
			}
		}
	}

	//VERIFICAR CONEXION POR WIFI
	protected Boolean conectadoWifi(){
		ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (info != null) { if (info.isConnected()) { return true;} }
		}
		return false;
	}

	//VERIFICAR CONEXION POR DATOS MOVILES
	protected Boolean conectadoRedMovil(){
		ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (info != null) { if (info.isConnected()) { return true; } }
		}
		return false;
	}
}