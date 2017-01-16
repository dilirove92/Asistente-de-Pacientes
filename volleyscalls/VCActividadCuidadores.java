package com.Notifications.patientssassistant.volleyscalls;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.Notifications.patientssassistant.VarEstatic;
import com.Notifications.patientssassistant.tables.TblActividadCuidador;
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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jazmine on 17/12/2015.
 */
public class VCActividadCuidadores extends VolleySingleton{

    JsonObjectRequest request;
    JsonArrayRequest req;
    android.content.Context Context;
    final String TAG = "VCActividadCuidadores";

    private static String ip= VarEstatic.ObtenerIP();

    public VCActividadCuidadores(android.content.Context context) {
        super(context);
        Context = context;
    }

    //INSERTAR REGISTRO DE ACTIVIDADES CUIDADORES
    public void InsertarActividadCuidadores(final String... params){

        String urlJson = "http://"+ip+"/ADP/ActividadCuidador/ActividadCuidadorInsertarObject";

        HashMap<String, String> dato = new HashMap<String, String>();
        dato.put("IdCuidador", params[0]);
        dato.put("IdActividad", params[1]);
        dato.put("Eliminado", params[2]);

        request = new JsonObjectRequest(Request.Method.POST, urlJson, new JSONObject(dato), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Boolean respuesta=response.getBoolean("Done");
                    if (respuesta) {

                        TblActividadCuidador guardar_actCui = new TblActividadCuidador(
                                Long.parseLong(params[0]), Long.parseLong(params[1]), Boolean.parseBoolean(params[2]));
                        guardar_actCui.save();
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

    //ACTUALIZAR EL ELIMINADO DE ACTIVIDAD CUIDADORES
    public void ActualizarActividadCuidadores(final String... params){

        String urlJson = "http://"+ip+"/ADP/ActividadCuidador/ActividadCuidadorEliminarObject";

        HashMap<String, String> dato = new HashMap<String, String>();
        dato.put("IdCuidador", params[0]);
        dato.put("IdActividad", params[1]);
        dato.put("Eliminado", params[2]);

        request = new JsonObjectRequest(Request.Method.PUT, urlJson, new JSONObject(dato), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Boolean respuesta=response.getBoolean("Done");
                    if (respuesta) {
                        Select actCuidador = Select.from(TblActividadCuidador.class)
                                .where(Condition.prop("ID_CUIDADOR").eq(params[0]),
                                        Condition.prop("ID_ACTIVIDAD").eq(params[1]));
                        TblActividadCuidador edit_Actividad=(TblActividadCuidador)actCuidador.first();
                        if (edit_Actividad!=null) {
                            edit_Actividad.delete();
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

    //BUSCAR ACTIVIDADES DE UN CUIDADOR
    public void BuscarAllActividadCuidador(final String... params){
        String id=params[0];
        String urlJsonArray = "http://"+ip+"/ADP/ActividadCuidador/ActividadCuidadorBuscarAllCuidador/"+id;
        req = new JsonArrayRequest(urlJsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {
                            for (int i = 0; i <response.length(); i++) {
                                JSONObject obj=response.getJSONObject(i);

                                TblActividadCuidador actCui= new TblActividadCuidador();
                                actCui.setIdCuidador(obj.getLong("IdCuidador"));
                                actCui.setIdActividad(obj.getLong("IdActividad"));
                                actCui.setEliminado(obj.getBoolean("Eliminado"));

                                TblActividadCuidador guardar_actCui = new TblActividadCuidador(
                                        actCui.getIdCuidador(), actCui.getIdActividad(), actCui.getEliminado());
                                guardar_actCui.save();
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

    //(ACTUALIZAR TABLA ACTIVIDADES CUIDADORES)
    //BUSCAR ACTIVIDADES DE TODOS LOS CUIDADORES
    public void BuscarAllActividadCuidadores(final String... params){
        String id=params[0];
        String urlJsonArray = "http://"+ip+"/ADP/ActividadCuidador/ActividadCuidadorBuscarAllCuidadores/"+id;
        req = new JsonArrayRequest(urlJsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {
                            for (int i = 0; i <response.length(); i++) {
                                JSONObject obj=response.getJSONObject(i);

                                TblActividadCuidador actCui= new TblActividadCuidador();
                                actCui.setIdCuidador(obj.getLong("IdCuidador"));
                                actCui.setIdActividad(obj.getLong("IdActividad"));
                                actCui.setEliminado(obj.getBoolean("Eliminado"));

                                TblActividadCuidador guardar_actCui = new TblActividadCuidador(
                                        actCui.getIdCuidador(), actCui.getIdActividad(), actCui.getEliminado());
                                guardar_actCui.save();

                                Log.e("ActividadCuidador", "#" + i + " Descargado: "+response.getJSONObject(i).toString());
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
