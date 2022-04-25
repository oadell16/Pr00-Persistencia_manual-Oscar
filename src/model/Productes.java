package model;

import java.io.Serializable;
import java.time.LocalDate;

public class Productes implements Serializable, Identificable, Comparable <Productes>{
    private static final long serialversionUID = 1234567890l;
    private Integer id;
    private String name;
    private Float price;
    private int stock;
    private LocalDate initialCatalogDate;
    private LocalDate endCatalogDate;

    public Productes(Integer id, String name, Float salePrice, int stock, LocalDate initialCatalogDate, LocalDate endCatalogDate) {
        this.id = id;
        this.name = name;
        this.price = salePrice;
        this.stock = stock;
        this.initialCatalogDate = initialCatalogDate;
        this.endCatalogDate = endCatalogDate;
    }

    public int getId() {
        return id;
    }

    public LocalDate getInitialCatalogDate() {
        return initialCatalogDate;
    }

    public void setInitialCatalogDate(LocalDate initialCatalogDate) {
        this.initialCatalogDate = initialCatalogDate;
    }

    public LocalDate getEndCatalogDate() {
        return endCatalogDate;
    }

    public void setEndCatalogDate(LocalDate endCatalogDate) {
        this.endCatalogDate = endCatalogDate;
    }

    public void setid(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrice() {
        return this.price;
    }

    public void setPrice(Float salePrice) {
        this.price = salePrice;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void putStock(int addStock) {
        this.stock=this.stock+addStock;
    }

    public void takeStock(int subtractStock) {
        this.stock=this.stock-subtractStock;
    }

    @Override
    public String toString() {
        return "Producto [Id del producto=" + id + ", nombre del producto=" + name + ", Precio del producto=" + price + ", stock del producto=" + stock + ", Fecha inicial catalogo="+initialCatalogDate+", Fecha final catalogo="+endCatalogDate+"]";
    }

    // @Override
    // public boolean equals(Object obj) {
    //     Productes p = (Productes)obj;
        
    //     return this.name.equals(p.name);
    // }

    @Override
    public int compareTo(Productes product) {
        return this.id.compareTo(product.id);
    }

    public void imprimir(){
		System.out.println("Id: " + id);
		System.out.println("Nom: " + name);
		System.out.println("Preu: " + price);
		System.out.println("Stock: " + stock);
		System.out.println("Data inicial del catalog: " + initialCatalogDate);
		System.out.println("Data final del catalog: " + endCatalogDate);
	}
}
