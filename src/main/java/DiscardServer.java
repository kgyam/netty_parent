import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiscardServer {

    private static final int PROT = 1234;
    private static final Logger LOGGER = LoggerFactory.getLogger(DiscardServer.class);

    public DiscardServer() {
    }


    public void run(){
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new DiscardServerHandler());
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture cf = serverBootstrap.bind(PROT).sync();

            cf.channel().closeFuture().sync();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();

        }

    }



    public static void main(String[] args) {

        new DiscardServer().run();
    }
}
