package com.Notifications.patientssassistant.adapters;


import com.Notifications.patientssassistant.R;
import com.Notifications.patientssassistant.tables.*;
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


public class AdapterListaRutinasCuidadores extends BaseExpandableListAdapter {
	
	private final SparseArray<TblRutinasCuidadores> Grupos;
	public LayoutInflater Inflater;
	public Context Context;
	
	public AdapterListaRutinasCuidadores(Context Context, SparseArray<TblRutinasCuidadores> Grupos) {
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
		
		final TblRutinasCuidadores Children = (TblRutinasCuidadores)getChild(groupPosition, childPosition);
		
		textvw = (TextView)convertView.findViewById(R.id.txtSubItem);		
		String Test1 = Context.getResources().getString(R.string.ChildrenDescripcion)+" ";
		String Test2 = Children.getDescripcion()+"\n";
		String Test3 = Context.getResources().getString(R.string.ChildrenTono)+" ";
		String Test4 = Children.getTono();
		
		SpannableString ssb1 = new SpannableString(Test1);
		SpannableString ssb2 = new SpannableString(Test2);
		SpannableString ssb3 = new SpannableString(Test3);
		SpannableString ssb4 = new SpannableString(Test4);
		
		ssb1.setSpan(new ForegroundColorSpan(Context.getResources().getColor(R.color.azulado)), 0, ssb1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		ssb1.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 0, ssb1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textvw.setText(ssb1);		
		textvw.append(ssb2);		
		
		ssb3.setSpan(new ForegroundColorSpan(Context.getResources().getColor(R.color.azulado)), 0, ssb3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		ssb3.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 0, ssb3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textvw.append(ssb3);		
		textvw.append(ssb4);			
		
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
		
		TblRutinasCuidadores Grupo = (TblRutinasCuidadores)getGroup(groupPosition);
		TblActividades laActividad= Select.from(TblActividades.class).where(Condition.prop("id_actividad").eq(Grupo.getIdActividad())).first();
		
		String lun,mar,mie,jue,vie,sab,dom,dias;	
		if ((Grupo.getLunes()==true)&&(Grupo.getMartes()==true)&&(Grupo.getMiercoles()==true)&&(Grupo.getJueves()==true)&&
			(Grupo.getViernes()==true)&&(Grupo.getSabado()==true)&&(Grupo.getDomingo()==true)) 
		{
			dias=Context.getResources().getString(R.string.TodosLosDias);
		}else{
			if(Grupo.getDomingo()==true){dom=Context.getResources().getString(R.string.Dom)+", ";}else{dom="";}
			if(Grupo.getLunes()==true){lun=Context.getResources().getString(R.string.Lun)+", ";}else{lun="";}
			if(Grupo.getMartes()==true){mar=Context.getResources().getString(R.string.Mar)+", ";}else{mar="";}
			if(Grupo.getMiercoles()==true){mie=Context.getResources().getString(R.string.Mie)+", ";}else{mie="";}
			if(Grupo.getJueves()==true){jue=Context.getResources().getString(R.string.Jue)+", ";}else{jue="";}
			if(Grupo.getViernes()==true){vie=Context.getResources().getString(R.string.Vie)+", ";}else{vie="";}
			if(Grupo.getSabado()==true){sab=Context.getResources().getString(R.string.Sab)+", ";}else{sab="";}
			dias=dom+lun+mar+mie+jue+vie+sab;
			dias=dias.substring(0, dias.length()-2);
		}		
		String hora = String.format("%02d:%02d", Grupo.getHora(), Grupo.getMinutos());
	
		String Test1 = laActividad.getNombreActividad()+"  "+hora+"\n";
		String Test2 = dias;		
		
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