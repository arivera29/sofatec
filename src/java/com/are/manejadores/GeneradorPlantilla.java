/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.manejadores;

import com.are.sofatec.db;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aimerrivera
 */
public class GeneradorPlantilla {

    private db conexion;
    private ArrayList<String> obs;
    private final static Logger LOGGER = Logger.getLogger(GeneradorPlantilla.class.getName());

    public GeneradorPlantilla(db conexion) {
        this.conexion = conexion;
    }

    public db getConexion() {
        return conexion;
    }

    public void setConexion(db conexion) {
        this.conexion = conexion;
    }

    public ArrayList<String> getObs() {
        return obs;
    }

    public void setObs(ArrayList<String> obs) {
        this.obs = obs;
    }

    public GeneradorPlantilla() {
        ArrayList<String> obs = new ArrayList<String>();
    }
    
    

    public String obtenerObservacion(String orden) throws SQLException {
        LOGGER.log(Level.INFO, "[GENERATE_TEMPLATE] -> Get observacion OS {0}", orden);
        String cadena = "";

        String sql = "SELECT  NUM_ACTA, NOMBRE_OPERARIO_HDA, "
                + " FECHA_CENSO, NUM_MT, NUM_CT, "
                + " IFNULL(OBSERVACION,'') OBSERVACION "
                + " FROM QO_ORDENES WHERE NUM_OS=?";

        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, orden);
        java.sql.ResultSet rs0 = conexion.Query(pst);

        if (rs0.next()) {
            cadena += " Acta " + rs0.getString("NUM_ACTA");
            cadena += " Fecha " + rs0.getString("FECHA_CENSO");
            cadena += " Tec. " + rs0.getString("NOMBRE_OPERARIO_HDA");
            cadena += " CT=" + rs0.getString("NUM_CT");
            cadena += " MT=" + rs0.getString("NUM_MT");
            if (!rs0.getString("OBSERVACION").equals("")) {
                cadena += " OBS " + rs0.getString("OBSERVACION");
            }

        }

        cadena += " ";
        double totalCenso = 0;
        sql = "SELECT DESC_AV, CANTIDAD, QO_EQUIPOS.CARGA "
                + "FROM QO_CENSO "
                + "INNER JOIN QO_EQUIPOS ON QO_CENSO.ID_EQUIPO = QO_EQUIPOS.ID "
                + "WHERE QO_CENSO.NUM_OS=?";
        pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, orden);
        java.sql.ResultSet rs1 = conexion.Query(pst);
        String cadena2 = "";
        while (rs1.next()) {
            cadena2 += rs1.getString("DESC_AV") + " " +  rs1.getDouble("CARGA") +  "*" + rs1.getInt("CANTIDAD") + "=" + (rs1.getDouble("CARGA")*rs1.getInt("CANTIDAD")) + "w ";
            totalCenso = totalCenso + (rs1.getInt("CANTIDAD")*rs1.getDouble("CARGA"));
        }
        
        cadena2 = " Total Censo " + totalCenso + " " + cadena2;
        cadena += cadena2;
        
        LOGGER.log(Level.INFO, "[GENERATE_TEMPLATE] -> Generate Observacion OS  {0} {1}", new Object[]{orden, cadena});

        return cadena;
    }

    public ArrayList<String> dividirObservacion(String observacion, int longitud) {
        ArrayList<String> cadena = new ArrayList<String>();
        if (observacion.length() <= longitud) {
            cadena.add(observacion);
        } else {

//            int nPartes = (int)(observacion.length() / longitud);
//            int posIni = 0;
//            int posFinal = longitud -1;
//            for (int x=1; x <= nPartes; x++) {
//                String str = observacion.substring(posIni,posFinal);
//                cadena.add(str);
//                posIni = posFinal;
//                if ((observacion.length() - (posFinal+1)) > longitud) {
//                    posFinal = posFinal + longitud;
//                }else {
//                    posFinal = observacion.length() - posFinal+1;
//                }
//                
//            }
            String[] lista = observacion.split(" ");
            String str = "";
            for (int x = 0; x < lista.length; x++) {
                if (((str.length() + lista[x].length() + 1) <= longitud)) {
                    str += lista[x] + " ";
                } else {
                    cadena.add(str);
                    str = lista[x] + " ";
                }
                
                if (x+1 == lista.length) {
                    if (!str.equals("")) {
                        cadena.add(str);
                    }
                }
            }

        }

        return cadena;
    }

    public boolean GenerarPlantilla(String orden, String tipo) throws SQLException, Exception {
        boolean resultado = false;
        LOGGER.log(Level.INFO, "[GENERATE_TEMPLATE] -> Generating template OS {0} tipo {1}", new Object[]{orden, tipo});
        String observacion = this.obtenerObservacion(orden);
        this.obs = this.dividirObservacion(observacion, 200);
        int index = 0;

        String sql = "SELECT * FROM QO_PLANRESO WHERE TIP_OS=?";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, tipo);
        java.sql.ResultSet rs = conexion.Query(pst);

        if (rs.next()) {
            InicializarOrden(orden);
            this.AgregarLectura(orden);
            conexion.Update("DELETE FROM QO_NUEVOS_PASOS WHERE NUM_OS='" + orden + "'");
            boolean pasa = true;
            int num_paso = 0;
            do {

                if (rs.getString("TIP_PASO").equals("1")) {
                    sql = "UPDATE QO_PASOS SET CUMPLIDO=1, CO_ACCEJE=?, OBSERVACION=?, EDIT_OBS=? WHERE NUM_OS=? AND DESCRIPCION=? ";
                    java.sql.PreparedStatement pst2 = conexion.getConnection().prepareStatement(sql);
                    pst2.setString(1, rs.getString("CO_ACCEJE"));
                    if (index < obs.size()) {
                        pst2.setString(2, obs.get(index));
                        pst2.setInt(3, 1);
                        index++;
                    } else {
                        pst2.setString(2, rs.getString("OBSERVACION"));
                        pst2.setInt(3, 0);
                    }
                    
                    pst2.setString(4, orden);
                    pst2.setString(5, rs.getString("DESC_PASO"));
                    
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
                            + "FLUJO,"
                            + "EDIT_OBS)"
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
                            + "?,"
                            + "?)";
                    java.sql.PreparedStatement pst2 = conexion.getConnection().prepareStatement(sql);
                    pst2.setString(1, orden);
                    pst2.setInt(2, num_paso);
                    pst2.setString(3, rs.getString("DESC_PASO"));
                    pst2.setString(4, rs.getString("CO_ACCEJE"));
                    if (index < obs.size()) {
                        pst2.setString(5, obs.get(index));
                        pst2.setInt(8, 1);
                        index++;
                    } else {
                        pst2.setString(5, rs.getString("OBSERVACION"));
                        pst2.setInt(8, 0);
                    }
                    pst2.setString(6, rs.getString("PARENT"));
                    pst2.setString(7, rs.getString("FLUJO"));
                    
                    if (conexion.Update(pst2) > 0) {

                    } else {
                        pasa = false;
                    }

                }

            } while (rs.next() && pasa == true);

            if (pasa == true) {
                conexion.Commit();
                resultado = true;
            } else {
                conexion.Rollback();

            }

        } else {

            throw new Exception("No existe plantilla para el tipo de orden ingresado");

        }

        return resultado;

    }

    private void InicializarOrden(String orden) throws SQLException {
        LOGGER.log(Level.INFO, "[GENERATE_TEMPLATE] -> Initializate OS {0}", orden);
        String sql = "UPDATE QO_PASOS SET CUMPLIDO=0, CO_ACCEJE='', OBSERVACION='', EDIT_OBS=0 WHERE NUM_OS=? ";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, orden);
        conexion.Update(pst);

        sql = "DELETE FROM QO_NUEVOS_PASOS WHERE NUM_OS=? ";
        java.sql.PreparedStatement pst2 = conexion.getConnection().prepareStatement(sql);
        pst2.setString(1, orden);
        conexion.Update(pst);

    }

    private void AgregarLectura(String orden) throws SQLException {
        LOGGER.log(Level.INFO, "[GENERATE_TEMPLATE] -> Remove record lectura OS {0}", orden);
        conexion.Update("DELETE FROM QO_ORDEN_LECTURA WHERE NUM_OS='" + orden + "'");
        String sql = "SELECT NUM_APA FROM QO_APARATOS WHERE NUM_OS=?";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, orden);
        LOGGER.log(Level.INFO, "[GENERATE_TEMPLATE] -> Verify if OS {0} have record in QO_APARATOS", orden);
        java.sql.ResultSet rs = conexion.Query(pst);
        if (rs.next()) {
            LOGGER.log(Level.INFO, "[GENERATE_TEMPLATE] -> OS {0} record in QO_APARAROS found ", orden);
            sql = "INSERT INTO QO_ORDEN_LECTURA (NUM_OS,NUM_APA,LECTURA) VALUES (?,?,1)";
            java.sql.PreparedStatement pst2 = conexion.getConnection().prepareStatement(sql);
            pst2.setString(1, orden);
            pst2.setString(2, rs.getString("NUM_APA"));
            LOGGER.log(Level.INFO, "[GENERATE_TEMPLATE] -> Add record of LECTURA=1 for OS {0}", orden);
            conexion.Update(pst2);
        }
    }
}
