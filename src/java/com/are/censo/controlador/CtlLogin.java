/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.censo.controlador;

import com.are.censo.entidades.Login;
import com.are.censo.entidades.LoginResult;
import com.are.sofatec.db;
import java.sql.SQLException;

/**
 *
 * @author aimerrivera
 */
public class CtlLogin {

    private Login login;
    private LoginResult resultLogin;

    public CtlLogin(Login login) {
        this.login = login;
        this.resultLogin = null;
    }

    public LoginResult getResultLogin() {
        return resultLogin;
    }

    public boolean Login() throws Exception, SQLException {
        boolean result = false;
        if (login != null) {

            db conexion = new db();
            String sql = "SELECT recucodi, recunomb, recucont, recuzona,recurol "
                    + " FROM recurso "
                    + " WHERE recucodi = ? "
                    + " AND recupass = md5(?) "
                    + " AND recuesta = 1";

            java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
            pst.setString(1, login.getUser());
            pst.setString(2, login.getPassword());

            java.sql.ResultSet rs = conexion.Query(pst);
            if (rs.next()) {

                resultLogin = new LoginResult();
                resultLogin.setContrata(rs.getString("recucont"));
                resultLogin.setLogin(true);
                resultLogin.setNombres(rs.getString("recunomb"));
                resultLogin.setUser(rs.getString("recucodi"));
                resultLogin.setZona(rs.getString("recuzona"));
                resultLogin.setTipo(rs.getString("recurol"));
                resultLogin.setError("");

                result = true;

            } else {
                resultLogin = new LoginResult();
                resultLogin.setContrata("");
                resultLogin.setLogin(false);
                resultLogin.setNombres("");
                resultLogin.setUser("");
                resultLogin.setZona("");
                resultLogin.setTipo("");
                resultLogin.setError("Usuario y/o contraseña no valido.");
            }
            
            conexion.Close();

        } else {
            throw new Exception("Objeto login no valido");
        }

        return result;
    }

}
