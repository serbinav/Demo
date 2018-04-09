package ru.org.autotest;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.By;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.awt.*;
import java.awt.datatransfer.*;
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

    protected void setClipboardContents(String aString){
        StringSelection stringSelection = new StringSelection(aString);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, clipboardSlave);
    }

    protected String getClipboardContents() {
        String result = "";
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        //odd: the Object param of getContents is not currently used
        Transferable contents = clipboard.getContents(null);
        boolean hasTransferableText =
                (contents != null) &&
                        contents.isDataFlavorSupported(DataFlavor.stringFlavor)
                ;
        if (hasTransferableText) {
            try {
                result = (String)contents.getTransferData(DataFlavor.stringFlavor);
            }
            catch (UnsupportedFlavorException | IOException ex){
                System.out.println(ex);
                ex.printStackTrace();
            }
        }
        return result;
    }


    @Test(enabled = false)
    public void userLogin() {
        open("/");
        $(By.xpath(anchor.getProperty("Login1"))).shouldBe(visible);

        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='email']"))
                .val(test.getProperty("account_exist_email"));

        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='password']"))
                .val(test.getProperty("account_exist_password"));

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

    @Test
    public void userLoginBubbleEmailEmpty() {
        open("/");
        $(By.xpath(anchor.getProperty("Login1"))).shouldBe(visible);
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button")).click();

        Assert.assertEquals(
                $(By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"))
                        .shouldBe(visible).getText(), test.getProperty("email"));
    }

    @Test
    public void userLoginBubblePasswordEmpty() {
        open("/");
        $(By.xpath(anchor.getProperty("Login1"))).shouldBe(visible);
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
        $(By.xpath(anchor.getProperty("Login1"))).shouldBe(visible);
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
        $(By.xpath(anchor.getProperty("Login1"))).shouldBe(visible);
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='email']")).setValue(
                test.getProperty("account_exist_email"));
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='password']")).setValue(
                getRandomString(Integer.parseInt(test.getProperty("password_random_lenght"))));
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button")).click();
        Assert.assertEquals(
                $(By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"))
                        .shouldBe(visible).getText(), test.getProperty("password_no_correct"));
    }

    @Test
    public void userLoginBubbleVeryLongMail() {
        open("/");
        $(By.xpath(anchor.getProperty("Login1"))).shouldBe(visible);
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
        $(By.xpath(anchor.getProperty("Login1"))).shouldBe(visible);
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
        $(By.xpath(anchor.getProperty("Login1"))).shouldBe(visible);
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button")).pressEnter();

        Assert.assertEquals(
                $(By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"))
                        .shouldBe(visible).getText(), test.getProperty("email"));
    }

    @Test
    public void userLoginBubbleEsc() {
        open("/");
        $(By.xpath(anchor.getProperty("Login1"))).shouldBe(visible);
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button")).click();
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='email']")).pressEscape();
        Assert.assertEquals(
                $(By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"))
                        .exists(), false);
    }

    @Test(enabled = false)
    public void userLoginBubbleTab() {
        open("/");
        $(By.xpath(anchor.getProperty("Login1"))).shouldBe(visible);
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='email']")).setValue(
                test.getProperty("account_exist_email")).pressTab();
        sleep(5000);
        //$(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='password']"))
        Assert.assertEquals(getFocusedElement().getTagName(),
                 "//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='password']");

    }

    @AfterClass
    public void clearCache() {
        clearBrowserCache();
    }
}