package com.Notifications.patientssassistant.asynctask;

public class Taskss {

	public static String idCuidador;
	public static int time=2000;
	
	public static void  IngresarCuidador(long id){
		idCuidador=String.valueOf(id);
	} 
	
	public static void ListaDeTareas(){
		SimpleThreadPool stp = new SimpleThreadPool(22);
		stp.enqueue(new TestTask1());
		stp.enqueue(new TestTask2());
		stp.enqueue(new TestTask3());
		stp.enqueue(new TestTask4());
		stp.enqueue(new TestTask5());
		stp.enqueue(new TestTask6());
		stp.enqueue(new TestTask7());
		stp.enqueue(new TestTask8());
		stp.enqueue(new TestTask9());
		stp.enqueue(new TestTask10());
		stp.enqueue(new TestTask11());
		stp.enqueue(new TestTask12());
		stp.enqueue(new TestTask13());
		stp.enqueue(new TestTask14());
		stp.enqueue(new TestTask15());
		stp.enqueue(new TestTask16());
		stp.enqueue(new TestTask17());
		stp.enqueue(new TestTask18());
		stp.enqueue(new TestTask19());
		stp.enqueue(new TestTask20());
		stp.enqueue(new TestTask21());
		stp.enqueue(new TestTask22());
	}
	
	public static class TestTask1 implements Runnable {
		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//ACTIVIDADES
				//TblActividades.deleteAll(TblActividades.class);
				ATActividades actividad = new ATActividades();
				actividad.new BuscarAllActividades().execute(idCuidador);
			}
		}
	}

	public static class TestTask2 implements Runnable {
		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//ACTIVIDADES CUIDADORES
				//TblActividadCuidador.deleteAll(TblActividadCuidador.class);
				ATActividadCuidadores actividadC = new ATActividadCuidadores();
				actividadC.new BuscarAllActividadCuidadores().execute(idCuidador);
			}
		}
	}
	
	public static class TestTask3 implements Runnable {
		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//ACTIVIDADES PACIENTES
				//TblActividadPaciente.deleteAll(TblActividadPaciente.class);
				ATActividadPaciente actividadP = new ATActividadPaciente();
				actividadP.new BuscarAllActividadCuidadores().execute(idCuidador);
			}
		}
	}

	public static class TestTask4 implements Runnable {
		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//BUZON
				//TblBuzon.deleteAll(TblBuzon.class);
				ATBuzon buzon = new ATBuzon();
				buzon.new BuscarAllBuzonesXCuidadores().execute(idCuidador);
			}
		}
	}
	
	public static class TestTask5 implements Runnable {
		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//CONTROL DIETA
				//TblControlDieta.deleteAll(TblControlDieta.class);
				ATControlDieta controlDieta = new ATControlDieta();
				controlDieta.new BuscarAllControlDietaCuidadores().execute(idCuidador);
			}
		}
	}

	public static class TestTask6 implements Runnable {
		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//CONTROL MEDICINA
				//TblControlMedicina.deleteAll(TblControlMedicina.class);
				ATControlMedicina controlMedicina = new ATControlMedicina();
				controlMedicina.new BuscarAllControlMedicinaXCuidadores().execute(idCuidador);
			}
		}
	}
	
	
	public static class TestTask7 implements Runnable {
		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//CUIDADOR
				//TblCuidador.deleteAll(TblCuidador.class);
				ATCuidador cuidador = new ATCuidador();
				cuidador.new  BuscarAllCuidadores().execute(idCuidador);
			}
		}
	}

	public static class TestTask8 implements Runnable {
		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//CUIDADORPR
				//TblCuidadorPr.deleteAll(TblCuidadorPr.class);
				ATCuidadorPr cuidadorPr = new ATCuidadorPr();
				cuidadorPr.new BuscarAllCuidadoresPr().execute(idCuidador);
			}
		}
	}
	
	public static class TestTask9 implements Runnable {
		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//CUIDADORS
				//TblCuidadorS.deleteAll(TblCuidadorS.class);
				ATCuidadorS cuidadorS = new ATCuidadorS();
				cuidadorS.new BuscarAllCuidadoresS().execute(idCuidador);
			}
		}
	}

	public static class TestTask10 implements Runnable {
		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//ESTADO DE SALUD
				//TblEstadoSalud.deleteAll(TblEstadoSalud.class);
				ATEstadoSalud estadoSalud= new ATEstadoSalud();
				estadoSalud.new BuscarAllEstadosSaludCuidadores().execute(idCuidador);
			}
		}
	}
	
	public static class TestTask11 implements Runnable {
		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//EVENTOS CUIDADORES
				//TblEventosCuidadores.deleteAll(TblEstadoSalud.class);
				ATEventosCuidador eventosCuidaores = new ATEventosCuidador();
				eventosCuidaores.new BuscarAllEventosCuidadores().execute(idCuidador);
			}
		}
	}

	public static class TestTask12 implements Runnable {
		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//EVENTOS PACIENTES
				//TblEventosPacientes.deleteAll(TblEventosPacientes.class);
				ATEventosPaciente eventosPacientes = new ATEventosPaciente();
				eventosPacientes.new BuscarAllEventosCuidadores().execute(idCuidador);
			}
		}
	}
	
	public static class TestTask13 implements Runnable {
		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//FAMILIARES PACIENTES
				//TblFamiliaresPacientes.deleteAll(TblFamiliaresPacientes.class);
				ATFamiliaresPacientes familiares = new ATFamiliaresPacientes();
				familiares.new BuscarAllFamiliaresPacientesCuidadores().execute(idCuidador);
			}
		}
	}

	public static class TestTask14 implements Runnable {
		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//HORARIOS MEDICINAS
				//TblHorarioMedicina.deleteAll(TblHorarioMedicina.class);
				ATHorarioMedicinas horariosMed = new ATHorarioMedicinas();
				horariosMed.new BuscarAllHorarioMedicinaXCuidadores().execute(idCuidador);
			}
		}
	}
	
	public static class TestTask15 implements Runnable {
		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//HORARIOS
				//TblHorarios.deleteAll(TblHorarios.class);
				ATHorarios horarios = new ATHorarios();
				horarios.new BuscarAllHorariosXCuidadores().execute(idCuidador);
			}
		}
	}

	public static class TestTask16 implements Runnable {
		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//TblObservaciones.deleteAll(TblObservaciones.class);
				ATObservaciones observaciones = new ATObservaciones();
				observaciones.new BuscarAllObservacionesCuidadores().execute(idCuidador);
			}
		}
	}
	
	public static class TestTask17 implements Runnable {
		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//PACIENTES
				//TblPacientes.deleteAll(TblPacientes.class);
				ATPacientes pacientes = new ATPacientes();
				pacientes.new BuscarAllPacientes().execute(idCuidador);
			}
		}
	}

	public static class TestTask18 implements Runnable {
		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//PERMISOS
				//TblPermisos.deleteAll(TblPermisos.class);
				ATPermisos permisos = new ATPermisos();
				permisos.new BuscarAllPermisosXCuidadores().execute(idCuidador);
			}
		}
	}
	
	public static class TestTask19 implements Runnable {
		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//RUTINAS CUIDADORES
				//TblRutinasCuidadores.deleteAll(TblRutinasCuidadores.class);
				ATRutinasCuidadores rutinasC = new ATRutinasCuidadores();
				rutinasC.new BuscarAllRutinasCuidadores().execute(idCuidador);
			}
		}
	}

	public static class TestTask20 implements Runnable {
		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//RUTINAS PACIENTES
				//TblRutinasPacientes.deleteAll(TblRutinasPacientes.class);
				ATRutinasPacientes rutinasP = new ATRutinasPacientes();
				rutinasP.new BuscarAllRutinasCuidadores().execute(idCuidador);
			}
		}
	}
	
	public static class TestTask21 implements Runnable {
		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//SEGUIMIENTO MEDICO
				//TblSeguimientoMedico.deleteAll(TblSeguimientoMedico.class);
				ATSeguimientoMedico seguimientoMed = new ATSeguimientoMedico();
				seguimientoMed.new BuscarAllSeguimientoMedicoXCuidadores().execute(idCuidador);
			}
		}
	}

	public static class TestTask22 implements Runnable {
		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//TIPO ACTIVIDAD
				//TblTipoActividad.deleteAll(TblTipoActividad.class);
				ATTipoActividad tipoActividad = new ATTipoActividad();
				tipoActividad.new BuscarAllTipoActividad().execute(idCuidador);
			}
		}
	}
	
}
