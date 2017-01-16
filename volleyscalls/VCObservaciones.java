package com.Notifications.patientssassistant.volleyscalls;

import android.content.Context;
import android.util.Log;

import com.Notifications.patientssassistant.VarEstatic;
import com.Notifications.patientssassistant.tables.TblActividades;
import com.Notifications.patientssassistant.tables.TblObservaciones;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jazmine on 18/12/2015.
 */
public class VCObservaciones extends VolleySingleton{

    JsonObjectRequest request;
    JsonArrayRequest req;
    android.content.Context Context;
    final String TAG = "VCObservaciones";
    private static String ip= VarEstatic.ObtenerIP();
    private static SimpleDateFormat sfDate =  new  SimpleDateFormat ("yyy-MM-dd");

    public VCObservaciones(Context context) {
        super(context);
    }

    //INSERTAR UNA OBSERVACIÓN
    public void InsertarObservacion(final String... params){

        String urlJson = "http://"+ip+"/ADP/Observaciones/ObservacionInsertarObject";

        HashMap<String, String> dato = new HashMap<String, String>();
        dato.put("IdObservacion", params[0]);
        dato.put("IdPaciente", params[1]);
        dato.put("Anio", params[2]);
        dato.put("Mes", params[3]);
        dato.put("Dia", params[4]);
        dato.put("Hora", params[5]);
        dato.put("Minutos", params[6]);
        dato.put("Observacion",params[7]);
        dato.put("Eliminado",params[8]);

        request = new JsonObjectRequest(Request.Method.POST, urlJson, new JSONObject(dato), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Long respuesta=response.getLong("IdObservacion");
                    if (respuesta>0) {
                        TblObservaciones observa = new TblObservaciones(
                                respuesta, Long.parseLong(params[1]),
                                Integer.parseInt(params[2]),
                                Integer.parseInt(params[3]),
                                Integer.parseInt(params[4]),
                                Integer.parseInt(params[5]),
                                Integer.parseInt(params[6]),
                                params[7], Boolean.parseBoolean(params[8]));
                        observa.save();
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

    //ACTUALIZAR UNA OBSERVACIÓN
    public void ActualizarObservacion(final String... params){

        String urlJson = "http://"+ip+"/ADP/Observaciones/ObservacionActualizarObject";

        HashMap<String, String> dato = new HashMap<String, String>();
        dato.put("IdObservacion", params[0]);
        dato.put("IdPaciente", params[1]);
        dato.put("Anio", params[2]);
        dato.put("Mes", params[3]);
        dato.put("Dia", params[4]);
        dato.put("Hora", params[5]);
        dato.put("Minutos", params[6]);
        dato.put("Observacion",params[7]);
        dato.put("Eliminado",params[8]);

        request = new JsonObjectRequest(Request.Method.PUT, urlJson, new JSONObject(dato), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    boolean respuesta=response.getBoolean("Done");
                    if (respuesta) {
                        String campo_ide=String.valueOf(Long.parseLong(params[0]));
                        Select observacion = Select.from(TblObservaciones.class)
                                .where(Condition.prop("ID_OBSERVACION").eq(campo_ide));
                        TblObservaciones editar_Ob=(TblObservaciones)observacion.first();

                        if (editar_Ob!=null) {
                            editar_Ob.setIdObservacion(Long.parseLong(params[0]));
                            editar_Ob.setIdPaciente(Long.parseLong(params[1]));
                            editar_Ob.setAnio(Integer.parseInt(params[2]));
                            editar_Ob.setMes(Integer.parseInt(params[3]));
                            editar_Ob.setDia(Integer.parseInt(params[4]));
                            editar_Ob.setHora(Integer.parseInt(params[5]));
                            editar_Ob.setMinutos(Integer.parseInt(params[6]));
                            editar_Ob.setObservacion(params[7]);
                            editar_Ob.setEliminado(Boolean.parseBoolean(params[8]));
                            editar_Ob.save();
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

    //BUSCAR UNA OBSERVACION
    public void BuscarObservacion(final String... params){
        String id=params[0];
        String urlJsonObject = "http://"+ip+"/ADP/Observaciones/ObservacionBuscar/"+id;
        request = new JsonObjectRequest(urlJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try {
                            TblObservaciones observa = new TblObservaciones();
                            observa.setIdObservacion(response.getLong("IdObservacion"));
                            observa.setIdPaciente(response.getLong("IdPaciente"));
                            observa.setAnio(response.getInt("Anio"));
                            observa.setMes(response.getInt("Mes"));
                            observa.setDia(response.getInt("Dia"));
                            observa.setHora(response.getInt("Hora"));
                            observa.setMinutos(response.getInt("Minutos"));
                            observa.setObservacion(response.getString("Observacion"));
                            observa.setEliminado(response.getBoolean("Eliminado"));

                            String campo_ide=String.valueOf(observa.getIdObservacion());
                            Select observacion = Select.from(TblObservaciones.class)
                                    .where(Condition.prop("ID_OBSERVACION").eq(campo_ide));
                            TblObservaciones editar_Ob=(TblObservaciones)observacion.first();

                            if (editar_Ob!=null) {
                                editar_Ob.setIdObservacion(observa.getIdObservacion());
                                editar_Ob.setIdPaciente(observa.getIdPaciente());
                                editar_Ob.setAnio(observa.getAnio());
                                editar_Ob.setMes(observa.getMes());
                                editar_Ob.setDia(observa.getDia());
                                editar_Ob.setHora(observa.getHora());
                                editar_Ob.setMinutos(observa.getMinutos());
                                editar_Ob.setObservacion(observa.getObservacion());
                                editar_Ob.setEliminado(observa.getEliminado());
                                editar_Ob.save();
                            }
                            else{
                                TblObservaciones newObser = new TblObservaciones(
                                        observa.getIdObservacion(), observa.getIdPaciente(),
                                        observa.getAnio(), observa.getMes(), observa.getDia(),
                                        observa.getHora(), observa.getMinutos(),
                                        observa.getObservacion(), observa.getEliminado());
                                newObser.save();
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

    //BUSCAR DE TODAS LAS ACTIVIDADES
    public void BuscarAllObservaciones(final String... params){
        String id=params[0];
        String urlJsonArray = "http://"+ip+"/ADP/Observaciones/ObservacionesBuscarXPaciente/"+id;
        req = new JsonArrayRequest(urlJsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {
                            for (int i = 0; i <response.length(); i++) {
                                JSONObject obj=response.getJSONObject(i);

                                TblObservaciones observa = new TblObservaciones();
                                observa.setIdObservacion(obj.getLong("IdObservacion"));
                                observa.setIdPaciente(obj.getLong("IdPaciente"));
                                observa.setAnio(obj.getInt("Anio"));
                                observa.setMes(obj.getInt("Mes"));
                                observa.setDia(obj.getInt("Dia"));
                                observa.setHora(obj.getInt("Hora"));
                                observa.setMinutos(obj.getInt("Minutos"));
                                observa.setObservacion(obj.getString("Observacion"));
                                observa.setEliminado(obj.getBoolean("Eliminado"));

                                TblObservaciones guardar_Ob = new TblObservaciones(
                                        observa.getIdObservacion(), observa.getIdPaciente(),
                                        observa.getAnio(), observa.getMes(), observa.getDia(),
                                        observa.getHora(), observa.getMinutos(),
                                        observa.getObservacion(), observa.getEliminado());
                                guardar_Ob.save();
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

    //ACTUALIZAR EL ELIMINAR UNA OBSERVACION
    public void EliminarObservacion(final String... params){
        String id=params[0];
        String urlJson = "http://"+ip+"/ADP/Observaciones/ObservacionEliminarObject/"+id;

        request = new JsonObjectRequest(Request.Method.DELETE, urlJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Boolean respuesta=response.getBoolean("Done");
                    if(respuesta){
                        Log.e("Observaciones", "Eliminado");
                        TblObservaciones observacion = new TblObservaciones();
                        observacion.EliminarPorIdObservacionRegTblObservaciones(Long.parseLong(params[0]));
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

    //UTIIZADA PARA LA ACTUALIZACION DE LA BD
    //TAREA ASINCRONA PARA BUSCAR VARIAS OBSERVACIONES
    public void BuscarAllObservacionesCuidadores(final String... params){
        String id=params[0];
        String urlJsonArray = "http://"+ip+"/ADP/Observaciones/ObservacionBuscarXCuidadores/"+id;
        req = new JsonArrayRequest(urlJsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {
                            for (int i = 0; i <response.length(); i++) {
                                JSONObject obj=response.getJSONObject(i);

                                TblObservaciones observa = new TblObservaciones();
                                observa.setIdObservacion(obj.getLong("IdObservacion"));
                                observa.setIdPaciente(obj.getLong("IdPaciente"));
                                observa.setAnio(obj.getInt("Anio"));
                                observa.setMes(obj.getInt("Mes"));
                                observa.setDia(obj.getInt("Dia"));
                                observa.setHora(obj.getInt("Hora"));
                                observa.setMinutos(obj.getInt("Minutos"));
                                observa.setObservacion(obj.getString("Observacion"));
                                observa.setEliminado(obj.getBoolean("Eliminado"));

                                TblObservaciones guardar_Ob = new TblObservaciones(
                                        observa.getIdObservacion(), observa.getIdPaciente(),
                                        observa.getAnio(), observa.getMes(), observa.getDia(),
                                        observa.getHora(), observa.getMinutos(),
                                        observa.getObservacion(), observa.getEliminado());
                                guardar_Ob.save();

                                Log.e("Observacion", "#" + i + " Descargado: "+response.getJSONObject(i).toString());
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
