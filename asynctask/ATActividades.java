package com.Notifications.patientssassistant.asynctask;

import java.util.ArrayList;
import java.util.Date;
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


import com.Notifications.patientssassistant.VarEstatic;
import com.Notifications.patientssassistant.tables.TblActividades;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.orm.query.Condition;
import com.orm.query.Select;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class ATActividades {
	
	//VARIABLES DE CLASE ATActividades 
	//public static String ip="192.168.1.4:1522";
	private static String ip=VarEstatic.ObtenerIP();
	
	//TAREA ASINCRONA DE PARA INSERTAR NUEVAS ACTIVIDADES
	public class InsertarActividades extends AsyncTask<String, Integer, Long>
    {
		
		private long id=0;
    	private TblActividades actividad;
    	
		@Override
		protected Long doInBackground(String... params) {
			// TODO Auto-generated method stub
			Boolean resul=true;
			long respuesta=0;
			HttpClient httpClient=new DefaultHttpClient();
			
			HttpPost post=new HttpPost(
					"http://"+ip+"/ADP/Actividades/ActividadInsertar");
			post.setHeader("content-type", "application/json");
			
			try {
				JSONObject dato=new JSONObject();
								
				dato.put("IdActividad", Long.parseLong(params[0]));
				dato.put("IdTipoActividad", Long.parseLong(params[1]));
				dato.put("NombreActividad", params[2]);
				dato.put("DetalleActividad", params[3]);
				dato.put("ImagenActividad", params[4]);
				dato.put("TonoActividad", params[5]);
				dato.put("Eliminado",Boolean.parseBoolean(params[6]));
				
				StringEntity entity = new StringEntity(dato.toString());
				post.setEntity(entity);
				
				HttpResponse resp=httpClient.execute(post);
				String respStr = EntityUtils.toString(resp.getEntity());
				respuesta=Long.parseLong(respStr);
				
				if (respuesta>0){
					actividad = new TblActividades(
									respuesta, Long.parseLong(params[1]),
									params[2], params[3], params[4], params[5],
									Boolean.parseBoolean(params[6]));
					
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
				actividad.save();
			}
		}
		
    }  
	
	//TAREA ASINCRONA PARA ACTUALIZAR ACTIVIDADES
    public class ActualizarActividades extends AsyncTask<String, Integer, Boolean>
    {

    	private TblActividades actividad;
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			Boolean resul=true;
			HttpClient httpClient=new DefaultHttpClient();
			
			HttpPut put=new HttpPut(
					"http://"+ip+"/ADP/Actividades/ActividadActualizar");
			put.setHeader("content-type", "application/json");
			
			try {
				JSONObject dato=new JSONObject();
				
				dato.put("IdActividad", Long.parseLong(params[0]));
				dato.put("IdTipoActividad", Long.parseLong(params[1]));
				dato.put("NombreActividad", params[2]);
				dato.put("DetalleActividad", params[3]);
				dato.put("ImagenActividad", params[4]);
				dato.put("TonoActividad", params[5]);
				dato.put("Eliminado",Boolean.parseBoolean(params[6]));
				
				StringEntity entity = new StringEntity(dato.toString());
				put.setEntity(entity);
				
				HttpResponse resp=httpClient.execute(put);
				String respStr = EntityUtils.toString(resp.getEntity());
				
				if (!respStr.equals("true")) {
					resul=false;
				}else{
					actividad = new TblActividades();
					actividad.setIdActividad(Long.parseLong(params[0]));
					actividad.setIdTipoActividad(Long.parseLong(params[1]));
					actividad.setNombreActividad(params[2]);
					actividad.setDetalleActividad(params[3]);
					actividad.setImagenActividad(params[4]);
					actividad.setTonoActividad(params[5]);
					actividad.setEliminado(Boolean.parseBoolean(params[6]));
				}
				
			} catch (Exception ex) {
				Log.e("ServicioRest", "Error!", ex);
				resul = false;
			}
			return resul;
		}
		
		@Override
		protected void onPostExecute(Boolean resul) {
			// TODO Auto-generated method stub
			if (resul) {
				String campo_ide=String.valueOf(actividad.getIdActividad());
				Select laActividad = Select.from(TblActividades.class)
						.where(Condition.prop("ID_ACTIVIDAD").eq(campo_ide));
				
		    	TblActividades editar_actividad=(TblActividades)laActividad.first();
				
				editar_actividad.setIdActividad(actividad.getIdActividad());
				editar_actividad.setIdTipoActividad(actividad.getIdTipoActividad());
				editar_actividad.setNombreActividad(actividad.getNombreActividad());
				editar_actividad.setDetalleActividad(actividad.getDetalleActividad());
				editar_actividad.setImagenActividad(actividad.getImagenActividad());
				editar_actividad.setTonoActividad(actividad.getTonoActividad());
				editar_actividad.setEliminado(actividad.getEliminado());
				editar_actividad.save();
				
			}
		}
		
    }
    
    //TAREA ASINCRONA PARA BUSCAR UNA ACTIVIDAD
	public class BuscarUnaActividad extends AsyncTask<String, Integer, Boolean>{

		private TblActividades actividad= new TblActividades();

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			HttpClient httpClient= new DefaultHttpClient();
			String id=params[0];
			HttpGet BO= new HttpGet (
					"http://"+ip+"/ADP/Actividades/ActividadBuscarActividad/"+id);
			BO.setHeader("content-type", "application/json");

			try {
				HttpResponse resp=httpClient.execute(BO);
				String respStr=EntityUtils.toString(resp.getEntity());

				JSONObject respJSON = new JSONObject(respStr);

				actividad = new TblActividades();
				actividad.setIdActividad(respJSON.getLong("IdActividad"));
				actividad.setIdTipoActividad(respJSON.getLong("IdTipoActividad"));
				actividad.setNombreActividad(respJSON.getString("NombreActividad"));
				actividad.setDetalleActividad(respJSON.getString("DetalleActividad"));
				actividad.setImagenActividad(respJSON.getString("ImagenActividad"));
				actividad.setTonoActividad(respJSON.getString("TonoActividad"));
				actividad.setEliminado(respJSON.getBoolean("Eliminado"));

				if(actividad!=null){

					/*String campo_ide=String.valueOf(actividad.getIdActividad());
					Select laActividad = Select.from(TblActividades.class)
							.where(Condition.prop("ID_ACTIVIDAD").eq(campo_ide));

					TblActividades editar_actividad=(TblActividades)laActividad.first();

					if (editar_actividad!=null) {
						editar_actividad.setIdActividad(actividad.getIdActividad());
						editar_actividad.setIdTipoActividad(actividad.getIdTipoActividad());
						editar_actividad.setNombreActividad(actividad.getNombreActividad());
						editar_actividad.setDetalleActividad(actividad.getDetalleActividad());
						editar_actividad.setImagenActividad(actividad.getImagenActividad());
						editar_actividad.setTonoActividad(actividad.getTonoActividad());
						editar_actividad.setEliminado(actividad.getEliminado());
						editar_actividad.save();


					}else{*/
						TblActividades newActividad = new  TblActividades(
								actividad.getIdActividad(), actividad.getIdTipoActividad(),
								actividad.getNombreActividad(), actividad.getDetalleActividad(),
								actividad.getImagenActividad(), actividad.getTonoActividad(),
								actividad.getEliminado());
						newActividad.save();

					//}
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
			/*if (result) {
				String campo_ide=String.valueOf(actividad.getIdActividad());
				Select laActividad = Select.from(TblActividades.class)
						.where(Condition.prop("ID_ACTIVIDAD").eq(campo_ide));

		    	TblActividades editar_actividad=(TblActividades)laActividad.first();

				if (editar_actividad!=null) {
					editar_actividad.setIdActividad(actividad.getIdActividad());
					editar_actividad.setIdTipoActividad(actividad.getIdTipoActividad());
					editar_actividad.setNombreActividad(actividad.getNombreActividad());
					editar_actividad.setDetalleActividad(actividad.getDetalleActividad());
					editar_actividad.setImagenActividad(actividad.getImagenActividad());
					editar_actividad.setTonoActividad(actividad.getTonoActividad());
					editar_actividad.setEliminado(actividad.getEliminado());
					editar_actividad.save();


				}else{
					TblActividades newActividad = new  TblActividades(
							actividad.getIdActividad(), actividad.getIdTipoActividad(),
							actividad.getNombreActividad(), actividad.getDetalleActividad(),
							actividad.getImagenActividad(), actividad.getTonoActividad(),
							actividad.getEliminado());
					newActividad.save();

				}

			}*/
		}
	}
      
    //TAREA ASINCRONA PARA BUSCAR UNA FOTO DE ACTIVIDAD
    public class BuscarUnaFotoActividad extends AsyncTask<String, Integer, Boolean>{

		private TblActividades actividad= new TblActividades();
    	
    	@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			HttpClient httpClient= new DefaultHttpClient();
			String id=params[0];
			HttpGet BO= new HttpGet (
					"http://"+ip+"/ADP/Actividades/ActividadBuscarFotoActividad/"+id);
			BO.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BO);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONObject respJSON = new JSONObject(respStr);
				
				actividad.setIdActividad(Long.parseLong(params[0]));
				actividad.setImagenActividad(respJSON.getString("ImagenActividad"));

				/*if(respStr!=null){
					String campo_ide=String.valueOf(actividad.getIdActividad());

					Select laActividad = Select.from(TblActividades.class)
							.where(Condition.prop("ID_ACTIVIDAD").eq(campo_ide));
					TblActividades editar_actividad=(TblActividades)laActividad.first();

					editar_actividad.setImagenActividad(actividad.getImagenActividad());
					editar_actividad.save();
				}*/
				
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
				String campo_ide=String.valueOf(actividad.getIdActividad());
				
				Select laActividad = Select.from(TblActividades.class)
						.where(Condition.prop("ID_ACTIVIDAD").eq(campo_ide));
		    	TblActividades editar_actividad=(TblActividades)laActividad.first();
		    	
				editar_actividad.setImagenActividad(actividad.getImagenActividad());
				editar_actividad.save();				
			}
		}
    }
        
    //TAREA SINCRONA PARA BUSCAR TODAS LAS ACTIVIDADES
	public class BuscarAllActividades extends AsyncTask<String, Integer, Boolean>{

		private List<TblActividades> actividades=new ArrayList<TblActividades>();

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			String id=params[0];
			HttpClient httpClient= new DefaultHttpClient();
			HttpGet BT= new HttpGet (
					"http://"+ip+"/ADP/Actividades/ActividadBuscarActividades/"+id);
			BT.setHeader("content-type", "application/json");

			try {
				HttpResponse resp=httpClient.execute(BT);
				String respStr=EntityUtils.toString(resp.getEntity());

				JSONArray respJSON = new JSONArray(respStr);

				if (!respStr.isEmpty()) {

					//TblActividades.deleteAll(TblActividades.class);
					for (int i = 0; i <respJSON.length(); i++) {
						JSONObject obj=respJSON.getJSONObject(i);

						TblActividades actividad=new TblActividades();
						actividad.setIdActividad(obj.getLong("IdActividad"));
						actividad.setIdTipoActividad(obj.getLong("IdTipoActividad"));
						actividad.setNombreActividad(obj.getString("NombreActividad"));
						actividad.setDetalleActividad(obj.getString("DetalleActividad"));
						actividad.setImagenActividad(obj.getString("ImagenActividad"));
						actividad.setTonoActividad(obj.getString("TonoActividad"));
						actividad.setEliminado(obj.getBoolean("Eliminado"));
						actividades.add(actividad);

						if (respStr!=null) {

							//for (int j = 0; j < actividades.size(); j++) {

								/*Select act = Select.from(TblActividades.class)
										.where(Condition.prop("ID_ACTIVIDAD").eq(actividad.getIdActividad().toString()));
								TblActividades editar_act=(TblActividades)act.first();

								if(editar_act!=null){

									editar_act.setIdActividad(actividad.getIdActividad());
									editar_act.setIdTipoActividad(actividad.getIdTipoActividad());
									editar_act.setNombreActividad(actividad.getNombreActividad());
									editar_act.setDetalleActividad(actividad.getDetalleActividad());
									editar_act.setImagenActividad(actividad.getImagenActividad());
									editar_act.setTonoActividad(actividad.getTonoActividad());
									editar_act.setEliminado(actividad.getEliminado());
									editar_act.save();
								}
								else{*/
							TblActividades guardar_act = new TblActividades(
									actividad.getIdActividad(), actividad.getIdTipoActividad(),
									actividad.getNombreActividad(), actividad.getDetalleActividad(),
									actividad.getImagenActividad(), actividad.getTonoActividad(),
									actividad.getEliminado());
							guardar_act.save();
							//}

							if(actividad.getIdTipoActividad()==5){
								String ids=String.valueOf(actividad.getIdActividad());
								HttpClient httpClient1= new DefaultHttpClient();
								HttpGet BO= new HttpGet (
										"http://"+ip+"/ADP/Actividades/ActividadBuscarFotoActividad/"+ids);
								BO.setHeader("content-type", "application/json");

								try {
									HttpResponse resp1=httpClient1.execute(BO);
									String respStr1=EntityUtils.toString(resp1.getEntity());

									JSONObject respJSON1 = new JSONObject(respStr1);

									actividad.setImagenActividad(respJSON1.getString("ImagenActividad"));

									if(respStr1!=null){
										String campo_ide=String.valueOf(actividad.getIdActividad());

										Select laActividad = Select.from(TblActividades.class)
												.where(Condition.prop("ID_ACTIVIDAD").eq(campo_ide));
										TblActividades editar_actividad=(TblActividades)laActividad.first();

										editar_actividad.setImagenActividad(actividad.getImagenActividad());
										editar_actividad.save();
									}

								} catch (Exception ex) {
									// TODO: handle exception
									Log.e("Serviciorest","Error!",ex);
									resul=false;
								}
							}
							//}
						}

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
			if (result) {

    			/*for (int i = 0; i < actividades.size(); i++) {

    				TblActividades actividad = actividades.get(i);
					TblActividades actividad = new TblActividades();
    				actividad.setIdActividad(actividades.get(i).getId());
    				actividad.setIdTipoActividad(actividades.get(i).getIdTipoActividad());
    				actividad.setNombreActividad(actividades.get(i).getNombreActividad());
    				actividad.setDetalleActividad(actividades.get(i).getDetalleActividad());
    				actividad.setImagenActividad(actividades.get(i).getImagenActividad());
    				actividad.setTonoActividad(actividades.get(i).getTonoActividad());
    				actividad.setEliminado(actividades.get(i).getEliminado());

					Select act = Select.from(TblActividades.class)
							.where(Condition.prop("ID_ACTIVIDAD").eq(actividad.getIdActividad().toString()));
			    	TblActividades editar_act=(TblActividades)act.first();

					if(editar_act!=null){

						editar_act.setIdActividad(actividad.getIdActividad());
						editar_act.setIdTipoActividad(actividad.getIdTipoActividad());
						editar_act.setNombreActividad(actividad.getNombreActividad());
						editar_act.setDetalleActividad(actividad.getDetalleActividad());
						editar_act.setImagenActividad(actividad.getImagenActividad());
						editar_act.setTonoActividad(actividad.getTonoActividad());
						editar_act.setEliminado(actividad.getEliminado());
						editar_act.save();
					}
					else{
						TblActividades guardar_act = new TblActividades(
								actividad.getIdActividad(), actividad.getIdTipoActividad(),
								actividad.getNombreActividad(), actividad.getDetalleActividad(),
								actividad.getImagenActividad(), actividad.getTonoActividad(),
								actividad.getEliminado());
						guardar_act.save();
					}

					BuscarUnaFotoActividad buscar= new BuscarUnaFotoActividad();
					buscar.execute(actividad.getIdActividad().toString());
				}*/
			}
		}
	}
    
    
  //TAREA SINCRONA PARA BUSCAR TODAS LAS ACTIVIDADES
  public class BuscarActividadesPrincipales extends AsyncTask<String, Integer, Boolean>{

	  private List<TblActividades> actividades=new ArrayList<TblActividades>();
	  TblActividades actividad;

	  @Override
	  protected Boolean doInBackground(String... params) {
		  // TODO Auto-generated method stub
		  boolean resul=true;
		  HttpClient httpClient= new DefaultHttpClient();
		  HttpGet BT= new HttpGet (
				  "http://"+ip+"/ADP/Actividades/ActividadBuscarActividadesPrincipales");
		  BT.setHeader("content-type", "application/json");

		  try {
			  HttpResponse resp=httpClient.execute(BT);
			  String respStr=EntityUtils.toString(resp.getEntity());

			  JSONArray respJSON = new JSONArray(respStr);

			  if (!respStr.isEmpty()) {

				  //TblActividades.deleteAll(TblActividades.class);

				  for (int i = 0; i <respJSON.length(); i++) {
					  JSONObject obj=respJSON.getJSONObject(i);

					  actividad=new TblActividades();
					  actividad.setIdActividad(obj.getLong("IdActividad"));
					  actividad.setIdTipoActividad(obj.getLong("IdTipoActividad"));
					  actividad.setNombreActividad(obj.getString("NombreActividad"));
					  actividad.setDetalleActividad(obj.getString("DetalleActividad"));
					  actividad.setImagenActividad(obj.getString("ImagenActividad"));
					  actividad.setTonoActividad(obj.getString("TonoActividad"));
					  actividad.setEliminado(obj.getBoolean("Eliminado"));
					  actividades.add(actividad);

					  if (respStr!=null) {

						  //for (int j = 0; j < actividades.size(); j++) {

								/*Select act = Select.from(TblActividades.class)
										.where(Condition.prop("ID_ACTIVIDAD").eq(actividad.getIdActividad().toString()));
								TblActividades editar_act=(TblActividades)act.first();

								if(editar_act!=null){

									editar_act.setIdActividad(actividad.getIdActividad());
									editar_act.setIdTipoActividad(actividad.getIdTipoActividad());
									editar_act.setNombreActividad(actividad.getNombreActividad());
									editar_act.setDetalleActividad(actividad.getDetalleActividad());
									editar_act.setImagenActividad(actividad.getImagenActividad());
									editar_act.setTonoActividad(actividad.getTonoActividad());
									editar_act.setEliminado(actividad.getEliminado());
									editar_act.save();
								}
								else{*/
						  TblActividades guardar_act = new TblActividades(
								  actividad.getIdActividad(), actividad.getIdTipoActividad(),
								  actividad.getNombreActividad(), actividad.getDetalleActividad(),
								  actividad.getImagenActividad(), actividad.getTonoActividad(),
								  actividad.getEliminado());
						  guardar_act.save();
						  //}

						  String id=String.valueOf(actividad.getIdActividad());
						  HttpClient httpClient1= new DefaultHttpClient();
						  HttpGet BO= new HttpGet (
								  "http://"+ip+"/ADP/Actividades/ActividadBuscarFotoActividad/"+id);
						  BO.setHeader("content-type", "application/json");

						  try {
							  HttpResponse resp1=httpClient1.execute(BO);
							  String respStr1=EntityUtils.toString(resp1.getEntity());

							  JSONObject respJSON1 = new JSONObject(respStr1);

							  actividad.setImagenActividad(respJSON1.getString("ImagenActividad"));

							  if(respStr1!=null){
								  String campo_ide=String.valueOf(actividad.getIdActividad());

								  Select laActividad = Select.from(TblActividades.class)
										  .where(Condition.prop("ID_ACTIVIDAD").eq(campo_ide));
								  TblActividades editar_actividad=(TblActividades)laActividad.first();

								  editar_actividad.setImagenActividad(actividad.getImagenActividad());
								  editar_actividad.save();
							  }

						  } catch (Exception ex) {
							  // TODO: handle exception
							  Log.e("Serviciorest","Error!",ex);
							  resul=false;
						  }
						  //}
					  }

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
		  if (result) {

    			/*for (int i = 0; i < actividades.size(); i++) {

    				TblActividades actividad = actividades.get(i);
					TblActividades actividad = new TblActividades();
    				actividad.setIdActividad(actividades.get(i).getIdActividad());
    				actividad.setIdTipoActividad(actividades.get(i).getIdTipoActividad());
    				actividad.setNombreActividad(actividades.get(i).getNombreActividad());
    				actividad.setDetalleActividad(actividades.get(i).getDetalleActividad());
    				actividad.setImagenActividad(actividades.get(i).getImagenActividad());
    				actividad.setTonoActividad(actividades.get(i).getTonoActividad());
    				actividad.setEliminado(actividades.get(i).getEliminado());

					Select act = Select.from(TblActividades.class)
							.where(Condition.prop("ID_ACTIVIDAD").eq(actividad.getIdActividad()));
			    	TblActividades editar_act=(TblActividades)act.first();

					if(editar_act!=null){

						editar_act.setIdActividad(actividad.getIdActividad());
						editar_act.setIdTipoActividad(actividad.getIdTipoActividad());
						editar_act.setNombreActividad(actividad.getNombreActividad());
						editar_act.setDetalleActividad(actividad.getDetalleActividad());
						editar_act.setImagenActividad(actividad.getImagenActividad());
						editar_act.setTonoActividad(actividad.getTonoActividad());
						editar_act.setEliminado(actividad.getEliminado());
						editar_act.save();
					}
					else{
						TblActividades guardar_act = new TblActividades(
								actividad.getIdActividad(), actividad.getIdTipoActividad(),
								actividad.getNombreActividad(), actividad.getDetalleActividad(),
								actividad.getImagenActividad(), actividad.getTonoActividad(),
								actividad.getEliminado());
						guardar_act.save();
					}

					BuscarUnaFotoActividad buscar= new BuscarUnaFotoActividad();
					buscar.execute(actividad.getIdActividad().toString());
				}*/
		  }
	  }
  }
    
    //TAREA ASINCRONA PARA ELIMINAR ACTIVIDADES
    public class EliminarActividades extends AsyncTask<String, Integer, Boolean>{

    	private String id="";
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=false;
			HttpClient httpClient= new DefaultHttpClient();
			id=params[0];
			HttpDelete del= new HttpDelete (
					"http://"+ip+"/ADP/Actividades/ActividadEliminar/"+id);
			del.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(del);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				if (respStr.equals("true")) {
					resul=true;
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
				TblActividades actividades = new TblActividades();
				actividades.EliminarPorIdActividadRegTblActividades(Long.parseLong(id));
			}
		}
    }

	public JsonArrayRequest AB_Actividades(long idC, final String TAG){
		String urlJsonArray = "http://"+ip+"ADP/Actividades/ActividadBuscarActividades/"+String.valueOf(idC);
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
								TblActividades actividad=new TblActividades();
								actividad.setIdActividad(obj.getLong("IdActividad"));
								actividad.setIdTipoActividad(obj.getLong("IdTipoActividad"));
								actividad.setNombreActividad(obj.getString("NombreActividad"));
								actividad.setDetalleActividad(obj.getString("DetalleActividad"));
								actividad.setImagenActividad(obj.getString("ImagenActividad"));
								actividad.setTonoActividad(obj.getString("TonoActividad"));
								actividad.setEliminado(obj.getBoolean("Eliminado"));

								/*String idTipoActividadStr=String.valueOf(actividad.getIdTipoActividad());
								if(idTipoActividadStr.equals("5")){
									actividad.setImagenActividad(fotoActividad(actividad.getIdTipoActividad(),TAG));
								}*/

								actividad.save();
								Log.e("Actividad => Registro", "#" + i + "= " + actividad);
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

	//BUSCAR UNA FOTO ACTIVIDAD
	public ImageRequest BuscarUnaFotoActividad(final String ... params){
		String id=params[0];
		final String TAG=params[1];
		String url = "http://"+ip+"/ADP/Actividades/ActividadBuscarFotoActividad/"+id;

		ImageRequest request = new ImageRequest(url, new Response.Listener<Bitmap>() {
			@Override
			public void onResponse(Bitmap response) {
				Log.d(TAG, "ImageRequest completa");
				//convertir de bitmap a string y guardar

				TblActividades actividad = new TblActividades();
				actividad.setImagenActividad("");

				String campo_ide=String.valueOf(Long.parseLong(params[0]));
				Select laActividad = Select.from(TblActividades.class)
						.where(Condition.prop("ID_ACTIVIDAD").eq(campo_ide));
				TblActividades editar_actividad=(TblActividades)laActividad.first();

				editar_actividad.setImagenActividad(actividad.getImagenActividad());
				editar_actividad.save();
			}
		} , 0, 0, null,
				new Response.ErrorListener() {
					public void onErrorResponse(VolleyError error) {
						Log.d(TAG, "Error en ImageRequest");
					}
				});
		return request;
	}
    
}
