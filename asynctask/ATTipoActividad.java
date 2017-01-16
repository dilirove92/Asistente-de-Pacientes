package com.Notifications.patientssassistant.asynctask;

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

import android.os.AsyncTask;
import android.util.Log;

import com.Notifications.patientssassistant.VarEstatic;
import com.Notifications.patientssassistant.tables.TblTipoActividad;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.orm.query.Condition;
import com.orm.query.Select;

public class ATTipoActividad {

	//VARIABLES DE CLASE ATCuidador 
	//private static String ip="192.168.1.4:1522";
	private static String ip=VarEstatic.ObtenerIP();
	
	//TAREA ASINCRONA PARA INSERTAR UN TIPO DE ACTIVIDAD
	public class InsertarTipoActividad extends AsyncTask<String, Integer, Long>
    {
		private TblTipoActividad tActividad;
    	private long id=0;
    	
		@Override
		protected Long doInBackground(String... params) {
			// TODO Auto-generated method stub
			Boolean resul=true;
			long respuesta=0;
			HttpClient httpClient=new DefaultHttpClient();
			
			HttpPost post=new HttpPost(
					"http://"+ip+"/ADP/TipoActividad/TipoActividadInsertar");
			post.setHeader("content-type", "application/json");
			
			try {
				JSONObject dato=new JSONObject();
							
				dato.put("IdTipoActividad", Long.parseLong(params[0]));
				dato.put("TipoDeActividad", params[1]);
				dato.put("DescripcionTipoAct", params[2]);
				dato.put("Eliminado",Boolean.parseBoolean(params[3]));
				
				StringEntity entity = new StringEntity(dato.toString());
				post.setEntity(entity);
				
				HttpResponse resp=httpClient.execute(post);
				String respStr = EntityUtils.toString(resp.getEntity());
			    respuesta=Long.parseLong(respStr);
				
				if (respuesta>0){
					tActividad = new TblTipoActividad(
									respuesta, params[1], params[2],
									Boolean.parseBoolean(params[3]));
				}else{resul=false;}
				
			} catch (Exception ex) {
				Log.e("ServicioRest", "Error!", ex);
				resul = false;
			}
			return respuesta;
		}
		
		@Override
		protected void onPostExecute(Long resul ) {
			// TODO Auto-generated method stub
			if (resul>0) {
				tActividad.save();
			}
		}
		
    }  
    
	//TAREA ASINCRONA PARA ACTUALIZAR UN TIPO DE ACTIVIDAD
    public class ActualizarTipoActividad extends AsyncTask<String, Integer, Boolean>
    {
    	private TblTipoActividad tActividad;
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			Boolean resul=true;
			HttpClient httpClient=new DefaultHttpClient();
			
			HttpPut put=new HttpPut(
					"http://"+ip+"/ADP/TipoActividad/TipoActividadActualizar");
			put.setHeader("content-type", "application/json");
			
			try {
				//Construimos el objeto alumno en formato JSON
				JSONObject dato=new JSONObject();
				
				dato.put("IdTipoActividad", Long.parseLong(params[0]));
				dato.put("TipoDeActividad", params[1]);
				dato.put("DescripcionTipoAct", params[2]);
				dato.put("Eliminado",Boolean.parseBoolean(params[3]));
				
				StringEntity entity = new StringEntity(dato.toString());
				put.setEntity(entity);
				
				HttpResponse resp=httpClient.execute(put);
				String respStr = EntityUtils.toString(resp.getEntity());
				
				if (!respStr.equals("true")) {
					resul=false;
				}else{
					tActividad = new TblTipoActividad();
					tActividad.setIdTipoActividad(Long.parseLong(params[0]));
					tActividad.setTipoActividad(params[1]);
					tActividad.setDescripcionTipoAct(params[2]);
					tActividad.setEliminado(Boolean.parseBoolean(params[3]));
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
			if (result) {
				String campo_ide=String.valueOf(tActividad.getIdTipoActividad());
				Select eltipo = Select.from(TblTipoActividad.class)
						.where(Condition.prop("ID_TIPO_ACTIVIDAD").eq(campo_ide));
		    	TblTipoActividad edit_tActividad=(TblTipoActividad)eltipo.first();
		    	
		    	if (edit_tActividad!=null) {
					edit_tActividad.setIdTipoActividad(tActividad.getIdTipoActividad());
					edit_tActividad.setTipoActividad(tActividad.getTipoActividad());
					edit_tActividad.setDescripcionTipoAct(tActividad.getDescripcionTipoAct());
					edit_tActividad.setEliminado(tActividad.getEliminado());
					edit_tActividad.save();
				}
			}
		}
		
    }
     
    //TAREA ASINCRONA PARA BUSCAR UN TIPO DE ACTIVIDAD
    public class BuscarUnTipoActividad extends AsyncTask<String, Integer, Boolean>{

		private TblTipoActividad tActividad= new TblTipoActividad();
    	
    	@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			HttpClient httpClient= new DefaultHttpClient();
			String id=params[0];
			HttpGet BO= new HttpGet (
					"http://"+ip+"/ADP/TipoActividad/TipoActividadBuscar/"+id);
			BO.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BO);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONObject respJSON = new JSONObject(respStr);
				
				tActividad.setIdTipoActividad(respJSON.getLong("IdTipoActividad"));
				tActividad.setTipoActividad(respJSON.getString("TipoDeActividad"));
				tActividad.setDescripcionTipoAct(respJSON.getString("DescripcionTipoAct"));
				tActividad.setEliminado(respJSON.getBoolean("Eliminado"));
				
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
				String campo_ide=String.valueOf(tActividad.getIdTipoActividad());
				Select eltipo = Select.from(TblTipoActividad.class)
						.where(Condition.prop("ID_TIPO_ACTIVIDAD").eq(campo_ide));
		    	TblTipoActividad edit_tActividad=(TblTipoActividad)eltipo.first();
		    	
		    	if (edit_tActividad!=null) {
					edit_tActividad.setIdTipoActividad(tActividad.getIdTipoActividad());
					edit_tActividad.setTipoActividad(tActividad.getTipoActividad());
					edit_tActividad.setDescripcionTipoAct(tActividad.getDescripcionTipoAct());
					edit_tActividad.setEliminado(tActividad.getEliminado());
					edit_tActividad.save();
				}
		    	else{
		    		TblTipoActividad tipoAct = new TblTipoActividad(
		    				tActividad.getIdTipoActividad(), tActividad.getTipoActividad(),
		    				tActividad.getDescripcionTipoAct(), tActividad.getEliminado());
		    		tipoAct.save();
		    	}
			}
		}
    }
       
    //TAREA ASINCRONA PARA BUSCAR TIPOS DE ACTIVIDADES
    public class BuscarAllTipoActividad extends AsyncTask<String, Integer, Boolean>{

    	private TblTipoActividad tActividad= new TblTipoActividad();
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			HttpClient httpClient= new DefaultHttpClient();
			HttpGet BT= new HttpGet (
					"http://"+ip+"/ADP/TipoActividad/TipoActividadBuscarAll");
			BT.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BT);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONArray respJSON = new JSONArray(respStr);
				
				for (int i = 0; i <respJSON.length(); i++) {
					JSONObject obj=respJSON.getJSONObject(i);
					
					tActividad= new TblTipoActividad();
					tActividad.setIdTipoActividad(obj.getLong("IdTipoActividad"));
					tActividad.setTipoActividad(obj.getString("TipoDeActividad"));
					tActividad.setDescripcionTipoAct(obj.getString("DescripcionTipoAct"));
					tActividad.setEliminado(obj.getBoolean("Eliminado"));
					
					
					TblTipoActividad guardar_tActividad = new TblTipoActividad(
							tActividad.getIdTipoActividad(), tActividad.getTipoActividad(),
							tActividad.getDescripcionTipoAct(), tActividad.getEliminado());
					guardar_tActividad.save();
					
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
      
    //TAREA ASINCRONA PARA ELIMINAR UN TIPO DE ACTIVIDAD
    public class EliminarTipoActividad extends AsyncTask<String, Integer, Boolean>{

    	private String id="";
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			HttpClient httpClient= new DefaultHttpClient();
			id=params[0];
			HttpDelete del= new HttpDelete (
					"http://"+ip+"/ADP/TipoActividad/TipoActividadEliminar/"+id);
			del.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(del);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				if (!respStr.equals("true")) {
					resul=false;
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
			if (result) {
				TblTipoActividad tipoAct = new TblTipoActividad();
				tipoAct.EliminarPorIdTipoActividadRegTblTipoActividad(Long.parseLong(id));
			}
		}
    }

	public JsonArrayRequest AB_TipoActividad(final String TAG){
		String urlJsonArray = "http://"+ip+"/ADP/TipoActividad/TipoActividadBuscarAll";
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

								TblTipoActividad tActividad= new TblTipoActividad();
								tActividad.setIdTipoActividad(obj.getLong("IdTipoActividad"));
								tActividad.setTipoActividad(obj.getString("TipoDeActividad"));
								tActividad.setDescripcionTipoAct(obj.getString("DescripcionTipoAct"));
								tActividad.setEliminado(obj.getBoolean("Eliminado"));
								tActividad.save();

								Log.e("Tipo_Act => Registro", "#" + i + "= " + tActividad);
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
