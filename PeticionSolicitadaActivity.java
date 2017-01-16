package com.Notifications.patientssassistant;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;


public class PeticionSolicitadaActivity extends Activity {

    private static final int WAKELOCK_TIMEOUT = 5000;
    private Bitmap bitmap;
    private byte[] b;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.peticion_seleccionada);
        this.setFinishOnTouchOutside(false);

        ImageView imagenIcono=(ImageView)findViewById(R.id.imageOpcion);
        TextView txtPeticion=(TextView)findViewById(R.id.txtOpcion);

        String vImagen = getIntent().getExtras().getString("varImagen");
        String vNombre = getIntent().getExtras().getString("varNombre");

        if (vImagen.equals("")) {
            imagenIcono.setImageResource(R.drawable.picture_peticion);
        }else{
            b = Base64.decode(vImagen, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            imagenIcono.setImageBitmap(bitmap);
        }
        txtPeticion.setText(vNombre.toUpperCase());

        Runnable temporizador = new Runnable() {
            @Override
            public void run() {
                finish();
            }
        };
        new Handler().postDelayed(temporizador, WAKELOCK_TIMEOUT);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    public void onBackPressed() { }


}