/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.modelo.dao;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;

/**
 *
 * @author Alumnos
 */
public class ConexionPool {

    private static DataSource dataSource;

    static {
        try {
            Context ctx = new InitialContext();
            Context envContext = (Context) ctx.lookup("java:/comp/env");
            dataSource = (DataSource) envContext.lookup("jdbc/bd_gestonceDB");
        } catch (Exception e) {
            throw new RuntimeException("No se pudo inicializar el pool de conexiones ", e);
}
}
public static Connection getConnection() throws Exception {
        return dataSource.getConnection(); // conexión "prestada" del pool
    }
}
