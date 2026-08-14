/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
// lista de pedidos,incidencias,productos,lugares y usuarios que se enviaran a los html lista para luego editar o eliminar por opcion
package app.controlador.servlet;

import app.modelo.dao.CuponDao;
import app.modelo.dao.LugarDao;
import app.modelo.dao.PedidoCuponDao;
import app.modelo.dao.PedidoRascaDao;
import app.modelo.dao.RascaDao;
import app.modelo.dao.TrabajadorDao;
import app.modelo.dao.UsuarioDao;
import app.modelo.entidad.Aviso;
import app.modelo.entidad.Cupon;
import app.modelo.entidad.Rasca;
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
@WebServlet(name = "ServletPaginacion", urlPatterns = {"/ServletPaginacion"})
public class ServletPaginacion extends HttpServlet {

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
    private TrabajadorDao trabajadorDao;
    private LugarDao lugarDao;
    private PedidoRascaDao pedidoRascaDao;
    private PedidoCuponDao pedidoCuponDao;
    private RascaDao rascaDao;
    private CuponDao cuponDao;

    @Override
    public void init() {
        usuarioDao = new UsuarioDao(); // creo instancia para do post
        trabajadorDao = new TrabajadorDao();
        lugarDao = new LugarDao();
        pedidoRascaDao = new PedidoRascaDao();
        pedidoCuponDao = new PedidoCuponDao();
        rascaDao = new RascaDao();
        cuponDao = new CuponDao();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
//        try (PrintWriter out = response.getWriter()) {
//            /* TODO output your page here. You may use following sample code. */
//            out.println("<!DOCTYPE html>");
//            out.println("<html>");
//            out.println("<head>");
//            out.println("<title>Servlet Contraseña</title>");
//            out.println("</head>");
//            out.println("<body>");
//            out.println("<h1>Servlet Contraseña at " + request.getContextPath() + "</h1>");
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
        HttpSession sesion = request.getSession(false);
        Usuario usu = (Usuario) sesion.getAttribute("usuario");
        Map<String, Object> usuario = new HashMap<>();
        usuario.put("usuario", usu);
        String vista = request.getParameter("vista");
        String pag = request.getParameter("pagina");
        switch (vista) {
            case "pedidotrabajador":

                RenderVista.renderizarVista(response, getServletContext().getRealPath("trabajador/listapedidostrabajador.html"), usuario);
                break;
            case "listaincidencia":
                RenderVista.renderizarVista(response, getServletContext().getRealPath("administrador/listaincidencias.html"), usuario);
                break;
            case "listausuarios":
                ArrayList<Map<String, Object>> tablausuario = new ArrayList<>();
                int totalusuarios = usuarioDao.contarUsuarios();
                int totalpaginas = (int) Math.ceil(totalusuarios / 10.0);
                if (totalpaginas == 0) {
                    totalpaginas = 1;
                }
                String paginaactualusuario = request.getParameter("paginaactual");
                int paginabaseusuario = 1;
                if (pag.equals("0")) {
                    paginabaseusuario = Integer.parseInt(pag);
                    tablausuario = usuarioDao.listaUsuarioPaginacion(paginabaseusuario);
                    usuario.put("primera", false);
                    if (totalpaginas > 1) {
                        usuario.put("ultima", true);
                    }
                    paginabaseusuario++;

                } else if (pag.equals("ultimapagina")) {
                    tablausuario = usuarioDao.listaUsuarioPaginacion((totalpaginas - 1));
                    usuario.put("ultima", false);
                    paginabaseusuario = totalpaginas;
                    if (totalpaginas > 1) {
                        usuario.put("primera", true);
                    }

                } else if (pag.equals("siguiente")) {
                    paginabaseusuario = Integer.parseInt(paginaactualusuario);
                    tablausuario = usuarioDao.listaUsuarioPaginacion(paginabaseusuario);
                    paginabaseusuario++;
                    if (!usuarioDao.listaUsuarioPaginacion(paginabaseusuario).isEmpty()) {
                        usuario.put("ultima", true);
                    }
                    usuario.put("primera", true);

                } else if (pag.equals("anterior")) {
                    paginabaseusuario = Integer.parseInt(paginaactualusuario) - 1;
                    tablausuario = usuarioDao.listaUsuarioPaginacion(paginabaseusuario);
                    if (!usuarioDao.listaUsuarioPaginacion((paginabaseusuario - 1)).isEmpty()) {
                        usuario.put("primera", true);
                    }
                    usuario.put("ultima", true);

                }
                usuario.put("pagina", paginabaseusuario);
                usuario.put("tabla", tablausuario);
                RenderVista.renderizarVista(response, getServletContext().getRealPath("administrador/listausuarios.html"), usuario);
                break;
            case "listapedidostotal":
                RenderVista.renderizarVista(response, getServletContext().getRealPath("administrador/listapedidos.html"), usuario);
                break;
            case "productos":
                ArrayList<Rasca> tablarasca = new ArrayList<>();
                int totalrasca = rascaDao.totalRasca();
                int totalpaginasrasca = (int) Math.ceil(totalrasca / 5.0);

                ArrayList<Cupon> tablacupon = new ArrayList<>();
                int totalcupon = cuponDao.totalCupon();
                int totalpaginascupon = (int) Math.ceil(totalcupon / 5.0);

                String paginaproductoactual = request.getParameter("paginaactual");
                int paginaproductobase = 1;
                String producto = request.getParameter("tipoproducto");

                switch (pag) {
                    case "inicio":
                        usuario.put("inicio", true);
                        break;
                    case "0":
                        paginaproductobase = Integer.parseInt(pag);
                        usuario.put("primera", false);
                        if (producto.equals("rasca")) {
                            usuario.put("productorasca", true);
                            tablarasca = rascaDao.obtenerRascaPaginacion(paginaproductobase);
                            if (totalpaginasrasca > 1) {
                                usuario.put("ultima", true);
                            }
                        } else {
                            usuario.put("productocupon", true);
                            tablacupon = cuponDao.obtenerCuponPaginacion(paginaproductobase);
                            System.out.println(tablacupon);
                            if (totalpaginascupon > 1) {
                                usuario.put("ultima", true);
                            }

                        }
                        paginaproductobase++;
                        break;
                    case "ultimapagina":
                        usuario.put("ultima", false);
                        if (producto.equals("rasca")) {
                            usuario.put("productorasca", true);
                            tablarasca = rascaDao.obtenerRascaPaginacion(totalpaginasrasca - 1);
                            paginaproductobase = totalpaginasrasca;
                            if (totalpaginasrasca > 1) {
                                usuario.put("primera", true);
                            }
                        } else {
                            usuario.put("productocupon", true);
                            tablacupon = cuponDao.obtenerCuponPaginacion(totalpaginascupon - 1);
                            paginaproductobase = totalpaginascupon;
                            if (totalpaginascupon > 1) {
                                usuario.put("primera", true);
                            }
                        }
                        break;
                    case "siguiente":
                        paginaproductobase = Integer.parseInt(paginaproductoactual);
                        if (producto.equals("rasca")) {
                            usuario.put("productorasca", true);
                            tablarasca = rascaDao.obtenerRascaPaginacion(paginaproductobase);
                            if (!rascaDao.obtenerRascaPaginacion(paginaproductobase + 1).isEmpty()) {
                                usuario.put("ultima", true);
                            }
                        } else {
                            usuario.put("productocupon", true);
                            tablacupon = cuponDao.obtenerCuponPaginacion(paginaproductobase);
                            if (!cuponDao.obtenerCuponPaginacion(paginaproductobase + 1).isEmpty()) {
                                usuario.put("ultima", true);
                            }
                        }
                        paginaproductobase++;
                        usuario.put("primera", true);
                        break;
                    case "anterior":
                        paginaproductobase = Integer.parseInt(paginaproductoactual) - 1;
                        if (producto.equals("rasca")) {
                            usuario.put("productorasca", true);
                            tablarasca = rascaDao.obtenerRascaPaginacion(paginaproductobase);
                            if (!rascaDao.obtenerRascaPaginacion(paginaproductobase - 1).isEmpty()) {
                                usuario.put("primera", true);
                            }
                        } else {
                            usuario.put("productocupon", true);
                            tablacupon = cuponDao.obtenerCuponPaginacion(paginaproductobase);
                            if (!cuponDao.obtenerCuponPaginacion(paginaproductobase - 1).isEmpty()) {
                                usuario.put("primera", true);
                            }
                        }
                        usuario.put("ultima", true);
                        break;

                }
                usuario.put("pagina", paginaproductobase);
                usuario.put("rasca", tablarasca);
                usuario.put("cupon", tablacupon);
                RenderVista.renderizarVista(response, getServletContext().getRealPath("administrador/listaproductos.html"), usuario);
                break;

            default:
                RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("¿Vaya no funciona?", "Buena suerte investigando😣", "ServletMenuPrincipal"));
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
