package main;

import chat.JFTrangChu;
import chat.JFrameBase;
import dangnhap.JFDangNhap;
import firebasedb.FirebaseHelper;
import util.Constants;
import util.TokenFile;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author thuy
 */
public class Main {

    public static void main(String[] args) {
        System.out.println(System.getProperty("os.name"));
        
        String token = TokenFile.readFile(Constants.TMP_FILE_NAME);
        if (FirebaseHelper.getInstance().checkAutoLogin(token)) {
            JFTrangChu view = new JFTrangChu();
            view.setVisible(true);
            view.setResizable(false);
            view.setLocationRelativeTo(null);
        } else {
            JFDangNhap view = new JFDangNhap();
            view.setVisible(true);
            view.setResizable(false);
            view.setLocationRelativeTo(null);
        }
    }
}
