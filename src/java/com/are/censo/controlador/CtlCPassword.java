/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.censo.controlador;

import com.are.censo.entidades.CPassword;
import com.are.censo.entidades.CPasswordResult;
import com.are.censo.entidades.Login;
import com.are.sofatec.db;
import java.sql.SQLException;

/**
 *
 * @author aimerrivera
 */
public class CtlCPassword {
    private CPassword cpassword;
    private CPasswordResult changepassword;

    public CtlCPassword(CPassword cpassword) {
        this.cpassword = cpassword;
        changepassword = null;
    }

    public CPassword getCpassword() {
        return cpassword;
    }

    public void setCpassword(CPassword cpassword) {
        this.cpassword = cpassword;
    }

    
    
    public CPasswordResult getChangepassword() {
        return changepassword;
    }
   
   public boolean ChangePassword() throws Exception, SQLException {
       boolean result = false;
       
        // Validar si el usuario es valido con el atributo old_password
       
       Login login = new Login();
       login.setPassword(cpassword.getOld_password());
       login.setUser(cpassword.getUser());
       login.setToken(cpassword.getToken());
       
       
       CtlLogin controlador = new CtlLogin(login);
       
       if (controlador.Login()) {
           db conexion = new db();
           String sql = "UPDATE recurso SET recupass = MD5(?) WHERE recucodi = ?";
           java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
           pst.setString(1, cpassword.getNew_password());
           pst.setString(2, cpassword.getUser());
           if (conexion.Update(pst) > 0) {
               conexion.Commit();
               changepassword = new CPasswordResult();
               changepassword.setChangepassord(true);
               changepassword.setError("");
               result = true;
           }else {
               changepassword = new CPasswordResult();
               changepassword.setChangepassord(false);
               changepassword.setError("Error al actualizar la contraseña del usuario " + cpassword.getUser());
           }

           conexion.Close();
       }else {
           changepassword = new CPasswordResult();
           changepassword.setChangepassord(false);
           changepassword.setError("Usuario y/o contraseña no validos");
       }
       
       
       return result;
   }
    
    
}
