package spring;


import DataBase.DbConnectionForBackEnd;
import core.Classes.User;
import core.sql.FactoryRecord;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import utility.Json.JSONCreation;

import java.sql.Connection;
import java.sql.SQLException;

@Controller
@EnableAutoConfiguration
public class GeneralController {

    Connection conn;

    @GetMapping("/user")
    @ResponseBody
    public String user(@RequestParam(value = "idUser", defaultValue = "0") String idUser) throws SQLException {
        DbConnectionForBackEnd db = new DbConnectionForBackEnd();
        db.createConnection();
        conn = db.getConnection();
        if (!conn.isClosed()) {
            return JSONCreation.getJSONToCreate(FactoryRecord.getNewIstance(conn).getSingleRecord(conn, User.class, "idUSer=" + idUser));
        }
        return null;
    }
}
