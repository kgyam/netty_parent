import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestClient.class);
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 8088;

    public void remote(String msg) {
        /*
        client的group
         */
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            /*
                            添加客户端处理器
                             */
                            ch.pipeline().addLast("decoder", new StringDecoder(CharsetUtil.UTF_8))
                                    .addLast("encoder", new StringEncoder(CharsetUtil.UTF_8))
                                    .addLast(new TestClientHandler());
                        }
                    });
            LOGGER.info("connect : {}:{}", HOST, PORT);
            ChannelFuture f = b.connect(HOST, PORT).sync();
            f.channel().writeAndFlush(msg);
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }


    public static void main(String[] args) {
        TestClient client = new TestClient();
        client.remote("hello world");
    }
}
