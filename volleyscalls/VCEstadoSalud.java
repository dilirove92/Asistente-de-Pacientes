package com.Notifications.patientssassistant.volleyscalls;

import android.content.Context;
import android.util.Log;

import com.Notifications.patientssassistant.VarEstatic;
import com.Notifications.patientssassistant.tables.TblActividades;
import com.Notifications.patientssassistant.tables.TblEstadoSalud;
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
public class VCEstadoSalud extends VolleySingleton {

    JsonObjectRequest request;
    JsonArrayRequest req;
    android.content.Context Context;
    final String TAG = "VCEstadoSalud";
    private static String ip= VarEstatic.ObtenerIP();

    public VCEstadoSalud(android.content.Context context) {
        super(context);
        Context=context;
    }

    //INSERTAR UN NUEVO REGISTRO DE ESTADO DE SALUD
    public void InsertarEstadoSalud(final String... params){

        String urlJson = "http://"+ip+"/ADP/EstadoSalud/EstadoSaludInsertarObject";

        HashMap<String, String> dato = new HashMap<String, String>();
        dato.put("IdPaciente", params[0]);
        dato.put("TipoSangre", params[1]);
        dato.put("FacultadMental", params[2]);
        dato.put("Enfermedad", params[3]);
        dato.put("DescEnfermedad", params[4]);
        dato.put("Cirugias",params[5]);
        dato.put("DescCirugias", params[6]);
        dato.put("Medicamentos", params[7]);
        dato.put("DescMedicamentos", params[8]);
        dato.put("Discapacidad", params[9]);
        dato.put("TipoDiscapacidad", params[10]);
        dato.put("GradoDiscapacidad", params[11]);
        dato.put("Implementos", params[12]);
        dato.put("Eliminado",params[13]);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, urlJson, new JSONObject(dato), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Boolean respuesta=response.getBoolean("Done");
                    if (respuesta) {
                        TblEstadoSalud eSalud = new TblEstadoSalud(
                                Long.parseLong(params[0]), params[1],
                                params[2], Boolean.parseBoolean(params[3]),
                                params[4], Boolean.parseBoolean(params[5]),
                                params[6], Boolean.parseBoolean(params[7]),
                                params[8], Boolean.parseBoolean(params[9]),
                                params[10], params[11], params[12],
                                Boolean.parseBoolean(params[13]));
                        eSalud.save();
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

    //ACTUALIZAR UN REGISTRO DE ESTADO DE SALUD DE UN PACIENTE
    public void ActualizarEstadoSalud(final String... params){

        String urlJson = "http://"+ip+"/ADP/EstadoSalud/EstadoSaludActualizarObject";

        HashMap<String, String> dato = new HashMap<String, String>();
        dato.put("IdPaciente", params[0]);
        dato.put("TipoSangre", params[1]);
        dato.put("FacultadMental", params[2]);
        dato.put("Enfermedad", params[3]);
        dato.put("DescEnfermedad", params[4]);
        dato.put("Cirugias",params[5]);
        dato.put("DescCirugias", params[6]);
        dato.put("Medicamentos", params[7]);
        dato.put("DescMedicamentos", params[8]);
        dato.put("Discapacidad", params[9]);
        dato.put("TipoDiscapacidad", params[10]);
        dato.put("GradoDiscapacidad", params[11]);
        dato.put("Implementos", params[12]);
        dato.put("Eliminado",params[13]);

        request = new JsonObjectRequest(Request.Method.PUT, urlJson, new JSONObject(dato), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Boolean respuesta=response.getBoolean("Done");
                    if (respuesta) {
                        String campo_ide=String.valueOf(Long.parseLong(params[0]));
                        Select estadoSalud = Select.from(TblEstadoSalud.class)
                                .where(Condition.prop("ID_PACIENTE").eq(campo_ide));
                        TblEstadoSalud edit_ES=(TblEstadoSalud)estadoSalud.first();

                        if (edit_ES!=null) {
                            edit_ES.setIdPaciente(Long.parseLong(params[0]));
                            edit_ES.setTipoSangre(params[1]);
                            edit_ES.setFacultadMental(params[2]);
                            edit_ES.setEnfermedad(Boolean.parseBoolean(params[3]));
                            edit_ES.setDescEnfermedad(params[4]);
                            edit_ES.setCirugias(Boolean.parseBoolean(params[5]));
                            edit_ES.setDescCirugias(params[6]);
                            edit_ES.setMedicamentos(Boolean.parseBoolean(params[7]));
                            edit_ES.setDescMedicamentos(params[8]);
                            edit_ES.setDiscapacidad(Boolean.parseBoolean(params[9]));
                            edit_ES.setTipoDiscapacidad(params[10]);
                            edit_ES.setGradoDiscapacidad(params[11]);
                            edit_ES.setImplementos(params[12]);
                            edit_ES.setEliminado(Boolean.parseBoolean(params[13]));
                            edit_ES.save();
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

    //BUSCAR UN REGISTRO DE ESTADO DE SALUD
    public void BuscarUnEstadoSalud(final String... params){
        String id=params[0];
        String urlJsonObject = "http://"+ip+"/ADP/EstadoSalud/EstadoSaludBuscar/"+id;
        request = new JsonObjectRequest(urlJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try {
                            TblEstadoSalud eSalud = new TblEstadoSalud();
                            eSalud.setIdPaciente(response.getLong("IdPaciente"));
                            eSalud.setTipoSangre(response.getString("TipoSangre"));
                            eSalud.setFacultadMental(response.getString("FacultadMental"));
                            eSalud.setEnfermedad(response.getBoolean("Enfermedad"));
                            eSalud.setDescEnfermedad(response.getString("DescEnfermedad"));
                            eSalud.setCirugias(response.getBoolean("Cirugias"));
                            eSalud.setDescCirugias(response.getString("DescCirugias"));
                            eSalud.setMedicamentos(response.getBoolean("Medicamentos"));
                            eSalud.setDescMedicamentos(response.getString("DescMedicamentos"));
                            eSalud.setDiscapacidad(response.getBoolean("Discapacidad"));
                            eSalud.setTipoDiscapacidad(response.getString("TipoDiscapacidad"));
                            eSalud.setGradoDiscapacidad(response.getString("GradoDiscapacidad"));
                            eSalud.setImplementos(response.getString("Implementos"));
                            eSalud.setEliminado(response.getBoolean("Eliminado"));

                            if(eSalud!=null){

                                String campo_ide=String.valueOf(eSalud.getIdPaciente());
                                Select estadoSalud = Select.from(TblEstadoSalud.class)
                                        .where(Condition.prop("ID_PACIENTE").eq(campo_ide));
                                TblEstadoSalud edit_ES=(TblEstadoSalud)estadoSalud.first();

                                if (edit_ES!=null) {
                                    edit_ES.setIdPaciente(eSalud.getIdPaciente());
                                    edit_ES.setTipoSangre(eSalud.getTipoSangre());
                                    edit_ES.setFacultadMental(eSalud.getFacultadMental());
                                    edit_ES.setEnfermedad(eSalud.getEnfermedad());
                                    edit_ES.setDescEnfermedad(eSalud.getDescEnfermedad());
                                    edit_ES.setCirugias(eSalud.getCirugias());
                                    edit_ES.setDescCirugias(eSalud.getDescCirugias());
                                    edit_ES.setMedicamentos(eSalud.getMedicamentos());
                                    edit_ES.setDescMedicamentos(eSalud.getDescMedicamentos());
                                    edit_ES.setDiscapacidad(eSalud.getDiscapacidad());
                                    edit_ES.setTipoDiscapacidad(eSalud.getTipoDiscapacidad());
                                    edit_ES.setGradoDiscapacidad(eSalud.getGradoDiscapacidad());
                                    edit_ES.setImplementos(eSalud.getImplementos());
                                    edit_ES.setEliminado(eSalud.getEliminado());
                                    edit_ES.save();
                                }
                                else{
                                    TblEstadoSalud newESalud = new TblEstadoSalud(
                                            eSalud.getIdPaciente(), eSalud.getTipoSangre(),
                                            eSalud.getFacultadMental(), eSalud.getEnfermedad(),
                                            eSalud.getDescEnfermedad(), eSalud.getCirugias(),
                                            eSalud.getDescCirugias(), eSalud.getMedicamentos(),
                                            eSalud.getDescMedicamentos(), eSalud.getDiscapacidad(),
                                            eSalud.getTipoDiscapacidad(), eSalud.getGradoDiscapacidad(),
                                            eSalud.getImplementos(), eSalud.getEliminado());
                                    newESalud.save();
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

    //BUSCAR REGISTROS DE ESTADO DE SALUD DE UN PACIENTE
    public void BuscarAllEstadosSaludPaciente(final String... params){
        String id=params[0];
        String urlJsonArray = "http://"+ip+"/ADP/EstadoSalud/EstadosSaludBuscarXPaciente/"+id;
        req = new JsonArrayRequest(urlJsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        List<TblActividades> actividades=new ArrayList<TblActividades>();
                        try {
                            for (int i = 0; i <response.length(); i++) {
                                JSONObject obj=response.getJSONObject(i);

                                TblEstadoSalud eSalud = new TblEstadoSalud();
                                eSalud.setIdPaciente(obj.getLong("IdPaciente"));
                                eSalud.setTipoSangre(obj.getString("TipoSangre"));
                                eSalud.setFacultadMental(obj.getString("FacultadMental"));
                                eSalud.setEnfermedad(obj.getBoolean("Enfermedad"));
                                eSalud.setDescEnfermedad(obj.getString("DescEnfermedad"));
                                eSalud.setCirugias(obj.getBoolean("Cirugias"));
                                eSalud.setDescCirugias(obj.getString("DescCirugias"));
                                eSalud.setMedicamentos(obj.getBoolean("Medicamentos"));
                                eSalud.setDescMedicamentos(obj.getString("DescMedicamentos"));
                                eSalud.setDiscapacidad(obj.getBoolean("Discapacidad"));
                                eSalud.setTipoDiscapacidad(obj.getString("TipoDiscapacidad"));
                                eSalud.setGradoDiscapacidad(obj.getString("GradoDiscapacidad"));
                                eSalud.setImplementos(obj.getString("Implementos"));
                                eSalud.setEliminado(obj.getBoolean("Eliminado"));

                                TblEstadoSalud guardar_EstSal = new TblEstadoSalud(
                                        eSalud.getIdPaciente(), eSalud.getTipoSangre(),
                                        eSalud.getFacultadMental(), eSalud.getEnfermedad(),
                                        eSalud.getDescEnfermedad(), eSalud.getCirugias(),
                                        eSalud.getDescCirugias(), eSalud.getMedicamentos(),
                                        eSalud.getDescMedicamentos(), eSalud.getDiscapacidad(),
                                        eSalud.getTipoDiscapacidad(), eSalud.getGradoDiscapacidad(),
                                        eSalud.getImplementos(), eSalud.getEliminado());
                                guardar_EstSal.save();
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

    //ACTUALIZAR EL ELIMINADO DE ESTADO DE SALUD
    public void EliminarActividades(final String... params){
        String id=params[0];
        String urlJson = "http://"+ip+"/ADP/EstadoSalud/EstadoSaludEliminarObject/"+id;

        request = new JsonObjectRequest(Request.Method.DELETE, urlJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Boolean respuesta=response.getBoolean("Done");
                    if(respuesta){
                        Log.e("Estado de Salud", "Eliminado");
                        TblEstadoSalud eSalud = new TblEstadoSalud();
                        eSalud.EliminarPorIdPacienteRegTblEstadoSalud(Long.parseLong(params[0]));
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

    //UTILIZADA PARA LA ACTUALIZACION DE LA BD
    //TAREA ASINCRONA PARA BUSCAR UN REGISTRO DE ESTADO DE SALUD DE UN PACIENTE
    public void BuscarAllEstadosSaludCuidadores(final String... params){
        String id=params[0];
        String urlJsonArray = "http://"+ip+"/ADP/EstadoSalud/EstadoSaludBuscarXCuidadores/"+id;
        req = new JsonArrayRequest(urlJsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        List<TblActividades> actividades=new ArrayList<TblActividades>();
                        try {
                            for (int i = 0; i <response.length(); i++) {
                                JSONObject obj=response.getJSONObject(i);

                                TblEstadoSalud eSalud = new TblEstadoSalud();
                                eSalud.setIdPaciente(obj.getLong("IdPaciente"));
                                eSalud.setTipoSangre(obj.getString("TipoSangre"));
                                eSalud.setFacultadMental(obj.getString("FacultadMental"));
                                eSalud.setEnfermedad(obj.getBoolean("Enfermedad"));
                                eSalud.setDescEnfermedad(obj.getString("DescEnfermedad"));
                                eSalud.setCirugias(obj.getBoolean("Cirugias"));
                                eSalud.setDescCirugias(obj.getString("DescCirugias"));
                                eSalud.setMedicamentos(obj.getBoolean("Medicamentos"));
                                eSalud.setDescMedicamentos(obj.getString("DescMedicamentos"));
                                eSalud.setDiscapacidad(obj.getBoolean("Discapacidad"));
                                eSalud.setTipoDiscapacidad(obj.getString("TipoDiscapacidad"));
                                eSalud.setGradoDiscapacidad(obj.getString("GradoDiscapacidad"));
                                eSalud.setImplementos(obj.getString("Implementos"));
                                eSalud.setEliminado(obj.getBoolean("Eliminado"));

                                TblEstadoSalud guardar_EstSal = new TblEstadoSalud(
                                        eSalud.getIdPaciente(), eSalud.getTipoSangre(),
                                        eSalud.getFacultadMental(), eSalud.getEnfermedad(),
                                        eSalud.getDescEnfermedad(), eSalud.getCirugias(),
                                        eSalud.getDescCirugias(), eSalud.getMedicamentos(),
                                        eSalud.getDescMedicamentos(), eSalud.getDiscapacidad(),
                                        eSalud.getTipoDiscapacidad(), eSalud.getGradoDiscapacidad(),
                                        eSalud.getImplementos(), eSalud.getEliminado());
                                guardar_EstSal.save();

                                Log.e("EstadoSalud ", "#" + i + " Descargado: "+response.getJSONObject(i).toString());
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
