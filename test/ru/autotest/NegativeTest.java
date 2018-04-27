package ru.autotest;

import com.codeborne.selenide.Configuration;
import org.openqa.selenium.Keys;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
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
public class NegativeTest {

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
    public Object[][] parseEmailPasswordData() {
        return new Object[][]{
                //case userLoginPasswordSwapEmail
                {test.getProperty("account_exist_password"),
                        test.getProperty("account_exist_email"),
                        test.getProperty("email")},
                //case userLoginBubbleNoAssociatedAcc
                {utils.composeEmail(utils.generateRandomString(Integer.parseInt(test.getProperty("email_random_lenght"))),
                        test.getProperty("email_template")),
                        utils.generateRandomString(Integer.parseInt(test.getProperty("password_random_lenght"))),
                        test.getProperty("no_account")},
                //case userLoginBubblePassEmpty
                {utils.composeEmail(utils.generateRandomString(Integer.parseInt(test.getProperty("email_random_lenght"))),
                        test.getProperty("email_template")),
                        "",
                        test.getProperty("password")},
                //case userLoginBubblePassOnlySpace
                {utils.composeEmail(utils.generateRandomString(Integer.parseInt(test.getProperty("email_random_lenght"))),
                        test.getProperty("email_template")),
                        "      ",
                        test.getProperty("password")},
                //case userLoginBubbleEmailWithSpace
                {utils.composeEmail(" ", test.getProperty("email_template")),
                        "",
                        test.getProperty("email")},
                //case userLoginBubbleMailRandomString
                {utils.generateRandomString(Integer.parseInt(test.getProperty("email_random_lenght"))),
                        "",
                        test.getProperty("email")},
                //case userLoginBubbleVeryLongMail
                {utils.composeEmail(utils.generateRandomString(Integer.parseInt(test.getProperty("very_longe_mail")) - test.getProperty("email_template").length()), test.getProperty("email_template")),
                        "",
                        test.getProperty("email")},
                //case userLoginArabicLetters
                {test.getProperty("account_error_email_saudi_arabia"),
                        "",
                        test.getProperty("email")},
                //case userLoginIndianLetters
                {test.getProperty("account_error_email_india"),
                        "",
                        test.getProperty("email")},
                //case userLoginChineseLetters
                {test.getProperty("account_error_email_china"),
                        "",
                        test.getProperty("email")},
                //case userLoginOtherLetters
                {test.getProperty("account_error_email_south_korea"),
                        "",
                        test.getProperty("email")},
                //case userLoginEmoji
                {test.getProperty("account_error_email_emoji"),
                        "",
                        test.getProperty("email")},
                //case userLoginBannedSymbol
                {test.getProperty("account_error_email_banned_character"),
                        "",
                        test.getProperty("email")},
                //case userLoginExtendedLatinAlphabet
                {test.getProperty("account_exist_email_extended_latin"),
                        utils.generateRandomString(Integer.parseInt(test.getProperty("password_random_lenght"))),
                        test.getProperty("no_account")},
                //case userLoginAccentedChars
                {test.getProperty("account_error_email_assignee_chars"),
                        utils.generateRandomString(Integer.parseInt(test.getProperty("password_random_lenght"))),
                        test.getProperty("no_account")},
                //case userLoginCyrillic
                {test.getProperty("account_exist_email_cyrillic"),
                        utils.generateRandomString(Integer.parseInt(test.getProperty("password_random_lenght"))),
                        test.getProperty("no_account")},
        };
    }

    @Test(dataProvider = "parseEmailPasswordData")
    public void userLoginBubble(String email, String password, String error) {
        open("/");
        LoginPage lp = new LoginPage(Integer.parseInt(test.getProperty("explicit_wait_lp")));
        lp.loginToEcwid(email, password);
        Assert.assertEquals(lp.getErrorBubbleText(), error);
    }

    //-----------------------------------------------------------------------------------------------------------------------------
    @Test
    public void userLoginBubbleEsc() {
        open("/");
        LoginPage lp = new LoginPage(Integer.parseInt(test.getProperty("explicit_wait_lp")));
        lp.clickSignInButton();
        lp.sendKeysEmail(Keys.ESCAPE);
        Assert.assertEquals(lp.existsErrorBubble(), false);
    }

    //-----------------------------------------------------------------------------------------------------------------------------
    @Test
    public void userLoginBubbleEnter() {
        open("/");
        LoginPage lp = new LoginPage(Integer.parseInt(test.getProperty("explicit_wait_lp")));
        lp.sendKeysSignInButton(Keys.ENTER);
        Assert.assertEquals(lp.getErrorBubbleText(), test.getProperty("email"));
    }

    //-----------------------------------------------------------------------------------------------------------------------------
    @Test
    public void userLoginBubbleNoCorrectPass() {
        open("/");
        LoginPage lp = new LoginPage(Integer.parseInt(test.getProperty("explicit_wait_lp")));
        lp.loginToEcwid(test.getProperty("account_exist_email"),
                utils.generateRandomString(Integer.parseInt(test.getProperty("password_random_lenght"))));
        // подправил тут ожидание может быть дольше чем в других тестах
        Assert.assertEquals(lp.getErrorBubbleText(Integer.parseInt(test.getProperty("explicit_wait_cp"))),
                test.getProperty("password_no_correct"));
    }
    //-----------------------------------------------------------------------------------------------------------------------------

    @Test
    public void userLoginBubbleEmptyEmail() {
        open("/");
        LoginPage lp = new LoginPage(Integer.parseInt(test.getProperty("explicit_wait_lp")));
        lp.clickSignInButton();
        Assert.assertEquals(lp.getErrorBubbleText(), test.getProperty("email"));
    }

    //-----------------------------------------------------------------------------------------------------------------------------
    @Test
    public void userLoginBrowserRefresh() {
        open("/");
        LoginPage lp = new LoginPage(Integer.parseInt(test.getProperty("explicit_wait_lp")));
        lp.setEmail(test.getProperty("account_exist_email"));
        lp.setPassword(test.getProperty("account_exist_password"));
        refresh();
        lp.clickSignInButton();
        Assert.assertEquals(lp.getErrorBubbleText(), test.getProperty("email"));
    }

    //-----------------------------------------------------------------------------------------------------------------------------
    @DataProvider
    public Object[][] parseEmailData() {
        return new Object[][]{
                //case   emailWithSpace
                {utils.composeEmail(" ", test.getProperty("email_template")),
                        test.getProperty("email")},
                //case  mailRandomString
                {utils.generateRandomString(Integer.parseInt(test.getProperty("email_random_lenght"))),
                        test.getProperty("email")},
                //case  veryLongMail
                {utils.composeEmail(utils.generateRandomString(Integer.parseInt(test.getProperty("very_longe_mail")) - test.getProperty("email_template").length()), test.getProperty("email_template")),
                        test.getProperty("email")},
                //case  arabicLetters
                {test.getProperty("account_error_email_saudi_arabia"),
                        test.getProperty("email")},
                //case  indianLetters
                {test.getProperty("account_error_email_india"),
                        test.getProperty("email")},
                //case  chineseLetters
                {test.getProperty("account_error_email_china"),
                        test.getProperty("email")},
                //case  otherLetters
                {test.getProperty("account_error_email_south_korea"),
                        test.getProperty("email")},
                //case  noAssociatedAcc
                {utils.composeEmail(utils.generateRandomString(Integer.parseInt(test.getProperty("email_random_lenght"))),
                        test.getProperty("email_template")),
                        test.getProperty("no_account")},
        };
    }

    @Test(dataProvider = "parseEmailData")
    public void userLoginRestorePasswordBadEmail(String email, String error) {
        open("/");
        LoginPage lp = new LoginPage(Integer.parseInt(test.getProperty("explicit_wait_lp")));
        lp.clickForgotPasswordLink();
        PasswordResetPage prp = new PasswordResetPage();
        prp.setEmail(email);
        prp.clickResetPasswordButton();
        Assert.assertEquals(lp.getErrorBubbleText(), error);
    }

    //-----------------------------------------------------------------------------------------------------------------------------

    @DataProvider
    public Object[][] parsePasswordData() {
        return new Object[][]{
                //case userLoginRestoreBadPasswordEmpty
                {"",
                        test.getProperty("six_letter")},
                //case userLoginRestoreBadPasswordSixSpace
                {"      ",
                        test.getProperty("six_letter")},
        };
    }

    @Test(dataProvider = "parsePasswordData")
    public void userLoginRestoreBadPassword(String password, String error) {
        open("/");
        try {
            LoginPage lp = new LoginPage(Integer.parseInt(test.getProperty("explicit_wait_lp")));
            executeJavaScript("window.open('" + test.getProperty("url_yandex") + "','/');");
            switchTo().window(1);

            YandexLoginPage yandexLogin = new YandexLoginPage(Integer.parseInt(test.getProperty("explicit_wait_yandex")));
            yandexLogin.loginToYandex(test.getProperty("login_yandex"), test.getProperty("password_yandex"));

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
            yandex.logout();

            executeJavaScript("window.close();");
            switchTo().window(0);
            open(restore);

            ChangePasswordPage cpp = new ChangePasswordPage();
            cpp.setNewPassword(password);
            cpp.clickChangePasswordButton();

            Assert.assertEquals(lp.getErrorBubbleText(), error);
        } finally {
            close();
        }
    }


    @DataProvider
    public Object[][] parseEmailDataRestore() {
        return new Object[][]{
                //case userLoginRestoreBadNoAccount
                {utils.composeEmail(utils.generateRandomString(Integer.parseInt(test.getProperty("email_random_lenght"))),
                        test.getProperty("email_template")),
                        test.getProperty("no_account")},
                //case userLoginRestoreBadEmail
                {test.getProperty("account_error_email_emoji"),
                        test.getProperty("email")},
        };
    }
    @Test(dataProvider = "parseEmailDataRestore")
    public void userLoginRestoreBadEmail(String email, String error) {
        open("/");
            LoginPage lp = new LoginPage(Integer.parseInt(test.getProperty("explicit_wait_lp")));
            lp.clickForgotPasswordLink();

            PasswordResetPage prp = new PasswordResetPage();
            prp.setEmail(email);
            prp.clickResetPasswordButton();
            Assert.assertEquals(lp.getErrorBubbleText(), error);
    }


    @AfterMethod
    public void tearDown() {
        clearBrowserCache();
    }

}


