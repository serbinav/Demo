package ru.org.autotest;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.impl.WebElementSource;
import org.openqa.selenium.By;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.*;
import java.util.Properties;
import java.util.Random;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.WebDriverRunner.clearBrowserCache;

/**
 * Created by makenshi on 4/3/18.
 */
public class FirstTestSelenide {

    @Test(enabled = false)
    public void userLogin() {
        open("/");
        $(By.xpath(anchor.getProperty("Login1"))).shouldBe(visible);

        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='email']"))
                .val(test.getProperty("LOGINEMAIL"));

        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='password']"))
                .val(test.getProperty("LOGINPASSWORD"));

        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button")).click();

        $(By.xpath("//div[@class='loading-panel' and not(contains(@style,'display: none'))]")).shouldBe(appear);
        $(By.xpath("//div[@class='loading-panel' and not(contains(@style,'display: none'))]")).shouldBe(disappear);
        $(By.xpath("//div[@class='menu']")).shouldBe(visible);
        $(By.xpath("//h1[@class='settings-page__title']")).shouldBe(visible);

        // TODO возможно данный вариант выхода из аккаунта "как из пушки по воробьям" и нужно конкретизировать
        clearBrowserCache();
        open("/");
        // TODO если тест упал надо удалять все куки и делать рефреш, переходить на дефолтный урл
    }

    @Test(enabled = false)
    public void userCreate() {
        open("/");
        $(By.xpath(anchor.getProperty("Login1"))).shouldBe(visible);
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//p[last()]//a")).click();

        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//input[@name='name']"))
                .val(test.getProperty("CREATENAME"));

        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//input[@name='email']"))
                .val(test.getProperty("CREATEEMAIL"));

        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//input[@name='password']"))
                .val(test.getProperty("CREATEPASSWORD"));

        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//button")).click();

/*
        if($(By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']")).
                shouldBe(visible).getText().compareTo(test.getProperty("ACCOUNTUSED")) == 0)
        {
            System.out.println("КУКУ");
        }
        else{}
*/

        $(By.xpath(anchor.getProperty("Login1"))).shouldBe(disappear);
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

//====================================================================================================================================

    private static Properties test;
    private static Properties anchor;

    @BeforeClass
    public static void setup() {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(new File("config/properties.ini")));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            //e.printStackTrace();
            System.exit(1);
        }
        Configuration.baseUrl = prop.getProperty("site_name");
        Configuration.browser = prop.getProperty("browser_name");

        test = new Properties();
        InputStream stream = null;
        InputStreamReader reader = null;
        try {
            try {
                stream = new FileInputStream(new File("config/test.ini"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                reader = new InputStreamReader(stream,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            test = new Properties();
            try {
                test.load(reader);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        anchor = new Properties();
        try {
            anchor.load(new FileInputStream(new File("config/anchor.ini")));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            //e.printStackTrace();
            System.exit(1);
        }
    }

    protected String getRandomString(int PHONE_NUMBER_LENGTH) {
        String s = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        StringBuffer RandomString = new StringBuffer();

        for (int i = 0; i < PHONE_NUMBER_LENGTH; i++) {
            RandomString.append(s.charAt(new Random().nextInt(s.length())));
        }
        return RandomString.toString();
    }

    protected String getRandomEmail(int PHONE_NUMBER_LENGTH, String Example) {
        String randomPart = getRandomString(PHONE_NUMBER_LENGTH);
        return Example.replaceAll("@", "+"+randomPart+"@");
    }


    @Test
    public void userLoginBubbleEmailEmpty() {
        open("/");
        $(By.xpath(anchor.getProperty("Login1"))).shouldBe(visible);
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button")).click();

        Assert.assertEquals(
                $(By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"))
                        .shouldBe(visible).getText(), test.getProperty("Email"));
    }

    @Test
    public void userLoginBubblePasswordEmpty() {
        open("/");
        $(By.xpath(anchor.getProperty("Login1"))).shouldBe(visible);
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='email']")).setValue(
                getRandomEmail(Integer.parseInt(test.getProperty("EmailRandomLenght")),test.getProperty("EmailTemplate")));
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button")).click();
        Assert.assertEquals(
                $(By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"))
                        .shouldBe(visible).getText(), test.getProperty("Password"));
    }

    @Test
    public void userCreateBubbleNameEmpty() {
        open("/");
        $(By.xpath(anchor.getProperty("Login1"))).shouldBe(visible);
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//p[last()]//a")).click();
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//button")).click();

        Assert.assertEquals(
                $(By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"))
                        .shouldBe(visible).getText(), test.getProperty("Name"));
    }

    @Test
    public void userCreateBubbleEmailEmpty() {
        open("/");
        $(By.xpath(anchor.getProperty("Login1"))).shouldBe(visible);
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//p[last()]//a")).click();
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//input[@name='name']")).setValue(
                getRandomString(Integer.parseInt(test.getProperty("RandomNameLenght"))));
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//button")).click();

        Assert.assertEquals(
                $(By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"))
                        .shouldBe(visible).getText(), test.getProperty("Email"));
    }

    @Test (description = "OK")
    public void userCreateBubblePasswordEmpty() {
        open("/");
        $(By.xpath(anchor.getProperty("Login1"))).shouldBe(visible);
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//p[last()]//a")).click();
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//input[@name='name']")).setValue(
                getRandomString(Integer.parseInt(test.getProperty("RandomNameLenght"))));
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//input[@name='email']")).setValue(
                getRandomEmail(Integer.parseInt(test.getProperty("EmailRandomLenght")),test.getProperty("EmailTemplate")));
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//button")).click();

        Assert.assertEquals(
                $(By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"))
                        .shouldBe(visible).getText(), test.getProperty("Sixletter"));
    }

    @Test
    public void userLoginBubbleNoAssociatedAcc() {
        open("/");
        $(By.xpath(anchor.getProperty("Login1"))).shouldBe(visible);
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='email']")).setValue(
                getRandomEmail(Integer.parseInt(test.getProperty("EmailRandomLenght")),test.getProperty("EmailTemplate")));
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='password']")).setValue(
                getRandomString(Integer.parseInt(test.getProperty("PasswordRandomLenght"))));
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button")).click();
        Assert.assertEquals(
                $(By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"))
                        .shouldBe(visible).getText(), test.getProperty("Noaccount"));
    }

    @Test
    public void userCreateBubbleAccExist() {
        open("/");
        $(By.xpath(anchor.getProperty("Login1"))).shouldBe(visible);
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//p[last()]//a")).click();
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//input[@name='name']")).setValue(
                getRandomString(Integer.parseInt(test.getProperty("RandomNameLenght"))));
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//input[@name='email']")).setValue(
                test.getProperty("Accountexistemail"));
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//input[@name='password']")).setValue(
                test.getProperty("Accountexistpassword"));
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//button")).click();
        Assert.assertEquals(
                $(By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"))
                        .shouldBe(visible).getText(), test.getProperty("Accountused"));
    }

    @Test
    public void userLoginBubbleNoCorrectPass() {
        open("/");
        $(By.xpath(anchor.getProperty("Login1"))).shouldBe(visible);
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='email']")).setValue(
                test.getProperty("Accountexistemail"));
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='password']")).setValue(
                getRandomString(Integer.parseInt(test.getProperty("PasswordRandomLenght"))));
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button")).click();
        Assert.assertEquals(
                $(By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"))
                        .shouldBe(visible).getText(), test.getProperty("Passwordnocorrect"));
    }

    @AfterClass
    public void clearCache() {
        clearBrowserCache();
    }
}