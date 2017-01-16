package com.Notifications.patientssassistant.fragments;


import com.Notifications.patientssassistant.*;
import com.Notifications.patientssassistant.tables.*;
import com.orm.query.Condition;
import com.orm.query.Select;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class TabCuidador1DpF extends Fragment {
	
	public final static String KEY_REG_TEXT1 = "ItemIdCuidador";
	public final static String KEY_REG_TEXT2 = "IdCuidador";
	public final static String KEY_REG_TEXT3 = "TipoCuidador";	
	public final static String KEY_REG_TEXT4 = "ControlT";	
	private static Long vItemIdCuidador;
	private static Long vIdCuidador;
	private static String vTipoCuidador;
	private static Boolean vControlT;	
	private int request_code = 1;
	
	//VARIABLES AUXILIARES
	private FragmentIterationListener mCallback = null;
	private static Boolean banderaEditar=false;
	private TblCuidador cuidador_seleccionado;			
	private Intent intent;
		
	//VARIABLES DE LOS ELEMENTOS DE LA IU
	ImageButton BtnEditar;
	ImageView ImagenFotoC;
	TextView TxtNombUsuaC;
	TextView TxtNombres;
	TextView TxtCI;
	TextView TxtCelular;
	TextView TxtTelefono;
	TextView TxtCorreo;
	TextView TxtDomicilio;
	TextView TxtLabor;
	CheckedTextView ChkPermiso;
	
	
	public interface FragmentIterationListener{
        public void onFragmentIteration(Bundle parameters);
    }
	
	public TabCuidador1DpF() {super();}
	
	public static TabCuidador1DpF newInstance(Long c_itemIdCuidador, Long c_idCuidador, String c_tipoCuidador, Boolean c_controlT) {
		TabCuidador1DpF frag = new TabCuidador1DpF();
		Bundle args = frag.getArguments();
		if (args == null)
			args = new Bundle();
		args.putLong(KEY_REG_TEXT1, c_itemIdCuidador);
		args.putLong(KEY_REG_TEXT2, c_idCuidador);
		args.putString(KEY_REG_TEXT3, c_tipoCuidador);
		args.putBoolean(KEY_REG_TEXT4, c_controlT);
		frag.setArguments(args);
		return frag;
	}
	
	public void RecogerParametrosFragTabCui1yFragMpMiPefil1() {
		vItemIdCuidador = getArguments().getLong(KEY_REG_TEXT1);
		vIdCuidador = getArguments().getLong(KEY_REG_TEXT2);
		vTipoCuidador = getArguments().getString(KEY_REG_TEXT3);
		vControlT = getArguments().getBoolean(KEY_REG_TEXT4);
	}
	
	//DATOS PARA EDITAR CUIDADOR PRIMARIO
	public void EnviarParametrosFragTabCuiDp1() {
		EnviarParametrosFragTabCuiDp2();
		TblCuidadorPr CuiPr = Select.from(TblCuidadorPr.class).where(Condition.prop("id_cuidador").eq(vItemIdCuidador),
						    										 Condition.prop("Eliminado").eq(0)).first();
		intent.putExtra("varContrasenaC", CuiPr.getContrasena());
		intent.putExtra("varTipoResidenciaC", CuiPr.getTipoResidencia());
		intent.putExtra("varClaveVinculacionC", CuiPr.getPassVinculacion());
	}
	
	//DATOS PARA EDITAR CUIDADOR SECUNDARIO
	public void EnviarParametrosFragTabCuiDp2() {		
		intent.putExtra("varItemIdCuidador", vItemIdCuidador);
		intent.putExtra("varUsuarioC", cuidador_seleccionado.getUsuarioC());
		intent.putExtra("varNombreC", cuidador_seleccionado.getNombreC());
		intent.putExtra("varCiRucC", cuidador_seleccionado.getCiRucC());
		intent.putExtra("varCelularC", cuidador_seleccionado.getCelularC());
		intent.putExtra("varTelefonoC", cuidador_seleccionado.getTelefonoC()); 
		intent.putExtra("varEmailC", cuidador_seleccionado.getEmailC());
		intent.putExtra("varDireccionC", cuidador_seleccionado.getDireccionC());     		
		intent.putExtra("varCargoC", cuidador_seleccionado.getCargoC()); 
		intent.putExtra("varControlTotal", cuidador_seleccionado.getControlTotal());
		intent.putExtra("varFotoC", cuidador_seleccionado.getFotoC());
		intent.putExtra("varIdCuidador", vIdCuidador);
	}
	
	//METODO QUE RETORNA "Editar"
	public static Boolean EsEditar() {
		Boolean resultado=banderaEditar;		
		return resultado;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	    try{
	        mCallback = (FragmentIterationListener)activity;
	    }catch(Exception ex){
	        Log.e(String.valueOf(R.string.ExampleFragment), String.valueOf(R.string.FragmentoImplementarFIL));
	    }	
	}	

	@Override
	public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
	}	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { 
		super.onCreateView(inflater, container, savedInstanceState); 
        				
        View rootView = inflater.inflate(R.layout.tab_cuidador_dp, container, false);
        if(rootView != null){
        	BtnEditar=(ImageButton)rootView.findViewById(R.id.btnEditar);
        	ImagenFotoC=(ImageView)rootView.findViewById(R.id.imagenFotoC);
        	TxtNombUsuaC=(TextView)rootView.findViewById(R.id.txtNombUsuaC);
        	TxtNombres=(TextView)rootView.findViewById(R.id.txtNombres);
        	TxtCI=(TextView)rootView.findViewById(R.id.txtCI);
        	TxtCelular=(TextView)rootView.findViewById(R.id.txtCelular);
        	TxtTelefono=(TextView)rootView.findViewById(R.id.txtTelefono);
        	TxtCorreo=(TextView)rootView.findViewById(R.id.txtCorreo);
        	TxtDomicilio=(TextView)rootView.findViewById(R.id.txtDomicilio);
        	TxtLabor=(TextView)rootView.findViewById(R.id.txtLabor);
        	ChkPermiso=(CheckedTextView)rootView.findViewById(R.id.chkPermiso);  
	    }
        return rootView;
    }		
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {		
		super.onViewCreated(view, savedInstanceState);
				
		RecogerParametrosFragTabCui1yFragMpMiPefil1();
		CargarDatosEnElementos();
		BtnEditarDP();
		ActDesElementos();
	}
	
	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {		
		super.onViewStateRestored(savedInstanceState);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {		
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onDetach() {
		mCallback = null;
	    super.onDetach();
	}	
		
	public void ActDesElementos() {
		//DESHABILITAR EL BOTON PARA EDITAR DATOS PERSONALES DEL CUIDADOR
		if (vControlT.equals(false)) {
			BtnEditar.setEnabled(false);
			BtnEditar.setImageResource(R.drawable.edit2);
		}
	}
	
	public void CargarDatosEnElementos() {
		cuidador_seleccionado = Select.from(TblCuidador.class).where(Condition.prop("id_cuidador").eq(vItemIdCuidador)).first();
		
		if (cuidador_seleccionado.getFotoC().equals("")) {
			ImagenFotoC.setImageResource(R.drawable.user_foto);				
		}else{
			byte[] b = Base64.decode(cuidador_seleccionado.getFotoC(), Base64.DEFAULT);
			Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        	ImagenFotoC.setImageBitmap(bitmap);				
		}
		TxtNombUsuaC.setText(cuidador_seleccionado.getUsuarioC()) ;
		TxtNombres.setText(cuidador_seleccionado.getNombreC()) ;
		TxtCI.setText(cuidador_seleccionado.getCiRucC()) ;
		TxtCelular.setText(cuidador_seleccionado.getCelularC()) ;
		TxtTelefono.setText(cuidador_seleccionado.getTelefonoC()) ;
		TxtCorreo.setText(cuidador_seleccionado.getEmailC()) ;
		TxtDomicilio.setText(cuidador_seleccionado.getDireccionC()) ;
		TxtLabor.setText(cuidador_seleccionado.getCargoC()) ;
		ChkPermiso.setChecked(cuidador_seleccionado.getControlTotal());
	}
	
	public void BtnEditarDP() {
    	//INICIA LA ACTIVIDAD PARA EDITAR
		BtnEditar.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				try {
					if (vTipoCuidador.equals("CP")) {
						intent=new Intent(getActivity(), RegistrarAhoraActivity.class);
						EnviarParametrosFragTabCuiDp1();
					}
					if (vTipoCuidador.equals("CS")) {
						intent=new Intent(getActivity(), NewEditCuidadorActivity.class);
						EnviarParametrosFragTabCuiDp2();
					}
					startActivityForResult(intent, request_code);
					banderaEditar=true;
				} catch (Exception ex) {
					Toast.makeText(getActivity(), getString(R.string.Error) + ex.getMessage(), Toast.LENGTH_SHORT).show();
				}				
			}
		}); 
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if ((requestCode == request_code) && (resultCode == Activity.RESULT_OK)){
			banderaEditar=false;
			CargarDatosEnElementos();
		}
	}
	

}