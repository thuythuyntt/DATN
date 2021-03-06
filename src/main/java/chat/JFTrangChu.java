/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import customview.MyPCControllerPopup;
import dangnhap.JFDangNhap;
import firebasedb.FirebaseHelper;
import java.awt.AWTException;
import java.awt.Adjustable;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import model.Message;
import model.User;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import model.SessionInfo;
import model.SocketMessage;
import model.Student;
import socket.SocketClient;
import util.Constants;
import util.OSCommand;
import util.TokenFile;
import util.WebPage;

/**
 *
 * @author nguyen.thi.thu.thuy
 */
public class JFTrangChu extends JFrameBase {

    public static final String STRING_ACTIVELY_DISCONNECT = "Tu tat may";
    public static final String STRING_PASSIVELY_DISCONNECT = "Bi giao vien tat may";
    public String SOCKET_SERVER_IP = "192.168.4.61";

    private final User user;
    private String toUserId = "";
    private String fromUserId = "";
    private String studentId = "";
    private final SocketClient sk = SocketClient.getInstance();

    private final SocketClient.Listener socketListener = new SocketClient.Listener() {
        @Override
        public void connected() {
            FirebaseHelper.getInstance().updateOnlineStatus(true);
            jMenuItemReconnect.setEnabled(false);
            firstConnect();
            showProgressBar();
            initCustomComponents();
            setupData();
        }

        @Override
        public void disconnected(Throwable e) {
            System.out.println("disconnected");
            jMenuItemReconnect.setEnabled(true);
        }

        @Override
        public void updateOnlineList(List<SessionInfo> list) {
            setupDSSinhVien(list);
        }

        @Override
        public void doControlAction(String action, String pc) {

            switch (action) {
                case SocketMessage.CTL_LOCK_SCREEN:
                    OSCommand.lockScreen();
                    break;
                case SocketMessage.CTL_RESTART:

                    FirebaseHelper.getInstance().updateOnlineStatus(false);
                    SessionInfo s1 = new SessionInfo();
                    s1.setDtLogout(getTimeNow());
                    s1.setReasonLogout(STRING_PASSIVELY_DISCONNECT);
                    sk.sendMessage(new SocketMessage(SocketMessage.DISCONNECT, s1));

                    sk.disconnect();
                    OSCommand.restart();
                    break;
                case SocketMessage.CTL_SHUTDOWN:
                    FirebaseHelper.getInstance().updateOnlineStatus(false);

                    FirebaseHelper.getInstance().updateOnlineStatus(false);
                    SessionInfo s2 = new SessionInfo();
                    s2.setDtLogout(getTimeNow());
                    s2.setReasonLogout(STRING_PASSIVELY_DISCONNECT);
                    sk.sendMessage(new SocketMessage(SocketMessage.DISCONNECT, s2));

                    sk.disconnect();
                    OSCommand.shutdown();
                    break;
                default:
                    break;
            }
        }

        @Override
        public void sendScreenshot() {
            System.out.println("[JFTrangChu] sendScreenshot");
//            try {
//                Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
//                BufferedImage capture = new Robot().createScreenCapture(screenRect);
//
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                try {
//                    ImageIO.write(capture, "jpg", baos);
//                    baos.flush();
//                    byte[] imageInByte = baos.toByteArray();
//                    String s = new BASE64Encoder().encode(imageInByte);
//                    baos.close();
//
//                    sk.sendMessage(new SocketMessage(SocketMessage.SET_VIEWER, s));
//                    System.out.println("BASE64Encoder: " + s);
//                } catch (IOException ex) {
//                    Logger.getLogger(JFTrangChu.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            } catch (AWTException ex) {
//                ex.printStackTrace();
//            }
        }

        @Override
        public void receiveScreenshot(String capture) {
//            try {
//                System.out.println("[JFTrangChu] receiveScreenshot");
//                JFrame frame = new JFrame("VIEWER");
//                frame.setSize(900, 600);
////                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
////                frame.setUndecorated(true);
//                frame.setVisible(true);
//                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//                frame.add(new JPSharingScreen(new BASE64Decoder().decodeBuffer(capture)));
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
        }

        @Override
        public void receiveNotification(String noti) {
            JOptionPane.showMessageDialog(JFTrangChu.this, noti, "THÔNG BÁO TỪ GIÁO VIÊN", JOptionPane.INFORMATION_MESSAGE);
        }

        @Override
        public void receiveListStudent(List<Student> lst) {
            list = new ArrayList<>();
            filterList = new ArrayList<>();

            list.addAll(lst);
            filterList.addAll(lst);

            setupDataTab3();
        }

        @Override
        public void receiveListSession(List<SessionInfo> list) {
            System.out.println("LIST SESSION: " + list.size());
            User sv = FirebaseHelper.getInstance().getUserFromId(studentId);
            JFLichSuHoatDong jf = new JFLichSuHoatDong(sv, list);
            JFTrangChu.this.showScreen(jf);
        }
    };

    private void firstConnect() {
        try {
            SessionInfo s = new SessionInfo();
            s.setUserId(user.getId());
            s.setFullName(user.getFullname());
            s.setUserName(user.getUsername());
            s.setPcName(InetAddress.getLocalHost().getHostName());
            s.setIpAddress(InetAddress.getLocalHost().getHostAddress());
            s.setDtLogin(getTimeNow());
            s.setRole(user.getRole());
            SocketMessage sm = new SocketMessage(SocketMessage.CONNECT, s);
            sk.sendMessage(sm);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public JFTrangChu() {
        initComponents();
        user = FirebaseHelper.getInstance().getAuthUser();
        String ipAddress = (String) JOptionPane.showInputDialog(this, "Địa chỉ IP: ", "Kết nối Server", JOptionPane.QUESTION_MESSAGE, null, null, SOCKET_SERVER_IP);
        sk.setHost(ipAddress);
        sk.connect(socketListener);
    }

    private void showProgressBar() {
        panelLoading.setVisible(true);
    }

    private void hideProgressBar() {
        panelLoading.setVisible(false);
    }

    private void initCustomComponents() {
//        try {
        if (user.getRole().equals(Constants.ROLE_STUDENT)) {
            jPanel2.remove(lbSendNotification);
        }
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                System.out.println("windowClosed");
            }
            
            
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("windowClosing");
//                
//                Thread t = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        FirebaseHelper.getInstance().updateOnlineStatus(false);
//                        SessionInfo s = new SessionInfo();
//                        s.setDtLogout(getTimeNow());
//                        s.setReasonLogout(STRING_ACTIVELY_DISCONNECT);
//                        System.out.println("sk.sendMessage DISCONNECT");
//                        sk.sendMessage(new SocketMessage(SocketMessage.DISCONNECT, s));
//                        Timer timer = new Timer();
//                        timer.schedule(new TimerTask() {
//                            @Override
//                            public void run() {
//                                System.out.println("sk.disconnect");
//                                sk.disconnect();
//                            }
//                        }, 5000);
//                    }
//                });
//                t.start();
            }
        });

//            String base64Ava = "/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAYEBQYFBAYGBQYHBwYI"
//                    + "ChAKCgkJChQODwwQFxQYGBcUFhYaHSUfGhsjHBYWICwgIyYnKSopGR8tMC0oM"
//                    + "CUoKSj/2wBDAQcHBwoIChMKChMoGhYaKCgoKCgoKCgoKCgoKCgoKCgoKCgoKC"
//                    + "goKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCj/wAARCABkAGQDASIAAhEBAxE"
//                    + "B/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIE"
//                    + "AwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2Jyg"
//                    + "gkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3"
//                    + "R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMX"
//                    + "Gx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEB"
//                    + "AQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECA"
//                    + "xEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJy"
//                    + "gpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYa"
//                    + "HiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX"
//                    + "2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD6oooooAKKKKACi"
//                    + "kyKzdc1ux0Wye61CdYo1GQOrN7AdzRtqNJt2Rpk460A+hrwjXvitfahO0emwm"
//                    + "0tc4BYZdvc9h9BWh8O/HNxNrUVjfzmRZTtDM3QmsPrEb2On6pNR5mez5ozQOl"
//                    + "FbnKFGaM0UALmikooAKKSjNAC0UmaRnCqWYgADJJ7UAeNfF/xL440/WWtPC8c"
//                    + "BgEQYRlf3kg/vKT+WPavJo/FGuXEzt4p0e+RwcGUISQc+h+or33xNrNvqF9Gs"
//                    + "KRtHATtkIyxJ6kegrMkaOf/AFiq/plRxXnVZJya3PWopxitLHjs3kXcfmWcu4"
//                    + "Zwc9R7EdqZ4dSeDxNp5JO7z0A56810/iTwTHczS3GiXT2l4TuyeVZvQ1X8I6F"
//                    + "4kmv0ju7Cwlu7Zg6K0/ltJtOcqSMH6Z/KlGPNoaymkrn05SVT0q6lvLKOa4tZ"
//                    + "bWYj54pQMqe/Tgj3q5XpI8TYKWkpaYBRRRSASorm4itojLPIkca9WdgAPxNS1"
//                    + "4r+0prQPhWfRrV281wskuw9BngH8ifyqak1CNzSlTdSXKewJf2kkLzRXMLxJ9"
//                    + "5lcEL9cVwHi/xQbwPZ2JIh/iPd/wD61eNfCbU3l8KAPI28Hy3yeDzwa7QqDE7"
//                    + "nk4GK4q1ebTjsehRw0Ivmvcht9RihmZHGCORnvWuus2qQtIw5UZGO/wCFcpPY"
//                    + "yzR5GQw5U5qnLLNHGpVT5i9V9RXFztna4Jkup/EqxtVnle1kVYCV3tjrjtjqT"
//                    + "R4L+IN5qV1HdTWMdlasyfZ2mnAeTcSAyqR8wyMEjoa898bWlpPYXHkLs3uHIw"
//                    + "euK53wva3M93aWzyXbJDINgkbKxLnJCjoMn869GjCEocz3PPqynGfKtj7q0TU"
//                    + "xe2yMw2yY+Ye9ateZeFdRbyVZX3FCAfcEV6RbSeZEreoropT54nJWp8kiWiii"
//                    + "tDEKKKKQHNePfFVp4T0Ka+uGVpiCsEWeZHxwPp618w3+p3PiC3v7m/k8ye5Uy"
//                    + "MfU8/pXWfHC8uNS8TzW7Mwhtl8uMHp15Nee6c7QyvDx8q9PXjn9K5Kr53bsel"
//                    + "h4Kmr9WO+G1+bWJ7SX5WYHAPA4Nd3e6yLHR7i4mKhIuCc4yewry2xjnhvnniV"
//                    + "tiSswPt3FX/iDqF1JottFaRCQTOT0zz649ccVi4c815m/PyQb7Ha6T4ys7y1J"
//                    + "jUCVOcZ6/TNabXcF8vmwkHHf1r5x0+6v7Jmx5kSO5UBjn8K9Q8G3zxwfvJQxP"
//                    + "OAO9KthfZ6rYKGJ9putTtrrTLS/XzJYv3nQ7TjIqHRdKjtLthKmYz/FjkflVy"
//                    + "zmWePKFVcfw+taFnbzTvmFGIBxkLxUKE7aGrlC+p1GgRQWk6qkxKSYyCO/Y16"
//                    + "jooZbGMP94AA4rzDRLG7cqJIN6qcfKCCp9v8ACvStLJgWOCUENt4J7124eMle"
//                    + "6POxTT2Zq0UlLXScQUUmaKAOL8beA7LxJG8iMILs878ZDH3rxTUvhD4r07U1u"
//                    + "rOGG9iU/Msc65Ze+A2Dmvp7NIQCOah04t3NY1pRVjyL4T+CNGn0C5m1HT0mvf"
//                    + "tU0Upmydu1sYA7VynxC+GkWnT5+d9LckxN/wA8yex9x2NfQFnZ29n532aJY/O"
//                    + "kM0mP4nOMn9KbqdnDf2UttcIHjkGCDQ6aasio12pXezPh/VfB4tL/AM2WaaQD"
//                    + "lfMPB/GtC1tDDYLLGOFbmvUfGvh6bRrp0mieW17PjIx7+lYVrpkWo2zpYKA4I"
//                    + "yjfL+XauSpzy0lud1LkWsRnhR5bmeN0UkAgscdBV7xD4uGiFZItrTk4aPHGfp"
//                    + "XT+C/Dr6dY3b3Meyffge47H6V85+L9UuLfV7wXuUlWV1+b+Hk124eHJDU48TN"
//                    + "SnoezeGfi7cJcL/af2eKIsM/KRx7EV71omqx6jawyocq44P8AWvgYI1/LAz3L"
//                    + "qsYLYX+IV9JfCHxLeSnStLwzytEu4ckL9fwxW9rnOfQ0JJXB6ipKhgIx1zmpC"
//                    + "ayYhSM0UZopANPFJTiKQLTQCUtLtpDxRcDnPF2n/abTeo+YDqOteaSRzJMA20"
//                    + "BT6V7NfKJLZgfSvNtXiVGdjwykggCriUmOs7wS2yxygKwGOOhryj4r+D7HWpH"
//                    + "nMOyZckuhwT9fWu9R8kY69AKlufKYLHPGkvGPnGaodj51sPDscKqNsszIcJGF"
//                    + "GZD/AHRive/gv4bvNKjOqatGkU7KVihXpGD3Pqa6DTLKy2qRaQxMPRQK6KKQC"
//                    + "IKAMUNisdBZT5QAn8a0FORmuZtrgKQuea6C3fegI71DQmT5opM0VIiWiiipAK"
//                    + "Y/CmiimgMLVbiRUIB4riNQcmQscEkUUVoi0YAlYzHpk1PPz8/faDRRVDNXTmJ"
//                    + "SPtkc4rZgclB0oopAx3mMCx9K6fR3Z7ZSxoopSIZo0UUVAj//2Q==";
//            
//            System.out.println("BASE64 size: " + base64Ava.length());
//            byte[] btDataFile = new BASE64Decoder().decodeBuffer(base64Ava);
//            BufferedImage image = ImageIO.read(new ByteArrayInputStream(btDataFile));
//            ImageIcon icon = new ImageIcon(image);
//            jLabel1.setIcon(icon);
//        BufferedImage img;
//        try {
//            img = ImageIO.read(new URL("http://www.java2s.com/style/download.png"));
//            ImageIcon icon = new ImageIcon(img.getScaledInstance(89, -1, Image.SCALE_SMOOTH));
//
//            jLabel1.setIcon(icon);
//        } catch (IOException ex) {
//            Logger.getLogger(JFTrangChu.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        } catch (IOException ex) {
//            Logger.getLogger(JFTrangChu.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    private boolean isFirstLoadTab2, isFirstLoadTab3 = true;

    private void setupData() {
        setupInfo();
        setupDSBanBe();
        setupPosIndex();
        listenGroupChatEvent();

        if (user.getRole().equals(Constants.ROLE_STUDENT)) {
            jTabbedPane1.remove(panelManagement);
            jTabbedPane1.remove(panelStatistics);
        }

        jTabbedPane1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JTabbedPane sourceTabbedPane = (JTabbedPane) e.getSource();
                int index = sourceTabbedPane.getSelectedIndex();
                switch (index) {
                    case 1:
                        if (isFirstLoadTab2) {
                            SocketMessage sm = new SocketMessage(SocketMessage.GET_LIST_ONINE);
                            sk.sendMessage(sm);
                        }
                        isFirstLoadTab2 = false;
                        break;
                    case 2:
                        if (isFirstLoadTab3) {
                            SocketMessage sm1 = new SocketMessage(SocketMessage.GET_LIST_STUDENT);
                            sk.sendMessage(sm1);
                        }
                        isFirstLoadTab3 = false;
                        break;
                }
            }
        });
    }

    private List<Student> list, filterList;

    private void setupDataTab3() {
        tblDSSVLSHD.removeAll();
        tblDSSVLSHD.revalidate();
        tblDSSVLSHD.repaint();

        DefaultTableModel model = (DefaultTableModel) tblDSSVLSHD.getModel();

        if (model.getRowCount() > 0) {
            model.setRowCount(0);
        }

        for (int i = 0; i < filterList.size(); i++) {
            Object[] row = new Object[5];
            row[0] = i + 1;
            row[1] = filterList.get(i).getUsername();
            row[2] = filterList.get(i).getFullname();
            row[3] = filterList.get(i).getCode();
            row[4] = filterList.get(i).getGroup();
            model.addRow(row);
        }

        tblDSSVLSHD.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                studentId = filterList.get(tblDSSVLSHD.getSelectedRow()).getId();
                SocketMessage sm1 = new SocketMessage(SocketMessage.GET_LIST_SESSION, studentId);
                sk.sendMessage(sm1);
            }
        });
    }

    private void setupDSSinhVien(List<SessionInfo> list) {
        tblDSSV.removeAll();
        tblDSSV.revalidate();
        tblDSSV.repaint();

        List<SessionInfo> onlineList = new ArrayList<>();
        for (SessionInfo ci : list) {
            System.out.println(ci.getFullName());
            if (!ci.getUserName().equals(user.getUsername())) {
                onlineList.add(ci);
            }
        }

        DefaultTableModel model = (DefaultTableModel) tblDSSV.getModel();

        if (model.getRowCount() > 0) {
            model.setRowCount(0);
        }

        for (int i = 0; i < onlineList.size(); i++) {
            Object[] row = new Object[5];
            row[0] = i + 1;
            row[1] = onlineList.get(i).getFullName();
            row[2] = onlineList.get(i).getIpAddress();
            row[3] = onlineList.get(i).getPcName();
            row[4] = onlineList.get(i).getDtLogin();
            model.addRow(row);
        }

        tblDSSV.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int r = tblDSSV.rowAtPoint(e.getPoint());
                if (r >= 0 && r < tblDSSV.getRowCount()) {
                    tblDSSV.setRowSelectionInterval(r, r);
                } else {
                    tblDSSV.clearSelection();
                }

                int rowindex = tblDSSV.getSelectedRow();
                if (rowindex < 0) {
                    return;
                }
                if (e.isPopupTrigger() && e.getComponent() instanceof JTable) {
                    SessionInfo c = onlineList.get(rowindex);
                    System.out.println(c.getIpAddress());
                    new MyPCControllerPopup(new MyPCControllerPopup.OnClick() {
                        @Override
                        public void clickLockScreen() {

                            Object[] options = {"Luôn và ngay",
                                "Hủy"};
                            int n = JOptionPane.showOptionDialog(
                                    JFTrangChu.this,
                                    "Bạn muốn LOCK SCREEN máy tính: " + c.getPcName() + "?",
                                    "XÁC NHẬN",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.QUESTION_MESSAGE,
                                    null,
                                    options,
                                    options[0]);
                            if (n == 0) {
                                sk.sendMessage(new SocketMessage(SocketMessage.CTL_LOCK_SCREEN, c));
                            }
                        }

                        @Override
                        public void clickShutdown() {
                            Object[] options = {"Luôn và ngay",
                                "Hủy"};
                            int n = JOptionPane.showOptionDialog(
                                    JFTrangChu.this,
                                    "Bạn muốn SHUT DOWN máy tính: " + c.getPcName() + "?",
                                    "XÁC NHẬN",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.QUESTION_MESSAGE,
                                    null,
                                    options,
                                    options[0]);
                            if (n == 0) {
                                sk.sendMessage(new SocketMessage(SocketMessage.CTL_SHUTDOWN, c));
                            }
                        }

                        @Override
                        public void clickRestart() {
                            Object[] options = {"Luôn và ngay",
                                "Hủy"};
                            int n = JOptionPane.showOptionDialog(
                                    JFTrangChu.this,
                                    "Bạn muốn RESTART máy tính: " + c.getPcName() + "?",
                                    "XÁC NHẬN",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.QUESTION_MESSAGE,
                                    null,
                                    options,
                                    options[0]);
                            if (n == 0) {
                                sk.sendMessage(new SocketMessage(SocketMessage.CTL_RESTART, c));
                            }
                        }

                        @Override
                        public void clickViewer() {
                            sk.sendMessage(new SocketMessage(SocketMessage.GET_VIEWER, c));
                        }
                    }).show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    private void setupInfo() {
        fromUserId = user.getId();
        lbTenGV.setText(user.getFullname());
        lbTenCuocTroChuyen.setText("Cả lớp");
    }

    private void setupDSBanBe() {
        List<User> list = new ArrayList<>();

        FirebaseHelper.getInstance().getListOnlineUsers(new FirebaseHelper.UserOnlineChangeListener() {
            @Override
            public void onEventOnline(List<User> lst) {
                list.clear();
                User all = new User();
                all.setId("");
                all.setFullname("Cả lớp");
                list.add(all);

                list.addAll(lst);

                DefaultTableModel model = (DefaultTableModel) tblDSSVOnline.getModel();

                if (model.getRowCount() > 0) {
                    model.setRowCount(0);
                }

                Object[] row = new Object[1];
                for (int i = 0; i < list.size(); i++) {
                    row[0] = list.get(i).getFullname();
                    model.addRow(row);
                }
            }
        });

        tblDSSVOnline.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showProgressBar();
                int row = tblDSSVOnline.rowAtPoint(evt.getPoint());
                toUserId = list.get(row).getId();
                setupPosIndex();
                scrollPaneNoiDungCuocTroChuyen.removeAll();
                scrollPaneNoiDungCuocTroChuyen.revalidate();
                scrollPaneNoiDungCuocTroChuyen.repaint();
                lbTenCuocTroChuyen.setText(list.get(row).getFullname());
                if (row == 0) {
                    listenGroupChatEvent();
                } else {
                    listenSingleChatEvent();
                }
            }
        });
    }

    private void setupPosIndex() {
        width = 370;
        posY = 10;
        padding = 8;
        margin = 8;
    }

    private void listenGroupChatEvent() {
        FirebaseHelper.getInstance().listenerGroupChatEvent(toUserId, new FirebaseHelper.RoomMessageChangeListener() {
            @Override
            public void onEvent(String userId, List<Message> list) {

                if (toUserId.equals(userId)) {
                    setupMessage(list);
                }
            }

            @Override
            public void onEvent(String toUserId, String fromUserId, List<Message> list) {
            }
        });
    }

    private void listenSingleChatEvent() {
        FirebaseHelper.getInstance().listenerSingleChatEvent(toUserId, new FirebaseHelper.RoomMessageChangeListener() {
            @Override
            public void onEvent(String userId, List<Message> list) {
            }

            @Override
            public void onEvent(String userA, String userB, List<Message> list) {
                if ((fromUserId.equals(userA) && toUserId.equals(userB)) || (fromUserId.equals(userB) && toUserId.equals(userA))) {
                    setupMessage(list);
                }
            }

        });
    }

    private void setupMessage(List<Message> list) {
        for (int i = 0; i < list.size(); i++) {
            Message m = list.get(i);
            addMessageToScrollPane(m);
        }
        hideProgressBar();
    }

    int width = 0;
    int posY = 0;
    int padding = 0;
    int margin = 0;

    private void addMessageToScrollPane(Message m) {
        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Times New Roman", Font.PLAIN, 13));

        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        textArea.setOpaque(true);
        textArea.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
        int height = 0;
        if (m.getFromUserId().equals(user.getId())) {
            height = TokenFile.getContentHeight(width, m.getText());
            textArea.setSize(width, height + padding);
            textArea.setText(m.getText());
            textArea.setLocation(340, posY);
            textArea.setBackground(Color.decode("#90CAF9"));
        } else {
            String content = FirebaseHelper.getInstance().getUserFromId(m.getFromUserId())
                    .getFullname() + ":\n" + m.getText();
            height = TokenFile.getContentHeight(width, content);
            textArea.setSize(width, height + padding);
            textArea.setText(content);
            textArea.setLocation(8, posY);
            textArea.setBackground(Color.decode("#B0BEC5"));
        }
        posY += padding * 2 + height + margin;
        scrollPaneNoiDungCuocTroChuyen.setPreferredSize(new Dimension(750, posY));
        scrollPaneNoiDungCuocTroChuyen.add(textArea);

        JScrollBar verticalBar = jScrollPane.getVerticalScrollBar();
        AdjustmentListener downScroller = new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                Adjustable adjustable = e.getAdjustable();
                adjustable.setValue(adjustable.getMaximum());
                verticalBar.removeAdjustmentListener(this);
            }
        };
        verticalBar.addAdjustmentListener(downScroller);

        scrollPaneNoiDungCuocTroChuyen.repaint();
        scrollPaneNoiDungCuocTroChuyen.revalidate();
        jScrollPane.repaint();
        jScrollPane.revalidate();
    }

    public void showPersonInformation() {
        if (user.getRole().equals(Constants.ROLE_STUDENT)) {
            this.showScreen(new JFThongTinSV());
        } else {
            this.showScreen(new JFThongTinGV());
        }
    }

    public void showChangePwDialog() {
        this.showScreen(new JFDoiMatKhau());
    }

    public void logOut() {
        TokenFile.deleteFile(Constants.TMP_FILE_NAME);
        FirebaseHelper.getInstance().updateToken("");
        this.dispose();
        this.showScreen(new JFDangNhap());
        FirebaseHelper.getInstance().updateOnlineStatus(false);

        SessionInfo s = new SessionInfo();
        s.setDtLogout(getTimeNow());
        s.setReasonLogout(STRING_ACTIVELY_DISCONNECT);
        sk.sendMessage(new SocketMessage(SocketMessage.DISCONNECT, s));

        sk.disconnect();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelLoading = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        panelHome = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        lbTenGV = new javax.swing.JLabel();
        panelAvatar = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        scrollPaneOnline = new javax.swing.JScrollPane();
        tblDSSVOnline = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        lbTenCuocTroChuyen = new javax.swing.JLabel();
        jScrollPane = new javax.swing.JScrollPane();
        scrollPaneNoiDungCuocTroChuyen = new javax.swing.JPanel();
        panelChatInput = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        areaNhapTinNhan = new javax.swing.JTextArea();
        btnSendFile = new javax.swing.JLabel();
        btnSendImage = new javax.swing.JLabel();
        btnSendMessage = new javax.swing.JLabel();
        lbSendNotification = new javax.swing.JLabel();
        panelManagement = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblDSSV = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        cbSelectAll = new javax.swing.JCheckBox();
        panelStatistics = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        tfSearchLSHD = new javax.swing.JTextField();
        btnSearchLSHD = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblDSSVLSHD = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenuSetting = new javax.swing.JMenu();
        jMenuItemChangePw = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuItemLogout = new javax.swing.JMenuItem();
        jMenuHelp = new javax.swing.JMenu();
        jMenuItemReconnect = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAboutMe = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new javax.swing.OverlayLayout(getContentPane()));

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/loading_icon.gif"))); // NOI18N

        javax.swing.GroupLayout panelLoadingLayout = new javax.swing.GroupLayout(panelLoading);
        panelLoading.setLayout(panelLoadingLayout);
        panelLoadingLayout.setHorizontalGroup(
            panelLoadingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        panelLoadingLayout.setVerticalGroup(
            panelLoadingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        getContentPane().add(panelLoading);

        jTabbedPane1.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(1000, 700));

        panelHome.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panelHome.setPreferredSize(new java.awt.Dimension(1000, 700));

        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(13, 71, 161), 2, true));
        jPanel1.setPreferredSize(new java.awt.Dimension(200, 670));

        lbTenGV.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lbTenGV.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbTenGV.setText("      ");
        lbTenGV.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lbTenGV.setPreferredSize(new java.awt.Dimension(100, 40));

        panelAvatar.setBackground(new java.awt.Color(204, 204, 204));
        panelAvatar.setPreferredSize(new java.awt.Dimension(140, 140));
        panelAvatar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelAvatarMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelAvatarLayout = new javax.swing.GroupLayout(panelAvatar);
        panelAvatar.setLayout(panelAvatarLayout);
        panelAvatarLayout.setHorizontalGroup(
            panelAvatarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 140, Short.MAX_VALUE)
        );
        panelAvatarLayout.setVerticalGroup(
            panelAvatarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 140, Short.MAX_VALUE)
        );

        jTabbedPane2.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jTabbedPane2.setFont(new java.awt.Font("Cambria Math", 0, 12)); // NOI18N
        jTabbedPane2.setPreferredSize(new java.awt.Dimension(170, 400));

        tblDSSVOnline.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        tblDSSVOnline.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                ""
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        scrollPaneOnline.setViewportView(tblDSSVOnline);

        jTabbedPane2.addTab("Online", scrollPaneOnline);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(panelAvatar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbTenGV, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelAvatar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lbTenGV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 415, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel2.setPreferredSize(new java.awt.Dimension(750, 670));

        lbTenCuocTroChuyen.setFont(new java.awt.Font("Cambria Math", 0, 14)); // NOI18N
        lbTenCuocTroChuyen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ic_group_32.png"))); // NOI18N
        lbTenCuocTroChuyen.setText("Tên cuộc trò chuyện");
        lbTenCuocTroChuyen.setPreferredSize(new java.awt.Dimension(700, 40));
        lbTenCuocTroChuyen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbTenCuocTroChuyenMouseClicked(evt);
            }
        });

        jScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane.setAutoscrolls(true);
        jScrollPane.setPreferredSize(new java.awt.Dimension(750, 400));

        scrollPaneNoiDungCuocTroChuyen.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(13, 71, 161)));
        scrollPaneNoiDungCuocTroChuyen.setAutoscrolls(true);
        scrollPaneNoiDungCuocTroChuyen.setPreferredSize(new java.awt.Dimension(750, 0));

        javax.swing.GroupLayout scrollPaneNoiDungCuocTroChuyenLayout = new javax.swing.GroupLayout(scrollPaneNoiDungCuocTroChuyen);
        scrollPaneNoiDungCuocTroChuyen.setLayout(scrollPaneNoiDungCuocTroChuyenLayout);
        scrollPaneNoiDungCuocTroChuyenLayout.setHorizontalGroup(
            scrollPaneNoiDungCuocTroChuyenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 748, Short.MAX_VALUE)
        );
        scrollPaneNoiDungCuocTroChuyenLayout.setVerticalGroup(
            scrollPaneNoiDungCuocTroChuyenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 451, Short.MAX_VALUE)
        );

        jScrollPane.setViewportView(scrollPaneNoiDungCuocTroChuyen);

        panelChatInput.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(13, 71, 161)));
        panelChatInput.setPreferredSize(new java.awt.Dimension(750, 190));

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setPreferredSize(new java.awt.Dimension(212, 112));

        areaNhapTinNhan.setColumns(20);
        areaNhapTinNhan.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        areaNhapTinNhan.setLineWrap(true);
        areaNhapTinNhan.setRows(5);
        areaNhapTinNhan.setWrapStyleWord(true);
        areaNhapTinNhan.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 16, 16, 16));
        areaNhapTinNhan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                areaNhapTinNhanKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(areaNhapTinNhan);

        btnSendFile.setFont(new java.awt.Font("Cambria Math", 2, 10)); // NOI18N
        btnSendFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ic_file_24.png"))); // NOI18N

        btnSendImage.setFont(new java.awt.Font("Cambria Math", 2, 10)); // NOI18N
        btnSendImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ic_image_24.png"))); // NOI18N

        btnSendMessage.setFont(new java.awt.Font("Cambria Math", 3, 10)); // NOI18N
        btnSendMessage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ic_send_24_1.png"))); // NOI18N
        btnSendMessage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSendMessageMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelChatInputLayout = new javax.swing.GroupLayout(panelChatInput);
        panelChatInput.setLayout(panelChatInputLayout);
        panelChatInputLayout.setHorizontalGroup(
            panelChatInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelChatInputLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelChatInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelChatInputLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnSendFile)
                        .addGap(49, 49, 49)
                        .addComponent(btnSendImage)
                        .addGap(39, 39, 39)
                        .addComponent(btnSendMessage)
                        .addGap(10, 10, 10)))
                .addContainerGap())
        );
        panelChatInputLayout.setVerticalGroup(
            panelChatInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelChatInputLayout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addGroup(panelChatInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSendMessage)
                    .addComponent(btnSendImage)
                    .addComponent(btnSendFile))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        lbSendNotification.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        lbSendNotification.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbSendNotification.setIcon(new javax.swing.ImageIcon(getClass().getResource("/notification_send_32.png"))); // NOI18N
        lbSendNotification.setText("Gửi thông báo");
        lbSendNotification.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbSendNotificationMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(lbTenCuocTroChuyen, javax.swing.GroupLayout.PREFERRED_SIZE, 587, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbSendNotification, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(panelChatInput, javax.swing.GroupLayout.DEFAULT_SIZE, 748, Short.MAX_VALUE)
                    .addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 748, Short.MAX_VALUE))
                .addGap(2, 2, 2))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbTenCuocTroChuyen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbSendNotification, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(panelChatInput, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout panelHomeLayout = new javax.swing.GroupLayout(panelHome);
        panelHome.setLayout(panelHomeLayout);
        panelHomeLayout.setHorizontalGroup(
            panelHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHomeLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelHomeLayout.setVerticalGroup(
            panelHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelHomeLayout.createSequentialGroup()
                .addGroup(panelHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 661, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 661, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Trang chủ", panelHome);

        panelManagement.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        panelManagement.setPreferredSize(new java.awt.Dimension(1000, 600));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("QUẢN LÝ MÁY TÍNH PHÒNG THỰC HÀNH");

        jScrollPane1.setBorder(javax.swing.BorderFactory.createCompoundBorder());

        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTextArea1.setRows(5);
        jTextArea1.setText("\t\n\t» Chọn một hàng trong bảng\n\t» Click chuột phải\n\t» Chọn hành động thích hợp:\n \t\t- Giám sát màn hình\n \t\t- Khóa máy\n \t\t- Tắt máy\n\t\t- Khởi động lại");
        jTextArea1.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        jScrollPane1.setViewportView(jTextArea1);

        tblDSSV.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        tblDSSV.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Họ tên", "Địa chỉ IP", "Máy tính", "Thời gian", "Chọn"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tblDSSV);
        if (tblDSSV.getColumnModel().getColumnCount() > 0) {
            tblDSSV.getColumnModel().getColumn(0).setPreferredWidth(16);
            tblDSSV.getColumnModel().getColumn(1).setPreferredWidth(64);
            tblDSSV.getColumnModel().getColumn(2).setPreferredWidth(48);
            tblDSSV.getColumnModel().getColumn(3).setPreferredWidth(32);
            tblDSSV.getColumnModel().getColumn(4).setPreferredWidth(48);
            tblDSSV.getColumnModel().getColumn(5).setPreferredWidth(16);
        }

        jLabel3.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/guideline.png"))); // NOI18N
        jLabel3.setText("Hướng dẫn:");

        cbSelectAll.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        cbSelectAll.setText("Chọn tất cả");
        cbSelectAll.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        cbSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbSelectAllActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelManagementLayout = new javax.swing.GroupLayout(panelManagement);
        panelManagement.setLayout(panelManagementLayout);
        panelManagementLayout.setHorizontalGroup(
            panelManagementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panelManagementLayout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(panelManagementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 889, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 889, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(46, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelManagementLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cbSelectAll)
                .addGap(96, 96, 96))
        );
        panelManagementLayout.setVerticalGroup(
            panelManagementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelManagementLayout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(cbSelectAll)
                .addGap(11, 11, 11)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Quản lý", panelManagement);

        panelStatistics.setPreferredSize(new java.awt.Dimension(1000, 600));

        jLabel4.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("DANH SÁCH SINH VIÊN");

        tfSearchLSHD.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N

        btnSearchLSHD.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        btnSearchLSHD.setText("Tìm kiếm");
        btnSearchLSHD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSearchLSHDMouseClicked(evt);
            }
        });

        tblDSSVLSHD.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Tên đăng nhập", "Họ tên", "Mã sinh viên", "Lớp"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(tblDSSVLSHD);
        if (tblDSSVLSHD.getColumnModel().getColumnCount() > 0) {
            tblDSSVLSHD.getColumnModel().getColumn(0).setPreferredWidth(16);
            tblDSSVLSHD.getColumnModel().getColumn(1).setPreferredWidth(48);
            tblDSSVLSHD.getColumnModel().getColumn(2).setPreferredWidth(64);
            tblDSSVLSHD.getColumnModel().getColumn(3).setPreferredWidth(32);
            tblDSSVLSHD.getColumnModel().getColumn(4).setPreferredWidth(32);
        }

        javax.swing.GroupLayout panelStatisticsLayout = new javax.swing.GroupLayout(panelStatistics);
        panelStatistics.setLayout(panelStatisticsLayout);
        panelStatisticsLayout.setHorizontalGroup(
            panelStatisticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelStatisticsLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tfSearchLSHD, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSearchLSHD)
                .addGap(330, 330, 330))
            .addGroup(panelStatisticsLayout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 872, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(53, Short.MAX_VALUE))
            .addGroup(panelStatisticsLayout.createSequentialGroup()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelStatisticsLayout.setVerticalGroup(
            panelStatisticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelStatisticsLayout.createSequentialGroup()
                .addGap(107, 107, 107)
                .addComponent(jLabel4)
                .addGap(47, 47, 47)
                .addGroup(panelStatisticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSearchLSHD)
                    .addComponent(tfSearchLSHD, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(110, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Lịch sử hoạt động", panelStatistics);

        getContentPane().add(jTabbedPane1);

        jMenuBar1.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N

        jMenuSetting.setText("Cài đặt");
        jMenuSetting.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N

        jMenuItemChangePw.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jMenuItemChangePw.setIcon(new javax.swing.ImageIcon(getClass().getResource("/change_password_16.png"))); // NOI18N
        jMenuItemChangePw.setText("Đổi mật khẩu");
        jMenuItemChangePw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemChangePwActionPerformed(evt);
            }
        });
        jMenuSetting.add(jMenuItemChangePw);
        jMenuSetting.add(jSeparator2);

        jMenuItemLogout.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jMenuItemLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/logout_16.png"))); // NOI18N
        jMenuItemLogout.setText("Đăng xuất");
        jMenuItemLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemLogoutActionPerformed(evt);
            }
        });
        jMenuSetting.add(jMenuItemLogout);

        jMenuBar1.add(jMenuSetting);

        jMenuHelp.setText("Trợ giúp");
        jMenuHelp.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N

        jMenuItemReconnect.setIcon(new javax.swing.ImageIcon(getClass().getResource("/reconnect.png"))); // NOI18N
        jMenuItemReconnect.setText("Kết nối lại");
        jMenuItemReconnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemReconnectActionPerformed(evt);
            }
        });
        jMenuHelp.add(jMenuItemReconnect);
        jMenuHelp.add(jSeparator1);

        jMenuItemAboutMe.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jMenuItemAboutMe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/about.png"))); // NOI18N
        jMenuItemAboutMe.setText("About me");
        jMenuItemAboutMe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAboutMeActionPerformed(evt);
            }
        });
        jMenuHelp.add(jMenuItemAboutMe);

        jMenuBar1.add(jMenuHelp);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSendMessageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSendMessageMouseClicked
        sendMessage();
    }//GEN-LAST:event_btnSendMessageMouseClicked

    private void areaNhapTinNhanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_areaNhapTinNhanKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            sendMessage();
        }
    }//GEN-LAST:event_areaNhapTinNhanKeyPressed

    private void jMenuItemChangePwActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemChangePwActionPerformed
        showChangePwDialog();
    }//GEN-LAST:event_jMenuItemChangePwActionPerformed

    private void jMenuItemLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemLogoutActionPerformed
        logOut();
    }//GEN-LAST:event_jMenuItemLogoutActionPerformed

    private void jMenuItemAboutMeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAboutMeActionPerformed
        try {
            WebPage.openWebpage(new URL("https://github.com/thuythuyntt/DATN"));
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_jMenuItemAboutMeActionPerformed

    private void jMenuItemReconnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemReconnectActionPerformed
        String ipAddress = (String) JOptionPane.showInputDialog(this, "Địa chỉ IP: ", "Kết nối Server", JOptionPane.QUESTION_MESSAGE, null, null, SOCKET_SERVER_IP);
        sk.setHost(ipAddress);
        sk.connect(socketListener);
    }//GEN-LAST:event_jMenuItemReconnectActionPerformed

    private void panelAvatarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelAvatarMouseClicked
        showPersonInformation();
    }//GEN-LAST:event_panelAvatarMouseClicked

    private void lbSendNotificationMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbSendNotificationMouseClicked
        String noti = JOptionPane.showInputDialog(this, "Nội dung thông báo: ", "GỬI THÔNG BÁO", JOptionPane.PLAIN_MESSAGE);
        sk.sendMessage(new SocketMessage(SocketMessage.SEND_NOTIFICATION, noti));
    }//GEN-LAST:event_lbSendNotificationMouseClicked

    private void btnSearchLSHDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSearchLSHDMouseClicked
        String search = tfSearchLSHD.getText().toLowerCase().trim();
        filterList.clear();
        if (search == "") {
            filterList.addAll(list);
        } else {
            for (Student s : list) {
                if (s.getFullname().toLowerCase().contains(search)) {
                    filterList.add(s);
                }
            }
        }
        setupDataTab3();
    }//GEN-LAST:event_btnSearchLSHDMouseClicked

    private void cbSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbSelectAllActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbSelectAllActionPerformed

    private void lbTenCuocTroChuyenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbTenCuocTroChuyenMouseClicked

    }//GEN-LAST:event_lbTenCuocTroChuyenMouseClicked

    private void sendMessage() {
        String from = FirebaseHelper.getInstance().getAuthUser().getId();
        String to = toUserId;
        String text = areaNhapTinNhan.getText();
        String dateTime = getTimeNow();
        Message m = new Message(from, to, text, dateTime);

        if (FirebaseHelper.getInstance().sendMessage(m)) {
        } else {
            JOptionPane.showMessageDialog(this, "Gửi tin nhắn thất bại! :(", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        areaNhapTinNhan.setText("");
    }

    private String getTimeNow() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea areaNhapTinNhan;
    private javax.swing.JButton btnSearchLSHD;
    private javax.swing.JLabel btnSendFile;
    private javax.swing.JLabel btnSendImage;
    private javax.swing.JLabel btnSendMessage;
    private javax.swing.JCheckBox cbSelectAll;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenu jMenuHelp;
    private javax.swing.JMenuItem jMenuItemAboutMe;
    private javax.swing.JMenuItem jMenuItemChangePw;
    private javax.swing.JMenuItem jMenuItemLogout;
    private javax.swing.JMenuItem jMenuItemReconnect;
    private javax.swing.JMenu jMenuSetting;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JLabel lbSendNotification;
    private javax.swing.JLabel lbTenCuocTroChuyen;
    private javax.swing.JLabel lbTenGV;
    private javax.swing.JPanel panelAvatar;
    private javax.swing.JPanel panelChatInput;
    private javax.swing.JPanel panelHome;
    private javax.swing.JPanel panelLoading;
    private javax.swing.JPanel panelManagement;
    private javax.swing.JPanel panelStatistics;
    private javax.swing.JPanel scrollPaneNoiDungCuocTroChuyen;
    private javax.swing.JScrollPane scrollPaneOnline;
    private javax.swing.JTable tblDSSV;
    private javax.swing.JTable tblDSSVLSHD;
    private javax.swing.JTable tblDSSVOnline;
    private javax.swing.JTextField tfSearchLSHD;
    // End of variables declaration//GEN-END:variables
}
