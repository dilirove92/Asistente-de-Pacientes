package com.Notifications.patientssassistant.dialogos;


import com.Notifications.patientssassistant.*;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;


public class DFTonos2 extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog alert = ((NewEditEventoCuidadorActivity)getActivity()).SeleccionarTono();
        return alert;
    }
}