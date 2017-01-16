package com.Notifications.patientssassistant.adapters;


import com.Notifications.patientssassistant.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class AdapterSpinnerSimple extends ArrayAdapter<String>{
	
	public Context context;
	public int recurso;
	public String[] datos;	

	public AdapterSpinnerSimple(Context context, int textViewResourceId, String[] objects) {
		super(context, textViewResourceId, objects);			
		this.context=context;
		this.recurso=textViewResourceId;
		this.datos=objects;
	}

	@Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
    	LayoutInflater inflater= ((Activity)context).getLayoutInflater(); 
	    View row=inflater.inflate(R.layout.adaptador_spinner, parent, false);	    
	    TextView txtOpcSel=(TextView)row.findViewById(R.id.txtOpcSel);
	    txtOpcSel.setText(datos[position]);	    
	    return row;
	}
 

}