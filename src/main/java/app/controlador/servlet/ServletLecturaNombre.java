/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
 /* 


por aqui pasas para nuevo pedido, incidencia,ir a tu perfil
lleva a formulario para editar perfil con usuario ,trabajador y lugar
lleva a formulario para editar pedidos para admin y trabajador y contraseña 
el trabajador solo tiene una tabla y no puedo difenrenciar si el id es rasca o cupon por lo tanto usa un editar difernete a el del admin
pero el admin necesita el id del trabajador que va ha hecho el pedido y su id 
eliminar algo de lo anterior

 */
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
import app.modelo.entidad.Lugar;
import app.modelo.entidad.Rasca;
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
import java.util.Map;

/**
 *
 * @author Alumnos
 */
@WebServlet(name = "ServletLecturaNombre", urlPatterns = {"/ServletLecturaNombre"})
public class ServletLecturaNombre extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private UsuarioDao usuarioDao; // cargada e inicializada para no hacerlo en cada case del menu
    private TrabajadorDao trabajadorDao;
    private LugarDao lugarDao;
    private PedidoRascaDao pedidoRascaDao;
    private PedidoCuponDao pedidoCuponDao;
    private RascaDao rascaDao;
    private CuponDao cuponDao;

    @Override
    public void init() {
        usuarioDao = new UsuarioDao(); // creo instancia para do get
        trabajadorDao = new TrabajadorDao();
        lugarDao = new LugarDao();
        pedidoRascaDao = new PedidoRascaDao();
        pedidoCuponDao = new PedidoCuponDao();
        rascaDao = new RascaDao();
        cuponDao = new CuponDao();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) // como lo enviamos en href eso nos lleva siempre al get no al post, podrias hacerlo tambien en processrequest
            throws ServletException, IOException {
        // buscamos la sesion abierta, la metemos en el hashmap que ira siempre al rendervista
        HttpSession sesion = request.getSession(false);
        Usuario usu = (Usuario) sesion.getAttribute("usuario");
        // sacamos el email y lo usamos para las funciones que necesites para identificarte y extraer datos
        String emailusuario = usu.getEmailUsu();
        // el mpa para el render vista
        Map<String, Object> usuario = new HashMap<>();
        String vista = request.getParameter("vista");
        usuario.put("usuario", usu);
        // se mete para usarlo en mustache por html
        String dueñosesion = usu.getNombreUsu();
        usuario.put("dueñosesion", dueñosesion);
        ArrayList<Rasca> listarasca = new ArrayList<>();
        ArrayList<Cupon> listacupon = new ArrayList<>();
        switch (vista) {
            /*            

            
            
hay que hacer un cambio en nuevo pedido y nueva incidencia para que el formulario sea dinamico y cambie dependiendo de que productos hay si editas uno eliminas otro o lo das de alta
            

            
             */
            case "nuevopedido":
                listarasca = rascaDao.ListaRasca();
                listacupon = cuponDao.listaCupon();
                usuario.put("rasca", listarasca);
                usuario.put("cupon", listacupon);
                RenderVista.renderizarVista(response, getServletContext().getRealPath("nuevopedido.html"), usuario);
                break;
            case "nuevaincidencia":
                listarasca = rascaDao.ListaRasca();
                listacupon = cuponDao.listaCupon();
                usuario.put("rasca", listarasca);
                usuario.put("cupon", listacupon);
                RenderVista.renderizarVista(response, getServletContext().getRealPath("nuevaincidencia.html"), usuario);
                break;
            case "tuperfil":
                Trabajador tb = new Trabajador();
                Lugar lb = new Lugar();
                ArrayList<Lugar> listalugar = new ArrayList<>();
//guardamos lugar, trabajador y usuario para envialo a tuperfil.html
                listalugar = lugarDao.obtenerLugarCompletoPoremail(emailusuario);
                tb = trabajadorDao.obtenerTrabajadorCompletoPorEmail(emailusuario);
                usuario.put("trabajador", tb);
                usuario.put("lugar", listalugar);
                RenderVista.renderizarVista(response, getServletContext().getRealPath("tuperfil.html"), usuario);
                break;
            case "editar":

                String operacion = request.getParameter("operacion");
                switch (operacion) {
                    case "editarlugar" -> {
                        // debes buscar por id lugar
                        usuario.put("casolugar", operacion); // aquie podria haber true no operacion, lo puso netbeans automaticamente
                        String id = request.getParameter("idlugar");
                        Lugar lb2 = new Lugar();
                        lb2 = lugarDao.obtenerLugarCompletoPorIDLugar(Integer.parseInt(id));
                        usuario.put("lugar", lb2);
                        RenderVista.renderizarVista(response, getServletContext().getRealPath("editorperfil.html"), usuario);
                    }
                    case "editarusuario" -> {
                        usuario.put("casousuario", operacion);
                        String id = request.getParameter("idusuario");
                        usuario.put("idusuario", Integer.valueOf(id));
                        // comprobamos si es admin con el perfil dentro de la sesion
                        if ("admin".equalsIgnoreCase(usu.getPerfil().name())) {
                            usuario.put("administrador", true);
                        } else {// si es trabajador, saldra otra parte del formulario
                            usuario.put("trabajador", true);
                        }
                        RenderVista.renderizarVista(response, getServletContext().getRealPath("editorperfil.html"), usuario);
                    }
                    case "editartrabajador" -> {
                        usuario.put("casotrabajador", operacion);
                        String id = request.getParameter("idtrabajador");//trabajador a editar
                        Trabajador tb2 = new Trabajador();
                        //sacas todos los datos a editar y los envias al formulario
                        tb2 = trabajadorDao.obtenerTrabajadorCompletoPorIDTrabajador(Integer.parseInt(id));
                        usuario.put("trabajador", tb2);
                        if ("admin".equalsIgnoreCase(usu.getPerfil().name())) {
                            usuario.put("administrador", true);
                        } else {
                            usuario.put("trabajadorlimite", true);
                        }
                        RenderVista.renderizarVista(response, getServletContext().getRealPath("editorperfil.html"), usuario);
                    }
                    case "editarcontraseña" -> {
                        usuario.put("casocontraseña", operacion);
                        String id = request.getParameter("idusuario");
                        usuario.put("idusuario", Integer.valueOf(id));
                        RenderVista.renderizarVista(response, getServletContext().getRealPath("editorperfil.html"), usuario);

                    }
                    case "editarpedido" -> {
                        //metemos algo en la clave para que se vea esa parte en el html, podria haber un true tambien
                        String id = request.getParameter("idpedido");
                        // pongo el id por separado por si lo necesito volver a poner dentro del formulario y no perderlo en los calculos
                        //para mi solo 
                        usuario.put("idpedido", Integer.valueOf(id));
                        Map<String, Object> formulario = new HashMap<>();
                        // si es cupon hace algo 
                        String producto = request.getParameter("tipo");
                        if (producto.equals("cupon")) {
                            // no uso la entidad pedidocupon porque extraigo tambien el nombre del trabajador y el nombre del producto
                            formulario = pedidoCuponDao.obtenerPedidoPorIDPedidoCupon(Integer.valueOf(id));
                        } else {
                            //como no es cupon es solo rasca ya que solo puede ser un valor operacion por como lo envio
                            formulario = pedidoRascaDao.obtenerPedidoPorIDPedidoRasca(Integer.valueOf(id));
                        }
//como el valor dentro de la clave formulario es el mismo map, puedes meterlo fuera de los if ya que solo cambiara los datos dentro de estos
                        usuario.put("formulario", formulario);
                        //metemos esto para que ponga el formulario del trabajador ya que tiene mas restricciones que el de administrador y por lo tanto diferente formulario
                        usuario.put("trabajadorlimite", true);
                        RenderVista.renderizarVista(response, getServletContext().getRealPath("editorpedidos.html"), usuario);

                    }
                    case "editarpedidocupon" -> {
                        String id = request.getParameter("idpedidocupon");
                        //guarda el id por separado para tenerlo ya en el formulario luego, mas facil para mi
                        usuario.put("idpedido", Integer.valueOf(id));
                        Map<String, Object> formulario = new HashMap<>();
                        // busca los que quieres cambiar y necesito el nombre del trabajador y el nombre del producto en un solo map asi no necesito varia funciones
                        formulario = pedidoCuponDao.obtenerPedidoPorIDPedidoCupon(Integer.valueOf(id));
                        usuario.put("formulario", formulario);
                        if ("admin".equalsIgnoreCase(usu.getPerfil().name())) {
                            usuario.put("administrador", true);
                        }
                        RenderVista.renderizarVista(response, getServletContext().getRealPath("editorpedidos.html"), usuario);

                    }
                    case "editarpedidorasca" -> { // es el mismo proceso para ambos rasca y cupon
                        String id = request.getParameter("idpedidorasca");
                        usuario.put("idpedido", Integer.valueOf(id));
                        Map<String, Object> formulario = new HashMap<>();
                        formulario = pedidoRascaDao.obtenerPedidoPorIDPedidoRasca(Integer.valueOf(id));
                        usuario.put("formulario", formulario);
                        if ("admin".equalsIgnoreCase(usu.getPerfil().name())) {
                            usuario.put("administrador", true);
                        }
                        RenderVista.renderizarVista(response, getServletContext().getRealPath("editorpedidos.html"), usuario);

                    }
                }
                break;

            case "eliminar":
                //elimiar pedidos, usuario, trabajador o lugar
                String eliminacion = request.getParameter("operacion");
                boolean exito = false;// esta variable sirve para saber a que pagina de avisos llegas
                switch (eliminacion) {
                    case "eliminarlugar" -> {
                        String idlugar = request.getParameter("idlugar");
                        exito = lugarDao.eliminarLugar(Integer.parseInt(idlugar));
                        if (exito == true) {
                            RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("El lugar ya no existe no existe", "Operacion realizada", "ServletMenuPrincipal"));
                        } else {
                            RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("¿Vaya no hay lugar?", "Buena suerte investigando😣", "ServletMenuPrincipal"));
                        }
                    }
                    case "eliminarusuario" -> {
                        String idusu = request.getParameter("idusuario");
                        exito = usuarioDao.eliminarUsuario(Integer.parseInt(idusu));
                        if (exito == true) {
                            RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("El usuario ya no existe no existe", "Operacion realizada, hasta siempre", "index.html"));
                        } else {
                            RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("¿Vaya no hay usuario?", "Buena suerte investigando😣", "ServletMenuPrincipal"));
                        }
                    }
                    case "eliminartrabajador" -> {
                        String idtrab = request.getParameter("idtrabajador");
                        exito = trabajadorDao.eliminarTrabajador(Integer.parseInt(idtrab));
                        if (exito == true) {
                            RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("El trabajador ya no existe no existe", "Operacion realizada", "ServletMenuPrincipal"));
                        } else {
                            RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("¿Vaya no hay trabajador?", "Buena suerte investigando😣", "ServletMenuPrincipal"));
                        }
                    }
                    case "eliminarpedido" -> {
                        // cojes el id
                        String idpedido = request.getParameter("idpedido");
                        // miras si es rasca o cupon
                        String producto = request.getParameter("tipo");
                        if (producto.equals("cupon")) {
                            exito = pedidoCuponDao.eliminarPedidoCupon(Integer.parseInt(idpedido));
                        } else {
                            exito = pedidoRascaDao.eliminarPedidoRasca(Integer.parseInt(idpedido));
                        }

                        if (exito == true) {
                            RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("El pedido ya no existe no existe", "Operacion realizada", "ServletMenuPrincipal"));
                        } else {
                            RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("¿Vaya no hay pedido?", "Buena suerte investigando😣", "ServletMenuPrincipal"));
                        }
                    }
                    case "eliminarpedidocupon" -> {
                        String idpedido = request.getParameter("idpedidocupon");
                        exito = pedidoCuponDao.eliminarPedidoCupon(Integer.parseInt(idpedido));
                        if (exito == true) {
                            RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("El pedido ya no existe no existe", "Operacion realizada", "ServletMenuPrincipal"));
                        } else {
                            RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("¿Vaya no hay pedido cupon?", "Buena suerte investigando😣", "ServletMenuPrincipal"));
                        }
                    }
                    case "eliminarpedidorasca" -> {
                        String idpedido = request.getParameter("idpedidorasca");
                        exito = pedidoRascaDao.eliminarPedidoRasca(Integer.parseInt(idpedido));
                        if (exito == true) {
                            RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("El pedido ya no existe no existe", "Operacion realizada", "ServletMenuPrincipal"));
                        } else {
                            RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("¿Vaya no hay pedido rasca?", "Buena suerte investigando😣", "ServletMenuPrincipal"));
                        }
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
