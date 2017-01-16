package com.Notifications.patientssassistant.volleyscalls;

import android.content.Context;

/**
 * Created by Jazmine on 28/12/2015.
 */
public class VCDownload {

    public void MetodosActualizacionBaseVolley(Long idC,Context Context){
        String idCuidador=String.valueOf(idC);

        VCActividadCuidadores objActCui= new VCActividadCuidadores(Context);
        objActCui.BuscarAllActividadCuidadores(idCuidador);

        VCActividades objAct= new VCActividades(Context);
        objAct.ListaDeActividades(idCuidador);
        //objAct.BuscarAllActividades(idCuidador);

        VCActividadPaciente objActPac= new VCActividadPaciente(Context);
        objActPac.BuscarAllActividadPacientes(idCuidador);

        VCControlDieta objContDie= new VCControlDieta(Context);
        objContDie.BuscarAllControlDietaCuidadores(idCuidador);

        VCControlMedicina objContMed= new VCControlMedicina(Context);
        objContMed.BuscarAllControlMedicinaXCuidadores(idCuidador);

        VCCuidador objCuidador= new VCCuidador(Context);
        objCuidador.ListaDeCuidadores(idCuidador);
        //objCuidador.BuscarAllCuidadores(idCuidador);

        VCCuidadorPr objCuiPr= new VCCuidadorPr(Context);
        objCuiPr.BuscarAllCuidadoresPr(idCuidador);

        VCCuidadorS objCuiS= new VCCuidadorS(Context);
        objCuiS.BuscarAllCuidadoresS(idCuidador);

        VCEstadoSalud objEstSal= new VCEstadoSalud(Context);
        objEstSal.BuscarAllEstadosSaludCuidadores(idCuidador);

        VCEventosCuidador objEvenCui= new VCEventosCuidador(Context);
        objEvenCui.BuscarAllEventosCuidadores(idCuidador);

        VCEventosPaciente objEvenPac= new VCEventosPaciente(Context);
        objEvenPac.BuscarAllEventosXCuidadores(idCuidador);

        VCFamiliaresPacientes objFamPac= new VCFamiliaresPacientes(Context);
        objFamPac.ListaDeFamiliares(idCuidador);
        //objFamPac.BuscarAllFamiliaresPacientesCuidadores(idCuidador);

        VCHorarioMedicinas objHorMed= new VCHorarioMedicinas(Context);
        objHorMed.BuscarAllHorarioMedicinaXCuidadores(idCuidador);

        VCHorarios objHorario= new VCHorarios(Context);
        objHorario.BuscarAllHorariosXCuidadores(idCuidador);

        VCInicioSesion objIniSes= new VCInicioSesion(Context);
        objIniSes.BuscarAllIniciosSesion(idCuidador);

        VCObservaciones objObs= new VCObservaciones(Context);
        objObs.BuscarAllObservacionesCuidadores(idCuidador);

        VCPacientes objPacientes= new VCPacientes(Context);
        objPacientes.ListaDePacientes(idCuidador);
        //objPacientes.BuscarAllPacientes(idCuidador);

        VCPermisos objPermiso= new VCPermisos(Context);
        objPermiso.BuscarAllPermisosXCuidadores(idCuidador);

        VCRutinasCuidadores objRutCui= new VCRutinasCuidadores(Context);
        objRutCui.BuscarAllRutinasCuidadores(idCuidador);

        VCRutinasPacientes objRutPac= new VCRutinasPacientes(Context);
        objRutPac.BuscarAllRutinasCuidadores(idCuidador);

        VCSeguimientoMedico objSegMed= new VCSeguimientoMedico(Context);
        objSegMed.BuscarAllSeguimientoMedicoXCuidadores(idCuidador);

        VCTipoActividad objTipoAct= new VCTipoActividad(Context);
        objTipoAct.BuscarAllTipoActividad();
    }
}
