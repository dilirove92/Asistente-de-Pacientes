package com.Notifications.patientssassistant;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import com.Notifications.patientssassistant.asynctask.*;
import com.Notifications.patientssassistant.tables.TblFamiliaresPacientes;
import com.Notifications.patientssassistant.volleyscalls.VolleySingleton;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class EnviarReporteActivity extends Activity {
	
	//VARIABLES DE LOS ELEMENTOS DE LA IU
	TextView TxtReportes;	
	Button BtnEnviar;
	Button BtnCancelar;
	
	//VARIABLES EXTRAS
	private Long vIdFamiliar;
	private Long vIdPaciente;
	private Long vIdCuidador;

	JsonObjectRequest request;
	JsonArrayRequest req;
	final String TAG = "VCFamiliaresPacientes";
	private static String ip= VarEstatic.ObtenerIP();

	public EnviarReporteActivity() { super(); }

	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.enviar_reportes);
		
		TxtReportes = (TextView)findViewById(R.id.txtReporte);
		BtnEnviar = (Button)findViewById(R.id.btnEnviar);
		BtnCancelar = (Button)findViewById(R.id.btnCancelar);
		
		RecogerParametros();
		CargarDatos();
		BtnEnviarReporte();
		BtnCancelar();
	}	
	
	public void RecogerParametros() {
		vIdFamiliar = getIntent().getExtras().getLong("IdFamiliar");
		vIdPaciente = getIntent().getExtras().getLong("IdPaciente");
		vIdCuidador = getIntent().getExtras().getLong("IdCuidador");
	}
	
	public void CargarDatos() {
		TxtReportes.setText("Cargando Reporte ...");

		String urlJson = "http://"+ip+"/ADP/FamiliaresPacientes/FamiliarPacienteLeerReporteObject";

		HashMap<String, String> dato = new HashMap<String, String>();
		dato.put("IdFamiliarPaciente", String.valueOf(vIdFamiliar));
		dato.put("IdPaciente", String.valueOf(vIdPaciente));
		dato.put("IdCuidador", String.valueOf(vIdCuidador));

		request = new JsonObjectRequest(Request.Method.POST, urlJson, new JSONObject(dato), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, response.toString());
                try {
                    String respuesta=response.getString("Reporte");
                        TxtReportes.setText(respuesta);
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
		//reporte = reporte1.replaceAll("\n","");
		// /String[] arregloString = reporte1.split("\n");

		//for(int x=0; x < arregloString.length; x++){
		//	reporte+=(arregloString[x])+"\n";
		//}
		//reporte=new String(reporte1);
		//TxtReportes.setText(reporte);
	}
	
	public void BtnEnviarReporte() {
		BtnEnviar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(estaConectado()) {
					try {
						ATFamiliaresPacientes famPac = new ATFamiliaresPacientes();
						famPac.new EnviarReporteAFamiliar().execute(String.valueOf(vIdFamiliar), String.valueOf(vIdPaciente),
								String.valueOf(vIdCuidador)).get();

						Toast.makeText(EnviarReporteActivity.this, getString(R.string.ReporteEnviado), Toast.LENGTH_LONG).show();
						LimpiarElementos();
						finish();
					} catch (Exception ex) {
						Toast.makeText(EnviarReporteActivity.this, getString(R.string.ReporteFallido), Toast.LENGTH_LONG).show();
						ex.printStackTrace();
					}
				}
			}
		});		
	}
	
	public void BtnCancelar() {
		BtnCancelar.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				finish();				
			}
		});
	}
	
	public void LimpiarElementos() {
		TxtReportes.setText("");
	}

	//VERIFICAR LA CONEXION DE INTERNET
	public Boolean estaConectado(){
		if(conectadoWifi()){
			return true;
		}else{
			if(conectadoRedMovil()){
				return true;
			}else{
				Toast.makeText(EnviarReporteActivity.this, "No hay Conexion a Internet", Toast.LENGTH_SHORT).show();
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