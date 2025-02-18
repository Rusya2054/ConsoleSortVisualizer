package com.Rusya2054.sorters;

import com.Rusya2054.settings.ApplicationSettings;

import java.util.concurrent.TimeUnit;

@SorterId(value = 3)
public class QuickSorter implements Sortable, SliceGateway {

    private final Integer[] data;
    private final int startIndex;
    private final int endIndex;

    public QuickSorter(Integer[] data, int startIndex, int endIndex) {
        this.data = data;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    public void sort() throws InterruptedException{
        quickSort(startIndex, endIndex - 1);
    }

    private void quickSort(int low, int high) throws InterruptedException {
        if (low < high) {
            int pivotIndex = partition(low, high);
            quickSort(low, pivotIndex - 1);
            quickSort(pivotIndex + 1, high);
        }
    }

    private int partition(int low, int high) throws InterruptedException {
        int pivot = data[high];
        int i = (low - 1);

        for (int j = low; j < high; j++) {
            if (data[j] <= pivot) {
                i++;
                swap(i, j);
                TimeUnit.MILLISECONDS.sleep(ApplicationSettings.DELAY_IN_MILLISECONDS);
            }
        }

        swap(i + 1, high);
        TimeUnit.MILLISECONDS.sleep(ApplicationSettings.DELAY_IN_MILLISECONDS);
        return i + 1;
    }

    private void swap(int i, int j) {
        int temp = data[i];
        data[i] = data[j];
        data[j] = temp;
        IterationCounter.swapCounter.incrementAndGet();
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
