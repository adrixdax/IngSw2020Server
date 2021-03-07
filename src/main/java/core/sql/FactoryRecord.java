/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.sql;

import utility.MySQLUtility;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Antimo
 */
public class FactoryRecord {

    private Connection cnn;

    public static FactoryRecord getNewIstance(Connection cnn) {
        FactoryRecord fa = new FactoryRecord();
        fa.setCnn(cnn);
        return fa;
    }

    public Connection getCnn() {
        return cnn;
    }

    public void setCnn(Connection cnn) {
        this.cnn = cnn;
    }

    public AbstractSQLRecord getSingleRecord(Connection cnn_sql, Class c, String whereclausule){

        Connection cnn = null;
        if (cnn_sql != null) {
            cnn = cnn_sql;
        }

        AbstractSQLRecord onj = null;
        if (!whereclausule.isEmpty()) {
        whereclausule = "where " + whereclausule.replaceFirst("where", "");
        }

        String select = "select * from " + c.getSimpleName() + " " + whereclausule;
        try {
            System.out.println(select);
            Statement st;
            assert cnn != null;
            st = cnn.createStatement();
            System.out.println(select);
            ResultSet rs = st.executeQuery(select);
            if (rs.next()) {
                onj = (AbstractSQLRecord) c.newInstance();
                onj.setSql_connection(cnn);
                MySQLUtility.getNewIstance(cnn).LoadFieldFromResultSet(onj, rs);
            }

            rs.close();
            st.close();

        } catch (SQLException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(FactoryRecord.class.getName()).log(Level.SEVERE, null, ex);
        }

        return onj;
    }

    public List<AbstractSQLRecord> getListOfRecord(Connection cnn_sql, Class c, String whereclausule){

        Connection cnn = null;
        if (cnn_sql != null) {
            cnn = cnn_sql;
        }

        AbstractSQLRecord onj = null;
        if (!whereclausule.isEmpty()) {
            whereclausule = "where " + whereclausule.replaceFirst("where", "");
        }

        List<AbstractSQLRecord> listOfRecord= new ArrayList<>();

        String select = "select * from " + c.getSimpleName() + " " + whereclausule;
        System.out.println(select);
        try {
            Statement st;
            assert cnn != null;
            st = cnn.createStatement();
            ResultSet rs = st.executeQuery(select);
            while (rs.next()) {
                onj = (AbstractSQLRecord) c.newInstance();
                onj.setSql_connection(cnn);
                MySQLUtility.getNewIstance(cnn).LoadFieldFromResultSet(onj, rs);
                listOfRecord.add(onj);
            }

            rs.close();
            st.close();

        } catch (SQLException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(FactoryRecord.class.getName()).log(Level.SEVERE, null, ex);
        }

        return listOfRecord;
    }


}
