package ru.org.autotest;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.By;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.awt.datatransfer.*;
import java.io.*;
import java.util.Properties;
import java.util.Random;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.WebDriverRunner.clearBrowserCache;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

/**
 * Created by makenshi on 4/3/18.
 */
public class FirstTestSelenide {

    private static ClipboardOwner clipboardSlave;
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

    protected String getRandomString(int LENGTH) {
        String s = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        StringBuffer RandomString = new StringBuffer();

        for (int i = 0; i < LENGTH; i++) {
            RandomString.append(s.charAt(new Random().nextInt(s.length())));
        }
        return RandomString.toString();
    }

    protected String getRandomEmail(int LENGTH, String Example) {
        String randomPart = getRandomString(LENGTH);
        return Example.replaceAll("@", randomPart+"@");
    }

    @Test (enabled = false)
    public void userLoginBubbleContextMenu() {
        open("/");
        // подправил ожидание на 10 секунд
        $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).waitUntil(visible,10000);
        //$(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='email']")).contextClick().getText();
        SelenideElement menu = $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='email']"));
        //menu.contextClick().sendKeys(Keys.ARROW_DOWN);
        //menu.sendKeys(Keys.ARROW_DOWN);
        //menu.sendKeys(Keys.ENTER);
        WebElement R1 = getWebDriver().findElement(
                By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='email']"));
        Actions builder = new Actions(getWebDriver());
        builder.contextClick(R1).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN)
                .sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ESCAPE).build().perform();
        //sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ENTER).
        //build().perform();
        //.sendKeys(Keys.ENTER)
        //action.contextClick(productLink).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.RETURN).build().perform();
        //System.out.println()
    }

    @Test
    public void userLogin() {
        open("/");
        try{
            $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).waitUntil(visible,10000);

            $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='email']"))
                    .val(test.getProperty("account_exist_email"));

            $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='password']"))
                    .val(test.getProperty("account_exist_password"));

            $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button")).click();

            $(By.xpath("//div[@class='loading-panel' and not(contains(@style,'display: none'))]")).waitUntil(appear,10000);
            $(By.xpath("//div[@class='menu']")).shouldBe(visible);
            $(By.xpath("//h1[@class='settings-page__title']")).shouldBe(visible);

            String url = WebDriverRunner.url();
            String[] parts = url.split("#");
            StringBuilder deleteUrl = new StringBuilder(parts[0] + "#profile");
            open(deleteUrl.toString());

            $(By.xpath("//div[@class='ProfileView']//div[@class='Title']//span[@class='gwt-InlineHTML']")).shouldBe(visible);

            Assert.assertTrue(
                    $$(By.xpath("//div[@class='backend-TitledPanel-content']//input[@class='gwt-TextBox']")).
                            findBy(value(test.getProperty("account_exist_email"))).exists());
            $(By.xpath("//a[@class='horizontal-icolink icolink-append']//div[@class='multiadmin-profile']")).click();
            $(By.xpath("//div[@class='store-profile-footer']//a")).click();
        }
        finally {
            //TODO еще подумать, но этот вариант рабочий
            clearBrowserCache();
        }
    }

    @Test
    public void userLoginBubbleEmailEmpty() {
        open("/");
        // подправил ожидание на 10 секунд
        $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).waitUntil(visible,10000);
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button")).click();

        Assert.assertEquals(
                $(By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"))
                        .shouldBe(visible).getText(), test.getProperty("email"));
    }

    @Test
    public void userLoginBubblePasswordEmpty() {
        open("/");
        // подправил ожидание на 10 секунд
        $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).waitUntil(visible,10000);
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='email']")).setValue(
                getRandomEmail(Integer.parseInt(test.getProperty("email_random_lenght")),test.getProperty("email_template")));
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button")).click();
        Assert.assertEquals(
                $(By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"))
                        .shouldBe(visible).getText(), test.getProperty("password"));
    }

    @Test
    public void userLoginBubbleNoAssociatedAcc() {
        open("/");
        // подправил ожидание на 10 секунд
        $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).waitUntil(visible,10000);
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='email']")).setValue(
                getRandomEmail(Integer.parseInt(test.getProperty("email_random_lenght")),test.getProperty("email_template")));
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='password']")).setValue(
                getRandomString(Integer.parseInt(test.getProperty("password_random_lenght"))));
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button")).click();
        Assert.assertEquals(
                $(By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"))
                        .shouldBe(visible).getText(), test.getProperty("no_account"));
    }

    @Test
    public void userLoginBubbleNoCorrectPass() {
        open("/");
        // подправил ожидание на 10 секунд
        $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).waitUntil(visible,10000);
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='email']")).setValue(
                test.getProperty("account_exist_email"));
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='password']")).setValue(
                getRandomString(Integer.parseInt(test.getProperty("password_random_lenght"))));
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button")).click();
        Assert.assertEquals(
                // подправил тут ожидание пока на 10 секунд, тут оно может быть дольше чем в других тестах
                $(By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"))
                        .waitUntil(visible,10000).getText(), test.getProperty("password_no_correct"));
    }

    @Test
    public void userLoginBubbleVeryLongMail() {
        open("/");
        // подправил ожидание на 10 секунд
        $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).waitUntil(visible,10000);
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='email']")).setValue(
        getRandomEmail(Integer.parseInt(test.getProperty("very_longe_mail"))-test.getProperty("email_template").length(),
                test.getProperty("email_template")));
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='password']")).setValue(
                getRandomString(Integer.parseInt(test.getProperty("password_random_lenght"))));
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button")).click();
        Assert.assertEquals(
                $(By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"))
                        .shouldBe(visible).getText(), test.getProperty("email"));
    }

    @Test
    public void userLoginBubbleMailRandomString() {
        open("/");
        // подправил ожидание на 10 секунд
        $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).waitUntil(visible,10000);
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='email']")).setValue(
                getRandomString(Integer.parseInt(test.getProperty("email_random_lenght"))));
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='password']")).setValue(
                getRandomString(Integer.parseInt(test.getProperty("password_random_lenght"))));
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button")).click();
        Assert.assertEquals(
                $(By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"))
                        .shouldBe(visible).getText(), test.getProperty("email"));
    }

    @Test
    public void userLoginBubbleEnter() {
        open("/");
        // подправил ожидание на 10 секунд
        $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).waitUntil(visible,10000);
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button")).pressEnter();
        Assert.assertEquals(
                $(By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"))
                        .shouldBe(visible).getText(), test.getProperty("email"));
    }

    @Test
    public void userLoginBubbleEsc() {
        open("/");
        // подправил ожидание на 10 секунд
        $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).waitUntil(visible,10000);
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button")).click();
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='email']")).pressEscape();
        Assert.assertEquals(
                $(By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"))
                        .exists(), false);
    }

    @Test
    public void userLoginBubbleTab() {
        open("/");
        // подправил ожидание на 10 секунд
        $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).waitUntil(visible,10000);
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='email']")).pressTab();

        Assert.assertEquals($(By.xpath(
                "//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//div[@class='field field--large field--focus']" +
                        "//input[@type='password']")).exists(), true);

        // TODO доработать кейс нужен еще один переход на кнопку
    }

    @Test
    public void userLoginBubbleBorder() {
        open("/");
        // подправил ожидание на 10 секунд
        $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).waitUntil(visible,10000);
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='email']")).click();
        $(By.xpath("//div[@class='field field--large field--focus']")).exists();
        Assert.assertEquals($(By.xpath(
                "//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//div[@class='field field--large field--focus']" +
                        "//input[@name='email']")).exists(), true);
    }

    @Test
    public void userLoginDifferentRegister() {
        open("/");
        try{
            $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).waitUntil(visible,10000);

            $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='email']"))
                    .val(test.getProperty("account_exist_email_register"));

            $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='password']"))
                    .val(test.getProperty("account_exist_password"));

            $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button")).click();

            $(By.xpath("//div[@class='loading-panel' and not(contains(@style,'display: none'))]")).waitUntil(appear,10000);
            $(By.xpath("//div[@class='menu']")).shouldBe(visible);
            $(By.xpath("//h1[@class='settings-page__title']")).shouldBe(visible);

            String url = WebDriverRunner.url();
            String[] parts = url.split("#");
            StringBuilder deleteUrl = new StringBuilder(parts[0] + "#profile");
            open(deleteUrl.toString());

            $(By.xpath("//div[@class='ProfileView']//div[@class='Title']//span[@class='gwt-InlineHTML']")).shouldBe(visible);

            Assert.assertTrue(
                    $$(By.xpath("//div[@class='backend-TitledPanel-content']//input[@class='gwt-TextBox']")).
                            findBy(value(test.getProperty("account_exist_email_register"))).exists());
            $(By.xpath("//a[@class='horizontal-icolink icolink-append']//div[@class='multiadmin-profile']")).click();
            $(By.xpath("//div[@class='store-profile-footer']//a")).click();
        }
        finally {
            //TODO еще подумать, но этот вариант рабочий
            clearBrowserCache();
        }
    }

    @AfterClass
    public void clearCache() {
        clearBrowserCache();
    }
}
