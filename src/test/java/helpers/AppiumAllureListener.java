package helpers;

import io.qameta.allure.selenide.AllureSelenide;
import io.qameta.allure.selenide.LogType;
import java.util.logging.Level;

/**
 * AllureSelenide listener для Android/Appium тестов
 * Отключает автоматическое добавление HTML source page, 
 * так как для Android тестов это XML-дерево Appium, а не HTML
 */
public class AppiumAllureListener extends AllureSelenide {
    
    public AppiumAllureListener() {
        super();
        // Отключаем автоматическое добавление page source
        // так как для Android тестов это XML, а не HTML
        this.savePageSource(false);
        
        // Включаем скриншоты при падении
        this.screenshots(true);
        
        // Настраиваем логи браузера (если доступны)
        this.enableLogs(LogType.BROWSER, Level.ALL);
    }
    
    /**
     * Создает listener для Android/Appium тестов
     */
    public static AppiumAllureListener forAppium() {
        return new AppiumAllureListener();
    }

}