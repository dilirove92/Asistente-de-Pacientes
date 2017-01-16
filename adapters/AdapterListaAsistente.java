package com.Notifications.patientssassistant.adapters;


import com.Notifications.patientssassistant.R;
import java.text.SimpleDateFormat;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class AdapterListaAsistente extends BaseAdapter {
	
	private final Context context;
	private List<ListaAsistente> listAsist;
	private static SimpleDateFormat sdfTime =  new  SimpleDateFormat ("HH:mm");
	
	
	public AdapterListaAsistente(Context context, List<ListaAsistente> listAsist) {
		super();
		this.context = context;
		this.listAsist = listAsist;
	}
	
	@Override
	public int getCount() {
		return listAsist.size();
	}
	
	@Override
	public Object getItem(int position) {
		return listAsist.get(position);
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
		v=inflater.inflate(R.layout.adaptador_list_asistente, parent, false);		
		final ListaAsistente asistente_clase = listAsist.get(position);
		
		vista.ImageView1=(ImageView)v.findViewById(R.id.imageView1);			
		vista.TxtHora=(TextView)v.findViewById(R.id.txtHora);
		vista.TxtAct=(TextView)v.findViewById(R.id.txtAct);
		
		int colorId=context.getResources().getIdentifier(asistente_clase.getColor(), "color", context.getPackageName());
		vista.ImageView1.setBackgroundColor(context.getResources().getColor(colorId));		
		vista.TxtHora.setText(sdfTime.format(asistente_clase.getHora()));
		vista.TxtAct.setText(asistente_clase.getActividad()+", "+asistente_clase.getDetalle());
		
		v.setTag(vista);		
		return v;
	}
	
	static class ViewHolder{
		public ImageView ImageView1;
		public TextView TxtHora;
		public TextView TxtAct;
	}


}