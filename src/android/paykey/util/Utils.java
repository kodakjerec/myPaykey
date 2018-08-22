package org.paykey.keyboard.sample.util;

/**
 * Created by alexkogan on 16/11/2017.
 */

public class Utils {

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
