/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author Admin
 */
public class GiaDinhVu {

    private String tenDichVu;
    private double giaTien;
    private String donVi; // ví dụ: "kWh", "Tháng", "Người"

    public GiaDinhVu() {
    }

    public GiaDinhVu(String tenDichVu, double giaTien, String donVi) {
        this.tenDichVu = tenDichVu;
        this.giaTien = giaTien;
        this.donVi = donVi;
    }

    // Getter (giá trị có sẵn, không cần setter)
    public String getTenDichVu() {
        return tenDichVu;
    }

    public double getGiaTien() {
        return giaTien;
    }

    @Override
    public String toString() {
        return String.format("- %-15s: %,.0f VND/%s", tenDichVu, giaTien, donVi);
    }
}
