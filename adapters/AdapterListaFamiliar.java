package com.Notifications.patientssassistant.adapters;


import com.Notifications.patientssassistant.R;
import com.Notifications.patientssassistant.tables.*;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;


public class AdapterListaFamiliar extends BaseExpandableListAdapter {
	
	public Context Context;
	private ArrayList<TblFamiliaresPacientes> FamiliaList;
	private ArrayList<TblFamiliaresPacientes> OriginalList;

	
	public AdapterListaFamiliar(Context Context, ArrayList<TblFamiliaresPacientes> familiaList) {
		this.Context = Context;
		this.FamiliaList = new ArrayList<TblFamiliaresPacientes>();	
		this.FamiliaList.addAll(familiaList);
		this.OriginalList = new ArrayList<TblFamiliaresPacientes>();	
		this.OriginalList.addAll(familiaList);
	}	
	
	@Override
	public int getGroupCount() {
		return FamiliaList.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		ArrayList<TblFamiliaresPacientes> subList = FamiliaList.get(groupPosition).getFamiliaList();
		return subList.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return FamiliaList.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		ArrayList<TblFamiliaresPacientes> subList = FamiliaList.get(groupPosition).getFamiliaList();
		return subList.get(childPosition);
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
	public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {		
		TextView textvw = null;
		CheckBox checkvw;		
		if (convertView == null) {
			LayoutInflater layoutInflater = (LayoutInflater)Context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.adaptador_list_subitems2, null);
		}
				
		final TblFamiliaresPacientes Children = (TblFamiliaresPacientes) getChild(groupPosition, childPosition);
		
		textvw = (TextView)convertView.findViewById(R.id.txtSubItem);
		String Test1 = Context.getResources().getString(R.string.ChildrenCi)+" ";
		String Test2 = Children.getCiContacto()+"\n";
		String Test3 = Context.getResources().getString(R.string.ChildrenParentesco)+" ";
		String Test4 = Children.getParentezco()+"\n";
		String Test5 = Context.getResources().getString(R.string.ChildrenCelular)+" ";
		String Test6 = Children.getCelular()+"\n";
		String Test7 = Context.getResources().getString(R.string.ChildrenTelefono)+" ";
		String Test8 = Children.getTelefono()+"\n";
		String Test9 = Context.getResources().getString(R.string.ChildrenDireccion)+" ";
		String Test10 = Children.getDireccion()+"\n";
		String Test11 = Context.getResources().getString(R.string.ChildrenCorreo)+" ";
		String Test12 = Children.getEmail()+"\n";
		String Test13 = Context.getResources().getString(R.string.ChildrenObservacion)+" ";
		String Test14 = Children.getObservacion();		
		
		SpannableString ssb1 = new SpannableString(Test1);
		SpannableString ssb2 = new SpannableString(Test2);
		SpannableString ssb3 = new SpannableString(Test3);
		SpannableString ssb4 = new SpannableString(Test4);
		SpannableString ssb5 = new SpannableString(Test5);
		SpannableString ssb6 = new SpannableString(Test6);
		SpannableString ssb7 = new SpannableString(Test7);
		SpannableString ssb8 = new SpannableString(Test8);
		SpannableString ssb9 = new SpannableString(Test9);
		SpannableString ssb10 = new SpannableString(Test10);
		SpannableString ssb11 = new SpannableString(Test11);
		SpannableString ssb12 = new SpannableString(Test12);		
		SpannableString ssb13 = new SpannableString(Test13);
		SpannableString ssb14 = new SpannableString(Test14);
		
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
		
		ssb9.setSpan(new ForegroundColorSpan(Context.getResources().getColor(R.color.azulado)), 0, ssb9.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		ssb9.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 0, ssb9.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textvw.append(ssb9);		
		textvw.append(ssb10);
		
		ssb11.setSpan(new ForegroundColorSpan(Context.getResources().getColor(R.color.azulado)), 0, ssb11.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		ssb11.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 0, ssb11.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textvw.append(ssb11);		
		textvw.append(ssb12);	
		
		ssb13.setSpan(new ForegroundColorSpan(Context.getResources().getColor(R.color.azulado)), 0, ssb13.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		ssb13.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 0, ssb13.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textvw.append(ssb13);		
		textvw.append(ssb14);			
		
		checkvw = (CheckBox)convertView.findViewById(R.id.chckSubItem);
		checkvw.setText(R.string.EnviarReportes);
		checkvw.setChecked(Children.getEnviarReportes());	
	
		return convertView;
	}
			
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		ImageView ImagenFoto;
		TextView TxtNombresListas = null;
		ImageView ImagenDU;
		
		if (convertView == null) {
			LayoutInflater layoutInflater = (LayoutInflater)Context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.adaptador_list_items2, null);
		}	
		
		ImagenFoto = (ImageView)convertView.findViewById(R.id.imagenFoto);
		TxtNombresListas = (TextView)convertView.findViewById(R.id.txtNombresListas);
		ImagenDU = (ImageView)convertView.findViewById(R.id.imagenDU);
				
		TblFamiliaresPacientes Grupo = (TblFamiliaresPacientes) getGroup(groupPosition);
					
		if (Grupo.getFotoContacto().equals("")) {
			ImagenFoto.setImageResource(R.drawable.user_foto);			
		}else {
			byte[] b = Base64.decode(Grupo.getFotoContacto(), Base64.DEFAULT);
			Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        	ImagenFoto.setImageBitmap(bitmap);        	
		}
			
		if (isExpanded) {
			ImagenDU.setImageResource(R.drawable.expand_up);
		}else{
			ImagenDU.setImageResource(R.drawable.expand_down);
		}
		
		TxtNombresListas.setText(Grupo.getNombreContacto());
		
		return convertView;
	}	

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	public void filterData(String query){
		Log.v("AdapterListaFamiliar", String.valueOf(FamiliaList.size()));
		FamiliaList.clear();
		   
		if(query.isEmpty()){
			FamiliaList.addAll(OriginalList);
		}else{		
			for (TblFamiliaresPacientes grupoF: OriginalList) {
				List<TblFamiliaresPacientes> childrenList = grupoF.getFamiliaList();
				ArrayList<TblFamiliaresPacientes> newList = new ArrayList<TblFamiliaresPacientes>();
				if (grupoF.getNombreContacto().toLowerCase().contains(query.toLowerCase()) || grupoF.getNombreContacto().toUpperCase().contains(query.toUpperCase())) {
					for (TblFamiliaresPacientes childrenF: childrenList) {
						newList.add(childrenF);
					}						
				}
				if (newList.size() > 0) {				
					TblFamiliaresPacientes nFamilia = new TblFamiliaresPacientes(grupoF.getIdFamiliarPaciente(), grupoF.getNombreContacto(), grupoF.getFotoContacto(), newList);
					FamiliaList.add(nFamilia);
				}
			}				
		}
		
		Log.v("AdapterListaFamiliar", String.valueOf(FamiliaList.size()));
		notifyDataSetChanged();		   
	}
	
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
	  
		 
}