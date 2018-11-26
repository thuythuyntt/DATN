/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socket;

import model.OnlineMessage;
import model.SocketMessage;

/**
 *
 * @author nguyen.thi.thu.thuy
 */
public class SocketHelper {

    private static SocketHelper instance = null;

    public static SocketHelper getInstance() {
        if (instance == null) {
            instance = new SocketHelper();
        }
        return instance;
    }

    private SocketClient skClient;

    private SocketHelper() {
    }

    public void connectServer(OnlineMessage om, SocketClient.Listener listener) {
        skClient = new SocketClient(listener);
        skClient.connect();
        sendOnlineMessageToServer(om);
    }

    public void sendOnlineMessageToServer(OnlineMessage om) {
        if (skClient == null) {
            return;
        }

        skClient.sendMessage(om);
    }
}
