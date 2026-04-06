/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Services;

/**
 *
 * @author Admin
 */
import java.io.*;
import java.util.List;

public class FileService {
    
    // Lưu bất kỳ danh sách nào ra file text
    public <T> void saveToFile(String path, List<T> data) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            for (T item : data) {
                bw.write(item.toString());
                bw.newLine();
            }
            System.out.println(">> Luu file thanh cong tai: " + path);
        } catch (IOException e) {
            System.out.println(">> Loi luu file: " + e.getMessage());
        }
    }
}