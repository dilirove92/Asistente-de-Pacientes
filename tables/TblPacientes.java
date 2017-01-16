package com.Notifications.patientssassistant.tables;


import com.Notifications.patientssassistant.R;
import android.util.Log;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;


public class TblPacientes extends SugarRecord<TblPacientes> {
	
	private Long IdPaciente;
	private String UsuarioP;
	private String CiP;
	private String NombreApellidoP;	
	private int Anio;
	private int Mes;
	private int Dia;
	private String EstadoCivilP;
	private String NivelEstudioP;
	private String MotivoIngresoP;
	private String TipoPacienteP;
	private int Edad;
	private String FotoP;
	private Boolean Eliminado;
	
	public TblPacientes() {super();}

	public TblPacientes(Long idPaciente, String usuarioP, String ciP, String nombreApellidoP, int anio, int mes, int dia, 
						String estadoCivilP, String nivelEstudioP, String motivoIngresoP, String tipoPacienteP, int edad, 
						String fotoP, Boolean eliminado) {
		super();		
		this.IdPaciente = idPaciente;		
		this.UsuarioP = usuarioP;
		this.CiP = ciP;
		this.NombreApellidoP = nombreApellidoP;		
		this.Anio = anio;
		this.Mes = mes;
		this.Dia = dia;
		this.EstadoCivilP = estadoCivilP;
		this.NivelEstudioP = nivelEstudioP;
		this.MotivoIngresoP = motivoIngresoP;
		this.TipoPacienteP = tipoPacienteP;
		this.Edad = edad;
		this.FotoP = fotoP;
		this.Eliminado = eliminado;
	}
	
	public void EliminarPorIdPacienteRegTblPacientes(Long idPaciente) {		
		try {
			//ELIMINAR ACTIVIDAD/PACIENTE POR ID_PACIENTE
			TblActividadPaciente lasActPac=new TblActividadPaciente();
			lasActPac.EliminarPorIdPacienteRegTblActividadPaciente(idPaciente);			
			//ELIMINAR PERMISO POR ID_PACIENTE
			TblPermisos losPer=new TblPermisos();
			losPer.EliminarPorIdPacienteRegTblPermisos(idPaciente);
			//ELIMINAR BUZON POR ID_PACIENTE
			TblBuzon elBuzon=new TblBuzon();
			elBuzon.EliminarPorIdPacienteRegTblBuzon(idPaciente);
			//ELIMINAR EVENTOS/PACIENTES POR ID_PACIENTE
			TblEventosPacientes losEvePac=new TblEventosPacientes();
			losEvePac.EliminarPorIdPacienteRegTblEventosPacientes(idPaciente);
			//ELIMINAR RUTINAS/PACIENTES POR ID_PACIENTE
			TblRutinasPacientes lasRutPac=new TblRutinasPacientes();
			lasRutPac.EliminarPorIdPacienteRegTblRutinasPacientes(idPaciente);			
			//ELIMINAR CONTROL/DIETA POR ID_PACIENTE
			TblControlDieta losConDie=new TblControlDieta();
			losConDie.EliminarPorIdPacienteRegTblControlDieta(idPaciente);
			//ELIMINAR OBSERVACIONES POR ID_PACIENTE
			TblObservaciones lasObs=new TblObservaciones();
			lasObs.EliminarPorIdPacienteRegTblObservaciones(idPaciente);
			//ELIMINAR FAMILIARES/PACIENTES POR ID_PACIENTE
			TblFamiliaresPacientes losFamPac=new TblFamiliaresPacientes();
			losFamPac.EliminarPorIdPacienteRegTblFamiliaresPacientes(idPaciente);			
			//ELIMINAR CONTROL/MEDICINA POR ID_PACIENTE
			TblControlMedicina losConMed=new TblControlMedicina();
			losConMed.EliminarPorIdPacienteRegTblControlMedicina(idPaciente);
			//ELIMINAR ESTADO DE SALUD POR ID_PACIENTE
			TblEstadoSalud losEstSal=new TblEstadoSalud();
			losEstSal.EliminarPorIdPacienteRegTblEstadoSalud(idPaciente);
			//ELIMINAR SEGUIMIENTO MEDICO POR ID_PACIENTE
			TblSeguimientoMedico losSegMed=new TblSeguimientoMedico();
			losSegMed.EliminarPorIdPacienteRegTblSeguimientoMedico(idPaciente);			
			//FINALMENTE SE ELIMINA EL PACIENTE
			TblPacientes elPaciente = Select.from(TblPacientes.class).where(Condition.prop("ID_PACIENTE").eq(idPaciente)).first();
			elPaciente.delete();			
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarPac), e.getMessage());
		}		
	}
	
	public Long getIdPaciente() {
		return IdPaciente;
	}

	public void setIdPaciente(Long idPaciente) {
		IdPaciente = idPaciente;
	}

	public String getUsuarioP() {
		return UsuarioP;
	}

	public void setUsuarioP(String usuarioP) {
		UsuarioP = usuarioP;
	}

	public String getCiP() {
		return CiP;
	}

	public void setCiP(String ciP) {
		CiP = ciP;
	}

	public String getNombreApellidoP() {
		return NombreApellidoP;
	}

	public void setNombreApellidoP(String nombreApellidoP) {
		NombreApellidoP = nombreApellidoP;
	}	

	public int getAnio() {
		return Anio;
	}

	public void setAnio(int anio) {
		Anio = anio;
	}

	public int getMes() {
		return Mes;
	}

	public void setMes(int mes) {
		Mes = mes;
	}

	public int getDia() {
		return Dia;
	}

	public void setDia(int dia) {
		Dia = dia;
	}

	public String getEstadoCivilP() {
		return EstadoCivilP;
	}

	public void setEstadoCivilP(String estadoCivilP) {
		EstadoCivilP = estadoCivilP;
	}

	public String getNivelEstudioP() {
		return NivelEstudioP;
	}

	public void setNivelEstudioP(String nivelEstudioP) {
		NivelEstudioP = nivelEstudioP;
	}

	public String getMotivoIngresoP() {
		return MotivoIngresoP;
	}

	public void setMotivoIngresoP(String motivoIngresoP) {
		MotivoIngresoP = motivoIngresoP;
	}

	public String getTipoPacienteP() {
		return TipoPacienteP;
	}

	public void setTipoPacienteP(String tipoPacienteP) {
		TipoPacienteP = tipoPacienteP;
	}

	public int getEdad() {
		return Edad;
	}

	public void setEdad(int edad) {
		Edad = edad;
	}

	public String getFotoP() {
		return FotoP;
	}

	public void setFotoP(String fotoP) {
		FotoP = fotoP;
	}
	
	public Boolean getEliminado() {
		return Eliminado;
	}

	public void setEliminado(Boolean eliminado) {
		Eliminado = eliminado;
	}

	public static void EliminarDatos(){
		TblPacientes.executeQuery("delete from "+TblPacientes.getTableName(TblPacientes.class));
		if(TblPacientes.count(TblPacientes.class)==0) {
			Log.i("TBLPACIENTES","------------->Eliminado");
		}else{
			Log.i("TBLPACIENTES","-------------> NOOOOOO Eliminado :'(");
		}
	}
}