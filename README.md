# LoginTest
## UTC login automation

Hiện project chỉ có **TC1** theo dòng 5 của file `20_testcase_dang_nhap_UTC.xlsx`:
để trống username và password, nhấn Đăng nhập, sau đó xác nhận hệ thống vẫn ở form
đăng nhập và có thông báo yêu cầu nhập thông tin. Test đọc cả validation native của
trình duyệt, JavaScript alert và phần nội dung mới xuất hiện trên trang nên không cần
đoán CSS selector của vùng thông báo lỗi.

Chạy test sau khi đã cài Maven và Google Chrome:

```powershell
mvn test -Dtest=LoginTest
```

Selenium Manager sẽ tự lấy ChromeDriver. Đặt `headless=true` trong
`src/test/resources/config.properties` nếu muốn chạy không mở cửa sổ Chrome.
