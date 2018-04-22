package ru.test.autotest;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.page.controlpanel.ControlPanelPage;
import ru.page.login.ChangePasswordPage;
import ru.page.login.LoginPage;
import ru.page.login.PasswordResetPage;
import ru.page.yandex.YandexLoginPage;
import ru.page.yandex.YandexPage;
import ru.utils.PropertiesStream;
import ru.utils.TestUtils;

import java.util.Properties;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.switchTo;
import static com.codeborne.selenide.WebDriverRunner.clearBrowserCache;

/**
 * Created by makenshi on 4/15/18.
 */
public class PositiveTest {

    private static Properties test;
    private static TestUtils utils;

    @BeforeClass
    public static void setUp() {
        utils = new TestUtils();
        Properties prop = new PropertiesStream("config/properties.ini", "UTF-8").getProperties();

        Configuration.baseUrl = prop.getProperty("site_name");
        Configuration.browser = prop.getProperty("browser_name");

        test = new PropertiesStream("config/test.ini", "UTF-8").getProperties();
    }

    @DataProvider
    public Object[][] parseLoginEmailPasswordData() {
        return new Object[][]{
                //case userLogin
                {test.getProperty("account_exist_email"),
                        test.getProperty("account_exist_password"),
                        true},
                //case userLoginDifferentRegister
                {test.getProperty("account_exist_email_register"),
                        test.getProperty("account_exist_password_register"),
                        true},
                //case userLoginLowerRegisterEmail
                {test.getProperty("account_exist_email_register").toLowerCase(),
                        test.getProperty("account_exist_password_register"),
                        false},
                // TODO в FF работает подругому
                //case userLoginSpacesBeforeLogin
                {"      " + test.getProperty("account_exist_email"),
                        test.getProperty("account_exist_password"),
                        true},
                // TODO в FF работает подругому
                //case userLoginSpacesAfterLogin
                {test.getProperty("account_exist_email") + "      ",
                        test.getProperty("account_exist_password"),
                        true},
                //case userLoginCyrillic
                {test.getProperty("account_exist_email_cyrillic"),
                        test.getProperty("account_exist_password_cyrillic"),
                        true},
                //case userLoginAccentedChars
                {test.getProperty("account_exist_email_accented_chars"),
                        test.getProperty("account_exist_password_accented_chars"),
                        true},
        };
    }

    @Test(dataProvider = "parseLoginEmailPasswordData")
    public void userLogin(String email, String password, boolean bool) {
        open("/");
        LoginPage lp = new LoginPage(Integer.parseInt(test.getProperty("explicit_wait_lp")));
        lp.getEmailField().val(email);
        lp.getPasswordField().val(password);
        lp.getSignInButton().click();

        ControlPanelPage cp = new ControlPanelPage(Integer.parseInt(test.getProperty("explicit_wait_cp")));

        String url = WebDriverRunner.url();
        String[] parts = url.split("#");
        StringBuilder deleteUrl = new StringBuilder(parts[0] + "#profile");
        open(deleteUrl.toString());

        cp.getPageProfile().shouldBe(Condition.visible);;
        Assert.assertEquals(
                cp.getCollectionUserData().findBy(Condition.value(email)).exists(), bool);
        cp.logout();
    }

    //-----------------------------------------------------------------------------------------------------------------------------
    @Test
    public void userLoginBorder() {
        open("/");
        LoginPage lp = new LoginPage(Integer.parseInt(test.getProperty("explicit_wait_lp")));
        SelenideElement focusFirst = lp.getEmailField();
        focusFirst.click();
        Assert.assertEquals(focusFirst.find(By.xpath(lp.FOCUS)).exists() , true);
    }

    //-----------------------------------------------------------------------------------------------------------------------------

    @Test
    public void userLoginBubbleTab() {
        open("/");
        LoginPage lp = new LoginPage(Integer.parseInt(test.getProperty("explicit_wait_lp")));
        lp.getEmailField().pressTab();
        Assert.assertEquals(lp.getPasswordField().find(By.xpath(lp.FOCUS)).exists(), true);
    }

    //-----------------------------------------------------------------------------------------------------------------------------
    @Test
    public void userLoginBrowserNewWindow() {
        open("/");
        LoginPage lp = new LoginPage(Integer.parseInt(test.getProperty("explicit_wait_lp")));
        lp.getEmailField().val(test.getProperty("account_exist_email"));
        lp.getPasswordField().val(test.getProperty("account_exist_password"));
        lp.getSignInButton().click();

        new ControlPanelPage(Integer.parseInt(test.getProperty("explicit_wait_cp")));
        executeJavaScript("window.open('" + WebDriverRunner.url() + "','/');");
        switchTo().window(1);

        try {
            ControlPanelPage cp = new ControlPanelPage(Integer.parseInt(test.getProperty("explicit_wait_cp")));
            Assert.assertEquals(cp.getAnchor().shouldBe(Condition.visible).exists(), true);
        }
        finally {
            executeJavaScript("window.close();");
            switchTo().window(0);
        }
    }

    @Test
    public void userLoginBrowserBack() {
        open("/");
        LoginPage lp = new LoginPage(Integer.parseInt(test.getProperty("explicit_wait_lp")));
        lp.getEmailField().val(test.getProperty("account_exist_email"));
        lp.getPasswordField().val(test.getProperty("account_exist_password"));
        lp.getSignInButton().click();
        ControlPanelPage cp = new ControlPanelPage(Integer.parseInt(test.getProperty("explicit_wait_cp")));
        back();
        Assert.assertEquals(cp.getAnchor().shouldBe(Condition.visible).exists(), true);
    }

    @Test
    public void userLoginSeveralAttemptsIncorrectly() {
        open("/");
        LoginPage lp = new LoginPage(Integer.parseInt(test.getProperty("explicit_wait_lp")));
        lp.getEmailField().setValue(test.getProperty("account_exist_email"));
        lp.getPasswordField().setValue(utils.generateRandomString(Integer.parseInt(test.getProperty("password_random_lenght"))));

        for (Integer i = 0; i < Integer.parseInt(test.getProperty("number_attempts_incorrectly_login")); i++) {
            lp.getSignInButton().click();
            // убрал проверку на ошибку в bubble  test.getProperty("password_no_correct"));
            // она тут не главная
            lp.getBubbleError().waitUntil(Condition.visible, 5000);
        }
        lp.getPasswordField().setValue(test.getProperty("account_exist_password"));
        lp.getSignInButton().click();

        ControlPanelPage cp = new ControlPanelPage(Integer.parseInt(test.getProperty("explicit_wait_cp")));
        Assert.assertEquals(cp.getAnchor().shouldBe(Condition.visible).exists(), true);
    }

    @Test
    public void userLoginKeepSignedIn() {
        open("/");
        LoginPage lp = new LoginPage(Integer.parseInt(test.getProperty("explicit_wait_lp")));
        lp.getEmailField().val(test.getProperty("account_exist_email"));
        lp.getPasswordField().val(test.getProperty("account_exist_password"));
        lp.getCheckKeepMeSignedIn().click();
        lp.getSignInButton().click();

        ControlPanelPage cp = new ControlPanelPage(Integer.parseInt(test.getProperty("explicit_wait_cp")));
        Assert.assertEquals(cp.getAnchor().shouldBe(Condition.visible).exists(), true);
    }

    @Test
    public void userLoginRestorePassword() {
        open("/");
        try{
            LoginPage lp = new LoginPage(Integer.parseInt(test.getProperty("explicit_wait_lp")));

            executeJavaScript("window.open('" + test.getProperty("url_yandex") + "','/');");
            switchTo().window(1);

            YandexLoginPage yandexLogin = new YandexLoginPage(Integer.parseInt(test.getProperty("explicit_wait_yandex")));
            yandexLogin.getBigButton().click();
            yandexLogin.getPassportLogin().setValue(test.getProperty("login_yandex"));
            yandexLogin.getPassportPasswd().setValue(test.getProperty("password_yandex"));
            yandexLogin.getLoginButton().click();

            System.out.println($(By.xpath("//span[@class='mail-App-Footer-Item']")).getText());

            YandexPage yandex = new YandexPage(Integer.parseInt(test.getProperty("explicit_wait_yandex")));
            yandex.getToolbarSelectAll().click();
            yandex.getToolbarDelete().click();

            switchTo().window(0);

            lp.getLinkForgotYourPassword().click();

            PasswordResetPage prp = new PasswordResetPage();
            prp.getField().setValue(test.getProperty("account_exist_email_yandex"));
            prp.getButton().click();
            switchTo().window(1);
            sleep(3000);

            yandex.waitRefresh(Integer.parseInt(test.getProperty("number_retry_yandex")),
                    1000,
                    yandex.getResetPasswordEmail(test.getProperty("find_email_text")));

            yandex.getResetPasswordEmail(test.getProperty("find_email_text")).click();

            String restore = yandex.getPasswordRecoveryLink().getAttribute("href");

            executeJavaScript("window.close();");
            switchTo().window(0);
            sleep(3000);
            open(restore);

            ChangePasswordPage cpp = new ChangePasswordPage();
            cpp.getField().setValue(test.getProperty("account_exist_password_yandex"));
            cpp.getButton().click();

            ControlPanelPage cp = new ControlPanelPage(Integer.parseInt(test.getProperty("explicit_wait_cp")));
            Assert.assertEquals(cp.getAnchor().shouldBe(Condition.visible).exists(), true);
        } finally {
            close();
        }
    }

    @AfterMethod
    public void tearDown() {
        clearBrowserCache();
    }
}
