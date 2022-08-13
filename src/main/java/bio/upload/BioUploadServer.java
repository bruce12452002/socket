package bio.upload;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.UUID;

public class BioUploadServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("bio upload server 啟動了！");

        while (true) {
            Socket socket = serverSocket.accept(); // 這行寫在迴圈裡可以接收多個客戶端
            new Thread(() -> {
                FileOutputStream out = null;
                try (
                        DataInputStream in = new DataInputStream(socket.getInputStream())
                ) {
                    String fileSuffixName = in.readUTF();
                    // UUID.randomUUID() 是怕多個 Client 傳來的檔名都一樣
                    out = new FileOutputStream("D:/" + UUID.randomUUID() + fileSuffixName);

                    byte[] buffer = new byte[20];
                    int len;
                    while ((len = in.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                    }
                    out.flush();
                    System.out.println("Server 端已保存");
                } catch (IOException e) {
                    System.out.println(socket.getRemoteSocketAddress() + "斷線了");
                    System.out.println(e.getMessage());
                } finally {
                    if (Objects.nonNull(out)) {
                        try {
                            out.close();
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
            }).start();
        }
    }
}
