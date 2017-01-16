package com.Notifications.patientssassistant.asynctask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
import com.Notifications.patientssassistant.tables.TblActividadCuidador;
import com.Notifications.patientssassistant.tables.TblBuzon;
import com.Notifications.patientssassistant.tables.TblControlDieta;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import android.os.AsyncTask;
import android.util.Log;

public class ATBuzon {
	
	//VARIABLES DE CLASE ATBuzon
	//private static String ip="192.168.1.4:1522";	
	private static String ip=VarEstatic.ObtenerIP();
	public static SimpleDateFormat sfDate =  new  SimpleDateFormat ("yyy-MM-dd");
	
	//TAREA ASINCRONA PARA INSERTAR UNA NUEVA NOTIFICACION EN EL BUZON
	public class InsertarBuzon extends AsyncTask<String, Integer, Boolean>
    {
		public TblBuzon buzon;
		
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			Boolean resul=true;
			
			HttpClient httpClient=new DefaultHttpClient();
			
			HttpPost post=new HttpPost(
					"http://"+ip+"/ADP/Buzon/BuzonInsertar");
			post.setHeader("content-type", "application/json");
			
			try {
				JSONObject dato=new JSONObject();
				
				//Date f=sfDate.parse(params[1]);
				
				dato.put("IdCuidador", Long.parseLong(params[0]));
				//dato.put("Fecha", f);
				//dato.put("Hora", params[2]);
				dato.put("IdPaciente", Long.parseLong(params[1]));
				dato.put("IdActividad", Long.parseLong(params[2]));
				dato.put("FechaString", params[3]);
				dato.put("Anio", params[2]);
				dato.put("Mes", params[4]);
				dato.put("Dia", params[5]);
				dato.put("Horas", params[6]);
				dato.put("Minutos", params[7]);
				dato.put("Eliminado", Boolean.parseBoolean(params[8]));

				StringEntity entity = new StringEntity(dato.toString());
				post.setEntity(entity);
				
				HttpResponse resp=httpClient.execute(post);
				String respStr = EntityUtils.toString(resp.getEntity());
				
				if (!respStr.equals("true")) {
					resul=false;
				}else{
					buzon = new TblBuzon(
							Long.parseLong(params[0]),
							Long.parseLong(params[1]),
							Long.parseLong(params[2]),
							Integer.parseInt(params[3]),
							Integer.parseInt(params[4]),
							Integer.parseInt(params[5]),
							Integer.parseInt(params[6]),
							Integer.parseInt(params[7]),
							Boolean.parseBoolean(params[8]),
							false);
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
			buzon.save();
		}
		
    }  
	
	//TAREA ASINCRONA PARA INSERTAR UNA NUEVA NOTIFICACION EN EL BUZON
		public class EnviarPeticion extends AsyncTask<String, Integer, Boolean>
	    {
			public TblBuzon buzon;
			
			@Override
			protected Boolean doInBackground(String... params) {
				// TODO Auto-generated method stub
				Boolean resul=true;
				
				HttpClient httpClient=new DefaultHttpClient();
				
				HttpPost post=new HttpPost(
						"http://"+ip+"/ADP/Buzon/BuzonPeticiones");
				post.setHeader("content-type", "application/json");
				
				try {
					JSONObject dato=new JSONObject();


					dato.put("IdCuidador", Long.parseLong(params[0]));
					//dato.put("Hora", params[2]);
					dato.put("IdPaciente", Long.parseLong(params[1]));
					dato.put("IdActividad", Long.parseLong(params[2]));
					//dato.put("FechaString", params[1]);
					dato.put("Anio", Integer.parseInt(params[3]));
					dato.put("Mes", Integer.parseInt(params[4]));
					dato.put("Dia", Integer.parseInt(params[5]));
					dato.put("Horas", Integer.parseInt(params[6]));
					dato.put("Minutos", Integer.parseInt(params[7]));
					dato.put("Eliminado", Boolean.parseBoolean(params[8]));
					
					StringEntity entity = new StringEntity(dato.toString());
					post.setEntity(entity);
					
					HttpResponse resp=httpClient.execute(post);
					String respStr = EntityUtils.toString(resp.getEntity());

					
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

	//TAREA ASINCRONA PARA BUSCAR TODAS LAS NOTIFICACIONES DE BUZON
	public class BuscarAllBuzonesXCuidador extends AsyncTask<String, Integer, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			String idC=params[0];
			HttpClient httpClient= new DefaultHttpClient();
			HttpGet BT= new HttpGet (
					"http://"+ip+"/ADP/Buzon/BuzonBuscarXCuidador/"+idC);
			BT.setHeader("content-type", "application/json");

			try {
				HttpResponse resp=httpClient.execute(BT);
				String respStr=EntityUtils.toString(resp.getEntity());

				JSONArray respJSON = new JSONArray(respStr);

				for (int i = 0; i <respJSON.length(); i++) {
					JSONObject obj=respJSON.getJSONObject(i);

					//String dateString=obj.getString("Fecha").substring(6, 18);
					//long Date1 = Long.parseLong(dateString);
					//Date netDate = (new Date(Date1));

					TblBuzon buzon= new TblBuzon();
					buzon.setIdCuidador(obj.getLong("IdCuidador"));
					//buzon.setFecha(netDate);
					//buzon.setHora(obj.getString("Hora"));
					buzon.setIdPaciente(obj.getLong("IdPaciente"));
					buzon.setIdActividad(obj.getLong("IdActividad"));
					buzon.setAnio(obj.getInt("Anio"));
					buzon.setMes(obj.getInt("Mes"));
					buzon.setDia(obj.getInt("Dia"));
					buzon.setHoras(obj.getInt("Horas"));
					buzon.setMinutos(obj.getInt("Minutos"));
					buzon.setEliminado(obj.getBoolean("Eliminado"));

					TblBuzon guardar_buzon = new TblBuzon(
							//buzon.getIdCuidador(), buzon.getFecha(), buzon.getHora(),
							buzon.getIdCuidador(), buzon.getIdPaciente(),
							buzon.getIdActividad(), buzon.getAnio(),
							buzon.getMes(), buzon.getDia(), buzon.getHoras(),
							buzon.getMinutos(), buzon.getEliminado(), false);
					guardar_buzon.save();
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

				//Toast.makeText(MainActivity.this, "All Selection ", 3000).show();
			}
		}
	}

	//TAREA ASINCRONA PARA BORRAR BUZON CADA 2 DIAS
	public class BorrarBuzon extends AsyncTask<String, Integer, Boolean>
	{
		public TblBuzon buzon;

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			Boolean resul=true;
			HttpClient httpClient=new DefaultHttpClient();

			HttpPost post=new HttpPost(
					"http://"+ip+"/ADP/Buzon/BuzonEliminar");
			post.setHeader("content-type", "application/json");

			try {
				JSONObject dato=new JSONObject();

				//Calendar cal = new GregorianCalendar();
				//Date date = cal.getTime();
				//String dateString =date.toGMTString();
				Calendar cal = new GregorianCalendar();
				cal.getTime();
				int anio = cal.get(Calendar.YEAR);
				int mes = cal.get(Calendar.MONTH);
				int dia = cal.get(Calendar.DAY_OF_MONTH);
				//String laFecha=String.valueOf(anio)+"/"+String.valueOf(mes)+"/"+String.valueOf(dia);
				int horas = cal.get(Calendar.HOUR_OF_DAY);
				int minutos = cal.get(Calendar.MINUTE);
				//String laHora=String.valueOf(hora)+":"+String.valueOf(minutos);

				dato.put("IdCuidador", Long.parseLong(params[0]));
				dato.put("Anio", anio);
				dato.put("Mes", mes);
				dato.put("Dia", dia);
				dato.put("Horas", horas);
				dato.put("Minutos", minutos);

				StringEntity entity = new StringEntity(dato.toString());
				post.setEntity(entity);

				HttpResponse resp=httpClient.execute(post);
				String respStr = EntityUtils.toString(resp.getEntity());

				if (!respStr.equals("true")) {
					resul=false;
				}else{
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
			//Se actualiza despues
		}

	}

	//TAREA ASINCRONA PARA BORRAR BUZON DE BD CADA MES
	public class BorrarBuzonBD extends AsyncTask<String, Integer, Boolean>
	{
		public TblBuzon buzon;

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			Boolean resul=true;
			HttpClient httpClient=new DefaultHttpClient();

			HttpPost post=new HttpPost(
					"http://"+ip+"/ADP/Buzon/BuzonEliminar");
			post.setHeader("content-type", "application/json");

			try {
				JSONObject dato=new JSONObject();

				//Calendar cal = new GregorianCalendar();
				//Date date = cal.getTime();
				//String dateString =date.toGMTString();
				Calendar cal = new GregorianCalendar();
				cal.getTime();
				int anio = cal.get(Calendar.YEAR);
				int mes = cal.get(Calendar.MONTH);
				int dia = cal.get(Calendar.DAY_OF_MONTH);
				//String laFecha=String.valueOf(anio)+"/"+String.valueOf(mes)+"/"+String.valueOf(dia);
				int horas = cal.get(Calendar.HOUR_OF_DAY);
				int minutos = cal.get(Calendar.MINUTE);
				//String laHora=String.valueOf(hora)+":"+String.valueOf(minutos);

				dato.put("IdCuidador", Long.parseLong(params[0]));
				dato.put("IdCuidador", Long.parseLong(params[0]));
				dato.put("Anio", anio);
				dato.put("Mes", mes);
				dato.put("Dia", dia);
				dato.put("Horas", horas);
				dato.put("Minutos", minutos);

				StringEntity entity = new StringEntity(dato.toString());
				post.setEntity(entity);

				HttpResponse resp=httpClient.execute(post);
				String respStr = EntityUtils.toString(resp.getEntity());

				if (!respStr.equals("true")) {
					resul=false;
				}else{
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
			//Se actualiza despues
		}

	}

	//UTILIZADO PARA ACTUALIZAR LA BD
	//TAREA ASINCRONA PARA BUSCAR TODAS LAS NOTIFICACIONES DE BUZON
	public class BuscarAllBuzonesXCuidadores extends AsyncTask<String, Integer, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			String idC=params[0];
			HttpClient httpClient= new DefaultHttpClient();
			HttpGet BT= new HttpGet (
					"http://"+ip+"/ADP/Buzon/BuzonBuscarXCuidadores/"+idC);
			BT.setHeader("content-type", "application/json");

			try {
				HttpResponse resp=httpClient.execute(BT);
				String respStr=EntityUtils.toString(resp.getEntity());

				JSONArray respJSON = new JSONArray(respStr);

				for (int i = 0; i <respJSON.length(); i++) {
					JSONObject obj=respJSON.getJSONObject(i);

					//String dateString=obj.getString("Fecha").substring(6, 18);
					//long Date1 = Long.parseLong(dateString);
					//Date netDate = (new Date(Date1));

					TblBuzon buzon= new TblBuzon();
					buzon.setIdCuidador(obj.getLong("IdCuidador"));
					//buzon.setFecha(netDate);
					//buzon.setHora(obj.getString("Hora"));
					buzon.setIdPaciente(obj.getLong("IdPaciente"));
					buzon.setIdActividad(obj.getLong("IdActividad"));
					buzon.setAnio(obj.getInt("Anio"));
					buzon.setMes(obj.getInt("Mes"));
					buzon.setDia(obj.getInt("Dia"));
					buzon.setHoras(obj.getInt("Horas"));
					buzon.setMinutos(obj.getInt("Minutos"));
					buzon.setEliminado(obj.getBoolean("Eliminado"));

					TblBuzon guardar_buzon = new TblBuzon(
							//buzon.getIdCuidador(), buzon.getFecha(), buzon.getHora(),
							buzon.getIdCuidador(), buzon.getIdPaciente(),
							buzon.getIdActividad(), buzon.getAnio(),
							buzon.getMes(), buzon.getDia(), buzon.getHoras(),
							buzon.getMinutos(), buzon.getEliminado(), false);;
					guardar_buzon.save();
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

				//Toast.makeText(MainActivity.this, "All Selection ", 3000).show();
			}
		}
	}

	public JsonArrayRequest AB_Buzon(long idC, final String TAG){
		String urlJsonArray = "http://"+ip+"/ADP/Buzon/BuzonBuscarXCuidadores/"+String.valueOf(idC);
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

								//String dateString=obj.getString("Fecha").substring(6, 18);
								//long Date1 = Long.parseLong(dateString);
								//Date netDate = (new Date(Date1));

								TblBuzon buzon= new TblBuzon();
								buzon.setIdCuidador(obj.getLong("IdCuidador"));
								//buzon.setFecha(netDate);
								//buzon.setHora(obj.getString("Hora"));
								buzon.setIdPaciente(obj.getLong("IdPaciente"));
								buzon.setIdActividad(obj.getLong("IdActividad"));
								buzon.setAnio(obj.getInt("Anio"));
								buzon.setMes(obj.getInt("Mes"));
								buzon.setDia(obj.getInt("Dia"));
								buzon.setHoras(obj.getInt("Horas"));
								buzon.setMinutos(obj.getInt("Minutos"));
								buzon.setEliminado(obj.getBoolean("Eliminado"));
								buzon.save();

								Log.e("Buzon => Registro", "#" + i + "= " + buzon);
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
