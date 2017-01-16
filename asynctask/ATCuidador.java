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
import com.Notifications.patientssassistant.tables.TblCuidador;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.orm.query.Condition;
import com.orm.query.Select;

public class ATCuidador {
	
	//VARIABLES DE CLASE ATCuidador 
	//private static String ip="192.168.1.4:1522";
	private static String ip=VarEstatic.ObtenerIP();
	public TblCuidador elCuidador;
	

	//TAREA ASINCRONA PARA INSERTAR UN CUIDADOR
	public class InsertarCuidador extends AsyncTask<String, Integer, Long>
    {
    	private TblCuidador cuidador;
		private long id;
    	
		@Override
		protected Long doInBackground(String... params) {
			// TODO Auto-generated method stub
			Boolean resul=false;
			HttpClient httpClient=new DefaultHttpClient();
			
			String link="http://"+ip+"/ADP/Cuidador/CuidadorInsertar";
			//String link="http://192.168.1.5:1522/ADP/Cuidador/CuidadorInsertar";
			HttpPost post=new HttpPost(link);
			post.setHeader("content-type", "application/json");
					
			try {
				//Construimos el objeto alumno en formato JSON
				JSONObject dato=new JSONObject();
							
				dato.put("IdCuidador", Long.parseLong(params[0]));
				dato.put("UsuarioC", params[1]);
				dato.put("NombreC", params[2]);
				dato.put("CiRucC", params[3]);
				dato.put("CelularC", params[4]);
				dato.put("TelefonoC",params[5]);
				dato.put("EmailC", params[6]);
				dato.put("DireccionC", params[7]);
				dato.put("CargoC", params[8]);
				dato.put("ControlTotal", Boolean.parseBoolean(params[9]));
				dato.put("FotoC", params[10]);
				dato.put("Eliminado",Boolean.parseBoolean(params[11]));
				
				StringEntity entity = new StringEntity(dato.toString());
				post.setEntity(entity);
				
				HttpResponse resp=httpClient.execute(post);
				String respStr = EntityUtils.toString(resp.getEntity());
				id=Long.parseLong(respStr);
				
				if (id>0){
					resul=true; 	
					cuidador=new TblCuidador(
								id, params[1], params[2], params[3], 
								params[4], params[5], params[6], params[7],
								params[8], Boolean.parseBoolean(params[9]),
								params[10], Boolean.parseBoolean(params[11]));
					cuidador.save();
				}else{resul=false;}
				
			} catch (Exception ex) {
				Log.e("ServicioRest", "Error!", ex);
				resul = false;
			}
			return id;
		}
		
		@Override
		protected void onPostExecute(Long resul ) {
			// TODO Auto-generated method stub
		}
		
    }  
    	
	//TAREA ASINCRONA PARA ACTUALIZAR EL REGISTRO DE UN CUIDADOR
    public class ActualizarCuidador extends AsyncTask<String, Integer, Boolean>
    {
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			Boolean resul=true;
			HttpClient httpClient=new DefaultHttpClient();
			
			HttpPut put=new HttpPut(
					"http://"+ip+"/ADP/Cuidador/CuidadorActualizar");
			put.setHeader("content-type", "application/json");
			
			try {
				//Construimos el objeto alumno en formato JSON
				JSONObject dato=new JSONObject();
				
				dato.put("IdCuidador", Long.parseLong(params[0]));
				dato.put("UsuarioC", params[1]);
				dato.put("NombreC", params[2]);
				dato.put("CiRucC", params[3]);
				dato.put("CelularC", params[4]);
				dato.put("TelefonoC",params[5]);
				dato.put("EmailC", params[6]);
				dato.put("DireccionC", params[7]);
				dato.put("CargoC", params[8]);
				dato.put("ControlTotal", Boolean.parseBoolean(params[9]));
				dato.put("FotoC", params[10]);
				dato.put("Eliminado",Boolean.parseBoolean(params[11]));
				
				StringEntity entity = new StringEntity(dato.toString());
				put.setEntity(entity);
				
				HttpResponse resp=httpClient.execute(put);
				String respStr = EntityUtils.toString(resp.getEntity());
				
				if (!respStr.equals("true")) {
					resul=false;
				}else{
					String campo_ide=String.valueOf(Long.parseLong(params[0]));
					Select unCuidador = Select.from(TblCuidador.class)
							.where(Condition.prop("ID_CUIDADOR").eq(campo_ide));
					TblCuidador edit_Cuidador=(TblCuidador)unCuidador.first();

					if (edit_Cuidador!=null) {
						edit_Cuidador.setIdCuidador(Long.parseLong(params[0]));
						edit_Cuidador.setUsuarioC(params[1]);
						edit_Cuidador.setNombreC(params[2]);
						edit_Cuidador.setCiRucC(params[3]);
						edit_Cuidador.setCelularC(params[4]);
						edit_Cuidador.setTelefonoC(params[5]);
						edit_Cuidador.setEmailC(params[6]);
						edit_Cuidador.setDireccionC(params[7]);
						edit_Cuidador.setCargoC(params[8]);
						edit_Cuidador.setControlTotal(Boolean.parseBoolean(params[9]));
						edit_Cuidador.setFotoC(params[10]);
						edit_Cuidador.setEliminado(Boolean.parseBoolean(params[11]));
						edit_Cuidador.save();
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
    
    //TAREA ASINCRONA PARA BUSCAR UN REGISTRO DE CUIDADOR
    public class BuscarUnCuidador extends AsyncTask<String, Integer, TblCuidador>{

		private TblCuidador cuidador= new TblCuidador();
    	
    	@Override
		protected TblCuidador doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			HttpClient httpClient= new DefaultHttpClient();
			String id=params[0];
			HttpGet BO= new HttpGet (
					"http://"+ip+"/ADP/Cuidador/CuidadorBuscar/"+id);
			BO.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BO);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONObject respJSON = new JSONObject(respStr);
				
				cuidador.setIdCuidador(respJSON.getLong("IdCuidador"));
				cuidador.setUsuarioC(respJSON.getString("UsuarioC"));
				cuidador.setNombreC(respJSON.getString("NombreC"));
				cuidador.setCiRucC(respJSON.getString("CiRucC"));
				cuidador.setCelularC(respJSON.getString("CelularC"));
				cuidador.setTelefonoC(respJSON.getString("TelefonoC"));
				cuidador.setEmailC(respJSON.getString("EmailC"));
				cuidador.setDireccionC(respJSON.getString("DireccionC"));
				cuidador.setCargoC(respJSON.getString("CargoC"));
				cuidador.setControlTotal(respJSON.getBoolean("ControlTotal"));
				cuidador.setFotoC(respJSON.getString("FotoC"));
				cuidador.setEliminado(respJSON.getBoolean("Eliminado"));
				
			} catch (Exception ex) {
				// TODO: handle exception
				Log.e("Serviciorest","Error!",ex);
				resul=false;
			}
    		
    		return cuidador;
		}
    	
    	@Override
		protected void onPostExecute(TblCuidador result) {
			// TODO Auto-generated method stub
			if (result!=null) {
				String campo_ide=String.valueOf(cuidador.getIdCuidador());
				Select unCuidador = Select.from(TblCuidador.class)
						.where(Condition.prop("ID_CUIDADOR").eq(campo_ide));
		    	TblCuidador edit_Cuidador=(TblCuidador)unCuidador.first();	
		    	
				if (edit_Cuidador!=null) {
					edit_Cuidador.setIdCuidador(cuidador.getIdCuidador());
					edit_Cuidador.setUsuarioC(cuidador.getUsuarioC());
					edit_Cuidador.setNombreC(cuidador.getNombreC());
					edit_Cuidador.setCiRucC(cuidador.getCiRucC());
					edit_Cuidador.setCelularC(cuidador.getCelularC());
					edit_Cuidador.setTelefonoC(cuidador.getTelefonoC());
					edit_Cuidador.setEmailC(cuidador.getEmailC());
					edit_Cuidador.setDireccionC(cuidador.getDireccionC());
					edit_Cuidador.setCargoC(cuidador.getCargoC());
					edit_Cuidador.setControlTotal(cuidador.getControlTotal());
					edit_Cuidador.setFotoC(cuidador.getFotoC());
					edit_Cuidador.setEliminado(cuidador.getEliminado());
					edit_Cuidador.save();
					
				}
				else{
					TblCuidador newCuidador= new TblCuidador(
							cuidador.getIdCuidador(),cuidador.getUsuarioC(),
							cuidador.getNombreC(), cuidador.getCiRucC(),
							cuidador.getCelularC(), cuidador.getTelefonoC(),
							cuidador.getEmailC(), cuidador.getDireccionC(),
							cuidador.getCargoC(), cuidador.getControlTotal(),
							cuidador.getFotoC(), cuidador.getEliminado());
					newCuidador.save();
					
				}
			}
		}
    }
       
	//TAREA ASINCRONA PARA BUSCAR UNA FOTO DE CUIDADOR
    public class BuscarUnaFotoCuidador extends AsyncTask<String, Integer, Boolean>{

		private TblCuidador cuidador= new TblCuidador();
    	
    	@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			HttpClient httpClient= new DefaultHttpClient();
			String id=params[0];
			HttpGet BO= new HttpGet (
					"http://"+ip+"/ADP/Cuidador/CuidadorBuscarFoto/"+id);
			BO.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BO);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONObject respJSON = new JSONObject(respStr);
				
				cuidador.setIdCuidador(Long.parseLong(id));
				cuidador.setFotoC(respJSON.getString("FotoC"));
				
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
				String campo_ide=String.valueOf(cuidador.getIdCuidador());
				Select elCuidador = Select.from(TblCuidador.class)
						.where(Condition.prop("ID_CUIDADOR").eq(campo_ide));
		    	TblCuidador edit_Cuidador=(TblCuidador)elCuidador.first();	
		    	
				if (edit_Cuidador!=null) {
					edit_Cuidador.setFotoC(cuidador.getFotoC());
					edit_Cuidador.save();
				}
			}
		}
    }
        
    //TAREA ASINCRONA PARA BUSCAR A TODOS LOS CUIDADORES
    public class BuscarAllCuidadores extends AsyncTask<String, Integer, Boolean>{

    	private List<TblCuidador> cuidadores = new ArrayList<TblCuidador>();
    	private TblCuidador cuidador= new TblCuidador();
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			String id=params[0];
			HttpClient httpClient= new DefaultHttpClient();
			HttpGet BT= new HttpGet (
					"http://"+ip+"/ADP/Cuidador/CuidadorBuscarAllXCuidadores/"+id);
			BT.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BT);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONArray respJSON = new JSONArray(respStr);
				
				for (int i = 0; i <respJSON.length(); i++) {
					JSONObject obj=respJSON.getJSONObject(i);
					
					cuidador = new TblCuidador();
					cuidador.setIdCuidador(obj.getLong("IdCuidador"));
					cuidador.setUsuarioC(obj.getString("UsuarioC"));
					cuidador.setNombreC(obj.getString("NombreC"));
					cuidador.setCiRucC(obj.getString("CiRucC"));
					cuidador.setCelularC(obj.getString("CelularC"));
					cuidador.setTelefonoC(obj.getString("TelefonoC"));
					cuidador.setEmailC(obj.getString("EmailC"));
					cuidador.setDireccionC(obj.getString("DireccionC"));
					cuidador.setCargoC(obj.getString("CargoC"));
					cuidador.setControlTotal(obj.getBoolean("ControlTotal"));
					cuidador.setFotoC(obj.getString("FotoC"));
					cuidador.setEliminado(obj.getBoolean("Eliminado"));
					cuidadores.add(cuidador);
					
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
    			for (int i = 0; i < cuidadores.size(); i++) {
    				
					cuidador = new TblCuidador();
					cuidador.setIdCuidador(cuidadores.get(i).getIdCuidador());
					cuidador.setUsuarioC(cuidadores.get(i).getUsuarioC());
					cuidador.setNombreC(cuidadores.get(i).getNombreC());
					cuidador.setCiRucC(cuidadores.get(i).getCiRucC());
					cuidador.setCelularC(cuidadores.get(i).getCelularC());
					cuidador.setTelefonoC(cuidadores.get(i).getTelefonoC());
					cuidador.setEmailC(cuidadores.get(i).getEmailC());
					cuidador.setDireccionC(cuidadores.get(i).getDireccionC());
					cuidador.setCargoC(cuidadores.get(i).getCargoC());
					cuidador.setControlTotal(cuidadores.get(i).getControlTotal());
					cuidador.setFotoC(cuidadores.get(i).getFotoC());
					cuidador.setEliminado(cuidadores.get(i).getEliminado());
					
					Select cuida = Select.from(TblCuidador.class)
							.where(Condition.prop("ID_CUIDADOR").eq(cuidador.getIdCuidador().toString()));
			    	TblCuidador edit_Cuidador=(TblCuidador)cuida.first();
			    	
			    	if (edit_Cuidador!=null) {
						edit_Cuidador.setIdCuidador(cuidador.getIdCuidador());
						edit_Cuidador.setUsuarioC(cuidador.getUsuarioC());
						edit_Cuidador.setNombreC(cuidador.getNombreC());
						edit_Cuidador.setCiRucC(cuidador.getCiRucC());
						edit_Cuidador.setCelularC(cuidador.getCelularC());
						edit_Cuidador.setTelefonoC(cuidador.getTelefonoC());
						edit_Cuidador.setEmailC(cuidador.getEmailC());
						edit_Cuidador.setDireccionC(cuidador.getDireccionC());
						edit_Cuidador.setCargoC(cuidador.getCargoC());
						edit_Cuidador.setControlTotal(cuidador.getControlTotal());
						edit_Cuidador.setFotoC(cuidador.getFotoC());
						edit_Cuidador.setEliminado(cuidador.getEliminado());
						edit_Cuidador.save();
					}
					else{
						TblCuidador newCuidador= new TblCuidador(
								cuidador.getIdCuidador(),cuidador.getUsuarioC(),
								cuidador.getNombreC(), cuidador.getCiRucC(),
								cuidador.getCelularC(), cuidador.getTelefonoC(),
								cuidador.getEmailC(), cuidador.getDireccionC(),
								cuidador.getCargoC(), cuidador.getControlTotal(),
								cuidador.getFotoC(), cuidador.getEliminado());
						newCuidador.save();
					}
					
					BuscarUnaFotoCuidador buscar= new BuscarUnaFotoCuidador();
					buscar.execute(cuidador.getIdCuidador().toString());
				}
			}
		}
    }
    
    //TAREA ASINCRONA PARA ELIMINAR UN CUIDADOR
    public class EliminarCuidador extends AsyncTask<String, Integer, Boolean>{

    	private String id="";
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			HttpClient httpClient= new DefaultHttpClient();
			id=params[0];
			HttpDelete del= new HttpDelete (
					"http://"+ip+"/ADP/Cuidador/CuidadorEliminar/"+id);
			del.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(del);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				if (!respStr.equals("true")) {
					resul=false;
				}else {
					TblCuidador cuidadores = new TblCuidador();
					cuidadores.EliminarPorIdCuidadorRegTblCuidador(Long.parseLong(id));
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
    
    //TAREA ASINCRONA PARA VERIFICAR SI YA EXISTE UN EMAIL
    public class ExisteEmail extends AsyncTask<String, Integer, Boolean>
    {
    	@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			Boolean resul=true;
			HttpClient httpClient=new DefaultHttpClient();
			
			HttpPut put=new HttpPut(
					"http://"+ip+"/ADP/Cuidador/CuidadorExisteEmail");
			put.setHeader("content-type", "application/json");
			
			try {
				//Construimos el objeto alumno en formato JSON
				JSONObject dato=new JSONObject();
				
				dato.put("EmailC", params[0]);
				
				StringEntity entity = new StringEntity(dato.toString());
				put.setEntity(entity);
				
				HttpResponse resp=httpClient.execute(put);
				String respStr = EntityUtils.toString(resp.getEntity());
				
				if (!respStr.equals("true")) {
					resul=false;
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
    
    //TAREA ASINCRONA PARA VERIFICAR SI YA EXISTE UN USUARIO
    public class ExisteUsuario extends AsyncTask<String, Integer, Boolean>
    {
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			Boolean resul=true;
			HttpClient httpClient=new DefaultHttpClient();
			
			HttpPut put=new HttpPut(
					"http://"+ip+"/ADP/Cuidador/CuidadorExisteUsuario");
			put.setHeader("content-type", "application/json");
			
			try {
				//Construimos el objeto alumno en formato JSON
				JSONObject dato=new JSONObject();
				
				dato.put("UsuarioC", params[0]);
				
				StringEntity entity = new StringEntity(dato.toString());
				put.setEntity(entity);
				
				HttpResponse resp=httpClient.execute(put);
				String respStr = EntityUtils.toString(resp.getEntity());
				
				if (!respStr.equals("true")) {
					resul=false;
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

	public JsonArrayRequest AB_Cuidadores(long idC, final String TAG){
		String urlJsonArray = "http://"+ip+"/ADP/Cuidador/CuidadorBuscarAllXCuidadores/"+String.valueOf(idC);
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
								TblCuidador cuidador = new TblCuidador();
								cuidador.setIdCuidador(obj.getLong("IdCuidador"));
								cuidador.setUsuarioC(obj.getString("UsuarioC"));
								cuidador.setNombreC(obj.getString("NombreC"));
								cuidador.setCiRucC(obj.getString("CiRucC"));
								cuidador.setCelularC(obj.getString("CelularC"));
								cuidador.setTelefonoC(obj.getString("TelefonoC"));
								cuidador.setEmailC(obj.getString("EmailC"));
								cuidador.setDireccionC(obj.getString("DireccionC"));
								cuidador.setCargoC(obj.getString("CargoC"));
								cuidador.setControlTotal(obj.getBoolean("ControlTotal"));
								cuidador.setFotoC(obj.getString("FotoC"));
								cuidador.setEliminado(obj.getBoolean("Eliminado"));
								cuidador.save();
								Log.e("Cuidador => Registro", "#" + i + "= " + cuidador);
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