package com.Notifications.patientssassistant.adapters;


import com.Notifications.patientssassistant.R;
import com.Notifications.patientssassistant.tables.*;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;


public class AdapterVistaPrevia extends BaseAdapter {

    private final Context context;
    private ArrayList<TblActividades> actPetPac ;
    private LayoutInflater mInflater;
    private int mItemHeight = 0;
    private int mNumColumns = 0;
    private RelativeLayout.LayoutParams mImageViewLayoutParams;


    public AdapterVistaPrevia(Context context, ArrayList<TblActividades> actPetPac) {
        super();
        this.context = context;
        this.actPetPac = actPetPac;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mImageViewLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public int getCount() {
        return actPetPac.size();
    }

    @Override
    public Object getItem(int position) {
        return actPetPac.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setNumColumns(int numColumns) {
        this.mNumColumns = numColumns;
    }

    public int getNumColumns() {
        return mNumColumns;
    }

    public void setItemHeight(int height) {
        if (height == mItemHeight) {
            return;
        }
        mItemHeight = height;
        mImageViewLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, mItemHeight);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertview, ViewGroup parent) {
        View v = convertview;
        ViewHolder vista = new ViewHolder();

        v = mInflater.inflate(R.layout.adaptador_gridview_opciones, parent, false);
        final TblActividades clase = actPetPac.get(position);

        vista.imagenIcono=(ImageView)v.findViewById(R.id.imageOpcion);
        vista.txtPeticion=(TextView)v.findViewById(R.id.txtOpcion);

        vista.imagenIcono.setLayoutParams(mImageViewLayoutParams);
        if (vista.imagenIcono.getLayoutParams().height != mItemHeight) {
            vista.imagenIcono.setLayoutParams(mImageViewLayoutParams);
        }

        if (clase.getImagenActividad().equals("")) {
            vista.imagenIcono.setImageResource(R.drawable.picture_peticion);
        }else{
            byte[] b = Base64.decode(clase.getImagenActividad(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            vista.imagenIcono.setImageBitmap(bitmap);
        }
        vista.txtPeticion.setText(clase.getNombreActividad().toUpperCase());

        v.setTag(vista);
        return v;
    }

    static class ViewHolder{
        public ImageView imagenIcono;
        public TextView txtPeticion;
    }


}