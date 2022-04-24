package model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
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

    public List<Product> getProductesList() {
		List<Product> productesList = new ArrayList<Product>();
		try (ResultSet result = conexionBD.createStatement().executeQuery("SELECT * FROM persones")) {
			while (result.next()) {
				productesList.add(
                    new Product(
                        result.getInt("id"), 
                        result.getString("name"), 
                        result.getInt("price"),
                        result.getInt("stock"),
                        result.getDate("initialCatalogDate").toLocalDate(),
                        result.getDate("endCatalogDate").toLocalDate()
                    )
                        
                );
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return productesList;
	}

    public boolean save(Product product){
		try {
			String sql = "";
			PreparedStatement stmt = null;
			if (this.find(product.getId()) == null){
				sql = "INSERT INTO persones VALUES(?,?,?,?,?)";
				stmt = conexionBD.prepareStatement(sql);
				int i = 1;
				stmt.setInt(i++, product.getId());
				stmt.setString(i++, product.getName());
				stmt.setString(i++, product.getPrice());
				stmt.setString(i++, product.getEmail());
				stmt.setString(i++, product.getTelefon());
			} else{
				sql = "UPDATE persones SET nom=?,apellidos=?,email=?,telefon=? WHERE id = ?";
				stmt = conexionBD.prepareStatement(sql);
				int i = 1;
				stmt.setString(i++, persona.getNom());
				stmt.setString(i++, persona.getApellidos());
				stmt.setString(i++, persona.getEmail());
				stmt.setString(i++, persona.getTelefon());
				stmt.setInt(i++, persona.getId());
			}
			int rows = stmt.executeUpdate();
			if (rows == 1) return true;
			else return false;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}


    @Override
    public Product add(Product p) {
        if (p!=null) {
            map.put(p.getId(),p);
            return p;
        } else {
            return null;
        }
    }

    @Override
    public Product search(Integer id) {
        return map.get(id);
    }

    @Override
    public Product delete(Integer id) {
        return map.remove(id);
    }

    

    @Override
    public HashMap<Integer, Product> getMap() {
        return this.map;
    }
    
    public boolean existObj(Integer id){
        return map.containsKey(id);
    }

    public ArrayList showDiscontinued(LocalDate date){
        ArrayList discontinuedProducts = new ArrayList<>();

        for (Product product : this.map.values()) {
            if(product.getEndCatalogDate().compareTo(date) == 0){
                discontinuedProducts.add("El producto "+product.getName()+" se va a descatalogar hoy");
            }else if(product.getEndCatalogDate().compareTo(date) < 0){
                long days = ChronoUnit.DAYS.between(product.getEndCatalogDate(),date);
                discontinuedProducts.add("El producto "+product.getName()+" lleva "+days+" dias descatalogado");
            }
        }
        
        return discontinuedProducts;
    }

    public void save(){
        System.out.println("---------------------");
        System.out.println("Guardando archivos...");
        System.out.println("---------------------\n");

        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("products.dat"));
            oos.writeObject(this.map); 
            oos.close();
            
        } catch (Exception e) {
            System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXX");
            System.out.println("Fallo al guardar el archivo");
            System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXX\n");
            e.printStackTrace();
        }
        System.out.println("------------------");
        System.out.println("Archivos guardados");
        System.out.println("------------------\n");
    }

    public void load(){
        System.out.println("--------------------");
        System.out.println("Cargando archivos...");
        System.out.println("--------------------\n");
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("products.dat"));
            try {   
                this.map = (HashMap<Integer,Product>)ois.readObject();
                ois.close();
            } catch (Exception e) {
                System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXX");
                System.out.println("Fallo al cargar el archivo");
                System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXX\n");

                e.printStackTrace();
            }
            System.out.println("-----------------");
            System.out.println("Archivos cargados");
            System.out.println("-----------------\n");

        } catch (Exception e) {
            System.out.println("XXXXXXXXXXXXXXXXXXXX");
            System.out.println("El archivo no existe");
            System.out.println("XXXXXXXXXXXXXXXXXXXX\n");
        }
        
            
        


        
    }
}