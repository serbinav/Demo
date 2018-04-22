package ru.page.login;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by makenshi on 4/19/18.
 */

public class LoginPage {

    // TODO так вроде бы получше
    public static final String FOCUS = "//div[@class='field field--large field--focus']";

    SelenideElement anchor;

    public LoginPage(int sleep){
        anchor = $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).waitUntil(Condition.visible, sleep);
    }

    public SelenideElement getAnchor() {
        return anchor;
    }

    public SelenideElement getEmailField() {
        return     $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='email']"));
    }

    public SelenideElement getPasswordField() {
        return     $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='password']"));
    }

    public SelenideElement getSignInButton() {
        return     $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button"));
    }

    public SelenideElement getBubbleError() {
        return     $(By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"));
    }

    public SelenideElement getCheckKeepMeSignedIn() {
        return     $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//span[@class='gwt-CheckBox']"));
    }

    public SelenideElement getLinkForgotYourPassword() {
        return     $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//p[1]//a[1]"));
    }

}


