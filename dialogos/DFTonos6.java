package com.Notifications.patientssassistant.dialogos;


import com.Notifications.patientssassistant.*;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;


public class DFTonos6 extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog alert = ((NewEditRutinaPacienteActivity)getActivity()).SeleccionarTono();
        return alert;
    }
}