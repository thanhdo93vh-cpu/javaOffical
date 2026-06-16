package util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import model.*;

public class QuanLyFile {

    // --- CÁC HÀM GIAO TIẾP VỚI CONTROLLER (Được giữ nguyên chữ ký) ---
    public static <T> List<T> readFile(File f) {
        return readFile(f.getName());
    }

    public static <T> List<T> readFile(String path) {
        // Tự động chuyển đổi đuôi .dat của hệ thống cũ sang .txt
        path = path.replace(".dat", ".txt"); 
        File f = new File(path);
        
        // Nếu file text chưa tồn tại, gọi hàm tạo dữ liệu giả lập
        if (!f.exists() || f.length() == 0) {
            new TaiDuLieu().Tai();
        }
        
        // Điều hướng luồng đọc text dựa trên tên file
        if (path.contains("giangvien")) return (List<T>) docFileGiangVien(path);
        if (path.contains("sinhvien")) return (List<T>) docFileSinhVien(path);
        if (path.contains("lophoc")) return (List<T>) docFileLopHoc(path);
        
        return new ArrayList<>();
    }

    public static <T> void writeFile(List<T> ds, File f) {
        ghiFile(f.getName(), ds);
    }

    public static <T> void ghiFile(File f, List<T> ds) {
        ghiFile(f.getName(), ds);
    }

    public static <T> void ghiFile(String path, List<T> ds) {
        path = path.replace(".dat", ".txt"); 
        
        if (path.contains("giangvien")) ghiFileGiangVien((List<GiangVien>) ds, path);
        else if (path.contains("sinhvien")) ghiFileSinhVien((List<SinhVien>) ds, path);
        else if (path.contains("lophoc")) ghiFileLopHoc((List<LopHoc>) ds, path);
    }

    // ========================================================================
    // CÁC HÀM ĐỌC/GHI FILE TEXT CỤ THỂ CHO TỪNG ĐỐI TƯỢNG (Dùng dấu phẩy ",")
    // ========================================================================
    
    // 1. TÀI KHOẢN (Format: Họ đệm, Tên, Email, Mật khẩu)
    private static final File f = new File("taikhoan.dat");
    
    public static List<TaiKhoan> docFileTaiKhoan()
    {
        List<TaiKhoan> tk = new ArrayList<>();
        
        if (!f.exists()) {
            tk.add(new TaiKhoan("Nguyễn Bá Tuấn", "tuan@admin.com", "123456", "ADMIN"));
            tk.add(new TaiKhoan("Nguyễn Thị Nhi", "nhi@admin.com", "123456", "ADMIN"));
            ghiFileTaiKhoan(tk);
            return tk;
        }
        
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f)))
        {
            tk = (ArrayList<TaiKhoan>) in.readObject();
        }
        catch (IOException | ClassNotFoundException e)
        {
            System.out.println("Lỗi đọc file tài khoản: " + e.getMessage());
        }
        return tk;
    }
    
    public static void ghiFileTaiKhoan(List<TaiKhoan> tk)
    {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f)))
        {
            out.writeObject(tk);
        }
        catch (IOException e)
        {
            System.out.println("Lỗi ghi file tài khoản: " + e.getMessage());
        }
    }

    // 2. GIẢNG VIÊN (Format: Mã GV, Tên GV, Địa chỉ, Email, Khoa)
    private static List<GiangVien> docFileGiangVien(String path) {
        List<GiangVien> ds = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] a = line.split(",", -1);
                if (a.length >= 5) ds.add(new GiangVien(a[0], a[1], a[2], a[3], a[4]));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return ds;
    }
    
    private static void ghiFileGiangVien(List<GiangVien> ds, String path) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8))) {
            for (GiangVien gv : ds) {
                bw.write(gv.getMaGV() + "," + gv.getTenGV() + "," + gv.getDiaChi() + "," + gv.getEmail() + "," + gv.getKhoa());
                bw.newLine();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    // 3. SINH VIÊN (Format: Mã SV, Tên SV, Ngày sinh, Giới tính, Điểm TB, Trạng thái)
    private static List<SinhVien> docFileSinhVien(String path) {
        List<SinhVien> ds = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] a = line.split(",", -1);
                if (a.length >= 6) {
                    double diem = 0;
                    try { diem = Double.parseDouble(a[4]); } catch (Exception ignored) {}
                    ds.add(new SinhVien(a[0], a[1], a[2], a[3], diem, a[5]));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return ds;
    }
    
    private static void ghiFileSinhVien(List<SinhVien> ds, String path) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8))) {
            for (SinhVien sv : ds) {
                bw.write(sv.getMaSV() + "," + sv.getTenSV() + "," + sv.getNgaySinh() + "," + sv.getGioiTinh() + "," + sv.getDiemTB() + "," + sv.getTrangThai());
                bw.newLine();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    // 4. LỚP HỌC (Format: Mã lớp, Tên lớp, Khoa, Mã GV Chủ Nhiệm, Mã SV1;Mã SV2;Mã SV3)
    private static List<LopHoc> docFileLopHoc(String path) {
        List<LopHoc> ds = new ArrayList<>();
        // Đọc kèm file Giảng viên và Sinh viên để nối quan hệ
        List<GiangVien> dsGV = docFileGiangVien(path.replace("lophoc", "giangvien"));
        List<SinhVien> dsSV = docFileSinhVien(path.replace("lophoc", "sinhvien"));
        
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] a = line.split(",", -1);
                
                if (a.length >= 4) {
                    String maLop = a[0];
                    String tenLop = a[1];
                    String khoa = a[2];
                    String maGV = a[3];
                    
                    // Tìm đối tượng GV
                    GiangVien gvChuNhiem = null;
                    for (GiangVien gv : dsGV) {
                        if (gv.getMaGV().equals(maGV)) { gvChuNhiem = gv; break; }
                    }
                    
                    // Tìm danh sách đối tượng SV
                    List<SinhVien> svLop = new ArrayList<>();
                    if (a.length >= 5 && !a[4].trim().isEmpty()) {
                        String[] dsMaSV = a[4].split(";");
                        for (String maSV : dsMaSV) {
                            for (SinhVien sv : dsSV) {
                                if (sv.getMaSV().equals(maSV)) { svLop.add(sv); break; }
                            }
                        }
                    }
                    ds.add(new LopHoc(maLop, tenLop, khoa, gvChuNhiem, svLop));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return ds;
    }

    private static void ghiFileLopHoc(List<LopHoc> ds, String path) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8))) {
            for (LopHoc lop : ds) {
                String maGV = lop.getGiangVienDay() != null ? lop.getGiangVienDay().getMaGV() : "";
                
                StringBuilder dsMaSV = new StringBuilder();
                if (lop.getDanhSachLop() != null) {
                    for (SinhVien sv : lop.getDanhSachLop()) {
                        dsMaSV.append(sv.getMaSV()).append(";");
                    }
                }
                if (dsMaSV.length() > 0) dsMaSV.deleteCharAt(dsMaSV.length() - 1); // Cắt dấu chấm phẩy thừa
                
                bw.write(lop.getMaLop() + "," + lop.getTenLop() + "," + lop.getKhoa() + "," + maGV + "," + dsMaSV.toString());
                bw.newLine();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}