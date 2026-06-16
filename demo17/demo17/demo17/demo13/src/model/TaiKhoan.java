package model;

import java.io.Serializable;

public class TaiKhoan implements Serializable {
    private String hoTen, email, matKhau, vaiTro;

    public TaiKhoan() {}

    public TaiKhoan(String hoTen, String email, String matKhau, String vaiTro) {
        this.hoTen = hoTen; this.email = email; this.matKhau = matKhau; this.vaiTro = vaiTro;
    }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }
    public String getVaiTro() {return vaiTro;}
    public void setVaiTro(String vaiTro) {this.vaiTro = vaiTro;}
    
}