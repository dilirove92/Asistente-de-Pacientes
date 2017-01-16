package com.Notifications.patientssassistant.volleyscalls;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.util.Log;

import com.Notifications.patientssassistant.VarEstatic;
import com.Notifications.patientssassistant.tables.TblActividadCuidador;
import com.Notifications.patientssassistant.tables.TblActividades;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.orm.query.Condition;
import com.orm.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jazmine on 17/12/2015.
 */
public class VCActividades extends VolleySingleton {

    JsonObjectRequest request;
    JsonArrayRequest req;
    android.content.Context Context;
    final String TAG = "VCActividades";

    private static String ip= VarEstatic.ObtenerIP();

    public VCActividades(android.content.Context context) {
        super(context);
        Context=context;
    }

    //INSERTAR ACTIVIDADES
    public void InsertarActividades(final String... params){

        String urlJson = "http://"+ip+"/ADP/Actividades/ActividadInsertarObject";

        HashMap<String, String> dato = new HashMap<String, String>();
        dato.put("IdActividad", params[0]);
        dato.put("IdTipoActividad", params[1]);
        dato.put("NombreActividad", params[2]);
        dato.put("DetalleActividad", params[3]);
        dato.put("ImagenActividad", params[4]);
        dato.put("TonoActividad", params[5]);
        dato.put("Eliminado",params[6]);

        request = new JsonObjectRequest(Request.Method.POST, urlJson, new JSONObject(dato), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Long respuesta=response.getLong("IdActividad");
                    if (respuesta>0) {
                        TblActividades actividad = new TblActividades(
                                respuesta, Long.parseLong(params[1]),
                                params[2], params[3], params[4], params[5],
                                Boolean.parseBoolean(params[6]));
                        actividad.save();
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

    //BUSCAR UNA ACTIVIDAD
    public void BuscarUnaActividad(final String... params){
        String id=params[0];
        String urlJsonObject = "http://"+ip+"/ADP/Actividades/ActividadBuscarActividad/"+id;
        request = new JsonObjectRequest(urlJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try {
                            TblActividades actividad = new TblActividades();
                            actividad.setIdActividad(response.getLong("IdActividad"));
                            actividad.setIdTipoActividad(response.getLong("IdTipoActividad"));
                            actividad.setNombreActividad(response.getString("NombreActividad"));
                            actividad.setDetalleActividad(response.getString("DetalleActividad"));
                            actividad.setImagenActividad(response.getString("ImagenActividad"));
                            actividad.setTonoActividad(response.getString("TonoActividad"));
                            actividad.setEliminado(response.getBoolean("Eliminado"));

                            if(actividad!=null){

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
                                Log.e("DatoActividad", ": " + response.toString());
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

    //BUSCAR UNA FOTO ACTIVIDAD
    public void BuscarUnaFotoActividad(final String ... params){
        String id=params[0];
        String url = "http://"+ip+"/ADP/Actividades/ActividadBuscarFotoActividad/"+id;

        ImageRequest request1 = new ImageRequest(url, new Response.Listener<Bitmap>() {
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
        VolleySingleton.getInstance(Context).addToRequestQueue(request1);
    }

    //TAREA SINCRONA PARA BUSCAR TODAS LAS ACTIVIDADES
    public void BuscarAllActividades(final String... params){
        String id=params[0];
        String urlJsonArray = "http://"+ip+"/ADP/Actividades/ActividadBuscarActividades/"+id;
        req = new JsonArrayRequest(urlJsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        List<TblActividades> actividades=new ArrayList<TblActividades>();
                        try {
                            for (int i = 0; i <response.length(); i++) {
                                JSONObject obj=response.getJSONObject(i);

                                TblActividades actividad=new TblActividades();
                                actividad.setIdActividad(obj.getLong("IdActividad"));
                                actividad.setIdTipoActividad(obj.getLong("IdTipoActividad"));
                                actividad.setNombreActividad(obj.getString("NombreActividad"));
                                actividad.setDetalleActividad(obj.getString("DetalleActividad"));
                                actividad.setImagenActividad(obj.getString("ImagenActividad"));
                                actividad.setTonoActividad(obj.getString("TonoActividad"));
                                actividad.setEliminado(obj.getBoolean("Eliminado"));
                                actividades.add(actividad);

                                if (actividad!=null) {
                                    TblActividades guardar_act = new TblActividades(
                                            actividad.getIdActividad(), actividad.getIdTipoActividad(),
                                            actividad.getNombreActividad(), actividad.getDetalleActividad(),
                                            actividad.getImagenActividad(), actividad.getTonoActividad(),
                                            actividad.getEliminado());
                                    guardar_act.save();
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
        });
        VolleySingleton.getInstance(Context).addToRequestQueue(req);
    }

    //TAREA SINCRONA PARA BUSCAR TODAS LAS ACTIVIDADES PRINCIPALES
    public void BuscarActividadesPrincipales(final String... params){
        String id=params[0];
        String urlJsonArray = "http://"+ip+"/ADP/Actividades/ActividadBuscarActividadesPrincipales";
        req = new JsonArrayRequest(urlJsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        List<TblActividades> actividades=new ArrayList<TblActividades>();
                        try {
                            for (int i = 0; i <response.length(); i++) {
                                JSONObject obj=response.getJSONObject(i);

                                TblActividades actividad=new TblActividades();
                                actividad.setIdActividad(obj.getLong("IdActividad"));
                                actividad.setIdTipoActividad(obj.getLong("IdTipoActividad"));
                                actividad.setNombreActividad(obj.getString("NombreActividad"));
                                actividad.setDetalleActividad(obj.getString("DetalleActividad"));
                                actividad.setImagenActividad(obj.getString("ImagenActividad"));
                                actividad.setTonoActividad(obj.getString("TonoActividad"));
                                actividad.setEliminado(obj.getBoolean("Eliminado"));
                                actividades.add(actividad);

                                if (actividad!=null) {
                                    TblActividades guardar_act = new TblActividades(
                                            actividad.getIdActividad(), actividad.getIdTipoActividad(),
                                            actividad.getNombreActividad(), actividad.getDetalleActividad(),
                                            actividad.getImagenActividad(), actividad.getTonoActividad(),
                                            actividad.getEliminado());
                                    guardar_act.save();
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
        });
        VolleySingleton.getInstance(Context).addToRequestQueue(req);
    }

    //TAREA SINCRONA PARA BUSCAR TODAS LAS ACTIVIDADES PRINCIPALES
    public void BuscarActividadesPrincipales1(){

        try {
            for (int i = 0; i < 8; i++) {
                BuscarUnaActividad(String.valueOf(i + 1));
                Log.e("ActividadPrincipal", "#" + i + " Descargado ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //ACTUALIZAR EL ELIMINADO DE ACTIVIDAD
    public void EliminarActividades(final String... params){
        String id=params[0];
        String urlJson = "http://"+ip+"/ADP/Actividades/ActividadEliminarObject/"+id;
        final String TAG=params[1];

        request = new JsonObjectRequest(Request.Method.DELETE, urlJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Boolean respuesta=response.getBoolean("Done");
                    if(respuesta){
                        Log.e("Actividad", "Eliminado");
                        TblActividades actividades = new TblActividades();
                        actividades.EliminarPorIdActividadRegTblActividades(Long.parseLong(params[0]));
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

    public void ListaDeActividades(String idC) {

        String urlJsonArray = "http://"+ip+"/ADP/Actividades/ActividadBuscarListaActividades/" + idC;
        req = new JsonArrayRequest(urlJsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {

                            BuscarActividadesPrincipales1();
                            Long idActividad;
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = (JSONObject) response.get(i);
                                idActividad = obj.getLong("id");
                                BuscarUnaActividad(String.valueOf(idActividad));
                                Log.e("Actividad", "#" + i + " Descargado ");
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
