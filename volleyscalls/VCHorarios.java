package com.Notifications.patientssassistant.volleyscalls;

import android.content.Context;
import android.util.Log;

import com.Notifications.patientssassistant.VarEstatic;
import com.Notifications.patientssassistant.tables.TblActividades;
import com.Notifications.patientssassistant.tables.TblHorarioMedicina;
import com.Notifications.patientssassistant.tables.TblHorarios;
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
public class VCHorarios extends VolleySingleton{

    JsonObjectRequest request;
    JsonArrayRequest req;
    android.content.Context Context;
    final String TAG = "VCHorarios";
    private static String ip= VarEstatic.ObtenerIP();

    public VCHorarios(Context context) {
        super(context);
        Context=context;
    }

    //INSERTAR UN HORARIO
    public void InsertarHorario(final String... params){

        String urlJson = "http://"+ip+"/ADP/Horarios/HorarioInsertarObject";

        HashMap<String, String> dato = new HashMap<String, String>();
        dato.put("IdHorario", params[0]);
        dato.put("IdCuidador", params[1]);
        dato.put("TipoHorario",params[2]);
        dato.put("HoraIni", params[3]);
        dato.put("MinutosIni", params[4]);
        dato.put("HoraFin", params[5]);
        dato.put("MinutosFin", params[6]);
        dato.put("Domingo", params[7]);
        dato.put("Lunes",params[8]);
        dato.put("Martes", params[9]);
        dato.put("Miercoles", params[10]);
        dato.put("Jueves", params[11]);
        dato.put("Viernes", params[12]);
        dato.put("Sabado", params[13]);
        dato.put("Eliminado",params[14]);

        request = new JsonObjectRequest(Request.Method.POST, urlJson, new JSONObject(dato), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Long respuesta=response.getLong("IdHorario");
                    if (respuesta>0) {
                        TblHorarios horario = new TblHorarios(
                                respuesta, Long.parseLong(params[1]),
                                params[2], Integer.parseInt(params[3]),
                                Integer.parseInt(params[4]),
                                Integer.parseInt(params[5]),
                                Integer.parseInt(params[6]),
                                Boolean.parseBoolean(params[7]),
                                Boolean.parseBoolean(params[8]),
                                Boolean.parseBoolean(params[9]),
                                Boolean.parseBoolean(params[10]),
                                Boolean.parseBoolean(params[11]),
                                Boolean.parseBoolean(params[12]),
                                Boolean.parseBoolean(params[13]),
                                Boolean.parseBoolean(params[14]));
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

    //EDITAR UN HORARIO
    public void ActualizarHorario(final String... params){

        String urlJson = "http://"+ip+"/ADP/Horarios/HorarioActualizarObject";

        HashMap<String, String> dato = new HashMap<String, String>();
        dato.put("IdHorario", params[0]);
        dato.put("IdCuidador", params[1]);
        dato.put("TipoHorario",params[2]);
        dato.put("HoraIni", params[3]);
        dato.put("MinutosIni", params[4]);
        dato.put("HoraFin", params[5]);
        dato.put("MinutosFin", params[6]);
        dato.put("Domingo", params[7]);
        dato.put("Lunes",params[8]);
        dato.put("Martes", params[9]);
        dato.put("Miercoles", params[10]);
        dato.put("Jueves", params[11]);
        dato.put("Viernes", params[12]);
        dato.put("Sabado", params[13]);
        dato.put("Eliminado",params[14]);

        request = new JsonObjectRequest(Request.Method.PUT, urlJson, new JSONObject(dato), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    boolean respuesta=response.getBoolean("Done");
                    if (respuesta) {
                        String campo_ide=String.valueOf(Long.parseLong(params[0]));
                        Select elHorario = Select.from(TblHorarios.class)
                                .where(Condition.prop("ID_HORARIO").eq(campo_ide));
                        TblHorarios edit_Horario=(TblHorarios)elHorario.first();

                        if (edit_Horario!=null) {
                            edit_Horario.setIdHorario(Long.parseLong(params[0]));
                            edit_Horario.setIdCuidador(Long.parseLong(params[1]));
                            edit_Horario.setTipoHorario(params[2]);
                            edit_Horario.setHoraIni(Integer.parseInt(params[3]));
                            edit_Horario.setMinutosIni(Integer.parseInt(params[4]));
                            edit_Horario.setHoraFin(Integer.parseInt(params[5]));
                            edit_Horario.setMinutosFin(Integer.parseInt(params[6]));
                            edit_Horario.setDomingo(Boolean.parseBoolean(params[7]));
                            edit_Horario.setLunes(Boolean.parseBoolean(params[8]));
                            edit_Horario.setMartes(Boolean.parseBoolean(params[9]));
                            edit_Horario.setMiercoles(Boolean.parseBoolean(params[10]));
                            edit_Horario.setJueves(Boolean.parseBoolean(params[11]));
                            edit_Horario.setViernes(Boolean.parseBoolean(params[12]));
                            edit_Horario.setSabado(Boolean.parseBoolean(params[13]));
                            edit_Horario.setEliminado(Boolean.parseBoolean(params[14]));
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

    //BUSCAR UN HORARIO
    public void BuscarUnHorario(final String... params){
        String id=params[0];
        String urlJsonObject = "http://"+ip+"/ADP/Horarios/HorarioBuscar/"+id;
        request = new JsonObjectRequest(urlJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try {
                            TblHorarios horario = new TblHorarios();
                            horario.setIdHorario(response.getLong("IdHorario"));
                            horario.setIdCuidador(response.getLong("IdCuidadora"));
                            horario.setTipoHorario(response.getString("TipoHorario"));
                            horario.setHoraIni(response.getInt("HoraIni"));
                            horario.setMinutosIni(response.getInt("MinutosIni"));
                            horario.setHoraFin(response.getInt("HoraFin"));
                            horario.setMinutosFin(response.getInt("MinutosFin"));
                            horario.setDomingo(response.getBoolean("Domingo"));
                            horario.setLunes(response.getBoolean("Lunes"));
                            horario.setMartes(response.getBoolean("Martes"));
                            horario.setMiercoles(response.getBoolean("Miercoles"));
                            horario.setJueves(response.getBoolean("Jueves"));
                            horario.setViernes(response.getBoolean("Viernes"));
                            horario.setSabado(response.getBoolean("Sabado"));
                            horario.setEliminado(response.getBoolean("Eliminado"));

                            if(horario!=null){

                                String campo_ide=String.valueOf(horario.getIdHorario());
                                Select elHorario = Select.from(TblHorarios.class)
                                        .where(Condition.prop("ID_HORARIO").eq(campo_ide));
                                TblHorarios edit_Horario=(TblHorarios)elHorario.first();

                                if (edit_Horario!=null) {
                                    edit_Horario.setIdHorario(horario.getIdHorario());
                                    edit_Horario.setIdCuidador(horario.getIdCuidador());
                                    edit_Horario.setTipoHorario(horario.getTipoHorario());
                                    edit_Horario.setHoraIni(horario.getHoraIni());
                                    edit_Horario.setMinutosIni(horario.getMinutosIni());
                                    edit_Horario.setHoraFin(horario.getHoraFin());
                                    edit_Horario.setMinutosFin(horario.getMinutosFin());
                                    edit_Horario.setDomingo(horario.getDomingo());
                                    edit_Horario.setLunes(horario.getLunes());
                                    edit_Horario.setMartes(horario.getMartes());
                                    edit_Horario.setMiercoles(horario.getMiercoles());
                                    edit_Horario.setJueves(horario.getJueves());
                                    edit_Horario.setViernes(horario.getViernes());
                                    edit_Horario.setSabado(horario.getSabado());
                                    edit_Horario.setEliminado(horario.getEliminado());
                                    edit_Horario.save();
                                }
                                else{
                                    TblHorarios newHorarios = new TblHorarios(
                                            horario.getIdHorario(), horario.getIdCuidador(),
                                            horario.getTipoHorario(), horario.getHoraIni(),
                                            horario.getMinutosIni(), horario.getHoraFin(),
                                            horario.getMinutosFin(), horario.getDomingo(),
                                            horario.getLunes(), horario.getMartes(),
                                            horario.getMiercoles(), horario.getJueves(),
                                            horario.getViernes(), horario.getSabado(),
                                            horario.getEliminado());
                                    newHorarios.save();
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

    //TAREA SINCRONA PARA BUSCAR TODOS LOS HORARIOS
    public void BuscarAllHorarios(final String... params){
        String id=params[0];
        final String TAG=params[1];
        String urlJsonArray = "http://"+ip+"/ADP/Horarios/HorariosBuscarXCuidador/"+id;
        req = new JsonArrayRequest(urlJsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        try {
                            for (int i = 0; i <response.length(); i++) {
                                JSONObject obj=response.getJSONObject(i);

                                TblHorarios horario = new TblHorarios();
                                horario.setIdHorario(obj.getLong("IdHorario"));
                                horario.setIdCuidador(obj.getLong("IdCuidador"));
                                horario.setTipoHorario(obj.getString("TipoHorario"));
                                horario.setHoraIni(obj.getInt("HoraIni"));
                                horario.setMinutosIni(obj.getInt("MinutosIni"));
                                horario.setHoraFin(obj.getInt("HoraFin"));
                                horario.setMinutosFin(obj.getInt("MinutosFin"));
                                horario.setDomingo(obj.getBoolean("Domingo"));
                                horario.setLunes(obj.getBoolean("Lunes"));
                                horario.setMartes(obj.getBoolean("Martes"));
                                horario.setMiercoles(obj.getBoolean("Miercoles"));
                                horario.setJueves(obj.getBoolean("Jueves"));
                                horario.setViernes(obj.getBoolean("Viernes"));
                                horario.setSabado(obj.getBoolean("Sabado"));
                                horario.setEliminado(obj.getBoolean("Eliminado"));

                                TblHorarios guardar_Horario = new TblHorarios(
                                        horario.getIdHorario(), horario.getIdCuidador(),
                                        horario.getTipoHorario(), horario.getHoraIni(),
                                        horario.getMinutosIni(), horario.getHoraFin(),
                                        horario.getMinutosFin(), horario.getDomingo(),
                                        horario.getLunes(), horario.getMartes(),
                                        horario.getMiercoles(), horario.getJueves(),
                                        horario.getViernes(), horario.getSabado(),
                                        horario.getEliminado());
                                guardar_Horario.save();

                                Log.e("Horario", "#" + i + " Descargado: "+response.getJSONObject(i).toString());
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

    //ACTUALIZAR EL ELIMINADO DE HORARIO
    public void EliminarHorario(final String... params){
        String id=params[0];
        String urlJson = "http://"+ip+"/ADP/Horarios/HorarioEliminarObject/"+id;

       request = new JsonObjectRequest(Request.Method.DELETE, urlJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Boolean respuesta=response.getBoolean("Done");
                    if(respuesta){
                        Log.e("Horario", "Eliminado");
                        TblHorarios horarios = new TblHorarios();
                        horarios.EliminarPorIdHorarioRegTblHorarios(Long.parseLong(params[0]));
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
    //TAREA ASINCRONA PARA BUSCAR TODOS LOS HORARIOS DE UN CUIDADOR
    public void BuscarAllHorariosXCuidadores(final String... params){
        String id=params[0];
        String urlJsonArray = "http://"+ip+"/ADP/Horarios/HorarioBuscarXCuidadores/"+id;
        req = new JsonArrayRequest(urlJsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        try {
                            for (int i = 0; i <response.length(); i++) {
                                JSONObject obj=response.getJSONObject(i);

                                TblHorarios horario = new TblHorarios();
                                horario.setIdHorario(obj.getLong("IdHorario"));
                                horario.setIdCuidador(obj.getLong("IdCuidador"));
                                horario.setTipoHorario(obj.getString("TipoHorario"));
                                horario.setHoraIni(obj.getInt("HoraIni"));
                                horario.setMinutosIni(obj.getInt("MinutosIni"));
                                horario.setHoraFin(obj.getInt("HoraFin"));
                                horario.setMinutosFin(obj.getInt("MinutosFin"));
                                horario.setDomingo(obj.getBoolean("Domingo"));
                                horario.setLunes(obj.getBoolean("Lunes"));
                                horario.setMartes(obj.getBoolean("Martes"));
                                horario.setMiercoles(obj.getBoolean("Miercoles"));
                                horario.setJueves(obj.getBoolean("Jueves"));
                                horario.setViernes(obj.getBoolean("Viernes"));
                                horario.setSabado(obj.getBoolean("Sabado"));
                                horario.setEliminado(obj.getBoolean("Eliminado"));

                                TblHorarios guardar_Horario = new TblHorarios(
                                        horario.getIdHorario(), horario.getIdCuidador(),
                                        horario.getTipoHorario(), horario.getHoraIni(),
                                        horario.getMinutosIni(), horario.getHoraFin(),
                                        horario.getMinutosFin(), horario.getDomingo(),
                                        horario.getLunes(), horario.getMartes(),
                                        horario.getMiercoles(), horario.getJueves(),
                                        horario.getViernes(), horario.getSabado(),
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
}
