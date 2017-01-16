package com.Notifications.patientssassistant.tables;


import com.Notifications.patientssassistant.*;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;


public class TblFamiliaresPacientes extends SugarRecord<TblFamiliaresPacientes> {
	
	private Long IdFamiliarPaciente;
	private Long IdPaciente;
	private String NombreContacto;
	private String CiContacto;
	private String Parentezco;
	private String Celular;
	private String Telefono;
	private String Direccion;
	private String Observacion;
	private Boolean EnviarReportes;
	private String FotoContacto;
	private String Email;
	private Boolean Eliminado;	
	private ArrayList<TblFamiliaresPacientes> FamiliaList = new ArrayList<TblFamiliaresPacientes>();
	
	
	public TblFamiliaresPacientes() { super(); }
	
	public TblFamiliaresPacientes(Long idFamiliarPaciente, Long idPaciente, String nombreContacto, String ciContacto, 
			                      String parentezco, String celular, String telefono, String direccion, String observacion, 
			                      Boolean enviarReportes, String fotoContacto, String email, Boolean eliminado) {
		super();
		this.IdFamiliarPaciente = idFamiliarPaciente;
		this.IdPaciente = idPaciente;
		this.NombreContacto = nombreContacto;
		this.CiContacto = ciContacto;
		this.Parentezco = parentezco;
		this.Celular = celular;
		this.Telefono = telefono;
		this.Direccion = direccion;
		this.Observacion = observacion;
		this.EnviarReportes = enviarReportes;
		this.FotoContacto = fotoContacto;
		this.Email = email;
		this.Eliminado = eliminado;
	}
	
	public TblFamiliaresPacientes(Long idFamiliarPaciente, String nombreContacto, String fotoContacto, ArrayList<TblFamiliaresPacientes> familiaList) {
		super();
		this.IdFamiliarPaciente = idFamiliarPaciente;
		this.NombreContacto = nombreContacto;
		this.FotoContacto = fotoContacto;
		this.FamiliaList = familiaList;
	}
	
	public TblFamiliaresPacientes(String ciContacto, String parentezco, String celular, String telefono, String direccion, 
							      String observacion, String email, Boolean enviarReportes) {
		super();
		this.CiContacto = ciContacto;
		this.Parentezco = parentezco;
		this.Celular = celular;
		this.Telefono = telefono;
		this.Direccion = direccion;
		this.Observacion = observacion;
		this.Email = email;
		this.EnviarReportes = enviarReportes;
	}
	
	public void EliminarPorIdFamiliarPacienteRegTblFamiliaresPacientes(Long idFamiliarPaciente) {		
		try {
			TblFamiliaresPacientes elFamPac = Select.from(TblFamiliaresPacientes.class).where(Condition.prop("ID_FAMILIAR_PACIENTE").eq(idFamiliarPaciente)).first();
			elFamPac.delete();			
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarFamPac), e.getMessage());
		}		
	}
	
	public void EliminarPorIdPacienteRegTblFamiliaresPacientes(Long idPaciente) {
		try {
			//GENERAR UNA LISTA DE LOS FAMILIARES DEL PACIENTE A ELIMINAR POR ID_PACIENTE
			List<TblFamiliaresPacientes> elFamPac = Select.from(TblFamiliaresPacientes.class).where(Condition.prop("ID_PACIENTE").eq(idPaciente)).list();
			TblFamiliaresPacientes eliminar_elFamPac = new TblFamiliaresPacientes();
			for (int i = 0; i < elFamPac.size(); i++) {
				TblFamiliaresPacientes registro_elFamPac=elFamPac.get(i);
				//LLAMAR AL METODO "EliminarPorIdFamiliarPacienteRegTblFamiliaresPacientes" EL CUAL ELIMINA EL FAMILIAR DEL PACIENTE POR ID_FAMILIAR_PACIENTE
				eliminar_elFamPac.EliminarPorIdFamiliarPacienteRegTblFamiliaresPacientes(registro_elFamPac.getIdFamiliarPaciente());				
			}
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarFamsPac), e.getMessage());
		}
	}
	
	public Long getIdFamiliarPaciente() {
		return IdFamiliarPaciente;
	}
	
	public void setIdFamiliarPaciente(Long idFamiliarPaciente) {
		IdFamiliarPaciente = idFamiliarPaciente;
	}
	
	public Long getIdPaciente() {
		return IdPaciente;
	}
	
	public void setIdPaciente(Long idPaciente) {
		IdPaciente = idPaciente;
	}
	
	public String getNombreContacto() {
		return NombreContacto;
	}
	
	public void setNombreContacto(String nombreContacto) {
		NombreContacto = nombreContacto;
	}
	
	public String getCiContacto() {
		return CiContacto;
	}
	
	public void setCiContacto(String ciContacto) {
		CiContacto = ciContacto;
	}
	
	public String getParentezco() {
		return Parentezco;
	}
	
	public void setParentezco(String parentezco) {
		Parentezco = parentezco;
	}
	
	public String getCelular() {
		return Celular;
	}
	
	public void setCelular(String celular) {
		Celular = celular;
	}
	
	public String getTelefono() {
		return Telefono;
	}
	
	public void setTelefono(String telefono) {
		Telefono = telefono;
	}
	
	public String getDireccion() {
		return Direccion;
	}
	
	public void setDireccion(String direccion) {
		Direccion = direccion;
	}
	
	public String getObservacion() {
		return Observacion;
	}
	
	public void setObservacion(String observacion) {
		Observacion = observacion;
	}
	
	public Boolean getEnviarReportes() {
		return EnviarReportes;
	}
	
	public void setEnviarReportes(Boolean enviarReportes) {
		EnviarReportes = enviarReportes;
	}
	
	public String getFotoContacto() {
		return FotoContacto;
	}
	
	public void setFotoContacto(String fotoContacto) {
		FotoContacto = fotoContacto;
	}
	
	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public Boolean getEliminado() {
		return Eliminado;
	}

	public void setEliminado(Boolean eliminado) {
		Eliminado = eliminado;
	}

	public ArrayList<TblFamiliaresPacientes> getFamiliaList() {
		return FamiliaList;
	}

	public void setFamiliaList(ArrayList<TblFamiliaresPacientes> familiaList) {
		this.FamiliaList = familiaList;
	}

	public static void EliminarDatos(){
		TblFamiliaresPacientes.executeQuery("delete from "+TblFamiliaresPacientes.getTableName(TblFamiliaresPacientes.class));
		if(TblFamiliaresPacientes.count(TblFamiliaresPacientes.class)==0) {
			Log.i("TBLFAMILIARESPACIENTES","------------->Eliminado");
		}else{
			Log.i("TBLFAMILIARESPACIENTES","-------------> NOOOOOO Eliminado :'(");
		}
	}
}