package page.yandex;

import com.codeborne.selenide.Condition;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.*;

public class YandexLiteMailPage {

    private By yandexLiteMailPageLocator = By.xpath("//span[@class='b-footer__copyright']");
    private By selectAllLocator = By.xpath("//input[@data-action='check-all']");
    private By deleteButtonLocator = By.xpath("//input[@name='delete']");
    private By lineInTableWithEmail = By.xpath("//div[contains(@class,'b-messages__message_unread')]//a[contains(@aria-label," +
            "'Reset your Ecwid password')]");
    private By passwordRecoveryLinkLocator = By.xpath("//a[@rel='noopener noreferrer']");
    private By exitLocator = By.xpath("//a[contains(@class, 'b-header__link_exit')]");

    public YandexLiteMailPage(int sleep) {
        $(yandexLiteMailPageLocator).waitUntil(Condition.visible, sleep);
    }

    public boolean isSelectAllCheckbox() {
        return $(selectAllLocator).exists();
    }

    public void clickSelectAllCheckbox() {
        $(selectAllLocator).click();
    }

    public void clickDeleteButton() {
        $(deleteButtonLocator).click();
    }

    private boolean isResetPasswordEmail() {
        return $(lineInTableWithEmail).exists();
    }

    public void clickResetPasswordEmail() {
        $(lineInTableWithEmail).click();
    }

    public String getPasswordRecoveryLinkText() {
        return $(passwordRecoveryLinkLocator).getText();
    }

    public void logout() {
        $(exitLocator).click();
    }

    public void waitEmail(int countRetry, int sleep) {
        for (Integer i = 0; i < countRetry; i++) {
            refresh();
            sleep(sleep);
            boolean visible = isResetPasswordEmail();
            if (visible == true)
                return;
        }
        return;
    }
}
