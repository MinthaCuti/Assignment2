/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilities;

/**
 *
 * @author Admin
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

public class JdbcHelper {

    
    public static Connection getConnection() {
        File checkFile = new File("db_config.txt");
        System.out.println(">>> NetBeans đang tìm file tại: " + checkFile.getAbsolutePath());
        
        List<String> configs = new ArrayList<>();

        // 1. Đọc file cấu hình db_config.txt
        try (BufferedReader br = new BufferedReader(new FileReader("db_config.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) { // Chỉ lấy dòng có chữ
                    configs.add(line.trim());
                }
            }
        } catch (Exception e) {
            System.err.println(">>> Lỗi: Không tìm thấy hoặc không đọc được file db_config.txt!");
            return null;
        }

        // 2. Kiểm tra xem có đủ ít nhất 5 thông số không
        if (configs.size() < 5) {
            System.err.println(">>> Lỗi: File cấu hình db_config.txt thiếu thông số (Cần 5 dòng)!");
            return null;
        }

        try {
            // 3. Gán giá trị từ List
            String host = configs.get(0);
            String port = configs.get(1);
            String dbName = configs.get(2);
            String user = configs.get(3);
            String pass = configs.get(4);

            // 4. Lắp ráp URL
            String url = String.format("jdbc:sqlserver://%s:%s;databaseName=%s;encrypt=false;trustServerCertificate=true",
                    host, port, dbName);

            // 5. Đăng ký Driver và Kết nối
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(url, user, pass);

        } catch (Exception e) {
            System.err.println(">>> Lỗi kết nối SQL (Kiểm tra lại User/Pass hoặc Server): " + e.getMessage());
            return null;
        }
    }
}
