package utils;

import java.util.Random;

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

}
