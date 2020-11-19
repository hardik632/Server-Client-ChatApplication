package Main;


import java.io.*;
import java.net.Socket;

class ClientHandler extends Thread {

    private final Socket s;
    DataInputStream dis;
    DataOutputStream dos;


    // Constructor
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos) {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }

    @Override
    public void run() {
//        Timer t = new Timer();
//        t.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                System.out.println("ARE YOU THERE....");
//            }
//        }, 15000, 15000);
        while(true){
        try {
            String received=dis.readUTF();
            System.out.println(received);
            if(received.equals("6")){
                System.out.println("Client " + this.s + " sends exit...");
                        System.out.println("Closing this connection.");
                        this.s.close();
                        System.out.println("Connection closed");
                        break;
            }
        } catch (IOException e) {

            try {
                s.close();
                dis.close();
                dos.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

        }}
        //
//        while (true) {
//            Timer timer = new Timer();
//            timer.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    System.out.println("sending heartbeat");
//                    try {
//                        dos.writeUTF("are you alive");
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }, 15000, 15000);
//
//        }

//            try {
////                 Ask user what he wants
//                dos.writeUTF("What do you want?\n\n " +
//                        "1. to know the number of online members \n " +
//                        "2. to know the information of other users  \n " +
//                        "3. to Communicate with others Connection by opening your socket \n " +
//                        "4. to Communicate with others Connection by connecting to others \n " +
//                        "5. to get the chat history \n " +
//                        "6. to terminate the Connection"
//                );
//
////                 receive the answer from client
//                received = dis.readUTF();
//                switch (received) {
//
//                    case "6":
//                        System.out.println("Client " + this.s + " sends exit...");
//                        System.out.println("Closing this connection.");
////                        ServerService.counter--;
//                        this.s.close();
//                        System.out.println("Connection closed");
//                        break;
//                    default:
//                        dos.writeUTF(" ");
//                        break;
//                }
//            } catch (IOException e) {
//                try {
//                    this.s.close();
//                } catch (IOException ioException) {
//                    ioException.printStackTrace();
//                }
////                System.out.println("Disconnected..");
//            }
//        }

    }
}