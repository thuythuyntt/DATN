/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import customview.MyAvatarPopup;
import customview.MyMouseListener;
import dangnhap.JFDangNhap;
import firebasedb.FirebaseHelper;
import java.awt.Adjustable;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import model.Message;
import model.User;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import util.Constants;
import util.Util;

/**
 *
 * @author nguyen.thi.thu.thuy
 */
public class JFTrangChu extends JFrameBase {

    /**
     * Creates new form JFHome
     */
    private String[] friendNames;
    private User user;
    private String toUserId = "";

    public JFTrangChu() {
        initComponents();
        initCustomComponents();
        setupData();
    }

    private void initCustomComponents() {
        panelAvatar.addMouseListener(new MyMouseListener(new MyAvatarPopup.OnClick() {
            @Override
            public void clickShowPersonInformation() {
                showPersonInformation();
            }

            @Override
            public void clickChangePassword() {
                showChangePwDialog();
            }

            @Override
            public void clickLogout() {
                logOut();
            }
        }));
//        BufferedImage img;
//        try {
//            img = ImageIO.read(new URL("http://www.java2s.com/style/download.png"));
//            ImageIcon icon = new ImageIcon(img.getScaledInstance(89, -1, Image.SCALE_SMOOTH));
//
//            jLabel1.setIcon(icon);
//        } catch (IOException ex) {
//            Logger.getLogger(JFTrangChu.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    private void setupData() {
        setupInfo();
        setupDSBanBe();
        setupPosIndex();
        listenMessageEvent();

    }

    private void setupInfo() {
        user = FirebaseHelper.getInstance().getAuthUser();
        lbTenGV.setText(user.getFullname());
        lbTenCuocTroChuyen.setText("ALL");
    }

    private void setupDSBanBe() {
        tblDSSVOnline.removeAll();
        tblDSSVOnline.revalidate();
        tblDSSVOnline.repaint();

        List<User> list = new ArrayList<User>();
        User all = new User();
        all.setId("");
        all.setFullname("ALL");
        list.add(all);
        list.addAll(FirebaseHelper.getInstance().getListFriends());
        DefaultTableModel model = (DefaultTableModel) tblDSSVOnline.getModel();
        
        Object[] row = new Object[1];
        for (int i = 0; i < list.size(); i++) {
            row[0] = list.get(i).getFullname();
            model.addRow(row);
        }

        if (user.getRole().equals(Constants.ROLE_TEACHER)) {
            tblDSSVOnline.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    int row = tblDSSVOnline.rowAtPoint(evt.getPoint());
                    toUserId = list.get(row).getId();
                    setupPosIndex();
                    lbTenCuocTroChuyen.setText(list.get(row).getFullname());
                    listenMessageEvent();
                }
            });
        }
    }

    private void setupPosIndex() {
        width = 370;
        posY = 10;
        padding = 8;
        margin = 8;
    }

    private void listenMessageEvent() {
        FirebaseHelper.getInstance().listenerRoomMessagesEvent(toUserId, new FirebaseHelper.RoomMessageChangeListener() {
            @Override
            public void onEvent(List<Message> list) {
                setupMessage(list);
            }
        });
    }

    private void setupMessage(List<Message> list) {
        scrollPaneNoiDungCuocTroChuyen.removeAll();
        scrollPaneNoiDungCuocTroChuyen.revalidate();
        scrollPaneNoiDungCuocTroChuyen.repaint();
        for (int i = 0; i < list.size(); i++) {
            Message m = list.get(i);
            addMessageToScrollPane(m);
        }
    }

    int width = 0;
    int posY = 0;
    int padding = 0;
    int margin = 0;

    private void addMessageToScrollPane(Message m) {
        JTextArea textArea = new JTextArea(m.getText());
        int height = Util.getContentHeight(width, m.getText());
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setSize(width, height + padding);
        textArea.setOpaque(true);
        textArea.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
        if (m.getFromUserId().equals(user.getId())) {
            textArea.setLocation(340, posY);
            textArea.setBackground(Color.decode("#90CAF9"));
        } else {
            textArea.setLocation(8, posY);
            textArea.setBackground(Color.decode("#B0BEC5"));
        }
        posY += padding * 2 + height + margin;

        scrollPaneNoiDungCuocTroChuyen.add(textArea);
        scrollPaneNoiDungCuocTroChuyen.setPreferredSize(new Dimension(750, posY));

        jScrollPane.repaint();

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
        Util.deleteFile(Constants.TMP_FILE_NAME);
        FirebaseHelper.getInstance().updateToken("");
        this.dispose();
        this.showScreen(new JFDangNhap());
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        panelHome = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        lbTenGV = new javax.swing.JLabel();
        panelAvatar = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
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
        panelManagement = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton2 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        panelStatistics = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTabbedPane1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(1000, 700));

        panelHome.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panelHome.setPreferredSize(new java.awt.Dimension(1000, 700));

        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(13, 71, 161), 2, true));
        jPanel1.setPreferredSize(new java.awt.Dimension(200, 670));

        lbTenGV.setFont(new java.awt.Font("Cambria Math", 0, 14)); // NOI18N
        lbTenGV.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbTenGV.setText("Nguyễn Trọng Khánh");
        lbTenGV.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lbTenGV.setPreferredSize(new java.awt.Dimension(100, 40));

        panelAvatar.setBackground(new java.awt.Color(204, 204, 204));
        panelAvatar.setPreferredSize(new java.awt.Dimension(140, 140));

        javax.swing.GroupLayout panelAvatarLayout = new javax.swing.GroupLayout(panelAvatar);
        panelAvatar.setLayout(panelAvatarLayout);
        panelAvatarLayout.setHorizontalGroup(
            panelAvatarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAvatarLayout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(0, 140, Short.MAX_VALUE))
        );
        panelAvatarLayout.setVerticalGroup(
            panelAvatarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAvatarLayout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(0, 140, Short.MAX_VALUE))
        );

        jTabbedPane2.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jTabbedPane2.setFont(new java.awt.Font("Cambria Math", 0, 12)); // NOI18N
        jTabbedPane2.setPreferredSize(new java.awt.Dimension(170, 400));

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
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lbTenGV, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(panelAvatar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
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
            .addGap(0, 409, Short.MAX_VALUE)
        );

        jScrollPane.setViewportView(scrollPaneNoiDungCuocTroChuyen);

        panelChatInput.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(13, 71, 161)));
        panelChatInput.setPreferredSize(new java.awt.Dimension(750, 190));

        jScrollPane2.setPreferredSize(new java.awt.Dimension(718, 100));

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
        btnSendMessage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ic_send_24.png"))); // NOI18N
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
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelChatInputLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSendFile)
                .addGap(49, 49, 49)
                .addComponent(btnSendImage)
                .addGap(49, 49, 49)
                .addComponent(btnSendMessage)
                .addGap(19, 19, 19))
        );
        panelChatInputLayout.setVerticalGroup(
            panelChatInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelChatInputLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelChatInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSendMessage)
                    .addGroup(panelChatInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnSendFile)
                        .addComponent(btnSendImage)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 740, Short.MAX_VALUE)
                    .addComponent(panelChatInput, javax.swing.GroupLayout.DEFAULT_SIZE, 740, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(lbTenCuocTroChuyen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(lbTenCuocTroChuyen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 413, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelChatInput, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout panelHomeLayout = new javax.swing.GroupLayout(panelHome);
        panelHome.setLayout(panelHomeLayout);
        panelHomeLayout.setHorizontalGroup(
            panelHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHomeLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelHomeLayout.setVerticalGroup(
            panelHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHomeLayout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(panelHomeLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 659, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Trang chủ", panelHome);

        panelManagement.setPreferredSize(new java.awt.Dimension(1000, 600));

        jLabel2.setText("QUẢN LÝ SINH VIÊN");

        jButton1.setText("Tìm kiếm");

        jLabel3.setText("Lọc:");

        jCheckBox1.setText("Online");

        jButton2.setText("OK");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "STT", "Họ tên", "Mã sinh viên", "Máy tính", "Thời gian", "Online"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(jTable1);

        javax.swing.GroupLayout panelManagementLayout = new javax.swing.GroupLayout(panelManagement);
        panelManagement.setLayout(panelManagementLayout);
        panelManagementLayout.setHorizontalGroup(
            panelManagementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelManagementLayout.createSequentialGroup()
                .addGap(164, 164, 164)
                .addGroup(panelManagementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelManagementLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox1)
                        .addGap(183, 183, 183)
                        .addComponent(jButton2))
                    .addGroup(panelManagementLayout.createSequentialGroup()
                        .addGroup(panelManagementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26)
                        .addComponent(jButton1)))
                .addContainerGap(478, Short.MAX_VALUE))
            .addGroup(panelManagementLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4)
                .addContainerGap())
        );
        panelManagementLayout.setVerticalGroup(
            panelManagementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelManagementLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(43, 43, 43)
                .addGroup(panelManagementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                    .addComponent(jTextField1))
                .addGap(31, 31, 31)
                .addGroup(panelManagementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jCheckBox1)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Quản lý", panelManagement);

        panelStatistics.setPreferredSize(new java.awt.Dimension(1000, 600));

        javax.swing.GroupLayout panelStatisticsLayout = new javax.swing.GroupLayout(panelStatistics);
        panelStatistics.setLayout(panelStatisticsLayout);
        panelStatisticsLayout.setHorizontalGroup(
            panelStatisticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 995, Short.MAX_VALUE)
        );
        panelStatisticsLayout.setVerticalGroup(
            panelStatisticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 682, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Thống kê và tổng hợp", panelStatistics);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

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

    private void sendMessage() {
        String from = FirebaseHelper.getInstance().getAuthUser().getId();
        String to = toUserId;
        String text = areaNhapTinNhan.getText();
        String dateTime = getTimeNow();
        Message m = new Message(from, to, text, dateTime);

        if (FirebaseHelper.getInstance().sendMessage(m)) {
            System.out.println("Gửi tin nhắn thành công");
        } else {
            System.out.println("Gửi tin nhắn thất bại");
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
    private javax.swing.JLabel btnSendFile;
    private javax.swing.JLabel btnSendImage;
    private javax.swing.JLabel btnSendMessage;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel lbTenCuocTroChuyen;
    private javax.swing.JLabel lbTenGV;
    private javax.swing.JPanel panelAvatar;
    private javax.swing.JPanel panelChatInput;
    private javax.swing.JPanel panelHome;
    private javax.swing.JPanel panelManagement;
    private javax.swing.JPanel panelStatistics;
    private javax.swing.JPanel scrollPaneNoiDungCuocTroChuyen;
    private javax.swing.JScrollPane scrollPaneOnline;
    private javax.swing.JTable tblDSSVOnline;
    // End of variables declaration//GEN-END:variables
}