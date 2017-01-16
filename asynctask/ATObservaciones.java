package com.Notifications.patientssassistant.asynctask;

import java.text.SimpleDateFormat;
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
import android.os.AsyncTask;
import android.util.Log;
import com.Notifications.patientssassistant.VarEstatic;
import com.Notifications.patientssassistant.tables.TblObservaciones;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.orm.query.Condition;
import com.orm.query.Select;


public class ATObservaciones {

	//VARIABLES DE CLASE ATObservaciones 
	//private static String ip="192.168.1.4:1522";	
	private static String ip=VarEstatic.ObtenerIP();
	private static SimpleDateFormat sfDate =  new  SimpleDateFormat ("yyy-MM-dd");
	
	//TAREA ASINCRONA PARA INSERTAR UNA OBSERVACION
	public class InsertarObservacion extends AsyncTask<String, Integer, Long>
    {
    	private TblObservaciones observa;
		private long id=0;
    	
		@Override
		protected Long doInBackground(String... params) {
			// TODO Auto-generated method stub
			Boolean resul=true;
			long respuesta=0;
			HttpClient httpClient=new DefaultHttpClient();
			
			HttpPost post=new HttpPost(
					"http://"+ip+"/ADP/Observaciones/ObservacionInsertar");
			post.setHeader("content-type", "application/json");
			
			try {
				//Construimos el objeto alumno en formato JSON
				JSONObject dato=new JSONObject();
				
				dato.put("IdObservacion", Long.parseLong(params[0]));
				dato.put("IdPaciente", Long.parseLong(params[1]));
				dato.put("Anio", Integer.parseInt(params[2]));
				dato.put("Mes", Integer.parseInt(params[3]));
				dato.put("Dia", Integer.parseInt(params[4]));
				dato.put("Hora", Integer.parseInt(params[5]));
				dato.put("Minutos", Integer.parseInt(params[6]));
				dato.put("Observacion",params[7]);
				dato.put("Eliminado",Boolean.parseBoolean(params[8]));
				
				StringEntity entity = new StringEntity(dato.toString());
				post.setEntity(entity);
				
				HttpResponse resp=httpClient.execute(post);
				String respStr = EntityUtils.toString(resp.getEntity());
				respuesta=Long.parseLong(respStr);
				
				if (respuesta>0){
					observa = new TblObservaciones(
							respuesta, Long.parseLong(params[1]),
							Integer.parseInt(params[2]), 
							Integer.parseInt(params[3]),
							Integer.parseInt(params[4]), 
							Integer.parseInt(params[5]),
							Integer.parseInt(params[6]),
							params[7], Boolean.parseBoolean(params[8]));
					observa.save();
				}else{resul=false;}
				
			} catch (Exception ex) {
				Log.e("ServicioRest", "Error!", ex);
				resul = false;
			}
			return respuesta;
		}
		
		@Override
		protected void onPostExecute(Long resul ) {
		}
		
    }  
    	
	//TAREA ASINCRONA PARA ACTUALIZAR UNA OBSERVACION
    public class ActualizarObservacion extends AsyncTask<String, Integer, Boolean>
    {
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			Boolean resul=true;
			HttpClient httpClient=new DefaultHttpClient();
			
			HttpPut put=new HttpPut(
					"http://"+ip+"/ADP/Observaciones/ObservacionActualizar");
			put.setHeader("content-type", "application/json");
			
			try {
				//Construimos el objeto alumno en formato JSON
				JSONObject dato=new JSONObject();
				
				dato.put("IdObservacion", Long.parseLong(params[0]));
				dato.put("IdPaciente", Long.parseLong(params[1]));
				dato.put("Anio", Integer.parseInt(params[2]));
				dato.put("Mes", Integer.parseInt(params[3]));
				dato.put("Dia", Integer.parseInt(params[4]));
				dato.put("Hora", Integer.parseInt(params[5]));
				dato.put("Minutos", Integer.parseInt(params[6]));
				dato.put("Observacion",params[7]);
				dato.put("Eliminado",Boolean.parseBoolean(params[8]));
				
				StringEntity entity = new StringEntity(dato.toString());
				put.setEntity(entity);
				
				HttpResponse resp=httpClient.execute(put);
				String respStr = EntityUtils.toString(resp.getEntity());
				
				if (!respStr.equals("true")) {
					resul=false;
				}else{
					String campo_ide=String.valueOf(Long.parseLong(params[0]));
					Select observacion = Select.from(TblObservaciones.class)
							.where(Condition.prop("ID_OBSERVACION").eq(campo_ide));
					TblObservaciones editar_Ob=(TblObservaciones)observacion.first();

					if (editar_Ob!=null) {
						editar_Ob.setIdObservacion(Long.parseLong(params[0]));
						editar_Ob.setIdPaciente(Long.parseLong(params[1]));
						editar_Ob.setAnio(Integer.parseInt(params[2]));
						editar_Ob.setMes(Integer.parseInt(params[3]));
						editar_Ob.setDia(Integer.parseInt(params[4]));
						editar_Ob.setHora(Integer.parseInt(params[5]));
						editar_Ob.setMinutos(Integer.parseInt(params[6]));
						editar_Ob.setObservacion(params[7]);
						editar_Ob.setEliminado(Boolean.parseBoolean(params[8]));
						editar_Ob.save();
					}
				}
				
			} catch (Exception ex) {
				Log.e("ServicioRest", "Error!", ex);
				resul = false;
			}
			return resul;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
		}
		
    }
      
    //TAREA ASINCRONA PARA BUSCAR UNA OBSERVACION
    public class BuscarObservacion extends AsyncTask<String, Integer, Boolean>{

		private TblObservaciones observa= new TblObservaciones();
    	
    	@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			HttpClient httpClient= new DefaultHttpClient();
			String id=params[0];
			HttpGet BO= new HttpGet (
					"http://"+ip+"/ADP/Observaciones/ObservacionBuscar/"+id);
			BO.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BO);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONObject respJSON = new JSONObject(respStr);
				
				observa.setIdObservacion(respJSON.getLong("IdObservacion"));
				observa.setIdPaciente(respJSON.getLong("IdPaciente"));
				observa.setAnio(respJSON.getInt("Anio"));
				observa.setMes(respJSON.getInt("Mes"));
				observa.setDia(respJSON.getInt("Dia"));
				observa.setHora(respJSON.getInt("Hora"));
				observa.setMinutos(respJSON.getInt("Minutos"));
				observa.setObservacion(respJSON.getString("Observacion"));
				observa.setEliminado(respJSON.getBoolean("Eliminado"));
				
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
			if (result) {
				String campo_ide=String.valueOf(observa.getIdObservacion());
				Select observacion = Select.from(TblObservaciones.class)
						.where(Condition.prop("ID_OBSERVACION").eq(campo_ide));
		    	TblObservaciones editar_Ob=(TblObservaciones)observacion.first();
		    	
				if (editar_Ob!=null) {
					editar_Ob.setIdObservacion(observa.getIdObservacion());
					editar_Ob.setIdPaciente(observa.getIdPaciente());
					editar_Ob.setAnio(observa.getAnio());
					editar_Ob.setMes(observa.getMes());
					editar_Ob.setDia(observa.getDia());
					editar_Ob.setHora(observa.getHora());
					editar_Ob.setMinutos(observa.getMinutos());
					editar_Ob.setObservacion(observa.getObservacion());
					editar_Ob.setEliminado(observa.getEliminado());
					editar_Ob.save();
				}
				else{
					TblObservaciones newObser = new TblObservaciones(
							observa.getIdObservacion(), observa.getIdPaciente(),
							observa.getAnio(), observa.getMes(), observa.getDia(), 
							observa.getHora(), observa.getMinutos(),
							observa.getObservacion(), observa.getEliminado());
					newObser.save();
				}
			}
		}
    }
    
    //TAREA ASINCRONA PARA BUSCAR VARIAS OBSERVACIONES
    public class BuscarAllObservaciones extends AsyncTask<String, Integer, Boolean>{

    	private TblObservaciones observa= new TblObservaciones();
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			String idP=params[0];
			HttpClient httpClient= new DefaultHttpClient();
			HttpGet BT= new HttpGet (
					"http://"+ip+"/ADP/Observaciones/ObservacionesBuscarXPaciente/"+idP);
			BT.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BT);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONArray respJSON = new JSONArray(respStr);
				
				for (int i = 0; i <respJSON.length(); i++) {
					JSONObject obj=respJSON.getJSONObject(i);
								
					observa = new TblObservaciones();
					observa.setIdObservacion(obj.getLong("IdObservacion"));
					observa.setIdPaciente(obj.getLong("IdPaciente"));
					observa.setAnio(obj.getInt("Anio"));
					observa.setMes(obj.getInt("Mes"));
					observa.setDia(obj.getInt("Dia"));
					observa.setHora(obj.getInt("Hora"));
					observa.setMinutos(obj.getInt("Minutos"));
					observa.setObservacion(obj.getString("Observacion"));
					observa.setEliminado(obj.getBoolean("Eliminado"));
					
					TblObservaciones guardar_Ob = new TblObservaciones(
							observa.getIdObservacion(), observa.getIdPaciente(),
							observa.getAnio(), observa.getMes(), observa.getDia(), 
							observa.getHora(), observa.getMinutos(),
							observa.getObservacion(), observa.getEliminado());
					guardar_Ob.save();
					
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
    
    //TAREA ASINCRONA PARA ELIMINAR UNA OBSERVACION
    public class EliminarObservacion extends AsyncTask<String, Integer, Boolean>{

    	private String id="";
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			HttpClient httpClient= new DefaultHttpClient();
			id=params[0];
			HttpDelete del= new HttpDelete (
					"http://"+ip+"/ADP/Observaciones/ObservacionEliminar/"+id);
			del.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(del);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				if (!respStr.equals("true")) {
					resul=false;
				}else{
					TblObservaciones observacion = new TblObservaciones();
					observacion.EliminarPorIdObservacionRegTblObservaciones(Long.parseLong(id));
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
		}
    }
    
    //UTIIZADA PARA LA ACTUALIZACION DE LA BD
    //TAREA ASINCRONA PARA BUSCAR VARIAS OBSERVACIONES
    public class BuscarAllObservacionesCuidadores extends AsyncTask<String, Integer, Boolean>{

    	private TblObservaciones observa= new TblObservaciones();
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			String idC=params[0];
			HttpClient httpClient= new DefaultHttpClient();
			HttpGet BT= new HttpGet (
					"http://"+ip+"/ADP/Observaciones/ObservacionesBuscarXCuidadores/"+idC);
			BT.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BT);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONArray respJSON = new JSONArray(respStr);
				
				for (int i = 0; i <respJSON.length(); i++) {
					JSONObject obj=respJSON.getJSONObject(i);
							
					observa = new TblObservaciones();
					observa.setIdObservacion(obj.getLong("IdObservacion"));
					observa.setIdPaciente(obj.getLong("IdPaciente"));
					observa.setAnio(obj.getInt("Anio"));
					observa.setMes(obj.getInt("Mes"));
					observa.setDia(obj.getInt("Dia"));
					observa.setHora(obj.getInt("Hora"));
					observa.setMinutos(obj.getInt("Minutos"));
					observa.setObservacion(obj.getString("Observacion"));
					observa.setEliminado(obj.getBoolean("Eliminado"));
					
					TblObservaciones guardar_Ob = new TblObservaciones(
							observa.getIdObservacion(), observa.getIdPaciente(),
							observa.getAnio(), observa.getMes(), observa.getDia(), 
							observa.getHora(), observa.getMinutos(),
							observa.getObservacion(), observa.getEliminado());
					guardar_Ob.save();
					
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

	public JsonArrayRequest AB_Observaciones(long idC, final String TAG){
		String urlJsonArray = "http://"+ip+"/ADP/Observaciones/ObservacionBuscarXCuidadores/"+String.valueOf(idC);
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

								TblObservaciones observa = new TblObservaciones();
								observa.setIdObservacion(obj.getLong("IdObservacion"));
								observa.setIdPaciente(obj.getLong("IdPaciente"));
								observa.setAnio(obj.getInt("Anio"));
								observa.setMes(obj.getInt("Mes"));
								observa.setDia(obj.getInt("Dia"));
								observa.setHora(obj.getInt("Hora"));
								observa.setMinutos(obj.getInt("Minutos"));
								observa.setObservacion(obj.getString("Observacion"));
								observa.setEliminado(obj.getBoolean("Eliminado"));
								observa.save();

								Log.e("Observa => Registro", "#" + i + "= " + observa);
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
