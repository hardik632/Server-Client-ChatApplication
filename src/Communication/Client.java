package Communication;

import Database.Connect;
import Main.ClientService;

import java.io.*;
import java.net.*;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Client {
    private final String hostname;
    private final int port;
    public static String username;
    public static Statement stmt = null;

    public Client(String hostname, int port , String username)
    {
        this.hostname=hostname;
        this.port =port;
        Client.username =username;

    }

    public void exe() throws IOException {
        try {
            Socket sock = new Socket(hostname, port);
//            sock.setSoTimeout(20000);

            Connect.connect();
            stmt = Connect.conn.createStatement();
            String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS chathistory"
                    + "(name varchar(100) , msg varchar(100) , timestamp varchar(100))";
            stmt.executeUpdate(CREATE_TABLE_SQL);

            SendToServerThread sendThread = new SendToServerThread(sock);
            Thread thread = new Thread(sendThread);
            thread.start();

            ReceiveFromServerThread receiveThread = new ReceiveFromServerThread(sock);
            Thread thread2 = new Thread(receiveThread);
            thread2.start();
        }
        catch (Exception e)
        {
            System.out.println("enter correct port number");
            ClientService.exec();
            System.out.println(e.getMessage());
        }
    }
}

class ReceiveFromServerThread implements Runnable {
    Socket sock;
    BufferedReader reader = null;

    public ReceiveFromServerThread(Socket sock) {
        this.sock = sock;
    }

    public void run() {

        try {
            reader = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));//get inputstream
            String messageString;
            while (true) {
                while ((messageString = reader.readLine()) != null) {
//                    if (messageString.equals("bye")) {
//                        System.out.println("Disconnected");
//                        break;
//                    }
                    System.out.println(messageString);//print the message from server
                }

//                this.sock.close();
//                ClientService.exec();
//                System.exit(0);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage() + " type bye to exit");
        }
    }
}

class SendToServerThread implements Runnable {
    Socket serversocket;
    PrintWriter print = null;
    BufferedReader input = null;

    public SendToServerThread(Socket sock) {
        this.serversocket = sock;
    }//end constructor

    public void run() {
        try {
            if (serversocket.isConnected()) {
                System.out.println("Client connected to " + serversocket.getInetAddress() + " on port " + serversocket.getPort());
                System.out.println("TYPE SOMETHING TO START Communication or bye to end communication");
                this.print = new PrintWriter(serversocket.getOutputStream(), true);
                while (true) {
                    input = new BufferedReader(new InputStreamReader(System.in));
                    String msgtoServerString ;
                    msgtoServerString = input.readLine();

                    Date date = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    String timeStamp = formatter.format(date);
                    String insert = "INSERT INTO chathistory" +
                            " VALUES(" + "'" + Client.username + "'" + "," +"'" + msgtoServerString + "'" + ",'" + timeStamp + "'" + ");";
                    Client.stmt.executeUpdate(insert);

                    System.out.println(Client.username + ": " +msgtoServerString);
                    this.print.println("\n"+Client.username + " : " +msgtoServerString);
                    this.print.flush();

                    if (msgtoServerString.equals("bye"))
                        break;
                }
                serversocket.close();
                ClientService.exec();
            }
        } catch (Exception e) {

            System.out.println(e.getMessage());
        }
    }
}