package com.demo.testcase;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Created by makenshi on 4/3/18.
 */
public class FirstProgsTest {

    private static WebDriver driver;
    private static Logger log;

    @BeforeClass
    public static void setup() {

        //System.out.println(System.getProperty("user.dir"));

        // TODO возможно нужно подключить Logger какой-нибудь
//        //https://habrahabr.ru/post/247647/
//        log = Logger.getLogger("FirstTest");
//        try {
//            LogManager.getLogManager().readConfiguration(new FileInputStream(new File("config/logging.properties")));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        //https://habrahabr.ru/post/194658/
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(new File("config/example.ini")));
        } catch (IOException e) {
            e.printStackTrace();
            //System.exit(1);
        }

        System.setProperty(props.getProperty("driver_name"), props.getProperty("driver_path"));

        // TODO подумать над моментом замены драйвера
        // driver = new OperaDriver();

        //глобальное высталение свойств не помогает
        //ChromeOptions options = new ChromeOptions();
        //options.setCapability(CapabilityType.PAGE_LOAD_STRATEGY, "normal");
        //driver = new ChromeDriver(options);
        //WebDriverWait wait = new WebDriverWait(driver, 10);

        driver = new ChromeDriver();
        //driver = new OperaDriver();

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get(props.getProperty("site_name"));
    }

    @AfterClass
    public static void tearDown() {
        driver.quit();
    }

}