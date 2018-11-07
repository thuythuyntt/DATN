/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customview;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 *
 * @author thuy
 */
public class MyAvatarPopup extends JPopupMenu {

    JMenuItem anItem1;
    JMenuItem anItem2;
    JMenuItem anItem3;

    OnClick listener;

    public MyAvatarPopup(OnClick listener) {
        this.listener = listener;

        anItem1 = new JMenuItem("Xem thông tin cá nhân");
        anItem2 = new JMenuItem("Đổi mật khẩu");
        anItem3 = new JMenuItem("Đăng xuất");

        anItem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.clickShowPersonInformation();
            }
        });
        anItem2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.clickChangePassword();
            }
        });
        anItem3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.clickLogout();
            }
        });

        add(anItem1);
        add(anItem2);
        add(anItem3);
    }

    public interface OnClick {
        void clickShowPersonInformation();
        void clickChangePassword();
        void clickLogout();
    }
}
