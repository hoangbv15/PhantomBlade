package com.sideprojects.megamanxphantomblade.math;

/**
 * Created by buivuhoang on 09/02/17.
 */
public class NumberMath {
    public static boolean hasNotExceeded(int i, int start, int end) {
        if (start > end) {
            return i >= end;
        } else {
            return i <= end;
        }
    }

    public static int iteratorNext(int i, int start, int end) {
        if (start > end) {
            return --i;
        } else {
            return ++i;
        }
    }

    public static boolean numberIsBetween(float x, float a, float b) {
        // Check if x is between a and b
        float m = (a + b)/2f;
        return Math.abs(x - m) <= Math.abs(a - m);
    }
}
