package com.Notifications.patientssassistant.volleyscalls;

import android.content.Context;
import android.util.Log;

import com.Notifications.patientssassistant.VarEstatic;
import com.Notifications.patientssassistant.tables.TblActividades;
import com.Notifications.patientssassistant.tables.TblControlDieta;
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
 * Created by Jazmine on 17/12/2015.
 */
public class VCControlDieta extends VolleySingleton{

    JsonObjectRequest request;
    JsonArrayRequest req;
    android.content.Context Context;
    final String TAG = "VCControlDieta";

    private static String ip= VarEstatic.ObtenerIP();

    public VCControlDieta(android.content.Context context) {
        super(context);
        Context=context;
    }

    //TAREA ASINCRONA PARA INSERTAR UN NUEVO CONTROL DE DIETA
    public void InsertarControlDieta(final String... params){

        String urlJson = "http://"+ip+"/ADP/ControlDieta/ControlDietaInsertarObject";

        HashMap<String, String> dato = new HashMap<String, String>();
        dato.put("IdControlDieta", params[0]);
        dato.put("IdPaciente", params[1]);
        dato.put("Motivo", params[2]);
        dato.put("AlimentosRecomendados", params[3]);
        dato.put("AlimentosNoAdecuados", params[4]);
        dato.put("Eliminado",params[5]);

        request = new JsonObjectRequest(Request.Method.POST, urlJson, new JSONObject(dato), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Long respuesta=response.getLong("IdControlDieta");
                    if (respuesta>0) {
                        TblControlDieta contDieta = new TblControlDieta(
                                respuesta, Long.parseLong(params[1]),
                                params[2], params[3], params[4],
                                Boolean.parseBoolean(params[5]));
                        contDieta.save();
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


    //TAREA ASINCRONA PARA ACTUALIZAR UN REGISTRO DE CONTROL DIETA
    public void ActualizarControlDieta(final String... params){

        String urlJson = "http://"+ip+"/ADP/ControlDieta/ControlDietaActualizarObject";

        HashMap<String, String> dato = new HashMap<String, String>();
        dato.put("IdControlDieta", params[0]);
        dato.put("IdPaciente", params[1]);
        dato.put("Motivo", params[2]);
        dato.put("AlimentosRecomendados", params[3]);
        dato.put("AlimentosNoAdecuados", params[4]);
        dato.put("Eliminado",params[5]);

        request = new JsonObjectRequest(Request.Method.PUT, urlJson, new JSONObject(dato), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Boolean respuesta=response.getBoolean("Done");
                    if (respuesta) {
                        String campo_ide=String.valueOf(Long.parseLong(params[0]));
                        Select contDietas = Select.from(TblControlDieta.class)
                                .where(Condition.prop("ID_CONTROL_DIETA").eq(campo_ide));
                        TblControlDieta editar_controlDieta=(TblControlDieta)contDietas.first();

                        if (editar_controlDieta!=null) {
                            editar_controlDieta.setIdControlDieta(Long.parseLong(params[0]));
                            editar_controlDieta.setIdPaciente(Long.parseLong(params[1]));
                            editar_controlDieta.setMotivo(params[2]);
                            editar_controlDieta.setAlimentosRecomendados(params[3]);
                            editar_controlDieta.setAlimentosNoAdecuados(params[4]);
                            editar_controlDieta.setEliminado(Boolean.parseBoolean(params[5]));
                            editar_controlDieta.save();
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

    //TAREA ASINCRONOA PARA BUSCAR UN REGISTRO DE CONTROL DIETA
    public void BuscarUnControlDieta(final String... params){
        String id=params[0];
        String urlJsonObject = "http://"+ip+"/ADP/ControlDieta/ControlDietaBuscar/"+id;
        request = new JsonObjectRequest(urlJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try {
                            TblControlDieta contDieta = new TblControlDieta();
                            contDieta.setIdControlDieta(response.getLong("IdControlDieta"));
                            contDieta.setIdPaciente(response.getLong("IdPaciente"));
                            contDieta.setMotivo(response.getString("Motivo"));
                            contDieta.setAlimentosRecomendados(response.getString("AlimentosRecomendados"));
                            contDieta.setAlimentosNoAdecuados(response.getString("AlimentosNoAdecuados"));
                            contDieta.setEliminado(response.getBoolean("Eliminado"));

                            if(contDieta!=null){

                                String campo_ide=String.valueOf(contDieta.getIdControlDieta());
                                Select contDietas = Select.from(TblControlDieta.class)
                                        .where(Condition.prop("ID_CONTROL_DIETA").eq(campo_ide));
                                TblControlDieta editar_controlDieta=(TblControlDieta)contDietas.first();

                                if (editar_controlDieta!=null) {
                                    editar_controlDieta.setIdControlDieta(contDieta.getIdControlDieta());
                                    editar_controlDieta.setIdPaciente(contDieta.getIdPaciente());
                                    editar_controlDieta.setMotivo(contDieta.getMotivo());
                                    editar_controlDieta.setAlimentosRecomendados(contDieta.getAlimentosRecomendados());
                                    editar_controlDieta.setAlimentosNoAdecuados(contDieta.getAlimentosNoAdecuados());
                                    editar_controlDieta.setEliminado(contDieta.getEliminado());
                                    editar_controlDieta.save();
                                }
                                else{
                                    TblControlDieta newControl = new TblControlDieta(
                                            contDieta.getIdControlDieta(), contDieta.getIdPaciente(),
                                            contDieta.getMotivo(), contDieta.getAlimentosRecomendados(),
                                            contDieta.getAlimentosNoAdecuados(), contDieta.getEliminado());
                                    newControl.save();
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

    //TAREA ASINCRONA PARA BUSCAR LOS CONTROLES DE DIETAS POR PACIENTE
    public void BuscarAllControlDietaPaciente(final String... params){
        String id=params[0];
        String urlJsonArray = "http://"+ip+"/ADP/ControlDieta/ControlDietaBuscarAllXPaciente/"+id;
        req = new JsonArrayRequest(urlJsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        try {
                            for (int i = 0; i <response.length(); i++) {
                                JSONObject obj=response.getJSONObject(i);

                                TblControlDieta act=new TblControlDieta();
                                act.setIdControlDieta(obj.getLong("IdControlDieta"));
                                act.setIdPaciente(obj.getLong("IdPaciente"));
                                act.setMotivo(obj.getString("Motivo"));
                                act.setAlimentosRecomendados(obj.getString("AlimentosRecomendados"));
                                act.setAlimentosNoAdecuados(obj.getString("AlimentosNoAdecuados"));
                                act.setEliminado(obj.getBoolean("Eliminado"));

                                TblControlDieta guardar_ctDieta = new TblControlDieta(
                                        act.getIdControlDieta(), act.getIdPaciente(),
                                        act.getMotivo(), act.getAlimentosRecomendados(),
                                        act.getAlimentosNoAdecuados(), act.getEliminado());
                                guardar_ctDieta.save();
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

    //UTILIZADA PARA LA ACTUALIZACION DE LA BD
    //TAREA ASINCRONA PARA BUSCAR LOS CONTROLES DE DIETAS POR PACIENTE
    public void BuscarAllControlDietaCuidadores(final String... params){
        String id=params[0];
        String urlJsonArray = "http://"+ip+"/ADP/ControlDieta/ControlDietaBuscarAllXCuidadores/"+id;
        req = new JsonArrayRequest(urlJsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        try {
                            for (int i = 0; i <response.length(); i++) {
                                JSONObject obj=response.getJSONObject(i);

                                TblControlDieta act=new TblControlDieta();
                                act.setIdControlDieta(obj.getLong("IdControlDieta"));
                                act.setIdPaciente(obj.getLong("IdPaciente"));
                                act.setMotivo(obj.getString("Motivo"));
                                act.setAlimentosRecomendados(obj.getString("AlimentosRecomendados"));
                                act.setAlimentosNoAdecuados(obj.getString("AlimentosNoAdecuados"));
                                act.setEliminado(obj.getBoolean("Eliminado"));

                                TblControlDieta guardar_ctDieta = new TblControlDieta(
                                        act.getIdControlDieta(), act.getIdPaciente(),
                                        act.getMotivo(), act.getAlimentosRecomendados(),
                                        act.getAlimentosNoAdecuados(), act.getEliminado());
                                guardar_ctDieta.save();

                                Log.e("ControlDieta", "#" + i + " Descargado: "+response.getJSONObject(i).toString());
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
        VolleySingleton.getInstance(Context).addToRequestQueue(req);;
    }

    //ACTUALIZAR EL ELIMINADO DE ACTIVIDAD
    public void EliminarControlDieta(final String... params){
        String id=params[0];
        String urlJson = "http://"+ip+"/ADP/ControlDieta/ControlDietaEliminarObject/"+id;

        request = new JsonObjectRequest(Request.Method.DELETE, urlJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Boolean respuesta=response.getBoolean("Done");
                    if(respuesta){
                        Log.e("Control Dieta", "Eliminado");
                        TblControlDieta controlDieta = new TblControlDieta();
                        controlDieta.EliminarPorIdControlDietaRegTblControlDieta(Long.parseLong(params[0]));
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
