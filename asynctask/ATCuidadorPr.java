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
import com.Notifications.patientssassistant.tables.TblCuidadorPr;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.orm.query.Condition;
import com.orm.query.Select;

public class ATCuidadorPr {
	
	//VARIABLES DE CLASE ATCuidadorPr
	//private static String ip="192.168.1.4:1522";
	private static String ip=VarEstatic.ObtenerIP();
	public TblCuidadorPr cuidadorPr;
	
	//TAREA ASINCRONA PARA INSERTAR UN NUEVO CUIDADOR PRIMARIO
	public class InsertarCuidadorPr extends AsyncTask<String, Integer, Boolean>
    {
    	private TblCuidadorPr cuidador;
		private long id=0;
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			Boolean resul=true;
			HttpClient httpClient=new DefaultHttpClient();
			
			HttpPost post=new HttpPost(
					"http://"+ip+"/ADP/CuidadorPr/CuidadorPrInsertar");
			post.setHeader("content-type", "application/json");
			
			try {
				JSONObject dato=new JSONObject();
							
				dato.put("IdCuidador", Long.parseLong(params[0]));
				dato.put("Contrasena", params[1]);
				dato.put("TipoResidencia", params[2]);
				dato.put("PassVincular", params[3]);
				dato.put("Eliminado",Boolean.parseBoolean(params[4]));
				
				StringEntity entity = new StringEntity(dato.toString());
				post.setEntity(entity);
				
				HttpResponse resp=httpClient.execute(post);
				String respStr = EntityUtils.toString(resp.getEntity());
				
				if (respStr.equals("true")){
					resul=true;
					cuidador = new TblCuidadorPr(
							Long.parseLong(params[0]), 
							params[1], params[2], params[3],
							Boolean.parseBoolean(params[4]));
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
	
	//TAREA ASINCRONA PARA ACTUALIZAR UN REGISTRO DE CUIDADOR PRIMARIO
    public class ActualizarCuidadorPr extends AsyncTask<String, Integer, Boolean>
    {
    	private TblCuidadorPr cuidador;
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			Boolean resul=true;
			HttpClient httpClient=new DefaultHttpClient();
			
			HttpPut put=new HttpPut(
					"http://"+ip+"/ADP/CuidadorPr/CuidadorPrActualizar");
			put.setHeader("content-type", "application/json");
			
			try {
				JSONObject dato=new JSONObject();
				
				dato.put("IdCuidador", Long.parseLong(params[0]));
				dato.put("Contrasena", params[1]);
				dato.put("TipoResidencia", params[2]);
				dato.put("PassVincular", params[3]);
				dato.put("Eliminado",Boolean.parseBoolean(params[4]));
				
				StringEntity entity = new StringEntity(dato.toString());
				put.setEntity(entity);
				
				HttpResponse resp=httpClient.execute(put);
				String respStr = EntityUtils.toString(resp.getEntity());
				
				if (!respStr.equals("true")) {
					resul=false;
				}else{
					cuidador=new TblCuidadorPr();
					cuidador.setIdCuidador(Long.parseLong(params[0]));
					cuidador.setContrasena(params[1]);
					cuidador.setTipoResidencia(params[2]);
					cuidador.setPassVinculacion(params[3]);
					cuidador.setEliminado(Boolean.parseBoolean(params[4]));
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
				Select elCuidadorPr = Select.from(TblCuidadorPr.class)
						.where(Condition.prop("ID_CUIDADOR").eq(campo_ide));
		    	TblCuidadorPr edit_Cuidador=(TblCuidadorPr)elCuidadorPr.first();		
				
		    	if (edit_Cuidador!=null) {
					edit_Cuidador.setIdCuidador(cuidador.getIdCuidador());
					edit_Cuidador.setContrasena(cuidador.getContrasena());
					edit_Cuidador.setTipoResidencia(cuidador.getTipoResidencia());
					edit_Cuidador.setPassVinculacion(cuidador.getPassVinculacion());
					edit_Cuidador.setEliminado(cuidador.getEliminado());
					edit_Cuidador.save();
				}
			}
		}
		
    }
    
    //TAREA ASINCRONO PARA BUSCAR UN CUIDADOR PRIMARIO
    public class BuscarUnCuidadorPr extends AsyncTask<String, Integer, TblCuidadorPr>{

		private TblCuidadorPr cuidador= new TblCuidadorPr();
    	
    	@Override
		protected TblCuidadorPr doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			HttpClient httpClient= new DefaultHttpClient();
			String id=params[0];
			HttpGet BO= new HttpGet (
					"http://"+ip+"/ADP/CuidadorPr/CuidadorPrBuscar/"+id);
			BO.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BO);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONObject respJSON = new JSONObject(respStr);
				
				cuidador.setIdCuidador(respJSON.getLong("IdCuidador"));
				cuidador.setContrasena(respJSON.getString("Contrasena"));
				cuidador.setTipoResidencia(respJSON.getString("TipoResidencia"));
				cuidador.setPassVinculacion(respJSON.getString("PassVincular"));
				cuidador.setEliminado(respJSON.getBoolean("Eliminado"));
				
			} catch (Exception ex) {
				// TODO: handle exception
				Log.e("Serviciorest","Error!",ex);
				resul=false;
			}
    		
    		return cuidador;
		}
    	
    	@Override
		protected void onPostExecute(TblCuidadorPr result) {
			// TODO Auto-generated method stub
			if (result!=null) {
				String campo_ide=String.valueOf(cuidador.getIdCuidador());
				Select elCuidadorPr = Select.from(TblCuidadorPr.class)
						.where(Condition.prop("ID_CUIDADOR").eq(campo_ide));
		    	TblCuidadorPr edit_Cuidador=(TblCuidadorPr)elCuidadorPr.first();		
				
		    	if (edit_Cuidador!=null) {
					edit_Cuidador.setIdCuidador(cuidador.getIdCuidador());
					edit_Cuidador.setContrasena(cuidador.getContrasena());
					edit_Cuidador.setTipoResidencia(cuidador.getTipoResidencia());
					edit_Cuidador.setPassVinculacion(cuidador.getPassVinculacion());
					edit_Cuidador.setEliminado(cuidador.getEliminado());
					edit_Cuidador.save();
					
					cuidadorPr= new TblCuidadorPr();
					cuidadorPr=edit_Cuidador;
				}
		    	else{
		    		TblCuidadorPr newCuidadorPr = new TblCuidadorPr(
		    				cuidador.getIdCuidador(), cuidador.getContrasena(),
		    				cuidador.getTipoResidencia(), cuidador.getPassVinculacion(),
		    				cuidador.getEliminado());
		    		newCuidadorPr.save();
		    		
		    		cuidadorPr= new TblCuidadorPr();
					cuidadorPr=newCuidadorPr;
		    	}
			}
		}
    }
       
    //TAREA ASINCRONA PARA BUSCAR TODOS LOS CUIDAORES PRIMARIOS
    public class BuscarAllCuidadoresPr extends AsyncTask<String, Integer, Boolean>{

    	private TblCuidadorPr cuidador= new TblCuidadorPr();
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			HttpClient httpClient= new DefaultHttpClient();
			HttpGet BT= new HttpGet (
					"http://"+ip+"/ADP/CuidadorPr/CuidadorPrBuscarAll");
			BT.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BT);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONArray respJSON = new JSONArray(respStr);
				
				for (int i = 0; i <respJSON.length(); i++) {
					JSONObject obj=respJSON.getJSONObject(i);
					
					cuidador = new  TblCuidadorPr();
					cuidador.setIdCuidador(obj.getLong("IdCuidador"));
					cuidador.setContrasena(obj.getString("Contrasena"));
					cuidador.setTipoResidencia(obj.getString("TipoResidencia"));
					cuidador.setPassVinculacion(obj.getString("PassVincular"));
					cuidador.setEliminado(obj.getBoolean("Eliminado"));
					
					
					TblCuidadorPr guardar_Cuidador = new TblCuidadorPr(
							cuidador.getIdCuidador(), cuidador.getContrasena(),
							cuidador.getTipoResidencia(), cuidador.getPassVinculacion(),
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
      
    //TAREA ASINCRONA PARA ELIMINAR UN CUIDADOR PRIMARIO
    public class EliminarCuidadorPr extends AsyncTask<String, Integer, Boolean>{

    	private String id="";
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			HttpClient httpClient= new DefaultHttpClient();
			id=params[0];
			HttpDelete del= new HttpDelete (
					"http://"+ip+"/ADP/CuidadorPr/CuidadorPrEliminar/"+id);
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
				TblCuidadorPr cuidadorPr = new TblCuidadorPr();
				cuidadorPr.EliminarPorIdCuidadorRegTblCuidadorPr(Long.parseLong(id));
			}
		}
    }

	public JsonObjectRequest AB_CuidadorPr(long idC, final String TAG){
		String urlJsonObject = "http://"+ip+"/ADP/CuidadorPr/CuidadorPrBuscar/"+String.valueOf(idC);
		JsonObjectRequest request = new JsonObjectRequest(urlJsonObject , null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject respJSON) {
				Log.d(TAG, respJSON.toString());

				try {
					TblCuidadorPr cuidador= new TblCuidadorPr();
					cuidador.setIdCuidador(respJSON.getLong("IdCuidador"));
					cuidador.setContrasena(respJSON.getString("Contrasena"));
					cuidador.setTipoResidencia(respJSON.getString("TipoResidencia"));
					cuidador.setPassVinculacion(respJSON.getString("PassVincular"));
					cuidador.setEliminado(respJSON.getBoolean("Eliminado"));
					cuidador.save();

					Log.e("CuidadorPr => Registro", "# 1 = " + cuidador);
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
		return request;
	}
}
