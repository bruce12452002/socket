package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Iterator;
import java.util.Scanner;

public class NioServer implements Runnable {
    private Selector selector; // 選擇器，注冊通道用
    private ByteBuffer inBuffer = ByteBuffer.allocate(1024); // 單位為 Byte
    private ByteBuffer outBuffer = ByteBuffer.allocate(1024);

    public static void main(String[] args) {
        new Thread(new NioServer()).start();
    }

    public NioServer() {
        try {
            System.out.println("nio server 啟動中…");
            this.selector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false); // 是否阻塞，預設就是 false(非阻塞)
            serverSocketChannel.bind(new InetSocketAddress(8888)); // SocketAddress 為抽象類，使用它的子類
            SelectionKey keys = serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);// OP_ACCEPT 為可接受用戶端連線
            System.out.println("nio server 啟動了！");
        } catch (IOException e) {
            e.getMessage();
        }
    }

    @Override
    public void run() {
        try {
            while (selector.select() > 0) { // 如果有 selectedKeys
                Iterator<SelectionKey> it = this.selector.selectedKeys().iterator();
                while (it.hasNext()) {
                    SelectionKey key = it.next();

                    if (key.isValid()) { // 通道有效
                        try {
                            if (key.isAcceptable()) { // 如果是 SelectionKey.OP_ACCEPT 狀態
                                // key.channel 回傳 SelectableChannel，是 ServerSocketChannel 的父類，但沒有 accept 方法
                                ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                                SocketChannel socketChannel = channel.accept(); // 阻塞方法
                                socketChannel.configureBlocking(false);
                                socketChannel.register(this.selector, SelectionKey.OP_READ);
                            }

                            if (key.isConnectable()) {// 如果是 SelectionKey.OP_CONNECT 狀態，可連接

                            }

                            if (key.isReadable()) {// 如果是 SelectionKey.OP_READ 狀態
                                this.inBuffer.clear();
                                // key.channel 回傳 SelectableChannel，是 SocketChannel 的父類，但沒有 read 方法
                                SocketChannel channel = (SocketChannel) key.channel();
                                int length = channel.read(inBuffer); //  將通道的資料讀到 Buffer，以後就針對 Buffer 操作

                                if (length != -1) {
                                    this.inBuffer.flip();
                                    byte[] datas = new byte[this.inBuffer.remaining()];
                                    this.inBuffer.get(datas);
                                    System.out.println("客戶端 IP：" + channel.getRemoteAddress() + "，資料為：" + new String(datas, StandardCharsets.UTF_8));
                                    channel.register(this.selector, SelectionKey.OP_WRITE);
                                    this.inBuffer.clear();
                                } else {
                                    key.channel().close();
                                    key.cancel();
                                    return;
                                }
                            }

                            if (key.isWritable()) {// 如果是 SelectionKey.OP_WRITE 狀態
                                this.outBuffer.clear();
                                // key.channel 回傳 SelectableChannel，是 SocketChannel 的父類，但沒有 write 方法
                                SocketChannel channel = (SocketChannel) key.channel();
                                Scanner scanner = new Scanner(System.in);

                                System.out.println("準備輸入訊息…");
                                String msg = scanner.nextLine();
                                this.outBuffer.put(msg.getBytes(StandardCharsets.UTF_8)); // 將寫入的訊息放到 Buffer 裡
                                this.outBuffer.flip();
                                channel.write(this.outBuffer);
                                channel.register(this.selector, SelectionKey.OP_READ);
                                this.outBuffer.clear();
                            }

                            it.remove(); // 下次還會拿到 SelectionKey 的狀態，所以這裡必需移除
                        } catch (CancelledKeyException e) {
                            e.printStackTrace();
                            key.cancel();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.getMessage();
        }
    }
}
