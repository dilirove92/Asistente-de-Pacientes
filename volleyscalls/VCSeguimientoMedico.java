package com.Notifications.patientssassistant.volleyscalls;

import android.content.Context;
import android.util.Log;

import com.Notifications.patientssassistant.VarEstatic;
import com.Notifications.patientssassistant.tables.TblActividades;
import com.Notifications.patientssassistant.tables.TblSeguimientoMedico;
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
public class VCSeguimientoMedico extends VolleySingleton{

    JsonObjectRequest request;
    JsonArrayRequest req;
    android.content.Context Context;
    final String TAG = "VCSeguimientoMedico";
    private static String ip= VarEstatic.ObtenerIP();

    public VCSeguimientoMedico(Context context) {
        super(context);
        Context=context;
    }

    //INSERTAR SEGUIMIENTO MÉDICO
    public void InsertarSeguimientoMedico(final String... params){

        String urlJson = "http://"+ip+"/ADP/SeguimientoMedico/SeguimientoMedicoInsertarObject";

        HashMap<String, String> dato = new HashMap<String, String>();
        dato.put("IdSeguimientoMedico", params[0]);
        dato.put("IdPaciente", params[1]);
        dato.put("Anio", params[2]);
        dato.put("Mes", params[3]);
        dato.put("Dia", params[4]);
        dato.put("UnidadMedica",params[5]);
        dato.put("Doctor",params[6]);
        dato.put("Diagnostico",params[7]);
        dato.put("TratamientoMedico",params[8]);
        dato.put("AlimentacionSugerida",params[9]);
        dato.put("Eliminado",params[10]);

        request = new JsonObjectRequest(Request.Method.POST, urlJson, new JSONObject(dato), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Long respuesta=response.getLong("IdSeguimientoMedico");
                    if (respuesta>0) {
                        TblSeguimientoMedico seguiMedico = new TblSeguimientoMedico(
                                respuesta, Long.parseLong(params[1]),
                                Integer.parseInt(params[2]),
                                Integer.parseInt(params[3]),
                                Integer.parseInt(params[4]),
                                params[5], params[6], params[7],
                                params[8], params[9],
                                Boolean.parseBoolean(params[10]));
                        seguiMedico.save();
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

    //ACTUALIZAR SEGUIMIENTO MÉDICO
    public void ActualizarSeguimientoMedico(final String... params){

        String urlJson = "http://"+ip+"/ADP/SeguimientoMedico/SeguimientoMedicoActualizarObject";

        HashMap<String, String> dato = new HashMap<String, String>();
        dato.put("IdSeguimientoMedico", params[0]);
        dato.put("IdPaciente", params[1]);
        dato.put("Anio", params[2]);
        dato.put("Mes", params[3]);
        dato.put("Dia", params[4]);
        dato.put("UnidadMedica",params[5]);
        dato.put("Doctor",params[6]);
        dato.put("Diagnostico",params[7]);
        dato.put("TratamientoMedico",params[8]);
        dato.put("AlimentacionSugerida",params[9]);
        dato.put("Eliminado",params[10]);

        request = new JsonObjectRequest(Request.Method.PUT, urlJson, new JSONObject(dato), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Boolean respuesta=response.getBoolean("Done");
                    if (respuesta) {
                        String campo_ide=String.valueOf(Long.parseLong(params[0]));
                        Select segMedico = Select.from(TblSeguimientoMedico.class)
                                .where(Condition.prop("ID_SEGUIMIENTO_MEDICO").eq(campo_ide));
                        TblSeguimientoMedico editar_SM=(TblSeguimientoMedico)segMedico.first();

                        if (editar_SM!=null) {
                            editar_SM.setIdSeguimientoMedico(Long.parseLong(params[0]));
                            editar_SM.setIdPaciente(Long.parseLong(params[1]));
                            editar_SM.setAnio(Integer.parseInt(params[2]));
                            editar_SM.setMes(Integer.parseInt(params[3]));
                            editar_SM.setDia(Integer.parseInt(params[4]));
                            editar_SM.setUnidadMedica(params[5]);
                            editar_SM.setDoctor(params[6]);
                            editar_SM.setDiagnostico(params[7]);
                            editar_SM.setTratamientoMedico(params[8]);
                            editar_SM.setAlimentacionSugerida(params[9]);
                            editar_SM.setEliminado(Boolean.parseBoolean(params[10]));
                            editar_SM.save();
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

    //BUSCAR UN REGISTRO DE SEGUIMIENTO MEDICO DE PACIENTES
    public void BuscarSeguimientoMedico(final String... params){
        String id=params[0];
        String urlJsonObject = "http://"+ip+"/ADP/SeguimientoMedico/SeguimientoMedicoBuscar/"+id;
        request = new JsonObjectRequest(urlJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try {
                            TblSeguimientoMedico seguiMedico = new TblSeguimientoMedico();
                            seguiMedico.setIdSeguimientoMedico(response.getLong("IdSeguimientoMedico"));
                            seguiMedico.setIdPaciente(response.getLong("IdPaciente"));
                            seguiMedico.setAnio(response.getInt("Anio"));
                            seguiMedico.setMes(response.getInt("Mes"));
                            seguiMedico.setDia(response.getInt("Dia"));
                            seguiMedico.setUnidadMedica(response.getString("UnidadMedica"));
                            seguiMedico.setDoctor(response.getString("Doctor"));
                            seguiMedico.setDiagnostico(response.getString("Diagnostico"));
                            seguiMedico.setTratamientoMedico(response.getString("TratamientoMedico"));
                            seguiMedico.setAlimentacionSugerida(response.getString("AlimentacionSugerida"));
                            seguiMedico.setEliminado(response.getBoolean("Eliminado"));

                            if(seguiMedico!=null){

                                String campo_ide=String.valueOf(seguiMedico.getIdSeguimientoMedico());
                                Select segMedico = Select.from(TblSeguimientoMedico.class)
                                        .where(Condition.prop("ID_SEGUIMIENTO_MEDICO").eq(campo_ide));
                                TblSeguimientoMedico editar_SM=(TblSeguimientoMedico)segMedico.first();

                                if (editar_SM!=null) {
                                    editar_SM.setIdSeguimientoMedico(seguiMedico.getIdSeguimientoMedico());
                                    editar_SM.setIdPaciente(seguiMedico.getIdPaciente());
                                    editar_SM.setAnio(seguiMedico.getAnio());
                                    editar_SM.setMes(seguiMedico.getMes());
                                    editar_SM.setDia(seguiMedico.getDia());
                                    editar_SM.setUnidadMedica(seguiMedico.getUnidadMedica());
                                    editar_SM.setDoctor(seguiMedico.getDoctor());
                                    editar_SM.setDiagnostico(seguiMedico.getDiagnostico());
                                    editar_SM.setTratamientoMedico(seguiMedico.getTratamientoMedico());
                                    editar_SM.setAlimentacionSugerida(seguiMedico.getAlimentacionSugerida());
                                    editar_SM.setEliminado(seguiMedico.getEliminado());
                                    editar_SM.save();
                                }
                                else{
                                    TblSeguimientoMedico newSM = new TblSeguimientoMedico(
                                            seguiMedico.getIdSeguimientoMedico(), seguiMedico.getIdPaciente(),
                                            seguiMedico.getAnio(), seguiMedico.getMes(), seguiMedico.getDia(),
                                            seguiMedico.getUnidadMedica(), seguiMedico.getDoctor(),
                                            seguiMedico.getDiagnostico(), seguiMedico.getTratamientoMedico(),
                                            seguiMedico.getAlimentacionSugerida(),seguiMedico.getEliminado());
                                    newSM.save();
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

    //BUSCAR UN GRUPO DE REGISTROS DE SEGUIMIENTOS MEDICOS
    public void BuscarAllSeguimientoMedicoXPaciente(final String... params){
        String id=params[0];
        String urlJsonArray = "http://"+ip+"/ADP/SeguimientoMedico/SeguimientosMedicosBuscarXPaciente/"+id;
        req = new JsonArrayRequest(urlJsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        try {
                            for (int i = 0; i <response.length(); i++) {
                                JSONObject obj=response.getJSONObject(i);

                                TblSeguimientoMedico seguiMedico = new TblSeguimientoMedico();
                                seguiMedico.setIdSeguimientoMedico(obj.getLong("IdSeguimientoMedico"));
                                seguiMedico.setIdPaciente(obj.getLong("IdPaciente"));
                                seguiMedico.setAnio(obj.getInt("Anio"));
                                seguiMedico.setMes(obj.getInt("Mes"));
                                seguiMedico.setDia(obj.getInt("Dia"));
                                seguiMedico.setUnidadMedica(obj.getString("UnidadMedica"));
                                seguiMedico.setDoctor(obj.getString("Doctor"));
                                seguiMedico.setDiagnostico(obj.getString("Diagnostico"));
                                seguiMedico.setTratamientoMedico(obj.getString("TratamientoMedico"));
                                seguiMedico.setAlimentacionSugerida(obj.getString("AlimentacionSugerida"));
                                seguiMedico.setEliminado(obj.getBoolean("Eliminado"));

                                TblSeguimientoMedico guardar_SM = new TblSeguimientoMedico(
                                        seguiMedico.getIdSeguimientoMedico(), seguiMedico.getIdPaciente(),
                                        seguiMedico.getAnio(), seguiMedico.getMes(), seguiMedico.getDia(),
                                        seguiMedico.getUnidadMedica(), seguiMedico.getDoctor(),
                                        seguiMedico.getDiagnostico(), seguiMedico.getTratamientoMedico(),
                                        seguiMedico.getAlimentacionSugerida(),seguiMedico.getEliminado());
                                guardar_SM.save();
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

    //ELIMINAR UN REGISTRO DE SEGUIMIENTO MEDICO
    public void EliminarSeguimientoMedico(final String... params){
        String id=params[0];
        String urlJson = "http://"+ip+"/ADP/SeguimientoMedico/SeguimientoMedicoEliminarObject/"+id;

        request = new JsonObjectRequest(Request.Method.DELETE, urlJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Boolean respuesta=response.getBoolean("Done");
                    if(respuesta){
                        Log.e("Seguimiento Medico", "Eliminado");
                        TblSeguimientoMedico seguimientoM = new TblSeguimientoMedico();
                        seguimientoM.EliminarPorIdSeguimientoMedicoRegTblSeguimientoMedico(Long.parseLong(params[0]));
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

    //UTILIZADO PARA ACTUALIZAR LA BD
    //BUSCAR UN GRUPO DE REGISTROS DE SEGUIMIENTOS MEDICOS
    public void BuscarAllSeguimientoMedicoXCuidadores(final String... params){
        String id=params[0];
        String urlJsonArray = "http://"+ip+"/ADP/SeguimientoMedico/SeguimientoMedicoBuscarXCuidadores/"+id;
        req = new JsonArrayRequest(urlJsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        try {
                            for (int i = 0; i <response.length(); i++) {
                                JSONObject obj=response.getJSONObject(i);

                                TblSeguimientoMedico seguiMedico = new TblSeguimientoMedico();
                                seguiMedico.setIdSeguimientoMedico(obj.getLong("IdSeguimientoMedico"));
                                seguiMedico.setIdPaciente(obj.getLong("IdPaciente"));
                                seguiMedico.setAnio(obj.getInt("Anio"));
                                seguiMedico.setMes(obj.getInt("Mes"));
                                seguiMedico.setDia(obj.getInt("Dia"));
                                seguiMedico.setUnidadMedica(obj.getString("UnidadMedica"));
                                seguiMedico.setDoctor(obj.getString("Doctor"));
                                seguiMedico.setDiagnostico(obj.getString("Diagnostico"));
                                seguiMedico.setTratamientoMedico(obj.getString("TratamientoMedico"));
                                seguiMedico.setAlimentacionSugerida(obj.getString("AlimentacionSugerida"));
                                seguiMedico.setEliminado(obj.getBoolean("Eliminado"));

                                TblSeguimientoMedico guardar_SM = new TblSeguimientoMedico(
                                        seguiMedico.getIdSeguimientoMedico(), seguiMedico.getIdPaciente(),
                                        seguiMedico.getAnio(), seguiMedico.getMes(), seguiMedico.getDia(),
                                        seguiMedico.getUnidadMedica(), seguiMedico.getDoctor(),
                                        seguiMedico.getDiagnostico(), seguiMedico.getTratamientoMedico(),
                                        seguiMedico.getAlimentacionSugerida(),seguiMedico.getEliminado());
                                guardar_SM.save();

                                Log.e("SeguimientoMedico", "#" + i + " Descargado: "+response.getJSONObject(i).toString());
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
