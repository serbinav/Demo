package ru.page.login;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public abstract class DropPassword {

    SelenideElement anchor;

    DropPassword(){
        anchor = $(By.xpath("//div[@class='reset-view block-view-on']//h3//div[@class='gwt-HTML']")).shouldBe(Condition.visible);
    }

    public SelenideElement getAnchor() {
        return anchor;
    }

    public abstract SelenideElement getField();

    public abstract SelenideElement getButton();

}
