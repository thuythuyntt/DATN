/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JEditorPane;

/**
 *
 * @author thuy
 */
public class TokenFile {

    public static int getContentHeight(int width, String content) {
        JEditorPane dummyEditorPane = new JEditorPane();
        dummyEditorPane.setSize(width, Short.MAX_VALUE);
        dummyEditorPane.setText(content);
        return dummyEditorPane.getPreferredSize().height;
    }

    public static String hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashInBytes = md.digest(input.getBytes(StandardCharsets.US_ASCII));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashInBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            return input;
        }
    }

    public static void saveFile(String content, String filename) {
        String dir = System.getProperty("java.io.tmpdir");
        String filepath = dir + File.separator + filename;
        try {
            File temp = new File(filepath);
            BufferedWriter bw = new BufferedWriter(new FileWriter(temp, false));
            bw.write(content);
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(File.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String readFile(String filename) {
        String dir = System.getProperty("java.io.tmpdir");
        String filepath = dir + File.separator + filename;
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            return formatTokenString(sb.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String formatTokenString(String token) {
        return token.replaceAll(Constants.FILE_LINE_BREAK, "");
    }

    public static boolean deleteFile(String filename) {
        String dir = System.getProperty("java.io.tmpdir");
        String filepath = dir + File.separator + filename;
        File temp = new File(filepath);
        return temp.delete();
    }
}
