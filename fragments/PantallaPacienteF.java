package com.Notifications.patientssassistant.fragments;


import com.Notifications.patientssassistant.R;
import com.Notifications.patientssassistant.adapters.*;
import com.Notifications.patientssassistant.tables.*;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.Toast;
import com.orm.query.Condition;
import com.orm.query.Select;
import java.util.ArrayList;
import java.util.List;


public class PantallaPacienteF extends Fragment {

    public final static String KEY_REG_TEXT1 = "IdPaciente";
    public final static String KEY_REG_TEXT2 = "DependeDe";
    private static Long vIdPaciente;
    private static Long vDependeDe;

    //VARIABLES AUXILIARES
    private FragmentIterationListener mCallback = null;
    private AdapterGridViewOpciones miAdapter;
    private ArrayList<TblActividades> list_acts = null;
    private int anchoPantalla, altoPantalla;
    private int nColumnas, nFilas;
    private int mPhotoSpacing;
    private int widthPhoto, heightPhoto;

    //VARIABLES DE LOS ELEMENTOS DE LA IU
    GridView GridViewPet;


    public interface FragmentIterationListener{
        public void onFragmentIteration(Bundle parameters);
    }

    public PantallaPacienteF() {super();}

    public static PantallaPacienteF newInstance(Long c_idPaciente, Long c_dependeDe) {
        PantallaPacienteF frag = new PantallaPacienteF();
        Bundle args = frag.getArguments();
        if (args == null)
            args = new Bundle();
        args.putLong(KEY_REG_TEXT1, c_idPaciente);
        args.putLong(KEY_REG_TEXT2, c_dependeDe);
        frag.setArguments(args);
        return frag;
    }

    public void RecogerParametrosActIS() {
        vIdPaciente = getArguments().getLong(KEY_REG_TEXT1);
        vDependeDe = getArguments().getLong(KEY_REG_TEXT2);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            mCallback = (FragmentIterationListener)activity;
        }catch(Exception ex){
            Log.e(String.valueOf(R.string.ExampleFragment), String.valueOf(R.string.FragmentoImplementarFIL));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Object restore = getActivity().getLastNonConfigurationInstance();
        if (restore != null)
        {
            list_acts = (ArrayList<TblActividades>) restore;
        }
        else
        {
            RecogerParametrosActIS();
            BuscarPeticiones();
        }

        EsPortraitLanscape();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.vista_gridview, container, false);
        if(rootView != null){
            GridViewPet = (GridView)rootView.findViewById(R.id.opcGrid);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //LISTA CON DATOS
        if(!list_acts.isEmpty()) {
            miAdapter = new AdapterGridViewOpciones(getActivity(), list_acts);
            GridViewPet.setAdapter(miAdapter);
        }

        CrearFilasColumnasGrid();
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
    }

    public void BuscarPeticiones() {
        try {
            List<TblActividadPaciente> list_actPac = Select.from(TblActividadPaciente.class).where(Condition.prop("id_paciente").eq(vIdPaciente),
                                                                            Condition.prop("Eliminado").eq(0)).orderBy("id_actividad asc").list();
            TblTipoActividad tipoActividad = Select.from(TblTipoActividad.class).where(Condition.prop("tipo_actividad").eq("PETICION"),
                                                                                       Condition.prop("Eliminado").eq(0)).first();
            list_acts=new ArrayList<TblActividades>();
            TblActividadPaciente registro=new TblActividadPaciente();

            for (int i = 0; i < list_actPac.size(); i++) {
                registro=list_actPac.get(i);

                TblActividades Acts = Select.from(TblActividades.class).where(Condition.prop("id_actividad").eq(registro.getIdActividad()),
                                                                Condition.prop("id_tipo_actividad").eq(tipoActividad.getIdTipoActividad()),
                                                                Condition.prop("Eliminado").eq(0)).first();
                if (Acts!=null) {
                    list_acts.add(Acts);
                }
            }
        } catch (Exception ex) {
            Toast.makeText(getActivity(), getString(R.string.Error) + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void EsPortraitLanscape() {
        Configuration newConfig= Resources.getSystem().getConfiguration();
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (list_acts.size()==8) { nColumnas = 4; nFilas = 2; }
            if (list_acts.size()==9) { nColumnas = 3; nFilas = 3; }
            if (list_acts.size()>=10) { nColumnas = 4; nFilas = 3; }
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            if (list_acts.size()==8) { nColumnas = 2; nFilas = 4; }
            if (list_acts.size()==9) { nColumnas = 3; nFilas = 3; }
            if (list_acts.size()>=10) { nColumnas = 3; nFilas = 4; }
        }
    }

    public void CrearFilasColumnasGrid() {
        mPhotoSpacing = getResources().getDimensionPixelSize(R.dimen.photo_spacing);

        //TAMAÑO DE LA PANTALLA ANCHO Y ALTO
        Display display = ((WindowManager)getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        anchoPantalla = display.getWidth();
        altoPantalla = display.getHeight();

        GridViewPet.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (miAdapter.getNumColumns() == 0) {
                    widthPhoto = (anchoPantalla / nColumnas) - mPhotoSpacing;
                    heightPhoto = (altoPantalla / nFilas) - mPhotoSpacing;
                    final int numColumns = (int) Math.floor(GridViewPet.getWidth() / (widthPhoto + mPhotoSpacing));
                    miAdapter.setNumColumns(numColumns);
                    miAdapter.setItemHeight(heightPhoto);
                    GridViewPet.setColumnWidth(widthPhoto);
                }
            }
        });
    }

    public Long vDependeDe(){
        return vDependeDe;
    }

    public Long vIdPaciente(){
        return vIdPaciente;
    }

    public void EliminarDatos(){
        TblInicioSesion.deleteAll(TblInicioSesion.class);
        TblActividades.deleteAll(TblActividades.class);
        TblActividadPaciente.deleteAll(TblActividadPaciente.class);
        TblCuidador.deleteAll(TblCuidador.class);
        TblCuidadorPr.deleteAll(TblCuidadorPr.class);
        TblCuidadorS.deleteAll(TblCuidadorS.class);
        TblPacientes.deleteAll(TblPacientes.class);
        TblPermisos.deleteAll(TblPermisos.class);
        TblTipoActividad.deleteAll(TblTipoActividad.class);
    }

    public Object onRetainNonConfigurationInstance ()
    {
        return list_acts;
    }


}