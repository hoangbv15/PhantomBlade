package com.sideprojects.megamanxphantomblade.math;

import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.physics.tiles.SquareTriangleTile;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

/**
 * Created by buivuhoang on 13/05/17.
 */
@RunWith(Parameterized.class)
public class GeoMathTriangleUpTests {
    @Parameterized.Parameters
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {
                        new SquareTriangleTile(1, 1, 2, 1, 2, 2, 1, 1, 0, 1),
                        new Vector2(0, 1.5f),
                        new Vector2(3f, 1.5f),
                        new Vector2(1.5f, 1.5f)
                },
                {
                        new SquareTriangleTile(1, 1, 2, 1, 2, 2, 1, 1, 0, 1),
                        new Vector2(1, 2f),
                        new Vector2(2, 1f),
                        new Vector2(1.5f, 1.5f)
                }
        });
    }

    @Parameterized.Parameter()
    public SquareTriangleTile rec;

    @Parameterized.Parameter(1)
    public Vector2 start;

    @Parameterized.Parameter(2)
    public Vector2 end;

    @Parameterized.Parameter(3)
    public Vector2 expected;


    @Test
    public void should_find_up_intersection() throws Exception {
        Vector2 intersection = GeoMathTriangle.findVertexIntersectionUp(rec, start, end);
        Assert.assertEquals(expected, intersection);
    }

}
