package com.Notifications.patientssassistant.volleyscalls;

import android.content.Context;
import android.util.Log;

import com.Notifications.patientssassistant.VarEstatic;
import com.Notifications.patientssassistant.tables.TblActividadCuidador;
import com.Notifications.patientssassistant.tables.TblActividadPaciente;
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
public class VCActividadPaciente extends VolleySingleton{

    JsonObjectRequest request;
    JsonArrayRequest req;
    android.content.Context Context;
    final String TAG = "VCActividadPaciente";

    private static String ip= VarEstatic.ObtenerIP();

    public VCActividadPaciente(android.content.Context context) {
        super(context);
        Context=context;
    }

    //INSERTAR REGISTRO DE ACTIVIDADES PACIENTES
    public void InsertarActividadPacientes(final String... params){

        String urlJson = "http://"+ip+"/ADP/ActividadPaciente/ActividadPacienteInsertarObject";

        HashMap<String, String> dato = new HashMap<String, String>();
        dato.put("IdPaciente", params[0]);
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

    //ACTUALIZAR EL ELIMINADO DE ACTIVIDAD PACIENTES
    public void ActualizarActividadPacientes(final String... params){

        String urlJson = "http://"+ip+"/ADP/ActividadPaciente/ActividadPacienteEliminarObject";

        HashMap<String, String> dato = new HashMap<String, String>();
        dato.put("IdPaciente", params[0]);
        dato.put("IdActividad", params[1]);
        dato.put("Eliminado", params[2]);


        request = new JsonObjectRequest(Request.Method.PUT, urlJson, new JSONObject(dato), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Boolean respuesta=response.getBoolean("Done");
                    if (respuesta) {
                        Select actPaciente = Select.from(TblActividadPaciente.class)
                                .where(Condition.prop("ID_PACIENTE").eq(params[0]),
                                        Condition.prop("ID_ACTIVIDAD").eq(params[1]));
                        TblActividadPaciente edit_Actividad=(TblActividadPaciente)actPaciente.first();
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

    //BUSCAR ACTIVIDADES DE UN PACIENTES
    public void BuscarAllPacientes(final String... params){
        String id=params[0];
        String urlJsonArray = "http://"+ip+"/ADP/ActividadPaciente/ActividadPacientes/"+id;
        req = new JsonArrayRequest(urlJsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {
                            for (int i = 0; i <response.length(); i++) {
                                JSONObject obj=response.getJSONObject(i);

                                TblActividadPaciente actPac = new TblActividadPaciente();
                                actPac.setIdPaciente(obj.getLong("IdPaciente"));
                                actPac.setIdActividad(obj.getLong("IdActividad"));
                                actPac.setEliminado(obj.getBoolean("Eliminado"));

                                TblActividadPaciente guardar_actPac = new TblActividadPaciente(
                                        actPac.getIdPaciente(),
                                        actPac.getIdActividad(),
                                        actPac.getEliminado());
                                guardar_actPac.save();
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
    //BUSCAR ACTIVIDADES DE TODOS LOS PACIENTES
    public void BuscarAllActividadPacientes(final String... params){
        String id=params[0];
        String urlJsonArray = "http://"+ip+"/ADP/ActividadPaciente/ActividadPacienteBuscarAllXCuidadores/"+id;
        req = new JsonArrayRequest(urlJsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {
                            for (int i = 0; i <response.length(); i++) {
                                JSONObject obj=response.getJSONObject(i);

                                TblActividadPaciente actPac= new TblActividadPaciente();
                                actPac.setIdPaciente(obj.getLong("IdPaciente"));
                                actPac.setIdActividad(obj.getLong("IdActividad"));
                                actPac.setEliminado(obj.getBoolean("Eliminado"));

                                TblActividadPaciente guardar_actPac = new TblActividadPaciente(
                                        actPac.getIdPaciente(), actPac.getIdActividad(), actPac.getEliminado());
                                guardar_actPac.save();

                                Log.e("ActividadPaciente", "#" + i + " Descargado: "+response.getJSONObject(i).toString());
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
