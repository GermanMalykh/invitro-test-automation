package helpers;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.RemoteWebDriver;
import tests.android.config.ConfigReader;
import tests.android.helpers.BrowserstackGetter;
import io.qameta.allure.Allure;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static org.openqa.selenium.logging.LogType.BROWSER;

public class Attach {

    static String env = ConfigReader.get("env");

    @Attachment(value = "{attachName}", type = "image/png")
    public static byte[] screenshotAs(String attachName) {
        return ((TakesScreenshot) getWebDriver()).getScreenshotAs(OutputType.BYTES);
    }

    @Attachment(value = "Page source", type = "text/plain")
    public static byte[] pageSource() {
        return getWebDriver().getPageSource().getBytes(StandardCharsets.UTF_8);
    }

    @Attachment(value = "{attachName}", type = "text/plain")
    public static String attachAsText(String attachName, String message) {
        return message;
    }

    public static void browserConsoleLogs() {
        attachAsText(
                "Browser console logs",
                String.join("\n", Selenide.getWebDriverLogs(BROWSER))
        );
    }

    @Attachment(value = "Video", type = "text/html", fileExtension = ".html")
    public static String addVideo() {
        return "<html><body><video width='100%' height='100%' controls autoplay><source src='"
                + getVideoUrl()
                + "' type='video/mp4'></video></body></html>";
    }

    @Attachment(value = "BrowserStack Video", type = "text/html", fileExtension = ".html")
    private static String getVideoBrowserstack(String sessionId) {
        return "<html><body><video width='100%' height='100%' controls autoplay><source src='"
                + BrowserstackGetter.videoUrl(sessionId)
                + "' type='video/mp4'></video></body></html>";
    }

    /**
     * Получает все вложения BrowserStack для сессии (видео + отчет)
     */
    public static void getBrowserstackAttachments(String sessionId) {
        // Получаем видео
        getVideoBrowserstack(sessionId);

        // Генерируем HTML отчет
        generateBrowserstackReport(sessionId);
    }

    public static URL getVideoUrl() {
        String videoUrl = "https://selenoid.autotests.cloud/video/" + getSessionId() + ".mp4";
        try {
            return new URL(videoUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getSessionId() {
        return ((RemoteWebDriver) getWebDriver()).getSessionId().toString();
    }

    @Attachment(value = "BrowserStack Session Report", type = "text/html")
    private static String generateBrowserstackReport(String sessionId) {
        try {
            // Получаем данные сессии через API
            var sessionInfo = BrowserstackGetter.getSessionInfo(sessionId);
            String sessionData = sessionInfo.response().asString();
            
            // Генерируем HTML отчет
            String htmlReport = BrowserstackGetter.generateHtmlReport(sessionData);
            
            // Добавляем информацию в Allure
            Allure.description("BrowserStack Session ID: " + sessionId);
            
            return htmlReport;
            
        } catch (Exception e) {
            System.err.println("Не удалось сгенерировать отчет BrowserStack: " + e.getMessage());
            e.printStackTrace();
            return "<html><body><h1>Ошибка генерации отчета BrowserStack</h1><p>" + e.getMessage() + "</p></body></html>";
        }
    }


}