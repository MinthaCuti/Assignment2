/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Services;

/**
 *
 * @author Admin
 */
import Models.NguoiDung;
import Models.NguoiDungDaTungThue;
import Models.NhaTro;
import java.util.ArrayList;
import java.util.List;

public class NguoiDungService {

    private List<NguoiDung> listDangThue = new ArrayList<>();
    private List<NguoiDungDaTungThue> listLichSu = new ArrayList<>(); // Lưu khách đã rời đi
    private NhaTroService phongService; // Để tương tác với danh sách phòng

    public NguoiDungService(NhaTroService phongService) {
        this.phongService = phongService;
    }

    // HÀM QUAN TRỌNG: Xử lý thuê (dùng chung cho cả cá nhân và nhóm)
    // HÀM QUAN TRỌNG: Xử lý thuê (Đã cập nhật thêm tham số String sdt)
    public void thuePhong(String ma, String ten, String sdt, String maPhong, int soLuong) throws Exception {
        // 1. Tìm phòng trong hệ thống
        NhaTro phong = phongService.findPhong(maPhong);
        if (phong == null) {
            throw new Exception("Không tìm thấy mã phòng: " + maPhong);
        }

        // 2. Kiểm tra vật chất (Nếu hỏng không cho thuê)
        if (phong.getTinhTrangVatChat() == 1) {
            throw new Exception("Phòng " + maPhong + " đang hỏng, không thể cho thuê!");
        }

        // 3. Kiểm tra sức chứa (Max 10 người)
        phong.checkAndAddPeople(soLuong);

        // 4. Tạo đối tượng lưu trữ (Đã sửa: Thay "N/A" bằng biến sdt truyền vào)
        // Cấu trúc: NguoiDung(maND, hoTen, sdt, email, maPhong)
        // Ở đây bạn đang tạm dùng cột Email để lưu số lượng người (soLuong)
        NguoiDung nd = new NguoiDung(ma, ten, sdt, String.valueOf(soLuong), maPhong);
        
        // Thêm vào danh sách đang thuê trong RAM
        listDangThue.add(nd);
        
        System.out.println(">>> [Service] Đã thêm khách " + ten + " vào danh sách RAM.");
    }

    // Hàm xử lý khi khách/nhóm rời đi
    public void moveUserToHistory(String maND) {
        NguoiDung target = listDangThue.stream()
                .filter(u -> u.getMaND().equalsIgnoreCase(maND))
                .findFirst().orElse(null);

        if (target != null) {
            // Lấy số lượng người từ email (nơi ta tạm lưu số lượng) để trừ ra khỏi phòng
            int soLuongXoa = Integer.parseInt(target.getEmail());

            NhaTro phong = phongService.findPhong(target.getMaPhong());
            if (phong != null) {
                phong.removePeople(soLuongXoa); // Trừ người trong phòng ở RAM
            }

            Models.NguoiDungDaTungThue nguoiCu = new Models.NguoiDungDaTungThue(target, 1);
            // Chuyển sang danh sách lịch sử
            listLichSu.add(nguoiCu);
            listDangThue.remove(target);
        }
    }

    public List<NguoiDung> getListDangThue() {
        return listDangThue;
    }

    public List<NguoiDungDaTungThue> getListLichSu() {
        return listLichSu;
    }
}
