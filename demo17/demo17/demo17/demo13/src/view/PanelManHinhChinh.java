/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import java.util.ArrayList;
import java.util.List;
import model.LopHoc;
import model.SinhVien;

/**
 *
 * @author DO
 */
public class PanelManHinhChinh extends javax.swing.JPanel {

    /**
     * Creates new form PanelManHinhChinh
     */
    List<LopHoc> dsLopHoc = new ArrayList<>();
    public PanelManHinhChinh() {
        initComponents();
        loadThongKe();
        
        tblTongQuanLop.addMouseMotionListener(new java.awt.event.MouseMotionAdapter()
        {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent evt)
            {
                int cot = tblTongQuanLop.columnAtPoint(evt.getPoint());
                if (cot == 5)
                {
                    tblTongQuanLop.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                }
                else
                {
                    tblTongQuanLop.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                }
            }
        });
        
        tblTongQuanLop.addMouseListener(new java.awt.event.MouseAdapter() //Bật cửa sổ hiện thị danh sách sinh viên dưới dạng của sổ phụ
        {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                int dong = tblTongQuanLop.rowAtPoint(evt.getPoint());
                int cot = tblTongQuanLop.columnAtPoint(evt.getPoint());

                if (cot == 5 && dong != -1) { 
                    LopHoc lhChon = dsLopHoc.get(dong);
                    List<SinhVien> dsSV = lhChon.getDanhSachLop();

                    ManHinhDanhSachSV hienThiDS = new ManHinhDanhSachSV(
                        (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(tblTongQuanLop), 
                        true, //Phải thoát mới điều khiển được ManHinhQuanLyLopHoc
                        dsSV, 
                        lhChon.getTenLop()
                    );
                    hienThiDS.setVisible(true);
                }
            }
        });
        
        // Auto-reload dữ liệu khi tab được bật
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent evt) {
                refreshData();
            }
        });
    }
    /** Gọi phương thức này từ bên ngoài để tải lại thống kê và bảng */
    public void refreshData() {
        loadThongKe();
    }
    private void loadThongKe() {
        java.util.List<model.LopHoc> dsLop = util.QuanLyFile.readFile("lophoc.txt");
        java.util.List<model.SinhVien> dsSV = util.QuanLyFile.readFile("sinhvien.txt");
        java.util.List<model.GiangVien> dsGV = util.QuanLyFile.readFile("giangvien.txt");
        
        this.dsLopHoc = dsLop;    
        
        // Gán dữ liệu số lượng lên các thẻ Panel màu
        lblLopHoc.setText(String.valueOf(dsLop.size()));
        lblSinhVien.setText(String.valueOf(dsSV.size()));
        lblGiangVien.setText(String.valueOf(dsGV.size()));

        // Đổ dữ liệu vào bảng danh sách tổng quan
        javax.swing.table.DefaultTableModel modelLop = (javax.swing.table.DefaultTableModel) tblTongQuanLop.getModel();
        modelLop.setRowCount(0); 
        for (model.LopHoc lop : dsLop) {
            String tenGV = lop.getGiangVienDay() != null ? lop.getGiangVienDay().getTenGV() : "Chưa có";
            int siSo = lop.getDanhSachLop() != null ? lop.getDanhSachLop().size() : 0;
            // Bổ sung chuỗi dữ liệu "Xem" cuối cùng để khớp cấu trúc 6 cột thiết kế
            modelLop.addRow(new Object[]{lop.getMaLop(), lop.getTenLop(), tenGV, lop.getKhoa(), siSo, "<html><a href=\"\">Xem chi tiết</a></html> "});
        }

        // Tìm sĩ số lớp đông nhất và ít nhất trong danh sách dữ liệu thực tế
        int maxSiSo = -1;
        int minSiSo = Integer.MAX_VALUE;
        for (model.LopHoc lop : dsLop) {
            int siSo = lop.getDanhSachLop() != null ? lop.getDanhSachLop().size() : 0;
            if (siSo > maxSiSo) maxSiSo = siSo;
            if (siSo < minSiSo) minSiSo = siSo;
        }
        if (minSiSo == Integer.MAX_VALUE) minSiSo = 0;

        // Đổ dữ liệu chi tiết vào hai bảng phụ dựa trên sĩ số vừa tìm được
        javax.swing.table.DefaultTableModel modelDong = (javax.swing.table.DefaultTableModel) tblDongData.getModel();
        javax.swing.table.DefaultTableModel modelIt = (javax.swing.table.DefaultTableModel) tblItData.getModel();
        modelDong.setRowCount(0);
        modelIt.setRowCount(0);

        for (model.LopHoc lop : dsLop) {
            int siSo = lop.getDanhSachLop() != null ? lop.getDanhSachLop().size() : 0;
            String tenGV = lop.getGiangVienDay() != null ? lop.getGiangVienDay().getTenGV() : "Chưa có";

            if (siSo == maxSiSo && !dsLop.isEmpty()) {
                modelDong.addRow(new Object[]{lop.getMaLop(), lop.getTenLop(), lop.getKhoa(), siSo, tenGV});
            }
            if (siSo == minSiSo && !dsLop.isEmpty()) {
                modelIt.addRow(new Object[]{lop.getMaLop(), lop.getTenLop(), lop.getKhoa(), siSo, tenGV});
            }
        }

        // Đổ dữ liệu danh sách giảng viên tổng quan kèm theo Khoa công tác
        javax.swing.table.DefaultTableModel modelGV = (javax.swing.table.DefaultTableModel) tblTheoGV.getModel();
        modelGV.setRowCount(0);
        for (model.GiangVien gv : dsGV) {
            modelGV.addRow(new Object[]{gv.getMaGV(), gv.getTenGV(), gv.getEmail(), gv.getKhoa()});
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        pnlHeader = new javax.swing.JPanel();
        lblTieuDe = new javax.swing.JLabel();
        pnlThongKe = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        pnlCards = new javax.swing.JPanel();
        cardLop = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        lblLopHoc = new javax.swing.JLabel();
        cardSV = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        lblSinhVien = new javax.swing.JLabel();
        cardGV = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        lblGiangVien = new javax.swing.JLabel();
        tabThongKe = new javax.swing.JTabbedPane();
        tabPnlTongQuan = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTongQuanLop = new javax.swing.JTable();
        tab2 = new javax.swing.JPanel();
        tblDongNhat = new javax.swing.JScrollPane();
        tblDongData = new javax.swing.JTable();
        tblItNhat = new javax.swing.JScrollPane();
        tblItData = new javax.swing.JTable();
        tab3 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblTheoGV = new javax.swing.JTable();

        setPreferredSize(new java.awt.Dimension(678, 358));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        pnlHeader.setBackground(new java.awt.Color(51, 102, 255));
        pnlHeader.setPreferredSize(new java.awt.Dimension(500, 100));

        lblTieuDe.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTieuDe.setForeground(new java.awt.Color(255, 255, 255));
        lblTieuDe.setText("Tổng quan hệ thống");
        pnlHeader.add(lblTieuDe);

        jPanel1.add(pnlHeader, java.awt.BorderLayout.PAGE_START);

        pnlThongKe.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.BorderLayout());

        pnlCards.setBackground(new java.awt.Color(255, 255, 255));
        pnlCards.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 16, 12, 16));
        pnlCards.setForeground(new java.awt.Color(255, 255, 255));
        pnlCards.setPreferredSize(new java.awt.Dimension(0, 100));
        pnlCards.setLayout(new java.awt.GridLayout(1, 3, 12, 0));

        cardLop.setBackground(new java.awt.Color(51, 51, 255));
        cardLop.setLayout(new java.awt.BorderLayout());

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Tổng lớp học");
        cardLop.add(jLabel2, java.awt.BorderLayout.PAGE_START);

        lblLopHoc.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lblLopHoc.setForeground(new java.awt.Color(255, 255, 255));
        lblLopHoc.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        cardLop.add(lblLopHoc, java.awt.BorderLayout.CENTER);

        pnlCards.add(cardLop);

        cardSV.setBackground(new java.awt.Color(51, 255, 51));
        cardSV.setLayout(new java.awt.BorderLayout());

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Tổng sinh viên");
        cardSV.add(jLabel3, java.awt.BorderLayout.PAGE_START);

        lblSinhVien.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lblSinhVien.setForeground(new java.awt.Color(255, 255, 255));
        lblSinhVien.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        cardSV.add(lblSinhVien, java.awt.BorderLayout.CENTER);

        pnlCards.add(cardSV);

        cardGV.setBackground(new java.awt.Color(255, 153, 51));
        cardGV.setLayout(new java.awt.BorderLayout());

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Tổng giảng viên");
        cardGV.add(jLabel4, java.awt.BorderLayout.PAGE_START);

        lblGiangVien.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lblGiangVien.setForeground(new java.awt.Color(255, 255, 255));
        lblGiangVien.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        cardGV.add(lblGiangVien, java.awt.BorderLayout.CENTER);

        pnlCards.add(cardGV);

        jPanel2.add(pnlCards, java.awt.BorderLayout.PAGE_START);

        tabPnlTongQuan.setLayout(new java.awt.BorderLayout());

        tblTongQuanLop.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Mã lớp", "Tên lớp", "GVCN", "Khoa", "Sĩ số", "Chi tiết"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblTongQuanLop);

        tabPnlTongQuan.add(jScrollPane1, java.awt.BorderLayout.PAGE_START);

        tabThongKe.addTab("Danh sách lớp", tabPnlTongQuan);

        tab2.setLayout(new java.awt.GridLayout(2, 1, 0, 12));

        tblDongNhat.setBorder(javax.swing.BorderFactory.createTitledBorder("Lớp đông nhất"));

        tblDongData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã lớp", "Tên lớp", "Khoa", "Sĩ số"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDongNhat.setViewportView(tblDongData);

        tab2.add(tblDongNhat);

        tblItNhat.setBorder(javax.swing.BorderFactory.createTitledBorder("Lớp ít nhất"));

        tblItData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã lớp", "Tên lớp", "Khoa", "Sĩ số"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblItNhat.setViewportView(tblItData);

        tab2.add(tblItNhat);

        tabThongKe.addTab("Lớp đông / ít nhất", tab2);

        tab3.setLayout(new java.awt.BorderLayout());

        jPanel3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tab3.add(jPanel3, java.awt.BorderLayout.PAGE_START);

        tblTheoGV.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã GV", "Tên GV", "Email", "Khoa"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblTheoGV);

        tab3.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        tabThongKe.addTab("Danh sách giảng viên", tab3);

        jPanel2.add(tabThongKe, java.awt.BorderLayout.CENTER);

        pnlThongKe.add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel1.add(pnlThongKe, java.awt.BorderLayout.CENTER);

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel cardGV;
    private javax.swing.JPanel cardLop;
    private javax.swing.JPanel cardSV;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblGiangVien;
    private javax.swing.JLabel lblLopHoc;
    private javax.swing.JLabel lblSinhVien;
    private javax.swing.JLabel lblTieuDe;
    private javax.swing.JPanel pnlCards;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlThongKe;
    private javax.swing.JPanel tab2;
    private javax.swing.JPanel tab3;
    private javax.swing.JPanel tabPnlTongQuan;
    private javax.swing.JTabbedPane tabThongKe;
    private javax.swing.JTable tblDongData;
    private javax.swing.JScrollPane tblDongNhat;
    private javax.swing.JTable tblItData;
    private javax.swing.JScrollPane tblItNhat;
    private javax.swing.JTable tblTheoGV;
    private javax.swing.JTable tblTongQuanLop;
    // End of variables declaration//GEN-END:variables
}
