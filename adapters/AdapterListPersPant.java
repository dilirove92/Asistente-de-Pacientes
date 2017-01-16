package com.Notifications.patientssassistant.adapters;


import com.Notifications.patientssassistant.R;
import com.Notifications.patientssassistant.tables.*;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class AdapterListPersPant extends BaseAdapter {
	
	private final Context context;
	private List<TblActividades> actPetPac;
			
	public AdapterListPersPant(Context context, List<TblActividades> actPetPac) {
		super();
		this.context = context;
		this.actPetPac = actPetPac;
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
	
	@Override
	public View getView(int position, View convertview, ViewGroup parent) {
		View v= convertview;
		ViewHolder vista=new ViewHolder();
		
		LayoutInflater inflater = ((Activity)context).getLayoutInflater();
		v=inflater.inflate(R.layout.adaptador_lista_cop, parent, false);			
		final TblActividades clase = actPetPac.get(position);	
								
		vista.imagenIcono=(ImageView)v.findViewById(R.id.imagenFoto);			
		vista.txtPeticion=(TextView)v.findViewById(R.id.txtNombresListas);
		
		if (clase.getImagenActividad().equals("")) {
			vista.imagenIcono.setImageResource(R.drawable.picture_peticion);				
		}else{
			byte[] b = Base64.decode(clase.getImagenActividad(), Base64.DEFAULT);
			Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
	    	vista.imagenIcono.setImageBitmap(bitmap);				
		}							
		vista.txtPeticion.setText(clase.getNombreActividad());						
	
		v.setTag(vista);
		return v;
	}
	
	static class ViewHolder{
		public ImageView imagenIcono;
		public TextView txtPeticion;		
	}


}