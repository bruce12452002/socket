package bio.upload;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.net.Socket;

public class BioUploadClient {
    public static void main(String[] args) throws Exception {
        try (FileInputStream in = new FileInputStream("D:/123.txt")) {
            Socket socket = new Socket("127.0.0.1", 8080);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(".txt");

            byte[] buffer = new byte[20];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
            // 如果 Server 端不想有 Exception，可以加 shutdownOutput
            if (!socket.isOutputShutdown()) {
                socket.shutdownOutput();
            }
            System.out.println("客戶端結束");
        }
    }
}
