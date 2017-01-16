package com.Notifications.patientssassistant.volleyscalls;

import android.content.Context;
import android.util.Log;

import com.Notifications.patientssassistant.VarEstatic;
import com.Notifications.patientssassistant.tables.TblActividades;
import com.Notifications.patientssassistant.tables.TblCuidadorPr;
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
 * Created by Jazmine on 18/12/2015.
 */
public class VCCuidadorPr extends VolleySingleton {

    JsonObjectRequest request;
    JsonArrayRequest req;
    android.content.Context Context;
    final String TAG = "VCCuidadorPr";
    private static String ip= VarEstatic.ObtenerIP();

    public VCCuidadorPr(android.content.Context context) {
        super(context);
        Context=context;
    }

    //TAREA ASINCRONA PARA INSERTAR UN NUEVO CUIDADOR PRIMARIO
    public void InsertarCuidadorPr(final String... params){

        String urlJson = "http://"+ip+"/ADP/CuidadorPr/CuidadorPrInsertarObject";

        HashMap<String, String> dato = new HashMap<String, String>();
        dato.put("IdCuidador", params[0]);
        dato.put("Contrasena", params[1]);
        dato.put("TipoResidencia", params[2]);
        dato.put("PassVincular", params[3]);
        dato.put("Eliminado",params[4]);

        request = new JsonObjectRequest(Request.Method.POST, urlJson, new JSONObject(dato), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Boolean respuesta=response.getBoolean("Done");
                    if (respuesta) {
                        TblCuidadorPr cuidador = new TblCuidadorPr(
                                Long.parseLong(params[0]),
                                params[1], params[2], params[3],
                                Boolean.parseBoolean(params[4]));
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

    //TAREA ASINCRONA PARA ACTUALIZAR UN REGISTRO DE CUIDADOR PRIMARIO
    public void ActualizarCuidadorPr(final String... params){

        String urlJson = "http://"+ip+"/ADP/CuidadorPr/CuidadorPrActualizarObject";

        HashMap<String, String> dato = new HashMap<String, String>();
        dato.put("IdCuidador", params[0]);
        dato.put("Contrasena", params[1]);
        dato.put("TipoResidencia", params[2]);
        dato.put("PassVincular", params[3]);
        dato.put("Eliminado",params[4]);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, urlJson, new JSONObject(dato), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Boolean respuesta=response.getBoolean("Done");
                    if (respuesta) {
                        String campo_ide=params[0];
                        Select elCuidadorPr = Select.from(TblCuidadorPr.class)
                                .where(Condition.prop("ID_CUIDADOR").eq(campo_ide));
                        TblCuidadorPr edit_Cuidador=(TblCuidadorPr)elCuidadorPr.first();

                        if (edit_Cuidador!=null) {
                            edit_Cuidador.setIdCuidador(Long.parseLong(params[0]));
                            edit_Cuidador.setContrasena(params[1]);
                            edit_Cuidador.setTipoResidencia(params[2]);
                            edit_Cuidador.setPassVinculacion(params[3]);
                            edit_Cuidador.setEliminado(Boolean.parseBoolean(params[4]));
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

    //TAREA ASINCRONO PARA BUSCAR UN CUIDADOR PRIMARIO
    public void BuscarUnCuidadorPr(final String... params){
        String id=params[0];
        String urlJsonObject = "http://"+ip+"/ADP/CuidadorPr/CuidadorPrBuscar/"+id;
        request = new JsonObjectRequest(urlJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try {
                            TblCuidadorPr cuidador = new TblCuidadorPr();
                            cuidador.setIdCuidador(response.getLong("IdCuidador"));
                            cuidador.setContrasena(response.getString("Contrasena"));
                            cuidador.setTipoResidencia(response.getString("TipoResidencia"));
                            cuidador.setPassVinculacion(response.getString("PassVincular"));
                            cuidador.setEliminado(response.getBoolean("Eliminado"));

                            if(cuidador!=null){

                                String campo_ide=String.valueOf(cuidador.getIdCuidador());
                                Select elCuidadorPr = Select.from(TblCuidadorPr.class)
                                        .where(Condition.prop("ID_CUIDADOR").eq(campo_ide));
                                TblCuidadorPr edit_Cuidador=(TblCuidadorPr)elCuidadorPr.first();

                                if (edit_Cuidador!=null) {
                                    edit_Cuidador.setIdCuidador(cuidador.getIdCuidador());
                                    edit_Cuidador.setContrasena(cuidador.getContrasena());
                                    edit_Cuidador.setTipoResidencia(cuidador.getTipoResidencia());
                                    edit_Cuidador.setPassVinculacion(cuidador.getPassVinculacion());
                                    edit_Cuidador.setEliminado(cuidador.getEliminado());
                                    edit_Cuidador.save();
                                }
                                else{
                                    TblCuidadorPr newCuidadorPr = new TblCuidadorPr(
                                            cuidador.getIdCuidador(), cuidador.getContrasena(),
                                            cuidador.getTipoResidencia(), cuidador.getPassVinculacion(),
                                            cuidador.getEliminado());
                                    newCuidadorPr.save();
                                }
                                Log.e("DatoCuidadorPr", ": " + response.toString());
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

    //TAREA ASINCRONA PARA BUSCAR TODOS LOS CUIDAORES PRIMARIOS
    public void BuscarAllCuidadoresPr(final String... params){
        String id=params[0];
        String urlJsonArray = "http://"+ip+"/ADP/CuidadorPr/CuidadorPrBuscarAll";
        req = new JsonArrayRequest(urlJsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {
                            for (int i = 0; i <response.length(); i++) {
                                JSONObject obj=response.getJSONObject(i);

                                TblCuidadorPr cuidador = new  TblCuidadorPr();
                                cuidador.setIdCuidador(obj.getLong("IdCuidador"));
                                cuidador.setContrasena(obj.getString("Contrasena"));
                                cuidador.setTipoResidencia(obj.getString("TipoResidencia"));
                                cuidador.setPassVinculacion(obj.getString("PassVincular"));
                                cuidador.setEliminado(obj.getBoolean("Eliminado"));


                                TblCuidadorPr guardar_Cuidador = new TblCuidadorPr(
                                        cuidador.getIdCuidador(), cuidador.getContrasena(),
                                        cuidador.getTipoResidencia(), cuidador.getPassVinculacion(),
                                        cuidador.getEliminado());
                                guardar_Cuidador.save();
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

    //ELIMINAR UN CUIDADOR PRIMARIO
    public void EliminarCuidadorPr(final String... params){
        String id=params[0];
        String urlJson = "http://"+ip+"/ADP/CuidadorPr/CuidadorPrEliminarObject/"+id;

        request = new JsonObjectRequest(Request.Method.DELETE, urlJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Boolean respuesta=response.getBoolean("Done");
                    if(respuesta){
                        Log.e("CuidadorPr", "Eliminado");
                        TblCuidadorPr cuidadorPr = new TblCuidadorPr();
                        cuidadorPr.EliminarPorIdCuidadorRegTblCuidadorPr(Long.parseLong(params[0]));
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
