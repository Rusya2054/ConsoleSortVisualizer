package com.Rusya2054.sorters;

import com.Rusya2054.settings.DefaultApplicationSettings;

import java.util.concurrent.TimeUnit;

public class TImSorter implements Sortable, SliceGateway{

    private final Integer[] data;
    private final int startIndex;
    private final int endIndex;

    private static final int MIN_RUN = 32;

    public TImSorter(Integer[] data, int startIndex, int endIndex) {
        this.data = data;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    public void sort() throws InterruptedException{
        int n = endIndex - startIndex + 1;
        for (int i = startIndex; i < endIndex; i += MIN_RUN) {
            insertionSort(data, i, Math.min((i + MIN_RUN - 1), endIndex - 1));
            IterationCounter.swapCounter.incrementAndGet();
            TimeUnit.MILLISECONDS.sleep(DefaultApplicationSettings.DELAY_IN_MILLISECONDS);
        }

        for (int size = MIN_RUN; size < n; size *= 2) {
            for (int left = startIndex; left < endIndex; left += 2 * size) {
                int right = Math.min(left + 2 * size - 1, endIndex - 1);
                int mid = Math.min(left + size - 1, endIndex - 1);
                if (mid < right) {
                    merge(data, left, mid, right);
                }
                IterationCounter.swapCounter.incrementAndGet();
                TimeUnit.MILLISECONDS.sleep(DefaultApplicationSettings.DELAY_IN_MILLISECONDS);
            }
        }
    }
    private void insertionSort(Integer[] data, int left, int right) throws InterruptedException {
        for (int i = left + 1; i <= right; i++) {
            int key = data[i];
            int j = i - 1;
            while (j >= left && data[j] > key) {
                data[j + 1] = data[j];
                j--;
            }
            data[j + 1] = key;
            IterationCounter.swapCounter.incrementAndGet();
            TimeUnit.MILLISECONDS.sleep(DefaultApplicationSettings.DELAY_IN_MILLISECONDS);
        }
    }

    private void merge(Integer[] data, int left, int mid, int right) throws InterruptedException {
        int len1 = mid - left + 1;
        int len2 = right - mid;
        Integer[] leftArray = new Integer[len1];
        Integer[] rightArray = new Integer[len2];

        System.arraycopy(data, left, leftArray, 0, len1);
        System.arraycopy(data, mid + 1, rightArray, 0, len2);

        int i = 0, j = 0;
        int k = left;
        while (i < len1 && j < len2) {
            if (leftArray[i] <= rightArray[j]) {
                data[k++] = leftArray[i++];
            } else {
                data[k++] = rightArray[j++];
            }
            IterationCounter.swapCounter.incrementAndGet();
            TimeUnit.MILLISECONDS.sleep(DefaultApplicationSettings.DELAY_IN_MILLISECONDS);
        }
        while (i < len1) {
            data[k++] = leftArray[i++];
        }
        while (j < len2) {
            data[k++] = rightArray[j++];
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
