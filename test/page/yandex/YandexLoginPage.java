package page.yandex;

import com.codeborne.selenide.Condition;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class YandexLoginPage {

    By yandexLoginOrLiteLoginPageLocator = By.xpath(
            "//span[contains(@class,'footer-item__rights')]|//div[@class='footer-copyright']");
    By loginLocator = By.xpath("//input[@class='passport-Input-Controller' and @name='login']");
    By loginLiteLocator = By.xpath("//input[@id='login']");
    By passwordLocator = By.xpath("//input[@class='passport-Input-Controller' and @name='passwd']");
    By passwordLiteLocator = By.xpath("//input[@id='passwd']");
    By loginButtonLocator = By.xpath("//span[@class='passport-Button-Text']");
    By loginButtonLiteLocator = By.xpath("//button[@type='submit']");

    public YandexLoginPage(int sleep) {
        $(yandexLoginOrLiteLoginPageLocator).waitUntil(Condition.visible, sleep);
    }

    public void setLogin(String userName) {
        if ($(loginLocator).exists() == true)
            $(loginLocator).setValue(userName);
        else {
            $(loginLiteLocator).setValue(userName);
        }
    }

    public void setPassword(String password) {
        if ($(passwordLocator).exists() == true)
            $(passwordLocator).setValue(password);
        else {
            $(passwordLiteLocator).setValue(password);
        }
    }

    public void clickLoginButton() {
        if ($(loginButtonLocator).exists() == true)
            $(loginButtonLocator).click();
        else {
            $(loginButtonLiteLocator).click();
        }
    }

    public void loginToYandex(String userName, String password) {
        this.setLogin(userName);
        this.setPassword(password);
        this.clickLoginButton();
    }
}
