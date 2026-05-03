package gestores;

import java.io.*;
import java.util.TreeSet;

import modelo.Paciente;

public class GestorPacientes {
	TreeSet<Paciente> pacientes;
	String rutaArchivo = "src/archivo.txt";

	public GestorPacientes() {
		this.pacientes = new TreeSet<>();
	}

	public void cargarDatos() {
		try (BufferedReader in = new BufferedReader(new FileReader(rutaArchivo))) {
			String lineaTXT;
			String[] partes;
			lineaTXT = in.readLine();
			while (lineaTXT != null) {
				partes = lineaTXT.split(";");
				if (partes.length == 4) {
					Paciente pacienteGuardado = new Paciente(partes[0], partes[1], partes[2],
							Boolean.parseBoolean(partes[3]));
					pacientes.add(pacienteGuardado);
				}
				lineaTXT = in.readLine();
			}
		} catch (FileNotFoundException e) {
			// Lo dejamos vacío de forma controlada.

		} catch (IOException e) {
			System.out.println("Error grave de LECTURA: " + e.getMessage());

		}

	}

	public void guardarDatos() {
		try (BufferedWriter out = new BufferedWriter(new FileWriter(rutaArchivo))) {
			for (Paciente paciente : pacientes) {
				out.write(paciente.getNombre() + ";" + paciente.getDireccion() + ";" + paciente.getTelefono() + ";"
						+ paciente.isBaja());
				out.newLine();
			}
		} catch (IOException e) {
			System.out.println("ERROR GRAVE: No se ha podido guardar");
		}
	}
}
