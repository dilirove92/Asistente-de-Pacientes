package com.Notifications.patientssassistant.volleyscalls;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.Notifications.patientssassistant.VarEstatic;
import com.Notifications.patientssassistant.tables.TblActividades;
import com.Notifications.patientssassistant.tables.TblPacientes;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jazmine on 19/12/2015.
 */
public class VCPacientes extends VolleySingleton{

    JsonObjectRequest request;
    JsonArrayRequest req;
    android.content.Context Context;
    final String TAG = "VCPacientes";
    private static String ip= VarEstatic.ObtenerIP();
    public static SimpleDateFormat sfDate =  new  SimpleDateFormat ("yyy-MM-dd");

    public VCPacientes(android.content.Context context) {
        super(context);
        Context=context;
    }

    //INSERTAR UN NUEVO PACIENTE
    public void InsertarPacientes(final String... params){

        String urlJson = "http://"+ip+"/ADP/Pacientes/PacienteInsertarObject";

        HashMap<String, String> dato = new HashMap<String, String>();
        dato.put("IdPaciente", params[0]);
        dato.put("UsuarioP", params[1]);
        dato.put("CiP", params[2]);
        dato.put("NombreApellidoP", params[3]);
        dato.put("Anio", params[4]);
        dato.put("Mes", params[5]);
        dato.put("Dia", params[6]);
        dato.put("EstadoCivilP",params[7]);
        dato.put("NivelEstudioP", params[8]);
        dato.put("MotivoIngresoP", params[9]);
        dato.put("TipoPacienteP", params[10]);
        dato.put("Edad", params[11]);
        dato.put("FotoP", params[12]);
        dato.put("Eliminado",params[13]);

        request = new JsonObjectRequest(Request.Method.POST, urlJson, new JSONObject(dato), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Long respuesta=response.getLong("IdPaciente");
                    if (respuesta>0) {
                        TblPacientes paciente = new TblPacientes(
                                respuesta, params[1],
                                params[2], params[3],
                                Integer.parseInt(params[4]),
                                Integer.parseInt(params[5]),
                                Integer.parseInt(params[6]),
                                params[7], params[8],
                                params[9], params[10],
                                Integer.parseInt(params[11]),
                                params[12],
                                Boolean.parseBoolean(params[13]));
                        paciente.save();
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

    //ACTUALIZAR UN PACIENTE
    public void ActualizarPaciente(final String... params){

        String urlJson = "http://"+ip+"/ADP/Pacientes/PacienteActualizarObject";

        HashMap<String, String> dato = new HashMap<String, String>();
        dato.put("IdPaciente", params[0]);
        dato.put("UsuarioP", params[1]);
        dato.put("CiP", params[2]);
        dato.put("NombreApellidoP", params[3]);
        dato.put("Anio", params[4]);
        dato.put("Mes", params[5]);
        dato.put("Dia", params[6]);
        dato.put("EstadoCivilP",params[7]);
        dato.put("NivelEstudioP", params[8]);
        dato.put("MotivoIngresoP", params[9]);
        dato.put("TipoPacienteP", params[10]);
        dato.put("Edad", params[11]);
        dato.put("FotoP", params[12]);
        dato.put("Eliminado",params[13]);

        request = new JsonObjectRequest(Request.Method.PUT, urlJson, new JSONObject(dato), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    boolean respuesta=response.getBoolean("Done");
                    if (respuesta) {
                        String campo_ide=String.valueOf(Long.parseLong(params[0]));
                        Select elPaciente = Select.from(TblPacientes.class)
                                .where(Condition.prop("ID_PACIENTE").eq(campo_ide));
                        TblPacientes edit_Pac=(TblPacientes)elPaciente.first();

                        if (edit_Pac!=null) {
                            edit_Pac.setIdPaciente(Long.parseLong(params[0]));
                            edit_Pac.setUsuarioP(params[1]);
                            edit_Pac.setCiP(params[2]);
                            edit_Pac.setNombreApellidoP(params[3]);
                            edit_Pac.setAnio(Integer.parseInt(params[4]));
                            edit_Pac.setMes(Integer.parseInt(params[5]));
                            edit_Pac.setDia(Integer.parseInt(params[6]));
                            edit_Pac.setEstadoCivilP(params[7]);
                            edit_Pac.setNivelEstudioP(params[8]);
                            edit_Pac.setMotivoIngresoP(params[9]);
                            edit_Pac.setTipoPacienteP(params[10]);
                            edit_Pac.setEdad(Integer.parseInt(params[11]));
                            edit_Pac.setFotoP(params[12]);
                            edit_Pac.setEliminado(Boolean.parseBoolean(params[13]));
                            edit_Pac.save();
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

    //BUSCAR LOS DATOS DE UN PACIENTE
    public void BuscarUnPaciente(final String... params){
        String id=params[0];
        String urlJsonObject = "http://"+ip+"/ADP/Pacientes/PacienteBuscar/"+id;
        request = new JsonObjectRequest(urlJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try {
                            TblPacientes paciente = new TblPacientes();
                            paciente.setIdPaciente(response.getLong("IdPaciente"));
                            paciente.setUsuarioP(response.getString("UsuarioP"));
                            paciente.setCiP(response.getString("CiP"));
                            paciente.setNombreApellidoP(response.getString("NombreApellidoP"));
                            paciente.setAnio(response.getInt("Anio"));
                            paciente.setMes(response.getInt("Mes"));
                            paciente.setDia(response.getInt("Dia"));
                            paciente.setEstadoCivilP(response.getString("EstadoCivilP"));
                            paciente.setNivelEstudioP(response.getString("NivelEstudioP"));
                            paciente.setMotivoIngresoP(response.getString("MotivoIngresoP"));
                            paciente.setTipoPacienteP(response.getString("TipoPacienteP"));
                            paciente.setEdad(response.getInt("Edad"));
                            paciente.setFotoP(response.getString("FotoP"));
                            paciente.setEliminado(response.getBoolean("Eliminado"));

                            if(paciente!=null){

                                String campo_ide=String.valueOf(paciente.getIdPaciente());
                                Select elPaciente = Select.from(TblPacientes.class)
                                        .where(Condition.prop("ID_PACIENTE").eq(campo_ide));
                                TblPacientes edit_Pac=(TblPacientes)elPaciente.first();

                                if (edit_Pac!=null) {
                                    edit_Pac.setIdPaciente(paciente.getIdPaciente());
                                    edit_Pac.setUsuarioP(paciente.getUsuarioP());
                                    edit_Pac.setCiP(paciente.getCiP());
                                    edit_Pac.setNombreApellidoP(paciente.getNombreApellidoP());
                                    edit_Pac.setAnio(paciente.getAnio());
                                    edit_Pac.setMes(paciente.getMes());
                                    edit_Pac.setDia(paciente.getDia());
                                    edit_Pac.setEstadoCivilP(paciente.getEstadoCivilP());
                                    edit_Pac.setNivelEstudioP(paciente.getNivelEstudioP());
                                    edit_Pac.setMotivoIngresoP(paciente.getMotivoIngresoP());
                                    edit_Pac.setTipoPacienteP(paciente.getTipoPacienteP());
                                    edit_Pac.setEdad(paciente.getEdad());
                                    edit_Pac.setFotoP(paciente.getFotoP());
                                    edit_Pac.setEliminado(paciente.getEliminado());
                                    edit_Pac.save();
                                }
                                else{
                                    TblPacientes newPac = new TblPacientes(
                                            paciente.getIdPaciente(), paciente.getUsuarioP(),
                                            paciente.getCiP(), paciente.getNombreApellidoP(),
                                            paciente.getAnio(), paciente.getMes(), paciente.getDia(),
                                            paciente.getEstadoCivilP(), paciente.getNivelEstudioP(),
                                            paciente.getMotivoIngresoP(), paciente.getTipoPacienteP(),
                                            paciente.getEdad(), paciente.getFotoP(), paciente.getEliminado());
                                    newPac.save();
                                }
                                Log.e("DatoPaciente", ": " + response.toString());
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

    //BUSCAR FOTOS DE LOS DATOS DE UN PACIENTE
    public void BuscarUnaFotoPaciente(final String ... params){
        String id=params[0];
        String url = "http://"+ip+"/ADP/Pacientes/PacienteBuscarFoto/"+id;

        ImageRequest request1 = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                Log.d(TAG, "ImageRequest completa");
                //convertir de bitmap a string y guardar

                TblPacientes paciente = new TblPacientes();
                paciente.setFotoP("");

                String campo_ide=String.valueOf(paciente.getIdPaciente());
                Select elPaciente = Select.from(TblPacientes.class)
                        .where(Condition.prop("ID_PACIENTE").eq(campo_ide));
                TblPacientes edit_Pac=(TblPacientes)elPaciente.first();

                if (edit_Pac!=null) {
                    edit_Pac.setFotoP(paciente.getFotoP());
                    edit_Pac.save();
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

    //BUSCAR TODOS LOS DATOS DE LOS PACIENTES DE UN DETERMINADO CUIDADOR
    public void BuscarAllPacientes(final String... params){
        String id=params[0];
        String urlJsonArray = "http://"+ip+"/ADP/Pacientes/PacienteBuscarXCuidadores/"+id;
        req = new JsonArrayRequest(urlJsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        try {
                            for (int i = 0; i <response.length(); i++) {
                                JSONObject obj=response.getJSONObject(i);

                                TblPacientes paciente = new TblPacientes();
                                paciente.setIdPaciente(obj.getLong("IdPaciente"));
                                paciente.setUsuarioP(obj.getString("UsuarioP"));
                                paciente.setCiP(obj.getString("CiP"));
                                paciente.setNombreApellidoP(obj.getString("NombreApellidoP"));
                                paciente.setAnio(obj.getInt("Anio"));
                                paciente.setMes(obj.getInt("Mes"));
                                paciente.setDia(obj.getInt("Dia"));
                                paciente.setEstadoCivilP(obj.getString("EstadoCivilP"));
                                paciente.setNivelEstudioP(obj.getString("NivelEstudioP"));
                                paciente.setMotivoIngresoP(obj.getString("MotivoIngresoP"));
                                paciente.setTipoPacienteP(obj.getString("TipoPacienteP"));
                                paciente.setEdad(obj.getInt("Edad"));
                                paciente.setFotoP(obj.getString("FotoP"));
                                paciente.setEliminado(obj.getBoolean("Eliminado"));

                                if (paciente!=null) {
                                    TblPacientes guardar_Pac = new TblPacientes(
                                            paciente.getIdPaciente(), paciente.getUsuarioP(),
                                            paciente.getCiP(), paciente.getNombreApellidoP(),
                                            paciente.getAnio(), paciente.getMes(), paciente.getDia(),
                                            paciente.getEstadoCivilP(), paciente.getNivelEstudioP(),
                                            paciente.getMotivoIngresoP(), paciente.getTipoPacienteP(),
                                            paciente.getEdad(), paciente.getFotoP(), paciente.getEliminado());
                                    guardar_Pac.save();
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

    //ELIMINAR UN REGISTRO DE PACIENTE
    public void EliminarPaciente(final String... params){
        String id=params[0];
        String urlJson = "http://"+ip+"/ADP/Pacientes/PacienteEliminarObject/"+id;
        final String TAG=params[1];

        request = new JsonObjectRequest(Request.Method.DELETE, urlJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Boolean respuesta=response.getBoolean("Done");
                    if(respuesta){
                        Log.e("Pacientes", "Eliminado");
                        TblPacientes pacientes = new TblPacientes();
                        pacientes.EliminarPorIdPacienteRegTblPacientes(Long.parseLong(params[0]));
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

    //VER SI EL USURIO YA EXISTE
    public void ExisteUserPaciente(final String... params){

        String urlJson = "http://"+ip+"/ADP/Pacientes/PacienteExisteUserObject";

        HashMap<String, String> dato = new HashMap<String, String>();
        dato.put("UsuarioP", params[0]);

        final String TAG=params[1];

        request = new JsonObjectRequest(Request.Method.POST, urlJson, new JSONObject(dato), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    boolean respuesta=response.getBoolean("Done");
                    if (respuesta) {
                        Log.e("Pacientes", "Existe Usuario");
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

    public void ListaDePacientes(String idC) {

        String urlJsonArray = "http://"+ip+"/ADP/Pacientes/PacienteIdsBuscarXCuidadores/" + idC;
        req = new JsonArrayRequest(urlJsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {
                            Long idPaciente;
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = (JSONObject) response.get(i);
                                idPaciente = obj.getLong("id");
                                BuscarUnPaciente(String.valueOf(idPaciente));
                                Log.e("Paciente", "#" + i + " Descargado ");
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
