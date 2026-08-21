/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package app.controlador.servlet;

import app.modelo.dao.TrabajadorDao;
import app.modelo.dao.UsuarioDao;
import app.modelo.entidad.Trabajador;
import app.modelo.entidad.Usuario;
import app.vista.mustache.RenderVista;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author Alumnos
 */
@WebServlet(name = "ServletFiltro", urlPatterns = {"/ServletFiltro"})
public class ServletFiltro extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private TrabajadorDao trabajadorDao;
    private UsuarioDao usuarioDao;

    @Override
    public void init() {
        trabajadorDao = new TrabajadorDao();
        usuarioDao = new UsuarioDao();
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
        HttpSession sesion = request.getSession(false);
        Usuario usu = (Usuario) sesion.getAttribute("usuario");
        Map<String, Object> usuario = new HashMap<>();
        usuario.put("usuario", usu);
        String filtro = request.getParameter("filtro");
        // ESTO ES EL MAP

        ArrayList<Map<String, Object>> listaMap = new ArrayList<>();
        listaMap = usuarioDao.listaUsuarioYTrabajador();
        List<Map<String, Object>> listanueva = listaMap.stream().filter(map -> map.get("trabajador").equals(filtro)).collect(Collectors.toList());
        usuario.put("tabla", listanueva);
        System.out.println(listanueva);
        RenderVista.renderizarVista(response, getServletContext().getRealPath("administrador/listausuarios.html"), usuario); 
        /*
        *
        *
        *ESTO ES PARA LISTA TRABAJADOR
        *
        *
        *
        *
         */
        ArrayList<Trabajador> lista = new ArrayList<>();
        lista = trabajadorDao.seleccionListaTrabajadores();
        List<Trabajador> nuevalista = lista.stream().filter(t -> t.getNombreTrab().equalsIgnoreCase("Juan")).collect(Collectors.toList());
        System.out.println(nuevalista);
       
        
        
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
