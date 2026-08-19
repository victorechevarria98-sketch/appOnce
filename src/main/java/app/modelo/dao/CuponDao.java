/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.modelo.dao;

import app.modelo.entidad.Cupon;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Alumnos
 */
public class CuponDao {

    public CuponDao() {
    }

    public ArrayList<Cupon> listaCupon() {
        String selectsql = "select c.id_cupon,c.nombre_cupon , c.preciocupon \n"
                + "from cupon c";
        try (Connection con = ConexionDBOnce.Conexiondb(); Statement st = con.createStatement();) {
            ResultSet rs = st.executeQuery(selectsql);
            ArrayList<Cupon> lista = new ArrayList<>();
            while (rs.next()) {
                Cupon c = new Cupon();
                c.setIdcupon(rs.getInt("id_cupon"));
                c.setNombreCupon(rs.getString("nombre_cupon"));
                c.setPrecioCupon(rs.getDouble("preciocupon"));
                lista.add(c);
            }
            return lista;
        } catch (SQLException sqle) {
            System.out.println("Error! listaCupon" + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! listaCupon" + e.getMessage());
        }
        return null;
    }

    // sacar di, nombre y precio de 5 en 5 para paginacion
    public ArrayList<Cupon> obtenerCuponPaginacion(int pagina) {
        String selectsql = "select c.id_cupon,c.nombre_cupon , c.preciocupon \n"
                + "from cupon c \n"
                + "order by c.nombre_cupon desc\n"
                + "limit ?, 5";
        int datos = pagina * 5;// transformas la pagina en el dato desde donde empiezas la nueva pagina
        try (Connection con = ConexionDBOnce.Conexiondb(); PreparedStatement stmt = con.prepareStatement(selectsql);) {
            stmt.setInt(1, datos);
            ResultSet rs = stmt.executeQuery();
            ArrayList<Cupon> lista = new ArrayList<>();
            while (rs.next()) {
                Cupon c = new Cupon();
                c.setIdcupon(rs.getInt("id_cupon"));
                c.setNombreCupon(rs.getString("nombre_cupon"));
                c.setPrecioCupon(rs.getDouble("preciocupon"));
                lista.add(c);
            }
            return lista;
        } catch (SQLException sqle) {
            System.out.println("Error! obtenerCuponPaginacion" + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! obtenerCuponPaginacion" + e.getMessage());
        }
        return null;
    }

    //calculas el total de filas, o en otras alabras cuantos tipos de cupones hay
    public int totalCupon() {
        String selectsql = "select count(*) as 'numero'\n"
                + "from cupon c ";
        try (Connection con = ConexionDBOnce.Conexiondb(); Statement stmt = con.createStatement();) {
            ResultSet rs = stmt.executeQuery(selectsql);
            if (rs.next()) {
                return rs.getInt("numero");
            }
        } catch (SQLException sqle) {
            System.out.println("Error! totalCupon" + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! totalCupon" + e.getMessage());
        }
        return 0;// es un int por lo cual no hay null sino 0
    }

    //update datos de un producto concreto
    public boolean editarCupon(Cupon c) {
        String updatesql = "update cupon set nombre_cupon = ?, preciocupon = ? where id_cupon = ?";
        try (Connection con = ConexionDBOnce.Conexiondb(); PreparedStatement stmtcupon = con.prepareStatement(updatesql);) {

            stmtcupon.setString(1, c.getNombreCupon());
            stmtcupon.setDouble(2, c.getPrecioCupon());
            stmtcupon.setInt(3, c.getIdcupon());

            int filas = stmtcupon.executeUpdate();
            if (filas == 1) {
                return true;
            }

        } catch (SQLException sqle) {
            System.out.println("Error! updateUsuario" + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! updateUsuario" + e.getMessage());
        }
        return false;

    }

    public boolean eliminarCupon(int id) {
        String deletesql = "delete from cupon where id_cupon = ?";
        try (Connection con = ConexionDBOnce.Conexiondb(); PreparedStatement pstmt = con.prepareStatement(deletesql)) {
            pstmt.setInt(1, id);
            int filas = pstmt.executeUpdate();
            if (filas == 1) {
                return true;
            }

        } catch (SQLException sqle) {
            System.out.println("Error! eliminarCupon" + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! eliminarCupon" + e.getMessage());
        }
        return false;
    }

    //nuevo cupon con nombre y precio
    public boolean insertarCupon(Cupon c) {
        String deletesql = "insert into cupon(\n"
                + "nombre_cupon,\n"
                + "preciocupon\n"
                + ") values \n"
                + "(?,?)";
        try (Connection con = ConexionDBOnce.Conexiondb(); PreparedStatement pstmt = con.prepareStatement(deletesql)) {
            pstmt.setString(1, c.getNombreCupon());
            pstmt.setDouble(1, c.getPrecioCupon());

            int filas = pstmt.executeUpdate();
            if (filas == 1) {
                return true;
            }

        } catch (SQLException sqle) {
            System.out.println("Error! insertarCupon" + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! insertarCupon" + e.getMessage());
        }
        return false;
    }

    public int obtenerCuponPorNombre(String nombre) {
        String selectsql = "select c.id_cupon \n"
                + "from cupon c\n"
                + "where nombre_cupon = ?";
        try (Connection con = ConexionDBOnce.Conexiondb(); PreparedStatement stmt = con.prepareStatement(selectsql);) {
            stmt.setString(1, nombre);
            ResultSet rs = stmt.executeQuery();            
            if (rs.next()) {
                return rs.getInt("id_cupon");
            }

        } catch (SQLException sqle) {
            System.out.println("Error! obtenerCuponPorID" + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! obtenerCuponPorID" + e.getMessage());
        }
        return 0;
    }
}
