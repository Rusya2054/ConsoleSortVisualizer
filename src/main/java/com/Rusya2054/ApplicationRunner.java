package com.Rusya2054;


import com.Rusya2054.data.DataGenerator;
import com.Rusya2054.settings.DefaultApplicationSettings;
import com.Rusya2054.sorters.BubbleSorter;
import com.Rusya2054.sorters.Sortable;
import com.Rusya2054.sorters.TImSorter;
import com.Rusya2054.ui.GraphConsolePlotter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ApplicationRunner {
    public static void main(String[] args) {
        ConsoleApplication consoleApplication = new ConsoleApplication();
        consoleApplication.start();
    }
}
