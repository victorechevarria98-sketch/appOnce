/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.modelo.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Alumnos
 */
public class ConexionDBOnce {

    private static final String url = "jdbc:mysql://localhost:3306/bd_gestonce";
    private static final String usuario = "victor"; // no he dado mas que insert select update permisos
    private static final String contraseña = "944393094";

    public static Connection Conexiondb() {
        try {
// Establecer la conexión
            Connection conexion = DriverManager.getConnection(url, usuario, contraseña);
            System.out.println("Conexión exitosa a la base de datos.");
            return conexion;
        } catch (SQLException e) {
            System.err.println("Error al conectar: " + e.getMessage());
        }
        return null;
    }
}
