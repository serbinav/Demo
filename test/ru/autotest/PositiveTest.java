package ru.autotest;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.Keys;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import page.controlpanel.ControlPanelPage;
import page.login.ChangePasswordPage;
import page.login.LoginPage;
import page.login.PasswordResetPage;
import page.yandex.YandexLiteMailPage;
import page.yandex.YandexLoginPage;
import utils.PropertiesStream;
import utils.TestUtils;

import java.util.Properties;

import static com.codeborne.selenide.Selenide.*;
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
                        true},
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
        };
    }

    @Test(dataProvider = "parseLoginEmailPasswordData")
    public void userLogin(String email, String password, boolean bool) {
        open("/");
        LoginPage lp = new LoginPage(Integer.parseInt(test.getProperty("explicit_wait_lp")));
        lp.loginToEcwid(email,password);

        ControlPanelPage cp = new ControlPanelPage(Integer.parseInt(test.getProperty("explicit_wait_cp")));

        String url = WebDriverRunner.url();
        String[] parts = url.split("#");
        StringBuilder deleteUrl = new StringBuilder(parts[0] + "#profile");
        open(deleteUrl.toString());

        Assert.assertEquals(cp.findEmail(email), bool);
        cp.logout();
    }

    //-----------------------------------------------------------------------------------------------------------------------------
    @Test
    public void userLoginBorder() {
        open("/");
        LoginPage lp = new LoginPage(Integer.parseInt(test.getProperty("explicit_wait_lp")));
        lp.sendKeysEmail(Keys.ENTER);
        Assert.assertEquals(lp.existsFocusEmail(), true);
    }

    //-----------------------------------------------------------------------------------------------------------------------------

    @Test
    public void userLoginBubbleTab() {
        open("/");
        LoginPage lp = new LoginPage(Integer.parseInt(test.getProperty("explicit_wait_lp")));
        lp.sendKeysEmail(Keys.TAB);
        Assert.assertEquals(lp.existsFocusPassword(), true);
    }

    //-----------------------------------------------------------------------------------------------------------------------------
    @Test
    public void userLoginBrowserNewWindow() {
        open("/");
        LoginPage lp = new LoginPage(Integer.parseInt(test.getProperty("explicit_wait_lp")));
        lp.loginToEcwid(test.getProperty("account_exist_email"),test.getProperty("account_exist_password"));

        new ControlPanelPage(Integer.parseInt(test.getProperty("explicit_wait_cp")));
        executeJavaScript("window.open('" + WebDriverRunner.url() + "','/');");
        switchTo().window(1);

        try {
            ControlPanelPage cp = new ControlPanelPage(Integer.parseInt(test.getProperty("explicit_wait_cp")));
            Assert.assertEquals(cp.existsControlPanel(), true);
        } finally {
            executeJavaScript("window.close();");
            switchTo().window(0);
        }
    }

    @Test
    public void userLoginBrowserBack() {
        open("/");
        LoginPage lp = new LoginPage(Integer.parseInt(test.getProperty("explicit_wait_lp")));
        lp.loginToEcwid(test.getProperty("account_exist_email"),test.getProperty("account_exist_password"));

        ControlPanelPage cp = new ControlPanelPage(Integer.parseInt(test.getProperty("explicit_wait_cp")));
        back();
        Assert.assertEquals(cp.existsControlPanel(), true);
    }

    @Test
    public void userLoginSeveralAttemptsIncorrectly() {
        open("/");
        LoginPage lp = new LoginPage(Integer.parseInt(test.getProperty("explicit_wait_lp")));
        lp.setEmail(test.getProperty("account_exist_email"));
        lp.setPassword(utils.generateRandomString(Integer.parseInt(test.getProperty("password_random_lenght"))));

        for (Integer i = 0; i < Integer.parseInt(test.getProperty("number_attempts_incorrectly_login")); i++) {
            lp.clickSignInButton();
            lp.getErrorBubbleText(Integer.parseInt(test.getProperty("explicit_wait_cp")));
        }
        lp.setPassword(test.getProperty("account_exist_password"));
        lp.clickSignInButton();

        ControlPanelPage cp = new ControlPanelPage(Integer.parseInt(test.getProperty("explicit_wait_cp")));
        Assert.assertEquals(cp.existsControlPanel(), true);
    }

    @Test
    public void userLoginKeepSignedIn() {
        open("/");
        LoginPage lp = new LoginPage(Integer.parseInt(test.getProperty("explicit_wait_lp")));
        lp.clickKeepMeSignedInCheckbox();
        lp.loginToEcwid(test.getProperty("account_exist_email"),test.getProperty("account_exist_password"));

        ControlPanelPage cp = new ControlPanelPage(Integer.parseInt(test.getProperty("explicit_wait_cp")));
        Assert.assertEquals(cp.existsControlPanel(), true);
    }

    @Test
    public void userLoginRestorePassword() {
        open("/");
        try {
            LoginPage lp = new LoginPage(Integer.parseInt(test.getProperty("explicit_wait_lp")));

            executeJavaScript("window.open('" + test.getProperty("url_yandex") + "','/');");
            switchTo().window(1);

            YandexLoginPage yandexLogin = new YandexLoginPage(Integer.parseInt(test.getProperty("explicit_wait_yandex")));
            yandexLogin.loginToYandex(test.getProperty("login_yandex"),test.getProperty("password_yandex"));

            YandexLiteMailPage yandex = new YandexLiteMailPage(Integer.parseInt(test.getProperty("explicit_wait_yandex")));
            //если есть что удалить удалим, если нет пойдем дальше
            if (yandex.existsSelectAllCheckbox() == true) {
                yandex.clickSelectAllCheckbox();
                yandex.clickDeleteButton();
            }

            switchTo().window(0);
            lp.clickForgotPasswordLink();

            PasswordResetPage prp = new PasswordResetPage();
            prp.setEmail(test.getProperty("account_exist_email_yandex"));
            prp.clickResetPasswordButton();
            switchTo().window(1);

            yandex.waitEmail(Integer.parseInt(test.getProperty("number_retry_yandex")),
                    Integer.parseInt(test.getProperty("sleep_between_retry_yandex")));
            yandex.clickResetPasswordEmail();
            String restore = yandex.getPasswordRecoveryLinkText();

            executeJavaScript("window.close();");
            switchTo().window(0);
            open(restore);

            ChangePasswordPage cpp = new ChangePasswordPage();
            cpp.setNewPassword(test.getProperty("account_exist_password_yandex"));
            cpp.clickChangePasswordButton();

            ControlPanelPage cp = new ControlPanelPage(Integer.parseInt(test.getProperty("explicit_wait_cp")));
            Assert.assertEquals(cp.existsControlPanel(), true);
        } finally {
            close();
        }
    }

    @AfterMethod
    public void tearDown() {
        clearBrowserCache();
    }
}
