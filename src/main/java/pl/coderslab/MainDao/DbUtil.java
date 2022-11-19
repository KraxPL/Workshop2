package pl.coderslab.MainDao;

import java.sql.*;

public class DbUtil {
private static final String DbURL = "jdbc:mysql://localhost:3306?useSSL=false&characterEncoding=utf8&serverTimezone=UTC&allowPublicKeyRetrieval=true";
private static final String DbUser = "root";
private static final String DbPass = "coderslab";

public static Connection connect() throws SQLException{
        return DriverManager.getConnection(DbURL, DbUser, DbPass);
}
}
