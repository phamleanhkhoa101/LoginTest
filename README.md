# UTC Login Automation

Project tự động kiểm thử trang đăng nhập Văn phòng điện tử UTC bằng Selenium WebDriver, Java và JUnit 5.

## Công nghệ

- Java 17 trở lên
- Selenium WebDriver 4.50.0
- JUnit Jupiter 5.13.4
- Maven
- Google Chrome và Selenium Manager để tự quản lý ChromeDriver

## Cấu trúc chính

```text
src/test/java/com/utc/testing/
├── base/BaseTest.java       # Khởi tạo/đóng Chrome và đọc biến môi trường
├── pages/LoginPage.java     # Locator và thao tác của trang Login
└── tests/LoginTest.java     # Các test case JUnit

src/test/resources/config.properties
```

## Cấu hình

URL kiểm thử và chế độ chạy Chrome nằm tại `src/test/resources/config.properties`:

```properties
base.url=https://vanphongdientu.utc.edu.vn/Login
headless=false
```

Đặt `headless=true` nếu không muốn Chrome hiển thị khi chạy test.

### Tài khoản kiểm thử

Các test TC4, TC5 và TC11 cần tài khoản kiểm thử hợp lệ. Không ghi username hoặc password vào source code. Thiết lập hai biến môi trường cục bộ:

```text
UTC_USERNAME
UTC_PASSWORD
```

Khi chạy bằng VS Code Test Runner, khai báo biến cho cấu hình test trong `.vscode/settings.json`. Thư mục `.vscode` đã được bỏ qua bởi Git.

```json
{
  "java.test.config": [
    {
      "name": "UTC login tests",
      "testKind": "junit",
      "env": {
        "UTC_USERNAME": "username_kiem_thu_hop_le",
        "UTC_PASSWORD": "mat_khau_kiem_thu_hop_le"
      }
    }
  ],
  "java.test.defaultConfig": "UTC login tests"
}
```

Sau khi thay đổi cấu hình, chạy lệnh `Developer: Reload Window` trong VS Code. Không commit `settings.json` chứa thông tin thật và không chia sẻ password trong log, ảnh chụp hoặc chat.

Nếu hai biến chưa được cấu hình, các test cần thông tin hợp lệ sẽ hiện **Skipped**, không phải **Failed**.

## Test case đã có

| ID | Nội dung |
| --- | --- |
| TC1 | Để trống username và password |
| TC2 | Để trống username |
| TC3 | Để trống password |
| TC4 | Username đúng, password sai |
| TC5 | Username sai, password đúng |
| TC6 | Username và password đều sai |
| TC8 | Chọn checkbox Giữ tôi luôn đăng nhập |
| TC9 | Bỏ chọn checkbox Giữ tôi luôn đăng nhập |
| TC10 | Xác minh password được che khi nhập |
| TC11 | Gửi form đăng nhập bằng phím Enter |
| TC12 | Mở quy trình Quên mật khẩu |
| TC13 | Điều hướng đến đăng nhập bằng e-mail UTC |
| TC14 | Username chỉ gồm dấu cách |

Các kịch bản được đối chiếu với file test case Excel trong project.

## Chạy test

Chạy toàn bộ lớp test:

```powershell
mvn test -Dtest=LoginTest
```

Chạy một test theo tên method:

```powershell
mvn test "-Dtest=LoginTest#shouldRejectLoginWhenUsernameIsEmpty"
```

Trong VS Code, có thể nhấn biểu tượng chạy cạnh `@Test` hoặc mở tab **Test Results** để chạy từng test.

## Kết quả và xử lý lỗi

- Dấu tích xanh: mọi assertion trong test đã Pass.
- Dấu đỏ: assertion hoặc thao tác Selenium thất bại. Mở phần Test Output để xem thông báo `Phản hồi thực tế`.
- Biểu tượng Skip: test cần `UTC_USERNAME` hoặc `UTC_PASSWORD` nhưng biến chưa được Test Runner nhận.

Chrome được đóng trong `@AfterEach` bằng `driver.quit()`, kể cả khi test Fail. Khi cần quan sát lỗi, chạy Debug và đặt breakpoint trước assertion hoặc tại `tearDown()`.

## Lưu ý an toàn

- Chỉ sử dụng tài khoản kiểm thử được cấp quyền.
- Không tự động thực hiện các payload SQL Injection trên website production.
- TC13 chỉ xác minh việc điều hướng đến Google Accounts; test không tự động đăng nhập vào Google.
