/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cinemates.core.cnntodb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Antimo
 */
public class ConnectionToMySQL {

    private String connectionString = "";
    private String localhost;
    private String nameDB;
    private String username;
    private String password;

    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String localhost) {
        this.connectionString = "jdbc:mysql://" + localhost;
    }

    public String getLocalhost() {
        return localhost;
    }

    public void setLocalhost(String localhost) {
        this.localhost = localhost;
    }

    public String getNameDB() {
        return nameDB;
    }

    public void setNameDB(String nameDB) {
        this.nameDB = nameDB;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Connection getCnn() {
        try {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            Connection connection = DriverManager.getConnection(getConnectionString(), getUsername(), getPassword());
            PreparedStatement st = connection.prepareStatement("use " + getNameDB());
            st.execute();
            st.close();
            if(connection!= null){
                System.out.println("schema  ->  " + connection.getSchema() + "  connessione  ->" + connection.getClientInfo());
                return connection;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionToMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

}
