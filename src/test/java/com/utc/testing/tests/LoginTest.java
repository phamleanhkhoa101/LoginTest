package com.utc.testing.tests;

import com.utc.testing.base.BaseTest;
import com.utc.testing.pages.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoginTest extends BaseTest {

    private static final String SAMPLE_PASSWORD = "123456";

    @Test
    @DisplayName("TC1 - Không đăng nhập khi để trống tên đăng nhập và mật khẩu")
    void shouldRejectLoginWhenUsernameAndPasswordAreEmpty() {
        LoginPage loginPage = new LoginPage(driver, wait);
        loginPage.open(baseUrl);

        String feedback = loginPage.submitEmptyCredentialsAndGetFeedback();

        assertTrue(loginPage.isStillOnLoginPage(),
                "Hệ thống không được xác thực khi username và password đều trống.");
        assertFalse(feedback.isBlank(),
                "Không tìm thấy thông báo sau khi gửi form với hai trường trống.");
        assertTrue(loginPage.isRequiredCredentialsMessage(feedback),
                () -> "Thông báo chưa yêu cầu nhập tài khoản/mật khẩu. Phản hồi thực tế: " + feedback);
    }

    @Test
    @DisplayName("TC2 - Không đăng nhập khi để trống tên đăng nhập")
    void shouldRejectLoginWhenUsernameIsEmpty() {
        LoginPage loginPage = new LoginPage(driver, wait);
        loginPage.open(baseUrl);

        String feedback = loginPage.submitWithBlankUsernameAndGetFeedback(SAMPLE_PASSWORD);

        assertTrue(loginPage.isStillOnLoginPage(),
                "Hệ thống không được xác thực khi username trống.");
        assertFalse(feedback.isBlank(),
                "Không tìm thấy thông báo sau khi gửi form với username trống.");
        assertTrue(loginPage.isMissingUsernameMessage(feedback),
                () -> "Thông báo chưa yêu cầu nhập tên đăng nhập. Phản hồi thực tế: " + feedback);
    }
}
