package com.sideprojects.megamanxphantomblade.math;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

/**
 * Created by buivuhoang on 10/02/17.
 */
@RunWith(Parameterized.class)
public class GeoMathDownInteresectionTests {
    @Parameterized.Parameters
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {
                        new Rectangle(1, 1, 1, 1),
                        new Vector2(0.5f, 2.5f),
                        new Vector2(2.5f, 0.5f),
                        new Vector2(2, 1)
                },
                {
                        new Rectangle(-2, 1, 1, 1),
                        new Vector2(-0.5f, 2.5f),
                        new Vector2(-2.5f, 0.5f),
                        new Vector2(-2, 1)
                },
                {
                        new Rectangle(1, -3, 1, 1),
                        new Vector2(0.5f, -3.5f),
                        new Vector2(2.5f, -1.5f),
                        null
                },
                {
                        new Rectangle(-2, -3, 1, 1),
                        new Vector2(-0.5f, -2.5f),
                        new Vector2(-2.5f, -0.5f),
                        null
                },
                {
                        new Rectangle(1, 1, 1, 1),
                        new Vector2(-0.5f, -2.5f),
                        new Vector2(-2.5f, -0.5f),
                        null
                },
                {
                        new Rectangle(1, 1, 1, 1),
                        new Vector2(0.5f, 2.5f),
                        new Vector2(2.5f, 1f),
                        null
                }
        });
    }

    @Parameterized.Parameter()
    public Rectangle rec;

    @Parameterized.Parameter(1)
    public Vector2 start;

    @Parameterized.Parameter(2)
    public Vector2 end;

    @Parameterized.Parameter(3)
    public Vector2 expected;


    @Test
    public void should_find_correct_left_intersection() throws Exception {
        Vector2 intersection = GeoMath.findIntersectionDown(rec, start, end);
        Assert.assertEquals(expected, intersection);
    }

}