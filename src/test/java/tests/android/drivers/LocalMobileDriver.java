package tests.android.drivers;

import com.codeborne.selenide.WebDriverProvider;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import tests.android.config.ConfigReader;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static io.appium.java_client.remote.AutomationName.ANDROID_UIAUTOMATOR2;
import static io.appium.java_client.remote.MobilePlatform.ANDROID;

public class LocalMobileDriver implements WebDriverProvider {

    public static URL getAppiumServerUrl() {
        try {
            return new URI(ConfigReader.get("appiumUrl")).toURL();
        } catch (URISyntaxException | MalformedURLException e) {
            throw new RuntimeException("Invalid Appium URL: " + ConfigReader.get("appiumUrl"), e);
        }
    }

    public String getAppPath() {
        String appPath = "src/test/resources/apps/com.invitro.apk";
        File app = new File(appPath);
        return app.getAbsolutePath();
    }

    @NotNull
    @Override
    public WebDriver createDriver(@NotNull Capabilities capabilities) {
        UiAutomator2Options options = new UiAutomator2Options();
        options.merge(capabilities);

        options.setAutomationName(ANDROID_UIAUTOMATOR2)
                .setPlatformName(ANDROID)
                .setDeviceName(ConfigReader.get("device"))
                .setPlatformVersion(ConfigReader.get("os_version"))
                .setApp(getAppPath())
                .setAppPackage(ConfigReader.get("appPackage"))
                .setAppActivity(ConfigReader.get("appActivity"));
        return new AndroidDriver(getAppiumServerUrl(), options);
    }

}
