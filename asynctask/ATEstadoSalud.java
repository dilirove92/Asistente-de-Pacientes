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
import com.Notifications.patientssassistant.tables.TblEstadoSalud;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.orm.query.Condition;
import com.orm.query.Select;


public class ATEstadoSalud {

	//VARIABLES DE CLASE ATAEstadoSalud
	//private static String ip="192.168.1.4:1522";
	private static String ip=VarEstatic.ObtenerIP();
	
	//TAREA ASINCRONA PARA INSERTAR UN NUEVO REGISTRO DE ESTADO DE SALUD
	public class InsertarEstadoSalud extends AsyncTask<String, Integer, Boolean>
    {
    	private TblEstadoSalud eSalud;
		private long id=0;
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			Boolean resul=true;
			HttpClient httpClient=new DefaultHttpClient();
			
			HttpPost post=new HttpPost(
					"http://"+ip+"/ADP/EstadoSalud/EstadoSaludInsertar");
			post.setHeader("content-type", "application/json");
			
			try {
				JSONObject dato=new JSONObject();
							
				dato.put("IdPaciente", Long.parseLong(params[0]));
				dato.put("TipoSangre", params[1]);
				dato.put("FacultadMental", params[2]);
				dato.put("Enfermedad", Boolean.parseBoolean(params[3]));
				dato.put("DescEnfermedad", params[4]);
				dato.put("Cirugias",Boolean.parseBoolean(params[5]));
				dato.put("DescCirugias", params[6]);
				dato.put("Medicamentos", Boolean.parseBoolean(params[7]));
				dato.put("DescMedicamentos", params[8]);
				dato.put("Discapacidad", Boolean.parseBoolean(params[9]));
				dato.put("TipoDiscapacidad", params[10]);
				dato.put("GradoDiscapacidad", params[11]);
				dato.put("Implementos", params[12]);
				dato.put("Eliminado",Boolean.parseBoolean(params[13]));
				
				StringEntity entity = new StringEntity(dato.toString());
				post.setEntity(entity);
				
				HttpResponse resp=httpClient.execute(post);
				String respStr = EntityUtils.toString(resp.getEntity());
				
				if (respStr.equals("true")){
					eSalud = new TblEstadoSalud(
							Long.parseLong(params[0]), params[1],
							params[2], Boolean.parseBoolean(params[3]),
							params[4], Boolean.parseBoolean(params[5]),
							params[6], Boolean.parseBoolean(params[7]),
							params[8], Boolean.parseBoolean(params[9]),
							params[10], params[11], params[12],
							Boolean.parseBoolean(params[13]));
					eSalud.save();
				}else{
					resul=false;}
				
			} catch (Exception ex) {
				Log.e("ServicioRest", "Error!", ex);
				resul = false;
			}
			return resul;
		}
		
		@Override
		protected void onPostExecute(Boolean resul ) {
		}
    }  
    
	//TAREA ASINCRONA PARA ACTUALIZAR UN REGISTRO DE ESTADO DE SALUD DE UN PACIENTE
    public class ActualizarEstadoSalud extends AsyncTask<String, Integer, Boolean>
    {
    	@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			Boolean resul=true;
			HttpClient httpClient=new DefaultHttpClient();
			
			HttpPut put=new HttpPut(
					"http://"+ip+"/ADP/EstadoSalud/EstadoSaludActualizar");
			put.setHeader("content-type", "application/json");
			
			try {
				JSONObject dato=new JSONObject();
				
				dato.put("IdPaciente", Long.parseLong(params[0]));
				dato.put("TipoSangre", params[1]);
				dato.put("FacultadMental", params[2]);
				dato.put("Enfermedad", Boolean.parseBoolean(params[3]));
				dato.put("DescEnfermedad", params[4]);
				dato.put("Cirugias",Boolean.parseBoolean(params[5]));
				dato.put("DescCirugias", params[6]);
				dato.put("Medicamentos", Boolean.parseBoolean(params[7]));
				dato.put("DescMedicamentos", params[8]);
				dato.put("Discapacidad", Boolean.parseBoolean(params[9]));
				dato.put("TipoDiscapacidad", params[10]);
				dato.put("GradoDiscapacidad", params[11]);
				dato.put("Implementos", params[12]);
				dato.put("Eliminado",Boolean.parseBoolean(params[13]));
				
				StringEntity entity = new StringEntity(dato.toString());
				put.setEntity(entity);
				
				HttpResponse resp=httpClient.execute(put);
				String respStr = EntityUtils.toString(resp.getEntity());
				
				if (!respStr.equals("true")) {
					resul=false;
				}else{
					String campo_ide=String.valueOf(Long.parseLong(params[0]));
					Select estadoSalud = Select.from(TblEstadoSalud.class)
							.where(Condition.prop("ID_PACIENTE").eq(campo_ide));
					TblEstadoSalud edit_ES=(TblEstadoSalud)estadoSalud.first();

					if (edit_ES!=null) {
						edit_ES.setIdPaciente(Long.parseLong(params[0]));
						edit_ES.setTipoSangre(params[1]);
						edit_ES.setFacultadMental(params[2]);
						edit_ES.setEnfermedad(Boolean.parseBoolean(params[3]));
						edit_ES.setDescEnfermedad(params[4]);
						edit_ES.setCirugias(Boolean.parseBoolean(params[5]));
						edit_ES.setDescCirugias(params[6]);
						edit_ES.setMedicamentos(Boolean.parseBoolean(params[7]));
						edit_ES.setDescMedicamentos(params[8]);
						edit_ES.setDiscapacidad(Boolean.parseBoolean(params[9]));
						edit_ES.setTipoDiscapacidad(params[10]);
						edit_ES.setGradoDiscapacidad(params[11]);
						edit_ES.setImplementos(params[12]);
						edit_ES.setEliminado(Boolean.parseBoolean(params[13]));
						edit_ES.save();
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
    
    //TAREA ASINCRONA PARA BUSCAR UN REGISTRO DE ESTADO DE SALUD
    public class BuscarUnEstadoSalud extends AsyncTask<String, Integer, Boolean>{

		private TblEstadoSalud eSalud= new TblEstadoSalud();
    	
    	@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			HttpClient httpClient= new DefaultHttpClient();
			String id=params[0];
			HttpGet BO= new HttpGet (
					"http://"+ip+"/ADP/EstadoSalud/EstadoSaludBuscar/"+id);
			BO.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BO);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONObject respJSON = new JSONObject(respStr);
				
				eSalud.setIdPaciente(respJSON.getLong("IdPaciente"));
				eSalud.setTipoSangre(respJSON.getString("TipoSangre"));
				eSalud.setFacultadMental(respJSON.getString("FacultadMental"));
				eSalud.setEnfermedad(respJSON.getBoolean("Enfermedad"));
				eSalud.setDescEnfermedad(respJSON.getString("DescEnfermedad"));
				eSalud.setCirugias(respJSON.getBoolean("Cirugias"));
				eSalud.setDescCirugias(respJSON.getString("DescCirugias"));
				eSalud.setMedicamentos(respJSON.getBoolean("Medicamentos"));
				eSalud.setDescMedicamentos(respJSON.getString("DescMedicamentos"));
				eSalud.setDiscapacidad(respJSON.getBoolean("Discapacidad"));
				eSalud.setTipoDiscapacidad(respJSON.getString("TipoDiscapacidad"));
				eSalud.setGradoDiscapacidad(respJSON.getString("GradoDiscapacidad"));
				eSalud.setImplementos(respJSON.getString("Implementos"));
				eSalud.setEliminado(respJSON.getBoolean("Eliminado"));
				
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
				String campo_ide=String.valueOf(eSalud.getIdPaciente());
				Select estadoSalud = Select.from(TblEstadoSalud.class)
						.where(Condition.prop("ID_PACIENTE").eq(campo_ide));
		    	TblEstadoSalud edit_ES=(TblEstadoSalud)estadoSalud.first();	
		    	
				if (edit_ES!=null) {
					edit_ES.setIdPaciente(eSalud.getIdPaciente());
					edit_ES.setTipoSangre(eSalud.getTipoSangre());
					edit_ES.setFacultadMental(eSalud.getFacultadMental());
					edit_ES.setEnfermedad(eSalud.getEnfermedad());
					edit_ES.setDescEnfermedad(eSalud.getDescEnfermedad());
					edit_ES.setCirugias(eSalud.getCirugias());
					edit_ES.setDescCirugias(eSalud.getDescCirugias());
					edit_ES.setMedicamentos(eSalud.getMedicamentos());
					edit_ES.setDescMedicamentos(eSalud.getDescMedicamentos());
					edit_ES.setDiscapacidad(eSalud.getDiscapacidad());
					edit_ES.setTipoDiscapacidad(eSalud.getTipoDiscapacidad());
					edit_ES.setGradoDiscapacidad(eSalud.getGradoDiscapacidad());
					edit_ES.setImplementos(eSalud.getImplementos());
					edit_ES.setEliminado(eSalud.getEliminado());
					edit_ES.save();
				}
				else{
					TblEstadoSalud newESalud = new TblEstadoSalud(
							eSalud.getIdPaciente(), eSalud.getTipoSangre(),
							eSalud.getFacultadMental(), eSalud.getEnfermedad(),
							eSalud.getDescEnfermedad(), eSalud.getCirugias(),
							eSalud.getDescCirugias(), eSalud.getMedicamentos(), 
							eSalud.getDescMedicamentos(), eSalud.getDiscapacidad(), 
							eSalud.getTipoDiscapacidad(), eSalud.getGradoDiscapacidad(), 
							eSalud.getImplementos(), eSalud.getEliminado()); 
					newESalud.save();
				}
			}
		}
    }
        
    //TAREA ASINCRONA PARA BUSCAR UN REGISTRO DE ESTADO DE SALUD DE UN PACIENTE
    public class BuscarAllEstadosSaludPaciente extends AsyncTask<String, Integer, Boolean>{

    	private TblEstadoSalud eSalud= new TblEstadoSalud();
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			String idP=params[0];
			HttpClient httpClient= new DefaultHttpClient();
			HttpGet BT= new HttpGet (
					"http://"+ip+"/ADP/EstadoSalud/EstadosSaludBuscarXPaciente/"+idP);
			BT.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BT);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONArray respJSON = new JSONArray(respStr);
				
				for (int i = 0; i <respJSON.length(); i++) {
					JSONObject obj=respJSON.getJSONObject(i);
					
					eSalud = new TblEstadoSalud();
					eSalud.setIdPaciente(obj.getLong("IdPaciente"));
					eSalud.setTipoSangre(obj.getString("TipoSangre"));
					eSalud.setFacultadMental(obj.getString("FacultadMental"));
					eSalud.setEnfermedad(obj.getBoolean("Enfermedad"));
					eSalud.setDescEnfermedad(obj.getString("DescEnfermedad"));
					eSalud.setCirugias(obj.getBoolean("Cirugias"));
					eSalud.setDescCirugias(obj.getString("DescCirugias"));
					eSalud.setMedicamentos(obj.getBoolean("Medicamentos"));
					eSalud.setDescMedicamentos(obj.getString("DescMedicamentos"));
					eSalud.setDiscapacidad(obj.getBoolean("Discapacidad"));
					eSalud.setTipoDiscapacidad(obj.getString("TipoDiscapacidad"));
					eSalud.setGradoDiscapacidad(obj.getString("GradoDiscapacidad"));
					eSalud.setImplementos(obj.getString("Implementos"));
					eSalud.setEliminado(obj.getBoolean("Eliminado"));
					
					TblEstadoSalud guardar_EstSal = new TblEstadoSalud(
							eSalud.getIdPaciente(), eSalud.getTipoSangre(),
							eSalud.getFacultadMental(), eSalud.getEnfermedad(),
							eSalud.getDescEnfermedad(), eSalud.getCirugias(),
							eSalud.getDescCirugias(), eSalud.getMedicamentos(),
							eSalud.getDescMedicamentos(), eSalud.getDiscapacidad(),
							eSalud.getTipoDiscapacidad(), eSalud.getGradoDiscapacidad(),
							eSalud.getImplementos(), eSalud.getEliminado());
					guardar_EstSal.save();
					
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
    
    //TAREA ASINCRONA PARA ELIMINAR UN REGISTRO DE ESTADO DE SALUD DE UN PACIENTE
    public class EliminarEstadoSalud extends AsyncTask<String, Integer, Boolean>{

    	private String id="";
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			HttpClient httpClient= new DefaultHttpClient();
			id=params[0];
			HttpDelete del= new HttpDelete (
					"http://"+ip+"/ADP/EstadoSalud/EstadoSaludEliminar/"+id);
			del.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(del);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				if (!respStr.equals("true")) {
					resul=false;
				}else{
					TblEstadoSalud eSalud = new TblEstadoSalud();
					eSalud.EliminarPorIdPacienteRegTblEstadoSalud(Long.parseLong(id));
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

    //UTILIZADA PARA LA ACTUALIZACION DE LA BD
    //TAREA ASINCRONA PARA BUSCAR UN REGISTRO DE ESTADO DE SALUD DE UN PACIENTE
    public class BuscarAllEstadosSaludCuidadores extends AsyncTask<String, Integer, Boolean>{

    	private TblEstadoSalud eSalud= new TblEstadoSalud();
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			String idC=params[0];
			HttpClient httpClient= new DefaultHttpClient();
			HttpGet BT= new HttpGet (
					"http://"+ip+"/ADP/EstadoSalud/EstadosSaludBuscarXCuidadores/"+idC);
			BT.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BT);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONArray respJSON = new JSONArray(respStr);
				
				for (int i = 0; i <respJSON.length(); i++) {
					JSONObject obj=respJSON.getJSONObject(i);
					
					eSalud = new TblEstadoSalud();
					eSalud.setIdPaciente(obj.getLong("IdPaciente"));
					eSalud.setTipoSangre(obj.getString("TipoSangre"));
					eSalud.setFacultadMental(obj.getString("FacultadMental"));
					eSalud.setEnfermedad(obj.getBoolean("Enfermedad"));
					eSalud.setDescEnfermedad(obj.getString("DescEnfermedad"));
					eSalud.setCirugias(obj.getBoolean("Cirugias"));
					eSalud.setDescCirugias(obj.getString("DescCirugias"));
					eSalud.setMedicamentos(obj.getBoolean("Medicamentos"));
					eSalud.setDescMedicamentos(obj.getString("DescMedicamentos"));
					eSalud.setDiscapacidad(obj.getBoolean("Discapacidad"));
					eSalud.setTipoDiscapacidad(obj.getString("TipoDiscapacidad"));
					eSalud.setGradoDiscapacidad(obj.getString("GradoDiscapacidad"));
					eSalud.setImplementos(obj.getString("Implementos"));
					eSalud.setEliminado(obj.getBoolean("Eliminado"));
					
					TblEstadoSalud guardar_EstSal = new TblEstadoSalud(
							eSalud.getIdPaciente(), eSalud.getTipoSangre(),
							eSalud.getFacultadMental(), eSalud.getEnfermedad(),
							eSalud.getDescEnfermedad(), eSalud.getCirugias(),
							eSalud.getDescCirugias(), eSalud.getMedicamentos(),
							eSalud.getDescMedicamentos(), eSalud.getDiscapacidad(),
							eSalud.getTipoDiscapacidad(), eSalud.getGradoDiscapacidad(),
							eSalud.getImplementos(), eSalud.getEliminado());
					guardar_EstSal.save();
					
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

	public JsonArrayRequest AB_EstadoSalud(long idC, final String TAG){
		String urlJsonArray = "http://"+ip+"/ADP/EstadoSalud/EstadoSaludBuscarXCuidadores/"+String.valueOf(idC);
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

								TblEstadoSalud eSalud = new TblEstadoSalud();
								eSalud.setIdPaciente(obj.getLong("IdPaciente"));
								eSalud.setTipoSangre(obj.getString("TipoSangre"));
								eSalud.setFacultadMental(obj.getString("FacultadMental"));
								eSalud.setEnfermedad(obj.getBoolean("Enfermedad"));
								eSalud.setDescEnfermedad(obj.getString("DescEnfermedad"));
								eSalud.setCirugias(obj.getBoolean("Cirugias"));
								eSalud.setDescCirugias(obj.getString("DescCirugias"));
								eSalud.setMedicamentos(obj.getBoolean("Medicamentos"));
								eSalud.setDescMedicamentos(obj.getString("DescMedicamentos"));
								eSalud.setDiscapacidad(obj.getBoolean("Discapacidad"));
								eSalud.setTipoDiscapacidad(obj.getString("TipoDiscapacidad"));
								eSalud.setGradoDiscapacidad(obj.getString("GradoDiscapacidad"));
								eSalud.setImplementos(obj.getString("Implementos"));
								eSalud.setEliminado(obj.getBoolean("Eliminado"));
								eSalud.save();

								Log.e("Est_Sal => Registro", "#" + i + " = " + eSalud);
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