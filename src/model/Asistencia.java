package model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Asistencia implements Serializable {

	private static final long serialVersionUID = 1234567890l;
	private int id;
	private LocalDateTime fechaEntrada;
	private LocalDateTime fechaSalida;

	public Asistencia(int id, LocalDateTime fechaEntrada, LocalDateTime fechaSalida) {
		this.id = id;
		this.fechaEntrada = fechaEntrada;
		this.fechaSalida = fechaSalida;
	}
	
	public int getId() {
		return id;
	}

	public LocalDateTime getFechaEntrada() {
		return fechaEntrada;
	}

	public void setFechaEntrada(LocalDateTime fechaEntrada) {
		this.fechaEntrada = fechaEntrada;
	}

	public LocalDateTime getFechaSalida() {
		return fechaSalida;
	}

	public void setFechaSalida(LocalDateTime fechaSalida) {
		this.fechaSalida = fechaSalida;
	}

	public void imprimir(){
		System.out.println("Id: " + id);
		System.out.println("fecha de entrada: " + fechaEntrada);
		System.out.println("Fecha de salida: " + fechaSalida);

	}
}
