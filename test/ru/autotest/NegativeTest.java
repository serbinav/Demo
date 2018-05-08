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

    private static Properties global;
    private static Properties test;
    private static TestUtils utils;

    @BeforeClass
    public static void setUp() {
        utils = new TestUtils();
        global = new PropertiesStream("config/properties.ini", "UTF-8").getProperties();

        Configuration.baseUrl = global.getProperty("site_name");
        Configuration.browser = global.getProperty("browser_name");

        test = new PropertiesStream("config/test.ini", "UTF-8").getProperties();
    }

    @DataProvider
    public Object[][] parseEmailPasswordData() {
        return new Object[][]{
                //case userLoginPasswordSwapEmail
                {test.getProperty("account_exist_password"),
                        test.getProperty("account_exist_email"),
                        test.getProperty("email"),
                        "меняем логин и пароль местами и пытаемся залогиниться"},
                //case userLoginBubbleNoAssociatedAcc
                {utils.composeEmail(utils.generateRandomString(Integer.parseInt(test.getProperty("email_random_lenght"))),
                        test.getProperty("email_template")),
                        utils.generateRandomString(Integer.parseInt(test.getProperty("password_random_lenght"))),
                        test.getProperty("no_account"),
                        "пытаемся залогиниться в несуществующий аккаунт"},
                //case userLoginBubblePassEmpty
                {utils.composeEmail(utils.generateRandomString(Integer.parseInt(test.getProperty("email_random_lenght"))),
                        test.getProperty("email_template")),
                        "",
                        test.getProperty("password"),
                        "пытаемся залогиниться c пустым паролем"},
                //case userLoginBubblePassOnlySpace
                {utils.composeEmail(utils.generateRandomString(Integer.parseInt(test.getProperty("email_random_lenght"))),
                        test.getProperty("email_template")),
                        "      ",
                        test.getProperty("password"),
                        "пытаемся залогиниться c паролем состоящим только из пробелов"},
                //case userLoginBubbleEmailWithSpace
                {utils.composeEmail(" ", test.getProperty("email_template")),
                        "",
                        test.getProperty("email"),
                        "пытаемся залогиниться c емаил в котором есть пробел"},
                //case userLoginBubbleMailRandomString
                {utils.generateRandomString(Integer.parseInt(test.getProperty("email_random_lenght"))),
                        "",
                        test.getProperty("email"),
                        "пытаемся залогиниться с не валидным емаил"},
                //case userLoginBubbleVeryLongMail
                {utils.composeEmail(utils.generateRandomString(Integer.parseInt(test.getProperty("very_longe_mail")) - test.getProperty("email_template").length()), test.getProperty("email_template")),
                        "",
                        test.getProperty("email"),
                        "пытаемся залогиниться с очень длинным емаил"},
                //case userLoginArabicLetters
                {test.getProperty("account_error_email_saudi_arabia"),
                        "",
                        test.getProperty("email"),
                        "пытаемся залогиниться с емаил содержащим арабскую вязь"},
                //case userLoginIndianLetters
                {test.getProperty("account_error_email_india"),
                        "",
                        test.getProperty("email"),
                        "пытаемся залогиниться с емаил содержащим индийские абугидами(что-то похожее на иероглифы)"},
                //case userLoginChineseLetters
                {test.getProperty("account_error_email_china"),
                        "",
                        test.getProperty("email"),
                        "пытаемся залогиниться с емаил содержащим китайские иероглифы"},
                //case userLoginOtherLetters
                {test.getProperty("account_error_email_south_korea"),
                        "",
                        test.getProperty("email"),
                        "пытаемся залогиниться с емаил содержащим китайские иероглифы"},
                //case userLoginEmoji
                {test.getProperty("account_error_email_emoji"),
                        "",
                        test.getProperty("email"),
                        "пытаемся залогиниться с емаил содержащим корейскую хангыль(что-то похожее на иероглифы)"},
                //case userLoginBannedSymbol
                {test.getProperty("account_error_email_banned_character"),
                        "",
                        test.getProperty("email"),
                        "пытаемся залогиниться с емаил содержащим эмоджи, пиктограммы и значки"},
                //case userLoginExtendedLatinAlphabet
                {test.getProperty("account_exist_email_extended_latin"),
                        utils.generateRandomString(Integer.parseInt(test.getProperty("password_random_lenght"))),
                        test.getProperty("no_account"),
                        "пытаемся залогиниться с емаил содержащим символы со знаками ударения"},
                //case userLoginAccentedChars
                {test.getProperty("account_error_email_assignee_chars"),
                        utils.generateRandomString(Integer.parseInt(test.getProperty("password_random_lenght"))),
                        test.getProperty("no_account"),
                        "пытаемся залогиниться с емаил содержащим неподдерживаемые специальные символы "},
                //case userLoginCyrillic
                {test.getProperty("account_exist_email_cyrillic"),
                        utils.generateRandomString(Integer.parseInt(test.getProperty("password_random_lenght"))),
                        test.getProperty("no_account"),
                        "пытаемся залогиниться с емаил содержащим кириллицу"},
        };
    }

    @Test(dataProvider = "parseEmailPasswordData")
    public void userLoginBubble(String email, String password, String error, String message) {
        open("/");
        LoginPage lp = new LoginPage(Integer.parseInt(global.getProperty("explicit_wait_lp")));
        lp.loginToEcwid(email, password);
        Assert.assertEquals(lp.getErrorBubbleText(), error, message);
    }

    //-----------------------------------------------------------------------------------------------------------------------------
    @Test
    public void userLoginBubbleEsc() {
        open("/");
        LoginPage lp = new LoginPage(Integer.parseInt(global.getProperty("explicit_wait_lp")));
        lp.clickSignInButton();
        lp.sendKeysEmail(Keys.ESCAPE);
        Assert.assertEquals(lp.isErrorBubble(), false, "должна появиться всплывающая ошибка(бабл) при нажатии ESCAPE");
    }

    //-----------------------------------------------------------------------------------------------------------------------------
    @Test
    public void userLoginBubbleEnter() {
        open("/");
        LoginPage lp = new LoginPage(Integer.parseInt(global.getProperty("explicit_wait_lp")));
        lp.sendKeysSignInButton(Keys.ENTER);
        Assert.assertEquals(lp.getErrorBubbleText(), test.getProperty("email"), "должна появиться всплывающая ошибка при " +
                "нажатии ENTER");
    }

    //-----------------------------------------------------------------------------------------------------------------------------
    @Test
    public void userLoginBubbleNoCorrectPass() {
        open("/");
        LoginPage lp = new LoginPage(Integer.parseInt(global.getProperty("explicit_wait_lp")));
        lp.loginToEcwid(test.getProperty("account_exist_email"),
                utils.generateRandomString(Integer.parseInt(test.getProperty("password_random_lenght"))));
        // подправил тут ожидание может быть дольше чем в других тестах
        Assert.assertEquals(lp.getErrorBubbleText(Integer.parseInt(global.getProperty("explicit_wait_cp"))),
                test.getProperty("password_no_correct"), "должна появиться ошибка о некорректном пароле, " +
                        "пытались залогиниться в существующий аккаунт с некорректным паролем");
    }

    //-----------------------------------------------------------------------------------------------------------------------------
    @Test
    public void userLoginBubbleEmptyEmail() {
        open("/");
        LoginPage lp = new LoginPage(Integer.parseInt(global.getProperty("explicit_wait_lp")));
        lp.clickSignInButton();
        Assert.assertEquals(lp.getErrorBubbleText(), test.getProperty("email"), "должна появиться ошибка о невалидном емаил, " +
                "пытались залогиниться с пустым логином и паролем");
    }

    //-----------------------------------------------------------------------------------------------------------------------------
    @Test
    public void userLoginBrowserRefresh() {
        open("/");
        LoginPage lp = new LoginPage(Integer.parseInt(global.getProperty("explicit_wait_lp")));
        lp.setEmail(test.getProperty("account_exist_email"));
        lp.setPassword(test.getProperty("account_exist_password"));
        refresh();
        lp.clickSignInButton();
        Assert.assertEquals(lp.getErrorBubbleText(), test.getProperty("email"), "message");
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
        LoginPage lp = new LoginPage(Integer.parseInt(global.getProperty("explicit_wait_lp")));
        lp.clickForgotPasswordLink();
        PasswordResetPage prp = new PasswordResetPage();
        prp.setEmail(email);
        prp.clickResetPasswordButton();
        Assert.assertEquals(lp.getErrorBubbleText(), error, "message");
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
            LoginPage lp = new LoginPage(Integer.parseInt(global.getProperty("explicit_wait_lp")));
            executeJavaScript("window.open('" + global.getProperty("url_yandex") + "','/');");
            switchTo().window(1);

            YandexLoginPage yandexLogin = new YandexLoginPage(Integer.parseInt(global.getProperty("explicit_wait_yandex")));
            yandexLogin.loginToYandex(test.getProperty("login_yandex"), test.getProperty("password_yandex"));

            YandexLiteMailPage yandex = new YandexLiteMailPage(Integer.parseInt(global.getProperty("explicit_wait_yandex")));
            //если есть что удалить удалим, если нет пойдем дальше
            if (yandex.isSelectAllCheckbox() == true) {
                yandex.clickSelectAllCheckbox();
                yandex.clickDeleteButton();
            }

            switchTo().window(0);
            lp.clickForgotPasswordLink();

            PasswordResetPage prp = new PasswordResetPage();
            prp.setEmail(test.getProperty("account_exist_email_yandex"));
            prp.clickResetPasswordButton();
            switchTo().window(1);

            yandex.waitEmail(Integer.parseInt(global.getProperty("number_retry_yandex")),
                    Integer.parseInt(global.getProperty("sleep_between_retry_yandex")));
            yandex.clickResetPasswordEmail();
            String restore = yandex.getPasswordRecoveryLinkText();
            yandex.logout();

            executeJavaScript("window.close();");
            switchTo().window(0);
            open(restore);

            ChangePasswordPage cpp = new ChangePasswordPage();
            cpp.setNewPassword(password);
            cpp.clickChangePasswordButton();

            Assert.assertEquals(lp.getErrorBubbleText(), error, "message");
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
            LoginPage lp = new LoginPage(Integer.parseInt(global.getProperty("explicit_wait_lp")));
            lp.clickForgotPasswordLink();

            PasswordResetPage prp = new PasswordResetPage();
            prp.setEmail(email);
            prp.clickResetPasswordButton();
            Assert.assertEquals(lp.getErrorBubbleText(), error, "message");
    }


    @AfterMethod
    public void tearDown() {
        clearBrowserCache();
    }

}


