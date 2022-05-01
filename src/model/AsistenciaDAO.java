package model;

import java.sql.Array;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
/**
 * Clase per gestionar la persistència de les dades de les asistencia
 * @author manuel
 *
 */
public class AsistenciaDAO {

	private Connection conexionBD;

	public AsistenciaDAO(Connection conexionBD) {
		this.conexionBD = conexionBD;
	}
	
	public List<Asistencia> getAsistenciaList() {
		List<Asistencia> asistenciaList = new ArrayList<Asistencia>();
		try (ResultSet result = conexionBD.createStatement().executeQuery("SELECT * FROM asistencia")) {
			while (result.next()) {
				try{
					Long fecha_entradaLong = result.getDate("fecha_entrada").getTime();
					Long fecha_salidaLong = result.getDate("fecha_salida").getTime();
					LocalDateTime fecha_entrada = LocalDateTime.ofInstant(Instant.ofEpochSecond(fecha_entradaLong),TimeZone.getDefault().toZoneId());
					LocalDateTime fecha_salida = LocalDateTime.ofInstant(Instant.ofEpochSecond(fecha_salidaLong),TimeZone.getDefault().toZoneId());

					asistenciaList.add(
						new Asistencia(
							result.getInt("id"), 
							fecha_entrada,
							fecha_salida
						)
					);
		
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return asistenciaList;
	}
	
	public boolean save(Asistencia asistencia){
		String sql = "";
		PreparedStatement stmt = null;

		try {
			if (this.find(asistencia.getId()) == null){
				sql = "INSERT INTO asistencia VALUES(?,?,?)";
				stmt = conexionBD.prepareStatement(sql);
				int i = 1;
				stmt.setInt(i++, asistencia.getId());
				// stmt.set(i++, asistencia.getFechaEntrada());
				// stmt.setString(i++, asistencia.getFechaSalida());

			} else{
				sql = "UPDATE asistencia SET fecha_entrada=?,fecha_salida=? WHERE id = "+asistencia.getId();
				stmt = conexionBD.prepareStatement(sql);
				int i = 1;
				stmt.setInt(i++, asistencia.getId());
				// stmt.set(i++, asistencia.getFechaEntrada());
				// stmt.setString(i++, asistencia.getFechaSalida());

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
				sql = "DELETE FROM asistencia WHERE id = ?";
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

	public Asistencia find(Integer id){
		if (id == null || id == 0){
			return null;
		}
		Asistencia a = null;

		try (PreparedStatement stmt = conexionBD.prepareStatement("SELECT * FROM asistencia WHERE id = "+id)){
			ResultSet result = stmt.executeQuery();
			if (result.next()) {
				a = new Asistencia(
					result.getInt("id"),
					null,
					null
				);
				a.imprimir();
			}	
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return a;
	}

	public void showAll(){
		try (ResultSet result = conexionBD.createStatement().executeQuery("SELECT * FROM asistencia")) {
			while (result.next()) {
				Asistencia a = new Asistencia(
					result.getInt("id"),
					null,
					null
				);
				a.imprimir();
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

}

