package socket;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.ClientInfo;
import model.SocketMessage;

public class SocketClient {

    public interface Listener {

        void connected();

        void disconnected(Throwable e);

        void updateOnlineList(List<ClientInfo> list);
    }

    //private Listener listener;
    private EchoClientHandler handler;

//    public SocketClient(Listener listener) {
//        this.listener = listener;
//    }
    private static SocketClient instance = null;

    public static SocketClient getInstance() {
        if (instance == null) {
            instance = new SocketClient();
        }
        return instance;
    }

    public void sendMessage(SocketMessage sm) {
        handler.sendSocketMessage(sm);
    }

    public void connect(Listener listener) {
        //this.listener = listener;
        try {
//            final String host = "192.168.4.36";
            final String host = "192.168.6.111";
//            final String host = "localhost";
            final int port = 8080;

//            handler = new EchoClientHandler(listener); 
            handler = new EchoClientHandler(listener);

            EventLoopGroup group = new NioEventLoopGroup();
            try {
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(group)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            public void initChannel(SocketChannel ch) throws Exception {
                                ch.pipeline().addLast(
                                        new StringEncoder(),
                                        new StringDecoder(),
                                        handler);
                            }
                        });

                System.out.println("conntect to " + host + ":" + port);
//                bootstrap.connect(host, port);
                ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
                channelFuture.channel().closeFuture().sync();
            } finally {
                System.out.println("shutdownGracefully");
//                group.shutdownGracefully().sync();
            }
        } catch (Exception ex) {
            Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
            listener.disconnected(ex);
        }
    }

}
