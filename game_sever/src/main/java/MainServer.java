import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainServer {
    private static final int PROT = 8888;
    private static final Logger LOGGER = LoggerFactory.getLogger(MainServer.class);

    private void start() {
        /*
        接收请求的
         */
        EventLoopGroup bGroup = new NioEventLoopGroup();

        /*
        处理请求的
         */
        EventLoopGroup wGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bGroup, wGroup)
                    /*
                    定义服务端的信道
                     */
                    .channel(NioServerSocketChannel.class)
                    /*
                    添加子处理器
                     */
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(
                            /*
                            Http 服务器编解码器
                             */
                                    new HttpServerCodec(),
                            /*
                            内容长度限制
                             */
                                    new HttpObjectAggregator(65535),
                            /*
                            websocket协议处理
                             */
                                    new WebSocketServerProtocolHandler("/websocket"),
                                    /*
                                    添加自定义处理器
                                     */
                                    new MsgHandler());
                        }
                    });

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            bGroup.shutdownGracefully();
            wGroup.shutdownGracefully();
        }

    }
}
