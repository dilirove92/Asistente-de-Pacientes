package com.Notifications.patientssassistant.adapters;


import com.Notifications.patientssassistant.*;
import com.Notifications.patientssassistant.tables.*;
import com.orm.query.Condition;
import com.orm.query.Select;
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


public class AdapterSpinnerPacientes extends BaseAdapter {
	
	private final Context context;
	private List<TblPermisos> pacientes;
		
	public AdapterSpinnerPacientes(Context context, List<TblPermisos> pacientes) {
		super();
		this.context = context;
		this.pacientes = pacientes;
	}
	
	@Override
	public int getCount() {
		return pacientes.size();
	}
	
	@Override
	public Object getItem(int position) {
		return pacientes.get(position);
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
		v=inflater.inflate(R.layout.adaptador_spinner_pacientes, parent, false);		
		final TblPermisos permiso_clase = pacientes.get(position);
		
		vista.imagenFoto=(ImageView)v.findViewById(R.id.imagenFoto);			
		vista.txtNombresListas=(TextView)v.findViewById(R.id.txtNombresListas);
		
		TblPacientes paciente_clase=Select.from(TblPacientes.class).where(Condition.prop("id_paciente").eq(permiso_clase.getIdPaciente())).first();
		
		if (paciente_clase.getFotoP().equals("")) {
			vista.imagenFoto.setImageResource(R.drawable.user_foto);				
		}else{
			byte[] b = Base64.decode(paciente_clase.getFotoP(), Base64.DEFAULT);
			Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length); 
	    	vista.imagenFoto.setImageBitmap(bitmap);				
		}			
		vista.txtNombresListas.setText(paciente_clase.getNombreApellidoP());
	
		v.setTag(vista);
		return v;
	}
	
	static class ViewHolder{
		public ImageView imagenFoto;
		public TextView txtNombresListas;		
	}


}