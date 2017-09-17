package com.sideprojects.megamanxphantomblade.math;

import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.physics.tiles.RectangleTile;
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
                        new RectangleTile(1, 1, 1, 1),
                        new Vector2(0.5f, 0.5f),
                        new Vector2(2.5f, 2.5f),
                        new Vector2(1, 1)
                },
                {
                        new RectangleTile(-2, 1, 1, 1),
                        new Vector2(-0.5f, 0.5f),
                        new Vector2(-2.5f, 2.5f),
                        new Vector2(-1, 1)
                },
                {
                        new RectangleTile(1, -3, 1, 1),
                        new Vector2(0.5f, -3.5f),
                        new Vector2(2.5f, -1.5f),
                        new Vector2(1, -3)
                },
                {
                        new RectangleTile(-2, -3, 1, 1),
                        new Vector2(-0.5f, -2.5f),
                        new Vector2(-2.5f, -0.5f),
                        null
                },
                {
                        new RectangleTile(1, 1, 1, 1),
                        new Vector2(-0.5f, -2.5f),
                        new Vector2(-2.5f, -0.5f),
                        null
                },
                {
                        new RectangleTile(1, 1, 1, 1),
                        new Vector2(0.5f, 2.5f),
                        new Vector2(2.5f, 1f),
                        null
                }
        });
    }

    @Parameterized.Parameter()
    public RectangleTile rec;

    @Parameterized.Parameter(1)
    public Vector2 start;

    @Parameterized.Parameter(2)
    public Vector2 end;

    @Parameterized.Parameter(3)
    public Vector2 expected;


    @Test
    public void should_find_correct_down_intersection() throws Exception {
        Vector2 intersection = GeoMathRectangle.findIntersectionDown(rec, start, end);
        Assert.assertEquals(expected, intersection);
    }

}