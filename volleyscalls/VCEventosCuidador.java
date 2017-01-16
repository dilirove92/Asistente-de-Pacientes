package com.Notifications.patientssassistant.volleyscalls;

import android.content.Context;
import android.util.Log;

import com.Notifications.patientssassistant.VarEstatic;
import com.Notifications.patientssassistant.tables.TblActividades;
import com.Notifications.patientssassistant.tables.TblEventosCuidadores;
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
public class VCEventosCuidador extends VolleySingleton {

    JsonObjectRequest request;
    JsonArrayRequest req;
    android.content.Context Context;
    final String TAG = "VCEventosCuidador";
    private static String ip= VarEstatic.ObtenerIP();

    public VCEventosCuidador(android.content.Context context) {
        super(context);
        Context=context;
    }

    //INSERTAR UN EVENTO DE UN CUIDADOR
    public void InsertarEventosCuidador(final String... params){

        String urlJson = "http://"+ip+"/ADP/EventosCuidador/EventoCuidadorInsertarObject";

        HashMap<String, String> dato = new HashMap<String, String>();
        dato.put("IdEventoC", params[0]);
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

        request = new JsonObjectRequest(Request.Method.POST, urlJson, new JSONObject(dato), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Long respuesta=response.getLong("IdEventoC");
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

    //ACTUALIZAR UN EVENTO DE UN CUIDADOR
    public void ActualizarEventosCuidador(final String... params){

        String urlJson = "http://"+ip+"/ADP/EventosCuidador/EventoCuidadorActualizarObject";

        HashMap<String, String> dato = new HashMap<String, String>();
        dato.put("IdEventoC", params[0]);
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
                        Select EventosCuidador = Select.from(TblEventosCuidadores.class)
                                .where(Condition.prop("ID_EVENTO_C").eq(campo_ide));
                        TblEventosCuidadores editar_EC=(TblEventosCuidadores)EventosCuidador.first();

                        if (editar_EC!=null) {
                            editar_EC.setIdCuidador(Long.parseLong(params[1]));
                            editar_EC.setIdActividad(Long.parseLong(params[2]));
                            editar_EC.setAnioE(Integer.parseInt(params[3]));
                            editar_EC.setMesE(Integer.parseInt(params[4]));
                            editar_EC.setDiaE(Integer.parseInt(params[5]));
                            editar_EC.setHoraE(Integer.parseInt(params[6]));
                            editar_EC.setMinutosE(Integer.parseInt(params[7]));
                            editar_EC.setAnioR(Integer.parseInt(params[8]));
                            editar_EC.setMesR(Integer.parseInt(params[9]));
                            editar_EC.setDiaR(Integer.parseInt(params[10]));
                            editar_EC.setHoraR(Integer.parseInt(params[11]));
                            editar_EC.setMinutosE(Integer.parseInt(params[12]));
                            editar_EC.setLugar(params[13]);
                            editar_EC.setDescripcion(params[14]);
                            editar_EC.setTono(params[15]);
                            editar_EC.setAlarma(Boolean.parseBoolean(params[16]));
                            editar_EC.setEliminado(Boolean.parseBoolean(params[17]));
                            editar_EC.save();
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

    //BUSCAR UN EVENTO REALIZADO POR UN CUIDADOR
    public void BuscarEventosCuidador(final String... params){
        String id=params[0];
        String urlJsonObject = "http://"+ip+"/ADP/EventosCuidador/EventoCuidadorBuscar/"+id;
        request = new JsonObjectRequest(urlJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try {
                            TblEventosCuidadores evenCuid = new TblEventosCuidadores();
                            evenCuid.setIdEventoC(response.getLong("IdEventoC"));
                            evenCuid.setIdCuidador(response.getLong("IdCuidador"));
                            evenCuid.setIdActividad(response.getLong("IdActividad"));
                            evenCuid.setAnioE(response.getInt("AnioE"));
                            evenCuid.setMesE(response.getInt("MesE"));
                            evenCuid.setDiaE(response.getInt("DiaE"));
                            evenCuid.setHoraE(response.getInt("HoraE"));
                            evenCuid.setMinutosE(response.getInt("MinutosE"));
                            evenCuid.setAnioR(response.getInt("AnioR"));
                            evenCuid.setMesR(response.getInt("MesR"));
                            evenCuid.setDiaR(response.getInt("DiaR"));
                            evenCuid.setHoraR(response.getInt("HoraR"));
                            evenCuid.setMinutosR(response.getInt("MinutosR"));
                            evenCuid.setLugar(response.getString("Lugar"));
                            evenCuid.setDescripcion(response.getString("Descripcion"));
                            evenCuid.setTono(response.getString("Tono"));
                            evenCuid.setAlarma(response.getBoolean("Alarma"));
                            evenCuid.setEliminado(response.getBoolean("Eliminado"));

                            if(evenCuid!=null){

                                String campo_ide=String.valueOf(evenCuid.getIdEventoC());
                                Select EventosCuidador = Select.from(TblEventosCuidadores.class)
                                        .where(Condition.prop("ID_EVENTO_C").eq(campo_ide));
                                TblEventosCuidadores editar_EC=(TblEventosCuidadores)EventosCuidador.first();
                                if (editar_EC!=null) {
                                    editar_EC.setIdEventoC(evenCuid.getIdEventoC());
                                    editar_EC.setIdCuidador(evenCuid.getIdCuidador());
                                    editar_EC.setIdActividad(evenCuid.getIdActividad());
                                    editar_EC.setAnioE(evenCuid.getAnioE());
                                    editar_EC.setMesE(evenCuid.getMesE());
                                    editar_EC.setDiaE(evenCuid.getDiaE());
                                    editar_EC.setHoraE(evenCuid.getHoraE());
                                    editar_EC.setMinutosE(evenCuid.getMinutosE());
                                    editar_EC.setAnioR(evenCuid.getAnioR());
                                    editar_EC.setMesR(evenCuid.getMesR());
                                    editar_EC.setDiaR(evenCuid.getDiaR());
                                    editar_EC.setHoraR(evenCuid.getHoraR());
                                    editar_EC.setMinutosE(evenCuid.getMinutosR());
                                    editar_EC.setLugar(evenCuid.getLugar());
                                    editar_EC.setDescripcion(evenCuid.getDescripcion());
                                    editar_EC.setTono(evenCuid.getTono());
                                    editar_EC.setAlarma(evenCuid.getAlarma());
                                    editar_EC.setEliminado(evenCuid.getEliminado());
                                    editar_EC.save();
                                }else{
                                    TblEventosCuidadores newEventoC = new TblEventosCuidadores(
                                            evenCuid.getIdEventoC(), evenCuid.getIdCuidador(),
                                            evenCuid.getIdActividad(), evenCuid.getAnioE(),
                                            evenCuid.getMesE(), evenCuid.getDiaE(), evenCuid.getHoraE(),
                                            evenCuid.getMinutosE(), evenCuid.getAnioR(),evenCuid.getMesR(),
                                            evenCuid.getDiaR(), evenCuid.getHoraR(), evenCuid.getMinutosR(),
                                            evenCuid.getLugar(), evenCuid.getDescripcion(), evenCuid.getTono(),
                                            evenCuid.getAlarma(), evenCuid.getEliminado());
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
    public void  BuscarAllEventosCuidador(final String... params){
        String id=params[0];
        String urlJsonArray = "http://"+ip+"/ADP/EventosCuidador/EventosCuidadorBuscarXCuidador/"+id;
        req = new JsonArrayRequest(urlJsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        List<TblActividades> actividades=new ArrayList<TblActividades>();
                        try {
                            for (int i = 0; i <response.length(); i++) {
                                JSONObject obj=response.getJSONObject(i);

                                TblEventosCuidadores evenCuid = new TblEventosCuidadores();
                                evenCuid.setIdEventoC(obj.getLong("IdEventoC"));
                                evenCuid.setIdCuidador(obj.getLong("IdCuidador"));
                                evenCuid.setIdActividad(obj.getLong("IdActividad"));
                                evenCuid.setAnioE(obj.getInt("AnioE"));
                                evenCuid.setMesE(obj.getInt("MesE"));
                                evenCuid.setDiaE(obj.getInt("DiaE"));
                                evenCuid.setHoraE(obj.getInt("HoraE"));
                                evenCuid.setMinutosE(obj.getInt("MinutosE"));
                                evenCuid.setAnioR(obj.getInt("AnioR"));
                                evenCuid.setMesR(obj.getInt("MesR"));
                                evenCuid.setDiaR(obj.getInt("DiaR"));
                                evenCuid.setHoraR(obj.getInt("HoraR"));
                                evenCuid.setMinutosR(obj.getInt("MinutosR"));
                                evenCuid.setLugar(obj.getString("Lugar"));
                                evenCuid.setDescripcion(obj.getString("Descripcion"));
                                evenCuid.setTono(obj.getString("Tono"));
                                evenCuid.setAlarma(obj.getBoolean("Alarma"));
                                evenCuid.setEliminado(obj.getBoolean("Eliminado"));

                                TblEventosCuidadores guardar_EC = new TblEventosCuidadores(
                                        evenCuid.getIdEventoC(), evenCuid.getIdCuidador(),
                                        evenCuid.getIdActividad(), evenCuid.getAnioE(),
                                        evenCuid.getMesE(), evenCuid.getDiaE(), evenCuid.getHoraE(),
                                        evenCuid.getMinutosE(), evenCuid.getAnioR(),evenCuid.getMesR(),
                                        evenCuid.getDiaR(), evenCuid.getHoraR(), evenCuid.getMinutosR(),
                                        evenCuid.getLugar(), evenCuid.getDescripcion(), evenCuid.getTono(),
                                        evenCuid.getAlarma(), evenCuid.getEliminado());
                                guardar_EC.save();
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

    //ACTUALIZAR EL ELIMINARDO DE UN EVENTO DE UN CUIDADOR
    public void EliminarEventoCuidador(final String... params){
        String id=params[0];
        String urlJson = "http://"+ip+"/ADP/EventosCuidador/EventoCuidadorEliminarObject/"+id;

        request = new JsonObjectRequest(Request.Method.DELETE, urlJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Boolean respuesta=response.getBoolean("Done");
                    if(respuesta){
                        Log.e("Evento Cuidador", "Eliminado");
                        TblEventosCuidadores eventoC = new TblEventosCuidadores();
                        eventoC.EliminarPorIdEventoRegTblEventosCuidadores(Long.parseLong(params[0]));
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
    //TAREA ASINCRONA PARA BUSCAR TODOS LOS EVENTOS DE UN CUIDADOR
    public void  BuscarAllEventosCuidadores(final String... params){
        String id=params[0];
        String urlJsonArray = "http://"+ip+"/ADP/EventosCuidador/EventoCuidadorBuscarXCuidadores/"+id;
        req = new JsonArrayRequest(urlJsonArray,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.d(TAG, response.toString());
                    try {
                        for (int i = 0; i <response.length(); i++) {
                            JSONObject obj=response.getJSONObject(i);

                            TblEventosCuidadores evenCuid = new TblEventosCuidadores();
                            evenCuid.setIdEventoC(obj.getLong("IdEventoC"));
                            evenCuid.setIdCuidador(obj.getLong("IdCuidador"));
                            evenCuid.setIdActividad(obj.getLong("IdActividad"));
                            evenCuid.setAnioE(obj.getInt("AnioE"));
                            evenCuid.setMesE(obj.getInt("MesE"));
                            evenCuid.setDiaE(obj.getInt("DiaE"));
                            evenCuid.setHoraE(obj.getInt("HoraE"));
                            evenCuid.setMinutosE(obj.getInt("MinutosE"));
                            evenCuid.setAnioR(obj.getInt("AnioR"));
                            evenCuid.setMesR(obj.getInt("MesR"));
                            evenCuid.setDiaR(obj.getInt("DiaR"));
                            evenCuid.setHoraR(obj.getInt("HoraR"));
                            evenCuid.setMinutosR(obj.getInt("MinutosR"));
                            evenCuid.setLugar(obj.getString("Lugar"));
                            evenCuid.setDescripcion(obj.getString("Descripcion"));
                            evenCuid.setTono(obj.getString("Tono"));
                            evenCuid.setAlarma(obj.getBoolean("Alarma"));
                            evenCuid.setEliminado(obj.getBoolean("Eliminado"));

                            TblEventosCuidadores guardar_EC = new TblEventosCuidadores(
                                    evenCuid.getIdEventoC(), evenCuid.getIdCuidador(),
                                    evenCuid.getIdActividad(), evenCuid.getAnioE(),
                                    evenCuid.getMesE(), evenCuid.getDiaE(), evenCuid.getHoraE(),
                                    evenCuid.getMinutosE(), evenCuid.getAnioR(),evenCuid.getMesR(),
                                    evenCuid.getDiaR(), evenCuid.getHoraR(), evenCuid.getMinutosR(),
                                    evenCuid.getLugar(), evenCuid.getDescripcion(), evenCuid.getTono(),
                                    evenCuid.getAlarma(), evenCuid.getEliminado());
                            guardar_EC.save();

                            Log.e("EventoCuidador", "#" + i + " Descargado: "+response.getJSONObject(i).toString());
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
