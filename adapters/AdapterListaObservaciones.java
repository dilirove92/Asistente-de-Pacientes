package com.Notifications.patientssassistant.adapters;


import java.util.Calendar;
import java.util.GregorianCalendar;

import com.Notifications.patientssassistant.MetodosValidacionesExtras;
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


public class AdapterListaObservaciones extends BaseExpandableListAdapter {
	
	private final SparseArray<TblObservaciones> Grupos;
	public LayoutInflater Inflater;
	public Context Context;
	
	public AdapterListaObservaciones(Context Context, SparseArray<TblObservaciones> Grupos) {
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
		final TblObservaciones Children = (TblObservaciones)getChild(groupPosition, childPosition);
		TextView textvw = null;		
		if (convertView == null) {
			convertView = Inflater.inflate(R.layout.adaptador_list_subitems, null);
		}
		
		textvw = (TextView)convertView.findViewById(R.id.txtSubItem);
		String Test1 = Context.getResources().getString(R.string.ChildrenObservacion)+" ";
		String Test2 = Children.getObservacion();
		
		SpannableString ssb1 = new SpannableString(Test1);
		SpannableString ssb2 = new SpannableString(Test2);
		
		ssb1.setSpan(new ForegroundColorSpan(Context.getResources().getColor(R.color.azulado)), 0, ssb1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		ssb1.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 0, ssb1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textvw.setText(ssb1);		
		textvw.append(ssb2);		
		
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
		TextView TxtItem = null;
		ImageView ImagenDU;		
		if (convertView == null) {
			convertView = Inflater.inflate(R.layout.adaptador_list_items, null);
		}
		
		TxtItem = (TextView)convertView.findViewById(R.id.txtItem);
		ImagenDU = (ImageView)convertView.findViewById(R.id.imagenDU);
		
		TblObservaciones Grupo = (TblObservaciones)getGroup(groupPosition);	
		
		Calendar calendario = new GregorianCalendar();
		calendario.set(Grupo.getAnio(), Grupo.getMes(), Grupo.getDia());    	
		String fecha = MetodosValidacionesExtras.CalcularLaFecha(Context, calendario.getTime());		
		String hora = String.format("%02d:%02d", Grupo.getHora(), Grupo.getMinutos());
			
		TxtItem.setText(fecha+"  "+hora);		
		
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