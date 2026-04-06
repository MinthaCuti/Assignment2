/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilities;

/**
 *
 * @author Admin
 */
import Models.NguoiDung;
import Models.NhaTro;
import java.sql.*;

public class DataRepository {

    // 1. Hàm dọn sạch DB cho phiên Demo
    public void clearAllDataDemo() {
        try (Connection conn = JdbcHelper.getConnection(); Statement st = conn.createStatement()) {
            st.executeUpdate("DELETE FROM NguoiDung");
            st.executeUpdate("DELETE FROM LichSuThue");
            st.executeUpdate("UPDATE NhaTro SET TrangThai = 0, TinhTrangVatChat = 0");
            System.out.println(">>> [SQL] Database đã được làm sạch cho phiên Demo.");
        } catch (Exception e) {
            System.err.println(">>>Lỗi Clear DB: " + e.getMessage());
        }
    }

    // 2. Hàm Insert Người dùng/Nhóm người (Dùng chung)
    public void insertNguoiDungSql(NguoiDung nd, int soLuong) throws Exception {
        // Chúng ta lưu số lượng vào cột Email để không cần sửa Table SQL
        String sql = "INSERT INTO NguoiDung (MaND, HoTen, SDT, Email, MaPhong) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = JdbcHelper.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, nd.getMaND());
            ps.setString(2, nd.getHoTen());
            ps.setString(3, "N/A"); 
            ps.setString(4, String.valueOf(soLuong)); // Lưu số lượng người vào đây
            ps.setString(5, nd.getMaPhong());
            
            ps.executeUpdate();
            
            // Cập nhật luôn trạng thái phòng lên "Đã thuê" (TrangThai = 1)
            updateTrangThaiPhongSql(nd.getMaPhong(), 1);
            
        } catch (SQLException e) {
            if (e.getErrorCode() == 2627) {
                throw new Exception("Mã [" + nd.getMaND() + "] đã tồn tại trên hệ thống!");
            }
            throw new Exception("Lỗi SQL: " + e.getMessage());
        }
    }

    // 3. Hàm xóa và đẩy sang Lịch sử
    public void deleteAndMoveToHistorySql(NguoiDung nd) {
        String sqlInsertLS = "INSERT INTO LichSuThue (MaND, HoTen, MaPhong, SoLanThue) VALUES (?, ?, ?, 1)";
        String sqlDelete = "DELETE FROM NguoiDung WHERE MaND = ?";
        
        try (Connection conn = JdbcHelper.getConnection()) {
            // Lưu lịch sử
            PreparedStatement psLS = conn.prepareStatement(sqlInsertLS);
            psLS.setString(1, nd.getMaND());
            psLS.setString(2, nd.getHoTen());
            psLS.setString(3, nd.getMaPhong());
            psLS.executeUpdate();
            
            // Xóa bảng hiện tại
            PreparedStatement psDel = conn.prepareStatement(sqlDelete);
            psDel.setString(1, nd.getMaND());
            psDel.executeUpdate();
            
            System.out.println(">>> [SQL] Đã di dời dữ liệu nhóm/khách sang Lịch sử.");
        } catch (Exception e) {
            System.err.println(">>> Lỗi SQL di dời: " + e.getMessage());
        }
    }

    public void updateTrangThaiPhongSql(String maPhong, int trangThai) {
        String sql = "UPDATE NhaTro SET TrangThai = ? WHERE MaPhong = ?";
        try (Connection conn = JdbcHelper.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, trangThai);
            ps.setString(2, maPhong);
            ps.executeUpdate();
        } catch (Exception e) {}
    }
    
    public void updateTinhTrangVatChatSql(String maPhong, int tinhTrang) {
        String sql = "UPDATE NhaTro SET TinhTrangVatChat = ? WHERE MaPhong = ?";
        try (Connection conn = JdbcHelper.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tinhTrang);
            ps.setString(2, maPhong);
            ps.executeUpdate();
        } catch (Exception e) {}
    }
    public void insertDanhGiaSql(String maKH, int soSao, String noiDung) {
    String sql = "INSERT INTO DanhGia (MaND, SoSao, NoiDung) VALUES (?, ?, ?)";
    try (Connection conn = JdbcHelper.getConnection(); 
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, maKH);
        ps.setInt(2, soSao);
        ps.setString(3, noiDung);
        ps.executeUpdate();
    } catch (Exception e) {
        System.err.println(">>>Lỗi SQL DanhGia: " + e.getMessage());
    }
}
}