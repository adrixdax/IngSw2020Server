/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cinemates.core.sql;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Antimo
 */
public class MySQLUtility {

    public static final int type_int = 0;
    public static final int type_long = 1;
    public static final int type_string = 2;
    public static final int type_text = 3;
    public static final int type_bool = 4;
    public static final int type_double = 5;

    private Connection cnn = null;

    public Connection getCnn() {
        return cnn;
    }

    public void setCnn(Connection cnn) {
        this.cnn = cnn;
    }

    public static MySQLUtility getNewIstance(Connection cnn) {
        MySQLUtility fa = new MySQLUtility();
        fa.setCnn(cnn);
        return fa;
    }

    public void LoadFieldFromResultSet(AbstractSQLRecord object, ResultSet rs) {
        Class c = object.getClass();
        Field[] recordField = c.getDeclaredFields();
        int recordFieldLength = recordField.length;

        for (int positionField = 0; positionField < recordFieldLength; ++positionField) {
            Field singleField = recordField[positionField];

            if (singleField.getAnnotation(MySqlAnnotation.class) != null) {
                try {
                    String fieldToSet = singleField.getName();
                    fieldToSet = fieldToSet.substring(0, 1).toUpperCase() + fieldToSet.substring(1);
                    setField(singleField, fieldToSet, c, object, rs);
                } catch (SecurityException | IllegalArgumentException ex) {
                    Logger.getLogger(MySQLUtility.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    private void setField(Field singleField, String fieldToSet, Class c, Object object, ResultSet rs) {
        try {
            Method metodo = null;
            switch (((MySqlAnnotation) singleField.getAnnotation(MySqlAnnotation.class)).type()) {
                case 0:
                    metodo = c.getMethod("set" + fieldToSet, Integer.TYPE);
                    metodo.invoke(object, rs.getInt(singleField.getName()));
                    break;
                case 1:
                    metodo = c.getMethod("set" + fieldToSet, Long.TYPE);
                    metodo.invoke(object, rs.getLong(singleField.getName()));
                    break;
                case 2:
                case 3:
                    metodo = c.getMethod("set" + fieldToSet, String.class);
                    metodo.invoke(object, rs.getString(singleField.getName()));
                    break;
                case 4:
                    metodo = c.getMethod("set" + fieldToSet, Boolean.TYPE);
                    metodo.invoke(object, rs.getBoolean(singleField.getName()));
                    break;
                case 5:
                    metodo = c.getMethod("set" + fieldToSet, Double.TYPE);
                    metodo.invoke(object, rs.getDouble(singleField.getName()));
                    break;
                default:
                    break;
            }
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SQLException ex) {
            Logger.getLogger(MySQLUtility.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateFieldToDB(Object object) {
        try {
            Class c = object.getClass();
            List<MySQLRecord> fieldToUpdate = new ArrayList();

            Field[] recordField = c.getDeclaredFields();
            int recordFieldLength = recordField.length;

            for (int positionField = 0; positionField < recordFieldLength; ++positionField) {
                Field singleField = recordField[positionField];
                if (singleField.getAnnotation(MySqlAnnotation.class) != null) {
                    MySQLRecord myfield = new MySQLRecord();
                    myfield.setColum_name(singleField.getName());
                    if (!myfield.getColum_name().isEmpty()) {
                        myfield.setData_type(((MySqlAnnotation) singleField.getAnnotation(MySqlAnnotation.class)).type());
                        String fieldToGet;
                        fieldToGet = myfield.getColum_name().substring(0, 1).toUpperCase() + myfield.getColum_name().substring(1);
                        Method metodo = null;
                        switch (myfield.getData_type()) {
                            case 0:
                            case 1:
                            case 5:
                                metodo = c.getMethod("get" + fieldToGet);
                                myfield.setCurretValue(String.valueOf(metodo.invoke(object)));
                                break;
                            case 2:
                            case 3:
                                metodo = c.getMethod("get" + fieldToGet);
                                myfield.setCurretValue((String) metodo.invoke(object));
                                break;
                            case 4:
                                metodo = c.getMethod("is" + fieldToGet);
                                if ((Boolean) metodo.invoke(object)) {
                                    myfield.setCurretValue("1");
                                } else {
                                    myfield.setCurretValue("0");
                                }
                            default:
                                break;
                        }
                        fieldToUpdate.add(myfield);
                    }
                }
            }

            if (fieldToUpdate.size() > 0) {
                String sqlUpdate = "UPDATE " + c.getSimpleName() + " SET ";
                String whereField = "";
                for (MySQLRecord mySQLField : fieldToUpdate) {
                    if (mySQLField.getColum_name().equals("id_" + c.getSimpleName())) {
                        whereField = "WHERE " + mySQLField.getColum_name() + " ='" + mySQLField.getCurretValue() + "'";
                    }
                    sqlUpdate = sqlUpdate + mySQLField.getUpdateCommand() + " ,";
                }

                sqlUpdate = sqlUpdate.substring(0, sqlUpdate.length() - 1) + whereField;
                Statement st = this.getCnn().createStatement();
                st.execute(sqlUpdate);
                st.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(MySQLUtility.class.getName()).log(Level.SEVERE, (String) null, ex);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(MySQLUtility.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void addRecordToDB(Object object) {
        try {
            Class c = object.getClass();
            List<MySQLRecord> fieldToAdd = new ArrayList();

            Field[] recordField = c.getDeclaredFields();
            int recordFieldLength = recordField.length;

            for (int positionField = 0; positionField < recordFieldLength; ++positionField) {
                Field singleField = recordField[positionField];
                if (singleField.getAnnotation(MySqlAnnotation.class) != null) {
                    MySQLRecord myfield = new MySQLRecord();
                    myfield.setColum_name(singleField.getName());
                    if (!myfield.getColum_name().isEmpty()) {
                        myfield.setData_type(((MySqlAnnotation) singleField.getAnnotation(MySqlAnnotation.class)).type());
                        String fieldToGet;
                        fieldToGet = myfield.getColum_name().substring(0, 1).toUpperCase() + myfield.getColum_name().substring(1);
                        Method metodo = null;
                        switch (myfield.getData_type()) {
                            case 0:
                            case 1:
                            case 5:
                                metodo = c.getMethod("get" + fieldToGet);
                                myfield.setCurretValue(String.valueOf(metodo.invoke(object)));
                                break;
                            case 2:
                            case 3:
                                metodo = c.getMethod("get" + fieldToGet);
                                myfield.setCurretValue((String) metodo.invoke(object));
                                break;
                            case 4:
                                metodo = c.getMethod("is" + fieldToGet);
                                if ((Boolean) metodo.invoke(object)) {
                                    myfield.setCurretValue("1");
                                } else {
                                    myfield.setCurretValue("0");
                                }
                            default:
                                break;
                        }
                        fieldToAdd.add(myfield);
                    }
                }
            }

            if (fieldToAdd.size() > 0) {
                String sqlAdd = "INSERT INTO " + c.getSimpleName() + " ";
                String sqlAttribute = "(";
                String sqlValues = "(";
                for (MySQLRecord mySQLField : fieldToAdd) {
                    sqlAttribute = sqlAttribute + mySQLField.getColum_name() + " ,";
                    sqlValues = sqlValues + "'" + mySQLField.getCurretValue() + "' ,";
                }

                sqlAttribute = sqlAttribute.substring(0, sqlAttribute.length() - 1) + ")";
                sqlValues = sqlValues.substring(0, sqlValues.length() - 1) + ")";
                sqlAdd = sqlAdd + sqlAttribute + "VALUES" + sqlValues + " ;";

                Statement st = this.getCnn().createStatement();
                st.execute(sqlAdd);
                st.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(MySQLUtility.class.getName()).log(Level.SEVERE, (String) null, ex);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(MySQLUtility.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
