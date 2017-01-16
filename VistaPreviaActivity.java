package com.Notifications.patientssassistant;


import com.Notifications.patientssassistant.adapters.*;
import com.Notifications.patientssassistant.tables.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.orm.query.Condition;
import com.orm.query.Select;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Display;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.Toast;


public class VistaPreviaActivity extends Activity {
		
	//VARIABLES AUXILIARES
	private static Long vIdPaciente;
	private AdapterVistaPrevia miAdapter;
	private ArrayList<TblActividades> list_acts = null;
	private int anchoPantalla, altoPantalla;
	private int nColumnas, nFilas;
	private int mPhotoSpacing;
	private int widthPhoto, heightPhoto;	
			
	//VARIABLES DE LOS ELEMENTOS DE LA IU
	GridView GridViewPet;
	
	
	public VistaPreviaActivity() {super();}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vista_gridview); 		
		
		GridViewPet = (GridView) findViewById(R.id.opcGrid);
		
        Object restore = getLastNonConfigurationInstance();        
        if (restore != null)
        {
        	list_acts = (ArrayList<TblActividades>) restore;  
        }
        else 
        {
        	RecogerParametrosFragMenPacPerP();
        	BuscarPeticiones();
        }
        
        //LISTA CON DATOS
		if(!list_acts.isEmpty()) {
			miAdapter = new AdapterVistaPrevia(VistaPreviaActivity.this, list_acts);
			GridViewPet.setAdapter(miAdapter);
			
			Collections.sort(list_acts, new Comparator<TblActividades>(){  //ORDENA LA LISTA
				@Override
				public int compare(TblActividades d1, TblActividades d2) {
					return d1.getNombreActividad().compareToIgnoreCase(d2.getNombreActividad());				
				}
	        });
		}	
		
		EsPortraitLanscape();
		CrearFilasColumnasGrid();
	}
	
	public void RecogerParametrosFragMenPacPerP() {	
		vIdPaciente = getIntent().getExtras().getLong("varIdPaciente");
	}
	
	public void BuscarPeticiones() {		
		try {
			List<TblActividadPaciente> list_actPac = Select.from(TblActividadPaciente.class).where(Condition.prop("id_paciente").eq(vIdPaciente),
			                                                            						   Condition.prop("Eliminado").eq(0)).list();
			TblTipoActividad tipoActividad = Select.from(TblTipoActividad.class).where(Condition.prop("tipo_actividad").eq("PETICION"),
																					   Condition.prop("Eliminado").eq(0)).first();
			list_acts=new ArrayList<TblActividades>();
			TblActividadPaciente registro=new TblActividadPaciente();			
			
			for (int i = 0; i < list_actPac.size(); i++) {				
				registro=list_actPac.get(i);
				
				TblActividades Acts = Select.from(TblActividades.class).where(Condition.prop("id_actividad").eq(registro.getIdActividad()),
																			  Condition.prop("id_tipo_actividad").eq(tipoActividad.getIdTipoActividad()),
																			  Condition.prop("Eliminado").eq(0)).first();
				if (Acts!=null) {					
					list_acts.add(Acts);					
				}								
			}						
		} catch (Exception ex) {
			Toast.makeText(VistaPreviaActivity.this, getString(R.string.Error) + ex.getMessage(), Toast.LENGTH_SHORT).show();
		}		
	}
	
    public void EsPortraitLanscape() {
		Configuration newConfig= Resources.getSystem().getConfiguration();		
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {	        
	        if (list_acts.size()==8) { nColumnas = 4; nFilas = 2; }
	        if (list_acts.size()==9) { nColumnas = 3; nFilas = 3; }
	        if (list_acts.size()>=10) { nColumnas = 4; nFilas = 3; }	        
	    } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){    	
	        if (list_acts.size()==8) { nColumnas = 2; nFilas = 4; }
	        if (list_acts.size()==9) { nColumnas = 3; nFilas = 3; }
	        if (list_acts.size()>=10) { nColumnas = 3; nFilas = 4; }
	    }
	}
	
	public void CrearFilasColumnasGrid() {
		mPhotoSpacing = getResources().getDimensionPixelSize(R.dimen.photo_spacing);
		
		//TAMAÑO DE LA PANTALLA ANCHO Y ALTO
		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		anchoPantalla = display.getWidth();
		altoPantalla = display.getHeight();
		
		GridViewPet.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				if (miAdapter.getNumColumns() == 0) {	
					widthPhoto = (anchoPantalla / nColumnas) - mPhotoSpacing;
					heightPhoto = (altoPantalla / nFilas) - mPhotoSpacing;					
					final int numColumns = (int) Math.floor(GridViewPet.getWidth() / (widthPhoto + mPhotoSpacing));
					miAdapter.setNumColumns(numColumns);						
					miAdapter.setItemHeight(heightPhoto);					
					GridViewPet.setColumnWidth(widthPhoto);
				}
			}
		});
	}
	
    @Override
    public Object onRetainNonConfigurationInstance()
    {    	   	
    	return list_acts;    	
    } 	
	
		
}