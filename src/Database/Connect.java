package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {
    /**
     * Connect to a sample database
     */
    public static Connection conn = null;

    public static void connect() {

        try {
            // db parameters
            String url = "jdbc:sqlite:/home/hardik/Database/chats";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
//            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
//        finally {
//            try {
//                if (conn != null) {
//                    conn.close();
//                }
//            } catch (SQLException ex) {
//                System.out.println(ex.getMessage());
//            }
//        }

    }

//    public static void main(String[] args) {
//        connect();
//    }
}