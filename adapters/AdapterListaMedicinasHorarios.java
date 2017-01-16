package com.Notifications.patientssassistant.adapters;


import com.Notifications.patientssassistant.R;
import com.Notifications.patientssassistant.tables.*;
import java.util.ArrayList;
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


public class AdapterListaMedicinasHorarios extends BaseExpandableListAdapter {
	
	private final SparseArray<TblControlMedicina> MedicinasList;
	private final Context Context;
	private LayoutInflater Inflater;	
	
	public AdapterListaMedicinasHorarios(Context Context, SparseArray<TblControlMedicina> medicinasList) {
		super();
		this.Context = Context;
		this.MedicinasList=medicinasList;		
		this.Inflater = ((Activity)Context).getLayoutInflater();
	}

	@Override
	public int getGroupCount() {
		return MedicinasList.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		ArrayList<TblHorarioMedicina> matriculasList = MedicinasList.get(groupPosition).getHorariosList();
		return matriculasList.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return MedicinasList.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		ArrayList<TblHorarioMedicina> matriculaList = MedicinasList.get(groupPosition).getHorariosList();
		return matriculaList.get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {
		TextView TxtItem = null;
		ImageView ImagenDU;		
		if (view == null) {
			view = Inflater.inflate(R.layout.adaptador_list_items, null);
		}	
		  
		TxtItem = (TextView)view.findViewById(R.id.txtItem);
		ImagenDU = (ImageView)view.findViewById(R.id.imagenDU);
		  
		TblControlMedicina Grupo = (TblControlMedicina)getGroup(groupPosition);
		
		String Test1 = Grupo.getMedicamento()+"\n";
		String Test2 = Grupo.getTiempo()+"\n";
		String Test3 = Context.getResources().getString(R.string.CDosis)+" ";
		String Test4 = Grupo.getDosis()+"\n";
		String Test5 = Context.getResources().getString(R.string.CNveces)+" ";
		String Test6 = Integer.toString(Grupo.getNdeVeces());		
		
		SpannableString ssb1 = new SpannableString(Test1);
		SpannableString ssb2 = new SpannableString(Test2);
		SpannableString ssb3 = new SpannableString(Test3);
		SpannableString ssb4 = new SpannableString(Test4);
		SpannableString ssb5 = new SpannableString(Test5);
		SpannableString ssb6 = new SpannableString(Test6);		
		
		ssb1.setSpan(new StyleSpan(Typeface.BOLD), 0, ssb1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		TxtItem.setText(ssb1);		
		TxtItem.append(ssb2);
		
		ssb3.setSpan(new ForegroundColorSpan(Context.getResources().getColor(R.color.azulado)), 0, ssb3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		TxtItem.append(ssb3);
		TxtItem.append(ssb4);
		
		ssb5.setSpan(new ForegroundColorSpan(Context.getResources().getColor(R.color.azulado)), 0, ssb5.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		TxtItem.append(ssb5);
		TxtItem.append(ssb6);		
		
		if (isExpanded) {
			ImagenDU.setImageResource(R.drawable.expand_up);
		}else{
			ImagenDU.setImageResource(R.drawable.expand_down);
		}
	  
		return view;	 
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
		final TblHorarioMedicina Children = (TblHorarioMedicina)getChild(groupPosition, childPosition);
		if (view == null) {
			view = Inflater.inflate(R.layout.adaptador_list_subitems3, null);
		}
		
		String lun,mar,mie,jue,vie,sab,dom,dias;	
		if ((Children.getLunes().equals(true))&&(Children.getMartes().equals(true))&&(Children.getMiercoles().equals(true))&&(Children.getJueves().equals(true))&&
			(Children.getViernes().equals(true))&&(Children.getSabado().equals(true))&&(Children.getDomingo().equals(true))) 
		{
			dias=Context.getResources().getString(R.string.TodosLosDias);
		}else{
			if(Children.getDomingo().equals(true)){dom=Context.getResources().getString(R.string.Dom)+", ";}else{dom="";}
			if(Children.getLunes().equals(true)){lun=Context.getResources().getString(R.string.Lun)+", ";}else{lun="";}
			if(Children.getMartes().equals(true)){mar=Context.getResources().getString(R.string.Mar)+", ";}else{mar="";}
			if(Children.getMiercoles().equals(true)){mie=Context.getResources().getString(R.string.Mie)+", ";}else{mie="";}
			if(Children.getJueves().equals(true)){jue=Context.getResources().getString(R.string.Jue)+", ";}else{jue="";}
			if(Children.getViernes().equals(true)){vie=Context.getResources().getString(R.string.Vie)+", ";}else{vie="";}
			if(Children.getSabado().equals(true)){sab=Context.getResources().getString(R.string.Sab)+", ";}else{sab="";}
			dias=dom+lun+mar+mie+jue+vie+sab;
			dias=dias.substring(0, dias.length()-2);
		}			
		
		TextView TxtSubItem = (TextView)view.findViewById(R.id.txtSubItem);		
		String Test1 = Context.getResources().getString(R.string.ChildrenHora)+" ";
		String Test2 = String.format("%02d:%02d", Children.getHora(), Children.getMinutos())+"\n";
		String Test3 = dias;
		
		SpannableString ssb1 = new SpannableString(Test1);
		SpannableString ssb2 = new SpannableString(Test2);
		SpannableString ssb3 = new SpannableString(Test3);
		
		ssb1.setSpan(new ForegroundColorSpan(Context.getResources().getColor(R.color.azulado)), 0, ssb1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		ssb1.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 0, ssb1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		TxtSubItem.setText(ssb1);		
		TxtSubItem.append(ssb2);
		TxtSubItem.append(ssb3);
		
		ToggleButton ToggAlarma = (ToggleButton)view.findViewById(R.id.toggAlarma);
		ToggAlarma.setChecked(Children.getActDesAlarma());
		
		return view;				
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	
}