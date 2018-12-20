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
        System.out.println(System.getProperty("os.name"));
        try {
            r.exec("shutdown -s -t 10 -c \"Giáo viên yêu cầu tắt máy của bạn sau 10 giây\"");
        } catch (IOException ex) {
            Logger.getLogger(OSCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void restart() {
        try {
            r.exec("shutdown -r");
        } catch (IOException ex) {
            Logger.getLogger(OSCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void lockScreen() {

        try {
            r.exec("C:\\Windows\\System32\\rundll32.exe user32.dll,LockWorkStation");
        } catch (IOException ex) {
            Logger.getLogger(OSCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
