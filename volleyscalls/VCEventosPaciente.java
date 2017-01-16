package com.Notifications.patientssassistant.volleyscalls;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.Notifications.patientssassistant.VarEstatic;
import com.Notifications.patientssassistant.tables.TblActividades;
import com.Notifications.patientssassistant.tables.TblEventosCuidadores;
import com.Notifications.patientssassistant.tables.TblEventosPacientes;
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
public class VCEventosPaciente extends VolleySingleton{

    JsonObjectRequest request;
    JsonArrayRequest req;
    android.content.Context Context;
    final String TAG = "VCEventosPaciente";
    private static String ip= VarEstatic.ObtenerIP();

    public VCEventosPaciente(Context context) {
        super(context);
        Context=context;
    }

    //INSERTAR UN EVENTO DE UN PACIENTE
    public void InsertarEventosPaciente(final String... params){

        String urlJson = "http://"+ip+"/ADP/EventosPaciente/EventoPacienteInsertarObject";

        HashMap<String, String> dato = new HashMap<String, String>();
        dato.put("IdEventoP", params[0]);
        dato.put("IdPaciente", params[1]);
        dato.put("IdActividad", params[2]);
        dato.put("AnioE", params[3]);
        dato.put("MesE", params[4]);
        dato.put("DiaE", params[5]);
        dato.put("HoraE", params[6]);
        dato.put("MinutosE", params[7]);
        dato.put("AnioR", params[8]);
        dato.put("MesR", params[9]);
        dato.put("DiaR", params[10]);
        dato.put("HoraR", params[11]);
        dato.put("MinutosR", params[12]);
        dato.put("Lugar", params[13]);
        dato.put("Descripcion", params[14]);
        dato.put("Tono", params[15]);
        dato.put("Alarma",params[16]);
        dato.put("Eliminado",params[17]);

        request = new JsonObjectRequest(Request.Method.POST, urlJson, new JSONObject(dato), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Long respuesta=response.getLong("IdEventoP");
                    if (respuesta>0) {
                        TblEventosCuidadores evenCuid = new TblEventosCuidadores(
                                respuesta, Long.parseLong(params[1]),
                                Long.parseLong(params[2]),
                                Integer.parseInt(params[3]),
                                Integer.parseInt(params[4]),
                                Integer.parseInt(params[5]),
                                Integer.parseInt(params[6]),
                                Integer.parseInt(params[7]),
                                Integer.parseInt(params[8]),
                                Integer.parseInt(params[9]),
                                Integer.parseInt(params[10]),
                                Integer.parseInt(params[11]),
                                Integer.parseInt(params[12]),
                                params[13], params[14], params[15],
                                Boolean.parseBoolean(params[16]),
                                Boolean.parseBoolean(params[17]));
                        evenCuid.save();
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

    //ACTUALIZAR UN EVENTO DE UN PACIENTE
    public void ActualizarEventosPaciente(final String... params){

        String urlJson = "http://"+ip+"/ADP/EventosPaciente/EventoPacienteActualizarObject";

        HashMap<String, String> dato = new HashMap<String, String>();
        dato.put("IdEventoP", params[0]);
        dato.put("IdCuidador", params[1]);
        dato.put("IdActividad", params[2]);
        dato.put("AnioE", params[3]);
        dato.put("MesE", params[4]);
        dato.put("DiaE", params[5]);
        dato.put("HoraE", params[6]);
        dato.put("MinutosE", params[7]);
        dato.put("AnioR", params[8]);
        dato.put("MesR", params[9]);
        dato.put("DiaR", params[10]);
        dato.put("HoraR", params[11]);
        dato.put("MinutosR", params[12]);
        dato.put("Lugar", params[13]);
        dato.put("Descripcion", params[14]);
        dato.put("Tono", params[15]);
        dato.put("Alarma",params[16]);
        dato.put("Eliminado",params[17]);

        request = new JsonObjectRequest(Request.Method.PUT, urlJson, new JSONObject(dato), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    boolean respuesta=response.getBoolean("Done");
                    if (respuesta) {
                        String campo_ide=String.valueOf(Long.parseLong(params[0]));
                        Select EventosPaciente = Select.from(TblEventosPacientes.class)
                                .where(Condition.prop("Id_EVENTO_P").eq(campo_ide));
                        TblEventosPacientes editar_EP=(TblEventosPacientes)EventosPaciente.first();

                        if (editar_EP!=null) {
                            editar_EP.setIdEventoP(Long.parseLong(params[0]));
                            editar_EP.setIdPaciente(Long.parseLong(params[1]));
                            editar_EP.setIdActividad(Long.parseLong(params[2]));
                            editar_EP.setAnioE(Integer.parseInt(params[3]));
                            editar_EP.setMesE(Integer.parseInt(params[4]));
                            editar_EP.setDiaE(Integer.parseInt(params[5]));
                            editar_EP.setHoraE(Integer.parseInt(params[6]));
                            editar_EP.setMinutosE(Integer.parseInt(params[7]));
                            editar_EP.setAnioR(Integer.parseInt(params[8]));
                            editar_EP.setMesR(Integer.parseInt(params[9]));
                            editar_EP.setDiaR(Integer.parseInt(params[10]));
                            editar_EP.setHoraR(Integer.parseInt(params[11]));
                            editar_EP.setMinutosE(Integer.parseInt(params[12]));
                            editar_EP.setLugar(params[13]);
                            editar_EP.setDescripcion(params[14]);
                            editar_EP.setTono(params[15]);
                            editar_EP.setAlarma(Boolean.parseBoolean(params[16]));
                            editar_EP.setEliminado(Boolean.parseBoolean(params[17]));
                            editar_EP.save();
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

    //BUSCAR UN EVENTO REALIZADO POR UN PACIENTE
    public void BuscarEventosPaciente(final String... params){
        String id=params[0];
        String urlJsonObject = "http://"+ip+"/ADP/EventosPaciente/EventoPacienteBuscar/"+id;
        request = new JsonObjectRequest(urlJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try {
                            TblEventosPacientes evenPac = new TblEventosPacientes();
                            evenPac.setIdEventoP(response.getLong("IdEventoP"));
                            evenPac.setIdPaciente(response.getLong("IdPaciente"));
                            evenPac.setIdActividad(response.getLong("IdActividad"));
                            evenPac.setAnioE(response.getInt("AnioE"));
                            evenPac.setMesE(response.getInt("MesE"));
                            evenPac.setDiaE(response.getInt("DiaE"));
                            evenPac.setHoraE(response.getInt("HoraE"));
                            evenPac.setMinutosE(response.getInt("MinutosE"));
                            evenPac.setAnioR(response.getInt("AnioR"));
                            evenPac.setMesR(response.getInt("MesR"));
                            evenPac.setDiaR(response.getInt("DiaR"));
                            evenPac.setHoraR(response.getInt("HoraR"));
                            evenPac.setMinutosR(response.getInt("MinutosR"));
                            evenPac.setLugar(response.getString("Lugar"));
                            evenPac.setDescripcion(response.getString("Descripcion"));
                            evenPac.setTono(response.getString("Tono"));
                            evenPac.setAlarma(response.getBoolean("Alarma"));
                            evenPac.setEliminado(response.getBoolean("Eliminado"));

                            if(evenPac!=null){

                                String campo_ide=String.valueOf(evenPac.getIdEventoP());
                                Select EventosPaciente = Select.from(TblEventosPacientes.class)
                                        .where(Condition.prop("ID_EVENTO_P").eq(campo_ide));
                                TblEventosPacientes editar_EP=(TblEventosPacientes)EventosPaciente.first();

                                if (editar_EP!=null) {
                                    editar_EP.setIdEventoP(evenPac.getIdEventoP());
                                    editar_EP.setIdPaciente(evenPac.getIdPaciente());
                                    editar_EP.setIdActividad(evenPac.getIdActividad());
                                    editar_EP.setAnioE(evenPac.getAnioE());
                                    editar_EP.setMesE(evenPac.getMesE());
                                    editar_EP.setDiaE(evenPac.getDiaE());
                                    editar_EP.setHoraE(evenPac.getHoraE());
                                    editar_EP.setMinutosE(evenPac.getMinutosE());
                                    editar_EP.setAnioR(evenPac.getAnioR());
                                    editar_EP.setMesR(evenPac.getMesR());
                                    editar_EP.setDiaR(evenPac.getDiaR());
                                    editar_EP.setHoraR(evenPac.getHoraR());
                                    editar_EP.setMinutosE(evenPac.getMinutosE());
                                    editar_EP.setLugar(evenPac.getLugar());
                                    editar_EP.setDescripcion(evenPac.getDescripcion());
                                    editar_EP.setTono(evenPac.getTono());
                                    editar_EP.setAlarma(evenPac.getAlarma());
                                    editar_EP.setEliminado(evenPac.getEliminado());
                                    editar_EP.save();
                                    editar_EP.save();
                                }
                                else{
                                    TblEventosCuidadores newEventoC = new TblEventosCuidadores(
                                            evenPac.getIdEventoP(), evenPac.getIdPaciente(),
                                            evenPac.getIdActividad(), evenPac.getAnioE(),
                                            evenPac.getMesE(), evenPac.getDiaE(), evenPac.getHoraE(),
                                            evenPac.getMinutosE(), evenPac.getAnioR(),evenPac.getMesR(),
                                            evenPac.getDiaR(), evenPac.getHoraR(), evenPac.getMinutosR(),
                                            evenPac.getLugar(), evenPac.getDescripcion(), evenPac.getTono(),
                                            evenPac.getAlarma(), evenPac.getEliminado());
                                    newEventoC.save();
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

    //BUSCAR TODOS LOS EVENTOS DE UN CUIDADOR
    public void BuscarAllEventosPaciente(final String... params){
        String id=params[0];
        String urlJsonArray = "http://"+ip+"/ADP/EventosPaciente/EventosPacienteBuscarXPaciente/"+id;
        req = new JsonArrayRequest(urlJsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        try {
                            for (int i = 0; i <response.length(); i++) {
                                JSONObject obj=response.getJSONObject(i);

                                TblEventosPacientes evenPac = new TblEventosPacientes();
                                evenPac.setIdEventoP(obj.getLong("IdEventoP"));
                                evenPac.setIdPaciente(obj.getLong("IdPaciente"));
                                evenPac.setIdActividad(obj.getLong("IdActividad"));
                                evenPac.setAnioE(obj.getInt("AnioE"));
                                evenPac.setMesE(obj.getInt("MesE"));
                                evenPac.setDiaE(obj.getInt("DiaE"));
                                evenPac.setHoraE(obj.getInt("HoraE"));
                                evenPac.setMinutosE(obj.getInt("MinutosE"));
                                evenPac.setAnioR(obj.getInt("AnioR"));
                                evenPac.setMesR(obj.getInt("MesR"));
                                evenPac.setDiaR(obj.getInt("DiaR"));
                                evenPac.setHoraR(obj.getInt("HoraR"));
                                evenPac.setMinutosR(obj.getInt("MinutosR"));
                                evenPac.setLugar(obj.getString("Lugar"));
                                evenPac.setDescripcion(obj.getString("Descripcion"));
                                evenPac.setTono(obj.getString("Tono"));
                                evenPac.setAlarma(obj.getBoolean("Alarma"));
                                evenPac.setEliminado(obj.getBoolean("Eliminado"));

                                TblEventosPacientes guardar_EP = new TblEventosPacientes(
                                        evenPac.getIdEventoP(), evenPac.getIdPaciente(),
                                        evenPac.getIdActividad(), evenPac.getAnioE(),
                                        evenPac.getMesE(), evenPac.getDiaE(), evenPac.getHoraE(),
                                        evenPac.getMinutosE(), evenPac.getAnioR(),evenPac.getMesR(),
                                        evenPac.getDiaR(), evenPac.getHoraR(), evenPac.getMinutosR(),
                                        evenPac.getLugar(), evenPac.getDescripcion(), evenPac.getTono(),
                                        evenPac.getAlarma(), evenPac.getEliminado());
                                guardar_EP.save();
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

    //ACTUALIZAR EL ELIMINARDO DE UN EVENTO DE UN PACIENTE
    public void EliminarEventoPaciente(final String... params){
        String id=params[0];
        String urlJson = "http://"+ip+"/ADP/EventosPaciente/EventoPacienteEliminarObject/"+id;

        request = new JsonObjectRequest(Request.Method.DELETE, urlJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Boolean respuesta=response.getBoolean("Done");
                    if(respuesta){
                        Log.e("Evento Paciente", "Eliminado");
                        TblEventosPacientes eventoP = new TblEventosPacientes();
                        eventoP.EliminarPorIdEventoRegTblEventosPacientes(Long.parseLong(params[0]));
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

    //UTILIZAR PARA LA ACTUALIZACION DE LA BD
    //TAREA ASINCRONA PARA BUSCAR TODOS LOS EVENTOS DE UN PACIENTE
    public void  BuscarAllEventosXCuidadores(final String... params){
        String id=params[0];
        String urlJsonArray = "http://"+ip+"/ADP/EventosPaciente/EventoPacienteBuscarXCuidadores/"+id;
        req = new JsonArrayRequest(urlJsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i <response.length(); i++) {
                                JSONObject obj=response.getJSONObject(i);

                                TblEventosPacientes evenPac = new TblEventosPacientes();
                                evenPac.setIdEventoP(obj.getLong("IdEventoP"));
                                evenPac.setIdPaciente(obj.getLong("IdPaciente"));
                                evenPac.setIdActividad(obj.getLong("IdActividad"));
                                evenPac.setAnioE(obj.getInt("AnioE"));
                                evenPac.setMesE(obj.getInt("MesE"));
                                evenPac.setDiaE(obj.getInt("DiaE"));
                                evenPac.setHoraE(obj.getInt("HoraE"));
                                evenPac.setMinutosE(obj.getInt("MinutosE"));
                                evenPac.setAnioR(obj.getInt("AnioR"));
                                evenPac.setMesR(obj.getInt("MesR"));
                                evenPac.setDiaR(obj.getInt("DiaR"));
                                evenPac.setHoraR(obj.getInt("HoraR"));
                                evenPac.setMinutosR(obj.getInt("MinutosR"));
                                evenPac.setLugar(obj.getString("Lugar"));
                                evenPac.setDescripcion(obj.getString("Descripcion"));
                                evenPac.setTono(obj.getString("Tono"));
                                evenPac.setAlarma(obj.getBoolean("Alarma"));
                                evenPac.setEliminado(obj.getBoolean("Eliminado"));

                                TblEventosPacientes guardar_EP = new TblEventosPacientes(
                                        evenPac.getIdEventoP(), evenPac.getIdPaciente(),
                                        evenPac.getIdActividad(), evenPac.getAnioE(),
                                        evenPac.getMesE(), evenPac.getDiaE(), evenPac.getHoraE(),
                                        evenPac.getMinutosE(), evenPac.getAnioR(),evenPac.getMesR(),
                                        evenPac.getDiaR(), evenPac.getHoraR(), evenPac.getMinutosR(),
                                        evenPac.getLugar(), evenPac.getDescripcion(), evenPac.getTono(),
                                        evenPac.getAlarma(), evenPac.getEliminado());
                                guardar_EP.save();

                                Log.e("EventoPaciente", "#" + i + " Descargado: "+response.getJSONObject(i).toString());
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
