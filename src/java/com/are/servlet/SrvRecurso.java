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
import com.are.entidades.Recurso;
import com.are.manejadores.ManejadorRecurso;
import com.are.sofatec.db;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class SrvRecurso
 */
@WebServlet("/SrvRecurso")
public class SrvRecurso extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public SrvRecurso() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void ProcesarPeticion(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=ISO-8859-1");
        PrintWriter out = response.getWriter();

        HttpSession sesion = request.getSession();
        if (sesion.getAttribute("usuario") == null) {
            out.print("La sesion ha expirado, debe iniciar sesión nuevamente");
            return;
        }

        String operacion = (String) request.getParameter("operacion");
        db conexion = null;

        /* Inicio operacion Agregar departamento */
        try {

            if (operacion.equals("add")) { // Agregar camp

                String codigo = (String) request.getParameter("codigo");
                String nombre = (String) request.getParameter("nombre");
                String direccion = (String) request.getParameter("direccion");
                String telefono = (String) request.getParameter("telefono");
                String cargo = (String) request.getParameter("cargo");
                String correo = (String) request.getParameter("correo");
                String estado = (String) request.getParameter("estado");
                String rol = (String) request.getParameter("rol");
                String contratista = (String) request.getParameter("contratista");
                String zona = (String) request.getParameter("zona");
                String auth = (String) request.getParameter("auth");

                conexion = new db();
                Recurso recurso = new Recurso();
                recurso.setCodigo(codigo);
                recurso.setNombre(nombre);
                recurso.setDireccion(direccion);
                recurso.setTelefono(telefono);
                recurso.setCorreo(correo);
                recurso.setCargo(cargo);
                recurso.setEstado(Integer.parseInt(estado));
                recurso.setRol(Integer.parseInt(rol));
                recurso.setContratista(Integer.parseInt(contratista));
                recurso.setZona(Integer.parseInt(zona));
                recurso.setAuthAPP(Integer.parseInt(auth));

                ManejadorRecurso manejador = new ManejadorRecurso(conexion);

                if (manejador.Add(recurso)) {
                    out.print("OK");
                } else {
                    throw new Exception("Error al procesar la solicitud");
                }

            }

            if (operacion.equals("update")) { // Modificar Tipo de Orden

                String codigo = (String) request.getParameter("codigo");
                String nombre = (String) request.getParameter("nombre");
                String direccion = (String) request.getParameter("direccion");
                String telefono = (String) request.getParameter("telefono");
                String cargo = (String) request.getParameter("cargo");
                String correo = (String) request.getParameter("correo");
                String estado = (String) request.getParameter("estado");
                String rol = (String) request.getParameter("rol");
                String contratista = (String) request.getParameter("contratista");
                String zona = (String) request.getParameter("zona");
                String auth = (String) request.getParameter("auth");
                String key = (String) request.getParameter("key");

                conexion = new db();
                Recurso recurso = new Recurso();
                recurso.setCodigo(codigo);
                recurso.setNombre(nombre);
                recurso.setDireccion(direccion);
                recurso.setTelefono(telefono);
                recurso.setCorreo(correo);
                recurso.setCargo(cargo);
                recurso.setEstado(Integer.parseInt(estado));
                recurso.setRol(Integer.parseInt(rol));
                recurso.setContratista(Integer.parseInt(contratista));
                recurso.setZona(Integer.parseInt(zona));
                recurso.setAuthAPP(Integer.parseInt(auth));

                ManejadorRecurso manejador = new ManejadorRecurso(conexion);

                if (manejador.Find(key)) {
                    if (manejador.Update(recurso, key)) {
                        out.print("OK");
                    } else {
                        throw new Exception("Error al procesar la solicitud");
                    }
                } else {
                    throw new Exception("Recurso no se encuentra registrado. " + key);
                }
            }

            if (operacion.equals("remove")) { // Eliminar Tipo de Orden
                String key = (String) request.getParameter("key");
                conexion = new db();

                ManejadorRecurso manejador = new ManejadorRecurso(conexion);

                if (manejador.Find(key)) {
                    if (manejador.Remove(key)) {
                        out.print("OK");
                    } else {
                        throw new Exception("Error al procesar la solicitud");
                    }
                } else {
                    throw new Exception("Recurso no se encuentra registrado. " + key);
                }
            }

            if (operacion.equals("password")) { // Eliminar Tipo de Orden
                String key = (String) request.getParameter("key");
                String clave = (String) request.getParameter("clave");
                conexion = new db();

                ManejadorRecurso manejador = new ManejadorRecurso(conexion);

                if (manejador.Find(key)) {
                    if (manejador.AsignarPassword(key, clave)) {
                        out.print("OK");
                    } else {
                        throw new Exception("Error al procesar la solicitud");
                    }
                } else {
                    throw new Exception("Recurso no se encuentra registrado. " + key);
                }
            }

            if (operacion.equals("list")) { // Listado de Tipo de ordenes
                conexion = new db();
                ManejadorRecurso manejador = new ManejadorRecurso(conexion);
                ArrayList<Recurso> lista = manejador.List();

                out.println("<h2>Listado Recurso</h2>");
                if (lista.size() > 0) {
                    out.println("<table>");
                    out.println("<tr>");
                    out.println("<th>CODIGO</th>");
                    out.println("<th>NOMBRE</th>");
                    out.println("<th>ESTADO</th>");
                    out.println("<th></th>");
                    out.println("</tr>");
                    int fila = 0;
                    for (Recurso recurso : lista) { // existen registros
                        out.println("<tr " + (fila % 2 == 0 ? "class='odd'" : "") + ">");
                        out.println("<td>" + recurso.getCodigo() + "</td>");
                        out.println("<td>" + recurso.getNombre() + "</td>");
                        out.println("<td>"
                                + (recurso.getEstado() == 1 ? "<img src=\"images/online.png\">"
                                : "<img src=\"images/offline.png\">")
                                + "</td>");
                        out.println("<td><a href='mod_recurso.jsp?codigo="
                                + recurso.getCodigo() + "'>Modificar</a>");
                        out.println("<a href=\"cambiarclaverecurso.jsp?id="
                                + recurso.getCodigo() + "\">Contraseña</a></td>");
                        out.println("</tr>");
                        fila++;
                    }

                    out.println("</table>");
                } else {
                    out.println("<strong>No hay registros</strong>");

                }

            }

            if (operacion.equals("consulta")) { // Listado de Tipo de ordenes
                String criterio = (String) request.getParameter("criterio");
                conexion = new db();
                ManejadorRecurso manejador = new ManejadorRecurso(conexion);
                ArrayList<Recurso> lista = manejador.List(criterio);

                out.println("<h2>Resultado busqueda</h2>");
                if (lista.size() > 0) {
                    out.println("<table>");
                    out.println("<tr>");
                    out.println("<th>CODIGO</th>");
                    out.println("<th>NOMBRE</th>");
                    out.println("<th>ESTADO</th>");
                    out.println("<th></th>");
                    out.println("</tr>");
                    int fila = 0;
                    for (Recurso recurso : lista) { // existen registros
                        out.println("<tr " + (fila % 2 == 0 ? "class='odd'" : "") + ">");
                        out.println("<td>" + recurso.getCodigo() + "</td>");
                        out.println("<td>" + recurso.getNombre() + "</td>");
                        out.println("<td>"
                                + (recurso.getEstado() == 1 ? "<img src=\"images/online.png\">"
                                : "<img src=\"images/offline.png\">")
                                + "</td>");
                        out.println("<td><a href='mod_recurso.jsp?codigo="
                                + recurso.getCodigo() + "'>Modificar</a>");
                        out.println("<a href=\"cambiarclaverecurso.jsp?id="
                                + recurso.getCodigo() + "\">Contraseña</a></td>");
                        out.println("</tr>");
                        fila++;
                    }

                    out.println("</table>");
                } else {
                    out.println("<strong>No hay registros</strong>");

                }
            }

            if (operacion.equals("filtro1")) { // Listado de Tipo de ordenes
                String contrata = (String) request.getParameter("contrata");
                conexion = new db();
                ManejadorRecurso manejador = new ManejadorRecurso(conexion);
                ArrayList<Recurso> lista = manejador.FilterByContratista(Integer.parseInt(contrata));

                out.println("<h2>Resultado busqueda</h2>");
                if (lista.size() > 0) {
                    out.println("<table>");
                    out.println("<tr>");
                    out.println("<th>CODIGO</th>");
                    out.println("<th>NOMBRE</th>");
                    out.println("<th>ESTADO</th>");
                    out.println("<th></th>");
                    out.println("</tr>");
                    int fila = 0;
                    for (Recurso recurso : lista) { // existen registros
                        out.println("<tr " + (fila % 2 == 0 ? "class='odd'" : "") + ">");
                        out.println("<td>" + recurso.getCodigo() + "</td>");
                        out.println("<td>" + recurso.getNombre() + "</td>");
                        out.println("<td>"
                                + (recurso.getEstado() == 1 ? "<img src=\"images/online.png\">"
                                : "<img src=\"images/offline.png\">")
                                + "</td>");
                        out.println("<td><a href='mod_recurso.jsp?codigo="
                                + recurso.getCodigo() + "'>Modificar</a>");
                        out.println("<a href=\"cambiarclaverecurso.jsp?id="
                                + recurso.getCodigo() + "\">Contraseña</a></td>");
                        out.println("</tr>");
                        fila++;
                    }

                    out.println("</table>");
                } else {
                    out.println("<strong>No hay registros</strong>");

                }
            }

            if (operacion.equals("filtro2")) { // Listado de Tipo de ordenes
                String zona = (String) request.getParameter("zona");
                conexion = new db();
                ManejadorRecurso manejador = new ManejadorRecurso(conexion);
                ArrayList<Recurso> lista = manejador.FilterByZona(Integer.parseInt(zona));

                out.println("<h2>Resultado busqueda</h2>");
                if (lista.size() > 0) {
                    out.println("<table>");
                    out.println("<tr>");
                    out.println("<th>CODIGO</th>");
                    out.println("<th>NOMBRE</th>");
                    out.println("<th>ESTADO</th>");
                    out.println("<th></th>");
                    out.println("</tr>");
                    int fila = 0;
                    for (Recurso recurso : lista) { // existen registros
                        out.println("<tr " + (fila % 2 == 0 ? "class='odd'" : "") + ">");
                        out.println("<td>" + recurso.getCodigo() + "</td>");
                        out.println("<td>" + recurso.getNombre() + "</td>");
                        out.println("<td>"
                                + (recurso.getEstado() == 1 ? "<img src=\"images/online.png\">"
                                : "<img src=\"images/offline.png\">")
                                + "</td>");
                        out.println("<td><a href='mod_recurso.jsp?codigo="
                                + recurso.getCodigo() + "'>Modificar</a>");
                        out.println("<a href=\"cambiarclaverecurso.jsp?id="
                                + recurso.getCodigo() + "\">Contraseña</a></td>");
                        out.println("</tr>");
                        fila++;
                    }

                    out.println("</table>");
                } else {
                    out.println("<strong>No hay registros</strong>");

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
