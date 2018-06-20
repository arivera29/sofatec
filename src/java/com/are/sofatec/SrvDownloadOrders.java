package com.are.sofatec;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;*/
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Servlet implementation class SrvDownloadOrders
 */
@WebServlet("/SrvDownloadOrders")
public class SrvDownloadOrders extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SrvDownloadOrders() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    @SuppressWarnings("deprecation")
	protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=\"listado.xls\""); 
    	
    	String operacion = (String)request.getParameter("operacion");
    	db conexion = null;
    	if (operacion.equals("download")) {
			
			
			String ciudad = (String)request.getParameter("localidad");
			
			String sql ="select depadesc,locadesc,orden,orders.nic,orders.nom_cli," +
					"orders.ape1_cli,orders.ape2_cli,orders.num_apa,orders.nom_calle," +
					"orders.nom_via,orders.placa,orders.crucero,orders.interior," +
					"tiordesc,orders.recurso,orders.fecha_asignacion," +
					"recurso.recunomb,orders.num_show " +
					"from orders,departamentos,localidad,tipo_orden,recurso " +
					"where localidad.locadepa=depacodi " +
					"and orders.localidad=locacodi " +
					"and orders.tipo=tiorcodi " +
					"and recucodi = orders.recurso " +
					"and orders.localidad = ? " +
					"and estado='2' " +
					"order by depadesc,locadesc,recurso,num_show";
			
			try {
				
				conexion = new db();
				java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
				pst.setString(1, ciudad);
				ResultSet rs = conexion.Query(pst);
				HSSFWorkbook wb = new HSSFWorkbook();
				 
	            //Se crea una nueva hoja dentro del libro
	            HSSFSheet sheet = wb.createSheet("Hoja 1");
	            HSSFRow rowTitle = sheet.createRow((short)0);
	            
	            rowTitle.createCell((short)0).setCellValue("Departamento");
	            rowTitle.createCell((short)1).setCellValue("Municipio");
	            rowTitle.createCell((short)2).setCellValue("Orden");
	            rowTitle.createCell((short)3).setCellValue("NIC");
	            rowTitle.createCell((short)4).setCellValue("Cliente");
	            rowTitle.createCell((short)5).setCellValue("Direccion");
	            rowTitle.createCell((short)6).setCellValue("Tipo");
	            rowTitle.createCell((short)7).setCellValue("Medidor");
	            rowTitle.createCell((short)8).setCellValue("Asignacion");
	            rowTitle.createCell((short)9).setCellValue("Nombre");
	            rowTitle.createCell((short)10).setCellValue("Fecha Asignacion");
	            rowTitle.createCell((short)11).setCellValue("Orden");
	            int fila = 1;
	            while(rs.next()) {
	            	HSSFRow row = sheet.createRow((short)fila);
	                row.createCell((short)0).setCellValue(rs.getString("depadesc"));
	                row.createCell((short)1).setCellValue(rs.getString("locadesc"));
	                row.createCell((short)2).setCellValue(rs.getString("orden"));
	                row.createCell((short)3).setCellValue(rs.getString("nic"));
	                row.createCell((short)4).setCellValue(rs.getString("nom_cli") + " " +  rs.getString("ape1_cli") + " " + rs.getString("ape2_cli") );
	                row.createCell((short)5).setCellValue(rs.getString("nom_calle") + " " + rs.getString("crucero") + " " + rs.getString("placa") + " " + rs.getString("interior"));
	                row.createCell((short)6).setCellValue(rs.getString("tiordesc"));
	                row.createCell((short)7).setCellValue(rs.getString("num_apa"));
	                row.createCell((short)8).setCellValue(rs.getString("recurso"));
	                row.createCell((short)9).setCellValue(rs.getString("recunomb"));
	                
	                HSSFCellStyle cellStyle = wb.createCellStyle();
	                cellStyle.setDataFormat(
	                        HSSFDataFormat.getBuiltinFormat("d/m/yy h:mm"));
	                HSSFCell cell = row.createCell((short)10);
	                
	                cell.setCellValue(rs.getTimestamp("fecha_asignacion"));
	                cell.setCellStyle(cellStyle);
	                
	                
	                row.createCell((short)11).setCellValue(rs.getInt("num_show"));
	                fila++;
	            }
	            
	            ServletOutputStream ouputStream = response.getOutputStream();
	            
	            //ouputStream.write(wb.getBytes(), 0, wb.getBytes().length);
	            wb.write(ouputStream); 
	            /*Limpiamos y cerramos flujos de salida*/
	            ouputStream.flush();
	            ouputStream.close();
	            rs.close();
	            
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.print("Error de conexion con el servidor: "
						+ e.getMessage());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.print("Error:  "
						+ e.getMessage());
			} finally {
				if (conexion != null) {
					try {
						conexion.Close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						System.out.print(e.getMessage());
					}
				}
			}
		}
    	
if (operacion.equals("download_resource")) {
			
			
			String cedula = (String)request.getParameter("cedula");
			
			String sql ="select depadesc,locadesc,orden,orders.nic,orders.nom_cli," +
					"orders.ape1_cli,orders.ape2_cli,orders.num_apa,orders.nom_calle," +
					"orders.nom_via,orders.placa,orders.crucero,orders.interior," +
					"tiordesc,orders.recurso,orders.fecha_asignacion," +
					"recurso.recunomb,orders.num_show " +
					"from orders,departamentos,localidad,tipo_orden,recurso " +
					"where localidad.locadepa=depacodi " +
					"and orders.localidad=locacodi " +
					"and orders.tipo=tiorcodi " +
					"and recucodi = orders.recurso " +
					"and orders.recurso = ? " +
					"and estado='2' " +
					"order by num_show";
			
			try {
				
				conexion = new db();
				java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
				pst.setString(1, cedula);
				ResultSet rs = conexion.Query(pst);
				HSSFWorkbook wb = new HSSFWorkbook();
				 
	            //Se crea una nueva hoja dentro del libro
	            HSSFSheet sheet = wb.createSheet("Hoja 1");
	            HSSFRow rowTitle = sheet.createRow((short)0);
	            
	            rowTitle.createCell((short)0).setCellValue("Departamento");
	            rowTitle.createCell((short)1).setCellValue("Municipio");
	            rowTitle.createCell((short)2).setCellValue("Orden");
	            rowTitle.createCell((short)3).setCellValue("NIC");
	            rowTitle.createCell((short)4).setCellValue("Cliente");
	            rowTitle.createCell((short)5).setCellValue("Direccion");
	            rowTitle.createCell((short)6).setCellValue("Tipo");
	            rowTitle.createCell((short)7).setCellValue("Medidor");
	            rowTitle.createCell((short)8).setCellValue("Asignacion");
	            rowTitle.createCell((short)9).setCellValue("Nombre");
	            rowTitle.createCell((short)10).setCellValue("Fecha Asignacion");
	            rowTitle.createCell((short)11).setCellValue("Orden");
	            int fila = 1;
	            while(rs.next()) {
	            	HSSFRow row = sheet.createRow((short)fila);
	                row.createCell((short)0).setCellValue(rs.getString("depadesc"));
	                row.createCell((short)1).setCellValue(rs.getString("locadesc"));
	                row.createCell((short)2).setCellValue(rs.getString("orden"));
	                row.createCell((short)3).setCellValue(rs.getString("nic"));
	                row.createCell((short)4).setCellValue(rs.getString("nom_cli") + " " +  rs.getString("ape1_cli") + " " + rs.getString("ape2_cli") );
	                row.createCell((short)5).setCellValue(rs.getString("nom_calle") + " " + rs.getString("crucero") + " " + rs.getString("placa") + " " + rs.getString("interior"));
	                row.createCell((short)6).setCellValue(rs.getString("tiordesc"));
	                row.createCell((short)7).setCellValue(rs.getString("num_apa"));
	                row.createCell((short)8).setCellValue(rs.getString("recurso"));
	                row.createCell((short)9).setCellValue(rs.getString("recunomb"));
	                HSSFCellStyle cellStyle = wb.createCellStyle();
	                cellStyle.setDataFormat(
	                        HSSFDataFormat.getBuiltinFormat("d/m/yy h:mm"));
	                HSSFCell cell = row.createCell((short)10);
	                
	                cell.setCellValue(rs.getTimestamp("fecha_asignacion"));
	                cell.setCellStyle(cellStyle);
	                
	                row.createCell((short)11).setCellValue(rs.getInt("num_show"));
	                fila++;
	            }
	            
	            ServletOutputStream ouputStream = response.getOutputStream();
	            
	            //ouputStream.write(wb.getBytes(), 0, wb.getBytes().length);
	            wb.write(ouputStream); 
	            /*Limpiamos y cerramos flujos de salida*/
	            ouputStream.flush();
	            ouputStream.close();
	            rs.close();
	            
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.print("Error de conexion con el servidor: "
						+ e.getMessage());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.print("Error:  "
						+ e.getMessage());
			} finally {
				if (conexion != null) {
					try {
						conexion.Close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						System.out.print(e.getMessage());
					}
				}
			}
		}
    	
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.ProcesarPeticion(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.ProcesarPeticion(request, response);
	}

}
