/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.modelo.entidad;

import app.util.PasswordUtil;

/**
 *
 * @author Alumnos
 */
public class Usuario {
// si no pones en el constructor el id mejor para insert y esas cosas
    private int idusu;
    private String nombreUsu;
    private String emailUsu;
    private String passwordUsu;
    private boolean activo;
    public enum rol {admin, trabajador};
    private rol perfil;

    public Usuario() {
    }

    public Usuario(int idusu, String nombreUsu, String emailUsu, String passwordUsu, boolean activo, rol perfil) {
        this.idusu = idusu;
        this.nombreUsu = nombreUsu;
        this.emailUsu = emailUsu;
        this.passwordUsu = passwordUsu;
        this.activo = activo;
        this.perfil = perfil;
    }

   

   

    public String getNombreUsu() {
        return nombreUsu;
    }

    public void setNombreUsu(String nombreUsu) {
        this.nombreUsu = nombreUsu;
    }

    public String getEmailUsu() {
        return emailUsu;
    }

    public void setEmailUsu(String emailUsu) {
        this.emailUsu = emailUsu;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public rol getPerfil() {
        return perfil;
    }

    public void setPerfil(rol perfil) {
        this.perfil = perfil;
    }

    public void setPasswordUsu(String passwordUsu) {
        this.passwordUsu = passwordUsu;
    }

   

    public boolean comprobarPasswordUsu(String passwordUsu) {
        //return passwordUsu.equals(this.passwordUsu);
        return PasswordUtil.verifyPassword( passwordUsu, this.passwordUsu);
    }

    @Override
    public String toString() {
        return "Usuario{" + "idusu=" + idusu + ", nombreUsu=" + nombreUsu + ", emailUsu=" + emailUsu + ", passwordUsu=" + passwordUsu + ", activo=" + activo + ", perfil=" + perfil + '}';
    }

    public int getIdusu() {
        return idusu;
    }

    public void setIdusu(int idusu) {
        this.idusu = idusu;
    }

    
    public static rol getEstadoFromString(String estadoStr) {
        try {
            return rol.valueOf(estadoStr);
        } catch (IllegalArgumentException e) {
            return rol.trabajador; // Valor por defecto
        }
    }
   
   
}
