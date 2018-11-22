/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import model.SocketMessage;

/**
 *
 * @author nguyen.thi.thu.thuy
 */
public class EchoClientHandler extends SimpleChannelInboundHandler<String>{
    private ChannelHandlerContext ctx;
    private WebSocketClient.Listener socketClientListener;
    
    public EchoClientHandler(WebSocketClient.Listener listener) {
        socketClientListener = listener;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //String message = "message from client yyyyyyyyy.";
        //System.out.println("Sending message: " + message);
        ctx.writeAndFlush("Client Active" + System.lineSeparator());
        this.ctx = ctx;
        socketClientListener.connected();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("Error caught in the communication service: " + cause);
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        //System.out.println("Received message: " + msg);
        SocketMessage sm = SocketMessage.fromJsonString(msg);
        System.out.println("channelRead0: " + sm.toString());
    }
    
    
    public void sendMessage(SocketMessage sm) {
        if (ctx == null) {
            return;
        }
        System.err.println("[sendMessage]SocketMessage: " + sm.toString());
        //try {
            //ctx.writeAndFlush(sm).sync();
            ctx.writeAndFlush(sm.toJsonString() + System.lineSeparator());
        //} catch (InterruptedException ex) {
        //    System.err.println("InterruptedException");
            //Logger.getLogger(WebSocketClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        //}
    }
}
