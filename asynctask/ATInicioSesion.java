package com.Notifications.patientssassistant.asynctask;

import java.text.SimpleDateFormat;
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

import android.os.AsyncTask;
import android.util.Log;

import com.Notifications.patientssassistant.VarEstatic;
import com.Notifications.patientssassistant.tables.TblBuzon;
import com.Notifications.patientssassistant.tables.TblCuidador;
import com.Notifications.patientssassistant.tables.TblCuidadorPr;
import com.Notifications.patientssassistant.tables.TblInicioSesion;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.orm.query.Condition;
import com.orm.query.Select;

public class ATInicioSesion {

	//VARIABLES DE CLASE ATInicioSesion 
	//private String ip="192.168.1.4:1522";
	private static String ip=VarEstatic.ObtenerIP();
	public static SimpleDateFormat sfDate =  new  SimpleDateFormat ("yyy-MM-dd");
	
	
	//TAREA ASINCRONA PARA INSERTAR UN INICIO DE SESION
	/*public class InsertarInicioSesion extends AsyncTask<String, Integer, Long>
    {
    	
		@Override
		protected Long doInBackground(String... params) {
			// TODO Auto-generated method stub
			long respuesta=0;
			HttpClient httpClient=new DefaultHttpClient();
			
			HttpPost post=new HttpPost("http://"+ip+"/ADP/InicioSesion/InicioSesionInsertar");
			post.setHeader("content-type", "application/json");
			
			try {
				JSONObject dato=new JSONObject();
				
				//Date f1=sfDate.parse(params[3]);
				//Date f2=sfDate.parse(params[5]);
								
				//dato.put("IdIniSes", Long.parseLong("0"));
				//dato.put("IdCuiPac", Long.parseLong("0"));
				dato.put("Tipo", "");
				dato.put("AnioIni",Integer.parseInt(params[3]));
				dato.put("MesIni",Integer.parseInt(params[4]));
				dato.put("DiaIni",Integer.parseInt(params[5]));
				dato.put("HoraIni",Integer.parseInt(params[6]));
				dato.put("MinutosIni",Integer.parseInt(params[7]));
				dato.put("AnioFin",Integer.parseInt(params[8]));
				dato.put("MesFin",Integer.parseInt(params[9]));
				dato.put("DiaFin",Integer.parseInt(params[10]));
				dato.put("HoraFin",Integer.parseInt(params[11]));
				dato.put("MinutosFin",Integer.parseInt(params[12]));
				dato.put("IdReGCM",params[13]);
				dato.put("Eliminado",Boolean.parseBoolean(params[14]));
				dato.put("Usuario", params[1]);
				dato.put("Contrasena", params[2]);
				dato.put("TipoUsuario", params[0]);
				//dato.put("FechainiString", params[3]);
				//dato.put("FechaFinString", params[5]);
				
				StringEntity entity = new StringEntity(dato.toString());
				post.setEntity(entity);

				HttpResponse resp=httpClient.execute(post);
				String respStr = EntityUtils.toString(resp.getEntity());
				respuesta=Long.parseLong(respStr);

				if(respuesta>0){

					TblInicioSesion iniSes= new TblInicioSesion();
					HttpClient httpClient1= new DefaultHttpClient();
					HttpGet BO= new HttpGet (
							"http://"+ip+"/ADP/InicioSesion/InicioSesionBuscar/"+respuesta);
					BO.setHeader("content-type", "application/json");


					try {
						HttpResponse resp1=httpClient1.execute(BO);
						String respStr1=EntityUtils.toString(resp1.getEntity());

						JSONObject respJSON = new JSONObject(respStr1);

						iniSes.setIdIniSes(respJSON.getLong("IdIniSes"));
						iniSes.setIdCuiPac(respJSON.getLong("IdCuiPac"));
						iniSes.setTipoUser(respJSON.getString("Tipo"));
						iniSes.setUsuario(respJSON.getString("Usuario"));
						iniSes.setAnioIni(respJSON.getInt("AnioIni"));
						iniSes.setMesIni(respJSON.getInt("MesIni"));
						iniSes.setDiaIni(respJSON.getInt("DiaIni"));
						iniSes.setHoraIni(respJSON.getInt("HoraIni"));
						iniSes.setMinutosIni(respJSON.getInt("MinutosIni"));
						iniSes.setAnioFin(respJSON.getInt("AnioFin"));
						iniSes.setMesFin(respJSON.getInt("MesFin"));
						iniSes.setDiaFin(respJSON.getInt("DiaFin"));
						iniSes.setHoraFin(respJSON.getInt("HoraFin"));
						iniSes.setMinutosFin(respJSON.getInt("MinutosFin"));
						iniSes.setIdReGCM(respJSON.getString("IdReGCM"));
						iniSes.setEliminado(respJSON.getBoolean("Eliminado"));

						TblInicioSesion guardar_sesion= new TblInicioSesion(
									iniSes.getIdIniSes(), iniSes.getIdCuiPac(), iniSes.getTipoUser(),
									iniSes.getUsuario(), iniSes.getAnioIni(),iniSes.getMesIni(),
									iniSes.getDiaIni(), iniSes.getHoraIni(), iniSes.getMinutosIni(),
									iniSes.getAnioFin(),iniSes.getMesFin(), iniSes.getDiaFin(),
									iniSes.getHoraFin(), iniSes.getMinutosFin(), iniSes.getIdReGCM(),
									iniSes.getEliminado());
						guardar_sesion.save();


					} catch (Exception ex) {
						// TODO: handle exception
						Log.e("Serviciorest","Error!",ex);
					}
				}

			} catch (Exception ex) {
				Log.e("ServicioRest", "Error!", ex);
			}
			return respuesta;
		}
		
		@Override
		protected void onPostExecute(Long resul ) {
			// TODO Auto-generated method stub
			if	(resul>0)
			{
				//BuscarUnInicioSesion buscar = new BuscarUnInicioSesion();
				//buscar.execute(String.valueOf(resul));
			}
		}
		
    }*/

	//TAREA ASINCRONA PARA INSERTAR UN INICIO DE SESION
	public class InsertarInicioSesion extends AsyncTask<String, Integer, Long>
	{

		@Override
		protected Long doInBackground(String... params) {
			// TODO Auto-generated method stub
			long respuesta=0;
			HttpClient httpClient=new DefaultHttpClient();

			HttpPost post=new HttpPost("http://"+ip+"/ADP/InicioSesion/InicioSesionInsertar");
			post.setHeader("content-type", "application/json");

			try {
				JSONObject dato=new JSONObject();

				//Date f1=sfDate.parse(params[3]);
				//Date f2=sfDate.parse(params[5]);

				//dato.put("IdIniSes", Long.parseLong("0"));
				//dato.put("IdCuiPac", Long.parseLong("0"));
				dato.put("Tipo", "");
				dato.put("AnioIni",Integer.parseInt(params[3]));
				dato.put("MesIni",Integer.parseInt(params[4]));
				dato.put("DiaIni",Integer.parseInt(params[5]));
				dato.put("HoraIni",Integer.parseInt(params[6]));
				dato.put("MinutosIni",Integer.parseInt(params[7]));
				dato.put("AnioFin",Integer.parseInt(params[8]));
				dato.put("MesFin",Integer.parseInt(params[9]));
				dato.put("DiaFin",Integer.parseInt(params[10]));
				dato.put("HoraFin",Integer.parseInt(params[11]));
				dato.put("MinutosFin",Integer.parseInt(params[12]));
				dato.put("IdReGCM",params[13]);
				dato.put("Eliminado",Boolean.parseBoolean(params[14]));
				dato.put("Usuario", params[1]);
				dato.put("Contrasena", params[2]);
				dato.put("TipoUsuario", params[0]);
				//dato.put("FechainiString", params[3]);
				//dato.put("FechaFinString", params[5]);

				StringEntity entity = new StringEntity(dato.toString());
				post.setEntity(entity);

				HttpResponse resp=httpClient.execute(post);
				String respStr = EntityUtils.toString(resp.getEntity());
				respuesta=Long.parseLong(respStr);

				if(respuesta>0){

					TblInicioSesion iniSes= new TblInicioSesion();
					HttpClient httpClient1= new DefaultHttpClient();
					HttpGet BO= new HttpGet (
							"http://"+ip+"/ADP/InicioSesion/InicioSesionBuscar/"+respuesta);
					BO.setHeader("content-type", "application/json");


					try {
						HttpResponse resp1=httpClient1.execute(BO);
						String respStr1=EntityUtils.toString(resp1.getEntity());

						JSONObject respJSON = new JSONObject(respStr1);

						iniSes.setIdIniSes(respJSON.getLong("IdIniSes"));
						iniSes.setIdCuiPac(respJSON.getLong("IdCuiPac"));
						iniSes.setTipoUser(respJSON.getString("Tipo"));
						iniSes.setUsuario(respJSON.getString("Usuario"));
						iniSes.setAnioIni(respJSON.getInt("AnioIni"));
						iniSes.setMesIni(respJSON.getInt("MesIni"));
						iniSes.setDiaIni(respJSON.getInt("DiaIni"));
						iniSes.setHoraIni(respJSON.getInt("HoraIni"));
						iniSes.setMinutosIni(respJSON.getInt("MinutosIni"));
						iniSes.setAnioFin(respJSON.getInt("AnioFin"));
						iniSes.setMesFin(respJSON.getInt("MesFin"));
						iniSes.setDiaFin(respJSON.getInt("DiaFin"));
						iniSes.setHoraFin(respJSON.getInt("HoraFin"));
						iniSes.setMinutosFin(respJSON.getInt("MinutosFin"));
						iniSes.setIdReGCM(respJSON.getString("IdReGCM"));
						iniSes.setEliminado(respJSON.getBoolean("Eliminado"));

						TblInicioSesion guardar_sesion= new TblInicioSesion(
								iniSes.getIdIniSes(), iniSes.getIdCuiPac(), iniSes.getTipoUser(),
								iniSes.getUsuario(), iniSes.getAnioIni(),iniSes.getMesIni(),
								iniSes.getDiaIni(), iniSes.getHoraIni(), iniSes.getMinutosIni(),
								iniSes.getAnioFin(),iniSes.getMesFin(), iniSes.getDiaFin(),
								iniSes.getHoraFin(), iniSes.getMinutosFin(), iniSes.getIdReGCM(),
								iniSes.getEliminado());
						guardar_sesion.save();


					} catch (Exception ex) {
						// TODO: handle exception
						Log.e("Serviciorest","Error!",ex);
					}
				}

			} catch (Exception ex) {
				Log.e("ServicioRest", "Error!", ex);
			}
			return respuesta;
		}

		@Override
		protected void onPostExecute(Long resul ) {
			// TODO Auto-generated method stub
			if	(resul>0)
			{
				//BuscarUnInicioSesion buscar = new BuscarUnInicioSesion();
				//buscar.execute(String.valueOf(resul));
			}
		}

	}

	//TAREA ASINCRONA PARA ACTUALIZAR UN INICIO DE SESION
    public class ActualizarInicioSesion extends AsyncTask<String, Integer, Boolean>
    {
    	private TblInicioSesion iniSes;
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			Boolean resul=true;
			HttpClient httpClient=new DefaultHttpClient();
			
			HttpPut put=new HttpPut(
					"http://"+ip+"/ADP/InicioSesion/InicioSesionActualizar");
			put.setHeader("content-type", "application/json");
			
			try {
				JSONObject dato=new JSONObject();
				
				//Date f1=sfDate.parse(params[3]);
				//Date f2=sfDate.parse(params[5]);
								
				dato.put("IdIniSes", Long.parseLong(params[0]));
				//dato.put("IdCuiPac", Long.parseLong(params[1]));
				//dato.put("Tipo", params[2]);
				//dato.put("AnioIni",params[3]);
				//dato.put("MesIni",params[4]);
				//dato.put("DiaIni",params[5]);
				//dato.put("HoraIni",params[6]);
				//dato.put("MinutosIni",params[7]);
				dato.put("AnioFin",Integer.parseInt(params[1]));
				dato.put("MesFin",Integer.parseInt(params[2]));
				dato.put("DiaFin",Integer.parseInt(params[3]));
				dato.put("HoraFin",Integer.parseInt(params[4]));
				dato.put("MinutosFin",Integer.parseInt(params[5]));
				dato.put("IdReGCM",params[6]);
				dato.put("Eliminado",Boolean.parseBoolean(params[7]));
				//dato.put("FechainiString", params[3]);
				//dato.put("FechaFinString", params[5]);
				
				StringEntity entity = new StringEntity(dato.toString());
				put.setEntity(entity);
				
				HttpResponse resp=httpClient.execute(put);
				String respStr = EntityUtils.toString(resp.getEntity());
				
				if (!respStr.equals("true")) {
					resul=false;
				}else{
					iniSes = new TblInicioSesion();
					iniSes.setIdIniSes(Long.parseLong(params[0]));
					/*iniSes.setIdCuiPac(Long.parseLong(params[1]));
					iniSes.setTipoUser(params[2]);
					iniSes.setAnioIni(Integer.parseInt(params[3]));
					iniSes.setMesIni(Integer.parseInt(params[4]));
					iniSes.setDiaIni(Integer.parseInt(params[5]));
					iniSes.setHoraIni(Integer.parseInt(params[6]));
					iniSes.setMinutosIni(Integer.parseInt(params[7]));*/
					iniSes.setAnioFin(Integer.parseInt(params[1]));
					iniSes.setMesFin(Integer.parseInt(params[2]));
					iniSes.setDiaFin(Integer.parseInt(params[3]));
					iniSes.setHoraFin(Integer.parseInt(params[4]));
					iniSes.setMinutosFin(Integer.parseInt(params[5]));
					iniSes.setIdReGCM(params[6]);
					iniSes.setEliminado(Boolean.parseBoolean(params[7]));
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
			if (result) {
				
				String campo_ide=String.valueOf(iniSes.getIdIniSes());
				
				Select InicioSesion = Select.from(TblInicioSesion.class)
						.where(Condition.prop("ID_INI_SES").eq(campo_ide));
				
		    	TblInicioSesion iniSesion=(TblInicioSesion)InicioSesion.first();
		    	
		    	if (iniSesion!=null) {
		    		if(!iniSes.getIdReGCM().equals("")){
		    			iniSesion.setIdReGCM(iniSes.getIdReGCM());
						iniSesion.save();
		    		}
		    		else
		    		{
		    			/*iniSesion.setIdIniSes(iniSes.getIdIniSes());
						iniSesion.setIdCuiPac(iniSes.getIdCuiPac());
						iniSesion.setTipoUser(iniSes.getTipoUser());
						iniSesion.setAnioIni(iniSes.getAnioIni());
						iniSesion.setMesIni(iniSes.getMesIni());
						iniSesion.setDiaIni(iniSes.getDiaIni());
						iniSesion.setHoraIni(iniSes.getHoraIni());
						iniSesion.setMinutosIni(iniSes.getMinutosIni());*/
						iniSesion.setAnioFin(iniSes.getAnioFin());
						iniSesion.setMesFin(iniSes.getMesFin());
						iniSesion.setDiaFin(iniSes.getDiaFin());
						iniSesion.setHoraFin(iniSes.getHoraFin());
						iniSesion.setMinutosFin(iniSes.getMinutosFin());
						iniSesion.setEliminado(iniSes.getEliminado());
						iniSesion.save();
		    		}
				}
			}
		}
		
    }
    
    public class BuscarUnInicioSesion extends AsyncTask<String, Integer, Long>{
  	
    	@Override
		protected Long doInBackground(String... params) {
			// TODO Auto-generated method stub
    		TblInicioSesion iniSes= new TblInicioSesion();
			HttpClient httpClient= new DefaultHttpClient();
			String id=params[0];
			long respuesta=0;
			HttpGet BO= new HttpGet (
					"http://"+ip+"/ADP/InicioSesion/InicioSesionBuscar/"+id);
			BO.setHeader("content-type", "application/json");
			
			
			try {
				HttpResponse resp=httpClient.execute(BO);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONObject respJSON = new JSONObject(respStr);
				
				/*String dateString1=respJSON.getString("FechaIni").substring(6, 18);
				long Date1 = Long.parseLong(dateString1); 
				Date netDate1 = (new Date(Date1));
				String dateString2=respJSON.getString("FechaFin").substring(6, 18);
				long Date2 = Long.parseLong(dateString2); 
				Date netDate2 = (new Date(Date2));*/
				
				iniSes.setIdIniSes(respJSON.getLong("IdIniSes"));
				iniSes.setIdCuiPac(respJSON.getLong("IdCuiPac"));
				iniSes.setTipoUser(respJSON.getString("Tipo"));
				iniSes.setUsuario(respJSON.getString("Usuario"));
				iniSes.setAnioIni(respJSON.getInt("AnioIni"));
				iniSes.setMesIni(respJSON.getInt("MesIni"));
				iniSes.setDiaIni(respJSON.getInt("DiaIni"));
				iniSes.setHoraIni(respJSON.getInt("HoraIni"));
				iniSes.setMinutosIni(respJSON.getInt("MinutosIni"));
				iniSes.setAnioFin(respJSON.getInt("AnioFin"));
				iniSes.setMesFin(respJSON.getInt("MesFin"));
				iniSes.setDiaFin(respJSON.getInt("DiaFin"));
				iniSes.setHoraFin(respJSON.getInt("HoraFin"));
				iniSes.setMinutosFin(respJSON.getInt("MinutosFin"));
				iniSes.setIdReGCM(respJSON.getString("IdReGCM"));
				iniSes.setEliminado(respJSON.getBoolean("Eliminado"));
				
				respuesta=iniSes.getIdCuiPac();
				//__________
				String campo_ide=String.valueOf(iniSes.getIdIniSes());
				
				Select InicioSesion = Select.from(TblInicioSesion.class)
						.where(Condition.prop("ID_INI_SES").eq(campo_ide));
				
		    	TblInicioSesion iniSesion=(TblInicioSesion)InicioSesion.first();
		    	
		    	if (iniSesion!=null) {
		    		iniSesion.setIdIniSes(iniSes.getIdIniSes());
					iniSesion.setIdCuiPac(iniSes.getIdCuiPac());
					iniSesion.setTipoUser(iniSes.getTipoUser());
					iniSesion.setUsuario(iniSes.getUsuario());
					iniSesion.setAnioIni(iniSes.getAnioIni());
					iniSesion.setMesIni(iniSes.getMesIni());
					iniSesion.setDiaIni(iniSes.getDiaIni());
					iniSesion.setHoraIni(iniSes.getHoraIni());
					iniSesion.setMinutosIni(iniSes.getMinutosIni());
					iniSesion.setAnioFin(iniSes.getAnioFin());
					iniSesion.setMesFin(iniSes.getMesFin());
					iniSesion.setDiaFin(iniSes.getDiaFin());
					iniSesion.setHoraFin(iniSes.getHoraFin());
					iniSesion.setMinutosFin(iniSes.getHoraFin());
					iniSesion.setIdReGCM(iniSes.getIdReGCM());
					iniSesion.setEliminado(iniSes.getEliminado());
					iniSesion.save();
				} else {
					TblInicioSesion guardar_sesion= new TblInicioSesion(
							iniSes.getIdIniSes(), iniSes.getIdCuiPac(), iniSes.getTipoUser(), 
							iniSes.getUsuario(), iniSes.getAnioIni(),iniSes.getMesIni(), 
							iniSes.getDiaIni(), iniSes.getHoraIni(), iniSes.getMinutosIni(), 
							iniSes.getAnioFin(),iniSes.getMesFin(), iniSes.getDiaFin(),
							iniSes.getHoraFin(), iniSes.getMinutosFin(), iniSes.getIdReGCM(),
							iniSes.getEliminado());
					guardar_sesion.save();
				}
				
			} catch (Exception ex) {
				// TODO: handle exception
				Log.e("Serviciorest","Error!",ex);
			}
    		
			return respuesta;
		}
    	
    	@Override
		protected void onPostExecute(Long respuesta) {
			// TODO Auto-generated method stub
			if (respuesta>0) {
				
				/*String campo_ide=String.valueOf(iniSes.getIdIniSes());
				
				Select InicioSesion = Select.from(TblInicioSesion.class)
						.where(Condition.prop("ID_INI_SES").eq(campo_ide));
				
		    	TblInicioSesion iniSesion=(TblInicioSesion)InicioSesion.first();
		    	
		    	if (iniSesion!=null) {
		    		iniSesion.setIdIniSes(iniSes.getIdIniSes());
					iniSesion.setIdCuiPac(iniSes.getIdCuiPac());
					iniSesion.setTipoUser(iniSes.getTipoUser());
					iniSesion.setUsuario(iniSes.getUsuario());
					iniSesion.setFechaIni(iniSes.getFechaIni());
					iniSesion.setHoraIni(iniSes.getHoraIni());
					iniSesion.setFechaFin(iniSes.getFechaFin());
					iniSesion.setHoraFin(iniSes.getHoraFin());;
					iniSesion.setEliminado(iniSes.getEliminado());
					iniSesion.save();
				} else {
					TblInicioSesion guardar_sesion= new TblInicioSesion(
							iniSes.getIdIniSes(), iniSes.getIdCuiPac(), iniSes.getTipoUser(), 
							iniSes.getUsuario(), iniSes.getFechaIni(), iniSes.getHoraIni().toString(), 
							iniSes.getFechaFin(), iniSes.getHoraFin().toString(), false);
					guardar_sesion.save();
				}*/
			}
		}
    }
    
    
    //TAREA ASINCRONA PARA BUSCAR UN INICIO DE SESION
    public class BuscarAllIniciosSesion extends AsyncTask<String, Integer, Boolean>{

    	private TblInicioSesion iniSes= new TblInicioSesion();
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			String idC=params[0];
			HttpClient httpClient= new DefaultHttpClient();
			HttpGet BT= new HttpGet (
					"http://"+ip+"/ADP/InicioSesion/InicioSesionesBuscarXCuidador/"+idC);
			BT.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(BT);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				JSONArray respJSON = new JSONArray(respStr);
				
				for (int i = 0; i <respJSON.length(); i++) {
					JSONObject obj=respJSON.getJSONObject(i);
										
					/*String dateString1=obj.getString("FechaIni").substring(6, 18);
					long Date1 = Long.parseLong(dateString1); 
					Date netDate1 = (new Date(Date1));
					String dateString2=obj.getString("FechaFin").substring(6, 18);
					long Date2 = Long.parseLong(dateString2); 
					Date netDate2 = (new Date(Date2));*/
					
					iniSes = new TblInicioSesion();
					iniSes.setIdIniSes(obj.getLong("IdIniSes"));
					iniSes.setIdCuiPac(obj.getLong("IdCuiPac"));
					iniSes.setTipoUser(obj.getString("Tipo"));
					iniSes.setUsuario(obj.getString("Usuario"));
					iniSes.setAnioIni(obj.getInt("AnioIni"));
					iniSes.setMesIni(obj.getInt("MesIni"));
					iniSes.setDiaIni(obj.getInt("DiaIni"));
					iniSes.setHoraIni(obj.getInt("HoraIni"));
					iniSes.setMinutosIni(obj.getInt("MinutosIni"));
					iniSes.setAnioFin(obj.getInt("AnioFin"));
					iniSes.setMesFin(obj.getInt("MesFin"));
					iniSes.setDiaFin(obj.getInt("DiaFin"));
					iniSes.setHoraFin(obj.getInt("HoraFin"));
					iniSes.setMinutosFin(obj.getInt("MinutosFin"));
					iniSes.setIdReGCM(obj.getString("IdReGCM"));
					iniSes.setEliminado(obj.getBoolean("Eliminado"));
					
					TblInicioSesion guardar_IS = new TblInicioSesion(
							iniSes.getIdIniSes(), iniSes.getIdCuiPac(), iniSes.getTipoUser(), 
							iniSes.getUsuario(), iniSes.getAnioIni(),iniSes.getMesIni(), 
							iniSes.getDiaIni(), iniSes.getHoraIni(), iniSes.getMinutosIni(), 
							iniSes.getAnioFin(),iniSes.getMesFin(), iniSes.getDiaFin(),
							iniSes.getHoraFin(), iniSes.getMinutosFin(), iniSes.getIdReGCM(),
							iniSes.getEliminado());
					guardar_IS.save();
					
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
    
    
    //TAREA ASINCRONA PARA ELIMINAR UN INICIO DE SESION
    public class EliminarInicioSesion extends AsyncTask<String, Integer, Boolean>{

    	private String id="";
    	
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean resul=true;
			HttpClient httpClient= new DefaultHttpClient();
			id=params[0];
			HttpDelete del= new HttpDelete (
					"http://"+ip+"/ADP/InicioSesion/InicioSesionEliminar/"+id);
			del.setHeader("content-type", "application/json");
			
			try {
				HttpResponse resp=httpClient.execute(del);
				String respStr=EntityUtils.toString(resp.getEntity());
				
				if (!respStr.equals("true")) {
					resul=false;
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

	public JsonArrayRequest AB_InicioSesion(long idC, final String TAG){
		String urlJsonArray = "http://"+ip+"/ADP/InicioSesion/InicioSesionBuscarXCuidador/"+String.valueOf(idC);
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

								TblInicioSesion iniSes = new TblInicioSesion();
								iniSes.setIdIniSes(obj.getLong("IdIniSes"));
								iniSes.setIdCuiPac(obj.getLong("IdCuiPac"));
								iniSes.setTipoUser(obj.getString("Tipo"));
								iniSes.setUsuario(obj.getString("Usuario"));
								iniSes.setAnioIni(obj.getInt("AnioIni"));
								iniSes.setMesIni(obj.getInt("MesIni"));
								iniSes.setDiaIni(obj.getInt("DiaIni"));
								iniSes.setHoraIni(obj.getInt("HoraIni"));
								iniSes.setMinutosIni(obj.getInt("MinutosIni"));
								iniSes.setAnioFin(obj.getInt("AnioFin"));
								iniSes.setMesFin(obj.getInt("MesFin"));
								iniSes.setDiaFin(obj.getInt("DiaFin"));
								iniSes.setHoraFin(obj.getInt("HoraFin"));
								iniSes.setMinutosFin(obj.getInt("MinutosFin"));
								iniSes.setIdReGCM(obj.getString("IdReGCM"));
								iniSes.setEliminado(obj.getBoolean("Eliminado"));
								iniSes.save();

								Log.e("Ini_Ses => Registro", "#" + i + "= " + iniSes);
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
