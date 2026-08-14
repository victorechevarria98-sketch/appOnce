/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.modelo.entidad;

/**
 *
 * @author Alumnos
 */
public class Rasca {
    
    private int idrasca;
    private String nombreRasca;
    private double precioRasca;

    public Rasca() {
    }

    public Rasca(String nombreRasca, double precioRasca) {
        this.nombreRasca = nombreRasca;
        this.precioRasca = precioRasca;
    }

    public String getNombreRasca() {
        return nombreRasca;
    }

    public void setNombreRasca(String nombreRasca) {
        this.nombreRasca = nombreRasca;
    }

    public double getPrecioRasca() {
        return precioRasca;
    }

    public void setPrecioRasca(double precioRasca) {
        this.precioRasca = precioRasca;
    }

    public int getIdrasca() {
        return idrasca;
    }

    public void setIdrasca(int idrasca) {
        this.idrasca = idrasca;
    }

    @Override
    public String toString() {
        return "Rasca{" + "idrasca=" + idrasca + ", nombreRasca=" + nombreRasca + ", precioRasca=" + precioRasca + '}';
    }

    

   
}
