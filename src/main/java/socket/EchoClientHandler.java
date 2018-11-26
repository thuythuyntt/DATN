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
public class EchoClientHandler extends SimpleChannelInboundHandler<String> {

    private ChannelHandlerContext ctx;
    private WebSocketClient.Listener socketClientListener;

    public EchoClientHandler(WebSocketClient.Listener listener) {
        socketClientListener = listener;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(new SocketMessage("1", "aaaaaaaaaaa").toJsonString());
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
//        SocketMessage sm = SocketMessage.fromJsonString(msg);
//        System.out.println("[channelRead0]: " + sm.toString());
        System.out.println("[channelRead0]: " + msg);
    }

//    public void sendMessage(SocketMessage sm) {
//        if (ctx == null) {
//            return;
//        }
//        System.err.println("Client [sendMessage]: " + sm.toString());
//        ctx.writeAndFlush(sm.toJsonString() + System.lineSeparator());
//    }
}
