package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class NioClient {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        try (
                SocketChannel channel = SocketChannel.open();
                Scanner scanner = new Scanner(System.in);
        ) {
            InetSocketAddress socket = new InetSocketAddress("127.0.0.1", 8888);
            channel.connect(socket);

            String msg;
            System.out.println("NIO 用戶端啟動了！可以開始輸入訊息：");
            while (true) {
                msg = scanner.nextLine();
                if (!msg.equals("exit")) {
                    buffer.clear();
                    buffer.put(msg.getBytes(StandardCharsets.UTF_8));
                    buffer.flip();
                    channel.write(buffer); // 發送到 server
                    buffer.clear();

                    int rLen = channel.read(buffer);
                    if (rLen != -1) {
                        buffer.flip();
                        byte[] datas = new byte[buffer.remaining()];
                        buffer.get(datas);
                        System.out.println("server 的資料是：" + new String(datas, StandardCharsets.UTF_8));
                        buffer.clear();
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.getMessage();
        }
    }
}
