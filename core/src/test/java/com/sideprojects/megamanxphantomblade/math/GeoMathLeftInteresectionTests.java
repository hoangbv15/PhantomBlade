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
public class GeoMathLeftInteresectionTests {
    @Parameterized.Parameters
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                // colliding
                {
                        new RectangleTile(1, 1, 1, 1),
                        new Vector2(0.5f, 1.5f),
                        new Vector2(1.5f, 1.5f),
                        new Vector2(1, 1.5f)
                },
                {
                        new RectangleTile(-2, 1, 1, 1),
                        new Vector2(-0.5f, 2.5f),
                        new Vector2(-2.5f, 0.5f),
                        null
                },
                {
                        new RectangleTile(1, -3, 1, 1),
                        new Vector2(0.5f, -2.5f),
                        new Vector2(2.5f, -0.5f),
                        new Vector2(1f, -2.0f)
                },
                {
                        new RectangleTile(1, 1, 1, 1),
                        new Vector2(0.5f, 2f),
                        new Vector2(1.5f, 2f),
                        new Vector2(1, 2)
                },
                // not colliding
                {
                        new RectangleTile(1, 1, 1, 1),
                        new Vector2(1f, 2f),
                        new Vector2(1.5f, 2f),
                        new Vector2(1, 2)
                },
                {
                        new RectangleTile(1, 1, 1, 1),
                        new Vector2(0.5f, 2.5f),
                        new Vector2(2.5f, 0.5f),
                        new Vector2(1, 2)
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
    public void should_find_correct_left_intersection() throws Exception {
        Vector2 intersection = GeoMathRectangle.findIntersectionLeft(rec, start, end);
        Assert.assertEquals(expected, intersection);
    }

}