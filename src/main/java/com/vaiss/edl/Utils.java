package com.vaiss.edl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

public class Utils {
    static void printCSV(List<String> list, String name) throws FileNotFoundException {
        File csvFile = new File(name);
        try (PrintWriter pw = new PrintWriter(csvFile)) {
            list.forEach(pw::println);
        }
    }
}
