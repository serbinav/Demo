package com.demo.testcase;

import com.codeborne.selenide.Condition;
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

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.*;
import java.util.Properties;
import java.util.Random;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.clearBrowserCache;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;


/**
 * Created by makenshi on 4/9/18.
 */
public class TestSelenideTemporaryDeprecated {

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
                stream = new FileInputStream(new File("config/test_create.ini"));
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
    public void userCreate() {
        open("/");
        try{
        $(By.xpath(anchor.getProperty("Login1"))).shouldBe(Condition.visible);
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

        $(By.xpath(anchor.getProperty("Login1"))).shouldBe(Condition.disappear);
        $(By.xpath("//div[@class='loading-panel' and not(contains(@style,'display: none'))]")).shouldBe(Condition.appear);
        $(By.xpath("//div[@class='loading-panel' and not(contains(@style,'display: none'))]")).shouldBe(Condition.disappear);
        $(By.xpath("//a[@class='skip']")).click();

        String url = WebDriverRunner.url();
        String[] parts = url.split("#");
        StringBuilder deleteUrl = new StringBuilder(parts[0] + "#profile");
        open(deleteUrl.toString());

        $(By.xpath("//a[@class='gwt-Anchor btn btn-default btn-medium delete-account-button']")).click();
        $(By.xpath("//div[@class='main-popup__container']//button[@class='btn btn-medium btn-primary']")).click();
        }
        finally {
            // TODO возможно данный вариант выхода из аккаунта "как из пушки по воробьям" и нужно конкретизировать
            clearBrowserCache();
            // TODO если тест упал надо удалять все куки и делать рефреш
        }
    }

    @Test(enabled = false)
    public void userCreateBubbleNameEmpty() {
        open("/");
        $(By.xpath(anchor.getProperty("Login1"))).shouldBe(Condition.visible);
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//p[last()]//a")).click();
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//button")).click();

        Assert.assertEquals(
                $(By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"))
                        .shouldBe(Condition.visible).getText(), test.getProperty("name"));
    }

    @Test(enabled = false)
    public void userCreateBubbleEmailEmpty() {
        open("/");
        $(By.xpath(anchor.getProperty("Login1"))).shouldBe(Condition.visible);
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//p[last()]//a")).click();
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//input[@name='name']")).setValue(
                getRandomString(Integer.parseInt(test.getProperty("random_name_lenght"))));
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//button")).click();

        Assert.assertEquals(
                $(By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"))
                        .shouldBe(Condition.visible).getText(), test.getProperty("email"));
    }

    @Test(enabled = false)
    public void userCreateBubblePasswordEmpty() {
        open("/");
        $(By.xpath(anchor.getProperty("Login1"))).shouldBe(Condition.visible);
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//p[last()]//a")).click();
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//input[@name='name']")).setValue(
                getRandomString(Integer.parseInt(test.getProperty("random_name_lenght"))));
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//input[@name='email']")).setValue(
                getRandomEmail(Integer.parseInt(test.getProperty("email_random_lenght")),test.getProperty("email_template")));
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//button")).click();

        Assert.assertEquals(
                $(By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"))
                        .shouldBe(Condition.visible).getText(), test.getProperty("six_letter"));
    }

    @Test(enabled = false)
    public void userCreateBubbleAccExist() {
        open("/");
        $(By.xpath(anchor.getProperty("Login1"))).shouldBe(Condition.visible);
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//p[last()]//a")).click();
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//input[@name='name']")).setValue(
                getRandomString(Integer.parseInt(test.getProperty("random_name_lenght"))));
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//input[@name='email']")).setValue(
                test.getProperty("account_exist_email"));
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//input[@name='password']")).setValue(
                test.getProperty("account_exist_password"));
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//button")).click();
        Assert.assertEquals(
                $(By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"))
                        .shouldBe(Condition.visible).getText(), test.getProperty("account_used"));
    }

    @Test(enabled = false)
    public void userCreateBubbleVeryLongName() {
        open("/");
        $(By.xpath(anchor.getProperty("Login1"))).shouldBe(Condition.visible);
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//p[last()]//a")).click();
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//input[@name='name']")).setValue(
                getRandomString(Integer.parseInt(test.getProperty("very_long_name"))));
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//input[@name='email']")).setValue(
                test.getProperty("email_template"));
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//input[@name='password']")).setValue(
                getRandomString(Integer.parseInt(test.getProperty("password_random_lenght"))));
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//button")).click();
        Assert.assertEquals(
                $(By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"))
                        .shouldBe(Condition.visible).getText(), test.getProperty("long_name"));
    }

    @Test(enabled = false)
    public void userCreateBubbleVeryLongMail() {
        open("/");
        $(By.xpath(anchor.getProperty("Login1"))).shouldBe(Condition.visible);
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//p[last()]//a")).click();
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//input[@name='name']")).setValue(
                getRandomString(Integer.parseInt(test.getProperty("random_name_lenght"))));
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//input[@name='email']")).setValue(
                getRandomEmail(Integer.parseInt(test.getProperty("very_longe_mail"))-test.getProperty("email_template").length(),
                        test.getProperty("email_template")));
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//input[@name='password']")).setValue(
                getRandomString(Integer.parseInt(test.getProperty("password_random_lenght"))));
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//button")).click();
        Assert.assertEquals(
                $(By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"))
                        .shouldBe(Condition.visible).getText(), test.getProperty("email"));
    }

    @Test (enabled = false, description = "ERROR")
    public void userCreateBubbleLongNameOk() {
        open("/");
        $(By.xpath(anchor.getProperty("Login1"))).shouldBe(Condition.visible);
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//p[last()]//a")).click();
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//input[@name='name']")).setValue(
                getRandomString(Integer.parseInt(test.getProperty("work_long_name"))));
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//input[@name='email']")).setValue(
                test.getProperty("email_template"));
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//input[@name='password']")).setValue(
                getRandomString(Integer.parseInt(test.getProperty("password_random_lenght"))));
        $(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//button")).click();
        Assert.assertEquals(
                $(By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"))
                        .shouldBe(Condition.visible).exists(), false);
        //TODO если тест пройдет то мы зарегистрируем аккаунт, но пока тут баг
        clearBrowserCache();
    }



    @Test (enabled = false)
    public void userLoginBubbleContextMenu() {
        open("/");
        // подправил ожидание на 10 секунд
        $(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")).waitUntil(Condition.visible,10000);
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

    @AfterClass
    public void clearCache() {
        clearBrowserCache();
    }
}
