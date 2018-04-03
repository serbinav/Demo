package ru.org.autotest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static org.testng.Assert.*;

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
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(props.getProperty("site_name"));
    }


    @Test
    public void userCreate() {
        //если мы регаемся такой элемент 1
        //div[@class='field field--large field--filled']//input[@name='email']

        WebElement dynamicLoad;
        dynamicLoad = (new WebDriverWait(driver, 5))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")));

        WebElement dynamicWait =
                driver.findElement(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//p[last()]//a"));
        dynamicWait.click();

        WebElement loginName =
                driver.findElement(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//input[@name='name']"));
        loginName.sendKeys("acdc");

        WebElement loginEmail =
                driver.findElement(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//input[@name='email']"));
        loginEmail.sendKeys("makenshi+5@ecwid.com");

        WebElement loginPassword =
                driver.findElement(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//input[@name='password']"));
        loginPassword.sendKeys("12345678");

        WebElement loginButton =
                driver.findElement(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//button"));
        loginButton.click();

        WebElement dynamicSkip = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@class='skip']")));
        dynamicSkip.click();

        WebElement dynamicGreetings = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[@class='settings-page__title']")));

        // TODO нужно удалять аккаунт
        driver.manage().deleteAllCookies();
        driver.navigate().refresh();
    }

    @Test
    public void userLogin() {
//        log.log(Level.INFO, "test");

        //если мы логинимся таких элемента 2
        //div[@class='field field--large field--filled']//input[@name='email']

        WebElement loginEmail =
                driver.findElement(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='email']"));
        loginEmail.sendKeys("makenshi@ecwid.com");

        WebElement loginPassword =
                driver.findElement(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='password']"));
        loginPassword.sendKeys("12345678");

        WebElement loginButton =
                driver.findElement(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button"));
        loginButton.click();

//        WebElement profileUser = driver.findElement(By.cssSelector(".login-button__user"));
//        String mailUser = profileUser.getText();
//        Assert.assertEquals("autotestorgua@ukr.net", mailUser);

        WebElement dynamicWait = (new WebDriverWait(driver, 15))
                .until(
                        ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[@class='settings-page__title']")));

//        driver.manage().deleteCookie(arg0);
//        driver.manage().deleteCookieNamed(arg0);
        driver.manage().deleteAllCookies();
        driver.navigate().refresh();
    }


    @AfterClass
    public static void tearDown() {
        driver.quit();
    }

}