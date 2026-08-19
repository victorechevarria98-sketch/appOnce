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
public class Incidencias {

    private int idincidencia;
    private String tipoIncident;
    private String comentario;
    private LocalDateTime fechaIncident;
    private int idtrab;
    private int idpedidorasca;
    private int idpedidocupon;
    private int idlugar;
    private boolean solucionada;

    public Incidencias() {
    }

    public Incidencias(int idincidencia, String tipoIncident, String comentario, LocalDateTime fechaIncident, int idtrab, int idpedidorasca, int idpedidocupon, int idlugar, boolean solucionada) {
        this.idincidencia = idincidencia;
        this.tipoIncident = tipoIncident;
        this.comentario = comentario;
        this.fechaIncident = fechaIncident;
        this.idtrab = idtrab;
        this.idpedidorasca = idpedidorasca;
        this.idpedidocupon = idpedidocupon;
        this.idlugar = idlugar;
        this.solucionada = solucionada;
    }

    public int getIdincidencia() {
        return idincidencia;
    }

    public void setIdincidencia(int idincidencia) {
        this.idincidencia = idincidencia;
    }

    public String getTipoIncident() {
        return tipoIncident;
    }

    public void setTipoIncident(String tipoIncident) {
        this.tipoIncident = tipoIncident;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDateTime getFechaIncident() {
        return fechaIncident;
    }

    public void setFechaIncident(LocalDateTime fechaIncident) {
        this.fechaIncident = fechaIncident;
    }

    public int getIdtrab() {
        return idtrab;
    }

    public void setIdtrab(int idtrab) {
        this.idtrab = idtrab;
    }

    public int getIdpedidorasca() {
        return idpedidorasca;
    }

    public void setIdpedidorasca(int idpedidorasca) {
        this.idpedidorasca = idpedidorasca;
    }

    public int getIdpedidocupon() {
        return idpedidocupon;
    }

    public void setIdpedidocupon(int idpedidocupon) {
        this.idpedidocupon = idpedidocupon;
    }

    public int getIdlugar() {
        return idlugar;
    }

    public void setIdlugar(int idlugar) {
        this.idlugar = idlugar;
    }

    public boolean isSolucionada() {
        return solucionada;
    }

    public void setSolucionada(boolean solucionada) {
        this.solucionada = solucionada;
    }

    @Override
    public String toString() {
        return "Incidencias{" + "idincidencia=" + idincidencia + ", tipoIncident=" + tipoIncident + ", comentario=" + comentario + ", fechaIncident=" + fechaIncident + ", idtrab=" + idtrab + ", idpedidorasca=" + idpedidorasca + ", idpedidocupon=" + idpedidocupon + ", idlugar=" + idlugar + ", solucionada=" + solucionada + '}';
    }

 
   
}
