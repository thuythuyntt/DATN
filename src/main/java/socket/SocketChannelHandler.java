/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.List;
import model.ClientInfo;
import model.SocketMessage;

/**
 *
 * @author nguyen.thi.thu.thuy
 */
public class SocketChannelHandler extends SimpleChannelInboundHandler<String> {

    private ChannelHandlerContext ctx;
    private SocketClient.Listener socketClientListener;

    public SocketChannelHandler(SocketClient.Listener listener) {
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
            List<ClientInfo> list = sm.getListOnline();
            socketClientListener.updateOnlineList(list);
        } else if (sm.getId().startsWith("CTL_")) {
            socketClientListener.doControlAction(sm.getId(), sm.getClientInfo().getPcname());
        }
    }

    public void sendSocketMessage(SocketMessage sm) {
        if (ctx == null) {
            System.out.println("[sendSocketMessage] but ctx null");
            return;
        }
        if (sm.getClientInfo()!= null && sm.getId().equals(SocketMessage.CONNECT)) {
            sm.getClientInfo().setIpAddress(ctx.channel().localAddress().toString());
        }
        System.out.println("[sendSocketMessage]: " + sm.getId());
        ctx.writeAndFlush(sm.toJsonString() + System.lineSeparator());
    }
    
    public void disconnect() {
        ctx.disconnect();
    }
}
