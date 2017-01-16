package com.Notifications.patientssassistant.asynctask;

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
import com.Notifications.patientssassistant.tables.*;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.orm.query.Condition;
import com.orm.query.Select;


public class ATPermisos {
	
	//VARIABLES DE CLASE ATPermisos
	//private static String ip="192.168.1.4:1522";
	private static String ip=VarEstatic.ObtenerIP();
	public TblPermisos permiso;
	public long idCuidadorCreador;
	
	//TAREA ASINCRONA PARA INSERTAR UN PERMISO
	public class InsertarPermiso extends AsyncTask<String, Integer, Long>
    {
    	private TblPermisos permiso;
		private long id=0;
    	
		@Override
		protected Long doInBackground(String... params) {
			// TODO Auto-generated method stub
			Boolean resul=true;
			HttpClient httpClient=new DefaultHttpClient();
			
			HttpPost post=new HttpPost(
					"http://"+ip+"/ADP/Permisos/PermisoInsertar");
			post.setHeader("content-type", "application/json");
			
			try {
				JSONObject dato=new JSONObject();
							
				dato.put("IdPermiso", Long.parseLong(params[0]));
				dato.put("IdCuidador", Long.parseLong(params[1]));
				dato.put("IdPaciente", Long.parseLong(params[2]));
				dato.put("NotifiAlarma", Boolean.parseBoolean(params[3]));
				dato.put("RegCreador",Boolean.parseBoolean(params[4]));
				dato.put("ContMedicina", Boolean.parseBoolean(params[5]));
				dato.put("Eliminado",Boolean.parseBoolean(params[6]));
				
				StringEntity entity = new StringEntity(dato.toString());
				post.setEntity(entity);
				
				HttpResponse resp=httpClient.execute(post);
				String respStr = EntityUtils.toString(resp.getEntity());
				id=Long.parseLong(respStr);
				
				if (id>0){
					permiso = new TblPermisos(
								id, Long.parseLong(params[1]),
								Long.parseLong(params[2]), 
								Boolean.parseBoolean(params[3]),
								Boolean.parseBoolean(params[5]),
								Boolean.parseBoolean(params[4]),
								Boolean.parseBoolean(params[6]));
				}else{resul=false;}
				
			} catch (Exception ex) {
				Log.e("ServicioRest", "Error!", ex);
				resul = false;
			}
			return id;
		}
		
		@Override
		protected void onPostExecute(Long resul ) {
			// TODO Auto-generated method stub
			if (resul>0) {
				permiso.save();
			}
		}
		
    }  
    	
	//TAREA ASINCRONA PARA ACTUALIZAR LOS PERMISOS
    public class ActualizarPermiso extends AsyncTask<String, Integer, Boolean>
    {
    	private TblPermisos permiso;

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			Boolean resul=true;
			HttpClient httpClient=new DefaultHttpClient();
			
			HttpPut put=new HttpPut(
					"http://"+ip+"/ADP/Permisos/PermisoActualizar");
			put.setHeader("content-type", "application/json");
			
			try {
				JSONObject dato=new JSONObject();
				
				dato.put("IdPermiso", Long.parseLong(params[0]));
				dato.put("NotifiAlarma", Boolean.parseBoolean(params[1]));
				dato.put("ContMedicina", Boolean.parseBoolean(params[2]));
				
				StringEntity entity = new StringEntity(dato.toString());
				put.setEntity(entity);
				
				HttpResponse resp=httpClient.execute(put);
				String respStr = EntityUtils.toString(resp.getEntity());
				
				if (!respStr.equals("true")) {
					resul=false;
				}else{
					permiso= new TblPermisos();
					permiso.setIdPermiso(Long.parseLong(params[0]));
					permiso.setNotifiAlarma(Boolean.parseBoolean(params[1]));
					permiso.setContMedicina(Boolean.parseBoolean(params[2]));
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
				String campo_ide=String.valueOf(permiso.getIdPermiso());
				Select elPermiso = Select.from(TblPermisos.class)
						.where(Condition.prop("ID_PERMISO").eq(campo_ide));
		    	TblPermisos edit_Per=(TblPermisos)elPermiso.first();		
		    	
				if (edit_Per!=null) {
					edit_Per.setIdPermiso(permiso.getIdPermiso());
					edit_Per.setNotifiAlarma(permiso.getNotifiAlarma());
					edit_Per.setContMedicina(permiso.getContMedicina());
					edit_Per.save();
				}
			}
		}
    }
     
    //TAREA ASINCRONA PARA BUSCAR UN PERMISO
    public class BuscarUnPermiso extends AsyncTask<String, Integer, TblPermisos>{

		private TblPermisos permiso= new TblPermisos();
    	
    	@Override
		protected TblPermisos doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			HttpClient httpClient= new DefaultHttpClient();
			String id=params[0];
			HttpGet BO= new HttpGet (
					"http://"+ip+"/ADP/Permisos/PermisoBuscar/"+id);
			BO.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BO);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONObject respJSON = new JSONObject(respStr);
				
				permiso.setIdPermiso(respJSON.getLong("IdPermiso"));
				permiso.setIdCuidador(respJSON.getLong("IdCuidador"));
				permiso.setIdPaciente(respJSON.getLong("IdPaciente"));
				permiso.setNotifiAlarma(respJSON.getBoolean("NotifiAlarma"));
				permiso.setRegCreador(respJSON.getBoolean("RegCreador"));
				permiso.setContMedicina(respJSON.getBoolean("ContMedicina"));
				permiso.setEliminado(respJSON.getBoolean("Eliminado"));
				
			} catch (Exception ex) {
				// TODO: handle exception
				Log.e("Serviciorest","Error!",ex);
				resul=false;
			}
    		
    		return permiso;
		}
    	
    	@Override
		protected void onPostExecute(TblPermisos result) {
			// TODO Auto-generated method stub
			if (result!=null) {
				String campo_ide=String.valueOf(permiso.getIdPermiso());
				Select elPermiso = Select.from(TblPermisos.class)
						.where(Condition.prop("ID_PERMISO").eq(campo_ide));
		    	TblPermisos edit_Per=(TblPermisos)elPermiso.first();		
		    	
				if (edit_Per!=null) {
					edit_Per.setIdPermiso(permiso.getIdPermiso());
					edit_Per.setIdCuidador(permiso.getIdCuidador());
					edit_Per.setIdPaciente(permiso.getIdPaciente());
					edit_Per.setNotifiAlarma(permiso.getNotifiAlarma());
					edit_Per.setRegCreador(permiso.getRegCreador());
					edit_Per.setContMedicina(permiso.getContMedicina());
					edit_Per.setEliminado(permiso.getEliminado());
					edit_Per.save();
					
					permiso=new TblPermisos();
					permiso=edit_Per;
				}
				else{
					TblPermisos newPermiso = new TblPermisos(
							permiso.getIdPermiso(), permiso.getIdCuidador(),
							permiso.getIdPaciente(), permiso.getNotifiAlarma(),
							permiso.getContMedicina(), permiso.getRegCreador(),
							permiso.getEliminado());
					newPermiso.save();
					
					permiso=new TblPermisos();
					permiso=newPermiso;
				}
			}
		}
    }
    
    //TAREA ASINCRONA PARA BUSCAR PERMISOS
    public class BuscarAllPermisosXCuidador extends AsyncTask<String, Integer, Boolean>{

    	private TblPermisos permiso= new TblPermisos();
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			String id=(params[0]);
			HttpClient httpClient= new DefaultHttpClient();
			HttpGet BT= new HttpGet (
					"http://"+ip+"/ADP/Permisos/PermisosBuscarXCuidador/"+id);
			BT.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BT);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONArray respJSON = new JSONArray(respStr);
				
				for (int i = 0; i <respJSON.length(); i++) {
					JSONObject obj=respJSON.getJSONObject(i);
					
					permiso.setIdPermiso(obj.getLong("IdPermiso"));
					permiso.setIdCuidador(obj.getLong("IdCuidador"));
					permiso.setIdPaciente(obj.getLong("IdPaciente"));
					permiso.setNotifiAlarma(obj.getBoolean("NotifiAlarma"));
					permiso.setRegCreador(obj.getBoolean("RegCreador"));
					permiso.setContMedicina(obj.getBoolean("ContMedicina"));
					permiso.setEliminado(obj.getBoolean("Eliminado"));

					TblPermisos guardar_permiso = new TblPermisos(
							permiso.getIdPermiso(), permiso.getIdCuidador(),
							permiso.getIdPaciente(), permiso.getNotifiAlarma(),
							permiso.getContMedicina(), permiso.getRegCreador(),
							permiso.getEliminado());
					guardar_permiso.save();
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
    
  //TAREA ASINCRONA PARA BUSCAR PERMISOS
    public class BuscarCuidadorCreador extends AsyncTask<String, Integer, Long>{

    	//private TblPermisos permiso= new TblPermisos();
    	private Long cuidadorCreador;
    	
		@Override
		protected Long doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			String id=(params[0]);
			HttpClient httpClient= new DefaultHttpClient();
			HttpGet BT= new HttpGet (
					"http://"+ip+"/ADP/Permisos/PermisoBuscarCreador/"+id);
			BT.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BT);
				String respStr=EntityUtils.toString(resp.getEntity());
				long idP=Long.parseLong(respStr);
				
				if (idP>0) {
					//idCuidadorCreador=idP;
					cuidadorCreador=idP;
				}
				
			} catch (Exception ex) {
				// TODO: handle exception
				Log.e("Serviciorest","Error!",ex);
				resul=false;
			}
    		
    		return cuidadorCreador;
		}
    	
    	@Override
		protected void onPostExecute(Long result) {
			// TODO Auto-generated method stub
		}
    }
    
  //BUSCAR CUIDADOR DE PETICIONES
    public class BuscarCuidadorPeticiones extends AsyncTask<String, Integer, Long>{

    	//private TblPermisos permiso= new TblPermisos();
    	
		@Override
		protected Long doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			Long cuidadorPeticiones=0L;
			String id=(params[0]);
			HttpClient httpClient= new DefaultHttpClient();
			HttpGet BT= new HttpGet (
					"http://"+ip+"/ADP/Permisos/PermisoBuscarCuidadorPeticiones/"+id);
			BT.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BT);
				String respStr=EntityUtils.toString(resp.getEntity());
				long idP=Long.parseLong(respStr);
				
				if (idP>0) {
					//idCuidadorCreador=idP;
					cuidadorPeticiones=idP;
				}
				
			} catch (Exception ex) {
				// TODO: handle exception
				Log.e("Serviciorest","Error!",ex);
				resul=false;
			}
    		
    		return cuidadorPeticiones;
		}
    	
    	@Override
		protected void onPostExecute(Long result) {
			// TODO Auto-generated method stub
		}
    }
    
    
    //TAREA ASINCRONA PARA ELIMINAR UN PERMISO
    public class EliminarPermisos extends AsyncTask<String, Integer, Boolean>{
    	private String id="";
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			HttpClient httpClient= new DefaultHttpClient();
			id=params[0];
			HttpDelete del= new HttpDelete (
					"http://"+ip+"/ADP/Permisos/PermisoEliminar/"+id);
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
				TblPermisos permiso = new TblPermisos();
				permiso.EliminarPorIdPermisoRegTblPermisos(Long.parseLong(id));
			}
		}
    }
	
    //UTILIZADA PARA ACTUALIZAR LA BD
    //TAREA ASINCRONA PARA BUSCAR PERMISOS
    public class BuscarAllPermisosXCuidadores extends AsyncTask<String, Integer, Boolean>{

    	private TblPermisos permiso= new TblPermisos();
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			String id=(params[0]);
			HttpClient httpClient= new DefaultHttpClient();
			HttpGet BT= new HttpGet (
					"http://"+ip+"/ADP/Permisos/PermisosBuscarXCuidadores/"+id);
			BT.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BT);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONArray respJSON = new JSONArray(respStr);
				
				for (int i = 0; i <respJSON.length(); i++) {
					JSONObject obj=respJSON.getJSONObject(i);
					
					permiso = new TblPermisos();
					permiso.setIdPermiso(obj.getLong("IdPermiso"));
					permiso.setIdCuidador(obj.getLong("IdCuidador"));
					permiso.setIdPaciente(obj.getLong("IdPaciente"));
					permiso.setNotifiAlarma(obj.getBoolean("NotifiAlarma"));
					permiso.setRegCreador(obj.getBoolean("RegCreador"));
					permiso.setContMedicina(obj.getBoolean("ContMedicina"));
					permiso.setEliminado(obj.getBoolean("Eliminado"));

					TblPermisos guardar_permiso = new TblPermisos(
							permiso.getIdPermiso(), permiso.getIdCuidador(),
							permiso.getIdPaciente(), permiso.getNotifiAlarma(),
							permiso.getContMedicina(), permiso.getRegCreador(),
							permiso.getEliminado());
					guardar_permiso.save();
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

	public JsonArrayRequest AB_Permisos(long idC, final String TAG){
		String urlJsonArray = "http://"+ip+"/ADP/Permisos/PermisoBuscarXCuidadores/"+String.valueOf(idC);
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

								permiso = new TblPermisos();
								permiso.setIdPermiso(obj.getLong("IdPermiso"));
								permiso.setIdCuidador(obj.getLong("IdCuidador"));
								permiso.setIdPaciente(obj.getLong("IdPaciente"));
								permiso.setNotifiAlarma(obj.getBoolean("NotifiAlarma"));
								permiso.setRegCreador(obj.getBoolean("RegCreador"));
								permiso.setContMedicina(obj.getBoolean("ContMedicina"));
								permiso.setEliminado(obj.getBoolean("Eliminado"));
								permiso.save();

								Log.e("Permiso => Registro", "#" + i + "= " + permiso);
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
