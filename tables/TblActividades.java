package com.Notifications.patientssassistant.tables;


import com.Notifications.patientssassistant.R;
import java.util.List;
import android.util.Log;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;


public class TblActividades extends SugarRecord<TblActividades> {
	
	private Long IdActividad;
	private Long IdTipoActividad;
	private String NombreActividad;
	private String DetalleActividad;
	private String ImagenActividad;
	private String TonoActividad;
	private Boolean Eliminado;
	
	public TblActividades() {super();}
	
	public TblActividades(Long idActividad, Long idTipoActividad, String nombreActividad, String detalleActividad,
						  String imagenActividad, String tonoActividad, Boolean eliminado) {
		super();
		this.IdActividad = idActividad;
		this.IdTipoActividad = idTipoActividad;
		this.NombreActividad = nombreActividad;
		this.DetalleActividad = detalleActividad;
		this.ImagenActividad = imagenActividad;
		this.TonoActividad = tonoActividad;
		this.Eliminado = eliminado;
	}
	
	public void EliminarPorIdActividadRegTblActividades(Long idActividad) {		
		try {
			//ELIMINAR BUZON POR ID_ACTIVIDAD
			TblBuzon elBuzon=new TblBuzon();
			elBuzon.EliminarPorIdActividadRegTblBuzon(idActividad);
			//ELIMINAR RUTINAS/CUIDADORES POR ID_ACTIVIDAD
			TblRutinasCuidadores lasRutCui=new TblRutinasCuidadores();
			lasRutCui.EliminarPorIdActividadRegTblRutinasCuidadores(idActividad);
			//ELIMINAR EVENTOS/CUIDADORES POR ID_ACTIVIDAD
			TblEventosCuidadores losEveCui=new TblEventosCuidadores();
			losEveCui.EliminarPorIdActividadRegTblEventosCuidadores(idActividad);
			//ELIMINAR ACTIVIDAD/CUIDADOR POR ID_ACTIVIDAD
			TblActividadCuidador lasActCui=new TblActividadCuidador();
			lasActCui.EliminarPorIdActividadRegTblActividadCuidador(idActividad);			
			//ELIMINAR ACTIVIDAD/PACIENTE POR ID_ACTIVIDAD
			TblActividadPaciente lasActPac=new TblActividadPaciente();
			lasActPac.EliminarPorIdActividadRegTblActividadPaciente(idActividad);
			//ELIMINAR EVENTOS/PACIENTES POR ID_ACTIVIDAD
			TblEventosPacientes losEvePac=new TblEventosPacientes();
			losEvePac.EliminarPorIdActividadRegTblEventosPacientes(idActividad);			
			//ELIMINAR RUTINAS/PACIENTES POR ID_ACTIVIDAD
			TblRutinasPacientes lasRutPac=new TblRutinasPacientes();
			lasRutPac.EliminarPorIdActividadRegTblRutinasPacientes(idActividad);			
			//FINALMENTE SE ELIMINA LA ACTIVIDAD
			TblActividades laActividad = Select.from(TblActividades.class).where(Condition.prop("ID_ACTIVIDAD").eq(idActividad)).first();
			laActividad.delete();			
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarAct), e.getMessage());
		}		
	}
	
	public void EliminarPorIdTipoActividadRegTblActividades(Long idTipoActividad) {
		try {
			//GENERAR UNA LISTA DE LAS ACTIVIDADES A ELIMINAR POR ID_TIPO_ACTIVIDAD
			List<TblActividades> laActividad = Select.from(TblActividades.class).where(Condition.prop("ID_TIPO_ACTIVIDAD").eq(idTipoActividad)).list();
			TblActividades eliminar_laActividad =new TblActividades();
			for (int i = 0; i < laActividad.size(); i++) {
				TblActividades registro_laActividad=laActividad.get(i);
				//LLAMAR AL METODO "EliminarPorIdActividadRegTblActividades" EL CUAL ELIMINA LA ACTIVIDAD POR ID_ACTIVIDAD
				eliminar_laActividad.EliminarPorIdActividadRegTblActividades(registro_laActividad.getIdActividad());
			}
		} catch (Exception e) {
			Log.e(String.valueOf(R.string.ErrorEliminarActs), e.getMessage());
		}
	}
	
	public Long getIdActividad() {
		return IdActividad;
	}
	
	public void setIdActividad(Long idActividad) {
		IdActividad = idActividad;
	}
	
	public Long getIdTipoActividad() {
		return IdTipoActividad;
	}
	
	public void setIdTipoActividad(Long idTipoActividad) {
		IdTipoActividad = idTipoActividad;
	}
	
	public String getNombreActividad() {
		return NombreActividad;
	}
	
	public void setNombreActividad(String nombreActividad) {
		NombreActividad = nombreActividad;
	}
	
	public String getDetalleActividad() {
		return DetalleActividad;
	}
	
	public void setDetalleActividad(String detalleActividad) {
		DetalleActividad = detalleActividad;
	}
	
	public String getImagenActividad() {
		return ImagenActividad;
	}
	
	public void setImagenActividad(String imagenActividad) {
		ImagenActividad = imagenActividad;
	}
		
	public String getTonoActividad() {
		return TonoActividad;
	}

	public void setTonoActividad(String tonoActividad) {
		TonoActividad = tonoActividad;
	}

	public Boolean getEliminado() {
		return Eliminado;
	}

	public void setEliminado(Boolean eliminado) {
		Eliminado = eliminado;
	}

	public static void EliminarDatos(){
		TblActividades.executeQuery("delete from "+TblActividades.getTableName(TblActividades.class));
		if(TblActividades.count(TblActividades.class)==0) {
			Log.i("TBLACTIVIDADES","------------->Eliminado");
		}else{
			Log.i("TBLACTIVIDADES","-------------> NOOOOOO Eliminado :'(");
		}
	}
	
}