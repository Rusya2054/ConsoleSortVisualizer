package com.Rusya2054.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DataGenerationTest {
    @Test
    public void testQuantityElements(){
        Assertions.assertEquals(0, DataGenerator.getRandomGeneratedList(0).length);
        Assertions.assertEquals(20, DataGenerator.getRandomGeneratedList(20).length);
        Assertions.assertEquals(1000, DataGenerator.getRandomGeneratedList(1000).length);
        Assertions.assertEquals(0, DataGenerator.getRandomGeneratedList(-1).length);
        Assertions.assertEquals(0, DataGenerator.getRandomGeneratedList(-20).length);
        Assertions.assertEquals(0, DataGenerator.getRandomGeneratedList(-1000).length);
    }
}
