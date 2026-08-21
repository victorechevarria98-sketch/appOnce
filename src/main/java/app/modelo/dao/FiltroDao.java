/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Alumnos
 */
public class FiltroDao {

    public FiltroDao() {
    }
      public ArrayList<Pokemon> seleccionPokemonPorFiltro(String nombre, Double pesoMin, Double pesoMax, Double alturaMin, Double alturaMax, Integer limite) {
        String consultasql = "Select numero_pokedex as id, nombre, peso, altura from pokemon ";
//        String where = "";
//        if(nombre != null && !nombre.trim().equalsIgnoreCase("")) where = " where nombre like '%?%'";
//        if(pesoMin != null) {
//            if (where.isEmpty()) where = "where peso > ?"; else where += " and peso > ?";
//        } .......................................
        String where = " where ";
        boolean filtro = false;
        if (nombre != null && !nombre.trim().equalsIgnoreCase("")) {
            if (!filtro) {
                filtro = true;
                where += " nombre like ?";
            }
        }
        if (pesoMin != null) {
            if (filtro) {
                where += " and ";

            }
            filtro = true;
            where += " peso > ?";
        }
        if (pesoMax != null) {
            if (filtro) {
                where += " and ";

            }
            filtro = true;
            where += " peso < ?";
        }
        if (alturaMin != null) {
            if (filtro) {
                where += " and ";

            }
            filtro = true;
            where += " altura > ?";
        }
        if (alturaMax != null) {
            if (filtro) {
                where += " and ";

            }
            filtro = true;
            where += " altura < ?";
        }

        if (filtro) {
            consultasql += where;
        }
        if (limite != null) {
            consultasql += " limit ?";
        }

        try (Connection con = ConexionDB.Conexriondb(); PreparedStatement stmt = con.prepareStatement(consultasql)) {
            int contador = 0;
            if (filtro) {

                if (nombre != null && !nombre.trim().equalsIgnoreCase("")) {
                    stmt.setString(++contador, '%' + nombre + '%');
                }

                if (pesoMin != null) {
                    stmt.setDouble(++contador, pesoMin);
                }

                if (pesoMax != null) {
                    stmt.setDouble(++contador, pesoMax);
                }

                if (alturaMin != null) {
                    stmt.setDouble(++contador, alturaMin);
                }

                if (alturaMax != null) {
                    stmt.setDouble(++contador, alturaMax);
                }

            }
            if (limite != null) {
                limite = limite.intValue();
                stmt.setInt(++contador, limite);
            }
            ResultSet rs = stmt.executeQuery();
            ArrayList<Pokemon> listafiltro = new ArrayList<>();
            while (rs.next()) {
                //mientras haya datos el next nos da la posicion de cada fila no los datos               
                listafiltro.add(new Pokemon(rs.getInt("id"), rs.getString("nombre"), rs.getDouble("peso"), rs.getDouble("altura")));

            }
            return listafiltro;
        } catch (SQLException sqle) {
            System.out.println("Error!" + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error!" + e.getMessage());
        }

        return null;
    }
    
}
