/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customview;

import customview.MyAvatarPopup.OnClick;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author thuy
 */
public class MyMouseListener extends MouseAdapter {

    private OnClick listener;

    public MyMouseListener(OnClick listener) {
        this.listener = listener;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger()) {
            showMyPopup(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
            showMyPopup(e);
        }
    }

    private void showMyPopup(MouseEvent e) {
        MyAvatarPopup menu = new MyAvatarPopup(listener);
        menu.show(e.getComponent(), e.getX(), e.getY());
    }

}
