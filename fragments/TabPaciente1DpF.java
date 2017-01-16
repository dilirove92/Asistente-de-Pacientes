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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class TabPaciente1DpF extends Fragment {
	
	public final static String KEY_REG_TEXT1 = "IdPaciente";
	public final static String KEY_REG_TEXT2 = "ControlT";	
	private static Long vItemIdPaciente;
	private static Boolean vControlT;
	private int request_code = 1;
	
	//VARIABLES AUXILIARES
	private FragmentIterationListener mCallback = null;	
	private static Boolean banderaEditar=false;	
	private TblPacientes paciente_seleccionado;			
	private Intent intent;
		
	//VARIABLES DE LOS ELEMENTOS DE LA IU
	ImageButton BtnEditar;
	ImageView ImagenFotoP;
	TextView TxtNombUsuaP;
	TextView TxtNombreApellidoP;
	TextView TxtCiP;
	TextView TxtFecNacP;
	TextView TxtEdad;
	TextView TxtEstaCiviP;
	TextView TxtNivEstuP;
	TextView TxtMotiIngrP;
	TextView TxtTipoPaciP;
	
	
	public interface FragmentIterationListener{
        public void onFragmentIteration(Bundle parameters);
    }
	
	public TabPaciente1DpF() {super();}
	
	public static TabPaciente1DpF newInstance(Long c_idPaciente, Boolean c_controlT) {
		TabPaciente1DpF frag = new TabPaciente1DpF();
		Bundle args = frag.getArguments();
		if (args == null)
			args = new Bundle();
		args.putLong(KEY_REG_TEXT1, c_idPaciente);
		args.putBoolean(KEY_REG_TEXT2, c_controlT);
		frag.setArguments(args);
		return frag;
	}
	
	public void RecogerParametrosFragTabPac1() {	
		vItemIdPaciente = getArguments().getLong(KEY_REG_TEXT1);
		vControlT = getArguments().getBoolean(KEY_REG_TEXT2);
	}
	
	public void EnviarParametrosFragTabPacDp() { 
		intent.putExtra("varIdPaciente", vItemIdPaciente);
		intent.putExtra("varUsuarioP", paciente_seleccionado.getUsuarioP());
		intent.putExtra("varNombreApellidoP", paciente_seleccionado.getNombreApellidoP());
		intent.putExtra("varCiP", paciente_seleccionado.getCiP());
		intent.putExtra("varAnio", paciente_seleccionado.getAnio());
		intent.putExtra("varMes", paciente_seleccionado.getMes());
		intent.putExtra("varDia", paciente_seleccionado.getDia());
		intent.putExtra("varEdad", paciente_seleccionado.getEdad());
		intent.putExtra("varEstadoCivilP", paciente_seleccionado.getEstadoCivilP()); 
		intent.putExtra("varNivelEstudioP", paciente_seleccionado.getNivelEstudioP());
		intent.putExtra("varMotivoIngresoP", paciente_seleccionado.getMotivoIngresoP());     		
		intent.putExtra("varTipoPacienteP", paciente_seleccionado.getTipoPacienteP());		
		intent.putExtra("varFotoP", paciente_seleccionado.getFotoP());
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
        				
        View rootView = inflater.inflate(R.layout.tab_paciente_dp, container, false);
        if(rootView != null){
        	BtnEditar=(ImageButton)rootView.findViewById(R.id.btnEditar);
        	ImagenFotoP=(ImageView)rootView.findViewById(R.id.imagenFotoP);
        	TxtNombUsuaP=(TextView)rootView.findViewById(R.id.txtNombUsuaP);
        	TxtNombreApellidoP=(TextView)rootView.findViewById(R.id.txtNombreApellidoP);
        	TxtCiP=(TextView)rootView.findViewById(R.id.txtCiP);
        	TxtFecNacP=(TextView)rootView.findViewById(R.id.txtFecNacP);
        	TxtEdad=(TextView)rootView.findViewById(R.id.txtEdad);
        	TxtEstaCiviP=(TextView)rootView.findViewById(R.id.txtEstaCiviP);
        	TxtNivEstuP=(TextView)rootView.findViewById(R.id.txtNivEstuP);
        	TxtMotiIngrP=(TextView)rootView.findViewById(R.id.txtMotiIngrP);
        	TxtTipoPaciP=(TextView)rootView.findViewById(R.id.txtTipoPaciP);
	    }
        return rootView;
    }		
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {		
		super.onViewCreated(view, savedInstanceState);
		
		RecogerParametrosFragTabPac1();
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
		//DESHABILITAR EL BOTON PARA EDITAR DATOS PERSONALES DEL PACIENTE
		if (vControlT.equals(false)) {
			BtnEditar.setEnabled(false);
			BtnEditar.setImageResource(R.drawable.edit2);
		}
	}
	
	public void CargarDatosEnElementos() {
		paciente_seleccionado = Select.from(TblPacientes.class).where(Condition.prop("id_paciente").eq(vItemIdPaciente)).first();
		
		if (paciente_seleccionado.getFotoP().equals("")) {
			ImagenFotoP.setImageResource(R.drawable.user_foto);				
		}else{
			byte[] b = Base64.decode(paciente_seleccionado.getFotoP(), Base64.DEFAULT);
			Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        	ImagenFotoP.setImageBitmap(bitmap);				
		}		
		TxtNombUsuaP.setText(paciente_seleccionado.getUsuarioP()) ;
    	TxtNombreApellidoP.setText(paciente_seleccionado.getNombreApellidoP()) ;
    	TxtCiP.setText(paciente_seleccionado.getCiP()) ;
		if(paciente_seleccionado.getDia()==0 && paciente_seleccionado.getMes()==0 && paciente_seleccionado.getAnio()==0){
			TxtFecNacP.setText(R.string.FijarFecha);
		}else{
			TxtFecNacP.setText(String.format("%02d/%02d/%02d", paciente_seleccionado.getDia(), paciente_seleccionado.getMes()+1, paciente_seleccionado.getAnio()));
		}
    	TxtEdad.setText(Integer.toString(paciente_seleccionado.getEdad())) ;
    	TxtEstaCiviP.setText(paciente_seleccionado.getEstadoCivilP()) ;
    	TxtNivEstuP.setText(paciente_seleccionado.getNivelEstudioP()) ;
    	TxtMotiIngrP.setText(paciente_seleccionado.getMotivoIngresoP()) ;
    	TxtTipoPaciP.setText(paciente_seleccionado.getTipoPacienteP()) ;
	}
	
	public void BtnEditarDP() {
    	//INICIA LA ACTIVIDAD PARA EDITAR
		BtnEditar.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				try {
					intent=new Intent(getActivity(), NewEditPacienteActivity.class);
					EnviarParametrosFragTabPacDp();
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