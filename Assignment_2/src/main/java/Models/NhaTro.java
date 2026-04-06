/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author Admin
 */
public class NhaTro {

    private String maPhong;
    private String maLoai;
    private double dienTich;
    private double giaRieng;
    private int trangThai; // 0: Trống, 1: Đã có người
    private int tinhTrangVatChat; // 0: Tốt, 1: Hỏng

    // BIẾN QUAN TRỌNG: Quản lý số người hiện có trong phòng
    private int soNguoiHienTai = 0;
    private static final int MAX_NGUOI = 10;

    public NhaTro() {
    }

    public NhaTro(String maPhong, String maLoai, double dienTich, double giaRieng, int trangThai, int tinhTrangVatChat) {
        this.maPhong = maPhong;
        this.maLoai = maLoai;
        this.dienTich = dienTich;
        this.giaRieng = giaRieng;
        this.trangThai = trangThai;
        this.tinhTrangVatChat = tinhTrangVatChat;
    }

    // Hàm kiểm tra và cộng thêm người (Dùng cho cả cá nhân và nhóm)
    public void checkAndAddPeople(int soLuongMoi) throws Exception {
        if (this.soNguoiHienTai + soLuongMoi > MAX_NGUOI) {
            throw new Exception("Phòng " + maPhong + " không đủ chỗ! (Hiện có: "
                    + soNguoiHienTai + ", thêm " + soLuongMoi + " sẽ vượt quá 10)");
        }
        this.soNguoiHienTai += soLuongMoi;

        // Nếu có người thì tự động chuyển trạng thái phòng sang "Đã thuê"
        if (this.soNguoiHienTai > 0) {
            this.trangThai = 1;
        }
    }

    // Hàm giảm số người khi có người rời đi
    public void removePeople(int soLuongBot) {
        this.soNguoiHienTai -= soLuongBot;
        if (this.soNguoiHienTai <= 0) {
            this.soNguoiHienTai = 0;
            this.trangThai = 0; // Phòng trống hoàn toàn
        }
    }

    // Getters và Setters
    public String getMaPhong() {
        return maPhong;
    }

    public void setMaPhong(String maPhong) {
        this.maPhong = maPhong;
    }

    public String getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(String maLoai) {
        this.maLoai = maLoai;
    }

    public int getSoNguoiHienTai() {
        return soNguoiHienTai;
    }

    public double getGiaRieng() {
        return giaRieng;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public int getTinhTrangVatChat() {
        return tinhTrangVatChat;
    }

    public void setTinhTrangVatChat(int tinhTrangVatChat) {
        this.tinhTrangVatChat = tinhTrangVatChat;
    }
    // Thêm vào trong class NhaTro.java

    public double getDienTich() {
        return dienTich;
    }

    public void setDienTich(double dienTich) {
        this.dienTich = dienTich;
    }

    @Override
    public String toString() {
        String tt = (trangThai == 0) ? "Trống" : "Đang thuê (" + soNguoiHienTai + "/10 người)";
        String vc = (tinhTrangVatChat == 0) ? "Tốt" : "Hỏng";
        return String.format("Phòng: %s | Loại: %s | %s | Tình trạng: %s | Giá: %,.0f VNĐ",
                maPhong, maLoai, tt, vc, giaRieng);
    }
}
