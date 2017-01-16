package com.Notifications.patientssassistant.adapters;


import com.Notifications.patientssassistant.R;
import java.util.ArrayList;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class AdapterDrawerList extends BaseAdapter {
	
    private Activity activity;  
	ArrayList<ListaItemDrawer> arrayitms; 

	public AdapterDrawerList(Activity activity, ArrayList<ListaItemDrawer> listarry) {  
		super();  
		this.activity = activity;  
		this.arrayitms=listarry;
	}     
      
	@Override
	public Object getItem(int position) {       
		return arrayitms.get(position);
	}
   
	public int getCount() {
		return arrayitms.size();  
	}    
   
	@Override
	public long getItemId(int position) {
		return position;
	}   

	public static class Fila  
	{  
		TextView titulo_itm;
		ImageView icono;
	}  
   
	public View getView(int position, View convertView, ViewGroup parent) {		
		View v = convertView;
		Fila view = new Fila();
		
		LayoutInflater inflator = activity.getLayoutInflater();
		ListaItemDrawer itm=arrayitms.get(position);
		v = inflator.inflate(R.layout.adaptador_drawer_list, null);
       	
       	view.titulo_itm = (TextView)v.findViewById(R.id.txtTituloItem);           
       	view.titulo_itm.setText(itm.getTitulo());                  
       	view.icono = (ImageView)v.findViewById(R.id.imagenIconoMenu);
       	view.icono.setImageResource(itm.getIcono());           
       
       	v.setTag(view); 		  
		return v;  
	}	

   
}