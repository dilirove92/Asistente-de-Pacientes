package com.Notifications.patientssassistant.dialogos;


import com.Notifications.patientssassistant.*;
import com.Notifications.patientssassistant.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;


public class DFCerrarSesion3 extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.DeseaCerrar)
                .setTitle(R.string.ConfSalida)
                .setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_alert))
                .setPositiveButton(R.string.Aceptar, new DialogInterface.OnClickListener()  {
                    public void onClick(DialogInterface dialog, int id) {
                        //CERRAMOS SESION Y ACTUALIZAMOS LA TABLA SESION
                        ((MenuPrincipalRestringidoActivity)getActivity()).GuardarFinSesion();
                        Log.e("Pasaba por aqui=>", " FIN SESION");
                        //ELIMINAR ALARMAS DEL CELL
                        //((MenuPrincipalRestringidoActivity)getActivity()).EliminarAlarmas();
                        //BORRAMOS EL USUARIO ALMACENADO EN PREFERENCIAS Y VOLVEMOS A LA PANTALLA LOGIN
                        SharedPreferences settings = getActivity().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putLong("idCoP", 0L);
                        editor.putString("tipoUser", "");
                        editor.putString("usuario", "");
                        editor.putString("fotoCoP", "");
                        editor.putString("tu", "");
                        editor.putLong("dependeDe", 0L);
                        editor.putBoolean("controlT", false);
                        editor.putLong("idIS", 0L);
                        //CONFIRMAMOS EL ALMACENAMIENTO
                        editor.commit();
                        //VOLVEMOS A LA PANTALLA LOGIN
                        Intent intent = new Intent(getActivity(), IniciarSesionActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                        dialog.cancel();
                    }
                })
                .setNegativeButton(R.string.Cancelar, new DialogInterface.OnClickListener() {
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