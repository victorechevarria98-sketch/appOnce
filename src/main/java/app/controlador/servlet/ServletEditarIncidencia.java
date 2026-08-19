/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package app.controlador.servlet;

/*

recibes la opcion editar o eliminar de incidencias



 */
import app.modelo.dao.CuponDao;
import app.modelo.dao.IncidenciasDao;
import app.modelo.dao.LugarDao;
import app.modelo.dao.PedidoCuponDao;
import app.modelo.dao.PedidoRascaDao;
import app.modelo.dao.RascaDao;
import app.modelo.entidad.Aviso;
import app.modelo.entidad.Cupon;
import app.modelo.entidad.Incidencias;
import app.modelo.entidad.Lugar;
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
@WebServlet(name = "ServletEditarIncidencia", urlPatterns = {"/ServletEditarIncidencia"})
public class ServletEditarIncidencia extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private RascaDao rascaDao;
    private CuponDao cuponDao;
    private IncidenciasDao incidenciasDao;
    private PedidoCuponDao pedidoCuponDao;
    private PedidoRascaDao pedidoRascaDao;
    private LugarDao lugarDao;

    public void init() {
        rascaDao = new RascaDao();
        cuponDao = new CuponDao();
        incidenciasDao = new IncidenciasDao();
        pedidoCuponDao = new PedidoCuponDao();
        pedidoRascaDao = new PedidoRascaDao();
        lugarDao = new LugarDao();
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
        String idincidencia = request.getParameter("idincidencia");
        int id = Integer.parseInt(idincidencia);// la incidencia pasada

        if (vista.equals("editar")) {
            ArrayList<Rasca> listarasca = new ArrayList<>();
            ArrayList<Cupon> listacupon = new ArrayList<>();
            listarasca = rascaDao.ListaRasca();// paso la lista de productos para el html
            listacupon = cuponDao.listaCupon();
            usuario.put("rasca", listarasca);
            usuario.put("cupon", listacupon);
            Incidencias i = new Incidencias();
            i = incidenciasDao.obtenerIncidenciaPorID(id); // incidencia completa
            usuario.put("incidencias", i);// paso la incidencia para el formulario
            int idcupon = i.getIdpedidocupon();
            int idrasca = i.getIdpedidorasca();
//paso el id del pedido para saber si es cupon o rasca a traves de si es 0 o no ya que como seria null en java pasara a 0
            Map<String, Object> pedido = new HashMap<>();
            String nombre;//envio el nombre del producto
            if (idcupon != 0) {
                pedido = pedidoCuponDao.obtenerPedidoPorIDPedidoCupon(i.getIdpedidocupon()); //saco el pedido con la incidencia
                nombre = (String) pedido.get("producto");//saco el dato del nombre del producto del metodo anterior para tenerlo y enviarlo al servlet si no edita el producto
                usuario.put("pedidoscupones", true);
            } else {// si el idcupon no existe es que es un rasca
                pedido = pedidoRascaDao.obtenerPedidoPorIDPedidoRasca(i.getIdpedidorasca());
                nombre = (String) pedido.get("producto");
                usuario.put("pedidosrasca", true);
            }
            Lugar l = new Lugar();
            l = lugarDao.obtenerLugarCompletoPorIDLugar(i.getIdlugar());
               
            usuario.put("lugar", l);// paso el lugar por si debe editar algo de el
            usuario.put("productoactual", nombre);
            usuario.put("idincidencia", id);
            RenderVista.renderizarVista(response, getServletContext().getRealPath("editarincidencia.html"), usuario);
        } else if (vista.equals("eliminar")) {
            // cojes el id
            // miras si es rasca o cupon
            boolean exito = false;
            exito = incidenciasDao.eliminarIncidecncia(id);
            if (exito == true) {
                RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("La incidencia ya no existe no existe", "Operacion realizada", "ServletMenuPrincipal"));
            } else {
                RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("¿Vaya no hay incidencia?", "Buena suerte investigando😣", "ServletMenuPrincipal"));
            }

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
