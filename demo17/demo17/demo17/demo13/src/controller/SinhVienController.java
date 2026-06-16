package controller;

import model.LopHoc;
import model.SinhVien;
import util.QuanLyFile;
import java.io.File;
import java.util.List;

public class SinhVienController {
    private static final String FILE_SV = "sinhvien.txt";
    private static final String FILE_LOP = "lophoc.txt";
    private List<SinhVien> danhSachSV;

    public SinhVienController() {
        danhSachSV = QuanLyFile.readFile(FILE_SV);
    }

    public void reload() {
        danhSachSV = QuanLyFile.readFile(FILE_SV);
    }

    public List<SinhVien> getDanhSachSV() { return danhSachSV; }

    public Object[][] getDuLieuBang(List<LopHoc> dsLop) {
        Object[][] data = new Object[danhSachSV.size()][6];
        for (int i = 0; i < danhSachSV.size(); i++) {
            SinhVien sv = danhSachSV.get(i);
            String lopCuaSV = "Chưa xếp lớp";
            for (LopHoc lop : dsLop) {
                if (lop.getDanhSachLop() != null) {
                    for (SinhVien svLop : lop.getDanhSachLop()) {
                        if (svLop.getMaSV().equals(sv.getMaSV())) {
                            lopCuaSV = lop.getTenLop();
                            break;
                        }
                    }
                }
                if (!lopCuaSV.equals("Chưa xếp lớp")) break;
            }
            // Fix cứng Khoa do model SV hiện tại chưa có trường Khoa, hoặc tuỳ bạn chỉnh.
            data[i] = new Object[]{sv.getMaSV(), sv.getTenSV(), "Công nghệ Thông tin và Truyền thông", lopCuaSV, sv.getTrangThai(), "Sửa/Xóa"};
        }
        return data;
    }

    public void themSinhVien(String maSV, String tenSV, String email, String khoa, String lop) {
        if (maSV == null || maSV.isBlank()) throw new IllegalArgumentException("Mã sinh viên không được để trống.");
        for (SinhVien sv : danhSachSV) {
            if (sv.getMaSV().equalsIgnoreCase(maSV.trim())) throw new IllegalArgumentException("Mã sinh viên đã tồn tại.");
        }
        
        SinhVien svMoi = new SinhVien(maSV.trim(), tenSV.trim(), "01/01/2000", "Nam", 0.0, "Đang học");
        danhSachSV.add(svMoi);
        QuanLyFile.ghiFile(new File(FILE_SV), danhSachSV);

        if (lop != null && !lop.isBlank() && !lop.equalsIgnoreCase("Chưa có")) {
            List<LopHoc> danhSachLop = QuanLyFile.readFile(FILE_LOP);
            for (LopHoc lh : danhSachLop) {
                if (lh.getTenLop().equalsIgnoreCase(lop.trim()) || lh.getMaLop().equalsIgnoreCase(lop.trim())) {
                    lh.themSinhVien(svMoi);
                    break;
                }
            }
            QuanLyFile.ghiFile(new File(FILE_LOP), danhSachLop);
        }
    }

    public void suaSinhVien(String maSV, String tenMoi, String trangThaiMoi) {
        SinhVien target = danhSachSV.stream().filter(s -> s.getMaSV().equals(maSV)).findFirst().orElse(null);
        if (target != null) {
            target.setTenSV(tenMoi);
            target.setTrangThai(trangThaiMoi);
            QuanLyFile.ghiFile(new File(FILE_SV), danhSachSV);
            
            // Đồng bộ sang lớp học
            List<LopHoc> dsLop = QuanLyFile.readFile(FILE_LOP);
            for (LopHoc lop : dsLop) {
                if (lop.getDanhSachLop() != null) {
                    for (SinhVien svLop : lop.getDanhSachLop()) {
                        if (svLop.getMaSV().equals(maSV)) {
                            svLop.setTenSV(tenMoi);
                            svLop.setTrangThai(trangThaiMoi);
                        }
                    }
                }
            }
            QuanLyFile.ghiFile(new File(FILE_LOP), dsLop);
        } else {
            throw new IllegalArgumentException("Không tìm thấy sinh viên để sửa.");
        }
    }

    public void xoaSinhVien(String maSV) {
        boolean removed = danhSachSV.removeIf(sv -> sv.getMaSV().equals(maSV));
        if (removed) {
            QuanLyFile.ghiFile(new File(FILE_SV), danhSachSV);
            
            // Xóa SV khỏi Lớp
            List<LopHoc> dsLop = QuanLyFile.readFile(FILE_LOP);
            for (LopHoc lop : dsLop) {
                if (lop.getDanhSachLop() != null) {
                    lop.getDanhSachLop().removeIf(sv -> sv.getMaSV().equals(maSV));
                }
            }
            QuanLyFile.ghiFile(new File(FILE_LOP), dsLop);
        } else {
            throw new IllegalArgumentException("Không tìm thấy sinh viên để xóa.");
        }
    }
}