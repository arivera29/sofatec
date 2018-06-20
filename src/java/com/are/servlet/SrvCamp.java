package com.are.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.are.entidades.Camp;
import com.are.manejadores.ManejadorCamp;
import com.are.sofatec.db;

/**
 * Servlet implementation class SrvGrupos
 */
@WebServlet("/SrvCamp")
public class SrvCamp extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public SrvCamp() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void ProcesarPeticion(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=ISO-8859-1");
        PrintWriter out = response.getWriter();

        String operacion = (String) request.getParameter("operacion");
        db conexion = null;

        /* Inicio operacion Agregar departamento */
        try {

            if (operacion.equals("add")) { // Agregar camp

                String codigo = (String) request.getParameter("codigo");
                String descripcion = (String) request.getParameter("descripcion");
                String plan = (String) request.getParameter("plan");
                String estado = (String) request.getParameter("estado");

                conexion = new db();
                Camp camp = new Camp();
                camp.setCodigo(codigo);
                camp.setDescripcion(descripcion);
                camp.setPlan(plan);
                camp.setEstado(Integer.parseInt(estado));

                ManejadorCamp manejador = new ManejadorCamp(conexion);

                if (manejador.Add(camp)) {
                    out.print("OK");
                } else {
                    throw new Exception("Error al procesar la solicitud");
                }

            }

            if (operacion.equals("update")) { // Modificar Tipo de Orden

                String codigo = (String) request.getParameter("codigo");
                String descripcion = (String) request.getParameter("descripcion");
                String plan = (String) request.getParameter("plan");
                String estado = (String) request.getParameter("estado");
                String key = (String) request.getParameter("key");

                conexion = new db();
                Camp camp = new Camp();
                camp.setCodigo(codigo);
                camp.setDescripcion(descripcion);
                camp.setPlan(plan);
                camp.setEstado(Integer.parseInt(estado));

                ManejadorCamp manejador = new ManejadorCamp(conexion);

                if (manejador.Find(key)) {
                    if (manejador.Update(camp, key)) {
                        out.print("OK");
                    } else {
                        throw new Exception("Error al procesar la solicitud");
                    }
                } else {
                    throw new Exception("Campaña no se encuentra registrada. "
                            + key);
                }
            }

            if (operacion.equals("remove")) { // Eliminar Tipo de Orden
                String key = (String) request.getParameter("key");
                conexion = new db();

                ManejadorCamp manejador = new ManejadorCamp(conexion);

                if (manejador.Find(key)) {
                    if (manejador.Remove(Integer.parseInt(key))) {
                        out.print("OK");
                    } else {
                        throw new Exception("Error al procesar la solicitud");
                    }
                } else {
                    throw new Exception("Campaña no se encuentra registrado. "
                            + key);
                }
            }
            
            if (operacion.equals("estado")) { // Eliminar Tipo de Orden
                String estado = (String) request.getParameter("estado");
                conexion = new db();

                ManejadorCamp manejador = new ManejadorCamp(conexion);

                    if (manejador.cambiarEstadoTodas(Integer.parseInt(estado))) {
                        out.print("OK");
                    } else {
                        throw new Exception("Error al procesar la solicitud");
                    }
            }
            
            if (operacion.equals("estado_criterio")) { // Eliminar Tipo de Orden
                String estado = (String) request.getParameter("estado");
                String criterio = (String)request.getParameter("criterio");
                conexion = new db();

                ManejadorCamp manejador = new ManejadorCamp(conexion);

                    if (manejador.cambiarEstadoTodasCriterio(Integer.parseInt(estado),criterio)) {
                        out.print("OK");
                    } else {
                        throw new Exception("Error al procesar la solicitud");
                    }
            }
            

            if (operacion.equals("list")) { // Listado de Tipo de ordenes
                conexion = new db();
                ManejadorCamp manejador = new ManejadorCamp(conexion);
                ArrayList<Camp> lista = manejador.List();

                out.println("<h2>Listado Campañas</h2>");
                if (lista.size() > 0) {
                    out.println("<table>");
                    out.println("<tr>");
                    out.println("<th>CODIGO</th>");
                    out.println("<th>NOMBRE</th>");
                    out.println("<th>PLAN</th>");
                    out.println("<th>ESTADO</th>");
                    out.println("<th></th>");
                    out.println("</tr>");

                    for (Camp camp : lista) { // existen registros
                        out.println("<tr>");
                        out.println("<td>" + camp.getCodigo() + "</td>");
                        out.println("<td>" + camp.getDescripcion() + "</td>");
                        out.println("<td>" + camp.getPlan() + "</td>");
                        out.println("<td>"
                                + (camp.getEstado() == 1 ? "<img src=\"images/online.png\">"
                                : "<img src=\"images/offline.png\">")
                                + "</td>");
                        out.println("<td><a href='mod_camp.jsp?codigo="
                                + camp.getCodigo() + "'>Modificar</a></td>");
                        out.println("</tr>");
                    }

                    out.println("</table>");
                } else {
                    out.println("<strong>No hay registros</strong>");

                }

            }
            
            if (operacion.equals("busqueda")) { // Listado de Tipo de ordenes
                String criterio = (String)request.getParameter("criterio");
                conexion = new db();
                ManejadorCamp manejador = new ManejadorCamp(conexion);
                ArrayList<Camp> lista = manejador.ListByCriterio(criterio);

                out.println("<h2>Resulado busqueda</h2>");
                out.println("<p>Registros encontrados: " + lista.size() + "</p>");
                if (lista.size() > 0) {
                    out.println("<table>");
                    out.println("<tr>");
                    out.println("<th>CODIGO</th>");
                    out.println("<th>NOMBRE</th>");
                    out.println("<th>PLAN</th>");
                    out.println("<th>ESTADO</th>");
                    out.println("<th></th>");
                    out.println("</tr>");
                    int cont =0;
                    for (Camp camp : lista) { // existen registros
                        if (cont %2 == 1) {
                            out.println("<tr class=\"odd\">");
                        }else {
                            out.println("<tr>");
                        }
                        out.println("<td>" + camp.getCodigo() + "</td>");
                        out.println("<td>" + camp.getDescripcion() + "</td>");
                        out.println("<td>" + camp.getPlan() + "</td>");
                        out.println("<td>"
                                + (camp.getEstado() == 1 ? "<img src=\"images/online.png\">"
                                : "<img src=\"images/offline.png\">")
                                + "</td>");
                        out.println("<td><a href='mod_camp.jsp?codigo="
                                + camp.getCodigo() + "'>Modificar</a></td>");
                        out.println("</tr>");
                        cont++;
                    }

                    out.println("</table>");
                } else {
                    out.println("<strong>No hay registros</strong>");

                }

            }
            
            if (operacion.equals("download")) {
                response.setContentType("application/vnd.ms-excel");
                response.setHeader("Content-Disposition", "attachment; filename=\"camp.csv\""); 
                conexion = new db();
                ManejadorCamp manejador = new ManejadorCamp(conexion);
                ArrayList<Camp> lista = manejador.List();
                
                out.println("CODIGO;NOMBRE;PLAN;ACTIVA");
                if (lista.size() > 0) {
                    

                    for (Camp camp : lista) { 
                        String linea = camp.getCodigo() + ";";
                        linea += camp.getDescripcion() + ";";
                        linea += camp.getPlan() + ";";
                        linea += (camp.getEstado()==1)?"SI":"NO" ;
                        out.println(linea);

                    }

                }
                
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            out.print("Error de conexion con el servidor: " + e.getMessage());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            out.print("Error:  " + e.getMessage());
        } finally {
            if (conexion != null) {
                try {
                    conexion.Close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    out.print(e.getMessage());
                }
            }
        }

        out.close();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        this.ProcesarPeticion(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        this.ProcesarPeticion(request, response);
    }

}
