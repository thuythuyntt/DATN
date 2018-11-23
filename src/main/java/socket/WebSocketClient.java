package socket;

import java.net.URI;

import io.netty.bootstrap.Bootstrap;
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

public class WebSocketClient {

    public interface Listener {

        void connected();
    }

    private Listener listener;
    private EchoClientHandler handler;

    public WebSocketClient(Listener listener) {
        this.listener = listener;
    }

    public void sendMessage(SocketMessage sm) {
        handler.sendMessage(sm);
    }

    public void connect(String webSocketUrl) {
        try {
            URI uri = null;

            uri = new URI(webSocketUrl);

            String scheme = uri.getScheme() == null ? "ws" : uri.getScheme();
            final String host = uri.getHost() == null ? "192.168.4.36" : uri.getHost();
            final int port;
            if (uri.getPort() == -1) {
                if ("ws".equalsIgnoreCase(scheme)) {
                    port = 80;
                } else if ("wss".equalsIgnoreCase(scheme)) {
                    port = 443;
                } else {
                    port = -1;
                }
            } else {
                port = uri.getPort();
            }

            if (!"ws".equalsIgnoreCase(scheme) && !"wss".equalsIgnoreCase(scheme)) {
                System.err.println("Only WS(S) is supported.");
                return;
            }

            EventLoopGroup group = new NioEventLoopGroup();
            handler = new EchoClientHandler(listener);
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

                System.out.println("conntect to " + host + ":" + port + " ...");
                bootstrap.connect(host, port);
                System.out.println("Message sent successfully.");
            } finally {
                System.out.println("shutdownGracefully");
//                group.shutdownGracefully();
            }
        } catch (Exception ex) {
            Logger.getLogger(WebSocketClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
