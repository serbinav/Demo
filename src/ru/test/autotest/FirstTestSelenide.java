package ru.test.autotest;

import com.codeborne.selenide.Configuration;
import org.testng.annotations.*;
import ru.utils.PropertiesStream;
import ru.utils.TestUtils;

import java.util.Properties;

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




    @AfterMethod
    public void tearDown(){
        clearBrowserCache();
    }


//    @AfterTest
//        public void Clear(){
//            clearBrowserCache();
//        }


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

