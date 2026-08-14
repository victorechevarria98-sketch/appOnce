/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
 /*


contraseña y email para un menu creado aqui por primera vez dependiendo de si eres administrador o trabajador


 */
package app.controlador.servlet;

import app.modelo.dao.PedidoCuponDao;
import app.modelo.dao.PedidoRascaDao;
import app.modelo.dao.TrabajadorDao;
import app.modelo.entidad.Aviso;
import app.modelo.entidad.Usuario;
import app.modelo.dao.UsuarioDao;
import app.vista.mustache.RenderVista;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Alumnos
 */
@WebServlet(name = "OnceServer", urlPatterns = {"/OnceServer"})
public class ServletLogin extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private UsuarioDao usuarioDao; // cargada e inicializada
    private PedidoRascaDao pedidoRascaDao;
    private PedidoCuponDao pedidoCuponDao;

    @Override
    public void init() { // instancia para no crearla cada vez que entro a un do
        usuarioDao = new UsuarioDao(); // creo instancia para do post
        pedidoRascaDao = new PedidoRascaDao();
        pedidoCuponDao = new PedidoCuponDao();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // extraigo el correo aunque lo he llamado nombre porque por ahora solo uso eso
        String nombreusuario = request.getParameter("nombreusuario").trim();
        // extraigo todo el usuario con el correo que tambien es unique
        Usuario user = usuarioDao.obtenerUsuarioActivoPorEmail(nombreusuario);
        // cojo la contraseña
        String passwordusuario = request.getParameter("passwordusuario").trim();
// si usuario existe hace eso sino envias a aviso.html con clase aviso
        if (user != null) {
            //comprobamos contraseña para sino como en usuario lo enviamos a error
            if (user.comprobarPasswordUsu(passwordusuario)) {
                // creamos sesion y metemos instancia user de usuario
                HttpSession sesion = request.getSession(true);
                sesion.setAttribute("usuario", user);
                if (user.getPerfil() == Usuario.getEstadoFromString("admin")) {   // administradores
                    // creo un array list con 5 datos de cupon y rasca cada uno
                    ArrayList<Map<String, Object>> tablacupones = new ArrayList<>();
                    ArrayList<Map<String, Object>> tablarascas = new ArrayList<>();
                    tablacupones = pedidoCuponDao.pedidoCupon();
                    tablarascas = pedidoRascaDao.pedidoRasca();
                    // creamos el hashmap que se pasara al render vista y metemos las tablas
                    Map<String, Object> datos = new HashMap<>();
                    datos.put("tablacupon", tablacupones);
                    datos.put("tablarasca", tablarascas);
                    // en el html admin de administrador saldran las 2 tablas siguiendo la logica de la clave para el mustache
                    RenderVista.renderizarVista(response, getServletContext().getRealPath("administrador/admin.html"), datos);
                } else { // si eres trabajador hace esto
                    ArrayList<Map<String, Object>> tabla = new ArrayList<>();
                    TrabajadorDao td = new TrabajadorDao();
                    // creamos el array para la tabla que en este caso sera los ultimos 10 pedidos da igual el producto
                    String nombre = user.getNombreUsu();
                    tabla = td.pedidoTotalTrabajador(nombre);
                    // metemos el array de los pedidos en el hashmap que va al rendervista
                    Map<String, Object> datos = new HashMap<>();
                    datos.put("tabla", tabla);
                    RenderVista.renderizarVista(response, getServletContext().getRealPath("trabajador/trabajador.html"), datos);
                }
            } else { // error en caontraseña
                RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("La contraseña no existe", "Introduzca una contraseña valida", "index.html"));
            }
        } else { // error en usuario
            RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("El usuario no existe", "Introduzca un usuario valido", "index.html"));
        }

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
