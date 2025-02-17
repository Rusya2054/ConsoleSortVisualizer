package com.Rusya2054;


import com.Rusya2054.data.DataGenerator;
import com.Rusya2054.sorters.BubbleSorter;
import com.Rusya2054.sorters.Sortable;
import com.Rusya2054.sorters.TImSorter;
import com.Rusya2054.ui.GraphConsolePlotter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ApplicationRunner {
    public static void main(String[] args) {
        Integer[] data = DataGenerator.getRandomGeneratedList(230);
        GraphConsolePlotter ui = new GraphConsolePlotter(data);
        int nThreads = 10;

        int dataPart = data.length/nThreads;
        ThreadPoolExecutor executor = new ThreadPoolExecutor(nThreads+2, nThreads + 2, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(10));
        executor.allowCoreThreadTimeOut(true);
        executor.submit(ui);
        CountDownLatch latch = new CountDownLatch(nThreads);

        for (int i =1; i < nThreads + 1; i++){
            int startIndex = (i - 1) * dataPart;
            int endIndex = Math.min(i, nThreads) * dataPart;
            Sortable sorter = new TImSorter(data, startIndex, endIndex);
            executor.submit(() -> {
                sorter.sort();
                latch.countDown();
            });
        }


        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        Sortable dataMerger = new TImSorter(data, 0, data.length);
        executor.submit(dataMerger::sort);
        // TODO: отправить флаг на завершение
    }
}
