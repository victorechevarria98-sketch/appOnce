/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.modelo.dao;

import app.modelo.entidad.Lugar;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Alumnos
 */
public class LugarDao {

    public LugarDao() {
    }

    public ArrayList<Lugar> obtenerLugarCompletoPoremail(String emailusuario) {
        String selectsql = "select l.id_lugar, l.calle, l.cod_postal, l.municipio, l.id_trabajador     \n"
                + "from lugar l \n"
                + "join trabajador t on t.id_trabajador = l.id_trabajador \n"
                + "join usuarios u on u.id_usu = t.id_usu \n"
                + "where u.email_usu = ?";
        try (Connection con = ConexionDBOnce.Conexiondb(); PreparedStatement pstmt = con.prepareStatement(selectsql)) {
            pstmt.setString(1, emailusuario);
            ArrayList <Lugar> listalugar = new ArrayList<>();
            ResultSet rs = pstmt.executeQuery();
            Lugar lg = new Lugar();
            while (rs.next()) {
                lg = mapResultSet(rs);
                listalugar.add(lg);
            }
            return listalugar;
        } catch (SQLException sqle) {
            System.out.println("Error! obtenerIdTrabajadorPorEmail" + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! obtenerIdTrabajadorPorEmail" + e.getMessage());
        }
        return null;

    }

    public Lugar mapResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id_lugar");
        String calle = rs.getString("calle");
        int codigo = rs.getInt("cod_postal");
        String municipio = rs.getString("municipio");
        int idtrab = rs.getInt("id_trabajador");

        Lugar lg = new Lugar(calle, municipio, codigo, idtrab);
        lg.setIdlugar(id);
        return lg;
    }

    public Lugar obtenerLugarCompletoPorIDLugar(int id) {
        String selectsql = "select l.id_lugar, l.calle, l.municipio, l.cod_postal, l.id_trabajador     \n"
                + "from lugar l \n"
                + "where l.id_lugar = ?";
        try (Connection con = ConexionDBOnce.Conexiondb(); PreparedStatement pstmt = con.prepareStatement(selectsql)) {
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Lugar lg = new Lugar();

                return mapResultSet(rs);
            }
        } catch (SQLException sqle) {
            System.out.println("Error! obtenerLugarCompletoPorIDLugar" + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! obtenerLugarCompletoPorIDLugar" + e.getMessage());
        }
        return null;

    }

    public int obtenerIdlugarCodPostalCalle(String calle, int codpostal) {
        String selectsql = "select l.id_lugar \n"
                + "from lugar l \n"
                + "where l.cod_postal = ? and l.calle like ?";
        try (Connection con = ConexionDBOnce.Conexiondb(); PreparedStatement pstmt = con.prepareStatement(selectsql)) {
            pstmt.setInt(1, codpostal);
            pstmt.setString(2, '%' + calle + '%');

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String idlug = "";
                idlug = rs.getString("id_lugar");

                return Integer.parseInt(idlug);
            }
        } catch (SQLException sqle) {
            System.out.println("Error! obtenerIdlugarParaIncidencia" + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! obtenerIdlugarParaIncidencia" + e.getMessage());
        }
        return 0;
    }

    public int nuevoLugar(Lugar lg) {
        String insertlugarsql = "insert into lugar (\n"
                + "calle,\n"
                + "municipio,\n"
                + "cod_postal,\n"
                + "id_trabajador\n"
                + ") values (?,?,?,?)";
        int idlugar = idlugar = obtenerIdlugarCodPostalCalle(lg.getCalle(), lg.getCodPostal());
        int filaslugar = 0;
        try (Connection con = ConexionDBOnce.Conexiondb(); PreparedStatement stmtlugar = con.prepareStatement(insertlugarsql);) {

            if (idlugar == 0) {
                stmtlugar.setString(1, lg.getCalle());
                stmtlugar.setString(2, lg.getMunicipio());
                stmtlugar.setInt(3, lg.getCodPostal());
                stmtlugar.setInt(4, lg.getIdtrab());
                filaslugar = stmtlugar.executeUpdate();
            }

            if (filaslugar == 1) {
                idlugar = obtenerIdlugarCodPostalCalle(lg.getCalle(), lg.getCodPostal());
                return idlugar;
            }

        } catch (SQLException sqle) {
            System.out.println("Error! nuevoLugar" + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! nuevoLugar" + e.getMessage());
        }
        return idlugar;
    }

    public boolean eliminarLugar(int id) {
        String deletesql = "delete from lugar where id_lugar = ?";
        try (Connection con = ConexionDBOnce.Conexiondb(); PreparedStatement pstmt = con.prepareStatement(deletesql)) {
            pstmt.setInt(1, id);
            int filas = pstmt.executeUpdate();
            if (filas == 1) {
                return true;
            }

        } catch (SQLException sqle) {
            System.out.println("Error! eliminarlugar" + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! eliminarlugar" + e.getMessage());
        }
        return false;
    }

    public boolean updatelugar(Lugar lg) {
        String updatesql = "update lugar set calle = ?, municipio = ?, cod_postal = ? where id_lugar = ?";
        try (Connection con = ConexionDBOnce.Conexiondb(); PreparedStatement stmtlugar = con.prepareStatement(updatesql);) {

            stmtlugar.setString(1, lg.getCalle());
            stmtlugar.setString(2, lg.getMunicipio());
            stmtlugar.setInt(3, lg.getCodPostal());
            stmtlugar.setInt(4, lg.getIdlugar());

            int filas = stmtlugar.executeUpdate();
            if (filas == 1) {
                return true;
            }

        } catch (SQLException sqle) {
            System.out.println("Error! updatelugar" + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! updatelugar" + e.getMessage());
        }
        return false;
    }

}
