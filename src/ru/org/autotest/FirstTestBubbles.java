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
public class FirstTestBubbles {

    private static WebDriver driver;
    private static Logger log;

    public static final String EMAIL = "Please enter a valid email address";
    public static final String PASSWORD = "Please enter your password";
    public static final String NOACCOUNT = "No accounts associated with this email. " +
            "If you don’t remember your account email, please contact us at login-issues@ecwid.com.";
    public static final String NAME = "Please enter your first and last names";
    public static final String SIXLETTER = "Your password should be at least 6 characters long";
    public static final String ACCOUNTUSED = "We already have an account associated with this email. " +
            "You can sign in into your existing account or create a new one using another email.";

    @Test
    public void userLoginBubbleEmail() {

        new WebDriverWait(driver, 5)
            .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")));

        WebElement loginButton =
                driver.findElement(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button"));
        loginButton.click();

        //btnDeletePopup.getAttribute("value");
        //btnDeletePopup.getText();
        //btnDeletePopupText.clear();

        Assert.assertEquals(driver.findElement(
                By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"))
                .getText(),EMAIL);

        driver.navigate().refresh();
    }

    @Test
    public void userLoginBubblePassword() {

        new WebDriverWait(driver, 5)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")));

        WebElement loginEmail =
                driver.findElement(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//input[@name='email']"));
        loginEmail.sendKeys("makenshi@ecwid.com");

        WebElement loginButton =
                driver.findElement(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//button"));
        loginButton.click();

        // TODO видимо не всегда успевает
        Assert.assertEquals(driver.findElement(
                By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"))
                .getText(),PASSWORD);

        driver.navigate().refresh();
    }

    @Test
    public void userCreateBubbleName() {

        new WebDriverWait(driver, 5)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")));

        WebElement dynamicWait =
                driver.findElement(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//p[last()]//a"));
        dynamicWait.click();

        WebElement loginButton =
                (new WebDriverWait(driver, 5))
                .until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//button")));
        loginButton.click();

        Assert.assertEquals(driver.findElement(
                By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"))
                .getText(),NAME);

        driver.navigate().refresh();
    }

    @Test
    public void userCreateBubbleEmail() {

        new WebDriverWait(driver, 5)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='block-view-on']//div[@class='gwt-HTML']")));

        WebElement dynamicWait =
                driver.findElement(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_1']//p[last()]//a"));
        dynamicWait.click();

        WebElement loginName =
                driver.findElement(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//input[@name='name']"));
        loginName.sendKeys("acdc");

        WebElement loginButton =
                driver.findElement(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//button"));
        loginButton.click();

        Assert.assertEquals(driver.findElement(
                By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"))
                .getText(),EMAIL);

        driver.navigate().refresh();
    }

    @Test
    public void userCreateBubblePassword() {

        new WebDriverWait(driver, 5)
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

        WebElement loginButton =
                driver.findElement(By.xpath("//form[@target='FormPanel_ru.cdev.xnext.myecwidcom.MyEcwidCom_2']//button"));
        loginButton.click();

        Assert.assertEquals(driver.findElement(
                By.xpath("//div[@class='bubble notitle']//div[@class='bubble-error bubble-left']//div[@class='gwt-HTML']"))
                .getText(),SIXLETTER);

        driver.navigate().refresh();
    }

}