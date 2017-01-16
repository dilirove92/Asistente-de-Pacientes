package com.Notifications.patientssassistant.volleyscalls;


import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Jazmine on 13/12/2015.
 */
public class VolleyS {
    private static VolleyS mVolleyS = null;
    //Este objeto es la cola que usará la aplicación
    private RequestQueue mRequestQueue;

    private VolleyS(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public static VolleyS getInstance(Context context) {
        if (mVolleyS == null) {
            mVolleyS = new VolleyS(context);
        }
        return mVolleyS;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

}