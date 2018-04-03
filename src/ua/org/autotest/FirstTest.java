package ua.org.autotest;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import java.util.logging.Logger;

public class FirstTest {

//    private static String SOME_STRING_VALUE;

    private static WebDriver driver;
    private static Logger log;


    @BeforeClass
    public static void setup() {

        //System.out.println(System.getProperty("user.dir"));

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
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(props.getProperty("site_name"));
    }


    @Test
    public void userCreate() {
        //если мы регаемся такой элемент 1
        //div[@class='field field--large field--filled']//input[@name='email']

        WebElement loginField =
                driver.findElement(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='email']"));
        loginField.sendKeys("makenshi@ecwid.com");

        WebElement passwordField =
                driver.findElement(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='password']"));
        passwordField.sendKeys("12345678");

        WebElement loginButton =
                driver.findElement(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button"));
        loginButton.click();

        WebElement dynamicElement = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[@class='settings-page__title']")));

        driver.manage().deleteAllCookies();
        driver.navigate().refresh();
    }

    @Test
    public void userLogin() {
//        log.log(Level.INFO, "test");

        //если мы логинимся таких элемента 2
        //div[@class='field field--large field--filled']//input[@name='email']

        WebElement loginField =
                driver.findElement(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='email']"));
        loginField.sendKeys("makenshi@ecwid.com");

        WebElement passwordField =
                driver.findElement(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='password']"));
        passwordField.sendKeys("12345678");

        WebElement loginButton =
                driver.findElement(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button"));
        loginButton.click();

//        WebElement profileUser = driver.findElement(By.cssSelector(".login-button__user"));
//        String mailUser = profileUser.getText();
//        Assert.assertEquals("autotestorgua@ukr.net", mailUser);

        WebElement dynamicElement = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[@class='settings-page__title']")));

//        driver.manage().deleteCookie(arg0);
//        driver.manage().deleteCookieNamed(arg0);
        driver.manage().deleteAllCookies();
        driver.navigate().refresh();
    }



//    @AfterClass
//    public static void tearDown() {
//        WebElement menuUser = driver.findElement(By.cssSelector(".login-button__menu-icon"));
//        menuUser.click();
//        WebElement logoutButton = driver.findElement(By.id("login__logout"));
//        logoutButton.click();
//        driver.quit();
//    }
}