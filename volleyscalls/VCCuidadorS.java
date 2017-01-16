package com.Notifications.patientssassistant.volleyscalls;

import android.content.Context;
import android.util.Log;

import com.Notifications.patientssassistant.VarEstatic;
import com.Notifications.patientssassistant.tables.TblCuidadorPr;
import com.Notifications.patientssassistant.tables.TblCuidadorS;
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
 * Created by Jazmine on 18/12/2015.
 */
public class VCCuidadorS extends VolleySingleton{

    JsonObjectRequest request;
    JsonArrayRequest req;
    android.content.Context Context;
    final String TAG = "VCCuidadorS";
    private static String ip= VarEstatic.ObtenerIP();

    public VCCuidadorS(android.content.Context context) {
        super(context);
        Context=context;
    }

    //INSERTAR UN NUEVO CUIDADOR SECUNDARIO
    public void InsertarCuidadorS(final String... params){

        String urlJson = "http://"+ip+"/ADP/CuidadorS/CuidadorSInsertarObject";

        HashMap<String, String> dato = new HashMap<String, String>();
        dato.put("IdCuidador", params[0]);
        dato.put("DependeDe", params[1]);
        dato.put("Eliminado",params[2]);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, urlJson, new JSONObject(dato), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Boolean respuesta=response.getBoolean("Done");
                    if (respuesta) {
                        TblCuidadorS cuidador = new TblCuidadorS(
                                Long.parseLong(params[0]),
                                Long.parseLong(params[1]),
                                Boolean.parseBoolean(params[2]));
                        cuidador.save();
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

    //ACTUALIZAR UN NUEVO CUIDADOR SECUNDARIO
    public void ActualizarCuidadorS(final String... params){

        String urlJson = "http://"+ip+"/ADP/CuidadorS/CuidadorSActualizarObject";

        HashMap<String, String> dato = new HashMap<String, String>();
        dato.put("IdCuidador", params[0]);
        dato.put("DependeDe", params[1]);
        dato.put("Eliminado",params[2]);

        request = new JsonObjectRequest(Request.Method.POST, urlJson, new JSONObject(dato), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Boolean respuesta=response.getBoolean("Done");
                    if (respuesta) {
                        String campo_ide=params[0];
                        Select elCuidador = Select.from(TblCuidadorS.class)
                                .where(Condition.prop("ID_CUIDADOR").eq(campo_ide));

                        TblCuidadorS edit_Cuidador=(TblCuidadorS)elCuidador.first();

                        if (edit_Cuidador!=null) {
                            edit_Cuidador.setIdCuidador(Long.parseLong(params[0]));
                            edit_Cuidador.setDependeDe(Long.parseLong(params[1]));
                            edit_Cuidador.setEliminado(Boolean.parseBoolean(params[2]));
                            edit_Cuidador.save();
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

    //TAREA ASINCRONA PARA BUSCAR UN CUIDADOR SECUNDARIO
    public void BuscarUnCuidadorS(final String... params){
        String id=params[0];
        String urlJsonObject = "http://"+ip+"/ADP/CuidadorS/CuidadorSBuscar/"+id;
        request = new JsonObjectRequest(urlJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try {
                            TblCuidadorS cuidador = new TblCuidadorS();
                            cuidador.setIdCuidador(response.getLong("IdCuidador"));
                            cuidador.setDependeDe(response.getLong("DependeDe"));
                            cuidador.setEliminado(response.getBoolean("Eliminado"));

                            if (cuidador != null) {

                                String campo_ide=String.valueOf(cuidador.getIdCuidador());
                                Select elCuidador = Select.from(TblCuidadorS.class)
                                        .where(Condition.prop("ID_CUIDADOR").eq(campo_ide));

                                TblCuidadorS edit_Cuidador=(TblCuidadorS)elCuidador.first();

                                if (edit_Cuidador!=null) {
                                    edit_Cuidador.setIdCuidador(cuidador.getIdCuidador());
                                    edit_Cuidador.setDependeDe(cuidador.getDependeDe());
                                    edit_Cuidador.setEliminado(cuidador.getEliminado());
                                    edit_Cuidador.save();
                                }
                                else{
                                    TblCuidadorS newCuidadorS = new TblCuidadorS(
                                            cuidador.getIdCuidador(), cuidador.getDependeDe(),
                                            cuidador.getEliminado());
                                    newCuidadorS.save();
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

    //BUSCAR TODOS LOS CUIDADORES SECUNDARIOS
    public void BuscarAllCuidadoresS(final String... params){
        String id=params[0];
        String urlJsonArray = "http://"+ip+"/ADP/CuidadorS/CuidadorSBuscarAllXCuidador/"+id;
        req = new JsonArrayRequest(urlJsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {
                            for (int i = 0; i <response.length(); i++) {
                                JSONObject obj=response.getJSONObject(i);

                                TblCuidadorS cuidador = new TblCuidadorS();
                                cuidador.setIdCuidador(obj.getLong("IdCuidador"));
                                cuidador.setDependeDe(obj.getLong("DependeDe"));
                                cuidador.setEliminado(obj.getBoolean("Eliminado"));

                                TblCuidadorS guardar_Cuidador = new TblCuidadorS(
                                        cuidador.getIdCuidador(), cuidador.getDependeDe(),
                                        cuidador.getEliminado());
                                guardar_Cuidador.save();

                                Log.e("CuidadorS", "#" + i + " Descargado: "+response.getJSONObject(i).toString());
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

    //ELIMINAR UN CUIDADOR SECUNDARIO
    public void EliminarCuidadorS(final String... params){
        String id=params[0];
        String urlJson = "http://"+ip+"/ADP/CuidadorS/CuidadorSEliminarObject/"+id;

        request = new JsonObjectRequest(Request.Method.DELETE, urlJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Boolean respuesta=response.getBoolean("Done");
                    if(respuesta){
                        Log.e("CuidadorS", "Eliminado");
                        TblCuidadorS cuidadorS = new TblCuidadorS();
                        cuidadorS.EliminarPorIdCuidadorRegTblCuidadorS(Long.parseLong(params[0]));
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
