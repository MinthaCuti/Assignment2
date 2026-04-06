/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author Admin
 */
import java.util.Date;

public class HoaDon {

    private String maHD;
    private String maPhong;
    private String maND;
    private double tongTien;
    private Date ngayLap;

    // Constructor
    public HoaDon() {
    }

    public HoaDon(String maHD, String maPhong, String maND, double tongTien) {
        this.maHD = maHD;
        this.maPhong = maPhong;
        this.maND = maND;
        this.tongTien = tongTien;
        this.ngayLap = new Date();
    }

    // Logic in hóa đơn nhanh để khách xem giá
    public void inTamTinh(double giaPhong, int soNguoi) {
        System.out.println("--- PHIẾU TẠM TÍNH ---");
        System.out.printf("Giá phòng: %,.0f VND\n", giaPhong);
        System.out.println("Số người ở: " + soNguoi);
        System.out.printf("Tổng cộng: %,.0f VND\n", (giaPhong * soNguoi)); // Có thể thêm logic phụ phí ở đây
        System.out.println("----------------------");
    }
}
