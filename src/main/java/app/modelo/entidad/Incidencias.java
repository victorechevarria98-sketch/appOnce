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
    private int idrasca;
    private int idcupon;
    private int idlugar;

    public Incidencias() {
    }

    public Incidencias( String tipoIncident, String comentario, LocalDateTime fechaIncident, int idtrab, int idrasca, int idcupon, int idlugar) {
        this.tipoIncident = tipoIncident;
        this.comentario = comentario;
        this.fechaIncident = fechaIncident;
        this.idtrab = idtrab;
        this.idrasca = idrasca;
        this.idcupon = idcupon;
        this.idlugar = idlugar;
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

    public int getIdincidencia() {
        return idincidencia;
    }

    public void setIdincidencia(int idincidencia) {
        this.idincidencia = idincidencia;
    }

    public int getIdtrab() {
        return idtrab;
    }

    public void setIdtrab(int idtrab) {
        this.idtrab = idtrab;
    }

    public int getIdrasca() {
        return idrasca;
    }

    public void setIdrasca(int idrasca) {
        this.idrasca = idrasca;
    }

    public int getIdcupon() {
        return idcupon;
    }

    public void setIdcupon(int idcupon) {
        this.idcupon = idcupon;
    }

    public int getIdlugar() {
        return idlugar;
    }

    public void setIdlugar(int idlugar) {
        this.idlugar = idlugar;
    }

    @Override
    public String toString() {
        return "Incidencias{" + "idincidencia=" + idincidencia + ", tipoIncident=" + tipoIncident + ", comentario=" + comentario + ", fechaIncident=" + fechaIncident + ", idtrab=" + idtrab + ", idrasca=" + idrasca + ", idcupon=" + idcupon + ", idlugar=" + idlugar + '}';
    }

   
   
}
