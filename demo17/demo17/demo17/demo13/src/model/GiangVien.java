    package model;

import java.io.Serializable;

public class GiangVien implements Serializable {
    private String maGV, tenGV, diaChi, email, khoa;

    public GiangVien(String maGV, String tenGV, String diaChi, String email, String khoa) {
        this.maGV = maGV; this.tenGV = tenGV; this.diaChi = diaChi; this.email = email; this.khoa = khoa;
    }

    public String getMaGV() { return maGV; }
    public void setMaGV(String maGV) { this.maGV = maGV; }
    public String getTenGV() { return tenGV; }
    public void setTenGV(String tenGV) { this.tenGV = tenGV; }
    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getKhoa() { return khoa; }
    public void setKhoa(String khoa) { this.khoa = khoa; }
}