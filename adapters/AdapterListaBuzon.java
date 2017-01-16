package com.Notifications.patientssassistant.adapters;


import com.Notifications.patientssassistant.R;
import com.Notifications.patientssassistant.tables.*;
import java.util.List;
import com.orm.query.Condition;
import com.orm.query.Select;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class AdapterListaBuzon extends BaseAdapter {
	
	private final Context context;
	private List<TblBuzon> buzon;

	public AdapterListaBuzon(Context context, List<TblBuzon> buzon) {
		super();
		this.context = context;
		this.buzon = buzon;
	}
		
	@Override
	public int getCount() {		
		return buzon.size();
	}
	
	@Override
	public Object getItem(int position) {
		return buzon.get(position);
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
		final TblBuzon buzon_clase = buzon.get(position);			
		v=inflater.inflate(R.layout.adaptador_lista_buzon, parent, false);
		
		vista.txtPacienteB=(TextView)v.findViewById(R.id.txtPacienteB);			
		vista.txtPeticionB=(TextView)v.findViewById(R.id.txtPeticionB);
		vista.txtFechaHoraB=(TextView)v.findViewById(R.id.txtFechaHoraB);
		
		Long campo_idPaciente=buzon_clase.getIdPaciente();			
		TblPacientes buscar_paciente= Select.from(TblPacientes.class).where(Condition.prop("id_paciente").eq(campo_idPaciente)).first();
		Long campo_idPeticion=buzon_clase.getIdActividad();			
		TblActividades buscar_peticion= Select.from(TblActividades.class).where(Condition.prop("id_actividad").eq(campo_idPeticion)).first();
			
		vista.txtPacienteB.setText(buscar_paciente.getNombreApellidoP());
		if (buzon_clase.getContador().equals(0)){
			vista.txtPeticionB.setText(buscar_peticion.getNombreActividad());
		}else{
			vista.txtPeticionB.setText(buscar_peticion.getNombreActividad()+"  ("+buzon_clase.getContador()+")  ");
		}
		vista.txtFechaHoraB.setText(String.format("%02d/%02d/%02d %02d:%02d", buzon_clase.getAnio(), buzon_clase.getMes()+1, buzon_clase.getDia(), buzon_clase.getHoras(), buzon_clase.getMinutos()));

		CambiarColorTexto((TextView) v.findViewById(R.id.txtPacienteB), buzon_clase.getPostergar());
		CambiarColorTexto((TextView) v.findViewById(R.id.txtPeticionB), buzon_clase.getPostergar());
		CambiarColorTexto((TextView) v.findViewById(R.id.txtFechaHoraB), buzon_clase.getPostergar());
				
		v.setTag(vista); 
		return v;
	}
	
	static class ViewHolder{
		public TextView txtPacienteB;
		public TextView txtPeticionB;
		public TextView txtFechaHoraB;
	}

	public void CambiarColorTexto(TextView view, boolean isOn) {
		if (isOn) {
			view.setTextColor(Color.BLUE);
		} else {
			view.setTextColor(Color.BLACK);
		}
	}

	
}