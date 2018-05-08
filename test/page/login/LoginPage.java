package page.login;

import com.codeborne.selenide.Condition;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by makenshi on 4/19/18.
 */

public class LoginPage {

    private By focusLocator = By.xpath("//div[@class='field field--large field--focus']");
    private By loginPageLocator = By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']");
    private By emailLocator = By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='email']");
    private By passwordLocator = By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']" +
            "//input[@name='password']");
    private By signInButtonLocator = By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button");
    private By bubbleNotitleLocator = By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']" +
            "//div[@class='gwt-HTML']");
    private By keepMeSignedInLocator = By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//span[@class='gwt-CheckBox']");
    private By forgotPasswordLinkLocator = By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']" +
            "//p[1]//a[1]");

    //TODO много методов объеденить их в какие-то действия
    public LoginPage(int sleep) {
        $(loginPageLocator).waitUntil(Condition.visible, sleep);
    }

    public void setEmail(String email) {
        $(emailLocator).setValue(email);
    }

    public void sendKeysEmail(CharSequence key) {
        $(emailLocator).sendKeys(key);
    }

    public boolean isFocusEmail() {
        return $(emailLocator).find(focusLocator).exists();
    }

    public void setPassword(String password) {
        $(passwordLocator).setValue(password);
    }

    public boolean isFocusPassword() {
        return $(passwordLocator).find(focusLocator).exists();
    }

    public void clickSignInButton() {
        $(signInButtonLocator).click();
    }

    public void sendKeysSignInButton(CharSequence key) {
        $(signInButtonLocator).sendKeys(key);
    }

    public boolean isErrorBubble() {
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


