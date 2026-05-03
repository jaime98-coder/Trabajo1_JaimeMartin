package modelo;

import java.util.Objects;

public class Paciente implements Comparable<Paciente> {

	// Constante compartida para el formateo de archivos
	private static final String SEPARADOR = ";";

	private String nombre;
	private String direccion;
	private String telefono;
	private boolean baja;

	public Paciente(String nombre, String direccion, String telefono, boolean baja) {
		this.nombre = nombre;
		this.direccion = direccion;

		// Validamos nulidad y longitud antes de asignar
		if (telefono == null || telefono.length() != 9) {
			throw new IllegalArgumentException("El teléfono debe tener 9 dígitos.");
		} else {
			this.telefono = telefono;
		}

		this.baja = baja;
	}

	public String getNombre() {
		return nombre;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	
	public String getDireccion() {
		return direccion;
	}

	public void setBaja(boolean baja) {
		this.baja = baja;
	}

	public boolean isBaja() {
		return baja;
	}

	@Override
	public int hashCode() {
		// Normalizamos el nombre a minúsculas para generar siempre el mismo hash
		// numérico
		return Objects.hash(nombre.toLowerCase(), telefono);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		Paciente other = (Paciente) obj;

		// Comparamos el nombre ignorando mayúsculas para cumplir el contrato con
		// compareTo
		return this.nombre.equalsIgnoreCase(other.nombre) && Objects.equals(this.telefono, other.telefono);
	}

	@Override
	public int compareTo(Paciente otroPaciente) {
		// Priorizamos el nombre alfabéticamente (ignorando mayúsculas)
		if (this.nombre.equalsIgnoreCase(otroPaciente.nombre)) {
			// Si el nombre es idéntico, desempatamos por el teléfono
			return this.telefono.compareTo(otroPaciente.telefono);
		} else {
			// Si el nombre es distinto, se ordenan por nombre
			return this.nombre.compareToIgnoreCase(otroPaciente.nombre);
		}
	}

	public String getFichaPantalla() {
		// El propio objeto traduce su booleano a texto comprensible para el usuario
		return "Nombre: " + nombre + "\nDirección: " + direccion + "\nTeléfono: " + telefono + "\nBaja: "
				+ (baja ? "Sí" : "No");
	}

	public String getFichaArchivo() {
		// Construcción de la línea plana para el txt
		return nombre + SEPARADOR + direccion + SEPARADOR + telefono + SEPARADOR + baja;
	}
}