/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.sofatec;

/**
 *
 * @author Aimer
 */
import java.sql.*;

import javax.naming.Context;
import javax.naming.NamingException;


public class db {

    private Connection conn = null;
    private int FilasAfectadas = 0;
    private String url = "jdbc:mysql://localhost:3306/sofatec";
    private String user = "root";
    private String password = "";

    public db() throws SQLException  {

            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            ReadConfigDatabase();
            conn = DriverManager.getConnection(this.url, this.user, this.password);
            if (conn == null) {
                throw new SQLException("Problema de conexion con el Servidor");
            } else {
                conn.setAutoCommit(false); // Modo de Autocommit deshabilitado
            }
        
    }

    public Connection getConnection() {
        return conn;
    }

    public ResultSet Query(String sql) throws SQLException {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        return rs;
    }
    
    public ResultSet Query(PreparedStatement pst) throws SQLException {
        ResultSet rs = pst.executeQuery();
        return rs;
    }
    

    public int Update(String sql) throws SQLException {
        Statement st = conn.createStatement();
        FilasAfectadas = st.executeUpdate(sql);
        return FilasAfectadas;
    }
    
    public int Update(PreparedStatement pst) throws SQLException {
        FilasAfectadas = pst.executeUpdate();
        return FilasAfectadas;
    }

    public int Update(String sql, int Modo) throws SQLException {
        Statement st = conn.createStatement();
        FilasAfectadas = st.executeUpdate(sql);
        if (Modo == 1) {  // efectuar el Commit
            this.Commit();
        }
        return FilasAfectadas;
    }

    public void Close() throws SQLException {
        conn.close();
    }

    public void Commit() throws SQLException {
        conn.commit();
    }

    public void Rollback() throws SQLException {
        conn.rollback();
    }
    
    public int getLastId() throws SQLException {
    	int last_id = -1;
    	String sql = "SELECT LAST_INSERT_ID()";
    	ResultSet rs = this.Query(sql);
    	if (rs.next()) {
    		last_id = rs.getInt(1);
    	}
    	rs.close();
    	return last_id;
    }
    
    public String current_date() throws SQLException {
        String sql = "select current_date() as today;";
        java.sql.ResultSet rs = this.Query(sql);
        if (rs.next()) {
            return rs.getString("today");
        }else {
            return "";
        }

    }
    
    public void ReadConfigDatabase() {
    	javax.naming.Context ctx;
		try {
			ctx = new javax.naming.InitialContext();
			javax.naming.Context env = (Context) ctx.lookup("java:comp/env");
			this.url = (String) env.lookup("url");
			this.user = (String) env.lookup("user");
			this.password = (String) env.lookup("password");
			//System.out.println("url:" + this.url + " user:" + this.user + " password:" + this.password);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	

    	// obtain the greetings message 
    	//configured by the deployer
    	
        
    }
    
    
}
