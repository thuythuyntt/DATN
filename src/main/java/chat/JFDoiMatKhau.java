/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import firebasedb.FirebaseHelper;
import javax.swing.JOptionPane;

/**
 *
 * @author thuy
 */
public class JFDoiMatKhau extends javax.swing.JFrame {

    public JFDoiMatKhau() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        pfMKCu = new javax.swing.JPasswordField();
        pfMKMoi = new javax.swing.JPasswordField();
        pfXacNhanMKMoi = new javax.swing.JPasswordField();
        btnDoiMK = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Cambria Math", 0, 11)); // NOI18N
        jLabel1.setText("Mật khẩu hiện tại");

        jLabel2.setFont(new java.awt.Font("Cambria Math", 0, 11)); // NOI18N
        jLabel2.setText("Mật khẩu mới");

        jLabel4.setFont(new java.awt.Font("Cambria Math", 0, 11)); // NOI18N
        jLabel4.setText("Xác nhận mật khẩu mới");

        jLabel6.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        jLabel6.setText("ĐỔI MẬT KHẨU");

        btnDoiMK.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        btnDoiMK.setText("ĐỔI");
        btnDoiMK.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDoiMKMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(138, 138, 138)
                        .addComponent(jLabel6))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(pfXacNhanMKMoi))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2))
                                .addGap(48, 48, 48)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(pfMKCu)
                                    .addComponent(pfMKMoi, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(148, 148, 148)
                        .addComponent(btnDoiMK, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(36, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel6)
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(pfMKCu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(pfMKMoi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(pfXacNhanMKMoi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(btnDoiMK)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDoiMKMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDoiMKMouseClicked
        // TODO add your handling code here:
        if(pfMKCu.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "Chưa điền mật khẩu hiện tại", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(pfMKMoi.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "Chưa điền mật khẩu mới", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(pfXacNhanMKMoi.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "Chưa xác nhận mật khẩu mới", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!pfMKMoi.getText().equals(pfXacNhanMKMoi.getText())) {
            JOptionPane.showMessageDialog(this, "Xác nhận mật khẩu mới không đúng", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!pfMKCu.getText().equals(firebasedb.FirebaseHelper.getInstance().getAuthUser().getPassword())){
            JOptionPane.showMessageDialog(this, "Mật khẩu hiện tại không đúng", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        } 
        if (pfMKCu.getText().equals(pfMKMoi.getText())){
            JOptionPane.showMessageDialog(this, "Hãy đổi mật khẩu mới khác mật khẩu hiện tại", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        } 
        else {
            FirebaseHelper.getInstance().updatePassword(pfMKMoi.getText());
            JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công!", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
        }
        
    }//GEN-LAST:event_btnDoiMKMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDangNhap;
    private javax.swing.JButton btnDoiMK;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPasswordField pfMKCu;
    private javax.swing.JPasswordField pfMKMoi;
    private javax.swing.JPasswordField pfMatKhau;
    private javax.swing.JPasswordField pfXacNhanMKMoi;
    private javax.swing.JTextField tfTenDangNhap;
    // End of variables declaration//GEN-END:variables
}
