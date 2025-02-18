package com.Rusya2054.ui;


import com.Rusya2054.settings.ApplicationSettings;
import com.Rusya2054.sorters.IterationCounter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class GraphConsolePlotter implements Runnable{
    private final String BAR_STRING_SYMBOL = "█";
    private final String EMPTY_STRING_SYMBOL = " ";
    private Integer[] inputRefData;
    private volatile boolean toShow = false;

    public GraphConsolePlotter(Integer[] inputRefData){
        this.inputRefData = inputRefData;
    }

    public void clearConsole() {
        try {
            new ProcessBuilder( "cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (InterruptedException interruptedException){
            Thread.currentThread().interrupt();
        } catch (IOException ignore){

        }
    }

    public void setInputRefData(Integer[] inputRefData) {
        this.inputRefData = inputRefData;
    }

    private String[] transposeStringArr (String[] strings, final int maxStringLenth){
        String[] transposedSprings = new String[maxStringLenth];

        for (int i = 0; i < strings.length; i++){
            if (strings[i].length() < maxStringLenth) {
                strings[i] = strings[i] + EMPTY_STRING_SYMBOL.repeat(maxStringLenth - strings[i].length());
            }
        }

        for (int i = maxStringLenth -1; i >= 0; i--){
            StringBuilder sb = new StringBuilder();
            for(int j = 0; j < strings.length; j++){
                sb.append(strings[j].charAt(i));
            }
            transposedSprings[maxStringLenth - i - 1] = sb.toString();
        }
        return transposedSprings;
    }


    public void expandConsoleWidth(int value){
        try {
            new ProcessBuilder( "mode", "con", "cons="+value).inheritIO().start().waitFor();
        } catch (InterruptedException interruptedException){
            Thread.currentThread().interrupt();
        } catch (IOException ignore){

        }
    }

    public void plotData(Integer[] data){
        clearConsole();
        System.out.printf("Количество перемещений: %s%n", IterationCounter.swapCounter.get());
        String[] strings = new String[data.length];
        int maxLength = 0;
        for (int i = 0; i<data.length;i++){
            String s = getBarFromInt(data[i]);
            if (s.length() > maxLength){
                maxLength = s.length();
            }
            strings[i] = s;
        }
        for (String s: transposeStringArr(strings, maxLength)){
            System.out.println(s);
        }
    }

    private String getBarFromInt(int integer){
        return BAR_STRING_SYMBOL.repeat(integer/10);
    }

    public void setToShow(boolean toShow) {
        this.toShow = toShow;
    }

    @Override
    public void run() {
        try {
            while (toShow){
                plotData(this.inputRefData);
                TimeUnit.MILLISECONDS.sleep(ApplicationSettings.DELAY_IN_MILLISECONDS);
            }
        } catch (Exception ignore){

        }
    }
}
