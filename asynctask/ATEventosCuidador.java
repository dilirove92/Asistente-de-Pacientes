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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.orm.query.Condition;
import com.orm.query.Select;


public class ATEventosCuidador {

	//VARIABLES DE CLASE ATEventosCuidador
	//private static String ip="192.168.1.4:1522";
	private static String ip=VarEstatic.ObtenerIP();


	//TAREA ASINCRONA PARA INSERTAR UN EVENTO DE UN CUIDADOR
	public class InsertarEventosCuidador extends AsyncTask<String, Integer, Long>
    {
    	private TblEventosCuidadores evenCuid;
		private long id=0;
    	
		@Override
		protected Long doInBackground(String... params) {
			// TODO Auto-generated method stub
			Boolean resul=true;
			long respuesta=0;
			HttpClient httpClient=new DefaultHttpClient();
			
			HttpPost post=new HttpPost(
					"http://"+ip+"/ADP/EventosCuidador/EventoCuidadorInsertar");
			post.setHeader("content-type", "application/json");
			
			try {
				JSONObject dato=new JSONObject();
				
				dato.put("IdEventoC", Long.parseLong(params[0]));
				dato.put("IdCuidador", Long.parseLong(params[1]));
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
					evenCuid = new TblEventosCuidadores(
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
					evenCuid.save();
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


	//TAREA ASINCRONA PARA ACTUALIZAR UN EVENTO DE UN CUIDADOR
    public class ActualizarEventosCuidador extends AsyncTask<String, Integer, Boolean>
    {
    	private TblEventosCuidadores evenCuid;
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			Boolean resul=true;
			HttpClient httpClient=new DefaultHttpClient();
			
			HttpPut put=new HttpPut(
					"http://"+ip+"/ADP/EventosCuidador/EventoCuidadorActualizar");
			put.setHeader("content-type", "application/json");
			
			try {
				JSONObject dato=new JSONObject();
				
				dato.put("IdEventoC", Long.parseLong(params[0]));
				dato.put("IdCuidador", Long.parseLong(params[1]));
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
					Select EventosCuidador = Select.from(TblEventosCuidadores.class)
							.where(Condition.prop("ID_EVENTO_C").eq(campo_ide));
					TblEventosCuidadores editar_EC=(TblEventosCuidadores)EventosCuidador.first();

					if (editar_EC!=null) {
						editar_EC.setIdCuidador(Long.parseLong(params[1]));
						editar_EC.setIdActividad(Long.parseLong(params[2]));
						editar_EC.setAnioE(Integer.parseInt(params[3]));
						editar_EC.setMesE(Integer.parseInt(params[4]));
						editar_EC.setDiaE(Integer.parseInt(params[5]));
						editar_EC.setHoraE(Integer.parseInt(params[6]));
						editar_EC.setMinutosE(Integer.parseInt(params[7]));
						editar_EC.setAnioR(Integer.parseInt(params[8]));
						editar_EC.setMesR(Integer.parseInt(params[9]));
						editar_EC.setDiaR(Integer.parseInt(params[10]));
						editar_EC.setHoraR(Integer.parseInt(params[11]));
						editar_EC.setMinutosE(Integer.parseInt(params[12]));
						editar_EC.setLugar(params[13]);
						editar_EC.setDescripcion(params[14]);
						editar_EC.setTono(params[15]);
						editar_EC.setAlarma(Boolean.parseBoolean(params[16]));
						editar_EC.setEliminado(Boolean.parseBoolean(params[17]));
						editar_EC.save();
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
      
    //TAREA ASINCRONA PARA BUSCAR UN EVENTO REALIZADO POR UN CUIDADOR
    public class BuscarEventosCuidador extends AsyncTask<String, Integer, Boolean>{

		private TblEventosCuidadores evenCuid= new TblEventosCuidadores();
    	
    	@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			HttpClient httpClient= new DefaultHttpClient();
			String id=params[0];
			HttpGet BO= new HttpGet (
					"http://"+ip+"/ADP/EventosCuidador/EventoCuidadorBuscar/"+id);
			BO.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BO);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONObject respJSON = new JSONObject(respStr);
				
				evenCuid.setIdEventoC(respJSON.getLong("IdEventoC"));
				evenCuid.setIdCuidador(respJSON.getLong("IdCuidador"));
				evenCuid.setIdActividad(respJSON.getLong("IdActividad"));
				evenCuid.setAnioE(respJSON.getInt("AnioE"));
				evenCuid.setMesE(respJSON.getInt("MesE"));
				evenCuid.setDiaE(respJSON.getInt("DiaE"));
				evenCuid.setHoraE(respJSON.getInt("HoraE"));
				evenCuid.setMinutosE(respJSON.getInt("MinutosE"));
				evenCuid.setAnioR(respJSON.getInt("AnioR"));
				evenCuid.setMesR(respJSON.getInt("MesR"));
				evenCuid.setDiaR(respJSON.getInt("DiaR"));
				evenCuid.setHoraR(respJSON.getInt("HoraR"));
				evenCuid.setMinutosR(respJSON.getInt("MinutosR"));
				evenCuid.setLugar(respJSON.getString("Lugar"));
				evenCuid.setDescripcion(respJSON.getString("Descripcion"));
				evenCuid.setTono(respJSON.getString("Tono"));
				evenCuid.setAlarma(respJSON.getBoolean("Alarma"));
				evenCuid.setEliminado(respJSON.getBoolean("Eliminado"));
				
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
				String campo_ide=String.valueOf(evenCuid.getIdEventoC());
				Select EventosCuidador = Select.from(TblEventosCuidadores.class)
						.where(Condition.prop("ID_EVENTO_C").eq(campo_ide));
		    	TblEventosCuidadores editar_EC=(TblEventosCuidadores)EventosCuidador.first();		
				if (editar_EC!=null) {
					editar_EC.setIdEventoC(evenCuid.getIdEventoC());
					editar_EC.setIdCuidador(evenCuid.getIdCuidador());
					editar_EC.setIdActividad(evenCuid.getIdActividad());
					editar_EC.setAnioE(evenCuid.getAnioE());
					editar_EC.setMesE(evenCuid.getMesE());
					editar_EC.setDiaE(evenCuid.getDiaE());
					editar_EC.setHoraE(evenCuid.getHoraE());
					editar_EC.setMinutosE(evenCuid.getMinutosE());
					editar_EC.setAnioR(evenCuid.getAnioR());
					editar_EC.setMesR(evenCuid.getMesR());
					editar_EC.setDiaR(evenCuid.getDiaR());
					editar_EC.setHoraR(evenCuid.getHoraR());
					editar_EC.setMinutosE(evenCuid.getMinutosE());
					editar_EC.setLugar(evenCuid.getLugar());
					editar_EC.setDescripcion(evenCuid.getDescripcion());
					editar_EC.setTono(evenCuid.getTono());
					editar_EC.setAlarma(evenCuid.getAlarma());
					editar_EC.setEliminado(evenCuid.getEliminado());
					editar_EC.save();
				}else{
					TblEventosCuidadores newEventoC = new TblEventosCuidadores(
							evenCuid.getIdEventoC(), evenCuid.getIdCuidador(),
							evenCuid.getIdActividad(), evenCuid.getAnioE(),
							evenCuid.getMesE(), evenCuid.getDiaE(), evenCuid.getHoraE(),
							evenCuid.getMinutosE(), evenCuid.getAnioR(),evenCuid.getMesR(),
							evenCuid.getDiaR(), evenCuid.getHoraR(), evenCuid.getMinutosR(),
							evenCuid.getLugar(), evenCuid.getDescripcion(), evenCuid.getTono(),
							evenCuid.getAlarma(), evenCuid.getEliminado());
					newEventoC.save();
				}
			}
		}
    }
       
    //TAREA ASINCRONA PARA BUSCAR TODOS LOS EVENTOS DE UN CUIDADOR
    public class BuscarAllEventosCuidador extends AsyncTask<String, Integer, Boolean>{

    	private TblEventosCuidadores evenCuid= new TblEventosCuidadores();
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			String idC=params[0];
			HttpClient httpClient= new DefaultHttpClient();
			HttpGet BT= new HttpGet (
					"http://"+ip+"/ADP/EventosCuidador/EventosCuidadorBuscarXCuidador/"+idC);
			BT.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BT);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONArray respJSON = new JSONArray(respStr);
				
				for (int i = 0; i <respJSON.length(); i++) {
					JSONObject obj=respJSON.getJSONObject(i);
									
					evenCuid = new TblEventosCuidadores();
					evenCuid.setIdEventoC(obj.getLong("IdEventoC"));
					evenCuid.setIdCuidador(obj.getLong("IdCuidador"));
					evenCuid.setIdActividad(obj.getLong("IdActividad"));
					evenCuid.setAnioE(obj.getInt("AnioE"));
					evenCuid.setMesE(obj.getInt("MesE"));
					evenCuid.setDiaE(obj.getInt("DiaE"));
					evenCuid.setHoraE(obj.getInt("HoraE"));
					evenCuid.setMinutosE(obj.getInt("MinutosE"));
					evenCuid.setAnioR(obj.getInt("AnioR"));
					evenCuid.setMesR(obj.getInt("MesR"));
					evenCuid.setDiaR(obj.getInt("DiaR"));
					evenCuid.setHoraR(obj.getInt("HoraR"));
					evenCuid.setMinutosR(obj.getInt("MinutosR"));
					evenCuid.setLugar(obj.getString("Lugar"));
					evenCuid.setDescripcion(obj.getString("Descripcion"));
					evenCuid.setTono(obj.getString("Tono"));
					evenCuid.setAlarma(obj.getBoolean("Alarma"));
					evenCuid.setEliminado(obj.getBoolean("Eliminado"));
					
					TblEventosCuidadores guardar_EC = new TblEventosCuidadores(
							evenCuid.getIdEventoC(), evenCuid.getIdCuidador(),
							evenCuid.getIdActividad(), evenCuid.getAnioE(),
							evenCuid.getMesE(), evenCuid.getDiaE(), evenCuid.getHoraE(),
							evenCuid.getMinutosE(), evenCuid.getAnioR(),evenCuid.getMesR(),
							evenCuid.getDiaR(), evenCuid.getHoraR(), evenCuid.getMinutosR(),
							evenCuid.getLugar(), evenCuid.getDescripcion(), evenCuid.getTono(),
							evenCuid.getAlarma(), evenCuid.getEliminado());
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
					"http://"+ip+"/ADP/EventosCuidador/EventoCuidadorEliminar/"+id);
			del.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(del);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				if (!respStr.equals("true")) {
					resul=false;
				}else{
					TblEventosCuidadores eventoC = new TblEventosCuidadores();
					eventoC.EliminarPorIdEventoRegTblEventosCuidadores(Long.parseLong(id));
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
    
    //UTILIZAR PARA LA ACTUALIZACION DE LA BD
    //TAREA ASINCRONA PARA BUSCAR TODOS LOS EVENTOS DE UN CUIDADOR
    public class BuscarAllEventosCuidadores extends AsyncTask<String, Integer, Boolean>{

    	private TblEventosCuidadores evenCuid= new TblEventosCuidadores();
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			String idC=params[0];
			HttpClient httpClient= new DefaultHttpClient();
			HttpGet BT= new HttpGet (
					"http://"+ip+"/ADP/EventosCuidador/EventosCuidadorBuscarXCuidadores/"+idC);
			BT.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BT);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONArray respJSON = new JSONArray(respStr);
				
				for (int i = 0; i <respJSON.length(); i++) {
					JSONObject obj=respJSON.getJSONObject(i);
				    
					evenCuid = new TblEventosCuidadores();
					evenCuid.setIdEventoC(obj.getLong("IdEventoC"));
					evenCuid.setIdCuidador(obj.getLong("IdCuidador"));
					evenCuid.setIdActividad(obj.getLong("IdActividad"));
					evenCuid.setAnioE(obj.getInt("AnioE"));
					evenCuid.setMesE(obj.getInt("MesE"));
					evenCuid.setDiaE(obj.getInt("DiaE"));
					evenCuid.setHoraE(obj.getInt("HoraE"));
					evenCuid.setMinutosE(obj.getInt("MinutosE"));
					evenCuid.setAnioR(obj.getInt("AnioR"));
					evenCuid.setMesR(obj.getInt("MesR"));
					evenCuid.setDiaR(obj.getInt("DiaR"));
					evenCuid.setHoraR(obj.getInt("HoraR"));
					evenCuid.setMinutosR(obj.getInt("MinutosR"));
					evenCuid.setLugar(obj.getString("Lugar"));
					evenCuid.setDescripcion(obj.getString("Descripcion"));
					evenCuid.setTono(obj.getString("Tono"));
					evenCuid.setAlarma(obj.getBoolean("Alarma"));
					evenCuid.setEliminado(obj.getBoolean("Eliminado"));
					
					TblEventosCuidadores guardar_EC = new TblEventosCuidadores(
							evenCuid.getIdEventoC(), evenCuid.getIdCuidador(),
							evenCuid.getIdActividad(), evenCuid.getAnioE(),
							evenCuid.getMesE(), evenCuid.getDiaE(), evenCuid.getHoraE(),
							evenCuid.getMinutosE(), evenCuid.getAnioR(),evenCuid.getMesR(),
							evenCuid.getDiaR(), evenCuid.getHoraR(), evenCuid.getMinutosR(),
							evenCuid.getLugar(), evenCuid.getDescripcion(), evenCuid.getTono(),
							evenCuid.getAlarma(), evenCuid.getEliminado());
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

	public JsonArrayRequest AB_EventosCuidador(long idC, final String TAG){
		String urlJsonArray = "http://"+ip+"/ADP/EventosCuidador/EventoCuidadorBuscarXCuidadores/"+String.valueOf(idC);
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

								TblEventosCuidadores evenCuid = new TblEventosCuidadores();
								evenCuid.setIdEventoC(obj.getLong("IdEventoC"));
								evenCuid.setIdCuidador(obj.getLong("IdCuidador"));
								evenCuid.setIdActividad(obj.getLong("IdActividad"));
								evenCuid.setAnioE(obj.getInt("AnioE"));
								evenCuid.setMesE(obj.getInt("MesE"));
								evenCuid.setDiaE(obj.getInt("DiaE"));
								evenCuid.setHoraE(obj.getInt("HoraE"));
								evenCuid.setMinutosE(obj.getInt("MinutosE"));
								evenCuid.setAnioR(obj.getInt("AnioR"));
								evenCuid.setMesR(obj.getInt("MesR"));
								evenCuid.setDiaR(obj.getInt("DiaR"));
								evenCuid.setHoraR(obj.getInt("HoraR"));
								evenCuid.setMinutosR(obj.getInt("MinutosR"));
								evenCuid.setLugar(obj.getString("Lugar"));
								evenCuid.setDescripcion(obj.getString("Descripcion"));
								evenCuid.setTono(obj.getString("Tono"));
								evenCuid.setAlarma(obj.getBoolean("Alarma"));
								evenCuid.setEliminado(obj.getBoolean("Eliminado"));
								evenCuid.save();

								Log.e("Even_Cui => Registro", "#" + i + "= " + evenCuid);
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
