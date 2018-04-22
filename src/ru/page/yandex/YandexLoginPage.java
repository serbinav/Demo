package ru.page.yandex;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class YandexLoginPage {

    SelenideElement anchor;

    public YandexLoginPage(int sleep) {
        anchor = $(By.xpath("//span[contains(@class,'footer-item__rights')]")).waitUntil(Condition.visible, sleep);
    }

    public SelenideElement getAnchor() {
        return anchor;
    }

    public SelenideElement getLoginInput() {
        return $(By.xpath("//input[@class='passport-Input-Controller' and @name='login']"));
    }

    public SelenideElement getPasswordInput() {
        return $(By.xpath("//input[@class='passport-Input-Controller' and @name='passwd']"));
    }

    public SelenideElement getLoginButton() {
        return $(By.xpath("//span[@class='passport-Button-Text']"));
    }

}
