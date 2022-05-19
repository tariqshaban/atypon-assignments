package ca.bcit;

import edu.princeton.cs.algs4.Stopwatch;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Stream;


/**
 * Homework - Sorting
 * Sort the list of doubles in the fastest possible way.
 * The only method you can change is the sort() method.
 * You can add additional methods if needed, without changing the load() and test() methods.
 */
public class Sorting {

    protected List<Integer> list = new ArrayList<>();

    /**
     * Loading the text files with double numbers
     */
    protected void load() {
        try (Stream<String> stream = Files.lines(Paths.get("numbers.txt"))) {
            stream.forEach(x -> list.add(Integer.parseInt(x)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Testing of your solution, using 100 shuffled examples
     *
     * @return execution time
     */
    protected double test() {
        Stopwatch watch = new Stopwatch();
        for (int i = 0; i < 100; i++) {
            Collections.shuffle(list, new Random(100)); //1.22
            sort(list);
        }
        return watch.elapsedTime();
    }

    /**
     * Sorting method - add your code in here
     *
     * @param list - list to be sorted
     */
    private void sort(List<Integer> list) {
        //Warning suppressed to attempt to show a clean code; this warning arises when a variable is assigned but never used
        //noinspection UnusedAssignment
        list = runSorter(1, list);
    }

    /**
     * Sorting algorithm selector method
     * Some methods have to return the list since its highly dependant on recursion (returns sub-lists)
     *
     * @param option - the algorithm used, having 1 as the fastest and 6 as the slowest default sorter
     * @param list   - the list that you wish to sort
     * @return the sorted list
     **/
    //Warning suppressed to attempt to show a clean code; this warning arises when a method is called with the same specific parameter (option) everytime
    @SuppressWarnings("SameParameterValue")
    List<Integer> runSorter(int option, List<Integer> list) {
        //Defining a ForkJoinPool to be used in case 2 and 3
        ForkJoinPool pool = ForkJoinPool.commonPool();
        //The program will not respond to any value other than between 1 and 6
        switch (option) { //These numbers shows the average time taken to sort the list (includes shuffling them)
            case 1:
                bucketSortThreaded(list); //3.54
                break;
            case 2:
                list = pool.invoke(new MergeSortTask(list.toArray(new Integer[0]), (Integer[]) Array.newInstance(Integer.class, list.size()), 0, list.size() - 1)); //6.508
                break;
            case 3:
                list = pool.invoke(new QuickSortTask(0, list.size() - 1, list)); //8.545
                break;
            case 4:
                bucketSort(list); //9.001
                break;
            case 5:
                list = multiThreadSort(12, list.toArray(new Integer[0]), 0, list.size() - 1); //9.669
                break;
            case 6:
                Collections.sort(list); //15.413
                break;
        }
        return list;
    }

    /**
     * Single-threaded bucket sorter algorithm method
     *
     * @param list - the list that you wish to sort
     **/
    private void bucketSort(List<Integer> list) {

        // Create bucket array
        ArrayList<List<Integer>> buckets = new ArrayList<>(3000);

        // Associate a list with each index in the bucket array
        for (int i = 0; i < 3000; i++) {
            buckets.add(new ArrayList<>());
        }
        // Assign numbers from array to the proper bucket by using a primitive hashing function (Appears to be fairly distributed)
        for (int num : list) {
            buckets.get(num / 3334).add(num);
        }

        // Sorts each bucket
        for (List<Integer> bucket : buckets) {
            //TODO: Find a faster inner sorting algorithm, expect result's effectiveness to be minimal
            Collections.sort(bucket);
        }

        //Clears old list
        list.clear();

        // Merge buckets to get sorted array
        for (List<Integer> bucket : buckets) {
            list.addAll(bucket);
        }
    }

    /**
     * Multi-threaded bucket sorter algorithm method
     *
     * @param list - the list that you wish to sort
     **/
    private void bucketSortThreaded(List<Integer> list) {

        // Create bucket array
        ArrayList<List<Integer>> buckets = new ArrayList<>(3000);

        // Associate a list with each index in the bucket array
        for (int i = 0; i < 3000; i++) {
            //A Collections.synchronizedList is required since ArrayLists are inconsistent in multithreaded environment
            buckets.add(Collections.synchronizedList(new ArrayList<>()));
        }

        // Assign numbers from array to the proper bucket by using a primitive hashing function (Appears to be fairly distributed)
        list.parallelStream().forEach(
                num -> buckets.get(num / 3334).add(num)
        );

        // Sorts each buckets
        buckets.parallelStream().forEach(
                //TODO: Find a faster inner sorting algorithm, expect result's effectiveness to be minimal
                Collections::sort
        );

        //Clears old list
        list.clear();

        // Merge buckets to get sorted array
        for (List<Integer> bucket : buckets) {
            list.addAll(bucket);
        }
    }


    /**
     * Sorting algorithm selector method
     * Some methods have to return the list since its highly dependant on recursion (returns sub-lists)
     *
     * @param threads - the number of threads that you wish to use, best to be the number of cores (physical and logical). e.g eight threads for a quad core cpu
     * @param arr     - the list that you wish to sort, converted into an array in order to invoke System.arraycopy method
     * @param start   - the start index, liable to change due to recursion
     * @param stop    - the end index, liable to change due to recursion
     * @return the sorted list
     **/
    public List<Integer> multiThreadSort(int threads, Integer[] arr, int start, int stop) {
        //A condition that checks whether the threads threshold is reached or the start index is larger than the end index (usually the second condition is always false for large arrays and low threads)
        if (threads > 1 && start < stop) {
            int midpoint = (start + stop) / 2;
            //Recursively calls the method as a thread for left and right arrays; keeping in mind to divide the threads by two inorder to not overload the threads count
            Thread left = new Thread(() -> multiThreadSort(threads / 2, arr, start, midpoint));
            Thread right = new Thread(() -> multiThreadSort(threads / 2, arr, midpoint + 1, stop));
            //Initiate the threads
            left.start();
            right.start();
            //Joins the thread
            //TODO: Find a way to keep the time of thread waiting minimal; by making both threads finish nearly at similar timing
            try {
                left.join();
                right.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //After each block is sorted, the function will be called to merge them
            merge(arr, (Integer[]) Array.newInstance(Integer.class, list.size()), start, midpoint, stop);
        } else
            //Sorts the sub-array
            //TODO: Find a faster inner sorting algorithm, expect result's effectiveness to be minimal
            Arrays.sort(arr, start, stop + 1);
        return Arrays.asList(arr);
    }

    //Javadoc comment is not implemented here since its private; end users are not permitted to view such method
    //Given the two sub-arrays, and assuming that each array is sorted individually, this function merges those arrays into a one sorted array
    private void merge(Integer[] a, Integer[] helper, int lo, int mid, int hi) {
        if (hi + 1 - lo >= 0) System.arraycopy(a, lo, helper, lo, hi + 1 - lo);
        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid) {
                a[k] = helper[j++];
            } else if (j > hi) {
                a[k] = helper[i++];
            } else if (isLess(helper[i], helper[j])) {
                a[k] = helper[i++];
            } else {
                a[k] = helper[j++];
            }
        }
    }

    //Simple comparable method
    private boolean isLess(Integer a, Integer b) {
        return a.compareTo(b) < 0;
    }
}

/**
 * A recursive task class, which extends fork join task. Must override a returnable "compute" method.
 * Automatically determine the appropriate number of threads to initiate
 * Uses the quicksort methodology by using multithreaded concepts
 */
class QuickSortTask extends RecursiveTask<List<Integer>> {
    //The start and end of the list-sublist
    int start, end;
    //The list itself
    List<Integer> list;

    /**
     * Parametrized constructor which assigns the values of the parameters into the private values of the object
     *
     * @param start - the start of the list-sublist
     * @param end   - the end of the list-sublist
     * @param list  - the list itself
     **/
    public QuickSortTask(int start, int end, List<Integer> list) {
        this.list = list;
        this.start = start;
        this.end = end;
    }

    /**
     * The main computation performed by this task
     *
     * @return the sorted list
     **/
    @Override
    protected List<Integer> compute() {
        //Determine when to exit the recursion
        if (start >= end)
            return null;
        //An appropriate index to split the list into to sides, making the left side less than the central index, as well as making the right side larger than it
        int p = partition(start, end, list);
        //Recursively creating the object
        QuickSortTask left = new QuickSortTask(start, p - 1, list);
        QuickSortTask right = new QuickSortTask(p + 1, end, list);

        //Asynchronously executes this task
        left.fork();
        //Running the first call to compute outside of the fork join pool
        right.compute();
        //Wait for this fork to finish
        left.join();
        //Returns the sorted list
        return list;
    }

    private int partition(int start, int end, List<Integer> arr) {
        int i = start, j = end;
        int pivot = new Random().nextInt(j - i) + i;
        int t = arr.get(j);
        arr.set(j, arr.get(pivot));
        arr.set(pivot, t);
        j--;

        while (i <= j) {
            if (arr.get(i) <= arr.get(end)) {
                i++;
                continue;
            }
            if (arr.get(j) >= arr.get(end)) {
                j--;
                continue;
            }
            t = arr.get(j);
            arr.set(j, arr.get(i));
            arr.set(i, t);
            j--;
            i++;
        }
        t = arr.get(j + 1);
        arr.set(j + 1, arr.get(end));
        arr.set(end, t);
        return j + 1;
    }
}

/**
 * A recursive task class, which extends fork join task. Must override a returnable "compute" method.
 * Automatically determine the appropriate number of threads to initiate
 * Uses the mergesort methodology by using multithreaded concepts
 */
class MergeSortTask extends RecursiveTask<List<Integer>> {
    //The list itself
    private final Integer[] arr;
    //A temporary array
    private final Integer[] helper;
    //The start of the list-sublist
    private final int lo;
    //The end of the list-sublist
    private final int hi;


    /**
     * Parametrized constructor which assigns the values of the parameters into the private values of the object
     *
     * @param arr    - the list itself
     * @param helper - A temporary array
     * @param lo     - The start of the list-sublist
     * @param hi     - The end of the list-sublist
     **/
    public MergeSortTask(Integer[] arr, Integer[] helper, int lo, int hi) {
        this.arr = arr;
        this.helper = helper;
        this.lo = lo;
        this.hi = hi;
    }

    /**
     * The main computation performed by this task
     *
     * @return the sorted list
     **/
    @Override
    protected List<Integer> compute() {
        //Determine when to exit the recursion
        if (lo >= hi)
            return null;
        //An appropriate index to split the list into to sides
        int mid = lo + (hi - lo) / 2;
        //Recursively creating the object
        MergeSortTask left = new MergeSortTask(arr, helper, lo, mid);
        MergeSortTask right = new MergeSortTask(arr, helper, mid + 1, hi);
        //Executes the given list of Callable tasks
        invokeAll(left, right);
        //After each block is sorted, the function will be called to merge them
        merge(this.arr, this.helper, this.lo, mid, this.hi);
        //Returns the sorted list
        return Arrays.asList(arr);
    }

    //Given the two sub-arrays, and assuming that each array is sorted individually, this function merges those arrays into a one sorted array
    private void merge(Integer[] a, Integer[] helper, int lo, int mid, int hi) {
        if (hi + 1 - lo >= 0) System.arraycopy(a, lo, helper, lo, hi + 1 - lo);
        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid) {
                a[k] = helper[j++];
            } else if (j > hi) {
                a[k] = helper[i++];
            } else if (isLess(helper[i], helper[j])) {
                a[k] = helper[i++];
            } else {
                a[k] = helper[j++];
            }
        }
    }

    //Simple comparable method
    private boolean isLess(Integer a, Integer b) {
        return a.compareTo(b) < 0;
    }
}