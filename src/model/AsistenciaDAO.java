package model;

import java.sql.Array;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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
					LocalDateTime fecha_entrada = result.getTimestamp("fecha_entrada").toLocalDateTime();

					if (result.getDate("fecha_salida")!=null) {
						LocalDateTime fecha_salida = result.getTimestamp("fecha_salida").toLocalDateTime();

						asistenciaList.add(
							new Asistencia(
								result.getInt("id"),
								fecha_entrada,
								fecha_salida
							)
						);
					}else{
						asistenciaList.add(
							new Asistencia(
								result.getInt("id"),
								fecha_entrada,
								null
							)
						);
					}
		
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
				stmt.setTimestamp(i++, Timestamp.valueOf(asistencia.getFechaEntrada()));
				stmt.setTimestamp(i++, Timestamp.valueOf(asistencia.getFechaSalida()));

			} else{
				sql = "UPDATE asistencia SET fecha_entrada=?,fecha_salida=? WHERE id = "+asistencia.getId();
				stmt = conexionBD.prepareStatement(sql);
				int i = 1;
				stmt.setTimestamp(i++, Timestamp.valueOf(asistencia.getFechaEntrada()));
				stmt.setTimestamp(i++, Timestamp.valueOf(asistencia.getFechaSalida()));

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
				LocalDateTime fecha_entrada = result.getTimestamp("fecha_entrada").toLocalDateTime();

				if (result.getDate("fecha_salida")!=null) {
					LocalDateTime fecha_salida = result.getTimestamp("fecha_salida").toLocalDateTime();

					a= new Asistencia(
						result.getInt("id"),
						fecha_entrada,
						fecha_salida
					);
				}else{
					a= new Asistencia(
						result.getInt("id"),
						fecha_entrada,
						null
					);
				}

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
				Asistencia a = null;
				LocalDateTime fecha_entrada = result.getTimestamp("fecha_entrada").toLocalDateTime();

				if (result.getDate("fecha_salida")!=null) {
					LocalDateTime fecha_salida = result.getTimestamp("fecha_salida").toLocalDateTime();

					a = new Asistencia(
						result.getInt("id"),
						fecha_entrada,
						fecha_salida
					);
				}else{
					a = new Asistencia(
						result.getInt("id"),
						fecha_entrada,
						null
					);
				}

				a.imprimir();
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

}

