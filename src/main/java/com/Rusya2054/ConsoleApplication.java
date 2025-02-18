package com.Rusya2054;

import com.Rusya2054.data.DataGenerator;
import com.Rusya2054.settings.ApplicationSettings;
import com.Rusya2054.sorters.IterationCounter;
import com.Rusya2054.sorters.Sortable;
import com.Rusya2054.ui.GraphConsolePlotter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ConsoleApplication {
    private final GraphConsolePlotter ui = new GraphConsolePlotter(new Integer[]{0});


    public void startMenu(){
        ui.clearConsole();
        System.out.println(" 0-выход; 1-старт; 2-настройки;\n"+ ApplicationSettings.getCurrentSettingsString());
        Scanner scanner = new Scanner(System.in);
        int inputVariant = 0;
        try {
            inputVariant = scanner.nextInt();
        } catch (Exception ignore){
            startMenu();
        }
        switch (inputVariant){
            case 0:
                Runtime.getRuntime().exit(0);
                break;
            case 1:
                startVisualization();
                break;
            case 2:
                settingsMenu();
                break;
            default:
                startMenu();
                break;
        }
    }
    private void settingsMenu(){
        ui.clearConsole();
        System.out.println("Текущие настройки: " + ApplicationSettings.getCurrentSettingsString());
        System.out.println("0-назад; 1-длина массива; 2-количество-потоков; 3-алгоритм сортировки");
        Scanner scanner = new Scanner(System.in);
        int inputVariant = 0;
        try {
            inputVariant = scanner.nextInt();
        } catch (Exception ignore){
            startMenu();
        }
        switch (inputVariant){
            case 0:
                startMenu();
                break;
            case 1:
                settingsArrLength();
                break;
            case 2:
                settingsNumberThreads();
                break;
            case 3:
                settingsAlgorithm();
                break;
            default:
                settingsMenu();
                break;
        }
    }

    private void settingsArrLength(){
        ui.clearConsole();
        System.out.println("0-назад; Введите длину массива (больше 1):\n");
        Scanner scanner = new Scanner(System.in);
        int inputVariant = 0;
        try {
            inputVariant = scanner.nextInt();
        } catch (Exception ignore){
            startMenu();
        }
        if (inputVariant == 0){
            settingsMenu();
        }
        if (inputVariant > 1 && inputVariant < Integer.MAX_VALUE){
            ApplicationSettings.setLengthOfGeneratedData(inputVariant);
            settingsMenu();
        } else {
            settingsArrLength();
        }


    }

    private void settingsNumberThreads(){
        ui.clearConsole();
        System.out.println("0-назад; Введите количество потоков (не более 100):\n");
        Scanner scanner = new Scanner(System.in);
        int inputVariant = 0;
        try {
            inputVariant = scanner.nextInt();
        } catch (Exception ignore){
            startMenu();
        }
        if (inputVariant == 0){
            settingsMenu();
        }
        if (inputVariant > 1 && inputVariant <= 100){
            ApplicationSettings.setNThreads(inputVariant);
            settingsMenu();
        } else {
            settingsNumberThreads();
        }
    }

    private void settingsAlgorithm(){
        ui.clearConsole();
        System.out.println("0-назад; Алгоритмы: ");
        for (Integer i: ApplicationSettings.getAlgoNamesMap().keySet()){
            System.out.println(i+"-"+ApplicationSettings.getAlgoNamesMap().get(i).getName());
        }
        System.out.println("Выбранный вариант: "+ApplicationSettings.getSorterNumber());

        Scanner scanner = new Scanner(System.in);
        int inputVariant = 0;
        try {
            inputVariant = scanner.nextInt();
        } catch (Exception ignore){
            startMenu();
        }
        if (inputVariant == 0){
            settingsMenu();
        }
        if (inputVariant <= Collections.max(ApplicationSettings.getAlgoNamesMap().keySet())){
            ApplicationSettings.setSorterNumber(inputVariant);
            settingsMenu();
        } else {
            settingsNumberThreads();
        }
    }


    private void startVisualization(){
        Integer[] generatedData = DataGenerator.getRandomGeneratedList(ApplicationSettings.getLengthOfGeneratedData());
        ui.setInputRefData(generatedData);
        ui.setToShow(true);
        if (generatedData.length > 210){
            ui.expandConsoleWidth(generatedData.length+20);
        }
        IterationCounter.swapCounter.set(0);
        int nThreads = (ApplicationSettings.getLengthOfGeneratedData() > ApplicationSettings.getNThreads())?ApplicationSettings.getNThreads():ApplicationSettings.getLengthOfGeneratedData()/2;
        ThreadPoolExecutor executor = new ThreadPoolExecutor(nThreads+2,
                nThreads + 2,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(10));
        executor.allowCoreThreadTimeOut(true);
        executor.submit(ui);
        int dataPart = generatedData.length/nThreads;
        CountDownLatch latch = new CountDownLatch(nThreads);

        for (int i =1; i < nThreads + 1; i++){
            int startIndex = (i - 1) * dataPart;
            int endIndex = Math.min(i, nThreads) * dataPart;
            Sortable sorter = ApplicationSettings.sorterFactory(generatedData, startIndex, endIndex);
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
        Sortable dataMerger = ApplicationSettings.sorterFactory(generatedData, 0, generatedData.length);
        try {
            dataMerger.sort();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        ui.setToShow(false);
        executor.shutdown();
        System.out.println("Введите любой символ...\n");
        Scanner scanner = new Scanner(System.in);
        scanner.next();
        startMenu();
    }
}
