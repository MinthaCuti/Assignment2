/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Services;

/**
 *
 * @author Admin
 */
import Models.NhaTro;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NhaTroService {

    // Danh sách lưu trữ trong RAM
    private List<NhaTro> listNhaTro = new ArrayList<>();

    // 1. Tìm kiếm phòng (Dùng cho Case 2, 3, 8)
    public NhaTro findPhong(String maPhong) {
        return listNhaTro.stream()
                .filter(nt -> nt.getMaPhong().equalsIgnoreCase(maPhong))
                .findFirst()
                .orElse(null);
    }

    // 2. Thống kê phòng trống (Sửa lỗi báo FULL nhầm)
    public void thongKePhongTrong(LoaiNhaService loaiService) {
        System.out.println("\n--- TÌNH TRẠNG HỆ THỐNG PHÒNG TRỌ ---");

        var dsLoai = loaiService.getAll();
        if (dsLoai.isEmpty()) {
            System.out.println(">>>Cảnh báo: Chưa có dữ liệu Loại Nhà từ SQL!");
            return;
        }

        for (var loai : dsLoai) {
            // Lọc danh sách phòng thuộc loại này
            List<NhaTro> phongThuocLoai = listNhaTro.stream()
                    .filter(nt -> nt.getMaLoai().equalsIgnoreCase(loai.getMaLoai()))
                    .collect(Collectors.toList());

            // Đếm phòng thực sự trống (TrangThai = 0 và Vật chất tốt = 0)
            long countTrong = phongThuocLoai.stream()
                    .filter(nt -> nt.getTrangThai() == 0 && nt.getTinhTrangVatChat() == 0)
                    .count();

            if (phongThuocLoai.isEmpty()) {
                // Nếu SQL có Loại nhà nhưng RAM chưa có phòng nào tương ứng
                System.out.printf("Loại %s (%s): CHƯA CÓ PHÒNG TRÊN HỆ THỐNG\n",
                        loai.getMaLoai(), loai.getTenLoai());
            } else {
                System.out.printf("Loại %s (%s): %s\n",
                        loai.getMaLoai(),
                        loai.getTenLoai(),
                        (countTrong > 0) ? "Còn " + countTrong + " phòng trống" : "HẾT PHÒNG");
            }
        }
    }

    // 3. Gợi ý phòng (Chỉ lấy phòng Trống + Tốt)
    public List<NhaTro> getPhongGoiY() {
        return listNhaTro.stream()
                .filter(nt -> nt.getTrangThai() == 0)
                .filter(nt -> nt.getTinhTrangVatChat() == 0)
                .collect(Collectors.toList());
    }

    // 4. Cập nhật hư hỏng (Case 8)
    public void capNhatTinhTrang(String maPhong, int tinhTrangMoi) {
        NhaTro nt = findPhong(maPhong);
        if (nt != null) {
            nt.setTinhTrangVatChat(tinhTrangMoi);
        }
    }

    // 5. Thêm phòng vào danh sách (Dùng cho initData)
    public void add(NhaTro nt) {
        listNhaTro.add(nt);
    }

    // 6. Lấy toàn bộ danh sách
    public List<NhaTro> getAll() {
        return listNhaTro;
    }

    // Thêm vào NhaTroService.java
    public List<NhaTro> getPhongTrongTheoLoai(String maLoai) {
        return listNhaTro.stream()
                .filter(nt -> nt.getMaLoai().equalsIgnoreCase(maLoai))
                .filter(nt -> nt.getTrangThai() == 0 && nt.getTinhTrangVatChat() == 0)
                .collect(Collectors.toList());
    }
}
