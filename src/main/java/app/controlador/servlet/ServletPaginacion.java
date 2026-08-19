/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
// lista de pedidos,incidencias,productos,lugares y usuarios que se enviaran a los html lista para luego editar o eliminar por opcion
package app.controlador.servlet;

import app.modelo.dao.CuponDao;
import app.modelo.dao.IncidenciasDao;
import app.modelo.dao.RascaDao;
import app.modelo.dao.UsuarioDao;
import app.modelo.entidad.Aviso;
import app.modelo.entidad.Cupon;
import app.modelo.entidad.Incidencias;
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
    private RascaDao rascaDao;
    private CuponDao cuponDao;
    private IncidenciasDao incidenciasDao;

    @Override
    public void init() {
        usuarioDao = new UsuarioDao(); // creo instancia para do post
        incidenciasDao = new IncidenciasDao();
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
            case "listalugar":
                RenderVista.renderizarVista(response, getServletContext().getRealPath("administrador/ListaLugares.html"), usuario);
                break;

            case "listapedidostotal":
                RenderVista.renderizarVista(response, getServletContext().getRealPath("administrador/listapedidos.html"), usuario);
                break;

            case "pedidotrabajador":
                RenderVista.renderizarVista(response, getServletContext().getRealPath("trabajador/listapedidostrabajador.html"), usuario);
                break;

            case "listaincidencia":
                //<li><a href="ServletPaginacion?vista=listaincidencia&pagina=0">Lista de Incidencias</a></li>
                ArrayList<Map<String,Object>> tablaincidencias = new ArrayList<>();// tabla para las incidencias
                // total de filas o incidencias
                int totalincidencias = incidenciasDao.contarIncidencias();
                //total de paginas
                totalpaginas = (int) Math.ceil(totalincidencias / 10.0);
                if (totalpaginas == 0) {
                    totalpaginas = 1;
                }
                paginaactual = request.getParameter("paginaactual");
                if (pag.equals("0")) {// primera conexion o boton a primera pagina
                    paginabase = Integer.parseInt(pag); // es pagina 0 o en la funcion select es LIMIT para dato 0
                    tablaincidencias = incidenciasDao.obtenerIncidenciaPaginacion(paginabase);
                    usuario.put("primera", false); // no aparecera la parte de primera pagina y pagina anterior
                    if (totalpaginas > 1) {// si solo hay 10 datos esta parte no aparece
                        usuario.put("ultima", true);
                    }
                    paginabase++;// aumentamos la pagina a 1 para que no parezca 0
                } else if (pag.equals("ultimapagina")) {
                    tablaincidencias = incidenciasDao.obtenerIncidenciaPaginacion((totalpaginas - 1));
                    usuario.put("ultima", false);// no hay siguiente
                    paginabase = totalpaginas;
                    if (totalpaginas > 1) {//si solo hay 10 datos esta parte no aparece
                        usuario.put("primera", true);
                    }
                } else if (pag.equals("siguiente")) {// boton siguiente 
                    paginabase = Integer.parseInt(paginaactual);
                    tablaincidencias = incidenciasDao.obtenerIncidenciaPaginacion(paginabase);
//sabemos donde estamos y como datos empiezan en 0 nos vale con la misma pagina, hay que miras si funciona con algo distinto a 10 como el limite de filas
                    paginabase++;// aumenta la pagina
                    if (!incidenciasDao.obtenerIncidenciaPaginacion(paginabase).isEmpty()) {// si no hay mas datos no hay boton siguiente
                        usuario.put("ultima", true);
                    }
                    usuario.put("primera", true);//metes primera pagina y anrterior
                } else if (pag.equals("anterior")) {//boton anterior
                    paginabase = Integer.parseInt(paginaactual) - 1; //guardas pagina anterior menos 1
                    tablaincidencias = incidenciasDao.obtenerIncidenciaPaginacion(paginabase);;//sacas los 10 anterores
                    if (incidenciasDao.obtenerIncidenciaPaginacion((paginabase - 1)) == null) {// si no hay datos anteriores estas en la primera pagina 
                        usuario.put("primera", true);
                    }
                    usuario.put("ultima", true);

                }
                
                usuario.put("pagina", paginabase);// esto siempre se tiene que enviar, solo cambia el contenido
                usuario.put("tabla", tablaincidencias);
                RenderVista.renderizarVista(response, getServletContext().getRealPath("administrador/listaincidencias.html"), usuario);
                break;

            case "listausuarios":
                //contruyes el map con la tabla que contiene los datos tanto de usuario como de otros datos que veas necesario
                ArrayList<Map<String, Object>> tablausuario = new ArrayList<>();
                //calculas el numero total de filas
                int totalusuarios = usuarioDao.contarUsuarios();
                // calculas las paginas maximas
                totalpaginas = (int) Math.ceil(totalusuarios / 10.0);
                //si hay algun error que salga por lo menos una pagina
                if (totalpaginas == 0) {
                    totalpaginas = 1;
                }
                //sacas la pagina en la que estas desde el formulario
                paginaactual = request.getParameter("paginaactual");
                // en cada if la idea es que paginaactualusuario cambie a lo que sea necesario
                if (pag.equals("0")) {// primera conexion o boton a primera pagina
                    paginabase = Integer.parseInt(pag); // es pagina 0 o en la funcion select es LIMIT para dato 0
                    tablausuario = usuarioDao.listaUsuarioPaginacion(paginabase); // envias el 0*10 y nos da desde que dato hasta que pagina es
                    usuario.put("primera", false); // no aparecera la parte de primera pagina y pagina anteriro
                    if (totalpaginas > 1) {// si solo hay 10 datos esta parte no aparece
                        usuario.put("ultima", true);
                    }
                    paginabase++;// aumentamos la pagina a 1 para que no parezca 0

                } else if (pag.equals("ultimapagina")) {// la pagina final
                    tablausuario = usuarioDao.listaUsuarioPaginacion((totalpaginas - 1)); // sacamos la lista pasanado el el ultimo valor de pagina usando el limit por 10
                    // si la ultima pagina es 3 es que hay como maximo 30 datos, por lo tanto del 20 al 29
                    usuario.put("ultima", false);// no hay siguiente
                    paginabase = totalpaginas;
                    if (totalpaginas > 1) {//si solo hay 10 datos esta parte no aparece
                        usuario.put("primera", true);
                    }

                } else if (pag.equals("siguiente")) {// boton siguiente 
                    paginabase = Integer.parseInt(paginaactual);
                    tablausuario = usuarioDao.listaUsuarioPaginacion(paginabase);
//sabemos donde estamos y como datos empiezan en 0 nos vale con la misma pagina, hay que miras si funciona con algo distinto a 10 como el limite de filas
                    paginabase++;// aumenta la pagina
                    if (!usuarioDao.listaUsuarioPaginacion(paginabase).isEmpty()) {// si no hay mas datos no hay boton siguiente
                        usuario.put("ultima", true);
                    }
                    usuario.put("primera", true);//metes primera pagina y anrterior

                } else if (pag.equals("anterior")) {//boton anterio
                    paginabase = Integer.parseInt(paginaactual) - 1; //guardas pagina anterior menos 1
                    tablausuario = usuarioDao.listaUsuarioPaginacion(paginabase);//sacas los 10 anterores
                    if (usuarioDao.listaUsuarioPaginacion((paginabase - 1)) == null) {// si no hay datos anteriores estas en la primera pagina 
                        usuario.put("primera", true);
                    }
                    usuario.put("ultima", true);

                }
                usuario.put("pagina", paginabase);// esto siempre se tiene que enviar, solo cambia el contenido
                usuario.put("tabla", tablausuario);
                RenderVista.renderizarVista(response, getServletContext().getRealPath("administrador/listausuarios.html"), usuario);
                break;

            case "productos":
                // los rascas
                ArrayList<Rasca> tablarasca = new ArrayList<>();
                //las filas de rascas
                int totalrasca = rascaDao.totalRasca();
                //maximas paginasdependiendo del numero de filas de rasca
                int totalpaginasrasca = (int) Math.ceil(totalrasca / 5.0);

                //lo mismo para cupon
                ArrayList<Cupon> tablacupon = new ArrayList<>();
                int totalcupon = cuponDao.totalCupon();
                int totalpaginascupon = (int) Math.ceil(totalcupon / 5.0);

                //sacas aqui la pagina en la que estas y solo la usas cuando la necesitas en los if
                paginaactual = request.getParameter("paginaactual");
                // lo que enviamos en el render vista que variara en los if
                // si es cupon o rasca
                String producto = request.getParameter("tipoproducto");

                switch (pag) {
                    case "inicio":// si es la primera vez te lleva a decidir que quieres
                        usuario.put("inicio", true);
                        break;
                    case "0": //cuando has decidido rasca o cupon esta es tu pagina inicial
                        paginabase = Integer.parseInt(pag);
                        usuario.put("primera", false);// no necesitas el boton anterior
                        if (producto.equals("rasca")) {// si el dato viene del formulario rasca
                            usuario.put("productorasca", true);//que aparezca el formulario de rasca
                            tablarasca = rascaDao.obtenerRascaPaginacion(paginabase);// datos de 5 en 5
                            if (totalpaginasrasca > 1) { // si solo hay 5 no ncesitas siguiente pagina
                                usuario.put("ultima", true);
                            }
                        } else {
                            usuario.put("productocupon", true);// te lleva al formulario cupon
                            tablacupon = cuponDao.obtenerCuponPaginacion(paginabase);// datos cupones
                            System.out.println(tablacupon);
                            if (totalpaginascupon > 1) {
                                usuario.put("ultima", true);//si no hay mas datos que 5 no necesitas boton siguiente
                            }

                        }
                        paginabase++;// aumentas la pagina en 1
                        break;
                    case "ultimapagina":// boton ultima pagina
                        usuario.put("ultima", false);// no necesitas boton siguiente
                        if (producto.equals("rasca")) {//si es rasca
                            usuario.put("productorasca", true);//formulario rasca
                            tablarasca = rascaDao.obtenerRascaPaginacion(totalpaginasrasca - 1);// los ultimos 5 datos
                            paginabase = totalpaginasrasca;// el dato de la pagina en la que estas usando las paginas totales
                            if (totalpaginasrasca > 1) {// si no hay mas que cinco datos
                                usuario.put("primera", true);
                            }
                        } else {//si es cupon
                            usuario.put("productocupon", true);//formulario cupon
                            tablacupon = cuponDao.obtenerCuponPaginacion(totalpaginascupon - 1);//ultimos 5 datos
                            paginabase = totalpaginascupon;
                            if (totalpaginascupon > 1) {
                                usuario.put("primera", true);
                            }
                        }
                        break;
                    case "siguiente":// boton siguiente
                        paginabase = Integer.parseInt(paginaactual);//sacas pagina actual
                        if (producto.equals("rasca")) {// si es rasca
                            usuario.put("productorasca", true);//formulario rasca
                            tablarasca = rascaDao.obtenerRascaPaginacion(paginabase);//sacamoos datos con la pagina
                            if (!rascaDao.obtenerRascaPaginacion(paginabase + 1).isEmpty()) {// si no hay mas datos no necesitas boton siguiente
                                usuario.put("ultima", true);
                            }
                        } else {// si es cupon
                            usuario.put("productocupon", true);
                            tablacupon = cuponDao.obtenerCuponPaginacion(paginabase); //pagina actual con datos siguiente
                            if (!cuponDao.obtenerCuponPaginacion(paginabase + 1).isEmpty()) {
                                usuario.put("ultima", true);
                            }
                        }
                        paginabase++;// aumentas la pagina
                        usuario.put("primera", true);// guardas boton anterior 
                        break;
                    case "anterior":
                        paginabase = Integer.parseInt(paginaactual) - 1;
                        if (producto.equals("rasca")) {
                            usuario.put("productorasca", true);
                            tablarasca = rascaDao.obtenerRascaPaginacion(paginabase);
                            if (rascaDao.obtenerRascaPaginacion(paginabase - 1) == null) {
                                usuario.put("primera", true);
                            }
                        } else {
                            usuario.put("productocupon", true);
                            tablacupon = cuponDao.obtenerCuponPaginacion(paginabase);
                            if (cuponDao.obtenerCuponPaginacion(paginabase - 1) == null) {
                                usuario.put("primera", true);
                            }
                        }
                        usuario.put("ultima", true);
                        break;

                }
                usuario.put("pagina", paginabase);
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
