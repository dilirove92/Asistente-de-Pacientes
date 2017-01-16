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
import com.Notifications.patientssassistant.tables.TblSeguimientoMedico;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.orm.query.Condition;
import com.orm.query.Select;


public class ATSeguimientoMedico {

	//VARIABLES DE CLASE ATSeguimientoMedico 
	//private static String ip="192.168.1.4:1522";
	private static String ip=VarEstatic.ObtenerIP();
	
	//TAREA ASINCRONA PARA INSERTAR UN REGISTRO DE SEGUIMIENTO MEDICO
	public class InsertarSeguimientoMedico extends AsyncTask<String, Integer, Long>
    {
		private TblSeguimientoMedico seguiMedico;
		
		@Override
		protected Long doInBackground(String... params) {
			// TODO Auto-generated method stub
			Boolean resul=true;
			long respuesta=0;
			HttpClient httpClient=new DefaultHttpClient();
			
			HttpPost post=new HttpPost(
					"http://"+ip+"/ADP/SeguimientoMedico/SeguimientoMedicoInsertar");
			post.setHeader("content-type", "application/json");
			
			try {
				//Construimos el objeto alumno en formato JSON
				JSONObject dato=new JSONObject();
				
				dato.put("IdSeguimientoMedico", Long.parseLong(params[0]));
				dato.put("IdPaciente", Long.parseLong(params[1]));
				dato.put("Anio", Integer.parseInt(params[2]));
				dato.put("Mes", Integer.parseInt(params[3]));
				dato.put("Dia", Integer.parseInt(params[4]));
				dato.put("UnidadMedica",params[5]);
				dato.put("Doctor",params[6]);
				dato.put("Diagnostico",params[7]);
				dato.put("TratamientoMedico",params[8]);
				dato.put("AlimentacionSugerida",params[9]);
				dato.put("Eliminado",Boolean.parseBoolean(params[10]));
				
				StringEntity entity = new StringEntity(dato.toString());
				post.setEntity(entity);
				
				HttpResponse resp=httpClient.execute(post);
				String respStr = EntityUtils.toString(resp.getEntity());
				respuesta=Long.parseLong(respStr);
				
				if (respuesta>0){
					seguiMedico = new TblSeguimientoMedico(
									respuesta, Long.parseLong(params[1]),
									Integer.parseInt(params[2]),
									Integer.parseInt(params[3]),
									Integer.parseInt(params[4]),
									params[5], params[6], params[7],
									params[8], params[9], 
									Boolean.parseBoolean(params[10]));
					seguiMedico.save();
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
    
    public class ActualizarSeguimientoMedico extends AsyncTask<String, Integer, Boolean>
    {
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			Boolean resul=true;
			HttpClient httpClient=new DefaultHttpClient();
			
			HttpPut put=new HttpPut(
					"http://"+ip+"/ADP/SeguimientoMedico/SeguimientoMedicoActualizar");
			put.setHeader("content-type", "application/json");
			
			try {
				//Construimos el objeto alumno en formato JSON
				JSONObject dato=new JSONObject();
				
				dato.put("IdSeguimientoMedico", Long.parseLong(params[0]));
				dato.put("IdPaciente", Long.parseLong(params[1]));
				dato.put("Anio", Integer.parseInt(params[2]));
				dato.put("Mes", Integer.parseInt(params[3]));
				dato.put("Dia", Integer.parseInt(params[4]));
				dato.put("UnidadMedica",params[5]);
				dato.put("Doctor",params[6]);
				dato.put("Diagnostico",params[7]);
				dato.put("TratamientoMedico",params[8]);
				dato.put("AlimentacionSugerida",params[9]);
				dato.put("Eliminado",Boolean.parseBoolean(params[10]));
				
				StringEntity entity = new StringEntity(dato.toString());
				put.setEntity(entity);
				
				HttpResponse resp=httpClient.execute(put);
				String respStr = EntityUtils.toString(resp.getEntity());
				
				if (!respStr.equals("true")) {
					resul=false;
				}else{
					String campo_ide=String.valueOf(Long.parseLong(params[0]));
					Select segMedico = Select.from(TblSeguimientoMedico.class)
							.where(Condition.prop("ID_SEGUIMIENTO_MEDICO").eq(campo_ide));
					TblSeguimientoMedico editar_SM=(TblSeguimientoMedico)segMedico.first();

					if (editar_SM!=null) {
						editar_SM.setIdSeguimientoMedico(Long.parseLong(params[0]));
						editar_SM.setIdPaciente(Long.parseLong(params[1]));
						editar_SM.setAnio(Integer.parseInt(params[2]));
						editar_SM.setMes(Integer.parseInt(params[3]));
						editar_SM.setDia(Integer.parseInt(params[4]));
						editar_SM.setUnidadMedica(params[5]);
						editar_SM.setDoctor(params[6]);
						editar_SM.setDiagnostico(params[7]);
						editar_SM.setTratamientoMedico(params[8]);
						editar_SM.setAlimentacionSugerida(params[9]);
						editar_SM.setEliminado(Boolean.parseBoolean(params[10]));
						editar_SM.save();
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
      
    //TAREA ASINCRONA PARA BUSCAR UN REGISTRO DE SEGUIMIENTO MEDICO DE PACIENTES
    public class BuscarSeguimientoMedico extends AsyncTask<String, Integer, Boolean>{

		private TblSeguimientoMedico seguiMedico= new TblSeguimientoMedico();
    	
    	@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			HttpClient httpClient= new DefaultHttpClient();
			String id=params[0];
			HttpGet BO= new HttpGet (
					"http://"+ip+"/ADP/SeguimientoMedico/SeguimientoMedicoBuscar/"+id);
			BO.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BO);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONObject respJSON = new JSONObject(respStr);
				
				seguiMedico.setIdSeguimientoMedico(respJSON.getLong("IdSeguimientoMedico"));
				seguiMedico.setIdPaciente(respJSON.getLong("IdPaciente"));
				seguiMedico.setAnio(respJSON.getInt("Anio"));
				seguiMedico.setMes(respJSON.getInt("Mes"));
				seguiMedico.setDia(respJSON.getInt("Dia"));
				seguiMedico.setUnidadMedica(respJSON.getString("UnidadMedica"));
				seguiMedico.setDoctor(respJSON.getString("Doctor"));
				seguiMedico.setDiagnostico(respJSON.getString("Diagnostico"));
				seguiMedico.setTratamientoMedico(respJSON.getString("TratamientoMedico"));
				seguiMedico.setAlimentacionSugerida(respJSON.getString("AlimentacionSugerida"));
				seguiMedico.setEliminado(respJSON.getBoolean("Eliminado"));
				
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
				String campo_ide=String.valueOf(seguiMedico.getIdSeguimientoMedico());
				Select segMedico = Select.from(TblSeguimientoMedico.class)
						.where(Condition.prop("ID_SEGUIMIENTO_MEDICO").eq(campo_ide));
		    	TblSeguimientoMedico editar_SM=(TblSeguimientoMedico)segMedico.first();	
		    	
				if (editar_SM!=null) {
					editar_SM.setIdSeguimientoMedico(seguiMedico.getIdSeguimientoMedico());
					editar_SM.setIdPaciente(seguiMedico.getIdPaciente());
					editar_SM.setAnio(seguiMedico.getAnio());
					editar_SM.setMes(seguiMedico.getMes());
					editar_SM.setDia(seguiMedico.getDia());
					editar_SM.setUnidadMedica(seguiMedico.getUnidadMedica());
					editar_SM.setDoctor(seguiMedico.getDoctor());
					editar_SM.setDiagnostico(seguiMedico.getDiagnostico());
					editar_SM.setTratamientoMedico(seguiMedico.getTratamientoMedico());
					editar_SM.setAlimentacionSugerida(seguiMedico.getAlimentacionSugerida());
					editar_SM.setEliminado(seguiMedico.getEliminado());
					editar_SM.save();
				}
				else{
					TblSeguimientoMedico newSM = new TblSeguimientoMedico(
							seguiMedico.getIdSeguimientoMedico(), seguiMedico.getIdPaciente(),
							seguiMedico.getAnio(), seguiMedico.getMes(), seguiMedico.getDia(),
							seguiMedico.getUnidadMedica(), seguiMedico.getDoctor(), 
							seguiMedico.getDiagnostico(), seguiMedico.getTratamientoMedico(), 
							seguiMedico.getAlimentacionSugerida(),seguiMedico.getEliminado());
					newSM.save();
				}
			}
		}
    }
      
    //TAREA ASINCRONA PARA BUSCAR UN GRUPO DE REGISTROS DE SEGUIMIENTOS MEDICOS
    public class BuscarAllSeguimientoMedicoXPaciente extends AsyncTask<String, Integer, Boolean>{

    	private TblSeguimientoMedico seguiMedico= new TblSeguimientoMedico();
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			String idP=params[0];
			HttpClient httpClient= new DefaultHttpClient();
			HttpGet BT= new HttpGet (
					"http://"+ip+"/ADP/SeguimientoMedico/SeguimientosMedicosBuscarXPaciente/"+idP);
			BT.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BT);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONArray respJSON = new JSONArray(respStr);
				
				for (int i = 0; i <respJSON.length(); i++) {
					JSONObject obj=respJSON.getJSONObject(i);
										
					seguiMedico = new TblSeguimientoMedico();
					seguiMedico.setIdSeguimientoMedico(obj.getLong("IdSeguimientoMedico"));
					seguiMedico.setIdPaciente(obj.getLong("IdPaciente"));
					seguiMedico.setAnio(obj.getInt("Anio"));
					seguiMedico.setMes(obj.getInt("Mes"));
					seguiMedico.setDia(obj.getInt("Dia"));
					seguiMedico.setUnidadMedica(obj.getString("UnidadMedica"));
					seguiMedico.setDoctor(obj.getString("Doctor"));
					seguiMedico.setDiagnostico(obj.getString("Diagnostico"));
					seguiMedico.setTratamientoMedico(obj.getString("TratamientoMedico"));
					seguiMedico.setAlimentacionSugerida(obj.getString("AlimentacionSugerida"));
					seguiMedico.setEliminado(obj.getBoolean("Eliminado"));
					
					TblSeguimientoMedico guardar_SM = new TblSeguimientoMedico(
							seguiMedico.getIdSeguimientoMedico(), seguiMedico.getIdPaciente(),
							seguiMedico.getAnio(), seguiMedico.getMes(), seguiMedico.getDia(),
							seguiMedico.getUnidadMedica(), seguiMedico.getDoctor(), 
							seguiMedico.getDiagnostico(), seguiMedico.getTratamientoMedico(), 
							seguiMedico.getAlimentacionSugerida(),seguiMedico.getEliminado());
					guardar_SM.save();
					
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
     
    //TAREA ASINCRONA PARA ELIMINAR UN REGISTRO DE SEGUIMIENTO MEDICO
    public class EliminarSeguimientoMedico extends AsyncTask<String, Integer, Boolean>{

    	private String id="";
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			HttpClient httpClient= new DefaultHttpClient();
			id=params[0];
			HttpDelete del= new HttpDelete (
					"http://"+ip+"/ADP/SeguimientoMedico/SeguimientoMedicoEliminar/"+id);
			del.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(del);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				if (!respStr.equals("true")) {
					resul=false;
				}else{
					TblSeguimientoMedico seguimientoM = new TblSeguimientoMedico();
					seguimientoM.EliminarPorIdSeguimientoMedicoRegTblSeguimientoMedico(Long.parseLong(id));
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
    //TAREA ASINCRONA PARA BUSCAR UN GRUPO DE REGISTROS DE SEGUIMIENTOS MEDICOS
    public class BuscarAllSeguimientoMedicoXCuidadores extends AsyncTask<String, Integer, Boolean>{

    	private TblSeguimientoMedico seguiMedico= new TblSeguimientoMedico();
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			String idC=params[0];
			HttpClient httpClient= new DefaultHttpClient();
			HttpGet BT= new HttpGet (
					"http://"+ip+"/ADP/SeguimientoMedico/SeguimientosMedicosBuscarXCuidadores/"+idC);
			BT.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BT);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONArray respJSON = new JSONArray(respStr);
				
				if(!respJSON.equals(null)){
					for (int i = 0; i <respJSON.length(); i++) {
						JSONObject obj=respJSON.getJSONObject(i);
											
						seguiMedico = new TblSeguimientoMedico();
						seguiMedico.setIdSeguimientoMedico(obj.getLong("IdSeguimientoMedico"));
						seguiMedico.setIdPaciente(obj.getLong("IdPaciente"));
						seguiMedico.setAnio(obj.getInt("Anio"));
						seguiMedico.setMes(obj.getInt("Mes"));
						seguiMedico.setDia(obj.getInt("Dia"));
						seguiMedico.setUnidadMedica(obj.getString("UnidadMedica"));
						seguiMedico.setDoctor(obj.getString("Doctor"));
						seguiMedico.setDiagnostico(obj.getString("Diagnostico"));
						seguiMedico.setTratamientoMedico(obj.getString("TratamientoMedico"));
						seguiMedico.setAlimentacionSugerida(obj.getString("AlimentacionSugerida"));
						seguiMedico.setEliminado(obj.getBoolean("Eliminado"));
						
						TblSeguimientoMedico guardar_SM = new TblSeguimientoMedico(
								seguiMedico.getIdSeguimientoMedico(), seguiMedico.getIdPaciente(),
								seguiMedico.getAnio(), seguiMedico.getMes(), seguiMedico.getDia(),
								seguiMedico.getUnidadMedica(), seguiMedico.getDoctor(), 
								seguiMedico.getDiagnostico(), seguiMedico.getTratamientoMedico(), 
								seguiMedico.getAlimentacionSugerida(),seguiMedico.getEliminado());
						guardar_SM.save();
						
					}
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

	public JsonArrayRequest AB_SeguimientoMedico(long idC, final String TAG){
		String urlJsonArray = "http://"+ip+"/ADP/SeguimientoMedico/SeguimientoMedicoBuscarXCuidadores/"+String.valueOf(idC);
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

								TblSeguimientoMedico seguiMedico = new TblSeguimientoMedico();
								seguiMedico.setIdSeguimientoMedico(obj.getLong("IdSeguimientoMedico"));
								seguiMedico.setIdPaciente(obj.getLong("IdPaciente"));
								seguiMedico.setAnio(obj.getInt("Anio"));
								seguiMedico.setMes(obj.getInt("Mes"));
								seguiMedico.setDia(obj.getInt("Dia"));
								seguiMedico.setUnidadMedica(obj.getString("UnidadMedica"));
								seguiMedico.setDoctor(obj.getString("Doctor"));
								seguiMedico.setDiagnostico(obj.getString("Diagnostico"));
								seguiMedico.setTratamientoMedico(obj.getString("TratamientoMedico"));
								seguiMedico.setAlimentacionSugerida(obj.getString("AlimentacionSugerida"));
								seguiMedico.setEliminado(obj.getBoolean("Eliminado"));
								seguiMedico.save();

								Log.e("Seg_Med => Registro", "#" + i + "= " + seguiMedico);
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
