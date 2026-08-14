/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.modelo.dao;

import app.modelo.entidad.Rasca;
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
public class RascaDao {

    public RascaDao() {
    }

    public ArrayList<Rasca> obtenerRascaPaginacion(int pagina) {
        String selectsql = "select r.id_rasca ,r.nombre_rasca , r.preciorasca \n"
                + "from rasca r \n"
                + "limit ?, 5";
        int datos = pagina * 5;
        try (Connection con = ConexionDBOnce.Conexiondb(); PreparedStatement stmt = con.prepareStatement(selectsql);) {
            stmt.setInt(1, datos);
            ResultSet rs = stmt.executeQuery();
            ArrayList<Rasca> lista = new ArrayList<>();
            while (rs.next()) {
                Rasca r = new Rasca();
                r.setIdrasca(rs.getInt("id_rasca"));
                r.setNombreRasca(rs.getString("nombre_rasca"));
                r.setPrecioRasca(rs.getDouble("preciorasca"));
                lista.add(r);
            }
            return lista;
        } catch (SQLException sqle) {
            System.out.println("Error! obtenerRascaPaginacion" + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! obtenerRascaPaginacion" + e.getMessage());
        }
        return null;
    }

    public int totalRasca() {
        String selectsql = "select count(*) as 'numero'\n"
                + "from rasca r ";
        try (Connection con = ConexionDBOnce.Conexiondb(); Statement stmt = con.createStatement();) {
            ResultSet rs = stmt.executeQuery(selectsql);
            if (rs.next()) {
                return rs.getInt("numero");
            }
        } catch (SQLException sqle) {
            System.out.println("Error! totalRasca" + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! totalRasca" + e.getMessage());
        }
        return 0;
    }

    public boolean editarRasca(Rasca r) {
        String updatesql = "update rasca set nombre_rasca = ?, preciorasca = ? where id_rasca = ?";
        try (Connection con = ConexionDBOnce.Conexiondb(); PreparedStatement stmtrasca = con.prepareStatement(updatesql);) {

            stmtrasca.setString(1, r.getNombreRasca());
            stmtrasca.setDouble(2, r.getPrecioRasca());
            stmtrasca.setInt(3, r.getIdrasca());

            int filas = stmtrasca.executeUpdate();
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

    public boolean eliminarRasca(int id) {
        String deletesql = "delete from rasca where id_rasca = ?";
        try (Connection con = ConexionDBOnce.Conexiondb(); PreparedStatement pstmt = con.prepareStatement(deletesql)) {
            pstmt.setInt(1, id);
            int filas = pstmt.executeUpdate();
            if (filas == 1) {
                return true;
            }

        } catch (SQLException sqle) {
            System.out.println("Error! eliminarRasca" + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! eliminarRasca" + e.getMessage());
        }
        return false;

    }

    public boolean insertarRasca(Rasca r) {
        String deletesql = "insert into rasca(\n"
                + "nombre_rasca,\n"
                + "preciorasca\n"
                + ") values \n"
                + "(?,?)";
        try (Connection con = ConexionDBOnce.Conexiondb(); PreparedStatement pstmt = con.prepareStatement(deletesql)) {
            pstmt.setString(1, r.getNombreRasca());
            pstmt.setDouble(1, r.getPrecioRasca());
            
            int filas = pstmt.executeUpdate();
            if (filas == 1) {
                return true;
            }

        } catch (SQLException sqle) {
            System.out.println("Error! insertarRasca" + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! insertarRasca" + e.getMessage());
        }
        return false;

    }
}
