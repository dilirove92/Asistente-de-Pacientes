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
import com.Notifications.patientssassistant.tables.TblEventosCuidadores;
import com.Notifications.patientssassistant.tables.TblEventosPacientes;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.orm.query.Condition;
import com.orm.query.Select;


public class ATEventosPaciente {

	//VARIABLES DE CLASE ATEventosPaciente 
	//private static String ip="192.168.1.4:1522";
	private static String ip=VarEstatic.ObtenerIP();
	public TblEventosPacientes eventoPaciente;
	
	
	//TAREA ASINCRONA PARA INSERTAR UN EVENTO DE UN PACIENTE
	public class InsertarEventosPaciente extends AsyncTask<String, Integer, Long>
    {
    	private TblEventosPacientes evenPac;
		private long id=0;
    	
		@Override
		protected Long doInBackground(String... params) {
			// TODO Auto-generated method stub
			Boolean resul=true;
			long respuesta=0;
			HttpClient httpClient=new DefaultHttpClient();
			
			HttpPost post=new HttpPost(
					"http://"+ip+"/ADP/EventosPaciente/EventoPacienteInsertar");
			post.setHeader("content-type", "application/json");
			
			try {
				JSONObject dato=new JSONObject();
				
				dato.put("IdEventoP", Long.parseLong(params[0]));
				dato.put("IdPaciente", Long.parseLong(params[1]));
				dato.put("IdActividad", Long.parseLong(params[2]));
				dato.put("AnioE", Integer.parseInt(params[3]));
				dato.put("MesE", Integer.parseInt(params[4]));
				dato.put("DiaE", Integer.parseInt(params[5]));
				dato.put("HoraE", Integer.parseInt(params[6]));
				dato.put("MinutosE", Integer.parseInt(params[7]));
				dato.put("AnioR", Integer.parseInt(params[8]));
				dato.put("MesR", Integer.parseInt(params[9]));
				dato.put("DiaR", Integer.parseInt(params[10]));
				dato.put("HoraR", Integer.parseInt(params[11]));
				dato.put("MinutosR", Integer.parseInt(params[12]));
				dato.put("Lugar", params[13]);
				dato.put("Descripcion", params[14]);
				dato.put("Tono", params[15]);
				dato.put("Alarma",Boolean.parseBoolean(params[16]));
				dato.put("Eliminado",Boolean.parseBoolean(params[17]));
				
				StringEntity entity = new StringEntity(dato.toString());
				post.setEntity(entity);
				
				HttpResponse resp=httpClient.execute(post);
				String respStr = EntityUtils.toString(resp.getEntity());
				respuesta=Long.parseLong(respStr);
				
				if (respuesta>0){
					evenPac = new TblEventosPacientes(
							respuesta, Long.parseLong(params[1]), 
							Long.parseLong(params[2]), 
							Integer.parseInt(params[3]),
							Integer.parseInt(params[4]),
							Integer.parseInt(params[5]),
							Integer.parseInt(params[6]),
							Integer.parseInt(params[7]),
							Integer.parseInt(params[8]),
							Integer.parseInt(params[9]),
							Integer.parseInt(params[10]),
							Integer.parseInt(params[11]),
							Integer.parseInt(params[12]),
							params[13], params[14], params[15],
							Boolean.parseBoolean(params[16]),
							Boolean.parseBoolean(params[17]));
					evenPac.save();
				}else{
					resul=false;}
				
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
    
	//TAREA ASINCRONA PARA ACTUALIZAR UN EVENTO DE UN PACIENTE
    public class ActualizarEventosPaciente extends AsyncTask<String, Integer, Boolean>
    {
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			Boolean resul=true;
			HttpClient httpClient=new DefaultHttpClient();
			
			HttpPut put=new HttpPut(
					"http://"+ip+"/ADP/EventosPaciente/EventoPacienteActualizar");
			put.setHeader("content-type", "application/json");
			
			try {
				JSONObject dato=new JSONObject();
				
				dato.put("IdEventoP", Long.parseLong(params[0]));
				dato.put("IdPaciente", Long.parseLong(params[1]));
				dato.put("IdActividad", Long.parseLong(params[2]));
				dato.put("AnioE", Integer.parseInt(params[3]));
				dato.put("MesE", Integer.parseInt(params[4]));
				dato.put("DiaE", Integer.parseInt(params[5]));
				dato.put("HoraE", Integer.parseInt(params[6]));
				dato.put("MinutosE", Integer.parseInt(params[7]));
				dato.put("AnioR", Integer.parseInt(params[8]));
				dato.put("MesR", Integer.parseInt(params[9]));
				dato.put("DiaR", Integer.parseInt(params[10]));
				dato.put("HoraR", Integer.parseInt(params[11]));
				dato.put("MinutosR", Integer.parseInt(params[12]));
				dato.put("Lugar", params[13]);
				dato.put("Descripcion", params[14]);
				dato.put("Tono", params[15]);
				dato.put("Alarma",Boolean.parseBoolean(params[16]));
				dato.put("Eliminado",Boolean.parseBoolean(params[17]));
				
				StringEntity entity = new StringEntity(dato.toString());
				put.setEntity(entity);
				
				HttpResponse resp=httpClient.execute(put);
				String respStr = EntityUtils.toString(resp.getEntity());
				
				if (!respStr.equals("true")) {
					resul=false;
				}else{
					String campo_ide=String.valueOf(Long.parseLong(params[0]));
					Select EventosPaciente = Select.from(TblEventosPacientes.class)
							.where(Condition.prop("Id_EVENTO_P").eq(campo_ide));
					TblEventosPacientes editar_EP=(TblEventosPacientes)EventosPaciente.first();

					if (editar_EP!=null) {
						editar_EP.setIdEventoP(Long.parseLong(params[0]));
						editar_EP.setIdPaciente(Long.parseLong(params[1]));
						editar_EP.setIdActividad(Long.parseLong(params[2]));
						editar_EP.setAnioE(Integer.parseInt(params[3]));
						editar_EP.setMesE(Integer.parseInt(params[4]));
						editar_EP.setDiaE(Integer.parseInt(params[5]));
						editar_EP.setHoraE(Integer.parseInt(params[6]));
						editar_EP.setMinutosE(Integer.parseInt(params[7]));
						editar_EP.setAnioR(Integer.parseInt(params[8]));
						editar_EP.setMesR(Integer.parseInt(params[9]));
						editar_EP.setDiaR(Integer.parseInt(params[10]));
						editar_EP.setHoraR(Integer.parseInt(params[11]));
						editar_EP.setMinutosE(Integer.parseInt(params[12]));
						editar_EP.setLugar(params[13]);
						editar_EP.setDescripcion(params[14]);
						editar_EP.setTono(params[15]);
						editar_EP.setAlarma(Boolean.parseBoolean(params[16]));
						editar_EP.setEliminado(Boolean.parseBoolean(params[17]));
						editar_EP.save();
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
    
    //TAREA ASINCRONA PARA BUSCAR UN EVENTO DE UN PACIENTE
    public class BuscarEventosPaciente extends AsyncTask<String, Integer, Boolean>{

		private TblEventosPacientes evenPac= new TblEventosPacientes();
    	
    	@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			HttpClient httpClient= new DefaultHttpClient();
			String id=params[0];
			HttpGet BO= new HttpGet (
					"http://"+ip+"/ADP/EventosPaciente/EventoPacienteBuscar/"+id);
			BO.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BO);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONObject respJSON = new JSONObject(respStr);
				
				evenPac.setIdEventoP(respJSON.getLong("IdEventoP"));
				evenPac.setIdPaciente(respJSON.getLong("IdPaciente"));
				evenPac.setIdActividad(respJSON.getLong("IdActividad"));
				evenPac.setAnioE(respJSON.getInt("AnioE"));
				evenPac.setMesE(respJSON.getInt("MesE"));
				evenPac.setDiaE(respJSON.getInt("DiaE"));
				evenPac.setHoraE(respJSON.getInt("HoraE"));
				evenPac.setMinutosE(respJSON.getInt("MinutosE"));
				evenPac.setAnioR(respJSON.getInt("AnioR"));
				evenPac.setMesR(respJSON.getInt("MesR"));
				evenPac.setDiaR(respJSON.getInt("DiaR"));
				evenPac.setHoraR(respJSON.getInt("HoraR"));
				evenPac.setMinutosR(respJSON.getInt("MinutosR"));
				evenPac.setLugar(respJSON.getString("Lugar"));
				evenPac.setDescripcion(respJSON.getString("Descripcion"));
				evenPac.setTono(respJSON.getString("Tono"));
				evenPac.setAlarma(respJSON.getBoolean("Alarma"));
				evenPac.setEliminado(respJSON.getBoolean("Eliminado"));
				
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
				String campo_ide=String.valueOf(evenPac.getIdEventoP());
				Select EventosPaciente = Select.from(TblEventosPacientes.class)
						.where(Condition.prop("ID_EVENTO_P").eq(campo_ide));
		    	TblEventosPacientes editar_EP=(TblEventosPacientes)EventosPaciente.first();	
		    	
				if (editar_EP!=null) {
					editar_EP.setIdEventoP(evenPac.getIdEventoP());
					editar_EP.setIdPaciente(evenPac.getIdPaciente());
					editar_EP.setIdActividad(evenPac.getIdActividad());
					editar_EP.setAnioE(evenPac.getAnioE());
					editar_EP.setMesE(evenPac.getMesE());
					editar_EP.setDiaE(evenPac.getDiaE());
					editar_EP.setHoraE(evenPac.getHoraE());
					editar_EP.setMinutosE(evenPac.getMinutosE());
					editar_EP.setAnioR(evenPac.getAnioR());
					editar_EP.setMesR(evenPac.getMesR());
					editar_EP.setDiaR(evenPac.getDiaR());
					editar_EP.setHoraR(evenPac.getHoraR());
					editar_EP.setMinutosE(evenPac.getMinutosE());
					editar_EP.setLugar(evenPac.getLugar());
					editar_EP.setDescripcion(evenPac.getDescripcion());
					editar_EP.setTono(evenPac.getTono());
					editar_EP.setAlarma(evenPac.getAlarma());
					editar_EP.setEliminado(evenPac.getEliminado());
					editar_EP.save();
					editar_EP.save();
				}
				else{
					TblEventosCuidadores newEventoC = new TblEventosCuidadores(
							evenPac.getIdEventoP(), evenPac.getIdPaciente(),
							evenPac.getIdActividad(), evenPac.getAnioE(),
							evenPac.getMesE(), evenPac.getDiaE(), evenPac.getHoraE(),
							evenPac.getMinutosE(), evenPac.getAnioR(),evenPac.getMesR(),
							evenPac.getDiaE(), evenPac.getHoraE(), evenPac.getMinutosE(), 
							evenPac.getLugar(), evenPac.getDescripcion(), evenPac.getTono(),
							evenPac.getAlarma(), evenPac.getEliminado());
					newEventoC.save();
				}
				
			}
		}
    }
     
    //TAREA ASINCRONA PARA BUSCAR TODOS LOS EVENTOS DE UN DETERMINADO PACIENTE
    public class BuscarAllEventosPaciente extends AsyncTask<String, Integer, Boolean>{

    	private TblEventosPacientes evenPac= new TblEventosPacientes();
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			String idP=params[0];
			HttpClient httpClient= new DefaultHttpClient();
			HttpGet BT= new HttpGet (
					"http://"+ip+"/ADP/EventosPaciente/EventosPacienteBuscarXPaciente/"+idP);
			BT.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BT);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONArray respJSON = new JSONArray(respStr);
				
				for (int i = 0; i <respJSON.length(); i++) {
					JSONObject obj=respJSON.getJSONObject(i);
										
					evenPac = new TblEventosPacientes();
					evenPac.setIdEventoP(obj.getLong("IdEventoP"));
					evenPac.setIdPaciente(obj.getLong("IdPaciente"));
					evenPac.setIdActividad(obj.getLong("IdActividad"));
					evenPac.setAnioE(obj.getInt("AnioE"));
					evenPac.setMesE(obj.getInt("MesE"));
					evenPac.setDiaE(obj.getInt("DiaE"));
					evenPac.setHoraE(obj.getInt("HoraE"));
					evenPac.setMinutosE(obj.getInt("MinutosE"));
					evenPac.setAnioR(obj.getInt("AnioR"));
					evenPac.setMesR(obj.getInt("MesR"));
					evenPac.setDiaR(obj.getInt("DiaR"));
					evenPac.setHoraR(obj.getInt("HoraR"));
					evenPac.setMinutosR(obj.getInt("MinutosR"));
					evenPac.setLugar(obj.getString("Lugar"));
					evenPac.setDescripcion(obj.getString("Descripcion"));
					evenPac.setTono(obj.getString("Tono"));
					evenPac.setAlarma(obj.getBoolean("Alarma"));
					evenPac.setEliminado(obj.getBoolean("Eliminado"));
					
					TblEventosPacientes guardar_EC = new TblEventosPacientes(
							evenPac.getIdEventoP(), evenPac.getIdPaciente(),
							evenPac.getIdActividad(), evenPac.getAnioE(),
							evenPac.getMesE(), evenPac.getDiaE(), evenPac.getHoraE(),
							evenPac.getMinutosE(), evenPac.getAnioR(),evenPac.getMesR(),
							evenPac.getDiaE(), evenPac.getHoraE(), evenPac.getMinutosE(), 
							evenPac.getLugar(), evenPac.getDescripcion(), evenPac.getTono(),
							evenPac.getAlarma(), evenPac.getEliminado());
					guardar_EC.save();
					
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
    
    //TAREA ASINCRONA PARA ELIMINAR UN EVENTO DE UN CUIDADOR
    public class EliminarEventoCuidador extends AsyncTask<String, Integer, Boolean>{

    	private String id="";
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			HttpClient httpClient= new DefaultHttpClient();
			id=params[0];
			HttpDelete del= new HttpDelete (
					"http://"+ip+"/ADP/EventosPaciente/EventoPacienteEliminar/"+id);
			del.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(del);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				if (!respStr.equals("true")) {
					resul=false;
				}else{
					TblEventosPacientes eventoP = new TblEventosPacientes();
					eventoP.EliminarPorIdEventoRegTblEventosPacientes(Long.parseLong(id));
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
    
    //UTILIZADO PARA ACTUALIZAR LA BD
    //TAREA ASINCRONA PARA BUSCAR TODOS LOS EVENTOS DE UN DETERMINADO PACIENTE
    public class BuscarAllEventosCuidadores extends AsyncTask<String, Integer, Boolean>{

    	private TblEventosPacientes evenPac= new TblEventosPacientes();
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			String idC=params[0];
			HttpClient httpClient= new DefaultHttpClient();
			HttpGet BT= new HttpGet (
					"http://"+ip+"/ADP/EventosPaciente/EventosPacienteBuscarXCuidadores/"+idC);
			BT.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BT);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONArray respJSON = new JSONArray(respStr);
				
				for (int i = 0; i <respJSON.length(); i++) {
					JSONObject obj=respJSON.getJSONObject(i);
										
					evenPac = new TblEventosPacientes();
					evenPac.setIdEventoP(obj.getLong("IdEventoP"));
					evenPac.setIdPaciente(obj.getLong("IdPaciente"));
					evenPac.setIdActividad(obj.getLong("IdActividad"));
					evenPac.setAnioE(obj.getInt("AnioE"));
					evenPac.setMesE(obj.getInt("MesE"));
					evenPac.setDiaE(obj.getInt("DiaE"));
					evenPac.setHoraE(obj.getInt("HoraE"));
					evenPac.setMinutosE(obj.getInt("MinutosE"));
					evenPac.setAnioR(obj.getInt("AnioR"));
					evenPac.setMesR(obj.getInt("MesR"));
					evenPac.setDiaR(obj.getInt("DiaR"));
					evenPac.setHoraR(obj.getInt("HoraR"));
					evenPac.setMinutosR(obj.getInt("MinutosR"));
					evenPac.setLugar(obj.getString("Lugar"));
					evenPac.setDescripcion(obj.getString("Descripcion"));
					evenPac.setTono(obj.getString("Tono"));
					evenPac.setAlarma(obj.getBoolean("Alarma"));
					evenPac.setEliminado(obj.getBoolean("Eliminado"));
					
					TblEventosPacientes guardar_EC = new TblEventosPacientes(
							evenPac.getIdEventoP(), evenPac.getIdPaciente(),
							evenPac.getIdActividad(), evenPac.getAnioE(),
							evenPac.getMesE(), evenPac.getDiaE(), evenPac.getHoraE(),
							evenPac.getMinutosE(), evenPac.getAnioR(),evenPac.getMesR(),
							evenPac.getDiaE(), evenPac.getHoraE(), evenPac.getMinutosE(), 
							evenPac.getLugar(), evenPac.getDescripcion(), evenPac.getTono(),
							evenPac.getAlarma(), evenPac.getEliminado());
					guardar_EC.save();
					
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

	public JsonArrayRequest AB_EventosPacientes(long idC, final String TAG){
		String urlJsonArray = "http://"+ip+"/ADP/EventosPaciente/EventoPacienteBuscarXCuidadores/"+String.valueOf(idC);
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

								TblEventosPacientes evenPac = new TblEventosPacientes();
								evenPac.setIdEventoP(obj.getLong("IdEventoP"));
								evenPac.setIdPaciente(obj.getLong("IdPaciente"));
								evenPac.setIdActividad(obj.getLong("IdActividad"));
								evenPac.setAnioE(obj.getInt("AnioE"));
								evenPac.setMesE(obj.getInt("MesE"));
								evenPac.setDiaE(obj.getInt("DiaE"));
								evenPac.setHoraE(obj.getInt("HoraE"));
								evenPac.setMinutosE(obj.getInt("MinutosE"));
								evenPac.setAnioR(obj.getInt("AnioR"));
								evenPac.setMesR(obj.getInt("MesR"));
								evenPac.setDiaR(obj.getInt("DiaR"));
								evenPac.setHoraR(obj.getInt("HoraR"));
								evenPac.setMinutosR(obj.getInt("MinutosR"));
								evenPac.setLugar(obj.getString("Lugar"));
								evenPac.setDescripcion(obj.getString("Descripcion"));
								evenPac.setTono(obj.getString("Tono"));
								evenPac.setAlarma(obj.getBoolean("Alarma"));
								evenPac.setEliminado(obj.getBoolean("Eliminado"));
								evenPac.save();

								Log.e("Even_Pac => Registro", "#" + i + "= " + evenPac);
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
