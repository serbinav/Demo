package ru.utils;

import java.util.Random;

/**
 * Created by makenshi on 4/12/18.
 */
public class TestUtils {

    public TestUtils() {}

    public String generateRandomString(int length) {
        String s = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        StringBuffer randomString = new StringBuffer();

        for (int i = 0; i < length; i++) {
            randomString.append(s.charAt(new Random().nextInt(s.length())));
        }
        return randomString.toString();
    }

    public String composeEmail(String randomPart, String example) {
        return example.replaceAll("@", randomPart+"@");
    }
}
