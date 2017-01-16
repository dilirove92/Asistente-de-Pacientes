package com.Notifications.patientssassistant.adapters;


import com.Notifications.patientssassistant.*;
import com.Notifications.patientssassistant.tables.*;
import java.util.List;
import com.orm.query.Condition;
import com.orm.query.Select;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;


public class AdapterListaPermisos extends BaseAdapter {
	
	private final Context context;
	private List<TblPermisos> permisos;	
	
	public AdapterListaPermisos(Context context, List<TblPermisos> permisos) {
		super();
		this.context = context;
		this.permisos = permisos;
	}

	@Override
	public int getCount() {
		return permisos.size();
	}
	
	@Override
	public Object getItem(int position) {
		return permisos.get(position);
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
		v=inflater.inflate(R.layout.adaptador_lista_permisos, parent, false);			
		final TblPermisos permisos_clase = permisos.get(position);			
			
		vista.ImagenFotoPC=(ImageView)v.findViewById(R.id.imagenFotoPC);
		vista.TxtNombresPC=(TextView)v.findViewById(R.id.txtNombresPC);
		vista.ChckNotiAlar=(CheckBox)v.findViewById(R.id.chckNotiAlar);
		vista.ChckContMedi=(CheckBox)v.findViewById(R.id.chckContMedi);
		
		Long campo_idPaciente=permisos_clase.getIdPaciente();			
		TblPacientes buscar_paciente= Select.from(TblPacientes.class).where(Condition.prop("id_paciente").eq(campo_idPaciente)).first();
		
		if (buscar_paciente.getFotoP().equals("")) {
			vista.ImagenFotoPC.setImageResource(R.drawable.user_foto);				
		}else{
			byte[] b = Base64.decode(buscar_paciente.getFotoP(), Base64.DEFAULT);
			Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
	    	vista.ImagenFotoPC.setImageBitmap(bitmap);				
		}					
		vista.TxtNombresPC.setText(buscar_paciente.getNombreApellidoP());
		vista.ChckNotiAlar.setChecked(permisos_clase.getNotifiAlarma());
		vista.ChckContMedi.setChecked(permisos_clase.getContMedicina());			
			
		v.setTag(vista);			
		return v;
	}
	
	static class ViewHolder{		
		public ImageView ImagenFotoPC;
		public TextView TxtNombresPC;		
		public CheckBox ChckNotiAlar;
		public CheckBox ChckContMedi;
	}		

	
}