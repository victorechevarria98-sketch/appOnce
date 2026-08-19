/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.modelo.dao;

import app.modelo.entidad.Incidencias;
import app.modelo.entidad.Lugar;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
            stmtinicdencia.setInt(7, idlugar);
            if (in.getIdpedidorasca() != 0) {//como la incidencia es de cupon o rasca hay que mirar que hemos recibido y si cualde las 2 es
                stmtinicdencia.setInt(5, in.getIdpedidorasca());
            } else {
                stmtinicdencia.setNull(5, java.sql.Types.INTEGER);
            }
            if (in.getIdpedidocupon() != 0) {
                stmtinicdencia.setInt(6, in.getIdpedidocupon());
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

    public int contarIncidencias() {
        String selectsql = "select COUNT(*) as 'numero'\n"
                + "from incidencias i";
        try (Connection con = ConexionDBOnce.Conexiondb(); Statement stmt = con.createStatement();) {
            ResultSet rs = stmt.executeQuery(selectsql);
            if (rs.next()) {
                return rs.getInt("numero");
            }
        } catch (SQLException sqle) {
            System.out.println("Error! contarIncidencias" + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! contarIncidencias" + e.getMessage());
        }
        return 0;
    }
    // buscas la tabla desde el dato sacado a traves de la pagina en la que estes

    public ArrayList<Map<String, Object>> obtenerIncidenciaPaginacion(int pagina) {
        String selectsql = "select i.id_incident ,i.tipo_incident , i.comentario, i.fecha_incident, i.id_trabajador,t.nombre_trab, r.nombre_rasca ,c.nombre_cupon , i.solucionada\n"
                + "from incidencias i\n"
                + "join trabajador t on i.id_trabajador = t.id_trabajador\n"
                + "left join pedidocupon p on i.id_pedidocupon = p.id_pedidocupon \n"
                + "left join pedidorasca p2 on i.id_pedidorasca = p2.id_pedidorasca \n"
                + "left join rasca r on r.id_rasca = p2.id_rasca \n"
                + "left join cupon c on c.id_cupon = p.id_cupon \n"
                + "order by fecha_incident desc\n"
                + "limit ?, 10";
        int datos = pagina * 10;
        try (Connection con = ConexionDBOnce.Conexiondb(); PreparedStatement stmt = con.prepareStatement(selectsql);) {
            stmt.setInt(1, datos);
            ResultSet rs = stmt.executeQuery();
            ArrayList<Map<String, Object>> lista = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> fila = new HashMap<>();
                fila.put("idincidencia", rs.getInt("id_incident"));
                fila.put("tipo", rs.getString("tipo_incident"));
                fila.put("comentario", rs.getString("comentario"));
                //revisa la fecha para entenderlo mejor
                java.sql.Timestamp timestamp = rs.getTimestamp("fecha_incident");
                fila.put("fecha", (timestamp != null) ? timestamp.toLocalDateTime() : null);
                fila.put("idtrabajador", rs.getInt("id_trabajador"));
                fila.put("nombretrabajador", rs.getString("nombre_trab"));
                fila.put("nombrecupon", rs.getString("nombre_cupon"));
                fila.put("nombrerasca", rs.getString("nombre_rasca"));
                fila.put("solucion", rs.getBoolean("solucionada"));
                lista.add(fila);
            }
            return lista;
        } catch (SQLException sqle) {
            System.out.println("Error! obtenerIncidenciaPaginacion" + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! obtenerIncidenciaPaginacion" + e.getMessage());
        }
        return null;
    }

    public Incidencias obtenerIncidenciaPorID(int id) {
        String selectsql = "select * \n"
                + "from incidencias i \n"
                + "where i.id_incident = ?";
        try (Connection con = ConexionDBOnce.Conexiondb(); PreparedStatement pstmt = con.prepareStatement(selectsql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Incidencias i = new Incidencias();
                i.setTipoIncident(rs.getString("tipo_incident"));
                i.setComentario(rs.getString("comentario"));
                i.setFechaIncident(rs.getTimestamp("fecha_incident").toLocalDateTime());
                i.setIdtrab(rs.getInt("id_trabajador"));
                i.setIdpedidocupon(rs.getInt("id_pedidocupon"));
                i.setIdpedidorasca(rs.getInt("id_pedidorasca"));
                i.setIdlugar(rs.getInt("id_lugar"));
                i.setSolucionada(rs.getBoolean("solucionada"));
                return i;
            }

        } catch (SQLException sqle) {
            System.out.println("Error! obtenerIncidenciaPorID" + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! obtenerIncidenciaPorID" + e.getMessage());
        }
        return null;
    }

    public boolean eliminarIncidecncia(int id) {
        String deletesql = "delete from incidencias where id_incident = ?";
        try (Connection con = ConexionDBOnce.Conexiondb(); PreparedStatement pstmt = con.prepareStatement(deletesql)) {
            pstmt.setInt(1, id);
            int filas = pstmt.executeUpdate();
            if (filas == 1) {
                return true;
            }

        } catch (SQLException sqle) {
            System.out.println("Error! eliminarIncidecncia" + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! eliminarIncidecncia" + e.getMessage());
        }
        return false;
    }

    public boolean updateIncidencias(Incidencias i) {
        String updatesql = "UPDATE incidencias \n"
                + "SET tipo_incident = ?, \n"
                + "    comentario = ?, \n"
                + "    fecha_incident = ?, \n"
                + "    id_trabajador = ?, \n"
                + "    id_pedidorasca = ?, \n"
                + "    id_pedidocupon = ?, \n"
                + "    id_lugar = ?, \n"
                + "    solucionada = ? \n"
                + "WHERE id_incident = ?;";
        try (Connection con = ConexionDBOnce.Conexiondb(); PreparedStatement stmtrabajador = con.prepareStatement(updatesql);) {

            stmtrabajador.setString(1, i.getTipoIncident());
            stmtrabajador.setString(2, i.getComentario());
            stmtrabajador.setTimestamp(3, Timestamp.valueOf(i.getFechaIncident()));
            stmtrabajador.setInt(4, i.getIdtrab());
            stmtrabajador.setInt(7, i.getIdlugar());
            stmtrabajador.setBoolean(8, i.isSolucionada());
            stmtrabajador.setInt(8, i.getIdincidencia());
            if (i.getIdpedidocupon() == 0) {
                stmtrabajador.setInt(5, i.getIdpedidorasca());
                stmtrabajador.setNull(6, java.sql.Types.INTEGER);
            } else {
                stmtrabajador.setNull(5, java.sql.Types.INTEGER);
                stmtrabajador.setInt(6, i.getIdpedidocupon());
            }

            int filas = stmtrabajador.executeUpdate();
            if (filas == 1) {
                return true;
            }

        } catch (SQLException sqle) {
            System.out.println("Error! updateIncidencias" + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! updateIncidencias" + e.getMessage());
        }
        return false;
    }
}
