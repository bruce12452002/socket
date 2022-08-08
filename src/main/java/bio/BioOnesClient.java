package bio;

import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

public class BioOnesClient {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("127.0.0.1", 8080);
        OutputStream out = socket.getOutputStream();
        PrintStream ps = new PrintStream(out);
        ps.print("I'm bio client"); // server 和 client 為 一次對一次
//        ps.println("I'm bio client"); // server 和 client 為 一行對一行
        ps.flush();
        System.out.println("客戶端結束");
    }
}
