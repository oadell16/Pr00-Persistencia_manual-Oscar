package model;

import java.io.Serializable;
import java.sql.Array;
import java.time.LocalDate;

public class Persona implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private String dni;
	private String nom;
	private String apellidos;
	private LocalDate fecha_nacimiento;
	private String email;
	private Array telefonos;
	private Direccion direccion;

	public Persona() {
		this.id = 0;
		this.nom = "";
		this.apellidos = "";
		this.email = "";
		this.telefonos = null;
		this.direccion = null;
	}
	
	public Persona(int id, String dni, String nom, String apellidos, LocalDate fecha_nacimiento, String email,
			Array telefonos, Direccion direccion) {
		this.id = id;
		this.dni = dni;
		this.nom = nom;
		this.apellidos = apellidos;
		this.fecha_nacimiento = fecha_nacimiento;
		this.email = email;
		this.telefonos = telefonos;
		this.direccion = direccion;
	}
	
	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public LocalDate getFecha_nacimiento() {
		return fecha_nacimiento;
	}

	public void setFecha_nacimiento(LocalDate fecha_nacimiento) {
		this.fecha_nacimiento = fecha_nacimiento;
	}

	public Array getTelefonos() {
		return telefonos;
	}

	public void setTelefonos(Array telefonos) {
		this.telefonos = telefonos;
	}

	public Direccion getDireccion() {
		return direccion;
	}

	public void setDireccion(Direccion direccion) {
		this.direccion = direccion;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	public void imprimir(){
		System.out.println("Id: " + id);
		System.out.println("DNI: " + dni);
		System.out.println("Nom: " + nom);
		System.out.println("Apellidos: " + apellidos);
		System.out.println("Fecha nacimiento: " + fecha_nacimiento);
		System.out.println("E-mail: " + email);
		System.out.println("Telèfon: " + telefonos);
		System.out.println("Direccion");
		System.out.println("Provincia: " + direccion.getProvincia());
		System.out.println("Localidad: " + direccion.getLocalidad());
		System.out.println("Cod postal: " + direccion.getCod_postal());
		System.out.println("Domicilio: " + direccion.getDomicilio());

	}
}
