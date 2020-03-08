package netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class MyNettyServer {
    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {

            ServerBootstrap bootstrap = new ServerBootstrap(); // 會繼承  extends AbstractBootstrap<ServerBootstrap, ServerChannel> ，channel 方法就必需是 ServerChannel (含子類)
            bootstrap.group(bossGroup, workerGroup) // group 是重載方法，有一個和兩個參數的，兩個是父子關係的ServerBootstrap
                    .channel(NioServerSocketChannel.class) // 用 NioServerSocketChannel 作為 Server 的通道
                    .option(ChannelOption.SO_BACKLOG, 128) // Thread 隊列得到的連接個數
                    .childOption(ChannelOption.SO_KEEPALIVE, true) // 長連接
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new MyHandler()); // 在管道的最後加上 Handler
                        }
                    });

            ChannelFuture channelFuture = bootstrap.bind(6666).sync();
            System.out.println("server is ready!!");

            // 對關閉通道進行監聽，只有有關通道的消息或事件才會處理，並不是一啟動就關閉
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
