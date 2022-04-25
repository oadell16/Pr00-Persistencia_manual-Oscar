package model;

import java.time.LocalDate;
import java.util.List;

public final class Pack extends Productes{
    private List<Integer> ids;
    private int percentageDiscount;

    public Pack(Integer idProduct, String name, Float salePrice, int stock, LocalDate initialCatalogDate, LocalDate endCatalogDate, List<Integer> ids, int percentageDiscount) {
        super(idProduct, name, salePrice, stock, initialCatalogDate, endCatalogDate);
        this.ids = ids;
        this.percentageDiscount = percentageDiscount;
    }

    public Integer getPercentageDiscount() {
        return this.percentageDiscount;
    }
    
    public List<Integer> getIds() {
        return this.ids;
    }

    public void setListIds(List<Integer> listIds) {
        this.ids = ids;
    }

    public void setPercentageDiscount(int percentageDiscount) {
        this.percentageDiscount = percentageDiscount;
    }
    public void addProductToPack(int productId){
        this.ids.add(productId);
    }
    public void delProductToPack(String productId){
        this.ids.remove(productId);
    }

    @Override
    public String toString() {
        return super.toString()+", Pack [Productos del pack=" + ids + ", Descuento del pack=" + percentageDiscount + "]";
    }
    // @Override
    // public boolean equals(Object obj) {
    //     Pack pack = (Pack)obj;

    //     return this.ids.equals(pack.ids);
    // }

    public void imprimir(){
        super.imprimir();
        System.out.print("Ids dels productes del pack: ");

        for (Integer id : ids) {
            System.out.print(id+",");
        }

        System.out.print("\n");
		System.out.println("Descompte del pack: " + percentageDiscount);
	}

}
