package model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductesDAO{

	private Connection conexionBD;

    public ProductesDAO(Connection conexionBD) {
		this.conexionBD = conexionBD;
	}

    public List<Productes> getProductesList() {
		List<Productes> productesList = new ArrayList<Productes>();
		try (ResultSet result = conexionBD.createStatement().executeQuery("SELECT * FROM productes")) {
			while (result.next()) {
				if (this.isPack(result.getInt("id"))) {

					List<Integer> ids = this.findProductes_pack(result.getInt("id"));
					try (ResultSet resultPack = conexionBD.createStatement().executeQuery("SELECT * FROM pack where id="+result.getInt("id"))) {
						if (resultPack.next()) {
							productesList.add(
								new Pack(
									resultPack.getInt("id"), 
									resultPack.getString("nombre"), 
									resultPack.getFloat("precio"),
									resultPack.getInt("stock"),
									resultPack.getDate("fecha_inicial_catalogo").toLocalDate(),
									resultPack.getDate("fecha_final_catalogo").toLocalDate(),
									ids,
									resultPack.getInt("dto")
								)
							);
						}
						
					}catch (SQLException e) {
						System.out.println(e.getMessage());
					}

				}else{
					productesList.add(
						new Productes(
							result.getInt("id"), 
							result.getString("nombre"), 
							result.getFloat("precio"),
							result.getInt("stock"),
							result.getDate("fecha_inicial_catalogo").toLocalDate(),
							result.getDate("fecha_final_catalogo").toLocalDate()
						)
							
					);
				}
				
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		System.out.println(productesList);
		return productesList;
	}

    public boolean saveProduct(Productes product){
		try {
            String sql = "";
			PreparedStatement stmt = null;

            if (this.findProducte(product.getId()) == null) {
				sql = "INSERT INTO productes VALUES(?,?,?,?,?,?)";
				stmt = conexionBD.prepareStatement(sql);
				int i = 1;
				stmt.setInt(i++, product.getId());
				stmt.setString(i++, product.getName());
				stmt.setFloat(i++, product.getPrice());
				stmt.setInt(i++, product.getStock());
				stmt.setDate(i++, Date.valueOf(product.getInitialCatalogDate()));
				stmt.setDate(i++, Date.valueOf(product.getEndCatalogDate()));
            }else {
                sql = "UPDATE productes SET nombre=?,precio=?,stock=?,fecha_inicial_catalogo=?,fecha_final_catalogo=? WHERE id = ?";
				stmt = conexionBD.prepareStatement(sql);
				int i = 1;
				stmt.setString(i++, product.getName());
				stmt.setFloat(i++, product.getPrice());
				stmt.setInt(i++, product.getStock());
				stmt.setDate(i++, Date.valueOf(product.getInitialCatalogDate()));
				stmt.setDate(i++, Date.valueOf(product.getEndCatalogDate()));
				stmt.setInt(i++, product.getId());
            };

			int rows = stmt.executeUpdate();
			if (rows == 1) return true;
			else return false;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}
	
    public boolean savePack(Pack pack){
		try {
            String sqlPack = "";
			PreparedStatement stmtPack = null;
			int rowsPack = 0;

            if(this.findPack(pack.getId()) == null) {
				sqlPack = "INSERT INTO pack VALUES(?,?,?,?,?,?,?)";
				stmtPack = conexionBD.prepareStatement(sqlPack);
				int i = 1;
				stmtPack.setInt(i++, pack.getId());
				stmtPack.setString(i++, pack.getName());
				stmtPack.setFloat(i++, pack.getPrice());
				stmtPack.setInt(i++, pack.getStock());
				stmtPack.setDate(i++, Date.valueOf(pack.getInitialCatalogDate()));
				stmtPack.setDate(i++, Date.valueOf(pack.getEndCatalogDate()));
				stmtPack.setInt(i++, pack.getPercentageDiscount());

            }else{
				System.out.println("update");
                sqlPack = "UPDATE pack SET nombre=?,precio=?,stock=?,fecha_inicial_catalogo=?,fecha_final_catalogo=?,dto=? WHERE id = "+pack.getId();
				stmtPack = conexionBD.prepareStatement(sqlPack);

				int i = 1;
				//modificar el pack
				stmtPack.setString(i++, pack.getName());
				stmtPack.setFloat(i++, pack.getPrice());
				stmtPack.setInt(i++, pack.getStock());
				stmtPack.setDate(i++, Date.valueOf(pack.getInitialCatalogDate()));
				stmtPack.setDate(i++, Date.valueOf(pack.getEndCatalogDate()));
				stmtPack.setInt(i++, pack.getPercentageDiscount());
            };

			rowsPack = stmtPack.executeUpdate();
			System.out.println("rowsPack");
			System.out.println(rowsPack);
			if (rowsPack == 1) {
				this.deleteProducts_pack(pack.getId());
				saveProductPack(pack.getId(), pack.getIds());
				return true;
				
			}else return false;

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	public boolean saveProductPack(Integer idPack, List<Integer> idProducts){
		String sql= "INSERT INTO productes_pack VALUES(?,?)";
		PreparedStatement stmt = null;
		int savedProductsInPack = 0;
		

		try {    
			for (Integer idProduct : idProducts) {
				stmt = conexionBD.prepareStatement(sql);
				int i = 1;
				stmt.setInt(i++, idPack);
				stmt.setInt(i++, idProduct);
				savedProductsInPack += stmt.executeUpdate();

			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		if (savedProductsInPack==idProducts.size()) return true;
		else return false;

	}

    public boolean deleteProducte(Integer id){
		int rows = 0;
		String sql = "";
		PreparedStatement stmt = null;

		try {
			if (this.findProducte(id) != null){
				sql = "DELETE FROM productes WHERE id = "+id;
				stmt = conexionBD.prepareStatement(sql);
			}
			rows = stmt.executeUpdate();
			if (rows == 1) return true;
			else return false;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	public boolean deletePack(Integer id){
		int rows = 0;
		String sql = "";
		PreparedStatement stmt = null;
		try {
			if (this.findPack(id) != null) {
				sql = "DELETE FROM pack WHERE id = "+id;
				stmt = conexionBD.prepareStatement(sql);
				
			}
			rows = stmt.executeUpdate();
			if (rows == 1) return true;
			else return false;

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	public boolean deleteProducts_pack(Integer idPack){
		int rows = 0;
		String sql = "";
		PreparedStatement stmt = null;
		try {
			if (this.findProductes_pack(idPack) != null) {
				sql = "DELETE FROM productes_pack WHERE id_pack = "+idPack;
				stmt = conexionBD.prepareStatement(sql);
			}
			rows = stmt.executeUpdate();
			if (rows == 1) return true;
			else return false;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

    public Productes findProducte(Integer id){
		if (id == null || id == 0){
			return null;
		}

		Productes p = null;
		try (PreparedStatement stmt = conexionBD.prepareStatement("SELECT * FROM productes WHERE id = ?")){
			stmt.setInt(1, id); //informem el primer paràmetre de la consulta amb ?
			ResultSet result = stmt.executeQuery();
			if (result.next()) {
				p = new Productes(
                    result.getInt("id"), 
                    result.getString("nombre"), 
                    result.getFloat("precio"),
                    result.getInt("stock"),
                    result.getDate("fecha_inicial_catalogo").toLocalDate(),
                    result.getDate("fecha_final_catalogo").toLocalDate()
                );

				p.imprimir();
			}	
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return p;
	}

	public Pack findPack(Integer id){
		if (id == null || id == 0){
			return null;
		}

		Pack p = null;
		ResultSet resultPack = null;

		try (PreparedStatement stmt = conexionBD.prepareStatement("SELECT * FROM pack WHERE id = "+id)){
			resultPack = stmt.executeQuery();

			if (resultPack.next()) {
				List<Integer> ids = findProductes_pack(id);
				if (ids!=null) {
					p = new Pack(
						resultPack.getInt("id"), 
						resultPack.getString("nombre"), 
						resultPack.getFloat("precio"),
						resultPack.getInt("stock"),
						resultPack.getDate("fecha_inicial_catalogo").toLocalDate(),
						resultPack.getDate("fecha_final_catalogo").toLocalDate(),
						ids,
						resultPack.getInt("dto")
					);
					p.imprimir();
				}
				
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return p;
	}

	public List<Integer> findProductes_pack(Integer idPack){
		List<Integer> ids = new ArrayList<Integer>();
		ResultSet resultProductesPack;

		try (PreparedStatement stmt = conexionBD.prepareStatement("SELECT * FROM productes_pack WHERE id_pack = ?")){
			stmt.setInt(1, idPack); //informem el primer paràmetre de la consulta amb ?
			resultProductesPack = stmt.executeQuery();

			while(resultProductesPack.next()){
				ids.add(resultProductesPack.getInt("id_producte"));
			}

		}catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return ids;
	}

    public boolean isPack(Integer id){
		boolean isPack = false;
        if (id == null || id == 0){
			isPack = false;
		}

		try (PreparedStatement stmt = conexionBD.prepareStatement("SELECT * FROM pack WHERE id = ?")){
			stmt.setInt(1, id); //informem el primer paràmetre de la consulta amb ?
			ResultSet result = stmt.executeQuery();
			if (result.next()) isPack = true;
			else isPack = false;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return isPack;
    }
    
    public void showAll(){
		try (ResultSet result = conexionBD.createStatement().executeQuery("SELECT * FROM productes")) {
			while (result.next()) {
				Productes p = new Productes(
                    result.getInt("id"), 
                    result.getString("nombre"), 
                    result.getFloat("precio"),
                    result.getInt("stock"),
                    result.getDate("fecha_inicial_catalogo").toLocalDate(),
                    result.getDate("fecha_final_catalogo").toLocalDate()
                );
				p.imprimir();
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public String getListproductsString(List<Integer> listIds){
		String resultado = "";

		for (Integer id : listIds) {
			resultado+= id+",";
		}

		return resultado;
	}

	public List<Integer> getIdPacksFromProductId(Integer productId){
		String sql = "";
		PreparedStatement stmt = null;
		List<Integer> idsPacks = new ArrayList<Integer>();
		try {
			if (this.findProducte(productId) != null){
				//seleccionar el id de los packs que contengan ese producto
				ResultSet result = conexionBD.createStatement().executeQuery("SELECT id_pack FROM productes_pack WHERE id_producte = "+productId);
				
				while (result.next()) {
					idsPacks.add(result.getInt("id_pack"));
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return idsPacks;
	}
}