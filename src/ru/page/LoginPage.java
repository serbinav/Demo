package ru.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by makenshi on 4/19/18.
 */


public class LoginPage {

    public static final By FOCUS = By.xpath("//div[@class='field field--large field--focus']");

    public LoginPage(int sleep){
        $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).waitUntil(Condition.visible, sleep);
    }

    public SelenideElement getEmailField() {
        return $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='email']"));
    }

    public SelenideElement getPasswordField() {
        return $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='password']"));
    }

    public SelenideElement getSignInButton() {
        return $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button"));
    }

//    public SelenideElement getLinkForgotYourPassword() {
//        return $("#ires .g", index);
//    }

//    public SelenideElement getCheckKeepMeSignedIn() {
//        return $("#ires .g", index);
//    }

}

