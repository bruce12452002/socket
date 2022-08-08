package bio.basic;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class BioServer {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("server 已啟動");
        Socket socket = serverSocket.accept();
        System.out.println("客戶端還沒連時，這行不會印");
        InputStream in = socket.getInputStream();

//        byte[] buffer = new byte[100];
//        int len;
////        while ((len = in.read(buffer)) != -1) {
//        if ((len = in.read(buffer)) != -1) {
//            System.out.println("收到了=" + len);
//        }


        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = br.readLine()) != null) {
//        if ((line = br.readLine()) != null) { // server 和 client 為 一行對一行
            System.out.println("收到了=" + line);
        }
        System.out.println("完美結束");
    }
}
