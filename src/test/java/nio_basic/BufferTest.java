package nio_basic;

import org.junit.Ignore;
import org.junit.Test;

import java.nio.ByteBuffer;

public class BufferTest {
    private ByteBuffer buffer = ByteBuffer.allocate(1024); // allocateDirect() 為直接 buffer，將 buffer 放在 OS 中，能增加效能
    private String data = "abcde";

    @Test
    @Ignore
    public void test() {
        // 在 buffer 裡，只有 pos 到 limit 是可以用的, cap 是 buffer 大小，不會變
        // XxxBuffer, 八大基本型態，除了 boolean 以外，七個都有
        displayBuffer(buffer); // pos=0 lim=1024 cap=1024

        buffer.put(data.getBytes());
        displayBuffer(buffer); // pos=5 lim=1024 cap=1024

        buffer.flip(); // 翻轉，5-1024 翻轉了之後，就變成 0-5 了，在 buffer 裡非常常用
        displayBuffer(buffer); // pos=0 lim=5 cap=1024

        buffer.rewind(); // 倒轉，回上一次，表示可重覆讀
        displayBuffer(buffer); // pos=0 lim=5 cap=1024

        buffer.clear(); // 清空 buffer，但實際上資料還在，只是不好控制了
        displayBuffer(buffer); // pos=0 lim=1024 cap=1024
        System.out.println((char) buffer.get(1)); // b
    }

    @Test
    public void compactTest() {
        // 如果 Buffer 中有未讀取的資料，而且想稍後再讀取，但是您需要先進行一些寫操作，那就呼叫 compact()
        displayBuffer(buffer); // pos=0 lim=1024 cap=1024

        buffer.put("abcdefghijklmnopqrstuvwxyz".getBytes());
        displayBuffer(buffer); // pos=26 lim=1024 cap=1024

        buffer.flip();
        displayBuffer(buffer); // pos=0 lim=26 cap=1024

        byte[] barray = new byte[buffer.limit()];

        System.out.println(buffer.get(barray, 0, 2)); // pos=2 lim=26 cap=1024

        buffer.compact(); // 將 position 設成最後一個未讀元素後面，所以是 26-2, limit 變成 capacity，而已讀元素就不見了
        // buffer 已準備好進行寫入，但是不會覆蓋未讀取的資料
        displayBuffer(buffer); // pos=24 lim=1024 cap=1024

        buffer.put("123".getBytes());
        displayBuffer(buffer); // pos=27 lim=1024 cap=1024

        buffer.flip();
        displayBuffer(buffer); // pos=0 lim=27 cap=1024

        for (var i = 0; i < buffer.limit(); i++) {
            System.out.println((char) buffer.get(i));
        }
    }


    @Test
    public void markTest() {
        // mark 會記錄當下的 position 位置，然後可用 reset 還原到 mark 的位置，預設為 -1
        displayBuffer(buffer); // pos=0 lim=1024 cap=1024

        buffer.put(data.getBytes());
        displayBuffer(buffer); // pos=5 lim=1024 cap=1024

        buffer.flip();
        displayBuffer(buffer); // pos=0 lim=5 cap=1024

        byte[] barray = new byte[buffer.limit()];

        System.out.println(buffer.get(barray, 0, 2)); // pos=2 lim=5 cap=1024

        buffer.mark(); // 記錄當下的 position 位置

        System.out.println(buffer.get(barray, 2, 2)); // pos=4 lim=5 cap=1024
        // System.out.println(new String(barray, 2, 2)); // cd

        buffer.reset(); // pos 會回覆到 2 的位置
        displayBuffer(buffer); // pos=2 lim=5 cap=1024

        if (buffer.hasRemaining()) {
            System.out.println(buffer.remaining()); // 3，limit - position
        }
    }

    private void displayBuffer(ByteBuffer buffer) {
        // mark <= position <= limit <= capacity
        System.out.println(buffer.position() + ":" + buffer.limit() + ":" + buffer.capacity());
        System.out.println("-----------------------");
    }
}
