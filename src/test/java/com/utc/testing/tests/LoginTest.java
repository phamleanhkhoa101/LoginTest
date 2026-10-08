package com.utc.testing.tests;

import com.utc.testing.base.BaseTest;
import com.utc.testing.pages.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class LoginTest extends BaseTest {

    private static final String SAMPLE_USERNAME = "huongnt";
    private static final String SAMPLE_PASSWORD = "123456";
    private static final String WRONG_PASSWORD = "utc@235";
    private static final String INVALID_USERNAME = "huongnguyenvien";
    private static final String INVALID_USERNAME_AND_PASSWORD = "abcxyz";

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

    @Test
    @DisplayName("TC3 - Không đăng nhập khi để trống mật khẩu")
    void shouldRejectLoginWhenPasswordIsEmpty() {
        LoginPage loginPage = new LoginPage(driver, wait);
        loginPage.open(baseUrl);

        String feedback = loginPage.submitWithBlankPasswordAndGetFeedback(SAMPLE_USERNAME);

        assertTrue(loginPage.isStillOnLoginPage(),
                "Hệ thống không được xác thực khi password trống.");
        assertFalse(feedback.isBlank(),
                "Không tìm thấy thông báo sau khi gửi form với password trống.");
        assertTrue(loginPage.isMissingPasswordMessage(feedback),
                () -> "Thông báo chưa yêu cầu nhập mật khẩu. Phản hồi thực tế: " + feedback);
    }

    @Test
    @DisplayName("TC4 - Không đăng nhập khi tên đăng nhập đúng và mật khẩu sai")
    void shouldRejectLoginWhenPasswordIsIncorrect() {
        assumeTrue(validUsername != null && !validUsername.isBlank(),
                "Bỏ qua TC4: chưa cấu hình biến môi trường UTC_USERNAME.");

        LoginPage loginPage = new LoginPage(driver, wait);
        loginPage.open(baseUrl);

        String feedback = loginPage.submitCredentialsAndGetFeedback(validUsername, WRONG_PASSWORD);

        assertTrue(loginPage.isStillOnLoginPage(),
                "Hệ thống không được xác thực khi mật khẩu sai.");
        assertFalse(feedback.isBlank(),
                "Không tìm thấy thông báo sau khi đăng nhập bằng mật khẩu sai.");
        assertTrue(loginPage.isInvalidCredentialsMessage(feedback),
                () -> "Thông báo chưa thể hiện thông tin đăng nhập không đúng. Phản hồi thực tế: " + feedback);
    }

    @Test
    @DisplayName("TC5 - Không đăng nhập khi tên đăng nhập sai và mật khẩu đúng")
    void shouldRejectLoginWhenUsernameIsIncorrect() {
        assumeTrue(validPassword != null && !validPassword.isBlank(),
                "Bỏ qua TC5: chưa cấu hình biến môi trường UTC_PASSWORD.");

        LoginPage loginPage = new LoginPage(driver, wait);
        loginPage.open(baseUrl);

        String feedback = loginPage.submitCredentialsAndGetFeedback(INVALID_USERNAME, validPassword);

        assertTrue(loginPage.isStillOnLoginPage(),
                "Hệ thống không được xác thực khi username sai.");
        assertFalse(feedback.isBlank(),
                "Không tìm thấy thông báo sau khi đăng nhập bằng username sai.");
        assertTrue(loginPage.isInvalidCredentialsMessage(feedback),
                () -> "Thông báo chưa thể hiện thông tin đăng nhập không đúng. Phản hồi thực tế: " + feedback);
    }

    @Test
    @DisplayName("TC6 - Không đăng nhập khi tên đăng nhập và mật khẩu đều sai")
    void shouldRejectLoginWhenUsernameAndPasswordAreIncorrect() {
        LoginPage loginPage = new LoginPage(driver, wait);
        loginPage.open(baseUrl);

        String feedback = loginPage.submitCredentialsAndGetFeedback(
                INVALID_USERNAME_AND_PASSWORD, SAMPLE_PASSWORD);

        assertTrue(loginPage.isStillOnLoginPage(),
                "Hệ thống không được xác thực khi username và password đều sai.");
        assertFalse(feedback.isBlank(),
                "Không tìm thấy thông báo sau khi đăng nhập bằng thông tin sai.");
        assertTrue(loginPage.isInvalidCredentialsMessage(feedback),
                () -> "Thông báo chưa thể hiện thông tin đăng nhập không đúng. Phản hồi thực tế: " + feedback);
    }
}
