package model;

import java.sql.Array;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 * Clase per gestionar la persistència de les dades de les persona
 * @author manuel
 *
 */
public class PersonesDAO {

	private Connection conexionBD;

	public PersonesDAO(Connection conexionBD) {
		this.conexionBD = conexionBD;
	}
	
	public List<Persona> getPersonesList() {
		List<Persona> personesList = new ArrayList<Persona>();
		try (ResultSet result = conexionBD.createStatement().executeQuery("SELECT * FROM persona")) {
			while (result.next()) {
				try{
					Direccion direccion = getDirectionFromResult(result);
					personesList.add(
						new Persona(
							result.getInt("id"), 
							result.getString("dni"), 
							result.getString("nombre"), 
							result.getString("apellidos"),
							result.getDate("fecha_nacimiento").toLocalDate(),
							result.getString("email"),
							result.getArray("telefonos"),
							direccion
						)
					);
		
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return personesList;
	}
	
	public boolean save(Persona persona){
		String sql = "";
		PreparedStatement stmt = null;

		try {
			if (this.find(persona.getId()) == null){
				sql = "INSERT INTO persona VALUES(?,?,?,?, ?,?,?, row(?,?,?,?))";
				stmt = conexionBD.prepareStatement(sql);
				int i = 1;
				stmt.setInt(i++, persona.getId());
				stmt.setString(i++, persona.getDni());
				stmt.setString(i++, persona.getNom());
				stmt.setString(i++, persona.getApellidos());

				stmt.setDate(i++, Date.valueOf(persona.getFecha_nacimiento()));
				stmt.setString(i++, persona.getEmail());
				stmt.setArray(i++, persona.getTelefonos());

				//direccion
				stmt.setString(i++, persona.getDireccion().getLocalidad());
				stmt.setString(i++, persona.getDireccion().getProvincia());
				stmt.setString(i++, persona.getDireccion().getCod_postal());
				stmt.setString(i++, persona.getDireccion().getDomicilio());

			} else{
				sql = "UPDATE persona SET dni=?,nom=?,apellidos=?,fecha_nacimiento=?,email=?,telefonos=?,direccion=row(?,?,?,?) WHERE id = "+persona.getId();
				stmt = conexionBD.prepareStatement(sql);
				int i = 1;
				stmt.setString(i++, persona.getDni());
				stmt.setString(i++, persona.getNom());
				stmt.setString(i++, persona.getApellidos());
				stmt.setDate(i++, Date.valueOf(persona.getFecha_nacimiento()));
				stmt.setString(i++, persona.getEmail());
				stmt.setArray(i++, persona.getTelefonos());	//telefonos
				
				//direccion
				stmt.setString(i++, persona.getDireccion().getLocalidad());
				stmt.setString(i++, persona.getDireccion().getProvincia());
				stmt.setString(i++, persona.getDireccion().getCod_postal());
				stmt.setString(i++, persona.getDireccion().getDomicilio());

			}

			int rows = stmt.executeUpdate();
			if (rows == 1) return true;
			else return false;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	public boolean delete(Integer id){
		try {
			String sql = "";
			PreparedStatement stmt = null;
			if (this.find(id) != null){
				sql = "DELETE FROM persona WHERE id = ?";
				stmt = conexionBD.prepareStatement(sql);
				int i = 1;
				stmt.setInt(i++, id);
			}
			int rows = stmt.executeUpdate();
			if (rows == 1) return true;
			else return false;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	public Persona find(Integer id){
		if (id == null || id == 0){
			return null;
		}
		Persona p = null;

		try (PreparedStatement stmt = conexionBD.prepareStatement("SELECT * FROM persona WHERE id = "+id)){
			ResultSet result = stmt.executeQuery();
			if (result.next()) {
				Direccion direccion = getDirectionFromResult(result);
				p = new Persona(
					result.getInt("id"),
					result.getString("dni"),
					result.getString("nombre"), 
					result.getString("apellidos"),

					result.getDate("fecha_nacimiento").toLocalDate(),
					result.getString("email"),
					result.getArray("telefonos"),//telefonos
					direccion
				);
				p.imprimir();
			}	
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return p;
	}

	public void showAll(){
		try (ResultSet result = conexionBD.createStatement().executeQuery("SELECT * FROM persona")) {
			while (result.next()) {
				Direccion direccion = getDirectionFromResult(result);
				Persona p = new Persona(
					result.getInt("id"),
					result.getString("dni"),
					result.getString("nombre"), 
					result.getString("apellidos"),

					result.getDate("fecha_nacimiento").toLocalDate(),
					result.getString("email"),
					result.getArray("telefonos"), //telefonos
					direccion //direccion
				);
				p.imprimir();
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public Array getArrayTelefonos(String telefonos){
		String[] telefonosTemp = telefonos.split(",");
		Array telefonosArray = null;
		try {
			telefonosArray = conexionBD.createArrayOf("VARCHAR", telefonosTemp);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return telefonosArray;
	}

	public Direccion getDirectionFromResult(ResultSet result){
		Direccion direccion = null;
		try {
			// Obtener los resultados de la columna direccion en string, separarlo por comas y guardarlo en una array.
			String [] dirValues = result.getObject("direccion").toString().split(",");

			// Creo el caracter vacio
			char empty = '\0';
			direccion = new Direccion("", "", "", "");
			// Añado a la direccion los atributos correspondientes remplazando los parentesis por el caracter vacio.
			direccion.setLocalidad(dirValues[0].replace('(', empty));
			direccion.setProvincia(dirValues[1]);
			direccion.setCod_postal(dirValues[2]);
			direccion.setDomicilio(dirValues[3].replace(')', empty));

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}		

		return direccion;
	}

	public String getPhonesNumbersString(String telefonos){
		char empty = '\0';
		telefonos = telefonos.replace('{', empty);
		telefonos = telefonos.replace('}', empty);
		telefonos = telefonos.replace('\"', empty);

		return telefonos;
	}
}

