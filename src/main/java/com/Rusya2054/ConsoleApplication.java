package com.Rusya2054;

import com.Rusya2054.data.DataGenerator;
import com.Rusya2054.settings.DefaultApplicationSettings;
import com.Rusya2054.sorters.BubbleSorter;
import com.Rusya2054.sorters.Sortable;
import com.Rusya2054.sorters.TImSorter;
import com.Rusya2054.ui.GraphConsolePlotter;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ConsoleApplication {
    private final DefaultApplicationSettings settings = new DefaultApplicationSettings();
    private GraphConsolePlotter ui;


    public void start(){
        Integer[] data = DataGenerator.getRandomGeneratedList(settings.getLengthOfGeneratedData() * 20);
        ui = new GraphConsolePlotter(data);
        int nThreads = settings.getNThreads();
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
                try {
                    sorter.sort();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                latch.countDown();
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        Sortable dataMerger = new TImSorter(data, 0, data.length);
        executor.submit(()->{
            try {
                dataMerger.sort();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
}
