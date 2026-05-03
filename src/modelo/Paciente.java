package modelo;

import java.util.Objects;

/**
 * La clase Paciente representa a un paciente con su información personal y
 * estado de baja. Implementa la interfaz Comparable para permitir la ordenación
 * natural por nombre (ignorando mayúsculas) y teléfono.
 * 
 * @author Jaime Martín
 */
public class Paciente implements Comparable<Paciente> {

	// Constante compartida para el formateo de archivos (Accesible por el Gestor)
	public static final String SEPARADOR = ";";

	private String nombre;
	private String direccion;
	private String telefono;
	private boolean baja;

	/**
	 * Constructor principal de la entidad Paciente. Valida estructuralmente el
	 * teléfono antes de permitir la creación del objeto en la RAM.
	 * 
	 * @param nombre    El nombre completo del paciente.
	 * @param direccion La dirección de residencia del paciente.
	 * @param telefono  El número de contacto (obligatorio 9 dígitos).
	 * @param baja      Estado médico del paciente (true si está de baja, false en
	 *                  caso contrario).
	 * @throws IllegalArgumentException Si el teléfono es nulo o no tiene
	 *                                  exactamente 9 dígitos.
	 */
	public Paciente(String nombre, String direccion, String telefono, boolean baja) {
		this.nombre = nombre;
		this.direccion = direccion;

		// Validamos nulidad y longitud antes de asignar para proteger la integridad del
		// objeto
		if (telefono == null || telefono.length() != 9) {
			throw new IllegalArgumentException("El teléfono debe tener 9 dígitos.");
		} else {
			this.telefono = telefono;
		}

		this.baja = baja;
	}

	/**
	 * Obtiene el nombre del paciente.
	 * 
	 * @return El nombre del paciente.
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Obtiene el teléfono del paciente.
	 * 
	 * @return El teléfono de contacto.
	 */
	public String getTelefono() {
		return telefono;
	}

	/**
	 * Modifica la dirección del paciente.
	 * 
	 * @param direccion La nueva dirección a registrar.
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	/**
	 * Obtiene la dirección del paciente.
	 * 
	 * @return La dirección actual.
	 */
	public String getDireccion() {
		return direccion;
	}

	/**
	 * Actualiza el estado de baja médica del paciente.
	 * 
	 * @param baja true para dar la baja, false para dar el alta médica.
	 */
	public void setBaja(boolean baja) {
		this.baja = baja;
	}

	/**
	 * Consulta si el paciente se encuentra actualmente de baja.
	 * 
	 * @return true si está de baja, false en caso contrario.
	 */
	public boolean isBaja() {
		return baja;
	}

	/**
	 * Genera un código hash único basado en la identidad del paciente. Se normaliza
	 * el nombre a minúsculas para asegurar que dos pacientes con el mismo nombre y
	 * teléfono generen el mismo hash, cumpliendo el contrato de Java.
	 * 
	 * @return El código hash numérico calculado.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(nombre.toLowerCase(), telefono);
	}

	/**
	 * Compara este paciente con otro objeto para determinar si son idénticos. Dos
	 * pacientes se consideran iguales si coinciden en nombre (ignorando mayúsculas)
	 * y teléfono.
	 * 
	 * @param obj El objeto con el que se va a comparar.
	 * @return true si representan al mismo paciente, false en caso contrario.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		Paciente other = (Paciente) obj;

		return this.nombre.equalsIgnoreCase(other.nombre) && Objects.equals(this.telefono, other.telefono);
	}

	/**
	 * Define el criterio de ordenación natural (para el TreeSet). La comparación se
	 * realiza primero por nombre (alfabéticamente ignorando mayúsculas). Si hay
	 * empate, se utiliza el teléfono como criterio secundario.
	 * 
	 * @param otroPaciente El paciente contra el que se va a comparar.
	 * @return Un número negativo si es menor, positivo si es mayor, o 0 si son
	 *         iguales.
	 */
	@Override
	public int compareTo(Paciente otroPaciente) {
		if (this.nombre.equalsIgnoreCase(otroPaciente.nombre)) {
			return this.telefono.compareTo(otroPaciente.telefono);
		} else {
			return this.nombre.compareToIgnoreCase(otroPaciente.nombre);
		}
	}

	/**
	 * Devuelve una representación textual y legible del paciente para la interfaz
	 * de usuario. Traduce el estado booleano a "Sí"/"No" para mejorar la
	 * Experiencia de Usuario (UX). Sobreescribe el método nativo de Java para
	 * imprimir directamente el objeto.
	 * 
	 * @return Cadena formateada con los datos del paciente listos para consola.
	 */
	@Override
	public String toString() {
		return "Nombre: " + nombre + "\nDirección: " + direccion + "\nTeléfono: " + telefono + "\nBaja: "
				+ (baja ? "Sí" : "No");
	}

	/**
	 * Genera una línea de texto plano estructurada para persistir el objeto en un
	 * archivo. Utiliza la constante SEPARADOR para delimitar los campos.
	 * 
	 * @return Cadena con el formato nombre;direccion;telefono;baja.
	 */
	public String getFichaArchivo() {
		// Construcción de la línea plana para el .txt
		return nombre + SEPARADOR + direccion + SEPARADOR + telefono + SEPARADOR + baja;
	}
}