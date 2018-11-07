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
public class JFThongTinGV extends javax.swing.JFrame {

    /**
     * Creates new form JFTeacherInfomation
     */
    private boolean isEditting = false;

    private User mUser;

    public JFThongTinGV() {
        initComponents();
        setupData();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelAvatar = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        btnOK = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        tfHoTen = new javax.swing.JTextField();
        tfKhoa = new javax.swing.JTextField();
        tfMaGV = new javax.swing.JTextField();
        formattedtfNgaySinh = new javax.swing.JFormattedTextField();
        tfSoDT = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        panelAvatar.setBackground(new java.awt.Color(102, 102, 255));

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

        jLabel1.setFont(new java.awt.Font("Cambria Math", 0, 11)); // NOI18N
        jLabel1.setText("Họ tên");

        jLabel2.setFont(new java.awt.Font("Cambria Math", 0, 11)); // NOI18N
        jLabel2.setText("Khoa");

        jLabel3.setFont(new java.awt.Font("Cambria Math", 0, 11)); // NOI18N
        jLabel3.setText("Mã giáo viên");

        jLabel4.setFont(new java.awt.Font("Cambria Math", 0, 11)); // NOI18N
        jLabel4.setText("Ngày sinh");

        jLabel5.setFont(new java.awt.Font("Cambria Math", 0, 11)); // NOI18N
        jLabel5.setText("Số điện thoại");

        btnOK.setFont(new java.awt.Font("Cambria Math", 0, 11)); // NOI18N
        btnOK.setText("OK");
        btnOK.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOKMouseClicked(evt);
            }
        });

        btnEdit.setFont(new java.awt.Font("Cambria Math", 0, 11)); // NOI18N
        btnEdit.setText("EDIT");
        btnEdit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEditMouseClicked(evt);
            }
        });

        tfHoTen.setFont(new java.awt.Font("Cambria Math", 0, 11)); // NOI18N
        tfHoTen.setEnabled(false);

        tfKhoa.setFont(new java.awt.Font("Cambria Math", 0, 11)); // NOI18N
        tfKhoa.setEnabled(false);

        tfMaGV.setFont(new java.awt.Font("Cambria Math", 0, 11)); // NOI18N
        tfMaGV.setEnabled(false);

        formattedtfNgaySinh.setEnabled(false);

        tfSoDT.setFont(new java.awt.Font("Cambria Math", 0, 11)); // NOI18N
        tfSoDT.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(210, 210, 210)
                        .addComponent(panelAvatar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4))
                        .addGap(31, 31, 31)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tfKhoa, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
                            .addComponent(tfHoTen)
                            .addComponent(tfMaGV)
                            .addComponent(formattedtfNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tfSoDT, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnOK, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(66, 66, 66)))
                .addContainerGap(82, Short.MAX_VALUE))
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
                    .addComponent(tfKhoa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfMaGV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(formattedtfNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(tfSoDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOK)
                    .addComponent(btnEdit))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void setupData() {
        mUser = FirebaseHelper.getInstance().getAuthUser();
        tfHoTen.setText(mUser.getFullname());
        tfKhoa.setText(mUser.getFaculty());
        tfMaGV.setText(mUser.getCode());
        formattedtfNgaySinh.setText(mUser.getDob());
        tfSoDT.setText(mUser.getPhone());
    }


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
            tfKhoa.setEnabled(true);
            tfMaGV.setEnabled(true);
            formattedtfNgaySinh.setEnabled(true);
            tfSoDT.setEnabled(true);
            btnOK.setText("CANCEL");
            btnEdit.setText("SAVE");
        } else {
            FirebaseHelper.getInstance().updatePersonInformation(tfMaGV.getText(),
                    formattedtfNgaySinh.getText(),
                    tfKhoa.getText(),
                    "",
                    tfHoTen.getText(),
                    tfSoDT.getText());
            this.setupData();
            this.setupView();
        }
        isEditting = !isEditting;
    }//GEN-LAST:event_btnEditMouseClicked

    public void setupView() {
        tfHoTen.setEnabled(false);
        tfKhoa.setEnabled(false);
        tfMaGV.setEnabled(false);
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
    private javax.swing.JTextField tfKhoa;
    private javax.swing.JTextField tfMaGV;
    private javax.swing.JTextField tfSoDT;
    // End of variables declaration//GEN-END:variables
}
