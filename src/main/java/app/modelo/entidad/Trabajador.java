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
public class Trabajador {

    private int idtrab;
    private String nombreTrab;
    private String apellidosTrab;
    private String NIF_Trab;
    private Date fechaNaTrab;
    private Date fechaIncorTrab;
    private int tldEmp;
    private boolean bajaLaboral;

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
    private int idusu;

    public Trabajador() {
    }

    public Trabajador(String nombreTrab, String apellidosTrab, String NIF_Trab, Date fechaNaTrab, Date fechaIncorTrab, int tldEmp, boolean bajaLaboral, Kiosko tipoKiosko, Contrato tipoContrato, Actividad tipoActividad, int idusu) {
        this.nombreTrab = nombreTrab;
        this.apellidosTrab = apellidosTrab;
        this.NIF_Trab = NIF_Trab;
        this.fechaNaTrab = fechaNaTrab;
        this.fechaIncorTrab = fechaIncorTrab;
        this.tldEmp = tldEmp;
        this.bajaLaboral = bajaLaboral;
        this.tipoKiosko = tipoKiosko;
        this.tipoContrato = tipoContrato;
        this.tipoActividad = tipoActividad;
        this.idusu = idusu;
    }

    public String getNombreTrab() {
        return nombreTrab;
    }

    public void setNombreTrab(String nombreTrab) {
        this.nombreTrab = nombreTrab;
    }

    public String getApellidosTrab() {
        return apellidosTrab;
    }

    public void setApellidosTrab(String apellidosTrab) {
        this.apellidosTrab = apellidosTrab;
    }

    public String getNIF_Trab() {
        return NIF_Trab;
    }

    public void setNIF_Trab(String NIF_Trab) {
        this.NIF_Trab = NIF_Trab;
    }

    public Date getFechaNaTrab() {
        return fechaNaTrab;
    }

    public void setFechaNaTrab(Date fechaNaTrab) {
        this.fechaNaTrab = fechaNaTrab;
    }

    public Date getFechaIncorTrab() {
        return fechaIncorTrab;
    }

    public void setFechaIncorTrab(Date fechaIncorTrab) {
        this.fechaIncorTrab = fechaIncorTrab;
    }

    public int getTldEmp() {
        return tldEmp;
    }

    public void setTldEmp(int tldEmp) {
        this.tldEmp = tldEmp;
    }

    public boolean isBajaLaboral() {
        return bajaLaboral;
    }

    public void setBajaLaboral(boolean bajaLaboral) {
        this.bajaLaboral = bajaLaboral;
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

    public int getIdusu() {
        return idusu;
    }

    public void setIdusu(int idusu) {
        this.idusu = idusu;
    }

    public int getIdtrab() {
        return idtrab;
    }

    public void setIdtrab(int idtrab) {
        this.idtrab = idtrab;
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
        return "Trabajador{" + "idtrab=" + idtrab + ", nombreTrab=" + nombreTrab + ", apellidosTrab=" + apellidosTrab + ", NIF_Trab=" + NIF_Trab + ", fechaNaTrab=" + fechaNaTrab + ", fechaIncorTrab=" + fechaIncorTrab + ", tldEmp=" + tldEmp + ", bajaLaboral=" + bajaLaboral + ", tipoKiosko=" + tipoKiosko + ", tipoContrato=" + tipoContrato + ", tipoActividad=" + tipoActividad + ", idusu=" + idusu + '}';
    }

}
