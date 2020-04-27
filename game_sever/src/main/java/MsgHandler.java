import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MsgHandler extends SimpleChannelInboundHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(MsgHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        LOGGER.debug("msg : {}", msg);

        /*
         WebSocket 二进制消息会通过 HttpServerCodec 解码成 BinaryWebSocketFrame 类对象
         */
        BinaryWebSocketFrame frame = (BinaryWebSocketFrame) msg;
        ByteBuf byteBuf = frame.content();
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);

        LOGGER.info("msg is :");
        for (byte b : bytes) {
            LOGGER.info(String.valueOf(b));
            LOGGER.info(",");
        }
    }
}
