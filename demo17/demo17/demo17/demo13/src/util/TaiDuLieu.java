package util;

import java.util.List;
import java.util.ArrayList;
import java.io.File;
import model.GiangVien;
import model.LopHoc;
import model.SinhVien;
import model.TaiKhoan;

public class TaiDuLieu {
    List<LopHoc> danhSachLopHoc = new ArrayList<>();
    List<GiangVien> danhsachGiangVien = new ArrayList<>();
    List<SinhVien> danhsachSinhVien = new ArrayList<>();
    List<TaiKhoan> danhSachTaiKhoan = new ArrayList<>();
    
    public <T> void Tai() {
        // Tự tạo tài khoản Admin
        danhSachTaiKhoan.add(new TaiKhoan("Quản trị", "Viên", "admin", "123456"));
        danhSachTaiKhoan.add(new TaiKhoan("Nguyễn", "Quản Lý", "quanly", "123456"));

        // 1. Tạo Giảng viên (10 người)
        GiangVien gv1 = new GiangVien("GV01", "Nguyễn Thị Lan", "Hà Nội", "lanng123@email.com", "Công nghệ Thông tin và Truyền thông");
        GiangVien gv2 = new GiangVien("GV02", "Phan Huy Tuấn", "Ninh Bình", "tuanhu@email.com", "Điện - Điện tử");
        GiangVien gv3 = new GiangVien("GV03", "Ngô Anh Tài", "Hà Nội", "taihuy8386@email.com", "Kinh tế");
        GiangVien gv4 = new GiangVien("GV04", "Phan Thế Thuật", "Nghệ An", "langvuong11@email.com", "Cơ khí - Ô tô");
        GiangVien gv5 = new GiangVien("GV05", "Nguyễn Tuấn Anh", "Hải Phòng", "tunah@email.com", "Ngoại ngữ - Du lịch");
        GiangVien gv6 = new GiangVien("GV06", "Trần Mai Phương", "Đà Nẵng", "phuongtm@email.com", "Công nghệ Thông tin và Truyền thông");
        GiangVien gv7 = new GiangVien("GV07", "Lê Văn Luyện", "Bắc Giang", "luyenlv@email.com", "Cơ khí - Ô tô");
        GiangVien gv8 = new GiangVien("GV08", "Phạm Thu Hà", "Hà Nội", "haphu@email.com", "Ngoại ngữ - Du lịch");
        GiangVien gv9 = new GiangVien("GV09", "Đinh Trọng Thủy", "Nam Định", "thuydt@email.com", "Điện - Điện tử");
        GiangVien gv10 = new GiangVien("GV10", "Vũ Nhật Minh", "Hưng Yên", "minhvn@email.com", "Kinh tế");

        danhsachGiangVien.addAll(List.of(gv1, gv2, gv3, gv4, gv5, gv6, gv7, gv8, gv9, gv10));
        
        // 2. Tạo Sinh viên (20 người đa dạng)
        SinhVien sv01 = new SinhVien("SV001", "Đoàn Minh Quân", "28/03/2000", "Nam", 3.7, "Đang học");
        SinhVien sv02 = new SinhVien("SV002", "Nguyễn Thị Huyền", "12/05/2006", "Nữ", 3.2, "Đang học");
        SinhVien sv03 = new SinhVien("SV003", "Trần Văn Cường", "20/08/2006", "Nam", 2.8, "Đang học");
        SinhVien sv04 = new SinhVien("SV004", "Lê Thị Dung", "15/01/2006", "Nữ", 3.5, "Đang học");
        SinhVien sv05 = new SinhVien("SV005", "Hoàng Văn Tuấn", "22/11/2006", "Nam", 2.5, "Đang học");
        SinhVien sv06 = new SinhVien("SV006", "Đỗ Minh Khang", "01/02/2006", "Nam", 3.8, "Đang học");
        SinhVien sv07 = new SinhVien("SV007", "Bùi Thu Trà", "05/06/2006", "Nữ", 3.1, "Đang học");
        SinhVien sv08 = new SinhVien("SV008", "Ngô Tấn Phát", "19/09/2006", "Nam", 2.9, "Đang học");
        SinhVien sv09 = new SinhVien("SV009", "Phạm Hoàng Anh", "30/12/2006", "Nam", 3.6, "Đang học");
        SinhVien sv10 = new SinhVien("SV010", "Vũ Bích Ngọc", "14/02/2006", "Nữ", 3.9, "Đang học");
        SinhVien sv11 = new SinhVien("SV011", "Đặng Quang Huy", "23/07/2006", "Nam", 2.4, "Bảo lưu");
        SinhVien sv12 = new SinhVien("SV012", "Hồ Bích Liên", "09/08/2006", "Nữ", 3.3, "Đang học");
        SinhVien sv13 = new SinhVien("SV013", "Dương Tấn Phong", "11/11/2006", "Nam", 2.7, "Đang học");
        SinhVien sv14 = new SinhVien("SV014", "Lý Thị Nhàn", "17/04/2006", "Nữ", 3.4, "Đang học");
        SinhVien sv15 = new SinhVien("SV015", "Phan Vũ Long", "04/11/2006", "Nam", 3.0, "Đang học");
        SinhVien sv16 = new SinhVien("SV016", "Trịnh Gia Bảo", "03/05/2006", "Nam", 2.6, "Đang học");
        SinhVien sv17 = new SinhVien("SV017", "Mai Phương Thảo", "29/10/2006", "Nữ", 3.0, "Đang học");
        SinhVien sv18 = new SinhVien("SV018", "Tạ Văn Hùng", "08/01/2006", "Nam", 2.2, "Đang học");
        SinhVien sv19 = new SinhVien("SV019", "Lương Thu Trang", "16/06/2006", "Nữ", 3.5, "Đang học");
        SinhVien sv20 = new SinhVien("SV020", "Chu Viết Nam", "25/09/2006", "Nam", 3.1, "Đang học");

        danhsachSinhVien.addAll(List.of(sv01, sv02, sv03, sv04, sv05, sv06, sv07, sv08, sv09, sv10, 
                                        sv11, sv12, sv13, sv14, sv15, sv16, sv17, sv18, sv19, sv20));

        // 3. Tạo Lớp Học (10 Lớp)
        LopHoc lop1 = new LopHoc("LH001", "Kỹ thuật phần mềm 1", "Công nghệ Thông tin và Truyền thông", gv1, new ArrayList<>());
        LopHoc lop2 = new LopHoc("LH002", "Kỹ thuật phần mềm 2", "Công nghệ Thông tin và Truyền thông", gv6, new ArrayList<>());
        LopHoc lop3 = new LopHoc("LH003", "Điện tử viễn thông 1", "Điện - Điện tử", gv2, new ArrayList<>());
        LopHoc lop4 = new LopHoc("LH004", "Tự động hóa 1", "Điện - Điện tử", gv9, new ArrayList<>());
        LopHoc lop5 = new LopHoc("LH005", "Kế toán kiểm toán", "Kinh tế", gv3, new ArrayList<>());
        LopHoc lop6 = new LopHoc("LH006", "Quản trị kinh doanh", "Kinh tế", gv10, new ArrayList<>());
        LopHoc lop7 = new LopHoc("LH007", "Công nghệ Ô tô 1", "Cơ khí - Ô tô", gv4, new ArrayList<>());
        LopHoc lop8 = new LopHoc("LH008", "Cơ điện tử 1", "Cơ khí - Ô tô", gv7, new ArrayList<>());
        LopHoc lop9 = new LopHoc("LH009", "Ngôn ngữ Anh", "Ngoại ngữ - Du lịch", gv5, new ArrayList<>());
        LopHoc lop10 = new LopHoc("LH010", "Quản trị lữ hành", "Ngoại ngữ - Du lịch", gv8, new ArrayList<>());

        // 4. Thêm Sinh viên vào Lớp để tạo sĩ số
        lop1.themSinhVien(sv01); lop1.themSinhVien(sv02); lop1.themSinhVien(sv15); 
        lop2.themSinhVien(sv03); lop2.themSinhVien(sv04);
        lop3.themSinhVien(sv05); lop3.themSinhVien(sv06); lop3.themSinhVien(sv07);
        lop4.themSinhVien(sv08); lop4.themSinhVien(sv09);
        lop5.themSinhVien(sv10); lop5.themSinhVien(sv11);
        lop6.themSinhVien(sv12); lop6.themSinhVien(sv13); lop6.themSinhVien(sv14);
        lop7.themSinhVien(sv16); lop7.themSinhVien(sv17);
        lop8.themSinhVien(sv18);
        lop9.themSinhVien(sv19);
        lop10.themSinhVien(sv20);

        danhSachLopHoc.addAll(List.of(lop1, lop2, lop3, lop4, lop5, lop6, lop7, lop8, lop9, lop10));
        
        // 5. Ghi ra file .txt 
        QuanLyFile.ghiFile(new File("lophoc.txt"), danhSachLopHoc);
        QuanLyFile.ghiFile(new File("giangvien.txt"), danhsachGiangVien);
        QuanLyFile.ghiFile(new File("sinhvien.txt"), danhsachSinhVien);
        QuanLyFile.ghiFile(new File("taikhoan.txt"), danhSachTaiKhoan);
    }
    
    public SinhVien timSinhVien(String maSV) {
        for (SinhVien sv : danhsachSinhVien) {
            if (sv.getMaSV().equalsIgnoreCase(maSV)) return sv;
        }
        return null;
    }
}