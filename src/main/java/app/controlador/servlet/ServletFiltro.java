/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package app.controlador.servlet;

import app.modelo.dao.FiltroDao;
import app.modelo.dao.TrabajadorDao;
import app.modelo.dao.UsuarioDao;
import app.modelo.entidad.Filtro;
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
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author Alumnos
 */
@WebServlet(name = "ServletFiltro", urlPatterns = {"/ServletFiltro"})
public class ServletFiltro extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private FiltroDao filtroDao;

    @Override
    public void init() {
        filtroDao = new FiltroDao();
        filtroDao = new FiltroDao();
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
        HttpSession sesion = request.getSession(false);
        Usuario usu = (Usuario) sesion.getAttribute("usuario");
        Map<String, Object> usuario = new HashMap<>();
        usuario.put("usuario", usu);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        String nombre = request.getParameter("nombre"); // nombre trabajador
        String apellido = request.getParameter("apellidos");
        String tipoproducto = request.getParameter("tipo"); // si es rasca o cupon
        String nombreproducto = request.getParameter("producto"); // nombre del producto del pedido
        String numeroserie = request.getParameter("serie");
        String kiosko = request.getParameter("tipokiosko");
        String contracto = request.getParameter("tipocontrato");
        String movilidad = request.getParameter("tipoactividad");
        String perfil = request.getParameter("perfil");
        String muni = request.getParameter("municipio");
        String codpostal = request.getParameter("CodPostal");

        // transforma fechas
        String fechainicio = request.getParameter("fechainicio"); // fecha del pedido
        LocalDateTime fechaminima = (fechainicio != null && !fechainicio.isEmpty()) ? LocalDateTime.parse(fechainicio) : null;

        String fechafinal = request.getParameter("fechafinal");
        LocalDateTime fechamaxima = (fechafinal != null && !fechafinal.isEmpty()) ? LocalDateTime.parse(fechafinal) : null;

        // transforma cantidad del pedido
        String cantidadpedidoinicio = request.getParameter("cantidadmin");
        int cantidadminima = (cantidadpedidoinicio != null && !cantidadpedidoinicio.isEmpty()) ? Integer.parseInt(cantidadpedidoinicio) : 0;

        String cantidadpedidofinal = request.getParameter("cantidadmax");
        int cantidadmaxima = (cantidadpedidofinal != null && !cantidadpedidofinal.isEmpty()) ? Integer.parseInt(cantidadpedidofinal) : 0;

        //transforma precio
        String precioinicio = request.getParameter("preciomin");
        int preciominimo = (precioinicio != null && !precioinicio.isEmpty()) ? Integer.parseInt("precioinicio") : 0;

        String preciofinal = request.getParameter("preciomax");
        int preciomaximo = (preciofinal != null && !preciofinal.isEmpty()) ? Integer.parseInt(preciofinal) : 0;

        //haremos un filtro dependiendo de si es cupon o rasca
        if (tipoproducto != null && !tipoproducto.isEmpty() && tipoproducto.equals("cupon")) {
            ArrayList<Map<String, Object>> listacupon = new ArrayList<>();
            listacupon = filtroDao.listaParaFiltradoCupon();
            List<Map<String, Object>> listafiltrada = listacupon.stream()
                    .filter(map -> {
                        String cadena;
                        int cantidad;
                        double precio;
                        LocalDateTime fechas;
                        if (nombre != null && !nombre.isEmpty()) {
                            cadena = (String) map.get("TrabajadorNombre");
                            if (cadena == null || !cadena.contains(nombre)) {
                                return false;
                            }
                        }
                        if (apellido != null && !apellido.isEmpty()) {
                            cadena = (String) map.get("TrabajadorApellidos");
                            if (cadena == null || !cadena.contains(apellido)) {
                                return false;
                            }
                        }
                        if (nombreproducto != null && !nombreproducto.isEmpty()) {
                            cadena = (String) map.get("producto");
                            if (cadena == null || !cadena.contains(nombreproducto)) {
                                return false;
                            }
                        }
                        if (numeroserie != null && !numeroserie.isEmpty()) {
                            cadena = (String) map.get("serie");
                            if (cadena == null || !cadena.contains(numeroserie)) {
                                return false;
                            }
                        }
                        if (kiosko != null && !kiosko.isEmpty()) {
                            cadena = (String) map.get("tipokiosko");
                            if (cadena == null || !cadena.contains(kiosko)) {
                                return false;
                            }
                        }
                        if (contracto != null && !contracto.isEmpty()) {
                            cadena = (String) map.get("tipocontrato");
                            if (cadena == null || !cadena.contains(contracto)) {
                                return false;
                            }
                        }
                        if (movilidad != null && !movilidad.isEmpty()) {
                            cadena = (String) map.get("tipoactividad");
                            if (cadena == null || !cadena.contains(movilidad)) {
                                return false;
                            }
                        }
                        if (perfil != null && !perfil.isEmpty()) {
                            cadena = (String) map.get("perfil");
                            if (cadena == null || !cadena.contains(perfil)) {
                                return false;
                            }
                        }
                        if (muni != null && !muni.isEmpty()) {
                            cadena = (String) map.get("municipio");
                            if (cadena == null || !cadena.contains(muni)) {
                                return false;
                            }
                        }
                        if (codpostal != null && !codpostal.isEmpty()) {
                            cadena = (String) map.get("codigoPostal");
                            if (cadena == null || !cadena.contains(codpostal)) {
                                return false;
                            }
                        }
                        if (fechaminima != null) {
                            fechas = (LocalDateTime) map.get("fecha");
                            if (fechas == null || fechas.isBefore(fechaminima)) {
                                return false;
                            }
                        }
                        if (fechamaxima != null) {
                            fechas = (LocalDateTime) map.get("fecha");
                            if (fechas == null || fechas.isAfter(fechamaxima)) {
                                return false;
                            }
                        }
                        if (cantidadminima != 0) {
                            cantidad = (int) map.get("pedido");
                            if (cantidad < cantidadminima) {
                                return false;
                            }
                        }
                        if (cantidadmaxima != 0) {
                            cantidad = (int) map.get("pedido");
                            if (cantidad > cantidadmaxima) {
                                return false;
                            }
                        }
                        if (preciominimo != 0) {
                            precio = (double) map.get("precio");
                            if (precio < preciominimo) {
                                return false;
                            }
                        }
                        if (preciomaximo != 0) {
                            precio = (double) map.get("precio");
                            if (precio > preciomaximo) {
                                return false;
                            }
                        }
                        return true;

                    }).collect(Collectors.toList());
            usuario.put("productocupon", true);
            usuario.put("tablacupon", listafiltrada);
            System.out.println(listafiltrada);
        } else if (tipoproducto != null && !tipoproducto.isEmpty() && tipoproducto.equals("rasca")) {
            ArrayList<Filtro> listarasca = new ArrayList<>();
            listarasca = filtroDao.listaFiltradoPedidoRascaEntidad();
            List<Filtro> listafiltrada = listarasca.stream()
                    .filter(map -> nombre != null && !nombre.isEmpty() && map.getNombre().contains(nombre))
                    .filter(map -> apellido != null && !apellido.isEmpty() && map.getApellidos().contains(apellido))
                    .filter(map -> nombreproducto != null && !nombreproducto.isEmpty() && map.getNombreProducto().contains(nombreproducto))
                    .filter(map -> numeroserie != null && !numeroserie.isEmpty() && map.getNumeroSerie().contains(numeroserie))
                    .filter(map -> kiosko != null && !kiosko.isEmpty() && map.getTipoKiosko().equals(kiosko))
                    .filter(map -> contracto != null && !contracto.isEmpty() && map.getTipoContrato().equals(contracto))
                    .filter(map -> movilidad != null && !movilidad.isEmpty() && map.getTipoActividad().equals(movilidad))
                    .filter(map -> perfil != null && !perfil.isEmpty() && map.getPerfil().equals(perfil))
                    .filter(map -> muni != null && !muni.isEmpty() && map.getMunicipio().contains(muni))
                    .filter(map -> codpostal != null && !codpostal.isEmpty() && map.getCodigoPostal() == Integer.parseInt(codpostal))
                    .filter(map -> fechaminima != null && fechaminima.isBefore(map.getFechaPedido()))
                    .filter(map -> fechamaxima != null && fechamaxima.isAfter(map.getFechaPedido()))
                    .filter(map -> cantidadminima != 0 && cantidadminima <= map.getCantidad())
                    .filter(map -> cantidadmaxima != 0 && cantidadmaxima >= map.getCantidad())
                    .filter(map -> preciominimo != 0 && preciominimo <= map.getPrecio())
                    .filter(map -> preciomaximo != 0 && preciomaximo >= map.getPrecio())
                    .collect(Collectors.toList());
            usuario.put("productorasca", true);
            if (tipoproducto != null && !tipoproducto.isEmpty() && tipoproducto.equals("rasca")) {
                usuario.put("tablafiltro", listafiltrada);
            } else {
                usuario.put("tablarasca", listafiltrada);
            }
            System.out.println(listafiltrada);
        }
        if (muni != null && !muni.isEmpty()) {
            usuario.put("lugar", true);
            usuario.put("municipio", muni);
        }
        usuario.put("pagina", 1);
        RenderVista.renderizarVista(response, getServletContext().getRealPath("administrador/listapedidos.html"), usuario);

//        String filtro = request.getParameter("filtro");
//        // ESTO ES EL MAP
//
//        ArrayList<Map<String, Object>> listaMap = new ArrayList<>();
//        listaMap = usuarioDao.listaUsuarioYTrabajador();
//        List<Map<String, Object>> listanueva = listaMap.stream().filter(map -> map.get("trabajador").equals(filtro)).collect(Collectors.toList());
//        usuario.put("tabla", listanueva);
//        System.out.println(listanueva);
//        RenderVista.renderizarVista(response, getServletContext().getRealPath("administrador/listausuarios.html"), usuario); 
//        /*
//        *
//        *
//        *ESTO ES PARA LISTA TRABAJADOR
//        *
//        *
//        *
//        *
//         */
//        ArrayList<Trabajador> lista = new ArrayList<>();
//        lista = trabajadorDao.seleccionListaTrabajadores();
//        List<Trabajador> nuevalista = lista.stream().filter(t -> t.getNombreTrab().equalsIgnoreCase("Juan")).collect(Collectors.toList());
//        System.out.println(nuevalista);
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
