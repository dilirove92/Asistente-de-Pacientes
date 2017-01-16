package com.Notifications.patientssassistant.asynctask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.Notifications.patientssassistant.VarEstatic;

import android.os.AsyncTask;
import android.util.Log;

public class ATRecuperacionContrasena {
	
	//VARIABLES DE CLASE ATRecuperacionContrasena
	//private static String ip="192.168.1.4:1522";
	private static String ip=VarEstatic.ObtenerIP();
	
		//TAREA ASINCRONA PARA ENVIAR CORREO
		public class EnviarCorreo extends AsyncTask<String, Integer, Boolean>
	    {
	    	
			@Override
			protected Boolean doInBackground(String... params) {
				// TODO Auto-generated method stub
				Boolean resul=true;
				HttpClient httpClient=new DefaultHttpClient();
				
				HttpPost post=new HttpPost(
						"http://"+ip+"/ADP/RecuperacionContrasena/RecuperacionContrasena");
				post.setHeader("content-type", "application/json");
				
				try {
					JSONObject dato=new JSONObject();
									
					dato.put("email", params[0]);
					
					StringEntity entity = new StringEntity(dato.toString());
					post.setEntity(entity);
					
					HttpResponse resp=httpClient.execute(post);
					String respStr = EntityUtils.toString(resp.getEntity());
					
					if (respStr.equals("true")){
						resul=true;
					}else{resul=false;}
					
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
}
