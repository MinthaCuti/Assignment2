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
            while (true) {
                System.out.print("Mã định danh (Khách/Nhóm) (K00x/N00x): ");
                maND = sc.nextLine().trim();
                final String fMa = maND;
                if (userService.getListDangThue().stream().anyMatch(u -> u.getMaND().equalsIgnoreCase(fMa))) {
                    System.out.println("Lỗi: Mã này đang được sử dụng.");
                } else {
                    break;
                }
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

            userService.thuePhong(maND, ten, mp, sl);
            sqlRepo.insertNguoiDungSql(new NguoiDung(maND, ten, "N/A", String.valueOf(sl), mp), sl);
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
            NguoiDung target = userService.getListDangThue().stream()
                    .filter(u -> u.getMaND().equalsIgnoreCase(ma))
                    .findFirst().orElse(null);

            if (target != null) {
                String confirm = "";
                do {
                    System.out.print("Xác nhận trả phòng? (C/K): ");
                    confirm = sc.nextLine().trim().toUpperCase();
                } while (!confirm.equals("C") && !confirm.equals("K"));

                if (confirm.equals("C")) {
                    String mp = target.getMaPhong();
                    userService.moveUserToHistory(ma);
                    sqlRepo.deleteAndMoveToHistorySql(target);
                    if (phongService.findPhong(mp).getSoNguoiHienTai() == 0) {
                        sqlRepo.updateTrangThaiPhongSql(mp, 0);
                    }
                    System.out.println("~ TRẢ PHÒNG THÀNH CÔNG ~");
                    System.out.print("Gửi đánh giá luôn không? (C/K): ");
                    if (sc.nextLine().trim().equalsIgnoreCase("C")) {
                        handleDanhGia(ma);
                    }
                }
            } else {
                System.out.println("Không tìm thấy mã khách.");
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

            System.out.print("Số sao (1-5): ");
            int sao = Integer.parseInt(sc.nextLine());

            System.out.print("Nội dung: ");
            String nd = sc.nextLine().trim();

            sqlRepo.insertDanhGiaSql(ma, sao, nd);
            System.out.println("~ ĐÃ LƯU ĐÁNH GIÁ ~");
        } catch (Exception e) {
            System.err.println("Lỗi đánh giá: " + e.getMessage());
        }
    }

    public void showKhachDangThue() {
        userService.getListDangThue().forEach(System.out::println);
    }

    public void showLichSu() {
        userService.getListLichSu().forEach(System.out::println);
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
