package DataBase;

import info.movito.themoviedbapi.*;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import info.movito.themoviedbapi.model.people.Person;
import info.movito.themoviedbapi.model.people.PersonCredit;
import info.movito.themoviedbapi.model.people.PersonCredits;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;


public class DbConnectionForBackEnd {
    private static Connection con;
    private static final File config = new File("src/main/java/DataBase/config");
    private final String linkToDb = "jdbc:mysql://192.168.1.202:3306";
    private String user = "";
    private String pw = "";
    private String schema = "";
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

    private void constructLink() {

    }

    public boolean createConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(linkToDb, user, pw);
            System.out.println("Status: " + (!con.isClosed()));
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
        this.recoverPW();
        this.recoverSchema();
        this.recoverUser();
        System.out.println(user + " " + pw + " " + schema);
        System.out.println(createConnection());

    }
}
