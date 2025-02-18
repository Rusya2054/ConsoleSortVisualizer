package com.Rusya2054.data;

import com.Rusya2054.sorters.BubbleSorter;
import com.Rusya2054.sorters.QuickSorter;
import com.Rusya2054.sorters.Sortable;
import com.Rusya2054.sorters.TimSorter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;

public class SortingAlgorithmsTest {
    @Test
    public void bubbleSortingTest() throws InterruptedException {
        Integer[] data = DataGenerator.getRandomGeneratedList(20);
        Sortable sortable = new BubbleSorter(data, 0, data.length);
        sortable.sort();
        Assertions.assertTrue(isIncreaseMonotonously(data));
    }


    @Test
    public void quickSortingTest() throws InterruptedException {
        Integer[] data = DataGenerator.getRandomGeneratedList(20);
        Sortable sortable = new QuickSorter(data, 0, data.length);
        sortable.sort();
        Assertions.assertTrue(isIncreaseMonotonously(data));
    }

    @Test
    public void timSortingTest() throws InterruptedException {
        Integer[] data = DataGenerator.getRandomGeneratedList(20);
        Sortable sortable = new TimSorter(data, 0, data.length);
        sortable.sort();
        Assertions.assertTrue(isIncreaseMonotonously(data));
    }

    @Test
    public void bubbleSortingTestZero() throws InterruptedException {
        Integer[] data = DataGenerator.getRandomGeneratedList(0);
        Sortable sortable = new BubbleSorter(data, 0, data.length);
        sortable.sort();
        Assertions.assertTrue(isIncreaseMonotonously(data));
    }


    @Test
    public void quickSortingTestZero() throws InterruptedException {
        Integer[] data = DataGenerator.getRandomGeneratedList(0);
        Sortable sortable = new QuickSorter(data, 0, data.length);
        sortable.sort();
        Assertions.assertTrue(isIncreaseMonotonously(data));
    }

    @Test
    public void imSortingTestZero() throws InterruptedException {
        Integer[] data = DataGenerator.getRandomGeneratedList(0);
        Sortable sortable = new TimSorter(data, 0, data.length);
        sortable.sort();
        Assertions.assertTrue(isIncreaseMonotonously(data));
    }

    @Test
    public void bubbleSortingTestOne() throws InterruptedException {
        Integer[] data = DataGenerator.getRandomGeneratedList(1);
        Sortable sortable = new BubbleSorter(data, 0, data.length);
        sortable.sort();
        Assertions.assertTrue(isIncreaseMonotonously(data));
    }


    @Test
    public void quickSortingTestOne() throws InterruptedException {
        Integer[] data = DataGenerator.getRandomGeneratedList(1);
        Sortable sortable = new QuickSorter(data, 0, data.length);
        sortable.sort();
        Assertions.assertTrue(isIncreaseMonotonously(data));
    }

    @Test
    public void imSortingTestOne() throws InterruptedException {
        Integer[] data = DataGenerator.getRandomGeneratedList(1);
        Sortable sortable = new TimSorter(data, 0, data.length);
        sortable.sort();
        Assertions.assertTrue(isIncreaseMonotonously(data));
    }

    @Test
    public void dataMonotonouslyTest(){
        Integer[] data1 = DataGenerator.getRandomGeneratedList(0);
        Assertions.assertTrue(isIncreaseMonotonously(data1));
        Integer[] data2 = DataGenerator.getRandomGeneratedList(1);
        Assertions.assertTrue(isIncreaseMonotonously(data2));
        Integer[] data3 = DataGenerator.getRandomGeneratedList(100);
        Assertions.assertFalse(isIncreaseMonotonously(data3));
    }

    private static boolean isIncreaseMonotonously(Integer[] data){
        boolean resultValue = true;
        BiFunction<Integer, Integer, Boolean> function = (a, b) -> b >= a;
        for (int i = 1; i< data.length; i++){
            resultValue = function.apply(data[i-1], data[i]);
            if (!resultValue){
                return false;
            }
        }
        return resultValue;
    }
}
