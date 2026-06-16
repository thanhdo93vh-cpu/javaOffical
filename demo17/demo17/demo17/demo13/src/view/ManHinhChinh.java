/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import java.awt.CardLayout;
import model.TaiKhoan;
/**
 *
 * @author DO
 */
public class ManHinhChinh extends javax.swing.JFrame {
    private static final String FILE_PATH = "lophoc.dat";
    private java.util.List<model.LopHoc> dsLopHoc = new java.util.ArrayList<>();
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ManHinhChinh.class.getName());
    
    private CardLayout cardLayout;
    private javax.swing.JPanel pnlContent; // panel trung tâm chứa CardLayout

    // Khai báo các panel nội dung
    private PanelManHinhChinh panelManHinhChinh;
    private PanelNhapDuLieu panelNhapDuLieu;
    private PanelHienThiDuLieu panelHienThi;
    private PanelXuLyChucNang panelXuLy;
    
    private TaiKhoan taiKhoan;
    private PanelManHinhChinh pnlHome;
    private sideBarPanel sidebar;
    
    public ManHinhChinh(TaiKhoan taiKhoanDangNhap) {
        this.taiKhoan = taiKhoanDangNhap;
        initComponents();
        getContentPane().removeAll();
        getContentPane().setLayout(new java.awt.BorderLayout());
        setupCardLayout();
        setupSidebar();
        setSize(1100, 650);
        setLocationRelativeTo(null);
        
        if (!"ADMIN".equalsIgnoreCase(taiKhoan.getVaiTro())) {
            phanQuyenChoGiaoVien();
        }
    }
    
    public ManHinhChinh() {
        this(new TaiKhoan("Admin thử nghiệm", "1234@admin.com", "123456", "ADMIN"));
    }
    
    private void setupCardLayout() {
        // Tạo CardLayout và panel chứa nó
        cardLayout = new CardLayout();
        pnlContent = new javax.swing.JPanel(cardLayout);

        // Khởi tạo các panel nội dung
        panelManHinhChinh = new PanelManHinhChinh();
        // Truyền callback: sau khi thêm dữ liệu → tự động refresh các panel hiển thị
        panelNhapDuLieu = new PanelNhapDuLieu(() -> {
            panelManHinhChinh.refreshData();
            panelHienThi.refreshData();
        });
        panelHienThi    = new PanelHienThiDuLieu();
        panelXuLy       = new PanelXuLyChucNang(() -> {
            panelManHinhChinh.refreshData();
            panelHienThi.refreshData();
        });

        // Thêm vào CardLayout với tên định danh
        pnlContent.add(wrapPanel(panelManHinhChinh), "MAN_HINH_CHINH");
        pnlContent.add(wrapPanel(panelNhapDuLieu),   "NHAP_DU_LIEU");
        pnlContent.add(wrapPanel(panelHienThi),      "HIEN_THI");
        pnlContent.add(wrapPanel(panelXuLy),         "XU_LY");

        // Đặt pnlContent vào vùng Center của ManHinhChinh
        // (thay thế jPanel1 hiện tại, hoặc add trực tiếp vào jPanel1)
        
        
        getContentPane().add(pnlContent, java.awt.BorderLayout.CENTER);
        // Hiển thị màn hình mặc định khi khởi động
        cardLayout.show(pnlContent, "MAN_HINH_CHINH");
    }
    private javax.swing.JPanel wrapPanel(javax.swing.JPanel inner) {
        javax.swing.JPanel wrapper = new javax.swing.JPanel(new java.awt.BorderLayout());
        wrapper.add(inner, java.awt.BorderLayout.CENTER);
        return wrapper;
    }
    private void setupSidebar() {
        // Nhúng sideBarPanel vào vùng pnlSideBar (West)
        sidebar = new sideBarPanel(tenManHinh -> {
            cardLayout.show(pnlContent, tenManHinh);
            if ("MAN_HINH_CHINH".equalsIgnoreCase(tenManHinh)) {
                if (panelManHinhChinh != null) {
                    panelManHinhChinh.refreshData();
                }
            }
        }, taiKhoan);
        
        getContentPane().add(sidebar, java.awt.BorderLayout.LINE_START);
    }
    
    private void phanQuyenChoGiaoVien()
    {
        if (sidebar != null)
        {
            sidebar.phanQuyenGiaoVien(); 
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlSideBar = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1100, 650));

        pnlSideBar.setBackground(new java.awt.Color(255, 255, 255));
        pnlSideBar.setAutoscrolls(true);
        pnlSideBar.setName("0"); // NOI18N
        pnlSideBar.setPreferredSize(new java.awt.Dimension(220, 0));

        javax.swing.GroupLayout pnlSideBarLayout = new javax.swing.GroupLayout(pnlSideBar);
        pnlSideBar.setLayout(pnlSideBarLayout);
        pnlSideBarLayout.setHorizontalGroup(
            pnlSideBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
        );
        pnlSideBarLayout.setVerticalGroup(
            pnlSideBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 505, Short.MAX_VALUE)
        );

        getContentPane().add(pnlSideBar, java.awt.BorderLayout.LINE_START);

        jPanel1.setLayout(new java.awt.BorderLayout());
        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new ManHinhChinh().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel pnlSideBar;
    // End of variables declaration//GEN-END:variables
}
