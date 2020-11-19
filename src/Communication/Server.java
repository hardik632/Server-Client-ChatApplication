package Communication;

import Database.Connect;

import java.io.*;
import java.net.*;
import java.lang.*;
import java.sql.SQLException;
import java.sql.Statement;

public class Server {

    public int port;
    public static String username;
    public static Statement stmt = null;
    public static Socket clientSocket;

    public Server(int port, String username) {
        this.port = port;
        Server.username = username;
    }

    public void exe() throws IOException, SQLException {

        System.out.println(username + " waiting for connection on port " + port);
        ServerSocket ss = new ServerSocket(port);
        while (true) {
            try {
                clientSocket = ss.accept();
//                clientSocket.setSoTimeout(20000);
//                System.out.println("Received connection from " + clientSocket.getInetAddress() + " on port " + clientSocket.getLocalPort());
                System.out.println("CONNECTED......");
                Connect.connect();
                stmt = Connect.conn.createStatement();
                String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS chathistory" +
                        "(name varchar(100), msg varchar(100) , timestamp varchar(100))";
                stmt.executeUpdate(CREATE_TABLE_SQL);

                Thread t = new Servers(clientSocket);
                t.start();

            } catch (Exception e) {
                e.printStackTrace();
            }

            //create two threads to send and receive from client
//            ReceiveFromClientThread receive = new ReceiveFromClientThread(clientSocket);
//            Thread thread = new Thread(receive);
//            thread.start();
//
//            SendToClientThread send = new SendToClientThread(clientSocket);
//            Thread thread2 = new Thread(send);
//            thread2.start();
        }
    }

}
