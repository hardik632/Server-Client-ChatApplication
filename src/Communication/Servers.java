package Communication;

import Main.ClientService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Servers extends Thread {
    Socket clientSocket;
    public Servers(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {

            ReceiveFromClientThread receive = new ReceiveFromClientThread(clientSocket);
            Thread thread = new Thread(receive);
            thread.start();

            SendToClientThread send = new SendToClientThread(clientSocket);
            Thread thread2 = new Thread(send);
            thread2.start();


    }
    class ReceiveFromClientThread implements Runnable {
        Socket clientSocket;
        BufferedReader reader = null;

        public ReceiveFromClientThread(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        public void run() {
            try {
                reader = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));

                String messageString;
                while (true) {
                    while ((messageString = reader.readLine()) != null) {
//                    if (messageString.equals("bye")) {
//                        System.out.println("Disconnected...");
//                        break;
//                    }
                        System.out.println(messageString);//print the message from client
                    }
//                this.clientSocket.close();
//                ClientService.exec();
//                System.exit(0);
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage() + " type bye to exit");
            }
        }
    }

    class SendToClientThread implements Runnable {
        PrintWriter pwPrintWriter;
        Socket clientSock;

        public SendToClientThread(Socket clientSock) {
            this.clientSock = clientSock;
        }

        public void run() {

            try {
                System.out.println("TYPE SOMETHING TO START COMMUNTICATION or bye to end communication");
                System.out.println("first message will go to the client connected first and so on...");
                pwPrintWriter = new PrintWriter(new OutputStreamWriter(this.clientSock.getOutputStream()));//get outputstream
                while (true) {

                    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));//get userinput
                    String msgToClientString = input.readLine();//get message to send to client
                    Date date = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    String timeStamp = formatter.format(date);
                    String insert = "INSERT INTO chathistory" +
                            " VALUES(" + "'" + Server.username + "'" + "," + "'" + msgToClientString + "'" + ",'" + timeStamp + "'" + ");";
                    Server.stmt.executeUpdate(insert);

                    System.out.println(Server.username + ": " + msgToClientString);
                    pwPrintWriter.println("\n" + Server.username + " : " + msgToClientString);//send message to client with PrintWriter
                    pwPrintWriter.flush();//flush the PrintWriter

                    if (msgToClientString.equals("bye"))
                        break;
                }
                clientSock.close();
                ClientService.exec();

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }


}
