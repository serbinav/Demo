package page.controlpanel;

import com.codeborne.selenide.Condition;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Created by makenshi on 4/19/18.
 */

public class ControlPanelPage {

    private By loadingLocator = By.xpath("//div[@class='loading-panel' and not(contains(@style,'display: none'))]");
    private By menuLocator = By.xpath("//div[@class='menu']");
    private By controlPanelPageLocator = By.xpath("//h1[@class='settings-page__title']");
    private By profilePageLocator = By.xpath("//div[@class='ProfileView']//div[@class='Title']" +
            "//span[@class='gwt-InlineHTML']");
    private By profileDataCollectionLocator = By.xpath("//div[@class='backend-TitledPanel-content']" +
            "//input[@class='gwt-TextBox']");

    //TODO переделать двойные xpath
    private By multiadminProfileLocator = By.xpath("//a[@class='horizontal-icolink icolink-append']" +
            "//div[@class='multiadmin-profile']");
    private By signOutLinkLocator = By.xpath("//div[@class='store-profile-footer']//a");

    public ControlPanelPage(int sleep) {
        $(loadingLocator).waitUntil(Condition.visible, sleep);
        $(menuLocator).waitUntil(Condition.visible, sleep);
        $(controlPanelPageLocator).waitUntil(Condition.visible, sleep);
    }

    public boolean isControlPanel() {
        return $(controlPanelPageLocator).shouldBe(Condition.visible).exists();
    }

    private boolean isProfilePage() {
        return $(profilePageLocator).shouldBe(Condition.visible).exists();
    }

    public boolean findEmail(String email) {
        if (this.isProfilePage() == true) {
            return $$(profileDataCollectionLocator).findBy(Condition.value(email)).exists();
        }
        return false;
    }

    public void logout() {
        $(multiadminProfileLocator).click();
        $(signOutLinkLocator).click();
    }
}



