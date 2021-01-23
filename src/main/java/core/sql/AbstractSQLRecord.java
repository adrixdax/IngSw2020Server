/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.sql;

import java.sql.Connection;

/**
 *
 * @author Antimo
 */
public abstract class AbstractSQLRecord {

    private Connection sql_connection;
    
    

    public abstract void afterRecordLoad();

    public abstract void beforeRecordLoad();

    public abstract void afterRecordUpdate();

    public abstract void beforeRecordUpdate();

    public abstract void beforeRecordInsert();

    public abstract void afterRecordInsert();

    public abstract void beforeRecordDeleted();

    public abstract void afterRecordDeleted();

    public Connection getSql_connection() {
        return sql_connection;
    }

    public void setSql_connection(Connection sql_connection) {
        this.sql_connection = sql_connection;
    }

    
    
    
}
