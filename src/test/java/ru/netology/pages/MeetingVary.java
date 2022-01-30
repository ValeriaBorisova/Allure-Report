package ru.netology.pages;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.LogEventListener;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import io.qameta.allure.selenide.AllureSelenide;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

@Feature("Тестирование заказа карты")
public class MeetingVary {

    @BeforeAll
    static void setUpAll() {

        SelenideLogger.addListener("allure", new AllureSelenide());
    }


    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Story("Первоначальный заказ карты")
    @Test
    public void shouldSendForm() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
        DataInfo info = DataGenerator.Form.generateInfo("ru");
        $("[data-test-id='city'] input").val(info.getCity());
        String planningDate = DataGenerator.generateDate(7);
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(planningDate).pressTab();
        $("[data-test-id='name'] input").val(info.getName());
        $(byName("phone")).setValue(info.getPhone());
        $(byClassName("checkbox__box")).click();
        $(withText("Запланировать")).click();
        $("[data-test-id='success-notification']").shouldHave(text("Успешно!"));
        $(".notification__content")
                .shouldHave(exactText("Встреча успешно запланирована на " + planningDate));

    }
    @Story("Перепланирование заказа карты на новую дату")
    @Test
    public void shouldSendFormNewDate() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
        DataInfo info = DataGenerator.Form.generateInfo("ru");
        $("[data-test-id='city'] input").val(info.getCity());
        String planningDate = DataGenerator.generateDate(7);
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(planningDate).pressTab();
        $("[data-test-id='name'] input").val(info.getName());
        $(byName("phone")).setValue(info.getPhone());
        $(byClassName("checkbox__box")).click();
        $(withText("Запланировать")).click();
        $("[data-test-id='success-notification']").shouldHave(text("Успешно!"));
        $(".notification__content")
                .shouldHave(exactText("Встреча успешно запланирована на " + planningDate));
        String planningDate2 = DataGenerator.generateDate(10);
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(planningDate2).pressTab();
        $(withText("Запланировать")).click();
        $("[data-test-id='replan-notification'] div[class='notification__title']").shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(text("Необходимо подтверждение"));
        $(withText("Перепланировать")).shouldBe(visible).click();
        $(".notification__content").shouldBe(visible)
                .shouldHave(exactText("Встреча успешно запланирована на " + planningDate2));


    }
}

