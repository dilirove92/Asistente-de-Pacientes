package com.Notifications.patientssassistant.dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.Notifications.patientssassistant.R;

/**
 * Created by Yahina on 28/12/2015.
 */
public class DFNoExisteCorreo extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.NoExisteCorreo)
                .setTitle(R.string.Informacion)
                .setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_info))
                .setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert=builder.create();
        setCancelable(false);
        alert.setCanceledOnTouchOutside(false);
        return alert;
    }
}