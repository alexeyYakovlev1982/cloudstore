import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.DefaultChannelPromise;

public class StringToByteBufHandler extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        String str = (String)msg;
        System.out.println(">>> StringToByteBufHandler got string: " + str);

        byte[] arr = str.getBytes();
        ByteBufAllocator al = new PooledByteBufAllocator();
        ByteBuf buf = al.buffer(arr.length);
        buf.writeBytes(arr);

        System.out.println(">>> StringToByteBufHandler transformed string to ByteBuf: " + buf);

        //ctx.writeAndFlush(buf);
        ctx.write(buf);
        ctx.flush();
    }
}
