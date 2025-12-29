package com.tta.backend_btl_mobile_app.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utility class để generate BCrypt password hash
 * Chạy class này để tạo mật khẩu mã hóa cho database
 */
public class PasswordHashGenerator {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        System.out.println("=== PASSWORD HASH GENERATOR ===");
        System.out.println();

        // Admin password: Admin123!
        String adminPassword = "Admin123!";
        String adminHash = encoder.encode(adminPassword);
        System.out.println("Admin:");
        System.out.println("  Password: " + adminPassword);
        System.out.println("  Hash: " + adminHash);
        System.out.println();

        // User 1: TuanAnh@123
        String user1Password = "TuanAnh@123";
        String user1Hash = encoder.encode(user1Password);
        System.out.println("TuanAnh:");
        System.out.println("  Password: " + user1Password);
        System.out.println("  Hash: " + user1Hash);
        System.out.println();

        // User 2: NgocBao@123
        String user2Password = "NgocBao@123";
        String user2Hash = encoder.encode(user2Password);
        System.out.println("NgocBao:");
        System.out.println("  Password: " + user2Password);
        System.out.println("  Hash: " + user2Hash);
        System.out.println();

        // User 3: MinhDuc@123
        String user3Password = "MinhDuc@123";
        String user3Hash = encoder.encode(user3Password);
        System.out.println("MinhDuc:");
        System.out.println("  Password: " + user3Password);
        System.out.println("  Hash: " + user3Hash);
        System.out.println();

        System.out.println("=== COPY VÀ DÁN VÀO FILE SQL ===");
        System.out.println();
        System.out.println("UPDATE users SET password_hash = '" + adminHash + "' WHERE username = 'admin';");
        System.out.println("UPDATE users SET password_hash = '" + user1Hash + "' WHERE username = 'tuananh';");
        System.out.println("UPDATE users SET password_hash = '" + user2Hash + "' WHERE username = 'ngocbao';");
        System.out.println("UPDATE users SET password_hash = '" + user3Hash + "' WHERE username = 'minhduc';");
    }
}

