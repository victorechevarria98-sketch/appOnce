/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package app.modelo.dao;

import app.modelo.entidad.Cupon;
import app.util.PasswordUtil;
import java.util.ArrayList;

/**
 *
 * @author Alumnos
 */
public class pruebas {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String password ="$2y$10$92IXUNpkjO0rO";
        
        System.out.println(PasswordUtil.hashPassword(password));
        ArrayList<Cupon>tablacupon= new ArrayList<>(); 
        CuponDao cuponDao = new CuponDao();
        tablacupon = cuponDao.obtenerCuponPaginacion(0);
        System.out.println(tablacupon);
         
      }
    
    }
    

