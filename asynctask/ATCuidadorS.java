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
import com.Notifications.patientssassistant.tables.TblCuidadorS;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.orm.query.Condition;
import com.orm.query.Select;

public class ATCuidadorS {
	
	//VARIABLES DE CLASE ATCuidadorS
	//private static String ip="192.168.1.4:1522";
	private static String ip=VarEstatic.ObtenerIP();
	public TblCuidadorS cuidadorS;
	
	//TAREA ASINCRONA PARA INSERTAR UN NUEVO CUIDADOR SECUNDARIO
	public class InsertarCuidadorS extends AsyncTask<String, Integer, Boolean>
    {
    	private TblCuidadorS cuidador;
		private long id=0;
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			Boolean resul=true;
			HttpClient httpClient=new DefaultHttpClient();
			
			HttpPost post=new HttpPost(
					"http://"+ip+"/ADP/CuidadorS/CuidadorSInsertar");
			post.setHeader("content-type", "application/json");
			
			try {
				JSONObject dato=new JSONObject();
							
				dato.put("IdCuidador", Long.parseLong(params[0]));
				dato.put("DependeDe", Long.parseLong(params[1]));
				dato.put("Eliminado",Boolean.parseBoolean(params[2]));
				
				StringEntity entity = new StringEntity(dato.toString());
				post.setEntity(entity);
				
				HttpResponse resp=httpClient.execute(post);
				String respStr = EntityUtils.toString(resp.getEntity());
				
				if (respStr.equals("true")){
					cuidador = new TblCuidadorS(
							Long.parseLong(params[0]),
							Long.parseLong(params[1]),
							Boolean.parseBoolean(params[2]));
				}else{resul=false;}
				
			} catch (Exception ex) {
				Log.e("ServicioRest", "Error!", ex);
				resul = false;
			}
			return resul;
		}
		
		@Override
		protected void onPostExecute(Boolean resul ) {
			// TODO Auto-generated method stub
			if (resul) {
				cuidador.save();
			}
		}
		
    }  
	
	//TAREA ASINCRONA PARA ACTUALIZAR UN NUEVO CUIDADOR SECUNDARIO
    public class ActualizarCuidadorS extends AsyncTask<String, Integer, Boolean>
    {
    	private TblCuidadorS cuidador;
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			Boolean resul=true;
			HttpClient httpClient=new DefaultHttpClient();
			
			HttpPut put=new HttpPut(
					"http://"+ip+"/ADP/CuidadorS/CuidadorSActualizar");
			put.setHeader("content-type", "application/json");
			
			try {
				JSONObject dato=new JSONObject();
				
				dato.put("IdCuidador", Long.parseLong(params[0]));
				dato.put("DependeDe", Long.parseLong(params[1]));
				dato.put("Eliminado",Boolean.parseBoolean(params[2]));
				
				StringEntity entity = new StringEntity(dato.toString());
				put.setEntity(entity);
				
				HttpResponse resp=httpClient.execute(put);
				String respStr = EntityUtils.toString(resp.getEntity());
				
				if (!respStr.equals("true")) {
					resul=false;
				}else{
					cuidador = new TblCuidadorS();
					cuidador.setIdCuidador(Long.parseLong(params[0]));
					cuidador.setDependeDe(Long.parseLong(params[1]));
					cuidador.setEliminado(Boolean.parseBoolean(params[2]));
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
				String campo_ide=String.valueOf(cuidador.getIdCuidador());
				Select elCuidador = Select.from(TblCuidadorS.class)
						.where(Condition.prop("ID_CUIDADOR").eq(campo_ide));
				
		    	TblCuidadorS edit_Cuidador=(TblCuidadorS)elCuidador.first();
		    	
				if (edit_Cuidador!=null) {
					edit_Cuidador.setIdCuidador(cuidador.getIdCuidador());
					edit_Cuidador.setDependeDe(cuidador.getDependeDe());
					edit_Cuidador.setEliminado(cuidador.getEliminado());
					edit_Cuidador.save();
				}
			}
		}
    }
    
    //TAREA ASINCRONA PARA BUSCAR UN CUIDADOR SECUNDARIO
    public class BuscarUnCuidadorS extends AsyncTask<String, Integer, TblCuidadorS>{

		private TblCuidadorS cuidador= new TblCuidadorS();
    	
    	@Override
		protected TblCuidadorS doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			HttpClient httpClient= new DefaultHttpClient();
			String id=params[0];
			HttpGet BO= new HttpGet (
					"http://"+ip+"/ADP/CuidadorS/CuidadorSBuscar/"+id);
			BO.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BO);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONObject respJSON = new JSONObject(respStr);
				
				cuidador.setIdCuidador(respJSON.getLong("IdCuidador"));
				cuidador.setDependeDe(respJSON.getLong("DependeDe"));
				cuidador.setEliminado(respJSON.getBoolean("Eliminado"));
				
			} catch (Exception ex) {
				// TODO: handle exception
				Log.e("Serviciorest","Error!",ex);
				resul=false;
			}
    		
    		return cuidador;
		}
    	
    	@Override
		protected void onPostExecute(TblCuidadorS result) {
			// TODO Auto-generated method stub
			if (result!=null) {
				String campo_ide=String.valueOf(cuidador.getIdCuidador());
				Select elCuidador = Select.from(TblCuidadorS.class)
						.where(Condition.prop("ID_CUIDADOR").eq(campo_ide));
				
		    	TblCuidadorS edit_Cuidador=(TblCuidadorS)elCuidador.first();
		    	
				if (edit_Cuidador!=null) {
					edit_Cuidador.setIdCuidador(cuidador.getIdCuidador());
					edit_Cuidador.setDependeDe(cuidador.getDependeDe());
					edit_Cuidador.setEliminado(cuidador.getEliminado());
					edit_Cuidador.save();
				}
				else{
					TblCuidadorS newCuidadorS = new TblCuidadorS(
							cuidador.getIdCuidador(), cuidador.getDependeDe(), 
							cuidador.getEliminado());
					newCuidadorS.save();
				}
			}
		}
    }
       
    //TAREA ASINCRONA PARA BUSCAR TODOS LOS CUIDADORES SECUNDARIOS
    public class BuscarAllCuidadoresS extends AsyncTask<String, Integer, Boolean>{

    	private TblCuidadorS cuidador= new TblCuidadorS();
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			String idC=params[0];
			HttpClient httpClient= new DefaultHttpClient();
			HttpGet BT= new HttpGet (
					"http://"+ip+"/ADP/CuidadorS/CuidadoresSBuscarAllXCuidador/"+idC);
			BT.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BT);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONArray respJSON = new JSONArray(respStr);
				
				for (int i = 0; i <respJSON.length(); i++) {
					JSONObject obj=respJSON.getJSONObject(i);
					
					cuidador = new TblCuidadorS();
					cuidador.setIdCuidador(obj.getLong("IdCuidador"));
					cuidador.setDependeDe(obj.getLong("DependeDe"));
					cuidador.setEliminado(obj.getBoolean("Eliminado"));
							
					TblCuidadorS guardar_Cuidador = new TblCuidadorS(
							cuidador.getIdCuidador(), cuidador.getDependeDe(),
							cuidador.getEliminado());
					guardar_Cuidador.save();
					
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
     
    //TAREA ASINCRONA PARA ELIMINAR UN CUIDADOR SECUNDARIO
    public class EliminarCuidadorS extends AsyncTask<String, Integer, Boolean>{

    	private String id="";
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			HttpClient httpClient= new DefaultHttpClient();
			id=params[0];
			HttpDelete del= new HttpDelete (
					"http://"+ip+"/ADP/CuidadorS/CuidadorSEliminar/"+id);
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
				TblCuidadorS cuidadorS = new TblCuidadorS();
				cuidadorS.EliminarPorIdCuidadorRegTblCuidadorS(Long.parseLong(id));
			}
		}
    }

	public JsonArrayRequest AB_CuidadorS(long idC, final String TAG){
		String urlJsonArray = "http://"+ip+"/ADP/CuidadorS/CuidadorSBuscarAllXCuidador/"+String.valueOf(idC);
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

								TblCuidadorS cuidador = new TblCuidadorS();
								cuidador.setIdCuidador(obj.getLong("IdCuidador"));
								cuidador.setDependeDe(obj.getLong("DependeDe"));
								cuidador.setEliminado(obj.getBoolean("Eliminado"));
								cuidador.save();

								Log.e("CuidadorS => Registro", "#" + i + "= " + cuidador);
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
