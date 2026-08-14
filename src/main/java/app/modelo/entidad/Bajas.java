/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.modelo.entidad;

import java.time.LocalDate;
import java.util.Date;

/**
 *
 * @author Alumnos
 */
public class Bajas {

    private int idbaja;
    private Date fechaBajaLaboral;
    private Date fechaFinBaja;
    private int idtrab;

    public Bajas() {
    }

    public Bajas( Date fechaBajaLaboral, Date fechaFinBaja, int idtrab) {
        this.fechaBajaLaboral = fechaBajaLaboral;
        this.fechaFinBaja = fechaFinBaja;
        this.idtrab = idtrab;
    }

    

    public Date getFechaBajaLaboral() {
        return fechaBajaLaboral;
    }

    public void setFechaBajaLaboral(Date fechaBajaLaboral) {
        this.fechaBajaLaboral = fechaBajaLaboral;
    }

    public Date getFechaFinBaja() {
        return fechaFinBaja;
    }

    public void setFechaFinBaja(Date fechaFinBaja) {
        this.fechaFinBaja = fechaFinBaja;
    }

    public int getIdbaja() {
        return idbaja;
    }

    public void setIdbaja(int idbaja) {
        this.idbaja = idbaja;
    }

    public int getIdtrab() {
        return idtrab;
    }

    public void setIdtrab(int idtrab) {
        this.idtrab = idtrab;
    }

    @Override
    public String toString() {
        return "Bajas{" + "idbaja=" + idbaja + ", fechaBajaLaboral=" + fechaBajaLaboral + ", fechaFinBaja=" + fechaFinBaja + ", idtrab=" + idtrab + '}';
    }

    

  
}
