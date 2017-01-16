package com.Notifications.patientssassistant.tables;


import com.Notifications.patientssassistant.R;
import android.util.Log;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;


public class TblCuidador extends SugarRecord<TblCuidador> {

	private Long IdCuidador;
	private String UsuarioC;
	private String NombreC;	
	private String CiRucC;	
	private String CelularC;
	private String TelefonoC;
	private String EmailC;
	private String DireccionC;	
	private String CargoC;
	private Boolean ControlTotal;
	private String FotoC;
	private Boolean Eliminado;
			
	public TblCuidador() {super();}

	public TblCuidador(Long idCuidador, String usuarioC, String nombreC, String ciRucC, String celularC,
					   String telefonoC,  String emailC, String direccionC, String cargoC, Boolean controlTotal, 
					   String fotoC, Boolean eliminado) {
		super();
		this.IdCuidador=idCuidador;
		this.UsuarioC = usuarioC;
		this.CiRucC = ciRucC;
		this.NombreC = nombreC;		
		this.CelularC = celularC;
		this.TelefonoC = telefonoC;
		this.DireccionC = direccionC;
		this.EmailC = emailC;
		this.CargoC = cargoC;
		this.ControlTotal = controlTotal;
		this.FotoC = fotoC;
		this.Eliminado = eliminado;
	}
	
	public void EliminarPorIdCuidadorRegTblCuidador(Long idCuidador) {		
		try {					
			//ELIMINAR HORARIOS POR ID_CUIDADOR
			TblHorarios losHor=new TblHorarios();
			losHor.EliminarPorIdCuidadorRegTblHorarios(idCuidador);			
			//ELIMINAR ACTIVIDAD/CUIDADOR POR ID_CUIDADOR
			TblActividadCuidador lasActCui=new TblActividadCuidador();
			lasActCui.EliminarPorIdCuidadorRegTblActividadCuidador(idCuidador);			
			//ELIMINAR EVENTOS/CUIDADOR POR ID_CUIDADOR
			TblEventosCuidadores losEveCui=new TblEventosCuidadores();
			losEveCui.EliminarPorIdCuidadorRegTblEventosCuidadores(idCuidador);
			//ELIMINAR RUTINAS/CUIDADORES POR ID_CUIDADOR
			TblRutinasCuidadores lasRutCui=new TblRutinasCuidadores();
			lasRutCui.EliminarPorIdCuidadorRegTblRutinasCuidadores(idCuidador);
			//ELIMINAR BUZON POR ID_CUIDADOR
			TblBuzon elBuzon=new TblBuzon();
			elBuzon.EliminarPorIdCuidadorRegTblBuzon(idCuidador);
			//ELIMINAR PERMISOS POR ID_CUIDADOR
			TblPermisos losPer=new TblPermisos();
			losPer.EliminarPorIdCuidadorRegTblPermisos(idCuidador);			
			//ELIMINAR CUIDADOR SECUNDARIO POR ID_CUIDADOR
			TblCuidadorS elCuiS=new TblCuidadorS();
			elCuiS.EliminarPorIdCuidadorRegTblCuidadorS(idCuidador);
			//FINALMENTE SE ELIMINA EL CUIDADOR
			TblCuidador elCuidador = Select.from(TblCuidador.class).where(Condition.prop("ID_CUIDADOR").eq(idCuidador)).first();
			elCuidador.delete();			
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarCui), e.getMessage());			
		}		
	}	

	public Long getIdCuidador() {
		return IdCuidador;
	}

	public void setIdCuidador(Long idCuidador) {
		IdCuidador = idCuidador;
	}

	public String getUsuarioC() {
		return UsuarioC;
	}

	public void setUsuarioC(String usuarioC) {
		UsuarioC = usuarioC;
	}

	public String getCiRucC() {
		return CiRucC;
	}

	public void setCiRucC(String ciRucC) {
		CiRucC = ciRucC;
	}

	public String getNombreC() {
		return NombreC;
	}

	public void setNombreC(String nombreC) {
		NombreC = nombreC;
	}

	public String getCelularC() {
		return CelularC;
	}

	public void setCelularC(String celularC) {
		CelularC = celularC;
	}

	public String getTelefonoC() {
		return TelefonoC;
	}

	public void setTelefonoC(String telefonoC) {
		TelefonoC = telefonoC;
	}

	public String getDireccionC() {
		return DireccionC;
	}

	public void setDireccionC(String direccionC) {
		DireccionC = direccionC;
	}

	public String getEmailC() {
		return EmailC;
	}

	public void setEmailC(String emailC) {
		EmailC = emailC;
	}

	public String getCargoC() {
		return CargoC;
	}

	public void setCargoC(String cargoC) {
		CargoC = cargoC;
	}

	public Boolean getControlTotal() {
		return ControlTotal;
	}

	public void setControlTotal(Boolean controlTotal) {
		ControlTotal = controlTotal;
	}	
	
	public String getFotoC() {
		return FotoC;
	}

	public void setFotoC(String fotoC) {
		FotoC = fotoC;
	}
	
	public Boolean getEliminado() {
		return Eliminado;
	}

	public void setEliminado(Boolean eliminado) {
		Eliminado = eliminado;
	}

	public static void EliminarDatos(){
		TblCuidador.executeQuery("delete from "+TblCuidador.getTableName(TblCuidador.class));
		if(TblCuidador.count(TblCuidador.class)==0) {
			Log.i("TBLCUIDADOR","------------->Eliminado");
		}else{
			Log.i("TBLCUIDADOR","-------------> NOOOOOO Eliminado :'(");
		}
	}
}