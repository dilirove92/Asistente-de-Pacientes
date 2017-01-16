package com.Notifications.patientssassistant;


import com.Notifications.patientssassistant.adapters.*;
import com.Notifications.patientssassistant.dialogos.*;
import com.Notifications.patientssassistant.tables.*;
import com.Notifications.patientssassistant.asynctask.*;
import com.Notifications.patientssassistant.volleyscalls.*;
//import android.support.multidex.MultiDex;
import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.orm.query.Condition;
import com.orm.query.Select;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.renderscript.Sampler.Value;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class IniciarSesionActivity extends Activity {

	//VARIABLES PARA INICIAR SESION
	private String campo_tipoUser;
	private String campo_usuario;
	private String campo_contrasena;
	private int request_code = 1;

	//VARIABLES PARA GUARDAR EL INICIO DE SESION
	private Long idTemporal=0L;
	private Long campo_idCoP;
	private String campo_tu;
	private Long campo_idIS;
	private static Long campo_idISE;

	//VARIABLES AUXILIARES	
	private static Boolean banderaNuevo=false;
	private String campo_fotoCoP;
	private Long campo_dependeDe;
	private Boolean campo_controlT;
	private Intent intent;
	private String cp;
	private String cs;
	private String pac;
	private FragmentManager fragmentManager = getFragmentManager();
	private TblInicioSesion estaSesion = new TblInicioSesion();
	private TblCuidadorS cuidadorS = new TblCuidadorS();
	private Boolean bandera;
	private String ip=VarEstatic.ObtenerIP();
	private Long unId;

	//VARIABLES DE IDS
	private Long idIS;
	private Long idCP;
	private Long idDD;

	//VARIABLES DE LOS ELEMENTOS DE LA IU
	Spinner CmbTipoUsuario;
	EditText EdtUsuario;
	EditText EdtContrasena;
	TextView TxtVsms;
	Button BtnIniciarSesion;
	Button BtnLinkContraseña;
	Button BtnLinkRegistrarse;
	//TextView TxtIdIS;
	//TextView TxtIdCP;
	//TextView TxtIdDD;

	//VARIABLES PARA POOLTHREAD
	int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
	private static final int KEEP_ALIVE_TIME = 1;
	private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

	//VARIABLES PARA GCM
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	private static final String TAG = "MainActivity";
	public static View view;
	private BroadcastReceiver mRegistrationBroadcastReceiver;
	public String Token;


	public IniciarSesionActivity() {super();}

	public void VariablesExtras() {
		cp=getString(R.string.CuidadorPrimario);
		cs=getString(R.string.CuidadorSecundario);
		pac=getString(R.string.Paciente);
	}

	private void tareaLarga()
	{
		try {
			Thread.sleep(1000);
		} catch(InterruptedException e) {}
	}

	public void EnviarParametrosActIS(){
		if (campo_tipoUser.equals(cp) || campo_tipoUser.equals(cs)) {
			intent.putExtra("varIdCoP", campo_idCoP);
			intent.putExtra("varTipoUsuario", campo_tipoUser);
			intent.putExtra("varUsuario", campo_usuario);
			intent.putExtra("varFotoC", campo_fotoCoP);
			intent.putExtra("varTU", campo_tu);
			intent.putExtra("varDependeDe", campo_dependeDe);
			intent.putExtra("varControlT", campo_controlT);
			intent.putExtra("varIdIS", campo_idIS);
		}
		if (campo_tipoUser.equals(pac)) {
			intent.putExtra("varIdCoP", campo_idCoP);
			intent.putExtra("varTipoUsuario", campo_tipoUser);
			intent.putExtra("varUsuario", campo_usuario);
			intent.putExtra("varFotoC", campo_fotoCoP);
			intent.putExtra("varIdIS", campo_idIS);
			intent.putExtra("varDependeDe", campo_dependeDe);
		}
	}


	//METODO QUE RETORNA "Nuevo"
	public static Boolean EsNuevo() {
		Boolean resultado=banderaNuevo;
		return resultado;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.iniciar_sesion);

		CmbTipoUsuario=(Spinner)findViewById(R.id.cmbTipoUsuario);
		EdtUsuario=(EditText)findViewById(R.id.edtUsuario);
		EdtContrasena=(EditText)findViewById(R.id.edtContrasena);
		TxtVsms=(TextView)findViewById(R.id.txtVsms);
		BtnIniciarSesion=(Button)findViewById(R.id.btnIniciarSesion);
		BtnLinkContraseña=(Button)findViewById(R.id.LinkOlvidoContr);
		BtnLinkRegistrarse=(Button)findViewById(R.id.LinkRegistrate);
		//TxtIdIS=(TextView)findViewById(R.id.txtIdIniSes);
		//TxtIdCP=(TextView)findViewById(R.id.txtIdCP);
		//TxtIdDD=(TextView)findViewById(R.id.txtIdDependeD);

		MisPreferencias();
		CargarOpcSpinner();
		BtnIniciar();
		LnkContrasena();
		LnkRegistrate();

		mRegistrationBroadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				SharedPreferences sharedPreferences =
						PreferenceManager.getDefaultSharedPreferences(context);
				boolean sentToken = sharedPreferences
						.getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
				if (sentToken) {
					// mInformationTextView.setText(getString(R.string.gcm_send_message));
				} else {
					// mInformationTextView.setText(getString(R.string.token_error_message));
				}
			}
		};

		GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
		int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (apiAvailability.isUserResolvableError(resultCode)) {
				apiAvailability.getErrorDialog(this, resultCode, 9000).show();
			} else {
				Log.i(TAG, "This device is not supported.");
				finish();
			}
		}
		Intent intent = new Intent(this,RegistrationIntentService.class);
		startService(intent);

		//idIS=0L; idCP=0L; idDD=0L;
	}

	//VERIFICAMOS SI LA SESION ESTA ABIERTA
	public void MisPreferencias() {
		VariablesExtras();
		//RECUPERAMOS LAS PREFERENCIAS ALMACENADAS
		SharedPreferences prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
		campo_idCoP = prefs.getLong("idCoP", 0L);
		campo_tipoUser = prefs.getString("tipoUser", "");
		campo_usuario = prefs.getString("usuario", "");
		campo_fotoCoP = prefs.getString("fotoCoP", "");
		campo_tu = prefs.getString("tu", "");
		campo_dependeDe = prefs.getLong("dependeDe", 0L);
		campo_controlT = prefs.getBoolean("controlT", false);
		campo_idIS = prefs.getLong("idIS", 0L);

		//OPCION CUIDADOR PRIMARIO
		if (campo_tu.equals("CP")) {
			intent=new Intent(IniciarSesionActivity.this, MenuPrincipalActivity.class);
			EnviarParametrosActIS();
			startActivity(intent);
			finish();
		}

		//OPCION CUIDADOR SECUNDARIO
		if (campo_tu.equals("CS")) {
			if (campo_controlT.equals(true)) {
				intent=new Intent(IniciarSesionActivity.this, MenuPrincipalActivity.class);
				EnviarParametrosActIS();
				startActivity(intent);
				finish();
			}else {
				intent=new Intent(IniciarSesionActivity.this, MenuPrincipalRestringidoActivity.class);
				EnviarParametrosActIS();
				startActivity(intent);
				finish();
			}
		}

		//OPCION PACIENTE
		if (campo_tu.equals("P")) {
			intent=new Intent(IniciarSesionActivity.this, MenuPrincipalPacienteActivity.class);
			EnviarParametrosActIS();
			startActivity(intent);
		}
	}

	public void CargarOpcSpinner() {
		String[] OpcTipoUsuario=getResources().getStringArray(R.array.opc_tipoUsuario);
		CmbTipoUsuario.setAdapter(new AdapterSpinnerSimple(IniciarSesionActivity.this, R.layout.adaptador_spinner, OpcTipoUsuario));
	}

	public void BtnIniciar() {
		BtnIniciarSesion.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if(estaConectado()){
					VariablesExtras();
					campo_tipoUser = CmbTipoUsuario.getSelectedItem().toString();
					campo_usuario = EdtUsuario.getText().toString().trim();
					campo_contrasena = EdtContrasena.getText().toString().trim();

					Calendar cal = new GregorianCalendar();
					cal.getTime();
					int anio = cal.get(Calendar.YEAR);
					int mes = cal.get(Calendar.MONTH);
					int dia = cal.get(Calendar.DAY_OF_MONTH);
					//String laFecha=String.valueOf(anio)+"/"+String.valueOf(mes)+"/"+String.valueOf(dia);

					int hora = cal.get(Calendar.HOUR_OF_DAY);
					int minutos = cal.get(Calendar.MINUTE);
					//String laHora=String.valueOf(hora)+":"+String.valueOf(minutos);

					if (campo_usuario.equals("") || campo_contrasena.equals("")) {
						DFIngresarDatos dialogo1 = new DFIngresarDatos();
						dialogo1.show(fragmentManager, "tagAlerta");
					} else {
						try {
							SharedPreferences prefs = getSharedPreferences("MisPreferencias123", Context.MODE_PRIVATE);
							Token=prefs.getString("tokengcm","no hay token");

							BtnIniciarSesion.setEnabled(false);
							TxtVsms.setText("Verificando datos...");

							String urlJson = "http://"+ip+"/ADP/InicioSesion/InicioSesionInsertarObject";

							HashMap<String, String> dato = new HashMap<String, String>();
							dato.put("Tipo", "");
							dato.put("AnioIni",String.valueOf(anio));
							dato.put("MesIni",String.valueOf(mes));
							dato.put("DiaIni",String.valueOf(dia));
							dato.put("HoraIni",String.valueOf(hora));
							dato.put("MinutosIni",String.valueOf(minutos));
							dato.put("AnioFin",String.valueOf(anio));
							dato.put("MesFin",String.valueOf(mes));
							dato.put("DiaFin",String.valueOf(dia));
							dato.put("HoraFin",String.valueOf(hora));
							dato.put("MinutosFin",String.valueOf(minutos));
							dato.put("IdReGCM",Token);
							dato.put("Eliminado","false");
							dato.put("Usuario", campo_usuario);
							dato.put("Contrasena", campo_contrasena);
							dato.put("TipoUsuario", campo_tipoUser);

							request = new JsonObjectRequest(Request.Method.POST, urlJson, new JSONObject(dato), new Response.Listener<JSONObject>() {
								@Override
								public void onResponse(JSONObject response) {
									Log.d(TAG, response.toString());
									try {
										Long respuesta=response.getLong("IdIniSes");
										String idResp=String.valueOf(respuesta);

										if(respuesta==0){
											TxtVsms.setText(R.string.UserPassIncorrectos);
											BtnIniciarSesion.setEnabled(true);
										}else{
											VCBuscarUnInicioSesion(idResp, campo_tipoUser);
											TxtVsms.setText("Conectando...");
										}
									} catch (JSONException e) {
										e.printStackTrace();
									}
								}
							}, new Response.ErrorListener() {
								@Override
								public void onErrorResponse(VolleyError error) {
									VolleyLog.d(TAG, "Error: " + error.getMessage());
									TxtVsms.setText(R.string.UserPassIncorrectos);
									BtnIniciarSesion.setEnabled(true);
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
							request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
							VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

						} catch (Exception ex) {
							//TxtVsms.setText(R.string.UserPassIncorrectos);
							//BtnIniciar();
						}
					}
				}
			}
		});
	}

	public long DevolverIniSesion(){
		return campo_idIS;
	}

	public void MiPreferenciaCui() {
		//ALMACENAR PREFERENCIA
		SharedPreferences settings = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putLong("idCoP", campo_idCoP);
		editor.putString("tipoUser", campo_tipoUser);
		editor.putString("usuario", campo_usuario);
		editor.putString("fotoCoP", campo_fotoCoP);
		editor.putString("tu", campo_tu);
		editor.putLong("dependeDe", campo_dependeDe);
		editor.putBoolean("controlT", campo_controlT);
		editor.putLong("idIS", campo_idIS);
		//CONFIRMAR EL ALMACENAMIENTO
		editor.commit();
	}

	public void MiPreferenciaPac() {
		//ALMACENAR PREFERENCIA
		SharedPreferences settings = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putLong("idCoP", campo_idCoP);
		editor.putString("tipoUser", campo_tipoUser);
		editor.putString("usuario", campo_usuario);
		editor.putString("fotoCoP", campo_fotoCoP);
		editor.putString("tu", campo_tu);
		editor.putLong("dependeDe", campo_dependeDe);
		editor.putBoolean("controlT", false);
		editor.putLong("idIS", campo_idIS);
		//CONFIRMAR EL ALMACENAMIENTO
		editor.commit();
	}

	public void LnkContrasena() {
		BtnLinkContraseña.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					Intent intent=new Intent(IniciarSesionActivity.this, RecuperarContrasenaActivity.class);
					startActivity(intent);
				} catch (Exception ex) {
					Toast.makeText(IniciarSesionActivity.this, getString(R.string.Error) + ex.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	public void LnkRegistrate() {
		BtnLinkRegistrarse.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					Intent intent=new Intent(IniciarSesionActivity.this, RegistrarAhoraActivity.class);
					startActivityForResult(intent, request_code);
					banderaNuevo=true;
				} catch (Exception ex) {
					Toast.makeText(IniciarSesionActivity.this, getString(R.string.Error) + ex.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if ((requestCode == request_code) && (resultCode == IniciarSesionActivity.RESULT_OK)){
			banderaNuevo=false;
		}
	}

	//VOLLEYCALLS DE INICIO DE SESION
	JsonObjectRequest request;
	JsonArrayRequest req;

	public void VCBuscarUnInicioSesion(final String... params){
		String id=params[0];
		final String tipo_user=params[1];
		String urlJsonObject = "http://"+ip+"/ADP/InicioSesion/InicioSesionBuscar/"+id;
		request = new JsonObjectRequest(urlJsonObject,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Log.d(TAG, response.toString());

						try {
							TblInicioSesion iniSes = new TblInicioSesion();
							iniSes.setIdIniSes(response.getLong("IdIniSes"));
							iniSes.setIdCuiPac(response.getLong("IdCuiPac"));
							iniSes.setTipoUser(response.getString("Tipo"));
							iniSes.setUsuario(response.getString("Usuario"));
							iniSes.setAnioIni(response.getInt("AnioIni"));
							iniSes.setMesIni(response.getInt("MesIni"));
							iniSes.setDiaIni(response.getInt("DiaIni"));
							iniSes.setHoraIni(response.getInt("HoraIni"));
							iniSes.setMinutosIni(response.getInt("MinutosIni"));
							iniSes.setAnioFin(response.getInt("AnioFin"));
							iniSes.setMesFin(response.getInt("MesFin"));
							iniSes.setDiaFin(response.getInt("DiaFin"));
							iniSes.setHoraFin(response.getInt("HoraFin"));
							iniSes.setMinutosFin(response.getInt("MinutosFin"));
							iniSes.setIdReGCM(response.getString("IdReGCM"));
							iniSes.setEliminado(response.getBoolean("Eliminado"));

							Long idIS=response.getLong("IdIniSes");String idISs=String.valueOf(idIS);
							Long idCP=response.getLong("IdCuiPac");String idCPs=String.valueOf(idCP);
							Long idDD=response.getLong("IdDD");String idDDs=String.valueOf(idDD);

							String campo_ide=String.valueOf(iniSes.getIdIniSes());

							Select InicioSesion = Select.from(TblInicioSesion.class)
									.where(Condition.prop("ID_INI_SES").eq(campo_ide));

							TblInicioSesion iniSesion=(TblInicioSesion)InicioSesion.first();

							if (iniSesion!=null) {
								iniSesion.setIdIniSes(iniSes.getIdIniSes());
								iniSesion.setIdCuiPac(iniSes.getIdCuiPac());
								iniSesion.setTipoUser(iniSes.getTipoUser());
								iniSesion.setUsuario(iniSes.getUsuario());
								iniSesion.setAnioIni(iniSes.getAnioIni());
								iniSesion.setMesIni(iniSes.getMesIni());
								iniSesion.setDiaIni(iniSes.getDiaIni());
								iniSesion.setHoraIni(iniSes.getHoraIni());
								iniSesion.setMinutosIni(iniSes.getMinutosIni());
								iniSesion.setAnioFin(iniSes.getAnioFin());
								iniSesion.setMesFin(iniSes.getMesFin());
								iniSesion.setDiaFin(iniSes.getDiaFin());
								iniSesion.setHoraFin(iniSes.getHoraFin());
								iniSesion.setMinutosFin(iniSes.getHoraFin());
								iniSesion.setIdReGCM(iniSes.getIdReGCM());
								iniSesion.setEliminado(iniSes.getEliminado());
								iniSesion.save();
							} else {
								TblInicioSesion guardar_sesion= new TblInicioSesion(
										iniSes.getIdIniSes(), iniSes.getIdCuiPac(), iniSes.getTipoUser(),
										iniSes.getUsuario(), iniSes.getAnioIni(),iniSes.getMesIni(),
										iniSes.getDiaIni(), iniSes.getHoraIni(), iniSes.getMinutosIni(),
										iniSes.getAnioFin(),iniSes.getMesFin(), iniSes.getDiaFin(),
										iniSes.getHoraFin(), iniSes.getMinutosFin(), iniSes.getIdReGCM(),
										iniSes.getEliminado());
								guardar_sesion.save();
							}
							if(tipo_user.equals("Cuidador primario")||tipo_user.equals("Cuidador secundario")) {
								BuscarUnCuidador(iniSes.getIdCuiPac().toString(), iniSes.getIdIniSes().toString(),idDDs.toString());
							}else if (tipo_user.equals("Paciente")){
								BuscarUnPaciente(iniSes.getIdCuiPac().toString(), iniSes.getIdIniSes().toString(),idDDs.toString());
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
		});
		request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
	}

	//TAREA ASINCRONA PARA BUSCAR UN REGISTRO DE CUIDADOR
	public void BuscarUnCuidador(final String... params){
		String id=params[0];
		final String idISs=params[1];
		final String idDDs=params[2];
		String urlJsonObject = "http://"+ip+"/ADP/Cuidador/CuidadorBuscar/"+id;
		request = new JsonObjectRequest(urlJsonObject,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Log.d(TAG, response.toString());

						try {
							TblCuidador cuidador = new TblCuidador();
							cuidador.setIdCuidador(response.getLong("IdCuidador"));
							cuidador.setUsuarioC(response.getString("UsuarioC"));
							cuidador.setNombreC(response.getString("NombreC"));
							cuidador.setCiRucC(response.getString("CiRucC"));
							cuidador.setCelularC(response.getString("CelularC"));
							cuidador.setTelefonoC(response.getString("TelefonoC"));
							cuidador.setEmailC(response.getString("EmailC"));
							cuidador.setDireccionC(response.getString("DireccionC"));
							cuidador.setCargoC(response.getString("CargoC"));
							cuidador.setControlTotal(response.getBoolean("ControlTotal"));
							cuidador.setFotoC(response.getString("FotoC"));
							cuidador.setEliminado(response.getBoolean("Eliminado"));

							if(response!=null){

								String campo_ide=String.valueOf(cuidador.getIdCuidador());
								Select unCuidador = Select.from(TblCuidador.class)
										.where(Condition.prop("ID_CUIDADOR").eq(campo_ide));
								TblCuidador edit_Cuidador=(TblCuidador)unCuidador.first();

								if (edit_Cuidador!=null) {
									edit_Cuidador.setIdCuidador(cuidador.getIdCuidador());
									edit_Cuidador.setUsuarioC(cuidador.getUsuarioC());
									edit_Cuidador.setNombreC(cuidador.getNombreC());
									edit_Cuidador.setCiRucC(cuidador.getCiRucC());
									edit_Cuidador.setCelularC(cuidador.getCelularC());
									edit_Cuidador.setTelefonoC(cuidador.getTelefonoC());
									edit_Cuidador.setEmailC(cuidador.getEmailC());
									edit_Cuidador.setDireccionC(cuidador.getDireccionC());
									edit_Cuidador.setCargoC(cuidador.getCargoC());
									edit_Cuidador.setControlTotal(cuidador.getControlTotal());
									edit_Cuidador.setFotoC(cuidador.getFotoC());
									edit_Cuidador.setEliminado(cuidador.getEliminado());
									edit_Cuidador.save();

								}
								else{
									TblCuidador newCuidador= new TblCuidador(
											cuidador.getIdCuidador(),cuidador.getUsuarioC(),
											cuidador.getNombreC(), cuidador.getCiRucC(),
											cuidador.getCelularC(), cuidador.getTelefonoC(),
											cuidador.getEmailC(), cuidador.getDireccionC(),
											cuidador.getCargoC(), cuidador.getControlTotal(),
											cuidador.getFotoC(), cuidador.getEliminado());
									newCuidador.save();

								}
								Log.e("DatoCuidador", ": " + response.toString());

								if(cuidador.getIdCuidador()==Long.parseLong(idDDs)) {
									LlenarParametrosCP(idISs, cuidador);
								}else{
									LlenarParametrosCS(idISs, cuidador, idDDs);
								}
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
		});
		VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
	}

	//BUSCAR LOS DATOS DE UN PACIENTE
	public void BuscarUnPaciente(final String... params){
		String id=params[0];
		final String idISs=params[1];
		final String idDDs=params[2];
		String urlJsonObject = "http://"+ip+"/ADP/Pacientes/PacienteBuscar/"+id;
		request = new JsonObjectRequest(urlJsonObject,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Log.d(TAG, response.toString());

						try {
							TblPacientes paciente = new TblPacientes();
							paciente.setIdPaciente(response.getLong("IdPaciente"));
							paciente.setUsuarioP(response.getString("UsuarioP"));
							paciente.setCiP(response.getString("CiP"));
							paciente.setNombreApellidoP(response.getString("NombreApellidoP"));
							paciente.setAnio(response.getInt("Anio"));
							paciente.setMes(response.getInt("Mes"));
							paciente.setDia(response.getInt("Dia"));
							paciente.setEstadoCivilP(response.getString("EstadoCivilP"));
							paciente.setNivelEstudioP(response.getString("NivelEstudioP"));
							paciente.setMotivoIngresoP(response.getString("MotivoIngresoP"));
							paciente.setTipoPacienteP(response.getString("TipoPacienteP"));
							paciente.setEdad(response.getInt("Edad"));
							paciente.setFotoP(response.getString("FotoP"));
							paciente.setEliminado(response.getBoolean("Eliminado"));

							if(paciente!=null){

								String campo_ide=String.valueOf(paciente.getIdPaciente());
								Select elPaciente = Select.from(TblPacientes.class)
										.where(Condition.prop("ID_PACIENTE").eq(campo_ide));
								TblPacientes edit_Pac=(TblPacientes)elPaciente.first();

								if (edit_Pac!=null) {
									edit_Pac.setIdPaciente(paciente.getIdPaciente());
									edit_Pac.setUsuarioP(paciente.getUsuarioP());
									edit_Pac.setCiP(paciente.getCiP());
									edit_Pac.setNombreApellidoP(paciente.getNombreApellidoP());
									edit_Pac.setAnio(paciente.getAnio());
									edit_Pac.setMes(paciente.getMes());
									edit_Pac.setDia(paciente.getDia());
									edit_Pac.setEstadoCivilP(paciente.getEstadoCivilP());
									edit_Pac.setNivelEstudioP(paciente.getNivelEstudioP());
									edit_Pac.setMotivoIngresoP(paciente.getMotivoIngresoP());
									edit_Pac.setTipoPacienteP(paciente.getTipoPacienteP());
									edit_Pac.setEdad(paciente.getEdad());
									edit_Pac.setFotoP(paciente.getFotoP());
									edit_Pac.setEliminado(paciente.getEliminado());
									edit_Pac.save();
								}
								else{
									TblPacientes newPac = new TblPacientes(
											paciente.getIdPaciente(), paciente.getUsuarioP(),
											paciente.getCiP(), paciente.getNombreApellidoP(),
											paciente.getAnio(), paciente.getMes(), paciente.getDia(),
											paciente.getEstadoCivilP(), paciente.getNivelEstudioP(),
											paciente.getMotivoIngresoP(), paciente.getTipoPacienteP(),
											paciente.getEdad(), paciente.getFotoP(), paciente.getEliminado());
									newPac.save();
								}
								Log.e("DatoPaciente", ": " + response.toString());
								LlenarParametrosP(idISs,paciente,idDDs);
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
		});
		VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
	}

	public void LlenarParametrosCP(String idIS, TblCuidador buscar_userCP){

		campo_idCoP = buscar_userCP.getIdCuidador();
		campo_fotoCoP = buscar_userCP.getFotoC();
		campo_controlT = buscar_userCP.getControlTotal();
		campo_dependeDe = buscar_userCP.getIdCuidador();
		campo_controlT=true;
		//GUARDANDO DATOS DE INICIO DE SESION
		campo_tu = "CP";
		campo_idIS=Long.parseLong(idIS);
		MiPreferenciaCui();

		TimeHilos th = new TimeHilos();
		th.HilosIniciales();
		BtnIniciarSesion.setEnabled(true);
		TxtVsms.setText("");

		intent = new Intent(IniciarSesionActivity.this, MenuPrincipalActivity.class);
		EnviarParametrosActIS();
		startActivity(intent);
		finish();
	}

	public void LlenarParametrosCS(String idIS, TblCuidador buscar_userCS, String idDDs){

		campo_idCoP = buscar_userCS.getIdCuidador();
		campo_fotoCoP = buscar_userCS.getFotoC();
		campo_controlT = buscar_userCS.getControlTotal();
		campo_dependeDe = Long.parseLong(idDDs);
		//GUARDANDO DATOS DE INICIO DE SESION
		campo_tu = "CS";
		campo_idIS = Long.parseLong(idIS);
		//ALMACENAMOS LA PREFERENCIA
		MiPreferenciaCui();

		TimeHilos th = new TimeHilos();
		th.HilosIniciales();
		BtnIniciarSesion.setEnabled(true);
		TxtVsms.setText("");

		//INICIANDO SIGUIENTE ACTIVIDAD
		if (campo_controlT.equals(true)) {
			//ENTRAMOS EN LA APP
			intent = new Intent(IniciarSesionActivity.this, MenuPrincipalActivity.class);
			EnviarParametrosActIS();
			startActivity(intent);
			finish();
		} else {
			//ENTRAMOS EN LA APP
			intent = new Intent(IniciarSesionActivity.this, MenuPrincipalRestringidoActivity.class);
			EnviarParametrosActIS();
			startActivity(intent);
			finish();
		}
	}

	public void LlenarParametrosP(String idIS, TblPacientes buscar_userP, String idDDs){

		campo_idCoP = buscar_userP.getIdPaciente();
		campo_fotoCoP = buscar_userP.getFotoP();
		campo_dependeDe=Long.parseLong(idDDs);
		//GUARDANDO DATOS DE INICIO DE SESION
		campo_tu = "P";
		campo_idIS = Long.parseLong(idIS);
		//ALMACENAMOS LA PREFERENCIA
		MiPreferenciaPac();

		try{

			ATTipoActividad actividades = new ATTipoActividad();
			actividades.new BuscarAllTipoActividad().execute();

			//---------> here
			ATActividadPaciente actPac = new ATActividadPaciente();
			List<TblActividadPaciente> listAP= actPac.new BuscarAllActividadXPaciente().execute(String.valueOf(buscar_userP.getIdPaciente())).get();

			int con=0;
			for(int i=0; i<listAP.size();i++){
				//ATActividades act = new ATActividades();
				//act.new BuscarUnaActividad(String.valueOf(listAP.get(i).getIdActividad())).execute(String.valueOf(listAP.get(i).getIdActividad()));
				VCActividades act=new VCActividades(getApplicationContext());
				act.BuscarUnaActividad(String.valueOf(listAP.get(i).getIdActividad()));
				con++;
				tareaLarga();
			}

		}catch(Exception err){
			Log.e("Actividades", "Errrrrrrrrrrrrrrrrror!", err);
		}

		BtnIniciarSesion.setEnabled(true);
		TxtVsms.setText("");

		//ENTRAMOS EN LA APP
		intent = new Intent(IniciarSesionActivity.this, MenuPrincipalPacienteActivity.class);
		EnviarParametrosActIS();
		startActivity(intent);
		finish();
	}

	//VERIFICAR LA CONEXION DE INTERNET
	public Boolean estaConectado(){
		if(conectadoWifi()){
			return true;
		}else{
			if(conectadoRedMovil()){
				return true;
			}else{
				Toast.makeText(IniciarSesionActivity.this, "No hay Conexion a Internet", Toast.LENGTH_SHORT).show();
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