/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.modelo.dao;

import app.modelo.entidad.Trabajador;
import app.modelo.entidad.Trabajador.Actividad;
import app.modelo.entidad.Trabajador.Contrato;
import app.modelo.entidad.Trabajador.Kiosko;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author Alumnos
 */
public class TrabajadorDao {

    public TrabajadorDao() {
    }

// la funcion para generar un codigo de producto, se supone que cada producto tiene uno pero tecnicamente lo pondria alguien externo al enviar el pedido por eso es aleatorio
    public static String generarCodigoFormat() {
        Random random = new Random();

        // 1. Generar 2 letras mayúsculas aleatorias (A-Z)
        char letra1 = (char) ('A' + random.nextInt(26));
        char letra2 = (char) ('A' + random.nextInt(26));

        // 2. Generar primer bloque de 2 dígitos (00 a 99)
        int bloque1 = random.nextInt(100);

        // 3. Generar segundo bloque de 4 dígitos (0000 a 9999)
        int bloque2 = random.nextInt(10000);

        // 4. Formatear la cadena con ceros a la izquierda si es necesario
        return String.format("%c%c-%02d-%04d", letra1, letra2, bloque1, bloque2);
    }

    // trabajador relacionado cone el usuario de sesion para editar en mi perfil
    public Trabajador obtenerTrabajadorCompletoPorEmail(String emailusuario) {
        String selectsql = "select t.id_trabajador, t.nombre_trab,t.apellidos_trab,t.NIF_Trab, t.fechaNa_trab, t.fechaIncor_trab, t.TLF_emp,t.BajaLaboral, t.tipokiosko, t.tipocontrato, t.tipoactividad, t.id_usu\n"
                + "from trabajador t \n"
                + "join usuarios u on u.id_usu = t.id_usu \n"
                + "where u.email_usu = ?";
        try (Connection con = ConexionDBOnce.Conexiondb(); PreparedStatement pstmt = con.prepareStatement(selectsql)) {
            pstmt.setString(1, emailusuario);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSet(rs);
            }

        } catch (SQLException sqle) {
            System.out.println("Error! obtenerIdTrabajadorPorEmail" + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! obtenerIdTrabajadorPorEmail" + e.getMessage());
        }
        return null;
    }
    
    
//sacar todas las variables de trabajador y no tener que escribirlas cada vez
    public Trabajador mapResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id_trabajador");
        String nombre = rs.getString("nombre_trab");
        String apellido = rs.getString("apellidos_trab");
        String nif = rs.getString("NIF_Trab");
        Date fechanacim = rs.getDate("fechaNa_trab");
        Date fechainicio = rs.getDate("fechaIncor_trab");
        int tlf = rs.getInt("TLF_emp");
        boolean baja = rs.getBoolean("BajaLaboral");
        Kiosko kiosko = Kiosko.valueOf(rs.getString("tipokiosko"));//estas tres variables son enum asi que obtienes si tipokiosko de rs es igual a alguno de los posibles valores de Kiosko
        Contrato contrato = Contrato.valueOf(rs.getString("tipocontrato"));
        Actividad actividad = Actividad.valueOf(rs.getString("tipoactividad"));
        int idusu = rs.getInt("id_usu");
        Trabajador tb = new Trabajador(nombre, apellido, nif, fechanacim, fechainicio, tlf, baja, kiosko, contrato, actividad, idusu);
        tb.setIdtrab(id);
        return tb;
    }

    
    //para non sacar toda la tabla de trabajador
    public Trabajador obtenerIdTrabajadorPorEmail(String emailusuario) {
        String selectsql = "select t.id_trabajador \n"
                + "from trabajador t \n"
                + "join usuarios u on u.id_usu = t.id_usu \n"
                + "where u.email_usu = ?";
        try (Connection con = ConexionDBOnce.Conexiondb(); PreparedStatement pstmt = con.prepareStatement(selectsql)) {
            pstmt.setString(1, emailusuario);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String idtrab = rs.getString("id_trabajador");
                Trabajador tb = new Trabajador();
                tb.setIdtrab(Integer.parseInt(idtrab));
                return tb;
            }
        } catch (SQLException sqle) {
            System.out.println("Error! obtenerIdTrabajadorPorEmail" + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! obtenerIdTrabajadorPorEmail" + e.getMessage());
        }
        return null;
    }

    //el id lo usas para extraer la informacion de ese trabajador
    public Trabajador obtenerTrabajadorCompletoPorIDTrabajador(int id) {
        String selectsql = "select t.id_trabajador, t.nombre_trab,t.apellidos_trab,t.NIF_Trab, t.fechaNa_trab, t.fechaIncor_trab, t.TLF_emp,t.BajaLaboral, t.tipokiosko, t.tipocontrato, t.tipoactividad, t.id_usu\n"
                + "from trabajador t \n"
                + "where t.id_trabajador = ?";
        try (Connection con = ConexionDBOnce.Conexiondb(); PreparedStatement pstmt = con.prepareStatement(selectsql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSet(rs);
            }

        } catch (SQLException sqle) {
            System.out.println("Error! obtenerIdTrabajadorPorEmail" + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! obtenerIdTrabajadorPorEmail" + e.getMessage());
        }
        return null;
    }

    public ArrayList<Map<String, Object>> pedidoTotalTrabajador(String email) {
// 10 ultimos pedidos por fecha pero tambien uso datos de otras clase para el select por lo tanto hashmap
        String selectsql = "(select p.id_pedidocupon as 'id_pedido', p.fecha_pedidocupon as 'fecha:',c.nombre_cupon as 'articulo:' ,'cupon'as 'tipo' ,p.num_seriecupon as 'numero de serie:',p.cant_pedidoC as 'pedido de:', c.preciocupon as 'precio:'\n"
                + "from trabajador t\n"
                + "join pedidocupon p on p.id_trabajador = t.id_trabajador \n"
                + "join cupon c  on c.id_cupon = p.id_cupon\n"
                + "join usuarios u on u.id_usu = t.id_usu \n"
                + "where u.nombre_usu = ?\n"
                + ")\n"
                + "union all\n"
                + "(select p.id_pedidorasca as 'id_pedido', p.fecha_pedidorasca  as 'fecha:',r.nombre_rasca  as 'articulo:' ,'rasca'as 'tipo' ,p.num_serierasca  as 'numero de serie:',p.cant_pedidoR  as 'pedido de:', r.preciorasca  as 'precio:'\n"
                + "from trabajador t\n"
                + "join pedidorasca p on p.id_trabajador = t.id_trabajador \n"
                + "join rasca r  on r.id_rasca  = p.id_rasca \n"
                + "join usuarios u on u.id_usu = t.id_usu \n"
                + "where u.nombre_usu = ?\n"
                + ")\n"
                + "order by 'fecha:' desc\n"
                + "limit 10;";
        try (Connection con = ConexionDBOnce.Conexiondb(); PreparedStatement stmt = con.prepareStatement(selectsql);) {
            // es un  union all por lo tanto 2 select que necesitan la misma interrogacion 2 veces
            stmt.setString(1, email);
            stmt.setString(2, email);
            ResultSet rs = stmt.executeQuery();
            // ejecutamos consulta y creamos el array para la table de html
            ArrayList<Map<String, Object>> tabla = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> fila = new HashMap<>();
                // creamos un hashmap para cada posicion de la columna de una fila que ira todo a una posicion del array
                fila.put("id_pedido", rs.getInt("id_pedido"));
                fila.put("fecha", rs.getTimestamp("fecha:"));
                fila.put("articulo", rs.getString("articulo:"));
                fila.put("tipo", rs.getString("tipo"));
                fila.put("serie", rs.getString("Numero de serie:"));
                fila.put("pedido", rs.getInt("pedido de:"));
                fila.put("precio", rs.getDouble("precio:"));
                //añadimos el hash map al array
                tabla.add(fila);
            }
            // devolvemos el array y capturamos errores si es necesario
            return tabla;
        } catch (SQLException sqle) {
            System.out.println("Error! pedidoTotalTrabajador" + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! pedidoTotalTrabajador" + e.getMessage());
        }
        return null;
    }

    public boolean eliminarTrabajador(int id) {
        String deletesql = "delete from trabajador where id_trabajador = ?";
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

    public boolean updateTrabajador(Trabajador lg) {
        String updatesql = "update trabajador set nombre_trab = ?, apellidos_trab = ?, NIF_Trab = ?, fechaNa_trab = ?, TLF_emp = ?, "
                + "BajaLaboral = ?, tipokiosko = ?, tipocontrato = ?, tipoactividad = ? where id_trabajador = ?";
        try (Connection con = ConexionDBOnce.Conexiondb(); PreparedStatement stmtrabajador = con.prepareStatement(updatesql);) {

            stmtrabajador.setString(1, lg.getNombreTrab());
            stmtrabajador.setString(2, lg.getApellidosTrab());
            stmtrabajador.setString(3, lg.getNIF_Trab());
            stmtrabajador.setDate(4, new java.sql.Date(lg.getFechaNaTrab().getTime()));
            stmtrabajador.setInt(5, lg.getTldEmp());
            stmtrabajador.setBoolean(6, lg.isBajaLaboral());
            stmtrabajador.setString(7, lg.getTipoKiosko().toString());
            stmtrabajador.setString(8, lg.getTipoContrato().toString());
            stmtrabajador.setString(9, lg.getTipoActividad().toString());
            stmtrabajador.setInt(10, lg.getIdtrab());

            int filas = stmtrabajador.executeUpdate();
            if (filas == 1) {
                return true;
            }

        } catch (SQLException sqle) {
            System.out.println("Error! updateTrabajador" + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! updateTrabajador" + e.getMessage());
        }
        return false;
    }
}
