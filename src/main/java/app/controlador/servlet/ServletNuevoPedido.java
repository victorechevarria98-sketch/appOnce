/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

//nuevo pedido donde calculas el insert con el trabajador corespondiente y lo envias a aviso con informacion sobre lo que haces

package app.controlador.servlet;

import app.modelo.dao.PedidoCuponDao;
import app.modelo.dao.PedidoRascaDao;
import app.modelo.entidad.Aviso;
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

/**
 *
 * @author Alumnos
 */
@WebServlet(name = "ServletPedidos", urlPatterns = {"/ServletPedidos"})
public class ServletNuevoPedido extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    private PedidoRascaDao pedidorascadao; // cargada e inicializada
    private PedidoCuponDao pedidocupondao;
    
    @Override
    public void init(){
        pedidorascadao = new PedidoRascaDao(); // creo instancia para do post
        pedidocupondao = new PedidoCuponDao();
    }
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
//        try (PrintWriter out = response.getWriter()) {
//            /* TODO output your page here. You may use following sample code. */
//            out.println("<!DOCTYPE html>");
//            out.println("<html>");
//            out.println("<head>");
//            out.println("<title>Servlet ServletPedidos</title>");
//            out.println("</head>");
//            out.println("<body>");
//            out.println("<h1>Servlet ServletPedidos at " + request.getContextPath() + "</h1>");
//            out.println("</body>");
//            out.println("</html>");
//        }
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
        String emailusuario = usu.getEmailUsu();
        String[] pedidocupon = request.getParameterValues("ProductoCupon");
        if (pedidocupon != null) {
            for (String cupon : pedidocupon) {
                int cantidad = Integer.parseInt(request.getParameter("cantidades_" + cupon));
                int idcupon = Integer.parseInt(cupon);
                pedidocupondao.insertarPedidoCuponPorTrabajador(idcupon, cantidad, emailusuario);
            }
        }
        String[] pedidorasca = request.getParameterValues("productosrasca");
        if (pedidorasca != null) {
            for (String rasca : pedidorasca) {
                int cantidad = Integer.parseInt(request.getParameter("cantidades_" + rasca));
                int idrasca = Integer.parseInt(rasca);

                pedidorascadao.insertarPedidoRascaPorTrabajador(idrasca, cantidad, emailusuario);
            }
        }
        if (pedidocupon != null && pedidorasca != null) {
            RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("Felicidades, pedido correcto", "Recibira su pedido en un par de dias, esclavo capitalista", "ServletMenuPrincipal"));
        } else if (pedidocupon != null) {
            RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("Felicidades, pedido de cupones correcto", "Recibira sus cupones en un par de dias, esclavo capitalista", "ServletMenuPrincipal"));
        } else if (pedidorasca != null) {
            RenderVista.renderizarVista(response,getServletContext().getRealPath("avisos.html"),new Aviso("Felicidades, pedido de rascas correcto","Recibira sus rascas en un par de dias, esclavo capitalista","ServletMenuPrincipal"));
        } else {
            RenderVista.renderizarVista(response,getServletContext().getRealPath("avisos.html"),new Aviso("Vaya algo que no se ha fallado","Buena suerte investigando😣","nuelvopedido.html"));
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
