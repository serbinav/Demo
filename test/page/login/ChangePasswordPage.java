package ru.page.login;

import com.codeborne.selenide.Condition;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class ChangePasswordPage {

    By changePasswordPageLocator = By.xpath("//div[@class='reset-view block-view-on']//h3//div[@class='gwt-HTML']");
    By newPasswordFieldLocator = By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_4']" +
            "//input[@name='password']");
    By changePasswordButtonLocator = By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_4']//button");

    public ChangePasswordPage() {
        $(changePasswordPageLocator).shouldBe(Condition.visible);
    }

    public void setNewPassword(String newPassword) {
        $(newPasswordFieldLocator).setValue(newPassword);
    }

    public void clickChangePasswordButton() {
        $(changePasswordButtonLocator).click();
    }
}