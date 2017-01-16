package com.Notifications.patientssassistant.adapters;


import com.Notifications.patientssassistant.*;
import com.Notifications.patientssassistant.tables.*;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;


public class AdapterListaP extends BaseAdapter implements Filterable {
	
	private final Context context;
	private List<TblPacientes> pacientes;
	private List<TblPacientes> filtrados;
		
	public AdapterListaP(Context context, List<TblPacientes> pacientes) {
		super();
		this.context = context;
		this.pacientes = pacientes;
	}
	
	@Override
	public int getCount() {
		return pacientes.size();
	}
	
	@Override
	public Object getItem(int position) {
		return pacientes.get(position);
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
		
        if (pacientes.get(position).getFotoP().equals("")) {
        	holder.imagenFoto.setImageResource(R.drawable.user_foto);				
		}else{
			byte[] b = Base64.decode(pacientes.get(position).getFotoP(), Base64.DEFAULT);
			Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
			holder.imagenFoto.setImageBitmap(bitmap);				
		}
        holder.txtNombresListas.setText(pacientes.get(position).getNombreApellidoP());

        return convertView;
    }			
	
	static class ViewHolder{
		public ImageView imagenFoto;
		public TextView txtNombresListas;		
	}

    public final Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<TblPacientes> results = new ArrayList<TblPacientes>();                
                if (filtrados == null)
                    filtrados = pacientes;
                if (constraint != null) {
                    if (filtrados != null && filtrados.size() > 0) {
                        for (final TblPacientes g : filtrados) {
                            if (g.getNombreApellidoP().toLowerCase().contains(constraint.toString().toLowerCase()) || g.getNombreApellidoP().toUpperCase().contains(constraint.toString().toUpperCase()))
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
                pacientes = (ArrayList<TblPacientes>) results.values;
                notifyDataSetChanged();                
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


}