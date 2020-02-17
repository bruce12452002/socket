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
        ExecutorService es = Executors.newFixedThreadPool(50); // 1. 使用 ThreadPool 必需創建的
        ServerSocket serverSocket = new ServerSocket(8888);
        System.out.println("bio thread pool server 啟動了！");

        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("客戶端還沒連時，這行不會印");

            es.execute(() -> { // 2. 這裡就不是 new Thread 了
                try (
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                        PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
                ) {
                    String rMsg;
                    while (true) {
                        System.out.println("bio thread pool server 讀");
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
            }); // 3. 因為為是 new Thread，所以就沒有 start 方法了
        }
    }
}
