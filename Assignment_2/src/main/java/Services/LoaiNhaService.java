/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Services;

/**
 *
 * @author Admin
 */

import Models.LoaiNha;
import java.util.ArrayList;
import java.util.List;

public class LoaiNhaService {
    private List<LoaiNha> listLoaiNha = new ArrayList<>();

    public LoaiNhaService() {
        // Khởi tạo sẵn 3 loại nhà như đề bài yêu cầu
        listLoaiNha.add(new LoaiNha("L1", "Phong Don", 2000000, "Phu hop 1-2 nguoi"));
        listLoaiNha.add(new LoaiNha("L2", "Phong Doi", 3500000, "Phu hop gia dinh nho"));
        listLoaiNha.add(new LoaiNha("L3", "Phong VIP", 5000000, "Day du tien nghi, view dep"));
    }

    public List<LoaiNha> getAll() {
        return listLoaiNha;
    }

    public LoaiNha findById(String maLoai) {
        return listLoaiNha.stream()
                .filter(l -> l.getMaLoai().equalsIgnoreCase(maLoai))
                .findFirst().orElse(null);
    }
}
