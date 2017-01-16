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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jazmine on 19/12/2015.
 */
public class VCRecuperacionContrasena extends VolleySingleton {

    JsonObjectRequest request;
    android.content.Context Context;
    final String TAG = "VCRecuperacionContrasena";
    private static String ip= VarEstatic.ObtenerIP();

    public VCRecuperacionContrasena(Context context) {
        super(context);
        Context=context;
    }

    //INSERTAR UNA NUEVA RUTINA DE PACIENTES
    public void InsertarRutinasPacientes(final String... params){

        String urlJson = "http://"+ip+"ADP/RecuperacionContrasena/RecuperacionContrasenaObject";

        HashMap<String, String> dato = new HashMap<String, String>();
        dato.put("email", params[0]);

        final String TAG=params[1];

        request = new JsonObjectRequest(Request.Method.POST, urlJson, new JSONObject(dato), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    boolean respuesta=response.getBoolean("Done");
                    if (respuesta) {
                        //mensaje
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
}
