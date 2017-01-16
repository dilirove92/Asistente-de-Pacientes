package com.Notifications.patientssassistant.adapters;


import com.Notifications.patientssassistant.R;
import com.Notifications.patientssassistant.tables.*;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class AdapterListaHT extends BaseAdapter {
	
	private final Context context;
	private List<TblHorarios> horarios;	
		
	public AdapterListaHT(Context context, List<TblHorarios> horarios) {
		super();
		this.context = context;
		this.horarios = horarios;
	}

	@Override
	public int getCount() {
		return horarios.size();
	}
	
	@Override
	public Object getItem(int position) {
		return horarios.get(position);
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
		final TblHorarios horarios_clase = horarios.get(position);			
		v=inflater.inflate(R.layout.adaptador_lista_ht, parent, false);			
								
		vista.TxtHoraIF=(TextView)v.findViewById(R.id.txtHoraIF);
		vista.TxtTipoH=(TextView)v.findViewById(R.id.txtTipoH);
		vista.TxtDias=(TextView)v.findViewById(R.id.txtDias);
					
		String lun,mar,mie,jue,vie,sab,dom,dias;			
		if ((horarios_clase.getLunes().equals(true))&&(horarios_clase.getMartes().equals(true))&&(horarios_clase.getMiercoles().equals(true))&&(horarios_clase.getJueves().equals(true))&&
			(horarios_clase.getViernes().equals(true))&&(horarios_clase.getSabado().equals(true))&&(horarios_clase.getDomingo().equals(true))) 
		{
			dias=context.getResources().getString(R.string.TodosLosDias);
		}else{
			if(horarios_clase.getDomingo().equals(true)){dom=context.getResources().getString(R.string.Dom)+", ";}else{dom="";}
			if(horarios_clase.getLunes().equals(true)){lun=context.getResources().getString(R.string.Lun)+", ";}else{lun="";}
			if(horarios_clase.getMartes().equals(true)){mar=context.getResources().getString(R.string.Mar)+", ";}else{mar="";}
			if(horarios_clase.getMiercoles().equals(true)){mie=context.getResources().getString(R.string.Mie)+", ";}else{mie="";}
			if(horarios_clase.getJueves().equals(true)){jue=context.getResources().getString(R.string.Jue)+", ";}else{jue="";}
			if(horarios_clase.getViernes().equals(true)){vie=context.getResources().getString(R.string.Vie)+", ";}else{vie="";}
			if(horarios_clase.getSabado().equals(true)){sab=context.getResources().getString(R.string.Sab)+", ";}else{sab="";}
			dias=dom+lun+mar+mie+jue+vie+sab;
			dias=dias.substring(0, dias.length()-2);
		}			
		String horaI = String.format("%02d:%02d", horarios_clase.getHoraIni(), horarios_clase.getMinutosIni());
		String horaF = String.format("%02d:%02d", horarios_clase.getHoraFin(), horarios_clase.getMinutosFin());
		
		vista.TxtHoraIF.setText(horaI+" - "+horaF);			
		vista.TxtTipoH.setText(horarios_clase.getTipoHorario());			
		vista.TxtDias.setText(dias);
		
		v.setTag(vista);					
		return v;
	}
	
	static class ViewHolder{		
		public TextView TxtHoraIF;
		public TextView TxtTipoH;		
		public TextView TxtDias;		
	}	

	
}