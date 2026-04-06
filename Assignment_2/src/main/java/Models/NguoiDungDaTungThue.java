/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author Admin
 */
import java.util.Date;

public class NguoiDungDaTungThue extends NguoiDung {

    private int soLanThue;
    private Date ngayRoiDi;
    private String ghiChu;

    public NguoiDungDaTungThue() {
        super();
    }

    // Constructor này dùng khi chuyển từ NguoiDung sang LichSu
    public NguoiDungDaTungThue(NguoiDung nd, int soLanThue) {
        super(nd.getMaND(), nd.getHoTen(), "", "", nd.getMaPhong());
        this.soLanThue = soLanThue;
        this.ngayRoiDi = new Date(); // Mặc định là ngày hôm nay
    }

    public int getSoLanThue() {
        return soLanThue;
    }

    public void setSoLanThue(int soLanThue) {
        this.soLanThue = soLanThue;
    }

    public void tangSoLanThue() {
        this.soLanThue++;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | So lan thue: %d | Ngay di: %s",
                soLanThue, ngayRoiDi.toString());
    }
}
