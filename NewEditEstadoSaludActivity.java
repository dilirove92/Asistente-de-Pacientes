package com.Notifications.patientssassistant;


import com.Notifications.patientssassistant.adapters.*;
import com.Notifications.patientssassistant.dialogos.*;
import com.Notifications.patientssassistant.fragments.*;
import com.Notifications.patientssassistant.asynctask.*;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class NewEditEstadoSaludActivity extends Activity {
					
	//VARIABLES PARA NUEVO REGISTRO
	private Long vIdPaciente;
	private String vNombreP;
	private String vFotoP;
	
    //VARIABLES PARA EDITAR REGISTRO 
	private String vTipoSangre;
	private String vFacultadMental;
	private Boolean vEnfermedad;
	private String vDescEnfermedad;
	private Boolean vCirugias;
	private String vDescCirugias;
	private Boolean vMedicamentos;
	private String vDescMedicamentos;		
	private Boolean vDiscapacidad;
	private String vTipoDiscapacidad;
	private String vGradoDiscapacidad;
	private String vImplementos;
	
	//VARIABLES QUE ALMACENAN LOS DATOS A GUARDAR	
	private String campo_tipoSangre;
	private String campo_facultadMental;
	private Boolean campo_enfermedad;
	private String campo_descEnfermedad;
	private Boolean campo_cirugias;
	private String campo_descCirugias;
	private Boolean campo_medicamentos;
	private String campo_descMedicamentos;
	private Boolean campo_discapacidad;
	private String campo_tipoDiscapacidad;
	private String campo_gradoDiscapacidad;
	private String campo_implementos;
		
	//VARIABLES DE LOS ELEMENTOS DE LA IU
	ImageView ImagenFotoP;
	TextView TxtNombreP;
	Spinner CmbNewTipoSangP;
	Spinner CmbNewFacuMentP;
	CheckedTextView ChckEnfermedades;
	EditText EdtNewEnfermedades;
	CheckedTextView ChckCirugias;
	EditText EdtNewCirugias;
	CheckedTextView ChckTomaMedi;
	EditText EdtNewTomaMedi;
	CheckedTextView ChckPadeDisc;
	Spinner CmbNewTipoDiscP;
	Spinner CmbNewGradoDiscP;
	Spinner CmbNewImplementosP;
	Button BtnNuevoEditar;
	
	//VARIABLES EXTRAS
	private Boolean banderaGuardar=false;
	private FragmentManager fragmentManager = getFragmentManager(); 
	
	
	public NewEditEstadoSaludActivity() { super(); }

	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nuevo_estado_salud);
		
		ImagenFotoP=(ImageView)findViewById(R.id.imagenFotoP);
		TxtNombreP=(TextView)findViewById(R.id.txtNombreP);
		CmbNewTipoSangP=(Spinner)findViewById(R.id.cmbNewTipoSangP);
		CmbNewFacuMentP=(Spinner)findViewById(R.id.cmbNewFacuMentP);
		ChckEnfermedades=(CheckedTextView)findViewById(R.id.chckEnfermedades);
		EdtNewEnfermedades=(EditText)findViewById(R.id.edtNewEnfermedades);
		ChckCirugias=(CheckedTextView)findViewById(R.id.chckCirugias);
		EdtNewCirugias=(EditText)findViewById(R.id.edtNewCirugias);
		ChckTomaMedi=(CheckedTextView)findViewById(R.id.chckTomaMedi);
		EdtNewTomaMedi=(EditText)findViewById(R.id.edtNewTomaMedi);
		ChckPadeDisc=(CheckedTextView)findViewById(R.id.chckPadeDisc);
		CmbNewTipoDiscP=(Spinner)findViewById(R.id.cmbNewTipoDiscP);
		CmbNewGradoDiscP=(Spinner)findViewById(R.id.cmbNewGradoDiscP);
		CmbNewImplementosP=(Spinner)findViewById(R.id.cmbNewImplementosP);
		BtnNuevoEditar=(Button)findViewById(R.id.btnNewGuardar);
		
		NuevoVsEditar();
	}
	
	//METODO QUE SERVIRA PARA IR A NUEVO O EDITAR REGISTRO DE PACIENTE
	public void NuevoVsEditar() {
		Boolean opcion1= TabPaciente2EsF.EsNuevo();	
		Boolean opcion2= TabPaciente2EsF.EsEditar();
		
		if (opcion1.equals(true)) {
			RecogerParametrosFragTabPacEs();
			CargarFotoNombrePaciente();			
			CargarOpcSpinnerTS();
			CargarOpcSpinnerFM();			
			BtnNuevoES();
		}
		if (opcion2.equals(true)) {
			RecogerParametrosFragTabPacEs2();
			CargarDatosEnLosElementos();
			SpinnersTouchTS();
			SpinnersTouchFM();			
			BtnEditarES();
		}
		ActDesCheckedTextView();
	}
	
	//----METODOS PARA UN NUEVO REGISTRO
	
	public void RecogerParametrosFragTabPacEs() {
		vIdPaciente = getIntent().getExtras().getLong("varIdPaciente");
		vNombreP = getIntent().getExtras().getString("varNombreP");
		vFotoP = getIntent().getExtras().getString("varFotoP");
	}
	
	public void CargarFotoNombrePaciente() {		
		if (vFotoP.equals("")) {
			ImagenFotoP.setImageResource(R.drawable.user_foto);				
		}else{
			byte[] b = Base64.decode(vFotoP, Base64.DEFAULT);
			Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
			ImagenFotoP.setImageBitmap(bitmap);
		}	
		TxtNombreP.setText(vNombreP);		    	
	}
	
	public void BtnNuevoES(){		
		BtnNuevoEditar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (estaConectado()) {
					try {
						banderaGuardar = true;
						ValidarDatos();
						//GUARDAR DATOS EN LA TABLA DE ESTADO DE SALUD
						if (banderaGuardar.equals(true)) {
							ATEstadoSalud newES = new ATEstadoSalud();
							newES.new InsertarEstadoSalud().execute(vIdPaciente.toString(), campo_tipoSangre, campo_facultadMental, campo_enfermedad.toString(), campo_descEnfermedad,
									campo_cirugias.toString(), campo_descCirugias, campo_medicamentos.toString(), campo_descMedicamentos, campo_discapacidad.toString(),
									campo_tipoDiscapacidad, campo_gradoDiscapacidad, campo_implementos, "false");

							LimpiarElementos();
							Toast.makeText(NewEditEstadoSaludActivity.this, R.string.GuardadoR, Toast.LENGTH_SHORT).show();
							Intent data = new Intent();
							setResult(RESULT_OK, data);
							finish();
						}
					} catch (Exception ex) {
						Toast.makeText(NewEditEstadoSaludActivity.this, getString(R.string.ErrorGuardar) + ex.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}
	
	//----METODOS PARA EDITAR UN REGISTRO
	
	public void RecogerParametrosFragTabPacEs2() {
		RecogerParametrosFragTabPacEs();	
		vTipoSangre = getIntent().getExtras().getString("varTipoSangre");
		vFacultadMental = getIntent().getExtras().getString("varFacultadMental");
		vEnfermedad = getIntent().getExtras().getBoolean("varEnfermedad");
		vDescEnfermedad = getIntent().getExtras().getString("varDescEnfermedad");
		vCirugias = getIntent().getExtras().getBoolean("varCirugias");
		vDescCirugias = getIntent().getExtras().getString("varDescCirugias");
		vMedicamentos = getIntent().getExtras().getBoolean("varMedicamentos");
		vDescMedicamentos = getIntent().getExtras().getString("varDescMedicamentos");		
		vDiscapacidad = getIntent().getExtras().getBoolean("varDiscapacidad");
		vTipoDiscapacidad = getIntent().getExtras().getString("varTipoDiscapacidad");
		vGradoDiscapacidad = getIntent().getExtras().getString("varGradoDiscapacidad");
		vImplementos = getIntent().getExtras().getString("varImplementos");		
	}
	
	public void CargarDatosEnLosElementos() {		
		CargarFotoNombrePaciente();
		ChckEnfermedades.setChecked(vEnfermedad);
		EdtNewEnfermedades.setText(vDescEnfermedad);		
		ChckCirugias.setChecked(vCirugias);
		EdtNewCirugias.setText(vDescCirugias);		
		ChckTomaMedi.setChecked(vMedicamentos);
		EdtNewTomaMedi.setText(vDescMedicamentos);		
		ChckPadeDisc.setChecked(vDiscapacidad);
		
		//CARGANDO DATOS GUARDADOS EN LOS SPINNERS
		String[] opcTipoSangP={vTipoSangre};
    	CmbNewTipoSangP.setAdapter(new AdapterSpinnerSimple(NewEditEstadoSaludActivity.this, R.layout.adaptador_spinner, opcTipoSangP));    	
    	String[] opcFacuMentP={vFacultadMental};
    	CmbNewFacuMentP.setAdapter(new AdapterSpinnerSimple(NewEditEstadoSaludActivity.this, R.layout.adaptador_spinner, opcFacuMentP));    	
    	String[] opcTipoDiscP={vTipoDiscapacidad};
    	CmbNewTipoDiscP.setAdapter(new AdapterSpinnerSimple(NewEditEstadoSaludActivity.this, R.layout.adaptador_spinner, opcTipoDiscP));    	
    	String[] opcGradoDiscP={vGradoDiscapacidad};
    	CmbNewGradoDiscP.setAdapter(new AdapterSpinnerSimple(NewEditEstadoSaludActivity.this, R.layout.adaptador_spinner, opcGradoDiscP));    	
    	String[] opcImplementosP={vImplementos};
    	CmbNewImplementosP.setAdapter(new AdapterSpinnerSimple(NewEditEstadoSaludActivity.this, R.layout.adaptador_spinner, opcImplementosP));

    	//ACTIVAR ELEMENTOS EN CASO DE QUE LOS CHECKBOX SEAN TRUE
    	if (ChckEnfermedades.isChecked()==true) {
			EdtNewEnfermedades.setEnabled(true);			
		}
    	if (ChckCirugias.isChecked()==true) {
			EdtNewCirugias.setEnabled(true);    		
		}
    	if (ChckTomaMedi.isChecked()==true) {
			EdtNewTomaMedi.setEnabled(true);    		
		}
    	if (ChckPadeDisc.isChecked()==true) {
			CmbNewTipoDiscP.setClickable(true);
			CmbNewGradoDiscP.setClickable(true);	
			CmbNewImplementosP.setClickable(true);	
			SpinnersTouchTD();
			SpinnersTouchGD();
			SpinnersTouchII();
		} 	    	
	}
	
	public void SpinnersTouchTS() {
		CmbNewTipoSangP.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (CmbNewTipoSangP.isPressed()) {
					CargarOpcSpinnerTS();
				}
				return false;
			}
		});			
	}
	
	public void SpinnersTouchFM() {
		CmbNewFacuMentP.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (CmbNewFacuMentP.isPressed()) {
					CargarOpcSpinnerFM();
				}
				return false;
			}
		});	
	}
	
	public void SpinnersTouchTD() {
		CmbNewTipoDiscP.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (CmbNewTipoDiscP.isPressed()) {
					if (ChckPadeDisc.isChecked()==true) {
						CargarOpcSpinnerTD();					
					}	
				}
				return false;
			}
		});
	}
	
	public void SpinnersTouchGD() {
		CmbNewGradoDiscP.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (CmbNewGradoDiscP.isPressed()) {
					if (ChckPadeDisc.isChecked()==true) {
						CargarOpcSpinnerGD();					
					}	
				}
				return false;
			}
		});	
	}
	
	public void SpinnersTouchII() {
		CmbNewImplementosP.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (CmbNewImplementosP.isPressed()) {
					if (ChckPadeDisc.isChecked()==true) {
						CargarOpcSpinnerII();					
					}	
				}
				return false;
			}
		});	
	}
	
	public void BtnEditarES(){		
		BtnNuevoEditar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (estaConectado()) {
					try {
						banderaGuardar = true;
						ValidarDatos();
						//EDITAR DATOS EN LA TABLA DE ESTADO DE SALUD
						if (banderaGuardar.equals(true)) {
							ATEstadoSalud newES = new ATEstadoSalud();
							newES.new ActualizarEstadoSalud().execute(vIdPaciente.toString(), campo_tipoSangre, campo_facultadMental, campo_enfermedad.toString(), campo_descEnfermedad,
									campo_cirugias.toString(), campo_descCirugias, campo_medicamentos.toString(), campo_descMedicamentos, campo_discapacidad.toString(),
									campo_tipoDiscapacidad, campo_gradoDiscapacidad, campo_implementos, "false");

							LimpiarElementos();
							Toast.makeText(NewEditEstadoSaludActivity.this, R.string.EditadoR, Toast.LENGTH_SHORT).show();
							Intent data = new Intent();
							setResult(RESULT_OK, data);
							finish();
						}
					} catch (Exception ex) {
						Toast.makeText(NewEditEstadoSaludActivity.this, getString(R.string.ErrorEditar) + ex.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}
	
	//----METODOS TANTO PARA NUEVO O EDITAR REGISTRO
		
	public Boolean ValidarDatos() {
		campo_tipoSangre=CmbNewTipoSangP.getSelectedItem().toString();
		campo_facultadMental=CmbNewFacuMentP.getSelectedItem().toString();
		campo_enfermedad=ChckEnfermedades.isChecked();
		campo_descEnfermedad=EdtNewEnfermedades.getText().toString().trim();
		campo_cirugias=ChckCirugias.isChecked();
		campo_descCirugias=EdtNewCirugias.getText().toString().trim();
		campo_medicamentos=ChckTomaMedi.isChecked();
		campo_descMedicamentos=EdtNewTomaMedi.getText().toString().trim();
		campo_discapacidad=ChckPadeDisc.isChecked();
				
		//VALIDAR SI SE INGRESA DATOS EN CASO DE ACTIVAR LOS CHECKBOX 
		if (campo_enfermedad.equals(true)) {
			if (campo_descEnfermedad.equals("")) {
				DFIngresarEnfermedades dialogo1 = new DFIngresarEnfermedades();
    	        dialogo1.show(fragmentManager, "tagAlerta");    	        
				banderaGuardar=false;
			}
		}					
		if (campo_cirugias.equals(true)) {
			if (campo_descCirugias.equals("")) {
				DFIngresarCirugias dialogo2 = new DFIngresarCirugias();
    	        dialogo2.show(fragmentManager, "tagAlerta");
    	        banderaGuardar=false;
			}						
		}					
		if (campo_medicamentos.equals(true)) {
			if (campo_descMedicamentos.equals("")) {
				DFIngresarMedicamentos dialogo3 = new DFIngresarMedicamentos();
    	        dialogo3.show(fragmentManager, "tagAlerta");
    	        banderaGuardar=false;
			}						
		}		
		//ENVIAR "" EN CASO DE QUE EL CHECKBOX DISCAPACIDAD SEA FALSE
		if (campo_discapacidad.equals(true)) {
			campo_tipoDiscapacidad=CmbNewTipoDiscP.getSelectedItem().toString();
			campo_gradoDiscapacidad=CmbNewGradoDiscP.getSelectedItem().toString();
			campo_implementos=CmbNewImplementosP.getSelectedItem().toString();
		}else{
			campo_tipoDiscapacidad="";
			campo_gradoDiscapacidad="";
			campo_implementos="";
		}		
		return banderaGuardar;
	}
	
	public void CargarOpcSpinnerTS() {
		String[] OpcTipoSangP=getResources().getStringArray(R.array.opc_tipoSangre);		
		CmbNewTipoSangP.setAdapter(new AdapterSpinnerSimple(NewEditEstadoSaludActivity.this, R.layout.adaptador_spinner, OpcTipoSangP));
	}
	
	public void CargarOpcSpinnerFM() {
		String[] OpcFacuMent=getResources().getStringArray(R.array.opc_facuMent);		
    	CmbNewFacuMentP.setAdapter(new AdapterSpinnerSimple(NewEditEstadoSaludActivity.this, R.layout.adaptador_spinner, OpcFacuMent));
	}
	
	public void CargarOpcSpinnerTD() {
		String[] OpcTipoDisc=getResources().getStringArray(R.array.opc_tipoDisc);		
    	CmbNewTipoDiscP.setAdapter(new AdapterSpinnerSimple(NewEditEstadoSaludActivity.this, R.layout.adaptador_spinner, OpcTipoDisc));    	
	}
	
	public void CargarOpcSpinnerGD() {
		String[] OpcGradoDisc=getResources().getStringArray(R.array.opc_gradoDisc);		
    	CmbNewGradoDiscP.setAdapter(new AdapterSpinnerSimple(NewEditEstadoSaludActivity.this, R.layout.adaptador_spinner, OpcGradoDisc));        	
	}
	
	public void CargarOpcSpinnerII() {
		String[] OpcImplem=getResources().getStringArray(R.array.opc_implem);		
    	CmbNewImplementosP.setAdapter(new AdapterSpinnerSimple(NewEditEstadoSaludActivity.this, R.layout.adaptador_spinner, OpcImplem));
	}	
	
    public void ActDesCheckedTextView() {
    	ChckEnfermedades.setClickable(true);
    	ChckEnfermedades.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				ChckEnfermedades.toggle();
				if (ChckEnfermedades.isChecked()==true) {
					EdtNewEnfermedades.setEnabled(true);					
				}else {
					EdtNewEnfermedades.getText().clear();
					EdtNewEnfermedades.setEnabled(false);
				}				
			}
		});    
    	ChckCirugias.setClickable(true); 
    	ChckCirugias.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				ChckCirugias.toggle();
				if (ChckCirugias.isChecked()==true) {
					EdtNewCirugias.setEnabled(true);					
				}else {
					EdtNewCirugias.getText().clear();
					EdtNewCirugias.setEnabled(false);
				}				
			}
		});	   
    	ChckTomaMedi.setClickable(true);
    	ChckTomaMedi.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				ChckTomaMedi.toggle();
				if (ChckTomaMedi.isChecked()==true) {
					EdtNewTomaMedi.setEnabled(true);					
				}else {
					EdtNewTomaMedi.getText().clear();
					EdtNewTomaMedi.setEnabled(false);
				}				
			}
		});
    	ChckPadeDisc.setClickable(true); 
    	ChckPadeDisc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ChckPadeDisc.toggle();
				if (ChckPadeDisc.isChecked() == true) {
					CmbNewTipoDiscP.setClickable(true);
					CmbNewGradoDiscP.setClickable(true);
					CmbNewImplementosP.setClickable(true);
					CargarOpcSpinnerTD();
					CargarOpcSpinnerGD();
					CargarOpcSpinnerII();
				} else {
					CmbNewTipoDiscP.setClickable(false);
					CmbNewGradoDiscP.setClickable(false);
					CmbNewImplementosP.setClickable(false);
					String[] opcTipoDiscP = {""};
					CmbNewTipoDiscP.setAdapter(new AdapterSpinnerSimple(NewEditEstadoSaludActivity.this, R.layout.adaptador_spinner, opcTipoDiscP));
					String[] opcGradoDiscP = {""};
					CmbNewGradoDiscP.setAdapter(new AdapterSpinnerSimple(NewEditEstadoSaludActivity.this, R.layout.adaptador_spinner, opcGradoDiscP));
					String[] opcImplementosP = {""};
					CmbNewImplementosP.setAdapter(new AdapterSpinnerSimple(NewEditEstadoSaludActivity.this, R.layout.adaptador_spinner, opcImplementosP));
				}
			}
		});  
	}
   
	public void LimpiarElementos() {
		ChckEnfermedades.setChecked(false);
		EdtNewEnfermedades.getText().clear();
		ChckCirugias.setChecked(false);
		EdtNewCirugias.getText().clear();
		ChckTomaMedi.setChecked(false);
		EdtNewTomaMedi.getText().clear();
		ChckPadeDisc.setChecked(false);
		CargarOpcSpinnerTS();
		CargarOpcSpinnerFM();
		CargarOpcSpinnerTD();
		CargarOpcSpinnerGD();
		CargarOpcSpinnerII();
	}

    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {	
    	if (keyCode == KeyEvent.KEYCODE_BACK) {
    		Intent data = new Intent();
			setResult(RESULT_OK, data);
			finish();
    		return true;    		
    	} 
		return super.onKeyDown(keyCode, event);
	}

	//VERIFICAR LA CONEXION DE INTERNET
	public Boolean estaConectado(){
		if(conectadoWifi()){
			return true;
		}else{
			if(conectadoRedMovil()){
				return true;
			}else{
				Toast.makeText(NewEditEstadoSaludActivity.this, "No hay Conexion a Internet", Toast.LENGTH_SHORT).show();
				return false;
			}
		}
	}

	//VERIFICAR CONEXION POR WIFI
	protected Boolean conectadoWifi(){
		ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (info != null) { if (info.isConnected()) { return true;} }
		}
		return false;
	}

	//VERIFICAR CONEXION POR DATOS MOVILES
	protected Boolean conectadoRedMovil(){
		ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (info != null) { if (info.isConnected()) { return true; } }
		}
		return false;
	}
}