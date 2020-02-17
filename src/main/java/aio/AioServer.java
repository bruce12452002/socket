package aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AioServer {
    public static void main(String[] args) {
        new AioServer();
    }

    private ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    // private AsynchronousChannelGroup group;
    private AsynchronousServerSocketChannel serverSocketChannel;

    public AsynchronousServerSocketChannel getServerSocketChannel() {
        return serverSocketChannel;
    }

    public AioServer() {
        try {
            System.out.println("aio server 啟動中…");
            /*
                        group = AsynchronousChannelGroup.withThreadPool(service);
                        serverSocketChannel = AsynchronousServerSocketChannel.open(group);
                        */
            serverSocketChannel = AsynchronousServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(8888));
            System.out.println("aio server 啟動了！");

            //  aio 中，監聽用戶的請求後還得開啟下一次的監聽
            serverSocketChannel.accept(this, new AioServerHandler());
            TimeUnit.SECONDS.sleep(1000);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
