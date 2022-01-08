// Author: Fei Zhai
// Date: 04 / 2021

package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServer {
    public static final int PORT = 5000;
    public static long time;


    public static void main(String[] args) throws IOException {
        // write your code here
        ServerSocket server = new ServerSocket(PORT);
        msg("Main Server starting ...Listen To Port 5000");
        while (true) {
            try {
                Socket socket = server.accept();
                time = System.currentTimeMillis();
                msg("A new client is connecting, SubServerThread Created to Communicate");

                SubServerThread subServerThread = new SubServerThread(socket);
                subServerThread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void msg(String m) {
        System.out.println("[" + System.currentTimeMillis() + "] Main Thread: " + m);
    }
}

