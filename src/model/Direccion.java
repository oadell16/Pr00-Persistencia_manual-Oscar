package model;

import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;
import java.sql.SQLType;

public class Direccion implements SQLType {
    private String localidad;
    private String provincia;
    private String cod_postal;
    private String domicilio;
    private String sql_type;

    public String getSQLTypeName() {
        return sql_type;
    }

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

    @Override
    public String getName() {
        return "Direccion";
    }

    @Override
    public String getVendor() {
        return "provincia = "+this.provincia+", localidad = "+this.localidad+", cod_postal = "+this.cod_postal+", domicilio = "+this.domicilio;
    }

    @Override
    public Integer getVendorTypeNumber() {
        // TODO Auto-generated method stub
        return null;
    }
}
