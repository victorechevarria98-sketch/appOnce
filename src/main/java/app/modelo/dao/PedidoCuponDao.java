/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.modelo.dao;

import app.modelo.entidad.PedidoCupon;
import app.modelo.entidad.Trabajador;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Alumnos
 */
public class PedidoCuponDao {

    public PedidoCuponDao() {
    }

    //introducir un nuevo pedido con la fecha de ahora 
    public boolean insertarPedidoCuponPorTrabajador(int idcupon, int cantidad, String emailusuario) {

        String insertsql = "insert into pedidocupon (\n"
                + "    num_seriecupon, \n"
                + "    fecha_pedidocupon,\n"
                + "    cant_pedidoC,\n"
                + "    id_cupon, \n"
                + "    id_trabajador\n"
                + ") values (?,now(),?,?,?)";
        try (Connection con = ConexionDBOnce.Conexiondb(); PreparedStatement stmtinsert = con.prepareStatement(insertsql);) {

            TrabajadorDao tbdao = new TrabajadorDao();
            Trabajador tb = tbdao.obtenerIdTrabajadorPorEmail(emailusuario);

            if (tb == null) {
                System.out.println("no hay usuario con ese correo insertarPedidoCuponPorTrabajador");
                return false;
            }

            stmtinsert.setString(1, TrabajadorDao.generarCodigoFormat());
            stmtinsert.setInt(2, cantidad);
            stmtinsert.setInt(3, idcupon);
            stmtinsert.setInt(4, tb.getIdtrab());

            int filas = stmtinsert.executeUpdate();

            if (filas == 1) {
                return true;
            }
        } catch (SQLException sqle) {
            System.out.println("Error! insertarPedidoCuponPorTrabajador" + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! insertarPedidoCuponPorTrabajador" + e.getMessage());
        }
        return false;
    }

    public ArrayList<Map<String, Object>> pedidoCupon() { // metodo para extraer 5 ultimos pedidos por fecha
        String selectsql = "select p.id_pedidocupon ,concat(t.nombre_trab,' ',t.apellidos_trab) as 'Trabajador:',p.fecha_pedidocupon as 'Fecha:',c.nombre_cupon as 'Cupon:' ,"
                + "p.num_seriecupon as 'Numero de serie:',"
                + "p.cant_pedidoC as 'pedido de:', "
                + "c.preciocupon as 'precio:'\n"
                + "from trabajador t join pedidocupon p on p.id_trabajador = t.id_trabajador \n"
                + "join cupon c  on c.id_cupon = p.id_cupon \n"
                + "order by p.fecha_pedidocupon desc \n"
                + "limit 5";
        // conecto y extraigo en un mismo try
        try (Connection con = ConexionDBOnce.Conexiondb(); Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(selectsql);
            ArrayList<Map<String, Object>> tabla = new ArrayList<>();
            //todo al mismo array list
            while (rs.next()) {
                //cada posicion del arraylist tiene un hashmap con una clave y valor para cada posicion de la fila
                Map<String, Object> fila = new HashMap<>();
                //clave y rs con cada columna
                fila.put("id_pedidocupon", rs.getInt("id_pedidocupon"));
                fila.put("Trabajador", rs.getString("Trabajador:"));
                fila.put("fechacupon", rs.getTimestamp("fecha:"));
                fila.put("cupon", rs.getString("Cupon:"));
                fila.put("serie", rs.getString("Numero de serie:"));
                fila.put("pedido", rs.getInt("pedido de:"));
                fila.put("precio", rs.getDouble("precio:"));
                //añadimos el hasmap a una fila
                tabla.add(fila);
            }
            // devolvemos el array y capturamos errores
            return tabla;
        } catch (SQLException sqle) {
            System.out.println("Error! pedidoCupon" + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! pedidoCupon" + e.getMessage());
        }
        return null;
    }

    // esta lista es para comprobar si el nombre que se extrae de un formulario coincide con alguno de la base de datos
    public ArrayList<String> listaCuponNombre() {
        String consultasql = "select c.nombre_cupon \n"
                + "from cupon c ";
        try (Connection con = ConexionDBOnce.Conexiondb(); Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(consultasql);
            //metemos en un array cada nombre en una posicion
            ArrayList<String> listaCompleta = new ArrayList<>();
            while (rs.next()) {

                listaCompleta.add(rs.getString("nombre_cupon"));
            }
            return listaCompleta;
        } catch (SQLException sqle) {
            System.out.println("Error! listaCuponNombre " + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! listaCuponNombre " + e.getMessage());
        }

        return null;
    }

    public int obtenerIdCupon(String producto) {
        String selectsql = "select c.id_cupon  \n"
                + "from cupon c \n"
                + "where c.nombre_cupon = ?";
        try (Connection con = ConexionDBOnce.Conexiondb(); PreparedStatement pstmt = con.prepareStatement(selectsql)) {
            pstmt.setString(1, producto);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_cupon");
            }

        } catch (SQLException sqle) {
            System.out.println("Error! obtenerIdCupon" + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! obtenerIdCupon" + e.getMessage());
        }
        return 0;
    }

    public boolean eliminarPedidoCupon(int id) {
        String deletesql = "delete from pedidocupon where id_pedidocupon = ?";
        try (Connection con = ConexionDBOnce.Conexiondb(); PreparedStatement pstmt = con.prepareStatement(deletesql)) {
            pstmt.setInt(1, id);
            int filas = pstmt.executeUpdate();
            if (filas == 1) {
                return true;
            }

        } catch (SQLException sqle) {
            System.out.println("Error! eliminartrabajador" + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! eliminartrabajador" + e.getMessage());
        }
        return false;
    }

    public Map<String, Object> obtenerPedidoPorIDPedidoCupon(int id) {
// saco la tabla pedido, nombre , nombreproducto
        String sentenciasql = "select t.nombre_trab,t.apellidos_trab,p.fecha_pedidocupon as 'Fecha:',c.nombre_cupon as 'Cupon:' ,"
                + "p.num_seriecupon as 'Numero de serie:',"
                + "p.cant_pedidoC as 'pedido de:', "
                + "c.preciocupon as 'precio:'\n"
                + "from trabajador t join pedidocupon p on p.id_trabajador = t.id_trabajador \n"
                + "join cupon c  on c.id_cupon = p.id_cupon \n"
                + "where p.id_pedidocupon = ?";
        try (Connection con = ConexionDBOnce.Conexiondb(); PreparedStatement pstmt = con.prepareStatement(sentenciasql)) {
            // where para la id del pedido que he recibido por valor 
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            // como solo hay un pedido por id no necestio array
            Map<String, Object> fila = new HashMap<>();
            if (rs.next()) {
                //metemos en el map una clave y valor por cada columna de la fila
                fila.put("TrabajadorNombre", rs.getString("nombre_trab"));
                fila.put("TrabajadorApellidos", rs.getString("apellidos_trab"));
                Timestamp fechalinea = rs.getTimestamp("fecha:");
                //pasamos la fecha a un localdatetmie con el format pero en una sola lines
                String fechaParaHtml = fechalinea.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
                fila.put("fechacupon", fechaParaHtml);
                fila.put("producto", rs.getString("Cupon:"));
                fila.put("serie", rs.getString("Numero de serie:"));
                fila.put("pedido", rs.getInt("pedido de:"));
                fila.put("precio", rs.getDouble("precio:"));
            }
            return fila;
        } catch (SQLException sqle) {
            System.out.println("Error! obtenerPedidoPorIDPedidoCupon " + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! obtenerPedidoPorIDPedidoCupon " + e.getMessage());
        }
        return null;
    }

    public boolean updatePedidoCupon(PedidoCupon c) {
        String updatesql = "update pedidocupon set fecha_pedidocupon = ? , num_seriecupon = ?, cant_pedidoC = ?, id_cupon = ? "
                + "where id_pedidocupon = ?";
        try (Connection con = ConexionDBOnce.Conexiondb(); PreparedStatement stmtrabajador = con.prepareStatement(updatesql);) {

            stmtrabajador.setObject(1, c.getFechaPedidoCupon());
            stmtrabajador.setString(2, c.getNumSerierCupon());
            stmtrabajador.setInt(3, c.getCantPedidoC());
            stmtrabajador.setInt(4, c.getIdcup());
            stmtrabajador.setInt(5, c.getIdpedidocupon());

            int filas = stmtrabajador.executeUpdate();
            if (filas == 1) {
                return true;
            }

        } catch (SQLException sqle) {
            System.out.println("Error! updatePedidoCupon" + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! updatePedidoCupon" + e.getMessage());
        }
        return false;
    }

}
