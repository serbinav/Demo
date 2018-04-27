package page.login;

import com.codeborne.selenide.Condition;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by makenshi on 4/19/18.
 */

public class LoginPage {

    By focusLocator = By.xpath("//div[@class='field field--large field--focus']");
    By loginPageLocator = By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']");
    By emailLocator = By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='email']");
    By passwordLocator = By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']" +
            "//input[@name='password']");
    By signInButtonLocator = By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button");
    By bubbleNotitleLocator = By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']" +
            "//div[@class='gwt-HTML']");
    By keepMeSignedInLocator = By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//span[@class='gwt-CheckBox']");
    By forgotPasswordLinkLocator = By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']" +
            "//p[1]//a[1]");

    public LoginPage(int sleep) {
        $(loginPageLocator).waitUntil(Condition.visible, sleep);
    }

    public void setEmail(String email) {
        $(emailLocator).setValue(email);
    }

    public void sendKeysEmail(CharSequence key) {
        $(emailLocator).sendKeys(key);
    }

    public boolean existsFocusEmail() {
        return $(emailLocator).find(focusLocator).exists();
    }

    public void setPassword(String password) {
        $(passwordLocator).setValue(password);
    }

    @Deprecated
    public void sendKeysPassword(CharSequence key) {
        $(passwordLocator).sendKeys(key);
    }

    public boolean existsFocusPassword() {
        return $(passwordLocator).find(focusLocator).exists();
    }

    public void clickSignInButton() {
        $(signInButtonLocator).click();
    }

    public void sendKeysSignInButton(CharSequence key) {
        $(signInButtonLocator).sendKeys(key);
    }

    @Deprecated
    public boolean existsFocusSignInButton() {
        return $(signInButtonLocator).find(focusLocator).exists();
    }

    public boolean existsErrorBubble() {
        return $(bubbleNotitleLocator).exists();
    }

    public String getErrorBubbleText() {

        return $(bubbleNotitleLocator).shouldBe(Condition.visible).getText();
    }

    public String getErrorBubbleText(int sleep) {

        return $(bubbleNotitleLocator).waitUntil(Condition.visible, sleep).getText();
    }

    public void clickKeepMeSignedInCheckbox() {
        $(keepMeSignedInLocator).click();
    }

    public void clickForgotPasswordLink() {
        $(forgotPasswordLinkLocator).click();
    }

    public void loginToEcwid(String email, String password) {
        this.setEmail(email);
        this.setPassword(password);
        this.clickSignInButton();
    }
}


