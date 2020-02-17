package aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class AioClient {
    public static void main(String[] args) {
        AioClient aioClient = new AioClient();
        try {
            System.out.println("AIO 用戶端啟動了！可以開始輸入訊息：");
            Scanner scanner = new Scanner(System.in);
            aioClient.write(scanner.next());
            aioClient.read();
        } finally {
            aioClient.close();
        }
    }

    private AsynchronousSocketChannel socketChannel;

    private AioClient() {
        try {
            socketChannel = AsynchronousSocketChannel.open();
            socketChannel.connect(new InetSocketAddress("127.0.0.1", 8888));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void write(String line) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put(line.getBytes(StandardCharsets.UTF_8));
        buffer.flip();
        socketChannel.write(buffer);
    }

    private void read() {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            socketChannel.read(buffer).get(); // read 為異步方法； get 為阻塞方法，等 OS 做完，開發時可不加，練習時可將 get 拿掉試試
            buffer.flip();
            System.out.println("server 端回傳=" + new String(buffer.array(), StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void close() {
        try {
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
