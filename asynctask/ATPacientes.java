package com.Notifications.patientssassistant.asynctask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.Notifications.patientssassistant.tables.TblActividadPaciente;
import com.Notifications.patientssassistant.tables.TblPacientes;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.orm.query.Condition;
import com.orm.query.Select;


public class ATPacientes {
	
	//VARIABLES DE CLASE ATPacientes
	//private static String ip="192.168.1.4:1522";
	private static String ip=VarEstatic.ObtenerIP();
	public static SimpleDateFormat sfDate =  new  SimpleDateFormat ("yyy-MM-dd");
	public TblPacientes paciente;
	
	//TAREA ASINCRONA PARA INSERTAR UN NUEVO PACIENTE
	public class InsertarPacientes extends AsyncTask<String, Integer, Long>
    {
    	private TblPacientes paciente;
		private long id=0;
    	
		@Override
		protected Long doInBackground(String... params) {
			// TODO Auto-generated method stub
			Boolean resul=true;
			long respuesta=0;
			HttpClient httpClient=new DefaultHttpClient();
			
			HttpPost post=new HttpPost(
					"http://"+ip+"/ADP/Pacientes/PacienteInsertar");
			post.setHeader("content-type", "application/json");
			
			try {
				JSONObject dato=new JSONObject();
				
				dato.put("IdPaciente", Long.parseLong(params[0]));
				dato.put("UsuarioP", params[1]);
				dato.put("CiP", params[2]);
				dato.put("NombreApellidoP", params[3]);
				dato.put("Anio", Integer.parseInt(params[4]));
				dato.put("Mes", Integer.parseInt(params[5]));
				dato.put("Dia", Integer.parseInt(params[6]));
				dato.put("EstadoCivilP",params[7]);
				dato.put("NivelEstudioP", params[8]);
				dato.put("MotivoIngresoP", params[9]);
				dato.put("TipoPacienteP", params[10]);
				dato.put("Edad", params[11]);
				dato.put("FotoP", params[12]);
				dato.put("Eliminado",Boolean.parseBoolean(params[13]));
				
				StringEntity entity = new StringEntity(dato.toString());
				post.setEntity(entity);
				
				HttpResponse resp=httpClient.execute(post);
				String respStr = EntityUtils.toString(resp.getEntity());
				respuesta=Long.parseLong(respStr);
				
				if (respuesta>0){
					paciente = new TblPacientes(
								respuesta, params[1], 
								params[2], params[3],
								Integer.parseInt(params[4]), 
								Integer.parseInt(params[5]),
								Integer.parseInt(params[6]), 
								params[7], params[8], 
								params[9], params[10], 
								Integer.parseInt(params[11]),
								params[12], 
								Boolean.parseBoolean(params[13]));
					paciente.save();
				}else{resul=false;}
				
			} catch (Exception ex) {
				Log.e("ServicioRest", "Error!", ex);
				resul = false;
			}
			return respuesta;
		}
		
		@Override
		protected void onPostExecute(Long resul ) {
			// TODO Auto-generated method stub
			if (resul>0) {
				for (int i = 0; i < 8; i++) {
					Long idAct=(long) (i+1);
					TblActividadPaciente actPac= new TblActividadPaciente(
							paciente.getIdPaciente(),idAct,false);
					actPac.save();
				}
			}
		}
    }  
    
	//TAREA ASINCRONA PARA ACTUALIZAR LOS DATOS DE UN PACIENTE
    public class ActualizarPaciente extends AsyncTask<String, Integer, Boolean>
    {
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			Boolean resul=true;
			HttpClient httpClient=new DefaultHttpClient();
			
			HttpPut put=new HttpPut(
					"http://"+ip+"/ADP/Pacientes/PacienteActualizar");
			put.setHeader("content-type", "application/json");
			
			try {
				//Construimos el objeto alumno en formato JSON
				JSONObject dato=new JSONObject();
				
				dato.put("IdPaciente", Long.parseLong(params[0]));
				dato.put("UsuarioP", params[1]);
				dato.put("CiP", params[2]);
				dato.put("NombreApellidoP", params[3]);
				dato.put("Anio", Integer.parseInt(params[4]));
				dato.put("Mes", Integer.parseInt(params[5]));
				dato.put("Dia", Integer.parseInt(params[6]));
				dato.put("EstadoCivilP",params[7]);
				dato.put("NivelEstudioP", params[8]);
				dato.put("MotivoIngresoP", params[9]);
				dato.put("TipoPacienteP", params[10]);
				dato.put("Edad", params[11]);
				dato.put("FotoP", params[12]);
				dato.put("Eliminado",Boolean.parseBoolean(params[13]));
				
				StringEntity entity = new StringEntity(dato.toString());
				put.setEntity(entity);
				
				HttpResponse resp=httpClient.execute(put);
				String respStr = EntityUtils.toString(resp.getEntity());
				
				if (!respStr.equals("true")) {
					resul=false;
				}else{
					String campo_ide=String.valueOf(Long.parseLong(params[0]));
					Select elPaciente = Select.from(TblPacientes.class)
							.where(Condition.prop("ID_PACIENTE").eq(campo_ide));
					TblPacientes edit_Pac=(TblPacientes)elPaciente.first();

					if (edit_Pac!=null) {
						edit_Pac.setIdPaciente(Long.parseLong(params[0]));
						edit_Pac.setUsuarioP(params[1]);
						edit_Pac.setCiP(params[2]);
						edit_Pac.setNombreApellidoP(params[3]);
						edit_Pac.setAnio(Integer.parseInt(params[4]));
						edit_Pac.setMes(Integer.parseInt(params[5]));
						edit_Pac.setDia(Integer.parseInt(params[6]));
						edit_Pac.setEstadoCivilP(params[7]);
						edit_Pac.setNivelEstudioP(params[8]);
						edit_Pac.setMotivoIngresoP(params[9]);
						edit_Pac.setTipoPacienteP(params[10]);
						edit_Pac.setEdad(Integer.parseInt(params[11]));
						edit_Pac.setFotoP(params[12]);
						edit_Pac.setEliminado(Boolean.parseBoolean(params[13]));
						edit_Pac.save();
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
    
    //TAREA ASINCRONA PARA BUSCAR LOS DATOS DE UN PACIENTE
    public class BuscarUnPaciente extends AsyncTask<String, Integer, Boolean>{

		private TblPacientes paciente= new TblPacientes();
    	
    	@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			HttpClient httpClient= new DefaultHttpClient();
			String id=params[0];
			HttpGet BO= new HttpGet (
					"http://"+ip+"/ADP/Pacientes/PacienteBuscar/"+id);
			BO.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BO);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONObject respJSON = new JSONObject(respStr);
				
				/*String dateString1=respJSON.getString("FechaNacP").substring(6, 18);
				long Date1 = Long.parseLong(dateString1); 
				Date netDate1 = (new Date(Date1));*/
				
				paciente.setIdPaciente(respJSON.getLong("IdPaciente"));
				paciente.setUsuarioP(respJSON.getString("UsuarioP"));
				paciente.setCiP(respJSON.getString("CiP"));
				paciente.setNombreApellidoP(respJSON.getString("NombreApellidoP"));
				paciente.setAnio(respJSON.getInt("Anio"));
				paciente.setMes(respJSON.getInt("Mes"));
				paciente.setDia(respJSON.getInt("Dia"));
				paciente.setEstadoCivilP(respJSON.getString("EstadoCivilP"));
				paciente.setNivelEstudioP(respJSON.getString("NivelEstudioP"));
				paciente.setMotivoIngresoP(respJSON.getString("MotivoIngresoP"));
				paciente.setTipoPacienteP(respJSON.getString("TipoPacienteP"));
				paciente.setEdad(respJSON.getInt("Edad"));
				paciente.setFotoP(respJSON.getString("FotoP"));
				paciente.setEliminado(respJSON.getBoolean("Eliminado"));
				
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
				String campo_ide=String.valueOf(paciente.getIdPaciente());
				Select elPaciente = Select.from(TblPacientes.class)
						.where(Condition.prop("ID_PACIENTE").eq(campo_ide));
		    	TblPacientes edit_Pac=(TblPacientes)elPaciente.first();
		    	
				if (edit_Pac!=null) {
					edit_Pac.setIdPaciente(paciente.getIdPaciente());
					edit_Pac.setUsuarioP(paciente.getUsuarioP());
					edit_Pac.setCiP(paciente.getCiP());
					edit_Pac.setNombreApellidoP(paciente.getNombreApellidoP());
					edit_Pac.setAnio(paciente.getAnio());
					edit_Pac.setMes(paciente.getMes());
					edit_Pac.setDia(paciente.getDia());
					edit_Pac.setEstadoCivilP(paciente.getEstadoCivilP());
					edit_Pac.setNivelEstudioP(paciente.getNivelEstudioP());
					edit_Pac.setMotivoIngresoP(paciente.getMotivoIngresoP());
					edit_Pac.setTipoPacienteP(paciente.getTipoPacienteP());
					edit_Pac.setEdad(paciente.getEdad());
					edit_Pac.setFotoP(paciente.getFotoP());
					edit_Pac.setEliminado(paciente.getEliminado());
					edit_Pac.save();
					
					//paciente = new TblPacientes();
					//paciente = edit_Pac;
				}
				else{
					TblPacientes newPac = new TblPacientes(
							paciente.getIdPaciente(), paciente.getUsuarioP(),
							paciente.getCiP(), paciente.getNombreApellidoP(),
							paciente.getAnio(), paciente.getMes(), paciente.getDia(),
							paciente.getEstadoCivilP(), paciente.getNivelEstudioP(), 
							paciente.getMotivoIngresoP(), paciente.getTipoPacienteP(), 
							paciente.getEdad(), paciente.getFotoP(), paciente.getEliminado());
					newPac.save();
					
					//paciente = new TblPacientes();
					//paciente = newPac;
				}
				
			}
		}
    }
    
    //TAREA ASINCRONA PARA BUSCAR FOTOS DE LOS DATOS DE UN PACIENTE
    public class BuscarUnaFotoPaciente extends AsyncTask<String, Integer, Boolean>{

		private TblPacientes paciente= new TblPacientes();
    	
    	@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			HttpClient httpClient= new DefaultHttpClient();
			String id=params[0];
			HttpGet BO= new HttpGet (
					"http://"+ip+"/ADP/Pacientes/PacienteBuscarFoto/"+id);
			BO.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BO);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONObject respJSON = new JSONObject(respStr);
				
				paciente.setIdPaciente(Long.parseLong(params[0]));
				paciente.setFotoP(respJSON.getString("FotoP"));
				
			} catch (Exception ex) {
				// TODO: handle exception
				Log.e("Serviciorest", "Error!", ex);
				resul = false;
			}
    		return resul;
		}
    	
    	@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			if (result) {
				String campo_ide=String.valueOf(paciente.getIdPaciente());
				Select elPaciente = Select.from(TblPacientes.class)
						.where(Condition.prop("ID_PACIENTE").eq(campo_ide));
		    	TblPacientes edit_Pac=(TblPacientes)elPaciente.first();
		    	
				if (edit_Pac!=null) {
					edit_Pac.setFotoP(paciente.getFotoP());
					edit_Pac.save();
				}
				
			}
		}
    }
      
    //BUSCAR TODOS LOS DATOS DE LOS PACIENTES DE UN DETERMINADO CUIDADOR
    public class BuscarAllPacientes extends AsyncTask<String, Integer, Boolean>{

    	private List<TblPacientes> pacientes = new ArrayList<TblPacientes>();
    	private TblPacientes paciente= new TblPacientes();
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			String idC=params[0];
			HttpClient httpClient= new DefaultHttpClient();
			HttpGet BT= new HttpGet (
					"http://"+ip+"/ADP/Pacientes/PacienteBuscarXCuidadores/"+idC);
			BT.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BT);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONArray respJSON = new JSONArray(respStr);
				
				for (int i = 0; i <respJSON.length(); i++) {
					JSONObject obj=respJSON.getJSONObject(i);
					
									
					paciente = new TblPacientes();
					paciente.setIdPaciente(obj.getLong("IdPaciente"));
					paciente.setUsuarioP(obj.getString("UsuarioP"));
					paciente.setCiP(obj.getString("CiP"));
					paciente.setNombreApellidoP(obj.getString("NombreApellidoP"));
					paciente.setAnio(obj.getInt("Anio"));
					paciente.setMes(obj.getInt("Mes"));
					paciente.setDia(obj.getInt("Dia"));
					paciente.setEstadoCivilP(obj.getString("EstadoCivilP"));
					paciente.setNivelEstudioP(obj.getString("NivelEstudioP"));
					paciente.setMotivoIngresoP(obj.getString("MotivoIngresoP"));
					paciente.setTipoPacienteP(obj.getString("TipoPacienteP"));
					paciente.setEdad(obj.getInt("Edad"));
					paciente.setFotoP(obj.getString("FotoP"));
					paciente.setEliminado(obj.getBoolean("Eliminado"));
					pacientes.add(paciente);
					
					TblPacientes guardar_Pac = new TblPacientes(
							paciente.getIdPaciente(), paciente.getUsuarioP(),
							paciente.getCiP(), paciente.getNombreApellidoP(),
							paciente.getAnio(), paciente.getMes(), paciente.getDia(),
							paciente.getEstadoCivilP(), paciente.getNivelEstudioP(), 
							paciente.getMotivoIngresoP(), paciente.getTipoPacienteP(), 
							paciente.getEdad(), paciente.getFotoP(), paciente.getEliminado());
					guardar_Pac.save();
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
				
				for (int i = 0; i < pacientes.size(); i++) {
					
					paciente = new TblPacientes();
					paciente.setIdPaciente(pacientes.get(i).getIdPaciente());
					paciente.setUsuarioP(pacientes.get(i).getUsuarioP());
					paciente.setCiP(pacientes.get(i).getCiP());
					paciente.setNombreApellidoP(pacientes.get(i).getNombreApellidoP());
					paciente.setAnio(pacientes.get(i).getAnio());
					paciente.setMes(pacientes.get(i).getMes());
					paciente.setDia(pacientes.get(i).getDia());
					paciente.setEstadoCivilP(pacientes.get(i).getEstadoCivilP());
					paciente.setNivelEstudioP(pacientes.get(i).getNivelEstudioP());
					paciente.setMotivoIngresoP(pacientes.get(i).getMotivoIngresoP());
					paciente.setTipoPacienteP(pacientes.get(i).getTipoPacienteP());
					paciente.setEdad(pacientes.get(i).getEdad());
					paciente.setFotoP(pacientes.get(i).getFotoP());
					paciente.setEliminado(pacientes.get(i).getEliminado());
					pacientes.add(paciente);
					
					String campo_ide=String.valueOf(paciente.getIdPaciente());
					Select elPaciente = Select.from(TblPacientes.class)
							.where(Condition.prop("ID_PACIENTE").eq(campo_ide));
			    	TblPacientes edit_Pac=(TblPacientes)elPaciente.first();
			    	
					if (edit_Pac!=null) {
						edit_Pac.setIdPaciente(paciente.getIdPaciente());
						edit_Pac.setUsuarioP(paciente.getUsuarioP());
						edit_Pac.setCiP(paciente.getCiP());
						edit_Pac.setNombreApellidoP(paciente.getNombreApellidoP());
						edit_Pac.setAnio(paciente.getAnio());
						edit_Pac.setMes(paciente.getMes());
						edit_Pac.setDia(paciente.getDia());
						edit_Pac.setEstadoCivilP(paciente.getEstadoCivilP());
						edit_Pac.setNivelEstudioP(paciente.getNivelEstudioP());
						edit_Pac.setMotivoIngresoP(paciente.getMotivoIngresoP());
						edit_Pac.setTipoPacienteP(paciente.getTipoPacienteP());
						edit_Pac.setEdad(paciente.getEdad());
						edit_Pac.setFotoP(paciente.getFotoP());
						edit_Pac.setEliminado(paciente.getEliminado());
						edit_Pac.save();
					}
					else{
						TblPacientes newPac = new TblPacientes(
								paciente.getIdPaciente(), paciente.getUsuarioP(),
								paciente.getCiP(), paciente.getNombreApellidoP(),
								paciente.getAnio(), paciente.getMes(), paciente.getDia(),
								paciente.getEstadoCivilP(), paciente.getNivelEstudioP(), 
								paciente.getMotivoIngresoP(), paciente.getTipoPacienteP(), 
								paciente.getEdad(), paciente.getFotoP(), paciente.getEliminado());
						newPac.save();
					}
					
					BuscarUnaFotoPaciente buscar= new BuscarUnaFotoPaciente();
					buscar.execute(campo_ide);
				}
    		
			}
		}
    }
    
    //TAREA ASINCRONA PARA ELIMINAR UN REGISTRO DE PACIENTE
    public class EliminarPaciente extends AsyncTask<String, Integer, Boolean>{

    	private String id="";
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			HttpClient httpClient= new DefaultHttpClient();
			id=params[0];
			HttpDelete del= new HttpDelete (
					"http://"+ip+"/ADP/Pacientes/PacienteEliminar/"+id);
			del.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(del);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				if (!respStr.equals("true")) {
					resul=false;
				}else{
					TblPacientes pacientes = new TblPacientes();
					pacientes.EliminarPorIdPacienteRegTblPacientes(Long.parseLong(id));
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
    
    //TAREA ASINCRONA PARA VER SI EL USUARIO YA EXISTE
  	public class ExisteUserPaciente extends AsyncTask<String, Integer, Boolean>
      {
      	private TblPacientes paciente;
  		private boolean existe=false;
      	
  		@Override
  		protected Boolean doInBackground(String... params) {
  			// TODO Auto-generated method stub
  			Boolean resul=true;
  			long respuesta=0;
  			HttpClient httpClient=new DefaultHttpClient();
  			
  			HttpPost post=new HttpPost(
  					"http://"+ip+"/ADP/Pacientes/PacienteExisteUser");
  			post.setHeader("content-type", "application/json");
  			
  			try {
  				JSONObject dato=new JSONObject();
  				
  				dato.put("UsuarioP", params[0]);
  				
  				StringEntity entity = new StringEntity(dato.toString());
  				post.setEntity(entity);
  				
  				HttpResponse resp=httpClient.execute(post);
  				String respStr = EntityUtils.toString(resp.getEntity());
  				
  				if(respStr.equals("true")){existe=true;}
  				
  				
  			} catch (Exception ex) {
  				Log.e("ServicioRest", "Error!", ex);
  				resul = false;
  			}
  			return existe;
  		}
  		
  		@Override
  		protected void onPostExecute(Boolean resul ) {
  			// TODO Auto-generated method stub
  		}
  		
      }

	public JsonArrayRequest AB_Pacientes(long idC, final String TAG){
		String urlJsonArray = "http://"+ip+"/ADP/Pacientes/PacienteBuscarXCuidadores/"+String.valueOf(idC);
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
								TblPacientes paciente = new TblPacientes();
								paciente.setIdPaciente(obj.getLong("IdPaciente"));
								paciente.setUsuarioP(obj.getString("UsuarioP"));
								paciente.setCiP(obj.getString("CiP"));
								paciente.setNombreApellidoP(obj.getString("NombreApellidoP"));
								paciente.setAnio(obj.getInt("Anio"));
								paciente.setMes(obj.getInt("Mes"));
								paciente.setDia(obj.getInt("Dia"));
								paciente.setEstadoCivilP(obj.getString("EstadoCivilP"));
								paciente.setNivelEstudioP(obj.getString("NivelEstudioP"));
								paciente.setMotivoIngresoP(obj.getString("MotivoIngresoP"));
								paciente.setTipoPacienteP(obj.getString("TipoPacienteP"));
								paciente.setEdad(obj.getInt("Edad"));
								paciente.setFotoP(obj.getString("FotoP"));
								paciente.setEliminado(obj.getBoolean("Eliminado"));
								paciente.save();
								Log.e("Paciente => Registro", "#" + i + "= " + paciente);
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