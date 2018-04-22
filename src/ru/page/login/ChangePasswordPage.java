package ru.page.login;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class ChangePasswordPage extends DropPassword {

    public ChangePasswordPage(){
        super();
    }

    //getNewPassword
    public SelenideElement getField() {
        return     $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_4']//input[@name='password']"));
    }
    //getChangePasswordButton
    public SelenideElement getButton() {
        return     $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_4']//button"));
    }

}