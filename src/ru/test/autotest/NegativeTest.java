package ru.test.autotest;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.utils.PropertiesStream;
import ru.utils.TestUtils;

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
        };
    }

    @Test(dataProvider = "parseEmailPasswordData")
    public void userLoginBubble(String email, String password, String error) {
        open("/");
        $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).waitUntil(Condition.visible, Integer.parseInt(test.getProperty("explicit_wait_lp")));

        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='email']")).
                setValue(email);
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='password']")).
                setValue(password);
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button")).click();
        Assert.assertEquals(
                $(By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"))
                        .shouldBe(Condition.visible).getText(), error);
    }

    //-----------------------------------------------------------------------------------------------------------------------------
    @Test
    public void userLoginBubbleEsc() {
        open("/");
        // подправил ожидание на 10 секунд
        $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).waitUntil(Condition.visible, Integer.parseInt(test.getProperty("explicit_wait_lp")));
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button")).click();
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='email']")).pressEscape();
        Assert.assertEquals(
                $(By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"))
                        .exists(), false);
    }

    //-----------------------------------------------------------------------------------------------------------------------------
    @Test
    public void userLoginBubbleEnter() {
        open("/");
        // подправил ожидание на 10 секунд
        $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).waitUntil(Condition.visible, Integer.parseInt(test.getProperty("explicit_wait_lp")));
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button")).pressEnter();
        Assert.assertEquals(
                $(By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"))
                        .shouldBe(Condition.visible).getText(), test.getProperty("email"));
    }

    //-----------------------------------------------------------------------------------------------------------------------------
    // долгое ожидание поэтому не добавил в parseEmailPasswordData
    @Test
    public void userLoginBubbleNoCorrectPass() {
        open("/");
        // подправил ожидание на 10 секунд
        $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).waitUntil(Condition.visible, Integer.parseInt(test.getProperty("explicit_wait_lp")));
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='email']")).setValue(
                test.getProperty("account_exist_email"));
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='password']")).setValue(
                utils.generateRandomString(Integer.parseInt(test.getProperty("password_random_lenght"))));
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button")).click();
        Assert.assertEquals(
                // подправил тут ожидание пока на 10 секунд, тут оно может быть дольше чем в других тестах
                $(By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"))
                        .waitUntil(Condition.visible, 10000).getText(), test.getProperty("password_no_correct"));
    }
    //-----------------------------------------------------------------------------------------------------------------------------

    @Test //email
    public void userLoginBubbleEmptyEmail() {
        open("/");
        $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).waitUntil(Condition.visible, Integer.parseInt(test.getProperty("explicit_wait_lp")));
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button")).click();
        Assert.assertEquals(
                $(By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"))
                        .shouldBe(Condition.visible).getText(), test.getProperty("email"));
    }

    //-----------------------------------------------------------------------------------------------------------------------------
    @Test
    public void userLoginBrowserRefresh() {
        open("/");
        $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).waitUntil(Condition.visible, Integer.parseInt(test.getProperty("explicit_wait_lp")));
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='email']")).
                setValue(test.getProperty("account_exist_email"));
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='password']")).
                setValue(test.getProperty("account_exist_password"));
        refresh();
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button")).click();
        Assert.assertEquals(
                $(By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"))
                        .shouldBe(Condition.visible).getText(), test.getProperty("email"));
    }

    //-----------------------------------------------------------------------------------------------------------------------------


    @DataProvider
    public Object[][] parseEmailData() {
        return new Object[][]{
                //case userRestorePasswordEmailWithSpace
                {utils.composeEmail(" ", test.getProperty("email_template")),
                        test.getProperty("email")},
                //case userRestorePasswordMailRandomString
                {utils.generateRandomString(Integer.parseInt(test.getProperty("email_random_lenght"))),
                        test.getProperty("email")},
                //case userRestorePasswordVeryLongMail
                {utils.composeEmail(utils.generateRandomString(Integer.parseInt(test.getProperty("very_longe_mail")) - test.getProperty("email_template").length()), test.getProperty("email_template")),
                        test.getProperty("email")},
                //case userRestorePasswordArabicLetters
                {test.getProperty("account_error_email_saudi_arabia"),
                        test.getProperty("email")},
                //case userRestorePasswordIndianLetters
                {test.getProperty("account_error_email_india"),
                        test.getProperty("email")},
                //case userRestorePasswordChineseLetters
                {test.getProperty("account_error_email_china"),
                        test.getProperty("email")},
                //case userRestorePasswordOtherLetters
                {test.getProperty("account_error_email_south_korea"),
                        test.getProperty("email")},
                //case userRestorePasswordNoAssociatedAcc
                {utils.composeEmail(utils.generateRandomString(Integer.parseInt(test.getProperty("email_random_lenght"))),
                        test.getProperty("email_template")),
                        test.getProperty("no_account")},
        };
    }

    @Test(dataProvider = "parseEmailData")
    public void userLoginRestorePasswordBadEmail(String email, String error) {
        open("/");
        $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).waitUntil(Condition.visible,
                Integer.parseInt(test.getProperty("explicit_wait_lp")));

        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//p[1]//a[1]")).click();

        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_3']//input[@name='email']")).
                setValue(email);
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_3']//button")).click();
        Assert.assertEquals(
                $(By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"))
                        .shouldBe(Condition.visible).getText(), error);
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

            $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).waitUntil(Condition.visible,
                    Integer.parseInt(test.getProperty("explicit_wait_lp")));

            executeJavaScript("window.open('" + test.getProperty("url_yandex") + "','/');");

            switchTo().window(1);

            $(By.xpath("//div[@class='Footer-Copyright']")).waitUntil(Condition.visible,
                    Integer.parseInt(test.getProperty("explicit_wait_yandex")));
            $(By.xpath("//a[contains(@class,'HeadBanner-Button-Enter')]")).click();
            $(By.xpath("//input[@class='passport-Input-Controller' and @name='login']")).
                    setValue(test.getProperty("login_yandex"));
            $(By.xpath("//input[@class='passport-Input-Controller' and @name='passwd']")).
                    setValue(test.getProperty("password_yandex"));
            $(By.xpath("//span[@class='passport-Button-Text']")).click();
            $(By.xpath("//div[contains(@class,'mail-Toolbar-Item_main-select-all')]//span[@class='checkbox_view']")).click();
            $(By.xpath("//span[contains(@class,'js-toolbar-item-title-delete')]")).click();

            switchTo().window(0);
            $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//p[1]//a[1]")).click();

            $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_3']//input[@name='email']")).
                    setValue(test.getProperty("account_exist_email_yandex"));
            $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_3']//button")).click();
            switchTo().window(1);

            for (Integer i = 0; i < Integer.parseInt(test.getProperty("number_retry_yandex")); i++) {
                sleep(1000);
                refresh();
                boolean visible =
                        $(By.xpath("//div[@class='mail-MessageSnippet-Content']/span[contains(., 'Reset your Ecwid password')]")).exists();
                if (visible == true) {
                    break;
                }
            }
            $(By.xpath("//div[@class='mail-MessageSnippet-Content']/span[contains(., 'Reset your Ecwid password')]")).click();
            String restore = $(By.xpath("//div[contains(@class,'mail-Message-Body-Content_plain')]//a[last()]")).getAttribute("href");
            $(By.xpath("//span[contains(@class, 'mail-User-Avatar')]")).click();

            for (Integer i = 0; i < Integer.parseInt(test.getProperty("number_retry_yandex")); i++) {
                $(By.xpath("//span[contains(@class, 'mail-User-Avatar')]")).click();
                boolean visible =
                        $(By.xpath("//div[@class='_nb-popup-content']//div[@class='b-mail-dropdown__item']/a[contains(@href, 'action=logout')]")).exists();
                if (visible == true) {
                    break;
                }
                sleep(1000);
            }
            $(By.xpath("//div[@class='_nb-popup-content']//div[@class='b-mail-dropdown__item']/a[contains(@href, 'action=logout')]")).click();

            executeJavaScript("window.close();");
            switchTo().window(0);
            open(restore);
            $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_4']//input[@name='password']")).
                    setValue(password);
            $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_4']//button")).click();
            Assert.assertEquals(
                    $(By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"))
                            .shouldBe(Condition.visible).getText(), error);
        } finally {
            close();
        }
    }

    @AfterMethod
    public void tearDown() {
        clearBrowserCache();
    }

}
