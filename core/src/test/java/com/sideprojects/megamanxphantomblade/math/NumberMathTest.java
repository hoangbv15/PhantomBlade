package com.sideprojects.megamanxphantomblade.math;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

/**
 * Created by buivuhoang on 10/02/17.
 */
@RunWith(Parameterized.class)
public class NumberMathTest {
    @Parameterized.Parameters
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {1, 2, 3, true},
                {1, -2, 3, false},
                {1, 3, 3, true},
                {1.1156f, 1.1156f, 5.1245f, true},
                {1.1156f, 1.1157f, 5.1245f, true},
                {1.1156f, 1.1155f, 5.1245f, false},
                {1.1156f, 5.12451f, 5.1245f, false},
                {-1, -2, -2, true}
        });
    }

    @Parameterized.Parameter()
    public float a;

    @Parameterized.Parameter(1)
    public float x;

    @Parameterized.Parameter(2)
    public float b;

    @Parameterized.Parameter(3)
    public boolean expected;

    @Test
    public void numberIsBetween() throws Exception {
        boolean actual = NumberMath.numberIsBetween(x, a, b);
        Assert.assertEquals(expected, actual);
    }

}