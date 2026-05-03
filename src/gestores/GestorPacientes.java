package gestores;

import java.io.*;
import java.util.Collection;
import java.util.TreeSet;

import modelo.Paciente;

/**
 * La clase GestorPacientes es responsable de gestionar la colección de
 * pacientes (el salón del restaurante). Se encarga de la persistencia de datos
 * (carga y guardado) en disco duro y de las operaciones lógicas sobre la
 * colección (añadir, buscar, eliminar).
 */
public class GestorPacientes {

	// Atributo principal que actúa como nuestra base de datos en memoria
	private TreeSet<Paciente> pacientes;

	// Ruta del archivo físico donde se persistirán los datos
	private String rutaArchivo = "src/informacionPacientes.txt";

	/**
	 * Constructor de la clase gestora. Inicializa la colección como un TreeSet
	 * vacío para garantizar la ordenación automática y evitar duplicados según el
	 * compareTo de Paciente.
	 */
	public GestorPacientes() {
		this.pacientes = new TreeSet<>();
	}

	/**
	 * Lee el archivo de texto y vuelca los datos en la memoria RAM (TreeSet). Si el
	 * archivo no existe en la primera ejecución, atrapa la excepción
	 * silenciosamente para que el programa arranque sin errores.
	 * 
	 * @throws IOException Si ocurre un error crítico de lectura en el disco duro.
	 */
	public void cargarDatos() throws IOException {
		try (BufferedReader in = new BufferedReader(new FileReader(rutaArchivo))) {
			// Variable para almacenar la línea de texto completa
			String lineaTXT;
			// Array para separar los atributos del paciente
			String[] partes;

			lineaTXT = in.readLine();

			while (lineaTXT != null) {
				partes = lineaTXT.split(Paciente.SEPARADOR);
				if (partes.length == 4) {

					Paciente pacienteGuardado = new Paciente(partes[0], partes[1], partes[2],
							Boolean.parseBoolean(partes[3]));

					pacientes.add(pacienteGuardado);
				}
				lineaTXT = in.readLine();
			}
		} catch (FileNotFoundException e) {
			// Lo dejamos vacío de forma controlada porque es normal al arrancar sin archivo
		}
	}

	/**
	 * Vuelca todos los pacientes alojados en la memoria RAM al archivo físico.
	 * 
	 * @throws IOException Si ocurre un error de escritura o falta de permisos en el
	 *                     disco.
	 */
	public void guardarDatos() throws IOException {
		try (BufferedWriter out = new BufferedWriter(new FileWriter(rutaArchivo))) {
			for (Paciente paciente : pacientes) {
				out.write(paciente.getFichaArchivo());
				out.newLine();
			}
		}
	}

	/**
	 * Comprueba si la colección de pacientes se encuentra vacía.
	 * 
	 * @return true si no hay pacientes registrados, false si existe al menos uno.
	 */
	public boolean estaVacio() {
		return pacientes.isEmpty();
	}

	/**
	 * Añade un nuevo paciente a la colección tras haber validado sus datos en la
	 * interfaz.
	 * 
	 * @param pacienteAAnadir El objeto Paciente que deseamos registrar.
	 * @return true si se añadió con éxito, false si el TreeSet lo rechaza por estar
	 *         duplicado.
	 */
	public boolean anadirPaciente(Paciente pacienteAAnadir) {
		return pacientes.add(pacienteAAnadir);
	}

	/**
	 * Busca a un paciente dentro de la colección basándose en su identidad. Utiliza
	 * un retorno temprano (Early Return) para optimizar el rendimiento del
	 * procesador.
	 * 
	 * @param nombre   El nombre del paciente (ignora diferencias de mayúsculas).
	 * @param telefono El teléfono exacto del paciente (9 dígitos).
	 * @return La referencia al objeto Paciente si existe, o null si no se
	 *         encuentra.
	 */
	public Paciente buscarPaciente(String nombre, String telefono) {

		for (Paciente paciente : pacientes) {
			if (paciente.getNombre().equalsIgnoreCase(nombre) && paciente.getTelefono().equals(telefono)) {
				return paciente;
			}
		}

		return null;
	}

	/**
	 * Elimina a un paciente de la base de datos en memoria.
	 * 
	 * @param nombre   El nombre del paciente a eliminar.
	 * @param telefono El teléfono del paciente a eliminar.
	 * @return true si el paciente existía y fue borrado exitosamente, false en caso
	 *         contrario.
	 */
	public boolean eliminarPaciente(String nombre, String telefono) {
		// Reutilizamos la lógica de búsqueda para localizar la referencia exacta en
		// memoria
		Paciente pacienteAEliminar = buscarPaciente(nombre, telefono);

		if (pacienteAEliminar != null) {
			return pacientes.remove(pacienteAEliminar);
		}

		return false;
	}

	/**
	 * Devuelve la colección completa de pacientes usando la interfaz genérica
	 * Collection.
	 * 
	 * @return La colección de todos los objetos Paciente alojados en memoria.
	 */
	public Collection<Paciente> obtenerPacientes() {
		return pacientes;
	}
}