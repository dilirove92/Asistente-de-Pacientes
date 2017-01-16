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


public class AdapterListaFamilia extends BaseAdapter {
	
	private final Context context;
	private List<TblFamiliaresPacientes> familiarPaciente;
	
	public AdapterListaFamilia(Context context, List<TblFamiliaresPacientes> familiarPaciente) {
		super();
		this.context = context;
		this.familiarPaciente = familiarPaciente;
	}

	@Override
	public int getCount() {
		return familiarPaciente.size();
	}
	
	@Override
	public Object getItem(int position) {
		return familiarPaciente.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(final int position, View convertview, ViewGroup parent) {
		View v = convertview;
		ViewHolder vista = new ViewHolder();
			
		LayoutInflater inflater = ((Activity)context).getLayoutInflater();
		v=inflater.inflate(R.layout.adaptador_lista_cop, parent, false);
		final TblFamiliaresPacientes fam_pac_clase = familiarPaciente.get(position);			
														
		vista.ImagenFotoF=(ImageView)v.findViewById(R.id.imagenFoto);
		vista.TxtNombresF=(TextView)v.findViewById(R.id.txtNombresListas);
		
		if (fam_pac_clase.getFotoContacto().equals("")) {
			vista.ImagenFotoF.setImageResource(R.drawable.user_foto);				
		}else{
			byte[] b = Base64.decode(fam_pac_clase.getFotoContacto(), Base64.DEFAULT);
			Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
	    	vista.ImagenFotoF.setImageBitmap(bitmap);				
		}					
		vista.TxtNombresF.setText(fam_pac_clase.getNombreContacto());
		
		v.setTag(vista);
		return v;
	}
	
	static class ViewHolder{		
		public ImageView ImagenFotoF;
		public TextView TxtNombresF;
	}
	
	
}