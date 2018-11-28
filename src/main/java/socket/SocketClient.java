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
import java.util.logging.Level;
import java.util.logging.Logger;
import model.SocketMessage;

public class SocketClient {

//    public interface Listener {
//
//        void connected();
//    }

//    private Listener listener;
    private EchoClientHandler handler;

//    public SocketClient(Listener listener) {
//        this.listener = listener;
//    }

    public void sendMessage(SocketMessage sm) {
        handler.sendOnlineMessage(sm);
    }

    public void connect() {
        try {
            final String host = "192.168.4.36";
//            final String host = "localhost";
            final int port = 8080;
            
//            handler = new EchoClientHandler(listener); 
            handler = new EchoClientHandler(); 

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
        }
    }

}
