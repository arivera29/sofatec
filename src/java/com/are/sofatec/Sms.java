package com.are.sofatec;

import java.sql.SQLException;

import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

import com.are.manejadores.ManejadorTipoOrden;

public class Sms {

    private db conexion = null;

    public db getConexion() {
        return conexion;
    }

    public void setConexion(db conexion) {
        this.conexion = conexion;
    }

    public Sms(db conexion) {
        super();
        this.conexion = conexion;
    }

    public int enviar(String cedula, String orden) throws SQLException {
        int ret = 0;
//        recursohumano recurso = new recursohumano(conexion);
//        Ordenes os = new Ordenes(conexion);
//        if (recurso.Find(cedula) && os.Find(orden)) {
//            if (!recurso.getTelefono().isEmpty()) { // el recurso tiene telefono
//                String telefono = recurso.getTelefono();
//                String direccion = os.getDireccion();
//                String medidor = os.getNum_apa();
//                String deuda = os.getDeuda();
//                String facturas = os.getCantfactura();
//                String tipo_orden = "DESCONOCIDO";
//                ManejadorTipoOrden manejador = new ManejadorTipoOrden(conexion);
//
//                if (manejador.Find(os.getTipo())) {
//                    tipo_orden = manejador.getTipo().getDescripcion();
//                }
//
//                String mensaje_sms = "#" + orden + " Tipo: " + tipo_orden + " Dir: " + direccion + " Med. " + medidor + " Deuda " + deuda + " Cnt Fac. " + facturas;
//
//                try {
//                    String endpoint = "http://www.elibom.com/services/sendmessagews";
//
//                    Service service = new Service();
//                    Call call = (Call) service.createCall();
//
//                    call.setTargetEndpointAddress(new java.net.URL(endpoint));
//
//                    call.setOperationName(new QName("http://www.elibom.com/sendmessage", "sendMessage"));
//
//                    ret = ((Integer) call.invoke(new Object[]{"aimer.rivera@are-soluciones.com", "pantera2341", telefono, mensaje_sms})).intValue();
//                } catch (ServiceException ex) {
//                    System.out.println("Exception: Number: " + telefono + " - Error: " + ex.getMessage());
//                } catch (Exception e) {
//                    System.out.println("Exception: Number: " + telefono + " - Error: " + e.getMessage());
//                }
//            }
//        }

        return ret;
    }

}
