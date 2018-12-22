/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customview;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 *
 * @author thuy
 */
public class MyPCControllerPopup extends JPopupMenu{
    JMenuItem anItem0;
    JMenuItem anItem1;
    JMenuItem anItem2;
    JMenuItem anItem3;

    OnClick listener;

    public MyPCControllerPopup(OnClick listener) {
        this.listener = listener;

        anItem0 = new JMenuItem("Giám sát màn hình");
        anItem1 = new JMenuItem("Khóa máy");
        anItem2 = new JMenuItem("Tắt máy");
        anItem3 = new JMenuItem("Khởi động lại");
        
        anItem0.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.clickViewer();
            }
        });

        anItem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.clickLockScreen();
            }
        });
        anItem2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.clickShutdown();
            }
        });
        anItem3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.clickRestart();
            }
        });

        add(anItem0);
        add(anItem1);
        add(anItem2);
        add(anItem3);
    }

    public interface OnClick {
        void clickViewer();
        void clickLockScreen();
        void clickShutdown();
        void clickRestart();
    }
}
