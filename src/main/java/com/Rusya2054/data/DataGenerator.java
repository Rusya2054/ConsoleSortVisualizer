package com.Rusya2054.data;

import java.util.Random;
import java.util.stream.IntStream;

public final class DataGenerator {
    private final static Random random = new Random();

    public static Integer[] getRandomGeneratedList(int N){
        return getRandomGeneratedList(N, 0, 100);
    }

    public static Integer[] getRandomGeneratedList(int N, double mean, double stddev){
        return IntStream.range(0, N).boxed().map(a->(int)Math.abs(random.nextGaussian(mean, stddev))).toArray(Integer[]::new);
    }
}
