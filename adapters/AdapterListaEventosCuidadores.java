package com.Notifications.patientssassistant.adapters;


import com.Notifications.patientssassistant.*;
import com.Notifications.patientssassistant.tables.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.orm.query.Condition;
import com.orm.query.Select;


public class AdapterListaEventosCuidadores extends BaseExpandableListAdapter {
	
	private final SparseArray<TblEventosCuidadores> Grupos;
	public LayoutInflater Inflater;
	public Context Context;
	
	public AdapterListaEventosCuidadores(Context Context, SparseArray<TblEventosCuidadores> Grupos) {
		this.Context = Context;
		this.Grupos = Grupos;
		this.Inflater = ((Activity)Context).getLayoutInflater();
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return Grupos.get(groupPosition).Children.get(childPosition);
	}
	
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}
	
	@Override
	public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		TextView textvw = null;		
		if (convertView == null) {
			convertView = Inflater.inflate(R.layout.adaptador_list_subitems, null);
		}
		
		final TblEventosCuidadores Children = (TblEventosCuidadores)getChild(groupPosition, childPosition);
		
		Calendar calendario = new GregorianCalendar();		   
		calendario.set(Children.getAnioR(), Children.getMesR(), Children.getDiaR()); 
		String fechaR = MetodosValidacionesExtras.CalcularLaFecha(Context, calendario.getTime());
		String horaR = String.format("%02d:%02d", Children.getHoraR(), Children.getMinutosR());
		
		textvw = (TextView)convertView.findViewById(R.id.txtSubItem);			
		String Test1 = Context.getResources().getString(R.string.ChildrenFechaRecordatorio)+" ";
		String Test2 = fechaR+"  "+horaR+"\n";
		String Test3 = Context.getResources().getString(R.string.ChildrenLugar)+" ";
		String Test4 = Children.getLugar()+"\n";
		String Test5 = Context.getResources().getString(R.string.ChildrenDescripcion)+" ";
		String Test6 = Children.getDescripcion()+"\n";
		String Test7 = Context.getResources().getString(R.string.ChildrenTono)+" ";
		String Test8 = Children.getTono();	
		
		SpannableString ssb1 = new SpannableString(Test1);
		SpannableString ssb2 = new SpannableString(Test2);
		SpannableString ssb3 = new SpannableString(Test3);
		SpannableString ssb4 = new SpannableString(Test4);
		SpannableString ssb5 = new SpannableString(Test5);
		SpannableString ssb6 = new SpannableString(Test6);
		SpannableString ssb7 = new SpannableString(Test7);
		SpannableString ssb8 = new SpannableString(Test8);
		
		ssb1.setSpan(new ForegroundColorSpan(Context.getResources().getColor(R.color.azulado)), 0, ssb1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		ssb1.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 0, ssb1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textvw.setText(ssb1);		
		textvw.append(ssb2);		
		
		ssb3.setSpan(new ForegroundColorSpan(Context.getResources().getColor(R.color.azulado)), 0, ssb3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		ssb3.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 0, ssb3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textvw.append(ssb3);		
		textvw.append(ssb4);
		
		ssb5.setSpan(new ForegroundColorSpan(Context.getResources().getColor(R.color.azulado)), 0, ssb5.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		ssb5.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 0, ssb5.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textvw.append(ssb5);
		textvw.append(ssb6);		
		
		ssb7.setSpan(new ForegroundColorSpan(Context.getResources().getColor(R.color.azulado)), 0, ssb7.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		ssb7.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 0, ssb7.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textvw.append(ssb7);
		textvw.append(ssb8);			
		
		return convertView;
	}
	
	@Override
	public int getChildrenCount(int groupPosition) {
		return Grupos.get(groupPosition).Children.size();
	}
	
	@Override
	public Object getGroup(int groupPosition) {
		return Grupos.get(groupPosition);
	}
	
	@Override
	public int getGroupCount() {
		return Grupos.size();
	}
	
	@Override
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);
	}
	
	@Override
	public void onGroupExpanded(int groupPosition) {
		super.onGroupExpanded(groupPosition);
	}
	
	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}
	
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		ToggleButton ToggAlarma;	
		TextView TxtItem = null;
		ImageView ImagenDU;		
		if (convertView == null) {
			convertView = Inflater.inflate(R.layout.adaptador_list_items3, null);
		}
		
		ToggAlarma = (ToggleButton)convertView.findViewById(R.id.toggAlarma);
		TxtItem = (TextView)convertView.findViewById(R.id.txtItem);
		ImagenDU = (ImageView)convertView.findViewById(R.id.imagenDU);
		
		TblEventosCuidadores Grupo = (TblEventosCuidadores)getGroup(groupPosition);		
		TblActividades laActividad= Select.from(TblActividades.class).where(Condition.prop("id_actividad").eq(Grupo.getIdActividad())).first();
		
		Calendar calendario = new GregorianCalendar();
		calendario.set(Grupo.getAnioE(), Grupo.getMesE(), Grupo.getDiaE());    	
		String fechaE = MetodosValidacionesExtras.CalcularLaFecha(Context, calendario.getTime());
		String horaE = String.format("%02d:%02d", Grupo.getHoraE(), Grupo.getMinutosE());
				
		String Test1 = laActividad.getNombreActividad()+"  "+horaE+"\n";
		String Test2 = fechaE;
		
		SpannableString ssb1 = new SpannableString(Test1);
		SpannableString ssb2 = new SpannableString(Test2);
						
		ssb1.setSpan(new StyleSpan(Typeface.BOLD), 0, ssb1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		TxtItem.setText(ssb1);		
		TxtItem.append(ssb2);	
		
		ToggAlarma.setChecked(Grupo.getAlarma());
		
		if (isExpanded) {
			ImagenDU.setImageResource(R.drawable.expand_up);
		}else{
			ImagenDU.setImageResource(R.drawable.expand_down);
		}
		
		return convertView;
	}
	
	@Override
	public boolean hasStableIds() {
		return false;
	}
	
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}


}