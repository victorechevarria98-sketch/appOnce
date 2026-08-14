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
public class PedidoRasca {

    private int idpedidorasca;
    private String numSerierRasca;
    private LocalDateTime fechaPedidoRasca;
    private int cantPedidoR;
    private int idras;
    private int idtrab;

    public PedidoRasca() {
    }

    public PedidoRasca( String numSerierRasca, LocalDateTime fechaPedidoRasca, int cantPedidoR, int idras, int idtrab) {
        this.numSerierRasca = numSerierRasca;
        this.fechaPedidoRasca = fechaPedidoRasca;
        this.cantPedidoR = cantPedidoR;
        this.idras = idras;
        this.idtrab = idtrab;
    }

   

    public String getNumSerierRasca() {
        return numSerierRasca;
    }

    public void setNumSerierRasca(String numSerierRasca) {
        this.numSerierRasca = numSerierRasca;
    }

    public LocalDateTime getFechaPedidoRasca() {
        return fechaPedidoRasca;
    }

    public void setFechaPedidoRasca(LocalDateTime fechaPedidoRasca) {
        this.fechaPedidoRasca = fechaPedidoRasca;
    }

    public int getCantPedidoR() {
        return cantPedidoR;
    }

    public void setCantPedidoR(int cantPedidoR) {
        this.cantPedidoR = cantPedidoR;
    }

    public int getIdpedidorasca() {
        return idpedidorasca;
    }

    public void setIdpedidorasca(int idpedidorasca) {
        this.idpedidorasca = idpedidorasca;
    }

    public int getIdras() {
        return idras;
    }

    public void setIdras(int idras) {
        this.idras = idras;
    }

    public int getIdtrab() {
        return idtrab;
    }

    public void setIdtrab(int idtrab) {
        this.idtrab = idtrab;
    }

    @Override
    public String toString() {
        return "PedidoRasca{" + "idpedidorasca=" + idpedidorasca + ", numSerierRasca=" + numSerierRasca + ", fechaPedidoRasca=" + fechaPedidoRasca + ", cantPedidoR=" + cantPedidoR + ", idras=" + idras + ", idtrab=" + idtrab + '}';
    }

    
    
}
