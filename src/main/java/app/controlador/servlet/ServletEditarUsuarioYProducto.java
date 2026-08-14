/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package app.controlador.servlet;

import app.modelo.dao.CuponDao;
import app.modelo.dao.RascaDao;
import app.modelo.dao.TrabajadorDao;
import app.modelo.dao.UsuarioDao;
import app.modelo.entidad.Aviso;
import app.modelo.entidad.Cupon;
import app.modelo.entidad.Rasca;
import app.modelo.entidad.Trabajador;
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
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Alumnos
 */
@WebServlet(name = "ServletUsuariosProductos", urlPatterns = {"/ServletUsuariosProductos"})
public class ServletEditarUsuarioYProducto extends HttpServlet {

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
    private RascaDao rascaDao;
    private CuponDao cuponDao;

    @Override
    public void init() {
        usuarioDao = new UsuarioDao(); // creo instancia para do post
        trabajadorDao = new TrabajadorDao();
        rascaDao = new RascaDao();
        cuponDao = new CuponDao();
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
        HttpSession sesion = request.getSession(false);
        Usuario usu = (Usuario) sesion.getAttribute("usuario");
        String dueñosesion = usu.getNombreUsu();
        Map<String, Object> formulario = new HashMap<>();
        String vista = request.getParameter("vista");
        formulario.put("dueñosesion", dueñosesion);
        String id;
        boolean exito = false;
        switch (vista) {
            case "editar":
                String operacion = request.getParameter("operacion");
                switch (operacion) {
                    case "editartrabajador":
                        formulario.put("casotrabajador", operacion);
                        id = request.getParameter("idtrabajador");
                        formulario.put("idtrabajador", Integer.valueOf(id));
                        Trabajador tb2 = new Trabajador();
                        tb2 = trabajadorDao.obtenerTrabajadorCompletoPorIDTrabajador(Integer.parseInt(id));
                        formulario.put("trabajador", tb2);
                        if ("admin".equalsIgnoreCase(usu.getPerfil().name())) {
                            formulario.put("administrador", true);
                        } else {
                            formulario.put("trabajadorlimite", true);
                        }
                        RenderVista.renderizarVista(response, getServletContext().getRealPath("editorperfil.html"), formulario);
                        break;
                    case "editarusuario":
                        formulario.put("casousuario", operacion);
                        id = request.getParameter("idusuario");
                        formulario.put("idusuario", Integer.valueOf(id));
                        Usuario u = new Usuario();
                        u = usuarioDao.obtenerconIDUsuarioParaEditar(Integer.parseInt(id));
                        formulario.put("usuario", u);
                        if ("admin".equalsIgnoreCase(usu.getPerfil().name())) {
                            formulario.put("administrador", true);
                        } else {
                            formulario.put("trabajador", true);
                        }
                        RenderVista.renderizarVista(response, getServletContext().getRealPath("editorperfil.html"), formulario);
                        break;

                }
                break;
            case "editarrasca":
                id = request.getParameter("idrasca");
                int idrasca = Integer.parseInt(id);
                String precior = request.getParameter("precio");
                double preciorasca = Double.parseDouble(precior);
                String productor = request.getParameter("producto");
                Rasca r = new Rasca();
                r.setIdrasca(idrasca);
                r.setNombreRasca(productor);
                r.setPrecioRasca(preciorasca);
                exito = rascaDao.editarRasca(r);
                if (exito == true) {
                    RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("producto actualizado", "Operacion realizada, muchas gracias", "ServletMenuPrincipal"));
                } else {
                    RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("Error", "Operacion fallida comprueba datos", "ServletMenuPrincipal"));
                }
                break;
            case "eliminarrasca":
                id = request.getParameter("idrasca");
                exito = rascaDao.eliminarRasca(Integer.parseInt(id));
                if (exito == true) {
                    RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("Rasca eliminado", "Operacion realizada", "ServletMenuPrincipal"));
                } else {
                    RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("¿Vaya no hay lugar?", "Buena suerte investigando😣", "ServletMenuPrincipal"));
                }
                break;
            case "editarcupon":
                id = request.getParameter("idcupon");
                int idcupon = Integer.parseInt(id);
                String precio = request.getParameter("precio");
                double preciocupon = Double.parseDouble(precio);
                String producto = request.getParameter("producto");
                Cupon c = new Cupon();
                c.setIdcupon(idcupon);
                c.setNombreCupon(producto);
                c.setPrecioCupon(preciocupon);
                exito = cuponDao.editarCupon(c);
                if (exito == true) {
                    RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("producto actualizado", "Operacion realizada, muchas gracias", "ServletMenuPrincipal"));
                } else {
                    RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("Error", "Operacion fallida comprueba datos", "ServletMenuPrincipal"));
                }

                break;
            case "eliminarcupon":
                id = request.getParameter("idcupon");
                exito = cuponDao.eliminarCupon(Integer.parseInt(id));
                if (exito == true) {
                    RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("Cupon eliminado", "Operacion realizada", "ServletMenuPrincipal"));
                } else {
                    RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("¿Vaya no hay lugar?", "Buena suerte investigando😣", "ServletMenuPrincipal"));
                }
                break;
            case "nuevoproducto":
                String inicio = request.getParameter("pagina");
                if (inicio.equals("inicio")) {
                    formulario.put("inicio", true);
                } else if (inicio.equals("nuevo")) {
                    formulario.put("nuevo", true);
                }
                RenderVista.renderizarVista(response, getServletContext().getRealPath("nuevoproducto.html"), formulario);
                break;
            case "insert":
                String eleccion = request.getParameter("producto");
                String nombre = request.getParameter("nombre");
                String valor = request.getParameter("precio");
                double precioproducto = Double.parseDouble(valor);
                if (eleccion.equals("rasca")) {
                    Rasca rasca = new Rasca();
                    rasca.setNombreRasca(nombre);
                    rasca.setPrecioRasca(precioproducto);
                    exito = rascaDao.insertarRasca(rasca);
                    if (exito == true) {
                        RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("Rasca creado", "Operacion realizada", "ServletMenuPrincipal"));
                    } else {
                        RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("¿Vaya no hay lugar?", "Buena suerte investigando😣", "ServletMenuPrincipal"));
                    }

                } else {
                    Cupon cupon = new Cupon();
                    cupon.setNombreCupon(nombre);
                    cupon.setPrecioCupon(precioproducto);
                    exito = cuponDao.insertarCupon(cupon);
                    if (exito == true) {
                        RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("Cupon creado", "Operacion realizada", "ServletMenuPrincipal"));
                    } else {
                        RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("¿Vaya no hay lugar?", "Buena suerte investigando😣", "ServletMenuPrincipal"));
                    }
                }
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
