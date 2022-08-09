package bio.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class BioThreadServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("bio thread server 啟動了！");

        while (true) {
            Socket socket = serverSocket.accept(); // 這行寫在迴圈裡可以接收多個客戶端
            System.out.println("客戶端還沒連時，這行不會印");
            new Thread(() -> {
                try (
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                ) {
                    String line;
                    while ((line = in.readLine()) != null) {
                        System.out.println("收到了=" + line + " IP=" + socket.getRemoteSocketAddress());
                    }
                } catch (IOException e) {
                    System.out.println(socket.getRemoteSocketAddress() + "斷線了");
                    System.out.println(e.getMessage());
                }
            }).start();
        }
    }
}
