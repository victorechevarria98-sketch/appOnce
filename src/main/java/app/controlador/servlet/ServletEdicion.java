/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
/*

recibes datos de los furmularios de editar pedidos
datos del fomulario para editar tu perfil: usuario,trabajador y lugar
cambio contraseña



*/
package app.controlador.servlet;

import app.modelo.dao.LugarDao;
import app.modelo.dao.PedidoCuponDao;
import app.modelo.dao.PedidoRascaDao;
import app.modelo.dao.TrabajadorDao;
import app.modelo.dao.UsuarioDao;
import app.modelo.entidad.Aviso;
import app.modelo.entidad.Lugar;
import app.modelo.entidad.PedidoCupon;
import app.modelo.entidad.PedidoRasca;
import app.modelo.entidad.Trabajador;
import app.modelo.entidad.Trabajador.Actividad;
import app.modelo.entidad.Trabajador.Contrato;
import app.modelo.entidad.Trabajador.Kiosko;
import app.modelo.entidad.Usuario;
import app.modelo.entidad.Usuario.rol;
import app.vista.mustache.RenderVista;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Alumnos
 */
@WebServlet(name = "ServletEdicion", urlPatterns = {"/ServletEdicion"})
public class ServletEdicion extends HttpServlet {

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

    @Override
    public void init() {
        usuarioDao = new UsuarioDao(); // creo instancia para do post
        trabajadorDao = new TrabajadorDao();
        lugarDao = new LugarDao();
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
        //como siempre sesion por si acaso y luego ya veremos
        HttpSession sesion = request.getSession(false);
        Usuario usu = (Usuario) sesion.getAttribute("usuario");
        String vista = request.getParameter("editar");
        // para la id en cada caso
        String id;
        // variable para saber si la edicion ha sido correcta
        boolean exito;

        switch (vista) {
            case "usuario":
                id = request.getParameter("idusuario");
                int idusuario = Integer.parseInt(id);
                String nombreusu = request.getParameter("nombreusuario").trim();
                String emailusu = request.getParameter("emailusuario").trim(); 
// comprueba que el nuevo no es unique comparadonlo con un select y devuelve aviso si existe
                String activorespuesta = request.getParameter("activo").trim();
                boolean activo = Boolean.parseBoolean(activorespuesta);
                String perfilrespuesta = request.getParameter("perfil").trim();
                rol perfil = Usuario.getEstadoFromString(perfilrespuesta);
                ArrayList<Usuario> lista = new ArrayList<>();
                lista = usuarioDao.seleccionTodosuariosAbsoluta();
                for (Usuario u : lista) {
                    if (u.getEmailUsu().equals(emailusu) && u.getIdusu() != idusuario) {
                        RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("correo repetido, prueba otra vez", "Operacion fallida, muchas gracias", "ServletMenuPrincipal"));
                        break;
                    }
                }
                Usuario u = new Usuario();
                u.setNombreUsu(nombreusu);
                u.setEmailUsu(emailusu);
                u.setActivo(activo);
                u.setPerfil(perfil);
                u.setIdusu(idusuario);
                exito = usuarioDao.updateUsuario(u);
                if (exito == true) {
                    RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("Usuario actualizado", "Operacion realizada, muchas gracias", "ServletMenuPrincipal"));
                } else {
                    RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("Error", "Operacion fallida comprueba datos", "ServletMenuPrincipal"));
                }

                break;
            case "trabajador":
                try {
                    id = request.getParameter("idtrabajador");
                    int idtrabajador = Integer.parseInt(id);
                    String nombre = request.getParameter("nombretrabajador").trim();
                    String Apellidos = request.getParameter("apellidoseusuario").trim();
                    String dni = request.getParameter("dni");
                    String nacimiento = request.getParameter("fechanacimineto");
                    String telefonoempresa = request.getParameter("telefonoempresa");
                    int tlf = Integer.parseInt(telefonoempresa);
                    String alta = request.getParameter("baja").trim();
                    boolean baja = Boolean.parseBoolean(alta);
                    String kiosko = request.getParameter("tipokiosko").trim();
                    Kiosko k = (Kiosko) Trabajador.getEstadoFromString(kiosko);

                    String contrato = request.getParameter("tipocontrato").trim();
                    Contrato c = (Contrato) Trabajador.getEstadoFromString(contrato);

                    String actividad = request.getParameter("tipoactividad").trim();
                    Actividad a = (Actividad) Trabajador.getEstadoFromString(actividad);

                    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

                    Date fechanacim = formato.parse(nacimiento);

                    Trabajador tb = new Trabajador();
                    tb.setNombreTrab(nombre);
                    tb.setApellidosTrab(Apellidos);
                    tb.setNIF_Trab(dni);
                    tb.setFechaNaTrab(fechanacim);
                    tb.setTldEmp(tlf);
                    tb.setBajaLaboral(baja);
                    tb.setTipoKiosko(k);
                    tb.setTipoContrato(c);
                    tb.setTipoActividad(a);
                    tb.setIdtrab(idtrabajador);
                    exito = trabajadorDao.updateTrabajador(tb);
                    if (exito == true) {
                        RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("Usuario actualizado", "Operacion realizada, muchas gracias", "ServletMenuPrincipal"));
                    } else {
                        RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("Error", "Operacion fallida comprueba datos", "ServletMenuPrincipal"));
                    }
                    break;
                } catch (ParseException e) {
                    RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("Error", "Operacion fallida comprueba datos", "ServletMenuPrincipal"));
                }

            case "lugar":
                id = request.getParameter("idlugar");
                int idlugar = Integer.parseInt(id);
                String calle = request.getParameter("calle").trim();
                if (calle.isBlank()) {
                    calle = null;
                }
                String municipio = request.getParameter("municipio").trim(); // si no escribe nada pasa un vacio
                if (municipio.isBlank()) {
                    municipio = null;
                }
                String codigo = request.getParameter("codigoPostal");
                int codpostal = Integer.parseInt(codigo);

                Lugar lg = new Lugar();

                lg.setCalle(calle);
                lg.setMunicipio(municipio);
                lg.setCodPostal(codpostal);
                lg.setIdlugar(idlugar);

                exito = lugarDao.updatelugar(lg);
                if (exito == true) {
                    RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("Lugar de trabajo actualizado", "Operacion realizada, muchas gracias", "ServletMenuPrincipal"));
                } else {
                    RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("Error", "Operacion fallida comprueba datos", "ServletMenuPrincipal"));
                }

                break;
            case "contraseña":
                id = request.getParameter("idusuario");
                int idusu = Integer.parseInt(id);
                String contraseña = request.getParameter("contraseñanueva").trim();
                exito = usuarioDao.updateContraseña(contraseña, idusu);
                if (exito == true) {
                    RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("contraseña actualizado", "Operacion realizada, muchas gracias", "ServletMenuPrincipal"));
                } else {
                    RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("Error", "Operacion fallida comprueba datos", "ServletMenuPrincipal"));
                }
                break;
            case "pedido":
                id = request.getParameter("idpedido");
                int idpedido = Integer.parseInt(id);
                String producto = request.getParameter("producto");
                String fechatexto = request.getParameter("fechapedido");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                LocalDateTime fechaHora = LocalDateTime.parse(fechatexto, formatter);
// parse la fecha usando un plano que sirva para localtimedate
                String numserie = request.getParameter("serie");
                String num = request.getParameter("cantidad");
                int cantidad = Integer.parseInt(num);
// aqui creamos una variable para buscar a traves de un for si el nombre del cupon que hemos extraido aparece en la base de datos
                boolean cupones = false;
                //pasamos en un array la lista de cupones
                ArrayList<String> listacupones = pedidoCuponDao.listaCuponNombre();
                int idprocuto = 0;
                // si se recorre el array y no se pasa por el if vamos al siguiente if
                for (String temporal : listacupones) {
                    if(producto.equals(temporal)){
                        // cuando pasas y se cumple la condicion el true hara que el primer if ocurra
                        cupones = true;
                        idprocuto = pedidoCuponDao.obtenerIdCupon(producto);
                    }
                }
                exito = false;
                if(cupones){
                    PedidoCupon pc = new PedidoCupon();
                    pc.setFechaPedidoCupon(fechaHora);
                    pc.setNumSerierCupon(numserie);
                    pc.setCantPedidoC(cantidad);
                    pc.setIdcup(idprocuto);
                    pc.setIdpedidocupon(idpedido);
                    exito = pedidoCuponDao.updatePedidoCupon(pc);
                    if (exito == true) {
                            RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("El pedido ha cambiado", "Operacion realizada", "ServletMenuPrincipal"));
                        } else {
                            RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("¿Vaya no hay pedido cupon?", "Buena suerte investigando😣", "ServletMenuPrincipal"));
                        }
                } else {
                    idprocuto = pedidoRascaDao.obtenerIdRasca(producto);
                    if(idprocuto == 0){
                        System.out.println("idprocuto sale o, mira porque");
                    }
                    PedidoRasca pr = new PedidoRasca();
                    pr.setFechaPedidoRasca(fechaHora);
                    pr.setNumSerierRasca(numserie);
                    pr.setCantPedidoR(cantidad);
                    pr.setIdras(idprocuto);
                    pr.setIdpedidorasca(idpedido);
                    exito = pedidoRascaDao.updatePedidoRasca(pr);
                    if (exito == true) {
                            RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("El pedido ha cambiado", "Operacion realizada", "ServletMenuPrincipal"));
                        } else {
                            RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("¿Vaya no hay pedido rasca?", "Buena suerte investigando😣", "ServletMenuPrincipal"));
                        }
                }

                break;
            

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
