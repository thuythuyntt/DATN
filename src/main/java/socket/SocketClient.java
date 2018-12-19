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
import io.netty.util.CharsetUtil;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.SessionInfo;
import model.SocketMessage;
import model.Student;

public class SocketClient {

    public interface Listener {

        void connected();

        void disconnected(Throwable e);

        void updateOnlineList(List<SessionInfo> list);
        
        void doControlAction(String action, String pcName);
        
        void sendScreenshot();
        
        void receiveScreenshot(String capture);
        
        void receiveNotification(String noti);
        
        void receiveListStudent(List<Student> list);
        
        void receiveListSession(List<SessionInfo> list);
    }

    private SocketChannelHandler handler;

    private static SocketClient instance = null;

    
    private String host = "";
    private int port = 8081;
    
    public static SocketClient getInstance() {
        if (instance == null) {
            instance = new SocketClient();
        }
        return instance;
    }

    public void sendMessage(SocketMessage sm) {
        handler.sendSocketMessage(sm);
    }

    public void setHost(String host) {
        this.host = host;
    }
    
    public void disconnect() {
        handler.disconnect();
    }

    public void connect(Listener listener) {
        try {
//            final String host = "192.168.6.57"; // .6.57 máy mac trên công ty; .4.36 máy tính cá nhân
//            final String host = "192.168.6.111";
//            final String host = "localhost";

            handler = new SocketChannelHandler(listener);

            EventLoopGroup group = new NioEventLoopGroup();
            try {
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(group)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            public void initChannel(SocketChannel ch) throws Exception {
                                ch.pipeline().addLast(
                                        new StringEncoder(CharsetUtil.UTF_8),
                                        new StringDecoder(CharsetUtil.UTF_8),
                                        handler);
                            }
                        });

                System.out.println("conntect to " + host + ":" + port);
                ChannelFuture channelFuture = bootstrap.connect(host, port);//.sync();
                channelFuture.channel().closeFuture();//.sync();
            } finally {
            }
        } catch (Exception ex) {
            Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
            listener.disconnected(ex);
        }
    }
}
