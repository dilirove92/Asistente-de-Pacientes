package com.Notifications.patientssassistant.volleyscalls;

import android.content.Context;
import android.print.PageRange;
import android.util.Log;

import com.Notifications.patientssassistant.VarEstatic;
import com.Notifications.patientssassistant.tables.TblActividades;
import com.Notifications.patientssassistant.tables.TblPermisos;
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
public class VCPermisos extends VolleySingleton{

    JsonObjectRequest request;
    JsonArrayRequest req;
    android.content.Context Context;
    final String TAG = "VCPermisos";
    private static String ip= VarEstatic.ObtenerIP();

    public VCPermisos(Context context) {
        super(context);
        Context = context;
    }

    //INSERTAR UN PERMISO
    public void InsertarPermiso(final String... params){

        String urlJson = "http://"+ip+"/ADP/Permisos/PermisoInsertarObject";

        HashMap<String, String> dato = new HashMap<String, String>();
        dato.put("IdPermiso", params[0]);
        dato.put("IdCuidador", params[1]);
        dato.put("IdPaciente", params[2]);
        dato.put("NotifiAlarma", params[3]);
        dato.put("RegCreador",params[4]);
        dato.put("ContMedicina", params[5]);
        dato.put("Eliminado",params[6]);

        request = new JsonObjectRequest(Request.Method.POST, urlJson, new JSONObject(dato), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Long respuesta=response.getLong("IdPermiso");
                    if (respuesta>0) {
                        TblPermisos permiso = new TblPermisos(
                                respuesta, Long.parseLong(params[1]),
                                Long.parseLong(params[2]),
                                Boolean.parseBoolean(params[3]),
                                Boolean.parseBoolean(params[5]),
                                Boolean.parseBoolean(params[4]),
                                Boolean.parseBoolean(params[6]));
                        permiso.save();
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

    //ACTUALIZAR UN PERMISO
    public void ActualizarPermiso(final String... params){

        String urlJson = "http://"+ip+"/ADP/Permisos/PermisoActualizarObject";

        HashMap<String, String> dato = new HashMap<String, String>();
        dato.put("IdPermiso", params[0]);
        dato.put("NotifiAlarma", params[1]);
        dato.put("ContMedicina", params[2]);

        request = new JsonObjectRequest(Request.Method.PUT, urlJson, new JSONObject(dato), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    boolean respuesta=response.getBoolean("Done");
                    if (respuesta) {
                        TblPermisos permiso= new TblPermisos();
                        permiso.setIdPermiso(Long.parseLong(params[0]));
                        permiso.setNotifiAlarma(Boolean.parseBoolean(params[1]));
                        permiso.setContMedicina(Boolean.parseBoolean(params[2]));

                        String campo_ide=String.valueOf(permiso.getIdPermiso());
                        Select elPermiso = Select.from(TblPermisos.class)
                                .where(Condition.prop("ID_PERMISO").eq(campo_ide));
                        TblPermisos edit_Per=(TblPermisos)elPermiso.first();

                        if (edit_Per!=null) {
                            edit_Per.setIdPermiso(permiso.getIdPermiso());
                            edit_Per.setNotifiAlarma(permiso.getNotifiAlarma());
                            edit_Per.setContMedicina(permiso.getContMedicina());
                            edit_Per.save();
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

    //BUSCAR UN PERMISO
    public void BuscarUnaActividad(final String... params){
        String id=params[0];
        String urlJsonObject = "http://"+ip+"/ADP/Permisos/PermisoBuscar/"+id;
        request = new JsonObjectRequest(urlJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try {
                            TblPermisos permiso = new TblPermisos();
                            permiso.setIdPermiso(response.getLong("IdPermiso"));
                            permiso.setIdCuidador(response.getLong("IdCuidador"));
                            permiso.setIdPaciente(response.getLong("IdPaciente"));
                            permiso.setNotifiAlarma(response.getBoolean("NotifiAlarma"));
                            permiso.setRegCreador(response.getBoolean("RegCreador"));
                            permiso.setContMedicina(response.getBoolean("ContMedicina"));
                            permiso.setEliminado(response.getBoolean("Eliminado"));

                            if(permiso!=null){

                                String campo_ide=String.valueOf(permiso.getIdPermiso());
                                Select elPermiso = Select.from(TblPermisos.class)
                                        .where(Condition.prop("ID_PERMISO").eq(campo_ide));
                                TblPermisos edit_Per=(TblPermisos)elPermiso.first();

                                if (edit_Per!=null) {
                                    edit_Per.setIdPermiso(permiso.getIdPermiso());
                                    edit_Per.setIdCuidador(permiso.getIdCuidador());
                                    edit_Per.setIdPaciente(permiso.getIdPaciente());
                                    edit_Per.setNotifiAlarma(permiso.getNotifiAlarma());
                                    edit_Per.setRegCreador(permiso.getRegCreador());
                                    edit_Per.setContMedicina(permiso.getContMedicina());
                                    edit_Per.setEliminado(permiso.getEliminado());
                                    edit_Per.save();
                                }
                                else{
                                    TblPermisos newPermiso = new TblPermisos(
                                            permiso.getIdPermiso(), permiso.getIdCuidador(),
                                            permiso.getIdPaciente(), permiso.getNotifiAlarma(),
                                            permiso.getContMedicina(), permiso.getRegCreador(),
                                            permiso.getEliminado());
                                    newPermiso.save();
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

    //BUSCAR PERMISOS
    public void BuscarAllPermisosXCuidador(final String... params){
        String id=params[0];
        String urlJsonArray = "http://"+ip+"/ADP/Permisos/PermisosBuscarXCuidador/"+id;
        req = new JsonArrayRequest(urlJsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        try {
                            for (int i = 0; i <response.length(); i++) {
                                JSONObject obj=response.getJSONObject(i);

                                TblPermisos permiso=new TblPermisos();
                                permiso.setIdPermiso(obj.getLong("IdPermiso"));
                                permiso.setIdCuidador(obj.getLong("IdCuidador"));
                                permiso.setIdPaciente(obj.getLong("IdPaciente"));
                                permiso.setNotifiAlarma(obj.getBoolean("NotifiAlarma"));
                                permiso.setRegCreador(obj.getBoolean("RegCreador"));
                                permiso.setContMedicina(obj.getBoolean("ContMedicina"));
                                permiso.setEliminado(obj.getBoolean("Eliminado"));


                                TblPermisos guardar_permiso = new TblPermisos(
                                        permiso.getIdPermiso(), permiso.getIdCuidador(),
                                        permiso.getIdPaciente(), permiso.getNotifiAlarma(),
                                        permiso.getContMedicina(), permiso.getRegCreador(),
                                        permiso.getEliminado());
                                guardar_permiso.save();
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

    //BUSCAR UN CUIDADOR CREADOR
    public void BuscarCuidadorCreador(final String... params){
        String id=params[0];
        String urlJsonObject = "http://"+ip+"/ADP/Permisos/PermisoBuscarCreador/"+id;
        request = new JsonObjectRequest(urlJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try {
                            TblPermisos permiso = new TblPermisos();
                            permiso.setIdCuidador(response.getLong("IdCuidador"));;
                            if(permiso.getIdCuidador()>0) {
                                Log.e("PERMISOS", "Encontrado");
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

    //BUSCAR EL CUIDADOR DE PETICIONES
    public void BuscarCuidadorPeticiones(final String... params){
        String id=params[0];
        String urlJsonObject = "http://"+ip+"/ADP/Permisos/PermisoBuscarCuidadorPeticiones/"+id;
        request = new JsonObjectRequest(urlJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try {
                            TblPermisos permiso = new TblPermisos();
                            permiso.setIdCuidador(response.getLong("IdCuidador"));;
                            if(permiso.getIdCuidador()>0) {
                                Log.e("PERMISOS", "Encontrado");
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

    //ACTUALIZAR EL ELIMINADO DE UN PERMISO
    public void EliminarPermisos(final String... params){
        String id=params[0];
        String urlJson = "http://"+ip+"/ADP/Permisos/PermisoEliminarObject/"+id;

        request = new JsonObjectRequest(Request.Method.DELETE, urlJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Boolean respuesta=response.getBoolean("Done");
                    if(respuesta){
                        Log.e("PERMISOS", "Eliminado");
                        TblPermisos permiso = new TblPermisos();
                        permiso.EliminarPorIdPermisoRegTblPermisos(Long.parseLong(params[0]));
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
    //BUSCAR PERMISOS
    public void BuscarAllPermisosXCuidadores(final String... params){
        String id=params[0];
        String urlJsonArray = "http://"+ip+"/ADP/Permisos/PermisoBuscarXCuidadores/"+id;
        req = new JsonArrayRequest(urlJsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        try {
                            for (int i = 0; i <response.length(); i++) {
                                JSONObject obj=response.getJSONObject(i);

                                TblPermisos permiso=new TblPermisos();
                                permiso.setIdPermiso(obj.getLong("IdPermiso"));
                                permiso.setIdCuidador(obj.getLong("IdCuidador"));
                                permiso.setIdPaciente(obj.getLong("IdPaciente"));
                                permiso.setNotifiAlarma(obj.getBoolean("NotifiAlarma"));
                                permiso.setRegCreador(obj.getBoolean("RegCreador"));
                                permiso.setContMedicina(obj.getBoolean("ContMedicina"));
                                permiso.setEliminado(obj.getBoolean("Eliminado"));

                                TblPermisos guardar_permiso = new TblPermisos(
                                        permiso.getIdPermiso(), permiso.getIdCuidador(),
                                        permiso.getIdPaciente(), permiso.getNotifiAlarma(),
                                        permiso.getContMedicina(), permiso.getRegCreador(),
                                        permiso.getEliminado());
                                guardar_permiso.save();

                                Log.e("Permiso ", "#" + i + " Descargado: "+response.getJSONObject(i).toString());
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
