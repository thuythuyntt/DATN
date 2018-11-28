/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.HashMap;
import java.util.List;
import model.ClientInfo;
import model.SocketMessage;

/**
 *
 * @author nguyen.thi.thu.thuy
 */
public class EchoClientHandler extends SimpleChannelInboundHandler<String> {

    private ChannelHandlerContext ctx;
    private SocketClient.Listener socketClientListener;

    public EchoClientHandler(SocketClient.Listener listener) {
        socketClientListener = listener;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
        socketClientListener.connected();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("Error caught in the communication service: " + cause);
        ctx.close();
        socketClientListener.disconnected(cause);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        SocketMessage sm = SocketMessage.fromJsonString(msg);
        if (sm == null) {
            System.out.println("[channelRead0] but SocketMessage null");
            return;
        }
        System.out.println("[channelRead0]: " + sm.getId());
        if (SocketMessage.SET_LIST_ONINE.equals(sm.getId())) {
            HashMap<String, ClientInfo> list = sm.getListOnline();
            System.err.println("SET_LIST_ONINE list:" + list == null ? "null" : list.size());
        }
        //System.out.println("[channelRead0]: " + msg);
    }

    public void sendSocketMessage(SocketMessage sm) {
        if (ctx == null) {
            System.out.println("[sendSocketMessage] but ctx null");
            return;
        }
        System.out.println("[sendSocketMessage]: " + sm.getId());
        ctx.writeAndFlush(sm.toJsonString() + System.lineSeparator());
    }
}
