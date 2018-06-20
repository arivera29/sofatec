package com.are.servlet;

import com.are.sofatec.db;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.*;
import java.sql.SQLException;

import com.csvreader.CsvReader;
import java.util.ArrayList;

/**
 * Servlet implementation class ProcessOrders
 */
@WebServlet("/SrvProcesarPlantilla")
public class SrvProcesarPlantilla extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public SrvProcesarPlantilla() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void ProcesarPeticion(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");
        //response.setHeader("Content-Disposition", "attachment; filename=\"log_carga_asignacion_ordenes.txt\"");
        PrintWriter out = response.getWriter();
        ServletContext servletContext = getServletContext();
        HttpSession sesion = request.getSession();
        if (sesion.getAttribute("usuario") == null) {
            out.print("La sesion ha caducado... intente de nuevo por favor");
            return;
        }

        String filename = (String) request.getParameter("file");
        String separador = (String) request.getParameter("separador");
        String path = servletContext.getRealPath("/upload") + File.separator + filename;
        CsvReader reader = null;
        db conexion = null;
        try {

            reader = new CsvReader(path);

            if (separador.equals("1")) {
                reader.setDelimiter('	');  // tabulador
            } else if (separador.equals("2")) {
                reader.setDelimiter(','); // coma
            } else if (separador.equals("3")) {
                reader.setDelimiter(';');  // punto y coma
            } else {
                reader.setDelimiter(',');
            }

            reader.readHeaders();

            String[] headers = reader.getHeaders();
            if (headers.length != 3) { // estan las columnas OK.
                throw new IOException("Archivo no cargado.  Numero de columnas no coinciden con la estructura del archivo de Asignacion");
            }
            

            conexion = new db();

            ArrayList<Orden> lista = new ArrayList<Orden>();
            ArrayList<String> ordenes = new ArrayList<String>();

            while (reader.readRecord()) {

                

                Orden orden = new Orden();
                orden.setOrden(reader.get(0));
                orden.setAccion(reader.get(1));
                String cadena = reader.get(2);
                if (reader.get(2).length() > 200) {
                    cadena = reader.get(2).substring(0, 199);
                }
                orden.setObservacion(cadena);

                lista.add(orden);
                boolean encontrado = false;
                for (int x = 0; x < ordenes.size(); x++) {
                    if (ordenes.get(x).equals(orden.getOrden())) {
                        encontrado = true;
                        break;
                    }
                }

                if (!encontrado) {
                    ordenes.add(orden.getOrden());
                }
            }

            for (String orden : ordenes) {
                String sql = "SELECT TIP_OS, NUM_OS, ESTADO_OPER FROM QO_ORDENES WHERE NUM_OS=?";
                java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
                pst.setString(1, orden);
                java.sql.ResultSet rs = conexion.Query(pst);
                if (rs.next()) {
                    this.InicializarOrden(orden, conexion);
                    if (this.AplicarPlantilla(orden, rs.getString("TIP_OS"), conexion)) {
                        boolean pasa = false;
                        for (Orden obj : lista) {
                            if (obj.orden.equals(orden)) {
                                if (this.ActualizarPaso(obj.orden, obj.accion, obj.observacion, conexion)) {
                                    pasa = true;
                                } else if (this.ActualizarNuevosPaso(obj.orden, obj.accion, obj.observacion, conexion)) {
                                    pasa = true;
                                }

                            }
                        }

                        if (pasa) {
                            if (this.ResolverOrden(orden, conexion)) {
                                conexion.Commit();
                                out.print("<img src='images/ok.png'>" + orden + ". OK <br>");
                            } else {
                                conexion.Rollback();
                                out.print("<img src='images/alerta.gif'>" + orden + ". Error al resolver orden <br>");
                            }
                        } else {
                            conexion.Rollback();
                        }

                    } else {
                        out.print("<img src='images/alerta.gif'>" + orden + ". No existe plantilla para el tipo de orden <br>");
                    }
                } else {
                    out.print("<img src='images/alerta.gif'>" + orden + ". Orden de servicio no regsitrada<br>");
                }

            }

            //out.println("</table>");
        } catch (FileNotFoundException e) {
            out.println("<br>Error: " + e.getMessage());
        } catch (IOException e) {
            out.println("<br>Error: " + e.getMessage());

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            out.println("<br>Error: " +e.getMessage());
        } finally {
            if (conexion != null) {
                try {
                    conexion.Close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    out.println("<br>Error: " + e.getMessage());
                }
            }
        }

    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        this.ProcesarPeticion(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        this.ProcesarPeticion(request, response);
    }

    private class Orden {

        private String orden;
        private String accion;
        private String observacion;

        public String getOrden() {
            return orden;
        }

        public void setOrden(String orden) {
            this.orden = orden;
        }

        public String getAccion() {
            return accion;
        }

        public void setAccion(String accion) {
            this.accion = accion;
        }

        public String getObservacion() {
            return observacion;
        }

        public void setObservacion(String observacion) {
            this.observacion = observacion;
        }

    }

    private void InicializarOrden(String orden, db conexion) throws SQLException {
        String sql = "UPDATE QO_PASOS SET CUMPLIDO=0, CO_ACCEJE='', OBSERVACION='' WHERE NUM_OS=? ";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, orden);
        conexion.Update(pst);

        sql = "DELETE FROM QO_NUEVOS_PASOS WHERE NUM_OS=? ";
        java.sql.PreparedStatement pst2 = conexion.getConnection().prepareStatement(sql);
        pst2.setString(1, orden);
        conexion.Update(pst);

    }

    private boolean ActualizarPaso(String orden, String accion, String observacion, db conexion) throws SQLException {
        boolean result = false;
        String sql = "UPDATE QO_PASOS SET CUMPLIDO=1, OBSERVACION=? WHERE NUM_OS=? AND CO_ACCEJE=?";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, observacion);
        pst.setString(2, orden);
        pst.setString(3, accion);
        if (conexion.Update(pst) == 1) {
            result = true;
        }

        return result;
    }

    private boolean ActualizarNuevosPaso(String orden, String accion, String observacion, db conexion) throws SQLException {
        boolean result = false;
        String sql = "UPDATE QO_NUEVOS_PASOS SET CUMPLIDO=1, OBSERVACION=? WHERE NUM_OS=? AND CO_ACCEJE=?";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, observacion);
        pst.setString(2, orden);
        pst.setString(3, accion);
        if (conexion.Update(pst) == 1) {
            result = true;
        }

        return result;
    }
    
    private void AgregarLectura(String orden, db conexion) throws SQLException {
        conexion.Update("DELETE FROM QO_ORDEN_LECTURA WHERE NUM_OS='" + orden + "'");
        String sql = "SELECT NUM_APA FROM QO_APARATOS WHERE NUM_OS=?";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, orden);
        java.sql.ResultSet rs = conexion.Query(pst);
        if (rs.next()) {
            sql = "INSERT INTO QO_ORDEN_LECTURA (NUM_OS,NUM_APA,LECTURA) VALUES (?,?,1)";
            java.sql.PreparedStatement pst2 = conexion.getConnection().prepareStatement(sql);
            pst2.setString(1, orden);
            pst2.setString(2, rs.getString("NUM_APA"));
            conexion.Update(pst2);
        }
    }

    private boolean ResolverOrden(String orden, db conexion) throws SQLException {
        boolean result = false;
        String sql = "UPDATE QO_ORDENES SET ESTADO_OPER='99', FECHA_CIERRE=SYSDATE() WHERE NUM_OS=? ";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, orden);
        if (conexion.Update(pst) >= 0) {
            result = true;
        }
        return result;
    }

    private boolean AplicarPlantilla(String orden, String tipo, db conexion) throws SQLException {
        boolean result = false;
        String sql = "SELECT * FROM QO_PLANRESO WHERE TIP_OS=?";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, tipo);
        java.sql.ResultSet rs = conexion.Query(pst);

        if (rs.next()) {
            InicializarOrden(orden, conexion);
            this.AgregarLectura(orden, conexion);
            conexion.Update("DELETE FROM QO_NUEVOS_PASOS WHERE NUM_OS='" + orden + "'");
            boolean pasa = true;
            int num_paso = 0;
            do {

                if (rs.getString("TIP_PASO").equals("1")) {
                    sql = "UPDATE QO_PASOS SET CUMPLIDO=1, CO_ACCEJE=?, OBSERVACION=? WHERE NUM_OS=? AND DESCRIPCION=? ";
                    java.sql.PreparedStatement pst2 = conexion.getConnection().prepareStatement(sql);
                    pst2.setString(1, rs.getString("CO_ACCEJE"));
                    pst2.setString(2, rs.getString("OBSERVACION"));
                    pst2.setString(3, orden);
                    pst2.setString(4, rs.getString("DESC_PASO"));
                    if (conexion.Update(pst2) > 0) {

                    } else {
                        pasa = false;
                    }

                } else { // Nuevo paso
                    num_paso++;
                    sql = "INSERT INTO QO_NUEVOS_PASOS "
                            + "(NUM_OS,"
                            + "NUM_PASO,"
                            + "OPCOND,"
                            + "DESCRIPCION,"
                            + "CONDICION,"
                            + "ELSEACCION,"
                            + "CUMPLIDO,"
                            + "CO_ACCEJE,"
                            + "IND_DECISOR,"
                            + "OBSERVACION,"
                            + "COBRO,"
                            + "PARENT,"
                            + "FLUJO)"
                            + " VALUES "
                            + "(?,"
                            + "?,"
                            + "2,"
                            + "?,"
                            + "2,"
                            + "0,"
                            + "1,"
                            + "?,"
                            + "1,"
                            + "?,"
                            + "1,"
                            + "?,"
                            + "?)";
                    java.sql.PreparedStatement pst2 = conexion.getConnection().prepareStatement(sql);
                    pst2.setString(1, orden);
                    pst2.setInt(2, num_paso);
                    pst2.setString(3, rs.getString("DESC_PASO"));
                    pst2.setString(4, rs.getString("CO_ACCEJE"));
                    pst2.setString(5, rs.getString("OBSERVACION"));
                    pst2.setString(6, rs.getString("PARENT"));
                    pst2.setString(7, rs.getString("FLUJO"));
                    if (conexion.Update(pst2) > 0) {

                    } else {
                        pasa = false;
                    }

                }

            } while (rs.next() && pasa == true);

            if (pasa == true) {

                result = true;
            }

        }
        return result;
    }

}
