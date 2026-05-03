package pacientes;

import java.util.Objects;

public class Paciente implements Comparable<Paciente> {
	private String nombre;
	private String direccion;
	private String telefono;
	private boolean baja;

	public Paciente(String nombre, String direccion, String telefono, boolean baja) {
		this.nombre = nombre;
		this.direccion = direccion;
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

	public void setBaja(boolean baja) {
		this.baja = baja;
	}

	@Override
	public int hashCode() {
		return Objects.hash(nombre, telefono);
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
		return Objects.equals(nombre, other.nombre) && Objects.equals(telefono, other.telefono);
	}

	@Override
	public int compareTo(Paciente otroPaciente) {
		if (this.nombre.equalsIgnoreCase(otroPaciente.nombre)) {
			return this.telefono.compareTo(otroPaciente.telefono);
		} else {
			return this.nombre.compareToIgnoreCase(otroPaciente.nombre);
		}
	}

}
