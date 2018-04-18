package ru.test.autotest;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.*;

import ru.utils.PropertiesStream;
import ru.utils.TestUtils;

import java.util.Properties;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;
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
        Properties prop = new PropertiesStream("config/properties.ini","UTF-8").getProperties();

        Configuration.baseUrl = prop.getProperty("site_name");
        Configuration.browser = prop.getProperty("browser_name");

        test = new PropertiesStream("config/test.ini","UTF-8").getProperties();
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
            $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).waitUntil(Condition.visible,
                    Integer.parseInt(test.getProperty("explicit_wait_lp")));

            $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='email']"))
                    .val(email);

            $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='password']"))
                    .val(password);

            $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button")).click();

            $(By.xpath("//div[@class='loading-panel' and not(contains(@style,'display: none'))]")).waitUntil(Condition.appear,Integer.parseInt(test.getProperty("explicit_wait_cp")));
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

    //-----------------------------------------------------------------------------------------------------------------------------
    @Test
    public void userLoginBorder() {
        open("/");
        // подправил ожидание на 10 секунд
        $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).waitUntil(Condition.visible,Integer.parseInt(test.getProperty("explicit_wait_lp")));
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
        $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).waitUntil(Condition.visible,Integer.parseInt(test.getProperty("explicit_wait_lp")));
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='email']")).pressTab();
        Assert.assertEquals($(By.xpath(
                "//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//div[@class='field field--large field--focus']" +
                        "//input[@type='password']")).exists(), true);
    }
    //-----------------------------------------------------------------------------------------------------------------------------

    @Test
    public void userLoginBrowserNewWindow() {
        open("/");
            $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).waitUntil(Condition.visible,Integer.parseInt(test.getProperty("explicit_wait_lp")));

            $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='email']"))
                    .val(test.getProperty("account_exist_email"));
            $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='password']"))
                    .val(test.getProperty("account_exist_password"));
            $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button")).click();
            $(By.xpath("//div[@class='loading-panel' and not(contains(@style,'display: none'))]")).waitUntil(Condition.appear,Integer.parseInt(test.getProperty("explicit_wait_cp")));
            $(By.xpath("//div[@class='menu']")).shouldBe(Condition.visible);
            $(By.xpath("//h1[@class='settings-page__title']")).shouldBe(Condition.visible);
            executeJavaScript("window.open('"+WebDriverRunner.url()+"','/');");
            switchTo().window(1);
            $(By.xpath("//div[@class='loading-panel' and not(contains(@style,'display: none'))]")).waitUntil(Condition.appear,Integer.parseInt(test.getProperty("explicit_wait_cp")));
            $(By.xpath("//div[@class='menu']")).shouldBe(Condition.visible);
            Assert.assertEquals(
                    $(By.xpath("//h1[@class='settings-page__title']")).shouldBe(Condition.visible).exists(),true);
            executeJavaScript("window.close();");
            switchTo().window(0);
    }

    @Test
    public void userLoginBrowserBack() {
        open("/");
            $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).waitUntil(Condition.visible,Integer.parseInt(test.getProperty("explicit_wait_lp")));

            $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='email']"))
                    .val(test.getProperty("account_exist_email"));

            $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='password']"))
                    .val(test.getProperty("account_exist_password"));

            $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button")).click();

            $(By.xpath("//div[@class='loading-panel' and not(contains(@style,'display: none'))]")).waitUntil(Condition.appear,Integer.parseInt(test.getProperty("explicit_wait_cp")));
            $(By.xpath("//div[@class='menu']")).shouldBe(Condition.visible);
            $(By.xpath("//h1[@class='settings-page__title']")).shouldBe(Condition.visible);
            back();
            Assert.assertEquals(
                    $(By.xpath("//h1[@class='settings-page__title']")).shouldBe(Condition.visible).exists(),true);
    }

    @Test
    public void userLoginSeveralAttemptsIncorrectly() {
        open("/");
            $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).waitUntil(Condition.visible, Integer.parseInt(test.getProperty("explicit_wait_lp")));
            $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='email']")).setValue(
                    test.getProperty("account_exist_email"));
            $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='password']")).setValue(
                    utils.generateRandomString(Integer.parseInt(test.getProperty("password_random_lenght"))));
            for (Integer i = 0; i < Integer.parseInt(test.getProperty("number_attempts_incorrectly_login")); i++) {
                $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button")).click();
                Assert.assertEquals(
                        $(By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"))
                                .waitUntil(Condition.visible,5000).getText(), test.getProperty("password_no_correct"));
            }
            $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='password']")).
                    setValue(test.getProperty("account_exist_password"));
            $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button")).click();
            $(By.xpath("//div[@class='loading-panel' and not(contains(@style,'display: none'))]")).waitUntil(Condition.appear,Integer.parseInt(test.getProperty("explicit_wait_cp")));
            $(By.xpath("//div[@class='menu']")).shouldBe(Condition.visible);
            Assert.assertEquals(
                    $(By.xpath("//h1[@class='settings-page__title']")).shouldBe(Condition.visible).exists(),true);
    }

    @Test
    public void userLoginKeepSignedIn() {
        open("/");
            $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).waitUntil(Condition.visible,Integer.parseInt(test.getProperty("explicit_wait_lp")));

            $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='email']"))
                    .val(test.getProperty("account_exist_email"));
            $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='password']"))
                    .val(test.getProperty("account_exist_password"));
            $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//span[@class='gwt-CheckBox']")).click();
            $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button")).click();
            $(By.xpath("//div[@class='loading-panel' and not(contains(@style,'display: none'))]")).waitUntil(Condition.appear,Integer.parseInt(test.getProperty("explicit_wait_cp")));
            $(By.xpath("//div[@class='menu']")).shouldBe(Condition.visible);
            $(By.xpath("//h1[@class='settings-page__title']")).shouldBe(Condition.visible);
            Assert.assertEquals(
                    $(By.xpath("//h1[@class='settings-page__title']")).shouldBe(Condition.visible).exists(),true);
    }

    @Test
    public void userLoginRestorePassword()
    {
        open("/");
        $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).waitUntil(Condition.visible,
                Integer.parseInt(test.getProperty("explicit_wait_lp")));

        executeJavaScript("window.open('"+ test.getProperty("url_yandex") +"','/');");
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
            refresh();
            sleep(1000);
            boolean visible =
                    $(By.xpath("//div[@class='mail-MessageSnippet-Content']/span[contains(., 'Reset your Ecwid password')]")).exists();
            if (visible == true)
            {
                break;
            }
        }
        $(By.xpath("//div[@class='mail-MessageSnippet-Content']/span[contains(., 'Reset your Ecwid password')]")).click();
        String restore = $(By.xpath("//div[contains(@class,'mail-Message-Body-Content_plain')]//a[last()]")).getAttribute("href");
        executeJavaScript("window.close();");
        switchTo().window(0);
        open(restore);
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_4']//input[@name='password']")).
                setValue(test.getProperty("account_exist_password_yandex"));
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_4']//button")).click();
        $(By.xpath("//div[@class='loading-panel' and not(contains(@style,'display: none'))]")).waitUntil(Condition.appear,Integer.parseInt(test.getProperty("explicit_wait_cp")));
        $(By.xpath("//div[@class='menu']")).shouldBe(Condition.visible);
        Assert.assertEquals(
                    $(By.xpath("//h1[@class='settings-page__title']")).shouldBe(Condition.visible).exists(),true);
        close();
    }

    @AfterMethod
    public void tearDown(){
        clearBrowserCache();
    }

}

