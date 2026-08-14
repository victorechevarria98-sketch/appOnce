/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.modelo.dao;

import app.modelo.entidad.Trabajador;
import app.modelo.entidad.Usuario;
import app.modelo.entidad.Usuario.rol;
import app.util.PasswordUtil;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Alumnos
 */
public class UsuarioDao {

    public UsuarioDao() {
    }

    public ArrayList<Usuario> seleccionTodosuariosAbsoluta() {
        String selectsql = "select u.id_usu, u.perfil, u.activo , u.nombre_usu, u.password_usu, u.email_usu from usuarios u ";
        try (Connection con = ConexionDBOnce.Conexiondb(); Statement stmt = con.createStatement();) {
            ResultSet rs = stmt.executeQuery(selectsql);
            ArrayList<Usuario> lista = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id_usu");
                String perfil = rs.getString("perfil");
                boolean activo = rs.getBoolean("activo");
                String nombre = rs.getString("nombre_usu");
                String contraseña = rs.getString("password_usu");
                String correo = rs.getString("email_usu");
                rol perfilenum = rol.valueOf(perfil);
                Usuario U = new Usuario(id, nombre, correo, contraseña, activo, perfilenum);
                lista.add(U);
            }
            return lista;
        } catch (SQLException sqle) {
            System.out.println("Error! seleccionTodosuariosAbsoluta" + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! seleccionTodosuariosAbsoluta" + e.getMessage());
        }
        return null;
    }

    public Usuario obtenerUsuarioActivoPorEmail(String nombre) {

        String sentenciasql = "select u.id_usu, u.perfil, u.activo , u.nombre_usu, u.password_usu, u.email_usu from usuarios u where u.email_usu = ? and u.activo = 1";
        try (Connection con = ConexionDBOnce.Conexiondb(); PreparedStatement pstmt = con.prepareStatement(sentenciasql)) {
            pstmt.setString(1, nombre);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSet(rs);
            }
        } catch (SQLException sqle) {
            System.out.println("Error! obtenerUsuarioPorEmail" + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! obtenerUsuarioPorEmail" + e.getMessage());
        }
        return null;
    }

    public Usuario mapResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id_usu");
        String usu = rs.getString("nombre_usu");
        String pass = rs.getString("password_usu");
        String email = rs.getString("email_usu");
        boolean act = rs.getBoolean("activo");
        rol perenum = rol.valueOf(rs.getString("perfil"));
        Usuario u = new Usuario(id, usu, email, pass, act, perenum);

        return u;
    }

    public Usuario obtenerUsuarioPorIDUsuario(int id) {

        String sentenciasql = "select u.id_usu, u.perfil, u.activo , u.nombre_usu, u.password_usu, u.email_usu from usuarios u where u.id_usu = ? and u.activo = 1";
        try (Connection con = ConexionDBOnce.Conexiondb(); PreparedStatement pstmt = con.prepareStatement(sentenciasql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSet(rs);
            }
        } catch (SQLException sqle) {
            System.out.println("Error! obtenerUsuarioPorEmail" + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! obtenerUsuarioPorEmail" + e.getMessage());
        }
        return null;
    }

    public boolean introducirNuevoUsuarioTrabajador(Usuario usu, Trabajador tb, String password) {
        // como no sabes que ha introducido hay que decidir una consulta sql
        String insertTrasql = "";
        // como si no ha hecho algo bien no quiero que se quede a medias hago ambas cosas a la vez
        // mira luego un procedimiento
        String insertUsusql = "insert into usuarios (nombre_usu,email_usu,password_usu,perfil) values (?,?,?,?)";

        //usas variables pasadas para saber que insert usas
        if (tb.getFechaNaTrab() == null && tb.getTldEmp() == 0) {
            insertTrasql = "insert into trabajador (nombre_trab,apellidos_trab,NIF_Trab,fechaIncor_trab) values (?,?,?,?)";
        } else if (tb.getFechaNaTrab() == null) {
            insertTrasql = "insert into trabajador (nombre_trab,apellidos_trab,NIF_Trab,fechaIncor_trab,TLF_emp) values (?,?,?,?,?)";
        } else if (tb.getTldEmp() == 0) {
            insertTrasql = "insert into trabajador (nombre_trab,apellidos_trab,NIF_Trab,fechaIncor_trab,fechaNa_trab) values (?,?,?,?,?)";
        } else {
            insertTrasql = "insert into trabajador (nombre_trab,apellidos_trab,NIF_Trab,fechaIncor_trab,fechaNa_trab,TLF_emp) values (?,?,?,?,?,?)";
        }
        // creas 2 prepared estatement para usuario y trabajador, 
        //puedes hacerlo separado con otra funcion pero no se si se guardara al ocurrir un error solo usuario o solo trabajador 
        try (Connection con = ConexionDBOnce.Conexiondb(); PreparedStatement stmtusu = con.prepareStatement(insertUsusql); PreparedStatement stmttra = con.prepareStatement(insertTrasql)) {
            // hasta que todo este bien no hacer commit
            con.setAutoCommit(false);// usuario esta bien
            stmtusu.setString(1, usu.getNombreUsu());
            stmtusu.setString(2, usu.getEmailUsu());
            //transofrmas la contraseña antes de meterla
            stmtusu.setString(3, PasswordUtil.hashPassword(password));
            stmtusu.setString(4, usu.getPerfil().name());

            stmttra.setString(1, tb.getNombreTrab());
            stmttra.setString(2, tb.getApellidosTrab());
            stmttra.setString(3, tb.getNIF_Trab());
            // al parecer esto transforma a fecha directamente, me imagino que un import y el gettime sobre esa fecha sera lo mismo
            //miralo
            stmttra.setDate(4, new java.sql.Date(tb.getFechaIncorTrab().getTime()));

            //de nuevo compruebas que se ha introducido usando null y 0 para fecha y telefono repectivamente
            if (tb.getFechaNaTrab() == null && tb.getTldEmp() != 0) {
                stmttra.setInt(5, tb.getTldEmp());
            } else if (tb.getTldEmp() == 0 && tb.getFechaNaTrab() != null) {
                stmttra.setDate(5, new java.sql.Date(tb.getFechaNaTrab().getTime()));
            } else if (tb.getFechaNaTrab() != null && tb.getTldEmp() != 0) {
                stmttra.setDate(5, new java.sql.Date(tb.getFechaNaTrab().getTime()));
                stmttra.setInt(6, tb.getTldEmp());
            }

            // el fila nos dira si la operacion ha sido u exito al mirar si se añadido la fila
            int filasusu = stmtusu.executeUpdate(); 
            int filastrab = stmttra.executeUpdate();

            //se ha añadido bien? commmit y return true, no se ha hecho? ve para atras y dame un sout con el problema
            if (filasusu == 1 && filastrab == 1) {
                con.commit();
                con.setAutoCommit(true);
                return true;
            } else {
                con.rollback();
                System.out.println("introducirNuevoUsuarioTrabajador ERROR!");
            }
        } catch (SQLException sqle) {
            System.out.println("Error! introducirNuevoUsuarioTrabajador" + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! introducirNuevoUsuarioTrabajador" + e.getMessage());
        }
        return false;
    }

    public boolean eliminarUsuario(int id) {
        String deletesql = "delete from usuarios where id_usu = ?";
        try (Connection con = ConexionDBOnce.Conexiondb(); PreparedStatement pstmt = con.prepareStatement(deletesql)) {
            pstmt.setInt(1, id);
            int filas = pstmt.executeUpdate();
            if (filas == 1) {
                return true;
            }

        } catch (SQLException sqle) {
            System.out.println("Error! eliminarusuario" + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! eliminarusuario" + e.getMessage());
        }
        return false;
    }

    public boolean updateUsuario(Usuario lg) {
        String updatesql = "update usuarios set nombre_usu = ?, email_usu = ?, activo = ?, perfil = ? where id_usu = ?";
        try (Connection con = ConexionDBOnce.Conexiondb(); PreparedStatement stmtusuario = con.prepareStatement(updatesql);) {

            stmtusuario.setString(1, lg.getNombreUsu());
            stmtusuario.setString(2, lg.getEmailUsu());
            stmtusuario.setBoolean(3, lg.isActivo());
            stmtusuario.setString(4, lg.getPerfil().toString());
            stmtusuario.setInt(5, lg.getIdusu());

            int filas = stmtusuario.executeUpdate();
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

    public boolean updateContraseña(String contraseña, int id) {
        String updatesql = "update usuarios set password_usu = ? where id_usu = ?";
        try (Connection con = ConexionDBOnce.Conexiondb(); PreparedStatement stmtusuario = con.prepareStatement(updatesql);) {

            String nuevacontraseña = PasswordUtil.hashPassword(contraseña);
            stmtusuario.setString(1, nuevacontraseña);
            stmtusuario.setInt(2, id);

            int filas = stmtusuario.executeUpdate();
            if (filas == 1) {
                return true;
            }

        } catch (SQLException sqle) {
            System.out.println("Error! updateContraseña" + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! updateContraseña" + e.getMessage());
        }
        return false;
    }

    public ArrayList<Map<String, Object>> listaUsuarioPaginacion(int pagina) {
        int datos = pagina * 10;
        String selectsql = "select u.nombre_usu, concat(t.nombre_trab,' ',t.apellidos_trab ) as 'trabajador' , u.email_usu ,u.activo , u.perfil, t.NIF_Trab , t.TLF_emp, t.BajaLaboral,u.id_usu, t.id_trabajador    \n"
                + "from usuarios u \n"
                + "join trabajador t on u.id_usu = t.id_usu\n"
                + "limit ?, 10";
        try (Connection con = ConexionDBOnce.Conexiondb(); PreparedStatement stmt = con.prepareStatement(selectsql);) {
            stmt.setInt(1, datos);
            ResultSet rs = stmt.executeQuery();
            ArrayList<Map<String, Object>> tabla = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> fila = new HashMap<>();
                fila.put("usuario", rs.getString("nombre_usu"));
                fila.put("trabajador", rs.getString("trabajador"));
                fila.put("email", rs.getString("email_usu"));
                fila.put("cuentaactiva", rs.getBoolean("activo"));
                fila.put("perfil", rs.getString("perfil"));
                fila.put("DNI", rs.getString("NIF_Trab"));
                fila.put("telefono", rs.getInt("TLF_emp"));
                fila.put("baja", rs.getBoolean("BajaLaboral"));
                fila.put("idusuario", rs.getInt("id_usu"));
                fila.put("idtrabajador", rs.getInt("id_trabajador"));
                tabla.add(fila);
            }
            return tabla;
        } catch (SQLException sqle) {
            System.out.println("Error! pedidoTotalTrabajador" + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! pedidoTotalTrabajador" + e.getMessage());
        }
        return null;
    }

    public int contarUsuarios() {
        String selectsql = "select COUNT(*) as 'numero'\n"
                + "from usuarios u \n"
                + "join trabajador t on u.id_usu = t.id_usu";
        try (Connection con = ConexionDBOnce.Conexiondb(); Statement stmt = con.createStatement();) {
            ResultSet rs = stmt.executeQuery(selectsql);
            if (rs.next()) {
                return rs.getInt("numero");
            }
        } catch (SQLException sqle) {
            System.out.println("Error! seleccionTodosuariosAbsoluta" + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! seleccionTodosuariosAbsoluta" + e.getMessage());
        }
        return 0;
    }

    public Usuario obtenerconIDUsuarioParaEditar(int id) {

        String sentenciasql = "select u.perfil, u.activo , u.nombre_usu, u.password_usu, u.email_usu from usuarios u where u.id_usu = ?";
        try (Connection con = ConexionDBOnce.Conexiondb(); PreparedStatement pstmt = con.prepareStatement(sentenciasql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            Usuario u = new Usuario();
            if (rs.next()) {
                String usu = rs.getString("nombre_usu");
                String email = rs.getString("email_usu");
                boolean act = rs.getBoolean("activo");
                rol perenum = rol.valueOf(rs.getString("perfil"));
                u.setNombreUsu(usu);
                u.setEmailUsu(email);
                u.setActivo(act);
                u.setPerfil(perenum);
            }
            return u;
        } catch (SQLException sqle) {
            System.out.println("Error! obtenerUsuarioPorEmail" + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error! obtenerUsuarioPorEmail" + e.getMessage());
        }
        return null;
    }
}
