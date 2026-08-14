/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

//cada vez que quieras volver a una pagina anterior debes pasra por aqui para que te lleve al menu del admin o del trabajador


package app.controlador.servlet;

import app.modelo.dao.PedidoCuponDao;
import app.modelo.dao.PedidoRascaDao;
import app.modelo.dao.TrabajadorDao;
import app.modelo.dao.UsuarioDao;
import app.modelo.entidad.Usuario;
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
@WebServlet(name = "ServletMenuPrincipal", urlPatterns = {"/ServletMenuPrincipal"})
public class ServletMenuPrincipal extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * 
     * 
     */
    
    private UsuarioDao usuarioDao; // cargada e inicializada
    private PedidoRascaDao pedidoRascaDao;
    private PedidoCuponDao pedidoCuponDao;
    
    
    @Override
    public void init(){
        usuarioDao = new UsuarioDao(); // creo instancia para do post
        pedidoRascaDao = new PedidoRascaDao();
        pedidoCuponDao = new PedidoCuponDao();
    }
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession sesion = request.getSession(false);
        String nombreusuario = "";
        Usuario usu = (Usuario) sesion.getAttribute("usuario");
        if (usu.getPerfil() == Usuario.getEstadoFromString("admin")) {
            ArrayList<Map<String, Object>> tablacupones = new ArrayList<>();
            ArrayList<Map<String, Object>> tablarascas = new ArrayList<>();            
            tablacupones = pedidoCuponDao.pedidoCupon();
            tablarascas = pedidoRascaDao.pedidoRasca();
            Map<String, Object> datos = new HashMap<>();
            datos.put("tablacupon", tablacupones);
            datos.put("tablarasca", tablarascas);
            RenderVista.renderizarVista(response, getServletContext().getRealPath("administrador/admin.html"), datos);
        } else {
            ArrayList<Map<String, Object>> tabla = new ArrayList<>();
            TrabajadorDao td = new TrabajadorDao();
            String nombre = usu.getNombreUsu();
            tabla = td.pedidoTotalTrabajador(nombre);
            Map<String, Object> datos = new HashMap<>();
            datos.put("tabla", tabla);
            
            RenderVista.renderizarVista(response, getServletContext().getRealPath("trabajador/trabajador.html"), datos);
        }
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
        processRequest(request, response);
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
