package com.Rusya2054.settings;

import com.Rusya2054.sorters.BubbleSorter;
import com.Rusya2054.sorters.Sortable;
import com.Rusya2054.sorters.SorterId;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.*;

public class ApplicationSettings {
    private static int lengthOfGeneratedData = 20;
    private static int nThreads = 1;
    public static final long DELAY_IN_MILLISECONDS = 20;
    private static int sorterNumber = 1;
    private final static Map<Integer, LoadedSorters> algoNamesMap = new HashMap<>();
    private static final String SETTINS_STRING_TEMPLATE = "Длина массива: %d, Количество потоков: %d, Сортировщик: %s";

    public static class LoadedSorters{
        private final String classPath;
        private final String name;
        private final int id;

        private LoadedSorters(String classPath, String name, int id){
            this.classPath = classPath;
            this.name = name;
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getClassPath() {
            return classPath;
        }
    }
    static {
        String sortersClassPath = "target/classes/com/Rusya2054/sorters";

        for (File file : Objects.requireNonNull(new File(sortersClassPath).listFiles())){
            if (file.getName().endsWith(".class")){
                String className = "com.Rusya2054.sorters" + "." + file.getName().replace(".class", "");
                try {
                    Class<?> clazz = Class.forName(className);
                    if (clazz.isAnnotationPresent(SorterId.class)){
                        SorterId sorterId = clazz.getAnnotation(SorterId.class);
                        algoNamesMap.put(sorterId.value(), new LoadedSorters(className, clazz.getName(), sorterId.value()));

                    }
                } catch (Exception ignore) {
                }
            }
        }
    }


    public static int getLengthOfGeneratedData() {
        return ApplicationSettings.lengthOfGeneratedData;
    }

    public static Map<Integer, LoadedSorters> getAlgoNamesMap() {
        return algoNamesMap;
    }

    public static void setLengthOfGeneratedData(int lengthOfGeneratedData) {
        ApplicationSettings.lengthOfGeneratedData = lengthOfGeneratedData;
    }

    public static int getNThreads() {
        return ApplicationSettings.nThreads;
    }

    public static Sortable sorterFactory(Integer[] data, int startIndex, int endIndex){
        try {
            if (ApplicationSettings.sorterNumber > Collections.max(algoNamesMap.keySet())){
                throw new Exception("Введен id алгоритма больше есть существует");
            }

            Class<?> clazz = Class.forName(algoNamesMap.get(ApplicationSettings.getSorterNumber()).getClassPath());
            return (Sortable) clazz.getConstructor(Integer[].class, int.class, int.class).newInstance(data, startIndex, endIndex);

        } catch (Exception ignore){
            return new BubbleSorter(data, startIndex, endIndex);
        }
    }

    public static void setNThreads(int nThreads) {
        ApplicationSettings.nThreads = nThreads;
    }

    public static int getSorterNumber() {
        return sorterNumber;
    }

    public static void setSorterNumber(int sorterNumber) {
        ApplicationSettings.sorterNumber = sorterNumber;
    }

    public static String getCurrentSettingsString(){
        return ApplicationSettings.SETTINS_STRING_TEMPLATE.formatted(lengthOfGeneratedData, nThreads, sorterFactory(new Integer[]{0}, 0, 1).getClass().getSimpleName());
    }
}
