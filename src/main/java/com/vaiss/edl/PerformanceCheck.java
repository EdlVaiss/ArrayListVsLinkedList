package com.vaiss.edl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

/**
 * The main purpose of that app is to benchmark LinkedList vs ArrayList add(int index, E element) method
 * As a result you'll got two .csv files with two columns.
 * First column contains indexes that have been used to insert test data, second one - time elapsed to make insertion (in nanoseconds)
 * It is highly recommended to follow some rules while changing constants:
 * SIZE % INDEX_SCOPE must be 0
 * INDEX_SCOPE % INDEX_STEP must be 0
 */

public class PerformanceCheck {
    //test collection size
    static final Integer SIZE = 500000;
    //a range of indexes to be processed by each thread
    static final Integer INDEX_SCOPE = 100;
    //index change step. E.g. if INDEX_STEP is 20 insertion will be made and benchmarked for indexes 0, 20, 40, 60 etc.
    static final Integer INDEX_STEP = 20;
    //amount of working threads
    static final Integer THREADS_NUM = 20;
    //number of elements to be inserted into test collection
    static final Integer INSERT_AMOUNT = 100;
    static final String CSV_FILE_PATH = "D:/";

    public static void main(String[] args) throws InterruptedException, FileNotFoundException {
        System.out.println("Processing LinkedList");
        execute("LinkedList");
        System.out.println("Processing ArrayList");
        execute("ArrayList");
    }

    static void execute(String type) throws InterruptedException, FileNotFoundException {
        List<Callable<List<String>>> callables = new ArrayList<>();
        for (int i = 0; i < SIZE; i += INDEX_SCOPE) {
            callables.add(new Task(type, i, i + INDEX_SCOPE));
        }

        ExecutorService pool = Executors.newFixedThreadPool(THREADS_NUM);
        List<Future<List<String>>> readyFutures = pool.invokeAll(callables);
        pool.shutdown();

        List<String> resultList = new ArrayList<>();

        readyFutures.forEach((future) -> {
            try {
                resultList.addAll(future.get());
            } catch (InterruptedException | ExecutionException e) {
                System.out.println(e.getMessage());
            }
        });
        printCSV(resultList, type);
    }

    static void printCSV(List<String> list, String name) throws FileNotFoundException {
        File csvFile = new File(CSV_FILE_PATH + name + SIZE + ".csv");
        try (PrintWriter pw = new PrintWriter(csvFile)) {
            list.forEach(pw::println);
        }
    }

    static List<String> check(String listType, int startIndex, int endIndex) throws ClassNotFoundException {
        List<String> resultsList = new ArrayList<>();
        List<Integer> list = getList(listType);

        for (int index = startIndex; index < endIndex; index += INDEX_STEP) {

            System.out.println(index);

            for (int i = 0; i < SIZE; i++) {
                list.add(i);
            }

            long start = System.nanoTime();
            for (int i = 0; i < INSERT_AMOUNT; i++) {
                list.add(index, Integer.MAX_VALUE);
            }
            long end = System.nanoTime() - start;

            resultsList.add(String.join(",", index + "", end + ""));
            list.clear();
        }

        return resultsList;
    }

    static List<Integer> getList(String listType) throws ClassNotFoundException {
        List<Integer> list;
        if ("LinkedList".equals(listType)) {
            list = new LinkedList<>();
        } else if ("ArrayList".equals(listType)) {
            list = new ArrayList<>(SIZE);
        } else {
            throw new ClassNotFoundException("Class must be LinkedList or ArrayList");
        }
        return list;
    }
}

class Task implements Callable<List<String>> {
    private String className;
    private int start;
    private int end;

    Task(String className, int start, int end) {
        this.className = className;
        this.start = start;
        this.end = end;
    }

    @Override
    public List<String> call() throws Exception {
        return PerformanceCheck.check(className, start, end);
    }
}
