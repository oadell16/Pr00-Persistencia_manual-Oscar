package model;

import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;
import java.sql.SQLType;

public class Direccion{
    private String localidad;
    private String provincia;
    private String cod_postal;
    
    public Direccion(String localidad, String provincia, String cod_postal, String domicilio) {
        this.localidad = localidad;
        this.provincia = provincia;
        this.cod_postal = cod_postal;
        this.domicilio = domicilio;
    }

    private String domicilio;

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getCod_postal() {
        return cod_postal;
    }

    public void setCod_postal(String cod_postal) {
        this.cod_postal = cod_postal;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

}
