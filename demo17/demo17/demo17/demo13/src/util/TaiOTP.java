/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;

public class TaiOTP
{
    public static String taoMatKhau()
    {
        SecureRandom rand = new SecureRandom();
        int num = rand.nextInt(1000000) + 123456;
        return String.format("%06d", num);
    }

    public static void writeFile(String inputEmail, String OTP)
    {
        File file = new File("Email/" + inputEmail + "/matkhau.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file)))
        {
            writer.write(OTP);
            System.out.println("Đã tạo OTP: " + OTP);
            System.out.println("Đã lưu OTP tại: " + file.getAbsolutePath());
        }
        catch (IOException e)
        {
            System.out.println("Lỗi ghi file OTP: " + e.getMessage());
        }
    }
}
