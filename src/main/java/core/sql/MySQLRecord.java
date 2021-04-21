package core.sql;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import utility.MySQLUtility;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Antimo
 */
public class MySQLRecord extends AbstractSQLRecord {

    private String colum_name = "";
    private int data_type = 0;
    private String curretValue = "";

    public String getColum_name() {
        return colum_name;
    }

    public void setColum_name(String colum_name) {
        this.colum_name = colum_name;
    }

    public int getData_type() {
        return data_type;
    }

    public void setData_type(int data_type) {
        this.data_type = data_type;
    }

    public String getCurretValue() {
        return curretValue;
    }

    public void setCurretValue(String curretValue) {
        if (curretValue != null) {
            this.curretValue = curretValue;
        } else {
            this.curretValue = "";
        }
    }

    @Override
    public void afterRecordLoad() {
    }

    @Override
    public void beforeRecordLoad() {
    }

    @Override
    public void afterRecordUpdate() {
    }

    @Override
    public void beforeRecordUpdate() {
    }

    @Override
    public void beforeRecordInsert() {
    }

    @Override
    public void afterRecordInsert() {
    }

    @Override
    public void beforeRecordDeleted() {
    }

    @Override
    public void afterRecordDeleted() {
    }

    public void deleteRecord() {

        try {
            this.beforeRecordDeleted();
            Statement st = this.getSql_connection().createStatement();
            String command = "delete from " + this.getClass().getSimpleName() + " where id" + this.getClass().getSimpleName() + "='" + this._getPrimaryKeyID() + "'";
            System.out.println(command);
            st.execute(command);
            st.close();
            this.afterRecordDeleted();
        } catch (SQLException ex) {
            Logger.getLogger(MySQLRecord.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private String _getPrimaryKeyID() {
        String primaryKey = "";
        try {
            Class c = this.getClass();
            Method metodo = c.getMethod("getId" + c.getSimpleName());
            primaryKey = String.valueOf(metodo.invoke(this));
        } catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException ex) {
            Logger.getLogger(MySQLRecord.class.getName()).log(Level.SEVERE, null, ex);
        }
        return primaryKey;
    }

    public String getUpdateCommand() {
        String rest = "";
        switch (this.data_type) {
            case 0:
            case 1:
            case 4:
            case 5:
                rest = this.colum_name + " =" + this.curretValue;
                break;
            case 2:
            case 3:
                rest = this.colum_name + " ='" + this.curretValue.replaceAll("'", "''") + "'";
                break;
            default:
                break;
        }
        return rest;
    }

    public void updateRecord() {

        try {
            this.beforeRecordUpdate();

            MySQLUtility.getNewIstance(this.getSql_connection()).updateFieldToDB(this);

            this.afterRecordUpdate();
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(MySQLRecord.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public void addRecord(){

        try{
            this.beforeRecordInsert();

            MySQLUtility.getNewIstance(this.getSql_connection()).addRecordToDB(this);

            this.afterRecordInsert();

        }catch (IllegalArgumentException ex) {
            Logger.getLogger(MySQLRecord.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
