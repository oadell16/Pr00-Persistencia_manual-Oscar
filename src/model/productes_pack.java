package model;

public class productes_pack {
    private int id_pack;
    private int id_producte;

    public productes_pack(Integer id_pack, Integer id_producte) {
        this.id_pack = id_pack;
        this.id_producte = id_producte;
    }

    public int getId_pack() {
        return id_pack;
    }

    public void setId_pack(int id_pack) {
        this.id_pack = id_pack;
    }

    public int getId_producte() {
        return id_producte;
    }

    public void setId_producte(int id_producte) {
        this.id_producte = id_producte;
    }

    
    
}
