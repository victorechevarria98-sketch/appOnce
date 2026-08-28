/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.modelo.entidad;

import java.time.LocalDateTime;

/**
 *
 * @author Alumnos
 */
public class Filtro {

    private String nombre;
    private String apellidos;
    private String combinacion;
    private String nombreProducto;
    private String numeroSerie;
    private int cantidad;
    private double precio;
    private LocalDateTime fechaPedido;
    private String municipio;

    public enum Kiosko {
        fijo, movil
    };
    private Kiosko tipoKiosko;

    public enum Contrato {
        indefinido, temporal
    };
    private Contrato tipoContrato;

    public enum Actividad {
        permanente, cambiable
    };
    private Actividad tipoActividad;
    private int codigoPostal;

    public enum rol {
        admin, trabajador
    };
    private rol perfil;
    private int idPedidoCupon;

    public Filtro() {
    }

    public Filtro(String nombre, String apellidos, String combinacion, String nombreProducto, String numeroSerie, int cantidad, double precio, LocalDateTime fechaPedido, String municipio, Kiosko tipoKiosko, Contrato tipoContrato, Actividad tipoActividad, int codigoPostal, rol perfil, int idPedidoCupon) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.combinacion = combinacion;
        this.nombreProducto = nombreProducto;
        this.numeroSerie = numeroSerie;
        this.cantidad = cantidad;
        this.precio = precio;
        this.fechaPedido = fechaPedido;
        this.municipio = municipio;
        this.tipoKiosko = tipoKiosko;
        this.tipoContrato = tipoContrato;
        this.tipoActividad = tipoActividad;
        this.codigoPostal = codigoPostal;
        this.perfil = perfil;
        this.idPedidoCupon = idPedidoCupon;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCombinacion() {
        return combinacion;
    }

    public void setCombinacion(String combinacion) {
        this.combinacion = combinacion;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDateTime fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public Kiosko getTipoKiosko() {
        return tipoKiosko;
    }

    public void setTipoKiosko(Kiosko tipoKiosko) {
        this.tipoKiosko = tipoKiosko;
    }

    public Contrato getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(Contrato tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public Actividad getTipoActividad() {
        return tipoActividad;
    }

    public void setTipoActividad(Actividad tipoActividad) {
        this.tipoActividad = tipoActividad;
    }

    public int getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(int codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public rol getPerfil() {
        return perfil;
    }

    public void setPerfil(rol perfil) {
        this.perfil = perfil;
    }

    public int getIdPedidoCupon() {
        return idPedidoCupon;
    }

    public void setIdPedidoCupon(int idPedidoCupon) {
        this.idPedidoCupon = idPedidoCupon;
    }

    public static Object getEstadoFromString(String estadoStr) {

        switch (estadoStr.toLowerCase()) {

            case "fijo":
                return Kiosko.fijo;

            case "movil":
                return Kiosko.movil;

            case "indefinido":
                return Contrato.indefinido;

            case "temporal":
                return Contrato.temporal;

            case "permanente":
                return Actividad.permanente;
            case "cambiable":
                return Actividad.cambiable;

        }
        return null;
    }

    @Override
    public String toString() {
        return "Filtro{" + "nombre=" + nombre + ", apellidos=" + apellidos + ", combinacion=" + combinacion + ", nombreProducto=" + nombreProducto + ", numeroSerie=" + numeroSerie + ", cantidad=" + cantidad + ", precio=" + precio + ", fechaPedido=" + fechaPedido + ", municipio=" + municipio + ", tipoKiosko=" + tipoKiosko + ", tipoContrato=" + tipoContrato + ", tipoActividad=" + tipoActividad + ", codigoPostal=" + codigoPostal + ", perfil=" + perfil + ", idPedidoCupon=" + idPedidoCupon + '}';
    }
    
}
