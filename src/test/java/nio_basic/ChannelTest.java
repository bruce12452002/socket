package nio_basic;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;

/**
 * A Channel -----------Buffer----------- B Channel
 * 如上圖，Channel 本身不傳遞資料，A Channel 會將資料放到 buffer，buffer 負責傳遞資料給 B Channel；反之亦然
 * BIO 使用 getChannel()；NIO 使用 open(), Files 使用 newByteChannel()
 */
public class ChannelTest {
    @Test
    public void test1() {
        try (
                FileChannel in = new FileInputStream("D:/63.mp4").getChannel();
                FileChannel out = new FileOutputStream("D:/a.mp4").getChannel()
        ) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (in.read(buffer) != -1) { // 將讀到的資料放到 buffer
                buffer.flip();
                out.write(buffer); // 將 buffer 的資料寫到 out
                buffer.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 和 allocateDirect() 一樣，都是直接 buffer
     */
    @Test
    public void test2() {
        // FileChannel.open 的第一個參數可以用 Path.of 和 Paths.get
        try (
                FileChannel in = FileChannel.open(Path.of("D:/63.mp4"), StandardOpenOption.READ);
                FileChannel out = FileChannel.open(Paths.get("D:/a.mp4"), StandardOpenOption.READ,
                        StandardOpenOption.WRITE, StandardOpenOption.CREATE)
        ) {
            System.out.println("bytes=" + in.size());
            MappedByteBuffer inMap = in.map(FileChannel.MapMode.READ_ONLY, 0, in.size());
            MappedByteBuffer outMap = out.map(FileChannel.MapMode.READ_WRITE, 0, in.size());

            byte[] data = new byte[inMap.limit()];
            inMap.get(data);
            outMap.put(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 還是直接 buffer
     */
    @Test
    public void test3() {
        try (
                FileChannel in = FileChannel.open(Path.of("D:/63.mp4"), StandardOpenOption.READ);
                FileChannel out = FileChannel.open(Paths.get("D:/a.mp4"), StandardOpenOption.READ,
                        StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW)
        ) {
            // 兩者擇其一
            // out.transferFrom(in, 0, in.size()); // out 從 in
            in.transferTo(0, in.size(), out); // in 到 out
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用多個 buffer，可以分散讀取和聚集寫入
     */
    @Test
    public void test4() {
        try (
                FileChannel in = new RandomAccessFile("D:/test.txt", "rw").getChannel();
                FileChannel out = new RandomAccessFile("D:/ttt.txt", "rw").getChannel()
        ) {
            ByteBuffer[] buffers = {ByteBuffer.allocate(10), ByteBuffer.allocate(100)};
            in.read(buffers);

            for (ByteBuffer b : buffers) {
                b.flip();
            }


            for (ByteBuffer b : buffers) {
                System.out.println(new String(b.array(), 0, b.limit()));
                System.out.println("-----------------------");
            }

            out.write(buffers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void charsetTest1() {
        Map<String, Charset> map = Charset.availableCharsets();
        map.forEach((k, v) -> System.out.println(k + "=" + v));
    }

    @Test
    public void charsetTest2() {
        Charset charset = Charset.forName("UTF-8");
        CharsetEncoder encoder = charset.newEncoder();
        CharsetDecoder decoder = charset.newDecoder();

        CharBuffer buffer = CharBuffer.allocate(1024);
        buffer.put("食飽未？");
        buffer.flip();

        try {
            ByteBuffer bb = encoder.encode(buffer);
            for (var i = 0; i < bb.limit(); i++) {
                System.out.println(bb.get());
            }

            buffer.flip();
            CharBuffer decode = decoder.decode(bb);
            System.out.println(decode.toString());

            Charset charset2 = Charset.forName("UTF-8");
            bb.flip();
            System.out.println(charset2.decode(bb));
        } catch (CharacterCodingException e) {
            e.printStackTrace();
        }
    }
}
