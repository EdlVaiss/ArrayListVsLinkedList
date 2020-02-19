package com.vaiss.edl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;


public class Main {
    public static final Integer CAPACITY = 5_000_000;
    public static final String CSV_FILE_NAME = "D:/";
    public static volatile List<String> resultList = new CopyOnWriteArrayList<>();

    public static void main(String[] args) throws InterruptedException, FileNotFoundException {
        String linkedListName = LinkedList.class.getName();
        String arrayListName = ArrayList.class.getName();
        System.out.println("Processing LinkedList");
        execute(linkedListName);
        System.out.println("Processing ArrayList");
        execute(arrayListName);
    }

    static void execute(String type) throws InterruptedException, FileNotFoundException {
        List<Callable<List<String>>> callables = new ArrayList<>();
        for (int i = 0; i < CAPACITY; i += 100) {
            callables.add(new Task(type, i, i + 100));
        }

        ExecutorService pool = Executors.newFixedThreadPool(20);
        List<Future<List<String>>> readyFutures = pool.invokeAll(callables);
        pool.shutdown();

        List<String> resultList = new ArrayList<>();

        readyFutures.stream().forEach((future) -> {
            try {
                resultList.addAll(future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
        printCSV(resultList, type);
    }

    static void printCSV(List<String> list, String name) throws FileNotFoundException {
        File csvFile = new File(CSV_FILE_NAME + name + CAPACITY + ".csv");
        try (PrintWriter pw = new PrintWriter(csvFile)) {
            list.forEach(pw::println);
        }
    }

    static List<String> check(String listType, int startIndex, int endIndex) throws ClassNotFoundException {
        List<String> resultsList = new ArrayList<>();
        List<Integer> list;
        if (listType.contains("LinkedList")) {
            list = new LinkedList<>();
        } else if (listType.contains("ArrayList")) {
            list = new ArrayList<>(CAPACITY);
        } else {
            throw new ClassNotFoundException("Class must be LinkedList or ArrayList");
        }

        for (int index = startIndex; index < endIndex; index += 20) {

            System.out.println(index);

            for (int i = 0; i < CAPACITY; i++) {
                list.add(new Integer(i));
            }

            long start = System.nanoTime();
            for (int i = 0; i < 100; i++) {
                list.add(index, new Integer(Integer.MAX_VALUE));
            }
            long end = System.nanoTime() - start;

            resultsList.add(String.join(",", list.getClass().getName(), index +"", end+""));
            list.clear();
        }

        return resultsList;
    }
}

class Task implements Callable<List<String>> {
    private String className;
    private int start;
    private int end;

    public Task(String className, int start, int end) {
        this.className = className;
        this.start = start;
        this.end = end;
    }

    @Override
    public List<String> call() throws Exception {
        return Main.check(className, start, end);
    }
}
