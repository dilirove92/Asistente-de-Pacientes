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
import com.Notifications.patientssassistant.tables.TblControlMedicina;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.orm.query.Condition;
import com.orm.query.Select;


public class ATControlMedicina {
		
	//VARIABLES DE CLASE ATControlMedicina 
	//private static String ip="192.168.1.4:1522";
	private static String ip=VarEstatic.ObtenerIP();
	
	//TAREA ASINCRONA PARA INGRESAR UN NUEVO CONTROL DE MEDICINA
	public class InsertarControlMedicina extends AsyncTask<String, Integer, Long>
    {
    	public TblControlMedicina contMedic;
		public long id=0;
    	
		@Override
		protected Long doInBackground(String... params) {
			// TODO Auto-generated method stub
			long respuesta=0;
			HttpClient httpClient=new DefaultHttpClient();
			
			HttpPost post=new HttpPost(
					"http://"+ip+"/ADP/ControlMedicina/ControlMedicinaInsertar");
			post.setHeader("content-type", "application/json");
			
			try {
				JSONObject dato=new JSONObject();
				
				dato.put("IdControlMedicina", Long.parseLong(params[0]));
				dato.put("IdPaciente", Long.parseLong(params[1]));
				dato.put("Medicamento", params[2]);
				dato.put("Descripcion", params[3]);
				dato.put("MotivoMedicacion", params[4]);
				dato.put("Tiempo",params[5]);
				dato.put("Dosis", params[6]);
				dato.put("NdeVeces", Integer.parseInt(params[7]));
				dato.put("Tono", params[8]);
				dato.put("Eliminado",Boolean.parseBoolean(params[9]));
				
				StringEntity entity = new StringEntity(dato.toString());
				post.setEntity(entity);
				
				HttpResponse resp=httpClient.execute(post);
				String respStr = EntityUtils.toString(resp.getEntity());
				respuesta=Long.parseLong(respStr);
				
				if (respuesta>0){
					contMedic = new TblControlMedicina(
								respuesta, Long.parseLong(params[1]),
								params[2], params[3], params[4], params[5],
								params[6], Integer.parseInt(params[7]),
								params[8], Boolean.parseBoolean(params[9]));
					contMedic.save();
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
    
	//TAREA ASINCRONA PARA ACTUALIZAR CONTROL DE MEDICINA
    public class ActualizarControlMedicina extends AsyncTask<String, Integer, Boolean>
    {
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			Boolean resul=true;
			HttpClient httpClient=new DefaultHttpClient();
			
			HttpPut put=new HttpPut(
					"http://"+ip+"/ADP/ControlMedicina/ControlMedicinaActualizar");
			put.setHeader("content-type", "application/json");
			
			try {
				JSONObject dato=new JSONObject();
				
				dato.put("IdControlMedicina", Long.parseLong(params[0]));
				dato.put("IdPaciente", Long.parseLong(params[1]));
				dato.put("Medicamento", params[2]);
				dato.put("Descripcion", params[3]);
				dato.put("MotivoMedicacion", params[4]);
				dato.put("Tiempo",params[5]);
				dato.put("Dosis", params[6]);
				dato.put("NdeVeces", Integer.parseInt(params[7]));
				dato.put("Tono", params[8]);
				dato.put("Eliminado",Boolean.parseBoolean(params[9]));
				
				StringEntity entity = new StringEntity(dato.toString());
				put.setEntity(entity);
				
				HttpResponse resp=httpClient.execute(put);
				String respStr = EntityUtils.toString(resp.getEntity());
				
				if (!respStr.equals("true")) {
					resul=false;
				}else{
					Long campo_ide=Long.parseLong(params[0]);
					Select elControlDMedicina = Select.from(TblControlMedicina.class)
							.where(Condition.prop("ID_CONTROL_MEDICINA").eq(campo_ide));
					TblControlMedicina editar_CM=(TblControlMedicina)elControlDMedicina.first();

					if (editar_CM!=null) {
						editar_CM.setIdControlMedicina(Long.parseLong(params[0]));
						editar_CM.setIdPaciente(Long.parseLong(params[1]));
						editar_CM.setMedicamento(params[2]);
						editar_CM.setDescripcion(params[3]);
						editar_CM.setMotivoMedicacion(params[4]);
						editar_CM.setTiempo(params[5]);
						editar_CM.setDosis(params[6]);
						editar_CM.setNdeVeces(Integer.parseInt(params[7]));
						editar_CM.setTono(params[8]);
						editar_CM.setEliminado(Boolean.parseBoolean(params[9]));
						editar_CM.save();
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
    
    //TAREA ASINCRONA PARA BUSCAR UN REGISTRO DE CONTROL DE MEDICINA
    public class BuscarUnControlMedicina extends AsyncTask<String, Integer, Boolean>{

		private TblControlMedicina contMedic= new TblControlMedicina();
    	
    	@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			HttpClient httpClient= new DefaultHttpClient();
			String id=params[0];
			HttpGet BO= new HttpGet (
					"http://"+ip+"/ADP/ControlMedicina/ControlMedicinaBuscar/"+id);
			BO.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BO);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONObject respJSON = new JSONObject(respStr);
				
				contMedic.setIdControlMedicina(respJSON.getLong("IdControlMedicina"));
				contMedic.setIdPaciente(respJSON.getLong("IdPaciente"));
				contMedic.setMedicamento(respJSON.getString("Medicamento"));
				contMedic.setDescripcion(respJSON.getString("Descripcion"));
				contMedic.setMotivoMedicacion(respJSON.getString("MotivoMedicacion"));
				contMedic.setTiempo(respJSON.getString("Tiempo"));
				contMedic.setDosis(respJSON.getString("Dosis"));
				contMedic.setNdeVeces(respJSON.getInt("NdeVeces"));
				contMedic.setTono(respJSON.getString("Tono"));
				contMedic.setEliminado(respJSON.getBoolean("Eliminado"));
				
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
				String campo_ide=String.valueOf(contMedic.getIdControlMedicina());
				Select elControlDMedicina = Select.from(TblControlMedicina.class)
						.where(Condition.prop("ID_CONTROL_MEDICINA").eq(campo_ide));
		    	TblControlMedicina editar_CM=(TblControlMedicina)elControlDMedicina.first();	
		    	
				if (editar_CM!=null) {
					editar_CM.setIdControlMedicina(contMedic.getIdControlMedicina());
					editar_CM.setIdPaciente(contMedic.getIdPaciente());
					editar_CM.setMedicamento(contMedic.getMedicamento());
					editar_CM.setDescripcion(contMedic.getDescripcion());
					editar_CM.setMotivoMedicacion(contMedic.getMotivoMedicacion());
					editar_CM.setTiempo(contMedic.getTiempo());
					editar_CM.setDosis(contMedic.getDosis());
					editar_CM.setNdeVeces(contMedic.getNdeVeces());
					editar_CM.setTono(contMedic.getTono());
					editar_CM.setEliminado(contMedic.getEliminado());
					editar_CM.save();
				}
				else{
					TblControlMedicina cM = new TblControlMedicina(
							contMedic.getIdControlMedicina(), contMedic.getIdPaciente(),
							contMedic.getMedicamento(), contMedic.getDescripcion(), 
							contMedic.getMotivoMedicacion(), contMedic.getTiempo(),
							contMedic.getDosis(), contMedic.getNdeVeces(), 
							contMedic.getTono(), contMedic.getEliminado());
					cM.save();
				}
			}
		}
    }
        
    //TAREA ASINCRONA PARA TRAER TODOS LOS DATOS DE CONTROL DE MEDICINA DE UN DETERMINADO PACIENTE
    public class BuscarAllControlMedicina extends AsyncTask<String, Integer, Boolean>{

    	private TblControlMedicina contMedic= new TblControlMedicina();
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			String idP=params[0];
			HttpClient httpClient= new DefaultHttpClient();
			HttpGet BT= new HttpGet (
					"http://"+ip+"/ADP/ControlMedicina/ControlMedicinasBuscarAllXPaciente/"+idP);
			BT.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BT);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONArray respJSON = new JSONArray(respStr);
				
				for (int i = 0; i <respJSON.length(); i++) {
					JSONObject obj=respJSON.getJSONObject(i);
								
					contMedic = new TblControlMedicina();
					contMedic.setIdControlMedicina(obj.getLong("IdControlMedicina"));
					contMedic.setIdPaciente(obj.getLong("IdPaciente"));
					contMedic.setMedicamento(obj.getString("Medicamento"));
					contMedic.setDescripcion(obj.getString("Descripcion"));
					contMedic.setMotivoMedicacion(obj.getString("MotivoMedicacion"));
					contMedic.setTiempo(obj.getString("TipoTratamiento"));
					contMedic.setDosis(obj.getString("Dosis"));
					contMedic.setNdeVeces(obj.getInt("NdeVeces"));
					contMedic.setTono(obj.getString("Tono"));
					contMedic.setEliminado(obj.getBoolean("Eliminado"));
					
					TblControlMedicina guardar_CM = new TblControlMedicina(
							contMedic.getIdControlMedicina(), contMedic.getIdPaciente(),
							contMedic.getMedicamento(), contMedic.getDescripcion(),
							contMedic.getMotivoMedicacion(), contMedic.getTiempo(),
							contMedic.getDosis(), contMedic.getNdeVeces(),
							contMedic.getTono() ,contMedic.getEliminado());
					guardar_CM.save();
					
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
    
    //TAREA ASINCRONA PARA ELIMINAR UN DETERMINADO REGISTRO DE CONTROL DE MEDICINA
    public class EliminarControlMedicina extends AsyncTask<String, Integer, Boolean>{

    	private String id="";
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			HttpClient httpClient= new DefaultHttpClient();
			id=params[0];
			HttpDelete del= new HttpDelete (
					"http://"+ip+"/ADP/ControlMedicina/ControlMedicinaEliminar/"+id);
			del.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(del);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				if (!respStr.equals("true")) {
					resul=false;
				}else{
					TblControlMedicina controlM = new TblControlMedicina();
					controlM.EliminarPorIdControlMedicinaRegTblControlMedicina(Long.parseLong(id));
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

    //UTILIZADA PARA ACTUALIZAR LA BD
    //TAREA ASINCRONA PARA TRAER TODOS LOS DATOS DE CONTROL DE MEDICINA DE CUIDADOR
    public class BuscarAllControlMedicinaXCuidadores extends AsyncTask<String, Integer, Boolean>{

    	private TblControlMedicina contMedic= new TblControlMedicina();
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			String idC=params[0];
			HttpClient httpClient= new DefaultHttpClient();
			HttpGet BT= new HttpGet (
					"http://"+ip+"/ADP/ControlMedicina/ControlMedicinasBuscarAllXCuidadores/"+idC);
			BT.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BT);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONArray respJSON = new JSONArray(respStr);
				
				for (int i = 0; i <respJSON.length(); i++) {
					JSONObject obj=respJSON.getJSONObject(i);
										
					contMedic = new TblControlMedicina();
					contMedic.setIdControlMedicina(obj.getLong("IdControlMedicina"));
					contMedic.setIdPaciente(obj.getLong("IdPaciente"));
					contMedic.setMedicamento(obj.getString("Medicamento"));
					contMedic.setDescripcion(obj.getString("Descripcion"));
					contMedic.setMotivoMedicacion(obj.getString("MotivoMedicacion"));
					contMedic.setTiempo(obj.getString("TipoTratamiento"));
					contMedic.setDosis(obj.getString("Dosis"));
					contMedic.setNdeVeces(obj.getInt("NdeVeces"));
					contMedic.setTono(obj.getString("Tono"));
					contMedic.setEliminado(obj.getBoolean("Eliminado"));
					
					TblControlMedicina guardar_CM = new TblControlMedicina(
							contMedic.getIdControlMedicina(), contMedic.getIdPaciente(),
							contMedic.getMedicamento(), contMedic.getDescripcion(),
							contMedic.getMotivoMedicacion(), contMedic.getTiempo(),
							contMedic.getDosis(), contMedic.getNdeVeces(),
							contMedic.getTono() ,contMedic.getEliminado());
					guardar_CM.save();
					
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

	public JsonArrayRequest AB_ControlMedicina(long idC, final String TAG){
		String urlJsonArray = "http://"+ip+"/ADP/ControlMedicina/ControlMedicinaBuscarAllXCuidadores/"+String.valueOf(idC);
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

								TblControlMedicina contMedic = new TblControlMedicina();
								contMedic.setIdControlMedicina(obj.getLong("IdControlMedicina"));
								contMedic.setIdPaciente(obj.getLong("IdPaciente"));
								contMedic.setMedicamento(obj.getString("Medicamento"));
								contMedic.setDescripcion(obj.getString("Descripcion"));
								contMedic.setMotivoMedicacion(obj.getString("MotivoMedicacion"));
								contMedic.setTiempo(obj.getString("TipoTratamiento"));
								contMedic.setDosis(obj.getString("Dosis"));
								contMedic.setNdeVeces(obj.getInt("NdeVeces"));
								contMedic.setTono(obj.getString("Tono"));
								contMedic.setEliminado(obj.getBoolean("Eliminado"));
								contMedic.save();

								Log.e("Cont_Med => Registro", "#" + i + "= " + contMedic);
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
