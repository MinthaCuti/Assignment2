/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

/**
 *
 * @author Admin
 */
import Services.*;
import Utilities.*;
import java.util.Scanner;

public class Main {

    private static Scanner sc = new Scanner(System.in);
    private static NhaTroService phongService = new NhaTroService();
    private static NguoiDungService userService = new NguoiDungService(phongService);
    private static DataRepository sqlRepo = new DataRepository();
    
    // Khoi tao MainService de xu ly logic
    private static MainService mainService = new MainService(phongService, userService, sqlRepo, sc);

    public static void main(String[] args) {
        // 1. Lam sach Database de Demo
        sqlRepo.clearAllDataDemo();

        // 2. Nap du lieu mac dinh thong qua Service
        mainService.initData();

        while (true) {
            System.out.println("\n========= HỆ THỐNG QUẢN LÝ NHÀ TRỌ =========");
            System.out.println("1.  Xem tình trạng & Đếm phòng trống");
            System.out.println("2.  Thuê phòng (Cá nhân/Nhóm)");
            System.out.println("3.  Trả phòng (Chuyển sang lịch sử)");
            System.out.println("4.  Xem bảng giá Điện/Nước & Dịch vụ");
            System.out.println("5.  Gợi ý phòng trống & Vật chất tốt");
            System.out.println("6.  Xem danh sách khách ĐANG THUÊ");
            System.out.println("7.  Xem lịch sử khách ĐÃ RỜI ĐI");
            System.out.println("8.  Cập nhật hư hỏng vật chất");
            System.out.println("9.  Gửi đánh giá dịch vụ");
            System.out.println("10. Xuất danh sách khách ra File (.txt)");
            System.out.println("0.  Thoát");
            System.out.print("Mời chọn chức năng (0-10): ");

            try {
                int chon = Integer.parseInt(sc.nextLine());
                switch (chon) {
                    case 1 -> phongService.thongKePhongTrong(new LoaiNhaService());
                    case 2 -> mainService.handleThuePhong();
                    case 3 -> mainService.handleRoiDi();
                    case 4 -> mainService.inGiaDichVu();
                    case 5 -> mainService.showPhongGoiY();
                    case 6 -> mainService.showKhachDangThue();
                    case 7 -> mainService.showLichSu();
                    case 8 -> mainService.handleCapNhatVatChat();
                    case 9 -> mainService.handleDanhGia(null);
                    case 10 -> mainService.exportToFile();
                    case 0 -> {
                        System.out.println("Cảm ơn đã sử dụng hệ thống!");
                        System.exit(0);
                    }
                    default -> System.out.println("Lựa chọn không hợp lệ!");
                }
            } catch (NumberFormatException e) {
                System.err.println("Lỗi: Vui lòng nhập số từ 0 đến 10!");
            } catch (Exception e) {
                System.err.println("Lỗi hệ thống: " + e.getMessage());
            }
        }
    }
}