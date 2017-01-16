package com.Notifications.patientssassistant.dialogos;


import com.Notifications.patientssassistant.R;
import com.Notifications.patientssassistant.*;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;


public class DFInformacionAlCorreo extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.InformacionAlCorreo)
                .setTitle(R.string.Informacion)
                .setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_info))
                .setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((RecuperarContrasenaActivity)getActivity()).LimpiarElementos();
                        dialog.cancel();
                        getActivity().finish();
                    }
                });
        AlertDialog alert=builder.create();
        setCancelable(false);
        alert.setCanceledOnTouchOutside(false);
        return alert;
    }
}