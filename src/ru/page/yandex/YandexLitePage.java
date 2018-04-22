package ru.page.yandex;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.*;

public class YandexLitePage {

    SelenideElement anchor;

    public YandexLitePage(int sleep) {
        anchor = $(By.xpath("//span[@class='b-footer__copyright']")).waitUntil(Condition.visible, sleep);
    }

    public SelenideElement getAnchor() {
        return anchor;
    }

    public SelenideElement getSelectAllCheckbox() {
        return $(By.xpath("//input[@data-action='check-all']"));
    }

    public SelenideElement getDeleteButton() {
        return $(By.xpath("//input[@name='delete']"));
    }

    public SelenideElement getResetPasswordEmail(String text) {
        return $(By.xpath("//div[contains(@class,'b-messages__message_unread')]//a[contains(@aria-label, '" + text + "')]"));
    }

    public SelenideElement getPasswordRecoveryLink() {
        return $(By.xpath("//a[@rel='noopener noreferrer']"));
    }

    public SelenideElement logout() {
        return $(By.xpath("//a[contains(@class, 'b-header__link_exit')]"));
    }

    public void sleepRefresh(int countRetry, int sleep, SelenideElement elementExists) {
        for (Integer i = 0; i < countRetry; i++) {
            refresh();
            sleep(sleep);
            boolean visible = elementExists.exists();
            if (visible == true)
                return;
        }
        return;
    }
}
