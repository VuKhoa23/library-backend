package com.library.Library.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class TestSelenium {

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        // Set up WebDriverManager
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @AfterEach
    public void tearDown() {
        // Close browser
        driver.quit();
    }

    public void loginTest() throws InterruptedException {
        Thread.sleep(1000);

        // Open the library application
        driver.get("http://localhost:3000/"); // Replace with your app's URL

        // Find and click the login button
        WebElement loginButton = driver.findElement(By.cssSelector("button[data-modal-target='login-modal'][data-modal-toggle='login-modal']"));
        loginButton.click();
        Thread.sleep(1000);

        // Enter username
        WebElement usernameField = driver.findElement(By.cssSelector("input[type='username']"));
        usernameField.sendKeys("user");
        Thread.sleep(1000);

        // Enter password
        WebElement passwordField = driver.findElement(By.cssSelector("input[type='password']"));
        passwordField.sendKeys("user");
        Thread.sleep(1000);

        // Click login button
        WebElement submitButton = driver.findElement(By.cssSelector("button.w-full.bg-blue-600.text-white.px-3.py-3.text-lg.transition.duration-100.rounded-lg.mt-10.hover\\:bg-blue-800.font-thin"));
        submitButton.click();
        Thread.sleep(1000);

        // XPath targeting the button based on its class
        WebElement welcomeButton = driver.findElement(By.xpath("//button[contains(@class, 'bg-indigo-600') and contains(@class, 'hover:bg-blue-700') and contains(., 'Welcome!')]"));
        String buttonText = welcomeButton.getText();
        assertTrue(buttonText.contains("Welcome!"));
    }
}