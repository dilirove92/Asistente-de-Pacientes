package com.Notifications.patientssassistant.asynctask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.Notifications.patientssassistant.VarEstatic;
import com.Notifications.patientssassistant.tables.TblActividadCuidador;
import com.Notifications.patientssassistant.tables.TblCuidador;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.orm.query.Condition;
import com.orm.query.Select;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class ATActividadCuidadores {
	
	//VARIABLES DE CLASE ATActividadCuidadores
	//public static String ip="192.168.1.4:1522";
	private static String ip=VarEstatic.ObtenerIP();
	
	//TAREA ASINCRONA PARA INSERTAR UNA ACTIVIDAD DE CUIDADOR
	public class InsertarActividadCuidadores extends AsyncTask<String, Integer, Boolean>
    {
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			Boolean resul=false;
			HttpClient httpClient=new DefaultHttpClient();
			
			HttpPost post=new HttpPost(
					"http://"+ip+"/ADP/ActividadCuidador/ActividadCuidadorInsertar");
			post.setHeader("content-type", "application/json");
			
			try {
				JSONObject dato=new JSONObject();
				
				dato.put("IdCuidador", Long.parseLong(params[0]));
				dato.put("IdActividad", Long.parseLong(params[1]));
				dato.put("Eliminado", Boolean.parseBoolean(params[2]));
				
				StringEntity entity = new StringEntity(dato.toString());
				post.setEntity(entity);
				
				HttpResponse resp=httpClient.execute(post);
				String respStr = EntityUtils.toString(resp.getEntity());
				
				if (respStr.equals("true")) {
					
					TblActividadCuidador guardar_actCui = new TblActividadCuidador(
							Long.parseLong(params[0]), Long.parseLong(params[1]), Boolean.parseBoolean(params[2]));
					guardar_actCui.save();
					
					resul=true;
				}
				
			} catch (Exception ex) {
				Log.e("ServicioRest", "Error!", ex);
				resul = false;
			}
			return resul;
		}
		
		@Override
		protected void onPostExecute(Boolean resul ) {
			// TODO Auto-generated method stub
		
		}
		
    }  
	
	//TAREA ASINCRONA PARA ELIMINAR UNA (ACTIVIDAD DE CUIDADOR)
	public class ActualizarActividadCuidadores extends AsyncTask<String, Integer, Boolean>
    {

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			Boolean resul=false;
			HttpClient httpClient=new DefaultHttpClient();
			
			HttpPut put=new HttpPut(
					"http://"+ip+"/ADP/ActividadCuidador/ActividadCuidadorEliminar");
			put.setHeader("content-type", "application/json");
			
			try {
				JSONObject dato=new JSONObject();
				
				dato.put("IdCuidador", Long.parseLong(params[0]));
				dato.put("IdActividad", Long.parseLong(params[1]));
				dato.put("Eliminado", Boolean.parseBoolean(params[2]));
				
				StringEntity entity = new StringEntity(dato.toString());
				put.setEntity(entity);
				
				HttpResponse resp=httpClient.execute(put);
				String respStr = EntityUtils.toString(resp.getEntity());
				
				if (respStr.equals("true")) {
					Select actCuidador = Select.from(TblActividadCuidador.class)
							.where(Condition.prop("ID_CUIDADOR").eq(params[0]), 
									Condition.prop("ID_ACTIVIDAD").eq(params[1]));
					TblActividadCuidador edit_Actividad=(TblActividadCuidador)actCuidador.first();
					if (edit_Actividad!=null) {
						edit_Actividad.delete();
					}
					resul=true;
				}
				
			} catch (Exception ex) {
				Log.e("ServicioRest", "Error!", ex);
				resul = false;
			}
			return resul;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
		}
		
    }
	
	//TAREA ASINCRONA PARA BUSCAR ACTIVIDADES DE UN CUIDADOR
	public class BuscarAllActividadCuidador extends AsyncTask<String, Integer, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			String id=params[0];
			HttpClient httpClient= new DefaultHttpClient();
			HttpGet BT= new HttpGet (
					"http://"+ip+"/ADP/ActividadCuidador/ActividadCuidadorBuscarAllCuidador/"+id);
			BT.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BT);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONArray respJSON = new JSONArray(respStr);
				
				
					for (int i = 0; i <respJSON.length(); i++) {
						JSONObject obj=respJSON.getJSONObject(i);
						
						TblActividadCuidador actCui= new TblActividadCuidador();
						actCui.setIdCuidador(obj.getLong("IdCuidador"));
						actCui.setIdActividad(obj.getLong("IdActividad"));
						actCui.setEliminado(obj.getBoolean("Eliminado"));
						
						TblActividadCuidador guardar_actCui = new TblActividadCuidador(
								actCui.getIdCuidador(), actCui.getIdActividad(), actCui.getEliminado());
						guardar_actCui.save();
					}
					
				
			} catch (Exception ex) {
				// TODO: handle exception
				Log.e("Serviciorest","Error!",ex);
				resul=false;
			}
 
    		return resul;
		}
    	
    	@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
		}
    	
    }
    
	//(ACTUALIZAR TABLA ACTIVIDADES CUIDADORES)
	//TAREA ASINCRONA PARA BUSCAR ACTIVIDADES DE TODOS LOS CUIDADORES
    public class BuscarAllActividadCuidadores extends AsyncTask<String, Integer, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			String id=params[0];
			HttpClient httpClient= new DefaultHttpClient();
			HttpGet BT= new HttpGet (
					"http://"+ip+"/ADP/ActividadCuidador/ActividadCuidadorBuscarAllCuidadores/"+id);
			BT.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BT);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONArray respJSON = new JSONArray(respStr);
				
				
					for (int i = 0; i <respJSON.length(); i++) {
						JSONObject obj=respJSON.getJSONObject(i);
						
						TblActividadCuidador actCui= new TblActividadCuidador();
						actCui.setIdCuidador(obj.getLong("IdCuidador"));
						actCui.setIdActividad(obj.getLong("IdActividad"));
						actCui.setEliminado(obj.getBoolean("Eliminado"));
						
						TblActividadCuidador guardar_actCui = new TblActividadCuidador(
								actCui.getIdCuidador(), actCui.getIdActividad(), actCui.getEliminado());
						guardar_actCui.save();
					}
					
				
			} catch (Exception ex) {
				// TODO: handle exception
				Log.e("Serviciorest","Error!",ex);
				resul=false;
			}
 
    		return resul;
		}
    	
    	@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
		}
    	
    }

	public JsonArrayRequest AB_ActividadesCuidador(long idC, final String TAG){
		String urlJsonArray = "http://"+ip+"/ADP/ActividadCuidador/ActividadCuidadorBuscarAllCuidadores/"+String.valueOf(idC);
		JsonArrayRequest req = new JsonArrayRequest(urlJsonArray,
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						Log.d(TAG, response.toString());

						try {
							//String jsonResponse = "";
							for (int i = 0; i < response.length(); i++) {
								String jsonResponse1 = "";
								JSONObject obj = (JSONObject) response.get(i);
								TblActividadCuidador actividad= new TblActividadCuidador();
								actividad.setIdCuidador(obj.getLong("IdCuidador"));
								actividad.setIdActividad(obj.getLong("IdActividad"));
								actividad.setEliminado(obj.getBoolean("Eliminado"));
								actividad.save();

								jsonResponse1 += "IdCuidador: " + actividad.getIdCuidador() + "\n";
								jsonResponse1 += "IdActividad: " + actividad.getIdActividad() + "\n";
								jsonResponse1 += "Eliminado: " + actividad.getEliminado() + "\n\n";
								Log.e("Act_Cui => Registro", "#" + i + "= " + jsonResponse1);
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
		return req;
	}
    
}
