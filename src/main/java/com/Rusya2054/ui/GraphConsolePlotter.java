package com.Rusya2054.ui;


import com.Rusya2054.sorters.IterationCounter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class GraphConsolePlotter implements Runnable{
    private final String BAR_STRING_SYMBOL = "x";
    private final String EMPTY_STRING_SYMBOL = " ";
    private Integer[] inputRefData;

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


    private String[] transposeStringArr (String[] strings, final int maxStringLenth){
        String[] transposedSprings = new String[maxStringLenth];

        for (int i = 0; i < strings.length; i++){
            if (strings[i].length() < maxStringLenth) {
                strings[i] = strings[i] + EMPTY_STRING_SYMBOL.repeat(maxStringLenth - strings[i].length());
            }
        }

        for (int i = maxStringLenth -1; i >= 0; i--){
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < strings.length; j++){
                sb.append(strings[j].charAt(i));
            }
            transposedSprings[maxStringLenth - i - 1] = sb.toString();
        }
        return transposedSprings;
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

    @Override
    public void run() {
         // TODO: добавить блокировку
        try {
            while (true){
                plotData(this.inputRefData);
                TimeUnit.MILLISECONDS.sleep(20);
            }
        } catch (Exception ignore){

        }
    }
}
