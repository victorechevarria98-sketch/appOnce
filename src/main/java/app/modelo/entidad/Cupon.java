/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.modelo.entidad;

/**
 *
 * @author Alumnos
 */
public class Cupon {
    
    private int idcupon;
    private String nombreCupon;
    private double precioCupon;

    public Cupon() {
    }

    public Cupon(String nombreCupon, double precioCupon) {
        this.nombreCupon = nombreCupon;
        this.precioCupon = precioCupon;
    }
   

    public String getNombreCupon() {
        return nombreCupon;
    }

    public void setNombreCupon(String nombreCupon) {
        this.nombreCupon = nombreCupon;
    }

    public double getPrecioCupon() {
        return precioCupon;
    }

    public void setPrecioCupon(double precioCupon) {
        this.precioCupon = precioCupon;
    }

    public int getIdcupon() {
        return idcupon;
    }

    public void setIdcupon(int idcupon) {
        this.idcupon = idcupon;
    }

    @Override
    public String toString() {
        return "Cupon{" + "idcupon=" + idcupon + ", nombreCupon=" + nombreCupon + ", precioCupon=" + precioCupon + '}';
    }

   

   
}
