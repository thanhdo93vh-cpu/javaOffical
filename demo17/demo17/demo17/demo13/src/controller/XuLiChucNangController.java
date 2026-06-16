package controller;

import model.GiangVien;
import model.LopHoc;
import util.QuanLyFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class XuLiChucNangController {
    // Đổi sang file .txt
    private static final String FILE_LOP = "lophoc.txt";
    private static final String FILE_LICH_SU = "lichsu.txt";
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final List<String[]> lichSu = new ArrayList<>();
    private List<LopHoc> danhSachLop;

    public XuLiChucNangController() {
        danhSachLop = QuanLyFile.readFile(FILE_LOP);
        taiLichSu();
    }

    public String[] getDanhSachMaLop() {
        return danhSachLop.stream()
                .map(LopHoc::getMaLop)
                .toArray(String[]::new);
    }

    public String getTenGV(String maLop) {
        LopHoc lop = timLop(maLop);
        if (lop == null || lop.getGiangVienDay() == null) return "(chưa có)";
        return lop.getGiangVienDay().getTenGV();
    }

    public void hoanDoiGiaoVien(String maLop1, String maLop2) {
        if (maLop1.equals(maLop2)) {
            throw new IllegalArgumentException("Vui lòng chọn hai lớp khác nhau.");
        }
        danhSachLop = QuanLyFile.readFile(FILE_LOP); // đồng bộ dữ liệu mới nhất trước khi sửa
        LopHoc lop1 = timLop(maLop1);
        LopHoc lop2 = timLop(maLop2);
        if (lop1 == null || lop2 == null) {
            throw new IllegalArgumentException("Không tìm thấy lớp được chọn.");
        }

        String tenGV1 = lop1.getGiangVienDay() != null ? lop1.getGiangVienDay().getTenGV() : "trống";
        String tenGV2 = lop2.getGiangVienDay() != null ? lop2.getGiangVienDay().getTenGV() : "trống";

        GiangVien tam = lop1.getGiangVienDay();
        lop1.setGiangVienDay(lop2.getGiangVienDay());
        lop2.setGiangVienDay(tam);

        String thoiGian = LocalDateTime.now().format(FMT);
        ghiLichSu(maLop1, "Đổi GV: " + tenGV1 + " → " + tenGV2 + " lúc " + thoiGian);
        ghiLichSu(maLop2, "Đổi GV: " + tenGV2 + " → " + tenGV1 + " lúc " + thoiGian);

        luuFile();
    }

    public Object[][] thongKeGiangVien(int nguongQuaTai) {
        Map<String, int[]> thongKe = new LinkedHashMap<>();

        for (LopHoc lop : danhSachLop) {
            if (lop.getGiangVienDay() == null) continue;
            String tenGV = lop.getGiangVienDay().getTenGV();
            thongKe.putIfAbsent(tenGV, new int[]{0, 0});
            thongKe.get(tenGV)[0]++;
            thongKe.get(tenGV)[1] += lop.getSiSo();
        }

        Object[][] result = new Object[thongKe.size()][4];
        int i = 0;
        for (Map.Entry<String, int[]> e : thongKe.entrySet()) {
            result[i][0] = e.getKey();
            result[i][1] = e.getValue()[0];
            result[i][2] = e.getValue()[1];
            result[i][3] = e.getValue()[0] > nguongQuaTai ? "⚠ Quá tải" : "Bình thường";
            i++;
        }
        return result;
    }

    public Object[][] getLichSuTheoLop(String maLop) {
        if (maLop == null || maLop.isBlank()) {
            throw new IllegalArgumentException("Vui lòng nhập mã lớp.");
        }

        List<String[]> ketQua = new ArrayList<>();
        for (String[] bghiLs : lichSu) {
            if (bghiLs[0].equalsIgnoreCase(maLop)) {
                ketQua.add(new String[]{"Thay đổi", bghiLs[1]});
            }
        }

        if (ketQua.isEmpty()) {
            // Đồng bộ lại danh sách lớp mới nhất (có thể có lớp mới gộp
            // hoặc lớp cũ đã bị xóa sau khi gộp) trước khi báo không tồn tại.
            danhSachLop = QuanLyFile.readFile(FILE_LOP);
            if (timLop(maLop) == null) {
                throw new IllegalArgumentException("Mã lớp \"" + maLop + "\" không tồn tại.");
            }
            return new Object[][]{{"(chưa có thay đổi)", ""}};
        }
        return ketQua.toArray(new Object[0][]);
    }

    private LopHoc timLop(String maLop) {
        for (LopHoc lop : danhSachLop) {
            if (lop.getMaLop().equalsIgnoreCase(maLop)) return lop;
        }
        return null;
    }

    private void ghiLichSu(String maLop, String chiTiet) {
        lichSu.add(new String[]{maLop, chiTiet});
        themLichSuVaoFile(maLop, chiTiet);
    }

    /** Tải toàn bộ lịch sử thay đổi đã lưu từ file vào danh sách trong bộ nhớ */
    private void taiLichSu() {
        File f = new File(FILE_LICH_SU);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(f, java.nio.charset.StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] a = line.split(",", 2);
                if (a.length == 2) {
                    lichSu.add(new String[]{a[0], a[1]});
                }
            }
        } catch (IOException e) {
            System.out.println("Lỗi đọc file lịch sử: " + e.getMessage());
        }
    }

    /** Ghi nối thêm một bản ghi lịch sử mới xuống file để không bị mất khi khởi động lại */
    private void themLichSuVaoFile(String maLop, String chiTiet) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_LICH_SU, java.nio.charset.StandardCharsets.UTF_8, true))) {
            bw.write(maLop + "," + chiTiet);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Lỗi ghi file lịch sử: " + e.getMessage());
        }
    }

    public void ghiLichSuGopLop(java.util.List<String> dsMaLopCu, String maLopMoi, int siSo) {
        String thoiGian = LocalDateTime.now().format(FMT);
        for (String maLop : dsMaLopCu) {
            ghiLichSu(maLop, "Gộp vào lớp " + maLopMoi + " lúc " + thoiGian);
        }
        ghiLichSu(maLopMoi, "Tạo từ gộp " + dsMaLopCu.size() + " lớp (" + String.join(", ", dsMaLopCu) + "), tổng " + siSo + " SV lúc " + thoiGian);
    }

    public void chuyenSinhVien(String maLopHienTai, String maLopDen, java.util.List<String> dsMaSinhVien) {
        danhSachLop = QuanLyFile.readFile(FILE_LOP); // đồng bộ dữ liệu mới nhất trước khi sửa
        LopHoc lopHienTai = timLop(maLopHienTai);
        LopHoc lopDen = timLop(maLopDen);
        
        if (lopHienTai == null || lopDen == null) {
            throw new IllegalArgumentException("Lớp không tồn tại!");
        }
        
        if (dsMaSinhVien == null || dsMaSinhVien.isEmpty()) {
            throw new IllegalArgumentException("Chưa chọn sinh viên nào!");
        }
        
        for (String maSV : dsMaSinhVien) {
            model.SinhVien sv = null;
            for (model.SinhVien s : lopHienTai.getDanhSachLop()) {
                if (s.getMaSV().equals(maSV)) {
                    sv = s;
                    break;
                }
            }
            
            if (sv != null) {
                lopHienTai.getDanhSachLop().remove(sv);
                lopDen.getDanhSachLop().add(sv);
            }
        }
        
        String thoiGian = LocalDateTime.now().format(FMT);
        ghiLichSu(maLopHienTai, "Chuyển " + dsMaSinhVien.size() + " SV sang " + maLopDen + " lúc " + thoiGian);
        ghiLichSu(maLopDen, "Nhận " + dsMaSinhVien.size() + " SV từ " + maLopHienTai + " lúc " + thoiGian);
        
        luuFile();
    }

    private void luuFile() {
        QuanLyFile.ghiFile(new File(FILE_LOP), danhSachLop);
    }
}