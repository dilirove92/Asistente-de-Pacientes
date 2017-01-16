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
import com.Notifications.patientssassistant.tables.TblRutinasPacientes;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.orm.query.Condition;
import com.orm.query.Select;


public class ATRutinasPacientes {

	//VARIABLES DE CLASE ATRutinasPacientes	 
	//private static String ip="192.168.1.4:1522";
	private static String ip=VarEstatic.ObtenerIP();

	
	//TAREAS ASINCRONAS PARA INSERTAR UNA RUTINA DE PACIENTE
	public class InsertarRutinasPacientes extends AsyncTask<String, Integer, Long>
    {
		private TblRutinasPacientes rutina;
    	private long id=0;
    	
		@Override
		protected Long doInBackground(String... params) {
			// TODO Auto-generated method stub
			Boolean resul=true;
			long respuesta=0;
			HttpClient httpClient=new DefaultHttpClient();
			
			HttpPost post=new HttpPost(
					"http://"+ip+"/ADP/RutinasPacientes/RutinaPacienteInsertar");
			post.setHeader("content-type", "application/json");
			
			try {
				JSONObject dato=new JSONObject();
							
				dato.put("IdRutinaP", Long.parseLong(params[0]));
				dato.put("IdPaciente", Long.parseLong(params[1]));
				dato.put("IdActividad", Long.parseLong(params[2]));
				dato.put("Hora", Integer.parseInt(params[3]));
				dato.put("Minutos", Integer.parseInt(params[4]));
				dato.put("Domingo", Boolean.parseBoolean(params[5]));
				dato.put("Lunes",Boolean.parseBoolean(params[6]));
				dato.put("Martes", Boolean.parseBoolean(params[7]));
				dato.put("Miercoles", Boolean.parseBoolean(params[8]));
				dato.put("Jueves", Boolean.parseBoolean(params[9]));
				dato.put("Viernes", Boolean.parseBoolean(params[10]));
				dato.put("Sabado", Boolean.parseBoolean(params[11]));
				dato.put("Tono", params[12]);
				dato.put("Descripcion", params[13]);
				dato.put("Alarma", Boolean.parseBoolean(params[14]));
				dato.put("Eliminado",Boolean.parseBoolean(params[15]));
				
				StringEntity entity = new StringEntity(dato.toString());
				post.setEntity(entity);
				
				HttpResponse resp=httpClient.execute(post);
				String respStr = EntityUtils.toString(resp.getEntity());
				respuesta=Long.parseLong(respStr);
				
				if (respuesta>0){
					rutina = new TblRutinasPacientes(
							respuesta, Long.parseLong(params[1]),
							Long.parseLong(params[2]), 
							Integer.parseInt(params[3]),
							Integer.parseInt(params[4]),
							Boolean.parseBoolean(params[5]),
							Boolean.parseBoolean(params[6]),
							Boolean.parseBoolean(params[7]),
							Boolean.parseBoolean(params[8]),
							Boolean.parseBoolean(params[9]),
							Boolean.parseBoolean(params[10]),
							Boolean.parseBoolean(params[11]),
							params[12], params[13], 
							Boolean.parseBoolean(params[14]),
							Boolean.parseBoolean(params[15]));
					rutina.save();
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

	//TAREA ASINCRONA PARA ACTUALIZAR RUTINAS DE PACIENTES
    public class ActualizarRutinasPacientes extends AsyncTask<String, Integer, Boolean>
    {
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			Boolean resul=true;
			HttpClient httpClient=new DefaultHttpClient();
			
			HttpPut put=new HttpPut(
					"http://"+ip+"/ADP/RutinasPacientes/RutinaPacienteActualizar");
			put.setHeader("content-type", "application/json");
			
			try {
				JSONObject dato=new JSONObject();
				
				dato.put("IdRutinaP", Long.parseLong(params[0]));
				dato.put("IdPaciente", Long.parseLong(params[1]));
				dato.put("IdActividad", Long.parseLong(params[2]));
				dato.put("Hora", Integer.parseInt(params[3]));
				dato.put("Minutos", Integer.parseInt(params[4]));
				dato.put("Domingo", Boolean.parseBoolean(params[5]));
				dato.put("Lunes",Boolean.parseBoolean(params[6]));
				dato.put("Martes", Boolean.parseBoolean(params[7]));
				dato.put("Miercoles", Boolean.parseBoolean(params[8]));
				dato.put("Jueves", Boolean.parseBoolean(params[9]));
				dato.put("Viernes", Boolean.parseBoolean(params[10]));
				dato.put("Sabado", Boolean.parseBoolean(params[11]));
				dato.put("Tono", params[12]);
				dato.put("Descripcion", params[13]);
				dato.put("Alarma", Boolean.parseBoolean(params[14]));
				dato.put("Eliminado",Boolean.parseBoolean(params[15]));
				
				StringEntity entity = new StringEntity(dato.toString());
				put.setEntity(entity);
				
				HttpResponse resp=httpClient.execute(put);
				String respStr = EntityUtils.toString(resp.getEntity());
				
				if (!respStr.equals("true")) {
					resul=false;
				}else{
					String campo_ide=String.valueOf(Long.parseLong(params[0]));
					Select laRutina = Select.from(TblRutinasPacientes.class)
							.where(Condition.prop("ID_RUTINA_P").eq(campo_ide));
					TblRutinasPacientes edit_Rutina=(TblRutinasPacientes)laRutina.first();

					if (edit_Rutina!=null) {
						edit_Rutina.setIdRutinaP(Long.parseLong(params[0]));
						edit_Rutina.setIdPaciente(Long.parseLong(params[1]));
						edit_Rutina.setIdActividad(Long.parseLong(params[2]));
						edit_Rutina.setHora(Integer.parseInt(params[3]));
						edit_Rutina.setMinutos(Integer.parseInt(params[4]));
						edit_Rutina.setDomingo(Boolean.parseBoolean(params[5]));
						edit_Rutina.setLunes(Boolean.parseBoolean(params[6]));
						edit_Rutina.setMartes(Boolean.parseBoolean(params[7]));
						edit_Rutina.setMiercoles(Boolean.parseBoolean(params[8]));
						edit_Rutina.setJueves(Boolean.parseBoolean(params[9]));
						edit_Rutina.setViernes(Boolean.parseBoolean(params[10]));
						edit_Rutina.setSabado(Boolean.parseBoolean(params[11]));
						edit_Rutina.setTono(params[12]);
						edit_Rutina.setDescripcion(params[13]);
						edit_Rutina.setAlarma(Boolean.parseBoolean(params[14]));
						edit_Rutina.setEliminado(Boolean.parseBoolean(params[15]));
						edit_Rutina.save();
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
    
    //TAREA ASINCRONA PARA BUSCAR UNA RUTINA DE PACIENTES
    public class BuscarUnaRutinaPacientes extends AsyncTask<String, Integer, Boolean>{

		private TblRutinasPacientes rutina= new TblRutinasPacientes();
    	
    	@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			HttpClient httpClient= new DefaultHttpClient();
			String id=params[0];
			HttpGet BO= new HttpGet (
					"http://"+ip+"/ADP/RutinasPacientes/RutinaPacienteBuscar/"+id);
			BO.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BO);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONObject respJSON = new JSONObject(respStr);
				
				rutina.setIdRutinaP(respJSON.getLong("IdRutinaP"));
				rutina.setIdPaciente(respJSON.getLong("IdPaciente"));
				rutina.setIdActividad(respJSON.getLong("IdActividad"));
				rutina.setHora(respJSON.getInt("Hora"));
				rutina.setMinutos(respJSON.getInt("Minutos"));
				rutina.setDomingo(respJSON.getBoolean("Domingo"));
				rutina.setLunes(respJSON.getBoolean("Lunes"));
				rutina.setMartes(respJSON.getBoolean("Martes"));
				rutina.setMiercoles(respJSON.getBoolean("Miercoles"));
				rutina.setJueves(respJSON.getBoolean("Jueves"));
				rutina.setViernes(respJSON.getBoolean("Viernes"));
				rutina.setSabado(respJSON.getBoolean("Sabado"));
				rutina.setTono(respJSON.getString("Tono"));
				rutina.setDescripcion(respJSON.getString("Descripcion"));
				rutina.setAlarma(respJSON.getBoolean("Alarma"));
				rutina.setEliminado(respJSON.getBoolean("Eliminado"));
				
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
				String campo_ide=String.valueOf(rutina.getIdRutinaP());
				Select laRutina = Select.from(TblRutinasPacientes.class)
						.where(Condition.prop("ID_RUTINA_P").eq(campo_ide));
		    	TblRutinasPacientes edit_Rutina=(TblRutinasPacientes)laRutina.first();	
		    	
				if (edit_Rutina!=null) {
					edit_Rutina.setIdRutinaP(rutina.getIdRutinaP());
					edit_Rutina.setIdPaciente(rutina.getIdPaciente());
					edit_Rutina.setIdActividad(rutina.getIdActividad());
					edit_Rutina.setHora(rutina.getHora());
					edit_Rutina.setMinutos(rutina.getMinutos());
					edit_Rutina.setDomingo(rutina.getDomingo());
					edit_Rutina.setLunes(rutina.getLunes());
					edit_Rutina.setMartes(rutina.getMartes());
					edit_Rutina.setMiercoles(rutina.getMiercoles());
					edit_Rutina.setJueves(rutina.getJueves());
					edit_Rutina.setViernes(rutina.getViernes());
					edit_Rutina.setSabado(rutina.getSabado());
					edit_Rutina.setTono(rutina.getTono());
					edit_Rutina.setDescripcion(rutina.getDescripcion());
					edit_Rutina.setAlarma(rutina.getAlarma());
					edit_Rutina.setEliminado(rutina.getEliminado());
					edit_Rutina.save();
				}
				else{
					TblRutinasPacientes newRutinaP = new TblRutinasPacientes(
							rutina.getIdRutinaP(), rutina.getIdPaciente(),
							rutina.getIdActividad(), rutina.getHora(),
							rutina.getMinutos(), rutina.getDomingo(), 
							rutina.getLunes(), rutina.getMartes(),
							rutina.getMiercoles(), rutina.getJueves(), 
							rutina.getViernes(), rutina.getSabado(), 
							rutina.getTono(), rutina.getDescripcion(),
							rutina.getAlarma(), rutina.getEliminado());
					newRutinaP.save();
				}
				
			}
		}
    }
       
    //TAREA ASINCRONA PARA BUSCAR ALGUNAS RUTINAS DE PACIENTES
    public class BuscarAllRutinasPacientes extends AsyncTask<String, Integer, Boolean>{

    	private TblRutinasPacientes rutina= new TblRutinasPacientes();
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			String idP=params[0];
			HttpClient httpClient= new DefaultHttpClient();
			HttpGet BT= new HttpGet (
					"http://"+ip+"/ADP/RutinasPacientes/RutinasPacientesBuscarXPaciente/"+idP);
			BT.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BT);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONArray respJSON = new JSONArray(respStr);
				
				for (int i = 0; i <respJSON.length(); i++) {
					JSONObject obj=respJSON.getJSONObject(i);
					
					rutina.setIdRutinaP(obj.getLong("IdRutinaP"));
					rutina.setIdPaciente(obj.getLong("IdPaciente"));
					rutina.setIdActividad(obj.getLong("IdActividad"));
					rutina.setHora(obj.getInt("Hora"));
					rutina.setMinutos(obj.getInt("Minutos"));
					rutina.setDomingo(obj.getBoolean("Domingo"));
					rutina.setLunes(obj.getBoolean("Lunes"));
					rutina.setMartes(obj.getBoolean("Martes"));
					rutina.setMiercoles(obj.getBoolean("Miercoles"));
					rutina.setJueves(obj.getBoolean("Jueves"));
					rutina.setViernes(obj.getBoolean("Viernes"));
					rutina.setSabado(obj.getBoolean("Sabado"));
					rutina.setTono(obj.getString("Tono"));
					rutina.setDescripcion(obj.getString("Descripcion"));
					rutina.setAlarma(obj.getBoolean("Alarma"));
					rutina.setEliminado(obj.getBoolean("Eliminado"));
					
					
					TblRutinasPacientes guardar_rutina = new TblRutinasPacientes(
							rutina.getIdPaciente(), rutina.getIdPaciente(),
							rutina.getIdActividad(), rutina.getHora(),
							rutina.getMinutos(), rutina.getDomingo(), 
							rutina.getLunes(), rutina.getMartes(),
							rutina.getMiercoles(), rutina.getJueves(), 
							rutina.getViernes(), rutina.getSabado(), 
							rutina.getTono(), rutina.getDescripcion(),
							rutina.getAlarma(), rutina.getEliminado());
					guardar_rutina.save();
					
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
        
    //TAREA ASINCRONA PARA ELIMINAR UNA RUTINA DE PACIENTE
    public class EliminarRutinasPacientes extends AsyncTask<String, Integer, Boolean>{

    	private String id="";
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			HttpClient httpClient= new DefaultHttpClient();
			id=params[0];
			HttpDelete del= new HttpDelete (
					"http://"+ip+"/ADP/RutinasPacientes/RutinaPacienteEliminar/"+id);
			del.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(del);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				if (!respStr.equals("true")) {
					resul=false;
				}else{
					TblRutinasPacientes rutinasP = new TblRutinasPacientes();
					rutinasP.EliminarPorIdRutinaRegTblRutinasPacientes(Long.parseLong(id));
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
    
    //UTILIZADO PARA LA ACTUALIZACION DE LA BD
    //TAREA ASINCRONA PARA BUSCAR ALGUNAS RUTINAS DE PACIENTES
    public class BuscarAllRutinasCuidadores extends AsyncTask<String, Integer, Boolean>{

    	private TblRutinasPacientes rutina= new TblRutinasPacientes();
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			String idC=params[0];
			HttpClient httpClient= new DefaultHttpClient();
			HttpGet BT= new HttpGet (
					"http://"+ip+"/ADP/RutinasPacientes/RutinasPacientesBuscarXCuidadores/"+idC);
			BT.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BT);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONArray respJSON = new JSONArray(respStr);
				
				for (int i = 0; i <respJSON.length(); i++) {
					JSONObject obj=respJSON.getJSONObject(i);
					
					rutina = new TblRutinasPacientes();
					rutina.setIdRutinaP(obj.getLong("IdRutinaP"));
					rutina.setIdPaciente(obj.getLong("IdPaciente"));
					rutina.setIdActividad(obj.getLong("IdActividad"));
					rutina.setHora(obj.getInt("Hora"));
					rutina.setMinutos(obj.getInt("Minutos"));
					rutina.setDomingo(obj.getBoolean("Domingo"));
					rutina.setLunes(obj.getBoolean("Lunes"));
					rutina.setMartes(obj.getBoolean("Martes"));
					rutina.setMiercoles(obj.getBoolean("Miercoles"));
					rutina.setJueves(obj.getBoolean("Jueves"));
					rutina.setViernes(obj.getBoolean("Viernes"));
					rutina.setSabado(obj.getBoolean("Sabado"));
					rutina.setTono(obj.getString("Tono"));
					rutina.setDescripcion(obj.getString("Descripcion"));
					rutina.setAlarma(obj.getBoolean("Alarma"));
					rutina.setEliminado(obj.getBoolean("Eliminado"));
					
					
					TblRutinasPacientes guardar_rutina = new TblRutinasPacientes(
							rutina.getIdPaciente(), rutina.getIdPaciente(),
							rutina.getIdActividad(), rutina.getHora(),
							rutina.getMinutos(), rutina.getDomingo(), 
							rutina.getLunes(), rutina.getMartes(),
							rutina.getMiercoles(), rutina.getJueves(), 
							rutina.getViernes(), rutina.getSabado(), 
							rutina.getTono(), rutina.getDescripcion(),
							rutina.getAlarma(), rutina.getEliminado());
					guardar_rutina.save();
					
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

	public JsonArrayRequest AB_RutinasPacientes(long idC, final String TAG){
		String urlJsonArray = "http://"+ip+"/ADP/RutinasPacientes/RutinaPacienteBuscarXCuidadores/"+String.valueOf(idC);
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

								TblRutinasPacientes rutina = new TblRutinasPacientes();
								rutina.setIdRutinaP(obj.getLong("IdRutinaP"));
								rutina.setIdPaciente(obj.getLong("IdPaciente"));
								rutina.setIdActividad(obj.getLong("IdActividad"));
								rutina.setHora(obj.getInt("Hora"));
								rutina.setMinutos(obj.getInt("Minutos"));
								rutina.setDomingo(obj.getBoolean("Domingo"));
								rutina.setLunes(obj.getBoolean("Lunes"));
								rutina.setMartes(obj.getBoolean("Martes"));
								rutina.setMiercoles(obj.getBoolean("Miercoles"));
								rutina.setJueves(obj.getBoolean("Jueves"));
								rutina.setViernes(obj.getBoolean("Viernes"));
								rutina.setSabado(obj.getBoolean("Sabado"));
								rutina.setTono(obj.getString("Tono"));
								rutina.setDescripcion(obj.getString("Descripcion"));
								rutina.setAlarma(obj.getBoolean("Alarma"));
								rutina.setEliminado(obj.getBoolean("Eliminado"));
								rutina.save();

								Log.e("Ruc_Pac => Registro", "#" + i + "= " + rutina);
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
