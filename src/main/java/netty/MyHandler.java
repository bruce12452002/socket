package netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class MyHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("進入 channelRead 方法, ctx為=" + ctx.name());

        ByteBuf buffer = (ByteBuf) msg;
        System.out.println("用戶消息為=" + buffer.toString(CharsetUtil.UTF_8));
        System.out.println("用戶消息為=" + ctx.channel().remoteAddress());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("超齡老木", CharsetUtil.UTF_8)); // 寫並放到 buffer
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close(); // ctx.channel().close(); 也可以
    }
}
