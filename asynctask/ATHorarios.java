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
import com.Notifications.patientssassistant.tables.TblHorarios;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.orm.query.Condition;
import com.orm.query.Select;


public class ATHorarios{

	//VARIABLES DE CLASE ATHorarios 
	//private static String ip="192.168.1.4:1522";
	private static String ip=VarEstatic.ObtenerIP();
	
	//TAREA ASINCRONA PARA INSERTAR UN HORARIO
	public class InsertarHorario extends AsyncTask<String, Integer, Long>
    {
    	private TblHorarios horario;
    	long respuesta=0;
		private long id=0;
    	
		@Override
		protected Long doInBackground(String... params) {
			// TODO Auto-generated method stub
			Boolean resul=true;
			HttpClient httpClient=new DefaultHttpClient();
			
			HttpPost post=new HttpPost(
					"http://"+ip+"/ADP/Horarios/HorarioInsertar");
			post.setHeader("content-type", "application/json");
			
			try {
				JSONObject dato=new JSONObject();
							
				dato.put("IdHorario", Long.parseLong(params[0]));
				dato.put("IdCuidador", Long.parseLong(params[1]));
				dato.put("TipoHorario",params[2]);
				dato.put("HoraIni", params[3]);
				dato.put("MinutosIni", params[4]);
				dato.put("HoraFin", params[5]);
				dato.put("MinutosFin", params[6]);
				dato.put("Domingo", Boolean.parseBoolean(params[7]));
				dato.put("Lunes",Boolean.parseBoolean(params[8]));
				dato.put("Martes", Boolean.parseBoolean(params[9]));
				dato.put("Miercoles", Boolean.parseBoolean(params[10]));
				dato.put("Jueves", Boolean.parseBoolean(params[11]));
				dato.put("Viernes", Boolean.parseBoolean(params[12]));
				dato.put("Sabado", Boolean.parseBoolean(params[13]));
				dato.put("Eliminado",Boolean.parseBoolean(params[14]));
				
				StringEntity entity = new StringEntity(dato.toString());
				post.setEntity(entity);
				
				HttpResponse resp=httpClient.execute(post);
				String respStr = EntityUtils.toString(resp.getEntity());
				respuesta=Long.parseLong(respStr);
				
				if (respuesta>0){
					horario = new TblHorarios(
								respuesta, Long.parseLong(params[1]),
								params[2], Integer.parseInt(params[3]),
								Integer.parseInt(params[4]),
								Integer.parseInt(params[5]),
								Integer.parseInt(params[6]),
								Boolean.parseBoolean(params[7]),
								Boolean.parseBoolean(params[8]),
								Boolean.parseBoolean(params[9]),
								Boolean.parseBoolean(params[10]),
								Boolean.parseBoolean(params[11]),
								Boolean.parseBoolean(params[12]),
								Boolean.parseBoolean(params[13]),
								Boolean.parseBoolean(params[14]));
					horario.save();
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
	
	//TAREA ASICRONA PARA ACTUALIZAR UN HORARIO
    public class ActualizarHorarios extends AsyncTask<String, Integer, Boolean>
    {
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			Boolean resul=true;
			HttpClient httpClient=new DefaultHttpClient();
			
			HttpPut put=new HttpPut(
					"http://"+ip+"/ADP/Horarios/HorarioActualizar");
			put.setHeader("content-type", "application/json");
			
			try {
				JSONObject dato=new JSONObject();
				
				dato.put("IdHorario", Long.parseLong(params[0]));
				dato.put("IdCuidador", Long.parseLong(params[1]));
				dato.put("TipoHorario",params[2]);
				dato.put("HoraIni", params[3]);
				dato.put("MinutosIni", params[4]);
				dato.put("HoraFin", params[5]);
				dato.put("MinutosFin", params[6]);
				dato.put("Domingo", Boolean.parseBoolean(params[7]));
				dato.put("Lunes",Boolean.parseBoolean(params[8]));
				dato.put("Martes", Boolean.parseBoolean(params[9]));
				dato.put("Miercoles", Boolean.parseBoolean(params[10]));
				dato.put("Jueves", Boolean.parseBoolean(params[11]));
				dato.put("Viernes", Boolean.parseBoolean(params[12]));
				dato.put("Sabado", Boolean.parseBoolean(params[13]));
				dato.put("Eliminado",Boolean.parseBoolean(params[14]));
				
				StringEntity entity = new StringEntity(dato.toString());
				put.setEntity(entity);
				
				HttpResponse resp=httpClient.execute(put);
				String respStr = EntityUtils.toString(resp.getEntity());
				
				if (!respStr.equals("true")) {
					resul=false;
				}else{
					String campo_ide=String.valueOf(Long.parseLong(params[0]));
					Select elHorario = Select.from(TblHorarios.class)
							.where(Condition.prop("ID_HORARIO").eq(campo_ide));
					TblHorarios edit_Horario=(TblHorarios)elHorario.first();

					if (edit_Horario!=null) {
						edit_Horario.setIdHorario(Long.parseLong(params[0]));
						edit_Horario.setIdCuidador(Long.parseLong(params[1]));
						edit_Horario.setTipoHorario(params[2]);
						edit_Horario.setHoraIni(Integer.parseInt(params[3]));
						edit_Horario.setMinutosIni(Integer.parseInt(params[4]));
						edit_Horario.setHoraFin(Integer.parseInt(params[5]));
						edit_Horario.setMinutosFin(Integer.parseInt(params[6]));
						edit_Horario.setDomingo(Boolean.parseBoolean(params[7]));
						edit_Horario.setLunes(Boolean.parseBoolean(params[8]));
						edit_Horario.setMartes(Boolean.parseBoolean(params[9]));
						edit_Horario.setMiercoles(Boolean.parseBoolean(params[10]));
						edit_Horario.setJueves(Boolean.parseBoolean(params[11]));
						edit_Horario.setViernes(Boolean.parseBoolean(params[12]));
						edit_Horario.setSabado(Boolean.parseBoolean(params[13]));
						edit_Horario.setEliminado(Boolean.parseBoolean(params[14]));
						edit_Horario.save();
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
    
    //TAREA ASINCRONA PARA BUSCAR UN HORARIO
    public class BuscarUnHorario extends AsyncTask<String, Integer, Boolean>{

		private TblHorarios horario= new TblHorarios();
    	
    	@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			HttpClient httpClient= new DefaultHttpClient();
			String id=params[0];
			HttpGet BO= new HttpGet (
					"http://"+ip+"/ADP/Horarios/HorarioBuscar/"+id);
			BO.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BO);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONObject respJSON = new JSONObject(respStr);
				
				horario = new TblHorarios();
				horario.setIdHorario(respJSON.getLong("IdHorario"));
				horario.setIdCuidador(respJSON.getLong("IdCuidadora"));
				horario.setTipoHorario(respJSON.getString("TipoHorario"));
				horario.setHoraIni(respJSON.getInt("HoraIni"));
				horario.setMinutosIni(respJSON.getInt("MinutosIni"));
				horario.setHoraFin(respJSON.getInt("HoraFin"));
				horario.setMinutosFin(respJSON.getInt("MinutosFin"));
				horario.setDomingo(respJSON.getBoolean("Domingo"));
				horario.setLunes(respJSON.getBoolean("Lunes"));
				horario.setMartes(respJSON.getBoolean("Martes"));
				horario.setMiercoles(respJSON.getBoolean("Miercoles"));
				horario.setJueves(respJSON.getBoolean("Jueves"));
				horario.setViernes(respJSON.getBoolean("Viernes"));
				horario.setSabado(respJSON.getBoolean("Sabado"));
				horario.setEliminado(respJSON.getBoolean("Eliminado"));
				
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
				String campo_ide=String.valueOf(horario.getIdHorario());
				Select elHorario = Select.from(TblHorarios.class)
						.where(Condition.prop("ID_HORARIO").eq(campo_ide));
		    	TblHorarios edit_Horario=(TblHorarios)elHorario.first();
		    	
				if (edit_Horario!=null) {
					edit_Horario.setIdHorario(horario.getIdHorario());
					edit_Horario.setIdCuidador(horario.getIdCuidador());
					edit_Horario.setTipoHorario(horario.getTipoHorario());
					edit_Horario.setHoraIni(horario.getHoraIni());
					edit_Horario.setMinutosIni(horario.getMinutosIni());
					edit_Horario.setHoraFin(horario.getHoraFin());
					edit_Horario.setMinutosFin(horario.getMinutosFin());
					edit_Horario.setDomingo(horario.getDomingo());
					edit_Horario.setLunes(horario.getLunes());
					edit_Horario.setMartes(horario.getMartes());
					edit_Horario.setMiercoles(horario.getMiercoles());
					edit_Horario.setJueves(horario.getJueves());
					edit_Horario.setViernes(horario.getViernes());
					edit_Horario.setSabado(horario.getSabado());
					edit_Horario.setEliminado(horario.getEliminado());
					edit_Horario.save();
				}
				else{
					TblHorarios newHorarios = new TblHorarios(
							horario.getIdHorario(), horario.getIdCuidador(),
							horario.getTipoHorario(), horario.getHoraIni(),
							horario.getMinutosIni(), horario.getHoraFin(), 
							horario.getMinutosFin(), horario.getDomingo(), 
							horario.getLunes(), horario.getMartes(), 
							horario.getMiercoles(), horario.getJueves(), 
							horario.getViernes(), horario.getSabado(), 
							horario.getEliminado());
					newHorarios.save();
				}
			}
		}
    }
    
    //TAREA ASINCRONA PARA BUSCAR TODOS LOS HORARIOS DE UN CUIDADOR
    public class BuscarAllHorarios extends AsyncTask<String, Integer, Boolean>{

    	private TblHorarios horario= new TblHorarios();
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			String idC=params[0];
			HttpClient httpClient= new DefaultHttpClient();
			HttpGet BT= new HttpGet (
					"http://"+ip+"/ADP/Horarios/HorariosBuscarXCuidador/"+idC);
			BT.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BT);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONArray respJSON = new JSONArray(respStr);
				
				for (int i = 0; i <respJSON.length(); i++) {
					JSONObject obj=respJSON.getJSONObject(i);
					
					horario = new TblHorarios();
					horario.setIdHorario(obj.getLong("IdHorario"));
					horario.setIdCuidador(obj.getLong("IdCuidador"));
					horario.setTipoHorario(obj.getString("TipoHorario"));
					horario.setHoraIni(obj.getInt("HoraIni"));
					horario.setMinutosIni(obj.getInt("MinutosIni"));
					horario.setHoraFin(obj.getInt("HoraFin"));
					horario.setMinutosFin(obj.getInt("MinutosFin"));
					horario.setDomingo(obj.getBoolean("Domingo"));
					horario.setLunes(obj.getBoolean("Lunes"));
					horario.setMartes(obj.getBoolean("Martes"));
					horario.setMiercoles(obj.getBoolean("Miercoles"));
					horario.setJueves(obj.getBoolean("Jueves"));
					horario.setViernes(obj.getBoolean("Viernes"));
					horario.setSabado(obj.getBoolean("Sabado"));
					horario.setEliminado(obj.getBoolean("Eliminado"));
					
					TblHorarios guardar_Horario = new TblHorarios(
							horario.getIdHorario(), horario.getIdCuidador(),
							horario.getTipoHorario(), horario.getHoraIni(),
							horario.getMinutosIni(), horario.getHoraFin(), 
							horario.getMinutosFin(), horario.getDomingo(), 
							horario.getLunes(), horario.getMartes(), 
							horario.getMiercoles(), horario.getJueves(), 
							horario.getViernes(), horario.getSabado(), 
							horario.getEliminado());
					guardar_Horario.save();
					
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
    
    //TAREA ASINCRONO PARA ELIMINAR UN HORARIO
    public class EliminarHorario extends AsyncTask<String, Integer, Boolean>{

    	private String id="";
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			HttpClient httpClient= new DefaultHttpClient();
			id=params[0];
			HttpDelete del= new HttpDelete (
					"http://"+ip+"/ADP/Horarios/HorarioEliminar/"+id);
			del.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(del);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				if (!respStr.equals("true")) {
					resul=false;
				}else{
					TblHorarios horarios = new TblHorarios();
					horarios.EliminarPorIdHorarioRegTblHorarios(Long.parseLong(id));
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
    //TAREA ASINCRONA PARA BUSCAR TODOS LOS HORARIOS DE UN CUIDADOR
    public class BuscarAllHorariosXCuidadores extends AsyncTask<String, Integer, Boolean>{

    	private TblHorarios horario= new TblHorarios();
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			String idC=params[0];
			HttpClient httpClient= new DefaultHttpClient();
			HttpGet BT= new HttpGet (
					"http://"+ip+"/ADP/Horarios/HorariosBuscarXCuidadores/"+idC);
			BT.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BT);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONArray respJSON = new JSONArray(respStr);
				
				for (int i = 0; i <respJSON.length(); i++) {
					JSONObject obj=respJSON.getJSONObject(i);
					
					horario = new TblHorarios();
					horario.setIdHorario(obj.getLong("IdHorario"));
					horario.setIdCuidador(obj.getLong("IdCuidador"));
					horario.setTipoHorario(obj.getString("TipoHorario"));
					horario.setHoraIni(obj.getInt("HoraIni"));
					horario.setMinutosIni(obj.getInt("MinutosIni"));
					horario.setHoraFin(obj.getInt("HoraFin"));
					horario.setMinutosFin(obj.getInt("MinutosFin"));
					horario.setDomingo(obj.getBoolean("Domingo"));
					horario.setLunes(obj.getBoolean("Lunes"));
					horario.setMartes(obj.getBoolean("Martes"));
					horario.setMiercoles(obj.getBoolean("Miercoles"));
					horario.setJueves(obj.getBoolean("Jueves"));
					horario.setViernes(obj.getBoolean("Viernes"));
					horario.setSabado(obj.getBoolean("Sabado"));
					horario.setEliminado(obj.getBoolean("Eliminado"));
					
					TblHorarios guardar_Horario = new TblHorarios(
							horario.getIdHorario(), horario.getIdCuidador(),
							horario.getTipoHorario(), horario.getHoraIni(),
							horario.getMinutosIni(), horario.getHoraFin(), 
							horario.getMinutosFin(), horario.getDomingo(), 
							horario.getLunes(), horario.getMartes(), 
							horario.getMiercoles(), horario.getJueves(), 
							horario.getViernes(), horario.getSabado(), 
							horario.getEliminado());
					guardar_Horario.save();
					
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

	public JsonArrayRequest AB_Horarios(long idC, final String TAG){
		String urlJsonArray = "http://"+ip+"/ADP/Horarios/HorarioBuscarXCuidadores/"+String.valueOf(idC);
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

								TblHorarios horario = new TblHorarios();
								horario.setIdHorario(obj.getLong("IdHorario"));
								horario.setIdCuidador(obj.getLong("IdCuidador"));
								horario.setTipoHorario(obj.getString("TipoHorario"));
								horario.setHoraIni(obj.getInt("HoraIni"));
								horario.setMinutosIni(obj.getInt("MinutosIni"));
								horario.setHoraFin(obj.getInt("HoraFin"));
								horario.setMinutosFin(obj.getInt("MinutosFin"));
								horario.setDomingo(obj.getBoolean("Domingo"));
								horario.setLunes(obj.getBoolean("Lunes"));
								horario.setMartes(obj.getBoolean("Martes"));
								horario.setMiercoles(obj.getBoolean("Miercoles"));
								horario.setJueves(obj.getBoolean("Jueves"));
								horario.setViernes(obj.getBoolean("Viernes"));
								horario.setSabado(obj.getBoolean("Sabado"));
								horario.setEliminado(obj.getBoolean("Eliminado"));
								horario.save();

								Log.e("Horario => Registro", "#" + i + "= " + horario);
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