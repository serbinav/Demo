package ru.page.yandex;

import com.codeborne.selenide.Condition;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class YandexLoginPage {

    By yandexLoginPageLocator = By.xpath("//span[contains(@class,'footer-item__rights')]");
    By loginLocator = By.xpath("//input[@class='passport-Input-Controller' and @name='login']");
    By passwordLocator = By.xpath("//input[@class='passport-Input-Controller' and @name='passwd']");
    By loginButtonLocator = By.xpath("//span[@class='passport-Button-Text']");

    public YandexLoginPage(int sleep) {
        $(yandexLoginPageLocator).waitUntil(Condition.visible, sleep);
    }

    public void setLogin(String userName) {
        $(loginLocator).setValue(userName);
    }

    public void setPassword(String password) {
        $(passwordLocator).setValue(password);
    }

    public void clickLoginButton() {
        $(loginButtonLocator).click();
    }

    public void loginToYandex(String userName, String password) {
        this.setLogin(userName);
        this.setPassword(password);
        this.clickLoginButton();
    }
}
