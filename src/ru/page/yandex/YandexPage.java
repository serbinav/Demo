package ru.page.yandex;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.refresh;
import static com.codeborne.selenide.Selenide.sleep;

public class YandexPage {

    SelenideElement anchor;

    public YandexPage(int sleep){
        // таких элементов 4, но думаю это не очень принципиально
        anchor = $(By.xpath("//span[@class='mail-App-Footer-Item']")).waitUntil(Condition.visible, sleep);
    }

    public SelenideElement getAnchor() {
        return anchor;
    }

    public SelenideElement getToolbarSelectAll() {
        return     $(By.xpath("//div[contains(@class,'mail-Toolbar-Item_main-select-all')]//span[@class='checkbox_view']"));
    }

    public SelenideElement getToolbarDelete() {
        return     $(By.xpath("//span[contains(@class,'js-toolbar-item-title-delete')]"));
    }

    public SelenideElement getResetPasswordEmail(String text) {
        return     $(By.xpath("//div[@class='mail-MessageSnippet-Content']/span[contains(., '"+text+"')]"));
    }

    public SelenideElement getPasswordRecoveryLink() {
        return     $(By.xpath("//div[contains(@class,'mail-Message-Body-Content_plain')]//a[last()]"));
    }

    public boolean waitRefresh(int countRetry, int sleep, SelenideElement element) {
        for (Integer i = 0; i < countRetry; i++) {
            refresh();
            sleep(sleep);
            boolean visible = element.exists();
            if (visible == true)
                return true;
        }
        return false;
    }

}
