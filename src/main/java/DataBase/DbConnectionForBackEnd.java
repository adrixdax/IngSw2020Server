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
    private String user = "Adriano";
    private String pw = "IngSw2020!";
    private String schema = "Cinemates20Development";

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

    public boolean createConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String linkToDb = "jdbc:mysql://87.1.139.228:3306";
            con = DriverManager.getConnection(linkToDb, user, pw);
            String command = "use "+schema;
            PreparedStatement st = con.prepareStatement(command);
            return st.execute(command);
        } catch (Exception ex) {
            return false;
        }
    }

    public Connection getConnection() {
        createConnection();
        return con;
    }

}
