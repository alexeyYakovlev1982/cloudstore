import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ProtocolHandler extends ChannelInboundHandlerAdapter { // (1)
    private int msgLen = -1;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = ((ByteBuf) msg);
        msgLen = buf.readInt();
        byte[] data = new byte[msgLen];
        buf.readBytes(data);
        ServerMessage serverMessage = new ServerMessage(data);

        ctx.write(serverMessage.getMsgReply());
        //ctx.write((ByteBuf)serverMessage.getMsgReplyBytes());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }


}