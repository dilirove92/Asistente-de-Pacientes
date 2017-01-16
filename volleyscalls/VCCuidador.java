package com.Notifications.patientssassistant.volleyscalls;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.Notifications.patientssassistant.VarEstatic;
import com.Notifications.patientssassistant.tables.TblActividades;
import com.Notifications.patientssassistant.tables.TblCuidador;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageRequest;
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
public class VCCuidador extends VolleySingleton{

    JsonObjectRequest request;
    JsonArrayRequest req;
    android.content.Context Context;
    final String TAG = "VCCuidador";
    private static String ip= VarEstatic.ObtenerIP();

    public VCCuidador(android.content.Context context) {
        super(context);
        Context = context;
    }

    //TAREA ASINCRONA PARA INSERTAR UN CUIDADOR
    public void InsertarCuidador(final String... params){

        String urlJson = "http://"+ip+"/ADP/Cuidador/CuidadorInsertarObject";

        HashMap<String, String> dato = new HashMap<String, String>();
        dato.put("IdCuidador", params[0]);
        dato.put("UsuarioC", params[1]);
        dato.put("NombreC", params[2]);
        dato.put("CiRucC", params[3]);
        dato.put("CelularC", params[4]);
        dato.put("TelefonoC",params[5]);
        dato.put("EmailC", params[6]);
        dato.put("DireccionC", params[7]);
        dato.put("CargoC", params[8]);
        dato.put("ControlTotal", params[9]);
        dato.put("FotoC", params[10]);
        dato.put("Eliminado",params[11]);

        request = new JsonObjectRequest(Request.Method.POST, urlJson, new JSONObject(dato), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Long respuesta=response.getLong("IdCuidador");
                    if (respuesta>0) {
                        TblCuidador cuidador=new TblCuidador(
                                respuesta, params[1], params[2], params[3],
                                params[4], params[5], params[6], params[7],
                                params[8], Boolean.parseBoolean(params[9]),
                                params[10], Boolean.parseBoolean(params[11]));
                        cuidador.save();
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

    //TAREA ASINCRONA PARA ACTUALIZAR EL REGISTRO DE UN CUIDADOR
    public void ActualizarCuidador(final String... params){

        String urlJson = "http://"+ip+"/ADP/Cuidador/CuidadorActualizarObject";

        HashMap<String, String> dato = new HashMap<String, String>();
        dato.put("IdCuidador", params[0]);
        dato.put("UsuarioC", params[1]);
        dato.put("NombreC", params[2]);
        dato.put("CiRucC", params[3]);
        dato.put("CelularC", params[4]);
        dato.put("TelefonoC",params[5]);
        dato.put("EmailC", params[6]);
        dato.put("DireccionC", params[7]);
        dato.put("CargoC", params[8]);
        dato.put("ControlTotal", params[9]);
        dato.put("FotoC", params[10]);
        dato.put("Eliminado",params[11]);

        request = new JsonObjectRequest(Request.Method.PUT, urlJson, new JSONObject(dato), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Boolean respuesta=response.getBoolean("Done");
                    if (respuesta) {
                        String campo_ide=String.valueOf(Long.parseLong(params[0]));
                        Select unCuidador = Select.from(TblCuidador.class)
                                .where(Condition.prop("ID_CUIDADOR").eq(campo_ide));
                        TblCuidador edit_Cuidador=(TblCuidador)unCuidador.first();

                        if (edit_Cuidador!=null) {
                            edit_Cuidador.setIdCuidador(Long.parseLong(params[0]));
                            edit_Cuidador.setUsuarioC(params[1]);
                            edit_Cuidador.setNombreC(params[2]);
                            edit_Cuidador.setCiRucC(params[3]);
                            edit_Cuidador.setCelularC(params[4]);
                            edit_Cuidador.setTelefonoC(params[5]);
                            edit_Cuidador.setEmailC(params[6]);
                            edit_Cuidador.setDireccionC(params[7]);
                            edit_Cuidador.setCargoC(params[8]);
                            edit_Cuidador.setControlTotal(Boolean.parseBoolean(params[9]));
                            edit_Cuidador.setFotoC(params[10]);
                            edit_Cuidador.setEliminado(Boolean.parseBoolean(params[11]));
                            edit_Cuidador.save();
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

    //TAREA ASINCRONA PARA BUSCAR UN REGISTRO DE CUIDADOR
    public void BuscarUnCuidador(final String... params){
        String id=params[0];
        String urlJsonObject = "http://"+ip+"/ADP/Cuidador/CuidadorBuscar/"+id;
        request = new JsonObjectRequest(urlJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try {
                            TblCuidador cuidador = new TblCuidador();
                            cuidador.setIdCuidador(response.getLong("IdCuidador"));
                            cuidador.setUsuarioC(response.getString("UsuarioC"));
                            cuidador.setNombreC(response.getString("NombreC"));
                            cuidador.setCiRucC(response.getString("CiRucC"));
                            cuidador.setCelularC(response.getString("CelularC"));
                            cuidador.setTelefonoC(response.getString("TelefonoC"));
                            cuidador.setEmailC(response.getString("EmailC"));
                            cuidador.setDireccionC(response.getString("DireccionC"));
                            cuidador.setCargoC(response.getString("CargoC"));
                            cuidador.setControlTotal(response.getBoolean("ControlTotal"));
                            cuidador.setFotoC(response.getString("FotoC"));
                            cuidador.setEliminado(response.getBoolean("Eliminado"));

                            if(response!=null){

                                String campo_ide=String.valueOf(cuidador.getIdCuidador());
                                Select unCuidador = Select.from(TblCuidador.class)
                                        .where(Condition.prop("ID_CUIDADOR").eq(campo_ide));
                                TblCuidador edit_Cuidador=(TblCuidador)unCuidador.first();

                                if (edit_Cuidador!=null) {
                                    edit_Cuidador.setIdCuidador(cuidador.getIdCuidador());
                                    edit_Cuidador.setUsuarioC(cuidador.getUsuarioC());
                                    edit_Cuidador.setNombreC(cuidador.getNombreC());
                                    edit_Cuidador.setCiRucC(cuidador.getCiRucC());
                                    edit_Cuidador.setCelularC(cuidador.getCelularC());
                                    edit_Cuidador.setTelefonoC(cuidador.getTelefonoC());
                                    edit_Cuidador.setEmailC(cuidador.getEmailC());
                                    edit_Cuidador.setDireccionC(cuidador.getDireccionC());
                                    edit_Cuidador.setCargoC(cuidador.getCargoC());
                                    edit_Cuidador.setControlTotal(cuidador.getControlTotal());
                                    edit_Cuidador.setFotoC(cuidador.getFotoC());
                                    edit_Cuidador.setEliminado(cuidador.getEliminado());
                                    edit_Cuidador.save();

                                }
                                else{
                                    TblCuidador newCuidador= new TblCuidador(
                                            cuidador.getIdCuidador(),cuidador.getUsuarioC(),
                                            cuidador.getNombreC(), cuidador.getCiRucC(),
                                            cuidador.getCelularC(), cuidador.getTelefonoC(),
                                            cuidador.getEmailC(), cuidador.getDireccionC(),
                                            cuidador.getCargoC(), cuidador.getControlTotal(),
                                            cuidador.getFotoC(), cuidador.getEliminado());
                                    newCuidador.save();

                                }
                                Log.e("DatoCuidador", ": " + response.toString());
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

    //TAREA ASINCRONA PARA BUSCAR UNA FOTO DE CUIDADOR
    public void BuscarUnaFotoCuidador(final String ... params){
        String id=params[0];
        final String TAG=params[1];
        String url = "http://"+ip+"/ADP/Cuidador/CuidadorBuscarFoto/"+id;

        ImageRequest request1 = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                Log.d(TAG, "ImageRequest completa");
                //convertir de bitmap a string y guardar

                TblCuidador cuidador = new TblCuidador();
                cuidador.setFotoC("");

                String campo_ide=String.valueOf(cuidador.getIdCuidador());
                Select elCuidador = Select.from(TblCuidador.class)
                        .where(Condition.prop("ID_CUIDADOR").eq(campo_ide));
                TblCuidador edit_Cuidador=(TblCuidador)elCuidador.first();

                if (edit_Cuidador!=null) {
                    edit_Cuidador.setFotoC(cuidador.getFotoC());
                    edit_Cuidador.save();
                }
            }
        } , 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error en ImageRequest");
                    }
                });
        VolleySingleton.getInstance(Context).addToRequestQueue(request1);
    }

    //TAREA ASINCRONA PARA BUSCAR A TODOS LOS CUIDADORES
    public void BuscarAllCuidadores(final String... params){
        String id=params[0];
        final String TAG=params[1];
        String urlJsonArray = "http://"+ip+"/ADP/Cuidador/CuidadorBuscarAllXCuidadores/"+id;
        req = new JsonArrayRequest(urlJsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        try {
                            for (int i = 0; i <response.length(); i++) {
                                JSONObject obj=response.getJSONObject(i);

                                TblCuidador cuidador = new TblCuidador();
                                cuidador.setIdCuidador(obj.getLong("IdCuidador"));
                                cuidador.setUsuarioC(obj.getString("UsuarioC"));
                                cuidador.setNombreC(obj.getString("NombreC"));
                                cuidador.setCiRucC(obj.getString("CiRucC"));
                                cuidador.setCelularC(obj.getString("CelularC"));
                                cuidador.setTelefonoC(obj.getString("TelefonoC"));
                                cuidador.setEmailC(obj.getString("EmailC"));
                                cuidador.setDireccionC(obj.getString("DireccionC"));
                                cuidador.setCargoC(obj.getString("CargoC"));
                                cuidador.setControlTotal(obj.getBoolean("ControlTotal"));
                                cuidador.setFotoC(obj.getString("FotoC"));
                                cuidador.setEliminado(obj.getBoolean("Eliminado"));

                                if (cuidador!=null) {
                                    TblCuidador newCuidador= new TblCuidador(
                                            cuidador.getIdCuidador(),cuidador.getUsuarioC(),
                                            cuidador.getNombreC(), cuidador.getCiRucC(),
                                            cuidador.getCelularC(), cuidador.getTelefonoC(),
                                            cuidador.getEmailC(), cuidador.getDireccionC(),
                                            cuidador.getCargoC(), cuidador.getControlTotal(),
                                            cuidador.getFotoC(), cuidador.getEliminado());
                                    newCuidador.save();
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
        VolleySingleton.getInstance(Context).addToRequestQueue(req);
    }

    // ELIMINAR UN CUIDADOR
    public void EliminarCuidador(final String... params){
        String id=params[0];
        String urlJson = "http://"+ip+"/ADP/Cuidador/CuidadorEliminarObject/"+id;

        request = new JsonObjectRequest(Request.Method.DELETE, urlJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Boolean respuesta=response.getBoolean("Done");
                    if(respuesta){
                        Log.e("Cuidador", "Eliminado");
                        TblCuidador cuidadores = new TblCuidador();
                        cuidadores.EliminarPorIdCuidadorRegTblCuidador(Long.parseLong(params[0]));
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

    //TAREA ASINCRONA PARA VERIFICAR SI YA EXISTE UN EMAIL
    public void ExisteEmail(final String... params){

        String urlJson = "http://"+ip+"/ADP/Cuidador/CuidadorExisteEmailObject";

        HashMap<String, String> dato = new HashMap<String, String>();
        dato.put("EmailC", params[0]);

        request = new JsonObjectRequest(Request.Method.PUT, urlJson, new JSONObject(dato), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Boolean respuesta=response.getBoolean("Done");
                    if (respuesta) {
                        if(respuesta){Log.e("Cuidador", "ExisteEmail");}
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

    //TAREA ASINCRONA PARA VERIFICAR SI YA EXISTE UN USUARIO
    public void ExisteUsuario(final String... params){

        String urlJson = "http://"+ip+"/ADP/Cuidador/CuidadorExisteUsuarioObject";

        HashMap<String, String> dato = new HashMap<String, String>();
        dato.put("UsuarioC", params[0]);

        request = new JsonObjectRequest(Request.Method.PUT, urlJson, new JSONObject(dato), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Boolean respuesta=response.getBoolean("Done");
                    if (respuesta) {
                        if(respuesta){Log.e("Cuidador", "ExisteUsuario");}
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

    public void ListaDeCuidadores(String idC) {

        String urlJsonArray = "http://"+ip+"/ADP/Cuidador/CuidadorBuscarAllIdsXCuidadores/" + idC;
        req = new JsonArrayRequest(urlJsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {
                            Long idCuidador;
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = (JSONObject) response.get(i);
                                idCuidador = obj.getLong("id");
                                BuscarUnCuidador(String.valueOf(idCuidador));
                                Log.e("Cuidador", "#" + i + " Descargado ");
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
