package Client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class PrincipalClient extends Thread {
    private static final int portNumber = 5000;
    private static final String address = "127.0.0.1";

    private String name;
    private final int methodCount = 8;

    public void run() {
        try {
            Socket socket = new Socket(address, portNumber);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            out.println(this.name);
            for (int i = 0; i < methodCount; i++) {
                out.println(this.name + " " + i);
                String res = in.readLine();
                msg(res);
                if (res.equals("GO Home"))
                    break;
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setNames(String name) {
        this.name = name;
    }

    public void msg(String m) {
        System.out.println("[" + System.currentTimeMillis() + "] " + this.name + ": " + m);
    }

}
