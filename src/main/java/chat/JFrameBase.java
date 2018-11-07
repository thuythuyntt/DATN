/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import javax.swing.JFrame;

/**
 *
 * @author thuy
 */
public class JFrameBase extends JFrame {

    public void showScreen(JFrame frame) {
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
    }

}
