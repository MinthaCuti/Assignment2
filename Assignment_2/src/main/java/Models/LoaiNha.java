/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author Admin
 */
public class LoaiNha {

    private String maLoai;
    private String tenLoai;
    private double giaCoDinh; // Dùng %f khi in
    private String moTa;

    public LoaiNha() {
    }

    public LoaiNha(String maLoai, String tenLoai, double giaCoDinh, String moTa) {
        this.maLoai = maLoai;
        this.tenLoai = tenLoai;
        this.giaCoDinh = giaCoDinh;
        this.moTa = moTa;
    }

    // Getter và Setter
    public String getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(String maLoai) {
        this.maLoai = maLoai;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }

    public double getGiaCoDinh() {
        return giaCoDinh;
    }

    public void setGiaCoDinh(double giaCoDinh) {
        this.giaCoDinh = giaCoDinh;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    @Override
    public String toString() {
        return String.format("Loại: %s | Tên: %s | Giá: %.2f | Mô tả: %s",
                maLoai, tenLoai, giaCoDinh, moTa);
    }
}
