package com.vaiss.edl;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
/**
 * The main purpose of that app is to explicitly show the complexity in terms of O-notation of LinkedList vs ArrayList add(int index, E element) method
 * As a result you'll got two .csv files with two columns.
 * First column contains collection size, second one - time elapsed to make insertion (in nanoseconds)
 * It is highly recommended to follow some rules while changing constants:
 * INSERT_INDEX must be within START_SIZE
 */
public class ComplexityCheck {
    //number of elements to be inserted into test collection
    static final Integer INSERT_AMOUNT = 100;
    //position to insert data
    static final Integer INSERT_INDEX = 2500;
    //initial collection size
    static final Integer START_SIZE = 5000;
    //insertion iterations amount
    static final Integer INSERT_SESSIONS = 1000;

    public static void main(String[] args) throws ClassNotFoundException, FileNotFoundException {
        System.out.println("Processing LinkedList");
        PerformanceCheck.printCSV(check("LinkedList"), "LinkedList");
        System.out.println("Processing ArrayList");
        PerformanceCheck.printCSV(check("ArrayList"), "ArrayList");
    }

    static List<String> check(String listType) throws ClassNotFoundException {
        List<String> resultsList = new ArrayList<>();
        List<Integer> list = getList(listType);

        for (int i = 0; i < START_SIZE; i++) {
            list.add(i);
        }

        for (int i = 0; i < INSERT_SESSIONS; i++) {
            System.out.println(i);
            long start = System.nanoTime();
            for (int k = 0; k < INSERT_AMOUNT; k++) {
                list.add(INSERT_INDEX, Integer.MAX_VALUE);
            }
            long end = System.nanoTime() - start;

            resultsList.add(String.join(",", list.size() + "", end + ""));
        }
        return resultsList;
    }

    static List<Integer> getList(String listType) throws ClassNotFoundException {
        List<Integer> list;
        if ("LinkedList".equals(listType)) {
            list = new LinkedList<>();
        } else if ("ArrayList".equals(listType)) {
            //to avoid influence of inner array resize we provide max capacity
            list = new ArrayList<>(START_SIZE + INSERT_SESSIONS * INSERT_AMOUNT);
        } else {
            throw new ClassNotFoundException("Class must be LinkedList or ArrayList");
        }
        return list;
    }
}
