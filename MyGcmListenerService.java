package com.Notifications.patientssassistant;


import com.Notifications.patientssassistant.alarmas.*;
import com.Notifications.patientssassistant.tables.*;
import com.google.android.gms.gcm.GcmListenerService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.util.Base64;
import android.util.Log;
import com.orm.query.Condition;
import com.orm.query.Select;


public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";
    private MediaPlayer mPlayer;


    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("msg");
        //String title = "NUEVA PETICIÓN";
        long idCuidador = Long.parseLong(data.getString("idC"));
        int anio=Integer.parseInt(data.getString("anio"));
        int mes=Integer.parseInt(data.getString("mes"));
        int dia=Integer.parseInt(data.getString("dia"));
        int horas=Integer.parseInt(data.getString("horas"));
        int minutos=Integer.parseInt(data.getString("minutos"));

        long idPaciente = Long.parseLong(data.getString("idP"));
        long idActividad = Long.parseLong(data.getString("idA"));

        Log.i(TAG, "From: " + from);
        Log.i(TAG, "Message: " + message);

        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }

        try{
            TblBuzon buzon = new TblBuzon(idCuidador,idPaciente,idActividad,anio, mes, dia, horas, minutos, false,false);
            buzon.save();
            sendNotification(message, idCuidador, anio, mes, dia, horas, minutos, idPaciente, idActividad);
        }
        catch(Exception ex) {
            System.out.println("Error de buzon:"+ex);
        }


    }


    private void sendNotification(String message, long idCuidador, int anio, int mes, int dia, int horas, int minutos, long idPaciente, long idActividad)
    {
        TblActividades buscar_actividad = Select.from(TblActividades.class).where(Condition.prop("id_actividad").eq(idActividad),
                                                                                  Condition.prop("Eliminado").eq(0)).first();

        TblPacientes buscar_userP = Select.from(TblPacientes.class).where(Condition.prop("id_paciente").eq(idPaciente),
                                                                          Condition.prop("Eliminado").eq(0)).first();
        Bitmap bitmap;
        if (buscar_userP.getFotoP().equals("")) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.user_foto);
        }else{
            byte[] b = Base64.decode(buscar_userP.getFotoP(), Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        }

        Intent intent = new Intent(MyGcmListenerService.this, IniciarSesionActivity.class);
        intent.putExtra("idCuidador", idCuidador);
        intent.putExtra("anio", anio);
        intent.putExtra("mes", mes);
        intent.putExtra("dia", dia);
        intent.putExtra("horas", horas);
        intent.putExtra("minutos", minutos);
        intent.putExtra("idPaciente", idPaciente);
        intent.putExtra("idActividad", idActividad);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(MyGcmListenerService.this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        long[] vibrate = {0,100,200,300};
        int idTono=TonosClass.BuscarIdTonoNotificacion(buscar_actividad.getTonoActividad());
        Uri otherUri = Uri.withAppendedPath(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, String.valueOf(idTono));

        mPlayer = new MediaPlayer();
        mPlayer= MediaPlayer.create(MyGcmListenerService.this, idTono);
        mPlayer.setLooping(false);
        mPlayer.start();

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_logo)
                    .setLargeIcon(bitmap)
                .setContentTitle(message)
                    .setContentText(buscar_userP.getNombreApellidoP()+" - "+horas+":"+minutos)
                    .setAutoCancel(true)
                    .setSound(otherUri)
                    .setVibrate(vibrate)
                    .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }


}