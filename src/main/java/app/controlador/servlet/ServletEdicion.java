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

import app.modelo.dao.IncidenciasDao;
import app.modelo.dao.LugarDao;
import app.modelo.dao.PedidoCuponDao;
import app.modelo.dao.PedidoRascaDao;
import app.modelo.dao.TrabajadorDao;
import app.modelo.dao.UsuarioDao;
import app.modelo.entidad.Aviso;
import app.modelo.entidad.Incidencias;
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
    private UsuarioDao usuarioDao; // cargada e inicializada para no hacerlo cada vez
    private TrabajadorDao trabajadorDao;
    private LugarDao lugarDao;
    private PedidoRascaDao pedidoRascaDao;
    private PedidoCuponDao pedidoCuponDao;
    private IncidenciasDao incidenciasDao;
    
    @Override
    public void init() {
        usuarioDao = new UsuarioDao(); // creo instancia para do post o do get
        trabajadorDao = new TrabajadorDao();
        lugarDao = new LugarDao();
        pedidoRascaDao = new PedidoRascaDao();
        pedidoCuponDao = new PedidoCuponDao();
        incidenciasDao = new IncidenciasDao();
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
                id = request.getParameter("idusuario"); // pasamos el id para hacer la edicion de un usuario general, y no solo el de tu perfil
                int idusuario = Integer.parseInt(id);
                String nombreusu = request.getParameter("nombreusuario").trim();
                String emailusu = request.getParameter("emailusuario").trim();
// comprueba que el nuevo no es unique comparadonlo con un select y devuelve aviso si existe
                String activorespuesta = request.getParameter("activo").trim();
                boolean activo = Boolean.parseBoolean(activorespuesta);
                String perfilrespuesta = request.getParameter("perfil").trim();
                rol perfil = Usuario.getEstadoFromString(perfilrespuesta);// saca el perfil del string para ver si concuerda con el rol, sino devolvera el rol de trabajador
                ArrayList<Usuario> lista = new ArrayList<>();
                lista = usuarioDao.seleccionTodosuariosAbsoluta(); //lista de todos sin tener en cuenta si esta activo para poder ver los usuarios que ya no "exiaten"
                for (Usuario u : lista) { // todo esto para comprobar que es correcto, mira si javascript puede hacerlo mas simple
                    if (u.getEmailUsu().equals(emailusu) && u.getIdusu() != idusuario) {
                        RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("correo repetido, prueba otra vez", "Operacion fallida, muchas gracias", "ServletMenuPrincipal"));
                        break;
                    }
                }
                Usuario u = new Usuario();// envia todo para el update necesario
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
                try {//el try es por la fecha que al formatear necesitar guardar el error de alguna forma si no funciona
                    //como tiene varios enum en la base de datos, pasas todos por la funcion que nos devuelve el atributo de trabajador
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
// como no estas obligado a escribir estos datos en en el formulario hay que comprobar que no envias nada y ponerlo en null
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
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");// este es el formato para localdatetime
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
                    if (producto.equals(temporal)) {// si el producto pasado por formulario es igual al nombre pasado por array cambias a true la variable
                        cupones = true;
                        idprocuto = pedidoCuponDao.obtenerIdCupon(producto);
                    }
                }
                exito = false;
                if (cupones) { // aqui decide si es cupon o rasca
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
                } else {// si es rasca hace esto por eliminacion
                    idprocuto = pedidoRascaDao.obtenerIdRasca(producto);
                    if (idprocuto == 0) {// por si acaso hay algun problema no solo lo envias a la pagian de error compruebas si este es el error
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
            case "incidencia":
                //id de incidente y lugar si se editan
                id = request.getParameter("idincidencia");
                String lugar = request.getParameter("idlugar");
                
                String caso = request.getParameter("incident");
                String comentario = request.getParameter("comentario");
                // como el formato por formulario es datetime local solo haces el parse directo sin format
                String fechaeditada = request.getParameter("fecha");
                LocalDateTime time = LocalDateTime.parse(fechaeditada);
                //cambias a boolean 
                String solucion = request.getParameter("solucion");
                Boolean corregido = Boolean.parseBoolean(solucion);
                String idtrabajadorincidencia = request.getParameter("idtrabajador");

                //producto anterior y actual para ver si cambia y a cual
                String productoanterior = request.getParameter("productoanterior");
                String productoeditado = request.getParameter("producto");

                //sacamos los id del pedido sean o no null
                String idrascapedido = request.getParameter("pedidorasca");
                String idcuponpedido = request.getParameter("pedidocupon");

                //datos del lugar para comprobar cambio y si hay que añadir un nuevo lugar
                String postal = request.getParameter("codigo_postal");
                //si el municipio cambia y cual era el municipio
                String municipioahora = request.getParameter("municipio");
                String municipioanterior = request.getParameter("municipioanterior");
                
                String sitio = request.getParameter("lugar");

                //comprobamos lugar con la funcion nuevo lugar que mirara si el id existe con esos datos y si no es asi insert del nuevo lugar
                Lugar l = new Lugar();
                l.setCalle(sitio);
                // si no ha escojido nada recibimos null o vacio
                l.setCodPostal(Integer.parseInt("postal"));
                l.setIdtrab(Integer.parseInt(idtrabajadorincidencia));
                if (municipioahora == null || municipioahora.isEmpty()) {
                    l.setMunicipio(municipioanterior);
                } else {// en el otro caso que envie lo nuevo
                    l.setMunicipio(municipioahora);
                }
                //ahora sacamos el id ya sea nuevo o el que concuerde con los datos introducidos
                int idlugareditado = lugarDao.nuevoLugar(l);
                
                Incidencias i = new Incidencias();
                i.setTipoIncident(caso);
                i.setComentario(comentario);
                i.setFechaIncident(time);
                i.setIdtrab(Integer.parseInt(idtrabajadorincidencia));
                i.setIdlugar(idlugareditado);
                i.setSolucionada(corregido);
                // si lo que escribimos antes es distinto a lo que enviamos
                if (productoeditado != null || !productoeditado.isEmpty()) {
                    int idproducto = 0;
                    int idsacado = 0;
                    // Es un cupon?
                    if (productoeditado.startsWith("Cupones_", 0)) {
                        // sacamos el id del producto y comparamos si es el mismo que habia en el pedido
                        idproducto = Integer.parseInt(productoeditado.replace("Cupones_", ""));// con esto eliminas el texto mientras lo transformas a un int
                        idsacado = pedidoCuponDao.obtenerIdCuponConIDPedido(Integer.parseInt(idcuponpedido));
                        // el id del producto es distinto al del pedido original?
                        if (idproducto != idsacado) {
                            // tenemos que modificar el producto dentro de pedido
                            boolean update = pedidoCuponDao.updateCuponEnPedido(Integer.parseInt(idcuponpedido), idproducto);//id pedido y id del producto
                            if (!update) {
                                RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("error en actualizar pedido?", "Buena suerte investigando😣", "ServletMenuPrincipal"));
                                break;
                            }
                        }
                        //guardamos el id del pedido
                        i.setIdpedidocupon(idsacado);
                        i.setIdpedidorasca(0);
                    } else {
                        // sacamos el id del producto y comparamos si es el mismo que habia en el pedido
                        idproducto = Integer.parseInt(productoeditado.replace("Rascas_", ""));
                        idsacado = pedidoRascaDao.obtenerIdRascaConIDPedido(Integer.parseInt(idrascapedido));
                        if (idproducto != idsacado) { // el id del producto es distinto al del pedido original?
                            // tenemos que modificar el pedido
                            boolean update = pedidoRascaDao.updateRascaEnPedido(Integer.parseInt(idrascapedido), idproducto);
                            if (!update) {//error si no se actualiza bien
                                RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("error en actualizar pedido?", "Buena suerte investigando😣", "ServletMenuPrincipal"));
                                break;
                            }
                        }
                        i.setIdpedidorasca(idsacado);
                        i.setIdpedidocupon(0);
                    }
                    i.setIdincidencia(Integer.parseInt(id));
                    exito =incidenciasDao.updateIncidencias(i);
                    if (!exito) {
                        RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("¿Vaya no hay pedido?", "Buena suerte investigando😣", "ServletMenuPrincipal"));
                        break;
                    } else {
                        RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("actualizado", "adios, te odio, porque eres tan complicado,aaaaaaaahh", "ServletMenuPrincipal"));
                    }
                } else {
                    
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
