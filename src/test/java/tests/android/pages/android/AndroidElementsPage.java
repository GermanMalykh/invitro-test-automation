package tests.android.pages.android;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.Point;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.time.Duration;
import java.util.Arrays;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.sleep;
import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

import static io.appium.java_client.AppiumBy.id;
public class AndroidElementsPage {

    public final String INVITRO_ID = "com.invitro.app:id/";

    private final SelenideElement SEARCH_BAR_ELEMENT = $(id("android:id/search_src_text")),
            PERMISSION_DENY_BUTTON = $(id("com.android.permissioncontroller:id/permission_deny_button")),
            CITIES_LIST = $(id(INVITRO_ID + "letter"));

    @Step("Поиск города: {city}")
    public AndroidElementsPage setSearchText(String city) {
        CITIES_LIST.shouldBe(visible, Duration.ofSeconds(15));
        SEARCH_BAR_ELEMENT
                .shouldBe(Condition.visible,Duration.ofSeconds(2))
                .type(city);
        return this;
    }

    @Step("Отклонение разрешения на геолокацию")
    public AndroidElementsPage locationPermissionDeny() {
        if ($(id("com.android.permissioncontroller:id/grant_dialog"))
                .is(Condition.visible, Duration.ofMillis(1500))) {
            PERMISSION_DENY_BUTTON.click();
        }
        return this;
    }

    @Step("Клик по координатам: x={x}, y={y}")
    public AndroidElementsPage tapByCoordinates(int x, int y) {
        sleep(1_500);
        RemoteWebDriver driver = (RemoteWebDriver) getWebDriver();
        
        final var finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        var tapPoint = new Point(x, y);
        var tap = new Sequence(finger, 1);
        
        tap.addAction(finger.createPointerMove(Duration.ofMillis(0),
            PointerInput.Origin.viewport(), tapPoint.x, tapPoint.y));
        tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tap.addAction(new Pause(finger, Duration.ofMillis(50)));
        tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        
        driver.perform(Arrays.asList(tap));
        
        return this;
    }

}
