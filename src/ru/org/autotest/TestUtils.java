package ru.org.autotest;

import java.util.Random;

/**
 * Created by makenshi on 4/12/18.
 */
public class TestUtils {

    public String generateRandomString(int LENGTH) {
        String s = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        StringBuffer RandomString = new StringBuffer();

        for (int i = 0; i < LENGTH; i++) {
            RandomString.append(s.charAt(new Random().nextInt(s.length())));
        }
        return RandomString.toString();
    }

    public String composeEmail(String randomPart, String Example) {
        return Example.replaceAll("@", randomPart+"@");
    }
}
