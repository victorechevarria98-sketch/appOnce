/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.modelo.entidad;

/**
 *
 * @author Alumnos
 */
public class Lugar {

    private int idlugar;
    private String calle;
    private String municipio;
    private int codPostal;
    private int idtrab;

    public Lugar() {
    }

    public Lugar(String calle, String municipio, int codPostal, int idtrab) {
        this.calle = calle;
        this.municipio = municipio;
        this.codPostal = codPostal;
        this.idtrab = idtrab;
    }

   

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public int getCodPostal() {
        return codPostal;
    }

    public void setCodPostal(int codPostal) {
        this.codPostal = codPostal;
    }

    public int getIdlugar() {
        return idlugar;
    }

    public void setIdlugar(int idlugar) {
        this.idlugar = idlugar;
    }

    public int getIdtrab() {
        return idtrab;
    }

    public void setIdtrab(int idtrab) {
        this.idtrab = idtrab;
    }

    @Override
    public String toString() {
        return "Lugar{" + "idlugar=" + idlugar + ", calle=" + calle + ", municipio=" + municipio + ", codPostal=" + codPostal + ", idtrab=" + idtrab + '}';
    }

    
    
}
