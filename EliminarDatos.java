package com.Notifications.patientssassistant;


import android.util.Log;

import com.Notifications.patientssassistant.tables.*;


public class EliminarDatos {

    public static void EliminarDatosDB(){
        TblInicioSesion.EliminarDatos();
        TblActividades.EliminarDatos();
        TblActividadCuidador.EliminarDatos();
        TblActividadPaciente.EliminarDatos();
        TblBuzon.EliminarDatos();
        TblControlDieta.EliminarDatos();
        TblControlMedicina.EliminarDatos();
        TblCuidador.EliminarDatos();
        TblCuidadorPr.EliminarDatos();
        TblCuidadorS.EliminarDatos();
        TblEstadoSalud.EliminarDatos();
        TblEventosCuidadores.EliminarDatos();
        TblEventosPacientes.EliminarDatos();
        TblFamiliaresPacientes.EliminarDatos();
        TblHorarioMedicina.EliminarDatos();
        TblHilos.EliminarDatos();
        TblHorarios.EliminarDatos();
        TblObservaciones.EliminarDatos();
        TblPacientes.EliminarDatos();
        TblPermisos.EliminarDatos();
        TblRutinasCuidadores.EliminarDatos();
        TblRutinasPacientes.EliminarDatos();
        TblSeguimientoMedico.EliminarDatos();
        TblTipoActividad.EliminarDatos();
        Log.e("Borrando Datos de =>", " TABLAS");
        //Datos de Tablas internas
        MiTblRutina.EliminarDatos();
        MiTblEvento.EliminarDatos();
        MiTblMedicina.EliminarDatos();
        Log.e("Borrando Datos de =>", " TABLAS ALARMAS");
    }


    public static void EliminarTablas(){
        TblInicioSesion.deleteAll(TblInicioSesion.class);
        TblActividades.deleteAll(TblActividades.class);
        TblActividadCuidador.deleteAll(TblActividadCuidador.class);
        TblActividadPaciente.deleteAll(TblActividadPaciente.class);
        TblBuzon.deleteAll(TblBuzon.class);
        TblControlDieta.deleteAll(TblControlDieta.class);
        TblControlMedicina.deleteAll(TblControlMedicina.class);
        TblCuidador.deleteAll(TblCuidador.class);
        TblCuidadorPr.deleteAll(TblCuidadorPr.class);
        TblCuidadorS.deleteAll(TblCuidadorS.class);
        TblEstadoSalud.deleteAll(TblEstadoSalud.class);
        TblEventosCuidadores.deleteAll(TblEstadoSalud.class);
        TblEventosPacientes.deleteAll(TblEventosPacientes.class);
        TblFamiliaresPacientes.deleteAll(TblFamiliaresPacientes.class);
        TblHorarioMedicina.deleteAll(TblHorarioMedicina.class);
        TblHilos.deleteAll(TblHilos.class);
        TblHorarios.deleteAll(TblHorarios.class);
        TblObservaciones.deleteAll(TblObservaciones.class);
        TblPacientes.deleteAll(TblPacientes.class);
        TblPermisos.deleteAll(TblPermisos.class);
        TblRutinasCuidadores.deleteAll(TblRutinasCuidadores.class);
        TblRutinasPacientes.deleteAll(TblRutinasPacientes.class);
        TblSeguimientoMedico.deleteAll(TblSeguimientoMedico.class);
        TblTipoActividad.deleteAll(TblTipoActividad.class);
        Log.e("Borrando Datos de =>"," TABLAS");
        //Tablas internas
        MiTblEvento.deleteAll(MiTblEvento.class);
        MiTblRutina.deleteAll(MiTblRutina.class);
        MiTblMedicina.deleteAll(MiTblMedicina.class);
        Log.e("Borrando Datos de =>", " TABLAS ALARMAS");
    }


}