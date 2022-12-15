package com.timmydont.wherearetherocas.utils;

import java.util.Arrays;

public class MathUtils {

    public static Float getMedian(Float [] numArray) {
        Arrays.sort(numArray);
        return numArray.length % 2 == 0 ?
                (numArray[numArray.length/2] + (float)numArray[numArray.length/2 - 1])/2 :
                numArray[numArray.length/2];
    }
}
