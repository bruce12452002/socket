package aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.Future;

public class AioServerHandler implements CompletionHandler<AsynchronousSocketChannel, AioServer> {
    @Override
    public void completed(AsynchronousSocketChannel result, AioServer attachment) {
        // 這裡就是下一次的監聽
        attachment.getServerSocketChannel().accept(attachment, this);
        doRead(result);
    }

    @Override
    public void failed(Throwable exc, AioServer attachment) {
        exc.printStackTrace();
    }

    private void doRead(AsynchronousSocketChannel channel) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        /*
         *  read(ByteBuffer dst, A attachment, CompletionHandler<Integer,? super A> handler
         *  dst：客戶端傳資料的緩存，可以不使用
         *  attachment：處理客戶端傳資料的物件
         *  handler： 邏輯寫在這
         */
        channel.read(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {

            @Override
            public void completed(Integer result, ByteBuffer attachment) { // attachment 就是 read 的第二個參數，在這個方法執行時，OS 已將客戶端的資料寫到 buffer 了
                System.out.println("資料長度為=" + result + " ,容量為=" + attachment.capacity());
                attachment.flip();
                System.out.println(new String(attachment.array(), StandardCharsets.UTF_8));
                doWrite(channel); // 寫給客戶端
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                exc.printStackTrace();
            }
        });
    }

    private void doWrite(AsynchronousSocketChannel channel) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put("成功了".getBytes(StandardCharsets.UTF_8));
        buffer.flip();
        channel.write(buffer); // 為異步操作
        // channel.write.get(); // get 為阻塞方法，會等 OS 寫完
    }
}
