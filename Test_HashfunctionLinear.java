package group_project_p2;
import group_project.DblHashMap;

import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class Test_HashfunctionLinear {
    public static void main(String[] args) {
        File data = new File("src/group_project_p2/data.csv");
        File collisionsCSV = new File("src/group_project_p2/FNV_sizePrime.txt");
        boolean doesExist = data.exists();

        if(doesExist) {
            try {
                long t1 = System.nanoTime();
                Scanner input = new Scanner(data);
                PrintWriter writer = new PrintWriter(collisionsCSV);
                writer.println("valCount,collisions");
                input.next();
                LinearHashMapTest<String, Integer> test = new LinearHashMapTest<>(3);
                int value = 0;
                while(input.hasNext()) {
                    String[] line = input.next().split(",");
                    if(Double.parseDouble(line[4]) > -7) {
                        test.put(line[0], 0);
                        writer.println(test.valCount + "," + test.collisions);
                    }
                }
                long t2 = System.nanoTime();
                System.out.println(t2 - t1);
                System.out.println(test.collisions);
                System.out.println(test.valCount);
                input.close();
                writer.close();

            } catch (IOException e) {
                System.out.println(":C");
            }

        }
    }
}
