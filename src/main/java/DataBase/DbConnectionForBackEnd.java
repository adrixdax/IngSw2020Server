package DataBase;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Scanner;


public class DbConnectionForBackEnd {
    private static Connection con;
    private static final File config = new File("src/main/java/DataBase/config");
    private final String linkToDb = "jdbc:mysql://87.13.160.70:3306";
    private String user = "Adriano";
    private String pw = "IngSw2020!";
    private String schema = "Cinemates20Development";
    private final String version = "";

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
            con = DriverManager.getConnection(linkToDb, user, pw);
            System.out.println("Status: " + (!con.isClosed()));
            String command = "use "+schema;
            PreparedStatement st = con.prepareStatement(command);
            st.execute(command);
            return true;
        } catch (Exception ex) {
            //ex.printStackTrace();
            return false;
        }
    }

    public Connection getConnection() {
        return con;
    }

    public void test() {
        //this.recoverPW();
        //this.recoverSchema();
        //this.recoverUser();
        System.out.println(user + " " + pw + " " + schema);
        System.out.println(createConnection());

    }
}