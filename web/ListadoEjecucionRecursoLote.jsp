<%@ page import="com.are.sofatec.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<%
        request.setCharacterEncoding("ISO-8859-1");
        db conexion = new db();
        String recurso = (String) request.getParameter("recurso");
        String fecha_inicial = (String) request.getParameter("fecha_inicial");
        String fecha_final = (String) request.getParameter("fecha_final");

        String sql = "select QO_ORDENES.NUM_OS,QO_ORDENES.NIC,DESC_TIPO, "
                        + "DATE(IF(ISNULL(FECHA_ANOMALIA),FECHA_CIERRE,FECHA_ANOMALIA)) fecha_cierre,"
                        + "TIME(IF(ISNULL(FECHA_ANOMALIA),FECHA_CIERRE,FECHA_ANOMALIA)) hora,"
                        + "QO_ORDENES.DIRECCION,QO_DATOSUM.DEPARTAMENTO, QO_DATOSUM.MUNICIPIO, "
                        + "IF(ESTADO_OPER=99,OBSERVACION_OS(NUM_OS),QO_ORDENES.OBSERVACION) OBSERVACION,"
                        + "QO_ORDENES.ESTADO_OPER,recurso.recunomb "
                        + " from QO_ORDENES,QO_DATOSUM,QO_TIPOS,recurso "
                        + " where QO_ORDENES.NIC = QO_DATOSUM.NIC "
                        + " AND QO_ORDENES.NIS_RAD = QO_DATOSUM.NIS_RAD "
                        + " and QO_ORDENES.TIP_OS = QO_TIPOS.TIPO "
                        + " and QO_ORDENES.TECNICO = recurso.recucodi "
                        + " and QO_ORDENES.TECNICO = ? "
                        + " and (DATE(FECHA_ANOMALIA) BETWEEN ? AND ?  OR DATE(FECHA_CIERRE) BETWEEN ? AND ? ) ";
        sql += " ORDER BY DEPARTAMENTO, MUNICIPIO";

        java.sql.PreparedStatement pst = conexion.getConnection()
                        .prepareStatement(sql);
        pst.setString(1, recurso);
        pst.setString(2, fecha_inicial);
        pst.setString(3, fecha_final);
        pst.setString(4, fecha_inicial);
        pst.setString(5, fecha_final);

        java.sql.ResultSet rs = conexion.Query(pst);
        boolean rsIsEmpty = !rs.next();
        int cont = 0;
%>
<h2>Ordenes ejecutadas</h2>
<table id="Exportar_a_Excel">
    <tr>
        <th>Dpto.</th>
        <th>Municipio</th>
        <th>Orden</th>
        <th>NIC</th>
        <th>Direccion</th>
        <th>Recurso</th>
        <th>Tipo orden</th>
        <th>Fecha</th>
        <th>Hora</th>
        <th>Resultado</th>
        <th>Observacion</th>
    </tr>
    <%
            if (!rsIsEmpty) {
    %>

    <%
            do {
    %>
    <tr <%=(cont % 2 == 0 ? "class=\"odd\"" : "")%>>
        <td><%=rs.getString("DEPARTAMENTO")%></td>
        <td><%=rs.getString("MUNICIPIO")%></td>
        <td><a href="DetalleOrdenLote.jsp?orden=<%=rs.getString("NUM_OS")%>" target="_blank"><%=rs.getString("NUM_OS")%></a></td>
        <td><%=rs.getString("NIC")%></td>
        <td><%=rs.getString("DIRECCION")%></td>
        <td><%=rs.getString("RECUNOMB")%></td>
        <td><%=rs.getString("DESC_TIPO")%></td>
        <td><%=rs.getDate("FECHA_CIERRE")%></td>
        <td><%=rs.getString("HORA")%></td>
        <td>
            <%
                    switch (rs.getInt("ESTADO_OPER")) {
                                    case 2:
                                            out.print("ASIGANDA");
                                            break;
                                    case 3:
                                            out.print("<label class=\"red\">ANOMALIA</label>");
                                            break;
                                    case 99:
                                            out.print("EFECTIVA");
                                            break;
                                    }
            %>

        </td>
        <td><%=rs.getString("OBSERVACION")==null?"":rs.getString("OBSERVACION").toUpperCase() %></td>
    </tr>
    <%
            cont++;
                    } while (rs.next());
    %>
    <tr>
        <td colspan="11">Total de ordenes: <%=cont%></td>
    </tr>
    <%
            } else {
    %>
    <tr>
        <td colspan="11">No se encontraron registros</td>
    </tr>

    <%
            }
    %>
</table>
<%
        conexion.Close();
%>