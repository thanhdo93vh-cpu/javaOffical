package controller;

import model.TaiKhoan;
import util.QuanLyFile;
import java.io.File;
import java.util.List;

public class AuthController {
    private List<TaiKhoan> danhSachTaiKhoan;

    public AuthController() {
        danhSachTaiKhoan = QuanLyFile.docFileTaiKhoan();
    }

    public TaiKhoan dangNhap(String email, String matKhau) {
        if (email.isEmpty() || matKhau.isEmpty()){
            throw new IllegalArgumentException("Vui lòng nhập đầy đủ thông tin!");
        };
        for (TaiKhoan tk : danhSachTaiKhoan) {
            if (tk.getEmail().equalsIgnoreCase(email.trim()) && tk.getMatKhau().equals(matKhau.trim())) return tk;
        }
        return null;
    }

    public boolean kiemTraTonTai(String email) {
        for (TaiKhoan tk : danhSachTaiKhoan) {
            if (tk.getEmail().equalsIgnoreCase(email.trim())) return true;
        }
        return false;
    }

    public boolean taoTaiKhoan(String hoTen, String email, String matKhau, String nhapLai) {
        if (hoTen.isEmpty()|| email.isEmpty() || matKhau.isEmpty() || nhapLai.isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập đầy đủ thông tin!");
        }
        
        if (hoTen.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Họ và tên không được phép chứa chữ số!");
        }
        
        String emailRegex = "^[^@]+@[^@]+mail\\.com$";
        if (!email.matches(emailRegex)) {
            throw new IllegalArgumentException("Email không đúng định dạng!\n(Phải có dạng: [tên]@[tên nhà cung cấp]mail.com)");
        }
        
        if (!matKhau.equalsIgnoreCase(nhapLai)){
            throw new IllegalArgumentException("Mật khẩu nhập lại không trùng khớp!");
        }
        
        if (kiemTraTonTai(email)){
            throw new IllegalArgumentException("Email này đã tồn tại trên hệ thống.");
        }
        else
        {
            try
            {
                String Email = "Email/" + email;
                File folder = new File(Email);
                if (!folder.exists())
                {
                    boolean checkFolder = folder.mkdirs();
                    if (checkFolder)
                    {
                        System.out.println("Đã tạo thành công thư mục: " + email);
                        System.out.println("Đường dẫn: Email/" + email);
                    }
                    else
                    {
                        System.out.println("Thư mục đã tồn tại!");
                    }
                }
            }
            catch (Exception e)
            {
                System.err.println("Lỗi tạo thư mục: " + e.getMessage());
            }
        
            danhSachTaiKhoan.add(new TaiKhoan(hoTen.trim(), email.trim(), matKhau.trim(), "USER"));
            QuanLyFile.ghiFileTaiKhoan(danhSachTaiKhoan);
            return true;
        }
    }

    public boolean datLaiMatKhau(String email, String matKhauMoi) {
        if (email.isEmpty() || matKhauMoi.isEmpty()) throw new IllegalArgumentException("Vui lòng nhập đầy đủ thông tin!");
        if (matKhauMoi.length() < 6) throw new IllegalArgumentException("Mật khẩu mới phải có ít nhất 6 ký tự!");
        
        for (TaiKhoan tk : danhSachTaiKhoan) {
            if (tk.getEmail().equalsIgnoreCase(email.trim())) {
                tk.setMatKhau(matKhauMoi.trim());
                QuanLyFile.ghiFileTaiKhoan(danhSachTaiKhoan);
                return true;
            }
        }
        throw new IllegalArgumentException("Tài khoản không tồn tại trên hệ thống.");
    }
}