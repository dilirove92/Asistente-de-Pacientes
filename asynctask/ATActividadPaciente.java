package com.Notifications.patientssassistant.asynctask;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.Notifications.patientssassistant.VarEstatic;
import com.Notifications.patientssassistant.tables.TblActividadCuidador;
import com.Notifications.patientssassistant.tables.TblActividadPaciente;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.orm.query.Condition;
import com.orm.query.Select;

import android.os.AsyncTask;
import android.util.Log;

public class ATActividadPaciente {
	
	    //VARIABLES DE CLASE ATActividadPaciente 
		//public static String ip="192.168.1.4:1522";
		private static String ip=VarEstatic.ObtenerIP();
	
		//TAREA ASINCRONA PARA INSERTAR UNA ACTIVIDAD PACIENTE
		public class InsertarActividadPacientes extends AsyncTask<String, Integer, Boolean>
	    {
	    	
			@Override
			protected Boolean doInBackground(String... params) {
				// TODO Auto-generated method stub
				Boolean resul=false;
				HttpClient httpClient=new DefaultHttpClient();
				
				HttpPost post=new HttpPost(
						"http://"+ip+"/ADP/ActividadPaciente/ActividadPacienteInsertar");
				post.setHeader("content-type", "application/json");
				
				try {
					JSONObject dato=new JSONObject();
					
					dato.put("IdPaciente", Long.parseLong(params[0]));
					dato.put("IdActividad", Long.parseLong(params[1]));
					dato.put("Eliminado", Boolean.parseBoolean(params[2]));
					
					StringEntity entity = new StringEntity(dato.toString());
					post.setEntity(entity);
					
					HttpResponse resp=httpClient.execute(post);
					String respStr = EntityUtils.toString(resp.getEntity());
					
					if (respStr.equals("true")) {
						
						TblActividadPaciente guardar_actPac = new TblActividadPaciente(
								Long.parseLong(params[0]), Long.parseLong(params[1]), Boolean.parseBoolean(params[2]));
						guardar_actPac.save();
						
						resul=true;
					}
					
				} catch (Exception ex) {
					Log.e("ServicioRest", "Error!", ex);
					resul = false;
				}
				return resul;
			}
			
			@Override
			protected void onPostExecute(Boolean resul ) {
				// TODO Auto-generated method stub
			}
			
	    }  
			
		//TAREA ASINCRONA PARA ELIMINAR UNA ACTIVIDAD PACIENTE
		public class ActualizarActividadPacientes extends AsyncTask<String, Integer, Boolean>
	    {

			@Override
			protected Boolean doInBackground(String... params) {
				// TODO Auto-generated method stub
				Boolean resul=false;
				HttpClient httpClient=new DefaultHttpClient();
				
				HttpPut put=new HttpPut(
						"http://"+ip+"/ADP/ActividadPaciente/ActividadPacienteEliminar");
				put.setHeader("content-type", "application/json");
				
				try {
					JSONObject dato=new JSONObject();
					
					dato.put("IdPaciente", Long.parseLong(params[0]));
					dato.put("IdActividad", Long.parseLong(params[1]));
					dato.put("Eliminado", Boolean.parseBoolean(params[2]));
					
					StringEntity entity = new StringEntity(dato.toString());
					put.setEntity(entity);
					
					HttpResponse resp=httpClient.execute(put);
					String respStr = EntityUtils.toString(resp.getEntity());
					
					if (respStr.equals("true")) {
						Select actPaciente = Select.from(TblActividadPaciente.class)
								.where(Condition.prop("ID_PACIENTE").eq(params[0]), 
										Condition.prop("ID_ACTIVIDAD").eq(params[1]));
						TblActividadPaciente edit_Actividad=(TblActividadPaciente)actPaciente.first();
						if (edit_Actividad!=null) {
							edit_Actividad.delete();
						}
						resul=true;
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
			}
			
	    }
		
	    //TAREA ASINCRONA PARA BUSCAR ACTIVIDADES PACIENTES
	    public class BuscarAllPacientes extends AsyncTask<String, Integer, Boolean>{

			@Override
			protected Boolean doInBackground(String... params) {
				// TODO Auto-generated method stub
				boolean resul=true;
				String id=params[0];
				HttpClient httpClient= new DefaultHttpClient();
				HttpGet BT= new HttpGet (
						"http://"+ip+"/ADP/ActividadPaciente/ActividadPacientesBuscarAllXCuidador/"+id);
				BT.setHeader("content-type", "application/json");
				
				try {
					HttpResponse resp=httpClient.execute(BT);
					String respStr=EntityUtils.toString(resp.getEntity());
					
					JSONArray respJSON = new JSONArray(respStr);
					
					
						for (int i = 0; i < respJSON.length(); i++) {
							JSONObject obj = respJSON.getJSONObject(i);

							TblActividadPaciente actPac = new TblActividadPaciente();
							actPac.setIdPaciente(obj.getLong("IdPaciente"));
							actPac.setIdActividad(obj.getLong("IdActividad"));
							actPac.setEliminado(obj.getBoolean("Eliminado"));

							TblActividadPaciente guardar_actPac = new TblActividadPaciente(
									actPac.getIdPaciente(),
									actPac.getIdActividad(),
									actPac.getEliminado());
							guardar_actPac.save();
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
	    
	    //(ACTUALIZAR TABLA ACTIVIDADES PACIENTES)
		//TAREA ASINCRONA PARA BUSCAR ACTIVIDADES DE TODOS LOS CUIDADORES
	    public class BuscarAllActividadCuidadores extends AsyncTask<String, Integer, Boolean>{

			@Override
			protected Boolean doInBackground(String... params) {
				// TODO Auto-generated method stub
				boolean resul=true;
				String id=params[0];
				HttpClient httpClient= new DefaultHttpClient();
				HttpGet BT= new HttpGet (
						"http://"+ip+"/ADP/ActividadPaciente/ActividadPacientesBuscarAllXCuidadores/"+id);
				BT.setHeader("content-type", "application/json");
				TblActividadPaciente actPac;
				
				try {
					HttpResponse resp=httpClient.execute(BT);
					String respStr=EntityUtils.toString(resp.getEntity());
					
					JSONArray respJSON = new JSONArray(respStr);
					
					
						for (int i = 0; i <respJSON.length(); i++) {
							JSONObject obj=respJSON.getJSONObject(i);
							
							actPac= new TblActividadPaciente();
							actPac.setIdPaciente(obj.getLong("IdPaciente"));
							actPac.setIdActividad(obj.getLong("IdActividad"));
							actPac.setEliminado(obj.getBoolean("Eliminado"));
							
							TblActividadPaciente guardar_actPac = new TblActividadPaciente(
									actPac.getIdPaciente(), actPac.getIdActividad(), actPac.getEliminado());
							guardar_actPac.save();
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
	    
	  //TAREA ASINCRONA PARA BUSCAR ACTIVIDADES DE UN PACIENTE
	  public class BuscarAllActividadXPaciente extends AsyncTask<String, Integer, List<TblActividadPaciente>>{

		  List<TblActividadPaciente> listActPac = new ArrayList<TblActividadPaciente>();

		  @Override
		  protected List<TblActividadPaciente> doInBackground(String... params) {
			  // TODO Auto-generated method stub
			  boolean resul=true;
			  String id=params[0];
			  HttpClient httpClient= new DefaultHttpClient();
			  HttpGet BT= new HttpGet (
					  "http://"+ip+"/ADP/ActividadPaciente/ActividadPacientes/"+id);
			  BT.setHeader("content-type", "application/json");
			  TblActividadPaciente actPac;

			  try {
				  HttpResponse resp=httpClient.execute(BT);
				  String respStr=EntityUtils.toString(resp.getEntity());

				  JSONArray respJSON = new JSONArray(respStr);


				  for (int i = 0; i <respJSON.length(); i++) {
					  JSONObject obj=respJSON.getJSONObject(i);

					  actPac= new TblActividadPaciente();
					  actPac.setIdPaciente(obj.getLong("IdPaciente"));
					  actPac.setIdActividad(obj.getLong("IdActividad"));
					  actPac.setEliminado(obj.getBoolean("Eliminado"));
					  listActPac.add(actPac);
					  TblActividadPaciente guardar_actPac = new TblActividadPaciente(
							  actPac.getIdPaciente(), actPac.getIdActividad(), actPac.getEliminado());
					  guardar_actPac.save();

				  }


			  } catch (Exception ex) {
				  // TODO: handle exception
				  Log.e("Serviciorest","Error!",ex);
				  resul=false;
			  }

			  return listActPac;
		  }

		  @Override
		  protected void onPostExecute(List<TblActividadPaciente> result) {
			  // TODO Auto-generated method stub
	    		/*for (int i = 0; i <listActPac.size(); i++) {
					
	    			TblActividadPaciente actPac= new TblActividadPaciente();
					actPac=listActPac.get(i);
					TblActividadPaciente guardar_actPac = new TblActividadPaciente(
							actPac.getIdPaciente(), actPac.getIdActividad(), actPac.getEliminado());
					guardar_actPac.save();
					
					//ATActividades actividad = new ATActividades();
					//actividad.new BuscarUnaActividad().execute(String.valueOf(actPac.getIdActividad()));
				}*/
		  }

	  }

	public JsonArrayRequest AB_ActividadesPaciente(long idC, final String TAG){
		String urlJsonArray = "http://"+ip+"/ADP/ActividadPaciente/ActividadPacienteBuscarAllXCuidadores/"+String.valueOf(idC);
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
								TblActividadPaciente actividad= new TblActividadPaciente();
								actividad.setIdPaciente(obj.getLong("IdPaciente"));
								actividad.setIdActividad(obj.getLong("IdActividad"));
								actividad.setEliminado(obj.getBoolean("Eliminado"));
								actividad.save();

								jsonResponse1 += "IdPaciente: " + actividad.getIdPaciente() + "\n";
								jsonResponse1 += "IdActividad: " + actividad.getIdActividad() + "\n";
								jsonResponse1 += "Eliminado: " + actividad.getEliminado() + "\n\n";
								Log.e("Act_Pac => Registro", "#" + i + "= " + jsonResponse1);
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
