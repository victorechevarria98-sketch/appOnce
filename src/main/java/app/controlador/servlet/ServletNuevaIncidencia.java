/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package app.controlador.servlet;

import app.modelo.dao.IncidenciasDao;
import app.modelo.dao.TrabajadorDao;
import app.modelo.entidad.Aviso;
import app.modelo.entidad.Incidencias;
import app.modelo.entidad.Lugar;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Alumnos
 */
@WebServlet(name = "ServletNuevaIncidencia", urlPatterns = {"/ServletNuevaIncidencia"})
public class ServletNuevaIncidencia extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    private TrabajadorDao trabajadorDao;
    private IncidenciasDao incidenciasDao;
    
    @Override
    public void init() {
     // creo instancia para do post cuando se crea un nuevo servlet y no tienes que repetir cada vez el new
        trabajadorDao = new TrabajadorDao();
        incidenciasDao = new IncidenciasDao();
    }
    
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        response.setContentType("text/html;charset=UTF-8");
//        try (PrintWriter out = response.getWriter()) {
//            /* TODO output your page here. You may use following sample code. */
//            out.println("<!DOCTYPE html>");
//            out.println("<html>");
//            out.println("<head>");
//            out.println("<title>Servlet ServletNuevaIncidencia</title>");
//            out.println("</head>");
//            out.println("<body>");
//            out.println("<h1>Servlet ServletNuevaIncidencia at " + request.getContextPath() + "</h1>");
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
        //processRequest(request, response);
        HttpSession sesion = request.getSession(false);
        Usuario usu = (Usuario) sesion.getAttribute("usuario");
        
        Trabajador tb = trabajadorDao.obtenerIdTrabajadorPorEmail(usu.getEmailUsu());
        
        Incidencias id = new Incidencias();
        Lugar lg = new Lugar();
        
        
        String tipo = request.getParameter("incident").trim();
        id.setTipoIncident(tipo);
        String comentario = request.getParameter("comentario").trim();
        id.setComentario(comentario);       
        String fechaincidente = request.getParameter("fecha");    
        // aqui guardamos si es rasca o cupon
        String producto = request.getParameter("producto").trim();
        //aqui compruebas como si fuera un substring cual de los dos casos cumple
        if(producto.startsWith("Cupones_", 0)){
            int idproducto = Integer.parseInt(producto.replace("Cupones_", ""));// con esto eliminas el texto mientras lo transformas a un int
            id.setIdcupon(idproducto);
        } else {
            int idproducto = Integer.parseInt(producto.replace("Rascas_", ""));
            id.setIdrasca(idproducto);
        }
        id.setIdtrab(tb.getIdtrab());
             
        LocalDateTime fecha = LocalDateTime.parse(fechaincidente);// cambias la fecha pero aqui no necesitas tu poner el format ya que te lo envian de la forma correcta solo cambias de strinf a lovaltimedate
        id.setFechaIncident(fecha);
      
        String codigopostal = request.getParameter("codigo postal");
        lg.setCodPostal(Integer.parseInt(codigopostal));
        String municipio = request.getParameter("municipio").trim();
        lg.setMunicipio(municipio);
        String lugar = request.getParameter("lugar").trim();
        lg.setCalle(lugar);
        lg.setIdtrab(tb.getIdtrab());   
        // envias los datos en 2 instancias
           
        boolean insert = incidenciasDao.nuevaIncidencia(id, lg); 
// esta funcion es anterior a que pusiese incidencias con todas las variable por lo tanto ante incidencias no tenia el id lugar asi que lo envio por separado
       
        if(insert){
            RenderVista.renderizarVista(response, getServletContext().getRealPath("avisos.html"), new Aviso("Su incidencia ha sido registrada", "Intentaremos ofrecer una solucion lo antes posible, muchas gracias", "ServletMenuPrincipal"));
        } else {
             RenderVista.renderizarVista(response,getServletContext().getRealPath("avisos.html"),new Aviso("Vaya algo que no se ha fallado","Buena suerte investigando😣", "nuevaincidencia.html"));
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
