/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
 /* 


cada vez que entras pasa a un nuevo trabajador y usuario y te lleva al menu principal dependiente de administrador o trabajador

 */
package app.controlador.servlet;

import app.modelo.dao.PedidoCuponDao;
import app.modelo.dao.PedidoRascaDao;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Alumnos
 */
@WebServlet(name = "UsuarioNuevo", urlPatterns = {"/UsuarioNuevo"})
public class ServletUsuarioNuevo extends HttpServlet {

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
    public void init() {
        usuarioDao = new UsuarioDao(); // creo instancia para do post cuando se crea un nuevo servlet y no tienes que repetir cada vez el new
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
        //extraemos todos los datos del formulario y quitamos expacios por si acaso
        String nombretrabajador = request.getParameter("nombretrabajador").trim();
        String apellidoseusuario = request.getParameter("apellidoseusuario").trim();
        String emailusuario = request.getParameter("emailusuario").trim();
        String perfil = request.getParameter("perfil").trim();
        String dni = request.getParameter("dni").trim();
        String fechanacimineto = request.getParameter("fechanacimineto").trim();
        String fechacontrato = request.getParameter("fechacontrato").trim();
        String telefonoempresa = request.getParameter("telefonoempresa").trim();
        String nombreusuario = request.getParameter("nombreusuario").trim();
        String passwordusuario = request.getParameter("passwordusuario").trim();
        //creamos el plano para pasar el string fecha a un date, en este caso
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        //creamos un usuario y trabajador para poder usar las clase para las funciones 
        Usuario usu = new Usuario();
        Trabajador tb = new Trabajador();
        try { // el try viene por la fecha y usar format
            //datos usuario
            usu.setNombreUsu(nombreusuario);
            usu.setEmailUsu(emailusuario);
            // usamos la funcion de la entidad usuario pra convertir el string perfil en el atributo rol e instancia perfil
            usu.setPerfil(Usuario.getEstadoFromString(perfil));

            //datos trabajador
            tb.setNombreTrab(nombretrabajador);
            tb.setApellidosTrab(apellidoseusuario);
            tb.setNIF_Trab(dni);
            // pasamos la fecha a date usando el formato como si de una clase no primitiva se tratase
            Date fechacontra = formato.parse(fechacontrato);
            tb.setFechaIncorTrab(fechacontra);
            //dependiendo de que dato se ha metido o no, hay que introducir en trabajador estas variables ya que no hay null de int
            if (telefonoempresa.isEmpty() && fechanacimineto.isEmpty()) {// no ha escrito nada
                tb.setTldEmp(0);
                tb.setFechaNaTrab(null);
            } else if (telefonoempresa.isEmpty()) { // solo ha esctrito fecha
                tb.setTldEmp(0);
                Date fechanacim = formato.parse(fechanacimineto);
                tb.setFechaNaTrab(fechanacim);
            } else if (fechanacimineto.isEmpty()) { // solo ha escrito telefono
                tb.setFechaNaTrab(null);
                int tlf = Integer.parseInt(telefonoempresa);
                tb.setTldEmp(tlf);
            } else { // ha esctrito ambas
                int tlf = Integer.parseInt(telefonoempresa);
                Date fechanacim = formato.parse(fechanacimineto);
                tb.setFechaNaTrab(fechanacim);
                tb.setTldEmp(tlf);
            }
            usuarioDao.introducirNuevoUsuarioTrabajador(usu, tb, passwordusuario); // introduces usuario trabajador y contraseña

            HttpSession sesion = request.getSession(true); // menu que repite todo lo que hace el servlet login
            sesion.setAttribute("usuario", usu);
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
                tabla = td.pedidoTotalTrabajador(nombreusuario);
                Map<String, Object> datos = new HashMap<>();
                datos.put("tabla", tabla);
                RenderVista.renderizarVista(response, getServletContext().getRealPath("trabajador/trabajador.html"), datos);
            }

        } catch (ParseException e) {
            System.out.println("Error: El formato del String no coincide con el patrón.");
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
