package DataBase;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Scanner;


public class DbConnectionForBackEnd {
    private static Connection con;
    private static final File config = new File("./config");
    private String user = "";
    private String pw = "";
    private String schema = "";

    public DbConnectionForBackEnd() {
        createConnection();
    }

    private void recoverUser() {
        try {
            Scanner myReader = new Scanner(config);
            user = myReader.nextLine().replaceFirst("u:", "");
        } catch (FileNotFoundException ex) {
            System.out.println("Server not configured");
        }
    }

    private void recoverPW() {
        try {
            Scanner myReader = new Scanner(config);
            myReader.nextLine();
            pw = myReader.nextLine().replaceFirst("p:", "");
        } catch (FileNotFoundException ex) {
            System.out.println("Server not configured");
        }
    }

    private void recoverSchema() {
        try {
            Scanner myReader = new Scanner(config);
            myReader.nextLine();
            myReader.nextLine();
            schema = myReader.nextLine().replaceFirst("s:", "");
        } catch (FileNotFoundException ex) {
            System.out.println("Server not configured");
        }
    }

    public void createConnection() {
        try {
            recoverUser();
            recoverPW();
            recoverSchema();
            Class.forName("com.mysql.cj.jdbc.Driver");
            java.util.Properties connProperties = new java.util.Properties();
            connProperties.put("user", user);
            connProperties.put("password", pw);
            connProperties.put("maxReconnects", "4");
            connProperties.put("connectTimeout","0");
            connProperties.put("socketTimeout","0");
            connProperties.put("autoReconnect", "true");
            String linkToDb = "jdbc:mysql://cinametes.cbyxxfgsthal.eu-central-1.rds.amazonaws.com:3306";
            con = DriverManager.getConnection(linkToDb, connProperties);
            String command = "use "+schema;
            PreparedStatement st = con.prepareStatement(command);
            st.execute(command);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Connection getConnection() {
        createConnection();
        return con;
    }

}