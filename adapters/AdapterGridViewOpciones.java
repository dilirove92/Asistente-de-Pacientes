package com.Notifications.patientssassistant.adapters;


import com.Notifications.patientssassistant.*;
import com.Notifications.patientssassistant.R;
import com.Notifications.patientssassistant.asynctask.*;
import com.Notifications.patientssassistant.fragments.*;
import com.Notifications.patientssassistant.tables.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.os.Handler;


public class AdapterGridViewOpciones extends BaseAdapter {

	private final Context context;
	private ArrayList<TblActividades> actPetPac ;
	private LayoutInflater mInflater;
	private int mItemHeight = 0;
	private int mNumColumns = 0;
	private RelativeLayout.LayoutParams mImageViewLayoutParams;
	private static final int WAKELOCK_TIMEOUT = 5000;


	public AdapterGridViewOpciones(Context context, ArrayList<TblActividades> actPetPac) {
		super();
		this.context = context;
		this.actPetPac = actPetPac;
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mImageViewLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}

	@Override
	public int getCount() {
		return actPetPac.size();
	}

	@Override
	public Object getItem(int position) {
		return actPetPac.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void setNumColumns(int numColumns) {
		this.mNumColumns = numColumns;
	}

	public int getNumColumns() {
		return mNumColumns;
	}

	public void setItemHeight(int height) {
		if (height == mItemHeight) {
			return;
		}
		mItemHeight = height;
		mImageViewLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, mItemHeight);
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertview, ViewGroup parent) {
		View v = convertview;
		ViewHolder vista = new ViewHolder();

		v = mInflater.inflate(R.layout.adaptador_gridview_opciones, parent, false);
		final TblActividades clase = actPetPac.get(position);

		vista.imagenIcono=(ImageView)v.findViewById(R.id.imageOpcion);
		vista.txtPeticion=(TextView)v.findViewById(R.id.txtOpcion);

		vista.imagenIcono.setLayoutParams(mImageViewLayoutParams);
		if (vista.imagenIcono.getLayoutParams().height != mItemHeight) {
			vista.imagenIcono.setLayoutParams(mImageViewLayoutParams);
		}

		if (clase.getImagenActividad().equals("")) {
			vista.imagenIcono.setImageResource(R.drawable.picture_peticion);
		}else{
			byte[] b = Base64.decode(clase.getImagenActividad(), Base64.DEFAULT);
			Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
			vista.imagenIcono.setImageBitmap(bitmap);
		}
		vista.txtPeticion.setText(clase.getNombreActividad().toUpperCase());
		CambiarColorTexto((TextView) v.findViewById(R.id.txtOpcion), false);

		v.setTag(Long.valueOf(clase.getIdActividad()));
		v.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				CambiarColorTexto((TextView) view.findViewById(R.id.txtOpcion), true);
				CambiarColorImagen((ImageView) view.findViewById(R.id.imageOpcion), true);
				final View vis = view;

				PantallaPacienteF met = new PantallaPacienteF();
				Long vDependeDe = met.vDependeDe();
				Long vIdPaciente = met.vIdPaciente();
				Long IdActividad = clase.getIdActividad();

				//FECHA Y HORA ACTUAL
				Calendar cal = new GregorianCalendar();
				cal.getTime();
				int anio = cal.get(Calendar.YEAR);
				int mes = cal.get(Calendar.MONTH);
				int dia = cal.get(Calendar.DAY_OF_MONTH);
				String anioS = String.valueOf(anio);
				String mesS = String.valueOf(mes);
				String diaS = String.valueOf(dia);
				if (mes < 10) { mesS = "0" + mesS; }
				if (dia < 10) { diaS = "0" + diaS; }
				String laFecha = anioS + "/" + mesS + "/" + diaS;

				int hora = cal.get(Calendar.HOUR_OF_DAY);
				int minutos = cal.get(Calendar.MINUTE);
				String horaS = String.valueOf(hora);
				String minutosS = String.valueOf(minutos);
				if (hora < 10) { horaS = "0" + horaS; }
				if (minutos < 10) { minutosS = "0" + minutosS; }
				String laHora = horaS + ":" + minutosS;

				ATBuzon buzon = new ATBuzon();
				buzon.new EnviarPeticion().execute(String.valueOf(vDependeDe), String.valueOf(vIdPaciente), String.valueOf(IdActividad),
													anioS, mesS, diaS, horaS, minutosS, String.valueOf(false));

				System.out.println(vDependeDe + "\n" + laFecha + " " + laHora + "\n" + vIdPaciente + "\n" + IdActividad);

				Runnable temporizador = new Runnable() {
					@Override
					public void run() {
						CambiarColorTexto((TextView) vis.findViewById(R.id.txtOpcion), false);
						CambiarColorImagen((ImageView) vis.findViewById(R.id.imageOpcion), false);
					}
				};
				new Handler().postDelayed(temporizador, WAKELOCK_TIMEOUT);

				Intent intent = new Intent(context, PeticionSolicitadaActivity.class);
				intent.putExtra("varImagen", clase.getImagenActividad());
				intent.putExtra("varNombre", clase.getNombreActividad());
				context.startActivity(intent);
			}
		});
		return v;
	}

	static class ViewHolder{
		public ImageView imagenIcono;
		public TextView txtPeticion;
	}

	public void CambiarColorTexto(TextView view, boolean isOn) {
		if (isOn) {
			view.setTextColor(Color.GREEN);
			view.setBackgroundColor(Color.BLUE);
		} else {
			view.setTextColor(Color.WHITE);
			view.setBackgroundColor(context.getResources().getColor(R.color.grid_state_pressed));
		}
	}

	public void CambiarColorImagen(ImageView view, boolean isOn) {
		if (isOn) {
			view.setBackgroundColor(Color.GREEN);
		} else {
			view.setBackgroundColor(Color.BLACK);
		}
	}


}