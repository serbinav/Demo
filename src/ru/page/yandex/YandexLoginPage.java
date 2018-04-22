package ru.page.yandex;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class YandexLoginPage {

    SelenideElement anchor;

    public YandexLoginPage(int sleep){
        anchor = $(By.xpath("//div[@class='Footer-Copyright']")).waitUntil(Condition.visible, sleep);
    }

    public SelenideElement getAnchor() {
        return anchor;
    }

    public SelenideElement getBigButton() {
        return     $(By.xpath("//a[contains(@class,'HeadBanner-Button-Enter')]"));
    }

    public SelenideElement getPassportLogin() {
        return     $(By.xpath("//input[@class='passport-Input-Controller' and @name='login']"));
    }

    public SelenideElement getPassportPasswd() {
        return     $(By.xpath("//input[@class='passport-Input-Controller' and @name='passwd']"));
    }

    public SelenideElement getLoginButton() {
        return     $(By.xpath("//span[@class='passport-Button-Text']"));
    }

}
