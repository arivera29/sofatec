/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.servlet;

import com.are.sofatec.UtilDate;
import com.are.sofatec.db;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author aimerrivera
 */

// http://sofatec.cloudsoluciones.co:8080/sofatec/SrvGenerateActaCenso?orden=25433145

@WebServlet(name = "SrvGenerateActaCenso", urlPatterns = {"/SrvGenerateActaCenso"})
public class SrvGenerateActaCenso extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String orden = (String) request.getParameter("orden");
        db conexion = null;
        Document document = null;
        ServletContext servletContext = getServletContext();
        String path_image = servletContext.getRealPath("/images") + File.separator + "logo_eca.png";
        try {

            String mimeType = "application/octet-stream";
            response.setContentType(mimeType);
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", "censo_" + orden + ".pdf");
            response.setHeader(headerKey, headerValue);
            OutputStream outStream = response.getOutputStream();

            document = new Document(); // Instancia de la clase itextpdf
            PdfWriter.getInstance(document, outStream);  // Se crea el domento en memoria
            document.open(); // Abrimos el documento

            // Creamos titulo del informe
            document.add(createTitulo("ACTA DE CENSO DE CARGA", 14));

            
            

            // Creamos una tabla de 4 columnas
            PdfPTable table = new PdfPTable(4); // 4 columns.
            table.setWidthPercentage(100);
            table.setSpacingBefore(30f);
            table.setSpacingAfter(10f);

            // Agregamos el logo de ELECTRICARIBE
            Image imagen = Image.getInstance(path_image);
            imagen.setAbsolutePosition(430f, 770f);
            imagen.scaleAbsolute(150f, 60f);
            //imagen.setAlignment(Element.ALIGN_RIGHT);
            document.add(imagen);

            conexion = new db();
            String sql = "SELECT A.NIC, A.NUM_OS, A.DIRECCION, A.FECHA_CENSO, A.FECHA_CIERRE, "
                    + " A.NOMBRE_OPERARIO_HDA,A.COMENT_OS, "
                    + " B.MUNICIPIO, B.LOCALIDAD, B.DEPARTAMENTO,"
                    + " B.APE1_CLI, B.APE2_CLI, B.NOM_CLI, B.REF_DIR, SYSDATE() AS HOY "
                    + " FROM QO_ORDENES A "
                    + " INNER JOIN QO_DATOSUM B ON A.NIC = B.NIC AND A.NIS_RAD = B.NIS_RAD "
                    + " WHERE A.NUM_OS = ?";
            java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
            pst.setString(1, orden);
            java.sql.ResultSet rs = conexion.Query(pst);
            if (rs.next()) {
                
                Calendar fecha = Calendar.getInstance();
                try {
                    fecha.setTime(new java.util.Date(rs.getDate("FECHA_CENSO").getTime()));
                }catch (NullPointerException ex) {
                    fecha.setTime(new java.util.Date(rs.getDate("FECHA_CIERRE").getTime()));
                }
                String nombreCliente = rs.getString("APE1_CLI") + " " + rs.getString("APE2_CLI") + " " + rs.getString("NOM_CLI");
                
                String parrafo = String.format("A los %s del mes de %s del %s, siendo las %s:%s se hace presente en el inmueble identificado "
                    + "comercialmente con el NIC %s la siguiente persona %s en representación de Electricaribe "
                    + " y en presencia del señor %s, en calidad de cliente/usuario, con el fin de efectuar una "
                    + " una revisión de los equipos de medida e instalaciones eléctricas del inmueble con el NIC "
                    + " indicado, cuyo resultado ha sido el siguiente.", 
                        fecha.get(Calendar.DAY_OF_MONTH), 
                        UtilDate.mesToText(fecha.get(Calendar.MONTH)),
                        fecha.get(Calendar.YEAR),
                        fecha.get(Calendar.HOUR_OF_DAY),
                        fecha.get(Calendar.MINUTE),
                        rs.getString("NIC"),
                        rs.getString("NOMBRE_OPERARIO_HDA"),
                        nombreCliente);
                
                document.add(createLabel("",10));
                document.add(createLabel(parrafo,10));

                addTituloFila("INFORMACION ORDEN DE SERVICIO", 4, table);
                table.addCell(addCeldaTitulo("ORDEN DE SERVICIO"));
                table.addCell(addCelda(rs.getString("NUM_OS")));
                table.addCell(addCeldaTitulo("NIC"));
                table.addCell(addCelda(rs.getString("NIC")));

                table.addCell(addCeldaTitulo("DIRECCION"));
                table.addCell(addCelda(rs.getString("DIRECCION")));
                table.addCell(addCeldaTitulo("MUNICIPIO"));
                table.addCell(addCelda(rs.getString("MUNICIPIO")));

                table.addCell(addCeldaTitulo("LOCALIDAD"));
                table.addCell(addCelda(rs.getString("LOCALIDAD")));
                table.addCell(addCeldaTitulo("DEPARTAMENTO"));
                table.addCell(addCelda(rs.getString("DEPARTAMENTO")));

                table.addCell(addCeldaTitulo("CLIENTE"));
                table.addCell(addCelda(nombreCliente));
                table.addCell(addCeldaTitulo("DIRECCION REFERENCIA"));
                table.addCell(addCelda(rs.getString("REF_DIR")));
                
                document.add(table);
                
                
                sql = "SELECT path "
                        + " FROM camp_orden_fotos "
                        + "WHERE id_orden = ("
                        + "     SELECT DISTINCT visita "
                        + "     FROM qo_censo "
                        + "     WHERE NUM_OS = ? "
                        + ")";
                
                java.sql.PreparedStatement pst2 = conexion.getConnection().prepareStatement(sql);
                pst2.setString(1, orden);
                java.sql.ResultSet rsFotos = conexion.Query(pst2);
                
                if (rsFotos.next()) {  // Censo tiene asociadas fotos
                    document.add(createTitulo("REGISTRO FOTOGRAFICO", 12));
                    do {
                        try {
                            imagen = Image.getInstance(rsFotos.getString("path"));
                            //imagen.setAbsolutePosition(430f, 770f);
                            //imagen.scaleAbsolute(150f, 60f);
                            imagen.setAlignment(Element.ALIGN_LEFT);
                            document.add(imagen);
                        }catch (Exception ex) {
                            Logger.getLogger(SrvGenerateActaCenso.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }while(rsFotos.next());
                    
                }
                
                
                table = new PdfPTable(4); // 4 columns.
                table.setWidthPercentage(100);
                table.setSpacingBefore(30f);
                table.setSpacingAfter(10f);
            

                sql = "SELECT QO_CENSO.ID_EQUIPO,QO_CENSO.CANTIDAD,QO_EQUIPOS.DESC_EQ, "
                        + " QO_EQUIPOS.CARGA, (QO_CENSO.CANTIDAD*QO_EQUIPOS.CARGA) AS TOTAL "
                        + " FROM QO_CENSO "
                        + " INNER JOIN QO_EQUIPOS ON QO_CENSO.ID_EQUIPO = QO_EQUIPOS.ID "
                        + " WHERE NUM_OS=?";
                java.sql.PreparedStatement pst0 = conexion.getConnection().prepareStatement(sql);
                pst0.setString(1, orden);
                java.sql.ResultSet rsCenso = conexion.Query(pst0);
                if (rsCenso.next()) {
                    addTituloFila("CENSO CARGA INSTALADA (APARATOS ELECTRICOS CONECTADOS EN LA RESIDENCIA O NEGOCIO)", 4, table);
                    table.addCell(addCeldaTitulo("EQUIPOS CONECTADOS"));
                    table.addCell(addCeldaTitulo("CANTIDAD"));
                    table.addCell(addCeldaTitulo("W/Un"));
                    table.addCell(addCeldaTitulo("TOTAL W"));

                    double total = 0;
                    do {
                        // Imprimir censo
                        table.addCell(addCelda(rsCenso.getString("DESC_EQ")));
                        table.addCell(addCeldaNumber(rsCenso.getString("CANTIDAD")));
                        table.addCell(addCeldaNumber(rsCenso.getString("CARGA")));
                        table.addCell(addCeldaNumber(rsCenso.getString("TOTAL")));
                        

                        total += rsCenso.getDouble("TOTAL");

                    } while (rsCenso.next());

                    addTituloFila("TOTAL CENSO", 3, table);
                    table.addCell(addCeldaNumber(Double.toString(total)));
                    document.add(table);
                }else {
                    document.add(createLabel("Orden de servicio no tiene censo asociado",12));
                }
                
                /*
                table = new PdfPTable(4); // 4 columns.
                table.setWidthPercentage(100);
                table.setSpacingBefore(30f);
                table.setSpacingAfter(10f);
                
                addTituloFila("INFORMACION ORDEN DE EJECUCION", 4, table);
                table.addCell(addCeldaTitulo("FECHA"));
                table.addCell(addCelda(rs.getString("FECHA_CENSO")));
                table.addCell(addCeldaTitulo("BRIGADA"));
                table.addCell(addCelda(rs.getString("NOMBRE_OPERARIO_HDA")));
                
                document.add(table);
                
                */
                
                table = new PdfPTable(1); // 4 columns.
                table.setWidthPercentage(100);
                table.setSpacingBefore(30f);
                table.setSpacingAfter(10f);
                addTituloFila("OBSERVACION", 1, table);
                table.addCell(addCelda(rs.getString("COMENT_OS")));
                document.add(table);
                
                
                document.add(createLabel("Fecha generación: " + rs.getDate("HOY").toString() ,10));
                

            } else {
                // No existe la orden de servicio
                document.add(createLabel("Orden de servicio no encontrada",12));
            }

        } catch (SQLException ex) {
            Logger.getLogger(SrvGenerateActaCenso.class.getName()).log(Level.SEVERE, null, ex);

        } catch (DocumentException ex) {
            Logger.getLogger(SrvGenerateActaCenso.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (Exception ex) {
            Logger.getLogger(SrvGenerateActaCenso.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            if (document != null) {

                document.close();
            }
            if (conexion != null) {
                try {
                    conexion.Close();
                } catch (SQLException ex) {
                    Logger.getLogger(SrvGenerateActaCenso.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
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

    // funciones soporte creación PDF
    private Paragraph createTitulo(String titulo, int size) {
        Paragraph p = new Paragraph(titulo,
                FontFactory.getFont("arial", // fuente
                        size, // tamaño
                        Font.BOLD, // estilo
                        new BaseColor(21, 65, 98)));
        p.setAlignment(Paragraph.ALIGN_CENTER);
        return p;
    }
    
    private Paragraph createLabel(String titulo, int size) {
        Paragraph p = new Paragraph(titulo,
                FontFactory.getFont("arial", // fuente
                        size, // tamaño
                        Font.NORMAL, // estilo
                        new BaseColor(21, 65, 98)));
        p.setAlignment(Paragraph.ALIGN_JUSTIFIED);
        return p;
    }

    private void addTituloFila(String c1, int span, PdfPTable table) {
        PdfPCell cell1 = new PdfPCell(new Paragraph(c1,
                FontFactory.getFont("arial", // fuente
                        12, // tamaño
                        Font.BOLD, // estilo
                        BaseColor.WHITE)));
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1.setBackgroundColor(new BaseColor(21, 65, 98));
        cell1.setColspan(span);
        table.addCell(cell1);
    }
    

    private PdfPCell addCeldaTitulo(String c) {
        PdfPCell cell1 = new PdfPCell(new Paragraph(c, FontFactory.getFont("arial", // fuente
                10, // tamaño
                Font.BOLD, // estilo
                BaseColor.WHITE)));
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1.setBackgroundColor(new BaseColor(21, 65, 98));
        cell1.setBorderColor(BaseColor.WHITE);
        return cell1;
    }

    private PdfPCell addCelda(String c) {
        PdfPCell cell1 = new PdfPCell(new Paragraph(c, FontFactory.getFont("arial", // fuente
                10, // tamaño
                Font.NORMAL, // estilo
                BaseColor.BLACK)));
        cell1.setBorderColor(new BaseColor(21, 65, 98));
        return cell1;
    }
    
    private PdfPCell addCeldaNumber(String c) {
        PdfPCell cell1 = new PdfPCell(new Paragraph(c, FontFactory.getFont("arial", // fuente
                10, // tamaño
                Font.NORMAL, // estilo
                BaseColor.BLACK)));
        cell1.setBorderColor(new BaseColor(21, 65, 98));
        cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        return cell1;
    }

}
