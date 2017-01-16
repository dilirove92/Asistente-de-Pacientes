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
import com.Notifications.patientssassistant.tables.TblControlDieta;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.orm.query.Condition;
import com.orm.query.Select;


public class ATControlDieta {
	
	//VARIABLES DE CLASE ATControlDieta
	//private static String ip="192.168.1.4:1522";
	private static String ip=VarEstatic.ObtenerIP();

	//TAREA ASINCRONA PARA INSERTAR UN NUEVO CONTROL DE DIETA
	public class InsertarControlDieta extends AsyncTask<String, Integer, Long>
	{
		private TblControlDieta contDieta;
		private long id=0;

		@Override
		protected Long doInBackground(String... params) {
			// TODO Auto-generated method stub
			Boolean resul=true;
			long respuesta=0;
			HttpClient httpClient=new DefaultHttpClient();

			HttpPost post=new HttpPost(
					"http://"+ip+"/ADP/ControlDieta/ControlDietaInsertar");
			post.setHeader("content-type", "application/json");

			try {
				JSONObject dato=new JSONObject();

				dato.put("IdControlDieta", Long.parseLong(params[0]));
				dato.put("IdPaciente", Long.parseLong(params[1]));
				dato.put("Motivo", params[2]);
				dato.put("AlimentosRecomendados", params[3]);
				dato.put("AlimentosNoAdecuados", params[4]);
				dato.put("Eliminado",Boolean.parseBoolean(params[5]));

				StringEntity entity = new StringEntity(dato.toString());
				post.setEntity(entity);

				HttpResponse resp=httpClient.execute(post);
				String respStr = EntityUtils.toString(resp.getEntity());
				respuesta=Long.parseLong(respStr);

				if (respuesta>0){
					contDieta = new TblControlDieta(
								respuesta, Long.parseLong(params[1]),
								params[2], params[3], params[4],
								Boolean.parseBoolean(params[5]));
					contDieta.save();
				}else{
					resul=false;
				}

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


	//TAREA ASINCRONA PARA ACTUALIZAR UN REGISTRO DE CONTROL DIETA
	public class ActualizarControlDieta extends AsyncTask<String, Integer, Boolean>
	{
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			Boolean resul=true;
			HttpClient httpClient=new DefaultHttpClient();

			HttpPut put=new HttpPut(
					"http://"+ip+"/ADP/ControlDieta/ControlDietaActualizar");
			put.setHeader("content-type", "application/json");

			try {
				JSONObject dato=new JSONObject();

				dato.put("IdControlDieta", Long.parseLong(params[0]));
				dato.put("IdPaciente", Long.parseLong(params[1]));
				dato.put("Motivo", params[2]);
				dato.put("AlimentosRecomendados", params[3]);
				dato.put("AlimentosNoAdecuados", params[4]);
				dato.put("Eliminado",Boolean.parseBoolean(params[5]));

				StringEntity entity = new StringEntity(dato.toString());
				put.setEntity(entity);

				HttpResponse resp=httpClient.execute(put);
				String respStr = EntityUtils.toString(resp.getEntity());

				if (!respStr.equals("true")) {
					resul=false;
				}else{
					String campo_ide=String.valueOf(Long.parseLong(params[0]));
					Select contDietas = Select.from(TblControlDieta.class)
							.where(Condition.prop("ID_CONTROL_DIETA").eq(campo_ide));
					TblControlDieta editar_controlDieta=(TblControlDieta)contDietas.first();

					if (editar_controlDieta!=null) {
						editar_controlDieta.setIdControlDieta(Long.parseLong(params[0]));
						editar_controlDieta.setIdPaciente(Long.parseLong(params[1]));
						editar_controlDieta.setMotivo(params[2]);
						editar_controlDieta.setAlimentosRecomendados(params[3]);
						editar_controlDieta.setAlimentosNoAdecuados(params[4]);
						editar_controlDieta.setEliminado(Boolean.parseBoolean(params[5]));
						editar_controlDieta.save();
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

	//TAREA ASINCRONOA PARA BUSCAR UN REGISTRO DE CONTROL DIETA
	public class BuscarUnControlDieta extends AsyncTask<String, Integer, Boolean>{

		private TblControlDieta contDieta= new TblControlDieta();

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			HttpClient httpClient= new DefaultHttpClient();
			String id=params[0];
			HttpGet BO= new HttpGet (
					"http://"+ip+"/ADP/ControlDieta/ControlDietaBuscar/"+id);
			BO.setHeader("content-type", "application/json");

			try {
				HttpResponse resp=httpClient.execute(BO);
				String respStr=EntityUtils.toString(resp.getEntity());

				JSONObject respJSON = new JSONObject(respStr);

				contDieta.setIdControlDieta(respJSON.getLong("IdControlDieta"));
				contDieta.setIdPaciente(respJSON.getLong("IdPaciente"));
				contDieta.setMotivo(respJSON.getString("Motivo"));
				contDieta.setAlimentosRecomendados(respJSON.getString("AlimentosRecomendados"));
				contDieta.setAlimentosNoAdecuados(respJSON.getString("AlimentosNoAdecuados"));
				contDieta.setEliminado(respJSON.getBoolean("Eliminado"));

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
				String campo_ide=String.valueOf(contDieta.getIdControlDieta());
				Select contDietas = Select.from(TblControlDieta.class)
						.where(Condition.prop("ID_CONTROL_DIETA").eq(campo_ide));
				TblControlDieta editar_controlDieta=(TblControlDieta)contDietas.first();

				if (editar_controlDieta!=null) {
					editar_controlDieta.setIdControlDieta(contDieta.getIdControlDieta());
					editar_controlDieta.setIdPaciente(contDieta.getIdPaciente());
					editar_controlDieta.setMotivo(contDieta.getMotivo());
					editar_controlDieta.setAlimentosRecomendados(contDieta.getAlimentosRecomendados());
					editar_controlDieta.setAlimentosNoAdecuados(contDieta.getAlimentosNoAdecuados());
					editar_controlDieta.setEliminado(contDieta.getEliminado());
					editar_controlDieta.save();
				}
				else{
					TblControlDieta newControl = new TblControlDieta(
							contDieta.getIdControlDieta(), contDieta.getIdPaciente(),
							contDieta.getMotivo(), contDieta.getAlimentosRecomendados(),
							contDieta.getAlimentosNoAdecuados(), contDieta.getEliminado());
					newControl.save();
				}
			}
		}
	}

	//TAREA ASINCRONA PARA BUSCAR LOS CONTROLES DE DIETAS POR PACIENTE
	public class BuscarAllControlDietaPaciente extends AsyncTask<String, Integer, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			String idP=params[0];
			HttpClient httpClient= new DefaultHttpClient();
			HttpGet BT= new HttpGet (
					"http://"+ip+"/ADP/ControlDieta/ControlDietaBuscarAllXPaciente/"+idP);
			BT.setHeader("content-type", "application/json");

			try {
				HttpResponse resp=httpClient.execute(BT);
				String respStr=EntityUtils.toString(resp.getEntity());

				JSONArray respJSON = new JSONArray(respStr);

				for (int i = 0; i <respJSON.length(); i++) {
					JSONObject obj=respJSON.getJSONObject(i);

					TblControlDieta act=new TblControlDieta();
					act.setIdControlDieta(obj.getLong("IdControlDieta"));
					act.setIdPaciente(obj.getLong("IdPaciente"));
					act.setMotivo(obj.getString("Motivo"));
					act.setAlimentosRecomendados(obj.getString("AlimentosRecomendados"));
					act.setAlimentosNoAdecuados(obj.getString("AlimentosNoAdecuados"));
					act.setEliminado(obj.getBoolean("Eliminado"));

					TblControlDieta guardar_ctDieta = new TblControlDieta(
							act.getIdControlDieta(), act.getIdPaciente(),
							act.getMotivo(), act.getAlimentosRecomendados(),
							act.getAlimentosNoAdecuados(), act.getEliminado());
					guardar_ctDieta.save();

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

	//TAREA ASINCRONA PRA ELIMINAR UN CONTROL DE DIETA
	public class EliminarControlDieta extends AsyncTask<String, Integer, Boolean>{

		private String id="";

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			HttpClient httpClient= new DefaultHttpClient();
			id=params[0];
			HttpDelete del= new HttpDelete (
					"http://"+ip+"/ADP/ControlDieta/ControlDietaEliminar/"+id);
			del.setHeader("content-type", "application/json");

			try {
				HttpResponse resp=httpClient.execute(del);
				String respStr=EntityUtils.toString(resp.getEntity());

				if (!respStr.equals("true")) {
					resul=false;
				}else{
					TblControlDieta controlDieta = new TblControlDieta();
					controlDieta.EliminarPorIdControlDietaRegTblControlDieta(Long.parseLong(id));
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
	//TAREA ASINCRONA PARA BUSCAR LOS CONTROLES DE DIETAS POR PACIENTE
	public class BuscarAllControlDietaCuidadores extends AsyncTask<String, Integer, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			String idC=params[0];
			HttpClient httpClient= new DefaultHttpClient();
			HttpGet BT= new HttpGet (
					"http://"+ip+"/ADP/ControlDieta/ControlDietaBuscarAllXPaciente/"+idC);
			BT.setHeader("content-type", "application/json");

			try {
				HttpResponse resp=httpClient.execute(BT);
				String respStr=EntityUtils.toString(resp.getEntity());

				JSONArray respJSON = new JSONArray(respStr);

				for (int i = 0; i <respJSON.length(); i++) {
					JSONObject obj=respJSON.getJSONObject(i);

					TblControlDieta act=new TblControlDieta();
					act.setIdControlDieta(obj.getLong("IdControlDieta"));
					act.setIdPaciente(obj.getLong("IdPaciente"));
					act.setMotivo(obj.getString("Motivo"));
					act.setAlimentosRecomendados(obj.getString("AlimentosRecomendados"));
					act.setAlimentosNoAdecuados(obj.getString("AlimentosNoAdecuados"));
					act.setEliminado(obj.getBoolean("Eliminado"));

					TblControlDieta guardar_ctDieta = new TblControlDieta(
							act.getIdControlDieta(), act.getIdPaciente(),
							act.getMotivo(), act.getAlimentosRecomendados(),
							act.getAlimentosNoAdecuados(), act.getEliminado());
					guardar_ctDieta.save();

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

		public JsonArrayRequest AB_ControlDieta(long idC, final String TAG){
			String urlJsonArray = "http://"+ip+"/ADP/ControlDieta/ControlDietaBuscarAllXPaciente/"+String.valueOf(idC);
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

									TblControlDieta act=new TblControlDieta();
									act.setIdControlDieta(obj.getLong("IdControlDieta"));
									act.setIdPaciente(obj.getLong("IdPaciente"));
									act.setMotivo(obj.getString("Motivo"));
									act.setAlimentosRecomendados(obj.getString("AlimentosRecomendados"));
									act.setAlimentosNoAdecuados(obj.getString("AlimentosNoAdecuados"));
									act.setEliminado(obj.getBoolean("Eliminado"));
									act.save();

									Log.e("Cont_Dieta => Registro", "#" + i + "= " + act);
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

}