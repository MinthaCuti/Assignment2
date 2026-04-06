/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author Admin
 */
public class DanhGia {

    private String maDG;
    private String maND; // Liên kết với MaND ở bảng NguoiDung hoặc LichSu
    private int soSao;   // 1 - 5 sao
    private String noiDung;
    private String ngayDanhGia;

    public DanhGia() {
    }

    public DanhGia(String maDG, String maND, int soSao, String noiDung, String ngayDanhGia) {
        this.maDG = maDG;
        this.maND = maND;
        this.soSao = soSao;
        this.noiDung = noiDung;
        this.ngayDanhGia = ngayDanhGia;
    }

    // Getter và Setter
    public String getMaND() {
        return maND;
    }

    public void setMaND(String maND) {
        this.maND = maND;
    }

    public int getSoSao() {
        return soSao;
    }

    public void setSoSao(int soSao) {
        this.soSao = soSao;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getMaDG() {
        return maDG;
    }

    public void setMaDG(String maDG) {
        this.maDG = maDG;
    }

    public String getNgayDanhGia() {
        return ngayDanhGia;
    }

    public void setNgayDanhGia(String ngayDanhGia) {
        this.ngayDanhGia = ngayDanhGia;
    }

    @Override
    public String toString() {
        String sao = "⭐".repeat(soSao);
        return String.format("User: %s | %s | Nội dung: %s", maND, sao, noiDung);
    }
}
