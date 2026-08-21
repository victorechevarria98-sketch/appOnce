/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package app.controlador.servlet;

import app.modelo.dao.PedidoCuponDao;
import app.modelo.dao.PedidoRascaDao;
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
import java.util.Map;

/**
 *
 * @author Alumnos
 */
@WebServlet(name = "ServletPedidosOperaciones", urlPatterns = {"/ServletPedidosOperaciones"})
public class ServletPedidosOperaciones extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private PedidoRascaDao pedidoRascaDao;
    private PedidoCuponDao pedidoCuponDao;

    @Override
    public void init() {
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
        HttpSession sesion = request.getSession(false);
        Usuario usu = (Usuario) sesion.getAttribute("usuario");
        Map<String, Object> usuario = new HashMap<>();
        usuario.put("usuario", usu);
        String vista = request.getParameter("vista");
        String pag = request.getParameter("pagina");
        String paginaactual = request.getParameter("paginaactual");
        int totalpaginas;
        // esta variable sirve para la pagina incial en la primera conexion y es lo que enviamos al final. asi si hay algun error en la operaciones intermedias llega esto
        int paginabase = 1;
        switch (vista) {
            case "listapedidostotal":
                ArrayList<Map<String, Object>> tablarasca = new ArrayList<>();
                //las filas de rascas
                int totalrasca = pedidoRascaDao.totalPedidoRasca();
                //maximas paginasdependiendo del numero de filas de rasca
                int totalpaginasrasca = (int) Math.ceil(totalrasca / 10.0);

                //lo mismo para cupon
                ArrayList<Map<String, Object>> tablacupon = new ArrayList<>();
                int totalcupon = pedidoCuponDao.totalPedidoCupon();
                int totalpaginascupon = (int) Math.ceil(totalcupon / 10.0);

                //sacas aqui la pagina en la que estas y solo la usas cuando la necesitas en los if
                paginaactual = request.getParameter("paginaactual");
                //que eliga si rasca o cupon 
                String pedido = request.getParameter("tipoproducto");

                switch (pag) {
                    case "inicio":// si es la primera vez te lleva a decidir que quieres
                        usuario.put("inicio", true);
                        break;
                    case "0":
                        paginabase = Integer.parseInt(pag);
                        usuario.put("primera", false);// no necesitas el boton anterior
                        if (pedido.equals("rasca")) {// si el dato viene del formulario rasca
                            usuario.put("productorasca", true);//que aparezca el formulario de rasca
                            tablarasca = pedidoRascaDao.obtenerPedidoRascaPaginacion(paginabase);// datos de 5 en 5
                            if (totalpaginasrasca > 1) { // si solo hay 5 no ncesitas siguiente pagina
                                usuario.put("ultima", true);
                            }
                        } else {
                            usuario.put("productocupon", true);// te lleva al formulario cupon
                            tablacupon = pedidoCuponDao.obtenerPedidoCuponPaginacion(paginabase);// datos cupones
                            System.out.println(tablacupon);
                            if (totalpaginascupon > 1) {
                                usuario.put("ultima", true);//si no hay mas datos que 5 no necesitas boton siguiente
                            }

                        }
                        paginabase++;// aumentas la pagina en 1
                        break;
                    case "ultimapagina":// boton ultima pagina
                        usuario.put("ultima", false);// no necesitas boton siguiente
                        if (pedido.equals("rasca")) {//si es rasca
                            usuario.put("productorasca", true);//formulario rasca
                            tablarasca = pedidoRascaDao.obtenerPedidoRascaPaginacion(totalpaginasrasca - 1);// los ultimos 5 datos
                            paginabase = totalpaginasrasca;// el dato de la pagina en la que estas usando las paginas totales
                            if (totalpaginasrasca > 1) {// si no hay mas que cinco datos
                                usuario.put("primera", true);
                            }
                        } else {//si es cupon
                            usuario.put("productocupon", true);//formulario cupon
                            tablacupon = pedidoCuponDao.obtenerPedidoCuponPaginacion(totalpaginascupon - 1);//ultimos 5 datos
                            paginabase = totalpaginascupon;
                            if (totalpaginascupon > 1) {
                                usuario.put("primera", true);
                            }
                        }
                        break;
                    case "siguiente":// boton siguiente
                        paginabase = Integer.parseInt(paginaactual);//sacas pagina actual
                        if (pedido.equals("rasca")) {// si es rasca
                            usuario.put("productorasca", true);//formulario rasca
                            tablarasca = pedidoRascaDao.obtenerPedidoRascaPaginacion(paginabase);//sacamoos datos con la pagina
                            if (!pedidoRascaDao.obtenerPedidoRascaPaginacion(paginabase + 1).isEmpty()) {// si no hay mas datos no necesitas boton siguiente
                                usuario.put("ultima", true);
                            }
                        } else {// si es cupon
                            usuario.put("productocupon", true);
                            tablacupon = pedidoCuponDao.obtenerPedidoCuponPaginacion(paginabase); //pagina actual con datos siguiente
                            if (!pedidoCuponDao.obtenerPedidoCuponPaginacion(paginabase + 1).isEmpty()) {
                                usuario.put("ultima", true);
                            }
                        }
                        paginabase++;// aumentas la pagina
                        usuario.put("primera", true);// guardas boton anterior 
                        break;
                    case "anterior":
                        paginabase = Integer.parseInt(paginaactual) - 1;
                        if (pedido.equals("rasca")) {
                            usuario.put("productorasca", true);
                            tablarasca = pedidoRascaDao.obtenerPedidoRascaPaginacion(paginabase);
                            if (pedidoRascaDao.obtenerPedidoRascaPaginacion(paginabase - 1) == null) {
                                usuario.put("primera", true);
                            }
                        } else {
                            usuario.put("productocupon", true);
                            tablacupon = pedidoCuponDao.obtenerPedidoCuponPaginacion(paginabase);
                            if (pedidoCuponDao.obtenerPedidoCuponPaginacion(paginabase - 1) == null) {
                                usuario.put("primera", true);
                            }
                        }
                        usuario.put("ultima", true);
                        break;

                }
                    usuario.put("pagina", paginabase);
                    usuario.put("tablarasca", tablarasca);
                    usuario.put("tablacupon", tablacupon);

                RenderVista.renderizarVista(response, getServletContext().getRealPath("administrador/listapedidos.html"), usuario);
                break;

            case "pedidotrabajador":
                RenderVista.renderizarVista(response, getServletContext().getRealPath("trabajador/listapedidostrabajador.html"), usuario);
                break;
        }
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
