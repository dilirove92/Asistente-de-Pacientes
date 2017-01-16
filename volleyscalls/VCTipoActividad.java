package com.Notifications.patientssassistant.volleyscalls;

import android.content.Context;
import android.util.Log;

import com.Notifications.patientssassistant.VarEstatic;
import com.Notifications.patientssassistant.tables.TblActividades;
import com.Notifications.patientssassistant.tables.TblTipoActividad;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jazmine on 19/12/2015.
 */
public class VCTipoActividad extends VolleySingleton{

    JsonObjectRequest request;
    JsonArrayRequest req;
    android.content.Context Context;
    final String TAG = "VCTipoActividad";
    private static String ip= VarEstatic.ObtenerIP();

    public VCTipoActividad(Context context) {
        super(context);
        Context=context;
    }

    //INSERTAR UN TIPO DE ACTIVIDAD
    public void InsertarTipoActividad(final String... params){

        String urlJson = "http://"+ip+"/ADP/TipoActividad/TipoActividadInsertarObject";

        HashMap<String, String> dato = new HashMap<String, String>();
        dato.put("IdTipoActividad", params[0]);
        dato.put("TipoDeActividad", params[1]);
        dato.put("DescripcionTipoAct", params[2]);
        dato.put("Eliminado",params[3]);

        request = new JsonObjectRequest(Request.Method.POST, urlJson, new JSONObject(dato), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Long respuesta=response.getLong("IdTipoActividad");
                    if (respuesta>0) {
                        TblTipoActividad atActividad = new TblTipoActividad(
                                respuesta, params[1], params[2],
                                Boolean.parseBoolean(params[3]));
                        atActividad.save();
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

    //ACTUALIZAR UN TIPO DE ACTIVIDAD
    public void ActualizarTipoActividad(final String... params){

        String urlJson = "http://"+ip+"/ADP/TipoActividad/TipoActividadActualizarObject";

        HashMap<String, String> dato = new HashMap<String, String>();
        dato.put("IdTipoActividad", params[0]);
        dato.put("TipoDeActividad", params[1]);
        dato.put("DescripcionTipoAct", params[2]);
        dato.put("Eliminado",params[3]);

        request = new JsonObjectRequest(Request.Method.PUT, urlJson, new JSONObject(dato), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    boolean respuesta=response.getBoolean("Done");
                    if (respuesta) {
                        TblTipoActividad tActividad = new TblTipoActividad();
                        tActividad.setIdTipoActividad(Long.parseLong(params[0]));
                        tActividad.setTipoActividad(params[1]);
                        tActividad.setDescripcionTipoAct(params[2]);
                        tActividad.setEliminado(Boolean.parseBoolean(params[3]));

                        String campo_ide=String.valueOf(tActividad.getIdTipoActividad());
                        Select eltipo = Select.from(TblTipoActividad.class)
                                .where(Condition.prop("ID_TIPO_ACTIVIDAD").eq(campo_ide));
                        TblTipoActividad edit_tActividad=(TblTipoActividad)eltipo.first();

                        if (edit_tActividad!=null) {
                            edit_tActividad.setIdTipoActividad(tActividad.getIdTipoActividad());
                            edit_tActividad.setTipoActividad(tActividad.getTipoActividad());
                            edit_tActividad.setDescripcionTipoAct(tActividad.getDescripcionTipoAct());
                            edit_tActividad.setEliminado(tActividad.getEliminado());
                            edit_tActividad.save();
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

    //BUSCAR UNA TIPO DE ACTIVIDAD
    public void BuscarUnTipoActividad(final String... params){
        String id=params[0];
        String urlJsonObject = "http://"+ip+"/ADP/TipoActividad/TipoActividadBuscar/"+id;
        request = new JsonObjectRequest(urlJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try {
                            TblTipoActividad tActividad = new TblTipoActividad();
                            tActividad.setIdTipoActividad(response.getLong("IdTipoActividad"));
                            tActividad.setTipoActividad(response.getString("TipoDeActividad"));
                            tActividad.setDescripcionTipoAct(response.getString("DescripcionTipoAct"));
                            tActividad.setEliminado(response.getBoolean("Eliminado"));

                            if(tActividad!=null){

                                String campo_ide=String.valueOf(tActividad.getIdTipoActividad());
                                Select eltipo = Select.from(TblTipoActividad.class)
                                        .where(Condition.prop("ID_TIPO_ACTIVIDAD").eq(campo_ide));
                                TblTipoActividad edit_tActividad=(TblTipoActividad)eltipo.first();

                                if (edit_tActividad!=null) {
                                    edit_tActividad.setIdTipoActividad(tActividad.getIdTipoActividad());
                                    edit_tActividad.setTipoActividad(tActividad.getTipoActividad());
                                    edit_tActividad.setDescripcionTipoAct(tActividad.getDescripcionTipoAct());
                                    edit_tActividad.setEliminado(tActividad.getEliminado());
                                    edit_tActividad.save();
                                }
                                else{
                                    TblTipoActividad tipoAct = new TblTipoActividad(
                                            tActividad.getIdTipoActividad(), tActividad.getTipoActividad(),
                                            tActividad.getDescripcionTipoAct(), tActividad.getEliminado());
                                    tipoAct.save();
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
        VolleySingleton.getInstance(Context).addToRequestQueue(request);
    }

    //BUSCAR TIPOS DE ACTIVIDADES
    public void BuscarAllTipoActividad(final String... params){

        String urlJsonArray = "http://"+ip+"/ADP/TipoActividad/TipoActividadBuscarAll";
        req = new JsonArrayRequest(urlJsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        try {
                            for (int i = 0; i <response.length(); i++) {
                                JSONObject obj=response.getJSONObject(i);

                                TblTipoActividad tActividad= new TblTipoActividad();
                                tActividad.setIdTipoActividad(obj.getLong("IdTipoActividad"));
                                tActividad.setTipoActividad(obj.getString("TipoDeActividad"));
                                tActividad.setDescripcionTipoAct(obj.getString("DescripcionTipoAct"));
                                tActividad.setEliminado(obj.getBoolean("Eliminado"));

                                TblTipoActividad guardar_tActividad = new TblTipoActividad(
                                        tActividad.getIdTipoActividad(), tActividad.getTipoActividad(),
                                        tActividad.getDescripcionTipoAct(), tActividad.getEliminado());
                                guardar_tActividad.save();

                                Log.e("TipoActividad", "#" + i + " Descargado: "+response.getJSONObject(i).toString());
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

    //ELIMINAR UN TIPO DE ACTIVIDAD
    public void EliminarTipoActividad(final String... params){
        String id=params[0];
        String urlJson = "http://"+ip+"/ADP/TipoActividad/TipoActividadEliminarObject/"+id;
        final String TAG=params[1];

        request = new JsonObjectRequest(Request.Method.DELETE, urlJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Boolean respuesta=response.getBoolean("Done");
                    if(respuesta){
                        Log.e("TIPO ACTIVIDAD", "Eliminado");
                        TblTipoActividad tipoAct = new TblTipoActividad();
                        tipoAct.EliminarPorIdTipoActividadRegTblTipoActividad(Long.parseLong(params[0]));
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
