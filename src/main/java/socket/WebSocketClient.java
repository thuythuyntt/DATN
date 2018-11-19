package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.SocketMessage;

public class WebSocketClient {
    
    public interface Listener {
        void connected();
    }
    
    private Listener listener;
    private WebSocketClientHandler handler;
    
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
            try {
                handler = new WebSocketClientHandler(WebSocketClientHandshakerFactory.newHandshaker(
                                        uri, WebSocketVersion.V13, null, false, new DefaultHttpHeaders()), listener);

                Bootstrap b = new Bootstrap();
                b.group(group)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) {
                                ChannelPipeline p = ch.pipeline();
                                p.addLast(
                                        new HttpClientCodec(),
                                        new HttpObjectAggregator(8192),
                                        handler);
                            }
                        });

                Channel ch = b.connect(uri.getHost(), port).sync().channel();
                handler.handshakeFuture().sync();
//                BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
//                while (true) {
//                    String msg;
//                    msg = console.readLine();
//
//                    if (msg == null) {
//                        break;
//                    } else if ("bye".equals(msg.toLowerCase())) {
//                        ch.writeAndFlush(new CloseWebSocketFrame());
//                        ch.closeFuture().sync();
//                        break;
//                    } else if ("ping".equals(msg.toLowerCase())) {
//                        WebSocketFrame frame = new PingWebSocketFrame(Unpooled.wrappedBuffer(new byte[]{8, 1, 8, 1}));
//                        ch.writeAndFlush(frame);
//                    } else {
//                        String jsonExample = "{\"foo\" : \"foo\", \"bar\" : \"bar\"}";
//                        WebSocketFrame frame = new TextWebSocketFrame(jsonExample);
//                        ch.writeAndFlush(frame);
//                    }
//                }
            } finally {
                //group.shutdownGracefully();
            }
        } catch (URISyntaxException ex) {
            Logger.getLogger(WebSocketClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(WebSocketClient.class.getName()).log(Level.SEVERE, null, ex);
        } //catch (IOException ex) {
        //    Logger.getLogger(WebSocketClient.class.getName()).log(Level.SEVERE, null, ex);
        //}
    }

}
