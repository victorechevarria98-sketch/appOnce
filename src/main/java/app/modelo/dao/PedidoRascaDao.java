/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.modelo.dao;

import app.modelo.entidad.PedidoRasca;
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
public class PedidoRascaDao {

    public PedidoRascaDao() {
    }

    // ten en cuenta que la fecha es en el momento del insert y que esta funcion es de cuando la entidad pedido rasca estaba incompleta
    public boolean insertarPedidoRascaPorTrabajador(int idrasca, int cantidad, String emailusuario) {

        String insertsql = "insert into pedidorasca (\n"
                + "num_serierasca,\n"
                + "fecha_pedidorasca,\n"
                + "cant_pedidoR,\n"
                + "id_rasca,\n"
                + "id_trabajador\n"
                + ") values (?,now(),?,?,?)";
        try (Connection con = ConexionDBOnce.Conexiondb(); PreparedStatement stmtinsert = con.prepareStatement(insertsql);) {

            TrabajadorDao tbdao = new TrabajadorDao();
            Trabajador tb = tbdao.obtenerIdTrabajadorPorEmail(emailusuario);

            if (tb == null) {
                System.out.println("no hay usuario con ese correo insertarPedidoRascaPorTrabajador");
                return false;
            }

            stmtinsert.setString(1, TrabajadorDao.generarCodigoFormat());
            stmtinsert.setInt(2, cantidad);
            stmtinsert.setInt(3, idrasca);
            stmtinsert.setInt(4, tb.getIdtrab());

            int filas = stmtinsert.executeUpdate();

            if (filas == 1) {
                return true;
            }
        } catch (SQLException sqle) {
            System.out.println("Error! insertarPedidoRascaPorTrabajador" + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! insertarPedidoRascaPorTrabajador" + e.getMessage());
        }
        return false;
    }

    public ArrayList<Map<String, Object>> pedidoRasca() { // 5 pedidos de rasca ordenados por fecha mas reciente
        String selectsql = "select p.id_pedidorasca, concat(t.nombre_trab,' ',t.apellidos_trab) as 'Trabajador:',p.fecha_pedidorasca as 'Fecha:'"
                + ",r.nombre_rasca  as 'Rasca:' ,p.num_serierasca  as 'Numero de serie:',p.cant_pedidoR  as 'pedido de:', "
                + "r.preciorasca  as 'precio:'\n"
                + "from trabajador t\n"
                + "join pedidorasca p on p.id_trabajador = t.id_trabajador \n"
                + "join rasca r  on r.id_rasca  = p.id_rasca \n"
                + "order by p.fecha_pedidorasca  desc \n"
                + "limit 5";
        try (Connection con = ConexionDBOnce.Conexiondb(); Statement stmt = con.createStatement()) {
            //conectamos y ejecutamos la consulta
            ResultSet rs = stmt.executeQuery(selectsql);
            // array para cada fila una posicion
            ArrayList<Map<String, Object>> tabla = new ArrayList<>();
            while (rs.next()) {
                //creamos un hashmap donde cada clave y valor es una posicion de la columna de una fila
                Map<String, Object> fila = new HashMap<>();
                fila.put("id_pedidorasca", rs.getInt("id_pedidorasca"));
                fila.put("Trabajador", rs.getString("Trabajador:"));
                fila.put("fecharasca", rs.getTimestamp("fecha:"));
                fila.put("rasca", rs.getString("Rasca:"));
                fila.put("serie", rs.getString("Numero de serie:"));
                fila.put("pedido", rs.getInt("pedido de:"));
                fila.put("precio", rs.getDouble("precio:"));
                //añadimos el map a la posicion de array
                tabla.add(fila);
            }
            // devolvemos array y capturamos errores si es necesrio
            return tabla;
        } catch (SQLException sqle) {
            System.out.println("Error! pedidoRasca" + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! pedidoRasca" + e.getMessage());
        }
        //devuelve un null si algo falla antes del try?
        return null;
    }

    //sacar el id con el nombre del producto
    public int obtenerIdRasca(String producto) {
        String selectsql = "select r.id_rasca \n"
                + "from rasca r \n"
                + "where r.nombre_rasca = ?";
        try (Connection con = ConexionDBOnce.Conexiondb(); PreparedStatement pstmt = con.prepareStatement(selectsql)) {
            pstmt.setString(1, producto);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_rasca");
            }

        } catch (SQLException sqle) {
            System.out.println("Error! obtenerIdRasca" + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! obtenerIdRasca" + e.getMessage());
        }
        return 0;
    }

    public boolean eliminarPedidoRasca(int id) {
        String deletesql = "delete from pedidorasca where id_pedidorasca = ?";
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

    public Map<String, Object> obtenerPedidoPorIDPedidoRasca(int id) {
//pido nombre, nombreproducto y la lista pedido en un solo map
        String sentenciasql = "select t.nombre_trab,t.apellidos_trab,p.fecha_pedidorasca as 'Fecha:'"
                + ",r.nombre_rasca  as 'Rasca:' ,p.num_serierasca  as 'Numero de serie:',p.cant_pedidoR  as 'pedido de:', "
                + "r.preciorasca  as 'precio:'\n"
                + "from trabajador t\n"
                + "join pedidorasca p on p.id_trabajador = t.id_trabajador \n"
                + "join rasca r  on r.id_rasca  = p.id_rasca \n"
                + "where p.id_pedidorasca = ?";
        try (Connection con = ConexionDBOnce.Conexiondb(); PreparedStatement pstmt = con.prepareStatement(sentenciasql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            //guarod en cada clave un valor ya que la consulta solo devolvera una fila al ser un id unico
            // no crees el map dentro ya que desapareze al salir y no podras hacer return fuera
            Map<String, Object> fila = new HashMap<>();
            if (rs.next()) {
                fila.put("TrabajadorNombre", rs.getString("nombre_trab"));
                fila.put("TrabajadorApellidos", rs.getString("apellidos_trab"));
                Timestamp fechalinea = rs.getTimestamp("fecha:");
                String fechaParaHtml = fechalinea.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
                fila.put("fecharasca", fechaParaHtml);
                fila.put("producto", rs.getString("Rasca:"));
                fila.put("serie", rs.getString("Numero de serie:"));
                fila.put("pedido", rs.getInt("pedido de:"));
                fila.put("precio", rs.getDouble("precio:"));
            }
            return fila;
        } catch (SQLException sqle) {
            System.out.println("Error! obtenerPedidoPorIDPedidoRasca " + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! obtenerPedidoPorIDPedidoRasca " + e.getMessage());
        }
        return null;
    }

    public boolean updatePedidoRasca(PedidoRasca r) {
        String updatesql = "update pedidorasca set fecha_pedidorasca = ?, num_serierasca = ?, cant_pedidoR = ?, id_rasca = ? "
                + "where id_pedidorasca = ?";
        try (Connection con = ConexionDBOnce.Conexiondb(); PreparedStatement stmtrabajador = con.prepareStatement(updatesql);) {

            stmtrabajador.setObject(1, r.getFechaPedidoRasca());
            stmtrabajador.setString(2, r.getNumSerierRasca());
            stmtrabajador.setInt(3, r.getCantPedidoR());
            stmtrabajador.setInt(4, r.getIdras());
            stmtrabajador.setInt(5, r.getIdpedidorasca());

            int filas = stmtrabajador.executeUpdate();
            if (filas == 1) {
                return true;
            }

        } catch (SQLException sqle) {
            System.out.println("Error! updatePedidoRasca" + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! updatePedidoRasca" + e.getMessage());
        }
        return false;
    }

}
