package ui;

import java.io.IOException;
import java.util.Scanner;

import gestores.GestorPacientes;
import modelo.Paciente;

/**
 * Clase principal que actúa como interfaz de usuario (Recepción) del sistema.
 * Gestiona la interacción con el usuario, los menús y delega las operaciones
 * lógicas al gestor correspondiente.
 * @author Jaime Martín
 */
public class Main {

	public static void main(String[] args) {
		GestorPacientes miGestor = new GestorPacientes();
		Scanner sc = new Scanner(System.in);

		boolean salir = false;
		int opcionElegida;

		System.out.println("------------------------------------");
		System.out.println("Bienvenido al gestor de pacientes...");
		System.out.println("------------------------------------");

		try {
			miGestor.cargarDatos();
		} catch (IOException e) {
			System.out.println("ERROR al cargar los datos: " + e.getMessage());
		}

		do {
			mostrarMenu();
			opcionElegida = sc.nextInt();

			// Limpiamos el buffer del Scanner
			sc.nextLine();

			switch (opcionElegida) {
			case 1:
				gestionarAltaPaciente(sc, miGestor);
				break;
			case 2:
				gestionarBusquedaPaciente(sc, miGestor);
				break;
			case 3:
				gestionarListadoPacientes(miGestor);
				break;
			case 4:
				gestionarModificacionPaciente(sc, miGestor);
				break;
			case 5:
				gestionarBajaPaciente(sc, miGestor);
				break;
			case 6:
				guardarDatosSistema(miGestor);
				break;
			case 0:
				System.out.println("Saliendo del sistema...");
				salir = true;
				System.out.println("Fin del programa. ¡Hasta pronto!");
				break;
			default:
				System.out.println("Opción no válida. Introduzca un número del 0 al 6.");
				break;
			}

		} while (!salir);

		sc.close();
	}

	// ----------------------------------------------------------------------
	// MÉTODOS AUXILIARES DE LA INTERFAZ DE USUARIO (UI)
	// ----------------------------------------------------------------------

	/**
	 * Gestiona el proceso de alta de un nuevo paciente solicitando sus datos por
	 * consola. Aplica validaciones de longitud y comprueba duplicados antes de
	 * completar el registro.
	 * 
	 * @param sc       Herramienta Scanner para la lectura de teclado.
	 * @param miGestor Instancia del gestor de pacientes que maneja los datos.
	 */
	private static void gestionarAltaPaciente(Scanner sc, GestorPacientes miGestor) {
		String nombrePaciente;
		String direccionPaciente;
		String telefonoPaciente;
		boolean bajaPaciente = false;
		boolean telefonoValido = false;

		System.out.println("\n--- AÑADIR PACIENTE ---");
		System.out.println("Introduce el NOMBRE del paciente:");
		nombrePaciente = sc.nextLine();

		do {
			System.out.println("Introduce el TELÉFONO del paciente:");
			telefonoPaciente = sc.nextLine().trim();
			if (telefonoPaciente.length() == 9) {
				telefonoValido = true;
			} else {
				System.out.println(
						"Número de teléfono no válido. Debe contener exactamente 9 dígitos. Inténtalo de nuevo.");
				telefonoValido = false;
			}
		} while (!telefonoValido);

		Paciente pacienteExistente = miGestor.buscarPaciente(nombrePaciente, telefonoPaciente);

		if (pacienteExistente != null) {
			System.out.println("Error: Ya existe un paciente con ese nombre y teléfono.");
		} else {
			System.out.println("Introduce la DIRECCIÓN del paciente:");
			direccionPaciente = sc.nextLine();

			boolean respuestaValida = false;

			do {
				System.out.println("¿El paciente está de BAJA? (Si/No):");
				String respuesta = sc.nextLine().trim();

				if (respuesta.equalsIgnoreCase("Si") || respuesta.equalsIgnoreCase("Sí")) {
					bajaPaciente = true;
					respuestaValida = true;
				} else if (respuesta.equalsIgnoreCase("No")) {
					bajaPaciente = false;
					respuestaValida = true;
				} else {
					System.out.println("Opción no válida. Por favor, escribe 'Si' o 'No'.");
				}
			} while (!respuestaValida);

			try {
				Paciente nuevoPaciente = new Paciente(nombrePaciente, direccionPaciente, telefonoPaciente,
						bajaPaciente);
				miGestor.anadirPaciente(nuevoPaciente);
				System.out.println("Paciente añadido con éxito.");
			} catch (IllegalArgumentException e) {
				System.out.println("No se pudo añadir: " + e.getMessage());
			}
		}
	}

	/**
	 * Solicita las credenciales de un paciente por consola y delega su búsqueda al
	 * gestor. Muestra los datos del paciente si existe, o un mensaje de error en
	 * caso contrario.
	 * 
	 * @param sc       Herramienta Scanner para la lectura de teclado.
	 * @param miGestor Instancia del gestor de pacientes.
	 */
	private static void gestionarBusquedaPaciente(Scanner sc, GestorPacientes miGestor) {
		String nombrePaciente;
		String telefonoPaciente;

		System.out.println("\n--- BUSCAR PACIENTE ---");
		System.out.println("Introduce el NOMBRE del paciente a buscar:");
		nombrePaciente = sc.nextLine();
		System.out.println("Introduce el TELÉFONO del paciente:");
		telefonoPaciente = sc.nextLine();

		Paciente pacienteEncontrado = miGestor.buscarPaciente(nombrePaciente, telefonoPaciente);

		if (pacienteEncontrado != null) {
			System.out.println("\nPaciente encontrado:\n" + pacienteEncontrado);
		} else {
			System.out.println("No se ha encontrado ningún paciente con esos datos.");
		}
	}

	/**
	 * Muestra por pantalla la información de todos los pacientes registrados en el
	 * sistema. Si el sistema está vacío, notifica al usuario.
	 * 
	 * @param miGestor Instancia del gestor de pacientes.
	 */
	private static void gestionarListadoPacientes(GestorPacientes miGestor) {
		System.out.println("\n--- LISTAR PACIENTES ---");
		if (miGestor.estaVacio()) {
			System.out.println("No hay pacientes registrados en el sistema.");
		} else {
			for (Paciente paciente : miGestor.obtenerPacientes()) {
				System.out.println(paciente);
				System.out.println("-------------------------");
			}
		}
	}

	/**
	 * Gestiona el proceso de modificación de los datos de un paciente existente.
	 * Permite actualizar la dirección o el estado de baja a través de un submenú
	 * interactivo.
	 * 
	 * @param sc       Herramienta Scanner para la lectura de teclado.
	 * @param miGestor Instancia del gestor de pacientes.
	 */
	private static void gestionarModificacionPaciente(Scanner sc, GestorPacientes miGestor) {
		String nombrePaciente;
		String telefonoPaciente;
		String direccionPaciente;

		System.out.println("\n--- MODIFICAR PACIENTE ---");
		System.out.println("Introduce el NOMBRE del paciente a modificar:");
		nombrePaciente = sc.nextLine();
		System.out.println("Introduce el TELÉFONO del paciente:");
		telefonoPaciente = sc.nextLine();

		Paciente pacienteAModificar = miGestor.buscarPaciente(nombrePaciente, telefonoPaciente);

		if (pacienteAModificar == null) {
			System.out.println("El paciente indicado no existe en el sistema.");
		} else {
			boolean modificacionTerminada = false;

			do {
				mostrarSubmenuModificar(pacienteAModificar);
				int opcionModificar = sc.nextInt();
				sc.nextLine();

				switch (opcionModificar) {
				case 1:
					System.out.println("Introduce la nueva DIRECCIÓN:");
					direccionPaciente = sc.nextLine();
					pacienteAModificar.setDireccion(direccionPaciente);
					System.out.println("Dirección actualizada correctamente.");
					modificacionTerminada = true;
					break;
				case 2:
					boolean respuestaValida = false;
					do {
						System.out.println("¿El paciente está de BAJA actualmente? (Si/No):");
						String respuesta = sc.nextLine().trim();

						if (respuesta.equalsIgnoreCase("Si") || respuesta.equalsIgnoreCase("Sí")) {
							pacienteAModificar.setBaja(true);
							respuestaValida = true;
						} else if (respuesta.equalsIgnoreCase("No")) {
							pacienteAModificar.setBaja(false);
							respuestaValida = true;
						} else {
							System.out.println("Opción no válida. Por favor, escribe 'Si' o 'No'.");
						}
					} while (!respuestaValida);

					System.out.println("Estado de baja actualizado correctamente.");
					modificacionTerminada = true;
					break;
				case 0:
					System.out.println("Operación de modificación cancelada.");
					modificacionTerminada = true;
					break;
				default:
					System.out.println("Opción no válida. Por favor, elija 1, 2 o 0.");
					break;
				}
			} while (!modificacionTerminada);
		}
	}

	/**
	 * Solicita las credenciales de un paciente y delega su eliminación al gestor.
	 * Informa al usuario sobre el éxito o fracaso de la operación.
	 * 
	 * @param sc       Herramienta Scanner para la lectura de teclado.
	 * @param miGestor Instancia del gestor de pacientes.
	 */
	private static void gestionarBajaPaciente(Scanner sc, GestorPacientes miGestor) {
		String nombrePaciente;
		String telefonoPaciente;

		System.out.println("\n--- ELIMINAR PACIENTE ---");
		System.out.println("Introduce el NOMBRE del paciente a eliminar:");
		nombrePaciente = sc.nextLine();
		System.out.println("Introduce el TELÉFONO del paciente:");
		telefonoPaciente = sc.nextLine();

		boolean eliminadoCorrectamente = miGestor.eliminarPaciente(nombrePaciente, telefonoPaciente);

		if (eliminadoCorrectamente) {
			System.out.println("Paciente eliminado correctamente.");
		} else {
			System.out.println("No se ha encontrado ningún paciente con esos datos.");
		}
	}

	/**
	 * Delega al gestor la persistencia de los datos actuales en el disco duro.
	 * Maneja visualmente las posibles excepciones de entrada/salida.
	 * 
	 * @param miGestor Instancia del gestor de pacientes.
	 */
	private static void guardarDatosSistema(GestorPacientes miGestor) {
		System.out.println("\n--- GUARDAR PACIENTES ---");
		try {
			miGestor.guardarDatos();
			System.out.println("Pacientes guardados correctamente.");
		} catch (IOException e) {
			System.out.println("ERROR al guardar los datos: " + e.getMessage());
		}
	}

	/**
	 * Imprime por consola el submenú de opciones para la modificación de un
	 * paciente.
	 * 
	 * @param pacienteAModificar Instancia del paciente cuyos datos se van a
	 *                           modificar.
	 */
	private static void mostrarSubmenuModificar(Paciente pacienteAModificar) {
		System.out.println("\n¿Qué dato desea modificar de " + pacienteAModificar.getNombre() + "?");
		System.out.println("1. Dirección");
		System.out.println("2. Estado de Baja");
		System.out.println("0. Cancelar");
		System.out.print("Seleccione una opción: ");
	}

	/**
	 * Imprime por consola el menú principal de la aplicación.
	 */
	private static void mostrarMenu() {
		System.out.println("\n------------------------------------");
		System.out.println("1. Añadir paciente");
		System.out.println("2. Buscar paciente");
		System.out.println("3. Listar pacientes");
		System.out.println("4. Modificar paciente");
		System.out.println("5. Eliminar paciente");
		System.out.println("6. Guardar pacientes");
		System.out.println("0. Salir");
		System.out.println("------------------------------------");
		System.out.print("Introduce la opción deseada: ");
	}
}