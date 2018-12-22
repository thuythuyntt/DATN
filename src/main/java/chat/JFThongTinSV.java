/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import firebasedb.FirebaseHelper;
import model.User;

/**
 *
 * @author thuy
 */
public class JFThongTinSV extends javax.swing.JFrame {

    private boolean isEditting = false;

    private User mUser;

    public JFThongTinSV() {
        initComponents();
        setupData();
    }

    public void setupData() {
        if (mUser == null) {
            mUser = FirebaseHelper.getInstance().getAuthUser();
        }
        tfHoTen.setText(mUser.getFullname());
        tfLop.setText(mUser.getGroup());
        tfMaSV.setText(mUser.getCode());
        formattedtfNgaySinh.setText(mUser.getDob());
        tfSoDT.setText(mUser.getPhone());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelAvatar = new javax.swing.JPanel();
        formattedtfNgaySinh = new javax.swing.JFormattedTextField();
        jLabel1 = new javax.swing.JLabel();
        tfSoDT = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        btnOK = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        tfHoTen = new javax.swing.JTextField();
        tfLop = new javax.swing.JTextField();
        tfMaSV = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        panelAvatar.setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout panelAvatarLayout = new javax.swing.GroupLayout(panelAvatar);
        panelAvatar.setLayout(panelAvatarLayout);
        panelAvatarLayout.setHorizontalGroup(
            panelAvatarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        panelAvatarLayout.setVerticalGroup(
            panelAvatarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        formattedtfNgaySinh.setEnabled(false);
        formattedtfNgaySinh.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        formattedtfNgaySinh.setSelectionColor(new java.awt.Color(204, 204, 204));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel1.setText("Họ tên");

        tfSoDT.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        tfSoDT.setEnabled(false);
        tfSoDT.setSelectionColor(new java.awt.Color(204, 204, 204));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel2.setText("Lớp");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel3.setText("Mã sinh viên");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel4.setText("Ngày sinh");

        jLabel5.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel5.setText("Số điện thoại");

        btnOK.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        btnOK.setText("OK");
        btnOK.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOKMouseClicked(evt);
            }
        });

        btnEdit.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        btnEdit.setText("EDIT");
        btnEdit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEditMouseClicked(evt);
            }
        });

        tfHoTen.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        tfHoTen.setEnabled(false);
        tfHoTen.setSelectionColor(new java.awt.Color(204, 204, 204));

        tfLop.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        tfLop.setEnabled(false);
        tfLop.setSelectionColor(new java.awt.Color(204, 204, 204));

        tfMaSV.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        tfMaSV.setEnabled(false);
        tfMaSV.setSelectionColor(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnOK, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(139, 139, 139))
            .addGroup(layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addGap(51, 51, 51)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(tfLop, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(39, 39, 39)
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(tfMaSV, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(tfHoTen)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4))
                        .addGap(23, 23, 23)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(formattedtfNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tfSoDT, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(33, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelAvatar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(171, 171, 171))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelAvatar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tfHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tfMaSV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(tfLop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(formattedtfNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(tfSoDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOK)
                    .addComponent(btnEdit))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnOKMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOKMouseClicked
        if (!isEditting) {
            this.dispose();
        } else {
            this.setupData();
            this.setupView();
            isEditting = false;
        }
    }//GEN-LAST:event_btnOKMouseClicked

    private void btnEditMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMouseClicked
        if (!isEditting) {
            //edit
            tfHoTen.setEnabled(true);
            tfLop.setEnabled(true);
            tfMaSV.setEnabled(true);
            formattedtfNgaySinh.setEnabled(true);
            tfSoDT.setEnabled(true);
            btnOK.setText("CANCEL");
            btnEdit.setText("SAVE");
        } else {
            FirebaseHelper.getInstance().updatePersonInformation(tfMaSV.getText(),
                    formattedtfNgaySinh.getText(),
                    "",
                    tfLop.getText(),
                    tfHoTen.getText(),
                    tfSoDT.getText());
            this.setupData();
            this.setupView();
        }
        isEditting = !isEditting;
    }//GEN-LAST:event_btnEditMouseClicked

    public void setupView() {
        tfHoTen.setEnabled(false);
        tfLop.setEnabled(false);
        tfMaSV.setEnabled(false);
        formattedtfNgaySinh.setEnabled(false);
        tfSoDT.setEnabled(false);
        btnOK.setText("OK");
        btnEdit.setText("EDIT");
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnOK;
    private javax.swing.JFormattedTextField formattedtfNgaySinh;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel panelAvatar;
    private javax.swing.JTextField tfHoTen;
    private javax.swing.JTextField tfLop;
    private javax.swing.JTextField tfMaSV;
    private javax.swing.JTextField tfSoDT;
    // End of variables declaration//GEN-END:variables
}
