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
public class PedidoCupon {
    
    private int idpedidocupon;
    private String numSerierCupon;
    private LocalDateTime fechaPedidoCupon;
    private int cantPedidoC;
    private int idcup;
    private int idtrab;

    public PedidoCupon() {
    }

    public PedidoCupon( String numSerierCupon, LocalDateTime fechaPedidoCupon, int cantPedidoC, int idcup, int idtrab) {
        this.numSerierCupon = numSerierCupon;
        this.fechaPedidoCupon = fechaPedidoCupon;
        this.cantPedidoC = cantPedidoC;
        this.idcup = idcup;
        this.idtrab = idtrab;
    }

    

    public String getNumSerierCupon() {
        return numSerierCupon;
    }

    public void setNumSerierCupon(String numSerierCupon) {
        this.numSerierCupon = numSerierCupon;
    }

    public LocalDateTime getFechaPedidoCupon() {
        return fechaPedidoCupon;
    }

    public void setFechaPedidoCupon(LocalDateTime fechaPedidoCupon) {
        this.fechaPedidoCupon = fechaPedidoCupon;
    }

    public int getCantPedidoC() {
        return cantPedidoC;
    }

    public void setCantPedidoC(int cantPedidoC) {
        this.cantPedidoC = cantPedidoC;
    }

    public int getIdpedidocupon() {
        return idpedidocupon;
    }

    public void setIdpedidocupon(int idpedidocupon) {
        this.idpedidocupon = idpedidocupon;
    }

    public int getIdcup() {
        return idcup;
    }

    public void setIdcup(int idcup) {
        this.idcup = idcup;
    }

    public int getIdtrab() {
        return idtrab;
    }

    public void setIdtrab(int idtrab) {
        this.idtrab = idtrab;
    }

    @Override
    public String toString() {
        return "PedidoCupon{" + "idpedidocupon=" + idpedidocupon + ", numSerierCupon=" + numSerierCupon + ", fechaPedidoCupon=" + fechaPedidoCupon + ", cantPedidoC=" + cantPedidoC + ", idcup=" + idcup + ", idtrab=" + idtrab + '}';
    }

   
    
}
