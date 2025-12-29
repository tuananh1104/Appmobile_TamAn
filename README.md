# ?? TAM AN APP - BACKEND API
> ?ng d?ng theo dõi c?m xúc và s?c kh?e tinh th?n
## ?? Gi?i thi?u
**Tam An App** là m?t ?ng d?ng backend REST API du?c xây d?ng b?ng Spring Boot, giúp ngu?i dùng theo dõi c?m xúc hàng ngày, d?t m?c tiêu c?i thi?n s?c kh?e tinh th?n, và nh?n các l?i khuyên h?u ích.
### Tính nang chính:
- ? **Authentication**: Ðang ký, dang nh?p v?i JWT
- ? **User Management**: Qu?n lý thông tin cá nhân, d?i m?t kh?u
- ? **Checkins**: Ghi l?i c?m xúc, ho?t d?ng, d?a di?m, ngu?i d?ng hành
- ? **Goals**: Ð?t và theo dõi m?c tiêu c?i thi?n tinh th?n
- ? **Tips**: Xem l?i khuyên theo ch? d? (cang th?ng, lo âu, h?nh phúc)
- ? **Statistics**: Th?ng kê c?m xúc, ho?t d?ng theo th?i gian
- ? **Admin Panel**: Qu?n lý users, tips, xem th?ng kê h? th?ng
## ??? Công ngh? s? d?ng
- **Java 17**
- **Spring Boot 3.4.1**
  - Spring Web (REST API)
  - Spring Data JPA (Database)
  - Spring Security (Authentication & Authorization)
- **MySQL 8.0** (Database)
- **JWT** (JSON Web Token)
- **Lombok** (Reduce boilerplate code)
- **Maven** (Build tool)
## ?? C?u trúc d? án
```
src/main/java/com/tta/backend_btl_mobile_app/
+-- config/              # C?u hình (Security, App)
+-- controller/          # REST Controllers
¦   +-- AuthController
¦   +-- UserController
¦   +-- CheckinController
¦   +-- GoalController
¦   +-- TipController
¦   +-- AdminController
+-- dto/                 # Data Transfer Objects
¦   +-- request/         # Request DTOs
¦   +-- response/        # Response DTOs
+-- entity/              # JPA Entities (User, Checkin, Goal, Tip)
+-- repository/          # JPA Repositories
+-- service/             # Business Logic
+-- security/            # JWT, Auth logic
+-- exception/           # Exception handlers
```
## ?? Hu?ng d?n cài d?t
### 1. Yêu c?u h? th?ng
- Java 17 ho?c m?i hon
- MySQL 8.0 tr? lên
- Maven 3.6+
### 2. Clone project
```bash
git clone <repository-url>
cd backend_btl_mobile_app
```
### 3. C?u hình Database
T?o database và import schema:
```bash
mysql -u root -p
CREATE DATABASE tam_an_app;
USE tam_an_app;
SOURCE tam_an_app.sql;
```
### 4. C?u hình application.properties
Ch?nh s?a `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/tam_an_app
spring.datasource.username=root
spring.datasource.password=your_password
server.port=8080
```
### 5. Build và ch?y
```bash
# Build project
mvn clean install
# Run application
mvn spring-boot:run
```
Ho?c ch?y t? IDE: Run `BackendBtlMobileAppApplication.java`
?ng d?ng s? ch?y t?i: **http://localhost:8080**
## ?? API Documentation
Xem hu?ng d?n chi ti?t v? cách test API trong file:
### ?? [API_TESTING_GUIDE.md](API_TESTING_GUIDE.md)
### API Endpoints tóm t?t:
#### Authentication
- `POST /api/auth/register` - Ðang ký
- `POST /api/auth/login` - Ðang nh?p
#### User
- `GET /api/user/profile` - Xem profile
- `PUT /api/user/profile` - C?p nh?t profile
- `PUT /api/user/change-password` - Ð?i m?t kh?u
- `GET /api/user/stats/*` - Xem th?ng kê
#### Checkins
- `POST /api/checkins` - T?o checkin
- `GET /api/checkins` - Danh sách checkins
- `PUT /api/checkins/{id}` - C?p nh?t
- `DELETE /api/checkins/{id}` - Xóa
- `GET /api/checkins/filter/*` - L?c theo di?u ki?n
#### Goals
- `POST /api/goals` - T?o m?c tiêu
- `GET /api/goals` - Danh sách m?c tiêu
- `PUT /api/goals/{id}` - C?p nh?t
- `DELETE /api/goals/{id}` - Xóa
- `PUT /api/goals/{id}/status` - C?p nh?t tr?ng thái
#### Tips
- `GET /api/tips` - Danh sách tips
- `GET /api/tips/category/{category}` - Tips theo ch? d?
- `GET /api/tips/random` - Tip ng?u nhiên
#### Admin
- `GET /api/admin/users` - Qu?n lý users
- `POST /api/admin/tips` - T?o/s?a tips
- `GET /api/admin/stats/system` - Th?ng kê h? th?ng
## ?? Test API v?i Postman
1. Import file `Tam_An_API.postman_collection.json` vào Postman
2. Ðang ký/Ðang nh?p d? l?y JWT token
3. Thêm token vào Header: `Authorization: Bearer {token}`
4. Test các API endpoints
**Xem chi ti?t:** [API_TESTING_GUIDE.md](API_TESTING_GUIDE.md)
## ??? Database Schema
### 4 B?ng chính:
1. **users** - Ngu?i dùng (admin & user)
2. **tips** - L?i khuyên s?c kh?e tinh th?n
3. **checkins** - Nh?t ký c?m xúc hàng ngày
4. **goals** - M?c tiêu cá nhân
Xem chi ti?t schema trong file: `tam_an_app.sql`
## ?? Tài kho?n m?c d?nh
### Admin Account
- Username: `admin`
- Password: `Admin123!`
### Test User Account
- Username: `testuser`
- Password: `Password123!`
## ?? Security
- S? d?ng JWT (JSON Web Token) cho authentication
- Password du?c mã hóa b?ng BCrypt
- Token h?t h?n sau 24 gi?
- CORS du?c c?u hình cho môi tru?ng development
## ?? Validation Rules
- **Username**: 3-50 ký t?, ch? ch? cái, s?, g?ch du?i
- **Password**: T?i thi?u 8 ký t?, có ch? hoa, ch? thu?ng, s?, ký t? d?c bi?t
- **Display Name**: 2-100 ký t?
## ?? Troubleshooting
### L?i k?t n?i database
Ki?m tra MySQL dang ch?y và thông tin k?t n?i trong `application.properties`
### L?i Port dã du?c s? d?ng
Ð?i port trong `application.properties`: `server.port=8081`
### L?i 401 Unauthorized
Token h?t h?n ho?c không h?p l?, dang nh?p l?i d? l?y token m?i
## ?? Support
N?u g?p v?n d?, vui lòng ki?m tra:
1. Application logs
2. MySQL logs
3. Postman console
## ?? License
This project is for educational purposes.
---
**Chúc b?n s? d?ng thành công! ??**
