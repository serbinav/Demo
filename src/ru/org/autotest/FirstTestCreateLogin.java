package ru.org.autotest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
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
public class FirstTestCreateLogin {

    private static WebDriver driver;
    private static Logger log;

    public static final String CREATENAME = "acdc";
    public static final String CREATEEMAIL = "makenshi+10@ecwid.com";
    public static final String CREATEPASSWORD = "12345678";
    public static final String LOGINEMAIL = "makenshi@ecwid.com";
    public static final String LOGINPASSWORD = "12345678";

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
        loginName.sendKeys(CREATENAME);

        // TODO подумать генератором логинов
        WebElement loginEmail =
                driver.findElement(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//input[@name='email']"));
        loginEmail.sendKeys(CREATEEMAIL);

        WebElement loginPassword =
                driver.findElement(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//input[@name='password']"));
        loginPassword.sendKeys(CREATEPASSWORD);

        WebElement loginButton =
                driver.findElement(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//button"));
        loginButton.click();

        // TODO если аккаунт зареган, то тут падаем
        WebElement dynamicSkip = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@class='skip']")));
        dynamicSkip.click();

        String url = driver.getCurrentUrl();
        String[] parts = url.split("#");
        StringBuilder deleteUrl = new StringBuilder(parts[0] + "#profile");
        driver.navigate().to(deleteUrl.toString());

        WebElement btnDelete;
        btnDelete = (new WebDriverWait(driver, 5))
                .until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//a[@class='gwt-Anchor btn btn-default btn-medium delete-account-button']")));
        btnDelete.click();

        WebElement btnDeletePopup;
        btnDeletePopup = (new WebDriverWait(driver, 5))
                .until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//div[@class='main-popup__container']//button[@class='btn btn-medium btn-primary']")));
        btnDeletePopup.click();

        driver.navigate().to(parts[0]);

        // TODO если тест упал надо удалять все куки и делать рефреш, переходить на дефолтный урл
    }

    @Test
    public void userLogin() {
//        log.log(Level.INFO, "test");

        //если мы логинимся таких элемента 2
        //div[@class='field field--large field--filled']//input[@name='email']

        WebElement dynamicLoad;
        dynamicLoad = (new WebDriverWait(driver, 5))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")));

        WebElement loginEmail =
                driver.findElement(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='email']"));
        loginEmail.sendKeys(LOGINEMAIL);

        WebElement loginPassword =
                driver.findElement(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='password']"));
        loginPassword.sendKeys(LOGINPASSWORD);

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
        // TODO возможно данный вариант выхода из аккаунта "как из пушки по воробьям" и нужно конкретизировать
        driver.manage().deleteAllCookies();
        driver.navigate().refresh();

        // TODO если тест упал надо удалять все куки и делать рефреш, переходить на дефолтный урл
    }
}