package com.utc.testing.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/** Locators are taken from the login form HTML supplied for the UTC site. */
public class LoginPage {
    private static final By USERNAME = By.name("username");
    private static final By PASSWORD = By.name("userpwd");
    private static final By LOGIN_BUTTON = By.cssSelector("input.submit_login[type='submit']");
    private static final By REMEMBER_ME = By.id("persistent");

    private final WebDriver driver;
    private final WebDriverWait wait;

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void open(String url) {
        driver.get(url);
        wait.until(ExpectedConditions.visibilityOfElementLocated(USERNAME));
        wait.until(ExpectedConditions.visibilityOfElementLocated(PASSWORD));
        wait.until(ExpectedConditions.elementToBeClickable(LOGIN_BUTTON));
    }

    public String submitEmptyCredentialsAndGetFeedback() {
        return submitAndGetFeedback();
    }

    public String submitWithBlankUsernameAndGetFeedback(String password) {
        driver.findElement(PASSWORD).sendKeys(password);
        return submitAndGetFeedback();
    }

    public void enterPassword(String password) {
        driver.findElement(PASSWORD).sendKeys(password);
    }

    public String submitWithBlankPasswordAndGetFeedback(String username) {
        driver.findElement(USERNAME).sendKeys(username);
        return submitAndGetFeedback();
    }

    public String submitCredentialsAndGetFeedback(String username, String password) {
        driver.findElement(USERNAME).sendKeys(username);
        driver.findElement(PASSWORD).sendKeys(password);
        return submitAndGetFeedback();
    }

    private String submitAndGetFeedback() {
        String textBeforeSubmit = driver.findElement(By.tagName("body")).getText();
        driver.findElement(LOGIN_BUTTON).click();

        return wait.until(currentDriver -> {
            String feedback = readValidationFeedback(textBeforeSubmit);
            return feedback.isBlank() ? null : feedback;
        });
    }

    public boolean isStillOnLoginPage() {
        return driver.findElements(USERNAME).size() == 1
                && driver.findElements(PASSWORD).size() == 1;
    }

    public boolean isPasswordMasked() {
        return "password".equalsIgnoreCase(driver.findElement(PASSWORD).getAttribute("type"));
    }

    public boolean isRememberMeSelected() {
        return driver.findElement(REMEMBER_ME).isSelected();
    }

    public void selectRememberMe() {
        WebElement checkbox = wait.until(ExpectedConditions.presenceOfElementLocated(REMEMBER_ME));
        if (!checkbox.isSelected()) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);
        }
    }

    public void deselectRememberMe() {
        WebElement checkbox = wait.until(ExpectedConditions.presenceOfElementLocated(REMEMBER_ME));
        if (checkbox.isSelected()) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);
        }
    }

    public boolean isRequiredCredentialsMessage(String feedback) {
        String normalized = feedback.toLowerCase(Locale.ROOT);
        boolean asksForInput = normalized.contains("yêu cầu")
                || normalized.contains("vui lòng")
                || normalized.contains("bắt buộc")
                || normalized.contains("chưa nhập")
                || normalized.contains("please fill")
                || normalized.contains("required");
        boolean mentionsCredentials = normalized.contains("tài khoản")
                || normalized.contains("đăng nhập")
                || normalized.contains("username")
                || normalized.contains("mật khẩu")
                || normalized.contains("password")
                || normalized.contains("this field");
        return asksForInput && mentionsCredentials;
    }

    public boolean isMissingUsernameMessage(String feedback) {
        String normalized = feedback.toLowerCase(Locale.ROOT);
        boolean asksForInput = normalized.contains("yêu cầu")
                || normalized.contains("vui lòng")
                || normalized.contains("bắt buộc")
                || normalized.contains("chưa nhập")
                || normalized.contains("please fill")
                || normalized.contains("required");
        boolean mentionsUsername = normalized.contains("tên đăng nhập")
                || normalized.contains("tài khoản")
                || normalized.contains("username")
                || normalized.contains("this field");
        return asksForInput && mentionsUsername;
    }

    public boolean isMissingPasswordMessage(String feedback) {
        String normalized = feedback.toLowerCase(Locale.ROOT);
        boolean asksForInput = normalized.contains("yêu cầu")
                || normalized.contains("vui lòng")
                || normalized.contains("bắt buộc")
                || normalized.contains("chưa nhập")
                || normalized.contains("please fill")
                || normalized.contains("required");
        boolean mentionsPassword = normalized.contains("mật khẩu")
                || normalized.contains("password")
                || normalized.contains("this field");
        return asksForInput && mentionsPassword;
    }

    public boolean isInvalidCredentialsMessage(String feedback) {
        String normalized = feedback.toLowerCase(Locale.ROOT);
        boolean reportsFailure = normalized.contains("không đúng")
                || normalized.contains("không chính xác")
                || normalized.contains("sai")
                || normalized.contains("thất bại")
                || normalized.contains("invalid")
                || normalized.contains("incorrect");
        boolean mentionsCredentials = normalized.contains("tài khoản")
                || normalized.contains("đăng nhập")
                || normalized.contains("username")
                || normalized.contains("mật khẩu")
                || normalized.contains("password");
        return reportsFailure && mentionsCredentials;
    }

    private String readValidationFeedback(String textBeforeSubmit) {
        String alertText = readAlertText();
        if (!alertText.isBlank()) {
            return alertText;
        }

        String browserValidation = (String) ((JavascriptExecutor) driver).executeScript("""
                return [document.querySelector('input[name="username"]'),
                        document.querySelector('input[name="userpwd"]')]
                    .filter(Boolean)
                    .map(element => element.validationMessage || '')
                    .filter(message => message.trim().length > 0)
                    .join('\\n');
                """);
        if (browserValidation != null && !browserValidation.isBlank()) {
            return browserValidation;
        }

        String textAfterSubmit = driver.findElement(By.tagName("body")).getText();
        Set<String> oldLines = normalizedLines(textBeforeSubmit);
        return normalizedLines(textAfterSubmit).stream()
                .filter(line -> !oldLines.contains(line))
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private String readAlertText() {
        try {
            var alert = driver.switchTo().alert();
            String text = alert.getText().trim();
            alert.accept();
            return text;
        } catch (NoAlertPresentException ignored) {
            return "";
        }
    }

    private Set<String> normalizedLines(String text) {
        return Arrays.stream(text.split("\\R"))
                .map(String::trim)
                .filter(line -> !line.isBlank())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
