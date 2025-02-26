package com.Rusya2054.settings;

import com.Rusya2054.ApplicationRunner;
import com.Rusya2054.sorters.BubbleSorter;
import com.Rusya2054.sorters.Sortable;
import com.Rusya2054.sorters.SorterId;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ApplicationSettings {
    private static int lengthOfGeneratedData = 20;
    private static int nThreads = 1;
    public static final long DELAY_IN_MILLISECONDS = 25;
    private static int sorterNumber = 1;
    private final static Map<Integer, LoadedSorters> algoNamesMap = new HashMap<>();
    private static final String SETTINS_STRING_TEMPLATE = "Длина массива: %d, Количество потоков: %d, Сортировщик: %s";

    public static class LoadedSorters {
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

    private static void findClassPath(File directory, String packageName) throws ClassNotFoundException {

        for (File file : directory.listFiles()) {
            String className = (packageName + "."
                + file.getName().replace(".class", "")).replace("/", ".");
            Class<?> clazz = Class.forName(className);
            if (clazz.isAnnotationPresent(SorterId.class)){
                SorterId sorterId = clazz.getAnnotation(SorterId.class);
                algoNamesMap.put(sorterId.value(), new LoadedSorters(className, clazz.getName(), sorterId.value()));
            }
        }

    }

    private static void findJarClassPath(String jarBasepath) throws IOException, ClassNotFoundException {
        if (jarBasepath == null){
            throw new RuntimeException("jarBasepath - null");
        }
        JarFile jarFile = new JarFile(jarBasepath);
        Enumeration<JarEntry> e = jarFile.entries();
        while (e.hasMoreElements()) {
            JarEntry je = e.nextElement();
            if(je.isDirectory() || !je.getName().endsWith(".class")){
                continue;
            }
            String className = je.getName().substring(0,je.getName().length()-6);
            className = className.replace('/', '.');
            Class<?> clazz = Class.forName(className);
            if (clazz.isAnnotationPresent(SorterId.class)){
                SorterId sorterId = clazz.getAnnotation(SorterId.class);
                algoNamesMap.put(sorterId.value(), new LoadedSorters(className, clazz.getName(), sorterId.value()));
            }
        }
        jarFile.close();
    }

    static {
        URL currentClassUrl = ApplicationSettings.class.getClassLoader().getResource(ApplicationSettings.class.getName().replace('.', '/') + ".class");
        String scheme = null;
        try {
            scheme = currentClassUrl.toURI().getScheme();
        } catch (Exception gignor){
            throw new RuntimeException("\"scheme\" не найдена");
        }
        if (scheme.equals("file")){
            String sortersClassPath = currentClassUrl.getPath().substring(1, currentClassUrl.getPath().indexOf("settings"))+"sorters";
            try {
                File file = new File(sortersClassPath);
                findClassPath(file, "com/Rusya2054/sorters");
            } catch (ClassNotFoundException cex){
                System.out.println(cex.getMessage());
                throw new ClassCastException("Классы не найдены");
            }
        }
        if (scheme.equals("jar")){
            String[] parts = currentClassUrl.getPath().split("!/", 2);
            String sortersJarClassPath = parts[0].substring(6);
            try {
                String decodedJarPath = URLDecoder.decode(sortersJarClassPath, StandardCharsets.UTF_8);
                findJarClassPath(decodedJarPath);
            } catch (IOException e){
                throw new RuntimeException("Проблема с чтением .jar файла");
            } catch (ClassNotFoundException cex){
                throw new ClassCastException("Классы внутри .jar не найдены");
            }
        }
        if (algoNamesMap.isEmpty()){
            throw new RuntimeException("Проблема с динамической загрузкой классов");
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
