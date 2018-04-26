package ru.page.yandex;

import com.codeborne.selenide.Condition;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.*;

public class YandexLiteMailPage {

    By yandexLiteMailPageLocator = By.xpath("//span[@class='b-footer__copyright']");
    By selectAllLocator = By.xpath("//input[@data-action='check-all']");
    By deleteButtonLocator = By.xpath("//input[@name='delete']");
    By lineInTableWithEmail = By.xpath("//div[contains(@class,'b-messages__message_unread')]//a[contains(@aria-label," +
            "'Reset your Ecwid password')]");
    By passwordRecoveryLinkLocator = By.xpath("//a[@rel='noopener noreferrer']");
    By exitLocator = By.xpath("//a[contains(@class, 'b-header__link_exit')]");

    public YandexLiteMailPage(int sleep) {
        $(yandexLiteMailPageLocator).waitUntil(Condition.visible, sleep);
    }

    public boolean existsSelectAllCheckbox() {
        return $(selectAllLocator).exists();
    }

    public void clickSelectAllCheckbox() {
        $(selectAllLocator).click();
    }

    public void clickDeleteButton() {
        $(deleteButtonLocator).click();
    }

    private boolean existsResetPasswordEmail() {
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
            boolean visible = existsResetPasswordEmail();
            if (visible == true)
                return;
        }
        return;
    }
}
