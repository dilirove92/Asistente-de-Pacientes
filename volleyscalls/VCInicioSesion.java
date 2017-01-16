package com.Notifications.patientssassistant.volleyscalls;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.renderscript.BaseObj;
import android.util.Log;

import com.Notifications.patientssassistant.VarEstatic;
import com.Notifications.patientssassistant.tables.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.orm.query.Condition;
import com.orm.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jazmine on 18/12/2015.
 */
public class VCInicioSesion extends VolleySingleton {

    JsonObjectRequest request;
    JsonArrayRequest req;
    android.content.Context Context;
    final String TAG = "VCInicioSesion";
    private static String ip= VarEstatic.ObtenerIP();
    public static SimpleDateFormat sfDate =  new  SimpleDateFormat ("yyy-MM-dd");

    public VCInicioSesion(android.content.Context context) {
        super(context);
        Context=context;
    }

    //INSERTAR INICIO DE SESIÓN
    public void InsertarInicioSesion(final String... params){
        String urlJson = "http://"+ip+"/ADP/InicioSesion/InicioSesionInsertarObject";

        HashMap<String, String> dato = new HashMap<String, String>();
        dato.put("Tipo", "");
        dato.put("AnioIni",params[3]);
        dato.put("MesIni",params[4]);
        dato.put("DiaIni",params[5]);
        dato.put("HoraIni",params[6]);
        dato.put("MinutosIni",params[7]);
        dato.put("AnioFin",params[8]);
        dato.put("MesFin",params[9]);
        dato.put("DiaFin",params[10]);
        dato.put("HoraFin",params[11]);
        dato.put("MinutosFin",params[12]);
        dato.put("IdReGCM",params[13]);
        dato.put("Eliminado",params[14]);
        dato.put("Usuario", params[1]);
        dato.put("Contrasena", params[2]);
        dato.put("TipoUsuario", params[0]);

        request = new JsonObjectRequest(Request.Method.POST, urlJson, new JSONObject(dato), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Long respuesta=response.getLong("IdIniSes");
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

    //BUSCAR UN INICIO DE SESIÓN
    public void BuscarUnInicioSesion(final String... params){
        String id=params[0];
        String urlJsonObject = "http://"+ip+"/ADP/InicioSesion/InicioSesionBuscar/"+id;
        request = new JsonObjectRequest(urlJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try {
                            TblInicioSesion iniSes = new TblInicioSesion();
                            iniSes.setIdIniSes(response.getLong("IdIniSes"));
                            iniSes.setIdCuiPac(response.getLong("IdCuiPac"));
                            iniSes.setTipoUser(response.getString("Tipo"));
                            iniSes.setUsuario(response.getString("Usuario"));
                            iniSes.setAnioIni(response.getInt("AnioIni"));
                            iniSes.setMesIni(response.getInt("MesIni"));
                            iniSes.setDiaIni(response.getInt("DiaIni"));
                            iniSes.setHoraIni(response.getInt("HoraIni"));
                            iniSes.setMinutosIni(response.getInt("MinutosIni"));
                            iniSes.setAnioFin(response.getInt("AnioFin"));
                            iniSes.setMesFin(response.getInt("MesFin"));
                            iniSes.setDiaFin(response.getInt("DiaFin"));
                            iniSes.setHoraFin(response.getInt("HoraFin"));
                            iniSes.setMinutosFin(response.getInt("MinutosFin"));
                            iniSes.setIdReGCM(response.getString("IdReGCM"));
                            iniSes.setEliminado(response.getBoolean("Eliminado"));

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
        VolleySingleton.getInstance(Context).addToRequestQueue(request);
    }

    //ACTUALIZAR INICIO DE SESIÓN
    public void ActualizarInicioSesion(final String... params){

        String urlJson = "http://"+ip+"/ADP/InicioSesion/InicioSesionActualizarObject";

        HashMap<String, String> dato = new HashMap<String, String>();
        dato.put("IdIniSes", params[0]);
        dato.put("AnioFin",params[1]);
        dato.put("MesFin",params[2]);
        dato.put("DiaFin",params[3]);
        dato.put("HoraFin",params[4]);
        dato.put("MinutosFin",params[5]);
        dato.put("IdReGCM",params[6]);
        dato.put("Eliminado",params[7]);

        request = new JsonObjectRequest(Request.Method.PUT, urlJson, new JSONObject(dato), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Boolean respuesta=response.getBoolean("Done");
                    if (respuesta) {
                        TblInicioSesion iniSes = new TblInicioSesion();
                        iniSes.setIdIniSes(Long.parseLong(params[0]));
                        iniSes.setAnioFin(Integer.parseInt(params[1]));
                        iniSes.setMesFin(Integer.parseInt(params[2]));
                        iniSes.setDiaFin(Integer.parseInt(params[3]));
                        iniSes.setHoraFin(Integer.parseInt(params[4]));
                        iniSes.setMinutosFin(Integer.parseInt(params[5]));
                        iniSes.setIdReGCM(params[6]);
                        iniSes.setEliminado(Boolean.parseBoolean(params[7]));

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

    //TAREA SINCRONA PARA BUSCAR TODAS LOS INICIOS DE SESIONES
    public void BuscarAllIniciosSesion(final String... params){
        String id=params[0];
        String urlJsonArray = "http://"+ip+"/ADP/InicioSesion/InicioSesionBuscarXCuidador/"+id;
        req = new JsonArrayRequest(urlJsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        try {
                            for (int i = 0; i <response.length(); i++) {
                                JSONObject obj=response.getJSONObject(i);

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

                                TblInicioSesion guardar_IS = new TblInicioSesion(
                                        iniSes.getIdIniSes(), iniSes.getIdCuiPac(), iniSes.getTipoUser(),
                                        iniSes.getUsuario(), iniSes.getAnioIni(),iniSes.getMesIni(),
                                        iniSes.getDiaIni(), iniSes.getHoraIni(), iniSes.getMinutosIni(),
                                        iniSes.getAnioFin(),iniSes.getMesFin(), iniSes.getDiaFin(),
                                        iniSes.getHoraFin(), iniSes.getMinutosFin(), iniSes.getIdReGCM(),
                                        iniSes.getEliminado());
                                guardar_IS.save();
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

    //ACTUALIZAR EL ELIMINADO DE ACTIVIDAD
    public void EliminarActividades(final String... params){
        String id=params[0];
        String urlJson = "http://"+ip+"/ADP/InicioSesion/InicioSesionEliminarObject/"+id;

        request = new JsonObjectRequest(Request.Method.DELETE, urlJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Boolean respuesta=response.getBoolean("Done");
                    if(respuesta){
                        Log.e("Inicio Sesion", "Eliminado");
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

}
