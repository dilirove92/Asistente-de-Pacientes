package com.Notifications.patientssassistant.volleyscalls;

import android.content.Context;
import android.util.Log;

import com.Notifications.patientssassistant.VarEstatic;
import com.Notifications.patientssassistant.tables.TblRutinasPacientes;
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
 * Created by Jazmine on 19/12/2015.
 */
public class VCRutinasPacientes extends VolleySingleton{

    JsonObjectRequest request;
    JsonArrayRequest req;
    android.content.Context Context;
    final String TAG = "VCActividadCuidadores";
    private static String ip= VarEstatic.ObtenerIP();

    public VCRutinasPacientes(android.content.Context context) {
        super(context);
        Context=context;
    }

    //INSERTAR UNA NUEVA RUTINA DE PACIENTES
    public void InsertarRutinasPacientes(final String... params){

        String urlJson = "http://"+ip+"/ADP/RutinasPacientes/RutinaPacienteInsertarObject";

        HashMap<String, String> dato = new HashMap<String, String>();
        dato.put("IdRutinaP", params[0]);
        dato.put("IdPaciente", params[1]);
        dato.put("IdActividad", params[2]);
        dato.put("Hora",params[3]);
        dato.put("Minutos",params[4]);
        dato.put("Domingo", params[5]);
        dato.put("Lunes",params[6]);
        dato.put("Martes", params[7]);
        dato.put("Miercoles", params[8]);
        dato.put("Jueves", params[9]);
        dato.put("Viernes", params[10]);
        dato.put("Sabado", params[11]);
        dato.put("Tono", params[12]);
        dato.put("Descripcion", params[13]);
        dato.put("Alarma", params[14]);
        dato.put("Eliminado",params[15]);

        request = new JsonObjectRequest(Request.Method.POST, urlJson, new JSONObject(dato), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Long respuesta=response.getLong("IdRutinaP");
                    if (respuesta>0) {
                        TblRutinasPacientes rutina = new TblRutinasPacientes(
                                respuesta, Long.parseLong(params[1]),
                                Long.parseLong(params[2]),
                                Integer.parseInt(params[3]),
                                Integer.parseInt(params[4]),
                                Boolean.parseBoolean(params[5]),
                                Boolean.parseBoolean(params[6]),
                                Boolean.parseBoolean(params[7]),
                                Boolean.parseBoolean(params[8]),
                                Boolean.parseBoolean(params[9]),
                                Boolean.parseBoolean(params[10]),
                                Boolean.parseBoolean(params[11]),
                                params[12], params[13],
                                Boolean.parseBoolean(params[14]),
                                Boolean.parseBoolean(params[15]));
                        rutina.save();
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

    //ACTUALIZAR RUTINAS DE PACIENTES
    public void ActualizarRutinasPacientes(final String... params){

        String urlJson = "http://"+ip+"/ADP/RutinasPacientes/RutinaPacienteActualizarObject";

        HashMap<String, String> dato = new HashMap<String, String>();
        dato.put("IdRutinaP", params[0]);
        dato.put("IdPaciente", params[1]);
        dato.put("IdActividad", params[2]);
        dato.put("Hora",params[3]);
        dato.put("Minutos",params[4]);
        dato.put("Domingo", params[5]);
        dato.put("Lunes",params[6]);
        dato.put("Martes", params[7]);
        dato.put("Miercoles", params[8]);
        dato.put("Jueves", params[9]);
        dato.put("Viernes", params[10]);
        dato.put("Sabado", params[11]);
        dato.put("Tono", params[12]);
        dato.put("Descripcion", params[13]);
        dato.put("Alarma", params[14]);
        dato.put("Eliminado",params[15]);

        request = new JsonObjectRequest(Request.Method.PUT, urlJson, new JSONObject(dato), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    boolean respuesta=response.getBoolean("Done");
                    if (respuesta) {
                        String campo_ide=String.valueOf(Long.parseLong(params[0]));
                        Select laRutina = Select.from(TblRutinasPacientes.class)
                                .where(Condition.prop("ID_RUTINA_P").eq(campo_ide));
                        TblRutinasPacientes edit_Rutina=(TblRutinasPacientes)laRutina.first();

                        if (edit_Rutina!=null) {
                            edit_Rutina.setIdRutinaP(Long.parseLong(params[0]));
                            edit_Rutina.setIdPaciente(Long.parseLong(params[1]));
                            edit_Rutina.setIdActividad(Long.parseLong(params[2]));
                            edit_Rutina.setHora(Integer.parseInt(params[3]));
                            edit_Rutina.setMinutos(Integer.parseInt(params[4]));
                            edit_Rutina.setDomingo(Boolean.parseBoolean(params[5]));
                            edit_Rutina.setLunes(Boolean.parseBoolean(params[6]));
                            edit_Rutina.setMartes(Boolean.parseBoolean(params[7]));
                            edit_Rutina.setMiercoles(Boolean.parseBoolean(params[8]));
                            edit_Rutina.setJueves(Boolean.parseBoolean(params[9]));
                            edit_Rutina.setViernes(Boolean.parseBoolean(params[10]));
                            edit_Rutina.setSabado(Boolean.parseBoolean(params[11]));
                            edit_Rutina.setTono(params[12]);
                            edit_Rutina.setDescripcion(params[13]);
                            edit_Rutina.setAlarma(Boolean.parseBoolean(params[14]));
                            edit_Rutina.setEliminado(Boolean.parseBoolean(params[15]));
                            edit_Rutina.save();
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

    //BUSCAR UNA RUTINA DE UN PACIENTE
    public void BuscarUnaRutinaPacientes(final String... params){
        String id=params[0];
        String urlJsonObject = "http://"+ip+"/ADP/RutinasPacientes/RutinaPacienteBuscar/"+id;
        request = new JsonObjectRequest(urlJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try {
                            TblRutinasPacientes rutina = new TblRutinasPacientes();
                            rutina.setIdRutinaP(response.getLong("IdRutinaP"));
                            rutina.setIdPaciente(response.getLong("IdPaciente"));
                            rutina.setIdActividad(response.getLong("IdActividad"));
                            rutina.setHora(response.getInt("Hora"));
                            rutina.setMinutos(response.getInt("Minutos"));
                            rutina.setDomingo(response.getBoolean("Domingo"));
                            rutina.setLunes(response.getBoolean("Lunes"));
                            rutina.setMartes(response.getBoolean("Martes"));
                            rutina.setMiercoles(response.getBoolean("Miercoles"));
                            rutina.setJueves(response.getBoolean("Jueves"));
                            rutina.setViernes(response.getBoolean("Viernes"));
                            rutina.setSabado(response.getBoolean("Sabado"));
                            rutina.setTono(response.getString("Tono"));
                            rutina.setDescripcion(response.getString("Descripcion"));
                            rutina.setAlarma(response.getBoolean("Alarma"));
                            rutina.setEliminado(response.getBoolean("Eliminado"));

                            if(rutina!=null){

                                String campo_ide=String.valueOf(rutina.getIdRutinaP());
                                Select laRutina = Select.from(TblRutinasPacientes.class)
                                        .where(Condition.prop("ID_RUTINA_P").eq(campo_ide));
                                TblRutinasPacientes edit_Rutina=(TblRutinasPacientes)laRutina.first();

                                if (edit_Rutina!=null) {
                                    edit_Rutina.setIdRutinaP(rutina.getIdRutinaP());
                                    edit_Rutina.setIdPaciente(rutina.getIdPaciente());
                                    edit_Rutina.setIdActividad(rutina.getIdActividad());
                                    edit_Rutina.setHora(rutina.getHora());
                                    edit_Rutina.setMinutos(rutina.getMinutos());
                                    edit_Rutina.setDomingo(rutina.getDomingo());
                                    edit_Rutina.setLunes(rutina.getLunes());
                                    edit_Rutina.setMartes(rutina.getMartes());
                                    edit_Rutina.setMiercoles(rutina.getMiercoles());
                                    edit_Rutina.setJueves(rutina.getJueves());
                                    edit_Rutina.setViernes(rutina.getViernes());
                                    edit_Rutina.setSabado(rutina.getSabado());
                                    edit_Rutina.setTono(rutina.getTono());
                                    edit_Rutina.setDescripcion(rutina.getDescripcion());
                                    edit_Rutina.setAlarma(rutina.getAlarma());
                                    edit_Rutina.setEliminado(rutina.getEliminado());
                                    edit_Rutina.save();
                                }
                                else{
                                    TblRutinasPacientes newRutinaP = new TblRutinasPacientes(
                                            rutina.getIdRutinaP(), rutina.getIdPaciente(),
                                            rutina.getIdActividad(), rutina.getHora(),
                                            rutina.getMinutos(), rutina.getDomingo(),
                                            rutina.getLunes(), rutina.getMartes(),
                                            rutina.getMiercoles(), rutina.getJueves(),
                                            rutina.getViernes(), rutina.getSabado(),
                                            rutina.getTono(), rutina.getDescripcion(),
                                            rutina.getAlarma(), rutina.getEliminado());
                                    newRutinaP.save();
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

    //BUSCAR RUTINAS DE PACIENTES
    public void TblRutinasCuidadores(final String... params){
        String id=params[0];
        String urlJsonArray = "http://"+ip+"/ADP/RutinasPacientes/RutinasPacientesBuscarXPaciente/"+id;
        req = new JsonArrayRequest(urlJsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        try {
                            for (int i = 0; i <response.length(); i++) {
                                JSONObject obj=response.getJSONObject(i);

                                TblRutinasPacientes rutina = new TblRutinasPacientes();
                                rutina.setIdRutinaP(obj.getLong("IdRutinaP"));
                                rutina.setIdPaciente(obj.getLong("IdPaciente"));
                                rutina.setIdActividad(obj.getLong("IdActividad"));
                                rutina.setHora(obj.getInt("Hora"));
                                rutina.setMinutos(obj.getInt("Minutos"));
                                rutina.setDomingo(obj.getBoolean("Domingo"));
                                rutina.setLunes(obj.getBoolean("Lunes"));
                                rutina.setMartes(obj.getBoolean("Martes"));
                                rutina.setMiercoles(obj.getBoolean("Miercoles"));
                                rutina.setJueves(obj.getBoolean("Jueves"));
                                rutina.setViernes(obj.getBoolean("Viernes"));
                                rutina.setSabado(obj.getBoolean("Sabado"));
                                rutina.setTono(obj.getString("Tono"));
                                rutina.setDescripcion(obj.getString("Descripcion"));
                                rutina.setAlarma(obj.getBoolean("Alarma"));
                                rutina.setEliminado(obj.getBoolean("Eliminado"));


                                TblRutinasPacientes guardar_rutina = new TblRutinasPacientes(
                                        rutina.getIdPaciente(), rutina.getIdPaciente(),
                                        rutina.getIdActividad(), rutina.getHora(),
                                        rutina.getMinutos(), rutina.getDomingo(),
                                        rutina.getLunes(), rutina.getMartes(),
                                        rutina.getMiercoles(), rutina.getJueves(),
                                        rutina.getViernes(), rutina.getSabado(),
                                        rutina.getTono(), rutina.getDescripcion(),
                                        rutina.getAlarma(), rutina.getEliminado());
                                guardar_rutina.save();
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

    //ELIMINAR UNA RUTINA DE UN PACIENTE
    public void EliminarRutinasPacientes(final String... params){
        String id=params[0];
        String urlJson = "http://"+ip+"/ADP/RutinasPacientes/RutinaPacienteEliminarObject/"+id;
        final String TAG=params[1];

        request = new JsonObjectRequest(Request.Method.DELETE, urlJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Boolean respuesta=response.getBoolean("Done");
                    if(respuesta){
                        Log.e("Rutinas Pacientes", "Eliminado");
                        TblRutinasPacientes rutinasP = new TblRutinasPacientes();
                        rutinasP.EliminarPorIdRutinaRegTblRutinasPacientes(Long.parseLong(params[0]));
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
    //BUSCAR RUTINAS DE PACIENTES
    public void BuscarAllRutinasCuidadores(final String... params){
        String id=params[0];
        String urlJsonArray = "http://"+ip+"/ADP/RutinasPacientes/RutinaPacienteBuscarXCuidadores/"+id;
        req = new JsonArrayRequest(urlJsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        try {
                            for (int i = 0; i <response.length(); i++) {
                                JSONObject obj=response.getJSONObject(i);

                                TblRutinasPacientes rutina = new TblRutinasPacientes();
                                rutina.setIdRutinaP(obj.getLong("IdRutinaP"));
                                rutina.setIdPaciente(obj.getLong("IdPaciente"));
                                rutina.setIdActividad(obj.getLong("IdActividad"));
                                rutina.setHora(obj.getInt("Hora"));
                                rutina.setMinutos(obj.getInt("Minutos"));
                                rutina.setDomingo(obj.getBoolean("Domingo"));
                                rutina.setLunes(obj.getBoolean("Lunes"));
                                rutina.setMartes(obj.getBoolean("Martes"));
                                rutina.setMiercoles(obj.getBoolean("Miercoles"));
                                rutina.setJueves(obj.getBoolean("Jueves"));
                                rutina.setViernes(obj.getBoolean("Viernes"));
                                rutina.setSabado(obj.getBoolean("Sabado"));
                                rutina.setTono(obj.getString("Tono"));
                                rutina.setDescripcion(obj.getString("Descripcion"));
                                rutina.setAlarma(obj.getBoolean("Alarma"));
                                rutina.setEliminado(obj.getBoolean("Eliminado"));

                                TblRutinasPacientes guardar_rutina = new TblRutinasPacientes(
                                        rutina.getIdRutinaP(), rutina.getIdPaciente(),
                                        rutina.getIdActividad(), rutina.getHora(),
                                        rutina.getMinutos(), rutina.getDomingo(),
                                        rutina.getLunes(), rutina.getMartes(),
                                        rutina.getMiercoles(), rutina.getJueves(),
                                        rutina.getViernes(), rutina.getSabado(),
                                        rutina.getTono(), rutina.getDescripcion(),
                                        rutina.getAlarma(), rutina.getEliminado());
                                guardar_rutina.save();

                                Log.e("RutinaPaciente", "#" + i + " Descargado: "+response.getJSONObject(i).toString());
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
