package com.Notifications.patientssassistant.asynctask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.Notifications.patientssassistant.VarEstatic;
import com.Notifications.patientssassistant.tables.TblActividadCuidador;
import com.Notifications.patientssassistant.tables.TblActividadPaciente;
import com.Notifications.patientssassistant.tables.TblActividades;
import com.Notifications.patientssassistant.tables.TblBuzon;
import com.Notifications.patientssassistant.tables.TblControlDieta;
import com.Notifications.patientssassistant.tables.TblControlMedicina;
import com.Notifications.patientssassistant.tables.TblCuidador;
import com.Notifications.patientssassistant.tables.TblCuidadorPr;
import com.Notifications.patientssassistant.tables.TblCuidadorS;
import com.Notifications.patientssassistant.tables.TblEstadoSalud;
import com.Notifications.patientssassistant.tables.TblEventosCuidadores;
import com.Notifications.patientssassistant.tables.TblEventosPacientes;
import com.Notifications.patientssassistant.tables.TblFamiliaresPacientes;
import com.Notifications.patientssassistant.tables.TblHorarioMedicina;
import com.Notifications.patientssassistant.tables.TblHorarios;
import com.Notifications.patientssassistant.tables.TblObservaciones;
import com.Notifications.patientssassistant.tables.TblPacientes;
import com.Notifications.patientssassistant.tables.TblPermisos;
import com.Notifications.patientssassistant.tables.TblRutinasCuidadores;
import com.Notifications.patientssassistant.tables.TblRutinasPacientes;
import com.Notifications.patientssassistant.tables.TblSeguimientoMedico;
import com.Notifications.patientssassistant.tables.TblTipoActividad;

import android.os.AsyncTask;
import android.util.Log;

public class ATActualizar extends AsyncTask<String, Integer, Boolean>{

	private static String ip=VarEstatic.ObtenerIP();
	
	@Override
	protected Boolean doInBackground(String... params) {
		// TODO Auto-generated method stub
		
		String id=params[0];
		boolean resul=true;
		HttpClient httpClient;
		HttpGet BT;
		
		//********************** ACTIVIDADES **********************
		httpClient= new DefaultHttpClient();
		BT= new HttpGet ("http://"+ip+"/ADP/Actividades/ActividadBuscarActividades/"+id);
		BT.setHeader("content-type", "application/json");
		
		try {
			HttpResponse resp=httpClient.execute(BT);
			String respStr=EntityUtils.toString(resp.getEntity());
			
			JSONArray respJSON = new JSONArray(respStr);
			
			if (!respStr.isEmpty()) {
				
				TblActividades.deleteAll(TblActividades.class);
				
				for (int i = 0; i <respJSON.length(); i++) {
					JSONObject obj=respJSON.getJSONObject(i);
										
					TblActividades actividad=new TblActividades();
					actividad.setIdActividad(obj.getLong("IdActividad"));
					actividad.setIdTipoActividad(obj.getLong("IdTipoActividad"));
					actividad.setNombreActividad(obj.getString("NombreActividad"));
					actividad.setDetalleActividad(obj.getString("DetalleActividad"));
					actividad.setImagenActividad(obj.getString("ImagenActividad"));
					actividad.setTonoActividad(obj.getString("TonoActividad"));
					actividad.setEliminado(obj.getBoolean("Eliminado"));
					
					httpClient= new DefaultHttpClient();
					HttpGet BO= new HttpGet ("http://"+ip+"/ADP/Actividades/ActividadBuscarFotoActividad/"+id);
					BO.setHeader("content-type", "application/json");
					
					try {
						HttpResponse resp1=httpClient.execute(BO);
						String respStr1=EntityUtils.toString(resp1.getEntity());
						JSONObject respJSON1 = new JSONObject(respStr1);
						actividad.setImagenActividad(respJSON1.getString("ImagenActividad"));
						
					} catch (Exception ex) {
						// TODO: handle exception
						Log.e("Serviciorest","Error!",ex);
						resul=false;
					}

					TblActividades guardar_act = new TblActividades(
							actividad.getIdActividad(), actividad.getIdTipoActividad(),
							actividad.getNombreActividad(), actividad.getDetalleActividad(),
							actividad.getImagenActividad(), actividad.getTonoActividad(),
							actividad.getEliminado());
					guardar_act.save();
					
				}
			}
			
		} catch (Exception ex) {
			// TODO: handle exception
			Log.e("Serviciorest","Error!",ex);
			resul=false;
		}
		
		
		//*********************** ACTIVIDADES CUIDADORES ***************************
		httpClient= new DefaultHttpClient();
		BT= new HttpGet ("http://"+ip+"/ADP/ActividadCuidador/ActividadCuidadorBuscarAllCuidador/"+id);
		BT.setHeader("content-type", "application/json");
		
		try {
			HttpResponse resp=httpClient.execute(BT);
			String respStr=EntityUtils.toString(resp.getEntity());
			
			JSONArray respJSON = new JSONArray(respStr);
			
			
				for (int i = 0; i <respJSON.length(); i++) {
					JSONObject obj=respJSON.getJSONObject(i);
					
					TblActividadCuidador actCui= new TblActividadCuidador();
					actCui.setIdCuidador(obj.getLong("IdCuidador"));
					actCui.setIdActividad(obj.getLong("IdActividad"));
					actCui.setEliminado(obj.getBoolean("Eliminado"));
					
					TblActividadCuidador guardar_actCui = new TblActividadCuidador(
							actCui.getIdCuidador(), actCui.getIdActividad(), actCui.getEliminado());
					guardar_actCui.save();
				}
				
			
		} catch (Exception ex) {
			// TODO: handle exception
			Log.e("Serviciorest","Error!",ex);
			resul=false;
		}
		
		//********************** ACTIVIDADES PACIENTES ********************
		httpClient= new DefaultHttpClient();
		BT= new HttpGet (
				"http://"+ip+"/ADP/ActividadPaciente/ActividadPacientesBuscarAllXCuidador/"+id);
		BT.setHeader("content-type", "application/json");
		
		try {
			HttpResponse resp=httpClient.execute(BT);
			String respStr=EntityUtils.toString(resp.getEntity());
			
			JSONArray respJSON = new JSONArray(respStr);
			
			
				for (int i = 0; i < respJSON.length(); i++) {
					JSONObject obj = respJSON.getJSONObject(i);

					TblActividadPaciente actPac = new TblActividadPaciente();
					actPac.setIdPaciente(obj.getLong("IdPaciente"));
					actPac.setIdActividad(obj.getLong("IdActividad"));
					actPac.setEliminado(obj.getBoolean("Eliminado"));

					TblActividadPaciente guardar_actPac = new TblActividadPaciente(
							actPac.getIdPaciente(),
							actPac.getIdActividad(),
							actPac.getEliminado());
					guardar_actPac.save();
				}
			
		} catch (Exception ex) {
			// TODO: handle exception
			Log.e("Serviciorest","Error!",ex);
			resul=false;
		}
		
		//************************ BUZON ****************************
		httpClient= new DefaultHttpClient();
		BT= new HttpGet ("http://"+ip+"/ADP/Buzon/BuzonBuscarXCuidador/"+id);
		BT.setHeader("content-type", "application/json");
		
		try {
			HttpResponse resp=httpClient.execute(BT);
			String respStr=EntityUtils.toString(resp.getEntity());
			
			JSONArray respJSON = new JSONArray(respStr);
			
			for (int i = 0; i <respJSON.length(); i++) {
				JSONObject obj=respJSON.getJSONObject(i);
				
				//String dateString=obj.getString("Fecha").substring(6, 18);
				//long Date1 = Long.parseLong(dateString);
				//Date netDate = (new Date(Date1));
				
				TblBuzon buzon= new TblBuzon();
				buzon.setIdCuidador(obj.getLong("IdCuidador"));
				//buzon.setFecha(netDate);
				//buzon.setHora(obj.getString("Hora"));
				buzon.setIdPaciente(obj.getLong("IdPaciente"));
				buzon.setIdActividad(obj.getLong("IdActividad"));
				buzon.setAnio(obj.getInt("Anio"));
				buzon.setMes(obj.getInt("Mes"));
				buzon.setDia(obj.getInt("Dia"));
				buzon.setHoras(obj.getInt("Horas"));
				buzon.setMinutos(obj.getInt("Minutos"));
				buzon.setEliminado(obj.getBoolean("Eliminado"));
				
				TblBuzon guardar_buzon = new TblBuzon(
						//buzon.getIdCuidador(), buzon.getFecha(), buzon.getHora(),
						buzon.getIdCuidador(), buzon.getIdPaciente(),
						buzon.getIdActividad(), buzon.getAnio(),
						buzon.getMes(), buzon.getDia(), buzon.getHoras(),
						buzon.getMinutos(), buzon.getEliminado(), false);
				guardar_buzon.save();
			}
			
		} catch (Exception ex) {
			// TODO: handle exception
			Log.e("Serviciorest","Error!",ex);
			resul=false;
		}
		
		//********************************* CONTROL DIETA *******************************
		httpClient= new DefaultHttpClient();
		BT= new HttpGet ("http://"+ip+"/ADP/ControlDieta/ControlDietaBuscarAllXPaciente/"+id);
		BT.setHeader("content-type", "application/json");
		
		try {
			HttpResponse resp=httpClient.execute(BT);
			String respStr=EntityUtils.toString(resp.getEntity());
			
			JSONArray respJSON = new JSONArray(respStr);
			
			for (int i = 0; i <respJSON.length(); i++) {
				JSONObject obj=respJSON.getJSONObject(i);
									
				TblControlDieta act=new TblControlDieta();
				act.setIdControlDieta(obj.getLong("IdControlDieta"));
				act.setIdPaciente(obj.getLong("IdPaciente"));
				act.setMotivo(obj.getString("Motivo"));
				act.setAlimentosRecomendados(obj.getString("AlimentosRecomendados"));
				act.setAlimentosNoAdecuados(obj.getString("AlimentosNoAdecuados"));
				act.setEliminado(obj.getBoolean("Eliminado"));
				
				TblControlDieta guardar_ctDieta = new TblControlDieta(
						act.getIdControlDieta(), act.getIdPaciente(),
						act.getMotivo(), act.getAlimentosRecomendados(),
						act.getAlimentosNoAdecuados(), act.getEliminado());
				guardar_ctDieta.save();
				
			}
			
		} catch (Exception ex) {
			// TODO: handle exception
			Log.e("Serviciorest","Error!",ex);
			resul=false;
		}
		
		//****************************** CONTROL MEDICINA **************************
		httpClient= new DefaultHttpClient();
		BT= new HttpGet ("http://"+ip+"/ADP/ControlMedicina/ControlMedicinasBuscarAllXCuidadores/"+id);
		BT.setHeader("content-type", "application/json");
		TblControlMedicina contMedic= new TblControlMedicina();
		
		try {
			HttpResponse resp=httpClient.execute(BT);
			String respStr=EntityUtils.toString(resp.getEntity());
			
			JSONArray respJSON = new JSONArray(respStr);
			
			for (int i = 0; i <respJSON.length(); i++) {
				JSONObject obj=respJSON.getJSONObject(i);
									
				contMedic.setIdControlMedicina(obj.getLong("IdControlMedicina"));
				contMedic.setIdPaciente(obj.getLong("IdPaciente"));
				contMedic.setMedicamento(obj.getString("Medicamento"));
				contMedic.setDescripcion(obj.getString("Descripcion"));
				contMedic.setMotivoMedicacion(obj.getString("MotivoMedicacion"));
				contMedic.setTiempo(obj.getString("TipoTratamiento"));
				contMedic.setDosis(obj.getString("Dosis"));
				contMedic.setNdeVeces(obj.getInt("NdeVeces"));
				contMedic.setTono(obj.getString("Tono"));
				contMedic.setEliminado(obj.getBoolean("Eliminado"));
				
				TblControlMedicina guardar_CM = new TblControlMedicina(
						contMedic.getIdControlMedicina(), contMedic.getIdPaciente(),
						contMedic.getMedicamento(), contMedic.getDescripcion(),
						contMedic.getMotivoMedicacion(), contMedic.getTiempo(),
						contMedic.getDosis(), contMedic.getNdeVeces(),
						contMedic.getTono() ,contMedic.getEliminado());
				guardar_CM.save();
				
			}
			
		} catch (Exception ex) {
			// TODO: handle exception
			Log.e("Serviciorest","Error!",ex);
			resul=false;
		}
		
		//********************************* CUIDADORES ******************************
		httpClient= new DefaultHttpClient();
		BT= new HttpGet ("http://"+ip+"/ADP/Cuidador/CuidadorBuscarAllXCuidadores");
		BT.setHeader("content-type", "application/json");
		TblCuidador cuidador;
		
		try {
			HttpResponse resp=httpClient.execute(BT);
			String respStr=EntityUtils.toString(resp.getEntity());
			
			JSONArray respJSON = new JSONArray(respStr);
			
			for (int i = 0; i <respJSON.length(); i++) {
				JSONObject obj=respJSON.getJSONObject(i);
				
				cuidador = new TblCuidador();
				cuidador.setIdCuidador(obj.getLong("IdCuidador"));
				cuidador.setUsuarioC(obj.getString("UsuarioC"));
				cuidador.setNombreC(obj.getString("NombreC"));
				cuidador.setCiRucC(obj.getString("CiRucC"));
				cuidador.setCelularC(obj.getString("CelularC"));
				cuidador.setTelefonoC(obj.getString("TelefonoC"));
				cuidador.setEmailC(obj.getString("EmailC"));
				cuidador.setDireccionC(obj.getString("DireccionC"));
				cuidador.setCargoC(obj.getString("CargoC"));
				cuidador.setControlTotal(obj.getBoolean("ControlTotal"));
				cuidador.setFotoC(obj.getString("FotoC"));
				cuidador.setEliminado(obj.getBoolean("Eliminado"));
				
				httpClient= new DefaultHttpClient();
				HttpGet BO= new HttpGet ("http://"+ip+"/ADP/Cuidador/CuidadorBuscarFoto/"+id);
				BO.setHeader("content-type", "application/json");
				
				try {
					HttpResponse resp1=httpClient.execute(BO);
					String respStr1=EntityUtils.toString(resp1.getEntity());
					JSONObject respJSON1 = new JSONObject(respStr1);
					cuidador.setFotoC(respJSON1.getString("FotoC"));
					
				} catch (Exception ex) {
					// TODO: handle exception
					Log.e("Serviciorest","Error!",ex);
					resul=false;
				}				
				
				cuidador.save();
			}
			
		} catch (Exception ex) {
			// TODO: handle exception
			Log.e("Serviciorest","Error!",ex);
			resul=false;
		}
		
		//******************************* CUIDADORPR **************************
		httpClient= new DefaultHttpClient();
		BT= new HttpGet ("http://"+ip+"/ADP/CuidadorPr/CuidadorPrBuscarAll");
		BT.setHeader("content-type", "application/json");
		TblCuidadorPr cuidadorPr;
		
		try {
			HttpResponse resp=httpClient.execute(BT);
			String respStr=EntityUtils.toString(resp.getEntity());
			
			JSONArray respJSON = new JSONArray(respStr);
			
			for (int i = 0; i <respJSON.length(); i++) {
				JSONObject obj=respJSON.getJSONObject(i);
				
				cuidadorPr = new TblCuidadorPr();
				cuidadorPr.setIdCuidador(obj.getLong("IdCuidador"));
				cuidadorPr.setContrasena(obj.getString("Contrasena"));
				cuidadorPr.setTipoResidencia(obj.getString("TipoResidencia"));
				cuidadorPr.setPassVinculacion(obj.getString("PassVincular"));
				cuidadorPr.setEliminado(obj.getBoolean("Eliminado"));
				
				TblCuidadorPr guardar_Cuidador = new TblCuidadorPr(
						cuidadorPr.getIdCuidador(), cuidadorPr.getContrasena(),
						cuidadorPr.getTipoResidencia(), cuidadorPr.getPassVinculacion(),
						cuidadorPr.getEliminado());
				guardar_Cuidador.save();
				
			}
			
		} catch (Exception ex) {
			// TODO: handle exception
			Log.e("Serviciorest","Error!",ex);
			resul=false;
		}
		
		//******************************* CUIDADORS *****************************
		httpClient= new DefaultHttpClient();
		BT= new HttpGet ("http://"+ip+"/ADP/CuidadorS/CuidadoresSBuscarAllXCuidador/"+id);
		BT.setHeader("content-type", "application/json");
		TblCuidadorS cuidadorS;
		
		try {
			HttpResponse resp=httpClient.execute(BT);
			String respStr=EntityUtils.toString(resp.getEntity());
			
			JSONArray respJSON = new JSONArray(respStr);
			
			for (int i = 0; i <respJSON.length(); i++) {
				JSONObject obj=respJSON.getJSONObject(i);
				
				cuidadorS = new TblCuidadorS();
				cuidadorS.setIdCuidador(obj.getLong("IdCuidador"));
				cuidadorS.setDependeDe(obj.getLong("DependeDe"));
				cuidadorS.setEliminado(obj.getBoolean("Eliminado"));
				
				
				TblCuidadorS guardar_Cuidador = new TblCuidadorS(
						cuidadorS.getIdCuidador(), cuidadorS.getDependeDe(),
						cuidadorS.getEliminado());
				guardar_Cuidador.save();
				
			}
			
		} catch (Exception ex) {
			// TODO: handle exception
			Log.e("Serviciorest","Error!",ex);
			resul=false;
		}
		
		//*********************************** ESTADO DE SALUD **************************
		httpClient= new DefaultHttpClient();
		BT= new HttpGet ("http://"+ip+"/ADP/EstadoSalud/EstadosSaludBuscarXCuidadores/"+id);
		BT.setHeader("content-type", "application/json");
		TblEstadoSalud eSalud;
		
		try {
			HttpResponse resp=httpClient.execute(BT);
			String respStr=EntityUtils.toString(resp.getEntity());
			
			JSONArray respJSON = new JSONArray(respStr);
			
			for (int i = 0; i <respJSON.length(); i++) {
				JSONObject obj=respJSON.getJSONObject(i);
				
				eSalud = new TblEstadoSalud();
				eSalud.setIdPaciente(obj.getLong("IdPaciente"));
				eSalud.setTipoSangre(obj.getString("TipoSangre"));
				eSalud.setFacultadMental(obj.getString("FacultadMental"));
				eSalud.setEnfermedad(obj.getBoolean("Enfermedad"));
				eSalud.setDescEnfermedad(obj.getString("DescEnfermedad"));
				eSalud.setCirugias(obj.getBoolean("Cirugias"));
				eSalud.setDescCirugias(obj.getString("DescCirugias"));
				eSalud.setMedicamentos(obj.getBoolean("Medicamentos"));
				eSalud.setDescMedicamentos(obj.getString("DescMedicamentos"));
				eSalud.setDiscapacidad(obj.getBoolean("Discapacidad"));
				eSalud.setTipoDiscapacidad(obj.getString("TipoDiscapacidad"));
				eSalud.setGradoDiscapacidad(obj.getString("GradoDiscapacidad"));
				eSalud.setImplementos(obj.getString("Implementos"));
				eSalud.setEliminado(obj.getBoolean("Eliminado"));
				
				TblEstadoSalud guardar_EstSal = new TblEstadoSalud(
						eSalud.getIdPaciente(), eSalud.getTipoSangre(),
						eSalud.getFacultadMental(), eSalud.getEnfermedad(),
						eSalud.getDescEnfermedad(), eSalud.getCirugias(),
						eSalud.getDescCirugias(), eSalud.getMedicamentos(),
						eSalud.getDescMedicamentos(), eSalud.getDiscapacidad(),
						eSalud.getTipoDiscapacidad(), eSalud.getGradoDiscapacidad(),
						eSalud.getImplementos(), eSalud.getEliminado());
				guardar_EstSal.save();
				
			}
			
		} catch (Exception ex) {
			// TODO: handle exception
			Log.e("Serviciorest","Error!",ex);
			resul=false;
		}
		
		//******************************* EVENTOS CUIDADORES *********************************
		httpClient= new DefaultHttpClient();
		BT= new HttpGet ("http://"+ip+"/ADP/EventosCuidador/EventosCuidadorBuscarXCuidadores/"+id);
		BT.setHeader("content-type", "application/json");
		TblEventosCuidadores evenCuid;
		
		try {
			HttpResponse resp=httpClient.execute(BT);
			String respStr=EntityUtils.toString(resp.getEntity());
			
			JSONArray respJSON = new JSONArray(respStr);
			
			for (int i = 0; i <respJSON.length(); i++) {
				JSONObject obj=respJSON.getJSONObject(i);
									
				evenCuid = new TblEventosCuidadores();
				evenCuid.setIdEventoC(obj.getLong("IdEventoC"));
				evenCuid.setIdCuidador(obj.getLong("IdCuidador"));
				evenCuid.setIdActividad(obj.getLong("IdActividad"));
				evenCuid.setAnioE(obj.getInt("AnioE"));
				evenCuid.setMesE(obj.getInt("MesE"));
				evenCuid.setDiaE(obj.getInt("DiaE"));
				evenCuid.setHoraE(obj.getInt("HoraE"));
				evenCuid.setMinutosE(obj.getInt("MinutosE"));
				evenCuid.setAnioR(obj.getInt("AnioR"));
				evenCuid.setMesR(obj.getInt("MesR"));
				evenCuid.setDiaR(obj.getInt("DiaR"));
				evenCuid.setHoraR(obj.getInt("HoraR"));
				evenCuid.setMinutosR(obj.getInt("MinutosR"));
				evenCuid.setLugar(obj.getString("Lugar"));
				evenCuid.setDescripcion(obj.getString("Descripcion"));
				evenCuid.setTono(obj.getString("Tono"));
				evenCuid.setAlarma(obj.getBoolean("Alarma"));
				evenCuid.setEliminado(obj.getBoolean("Eliminado"));
				
				TblEventosCuidadores guardar_EC = new TblEventosCuidadores(
						evenCuid.getIdEventoC(), evenCuid.getIdCuidador(),
						evenCuid.getIdActividad(), evenCuid.getAnioE(),
						evenCuid.getMesE(), evenCuid.getDiaE(), evenCuid.getHoraE(),
						evenCuid.getMinutosE(), evenCuid.getAnioR(),evenCuid.getMesR(),
						evenCuid.getDiaE(), evenCuid.getHoraE(), evenCuid.getMinutosE(), 
						evenCuid.getLugar(), evenCuid.getDescripcion(), evenCuid.getTono(),
						evenCuid.getAlarma(), evenCuid.getEliminado());
				guardar_EC.save();
				
			}
			
		} catch (Exception ex) {
			// TODO: handle exception
			Log.e("Serviciorest","Error!",ex);
			resul=false;
		}
		
		//********************************* EVENTOS PACIENTES ****************************
		httpClient= new DefaultHttpClient();
		BT= new HttpGet ("http://"+ip+"/ADP/EventosPaciente/EventosPacienteBuscarXCuidadores/"+id);
		BT.setHeader("content-type", "application/json");
		TblEventosPacientes evenPac = new TblEventosPacientes();
		
		try {
			HttpResponse resp=httpClient.execute(BT);
			String respStr=EntityUtils.toString(resp.getEntity());
			
			JSONArray respJSON = new JSONArray(respStr);
			
			for (int i = 0; i <respJSON.length(); i++) {
				JSONObject obj=respJSON.getJSONObject(i);
				
				evenPac = new TblEventosPacientes();
				evenPac.setIdEventoP(obj.getLong("IdEventoP"));
				evenPac.setIdPaciente(obj.getLong("IdPaciente"));
				evenPac.setIdActividad(obj.getLong("IdActividad"));
				evenPac.setAnioE(obj.getInt("AnioE"));
				evenPac.setMesE(obj.getInt("MesE"));
				evenPac.setDiaE(obj.getInt("DiaE"));
				evenPac.setHoraE(obj.getInt("HoraE"));
				evenPac.setMinutosE(obj.getInt("MinutosE"));
				evenPac.setAnioR(obj.getInt("AnioR"));
				evenPac.setMesR(obj.getInt("MesR"));
				evenPac.setDiaR(obj.getInt("DiaR"));
				evenPac.setHoraR(obj.getInt("HoraR"));
				evenPac.setMinutosR(obj.getInt("MinutosR"));
				evenPac.setLugar(obj.getString("Lugar"));
				evenPac.setDescripcion(obj.getString("Descripcion"));
				evenPac.setTono(obj.getString("Tono"));
				evenPac.setAlarma(obj.getBoolean("Alarma"));
				evenPac.setEliminado(obj.getBoolean("Eliminado"));
				
				TblEventosPacientes guardar_EC = new TblEventosPacientes(
						evenPac.getIdEventoP(), evenPac.getIdPaciente(),
						evenPac.getIdActividad(), evenPac.getAnioE(),
						evenPac.getMesE(), evenPac.getDiaE(), evenPac.getHoraE(),
						evenPac.getMinutosE(), evenPac.getAnioR(),evenPac.getMesR(),
						evenPac.getDiaE(), evenPac.getHoraE(), evenPac.getMinutosE(), 
						evenPac.getLugar(), evenPac.getDescripcion(), evenPac.getTono(),
						evenPac.getAlarma(), evenPac.getEliminado());
				guardar_EC.save();
				
			}
			
		} catch (Exception ex) {
			// TODO: handle exception
			Log.e("Serviciorest","Error!",ex);
			resul=false;
		}
		
		//**************************** FAMILIARES PACIENTES ****************************
		httpClient= new DefaultHttpClient();
		BT= new HttpGet ("http://"+ip+"/ADP/FamiliaresPacientes/FamiliaresPacientesBuscarXCuidadores/"+id);
		BT.setHeader("content-type", "application/json");
		TblFamiliaresPacientes famPac = new TblFamiliaresPacientes();
		
		try {
			HttpResponse resp=httpClient.execute(BT);
			String respStr=EntityUtils.toString(resp.getEntity());
			
			JSONArray respJSON = new JSONArray(respStr);
			
			for (int i = 0; i <respJSON.length(); i++) {
				JSONObject obj=respJSON.getJSONObject(i);
				
				famPac.setIdFamiliarPaciente(obj.getLong("IdFamiliarPaciente"));
				famPac.setIdPaciente(obj.getLong("IdPaciente"));
				famPac.setNombreContacto(obj.getString("NombreContacto"));
				famPac.setCiContacto(obj.getString("CiContacto"));
				famPac.setParentezco(obj.getString("Parentezco"));
				famPac.setCelular(obj.getString("Celular"));
				famPac.setTelefono(obj.getString("Telefono"));
				famPac.setDireccion(obj.getString("Direccion"));
				famPac.setObservacion(obj.getString("Observacion"));
				famPac.setEnviarReportes(obj.getBoolean("EnviarReportes"));
				famPac.setFotoContacto(obj.getString("FotoContacto"));
				famPac.setEmail(obj.getString("Email"));
				famPac.setEliminado(obj.getBoolean("Eliminado"));
				
				HttpGet BO= new HttpGet ("http://"+ip+"/ADP/FamiliaresPacientes/FamiliarPacienteBuscarOneFoto/"+famPac.getIdFamiliarPaciente());
				BO.setHeader("content-type", "application/json");
				
				try {
					HttpResponse resp1=httpClient.execute(BO);
					String respStr1=EntityUtils.toString(resp1.getEntity());
					JSONObject respJSON1 = new JSONObject(respStr1);
					famPac.setFotoContacto(respJSON1.getString("FotoContacto"));
					
				} catch (Exception ex) {
					// TODO: handle exception
					Log.e("Serviciorest","Error!",ex);
					resul=false;
				}
				
				famPac.save();
				
			}
			
		} catch (Exception ex) {
			// TODO: handle exception
			Log.e("Serviciorest","Error!",ex);
			resul=false;
		}
		
		//****************************** HORARIOS MEDICINAS *********************************
		httpClient= new DefaultHttpClient();
		BT= new HttpGet ("http://"+ip+"/ADP/HorarioMedicinas/HorariosMedicinasBuscarXCuidadores/"+id);
		BT.setHeader("content-type", "application/json");
		TblHorarioMedicina horarioMed;
		
		try {
			HttpResponse resp=httpClient.execute(BT);
			String respStr=EntityUtils.toString(resp.getEntity());
			
			JSONArray respJSON = new JSONArray(respStr);
			
			for (int i = 0; i <respJSON.length(); i++) {
				JSONObject obj=respJSON.getJSONObject(i);
				
				horarioMed = new TblHorarioMedicina();
				horarioMed.setIdHorarioMedicina(obj.getLong("IdHorarioMedicina"));
				horarioMed.setIdControlMedicina(obj.getLong("IdControlMedicina"));
				horarioMed.setHora(obj.getInt("Hora"));
				horarioMed.setMinutos(obj.getInt("Minutos"));
				horarioMed.setDomingo(obj.getBoolean("Domingo"));
				horarioMed.setLunes(obj.getBoolean("Lunes"));
				horarioMed.setMartes(obj.getBoolean("Martes"));
				horarioMed.setMiercoles(obj.getBoolean("Miercoles"));
				horarioMed.setJueves(obj.getBoolean("Jueves"));
				horarioMed.setViernes(obj.getBoolean("Viernes"));
				horarioMed.setSabado(obj.getBoolean("Sabado"));
				horarioMed.setActDesAlarma(obj.getBoolean("ActDesAlarma"));
				horarioMed.setEliminado(obj.getBoolean("Eliminado"));
				
				TblHorarioMedicina guardar_Horario = new TblHorarioMedicina(
						horarioMed.getIdHorarioMedicina(), horarioMed.getIdControlMedicina(),
	    				horarioMed.getHora(), horarioMed.getMinutos(),
	    				horarioMed.getDomingo(), horarioMed.getLunes(),
	    				horarioMed.getMartes(), horarioMed.getMiercoles(),
	    				horarioMed.getJueves(), horarioMed.getViernes(),
	    				horarioMed.getSabado(), horarioMed.getActDesAlarma(),
	    				horarioMed.getEliminado());
				guardar_Horario.save();
				
			}
			
		} catch (Exception ex) {
			// TODO: handle exception
			Log.e("Serviciorest","Error!",ex);
			resul=false;
		}
		
		//*********************************** HORARIOS **********************************
		httpClient= new DefaultHttpClient();
		BT= new HttpGet ("http://"+ip+"/ADP/Horarios/HorariosBuscarXCuidadores/"+id);
		BT.setHeader("content-type", "application/json");
		TblHorarios horario;
		
		try {
			HttpResponse resp=httpClient.execute(BT);
			String respStr=EntityUtils.toString(resp.getEntity());
			
			JSONArray respJSON = new JSONArray(respStr);
			
			for (int i = 0; i <respJSON.length(); i++) {
				JSONObject obj=respJSON.getJSONObject(i);
				
				horario = new TblHorarios();
				horario.setIdHorario(obj.getLong("IdHorario"));
				horario.setIdCuidador(obj.getLong("IdCuidador"));
				horario.setTipoHorario(obj.getString("TipoHorario"));
				horario.setHoraIni(obj.getInt("HoraIni"));
				horario.setMinutosIni(obj.getInt("MinutosIni"));
				horario.setHoraFin(obj.getInt("HoraFin"));
				horario.setMinutosFin(obj.getInt("MinutosFin"));
				horario.setDomingo(obj.getBoolean("Domingo"));
				horario.setLunes(obj.getBoolean("Lunes"));
				horario.setMartes(obj.getBoolean("Martes"));
				horario.setMiercoles(obj.getBoolean("Miercoles"));
				horario.setJueves(obj.getBoolean("Jueves"));
				horario.setViernes(obj.getBoolean("Viernes"));
				horario.setSabado(obj.getBoolean("Sabado"));
				horario.setEliminado(obj.getBoolean("Eliminado"));
				
				TblHorarios guardar_Horario = new TblHorarios(
						horario.getIdHorario(), horario.getIdCuidador(),
						horario.getTipoHorario(), horario.getHoraIni(),
						horario.getMinutosIni(), horario.getHoraFin(), 
						horario.getMinutosFin(), horario.getDomingo(), 
						horario.getLunes(), horario.getMartes(), 
						horario.getMiercoles(), horario.getJueves(), 
						horario.getViernes(), horario.getSabado(), 
						horario.getEliminado());
				guardar_Horario.save();
				
			}
			
		} catch (Exception ex) {
			// TODO: handle exception
			Log.e("Serviciorest","Error!",ex);
			resul=false;
		}
		
		//********************************** OBSERVACIONES *****************************
		httpClient= new DefaultHttpClient();
		BT= new HttpGet ("http://"+ip+"/ADP/Observaciones/ObservacionesBuscarXCuidadores/"+id);
		BT.setHeader("content-type", "application/json");
		TblObservaciones observa;
		
		try {
			HttpResponse resp=httpClient.execute(BT);
			String respStr=EntityUtils.toString(resp.getEntity());
			
			JSONArray respJSON = new JSONArray(respStr);
			
			for (int i = 0; i <respJSON.length(); i++) {
				JSONObject obj=respJSON.getJSONObject(i);
						
				observa= new TblObservaciones();
				observa.setIdObservacion(obj.getLong("IdObservacion"));
				observa.setIdPaciente(obj.getLong("IdPaciente"));
				observa.setAnio(obj.getInt("Anio"));
				observa.setMes(obj.getInt("Mes"));
				observa.setDia(obj.getInt("Dia"));
				observa.setHora(obj.getInt("Hora"));
				observa.setMinutos(obj.getInt("Minutos"));
				observa.setObservacion(obj.getString("Observacion"));
				observa.setEliminado(obj.getBoolean("Eliminado"));
				
				TblObservaciones guardar_Ob = new TblObservaciones(
						observa.getIdObservacion(), observa.getIdPaciente(),
						observa.getAnio(), observa.getMes(), observa.getDia(), 
						observa.getHora(), observa.getMinutos(),
						observa.getObservacion(), observa.getEliminado());
				guardar_Ob.save();
				
			}
			
		} catch (Exception ex) {
			// TODO: handle exception
			Log.e("Serviciorest","Error!",ex);
			resul=false;
		}
		
		//************************************ PACIENTES **************************
		httpClient= new DefaultHttpClient();
		BT= new HttpGet ("http://"+ip+"/ADP/Pacientes/PacienteBuscarXCuidadores/"+id);
		BT.setHeader("content-type", "application/json");
		TblPacientes paciente;
		
		try {
			HttpResponse resp=httpClient.execute(BT);
			String respStr=EntityUtils.toString(resp.getEntity());
			
			JSONArray respJSON = new JSONArray(respStr);
			
			for (int i = 0; i <respJSON.length(); i++) {
				JSONObject obj=respJSON.getJSONObject(i);
				
				paciente = new TblPacientes();
				paciente.setIdPaciente(obj.getLong("IdPaciente"));
				paciente.setUsuarioP(obj.getString("UsuarioP"));
				paciente.setCiP(obj.getString("CiP"));
				paciente.setNombreApellidoP(obj.getString("NombreApellidoP"));
				paciente.setAnio(obj.getInt("Anio"));
				paciente.setMes(obj.getInt("Mes"));
				paciente.setDia(obj.getInt("Dia"));
				paciente.setEstadoCivilP(obj.getString("EstadoCivilP"));
				paciente.setNivelEstudioP(obj.getString("NivelEstudioP"));
				paciente.setMotivoIngresoP(obj.getString("MotivoIngresoP"));
				paciente.setTipoPacienteP(obj.getString("TipoPacienteP"));
				paciente.setEdad(obj.getInt("Edad"));
				paciente.setFotoP(obj.getString("FotoP"));
				paciente.setEliminado(obj.getBoolean("Eliminado"));
				
				httpClient= new DefaultHttpClient();
				HttpGet BO= new HttpGet (
						"http://"+ip+"/ADP/Pacientes/PacienteBuscarFoto/"+id);
				BO.setHeader("content-type", "application/json");
				
				try {
					HttpResponse resp1=httpClient.execute(BO);
					String respStr1=EntityUtils.toString(resp1.getEntity());
					JSONObject respJSON1 = new JSONObject(respStr1);
					paciente.setFotoP(respJSON1.getString("FotoP"));
					
				} catch (Exception ex) {
					// TODO: handle exception
					Log.e("Serviciorest","Error!",ex);
					resul=false;
				}
				
				TblPacientes guardar_Pac = new TblPacientes(
						paciente.getIdPaciente(), paciente.getUsuarioP(),
						paciente.getCiP(), paciente.getNombreApellidoP(),
						paciente.getAnio(), paciente.getMes(), paciente.getDia(),
						paciente.getEstadoCivilP(), paciente.getNivelEstudioP(), 
						paciente.getMotivoIngresoP(), paciente.getTipoPacienteP(), 
						paciente.getEdad(), paciente.getFotoP(), paciente.getEliminado());
				guardar_Pac.save();
			}
			
		} catch (Exception ex) {
			// TODO: handle exception
			Log.e("Serviciorest","Error!",ex);
			resul=false;
		}
		
		//************************************** PERMISOS ****************************
		httpClient= new DefaultHttpClient();
		BT= new HttpGet ("http://"+ip+"/ADP/Permisos/PermisosBuscarXCuidadores/"+id);
		BT.setHeader("content-type", "application/json");
		TblPermisos permiso;
		
		try {
			HttpResponse resp=httpClient.execute(BT);
			String respStr=EntityUtils.toString(resp.getEntity());
			
			JSONArray respJSON = new JSONArray(respStr);
			
			for (int i = 0; i <respJSON.length(); i++) {
				JSONObject obj=respJSON.getJSONObject(i);
				
				permiso = new TblPermisos();
				permiso.setIdPermiso(obj.getLong("IdPermiso"));
				permiso.setIdCuidador(obj.getLong("IdCuidador"));
				permiso.setIdPaciente(obj.getLong("IdPaciente"));
				permiso.setNotifiAlarma(obj.getBoolean("NotifiAlarma"));
				permiso.setRegCreador(obj.getBoolean("RegCreador"));
				permiso.setContMedicina(obj.getBoolean("ContMedicina"));
				permiso.setEliminado(obj.getBoolean("Eliminado"));
				
				TblPermisos guardar_permiso = new TblPermisos(
						permiso.getIdPermiso(), permiso.getIdCuidador(),
						permiso.getIdPaciente(), permiso.getNotifiAlarma(),
						permiso.getRegCreador(), permiso.getContMedicina(),
						permiso.getEliminado());
				guardar_permiso.save();
				
			}
			
		} catch (Exception ex) {
			// TODO: handle exception
			Log.e("Serviciorest","Error!",ex);
			resul=false;
		}
		
		//************************************ RUTINAS CUIDADORES ********************************
		httpClient= new DefaultHttpClient();
		BT= new HttpGet ("http://"+ip+"/ADP/RutinasCuidadores/RutinaCuidadorBuscarXCuidadores/"+id);
		BT.setHeader("content-type", "application/json");
		TblRutinasCuidadores rutina;
		
		try {
			HttpResponse resp=httpClient.execute(BT);
			String respStr=EntityUtils.toString(resp.getEntity());
			
			JSONArray respJSON = new JSONArray(respStr);
			
			for (int i = 0; i <respJSON.length(); i++) {
				JSONObject obj=respJSON.getJSONObject(i);
				
				rutina= new TblRutinasCuidadores();
				rutina.setIdRutinaC(obj.getLong("IdRutinaC"));
				rutina.setIdCuidador(obj.getLong("IdCuidador"));
				rutina.setIdActividad(obj.getLong("IdActividad"));
				rutina.setHora(obj.getInt("Hora"));
				rutina.setMinutos(obj.getInt("Minutos"));
				rutina.setDomingo(obj.getBoolean("Domingo"));
				rutina.setLunes(obj.getBoolean("Lunes"));
				rutina.setMartes(obj.getBoolean("Martes"));
				rutina.setMiercoles(obj.getBoolean("Miercoles"));
				rutina.setJueves(obj.getBoolean("Jueves"));
				rutina.setViernes(obj.getBoolean("Viernes"));
				rutina.setSabado(obj.getBoolean("Sabado"));
				rutina.setTono(obj.getString("Tono"));
				rutina.setDescripcion(obj.getString("Descripcion"));
				rutina.setAlarma(obj.getBoolean("Alarma"));
				rutina.setEliminado(obj.getBoolean("Eliminado"));
				
				TblRutinasCuidadores guardar_rutina = new TblRutinasCuidadores(
						rutina.getIdRutinaC(), rutina.getIdCuidador(),
						rutina.getIdActividad(), rutina.getHora(),
						rutina.getMinutos(), rutina.getDomingo(), 
						rutina.getLunes(), rutina.getMartes(),
						rutina.getMiercoles(), rutina.getJueves(), 
						rutina.getViernes(), rutina.getSabado(), 
						rutina.getTono(), rutina.getDescripcion(),
						rutina.getAlarma(), rutina.getEliminado());
				guardar_rutina.save();
				
			}
			
		} catch (Exception ex) {
			// TODO: handle exception
			Log.e("Serviciorest","Error!",ex);
			resul=false;
		}
		
		//********************************** RUTINAS PACIENTES **********************
		httpClient= new DefaultHttpClient();
		BT= new HttpGet ("http://"+ip+"/ADP/RutinasPacientes/RutinasPacientesBuscarXCuidadores/"+id);
		BT.setHeader("content-type", "application/json");
		TblRutinasPacientes rutinaP;
		
		try {
			HttpResponse resp=httpClient.execute(BT);
			String respStr=EntityUtils.toString(resp.getEntity());
			
			JSONArray respJSON = new JSONArray(respStr);
			
			for (int i = 0; i <respJSON.length(); i++) {
				JSONObject obj=respJSON.getJSONObject(i);
				
				rutinaP = new TblRutinasPacientes();
				rutinaP.setIdRutinaP(obj.getLong("IdRutinaP"));
				rutinaP.setIdPaciente(obj.getLong("IdPaciente"));
				rutinaP.setIdActividad(obj.getLong("IdActividad"));
				rutinaP.setHora(obj.getInt("Hora"));
				rutinaP.setMinutos(obj.getInt("Minutos"));
				rutinaP.setDomingo(obj.getBoolean("Domingo"));
				rutinaP.setLunes(obj.getBoolean("Lunes"));
				rutinaP.setMartes(obj.getBoolean("Martes"));
				rutinaP.setMiercoles(obj.getBoolean("Miercoles"));
				rutinaP.setJueves(obj.getBoolean("Jueves"));
				rutinaP.setViernes(obj.getBoolean("Viernes"));
				rutinaP.setSabado(obj.getBoolean("Sabado"));
				rutinaP.setTono(obj.getString("Tono"));
				rutinaP.setDescripcion(obj.getString("Descripcion"));
				rutinaP.setAlarma(obj.getBoolean("Alarma"));
				rutinaP.setEliminado(obj.getBoolean("Eliminado"));
				
				
				TblRutinasPacientes guardar_rutina = new TblRutinasPacientes(
						rutinaP.getIdPaciente(), rutinaP.getIdPaciente(),
						rutinaP.getIdActividad(), rutinaP.getHora(),
						rutinaP.getMinutos(), rutinaP.getDomingo(), 
						rutinaP.getLunes(), rutinaP.getMartes(),
						rutinaP.getMiercoles(), rutinaP.getJueves(), 
						rutinaP.getViernes(), rutinaP.getSabado(), 
						rutinaP.getTono(), rutinaP.getDescripcion(),
						rutinaP.getAlarma(), rutinaP.getEliminado());
				guardar_rutina.save();
				
			}
			
		} catch (Exception ex) {
			// TODO: handle exception
			Log.e("Serviciorest","Error!",ex);
			resul=false;
		}
		
		//*************************** SEGUIMIENTO MEDICO **********************
		httpClient= new DefaultHttpClient();
		BT= new HttpGet ("http://"+ip+"/ADP/SeguimientoMedico/SeguimientosMedicosBuscarXCuidadores/"+id);
		BT.setHeader("content-type", "application/json");
		TblSeguimientoMedico seguiMedico;
		
		try {
			HttpResponse resp=httpClient.execute(BT);
			String respStr=EntityUtils.toString(resp.getEntity());
			
			JSONArray respJSON = new JSONArray(respStr);
			
			for (int i = 0; i <respJSON.length(); i++) {
				JSONObject obj=respJSON.getJSONObject(i);
						
				seguiMedico = new TblSeguimientoMedico();
				seguiMedico.setIdSeguimientoMedico(obj.getLong("IdSeguimientoMedico"));
				seguiMedico.setIdPaciente(obj.getLong("IdPaciente"));
				seguiMedico.setAnio(obj.getInt("Anio"));
				seguiMedico.setMes(obj.getInt("Mes"));
				seguiMedico.setDia(obj.getInt("Dia"));
				seguiMedico.setUnidadMedica(obj.getString("UnidadMedica"));
				seguiMedico.setDoctor(obj.getString("Doctor"));
				seguiMedico.setDiagnostico(obj.getString("Diagnostico"));
				seguiMedico.setTratamientoMedico(obj.getString("TratamientoMedico"));
				seguiMedico.setAlimentacionSugerida(obj.getString("AlimentacionSugerida"));
				seguiMedico.setEliminado(obj.getBoolean("Eliminado"));
				
				TblSeguimientoMedico guardar_SM = new TblSeguimientoMedico(
						seguiMedico.getIdSeguimientoMedico(), seguiMedico.getIdPaciente(),
						seguiMedico.getAnio(), seguiMedico.getMes(), seguiMedico.getDia(),
						seguiMedico.getUnidadMedica(), seguiMedico.getDoctor(), 
						seguiMedico.getDiagnostico(), seguiMedico.getTratamientoMedico(), 
						seguiMedico.getAlimentacionSugerida(),seguiMedico.getEliminado());
				guardar_SM.save();
				
			}
			
		} catch (Exception ex) {
			// TODO: handle exception
			Log.e("Serviciorest","Error!",ex);
			resul=false;
		}
		
		//****************************** TIPOS DE ACTIVIDAD
		httpClient= new DefaultHttpClient();
		BT= new HttpGet ("http://"+ip+"/ADP/TipoActividad/TipoActividadBuscarAll");
		BT.setHeader("content-type", "application/json");
		TblTipoActividad tActividad;
		
		try {
			HttpResponse resp=httpClient.execute(BT);
			String respStr=EntityUtils.toString(resp.getEntity());
			
			JSONArray respJSON = new JSONArray(respStr);
			
			for (int i = 0; i <respJSON.length(); i++) {
				JSONObject obj=respJSON.getJSONObject(i);
				
				tActividad = new TblTipoActividad();
				tActividad.setIdTipoActividad(obj.getLong("IdTipoActividad"));
				tActividad.setTipoActividad(obj.getString("TipoDeActividad"));
				tActividad.setDescripcionTipoAct(obj.getString("DescripcionTipoAct"));
				tActividad.setEliminado(obj.getBoolean("Eliminado"));
				
				TblTipoActividad guardar_tActividad = new TblTipoActividad(
						tActividad.getIdTipoActividad(), tActividad.getTipoActividad(),
						tActividad.getDescripcionTipoAct(), tActividad.getEliminado());
				guardar_tActividad.save();
				
			}
			
		} catch (Exception ex) {
			// TODO: handle exception
			Log.e("Serviciorest","Error!",ex);
			resul=false;
		}
		
		return resul;
	}

}
