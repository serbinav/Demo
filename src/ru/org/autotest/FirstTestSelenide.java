package ru.org.autotest;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.By;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.WebDriverRunner.clearBrowserCache;

/**
 * Created by makenshi on 4/3/18.
 */
public class FirstTestSelenide {

    public static final String CREATENAME = "acdc";
    public static final String CREATEEMAIL = "makenshi+10@ecwid.com";
    public static final String CREATEPASSWORD = "12345678";
    public static final String LOGINEMAIL = "makenshi@ecwid.com";
    public static final String LOGINPASSWORD = "12345678";

    public static final String EMAIL = "Please enter a valid email address";
    public static final String PASSWORD = "Please enter your password";
    public static final String NOACCOUNT = "No accounts associated with this email. " +
            "If you don’t remember your account email, please contact us at login-issues@ecwid.com.";
    public static final String NAME = "Please enter your first and last names";
    public static final String SIXLETTER = "Your password should be at least 6 characters long";
    public static final String ACCOUNTUSED = "We already have an account associated with this email. " +
            "You can sign in into your existing account or create a new one using another email.";

    @BeforeClass
    public static void setup() {
        Configuration.baseUrl = "https://my41243yandex.sandbox.ecwid.com";
        Configuration.browser = "chrome";
        //"chrome", "ie", "firefox")
    }

    @Test
    public void userLogin() {
        open("/");
        $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).shouldBe(visible);

        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='email']"))
                .val(LOGINEMAIL);

        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='password']"))
                .val(LOGINPASSWORD);

        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button")).click();

        $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).shouldBe(disappear);
        $(By.xpath("//div[@class='loading-panel' and not(contains(@style,'display: none'))]")).shouldBe(appear);
        $(By.xpath("//div[@class='loading-panel' and not(contains(@style,'display: none'))]")).shouldBe(disappear);
        $(By.xpath("//div[@class='menu']")).shouldBe(visible);
        $(By.xpath("//h1[@class='settings-page__title']")).shouldBe(visible);

        // TODO возможно данный вариант выхода из аккаунта "как из пушки по воробьям" и нужно конкретизировать
        clearBrowserCache();
        open("/");
        // TODO если тест упал надо удалять все куки и делать рефреш, переходить на дефолтный урл
    }

    @Test
    public void userCreate() {
        open("/");
        $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).shouldBe(visible);
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//p[last()]//a")).click();

        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//input[@name='name']"))
                .val(CREATENAME);

        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//input[@name='email']"))
                .val(CREATEEMAIL);

        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//input[@name='password']"))
                .val(CREATEPASSWORD);

        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//button")).click();

        // TODO если аккаунт зареган, то тут падаем
        $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).shouldBe(disappear);
        $(By.xpath("//div[@class='loading-panel' and not(contains(@style,'display: none'))]")).shouldBe(appear);
        $(By.xpath("//div[@class='loading-panel' and not(contains(@style,'display: none'))]")).shouldBe(disappear);
        $(By.xpath("//a[@class='skip']")).click();

        String url = WebDriverRunner.url();
        String[] parts = url.split("#");
        StringBuilder deleteUrl = new StringBuilder(parts[0] + "#profile");
        open(deleteUrl.toString());

        $(By.xpath("//a[@class='gwt-Anchor btn btn-default btn-medium delete-account-button']")).click();
        $(By.xpath("//div[@class='main-popup__container']//button[@class='btn btn-medium btn-primary']")).click();

        open("/");
        // TODO если тест упал надо удалять все куки и делать рефреш, переходить на дефолтный урл
    }

    @Test
    public void userLoginBubbleEmail() {
        open("/");
        $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).shouldBe(visible);
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button")).click();

        Assert.assertEquals(
                $(By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"))
                        .shouldBe(visible).getText(), EMAIL);
    }

    @Test
    public void userLoginBubblePassword() {
        open("/");
        $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).shouldBe(visible);
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='email']"))
                .setValue("test@ecwid.com");
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button")).click();

        Assert.assertEquals(
                $(By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"))
                        .shouldBe(visible).getText(), PASSWORD);
    }

    //
    @Test
    public void userCreateBubbleName() {
        open("/");
        $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).shouldBe(visible);
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//p[last()]//a")).click();
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//button")).click();

        Assert.assertEquals(
                $(By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"))
                        .shouldBe(visible).getText(), NAME);
    }

    @Test
    public void userCreateBubbleEmail() {
        open("/");
        $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).shouldBe(visible);
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//p[last()]//a")).click();
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//input[@name='name']"))
                .setValue("12345");
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//button")).click();

        Assert.assertEquals(
                $(By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"))
                        .shouldBe(visible).getText(), EMAIL);
    }

    @Test
    public void userCreateBubblePassword() {
        open("/");
        $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).shouldBe(visible);
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//p[last()]//a")).click();
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//input[@name='name']"))
                .setValue("12345");
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//input[@name='email']"))
                .setValue("test@ecwid.com");
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//button")).click();

        Assert.assertEquals(
                $(By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"))
                        .shouldBe(visible).getText(), SIXLETTER);
    }

    @AfterClass
    public void clearCache() {
        clearBrowserCache();
    }
}