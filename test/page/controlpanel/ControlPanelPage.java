package ru.page.controlpanel;

import com.codeborne.selenide.Condition;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Created by makenshi on 4/19/18.
 */

public class ControlPanelPage {

    By loadingLocator = By.xpath("//div[@class='loading-panel' and not(contains(@style,'display: none'))]");
    By menuLocator = By.xpath("//div[@class='menu']");
    By controlPanelPageLocator = By.xpath("//h1[@class='settings-page__title']");
    By profilePageLocator = By.xpath("//div[@class='ProfileView']//div[@class='Title']" +
            "//span[@class='gwt-InlineHTML']");
    By profileDataCollectionLocator = By.xpath("//div[@class='backend-TitledPanel-content']" +
            "//input[@class='gwt-TextBox']");
    By multiadminProfileLocator = By.xpath("//a[@class='horizontal-icolink icolink-append']" +
            "//div[@class='multiadmin-profile']");
    By signOutLinkLocator = By.xpath("//div[@class='store-profile-footer']//a");

    public ControlPanelPage(int sleep) {
        $(loadingLocator).waitUntil(Condition.visible, sleep);
        $(menuLocator).waitUntil(Condition.visible, sleep);
        $(controlPanelPageLocator).waitUntil(Condition.visible, sleep);
    }

    public boolean existsControlPanel() {
        return $(controlPanelPageLocator).shouldBe(Condition.visible).exists();
    }

    private boolean existsProfilePage() {
        return $(profilePageLocator).shouldBe(Condition.visible).exists();
    }

    public boolean findEmail(String email) {
        if (this.existsProfilePage() == true) {
            return $$(profileDataCollectionLocator).findBy(Condition.value(email)).exists();
        }
        return false;
    }

    public void logout() {
        $(multiadminProfileLocator).click();
        $(signOutLinkLocator).click();
    }
}



