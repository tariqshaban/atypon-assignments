package ca.bcit;

import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.*;

@BenchmarkMode(Mode.AverageTime)
public class MyBenchmark {


    @Benchmark
    @Fork(value = 1)
    @Warmup(iterations = 3)
    @Measurement(iterations = 10)
    public void compete() {
        try {
            Sorting sorting = new Sorting();
            sorting.load();
            System.out.println(sorting.test());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        Main.main(args);
    }
}