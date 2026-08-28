/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.modelo.dao;

import app.modelo.entidad.Filtro;
import app.modelo.entidad.Filtro.Kiosko;
import java.sql.Connection;
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
public class FiltroDao {

    public FiltroDao() {
    }

    public ArrayList<Filtro> listaFiltradoPedidoRascaEntidad() {
        String sentenciasql = "select CONCAT(t.nombre_trab, ' ', t.apellidos_trab) AS 'Trabajador', "
                + "t.nombre_trab, t.apellidos_trab ,\n"
                + "r.nombre_rasca  AS 'Rasca', p.num_serierasca  AS 'Numero', \n"
                + "p.cant_pedidoR  AS 'pedido', r.preciorasca  AS 'precio', \n"
                + "p.fecha_pedidorasca  as 'fecha', l.municipio, \n"
                + "t.tipoactividad, t.tipocontrato, \n"
                + "t.tipokiosko, u.perfil,\n"
                + "l.cod_postal,p.id_pedidorasca \n"
                + "from pedidorasca p \n"
                + "join rasca r on p.id_rasca = r.id_rasca\n"
                + "join trabajador t on p.id_trabajador = t.id_trabajador\n"
                + "join lugar l on t.id_trabajador = l.id_trabajador\n"
                + "join usuarios u on t.id_usu = u.id_usu";
        try (Connection con = ConexionDBOnce.Conexiondb(); Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(sentenciasql);
            ArrayList<Filtro> lista = new ArrayList<>();
            while (rs.next()) {
                Filtro f = new Filtro(rs.getString("nombre_trab"),
                        rs.getString("apellidos_trab"),
                        rs.getString("Trabajador"),
                        rs.getString("Rasca"),
                        rs.getString("Numero"),
                        rs.getInt("pedido"),
                        rs.getDouble("precio"),
                        rs.getTimestamp("fecha").toLocalDateTime(),
                        rs.getString("municipio"),
                        Kiosko.valueOf(rs.getString("tipokiosko").toString()),
                        Filtro.Contrato.valueOf(rs.getString("tipocontrato").toString()),
                        Filtro.Actividad.valueOf(rs.getString("tipoactividad").toString()),
                        rs.getInt("cod_postal"),
                        Filtro.rol.valueOf(rs.getString("perfil").toString()),
                        rs.getInt("id_pedidorasca"));
                lista.add(f);
            }
            return lista;
        } catch (SQLException sqle) {
            System.out.println("Error! listaFiltradoPedidoRascaEntidad " + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! listaFiltradoPedidoRascaEntidad " + e.getMessage());
        }
        return null;
    }
//      public ArrayList<Pokemon> seleccionPokemonPorFiltro(String nombre, Double pesoMin, Double pesoMax, Double alturaMin, Double alturaMax, Integer limite) {
//        String consultasql = "Select numero_pokedex as id, nombre, peso, altura from pokemon ";

    ////        String where = "";
////        if(nombre != null && !nombre.trim().equalsIgnoreCase("")) where = " where nombre like '%?%'";
////        if(pesoMin != null) {
////            if (where.isEmpty()) where = "where peso > ?"; else where += " and peso > ?";
////        } .......................................
//        String where = " where ";
//        boolean filtro = false;
//        if (nombre != null && !nombre.trim().equalsIgnoreCase("")) {
//            if (!filtro) {
//                filtro = true;
//                where += " nombre like ?";
//            }
//        }
//        if (pesoMin != null) {
//            if (filtro) {
//                where += " and ";
//
//            }
//            filtro = true;
//            where += " peso > ?";
//        }
//        if (pesoMax != null) {
//            if (filtro) {
//                where += " and ";
//
//            }
//            filtro = true;
//            where += " peso < ?";
//        }
//        if (alturaMin != null) {
//            if (filtro) {
//                where += " and ";
//
//            }
//            filtro = true;
//            where += " altura > ?";
//        }
//        if (alturaMax != null) {
//            if (filtro) {
//                where += " and ";
//
//            }
//            filtro = true;
//            where += " altura < ?";
//        }
//
//        if (filtro) {
//            consultasql += where;
//        }
//        if (limite != null) {
//            consultasql += " limit ?";
//        }
//
//        try (Connection con = ConexionDB.Conexriondb(); PreparedStatement stmt = con.prepareStatement(consultasql)) {
//            int contador = 0;
//            if (filtro) {
//
//                if (nombre != null && !nombre.trim().equalsIgnoreCase("")) {
//                    stmt.setString(++contador, '%' + nombre + '%');
//                }
//
//                if (pesoMin != null) {
//                    stmt.setDouble(++contador, pesoMin);
//                }
//
//                if (pesoMax != null) {
//                    stmt.setDouble(++contador, pesoMax);
//                }
//
//                if (alturaMin != null) {
//                    stmt.setDouble(++contador, alturaMin);
//                }
//
//                if (alturaMax != null) {
//                    stmt.setDouble(++contador, alturaMax);
//                }
//
//            }
//            if (limite != null) {
//                limite = limite.intValue();
//                stmt.setInt(++contador, limite);
//            }
//            ResultSet rs = stmt.executeQuery();
//            ArrayList<Pokemon> listafiltro = new ArrayList<>();
//            while (rs.next()) {
//                //mientras haya datos el next nos da la posicion de cada fila no los datos               
//                listafiltro.add(new Pokemon(rs.getInt("id"), rs.getString("nombre"), rs.getDouble("peso"), rs.getDouble("altura")));
//
//            }
//            return listafiltro;
//        } catch (SQLException sqle) {
//            System.out.println("Error!" + sqle.getMessage());
//        } catch (Exception e) {
//            System.out.println("Error!" + e.getMessage());
//        }
//
//        return null;
//    }
//    
    
    public ArrayList<Map<String, Object>> listaParaFiltradoCupon() {
        String sentenciasql = "select CONCAT(t.nombre_trab, ' ', t.apellidos_trab) AS 'Trabajador:', "
                + "t.nombre_trab, t.apellidos_trab ,\n"
                + "c.nombre_cupon AS 'Cupon:', p.num_seriecupon AS 'Numero de serie:', \n"
                + "p.cant_pedidoC AS 'pedido de:', c.preciocupon AS 'precio:', \n"
                + "p.fecha_pedidocupon as 'fecha:', l.municipio, \n"
                + "t.tipoactividad, t.tipocontrato, \n"
                + "t.tipokiosko, u.perfil,\n"
                + "l.cod_postal,p.id_pedidocupon\n"
                + "from pedidocupon p \n"
                + "join cupon c on p.id_cupon = c.id_cupon\n"
                + "join trabajador t on p.id_trabajador = t.id_trabajador\n"
                + "join lugar l on t.id_trabajador = l.id_trabajador\n"
                + "join usuarios u on t.id_usu = u.id_usu";
        try (Connection con = ConexionDBOnce.Conexiondb(); Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(sentenciasql);
            ArrayList<Map<String, Object>> lista = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> fila = new HashMap<>();
                //metemos en el map una clave y valor por cada columna de la fila
                fila.put("TrabajadorNombre", rs.getString("nombre_trab"));
                fila.put("TrabajadorApellidos", rs.getString("apellidos_trab"));
                Timestamp fechalinea = rs.getTimestamp("fecha:");
                //pasamos la fecha a un localdatetmie con el format pero en una sola lines
                //String fechaParaHtml = fechalinea.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
                fila.put("fecha", (fechalinea != null) ? fechalinea.toLocalDateTime() : null);
                fila.put("producto", rs.getString("Cupon:"));
                fila.put("serie", rs.getString("Numero de serie:"));
                fila.put("pedido", rs.getInt("pedido de:"));
                fila.put("precio", rs.getDouble("precio:"));
                fila.put("Trabajador", rs.getString("Trabajador:"));
                fila.put("municipio", rs.getString("municipio"));
                fila.put("codigoPostal", rs.getString("cod_postal"));
                fila.put("tipokiosko", rs.getString("tipokiosko"));
                fila.put("tipoactividad", rs.getString("tipoactividad"));
                fila.put("tipocontrato", rs.getString("tipocontrato"));
                fila.put("perfil", rs.getString("perfil"));
                fila.put("id_pedidocupon", rs.getString("id_pedidocupon"));
                lista.add(fila);
            }
            return lista;
        } catch (SQLException sqle) {
            System.out.println("Error! listaParaFiltradoCupon " + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! listaParaFiltradoCupon " + e.getMessage());
        }
        return null;
    }

    public ArrayList<Map<String, Object>> listaParaFiltradoRasca() {
        String sentenciasql = "select CONCAT(t.nombre_trab, ' ', t.apellidos_trab) AS 'Trabajador:', "
                + "t.nombre_trab, t.apellidos_trab ,\n"
                + "r.nombre_rasca  AS 'Rasca:', p.num_serierasca  AS 'Numero de serie:', \n"
                + "p.cant_pedidoR  AS 'pedido de:', r.preciorasca  AS 'precio:', \n"
                + "p.fecha_pedidorasca  as 'fecha:', l.municipio, \n"
                + "t.tipoactividad, t.tipocontrato, \n"
                + "t.tipokiosko, u.perfil,\n"
                + "l.cod_postal,p.id_pedidorasca \n"
                + "from pedidorasca p \n"
                + "join rasca r on p.id_rasca = r.id_rasca\n"
                + "join trabajador t on p.id_trabajador = t.id_trabajador\n"
                + "join lugar l on t.id_trabajador = l.id_trabajador\n"
                + "join usuarios u on t.id_usu = u.id_usu";
        try (Connection con = ConexionDBOnce.Conexiondb(); Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(sentenciasql);
            ArrayList<Map<String, Object>> lista = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> fila = new HashMap<>();
                //metemos en el map una clave y valor por cada columna de la fila
                fila.put("TrabajadorNombre", rs.getString("nombre_trab"));
                fila.put("TrabajadorApellidos", rs.getString("apellidos_trab"));
                Timestamp fechalinea = rs.getTimestamp("Fecha:");
                //pasamos la fecha a un localdatetmie con el format pero en una sola lines
                //String fechaParaHtml = fechalinea.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
                fila.put("fecha", (fechalinea != null) ? fechalinea.toLocalDateTime() : null);
                fila.put("producto", rs.getString("Rasca:"));
                fila.put("serie", rs.getString("Numero de serie:"));
                fila.put("pedido", rs.getInt("pedido de:"));
                fila.put("precio", rs.getDouble("precio:"));
                fila.put("Trabajador", rs.getString("Trabajador:"));
                fila.put("municipio", rs.getString("municipio"));
                fila.put("codigoPostal", rs.getString("cod_postal"));
                fila.put("tipokiosko", rs.getString("tipokiosko"));
                fila.put("tipoactividad", rs.getString("tipoactividad"));
                fila.put("tipocontrato", rs.getString("tipocontrato"));
                fila.put("perfil", rs.getString("perfil"));
                fila.put("id_pedidorasca", rs.getString("id_pedidorasca"));
                lista.add(fila);
            }
            return lista;
        } catch (SQLException sqle) {
            System.out.println("Error! listaParaFiltradoRasca " + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! listaParaFiltradoRasca " + e.getMessage());
        }
        return null;
    }
}
