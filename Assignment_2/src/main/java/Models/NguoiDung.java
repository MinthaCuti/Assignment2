/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author Admin
 */

public class NguoiDung {
    private String maND;
    private String hoTen;
    private String sdt;
    private String email; // Biến này sẽ dùng để lưu số lượng người (ép kiểu String)
    private String maPhong;

    public NguoiDung() {
    }

    // Constructor đầy đủ 5 tham số để MainApp gọi không bị lỗi
    public NguoiDung(String maND, String hoTen, String sdt, String email, String maPhong) {
        this.maND = maND;
        this.hoTen = hoTen;
        this.sdt = sdt;
        this.email = email;
        this.maPhong = maPhong;
    }

    // HÀM QUAN TRỌNG: Phải đúng tên getEmail() để NguoiDungService hết báo đỏ
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMaND() {
        return maND;
    }

    public void setMaND(String maND) {
        this.maND = maND;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getMaPhong() {
        return maPhong;
    }

    public void setMaPhong(String maPhong) {
        this.maPhong = maPhong;
    }

    @Override
    public String toString() {
        // Nếu email là số thì hiểu là số lượng người
        return String.format("Mã: %s | Tên: %s | Phòng: %s | Số lượng: %s", 
                              maND, hoTen, maPhong, email);
    }
}