package ru.test.autotest;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.By;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.utils.PropertiesStream;
import ru.utils.TestUtils;

import java.util.Properties;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.clearBrowserCache;

/**
 * Created by makenshi on 4/3/18.
 */
public class FirstTestSelenide {

    private static Properties test;
    private static TestUtils utils;

    @BeforeClass
    public static void setUp() {
        utils = new TestUtils();
        Properties prop = new PropertiesStream("config/properties.ini","UTF-8").getProperties();

        Configuration.baseUrl = prop.getProperty("site_name");
        Configuration.browser = prop.getProperty("browser_name");

        test = new PropertiesStream("config/test.ini","UTF-8").getProperties();
    }

    //TODO подумать над тем что некоторые тесты могут падать при условии что аккаунты не зареганы
    // 8 из 26
    //-----------------------------------------------------------------------------------------------------------------------------

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
                {"      "+ test.getProperty("account_exist_email"),
                        test.getProperty("account_exist_password"),
                        true},
                // TODO в FF работает подругому
                //case userLoginSpacesAfterLogin
                {test.getProperty("account_exist_email")+"      ",
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
        try{
            $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).waitUntil(Condition.visible,10000);

            $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='email']"))
                    .val(email);

            $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='password']"))
                    .val(password);

            $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button")).click();

            $(By.xpath("//div[@class='loading-panel' and not(contains(@style,'display: none'))]")).waitUntil(Condition.appear,10000);
            $(By.xpath("//div[@class='menu']")).shouldBe(Condition.visible);
            $(By.xpath("//h1[@class='settings-page__title']")).shouldBe(Condition.visible);

            String url = WebDriverRunner.url();
            String[] parts = url.split("#");
            StringBuilder deleteUrl = new StringBuilder(parts[0] + "#profile");
            open(deleteUrl.toString());

            $(By.xpath("//div[@class='ProfileView']//div[@class='Title']//span[@class='gwt-InlineHTML']")).shouldBe(Condition.visible);

            Assert.assertEquals(
                    $$(By.xpath("//div[@class='backend-TitledPanel-content']//input[@class='gwt-TextBox']")).
                            findBy(Condition.value(email)).exists(),bool);
            $(By.xpath("//a[@class='horizontal-icolink icolink-append']//div[@class='multiadmin-profile']")).click();
            $(By.xpath("//div[@class='store-profile-footer']//a")).click();
        }
        finally {
            //TODO еще подумать, но этот вариант рабочий
            clearBrowserCache();
        }
    }

    //-----------------------------------------------------------------------------------------------------------------------------
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
                //case userLoginBubbleEmailEmpty
                {utils.composeEmail(" ",test.getProperty("email_template")),
                        "",
                        test.getProperty("email")},
                //case userLoginBubbleMailRandomString
                {utils.generateRandomString(Integer.parseInt(test.getProperty("email_random_lenght"))),
                        "",
                        test.getProperty("email")},
                //case userLoginBubbleVeryLongMail
                {utils.composeEmail(utils.generateRandomString(Integer.parseInt(test.getProperty("very_longe_mail"))-test.getProperty("email_template").length()), test.getProperty("email_template")),
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
        $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).waitUntil(Condition.visible, 10000);

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
    public void userLoginBorder() {
        open("/");
        // подправил ожидание на 10 секунд
        $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).waitUntil(Condition.visible,10000);
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='email']")).click();
        $(By.xpath("//div[@class='field field--large field--focus']")).exists();
        Assert.assertEquals($(By.xpath(
                "//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//div[@class='field field--large field--focus']" +
                        "//input[@name='email']")).exists(), true);
    }
    //-----------------------------------------------------------------------------------------------------------------------------
    @Test
    public void userLoginBubbleTab() {
        open("/");
        // подправил ожидание на 10 секунд
        $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).waitUntil(Condition.visible,10000);
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='email']")).pressTab();
        Assert.assertEquals($(By.xpath(
                "//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//div[@class='field field--large field--focus']" +
                        "//input[@type='password']")).exists(), true);
    }
    //-----------------------------------------------------------------------------------------------------------------------------
    @Test
    public void userLoginBubbleEsc() {
        open("/");
        // подправил ожидание на 10 секунд
        $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).waitUntil(Condition.visible,10000);
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
        $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).waitUntil(Condition.visible,10000);
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
        $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).waitUntil(Condition.visible,10000);
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='email']")).setValue(
                test.getProperty("account_exist_email"));
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='password']")).setValue(
                utils.generateRandomString(Integer.parseInt(test.getProperty("password_random_lenght"))));
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button")).click();
        Assert.assertEquals(
                // подправил тут ожидание пока на 10 секунд, тут оно может быть дольше чем в других тестах
                $(By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"))
                        .waitUntil(Condition.visible,10000).getText(), test.getProperty("password_no_correct"));
    }
    //-----------------------------------------------------------------------------------------------------------------------------

    @Test //email
    public void userLoginBubbleMailWithSpace() {
        open("/");
        $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).waitUntil(Condition.visible, 10000);
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button")).click();
        Assert.assertEquals(
                $(By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"))
                        .shouldBe(Condition.visible).getText(), test.getProperty("email"));
    }

    //-----------------------------------------------------------------------------------------------------------------------------
    @Test
    public void userLoginBrowserRefresh() {
        open("/");
        $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).waitUntil(Condition.visible, 10000);
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

    @Test
    public void userLoginBrowserNewWindow() {
        open("/");
        try{
            $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).waitUntil(Condition.visible,10000);

            $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='email']"))
                    .val(test.getProperty("account_exist_email"));
            $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='password']"))
                    .val(test.getProperty("account_exist_password"));
            $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button")).click();
            $(By.xpath("//div[@class='loading-panel' and not(contains(@style,'display: none'))]")).waitUntil(Condition.appear,10000);
            $(By.xpath("//div[@class='menu']")).shouldBe(Condition.visible);
            $(By.xpath("//h1[@class='settings-page__title']")).shouldBe(Condition.visible);

            executeJavaScript("window.open('"+WebDriverRunner.url()+"','/');");
            switchTo().window(1);
            $(By.xpath("//div[@class='loading-panel' and not(contains(@style,'display: none'))]")).waitUntil(Condition.appear,10000);
            $(By.xpath("//div[@class='menu']")).shouldBe(Condition.visible);
            Assert.assertEquals(
                    $(By.xpath("//h1[@class='settings-page__title']")).shouldBe(Condition.visible).exists(),true);

//TODO закрывать вторую вкладку
        }
        finally {
            //TODO еще подумать, но этот вариант рабочий
            clearBrowserCache();
        }
    }

    @AfterClass
    public void tearDown() {
        clearBrowserCache();
    }

    @Test(enabled = false)
    public void userLoginBrowserPasswordRecovery() {
//        switchTo().window(newWindow);
//        System.out.println("New window title: " + WebDriverRunner.url());
//        switchTo().window(originalWindow);
//        System.out.println("Old window title: " + WebDriverRunner.url());
//        ExecuteScript("window.open('your url','_blank');");
//        executeJavaScript("alert('"+WebDriverRunner.url()+"');");
    }
}
