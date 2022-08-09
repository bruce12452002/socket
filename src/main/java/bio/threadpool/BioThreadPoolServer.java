package bio.threadpool;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 和沒有  ThreadPool 就 3 個地方不同，下面有註解
 * 與沒有 ThreadPool 的比較，就差在 ThreadPool 會先準備好 thread，會比較快，但還是 BIO
 */
public class BioThreadPoolServer {
    public static void main(String[] args) throws Exception {
        ExecutorService es = Executors.newFixedThreadPool(2); // 1. 使用 ThreadPool 必需創建的，Client 超過這裡設定的執行緒數會阻塞
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("bio thread pool server 啟動了！");

        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("客戶端還沒連時，這行不會印");

            es.execute(() -> { // 2. 這裡就不是 new Thread 了
                try (
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                ) {
                    String line;
                    while ((line = in.readLine()) != null) {
                        System.out.print("收到了=" + line + " IP=" + socket.getRemoteSocketAddress());
                        System.out.println(" " + Thread.currentThread().getName());
                    }
                } catch (IOException e) {
                    System.out.println(socket.getRemoteSocketAddress() + "斷線了");
                    System.out.println(e.getMessage());
                }
            }); // 3. 因為不是 new Thread，所以就沒有 start 方法了
        }
    }
}
