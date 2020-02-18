package nio_basic;

import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.stream.IntStream;

/**
 * Paths.get(1.7) 和 Path.of(java 11) 差不多，返回的都是 Path，只用 Pahts.get 測試
 * Files(1.7)
 */
public class PathAndFilesTest {
    private Path paths = Paths.get("D:/abc/def/", "ghi/jkl");
    private Path uriPaths = Paths.get(URI.create("file:///aaa/bbb/ccc"));

    @Test
    public void test1() {
        IntStream.range(0, paths.getNameCount()).forEach(i -> System.out.println(paths.getName(i)));
        System.out.println("--------------------");
        IntStream.range(0, uriPaths.getNameCount()).forEach(i -> System.out.println(uriPaths.getName(i)));
    }

    @Test
    public void test2() {
        System.out.println(paths.getRoot() + "---" + paths.getParent() + "---" + paths.getFileName());
        System.out.println(uriPaths.getRoot() + "---" + uriPaths.getParent() + "---" + uriPaths.getFileName());
    }

    @Test
    public void test3() {
        Path paths = Paths.get("D:/qoo/", "xxx/ooo");
        System.out.println(paths.resolve("")); // D:\qoo\xxx\ooo
        System.out.println(paths.resolve("aaa")); // D:\qoo\xxx\ooo\aaa
        System.out.println(paths.resolve("Z:/")); // Z:\
    }

    @Test
    public void test4() {
        try {
            Files.copy(Paths.get("source path"), Paths.get("destination path"), StandardCopyOption.REPLACE_EXISTING);
            Files.deleteIfExists(Paths.get("path"));

            // 移動
            Files.move(Paths.get("source path"), Paths.get("destination path"), StandardCopyOption.ATOMIC_MOVE);

            // 取得附加屬性(創建時間、修改時間…)
            BasicFileAttributes attr = Files.readAttributes(Paths.get("path"), BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);

            // 取得 channel
            SeekableByteChannel seekableByteChannel = Files.newByteChannel(Paths.get("xxx.mp4"), StandardOpenOption.READ);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test5() {
        try (
                // 取得目錄
                DirectoryStream<Path> dir = Files.newDirectoryStream(Paths.get("D:/DesignPattern"));
        ) {
            dir.forEach(d -> System.out.println(d.getFileName())); // 列出目錄下的檔案和目錄名稱
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
