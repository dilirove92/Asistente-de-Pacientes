package com.Notifications.patientssassistant.adapters;


import com.Notifications.patientssassistant.*;
import com.Notifications.patientssassistant.R;
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


public class AdapterListaSeguimientoMedico extends BaseExpandableListAdapter {
	
	private final SparseArray<TblSeguimientoMedico> Grupos;
	public LayoutInflater Inflater;
	public Context Context;
	
	public AdapterListaSeguimientoMedico(Context Context, SparseArray<TblSeguimientoMedico> Grupos) {
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
		
		final TblSeguimientoMedico Children = (TblSeguimientoMedico)getChild(groupPosition, childPosition);
		
		textvw = (TextView)convertView.findViewById(R.id.txtSubItem);	
		String Test1 = Context.getResources().getString(R.string.ChildrenUnidMedi)+" ";
		String Test2 = Children.getUnidadMedica()+"\n";
		String Test3 = Context.getResources().getString(R.string.ChildrenDiagnostico)+" ";
		String Test4 = Children.getDiagnostico()+"\n";
		String Test5 = Context.getResources().getString(R.string.ChildrenTrataMedi)+" ";
		String Test6 = Children.getTratamientoMedico()+"\n";
		String Test7 = Context.getResources().getString(R.string.ChildrenAlimSuge)+" ";
		String Test8 = Children.getAlimentacionSugerida();	
		
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
		TextView TxtItem = null;
		ImageView ImagenDU;		
		if (convertView == null) {
			convertView = Inflater.inflate(R.layout.adaptador_list_items, null);
		}
		
		TxtItem = (TextView)convertView.findViewById(R.id.txtItem);
		ImagenDU = (ImageView)convertView.findViewById(R.id.imagenDU);
		
		TblSeguimientoMedico Grupo = (TblSeguimientoMedico)getGroup(groupPosition);
		
		Calendar calendario = new GregorianCalendar();
		calendario.set(Grupo.getAnio(), Grupo.getMes(), Grupo.getDia());    	
		String fecha = MetodosValidacionesExtras.CalcularLaFecha(Context, calendario.getTime());
			
		String Test1 = fecha+"\n";
		String Test2 = Grupo.getDoctor();
		
		SpannableString ssb1 = new SpannableString(Test1);
		SpannableString ssb2 = new SpannableString(Test2);
						
		ssb1.setSpan(new StyleSpan(Typeface.BOLD), 0, ssb1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		TxtItem.setText(ssb1);		
		TxtItem.append(ssb2);
				
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