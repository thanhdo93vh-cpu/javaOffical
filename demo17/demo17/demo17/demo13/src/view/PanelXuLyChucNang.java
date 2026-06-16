/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

/**
 *
 * @author DO
 */
public class PanelXuLyChucNang extends javax.swing.JPanel {

    /**
     * Creates new form ManHinhChucNang2
     */
    
    private controller.LopHocController lopCtrl = new controller.LopHocController();
    private controller.XuLiChucNangController xlCtrl = new controller.XuLiChucNangController();
    private Runnable onDataChanged;
    
    public PanelXuLyChucNang() {
        this(null);
    }
    
    public PanelXuLyChucNang(Runnable onDataChanged) {
        this.onDataChanged = onDataChanged;
        initComponents();
        loadDataToComboBox();
        loadDataToTables();
        
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent evt) {
                lopCtrl = new controller.LopHocController();
                loadDataToComboBox();
                loadDataToTables();
            }
        });

        cboLopHienTai.addActionListener(e -> {
            loadSinhVienTheoLopChon();
        });
        
        btnXacThucGV.addActionListener(e -> xacThucGV());
        btnBoNhiemGV.addActionListener(e -> boNhiemGV());
        btnLamMoiBoNhiem.addActionListener(e -> lamMoiPhanCong());
        btnLamMoiGop.addActionListener(e -> lamMoiGop());
        btnLamMoiChuyen.addActionListener(e -> lamMoiChuyen());
    }
    
    private void notifyDataChanged() {
        if (onDataChanged != null) {
            javax.swing.SwingUtilities.invokeLater(onDataChanged);
        }
    }

    private void loadDataToComboBox() {
        cboLopHienTai.removeAllItems();
        cboLopDen.removeAllItems();
        cboKhoaLopMoi.removeAllItems();
        cboGvcnMoi.removeAllItems();
        
        String[] dsKhoa = {"Công nghệ Thông tin và Truyền thông", "Điện - Điện tử", "Cơ khí - Ô tô", "Kinh tế", "Ngoại ngữ - Du lịch"};
        for (String k : dsKhoa) cboKhoaLopMoi.addItem(k);
        
        for (model.LopHoc lop : lopCtrl.getDanhSachLop()) {
            cboLopHienTai.addItem(lop.getMaLop());
            cboLopDen.addItem(lop.getMaLop());
        }
        
        // Load danh sách giảng viên vào combobox
        for (model.GiangVien gv : lopCtrl.getDanhSachGV()) {
            cboGvcnMoi.addItem(gv.getTenGV());
        }
    }
    
    private void loadDataToTables() {
        // Gọi reload để đảm bảo đồng bộ dữ liệu mới nhất từ file text
        lopCtrl.reload();

        // 1. Đổ dữ liệu vào bảng Tab "Gộp lớp" (jTable2)
        javax.swing.table.DefaultTableModel modelGop = (javax.swing.table.DefaultTableModel) jTable2.getModel();
        modelGop.setRowCount(0);
        for (model.LopHoc lop : lopCtrl.getDanhSachLop()) {
            String gvcn = lop.getGiangVienDay() != null ? lop.getGiangVienDay().getTenGV() : "Chưa có";
            int siSo = lop.getDanhSachLop() != null ? lop.getDanhSachLop().size() : 0;
            modelGop.addRow(new Object[]{false, lop.getMaLop(), lop.getTenLop(), lop.getKhoa(), siSo, gvcn});
        }

        // 2. Đổ dữ liệu vào bảng Tab "Phân công chủ nhiệm" - chỉ lớp chưa có GVCN
        javax.swing.table.DefaultTableModel modelPhanCong = (javax.swing.table.DefaultTableModel) tblLopPhanCong.getModel();
        modelPhanCong.setRowCount(0);
        for (model.LopHoc lop : lopCtrl.getDanhSachLop()) {
            if (lop.getGiangVienDay() == null) {
                int siSo = lop.getDanhSachLop() != null ? lop.getDanhSachLop().size() : 0;
                modelPhanCong.addRow(new Object[]{false, lop.getMaLop(), lop.getTenLop(), lop.getKhoa(), siSo});
            }
        }

        // 3. Tự động hiển thị sinh viên của lớp đang được chọn
        loadSinhVienTheoLopChon();
    }
    
    private void xacThucGV() {
        String maGV = txtMaGVXacThuc.getText().trim();
        if (maGV.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng nhập mã giáo viên!");
            return;
        }

        model.GiangVien gv = null;
        for (model.GiangVien g : lopCtrl.getDanhSachGV()) {
            if (g.getMaGV().equalsIgnoreCase(maGV)) {
                gv = g;
                break;
            }
        }

        if (gv == null) {
            javax.swing.JOptionPane.showMessageDialog(this, "Giáo viên này không tồn tại!");
            lblHienThiTenGV.setText("[Tên]");
            lblHienThiKhoaGV.setText("[Khoa]");
            lblHienThiEmailGV.setText("[Email]");
            jLabel11.setText("[Số lớp]");
            return;
        }

        // Hiển thị thông tin lên giao diện chi tiết
        lblHienThiTenGV.setText(gv.getTenGV());
        lblHienThiKhoaGV.setText(gv.getKhoa());
        lblHienThiEmailGV.setText(gv.getEmail());

        // Tính toán số lớp đang chủ nhiệm thực tế
        int soLopChuNhiem = 0;
        for (model.LopHoc lop : lopCtrl.getDanhSachLop()) {
            if (lop.getGiangVienDay() != null && lop.getGiangVienDay().getMaGV().equals(gv.getMaGV())) {
                soLopChuNhiem++;
            }
        }
        jLabel11.setText(String.valueOf(soLopChuNhiem));

        // Cập nhật bảng phân công bên dưới
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) tblLopPhanCong.getModel();
        model.setRowCount(0);
        for (model.LopHoc lop : lopCtrl.getDanhSachLop()) {
            if (lop.getGiangVienDay() == null) {
                int siSo = lop.getDanhSachLop() != null ? lop.getDanhSachLop().size() : 0;
                model.addRow(new Object[]{false, lop.getMaLop(), lop.getTenLop(), lop.getKhoa(), siSo});
            }
        }

        javax.swing.JOptionPane.showMessageDialog(this, "Xác thực thành công! Chọn lớp và nhấn Bổ nhiệm.");
    }
    
    private void boNhiemGV() {
        String maGV = txtMaGVXacThuc.getText().trim();
        if (maGV.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng xác thực giáo viên trước!");
            return;
        }

        int selectedRow = tblLopPhanCong.getSelectedRow();
        if (selectedRow == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng chọn lớp từ bảng!");
            return;
        }

        String maLop = tblLopPhanCong.getValueAt(selectedRow, 1).toString();

        model.GiangVien gv = null;
        for (model.GiangVien g : lopCtrl.getDanhSachGV()) {
            if (g.getMaGV().equalsIgnoreCase(maGV)) {
                gv = g;
                break;
            }
        }

        if (gv == null) {
            javax.swing.JOptionPane.showMessageDialog(this, "Giáo viên không tồn tại!");
            return;
        }

        for (model.LopHoc lop : lopCtrl.getDanhSachLop()) {
            if (lop.getMaLop().equalsIgnoreCase(maLop)) {
                lop.setGiangVienDay(gv);
                break;
            }
        }

        // Ghi dữ liệu đồng bộ xuống file text nghiệp vụ
        java.io.File fileOut = new java.io.File("lophoc.txt");
        util.QuanLyFile.ghiFile(fileOut, lopCtrl.getDanhSachLop());
        lopCtrl.reload();

        javax.swing.JOptionPane.showMessageDialog(this, "Bổ nhiệm giảng viên thành công!");
        loadDataToTables();
        lamMoiPhanCong();
        notifyDataChanged();
    }

    private void loadSinhVienTheoLopChon() {
        // Đổ dữ liệu vào bảng Tab "Chuyển lớp" (tblSinhVienChuyen) dựa theo Combobox
        javax.swing.table.DefaultTableModel modelChuyen = (javax.swing.table.DefaultTableModel) tblSinhVienChuyen.getModel();
        modelChuyen.setRowCount(0);
        
        Object selectedLop = cboLopHienTai.getSelectedItem();
        if (selectedLop != null) {
            String maLopSelect = selectedLop.toString();
            for (model.LopHoc lop : lopCtrl.getDanhSachLop()) {
                if (lop.getMaLop().equalsIgnoreCase(maLopSelect)) {
                    if (lop.getDanhSachLop() != null) {
                        for (model.SinhVien sv : lop.getDanhSachLop()) {
                            // Map tạm email sinh viên theo mã để hiển thị kín bảng UI mẫu của bạn
                            modelChuyen.addRow(new Object[]{false, sv.getMaSV(), sv.getTenSV(), lop.getTenLop(), sv.getMaSV() + "@student.edu.vn"});
                        }
                    }
                    break;
                }
            }
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
        jPanel2 = new javax.swing.JPanel();
        header = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        txtMaLopMoi = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtTenLopMoi = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        cboKhoaLopMoi = new javax.swing.JComboBox<>();
        cboGvcnMoi = new javax.swing.JComboBox<>();
        jPanel12 = new javax.swing.JPanel();
        btnXacNhanGop = new javax.swing.JButton();
        btnLamMoiGop = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        manhinh1 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cboLopHienTai = new javax.swing.JComboBox<>();
        cboLopDen = new javax.swing.JComboBox<>();
        btnXacNhanChuyen = new javax.swing.JButton();
        btnLamMoiChuyen = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblSinhVienChuyen = new javax.swing.JTable();
        mh2 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtMaGVXacThuc = new javax.swing.JTextField();
        btnXacThucGV = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        lblHienThiSoLopChuNhiem = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        lblHienThiTenGV = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblHienThiKhoaGV = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        lblHienThiEmailGV = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        btnLamMoiBoNhiem = new javax.swing.JButton();
        btnBoNhiemGV = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblLopPhanCong = new javax.swing.JTable();
        mh3 = new javax.swing.JPanel();
        header3 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txtMaLop = new javax.swing.JTextField();
        btnXacNhan = new javax.swing.JButton();
        center3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblLichSu = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.BorderLayout());

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setText("Xử lí nghiệp vụ");

        javax.swing.GroupLayout headerLayout = new javax.swing.GroupLayout(header);
        header.setLayout(headerLayout);
        headerLayout.setHorizontalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headerLayout.createSequentialGroup()
                .addContainerGap(293, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(263, 263, 263))
        );
        headerLayout.setVerticalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerLayout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jLabel2)
                .addContainerGap(42, Short.MAX_VALUE))
        );

        jPanel2.add(header, java.awt.BorderLayout.PAGE_START);

        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder("Thông tin lớp mới sau khi gộp"));

        jLabel14.setText("Mã lớp:");

        txtMaLopMoi.addActionListener(this::txtMaLopMoiActionPerformed);

        jLabel15.setText("Tên lớp:");

        jLabel16.setText("Khoa:");

        jLabel18.setText("GVCN:");

        cboKhoaLopMoi.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cboGvcnMoi.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addComponent(txtMaLopMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(txtTenLopMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addComponent(cboKhoaLopMoi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(52, 52, 52)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18)
                    .addComponent(cboGvcnMoi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(271, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMaLopMoi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTenLopMoi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboKhoaLopMoi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboGvcnMoi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(40, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel10, java.awt.BorderLayout.PAGE_START);

        btnXacNhanGop.setText("Xác nhận gộp");
        btnXacNhanGop.addActionListener(this::btnXacNhanGopActionPerformed);

        btnLamMoiGop.setText("Làm mới");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap(448, Short.MAX_VALUE)
                .addComponent(btnLamMoiGop)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnXacNhanGop)
                .addGap(94, 94, 94))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnXacNhanGop)
                    .addComponent(btnLamMoiGop))
                .addContainerGap(40, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel12, java.awt.BorderLayout.PAGE_END);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Chọn", "Mã lớp", "Tên lớp", "Khoa", "Sĩ số", "GVCN"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable2);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 722, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel13, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Gộp lớp", jPanel5);

        manhinh1.setLayout(new java.awt.BorderLayout());

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Chọn lớp chuyển:"));

        jLabel1.setText("Lớp hiện tại:");

        jLabel3.setText("Chuyển đến lớp:");

        cboLopHienTai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cboLopDen.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnXacNhanChuyen.setText("Xác nhận chuyển");
        btnXacNhanChuyen.addActionListener(this::btnXacNhanChuyenActionPerformed);

        btnLamMoiChuyen.setText("Làm mới");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(101, 101, 101)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cboLopHienTai, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(cboLopDen, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(43, 43, 43)
                .addComponent(btnXacNhanChuyen)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnLamMoiChuyen, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cboLopHienTai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cboLopDen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnXacNhanChuyen)
                    .addComponent(btnLamMoiChuyen))
                .addGap(20, 20, 20))
        );

        manhinh1.add(jPanel8, java.awt.BorderLayout.PAGE_START);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Danh sách sinh viên của lớp hiện tại"));

        tblSinhVienChuyen.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Chọn", "Mã SV", "Họ tên", "Lớp", "Email"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tblSinhVienChuyen);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 715, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 3, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        manhinh1.add(jPanel6, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Chuyển lớp cho sinh viên", manhinh1);

        mh2.setLayout(new java.awt.BorderLayout());

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Xác thực thông tin giảng viên:"));

        jLabel4.setText("Nhập mã GV:");

        txtMaGVXacThuc.addActionListener(this::txtMaGVXacThucActionPerformed);

        btnXacThucGV.setText("Xác thực");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(txtMaGVXacThuc, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(63, 63, 63)
                        .addComponent(btnXacThucGV)))
                .addContainerGap(414, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMaGVXacThuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnXacThucGV))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        mh2.add(jPanel7, java.awt.BorderLayout.PAGE_START);

        jPanel3.setLayout(new java.awt.BorderLayout());

        lblHienThiSoLopChuNhiem.setBorder(javax.swing.BorderFactory.createTitledBorder("Thông tin chi tiết"));

        jLabel5.setText("Họ tên:");

        lblHienThiTenGV.setText("[Tên]");

        jLabel7.setText("Khoa:");

        lblHienThiKhoaGV.setText("[Khoa]");

        jLabel10.setText("Số lớp đang chủ nhiệm:");

        jLabel11.setText("[Số lớp]");

        jLabel12.setText("Email:");

        lblHienThiEmailGV.setText("[Email]");

        javax.swing.GroupLayout lblHienThiSoLopChuNhiemLayout = new javax.swing.GroupLayout(lblHienThiSoLopChuNhiem);
        lblHienThiSoLopChuNhiem.setLayout(lblHienThiSoLopChuNhiemLayout);
        lblHienThiSoLopChuNhiemLayout.setHorizontalGroup(
            lblHienThiSoLopChuNhiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lblHienThiSoLopChuNhiemLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(lblHienThiSoLopChuNhiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(lblHienThiTenGV))
                .addGap(96, 96, 96)
                .addGroup(lblHienThiSoLopChuNhiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(lblHienThiKhoaGV))
                .addGap(98, 98, 98)
                .addGroup(lblHienThiSoLopChuNhiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11))
                .addGap(98, 98, 98)
                .addGroup(lblHienThiSoLopChuNhiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblHienThiEmailGV)
                    .addComponent(jLabel12))
                .addContainerGap(151, Short.MAX_VALUE))
        );
        lblHienThiSoLopChuNhiemLayout.setVerticalGroup(
            lblHienThiSoLopChuNhiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lblHienThiSoLopChuNhiemLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(lblHienThiSoLopChuNhiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel7)
                    .addComponent(jLabel10)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(lblHienThiSoLopChuNhiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblHienThiTenGV)
                    .addComponent(lblHienThiKhoaGV)
                    .addComponent(jLabel11)
                    .addComponent(lblHienThiEmailGV))
                .addContainerGap(46, Short.MAX_VALUE))
        );

        jPanel3.add(lblHienThiSoLopChuNhiem, java.awt.BorderLayout.PAGE_START);

        btnLamMoiBoNhiem.setText("Làm mới");

        btnBoNhiemGV.setText("Bổ nhiệm");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(509, Short.MAX_VALUE)
                .addComponent(btnLamMoiBoNhiem)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBoNhiemGV)
                .addGap(55, 55, 55))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(40, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLamMoiBoNhiem)
                    .addComponent(btnBoNhiemGV))
                .addGap(37, 37, 37))
        );

        jPanel3.add(jPanel9, java.awt.BorderLayout.PAGE_END);

        tblLopPhanCong.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Chọn", "Mã lớp", "Tên lớp", "Khoa", "Sĩ số"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane4.setViewportView(tblLopPhanCong);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 728, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.add(jPanel11, java.awt.BorderLayout.CENTER);

        mh2.add(jPanel3, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Phân công chủ nhiệm", mh2);

        mh3.setLayout(new java.awt.BorderLayout());

        jLabel9.setText("Nhập mã lớp:");

        txtMaLop.addActionListener(this::txtMaLopActionPerformed);

        btnXacNhan.setText("Xác thực");
        btnXacNhan.addActionListener(this::btnXacNhanActionPerformed);

        javax.swing.GroupLayout header3Layout = new javax.swing.GroupLayout(header3);
        header3.setLayout(header3Layout);
        header3Layout.setHorizontalGroup(
            header3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(header3Layout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtMaLop, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(btnXacNhan)
                .addContainerGap(383, Short.MAX_VALUE))
        );
        header3Layout.setVerticalGroup(
            header3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(header3Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(header3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtMaLop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnXacNhan))
                .addContainerGap(46, Short.MAX_VALUE))
        );

        mh3.add(header3, java.awt.BorderLayout.PAGE_START);

        tblLichSu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Loại thay đổi", "Chi tiết"
            }
        ));
        jScrollPane2.setViewportView(tblLichSu);

        javax.swing.GroupLayout center3Layout = new javax.swing.GroupLayout(center3);
        center3.setLayout(center3Layout);
        center3Layout.setHorizontalGroup(
            center3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 728, Short.MAX_VALUE)
        );
        center3Layout.setVerticalGroup(
            center3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(center3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        mh3.add(center3, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Lịch sử thay đổi", mh3);

        jPanel2.add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 728, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 728, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 646, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 646, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void txtMaLopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaLopActionPerformed
    }//GEN-LAST:event_txtMaLopActionPerformed

    private void btnXacNhanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXacNhanActionPerformed
        String maLop = txtMaLop.getText().trim();
        if (maLop.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng nhập mã lớp để xem lịch sử!");
            return;
        }
        try {
            Object[][] data = xlCtrl.getLichSuTheoLop(maLop);
            javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) tblLichSu.getModel();
            model.setRowCount(0);
            for (Object[] row : data) {
                model.addRow(row);
            }
            javax.swing.JOptionPane.showMessageDialog(this, "Đã tải lịch sử thay đổi của lớp: " + maLop);
        } catch (IllegalArgumentException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, ex.getMessage());
        }
        
    }//GEN-LAST:event_btnXacNhanActionPerformed

    private void txtMaGVXacThucActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaGVXacThucActionPerformed
    }//GEN-LAST:event_txtMaGVXacThucActionPerformed

    private void btnXacNhanChuyenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXacNhanChuyenActionPerformed
        Object lopHienTaiObj = cboLopHienTai.getSelectedItem();
        Object lopDenObj = cboLopDen.getSelectedItem();
        
        if (lopHienTaiObj == null || lopDenObj == null) {
            javax.swing.JOptionPane.showMessageDialog(this, "Chưa có danh sách lớp để thực hiện chuyển!");
            return;
        }
        
        String lopHienTai = lopHienTaiObj.toString();
        String lopDen = lopDenObj.toString();
        
        if (lopHienTai.equals(lopDen)) {
            javax.swing.JOptionPane.showMessageDialog(this, "Lớp muốn chuyển đến phải khác lớp hiện tại!");
            return;
        }
        
        // Lấy sinh viên được chọn (checkbox = true)
        java.util.List<String> dsMaSinhVien = new java.util.ArrayList<>();
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) tblSinhVienChuyen.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            boolean isChecked = (Boolean) model.getValueAt(i, 0);
            if (isChecked) {
                String maSV = model.getValueAt(i, 1).toString();
                dsMaSinhVien.add(maSV);
            }
        }
        
        if (dsMaSinhVien.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng chọn sinh viên để chuyển!");
            return;
        }
        
        int confirm = javax.swing.JOptionPane.showConfirmDialog(this, "Xác nhận chuyển " + dsMaSinhVien.size() + " sinh viên từ " + lopHienTai + " sang " + lopDen + "?", "Xác nhận", javax.swing.JOptionPane.YES_NO_OPTION);
        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            try {
                xlCtrl.chuyenSinhVien(lopHienTai, lopDen, dsMaSinhVien);
                javax.swing.JOptionPane.showMessageDialog(this, "Chuyển lớp thành công!");
                lopCtrl.reload();
                loadDataToTables();  // Reload tất cả bảng + combobox
                loadSinhVienTheoLopChon();
                notifyDataChanged();
            } catch (IllegalArgumentException ex) {
                javax.swing.JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        }
    }//GEN-LAST:event_btnXacNhanChuyenActionPerformed

    private void btnXacNhanGopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXacNhanGopActionPerformed
        String maMoi = txtMaLopMoi.getText().trim();
        String tenMoi = txtTenLopMoi.getText().trim();
        
        String khoaMoi = cboKhoaLopMoi.getSelectedItem().toString();
        String tenGVMoi = cboGvcnMoi.getSelectedItem().toString();

        if (maMoi.isEmpty() || tenMoi.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng nhập Mã và Tên lớp mới trước khi gộp!");
            return;
        }

        // Lấy danh sách lớp được chọn (checkbox = true)
        java.util.List<String> dsMaLopChon = new java.util.ArrayList<>();
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) jTable2.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            Boolean isChecked = (Boolean) model.getValueAt(i, 0);
            if (Boolean.TRUE.equals(isChecked)) {
                dsMaLopChon.add(model.getValueAt(i, 1).toString());
            }
        }

        if (dsMaLopChon.size() < 2) {
            javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất 2 lớp để gộp!");
            return;
        }

        int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
            "Xác nhận gộp " + dsMaLopChon.size() + " lớp thành lớp " + maMoi + "?",
            "Xác nhận gộp lớp", javax.swing.JOptionPane.YES_NO_OPTION);

        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            try {
                // Gộp tất cả SV của các lớp được chọn vào lớp mới
                java.util.List<model.SinhVien> dsSVGop = new java.util.ArrayList<>();
                java.util.List<model.LopHoc> dsTatCaLop = lopCtrl.getDanhSachLop();

                for (String maLop : dsMaLopChon) {
                    for (model.LopHoc lop : dsTatCaLop) {
                        if (lop.getMaLop().equalsIgnoreCase(maLop) && lop.getDanhSachLop() != null) {
                            dsSVGop.addAll(lop.getDanhSachLop());
                        }
                    }
                }

                // Tìm GV theo tên nếu có nhập
                model.GiangVien gvMoi = tenGVMoi.isEmpty() ? null : lopCtrl.timGVTheoTen(tenGVMoi);

                // Tạo lớp mới
                model.LopHoc lopMoi = new model.LopHoc(maMoi, tenMoi, khoaMoi, gvMoi, dsSVGop);

                // Xóa các lớp cũ đã gộp
                for (String maLop : dsMaLopChon) {
                    lopCtrl.xoaLop(maLop);
                }

                // Thêm lớp mới
                lopCtrl.getDanhSachLop().add(lopMoi);
                util.QuanLyFile.ghiFile(new java.io.File("lophoc.txt"), lopCtrl.getDanhSachLop());
                lopCtrl.reload();

                xlCtrl.ghiLichSuGopLop(dsMaLopChon, maMoi, dsSVGop.size());

                javax.swing.JOptionPane.showMessageDialog(this, "Gộp lớp thành công! Lớp mới: " + maMoi + " có " + dsSVGop.size() + " sinh viên.");
                lamMoiGop();
                loadDataToComboBox();
                loadDataToTables();
                notifyDataChanged();

            } catch (Exception ex) {
                javax.swing.JOptionPane.showMessageDialog(this, "Lỗi khi gộp lớp: " + ex.getMessage());
            }
        }
    }//GEN-LAST:event_btnXacNhanGopActionPerformed

    private void txtMaLopMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaLopMoiActionPerformed
    }//GEN-LAST:event_txtMaLopMoiActionPerformed

    // Làm mới form gộp lớp
    private void lamMoiGop() {
        txtMaLopMoi.setText("");
        txtTenLopMoi.setText("");
        
        if (cboGvcnMoi.getItemCount() > 0) cboGvcnMoi.setSelectedIndex(0);
        if (cboKhoaLopMoi.getItemCount() > 0) cboKhoaLopMoi.setSelectedIndex(0);
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) jTable2.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(false, i, 0);
        }
    }

    // Làm mới form chuyển lớp
    private void lamMoiChuyen() {
        if (cboLopHienTai.getItemCount() > 0) cboLopHienTai.setSelectedIndex(0);
        if (cboLopDen.getItemCount() > 0) cboLopDen.setSelectedIndex(0);
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) tblSinhVienChuyen.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(false, i, 0);
        }
        loadSinhVienTheoLopChon();
    }

    // Làm mới form
    private void lamMoiPhanCong() {
        txtMaGVXacThuc.setText("");
        lblHienThiTenGV.setText("[Tên]");
        lblHienThiKhoaGV.setText("[Khoa]");
        lblHienThiEmailGV.setText("[Email]");
        jLabel11.setText("[Số lớp]");
        tblLopPhanCong.clearSelection();
        
        // Reload dữ liệu
        lopCtrl = new controller.LopHocController();
        loadDataToTables();
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBoNhiemGV;
    private javax.swing.JButton btnLamMoiBoNhiem;
    private javax.swing.JButton btnLamMoiChuyen;
    private javax.swing.JButton btnLamMoiGop;
    private javax.swing.JButton btnXacNhan;
    private javax.swing.JButton btnXacNhanChuyen;
    private javax.swing.JButton btnXacNhanGop;
    private javax.swing.JButton btnXacThucGV;
    private javax.swing.JComboBox<String> cboGvcnMoi;
    private javax.swing.JComboBox<String> cboKhoaLopMoi;
    private javax.swing.JComboBox<String> cboLopDen;
    private javax.swing.JComboBox<String> cboLopHienTai;
    private javax.swing.JPanel center3;
    private javax.swing.JPanel header;
    private javax.swing.JPanel header3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable2;
    private javax.swing.JLabel lblHienThiEmailGV;
    private javax.swing.JLabel lblHienThiKhoaGV;
    private javax.swing.JPanel lblHienThiSoLopChuNhiem;
    private javax.swing.JLabel lblHienThiTenGV;
    private javax.swing.JPanel manhinh1;
    private javax.swing.JPanel mh2;
    private javax.swing.JPanel mh3;
    private javax.swing.JTable tblLichSu;
    private javax.swing.JTable tblLopPhanCong;
    private javax.swing.JTable tblSinhVienChuyen;
    private javax.swing.JTextField txtMaGVXacThuc;
    private javax.swing.JTextField txtMaLop;
    private javax.swing.JTextField txtMaLopMoi;
    private javax.swing.JTextField txtTenLopMoi;
    // End of variables declaration//GEN-END:variables
}
