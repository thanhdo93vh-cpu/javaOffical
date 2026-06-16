package controller;

import model.GiangVien;
import model.LopHoc;
import util.QuanLyFile;
import java.io.File;
import java.util.List;

public class GiangVienController {
    private static final String FILE_GV = "giangvien.txt";
    private static final String FILE_LOP = "lophoc.txt";
    private List<GiangVien> danhSachGV;

    public GiangVienController() {
        danhSachGV = QuanLyFile.readFile(FILE_GV);
    }

    public void reload() {
        danhSachGV = QuanLyFile.readFile(FILE_GV);
    }

    public List<GiangVien> getDanhSachGV() { return danhSachGV; }

    public Object[][] getDuLieuBang(List<LopHoc> dsLop) {
        Object[][] data = new Object[danhSachGV.size()][6];
        for (int i = 0; i < danhSachGV.size(); i++) {
            GiangVien gv = danhSachGV.get(i);
            String lopChuNhiem = "Chưa phân công";
            for (LopHoc lop : dsLop) {
                if (lop.getGiangVienDay() != null && lop.getGiangVienDay().getMaGV().equals(gv.getMaGV())) {
                    lopChuNhiem = lop.getTenLop();
                    break;
                }
            }
            data[i] = new Object[]{gv.getMaGV(), gv.getTenGV(), gv.getKhoa(), lopChuNhiem, gv.getEmail(), "Sửa/Xóa"};
        }
        return data;
    }

    public void themGiangVien(String maGV, String tenGV, String diaChi, String email, String khoa) {
        if (maGV == null || maGV.isBlank() || tenGV == null || tenGV.isBlank()) 
            throw new IllegalArgumentException("Mã và tên giảng viên không được để trống.");
        for (GiangVien gv : danhSachGV) {
            if (gv.getMaGV().equalsIgnoreCase(maGV.trim())) throw new IllegalArgumentException("Mã giảng viên đã tồn tại.");
        }
        danhSachGV.add(new GiangVien(maGV.trim(), tenGV.trim(), diaChi.trim(), email.trim(), khoa.trim()));
        QuanLyFile.ghiFile(new File(FILE_GV), danhSachGV);
    }

    public void suaGiangVien(String maGV, String tenMoi, String emailMoi, String khoaMoi) {
        GiangVien target = danhSachGV.stream().filter(g -> g.getMaGV().equals(maGV)).findFirst().orElse(null);
        if (target != null) {
            target.setTenGV(tenMoi);
            target.setEmail(emailMoi);
            target.setKhoa(khoaMoi);
            QuanLyFile.ghiFile(new File(FILE_GV), danhSachGV);

            // Đồng bộ tên GV trong file lớp học
            List<LopHoc> dsLop = QuanLyFile.readFile(FILE_LOP);
            for (LopHoc lop : dsLop) {
                if (lop.getGiangVienDay() != null && lop.getGiangVienDay().getMaGV().equals(maGV)) {
                    lop.getGiangVienDay().setTenGV(tenMoi);
                }
            }
            QuanLyFile.ghiFile(new File(FILE_LOP), dsLop);
        } else {
            throw new IllegalArgumentException("Không tìm thấy giảng viên.");
        }
    }

    public void xoaGiangVien(String maGV) {
        boolean removed = danhSachGV.removeIf(gv -> gv.getMaGV().equals(maGV));
        if (removed) {
            QuanLyFile.ghiFile(new File(FILE_GV), danhSachGV);
            
            // Xóa phân công GV khỏi Lớp Học
            List<LopHoc> dsLop = QuanLyFile.readFile(FILE_LOP);
            for (LopHoc lop : dsLop) {
                if (lop.getGiangVienDay() != null && lop.getGiangVienDay().getMaGV().equals(maGV)) {
                    lop.setGiangVienDay(null);
                }
            }
            QuanLyFile.ghiFile(new File(FILE_LOP), dsLop);
        } else {
            throw new IllegalArgumentException("Không tìm thấy giảng viên để xóa.");
        }
    }
}