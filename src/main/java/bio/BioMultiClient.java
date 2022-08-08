package bio;

import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class BioMultiClient {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("127.0.0.1", 8080);
        OutputStream out = socket.getOutputStream();
        PrintStream ps = new PrintStream(out);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("準備發送：");
            ps.println(scanner.nextLine()); // 解決一行對一行的問題
            ps.flush();
        }
    }
}
