package ru.utils;

import org.openqa.selenium.By;

import java.util.Random;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.refresh;
import static com.codeborne.selenide.Selenide.sleep;

/**
 * Created by makenshi on 4/12/18.
 */
public class TestUtils {
    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";

    public static String generateRandomString(int length) {
        StringBuilder randomString = new StringBuilder();
        for (int i = 0; i < length; i++) {
            randomString.append(LETTERS.charAt(new Random().nextInt(LETTERS.length())));
        }
        return randomString.toString();
    }

    public static String composeEmail(String randomPart, String example) {
        return example.replaceAll("@", randomPart + "@");
    }

    public boolean waitRefresh(int countRetry, int sleep, String xpath) {
        for (Integer i = 0; i < countRetry; i++) {
            refresh();
            sleep(sleep);
            boolean visible =
                    $(By.xpath(xpath)).exists();
            if (visible == true)
                return true;
        }
        return false;
    }
}
