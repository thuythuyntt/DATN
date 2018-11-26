/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socket;

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

    private WebSocketClient skClient;

    private SocketHelper() {
    }

    public void connectServer(WebSocketClient.Listener listener) {
        skClient = new WebSocketClient(listener);
        skClient.connect();
    }

//    public void sendMessageToServer(SocketMessage sm) {
//        if (skClient == null) {
//            return;
//        }

//        skClient.sendMessage(sm);
//    }
}
