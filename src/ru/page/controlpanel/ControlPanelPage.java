package ru.page.controlpanel;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Created by makenshi on 4/19/18.
 */

public class ControlPanelPage {

    SelenideElement anchor;

    public ControlPanelPage(int sleep) {
        $(By.xpath("//div[@class='loading-panel' and not(contains(@style,'display: none'))]")).waitUntil(Condition.visible, sleep);
        $(By.xpath("//div[@class='menu']")).waitUntil(Condition.visible, sleep);
        anchor = $(By.xpath("//h1[@class='settings-page__title']")).waitUntil(Condition.visible, sleep);
    }

    public SelenideElement getAnchor() {
        return anchor;
    }

    public SelenideElement getProfilePage() {
        return     $(By.xpath("//div[@class='ProfileView']//div[@class='Title']//span[@class='gwt-InlineHTML']"));
    }

    public ElementsCollection getUserDataCollection() {
        return     $$(By.xpath("//div[@class='backend-TitledPanel-content']//input[@class='gwt-TextBox']"));
    }

    public void logout() {
        $(By.xpath("//a[@class='horizontal-icolink icolink-append']//div[@class='multiadmin-profile']")).click();
        $(By.xpath("//div[@class='store-profile-footer']//a")).click();
    }
}



