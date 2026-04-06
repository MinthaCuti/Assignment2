/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Services;

/**
 *
 * @author Admin
 */
import Models.*;
import Utilities.*;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class MainService {

    private NhaTroService phongService;
    private NguoiDungService userService;
    private DataRepository sqlRepo;
    private Scanner sc;

    public MainService(NhaTroService phongService, NguoiDungService userService, DataRepository sqlRepo, Scanner sc) {
        this.phongService = phongService;
        this.userService = userService;
        this.sqlRepo = sqlRepo;
        this.sc = sc;
    }

    public void initData() {
        phongService.add(new NhaTro("P101", "L1", 20.0, 2000000, 0, 0));
        phongService.add(new NhaTro("P102", "L1", 20.0, 2000000, 0, 0));
        phongService.add(new NhaTro("P103", "L1", 20.0, 2000000, 0, 0));

        phongService.add(new NhaTro("P201", "L2", 35.0, 3500000, 0, 0));
        phongService.add(new NhaTro("P202", "L2", 35.0, 3500000, 0, 0));
        phongService.add(new NhaTro("P203", "L2", 35.0, 3500000, 0, 0));

        phongService.add(new NhaTro("P301", "L3", 50.0, 5000000, 0, 0));
        phongService.add(new NhaTro("P302", "L3", 50.0, 5000000, 0, 0));
        phongService.add(new NhaTro("P303", "L3", 50.0, 5000000, 0, 0));
        System.out.println(">>> Hệ thống: Đã khởi tạo dữ liệu phòng.");
    }

    public void inGiaDichVu() {
        System.out.println("\n--- BẢNG GIÁ DỊCH VỤ ---");
        System.out.println("1. Điện: 3.500 VND/kWh | 2. Nước: 15.000 VND/m3");
    }

    public void handleThuePhong() {
        try {
            String maLoai = "";
            List<NhaTro> dsTrong = null;

            while (true) {
                System.out.println("\n--- CHỌN LOẠI PHÒNG MUỐN THUÊ ---");
                System.out.println("L1. Phòng Đơn | L2. Phòng Đôi | L3. Phòng VIP");
                System.out.print("Nhập mã loại (L1/L2/L3): ");
                maLoai = sc.nextLine().trim().toUpperCase();

                if (maLoai.matches("L[1-3]")) {
                    dsTrong = phongService.getPhongTrongTheoLoai(maLoai);
                    if (dsTrong.isEmpty()) {
                        System.out.println("Loại " + maLoai + " hiện tại đã hết phòng trống.");
                        return;
                    }
                    break;
                } else {
                    System.out.println("Sai định dạng! Vui lòng chỉ nhập L1, L2 hoặc L3.");
                }
            }

            dsTrong.forEach(p -> System.out.println(" -> Mã phòng: " + p.getMaPhong() + " | Diện tích: " + p.getDienTich() + "m2"));

            String mp = "";
            while (true) {
                System.out.print("Nhập mã phòng bạn chọn: ");
                mp = sc.nextLine().trim().toUpperCase();
                final String fMp = mp;
                if (dsTrong.stream().anyMatch(p -> p.getMaPhong().equals(fMp))) {
                    break;
                } else {
                    System.out.println("Mã phòng không hợp lệ.");
                }
            }

            String maND = "";
            String mobile = "";
            String regex = "^[KN]\\d{3}$";
            String regexSDT = "0\\d{9}$";

            while (true) {
                // 1. Nhập và kiểm tra định dạng Regex
                System.out.print("Mã định danh (Khách/Nhóm) (K00x/N00x): ");
                maND = sc.nextLine().trim();

                if (!maND.matches(regex)) {
                    System.out.println("Lỗi: Định dạng không hợp lệ! Vui lòng nhập theo mẫu K00x hoặc N00x.");
                    continue; // Bắt nhập lại ngay lập tức
                }

                // Nếu định dạng đã đúng, mới kiểm tra xem mã đã tồn tại chưa
                final String fMa = maND;
                boolean isDuplicate = userService.getListDangThue().stream()
                        .anyMatch(u -> u.getMaND().equalsIgnoreCase(fMa));

                if (isDuplicate) {
                    System.out.println("Lỗi: Mã này đang được sử dụng.");
                    continue;
                }

                // 2. Nhập số điện thoại và kiểm tra định dạng
                System.out.println("Nhập số điện thoại người thuê: ");
                mobile = sc.nextLine().trim();

                if (!mobile.matches(regexSDT)) {
                    System.out.println("Lỗi: Định dạng không hợp lệ! SĐT phải bắt đầu bằng số 0 và có đúng 10 chữ số!");
                    continue;
                }

                // Kiểm tra trùng số điện thoại
                final String fmobile = mobile;
                // Sửa lỗi: anyMatch cần so sánh bằng equals thay vì chỉ gọi getSdt()
                boolean isSdtTrung = userService.getListDangThue().stream()
                        .anyMatch(u -> u.getSdt() != null && u.getSdt().equals(fmobile));

                if (isSdtTrung) {
                    System.out.println("Lỗi: Số điện thoại này đã được đăng ký bởi người khác.");
                    continue;
                }
                break;
            }

            System.out.print("Chọn hình thức (1. Cá nhân | 2. Nhóm): ");
            int ht = Integer.parseInt(sc.nextLine());
            System.out.print("Tên người đại diện: ");
            String ten = sc.nextLine().trim();

            int sl = 1;
            if (ht == 2) {
                System.out.print("Số lượng người (2-10): ");
                sl = Integer.parseInt(sc.nextLine());
            }

//--------------------------------------------------------------------------------
            NguoiDung ndMoi = new NguoiDung(maND, ten, mobile, String.valueOf(sl), mp);

            // 2. Gọi Service để lưu vào RAM (QUAN TRỌNG: Phải truyền thêm 'mobile' vào đây)
            userService.thuePhong(maND, ten, mobile, mp, sl);

            // 3. Gọi Repo để lưu vào SQL
            sqlRepo.insertNguoiDungSql(ndMoi, sl);
            System.out.println("~ THUÊ PHÒNG THÀNH CÔNG ~");
            Models.NhaTro phongVuaThue = phongService.findPhong(mp);
            if (phongVuaThue != null) {
                // Tạo hóa đơn tạm
                Models.HoaDon hd = new Models.HoaDon("HD_TAM", mp, maND, phongVuaThue.getGiaRieng());
                // In phiếu tạm tính
                System.out.println();
                hd.inTamTinh(phongVuaThue.getGiaRieng(), sl);
            }
        } catch (Exception e) {
            System.err.println("Lỗi thuê phòng: " + e.getMessage());
        }
    }

    public void handleRoiDi() {
        try {
            System.out.print("Nhập mã khách muốn trả phòng: ");
            String ma = sc.nextLine().trim();

            // 1. Tìm đối tượng khách TRƯỚC
            NguoiDung target = userService.getListDangThue().stream()
                    .filter(u -> u.getMaND().equalsIgnoreCase(ma))
                    .findFirst().orElse(null);

            if (target != null) {
                String confirm = "";
                do {
                    System.out.print("Xác nhận trả phòng cho mã [" + ma + "]? (C/K): ");
                    confirm = sc.nextLine().trim().toUpperCase();
                } while (!confirm.equals("C") && !confirm.equals("K"));

                if (confirm.equals("C")) {
                    // --- BƯỚC QUAN TRỌNG: ĐÁNH GIÁ TRƯỚC KHI XÓA ---
                    System.out.print("Gửi đánh giá luôn không? (C/K): ");
                    if (sc.nextLine().trim().equalsIgnoreCase("C")) {
                        // Lúc này khách vẫn còn trong listDangThue nên handleDanhGia sẽ tìm thấy
                        handleDanhGia(ma);
                    }

                    // --- SAU ĐÓ MỚI THỰC HIỆN XÓA ---
                    String mp = target.getMaPhong();

                    // Xóa khỏi RAM
                    userService.moveUserToHistory(ma);

                    // Xóa khỏi SQL và đẩy sang Lịch sử
                    sqlRepo.deleteAndMoveToHistorySql(target);

                    // Cập nhật trạng thái phòng về "Trống" (0) nếu không còn ai ở
                    if (phongService.findPhong(mp).getSoNguoiHienTai() == 0) {
                        sqlRepo.updateTrangThaiPhongSql(mp, 0);
                    }

                    System.out.println("~ TRẢ PHÒNG THÀNH CÔNG ~");
                }
            } else {
                System.out.println("Lỗi: Không tìm thấy người thuê nào với mã " + ma);
            }
        } catch (Exception e) {
            System.err.println("Lỗi trả phòng: " + e.getMessage());
        }
    }

    public void handleCapNhatVatChat() {
        try {
            System.out.print("Nhập mã loại (L1-L3): ");
            String ml = sc.nextLine().trim().toUpperCase();
            List<NhaTro> ds = phongService.getPhongTrongTheoLoai(ml);
            if (ds.isEmpty()) {
                return;
            }
            ds.forEach(p -> System.out.println(" -> Mã: " + p.getMaPhong()));
            System.out.print("Nhập mã phòng: ");
            String mp = sc.nextLine().trim().toUpperCase();
            System.out.print("Tình trạng (0: Tốt | 1: Hỏng): ");
            int tt = Integer.parseInt(sc.nextLine());
            phongService.capNhatTinhTrang(mp, tt);
            sqlRepo.updateTinhTrangVatChatSql(mp, tt);
            System.out.println("CẬP NHẬT THÀNH CÔNG.");
        } catch (Exception e) {
            System.err.println("Lỗi cập nhật: " + e.getMessage());
        }
    }

    public void handleDanhGia(String maTuDong) {
        try {
            String ma;
            // Kiểm tra nếu không có mã tự động thì mới bắt nhập tay
            if (maTuDong == null || maTuDong.isEmpty()) {
                System.out.print("Nhập mã khách hàng: ");
                ma = sc.nextLine().trim();
            } else {
                ma = maTuDong;
                System.out.println("Thực hiện đánh giá cho mã: " + ma);
            }
            var userOpt = userService.getListDangThue().stream()
                    .filter(u -> u.getMaND().equalsIgnoreCase(ma))
                    .findFirst();

            if (userOpt.isEmpty()) {
                System.out.println("Lỗi: Không tìm thấy người thuê nào với mã " + ma);
                return; // Thoát ra nếu không thấy người thuê
            }

            String tenNguoiThue = userOpt.get().getHoTen(); // Lấy tên từ object User
            System.out.println(">> Đang thực hiện đánh giá cho: " + tenNguoiThue + " (" + ma + ")");

            System.out.print("Số sao (1-5): ");
            int sao = Integer.parseInt(sc.nextLine());

            System.out.print("Nội dung: ");
            String nd = sc.nextLine().trim();

            sqlRepo.insertDanhGiaSql(ma, tenNguoiThue, sao, nd);
            System.out.println("~ ĐÃ LƯU ĐÁNH GIÁ ~");
        } catch (Exception e) {
            System.err.println("Lỗi đánh giá: " + e.getMessage());
        }
    }

    public void showKhachDangThue() {
        System.out.println("\n==========================================================================");
        System.out.println("                DANH SÁCH KHÁCH HÀNG ĐANG THUÊ PHÒNG                      ");
        System.out.println("==========================================================================");

        // 1. Lấy danh sách từ Service 
        // Lưu ý: Đảm bảo trong NguoiDungService đã xóa dòngs
        List<NguoiDung> ds = userService.getListDangThue();

        // 2. Kiểm tra nếu danh sách trống
        if (ds == null || ds.isEmpty()) {
            System.out.println(" >>> THÔNG BÁO: Hiện tại hệ thống chưa có khách hàng nào đang thuê.");
            System.out.println("==========================================================================");
            return; // Thoát hàm sớm
        }

        // 3. In tiêu đề cột (Sử dụng printf để căn lề)
        // %-10s: Căn lề trái 10 ký tự | %-20s: Căn lề trái 20 ký tự...
        System.out.printf("%-10s | %-20s | %-12s | %-10s | %-5s\n",
                "Mã ND", "Họ và Tên", "Số ĐT", "Mã Phòng", "SL");
        System.out.println("--------------------------------------------------------------------------");

        // 4. Duyệt danh sách và in dữ liệu
        for (NguoiDung nd : ds) {
            // Lấy số lượng người tạm lưu ở cột Email
            String soLuong = nd.getEmail();

            System.out.printf("%-10s | %-20s | %-12s | %-10s | %-5s\n",
                    nd.getMaND(),
                    nd.getHoTen(),
                    nd.getSdt(),
                    nd.getMaPhong(),
                    soLuong);
        }

        System.out.println("--------------------------------------------------------------------------");
        System.out.println(" Tổng cộng: " + ds.size() + " nhóm/khách đang ở.");
        System.out.println("==========================================================================\n");
    }

    public void showLichSu() {
        System.out.println("\n==========================================================================");
        System.out.println("                DANH SÁCH KHÁCH HÀNG ĐANG THUÊ PHÒNG                      ");
        System.out.println("==========================================================================");

        List<NguoiDungDaTungThue> ds = userService.getListLichSu();

        if (ds == null || ds.isEmpty()) {
            System.out.println(" >>> THÔNG BÁO: Hiện tại hệ thống chưa có khách hàng nào đang thuê.");
            System.out.println("==========================================================================");
            return; // Thoát hàm sớm
        }

        System.out.printf("%-10s | %-20s | %-12s | %-10s | %-5s\n",
                "Mã ND", "Họ và Tên", "Số ĐT", "Mã Phòng", "SL");
        System.out.println("--------------------------------------------------------------------------");

        for (NguoiDung nd : ds) {
            // Lấy số lượng người tạm lưu ở cột Email
            String soLuong = nd.getEmail();

            System.out.printf("%-10s | %-20s | %-12s | %-10s | %-5s\n",
                    nd.getMaND(),
                    nd.getHoTen(),
                    nd.getSdt(),
                    nd.getMaPhong(),
                    soLuong);
        }
    }

    public void showPhongGoiY() {
        phongService.getPhongGoiY().forEach(System.out::println);
    }

    public void exportToFile() {
        // Lay thoi gian thuc va format
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy_HHmmss");
        String timestamp = now.format(formatter);

        String fileName = "danhsachnhatro_" + timestamp + ".txt";

        try (PrintWriter pw = new PrintWriter(fileName)) {
            pw.println("DANH SÁCH KHÁCH THUÊ - THỜI GIAN XUẤT: " + now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            pw.println("--------------------------------------------------");
            userService.getListDangThue().forEach(nd -> pw.println(nd.toString()));
            System.out.println("ĐÃ XUẤT FILE: " + fileName);
        } catch (Exception e) {
            System.err.println("Lỗi xuất file: " + e.getMessage());
        }
    }
}
