package com.Notifications.patientssassistant.adapters;


import com.Notifications.patientssassistant.R;
import com.Notifications.patientssassistant.tables.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class AdapterListaC extends BaseAdapter implements Filterable {
		
	private final Context context;
	private List<TblCuidador> cuidadores;
	private List<TblCuidador> filtrados;
    
	
	public AdapterListaC(Context context, List<TblCuidador> cuidadores) {
		super();
		this.context = context;
		this.cuidadores = cuidadores;		
	} 

	@Override
	public int getCount() {
		return cuidadores.size();
	}
	
	@Override
	public Object getItem(int position) {
		return cuidadores.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null)
        {
            convertView=LayoutInflater.from(context).inflate(R.layout.adaptador_lista_cop, parent, false);
            holder=new ViewHolder();
            holder.imagenFoto=(ImageView) convertView.findViewById(R.id.imagenFoto);
            holder.txtNombresListas=(TextView) convertView.findViewById(R.id.txtNombresListas);
            convertView.setTag(holder);
        }
        else
        {
            holder=(ViewHolder) convertView.getTag();
        }
		
        if (cuidadores.get(position).getFotoC().equals("")) {
        	holder.imagenFoto.setImageResource(R.drawable.user_foto);				
		}else{
			byte[] b = Base64.decode(cuidadores.get(position).getFotoC(), Base64.DEFAULT);
			Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
			holder.imagenFoto.setImageBitmap(bitmap);				
		}
        holder.txtNombresListas.setText(cuidadores.get(position).getNombreC());

        return convertView;
    }		
	
	public class ViewHolder{
		public ImageView imagenFoto;
		public TextView txtNombresListas;
	}

    public final Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<TblCuidador> results = new ArrayList<TblCuidador>();                
                if (filtrados == null)
                    filtrados = cuidadores;
                if (constraint != null) {
                    if (filtrados != null && filtrados.size() > 0) {
                        for (final TblCuidador g : filtrados) {
                            if (g.getNombreC().toLowerCase().contains(constraint.toString().toLowerCase()) || g.getNombreC().toUpperCase().contains(constraint.toString().toUpperCase()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {            	
                cuidadores = (ArrayList<TblCuidador>) results.values;
                notifyDataSetChanged();                
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
	
	
}