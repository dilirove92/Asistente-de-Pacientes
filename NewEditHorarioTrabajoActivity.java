package com.Notifications.patientssassistant;


import com.Notifications.patientssassistant.adapters.*;
import com.Notifications.patientssassistant.dialogos.*;
import com.Notifications.patientssassistant.fragments.*;
import com.Notifications.patientssassistant.asynctask.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;


public class NewEditHorarioTrabajoActivity extends Activity {
	
	//VARIABLES PARA NUEVO REGISTRO
	private Long vIdCuidador;
	private String vNombres;
	private String vFoto;
	
	//VARIABLES PARA EDITAR REGISTRO
	private Long vIdHorario;
	private String vTipoHorario;
	private int vHoraIni;
    private int vMinutosIni;
    private int vHoraFin;
    private int vMinutosFin;
	private Boolean vDom;
	private Boolean vLun;
	private Boolean vMar;
	private Boolean vMie;
	private Boolean vJue;
	private Boolean vVie;
	private Boolean vSab;
	
	//VARIABLES PARA LOS TIMEPICKER
	private static SimpleDateFormat sdfTime =  new  SimpleDateFormat ("HH:mm");
	private int hoursI, minutesI;
	private int hoursF, minutesF;
	private TimePickerDialog timePickerDialog;
	
	//VARIABLES QUE ALMACENAN LOS DATOS A GUARDAR	 
	private String campo_tipoHorario;
	private int campo_horaIni=0;    		    		    		  		
	private int campo_minutosIni=0;
	private int campo_horaFin=0;  
	private int campo_minutosFin=0;
	private Boolean campo_domingo;
	private Boolean campo_lunes;
	private Boolean campo_martes;
	private Boolean campo_miercoles;
	private Boolean campo_jueves;
	private Boolean campo_viernes;
	private Boolean campo_sabados;
	
	//VARIABLES DE LOS ELEMENTOS DE LA IU
	ImageView ImagenFotoCu;
	TextView TxtNombreCu;
	Spinner CmbNewTipoHora;
	Button BtnNewHI;
	Button BtnNewHF;
	ToggleButton ToggleD;
	ToggleButton ToggleL;
	ToggleButton ToggleMa;
	ToggleButton ToggleMi;
	ToggleButton ToggleJ;
	ToggleButton ToggleV;
	ToggleButton ToggleS;
	Button BtnNuevoEditar;
	
	//VARIABLES EXTRAS
	private Boolean banderaGuardar=false;
	private FragmentManager fragmentManager = getFragmentManager(); 
			
	
	public NewEditHorarioTrabajoActivity() { super(); }

	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nuevo_horario_trabajo); 
		
		ImagenFotoCu=(ImageView)findViewById(R.id.imagenFotoCu);
		TxtNombreCu=(TextView)findViewById(R.id.txtNombreCu);
		CmbNewTipoHora=(Spinner)findViewById(R.id.cmbNewTipoHora);
		BtnNewHI=(Button)findViewById(R.id.btnNewHI);
		BtnNewHF=(Button)findViewById(R.id.btnNewHF);
		ToggleD=(ToggleButton)findViewById(R.id.toggleD);
		ToggleL=(ToggleButton)findViewById(R.id.toggleL);
		ToggleMa=(ToggleButton)findViewById(R.id.toggleMa);
		ToggleMi=(ToggleButton)findViewById(R.id.toggleMi);
		ToggleJ=(ToggleButton)findViewById(R.id.toggleJ);
		ToggleV=(ToggleButton)findViewById(R.id.toggleV);
		ToggleS=(ToggleButton)findViewById(R.id.toggleS);
		BtnNuevoEditar=(Button)findViewById(R.id.btnNewGuardar);
			
		NuevoVsEditar();		    	
	}
	
	//METODO QUE SERVIRA PARA IR A NUEVO O EDITAR REGISTRO DE HORARIO DE TRABAJO
	public void NuevoVsEditar() {		
		Boolean opcion1= TabCuidador2HtF.EsNuevo();
		Boolean opcion2= TabCuidador2HtF.EsEditar();
		
		if (opcion1.equals(true)) {
			NewEditHorarioTrabajoActivity.this.setTitle(R.string.NewHorario);
			RecogerParametrosFragTabCuiHt();
			CargarFotoNombreCuidador();
			CargarOpcSpinner();					
	 		BtnNuevoHT();
	 		//DATOS PARA EL TIMEPICKERDIALOG
			final Calendar calendar = Calendar.getInstance();
			hoursI = calendar.get(Calendar.HOUR);
			minutesI = calendar.get(Calendar.MINUTE);
			hoursF = calendar.get(Calendar.HOUR);
			minutesF = calendar.get(Calendar.MINUTE);	 		
		}
		if (opcion2.equals(true)) {
			NewEditHorarioTrabajoActivity.this.setTitle(R.string.EditHorario);
			RecogerParametrosFragTabCuiHt2();
			CargarDatosEnLosElementos();
			SpinnerTouch();			
			BtnEditarHt();
			//DATOS PARA EL TIMEPICKERDIALOG			
			hoursI = vHoraIni;
			minutesI = vMinutosIni;
			hoursF = vHoraFin;
			minutesF = vMinutosFin;
		}
		BtnsTimePickerHIF();		
	}
	
	//----METODOS PARA UN NUEVO REGISTRO
	
	public void RecogerParametrosFragTabCuiHt() {
		vIdCuidador = getIntent().getExtras().getLong("varItemIdCuidador");
		vFoto = getIntent().getExtras().getString("varFotoCuidador");
		vNombres = getIntent().getExtras().getString("varNombreCuidador");
	}
	
	public void CargarFotoNombreCuidador() {
		if (vFoto.equals("")) {
			ImagenFotoCu.setImageResource(R.drawable.user_foto);				
		}else{
			byte[] b = Base64.decode(vFoto, Base64.DEFAULT);
			Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        	ImagenFotoCu.setImageBitmap(bitmap);
		} 	    	
    	TxtNombreCu.setText(vNombres);
	}
	
	public void BtnNuevoHT(){		
		BtnNuevoEditar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (estaConectado()) {
					try {
						VerificarDatos();
						if (banderaGuardar.equals(true)) {
							//GUARDAR DATOS EN LA TABLA DE HORARIOS DE TRABAJO
							ATHorarios newHorario = new ATHorarios();
							long idH = newHorario.new InsertarHorario().execute("0", vIdCuidador.toString(), campo_tipoHorario, String.valueOf(campo_horaIni), String.valueOf(campo_minutosIni),
									String.valueOf(campo_horaFin), String.valueOf(campo_minutosFin), campo_domingo.toString(), campo_lunes.toString(),
									campo_martes.toString(), campo_miercoles.toString(), campo_jueves.toString(), campo_viernes.toString(),
									campo_sabados.toString(), "false").get().longValue();

							LimpiarElementos();
							Toast.makeText(NewEditHorarioTrabajoActivity.this, R.string.GuardadoR, Toast.LENGTH_SHORT).show();
							Intent data = new Intent();
							setResult(RESULT_OK, data);
							finish();
						}
					} catch (Exception ex) {
						Toast.makeText(NewEditHorarioTrabajoActivity.this, getString(R.string.ErrorGuardar) + ex.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}
	
	//----METODOS PARA EDITAR UN REGISTRO	
	
	public void RecogerParametrosFragTabCuiHt2() {
		RecogerParametrosFragTabCuiHt();				
		vIdHorario = getIntent().getExtras().getLong("varIdHorario");
		vTipoHorario = getIntent().getExtras().getString("varTipoHorario");
		vHoraIni = getIntent().getExtras().getInt("varHoraIni");
		vMinutosIni = getIntent().getExtras().getInt("varMinutosIni");
		vHoraFin = getIntent().getExtras().getInt("varHoraFin");
		vMinutosFin = getIntent().getExtras().getInt("varMinutosFin");
		vDom = getIntent().getExtras().getBoolean("varDom");
		vLun = getIntent().getExtras().getBoolean("varLun");
		vMar = getIntent().getExtras().getBoolean("varMar");
		vMie = getIntent().getExtras().getBoolean("varMie");
		vJue = getIntent().getExtras().getBoolean("varJue");
		vVie = getIntent().getExtras().getBoolean("varVie");
		vSab = getIntent().getExtras().getBoolean("varSab");
	}
	
	public void CargarDatosEnLosElementos() {		
		CargarFotoNombreCuidador();  	
    	BtnNewHI.setText(String.format("%02d:%02d", vHoraIni, vMinutosIni));
    	BtnNewHF.setText(String.format("%02d:%02d", vHoraFin, vMinutosFin));
    	ToggleD.setChecked(vDom);
    	ToggleL.setChecked(vLun);
    	ToggleMa.setChecked(vMar);
    	ToggleMi.setChecked(vMie);
    	ToggleJ.setChecked(vJue);
    	ToggleV.setChecked(vVie);
    	ToggleS.setChecked(vSab);    	
    	//CARGANDO EL DATO GUARDADO EN EL SPINNER    	
    	String[] opcGuardada={vTipoHorario};
    	CmbNewTipoHora.setAdapter(new AdapterSpinnerSimple(NewEditHorarioTrabajoActivity.this, R.layout.adaptador_spinner, opcGuardada));
		campo_horaIni=vHoraIni;
		campo_minutosIni=vMinutosIni;
		campo_horaFin=vHoraFin;
		campo_minutosFin=vMinutosFin;
	}
	
	public void SpinnerTouch() {		
		CmbNewTipoHora.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (CmbNewTipoHora.isPressed()) {
					CargarOpcSpinner();
				}
				return false;
			}
		});			
	}
	
	public void BtnEditarHt(){		
		BtnNuevoEditar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (estaConectado()) {
					try {
						VerificarDatos();
						if (banderaGuardar.equals(true)) {
							//EDITANDO DATOS EN LA TABLA DE HORARIOS DE TRABAJO
							ATHorarios newHorario = new ATHorarios();
							newHorario.new ActualizarHorarios().execute(vIdHorario.toString(), vIdCuidador.toString(), campo_tipoHorario, String.valueOf(campo_horaIni), String.valueOf(campo_minutosIni),
									String.valueOf(campo_horaFin), String.valueOf(campo_minutosFin), campo_domingo.toString(), campo_lunes.toString(), campo_martes.toString(),
									campo_miercoles.toString(), campo_jueves.toString(), campo_viernes.toString(), campo_sabados.toString(), "false");

							LimpiarElementos();
							Toast.makeText(NewEditHorarioTrabajoActivity.this, R.string.EditadoR, Toast.LENGTH_SHORT).show();
							Intent data = new Intent();
							setResult(RESULT_OK, data);
							finish();
						}
					} catch (Exception ex) {
						Toast.makeText(NewEditHorarioTrabajoActivity.this, getString(R.string.ErrorEditar) + ex.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}	
	
	//----METODOS TANTO PARA NUEVO O EDITAR REGISTRO
	
	public Boolean VerificarDatos() {
		try {
			campo_tipoHorario=CmbNewTipoHora.getSelectedItem().toString();
			campo_domingo=ToggleD.isChecked();
			campo_lunes=ToggleL.isChecked();
			campo_martes=ToggleMa.isChecked();
			campo_miercoles=ToggleMi.isChecked();
			campo_jueves=ToggleJ.isChecked();
			campo_viernes=ToggleV.isChecked();
			campo_sabados=ToggleS.isChecked();
			
			if((campo_domingo.equals(false))&&(campo_lunes.equals(false))&&(campo_martes.equals(false))&&(campo_miercoles.equals(false))&&
			   (campo_jueves.equals(false))&&(campo_viernes.equals(false))&&(campo_sabados.equals(false)))		    		 
			{
				DFSeleccioneUnDia dialogo1 = new DFSeleccioneUnDia();
		        dialogo1.show(fragmentManager, "tagAlerta");
			}else {
				//VALIDAMOS QUE LA HORA FINAL SEA MAYOR A LA HORA INICIAL
				Date horaI=sdfTime.parse(BtnNewHI.getText().toString());
				Date horaF=sdfTime.parse(BtnNewHF.getText().toString());
				
				if (horaF.after(horaI)) {					
					banderaGuardar=true;		
				}else{
					DFValidarHoras dialogo2 = new DFValidarHoras();
	    	        dialogo2.show(fragmentManager, "tagAlerta");
				}
			}			
		} 
		catch (Exception e) { }
		return banderaGuardar;
	}
	
	public void CargarOpcSpinner() {
		String[] OpcTipoHorario=getResources().getStringArray(R.array.opc_tipoHorario);		
    	CmbNewTipoHora.setAdapter(new AdapterSpinnerSimple(NewEditHorarioTrabajoActivity.this, R.layout.adaptador_spinner, OpcTipoHorario));
	}
	
	public void BtnsTimePickerHIF() {       
 		//TIMEPICKER PARA HORA INICIO	       
        BtnNewHI.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) { DialogoTimePicker(); }
		});
 		//TIMEPICKER PARA HORA FIN
 		BtnNewHF.setOnClickListener(new OnClickListener() {			
 			@Override
 			public void onClick(View v) { DialogoTimePicker(); }
 		});	
	}
	
	public void DialogoTimePicker() {
		if (BtnNewHI.isPressed()) {
 			timePickerDialog = new TimePickerDialog(NewEditHorarioTrabajoActivity.this, onTimeSetListener1, hoursI, minutesI, true);
 			timePickerDialog.setTitle(R.string.HoraIni);
 			timePickerDialog.setIcon(R.drawable.horario);
 		}
		if (BtnNewHF.isPressed()) {
			timePickerDialog = new TimePickerDialog(NewEditHorarioTrabajoActivity.this, onTimeSetListener2, hoursF, minutesF, true);
			timePickerDialog.setTitle(R.string.HoraFin);
			timePickerDialog.setIcon(R.drawable.horario);
		}
		
		timePickerDialog.setCanceledOnTouchOutside(false);	
		
		timePickerDialog.setOnShowListener(new OnShowListener(){	
		    @Override
		    public void onShow(DialogInterface arg0) {  }});
	        
		timePickerDialog.setOnCancelListener(new OnCancelListener(){	
		    @Override
		   	public void onCancel(DialogInterface arg0) {  }});
	       
		timePickerDialog.setOnDismissListener(new OnDismissListener(){	
		    @Override
		    public void onDismiss(DialogInterface arg0) {  }});
 		
		timePickerDialog.show();
	}
	
	OnTimeSetListener onTimeSetListener1 = new OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			BtnNewHI.setText(String.format("%02d:%02d", hourOfDay, minute));	
			//DATOS PARA EL TIMEPICKERDIALOG
			hoursI=hourOfDay;  minutesI=minute;
			//DATOS PARA GUARDAR EN LA TABLA		
			campo_horaIni=hourOfDay;  campo_minutosIni=minute;  
		}
	};
	
	OnTimeSetListener onTimeSetListener2 = new OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			BtnNewHF.setText(String.format("%02d:%02d", hourOfDay, minute));	
			//DATOS PARA EL TIMEPICKERDIALOG
			hoursF=hourOfDay;  minutesF=minute;
			//DATOS PARA GUARDAR EN LA TABLA		
			campo_horaFin=hourOfDay;  campo_minutosFin=minute;  
			
		}
	};	
		
	public void LimpiarElementos() {			
		BtnNewHI.setText(R.string.FijarHora);
		BtnNewHF.setText(R.string.FijarHora);
		ToggleD.setChecked(false);
		ToggleL.setChecked(false);
		ToggleMa.setChecked(false);
		ToggleMi.setChecked(false);
		ToggleJ.setChecked(false);
		ToggleV.setChecked(false);
		ToggleS.setChecked(false);
		CargarOpcSpinner();
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
				Toast.makeText(NewEditHorarioTrabajoActivity.this, "No hay Conexion a Internet", Toast.LENGTH_SHORT).show();
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