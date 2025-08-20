package tests.web.pages;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.Selenide.sleep;

public class BannersPage {

    // TODO: Улучшить метод закрытия баннера
    // - Добавить обработку разных типов баннеров (не только popmechanic)
    // - Добавить retry логику для нестабильных элементов
    // - Добавить логирование для отладки
    public void closePromoBanner() {
        try {
            sleep(500);
            if ($("[id*='popmechanic-container']").isDisplayed()) {
                $("[data-popmechanic-close]").shouldBe(visible, Duration.ofSeconds(10));
                executeJavaScript("arguments[0].scrollIntoView({block: 'center'});", $("[data-popmechanic-close]"));
                $("[data-popmechanic-close]").click();
                $("[id*='popmechanic-container']").shouldNotBe(visible, Duration.ofSeconds(10));
                sleep(1000);
            }
        } catch (Exception e) {
            try {
                executeJavaScript("document.querySelectorAll('[id*=\"popmechanic-container\"]').forEach(e => e.remove());");
            } catch (Exception jsEx) {
                // Игнорируем ошибки при принудительном удалении
            }
        }
    }

    public BannersPage closeCookieBanner() {
        executeJavaScript("document.querySelectorAll('.cookie--popup, .attention--page, .attention--fixed').forEach(e => e.remove());");
        return this;
    }
}
