package socket;

import java.util.List;

//import test.handler.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import com.google.gson.Gson;
import model.SocketMessage;

public class MessageToTextFrameHandler extends MessageToMessageEncoder<SocketMessage> {

	@Override
	protected void encode(ChannelHandlerContext ctx, SocketMessage msg, List<Object> out) throws Exception {
		Gson gson = new Gson();
		String json = gson.toJson(msg);
		out.add(new TextWebSocketFrame(json));
	}

}