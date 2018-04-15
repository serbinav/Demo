package ru.test.autotest;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
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

    @Test
    public void userLoginSeveralAttemptsIncorrectly() {
            open("/");
        try{
            $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).waitUntil(Condition.visible, 10000);
            $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='email']")).setValue(
                    test.getProperty("account_exist_email"));
            $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='password']")).setValue(
                    utils.generateRandomString(Integer.parseInt(test.getProperty("password_random_lenght"))));
            for (Integer i = 0; i < Integer.parseInt(test.getProperty("number_attempts_incorrectly_login")); i++) {
                $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button")).click();
                Assert.assertEquals(
                    $(By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"))
                            .shouldBe(Condition.visible).getText(), test.getProperty("password_no_correct"));
            }
            $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='password']")).
                    setValue(test.getProperty("account_exist_password"));
            $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button")).click();
            $(By.xpath("//div[@class='loading-panel' and not(contains(@style,'display: none'))]")).waitUntil(Condition.appear,10000);
            $(By.xpath("//div[@class='menu']")).shouldBe(Condition.visible);
            Assert.assertEquals(
                    $(By.xpath("//h1[@class='settings-page__title']")).shouldBe(Condition.visible).exists(),true);
        }
            finally {
            //TODO еще подумать, но этот вариант рабочий
            clearBrowserCache();
        }

    }
    //-----------------------------------------------------------------------------------------------------------------------------

    @Test
    public void userLoginBrowserBack() {
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
            back();
            Assert.assertEquals(
                    $(By.xpath("//h1[@class='settings-page__title']")).shouldBe(Condition.visible).exists(),true);
        }
        finally {
            //TODO еще подумать, но этот вариант рабочий
            clearBrowserCache();
        }
    }
    //-----------------------------------------------------------------------------------------------------------------------------
    @Test
    public void userLoginKeepSignedIn() {
        open("/");
        try{
            $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).waitUntil(Condition.visible,10000);

            $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='email']"))
                    .val(test.getProperty("account_exist_email"));
            $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='password']"))
                    .val(test.getProperty("account_exist_password"));
            $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//span[@class='gwt-CheckBox']")).click();
            $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button")).click();
            $(By.xpath("//div[@class='loading-panel' and not(contains(@style,'display: none'))]")).waitUntil(Condition.appear,10000);
            $(By.xpath("//div[@class='menu']")).shouldBe(Condition.visible);
            $(By.xpath("//h1[@class='settings-page__title']")).shouldBe(Condition.visible);
            Assert.assertEquals(
                    $(By.xpath("//h1[@class='settings-page__title']")).shouldBe(Condition.visible).exists(),true);

        }
        finally {
            //TODO еще подумать, но этот вариант рабочий
            clearBrowserCache();
        }
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

    @AfterClass
    public void tearDown() {
        clearBrowserCache();
    }
}

