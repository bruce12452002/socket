package bio.thread;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class BioThreadServer {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(8888);
        System.out.println("bio thread server 啟動了！");

        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("客戶端還沒連時，這行不會印");
            new Thread(() -> {
                try (
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                        PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
                ) {
                    String rMsg;
                    while (true) {
                        System.out.println("bio thread server 讀");
                        if ((rMsg = in.readLine()) != null) {
                            out.println(rMsg + "哈哈");
                            out.flush();
                        } else {
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.getMessage();
                }
            }).start();
        }
    }
}
