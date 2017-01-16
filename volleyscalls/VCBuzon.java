package com.Notifications.patientssassistant.volleyscalls;

import android.content.Context;
import android.util.Log;

import com.Notifications.patientssassistant.VarEstatic;
import com.Notifications.patientssassistant.tables.TblActividades;
import com.Notifications.patientssassistant.tables.TblBuzon;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jazmine on 17/12/2015.
 */
public class VCBuzon extends VolleySingleton{

    JsonObjectRequest request;
    JsonArrayRequest req;
    android.content.Context Context;
    final String TAG = "VCBuzon";

    private static String ip= VarEstatic.ObtenerIP();
    public static SimpleDateFormat sfDate =  new  SimpleDateFormat ("yyy-MM-dd");

    public VCBuzon(android.content.Context context) {
        super(context);
        Context=context;
    }

    public void InsertarBuzon(final String... params){

        String urlJson = "http://"+ip+"/ADP/Actividades/ActividadInsertarObject";
        HashMap<String, String> dato = new HashMap<String, String>();
        dato.put("IdCuidador", params[0]);
        dato.put("IdPaciente", params[1]);
        dato.put("IdActividad", params[2]);
        dato.put("FechaString", params[3]);
        dato.put("Anio", params[2]);
        dato.put("Mes", params[4]);
        dato.put("Dia", params[5]);
        dato.put("Horas", params[6]);
        dato.put("Minutos", params[7]);
        dato.put("Eliminado", params[8]);

        request = new JsonObjectRequest(Request.Method.POST, urlJson, new JSONObject(dato), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Boolean respuesta=response.getBoolean("Done");
                    if (respuesta) {
                        try {
                            Date f=sfDate.parse(params[1]);
                            TblBuzon buzon = new TblBuzon(
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
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
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
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        VolleySingleton.getInstance(Context).addToRequestQueue(request);
    }

    public void EnviarPeticion(final String... params){

        String urlJson = "http://"+ip+"/ADP/Buzon/BuzonPeticionesObject";
        HashMap<String, String> dato = new HashMap<String, String>();
        dato.put("IdCuidador", params[0]);
        dato.put("IdPaciente", params[1]);
        dato.put("IdActividad", params[2]);
        dato.put("FechaString", params[3]);
        dato.put("Anio", params[2]);
        dato.put("Mes", params[4]);
        dato.put("Dia", params[5]);
        dato.put("Horas", params[6]);
        dato.put("Minutos", params[7]);
        dato.put("Eliminado", params[8]);

        request = new JsonObjectRequest(Request.Method.POST, urlJson, new JSONObject(dato), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Boolean respuesta=response.getBoolean("Done");
                    if(respuesta){Log.e("Buzon", "Peticion enviada");}
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        VolleySingleton.getInstance(Context).addToRequestQueue(request);
    }

    //TAREA ASINCRONA PARA BUSCAR TODAS LAS NOTIFICACIONES DE BUZON
    public void BuscarAllBuzonesXCuidadores(final String... params){
        String id=params[0];
        String urlJsonArray = "http://"+ip+"/ADP/Buzon/BuzonBuscarAllXCuidadores/"+id;
        req = new JsonArrayRequest(urlJsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        List<TblActividades> actividades=new ArrayList<TblActividades>();
                        try {
                            for (int i = 0; i <response.length(); i++) {
                                JSONObject obj=response.getJSONObject(i);

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

                                Log.e("Buzon", "#" + i + " Descargado: "+response.getJSONObject(i).toString());
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
        VolleySingleton.getInstance(Context).addToRequestQueue(req);
    }

    //TAREA ASINCRONA PARA BUSCAR TODAS LAS NOTIFICACIONES DE BUZON
    public void BuscarAllBuzonesXCuidador(final String... params){
        String id=params[0];
        String urlJsonArray = "http://"+ip+"/ADP/Buzon/BuzonBuscarAllXCuidador/"+id;
        req = new JsonArrayRequest(urlJsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        List<TblActividades> actividades=new ArrayList<TblActividades>();
                        try {
                            for (int i = 0; i <response.length(); i++) {
                                JSONObject obj=response.getJSONObject(i);

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

                                Log.e("Buzon", "#" + i + " Descargado: "+response.getJSONObject(i).toString());
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
        VolleySingleton.getInstance(Context).addToRequestQueue(req);
    }
}
