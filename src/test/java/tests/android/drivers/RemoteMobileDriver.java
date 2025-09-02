package tests.android.drivers;

import com.codeborne.selenide.WebDriverProvider;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import tests.android.config.ConfigReader;

import java.net.URL;

import static io.appium.java_client.remote.AutomationName.ANDROID_UIAUTOMATOR2;
import static io.appium.java_client.remote.MobilePlatform.ANDROID;

public class RemoteMobileDriver implements WebDriverProvider {

    @NotNull
    @Override
    public WebDriver createDriver(@NotNull Capabilities capabilities) {

        UiAutomator2Options options = new UiAutomator2Options();

        // Основные capabilities для приложения
        options.setAutomationName(ANDROID_UIAUTOMATOR2)
                .setPlatformName(ANDROID)
                .setDeviceName(ConfigReader.get("device"))
                .setPlatformVersion(ConfigReader.get("os_version"))
                .setApp(ConfigReader.get("app"))
                .setAutoGrantPermissions(true)
                .setCapability("appium:disableSuppressAccessibilityService", false);

        // BrowserStack capabilities - используем MutableCapabilities чтобы избежать appium: префиксов
        MutableCapabilities bstackOptions = new MutableCapabilities();
        bstackOptions.setCapability("userName", ConfigReader.get("browserstack.user"));
        bstackOptions.setCapability("accessKey", ConfigReader.get("browserstack.key"));
        bstackOptions.setCapability("projectName", "Invitro Project");
        bstackOptions.setCapability("buildName", "Invitro | BuildDateAndTime: " + getCurrentDateTime());
        bstackOptions.setCapability("buildTag", "automated_tests");
        bstackOptions.setCapability("sessionName", ConfigReader.get("device") + "_" + ConfigReader.get("os_version") + "_Invitro_Test");

        options.setCapability("bstack:options", bstackOptions);

        try {
            String userName = ConfigReader.get("browserstack.user");
            String accessKey = ConfigReader.get("browserstack.key");
            URL hubUrl = new URL(String.format("https://%s:%s@hub.browserstack.com/wd/hub", userName, accessKey));
            return new AndroidDriver(hubUrl, options);

        } catch (Exception e) {
            System.err.println("Error creating AndroidDriver: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to create AndroidDriver: " + e.getMessage(), e);
        }
    }

    private String getCurrentDateTime() {
        return java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy_HH:mm:ss"));
    }
}
