package com.Rusya2054.settings;

public class DefaultApplicationSettings {
    private int lengthOfGeneratedData = 20;
    public static final long DELAY_IN_MILLISECONDS = 20;
    private int nThreads = 1;

    public int getLengthOfGeneratedData() {
        return lengthOfGeneratedData;
    }

    public void setLengthOfGeneratedData(int lengthOfGeneratedData) {
        this.lengthOfGeneratedData = lengthOfGeneratedData;
    }

    public int getNThreads() {
        return nThreads;
    }

    public void setNThreads(int nThreads) {
        this.nThreads = nThreads;
    }
}
