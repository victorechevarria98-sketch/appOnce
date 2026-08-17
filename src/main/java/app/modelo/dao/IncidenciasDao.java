/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.modelo.dao;

import app.modelo.entidad.Incidencias;
import app.modelo.entidad.Lugar;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 *
 * @author Alumnos
 */
public class IncidenciasDao {

    public IncidenciasDao() {
    }

    public boolean nuevaIncidencia(Incidencias in, Lugar lg) { // como no tenia al principio idlugar, lo tengo por separado, revisa mas tarde
        String insertincidenciasql = "insert into incidencias (\n"
                + "tipo_incident,\n"
                + "comentario,\n"
                + "fecha_incident,\n"
                + "id_trabajador,\n"
                + "id_pedidorasca,\n"
                + "id_pedidocupon,\n"
                + "id_lugar\n"
                + ") values (?,?,?,?,?,?,?)";      
        try (Connection con = ConexionDBOnce.Conexiondb(); PreparedStatement stmtinicdencia = con.prepareStatement(insertincidenciasql)) {
            LugarDao lgdao = new LugarDao();
            int idlugar = lgdao.nuevoLugar(lg);
            
            stmtinicdencia.setString(1, in.getTipoIncident());
            stmtinicdencia.setString(2, in.getComentario());
            stmtinicdencia.setObject(3, in.getFechaIncident());
            stmtinicdencia.setInt(4, in.getIdtrab());
            stmtinicdencia.setInt(7,idlugar);
            if (in.getIdrasca() != 0) {//como la incidencia es de cupon o rasca hay que mirar que hemos recibido y si cualde las 2 es
                stmtinicdencia.setInt(5, in.getIdrasca());
            } else {
                stmtinicdencia.setNull(5, java.sql.Types.INTEGER);
            }
            if (in.getIdcupon() != 0) {
                stmtinicdencia.setInt(6, in.getIdcupon());
            } else {
                stmtinicdencia.setNull(6, java.sql.Types.INTEGER);
            }
            int filas = stmtinicdencia.executeUpdate();
            
             if (filas == 1) {                
                return true;
            }
                              

        } catch (SQLException sqle) {
            System.out.println("Error! nuevaIncidencia" + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! nuevaIncidencia" + e.getMessage());
        }
        return false;
    }
}
