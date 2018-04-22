package ru.page.login;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class PasswordResetPage extends DropPassword {

    public PasswordResetPage(){
        anchor = $(By.xpath("//div[@class='reset-view block-view-on']//h3//div[@class='gwt-HTML']")).shouldBe(Condition.visible);
    }

    //getEmailField
    public SelenideElement getField() {
        return     $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_3']//input[@name='email']"));
    }

    //getResetPasswordButton
    public SelenideElement getButton() {
        return     $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_3']//button"));
    }

}