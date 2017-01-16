package com.Notifications.patientssassistant.asynctask;

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
import com.Notifications.patientssassistant.tables.TblFamiliaresPacientes;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.orm.query.Condition;
import com.orm.query.Select;

public class ATFamiliaresPacientes {

	//VARIABLES DE CLASE ATFamiliaresPacientes
	//private static String ip="192.168.1.4:1522";
	private static String ip=VarEstatic.ObtenerIP();
	
	//TAREA ASINCRONA PARA INSERTAR UN REGISTRO DE FAMILIAR DE UN PACIENTE
	public class InsertarFamiliaresPacientes extends AsyncTask<String, Integer, Long>
    {
    	private TblFamiliaresPacientes famPac;
    	
		@Override
		protected Long doInBackground(String... params) {
			// TODO Auto-generated method stub
			Boolean resul=true;
			long respuesta=0;
			HttpClient httpClient=new DefaultHttpClient();
			
			HttpPost post=new HttpPost(
					"http://"+ip+"/ADP/FamiliaresPacientes/FamiliarPacienteInsertar");
			post.setHeader("content-type", "application/json");
			
			try {
				JSONObject dato=new JSONObject();
							
				dato.put("IdFamiliarPaciente", Long.parseLong(params[0]));
				dato.put("IdPaciente", Long.parseLong(params[1]));
				dato.put("NombreContacto", params[2]);
				dato.put("CiContacto", params[3]);
				dato.put("Parentezco", params[4]);
				dato.put("Celular",params[5]);
				dato.put("Telefono", params[6]);
				dato.put("Direccion", params[7]);
				dato.put("Observacion", params[8]);
				dato.put("EnviarReportes", Boolean.parseBoolean(params[9]));
				dato.put("FotoContacto", params[10]);
				dato.put("Email", params[11]);
				dato.put("Eliminado",Boolean.parseBoolean(params[12]));
				
				StringEntity entity = new StringEntity(dato.toString());
				post.setEntity(entity);
				
				HttpResponse resp=httpClient.execute(post);
				String respStr = EntityUtils.toString(resp.getEntity());
				respuesta=Long.parseLong(respStr);
				
				if (respuesta>0){
					famPac = new TblFamiliaresPacientes(
								respuesta, Long.parseLong(params[1]),
								params[2], params[3], params[4],
								params[5], params[6], params[7],
								params[8], Boolean.parseBoolean(params[9]),
								params[10], params[11],
								Boolean.parseBoolean(params[12]));
					famPac.save();
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
	
	//TAREA ASINCRONA PARA ACTUALIZAR EL REGISTRO DE UN FAMILIAR DE UN PACENTE
    public class ActualizarFamiliaresPacientes extends AsyncTask<String, Integer, Boolean>
    {
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			Boolean resul=true;
			HttpClient httpClient=new DefaultHttpClient();
			
			HttpPut put=new HttpPut(
					"http://"+ip+"/ADP/FamiliaresPacientes/FamiliarPacienteActualizar");
			put.setHeader("content-type", "application/json");
			
			try {
				JSONObject dato=new JSONObject();
				
				dato.put("IdFamiliarPaciente", Long.parseLong(params[0]));
				dato.put("IdPaciente", Long.parseLong(params[1]));
				dato.put("NombreContacto", params[2]);
				dato.put("CiContacto", params[3]);
				dato.put("Parentezco", params[4]);
				dato.put("Celular",params[5]);
				dato.put("Telefono", params[6]);
				dato.put("Direccion", params[7]);
				dato.put("Observacion", params[8]);
				dato.put("EnviarReportes", Boolean.parseBoolean(params[9]));
				dato.put("FotoContacto", params[10]);
				dato.put("Email", params[11]);
				dato.put("Eliminado",Boolean.parseBoolean(params[12]));
				
				StringEntity entity = new StringEntity(dato.toString());
				put.setEntity(entity);
				
				HttpResponse resp=httpClient.execute(put);
				String respStr = EntityUtils.toString(resp.getEntity());
				
				if (!respStr.equals("true")) {
					resul=false;
				}else{
					String campo_ide=String.valueOf(Long.parseLong(params[0]));
					Select elFamPac = Select.from(TblFamiliaresPacientes.class)
							.where(Condition.prop("ID_FAMILIAR_PACIENTE").eq(campo_ide));
					TblFamiliaresPacientes edit_FP=(TblFamiliaresPacientes)elFamPac.first();

					if (edit_FP!=null) {
						edit_FP.setIdFamiliarPaciente(Long.parseLong(params[0]));
						edit_FP.setIdPaciente(Long.parseLong(params[1]));
						edit_FP.setNombreContacto(params[2]);
						edit_FP.setCiContacto(params[3]);
						edit_FP.setParentezco(params[4]);
						edit_FP.setCelular(params[5]);
						edit_FP.setTelefono(params[6]);
						edit_FP.setDireccion(params[7]);
						edit_FP.setObservacion(params[8]);
						edit_FP.setEnviarReportes(Boolean.parseBoolean(params[9]));
						edit_FP.setFotoContacto(params[10]);
						edit_FP.setEmail(params[11]);
						edit_FP.setEliminado(Boolean.parseBoolean(params[12]));
						edit_FP.save();
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
    
    //TAREA ASINCRONA PARA BUSCAR EL REGISTRO DE UN FAMILIAR DE UN PACIENTE
    public class BuscarUnFamiliarPaciente extends AsyncTask<String, Integer, Boolean>{

		private TblFamiliaresPacientes famPac= new TblFamiliaresPacientes();
    	
    	@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			HttpClient httpClient= new DefaultHttpClient();
			String id=params[0];
			HttpGet BO= new HttpGet (
					"http://"+ip+"/ADP/FamiliaresPacientes/FamiliarPacienteBuscar/"+id);
			BO.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BO);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONObject respJSON = new JSONObject(respStr);
				
				famPac.setIdFamiliarPaciente(respJSON.getLong("IdFamiliarPaciente"));
				famPac.setIdPaciente(respJSON.getLong("IdPaciente"));
				famPac.setNombreContacto(respJSON.getString("NombreContacto"));
				famPac.setCiContacto(respJSON.getString("CiContacto"));
				famPac.setParentezco(respJSON.getString("Parentezco"));
				famPac.setCelular(respJSON.getString("Celular"));
				famPac.setTelefono(respJSON.getString("Telefono"));
				famPac.setDireccion(respJSON.getString("Direccion"));
				famPac.setObservacion(respJSON.getString("Observacion"));
				famPac.setEnviarReportes(respJSON.getBoolean("EnviarReportes"));
				famPac.setFotoContacto(respJSON.getString("FotoContacto"));
				famPac.setEmail(respJSON.getString("Email"));
				famPac.setEliminado(respJSON.getBoolean("Eliminado"));
				
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
				String campo_ide=String.valueOf(famPac.getIdFamiliarPaciente());
				Select elFamPac = Select.from(TblFamiliaresPacientes.class)
						.where(Condition.prop("ID_FAMILIAR_PACIENTE").eq(campo_ide));
		    	TblFamiliaresPacientes edit_FP=(TblFamiliaresPacientes)elFamPac.first();
		    	
				if (edit_FP!=null) {
					edit_FP.setIdFamiliarPaciente(famPac.getIdFamiliarPaciente());
					edit_FP.setIdPaciente(famPac.getIdPaciente());
					edit_FP.setNombreContacto(famPac.getNombreContacto());
					edit_FP.setCiContacto(famPac.getCiContacto());
					edit_FP.setParentezco(famPac.getParentezco());
					edit_FP.setCelular(famPac.getCelular());
					edit_FP.setTelefono(famPac.getTelefono());
					edit_FP.setDireccion(famPac.getDireccion());
					edit_FP.setObservacion(famPac.getObservacion());
					edit_FP.setEnviarReportes(famPac.getEnviarReportes());
					edit_FP.setFotoContacto(famPac.getFotoContacto());
					edit_FP.setEliminado(famPac.getEliminado());
					edit_FP.save();
				}
				else{
					TblFamiliaresPacientes newFamiliar = new TblFamiliaresPacientes(
							famPac.getIdFamiliarPaciente(),famPac.getIdPaciente(), 
							famPac.getNombreContacto(), famPac.getCiContacto(), 
							famPac.getParentezco(), famPac.getCelular(), 
							famPac.getTelefono(), famPac.getDireccion(), 
							famPac.getObservacion(), famPac.getEnviarReportes(), 
							famPac.getFotoContacto(), famPac.getEmail(),
							famPac.getEliminado());
					newFamiliar.save();
				}
			}
		}
    }
    
    
    //TAREA ASINCRONA PARA BUSCAR LA FOTO DE UN FAMILIAR DE UN PACIENTE
    public class BuscarUnaFotoFamiliarPaciente extends AsyncTask<String, Integer, Boolean>{

		private TblFamiliaresPacientes famPac= new TblFamiliaresPacientes();
    	
    	@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			HttpClient httpClient= new DefaultHttpClient();
			String id=params[0];
			HttpGet BO= new HttpGet (
					"http://"+ip+"/ADP/FamiliaresPacientes/FamiliarPacienteBuscarOneFoto/"+id);
			BO.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BO);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONObject respJSON = new JSONObject(respStr);
				
				famPac.setIdFamiliarPaciente(Long.parseLong(params[0]));
				famPac.setFotoContacto(respJSON.getString("FotoContacto"));
				
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
				String campo_ide=String.valueOf(famPac.getIdFamiliarPaciente());
				Select elFamPac = Select.from(TblFamiliaresPacientes.class)
						.where(Condition.prop("ID_FAMILIAR_PACIENTE").eq(campo_ide));
		    	TblFamiliaresPacientes edit_FP=(TblFamiliaresPacientes)elFamPac.first();
		    	
				if (edit_FP!=null) {
					edit_FP.setFotoContacto(famPac.getFotoContacto());
					edit_FP.save();
				}
			}
		}
    }
    
    
    //TAREA ASINCRONA PARA BUSCAR TODOS LOS REGISTROS DE FAMILIARES DE UN PACIENTE
    public class BuscarAllFamiliaresPacientes extends AsyncTask<String, Integer, Boolean>{

    	private List<TblFamiliaresPacientes> familiares = new ArrayList<TblFamiliaresPacientes>();
    	private TblFamiliaresPacientes famPac= new TblFamiliaresPacientes();
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			String idP=params[0];
			HttpClient httpClient= new DefaultHttpClient();
			HttpGet BT= new HttpGet (
					"http://"+ip+"/ADP/FamiliaresPacientes/FamiliaresPacientesBuscarXPaciente/"+idP);
			BT.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BT);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONArray respJSON = new JSONArray(respStr);
				
				for (int i = 0; i <respJSON.length(); i++) {
					JSONObject obj=respJSON.getJSONObject(i);
					
					famPac = new TblFamiliaresPacientes();
					famPac.setIdFamiliarPaciente(obj.getLong("IdFamiliarPaciente"));
					famPac.setIdPaciente(obj.getLong("IdPaciente"));
					famPac.setNombreContacto(obj.getString("NombreContacto"));
					famPac.setCiContacto(obj.getString("CiContacto"));
					famPac.setParentezco(obj.getString("Parentezco"));
					famPac.setCelular(obj.getString("Celular"));
					famPac.setTelefono(obj.getString("Telefono"));
					famPac.setDireccion(obj.getString("Direccion"));
					famPac.setObservacion(obj.getString("Observacion"));
					famPac.setEnviarReportes(obj.getBoolean("EnviarReportes"));
					famPac.setFotoContacto(obj.getString("FotoContacto"));
					famPac.setEmail(obj.getString("Email"));
					famPac.setEliminado(obj.getBoolean("Eliminado"));
					familiares.add(famPac);
					
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
				
				for (int i = 0; i < familiares.size(); i++) {
					
					famPac.setIdFamiliarPaciente(familiares.get(i).getIdFamiliarPaciente());
					famPac.setIdPaciente(familiares.get(i).getIdPaciente());
					famPac.setNombreContacto(familiares.get(i).getNombreContacto());
					famPac.setCiContacto(familiares.get(i).getCiContacto());
					famPac.setParentezco(familiares.get(i).getParentezco());
					famPac.setCelular(familiares.get(i).getCelular());
					famPac.setTelefono(familiares.get(i).getTelefono());
					famPac.setDireccion(familiares.get(i).getDireccion());
					famPac.setObservacion(familiares.get(i).getObservacion());
					famPac.setEnviarReportes(familiares.get(i).getEnviarReportes());
					famPac.setFotoContacto(familiares.get(i).getFotoContacto());
					famPac.setEmail(familiares.get(i).getEmail());
					famPac.setEliminado(familiares.get(i).getEliminado());
					
					Select elFamPac = Select.from(TblFamiliaresPacientes.class)
							.where(Condition.prop("ID_FAMILIAR_PACIENTE").eq(famPac.getIdFamiliarPaciente().toString()));
			    	TblFamiliaresPacientes edit_FP=(TblFamiliaresPacientes)elFamPac.first();
			    	
			    	if (edit_FP!=null) {
						edit_FP.setIdFamiliarPaciente(famPac.getIdFamiliarPaciente());
						edit_FP.setIdPaciente(famPac.getIdPaciente());
						edit_FP.setNombreContacto(famPac.getNombreContacto());
						edit_FP.setCiContacto(famPac.getCiContacto());
						edit_FP.setParentezco(famPac.getParentezco());
						edit_FP.setCelular(famPac.getCelular());
						edit_FP.setTelefono(famPac.getTelefono());
						edit_FP.setDireccion(famPac.getDireccion());
						edit_FP.setObservacion(famPac.getObservacion());
						edit_FP.setEnviarReportes(famPac.getEnviarReportes());
						edit_FP.setFotoContacto(famPac.getFotoContacto());
						edit_FP.setEmail(famPac.getEmail());
						edit_FP.setEliminado(famPac.getEliminado());
						edit_FP.save();
					}
					else{
						TblFamiliaresPacientes guardar_FP = new TblFamiliaresPacientes(
								famPac.getIdFamiliarPaciente(), famPac.getIdPaciente(),
								famPac.getNombreContacto(), famPac.getCiContacto(),
								famPac.getParentezco(), famPac.getCelular(),
								famPac.getTelefono(), famPac.getDireccion(),
								famPac.getObservacion(), famPac.getEnviarReportes(),
								famPac.getFotoContacto(), famPac.getEmail(),
								famPac.getEliminado());
						guardar_FP.save();
					}
					
					BuscarUnaFotoFamiliarPaciente buscar= new BuscarUnaFotoFamiliarPaciente();
					buscar.execute(famPac.getIdFamiliarPaciente().toString());
				}
				
			}
		}
    }
    
    
    //TAREA ASINCRONA PARA ELIMINAR EL REGISTRO DE UN FAMILIAR DE UN PACIENTE
    public class EliminarFamiliaPaciente extends AsyncTask<String, Integer, Boolean>{

    	private String id="";
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			HttpClient httpClient= new DefaultHttpClient();
			id=params[0];
			HttpDelete del= new HttpDelete (
					"http://"+ip+"/ADP/FamiliaresPacientes/FamiliarPacienteEliminar/"+id);
			del.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(del);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				if (!respStr.equals("true")) {
					resul=false;
				}else{
					TblFamiliaresPacientes famPac = new TblFamiliaresPacientes();
					famPac.EliminarPorIdFamiliarPacienteRegTblFamiliaresPacientes(Long.parseLong(id));
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
    
    //UTILIAZADA PARA ACTUALIZAR LA BD
    //TAREA ASINCRONA PARA BUSCAR TODOS LOS REGISTROS DE FAMILIARES DE UN PACIENTE
    public class BuscarAllFamiliaresPacientesCuidadores extends AsyncTask<String, Integer, Boolean>{

    	private List<TblFamiliaresPacientes> familiares = new ArrayList<TblFamiliaresPacientes>();
    	private TblFamiliaresPacientes famPac= new TblFamiliaresPacientes();
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			String idC=params[0];
			HttpClient httpClient= new DefaultHttpClient();
			HttpGet BT= new HttpGet (
					"http://"+ip+"/ADP/FamiliaresPacientes/FamiliaresPacientesBuscarXCuidadores/"+idC);
			BT.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BT);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONArray respJSON = new JSONArray(respStr);
				
				for (int i = 0; i <respJSON.length(); i++) {
					JSONObject obj=respJSON.getJSONObject(i);
					
					famPac = new TblFamiliaresPacientes();
					famPac.setIdFamiliarPaciente(obj.getLong("IdFamiliarPaciente"));
					famPac.setIdPaciente(obj.getLong("IdPaciente"));
					famPac.setNombreContacto(obj.getString("NombreContacto"));
					famPac.setCiContacto(obj.getString("CiContacto"));
					famPac.setParentezco(obj.getString("Parentezco"));
					famPac.setCelular(obj.getString("Celular"));
					famPac.setTelefono(obj.getString("Telefono"));
					famPac.setDireccion(obj.getString("Direccion"));
					famPac.setObservacion(obj.getString("Observacion"));
					famPac.setEnviarReportes(obj.getBoolean("EnviarReportes"));
					famPac.setFotoContacto(obj.getString("FotoContacto"));
					famPac.setEmail(obj.getString("Email"));
					famPac.setEliminado(obj.getBoolean("Eliminado"));
					familiares.add(famPac);
					
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
				
				for (int i = 0; i < familiares.size(); i++) {
					famPac.setIdFamiliarPaciente(familiares.get(i).getIdFamiliarPaciente());
					famPac.setIdPaciente(familiares.get(i).getIdPaciente());
					famPac.setNombreContacto(familiares.get(i).getNombreContacto());
					famPac.setCiContacto(familiares.get(i).getCiContacto());
					famPac.setParentezco(familiares.get(i).getParentezco());
					famPac.setCelular(familiares.get(i).getCelular());
					famPac.setTelefono(familiares.get(i).getTelefono());
					famPac.setDireccion(familiares.get(i).getDireccion());
					famPac.setObservacion(familiares.get(i).getObservacion());
					famPac.setEnviarReportes(familiares.get(i).getEnviarReportes());
					famPac.setFotoContacto(familiares.get(i).getFotoContacto());
					famPac.setEmail(familiares.get(i).getEmail());
					famPac.setEliminado(familiares.get(i).getEliminado());
					
					Select elFamPac = Select.from(TblFamiliaresPacientes.class)
							.where(Condition.prop("ID_FAMILIAR_PACIENTE").eq(famPac.getIdFamiliarPaciente().toString()));
			    	TblFamiliaresPacientes edit_FP=(TblFamiliaresPacientes)elFamPac.first();
			    	
			    	if (edit_FP!=null) {
						edit_FP.setIdFamiliarPaciente(famPac.getIdFamiliarPaciente());
						edit_FP.setIdPaciente(famPac.getIdPaciente());
						edit_FP.setNombreContacto(famPac.getNombreContacto());
						edit_FP.setCiContacto(famPac.getCiContacto());
						edit_FP.setParentezco(famPac.getParentezco());
						edit_FP.setCelular(famPac.getCelular());
						edit_FP.setTelefono(famPac.getTelefono());
						edit_FP.setDireccion(famPac.getDireccion());
						edit_FP.setObservacion(famPac.getObservacion());
						edit_FP.setEnviarReportes(famPac.getEnviarReportes());
						edit_FP.setFotoContacto(famPac.getFotoContacto());
						edit_FP.setEmail(famPac.getEmail());
						edit_FP.setEliminado(famPac.getEliminado());
						edit_FP.save();
					}
					else{
						TblFamiliaresPacientes guardar_FP = new TblFamiliaresPacientes(
								famPac.getIdFamiliarPaciente(), famPac.getIdPaciente(),
								famPac.getNombreContacto(), famPac.getCiContacto(),
								famPac.getParentezco(), famPac.getCelular(),
								famPac.getTelefono(), famPac.getDireccion(),
								famPac.getObservacion(), famPac.getEnviarReportes(),
								famPac.getFotoContacto(), famPac.getEmail(),
								famPac.getEliminado());
						guardar_FP.save();
					}
					
					BuscarUnaFotoFamiliarPaciente buscar= new BuscarUnaFotoFamiliarPaciente();
					buscar.execute(famPac.getIdFamiliarPaciente().toString());
				}
				
			}
		}
    }
    
    //TAREA ASINCRONA PARA ENVIAR REPORTE A UN FAMILIAR DE UN PACIENTE
  	public class EnviarReporteAFamiliar extends AsyncTask<String, Integer, Boolean>
      {
      	private TblFamiliaresPacientes famPac;
      	
  		@Override
  		protected Boolean doInBackground(String... params) {
  			// TODO Auto-generated method stub
  			Boolean resul=true;
  			HttpClient httpClient=new DefaultHttpClient();
  			
  			HttpPost post=new HttpPost(
  					"http://"+ip+"/ADP/FamiliaresPacientes/FamiliarPacienteEnviarReporte");
  			post.setHeader("content-type", "application/json");
  			
  			try {
  				JSONObject dato=new JSONObject();
  							
  				dato.put("IdFamiliarPaciente", Long.parseLong(params[0]));
  				dato.put("IdPaciente", Long.parseLong(params[1]));
  				dato.put("IdCuidador", Long.parseLong(params[2]));
  				
  				StringEntity entity = new StringEntity(dato.toString());
  				post.setEntity(entity);
  				
  				HttpResponse resp=httpClient.execute(post);
  				String respStr = EntityUtils.toString(resp.getEntity());
  				
  				if (respStr.equals("true")){resul=true;} else {resul=false;}
  				
  			} catch (Exception ex) {
  				Log.e("ServicioRest", "Error!", ex);
  				resul = false;
  			}
  			return resul;
  		}
  		
  		@Override
  		protected void onPostExecute(Boolean resul ) {
  			// TODO Auto-generated method stub
  			if (resul) {
  				//mostrar que el reporte fue enviado
  			}
  		}
  		
      } 
  	
  	public class LeerReporteAEnviar extends AsyncTask<String, Integer, String>
    {
    	private TblFamiliaresPacientes famPac;
    	
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String resul="";
			HttpClient httpClient=new DefaultHttpClient();
			
			HttpPost post=new HttpPost(
					"http://"+ip+"/ADP/FamiliaresPacientes/FamiliarPacienteLeerReporte");
			post.setHeader("content-type", "application/json");
			
			try {
				JSONObject dato=new JSONObject();
							
				dato.put("IdFamiliarPaciente", Long.parseLong(params[0]));
				dato.put("IdPaciente", Long.parseLong(params[1]));
				dato.put("IdCuidador", Long.parseLong(params[2]));
				
				StringEntity entity = new StringEntity(dato.toString());
				post.setEntity(entity);
				
				HttpResponse resp=httpClient.execute(post);
				String respStr = EntityUtils.toString(resp.getEntity());
							
				resul = respStr;
				
			} catch (Exception ex) {
				Log.e("ServicioRest", "Error!", ex);
				resul = "No se pudo contactar con el servidor";
			}
			return resul;
		}
		
		@Override
		protected void onPostExecute(String resul ) {
			// TODO Auto-generated method stub
			
		}
		
    }

	public JsonArrayRequest AB_FamiliaresPacientes(long idC, final String TAG){
		String urlJsonArray = "http://"+ip+"/ADP/FamiliaresPacientes/FamiliarPacienteBuscarXCuidadores/"+String.valueOf(idC);
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
								TblFamiliaresPacientes famPac = new TblFamiliaresPacientes();
								famPac.setIdFamiliarPaciente(obj.getLong("IdFamiliarPaciente"));
								famPac.setIdPaciente(obj.getLong("IdPaciente"));
								famPac.setNombreContacto(obj.getString("NombreContacto"));
								famPac.setCiContacto(obj.getString("CiContacto"));
								famPac.setParentezco(obj.getString("Parentezco"));
								famPac.setCelular(obj.getString("Celular"));
								famPac.setTelefono(obj.getString("Telefono"));
								famPac.setDireccion(obj.getString("Direccion"));
								famPac.setObservacion(obj.getString("Observacion"));
								famPac.setEnviarReportes(obj.getBoolean("EnviarReportes"));
								famPac.setFotoContacto(obj.getString("FotoContacto"));
								famPac.setEmail(obj.getString("Email"));
								famPac.setEliminado(obj.getBoolean("Eliminado"));
								famPac.save();
								Log.e("Familiar => Registro", "#" + i + "= " + famPac);
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