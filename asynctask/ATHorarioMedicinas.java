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
import com.Notifications.patientssassistant.tables.TblHorarioMedicina;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.orm.query.Condition;
import com.orm.query.Select;


public class ATHorarioMedicinas {

	//VARIABLES DE CLASE ATHorariosMedicinas 
	//private static String ip="192.168.1.4:1522";
	private static String ip=VarEstatic.ObtenerIP();
	
	//TAREA ASINCRONA PARA INSERTAR UN HORARIO DE MEDICINAS
	public class InsertarHorarioMedicinas extends AsyncTask<String, Integer, Long>
    {
    	private TblHorarioMedicina horario;
		public long id=0;
    	
		@Override
		protected Long doInBackground(String... params) {
			// TODO Auto-generated method stub
			long respuesta=0;
			
			HttpClient httpClient=new DefaultHttpClient();
			
			HttpPost post=new HttpPost(
					"http://"+ip+"/ADP/HorarioMedicinas/HorarioMedicinaInsertar");
			post.setHeader("content-type", "application/json");
			
			try {
				JSONObject dato=new JSONObject();
							
				dato.put("IdHorarioMedicina", Long.parseLong(params[0]));
				dato.put("IdControlMedicina", Long.parseLong(params[1]));
				dato.put("Hora", Integer.parseInt(params[2]));
				dato.put("Minutos", Integer.parseInt(params[3]));
				dato.put("Domingo", Boolean.parseBoolean(params[4]));
				dato.put("Lunes",Boolean.parseBoolean(params[5]));
				dato.put("Martes", Boolean.parseBoolean(params[6]));
				dato.put("Miercoles", Boolean.parseBoolean(params[7]));
				dato.put("Jueves", Boolean.parseBoolean(params[8]));
				dato.put("Viernes", Boolean.parseBoolean(params[9]));
				dato.put("Sabado", Boolean.parseBoolean(params[10]));
				dato.put("ActDesAlarma",Boolean.parseBoolean(params[11]));
				dato.put("Eliminado",Boolean.parseBoolean(params[12]));
				
				StringEntity entity = new StringEntity(dato.toString());
				post.setEntity(entity);
				
				HttpResponse resp=httpClient.execute(post);
				String respStr = EntityUtils.toString(resp.getEntity());
				respuesta=Long.parseLong(respStr);
				
				if (respuesta>0){
					horario = new TblHorarioMedicina(
								respuesta, Long.parseLong(params[1]), 
								Integer.parseInt(params[2]),
								Integer.parseInt(params[3]),
								Boolean.parseBoolean(params[4]),
								Boolean.parseBoolean(params[5]),
								Boolean.parseBoolean(params[6]),
								Boolean.parseBoolean(params[7]),
								Boolean.parseBoolean(params[8]),
								Boolean.parseBoolean(params[9]),
								Boolean.parseBoolean(params[10]),
								Boolean.parseBoolean(params[11]),
								Boolean.parseBoolean(params[12]));
					horario.save();
				} 
				
			} catch (Exception ex) {
				Log.e("ServicioRest", "Error!", ex);
			}
			return respuesta;
		}
		
		@Override
		protected void onPostExecute(Long resul ) {
		}
		
    }  
    	
	//TAREA ASINCRONA PARA ACTUALIZAR UN HORARIO DE MEDICINA DE UN PACIENTE
    public class ActualizarHorarioMedicina extends AsyncTask<String, Integer, Boolean>
    {
    	private TblHorarioMedicina horario;
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			Boolean resul=true;
			HttpClient httpClient=new DefaultHttpClient();
			
			HttpPut put=new HttpPut(
					"http://"+ip+"/ADP/HorarioMedicinas/HorarioMedicinaActualizar");
			put.setHeader("content-type", "application/json");
			
			try {
				JSONObject dato=new JSONObject();
				
				dato.put("IdHorarioMedicina", Long.parseLong(params[0]));
				dato.put("IdControlMedicina", Long.parseLong(params[1]));
				dato.put("Hora", Integer.parseInt(params[2]));
				dato.put("Minutos", Integer.parseInt(params[3]));
				dato.put("Domingo", Boolean.parseBoolean(params[4]));
				dato.put("Lunes",Boolean.parseBoolean(params[5]));
				dato.put("Martes", Boolean.parseBoolean(params[6]));
				dato.put("Miercoles", Boolean.parseBoolean(params[7]));
				dato.put("Jueves", Boolean.parseBoolean(params[8]));
				dato.put("Viernes", Boolean.parseBoolean(params[9]));
				dato.put("Sabado", Boolean.parseBoolean(params[10]));
				dato.put("ActDesAlarma",Boolean.parseBoolean(params[11]));
				dato.put("Eliminado",Boolean.parseBoolean(params[12]));
				
				StringEntity entity = new StringEntity(dato.toString());
				put.setEntity(entity);
				
				HttpResponse resp=httpClient.execute(put);
				String respStr = EntityUtils.toString(resp.getEntity());
				
				if (!respStr.equals("true")) {
					resul=false;
				}else{
					Long campo_ide=Long.parseLong(params[0]);
					Select elHorario = Select.from(TblHorarioMedicina.class)
							.where(Condition.prop("ID_HORARIO_MEDICINA").eq(campo_ide));
					TblHorarioMedicina edit_Horario=(TblHorarioMedicina)elHorario.first();

					if (edit_Horario!=null) {
						edit_Horario.setIdHorarioMedicina(Long.parseLong(params[0]));
						edit_Horario.setIdControlMedicina(Long.parseLong(params[1]));
						edit_Horario.setHora(Integer.parseInt(params[2]));
						edit_Horario.setMinutos(Integer.parseInt(params[3]));
						edit_Horario.setDomingo(Boolean.parseBoolean(params[4]));
						edit_Horario.setLunes(Boolean.parseBoolean(params[5]));
						edit_Horario.setMartes(Boolean.parseBoolean(params[6]));
						edit_Horario.setMiercoles(Boolean.parseBoolean(params[7]));
						edit_Horario.setJueves(Boolean.parseBoolean(params[8]));
						edit_Horario.setViernes(Boolean.parseBoolean(params[9]));
						edit_Horario.setSabado(Boolean.parseBoolean(params[10]));
						edit_Horario.setActDesAlarma(Boolean.parseBoolean(params[11]));
						edit_Horario.setEliminado(Boolean.parseBoolean(params[12]));
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
     
    //TAREA ASINCRONA PARA BUSCAR UN HORARIO DE MEDICINA DE UN PACIENTE
    public class BuscarUnHorarioMedicina extends AsyncTask<String, Integer, Boolean>{

		private TblHorarioMedicina horario= new TblHorarioMedicina();
    	
    	@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			HttpClient httpClient= new DefaultHttpClient();
			String id=params[0];
			HttpGet BO= new HttpGet (
					"http://"+ip+"/ADP/HorarioMedicinas/HorarioMedicinaBuscar/"+id);
			BO.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BO);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONObject respJSON = new JSONObject(respStr);
				
				horario.setIdHorarioMedicina(respJSON.getLong("IdHorarioMedicina"));
				horario.setIdControlMedicina(respJSON.getLong("IdControlMedicina"));
				horario.setHora(respJSON.getInt("Hora"));
				horario.setMinutos(respJSON.getInt("Minutos"));
				horario.setDomingo(respJSON.getBoolean("Domingo"));
				horario.setLunes(respJSON.getBoolean("Lunes"));
				horario.setMartes(respJSON.getBoolean("Martes"));
				horario.setMiercoles(respJSON.getBoolean("Miercoles"));
				horario.setJueves(respJSON.getBoolean("Jueves"));
				horario.setViernes(respJSON.getBoolean("Viernes"));
				horario.setSabado(respJSON.getBoolean("Sabado"));
				horario.setActDesAlarma(respJSON.getBoolean("ActDesAlarma"));
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
				String campo_ide=String.valueOf(horario.getIdHorarioMedicina());
				Select elHorario = Select.from(TblHorarioMedicina.class)
						.where(Condition.prop("ID_HORARIO_MEDICINA").eq(campo_ide));
		    	TblHorarioMedicina edit_Horario=(TblHorarioMedicina)elHorario.first();		
				
		    	if (edit_Horario!=null) {
					edit_Horario.setIdHorarioMedicina(horario.getIdHorarioMedicina());
					edit_Horario.setIdControlMedicina(horario.getIdControlMedicina());
					edit_Horario.setHora(horario.getHora());
					edit_Horario.setMinutos(horario.getMinutos());
					edit_Horario.setDomingo(horario.getDomingo());
					edit_Horario.setLunes(horario.getLunes());
					edit_Horario.setMartes(horario.getMartes());
					edit_Horario.setMiercoles(horario.getMiercoles());
					edit_Horario.setJueves(horario.getJueves());
					edit_Horario.setViernes(horario.getViernes());
					edit_Horario.setSabado(horario.getSabado());
					edit_Horario.setActDesAlarma(horario.getActDesAlarma());
					edit_Horario.setEliminado(horario.getEliminado());
					edit_Horario.save();
				}
		    	else{
		    		TblHorarioMedicina horaMed= new TblHorarioMedicina(
		    				horario.getIdHorarioMedicina(), horario.getIdControlMedicina(),
		    				horario.getHora(), horario.getMinutos(),
		    				horario.getDomingo(), horario.getLunes(),
		    				horario.getMartes(), horario.getMiercoles(),
		    				horario.getJueves(), horario.getViernes(),
		    				horario.getSabado(), horario.getActDesAlarma(),
		    				horario.getEliminado());
		    		horaMed.save();
		    	}
			}
		}
    }
       
    //TAREA ASINCRONA PARA BUSCAR TODOS LOS HORARIOS DE MEDICINAS DE UN PACIENTE
    public class BuscarAllHorarioMedicinaXControl extends AsyncTask<String, Integer, Boolean>{

    	private TblHorarioMedicina horario= new TblHorarioMedicina();
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			String idCM=params[0];
			HttpClient httpClient= new DefaultHttpClient();
			HttpGet BT= new HttpGet (
					"http://"+ip+"/ADP/HorarioMedicinas/HorariosMedicinasBuscarXControl/"+idCM);
			BT.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BT);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONArray respJSON = new JSONArray(respStr);
				
				for (int i = 0; i <respJSON.length(); i++) {
					JSONObject obj=respJSON.getJSONObject(i);
					
					
					horario = new TblHorarioMedicina();
					horario.setIdHorarioMedicina(obj.getLong("IdHorarioMedicina"));
					horario.setIdControlMedicina(obj.getLong("IdControlMedicina"));
					horario.setHora(obj.getInt("Hora"));
					horario.setMinutos(obj.getInt("Minutos"));
					horario.setDomingo(obj.getBoolean("Domingo"));
					horario.setLunes(obj.getBoolean("Lunes"));
					horario.setMartes(obj.getBoolean("Martes"));
					horario.setMiercoles(obj.getBoolean("Miercoles"));
					horario.setJueves(obj.getBoolean("Jueves"));
					horario.setViernes(obj.getBoolean("Viernes"));
					horario.setSabado(obj.getBoolean("Sabado"));
					horario.setActDesAlarma(obj.getBoolean("ActDesAlarma"));
					horario.setEliminado(obj.getBoolean("Eliminado"));
					
					TblHorarioMedicina guardar_Horario = new TblHorarioMedicina(
							horario.getIdHorarioMedicina(), horario.getIdControlMedicina(),
		    				horario.getHora(), horario.getMinutos(),
		    				horario.getDomingo(), horario.getLunes(),
		    				horario.getMartes(), horario.getMiercoles(),
		    				horario.getJueves(), horario.getViernes(),
		    				horario.getSabado(), horario.getActDesAlarma(),
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
      
    //TAREA ASINCRONA PARA ELIMINAR UN HORARIO DE MEDICINA DE UN PACIENTE
    public class EliminarHorarioMedicina extends AsyncTask<String, Integer, Boolean>{

    	private String id="";
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			HttpClient httpClient= new DefaultHttpClient();
			id=params[0];
			HttpDelete del= new HttpDelete (
					"http://"+ip+"/ADP/HorarioMedicinas/HorarioMedicinaEliminar/"+id);
			del.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(del);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				if (!respStr.equals("true")) {
					resul=false;
				}else{
					TblHorarioMedicina horaMed = new TblHorarioMedicina();
					horaMed.EliminarPorIdHorarioMedicinaRegTblHorarioMedicina(Long.parseLong(id));
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
    //TAREA ASINCRONA PARA BUSCAR TODOS LOS HORARIOS DE MEDICINAS DE UN PACIENTE
    public class BuscarAllHorarioMedicinaXCuidadores extends AsyncTask<String, Integer, Boolean>{

    	private TblHorarioMedicina horarioMed= new TblHorarioMedicina();
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			String idCM=params[0];
			HttpClient httpClient= new DefaultHttpClient();
			HttpGet BT= new HttpGet (
					"http://"+ip+"/ADP/HorarioMedicinas/HorariosMedicinasBuscarXCuidadores/"+idCM);
			BT.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BT);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONArray respJSON = new JSONArray(respStr);
				
				for (int i = 0; i <respJSON.length(); i++) {
					JSONObject obj=respJSON.getJSONObject(i);
					
					horarioMed = new TblHorarioMedicina();
					horarioMed.setIdHorarioMedicina(obj.getLong("IdHorarioMedicina"));
					horarioMed.setIdControlMedicina(obj.getLong("IdControlMedicina"));
					horarioMed.setHora(obj.getInt("Hora"));
					horarioMed.setMinutos(obj.getInt("Minutos"));
					horarioMed.setDomingo(obj.getBoolean("Domingo"));
					horarioMed.setLunes(obj.getBoolean("Lunes"));
					horarioMed.setMartes(obj.getBoolean("Martes"));
					horarioMed.setMiercoles(obj.getBoolean("Miercoles"));
					horarioMed.setJueves(obj.getBoolean("Jueves"));
					horarioMed.setViernes(obj.getBoolean("Viernes"));
					horarioMed.setSabado(obj.getBoolean("Sabado"));
					horarioMed.setActDesAlarma(obj.getBoolean("ActDesAlarma"));
					horarioMed.setEliminado(obj.getBoolean("Eliminado"));
					
					TblHorarioMedicina guardar_Horario = new TblHorarioMedicina(
							horarioMed.getIdHorarioMedicina(), horarioMed.getIdControlMedicina(),
		    				horarioMed.getHora(), horarioMed.getMinutos(),
		    				horarioMed.getDomingo(), horarioMed.getLunes(),
		    				horarioMed.getMartes(), horarioMed.getMiercoles(),
		    				horarioMed.getJueves(), horarioMed.getViernes(),
		    				horarioMed.getSabado(), horarioMed.getActDesAlarma(),
		    				horarioMed.getEliminado());
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

	public JsonArrayRequest AB_HorarioMedicinas(long idC, final String TAG){
		String urlJsonArray = "http://"+ip+"/ADP/HorarioMedicinas/HorarioMedicinaBuscarXCuidadores/"+String.valueOf(idC);
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

								TblHorarioMedicina horarioMed = new TblHorarioMedicina();
								horarioMed.setIdHorarioMedicina(obj.getLong("IdHorarioMedicina"));
								horarioMed.setIdControlMedicina(obj.getLong("IdControlMedicina"));
								horarioMed.setHora(obj.getInt("Hora"));
								horarioMed.setMinutos(obj.getInt("Minutos"));
								horarioMed.setDomingo(obj.getBoolean("Domingo"));
								horarioMed.setLunes(obj.getBoolean("Lunes"));
								horarioMed.setMartes(obj.getBoolean("Martes"));
								horarioMed.setMiercoles(obj.getBoolean("Miercoles"));
								horarioMed.setJueves(obj.getBoolean("Jueves"));
								horarioMed.setViernes(obj.getBoolean("Viernes"));
								horarioMed.setSabado(obj.getBoolean("Sabado"));
								horarioMed.setActDesAlarma(obj.getBoolean("ActDesAlarma"));
								horarioMed.setEliminado(obj.getBoolean("Eliminado"));
								horarioMed.save();

								Log.e("Buzon => Registro", "#" + i + "= " + horarioMed);
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
