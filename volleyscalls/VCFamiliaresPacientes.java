package com.Notifications.patientssassistant.volleyscalls;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.Notifications.patientssassistant.NewEditControlDietaActivity;
import com.Notifications.patientssassistant.VarEstatic;
import com.Notifications.patientssassistant.tables.TblActividades;
import com.Notifications.patientssassistant.tables.TblFamiliaresPacientes;
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
 * Created by Jazmine on 18/12/2015.
 */
public class VCFamiliaresPacientes extends VolleySingleton{

    JsonObjectRequest request;
    JsonArrayRequest req;
    android.content.Context Context;
    final String TAG = "VCFamiliaresPacientes";
    private static String ip= VarEstatic.ObtenerIP();

    public VCFamiliaresPacientes(android.content.Context context) {
        super(context);
        Context=context;
    }

    //INSERTAR UN REGISTRO DE FAMILIAR DE UN PACIENTE
    public void InsertarFamiliaresPacientes(final String... params){

        String urlJson = "http://"+ip+"/ADP/FamiliaresPacientes/FamiliarPacienteInsertarObject";

        HashMap<String, String> dato = new HashMap<String, String>();
        dato.put("IdFamiliarPaciente", params[0]);
        dato.put("IdPaciente", params[1]);
        dato.put("NombreContacto", params[2]);
        dato.put("CiContacto", params[3]);
        dato.put("Parentezco", params[4]);
        dato.put("Celular",params[5]);
        dato.put("Telefono", params[6]);
        dato.put("Direccion", params[7]);
        dato.put("Observacion", params[8]);
        dato.put("EnviarReportes", params[9]);
        dato.put("FotoContacto", params[10]);
        dato.put("Email", params[11]);
        dato.put("Eliminado",params[12]);

        request = new JsonObjectRequest(Request.Method.POST, urlJson, new JSONObject(dato), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Long respuesta=response.getLong("IdFamiliarPaciente");
                    if (respuesta>0) {
                        TblFamiliaresPacientes famPac = new TblFamiliaresPacientes(
                                respuesta, Long.parseLong(params[1]),
                                params[2], params[3], params[4],
                                params[5], params[6], params[7],
                                params[8], Boolean.parseBoolean(params[9]),
                                params[10], params[11],
                                Boolean.parseBoolean(params[12]));
                        famPac.save();
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

    //ACTUALIZAR UN REGISTRO DE FAMILIAR DE UN PACIENTE
    public void ActualizarFamiliaresPacientes(final String... params){

        String urlJson = "http://"+ip+"/ADP/FamiliaresPacientes/FamiliarPacienteActualizarObject";

        HashMap<String, String> dato = new HashMap<String, String>();
        dato.put("IdFamiliarPaciente", params[0]);
        dato.put("IdPaciente", params[1]);
        dato.put("NombreContacto", params[2]);
        dato.put("CiContacto", params[3]);
        dato.put("Parentezco", params[4]);
        dato.put("Celular",params[5]);
        dato.put("Telefono", params[6]);
        dato.put("Direccion", params[7]);
        dato.put("Observacion", params[8]);
        dato.put("EnviarReportes", params[9]);
        dato.put("FotoContacto", params[10]);
        dato.put("Email", params[11]);
        dato.put("Eliminado",params[12]);

        request = new JsonObjectRequest(Request.Method.PUT, urlJson, new JSONObject(dato), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    boolean respuesta=response.getBoolean("Done");
                    if (respuesta) {
                        String campo_ide=String.valueOf(Long.parseLong(params[0]));
                        Select elFamPac = Select.from(TblFamiliaresPacientes.class)
                                .where(Condition.prop("ID_FAMILIAR_PACIENTE").eq(campo_ide));
                        TblFamiliaresPacientes edit_FP=(TblFamiliaresPacientes)elFamPac.first();

                        if (edit_FP!=null) {
                            edit_FP.setIdFamiliarPaciente(Long.parseLong(params[0]));
                            edit_FP.setIdPaciente(Long.parseLong(params[1]));
                            edit_FP.setNombreContacto(params[2]);
                            edit_FP.setCiContacto(params[3]);
                            edit_FP.setParentezco(params[4]);
                            edit_FP.setCelular(params[5]);
                            edit_FP.setTelefono(params[6]);
                            edit_FP.setDireccion(params[7]);
                            edit_FP.setObservacion(params[8]);
                            edit_FP.setEnviarReportes(Boolean.parseBoolean(params[9]));
                            edit_FP.setFotoContacto(params[10]);
                            edit_FP.setEmail(params[11]);
                            edit_FP.setEliminado(Boolean.parseBoolean(params[12]));
                            edit_FP.save();
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

    //BUSCAR UNA FAMILIAR PACIENTE
    public void BuscarUnaFamiliarPaciente(final String... params){
        String id=params[0];
        String urlJsonObject = "http://"+ip+"/ADP/FamiliaresPacientes/FamiliarPacienteBuscar/"+id;
        request = new JsonObjectRequest(urlJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try {
                            TblFamiliaresPacientes famPac = new TblFamiliaresPacientes();
                            famPac.setIdFamiliarPaciente(response.getLong("IdFamiliarPaciente"));
                            famPac.setIdPaciente(response.getLong("IdPaciente"));
                            famPac.setNombreContacto(response.getString("NombreContacto"));
                            famPac.setCiContacto(response.getString("CiContacto"));
                            famPac.setParentezco(response.getString("Parentezco"));
                            famPac.setCelular(response.getString("Celular"));
                            famPac.setTelefono(response.getString("Telefono"));
                            famPac.setDireccion(response.getString("Direccion"));
                            famPac.setObservacion(response.getString("Observacion"));
                            famPac.setEnviarReportes(response.getBoolean("EnviarReportes"));
                            famPac.setFotoContacto(response.getString("FotoContacto"));
                            famPac.setEmail(response.getString("Email"));
                            famPac.setEliminado(response.getBoolean("Eliminado"));

                            if(famPac!=null){

                                String campo_ide=String.valueOf(famPac.getIdFamiliarPaciente());
                                Select elFamPac = Select.from(TblFamiliaresPacientes.class)
                                        .where(Condition.prop("ID_FAMILIAR_PACIENTE").eq(campo_ide));
                                TblFamiliaresPacientes edit_FP=(TblFamiliaresPacientes)elFamPac.first();

                                if (edit_FP!=null) {
                                    edit_FP.setIdFamiliarPaciente(famPac.getIdFamiliarPaciente());
                                    edit_FP.setIdPaciente(famPac.getIdPaciente());
                                    edit_FP.setNombreContacto(famPac.getNombreContacto());
                                    edit_FP.setCiContacto(famPac.getCiContacto());
                                    edit_FP.setParentezco(famPac.getParentezco());
                                    edit_FP.setCelular(famPac.getCelular());
                                    edit_FP.setTelefono(famPac.getTelefono());
                                    edit_FP.setDireccion(famPac.getDireccion());
                                    edit_FP.setObservacion(famPac.getObservacion());
                                    edit_FP.setEnviarReportes(famPac.getEnviarReportes());
                                    edit_FP.setFotoContacto(famPac.getFotoContacto());
                                    edit_FP.setEliminado(famPac.getEliminado());
                                    edit_FP.save();
                                }
                                else{
                                    TblFamiliaresPacientes newFamiliar = new TblFamiliaresPacientes(
                                            famPac.getIdFamiliarPaciente(),famPac.getIdPaciente(),
                                            famPac.getNombreContacto(), famPac.getCiContacto(),
                                            famPac.getParentezco(), famPac.getCelular(),
                                            famPac.getTelefono(), famPac.getDireccion(),
                                            famPac.getObservacion(), famPac.getEnviarReportes(),
                                            famPac.getFotoContacto(), famPac.getEmail(),
                                            famPac.getEliminado());
                                    newFamiliar.save();
                                }
                                Log.e("DatoFamiliarPaciente", ": " + response.toString());
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

    //BUSCAR UNA FOTO FAMILIARPACIENTE
    public void BuscarUnaFotoFamiliarPaciente(final String ... params){
        String id=params[0];
        String url = "http://"+ip+"/ADP/FamiliaresPacientes/FamiliarPacienteBuscarOneFoto/"+id;

        ImageRequest request1 = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                Log.d(TAG, "ImageRequest completa");
                //convertir de bitmap a string y guardar

                TblFamiliaresPacientes famPac = new TblFamiliaresPacientes();
                famPac.setFotoContacto("");

                String campo_ide=String.valueOf(famPac.getIdFamiliarPaciente());
                Select elFamPac = Select.from(TblFamiliaresPacientes.class)
                        .where(Condition.prop("ID_FAMILIAR_PACIENTE").eq(campo_ide));
                TblFamiliaresPacientes edit_FP=(TblFamiliaresPacientes)elFamPac.first();

                if (edit_FP!=null) {
                    edit_FP.setFotoContacto(famPac.getFotoContacto());
                    edit_FP.save();
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

    //TAREA ASINCRONA PARA BUSCAR TODOS LOS REGISTROS DE FAMILIARES DE UN PACIENTE
    public void BuscarAllFamiliaresPacientes(final String... params){
        String id=params[0];
        String urlJsonArray = "http://"+ip+"/ADP/FamiliaresPacientes/FamiliaresPacientesBuscarXPaciente/"+id;
        req = new JsonArrayRequest(urlJsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        try {
                            for (int i = 0; i <response.length(); i++) {
                                JSONObject obj=response.getJSONObject(i);

                                TblFamiliaresPacientes famPac = new TblFamiliaresPacientes();
                                famPac.setIdFamiliarPaciente(obj.getLong("IdFamiliarPaciente"));
                                famPac.setIdPaciente(obj.getLong("IdPaciente"));
                                famPac.setNombreContacto(obj.getString("NombreContacto"));
                                famPac.setCiContacto(obj.getString("CiContacto"));
                                famPac.setParentezco(obj.getString("Parentezco"));
                                famPac.setCelular(obj.getString("Celular"));
                                famPac.setTelefono(obj.getString("Telefono"));
                                famPac.setDireccion(obj.getString("Direccion"));
                                famPac.setObservacion(obj.getString("Observacion"));
                                famPac.setEnviarReportes(obj.getBoolean("EnviarReportes"));
                                famPac.setFotoContacto(obj.getString("FotoContacto"));
                                famPac.setEmail(obj.getString("Email"));
                                famPac.setEliminado(obj.getBoolean("Eliminado"));

                                Select elFamPac = Select.from(TblFamiliaresPacientes.class)
                                        .where(Condition.prop("ID_FAMILIAR_PACIENTE").eq(famPac.getIdFamiliarPaciente().toString()));
                                TblFamiliaresPacientes edit_FP=(TblFamiliaresPacientes)elFamPac.first();

                                if (edit_FP!=null) {
                                    edit_FP.setIdFamiliarPaciente(famPac.getIdFamiliarPaciente());
                                    edit_FP.setIdPaciente(famPac.getIdPaciente());
                                    edit_FP.setNombreContacto(famPac.getNombreContacto());
                                    edit_FP.setCiContacto(famPac.getCiContacto());
                                    edit_FP.setParentezco(famPac.getParentezco());
                                    edit_FP.setCelular(famPac.getCelular());
                                    edit_FP.setTelefono(famPac.getTelefono());
                                    edit_FP.setDireccion(famPac.getDireccion());
                                    edit_FP.setObservacion(famPac.getObservacion());
                                    edit_FP.setEnviarReportes(famPac.getEnviarReportes());
                                    edit_FP.setFotoContacto(famPac.getFotoContacto());
                                    edit_FP.setEmail(famPac.getEmail());
                                    edit_FP.setEliminado(famPac.getEliminado());
                                    edit_FP.save();
                                }
                                else{
                                    TblFamiliaresPacientes guardar_FP = new TblFamiliaresPacientes(
                                            famPac.getIdFamiliarPaciente(), famPac.getIdPaciente(),
                                            famPac.getNombreContacto(), famPac.getCiContacto(),
                                            famPac.getParentezco(), famPac.getCelular(),
                                            famPac.getTelefono(), famPac.getDireccion(),
                                            famPac.getObservacion(), famPac.getEnviarReportes(),
                                            famPac.getFotoContacto(), famPac.getEmail(),
                                            famPac.getEliminado());
                                    guardar_FP.save();
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

    //ELIMINAR EL REGISTRO DE UN FAMILIAR DE UN PACIENTE
    public void EliminarFamiliaPaciente(final String... params){
        String id=params[0];
        String urlJson = "http://"+ip+"/ADP/FamiliaresPacientes/FamiliarPacienteEliminarObject/"+id;

        request = new JsonObjectRequest(Request.Method.DELETE, urlJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Boolean respuesta=response.getBoolean("Done");
                    if(respuesta){
                        Log.e("Familiares Pacientes", "Eliminado");
                        TblFamiliaresPacientes famPac = new TblFamiliaresPacientes();
                        famPac.EliminarPorIdFamiliarPacienteRegTblFamiliaresPacientes(Long.parseLong(params[0]));
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

    //UTILIAZADA PARA ACTUALIZAR LA BD
    //TAREA ASINCRONA PARA BUSCAR TODOS LOS REGISTROS DE FAMILIARES DE UN PACIENTE
    public void BuscarAllFamiliaresPacientesCuidadores(final String... params){
        String id=params[0];
        String urlJsonArray = "http://"+ip+"/ADP/FamiliaresPacientes/FamiliarPacienteBuscarXCuidadores/"+id;
        req = new JsonArrayRequest(urlJsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {
                            for (int i = 0; i < response.length(); i++) {
                                String jsonResponse1 = "";
                                JSONObject obj = (JSONObject) response.get(i);
                                TblFamiliaresPacientes famPac = new TblFamiliaresPacientes();
                                famPac.setIdFamiliarPaciente(obj.getLong("IdFamiliarPaciente"));
                                famPac.setIdPaciente(obj.getLong("IdPaciente"));
                                famPac.setNombreContacto(obj.getString("NombreContacto"));
                                famPac.setCiContacto(obj.getString("CiContacto"));
                                famPac.setParentezco(obj.getString("Parentezco"));
                                famPac.setCelular(obj.getString("Celular"));
                                famPac.setTelefono(obj.getString("Telefono"));
                                famPac.setDireccion(obj.getString("Direccion"));
                                famPac.setObservacion(obj.getString("Observacion"));
                                famPac.setEnviarReportes(obj.getBoolean("EnviarReportes"));
                                famPac.setFotoContacto(obj.getString("FotoContacto"));
                                famPac.setEmail(obj.getString("Email"));
                                famPac.setEliminado(obj.getBoolean("Eliminado"));
                                famPac.save();
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

    public void ListaDeFamiliares(String idC) {

        String urlJsonArray = "http://"+ip+"/ADP/FamiliaresPacientes/FamiliarPacienteIdsBuscarXCuidadores/" + idC;
        req = new JsonArrayRequest(urlJsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {
                            Long idFamPac;
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = (JSONObject) response.get(i);
                                idFamPac = obj.getLong("id");
                                BuscarUnaFamiliarPaciente(String.valueOf(idFamPac));
                                Log.e("FamiliarPaciente", "#" + i + " Descargado ");
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
