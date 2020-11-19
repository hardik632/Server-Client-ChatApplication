package Main;

import Communication.Client;
import Communication.Server;
import Database.Connect;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.*;
import java.net.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

// Client class
public class ClientService {

    public static Socket s;
    public static int listenport;
    public static String username;
    public static String hostname;
    public static int port;
    static DataInputStream dis = null;
    static DataOutputStream dos = null;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws  IOException, SQLException {
        if (args.length < 4) return;

        hostname = args[0];
        port = Integer.parseInt(args[1]);
        listenport = Integer.parseInt(args[2]);
        username =(args[3]);

        Connect.connect();
        Statement stmt = Connect.conn.createStatement();

        String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS online"
                + "(name varchar(100) ,listenport varchar(100))";
        stmt.executeUpdate(CREATE_TABLE_SQL);

        String insert = "insert into online VALUES(" + "'" + username + "'" + "," +"'" + listenport + "'" + ");";
        stmt.executeUpdate(insert);

        exec();

    }

    public static void exec() throws IOException {
        try {
            s = new Socket(hostname, port);

            s.setSoTimeout(20000);
            Console console = System.console();
            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());
            Timer t = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        dos.writeUTF("sending heartbeat...");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("ARE YOU THERE.... \t type yes or choose any option to continue");
                }
            }, 60000, 60000);

            // the following loop performs the exchange of
            // information between client and client handler
            while (true) {
//                System.out.println(dis.readUTF());
                System.out.println("What do you want?\n\n " +
                        "1. to know the number of online members \n " +
                        "2. to know the information of other users  \n " +
                        "3. to Communicate with others Connection by opening your socket \n " +
                        "4. to Communicate with others Connection by connecting to others \n " +
                        "5. to get the chat history \n " +
                        "6. to terminate the Connection"
                );


                String tosend = sc.nextLine();
//                dos.writeUTF(tosend);
                if(tosend.equals("yes")) {
                    continue;
                }
                if(tosend.equals("1"))
                {
                    dos.writeUTF(username + " wants to know the number of online members");
                    online_users_count();
                }

                if(tosend.equals("2"))
                {
                    dos.writeUTF(username + " wants to know the details of online members");
                    storage();
                }
                if (tosend.equals("3"))
                {
                    t.cancel();
                    dos.writeUTF(username + " decided to open listen port for communication ");
                    Server server = new Server(listenport, username);
                    server.exe();
                    break;
                }
                if (tosend.equals("4")) {
                    t.cancel();
                    dos.writeUTF(username + " decided to do communication by connecting to others listen port  ");
                    int serverport = Integer.parseInt(console.readLine("Enter the port number: "));
                    Client client = new Client(InetAddress.getLocalHost().getHostAddress(), serverport, username);
                    client.exe();

                    break;
                }
                if (tosend.equals("5")) {
                    dos.writeUTF(username + " decided to know the chat history ");
                    chatHistory();
                }
                if (tosend.equals("6")) {
                    dos.writeUTF(username + " is disconnected..... ");
                    remove();
                    t.cancel();
                    break;
                }
//                String received = dis.readUTF();
//                System.out.println(received);
            }

        }

        catch (JSONException | IOException | SQLException e) {
//            s.close();
            e.printStackTrace();
            s.close();dis.close();dos.close();
        }
    }

    private static void remove() throws SQLException, IOException, JSONException {
        Connect.connect();
        Statement stmt = Connect.conn.createStatement();
        String delete = "delete from online where listenport ="+ listenport ;
        stmt.executeUpdate(delete);
        storage();
        System.out.println("Closing this connection : " + s);
        s.close();
        System.out.println("Connection closed");
    }

    private static void chatHistory() throws SQLException {
        Console console = System.console();
        Connect.connect();
        Statement stmt = Connect.conn.createStatement();
       String name = console.readLine("enter client name whose chat history you want to fetch: ");
       String time =console.readLine("enter the data to get the chathistory in form dd/mm/yyyy: ");
        String sql = "select msg from chathistory where name = '"+name +"' and timestamp = '"+ time +"'";
        System.out.println("Getting : "+ sql);
        ResultSet rs = stmt.executeQuery(sql);
        System.out.println("-------------------------------------------------------------------------------");
        while (rs.next()) {
            //Retrieve by column name
//            String username = rs.getString("name");
            String msg = rs.getString("msg");
//            String timestamp = rs.getString("timestamp");

            //Display values
//            System.out.print(" name: " + username);
            System.out.print("| msg: " + msg );
//            System.out.print(", timestamp: " + timestamp);
            System.out.println();

        }
        System.out.println("-------------------------------------------------------------------------------");
        rs.close();
    }

    private static void storage() throws SQLException, JSONException, IOException {
        Connect.connect();
        Statement stmt = Connect.conn.createStatement();

        String sql = "select * from online";
        ResultSet rs = stmt.executeQuery(sql);
        JSONObject jsonObject = new JSONObject();
        //Creating a json array
        JSONArray array = new JSONArray();
        //Inserting ResutlSet data into the json object
        while(rs.next()) {
            JSONObject record = new JSONObject();
            //Inserting key-value pairs into the json object
            record.put("username", rs.getString("name"));
            record.put("Listenport", rs.getString("listenport"));
            array.put(record);
        }
        jsonObject.put("onlineusers", array);
        try {
            FileWriter file = new FileWriter("Client_Data");
            file.write(jsonObject.toString());
            file.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
        StringBuilder clientdata = new StringBuilder();
        FileReader fr = new FileReader("Client_Data");
        int i;
        while ((i = fr.read()) != -1)
            clientdata.append((char) i);
        fr.close();
        System.out.println(clientdata);
    }

    private static void online_users_count() throws SQLException {
        Connect.connect();
        Statement statement =Connect.conn.createStatement();
        String count = "select count(*) from online";
        ResultSet rs =statement.executeQuery(count);
        rs.next();
        int online_Users = rs.getInt(1);
        System.out.println("Number of online users are: " + online_Users);
        rs.close();
    }

}
