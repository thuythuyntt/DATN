/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thuy
 */
public class OSCommand {

    public static Runtime r = Runtime.getRuntime();

    public static void shutdown() {
        try {
            String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                r.exec("shutdown -s -t 10 -c \"Giáo viên yêu cầu tắt máy của bạn sau 10 giây\"");
            } else if (os.contains("Mac OS")) {
                r.exec("shutdown -h +10");
            } else if (os.contains("Linux")) {
                r.exec("shutdown -h +10 \"Giáo viên yêu cầu tắt máy của bạn sau 10 giây\"");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void restart() {
        try {
            String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                r.exec("shutdown -r");
            } else if (os.contains("Mac OS")) {
                r.exec("shutdown -r now");
            } else if (os.contains("Linux")) {
                r.exec("shutdown -r now");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void lockScreen() {
        try {
            String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                r.exec("C:\\Windows\\System32\\rundll32.exe user32.dll,LockWorkStation");
            } else if (os.contains("Mac OS")) {
                r.exec("pmset displaysleepnow");
            } else if (os.contains("Linux")) {
                r.exec("gnome-screensaver-command -l");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
