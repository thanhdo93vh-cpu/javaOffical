/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

/**
 *
 * @author DO
 */
public class PanelHienThiDuLieu extends javax.swing.JPanel {

    /**
     * Creates new form ManHinhHienThiDuLieu2
     */
    
    private controller.LopHocController lopCtrl = new controller.LopHocController();
    private controller.GiangVienController gvCtrl = new controller.GiangVienController();
    private controller.SinhVienController svCtrl = new controller.SinhVienController();
    public PanelHienThiDuLieu() {
        initComponents();
        setLayout(new java.awt.BorderLayout());
        add(jPanel2, java.awt.BorderLayout.CENTER);
        loadDataToTables();
        
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent evt) {
                refreshData();
            }
        });
    }
    
    /** Gọi phương thức này từ bên ngoài để tải lại toàn bộ dữ liệu vào bảng */
    public void refreshData() {
        lopCtrl.reload(); 
        gvCtrl.reload();
        svCtrl.reload();
        loadDataToTables();         
    }
    
    private void loadDataToTables() {
        javax.swing.table.DefaultTableModel modelLop = (javax.swing.table.DefaultTableModel) tblHienThiLop.getModel();
        modelLop.setRowCount(0); 
        for (Object[] row : lopCtrl.getDuLieuBang()) modelLop.addRow(row);

        javax.swing.table.DefaultTableModel modelGV = (javax.swing.table.DefaultTableModel) tblHienThiGV.getModel();
        modelGV.setRowCount(0);
        for (Object[] row : gvCtrl.getDuLieuBang(lopCtrl.getDanhSachLop())) modelGV.addRow(row);

        javax.swing.table.DefaultTableModel modelSV = (javax.swing.table.DefaultTableModel) tblHienThiSV.getModel();
        modelSV.setRowCount(0);
        for (Object[] row : svCtrl.getDuLieuBang(lopCtrl.getDanhSachLop())) modelSV.addRow(row);
    }
    
    private void thucHienXoa(String loai, String id) {
        try {
            if (loai.equals("LỚP HỌC")) {
                java.util.List<model.LopHoc> dsLop = util.QuanLyFile.readFile("lophoc.txt");
                dsLop.removeIf(lop -> lop.getMaLop().equals(id));
                util.QuanLyFile.ghiFile(new java.io.File("lophoc.txt"), dsLop);
                
            } else if (loai.equals("GIẢNG VIÊN")) {
                java.util.List<model.GiangVien> dsGV = util.QuanLyFile.readFile("giangvien.txt");
                dsGV.removeIf(gv -> gv.getMaGV().equals(id));
                util.QuanLyFile.ghiFile(new java.io.File("giangvien.txt"), dsGV);
                
            } else if (loai.equals("SINH VIÊN")) {
                java.util.List<model.SinhVien> dsSV = util.QuanLyFile.readFile("sinhvien.txt");
                dsSV.removeIf(sv -> sv.getMaSV().equals(id));
                util.QuanLyFile.ghiFile(new java.io.File("sinhvien.txt"), dsSV);
                
                // Đồng bộ: xóa SV này khỏi danh sách của Lớp học
                java.util.List<model.LopHoc> dsLop = util.QuanLyFile.readFile("lophoc.txt");
                for (model.LopHoc lop : dsLop) {
                    if (lop.getDanhSachLop() != null) {
                        lop.getDanhSachLop().removeIf(sv -> sv.getMaSV().equals(id));
                    }
                }
                util.QuanLyFile.ghiFile(new java.io.File("lophoc.txt"), dsLop);
            }
            javax.swing.JOptionPane.showMessageDialog(this, "Xóa dữ liệu thành công!");
            refreshData(); 
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Lỗi khi xóa: " + e.getMessage());
        }
    }
    
    private void thucHienSua(String loai, String id) {
        try {
            String[] dsKhoa = {"Công nghệ Thông tin và Truyền thông", "Điện - Điện tử", "Cơ khí - Ô tô", "Kinh tế", "Ngoại ngữ - Du lịch"};
            
            if (loai.equals("LỚP HỌC")) {
                java.util.List<model.LopHoc> dsLop = util.QuanLyFile.readFile("lophoc.txt");
                model.LopHoc target = dsLop.stream().filter(l -> l.getMaLop().equals(id)).findFirst().orElse(null);
                if (target != null) {
                    javax.swing.JTextField txtTen = new javax.swing.JTextField(target.getTenLop());
                    javax.swing.JComboBox<String> cboKhoa = new javax.swing.JComboBox<>(dsKhoa);
                    cboKhoa.setSelectedItem(target.getKhoa());
                    
                    Object[] msg = {"Tên lớp học:", txtTen, "Khoa:", cboKhoa};
                    int result = javax.swing.JOptionPane.showConfirmDialog(this, msg, "Sửa Lớp Học", javax.swing.JOptionPane.OK_CANCEL_OPTION);
                    if (result == javax.swing.JOptionPane.OK_OPTION) {
                        target.setTenLop(txtTen.getText());
                        target.setKhoa(cboKhoa.getSelectedItem().toString());
                        util.QuanLyFile.ghiFile(new java.io.File("lophoc.txt"), dsLop);
                        refreshData();
                        javax.swing.JOptionPane.showMessageDialog(this, "Sửa thành công!");
                    }
                }
            } else if (loai.equals("GIẢNG VIÊN")) {
                java.util.List<model.GiangVien> dsGV = util.QuanLyFile.readFile("giangvien.txt");
                model.GiangVien target = dsGV.stream().filter(g -> g.getMaGV().equals(id)).findFirst().orElse(null);
                if (target != null) {
                    javax.swing.JTextField txtTen = new javax.swing.JTextField(target.getTenGV());
                    javax.swing.JTextField txtEmail = new javax.swing.JTextField(target.getEmail());
                    javax.swing.JComboBox<String> cboKhoa = new javax.swing.JComboBox<>(dsKhoa);
                    cboKhoa.setSelectedItem(target.getKhoa());
                    
                    Object[] msg = {"Họ tên GV:", txtTen, "Email:", txtEmail, "Khoa:", cboKhoa};
                    int result = javax.swing.JOptionPane.showConfirmDialog(this, msg, "Sửa Giảng Viên", javax.swing.JOptionPane.OK_CANCEL_OPTION);
                    if (result == javax.swing.JOptionPane.OK_OPTION) {
                        target.setTenGV(txtTen.getText());
                        target.setEmail(txtEmail.getText());
                        target.setKhoa(cboKhoa.getSelectedItem().toString());
                        util.QuanLyFile.ghiFile(new java.io.File("giangvien.txt"), dsGV);
                        
                        // Đồng bộ tên GV trong Lớp học nếu GV này đang chủ nhiệm
                        java.util.List<model.LopHoc> dsLop = util.QuanLyFile.readFile("lophoc.txt");
                        for(model.LopHoc lop : dsLop) {
                            if(lop.getGiangVienDay() != null && lop.getGiangVienDay().getMaGV().equals(id)) {
                                lop.getGiangVienDay().setTenGV(txtTen.getText());
                            }
                        }
                        util.QuanLyFile.ghiFile(new java.io.File("lophoc.txt"), dsLop);
                        refreshData();
                        javax.swing.JOptionPane.showMessageDialog(this, "Sửa thành công!");
                    }
                }
            } else if (loai.equals("SINH VIÊN")) {
                java.util.List<model.SinhVien> dsSV = util.QuanLyFile.readFile("sinhvien.txt");
                model.SinhVien target = dsSV.stream().filter(s -> s.getMaSV().equals(id)).findFirst().orElse(null);
                if (target != null) {
                    javax.swing.JTextField txtTen = new javax.swing.JTextField(target.getTenSV());
                    javax.swing.JComboBox<String> cboTrangThai = new javax.swing.JComboBox<>(new String[]{"Đang học", "Bảo lưu", "Đã tốt nghiệp", "Thôi học"});
                    cboTrangThai.setSelectedItem(target.getTrangThai());
                    
                    Object[] msg = {"Họ tên SV:", txtTen, "Trạng thái:", cboTrangThai};
                    int result = javax.swing.JOptionPane.showConfirmDialog(this, msg, "Sửa Sinh Viên", javax.swing.JOptionPane.OK_CANCEL_OPTION);
                    if (result == javax.swing.JOptionPane.OK_OPTION) {
                        target.setTenSV(txtTen.getText());
                        target.setTrangThai(cboTrangThai.getSelectedItem().toString());
                        util.QuanLyFile.ghiFile(new java.io.File("sinhvien.txt"), dsSV);
                        
                        // Đồng bộ tên SV trong danh sách lớp học
                        java.util.List<model.LopHoc> dsLop = util.QuanLyFile.readFile("lophoc.txt");
                        for(model.LopHoc lop : dsLop) {
                            if(lop.getDanhSachLop() != null) {
                                for(model.SinhVien svLop : lop.getDanhSachLop()) {
                                    if(svLop.getMaSV().equals(id)) {
                                        svLop.setTenSV(txtTen.getText());
                                        svLop.setTrangThai(cboTrangThai.getSelectedItem().toString());
                                    }
                                }
                            }
                        }
                        util.QuanLyFile.ghiFile(new java.io.File("lophoc.txt"), dsLop);
                        refreshData();
                        javax.swing.JOptionPane.showMessageDialog(this, "Sửa thành công!");
                    }
                }
            }
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Lỗi khi sửa: " + e.getMessage());
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

        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        lblTieuDe = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        txtTimKiem = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        btnLoc = new javax.swing.JButton();
        btnSuaLop = new javax.swing.JButton();
        btnXoaLop = new javax.swing.JButton();
        btnLamMoiLop = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHienThiLop = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        txtTimKiemGV = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        btnLocGV = new javax.swing.JButton();
        btnSuaGV = new javax.swing.JButton();
        btnXoaGV = new javax.swing.JButton();
        btnLamMoiGV = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblHienThiGV = new javax.swing.JTable();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        txtTimKiemSV = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        btnLocSV = new javax.swing.JButton();
        btnSuaSV = new javax.swing.JButton();
        btnXoaSV = new javax.swing.JButton();
        btnLamMoiSV = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblHienThiSV = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.BorderLayout());

        lblTieuDe.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTieuDe.setText("DANH SÁCH");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(203, 203, 203)
                .addComponent(lblTieuDe)
                .addContainerGap(337, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(lblTieuDe)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel3, java.awt.BorderLayout.PAGE_START);

        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel5.setLayout(new java.awt.BorderLayout());

        txtTimKiem.addActionListener(this::txtTimKiemActionPerformed);

        jLabel2.setText("Tìm theo mã lớp hoặc tên lớp hoặc GVCN:");

        btnLoc.setText("Lọc");
        btnLoc.addActionListener(this::btnLocActionPerformed);

        btnSuaLop.setText("Sửa lớp");
        btnSuaLop.addActionListener(this::btnSuaLopActionPerformed);

        btnXoaLop.setText("Xóa lớp");
        btnXoaLop.addActionListener(this::btnXoaLopActionPerformed);

        btnLamMoiLop.setText("Làm mới");
        btnLamMoiLop.addActionListener(this::btnLamMoiLopActionPerformed);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addComponent(btnLoc)
                .addGap(18, 18, 18)
                .addComponent(btnSuaLop)
                .addGap(18, 18, 18)
                .addComponent(btnXoaLop)
                .addGap(18, 18, 18)
                .addComponent(btnLamMoiLop)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLoc)
                    .addComponent(btnSuaLop)
                    .addComponent(btnXoaLop)
                    .addComponent(btnLamMoiLop))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel6, java.awt.BorderLayout.PAGE_START);

        tblHienThiLop.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Mã lớp", "Tên lớp", "Khoa", "Sĩ số", "Giáo viên chủ nhiệm"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblHienThiLop);

        jPanel5.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Lớp học", jPanel5);

        jPanel7.setLayout(new java.awt.BorderLayout());

        txtTimKiemGV.addActionListener(this::txtTimKiemGVActionPerformed);

        jLabel3.setText("Tìm theo mã GV hoặc họ tên:");

        btnLocGV.setText("Lọc");
        btnLocGV.addActionListener(this::btnLocGVActionPerformed);

        btnSuaGV.setText("Sửa GV");
        btnSuaGV.addActionListener(this::btnSuaGVActionPerformed);

        btnXoaGV.setText("Xóa GV");
        btnXoaGV.addActionListener(this::btnXoaGVActionPerformed);

        btnLamMoiGV.setText("Làm mới");
        btnLamMoiGV.addActionListener(this::btnLamMoiGVActionPerformed);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(89, 89, 89)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtTimKiemGV))
                .addGap(38, 38, 38)
                .addComponent(btnLocGV)
                .addGap(18, 18, 18)
                .addComponent(btnSuaGV)
                .addGap(18, 18, 18)
                .addComponent(btnXoaGV)
                .addGap(18, 18, 18)
                .addComponent(btnLamMoiGV)
                .addContainerGap(45, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTimKiemGV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLocGV)
                    .addComponent(btnSuaGV)
                    .addComponent(btnXoaGV)
                    .addComponent(btnLamMoiGV))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        jPanel7.add(jPanel8, java.awt.BorderLayout.PAGE_START);

        tblHienThiGV.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Mã GV", "Họ tên", "Khoa", "Lớp chủ nhiệm", "Email"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblHienThiGV);

        jPanel7.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Giảng viên", jPanel7);

        jPanel9.setLayout(new java.awt.BorderLayout());

        txtTimKiemSV.addActionListener(this::txtTimKiemSVActionPerformed);

        jLabel4.setText("Tìm theo mã SV hoặc họ tên:");

        btnLocSV.setText("Lọc");
        btnLocSV.addActionListener(this::btnLocSVActionPerformed);

        btnSuaSV.setText("Sửa SV");
        btnSuaSV.addActionListener(this::btnSuaSVActionPerformed);

        btnXoaSV.setText("Xóa SV");
        btnXoaSV.addActionListener(this::btnXoaSVActionPerformed);

        btnLamMoiSV.setText("Làm mới");
        btnLamMoiSV.addActionListener(this::btnLamMoiSVActionPerformed);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(89, 89, 89)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtTimKiemSV))
                .addGap(38, 38, 38)
                .addComponent(btnLocSV)
                .addGap(18, 18, 18)
                .addComponent(btnSuaSV)
                .addGap(18, 18, 18)
                .addComponent(btnXoaSV)
                .addGap(18, 18, 18)
                .addComponent(btnLamMoiSV)
                .addContainerGap(47, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTimKiemSV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLocSV)
                    .addComponent(btnSuaSV)
                    .addComponent(btnXoaSV)
                    .addComponent(btnLamMoiSV))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        jPanel9.add(jPanel10, java.awt.BorderLayout.PAGE_START);

        tblHienThiSV.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã SV", "Họ tên", "Khoa", "Lớp"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tblHienThiSV);

        jPanel9.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Sinh viên", jPanel9);

        jPanel4.add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel4, java.awt.BorderLayout.CENTER);

        add(jPanel2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void txtTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKiemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKiemActionPerformed

    private void btnLocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLocActionPerformed
        // TODO add your handling code here:
        String tuKhoa = txtTimKiem.getText().trim();
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) tblHienThiLop.getModel();
        javax.swing.table.TableRowSorter<javax.swing.table.DefaultTableModel> sorter = new javax.swing.table.TableRowSorter<>(model);
        tblHienThiLop.setRowSorter(sorter);

        if (tuKhoa.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            // Áp dụng bộ lọc tìm kiếm không phân biệt chữ hoa chữ thường (?i)
            sorter.setRowFilter(javax.swing.RowFilter.regexFilter("(?i)" + tuKhoa));

            // 🌟 BỔ SUNG: Kiểm tra số dòng hiển thị sau khi lọc trên giao diện
            if (tblHienThiLop.getRowCount() == 0) {
                javax.swing.JOptionPane.showMessageDialog(this, 
                        "Không tìm thấy kết quả phù hợp cho từ khóa: \"" + tuKhoa + "\"", 
                        "Thông báo", 
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);

                // Tùy chọn (Hữu ích cho UI): Hủy bộ lọc để bảng tự động hiển thị lại toàn bộ danh sách cũ
                sorter.setRowFilter(null); 
                txtTimKiem.requestFocus();
                txtTimKiem.selectAll(); // Bôi đen từ khóa lỗi để người dùng gõ từ mới nhanh hơn
            }
        }
    }//GEN-LAST:event_btnLocActionPerformed

    private void txtTimKiemGVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKiemGVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKiemGVActionPerformed

    private void btnLocGVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLocGVActionPerformed
        // TODO add your handling code here:
        String tuKhoa = txtTimKiemGV.getText().trim();
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) tblHienThiGV.getModel();
        javax.swing.table.TableRowSorter<javax.swing.table.DefaultTableModel> sorter = new javax.swing.table.TableRowSorter<>(model);
        tblHienThiGV.setRowSorter(sorter);
        
        if (tuKhoa.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(javax.swing.RowFilter.regexFilter("(?i)" + tuKhoa));
        }
    }//GEN-LAST:event_btnLocGVActionPerformed

    private void txtTimKiemSVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKiemSVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKiemSVActionPerformed

    private void btnLocSVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLocSVActionPerformed
        // TODO add your handling code here:
        String tuKhoa = txtTimKiemSV.getText().trim();
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) tblHienThiSV.getModel();
        javax.swing.table.TableRowSorter<javax.swing.table.DefaultTableModel> sorter = new javax.swing.table.TableRowSorter<>(model);
        tblHienThiSV.setRowSorter(sorter);
        
        if (tuKhoa.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(javax.swing.RowFilter.regexFilter("(?i)" + tuKhoa));
        }
    }//GEN-LAST:event_btnLocSVActionPerformed

    private void btnXoaLopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaLopActionPerformed
        // TODO add your handling code here:
        int row = tblHienThiLop.getSelectedRow();
        if (row == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng chọn một lớp học trong bảng để xóa!");
            return;
        }
        int confirm = javax.swing.JOptionPane.showConfirmDialog(this, "Chắc chắn xóa lớp này khỏi hệ thống?", "Xác nhận", javax.swing.JOptionPane.YES_NO_OPTION);
        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            try {
                lopCtrl.xoaLop(tblHienThiLop.getValueAt(row, 0).toString());
                javax.swing.JOptionPane.showMessageDialog(this, "Xóa dữ liệu thành công!");
                refreshData();
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(this, "Lỗi khi xóa: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_btnXoaLopActionPerformed

    private void btnSuaGVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaGVActionPerformed
        // TODO add your handling code here:
        int row = tblHienThiGV.getSelectedRow();
        if (row == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng chọn một giảng viên để sửa!");
            return;
        }
        
        String idGV = tblHienThiGV.getValueAt(row, 0).toString();
        javax.swing.JTextField txtTen = new javax.swing.JTextField(tblHienThiGV.getValueAt(row, 1).toString());
        javax.swing.JTextField txtEmail = new javax.swing.JTextField(tblHienThiGV.getValueAt(row, 4).toString());
        javax.swing.JComboBox<String> cboKhoa = new javax.swing.JComboBox<>(new String[]{"Công nghệ Thông tin và Truyền thông", "Điện - Điện tử", "Cơ khí - Ô tô", "Kinh tế", "Ngoại ngữ - Du lịch"});
        cboKhoa.setSelectedItem(tblHienThiGV.getValueAt(row, 2).toString());
        
        Object[] msg = {"Họ tên GV:", txtTen, "Email:", txtEmail, "Khoa:", cboKhoa};
        if (javax.swing.JOptionPane.showConfirmDialog(this, msg, "Sửa Giảng Viên", javax.swing.JOptionPane.OK_CANCEL_OPTION) == javax.swing.JOptionPane.OK_OPTION) {
            try {
                gvCtrl.suaGiangVien(idGV, txtTen.getText(), txtEmail.getText(), cboKhoa.getSelectedItem().toString());
                refreshData();
                javax.swing.JOptionPane.showMessageDialog(this, "Sửa thành công!");
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_btnSuaGVActionPerformed

    private void btnXoaGVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaGVActionPerformed
        // TODO add your handling code here:
        int row = tblHienThiGV.getSelectedRow();
        if (row == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng chọn một giảng viên để xóa!");
            return;
        }
        if (javax.swing.JOptionPane.showConfirmDialog(this, "Chắc chắn xóa giảng viên này?", "Xác nhận", javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION) {
            try {
                gvCtrl.xoaGiangVien(tblHienThiGV.getValueAt(row, 0).toString());
                javax.swing.JOptionPane.showMessageDialog(this, "Xóa dữ liệu thành công!");
                refreshData();
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(this, "Lỗi khi xóa: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_btnXoaGVActionPerformed

    private void btnSuaSVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaSVActionPerformed
        // TODO add your handling code here:
        int row = tblHienThiSV.getSelectedRow();
        if (row == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng chọn một sinh viên để sửa!");
            return;
        }

        String idSV = tblHienThiSV.getValueAt(row, 0).toString();
        javax.swing.JTextField txtTen = new javax.swing.JTextField(tblHienThiSV.getValueAt(row, 1).toString());
        javax.swing.JComboBox<String> cboTrangThai = new javax.swing.JComboBox<>(new String[]{"Đang học", "Bảo lưu", "Đã tốt nghiệp", "Đuổi học"});
        cboTrangThai.setSelectedItem(tblHienThiSV.getValueAt(row, 4).toString());
        
        Object[] msg = {"Họ tên SV:", txtTen, "Trạng thái:", cboTrangThai};
        if (javax.swing.JOptionPane.showConfirmDialog(this, msg, "Sửa Sinh Viên", javax.swing.JOptionPane.OK_CANCEL_OPTION) == javax.swing.JOptionPane.OK_OPTION) {
            try {
                svCtrl.suaSinhVien(idSV, txtTen.getText(), cboTrangThai.getSelectedItem().toString());
                refreshData();
                javax.swing.JOptionPane.showMessageDialog(this, "Sửa thành công!");
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_btnSuaSVActionPerformed

    private void btnXoaSVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaSVActionPerformed
        // TODO add your handling code here:
        int row = tblHienThiSV.getSelectedRow();
        if (row == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng chọn một sinh viên để xóa!");
            return;
        }
        if (javax.swing.JOptionPane.showConfirmDialog(this, "Chắc chắn xóa sinh viên này?", "Xác nhận", javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION) {
            try {
                svCtrl.xoaSinhVien(tblHienThiSV.getValueAt(row, 0).toString());
                javax.swing.JOptionPane.showMessageDialog(this, "Xóa dữ liệu thành công!");
                refreshData();
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(this, "Lỗi khi xóa: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_btnXoaSVActionPerformed

    private void btnSuaLopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaLopActionPerformed
        // TODO add your handling code here:
        int row = tblHienThiLop.getSelectedRow();
        if (row == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng chọn một lớp học trong bảng để sửa!");
            return;
        }

        String idLop = tblHienThiLop.getValueAt(row, 0).toString();
        javax.swing.JTextField txtTen = new javax.swing.JTextField(tblHienThiLop.getValueAt(row, 1).toString());
        javax.swing.JComboBox<String> cboKhoa = new javax.swing.JComboBox<>(new String[]{
            "Công nghệ Thông tin và Truyền thông", "Điện - Điện tử", "Cơ khí - Ô tô", "Kinh tế", "Ngoại ngữ - Du lịch"
        });
        cboKhoa.setSelectedItem(tblHienThiLop.getValueAt(row, 2).toString());

        Object[] msg = {"Tên lớp học:", txtTen, "Khoa:", cboKhoa};

        javax.swing.JOptionPane optionPane = new javax.swing.JOptionPane(
                msg, 
                javax.swing.JOptionPane.QUESTION_MESSAGE, 
                javax.swing.JOptionPane.OK_CANCEL_OPTION
        );

        javax.swing.JDialog dialog = optionPane.createDialog(this, "Sửa Lớp Học");

        optionPane.addPropertyChangeListener(e -> {
            String prop = e.getPropertyName();

            if (javax.swing.JOptionPane.VALUE_PROPERTY.equals(prop)) {
                Object value = optionPane.getValue();

                // 🌟 SỬA ĐOẠN NÀY: Nếu value là null, hoặc là chuỗi chưa khởi tạo thì không xử lý ép kiểu Integer để tránh ClassCastException
                if (value == null || value == javax.swing.JOptionPane.UNINITIALIZED_VALUE) {
                    return; 
                }

                // Nếu value là chuỗi String (do hệ thống tự trả về khi reset), cũng bỏ qua không ép kiểu
                if (value instanceof String) {
                    return;
                }

                // Bây giờ an toàn 100% để ép kiểu sang Integer
                Integer option = (Integer) value;

                // Nếu người dùng nhấn hủy hoặc đóng cửa sổ
                if (option == javax.swing.JOptionPane.CANCEL_OPTION || option == javax.swing.JOptionPane.CLOSED_OPTION) {
                    dialog.dispose();
                } 
                // Nếu người dùng nhấn nút OK
                else if (option == javax.swing.JOptionPane.OK_OPTION) {

                    // BẮT LỖI TRỐNG TÊN LỚP HỌC
                    if (txtTen.getText().trim().isEmpty()) {
                        javax.swing.JOptionPane.showMessageDialog(dialog, "Tên lớp học không được để trống!", "Cảnh báo", javax.swing.JOptionPane.WARNING_MESSAGE);
                        txtTen.requestFocus();

                        // Reset giá trị về trạng thái chưa click. Lệnh này kích hoạt PropertyChangeEvent, 
                        // nhưng nhờ đoạn check 'instanceof String' ở trên đầu nên code sẽ không bị lỗi ClassCastException nữa!
                        optionPane.setValue(javax.swing.JOptionPane.UNINITIALIZED_VALUE);
                    } 
                    // Nếu dữ liệu hợp lệ, tiến hành lưu
                    else {
                        try {
                            lopCtrl.suaLop(idLop, txtTen.getText().trim(), cboKhoa.getSelectedItem().toString());
                            refreshData();
                            javax.swing.JOptionPane.showMessageDialog(this, "Sửa thành công!");
                            dialog.dispose(); // Lưu xong thành công mới tắt dialog
                        } catch (Exception ex) {
                            javax.swing.JOptionPane.showMessageDialog(dialog, "Lỗi: " + ex.getMessage());
                            optionPane.setValue(javax.swing.JOptionPane.UNINITIALIZED_VALUE);
                        }
                    }
                }
            }
        });

        dialog.setVisible(true);
    }//GEN-LAST:event_btnSuaLopActionPerformed

    private void btnLamMoiSVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiSVActionPerformed
        // TODO add your handling code here:
        txtTimKiemSV.setText("");
        tblHienThiSV.setRowSorter(null);
    }//GEN-LAST:event_btnLamMoiSVActionPerformed

    private void btnLamMoiGVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiGVActionPerformed
        // TODO add your handling code here:
        txtTimKiemGV.setText("");
        tblHienThiGV.setRowSorter(null);
    }//GEN-LAST:event_btnLamMoiGVActionPerformed

    private void btnLamMoiLopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiLopActionPerformed
        // TODO add your handling code here:
        txtTimKiem.setText("");
        tblHienThiLop.setRowSorter(null);
    }//GEN-LAST:event_btnLamMoiLopActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLamMoiGV;
    private javax.swing.JButton btnLamMoiLop;
    private javax.swing.JButton btnLamMoiSV;
    private javax.swing.JButton btnLoc;
    private javax.swing.JButton btnLocGV;
    private javax.swing.JButton btnLocSV;
    private javax.swing.JButton btnSuaGV;
    private javax.swing.JButton btnSuaLop;
    private javax.swing.JButton btnSuaSV;
    private javax.swing.JButton btnXoaGV;
    private javax.swing.JButton btnXoaLop;
    private javax.swing.JButton btnXoaSV;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblTieuDe;
    private javax.swing.JTable tblHienThiGV;
    private javax.swing.JTable tblHienThiLop;
    private javax.swing.JTable tblHienThiSV;
    private javax.swing.JTextField txtTimKiem;
    private javax.swing.JTextField txtTimKiemGV;
    private javax.swing.JTextField txtTimKiemSV;
    // End of variables declaration//GEN-END:variables
}
