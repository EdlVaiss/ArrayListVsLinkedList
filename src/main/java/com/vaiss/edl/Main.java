package com.vaiss.edl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Main {
    public static final Integer CAPACITY = 5_000_000;
    public static final String CSV_FILE_NAME = "D:/";

    public static void main(String[] args) {
        List<Integer> llist = new LinkedList<>();
        List<Integer> alist = new ArrayList<>(CAPACITY);

        check(llist);
        check(alist);
    }

    static void printCSV(List<String> list, String name) {
        File csvFile = new File(CSV_FILE_NAME + name + CAPACITY + ".csv");
        try (PrintWriter pw = new PrintWriter(csvFile)) {
            list.forEach(pw::println);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    static void check(List<Integer> list) {
        DataHolder holder = new DataHolder();
        List<String> resultList = new ArrayList<>();
        for (int index = 0; index < CAPACITY; index += 5) {
            System.out.println(index);
            for (int i = 0; i < CAPACITY; i++) {
                list.add(new Integer(i));
            }

            long start = System.currentTimeMillis();
            for (int i = 0; i < 100; i++) {
                list.add(index, new Integer(Integer.MAX_VALUE));
            }
            long end = System.currentTimeMillis() - start;
            holder.setParams(list.getClass().getName(), index, end) ;
            resultList.add(holder.toString());
            list.clear();

//            System.out.println(String.format("Время работы для %s (в милисекундах) = %d", list.getClass(), (System.currentTimeMillis() - start)));
        }

        printCSV(resultList, holder.getName().split("\\.")[2]);
    }
}
