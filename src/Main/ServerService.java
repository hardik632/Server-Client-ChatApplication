package Main;

import java.io.*;
import java.net.*;

// Server class
public class ServerService {
    static ServerSocket ss ;
    static Socket s ;

    public static void main(String[] args)  {
        if (args.length < 1) {
            System.out.println("Syntax: java ChatServer <port-number>");
            System.exit(0);
        }
        int port = Integer.parseInt(args[0]);
        try {
            ss = new ServerSocket(port);
//            ss.setSoTimeout(15000);


            System.out.println("Server is listening to port : " + port);
            System.out.println("press ctrl +c to terminate");

            // running infinite loop for getting
            // client request
            while (true) {
                // socket object to receive incoming client requests 
                s = ss.accept();
//                s.setSoTimeout(15000);

                // obtaining input and out streams 
                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                System.out.println("Assigning new thread for this client");
                // create a new thread object

                Thread t = new ClientHandler(s, dis, dos);
                // Invoking the start() method
                t.start();
            }
        }
        catch (IOException e)
        {
//            e.printStackTrace();
            System.out.println(e.getMessage());
//            System.out.println("client is inactive");
//            s.close();
//            ss.close();
        }
    }
}