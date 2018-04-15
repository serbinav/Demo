package ru.test.autotest;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import ru.utils.PropertiesStream;
import ru.utils.TestUtils;

import java.util.Properties;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;
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
            Properties prop = new PropertiesStream("config/properties.ini","UTF-8").getProperties();

            Configuration.baseUrl = prop.getProperty("site_name");
            Configuration.browser = prop.getProperty("browser_name");

            test = new PropertiesStream("config/test.ini","UTF-8").getProperties();
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

        @AfterClass
        public void tearDown() {
            clearBrowserCache();
        }

}
