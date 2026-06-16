package controller;

import model.GiangVien;
import model.LopHoc;
import model.SinhVien;
import util.QuanLyFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LopHocController {
    private static final String FILE_LOP = "lophoc.txt";
    private static final String FILE_GV  = "giangvien.txt";

    private List<LopHoc>    danhSachLop;
    private List<GiangVien> danhSachGV;

    public LopHocController() {
        danhSachLop = QuanLyFile.readFile(FILE_LOP);
        danhSachGV  = QuanLyFile.readFile(FILE_GV);
    }

    public List<LopHoc> getDanhSachLop() { return danhSachLop; }
    public List<GiangVien> getDanhSachGV() { return danhSachGV; }

    public Object[][] getDuLieuBang() {
        Object[][] data = new Object[danhSachLop.size()][6];
        for (int i = 0; i < danhSachLop.size(); i++) {
            LopHoc lop = danhSachLop.get(i);
            data[i][0] = lop.getMaLop();
            data[i][1] = lop.getTenLop();
            data[i][2] = lop.getKhoa();
            data[i][3] = lop.getSiSo(); 
            data[i][4] = lop.getGiangVienDay() != null ? lop.getGiangVienDay().getTenGV() : "Chưa phân công"; 
            data[i][5] = "Sửa/Xóa";
        }
        return data;
    }

    public LopHoc timTheoMa(String maLop) {
        for (LopHoc lop : danhSachLop) {
            if (lop.getMaLop().equalsIgnoreCase(maLop)) return lop;
        }
        return null;
    }

    public GiangVien timGVTheoTen(String tenGV) {
        for (GiangVien gv : danhSachGV) {
            if (gv.getTenGV().equalsIgnoreCase(tenGV)) return gv;
        }
        return null;
    }

    public void reload() {
        danhSachLop = QuanLyFile.readFile(FILE_LOP);
        danhSachGV  = QuanLyFile.readFile(FILE_GV);
    }

    public void themLop(String maLop, String tenLop, String khoa, String tenGV) {
        if (maLop == null || maLop.isBlank() || tenLop == null || tenLop.isBlank() || khoa == null || khoa.isBlank())  
            throw new IllegalArgumentException("Vui lòng nhập đầy đủ thông tin.");
        if (timTheoMa(maLop) != null) throw new IllegalArgumentException("Mã lớp \"" + maLop + "\" đã tồn tại.");
        
        GiangVien gv = (tenGV != null && !tenGV.isBlank()) ? timGVTheoTen(tenGV) : null;
        danhSachLop.add(new LopHoc(maLop.trim(), tenLop.trim(), khoa.trim(), gv, new ArrayList<>()));
        luuFile();
    }

    public void suaLop(String maLop, String tenMoi, String khoaMoi) {
        reload(); // đồng bộ dữ liệu mới nhất
        LopHoc target = timTheoMa(maLop);
        if (target != null) {
            target.setTenLop(tenMoi);
            target.setKhoa(khoaMoi);
            luuFile();
        } else {
            throw new IllegalArgumentException("Không tìm thấy lớp học.");
        }
    }

    public void xoaLop(String maLop) {
        LopHoc lop = timTheoMa(maLop);
        if (lop != null) {
            danhSachLop.remove(lop);
            luuFile();
        } else {
            throw new IllegalArgumentException("Không tìm thấy lớp học để xóa.");
        }
    }

    private void luuFile() {
        QuanLyFile.ghiFile(new File(FILE_LOP), danhSachLop);
    }
}