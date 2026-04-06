/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author Admin
 */
public class TrangThai {
    // Trạng thái thuê phòng
    public enum Phong {
        TRONG,      // Còn trống hoàn toàn
        DANG_THUE,  // Có người ở nhưng chưa full (dưới 10)
        FULL,       // Đã đủ 10 người
        BAO_TRI     // Đang sửa chữa
    }

    // Tình trạng cơ sở vật chất
    public enum VatChat {
        TOT,
        HU_HONG
    }
}
