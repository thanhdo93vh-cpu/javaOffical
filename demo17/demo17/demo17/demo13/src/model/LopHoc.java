package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LopHoc implements Serializable {
    private String maLop, tenLop, khoa;
    private GiangVien giangVienDay;
    private List<SinhVien> sinhVienHoc;

    public LopHoc(String maLop, String tenLop, String khoa, GiangVien giangVienDay, List<SinhVien> sinhVienHoc) {
        this.maLop = maLop; this.tenLop = tenLop; this.khoa = khoa; this.giangVienDay = giangVienDay;
        this.sinhVienHoc = (sinhVienHoc != null) ? sinhVienHoc : new ArrayList<>();
    }

    public String getMaLop() { return maLop; }
    public void setMaLop(String maLop) { this.maLop = maLop; }
    public String getTenLop() { return tenLop; }
    public void setTenLop(String tenLop) { this.tenLop = tenLop; }
    public String getKhoa() { return khoa; }
    public void setKhoa(String khoa) { this.khoa = khoa; }
    public GiangVien getGiangVienDay() { return giangVienDay; }
    public void setGiangVienDay(GiangVien giangVienDay) { this.giangVienDay = giangVienDay; }
    public int getSiSo() { return sinhVienHoc.size(); }
    public void themSinhVien(SinhVien sv) { this.sinhVienHoc.add(sv); }
    public List<SinhVien> getDanhSachLop() { return sinhVienHoc; }
}