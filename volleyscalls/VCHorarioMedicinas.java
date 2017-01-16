package com.Notifications.patientssassistant.volleyscalls;

import android.content.Context;
import android.util.Log;

import com.Notifications.patientssassistant.VarEstatic;
import com.Notifications.patientssassistant.tables.TblActividades;
import com.Notifications.patientssassistant.tables.TblHorarioMedicina;
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
public class VCHorarioMedicinas extends VolleySingleton {

    JsonObjectRequest request;
    JsonArrayRequest req;
    android.content.Context Context;
    final String TAG = "VCHorarioMedicinas";
    private static String ip= VarEstatic.ObtenerIP();

    public VCHorarioMedicinas(android.content.Context context) {
        super(context);
        Context=context;
    }

    //INSERTAR UN HORARIO DE MEDICINAS
    public void InsertarHorarioMedicinas(final String... params){

        String urlJson = "http://"+ip+"/ADP/HorarioMedicinas/HorarioMedicinaInsertarObject";

        HashMap<String, String> dato = new HashMap<String, String>();
        dato.put("IdHorarioMedicina", params[0]);
        dato.put("IdControlMedicina", params[1]);
        dato.put("Hora", params[2]);
        dato.put("Minutos", params[3]);
        dato.put("Domingo", params[4]);
        dato.put("Lunes",params[5]);
        dato.put("Martes", params[6]);
        dato.put("Miercoles", params[7]);
        dato.put("Jueves", params[8]);
        dato.put("Viernes", params[9]);
        dato.put("Sabado", params[10]);
        dato.put("ActDesAlarma",params[11]);
        dato.put("Eliminado",params[12]);

        request = new JsonObjectRequest(Request.Method.POST, urlJson, new JSONObject(dato), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Long respuesta=response.getLong("IdHorarioMedicina");
                    if (respuesta>0) {
                        TblHorarioMedicina horario = new TblHorarioMedicina(
                                respuesta, Long.parseLong(params[1]),
                                Integer.parseInt(params[2]),
                                Integer.parseInt(params[3]),
                                Boolean.parseBoolean(params[4]),
                                Boolean.parseBoolean(params[5]),
                                Boolean.parseBoolean(params[6]),
                                Boolean.parseBoolean(params[7]),
                                Boolean.parseBoolean(params[8]),
                                Boolean.parseBoolean(params[9]),
                                Boolean.parseBoolean(params[10]),
                                Boolean.parseBoolean(params[11]),
                                Boolean.parseBoolean(params[12]));
                        horario.save();
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

    //EDITAR UN HORARIO DE MEDICINAS
    public void ActualizarHorarioMedicinas(final String... params){

        String urlJson = "http://"+ip+"/ADP/HorarioMedicinas/HorarioMedicinaActualizarObject";

        HashMap<String, String> dato = new HashMap<String, String>();
        dato.put("IdHorarioMedicina", params[0]);
        dato.put("IdControlMedicina", params[1]);
        dato.put("Hora", params[2]);
        dato.put("Minutos", params[3]);
        dato.put("Domingo", params[4]);
        dato.put("Lunes",params[5]);
        dato.put("Martes", params[6]);
        dato.put("Miercoles", params[7]);
        dato.put("Jueves", params[8]);
        dato.put("Viernes", params[9]);
        dato.put("Sabado", params[10]);
        dato.put("ActDesAlarma",params[11]);
        dato.put("Eliminado",params[12]);

        request = new JsonObjectRequest(Request.Method.PUT, urlJson, new JSONObject(dato), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    boolean respuesta=response.getBoolean("Done");
                    if (respuesta) {
                        String campo_ide=String.valueOf(Long.parseLong(params[0]));
                        Select elHorario = Select.from(TblHorarioMedicina.class)
                                .where(Condition.prop("ID_HORARIO_MEDICINA").eq(campo_ide));
                        TblHorarioMedicina edit_Horario=(TblHorarioMedicina)elHorario.first();

                        if (edit_Horario!=null) {
                            edit_Horario.setIdHorarioMedicina(Long.parseLong(params[0]));
                            edit_Horario.setIdControlMedicina(Long.parseLong(params[1]));
                            edit_Horario.setHora(Integer.parseInt(params[2]));
                            edit_Horario.setMinutos(Integer.parseInt(params[3]));
                            edit_Horario.setDomingo(Boolean.parseBoolean(params[4]));
                            edit_Horario.setLunes(Boolean.parseBoolean(params[5]));
                            edit_Horario.setMartes(Boolean.parseBoolean(params[6]));
                            edit_Horario.setMiercoles(Boolean.parseBoolean(params[7]));
                            edit_Horario.setJueves(Boolean.parseBoolean(params[8]));
                            edit_Horario.setViernes(Boolean.parseBoolean(params[9]));
                            edit_Horario.setSabado(Boolean.parseBoolean(params[10]));
                            edit_Horario.setActDesAlarma(Boolean.parseBoolean(params[11]));
                            edit_Horario.setEliminado(Boolean.parseBoolean(params[12]));
                            edit_Horario.save();
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

    //BUSCAR UN HORARIO DE MEDICINA DE UN PACIENTE
    public void BuscarUnHorarioMedicina(final String... params){
        String id=params[0];
        String urlJsonObject = "http://"+ip+"/ADP/HorarioMedicinas/HorarioMedicinaBuscar/"+id;
        request = new JsonObjectRequest(urlJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try {
                            TblHorarioMedicina horario = new TblHorarioMedicina();
                            horario.setIdHorarioMedicina(response.getLong("IdHorarioMedicina"));
                            horario.setIdControlMedicina(response.getLong("IdControlMedicina"));
                            horario.setHora(response.getInt("Hora"));
                            horario.setMinutos(response.getInt("Minutos"));
                            horario.setDomingo(response.getBoolean("Domingo"));
                            horario.setLunes(response.getBoolean("Lunes"));
                            horario.setMartes(response.getBoolean("Martes"));
                            horario.setMiercoles(response.getBoolean("Miercoles"));
                            horario.setJueves(response.getBoolean("Jueves"));
                            horario.setViernes(response.getBoolean("Viernes"));
                            horario.setSabado(response.getBoolean("Sabado"));
                            horario.setActDesAlarma(response.getBoolean("ActDesAlarma"));
                            horario.setEliminado(response.getBoolean("Eliminado"));

                            if(horario!=null){

                                String campo_ide=String.valueOf(horario.getIdHorarioMedicina());
                                Select elHorario = Select.from(TblHorarioMedicina.class)
                                        .where(Condition.prop("ID_HORARIO_MEDICINA").eq(campo_ide));
                                TblHorarioMedicina edit_Horario=(TblHorarioMedicina)elHorario.first();

                                if (edit_Horario!=null) {
                                    edit_Horario.setIdHorarioMedicina(horario.getIdHorarioMedicina());
                                    edit_Horario.setIdControlMedicina(horario.getIdControlMedicina());
                                    edit_Horario.setHora(horario.getHora());
                                    edit_Horario.setMinutos(horario.getMinutos());
                                    edit_Horario.setDomingo(horario.getDomingo());
                                    edit_Horario.setLunes(horario.getLunes());
                                    edit_Horario.setMartes(horario.getMartes());
                                    edit_Horario.setMiercoles(horario.getMiercoles());
                                    edit_Horario.setJueves(horario.getJueves());
                                    edit_Horario.setViernes(horario.getViernes());
                                    edit_Horario.setSabado(horario.getSabado());
                                    edit_Horario.setActDesAlarma(horario.getActDesAlarma());
                                    edit_Horario.setEliminado(horario.getEliminado());
                                    edit_Horario.save();
                                }
                                else{
                                    TblHorarioMedicina horaMed= new TblHorarioMedicina(
                                            horario.getIdHorarioMedicina(), horario.getIdControlMedicina(),
                                            horario.getHora(), horario.getMinutos(),
                                            horario.getDomingo(), horario.getLunes(),
                                            horario.getMartes(), horario.getMiercoles(),
                                            horario.getJueves(), horario.getViernes(),
                                            horario.getSabado(), horario.getActDesAlarma(),
                                            horario.getEliminado());
                                    horaMed.save();
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

    //TAREA ASINCRONA PARA BUSCAR TODOS LOS HORARIOS DE MEDICINAS DE UN PACIENTE
    public void BuscarAllHorarioMedicinaXControl(final String... params){
        String id=params[0];
        String urlJsonArray = "http://"+ip+"/ADP/HorarioMedicinas/HorariosMedicinasBuscarXControl/"+id;
        req = new JsonArrayRequest(urlJsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        try {
                            for (int i = 0; i <response.length(); i++) {
                                JSONObject obj=response.getJSONObject(i);

                                TblHorarioMedicina horario = new TblHorarioMedicina();
                                horario.setIdHorarioMedicina(obj.getLong("IdHorarioMedicina"));
                                horario.setIdControlMedicina(obj.getLong("IdControlMedicina"));
                                horario.setHora(obj.getInt("Hora"));
                                horario.setMinutos(obj.getInt("Minutos"));
                                horario.setDomingo(obj.getBoolean("Domingo"));
                                horario.setLunes(obj.getBoolean("Lunes"));
                                horario.setMartes(obj.getBoolean("Martes"));
                                horario.setMiercoles(obj.getBoolean("Miercoles"));
                                horario.setJueves(obj.getBoolean("Jueves"));
                                horario.setViernes(obj.getBoolean("Viernes"));
                                horario.setSabado(obj.getBoolean("Sabado"));
                                horario.setActDesAlarma(obj.getBoolean("ActDesAlarma"));
                                horario.setEliminado(obj.getBoolean("Eliminado"));

                                TblHorarioMedicina guardar_Horario = new TblHorarioMedicina(
                                        horario.getIdHorarioMedicina(), horario.getIdControlMedicina(),
                                        horario.getHora(), horario.getMinutos(),
                                        horario.getDomingo(), horario.getLunes(),
                                        horario.getMartes(), horario.getMiercoles(),
                                        horario.getJueves(), horario.getViernes(),
                                        horario.getSabado(), horario.getActDesAlarma(),
                                        horario.getEliminado());
                                guardar_Horario.save();
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

    //ELIMINAR UN HORARIO DE MEDICINA DE UN PACIENTE
    public void EliminarHorarioMedicina(final String... params){
        String id=params[0];
        String urlJson = "http://"+ip+"/ADP/HorarioMedicinas/HorarioMedicinaEliminarObject/"+id;

        request = new JsonObjectRequest(Request.Method.DELETE, urlJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Boolean respuesta=response.getBoolean("Done");
                    if(respuesta){
                        Log.e("HorariMedicinas", "Eliminado");
                        TblHorarioMedicina horaMed = new TblHorarioMedicina();
                        horaMed.EliminarPorIdHorarioMedicinaRegTblHorarioMedicina(Long.parseLong(params[0]));
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
    //TAREA ASINCRONA PARA BUSCAR TODOS LOS HORARIOS DE MEDICINAS DE UN PACIENTE
    public void BuscarAllHorarioMedicinaXCuidadores(final String... params){
        String id=params[0];
        String urlJsonArray = "http://"+ip+"/ADP/HorarioMedicinas/HorarioMedicinaBuscarXCuidadores/"+id;
        req = new JsonArrayRequest(urlJsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        try {
                            for (int i = 0; i <response.length(); i++) {
                                JSONObject obj=response.getJSONObject(i);

                                TblHorarioMedicina horario = new TblHorarioMedicina();
                                horario.setIdHorarioMedicina(obj.getLong("IdHorarioMedicina"));
                                horario.setIdControlMedicina(obj.getLong("IdControlMedicina"));
                                horario.setHora(obj.getInt("Hora"));
                                horario.setMinutos(obj.getInt("Minutos"));
                                horario.setDomingo(obj.getBoolean("Domingo"));
                                horario.setLunes(obj.getBoolean("Lunes"));
                                horario.setMartes(obj.getBoolean("Martes"));
                                horario.setMiercoles(obj.getBoolean("Miercoles"));
                                horario.setJueves(obj.getBoolean("Jueves"));
                                horario.setViernes(obj.getBoolean("Viernes"));
                                horario.setSabado(obj.getBoolean("Sabado"));
                                horario.setActDesAlarma(obj.getBoolean("ActDesAlarma"));
                                horario.setEliminado(obj.getBoolean("Eliminado"));

                                TblHorarioMedicina guardar_Horario = new TblHorarioMedicina(
                                        horario.getIdHorarioMedicina(), horario.getIdControlMedicina(),
                                        horario.getHora(), horario.getMinutos(),
                                        horario.getDomingo(), horario.getLunes(),
                                        horario.getMartes(), horario.getMiercoles(),
                                        horario.getJueves(), horario.getViernes(),
                                        horario.getSabado(), horario.getActDesAlarma(),
                                        horario.getEliminado());
                                guardar_Horario.save();

                                Log.e("HorarioMedicina", "#" + i + " Descargado: "+response.getJSONObject(i).toString());
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
