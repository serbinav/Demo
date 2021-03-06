package page.login;

import com.codeborne.selenide.Condition;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class PasswordResetPage {

    private By passwordResetPageLocator = By.xpath("//div[@class='reset-view block-view-on']//h3//div[@class='gwt-HTML']");
    private By emailFieldLocator = By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_3']" +
            "//input[@name='email']");
    private By passwordButtonLocator = By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_3']//button");

    public PasswordResetPage() {
        $(passwordResetPageLocator).shouldBe(Condition.visible);
    }

    public void setEmail(String email) {
        $(emailFieldLocator).setValue(email);
    }

    public void clickResetPasswordButton() {
        $(passwordButtonLocator).click();
    }

}