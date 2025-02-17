package com.Rusya2054.sorters;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class BubbleSorter implements Sortable, SliceGateway{

    private final Integer[] data;
    private final int startIndex;
    private final int endIndex;

    public BubbleSorter(Integer[] data, int startIndex, int endIndex) {
        this.data = data;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    public void sort() {
        for (int i=startIndex; i< endIndex; i++){
            for (int j=startIndex; j < endIndex; j++){
                if (this.data[i] < this.data[j]){
                    int temp = this.data[i];
                    this.data[i] = this.data[j];
                    this.data[j] = temp;
                    IterationCounter.swapCounter.incrementAndGet();
                    try {
                        TimeUnit.MILLISECONDS.sleep(20);
                    } catch (Exception ignore){

                    }
                }
            }
        }
    }

    @Override
    public int getStartIndex() {
        return this.startIndex;
    }

    @Override
    public int getEndIndex() {
        return this.endIndex;
    }

    @Override
    public Integer[] getData() {
        int length = endIndex - startIndex;
        Integer[] deepCopy = new Integer[length];
        System.arraycopy(this.data, this.startIndex, deepCopy, 0, length);
        return deepCopy;
    }
}
