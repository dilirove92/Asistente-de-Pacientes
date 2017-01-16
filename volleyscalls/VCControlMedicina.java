package com.Notifications.patientssassistant.volleyscalls;

import android.content.Context;
import android.util.Log;

import com.Notifications.patientssassistant.VarEstatic;
import com.Notifications.patientssassistant.tables.TblActividades;
import com.Notifications.patientssassistant.tables.TblControlDieta;
import com.Notifications.patientssassistant.tables.TblControlMedicina;
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
public class VCControlMedicina extends VolleySingleton {

    JsonObjectRequest request;
    JsonArrayRequest req;
    android.content.Context Context;
    final String TAG = "VCControlMedicina";

    private static String ip= VarEstatic.ObtenerIP();

    public VCControlMedicina(android.content.Context context) {
        super(context);
        Context=context;
    }

    //TAREA ASINCRONA PARA INGRESAR UN NUEVO CONTROL DE MEDICINA
    public void InsertarControlMedicina(final String... params){

        String urlJson = "http://"+ip+"/ADP/ControlMedicina/ControlMedicinaInsertarObject";

        HashMap<String, String> dato = new HashMap<String, String>();
        dato.put("IdControlMedicina", params[0]);
        dato.put("IdPaciente", params[1]);
        dato.put("Medicamento", params[2]);
        dato.put("Descripcion", params[3]);
        dato.put("MotivoMedicacion", params[4]);
        dato.put("Tiempo",params[5]);
        dato.put("Dosis", params[6]);
        dato.put("NdeVeces", params[7]);
        dato.put("Tono", params[8]);
        dato.put("Eliminado",params[9]);

        request = new JsonObjectRequest(Request.Method.POST, urlJson, new JSONObject(dato), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Long respuesta=response.getLong("IdControlMedicina");
                    if (respuesta>0) {
                        TblControlMedicina contMedic = new TblControlMedicina(
                                respuesta, Long.parseLong(params[1]),
                                params[2], params[3], params[4], params[5],
                                params[6], Integer.parseInt(params[7]),
                                params[8], Boolean.parseBoolean(params[9]));
                        contMedic.save();
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

    //TAREA ASINCRONA PARA ACTUALIZAR CONTROL DE MEDICINA
    public void ActualizarControlMedicina(final String... params){

        String urlJson = "http://"+ip+"/ADP/ControlMedicina/ControlMedicinaActualizarObject";

        HashMap<String, String> dato = new HashMap<String, String>();
        dato.put("IdControlMedicina", params[0]);
        dato.put("IdPaciente", params[1]);
        dato.put("Medicamento", params[2]);
        dato.put("Descripcion", params[3]);
        dato.put("MotivoMedicacion", params[4]);
        dato.put("Tiempo",params[5]);
        dato.put("Dosis", params[6]);
        dato.put("NdeVeces", params[7]);
        dato.put("Tono", params[8]);
        dato.put("Eliminado",params[9]);

        request = new JsonObjectRequest(Request.Method.PUT, urlJson, new JSONObject(dato), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Boolean respuesta=response.getBoolean("Done");
                    if (respuesta) {
                        String campo_ide=String.valueOf(Long.parseLong(params[0]));
                        Select elControlDMedicina = Select.from(TblControlMedicina.class)
                                .where(Condition.prop("ID_CONTROL_MEDICINA").eq(campo_ide));
                        TblControlMedicina editar_CM=(TblControlMedicina)elControlDMedicina.first();

                        if (editar_CM!=null) {
                            editar_CM.setIdControlMedicina(Long.parseLong(params[0]));
                            editar_CM.setIdPaciente(Long.parseLong(params[1]));
                            editar_CM.setMedicamento(params[2]);
                            editar_CM.setDescripcion(params[3]);
                            editar_CM.setMotivoMedicacion(params[4]);
                            editar_CM.setTiempo(params[5]);
                            editar_CM.setDosis(params[6]);
                            editar_CM.setNdeVeces(Integer.parseInt(params[7]));
                            editar_CM.setTono(params[8]);
                            editar_CM.setEliminado(Boolean.parseBoolean(params[9]));
                            editar_CM.save();
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

    //TAREA ASINCRONA PARA BUSCAR UN REGISTRO DE CONTROL DE MEDICINA
    public void BuscarUnControlMedicina(final String... params){
        String id=params[0];
        String urlJsonObject = "http://"+ip+"/ADP/ControlMedicina/ControlMedicinaBuscar/"+id;
        request = new JsonObjectRequest(urlJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try {
                            TblControlMedicina contMedic = new TblControlMedicina();
                            contMedic.setIdControlMedicina(response.getLong("IdControlMedicina"));
                            contMedic.setIdPaciente(response.getLong("IdPaciente"));
                            contMedic.setMedicamento(response.getString("Medicamento"));
                            contMedic.setDescripcion(response.getString("Descripcion"));
                            contMedic.setMotivoMedicacion(response.getString("MotivoMedicacion"));
                            contMedic.setTiempo(response.getString("Tiempo"));
                            contMedic.setDosis(response.getString("Dosis"));
                            contMedic.setNdeVeces(response.getInt("NdeVeces"));
                            contMedic.setTono(response.getString("Tono"));
                            contMedic.setEliminado(response.getBoolean("Eliminado"));

                            if(contMedic!=null){

                                String campo_ide=String.valueOf(contMedic.getIdControlMedicina());
                                Select elControlDMedicina = Select.from(TblControlMedicina.class)
                                        .where(Condition.prop("ID_CONTROL_MEDICINA").eq(campo_ide));
                                TblControlMedicina editar_CM=(TblControlMedicina)elControlDMedicina.first();

                                if (editar_CM!=null) {
                                    editar_CM.setIdControlMedicina(contMedic.getIdControlMedicina());
                                    editar_CM.setIdPaciente(contMedic.getIdPaciente());
                                    editar_CM.setMedicamento(contMedic.getMedicamento());
                                    editar_CM.setDescripcion(contMedic.getDescripcion());
                                    editar_CM.setMotivoMedicacion(contMedic.getMotivoMedicacion());
                                    editar_CM.setTiempo(contMedic.getTiempo());
                                    editar_CM.setDosis(contMedic.getDosis());
                                    editar_CM.setNdeVeces(contMedic.getNdeVeces());
                                    editar_CM.setTono(contMedic.getTono());
                                    editar_CM.setEliminado(contMedic.getEliminado());
                                    editar_CM.save();
                                }
                                else{
                                    TblControlMedicina cM = new TblControlMedicina(
                                            contMedic.getIdControlMedicina(), contMedic.getIdPaciente(),
                                            contMedic.getMedicamento(), contMedic.getDescripcion(),
                                            contMedic.getMotivoMedicacion(), contMedic.getTiempo(),
                                            contMedic.getDosis(), contMedic.getNdeVeces(),
                                            contMedic.getTono(), contMedic.getEliminado());
                                    cM.save();
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

    //TAREA ASINCRONA PARA TRAER TODOS LOS DATOS DE CONTROL DE MEDICINA DE UN DETERMINADO PACIENTE
    public void BuscarAllControlMedicina(final String... params){
        String id=params[0];
        String urlJsonArray = "http://"+ip+"/ADP/ControlMedicina/ControlMedicinasBuscarAllXPaciente/"+id;
        req = new JsonArrayRequest(urlJsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        try {
                            for (int i = 0; i <response.length(); i++) {
                                JSONObject obj=response.getJSONObject(i);

                                TblControlMedicina contMedic = new TblControlMedicina();
                                contMedic.setIdControlMedicina(obj.getLong("IdControlMedicina"));
                                contMedic.setIdPaciente(obj.getLong("IdPaciente"));
                                contMedic.setMedicamento(obj.getString("Medicamento"));
                                contMedic.setDescripcion(obj.getString("Descripcion"));
                                contMedic.setMotivoMedicacion(obj.getString("MotivoMedicacion"));
                                contMedic.setTiempo(obj.getString("TipoTratamiento"));
                                contMedic.setDosis(obj.getString("Dosis"));
                                contMedic.setNdeVeces(obj.getInt("NdeVeces"));
                                contMedic.setTono(obj.getString("Tono"));
                                contMedic.setEliminado(obj.getBoolean("Eliminado"));

                                TblControlMedicina guardar_CM = new TblControlMedicina(
                                        contMedic.getIdControlMedicina(), contMedic.getIdPaciente(),
                                        contMedic.getMedicamento(), contMedic.getDescripcion(),
                                        contMedic.getMotivoMedicacion(), contMedic.getTiempo(),
                                        contMedic.getDosis(), contMedic.getNdeVeces(),
                                        contMedic.getTono() ,contMedic.getEliminado());
                                guardar_CM.save();
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

    //TAREA ASINCRONA PARA ELIMINAR UN DETERMINADO REGISTRO DE CONTROL DE MEDICINA
    public void EliminarControlMedicina(final String... params){
        String id=params[0];
        String urlJson = "http://"+ip+"/ADP/ControlMedicina/ControlMedicinaEliminarObject/"+id;

        request = new JsonObjectRequest(Request.Method.DELETE, urlJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Boolean respuesta=response.getBoolean("Done");
                    if(respuesta){
                        Log.e("ControlMedicina", "Eliminado");
                        TblControlMedicina controlM = new TblControlMedicina();
                        controlM.EliminarPorIdControlMedicinaRegTblControlMedicina(Long.parseLong(params[0]));
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

    //UTILIZADA PARA ACTUALIZAR LA BD
    //TAREA ASINCRONA PARA TRAER TODOS LOS DATOS DE CONTROL DE MEDICINA DE CUIDADOR
    public void BuscarAllControlMedicinaXCuidadores(final String... params){
        String id=params[0];
        String urlJsonArray = "http://"+ip+"/ADP/ControlMedicina/ControlMedicinaBuscarAllXCuidadores/"+id;
        req = new JsonArrayRequest(urlJsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i <response.length(); i++) {
                                JSONObject obj=response.getJSONObject(i);

                                TblControlMedicina contMedic = new TblControlMedicina();
                                contMedic.setIdControlMedicina(obj.getLong("IdControlMedicina"));
                                contMedic.setIdPaciente(obj.getLong("IdPaciente"));
                                contMedic.setMedicamento(obj.getString("Medicamento"));
                                contMedic.setDescripcion(obj.getString("Descripcion"));
                                contMedic.setMotivoMedicacion(obj.getString("MotivoMedicacion"));
                                contMedic.setTiempo(obj.getString("Tiempo"));
                                contMedic.setDosis(obj.getString("Dosis"));
                                contMedic.setNdeVeces(obj.getInt("NdeVeces"));
                                contMedic.setTono(obj.getString("Tono"));
                                contMedic.setEliminado(obj.getBoolean("Eliminado"));

                                TblControlMedicina guardar_CM = new TblControlMedicina(
                                        contMedic.getIdControlMedicina(), contMedic.getIdPaciente(),
                                        contMedic.getMedicamento(), contMedic.getDescripcion(),
                                        contMedic.getMotivoMedicacion(), contMedic.getTiempo(),
                                        contMedic.getDosis(), contMedic.getNdeVeces(),
                                        contMedic.getTono() ,contMedic.getEliminado());
                                guardar_CM.save();

                                Log.e("ControlMedicina ", "#" + i + " Descargado: "+response.getJSONObject(i).toString());
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
