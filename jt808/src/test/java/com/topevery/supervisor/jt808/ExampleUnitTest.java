package com.example.android_supervisor.jt808;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);

//        String _10 = "1111471286707625986";
//        String _10 = "1111471286707625986";
        String _10 = "99999999999999999999";
        String _36 = new BigInteger(_10).toString(16);
        System.out.print(_36);
        System.out.print(Arrays.toString(_36.getBytes("GBK")));

//        8fzzos5rij2a
    }
}