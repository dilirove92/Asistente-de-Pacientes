package com.Notifications.patientssassistant;


import com.Notifications.patientssassistant.adapters.*;
import com.Notifications.patientssassistant.asynctask.*;
import com.Notifications.patientssassistant.dialogos.*;
import com.Notifications.patientssassistant.fragments.*;
import com.Notifications.patientssassistant.tables.TblActividadPaciente;
import com.Notifications.patientssassistant.volleyscalls.VCActividades;
import com.Notifications.patientssassistant.volleyscalls.VolleySingleton;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


public class MenuPrincipalPacienteActivity extends Activity {

    //VARIABLES PARA RECOGER DATOS DEL PACIENTE
    private static Long vIdPaciente;
    private static Long vDependeDe;
    private String vTipoUsuario;
    private String vUsuarioP;
    private String vFotoP;
    private Long vIdIS;

    //VARIABLES AUXILIARES
    private String[] titulos;
    private ArrayList<ListaItemDrawer> NavItms;
    private TypedArray NavIcons;
    private CharSequence mTitle;
    private FragmentTransaction ft;
    private Fragment fragment;
    private FragmentManager fragmentManager = getFragmentManager();
    private ProgressDialog progressDialogo;

    //VARIABLES DE LOS ELEMENTOS DE LA IU
    DrawerLayout NavDrawerLayout;
    ListView NavList;
    ActionBarDrawerToggle mDrawerToggle;
    AdapterDrawerList NavAdapter;
    ImageView ImagenFotoU;
    TextView TxtNombreU;
    TextView TxtTipoU;

    //VARIABLES PARA CONEXION DE DATOS
    public String jsonResponse;
    public Context context;

    JsonObjectRequest request;
    android.content.Context Context;
    final String TAG = "MenuPrincipalPacienteActivity";
    private static String ip= VarEstatic.ObtenerIP();

    public MenuPrincipalPacienteActivity() {super();}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_principal);
        context=getApplicationContext();

        RecogerParametrosActIS();

        //DRAWER LAYOUT
        NavDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        NavList=(ListView)findViewById(android.R.id.list);

        //ADAPTADOR HEADER
        View header=getLayoutInflater().inflate(R.layout.adaptador_header, null);
        ImagenFotoU=(ImageView)header.findViewById(R.id.imagenFotoU);
        TxtNombreU=(TextView)header.findViewById(R.id.txtNombreU);
        TxtTipoU=(TextView)header.findViewById(R.id.txtTipoU);

        if (vFotoP.equals("")) {
            ImagenFotoU.setImageResource(R.drawable.user_foto);
        }else{
            byte[] b = Base64.decode(vFotoP, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            ImagenFotoU.setImageBitmap(bitmap);
        }
        TxtNombreU.setText(vUsuarioP);
        TxtTipoU.setText(vTipoUsuario);

        NavList.addHeaderView(header);
        NavIcons = getResources().obtainTypedArray(R.array.Iconos_menu_principal_paciente);
        titulos = getResources().getStringArray(R.array.Menu_principal_paciente);

        NavItms = new ArrayList<ListaItemDrawer>();
        NavItms.add(new ListaItemDrawer(titulos[0], NavIcons.getResourceId(0, -1)));
        NavItms.add(new ListaItemDrawer(titulos[1], NavIcons.getResourceId(1, -1)));
        NavItms.add(new ListaItemDrawer(titulos[2], NavIcons.getResourceId(2, -1)));

        NavAdapter= new AdapterDrawerList(MenuPrincipalPacienteActivity.this,NavItms);
        NavList.setAdapter(NavAdapter);

        mDrawerToggle = new ActionBarDrawerToggle(MenuPrincipalPacienteActivity.this, NavDrawerLayout,
                            R.drawable.tcgtheme1_ic_navigation_drawer, R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerClosed(View view) {}

            public void onDrawerOpened(View drawerView) {}
        };

        NavDrawerLayout.setDrawerListener(mDrawerToggle);

        NavList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                MostrarFragment(position);
            }
        });

        EnviarParametrosActPP();
    }


    public void RecogerParametrosActIS() {
        vIdPaciente = getIntent().getExtras().getLong("varIdCoP");
        vDependeDe = getIntent().getExtras().getLong("varDependeDe");
        vTipoUsuario = getIntent().getExtras().getString("varTipoUsuario");
        vUsuarioP = getIntent().getExtras().getString("varUsuario");
        vFotoP = getIntent().getExtras().getString("varFotoC");
        vIdIS = getIntent().getExtras().getLong("varIdIS");
    }

    public void EnviarParametrosActPP() {
        fragment = PantallaPacienteF.newInstance(vIdPaciente, vDependeDe);
        ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment, "PantallaPacienteF");
        ft.addToBackStack(null);
        ft.commit();
    }

    public void Informacion() {
        fragment = new MpInformacionF();
        ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment, "MpInformacionF");
        ft.addToBackStack(null);
        ft.commit();
    }

    public void CerrarSesion() {
        DFCerrarSesion2 dialogo1 = new DFCerrarSesion2();
        dialogo1.show(fragmentManager, "tagConfirmacion");
    }

    //METODO QUE ACTUALIZA EL CIERRE DE SESION
    public void GuardarFinSesion() {
        try {
            Calendar cal = new GregorianCalendar();
            cal.getTime();
            int anio = cal.get(Calendar.YEAR);
            int mes = cal.get(Calendar.MONTH);
            int dia = cal.get(Calendar.DAY_OF_MONTH);
            int hora = cal.get(Calendar.HOUR_OF_DAY);
            int minutos = cal.get(Calendar.MINUTE);

            //ATInicioSesion iniSesion=new ATInicioSesion();
            //iniSesion.new ActualizarInicioSesion().execute(String.valueOf(vIdIS),  String.valueOf(anio), String.valueOf(mes),
            //                                        String.valueOf(dia), String.valueOf(hora),String.valueOf(minutos), "", "true");

            String urlJson = "http://"+ip+"/ADP/InicioSesion/InicioSesionActualizarObject";

            HashMap<String, String> dato = new HashMap<String, String>();
            dato.put("IdIniSes", String.valueOf(vIdIS));
            dato.put("AnioFin",String.valueOf(anio));
            dato.put("MesFin",String.valueOf(mes));
            dato.put("DiaFin",String.valueOf(dia));
            dato.put("HoraFin",String.valueOf(hora));
            dato.put("MinutosFin",String.valueOf(minutos));
            dato.put("IdReGCM","");
            dato.put("Eliminado","true");

            request = new JsonObjectRequest(Request.Method.PUT, urlJson, new JSONObject(dato), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Boolean respuesta=response.getBoolean("Done");
                        if (respuesta) {
                            Log.e("¿Actualizado? =>", respuesta.toString());
                            Log.e("Datos Actualizados =>", "  SERVER");
                            EliminarDatos.EliminarDatosDB();
                            Log.e("Pasaba por Aqui =>", " DELETE TABLAS");
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
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);


        } catch (Exception ex) {
            Toast.makeText(MenuPrincipalPacienteActivity.this, getString(R.string.Error) + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void tareaLarga()
    {
        try {
            Thread.sleep(1000);
        } catch(InterruptedException e) {}
    }

    protected void ActualizarDatos(){
        progressDialogo = new ProgressDialog(MenuPrincipalPacienteActivity.this);
        progressDialogo.setCancelable(false);
        progressDialogo.setMessage(getResources().getString(R.string.DescargandoDatos));
        progressDialogo.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialogo.setProgress(0);
        progressDialogo.setMax((int) 100);
        progressDialogo.show();

        Thread t = new Thread(){
            public void run(){
                try {
                    int progreso = 0;
                    ActualizarDatosPac();
                    while (progreso <= 100) {
                        progreso+=10;
                        Thread.sleep(1000);
                        progressHandlerInc.sendMessage(progressHandlerInc.obtainMessage());
                    }
                    progressHandlerFin.sendMessage(progressHandlerFin.obtainMessage());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    Handler progressHandlerInc = new Handler() {
        public void handleMessage(Message msg) {
            progressDialogo.incrementProgressBy(10);
        }
    };

    Handler progressHandlerFin = new Handler() {
        public void handleMessage(Message msg) {
            progressDialogo.dismiss();
        }
    };

    public void ActualizarDatosPac(){
        if(estaConectado()) {
            TblActividadPaciente.EliminarDatos();
            ATActividadPaciente actPac = new ATActividadPaciente();
            List<TblActividadPaciente> listAP = null;
            try {
                listAP = actPac.new BuscarAllActividadXPaciente().execute(String.valueOf(vIdPaciente)).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            int con = 0;
            for (int i = 0; i < listAP.size(); i++) {
                VCActividades act = new VCActividades(getApplicationContext());
                act.BuscarUnaActividad(String.valueOf(listAP.get(i).getIdActividad()));
                con++;
                tareaLarga();
            }
            EnviarParametrosActPP();
        }
    }

    private void MostrarFragment(int position) {
        fragment = null;
        switch (position) {
            case 1:
                //PANTALLA: ACTUALIZAR DATOS
                //ActualizarDatos();
                break;
            case 2:
                //PANTALLA: INFORMACION
                Informacion();
                break;
            case 3:
                //PANTALLA: CERRAR SESION
                CerrarSesion();
                break;
        }

        if (fragment!=null) {
            NavList.setItemChecked(position, true);
            NavList.setSelection(position);
            setTitle(titulos[position-1]);
            NavDrawerLayout.closeDrawer(NavList);
            mTitle = getTitle();
        } else {
            Log.e(String.valueOf(R.string.Error), String.valueOf(R.string.MostrarFragmento) + position);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            Log.e(String.valueOf(R.string.DrawerTogglePushed), String.valueOf(R.string.X));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //VERIFICAR LA CONEXION DE INTERNET
    public Boolean estaConectado(){
        if(conectadoWifi()){
            return true;
        }else{
            if(conectadoRedMovil()){
                return true;
            }else{
                Toast.makeText(MenuPrincipalPacienteActivity.this, "No hay Conexion a Internet", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }

    //VERIFICAR CONEXION POR WIFI
    protected Boolean conectadoWifi(){
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (info != null) { if (info.isConnected()) { return true;} }
        }
        return false;
    }

    //VERIFICAR CONEXION POR DATOS MOVILES
    protected Boolean conectadoRedMovil(){
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (info != null) { if (info.isConnected()) { return true; } }
        }
        return false;
    }
}