/**
 * Class for Investigation 2, exploring how changing the switch to
 * insertion sort affects runtime.
 * Note that there are many private methods here for how quicksort works.
 * You'll only need to look at the two methods named quicksort and potentially
 * the insertionSort method. You'll modify main, and you're welcome to add
 * any additional methods you'd like. 
 * 
 * @author Anna Rafferty
 */

import java.util.List;
import java.util.ArrayList;

public class Quicksort {
    public static int MIN_SIZE;

    /** 
     * Sorts an array in increasing order using quicksort.
     * Quicksort code is based off of Carrano and Henry. 
     * array will be sorted in place.
     */
    public static void quicksort(int[] array) {
        //System.out.println("minsize = " + MIN_SIZE);
        quicksort(array, 0, array.length - 1);
    }
    
    /** 
     * Helper method:
     * Sorts the part of the array a between index first and index last
     *  in increasing order using quicksort.
     */
    private static void quicksort(int[] a, int first, int last) {
        if (last - first + 1 < MIN_SIZE) {
            insertionSort(a, first, last);
        } else {
            // create the partition: Smaller | Pivot | Larger
            int pivotIndex = partition (a, first, last);
            // sort subarrays Smaller and Larger
            quicksort(a, first, pivotIndex - 1);
            quicksort(a, pivotIndex + 1, last);
        }
    }
    
    /** 
     * Orders two given array entries into ascending order
     * so that a[i] <= a[j].
     * @param a an array of objects
     * @param i an integer >= 0 and < array.length
     * @param j an integer >= 0 and < array.length
     */
    private static void order (int[] a, int i, int j) {
        if (a[i] > a [j]) {
            swap(a, i, j);
        }
    }
    
    /** 
     * Swaps the array entries array[i] and array[j]. 
     */
    private static void swap (int[] array, int i, int j) {
        int temp = array[i];
        array [i] = array[j];
        array [j] = temp;
    }
    
    /** 
     * Sorts the first, middle, and last entries of an array
     * into ascending order.
     * @param a an array of ints
     * @param first the integer index of the first array entry;
     * first >= 0 and < a.length
     * @param mid the integer index of the middle array entry
     * @param last the integer index of the last array entry;
     * last - first >= 2, last < a.length 
     */
    private static void sortFirstMiddleLast (int[] a, int first, int mid, int last){
        order(a, first, mid); // make a[first] <= a[mid]
        order(a, mid, last); // make a[mid] <= a[last]
        order(a, first, mid); // make a[first] <= a[mid]
    }
    
    /**
     * Partitions the array a so that the entries in [first, last]
     * are rearranged into a "smaller" part and a larger part.
     * The return value is the location of the pivot - all the entries
     * located to the left of that index are less than the value at the
     * pivot index, and all the entries to the right of that index are
     * greater than the pivot.
     */
    private static int partition (int[] a, int first, int last) {
        int mid = (first + last) / 2;
        sortFirstMiddleLast (a, first, mid, last);
        // Assertion: The pivot is a[mid]; a[first] <= pivot and
        // a[last] >= pivot, so do not compare these two array entries
        // with pivot.
        // move pivot to next-to-last position in array
        swap(a, mid, last - 1);
        int pivotIndex = last - 1;
        int pivot = a[pivotIndex];
        // determine subarrays Smaller = a[first..endSmaller]
        // and Larger = a[endSmaller+1..last-1]
        // such that entries in Smaller are <= pivot and
        // entries in Larger are >= pivot; initially, these subarrays are empty
        int indexFromLeft = first + 1;
        int indexFromRight = last - 2;
        boolean done = false;
        while (!done) {
            // starting at beginning of array, leave entries that are < pivot;
            // locate first entry that is >= pivot; you will find one,
            // since last entry is >= pivot
            while (a[indexFromLeft] < pivot) {
                indexFromLeft++;
            }
            // starting at end of array, leave entries that are > pivot;
            // locate first entry that is <= pivot; you will find one,
            // since first entry is <= pivot
            while (a[indexFromRight] > pivot) {
                indexFromRight--;
            }

            if (indexFromLeft < indexFromRight) {
                swap(a, indexFromLeft, indexFromRight);
                indexFromLeft++;
                indexFromRight--;
            } else {
                done = true;
            }
        } 


        // place pivot between Smaller and Larger subarrays
        swap (a, pivotIndex, indexFromLeft);
        pivotIndex = indexFromLeft;
        return pivotIndex;
    }
    
    /**
     * Sorts the specified array in increasing order using insertion sort.
     */
    public static void insertionSort(int[] a) {
        insertionSort(a, 0, a.length-1);
    }
    
    /**
     * Helper method for insertion sort to enable insertion sorting only part
     * of an array.
     */
    private static void insertionSort(int[] array, int first, int last) {    
        for(int i = first+1; i <= last; i++) {
            //i is the index in the array we're going to find the right place for
            int j = i;
            while(j > first && array[j-1] > array[j]) {
                int numToSwapOut = array[j];
                array[j] = array[j-1];
                array[j-1] = numToSwapOut;
                j--;
            }
        }
    }
    
    /**
     * Generates a pseudo-random permutation of the integers from 0 to
     * a.length - 1.
     * See p. 139 of "Seminumerical Algorithms, 2nd edition," by Donald Knuth.
     */
    public static void fillAndShuffle(int[] a) {
        // Fill the array with the integers from 0 to a.length - 1.
        int k;
        for (k = 0; k < a.length; k++) {
            a[k] = k;
        }

        // Shuffle.
        for (k = a.length - 1; k > 0; k--) {
            int swapIndex = (int)Math.floor(Math.random() * (k + 1));
            int temp = a[k];
            a[k] = a[swapIndex];
            a[swapIndex] = temp;
        }
    }
    
    /**
     * You'll put your experiments for the investigation here. The current contents
     * is just to give you an example.
     * 
     */
    public static void main(String[] args) {
        int LOWER_BOUND = 20;
        int UPPER_BOUND = 80;
        int GAP = 5;
        int TIMES = 10;
        int ARRAY_LENGTH = 10000000;//change this value if needed
        Long[][] results = new Long[TIMES+1][(UPPER_BOUND-LOWER_BOUND)/GAP]; //last row for sum/average
        for(int threshold = LOWER_BOUND; threshold < UPPER_BOUND; threshold = threshold + GAP){
            
            for (int times = 0; times < TIMES; times++){
                int[] quicksortArray = new int[ARRAY_LENGTH * times];//delete *times if you want constant arraylength!
                fillAndShuffle(quicksortArray);
                MIN_SIZE = threshold;
                long startTime = System.currentTimeMillis();
                quicksort(quicksortArray);
                long endTime = System.currentTimeMillis();
                long duration = endTime-startTime;
                //System.out.println("times = " + times);
                //System.out.println("Array length: " + quicksortArray.length + "; time to sort (ms): " + (duration));
                
                results[times][(threshold-LOWER_BOUND)/GAP] = duration;
                //System.out.println("results = " + results[times][threshold-3]);
                
            }
            
        }
        //This step calculates the average runtime for each MIN_SIZE value.
        for(int i = 0; i < (UPPER_BOUND-LOWER_BOUND)/GAP; i++){
            //System.out.println("he");
            results[TIMES][i] = 0L;
            for(int j = 0; j < TIMES; j++){
                System.out.print(results[j][i]+", ");
                //results[10][i] += results[j][i];
            }
            System.out.println("");
            //results[10][i] = results[10][i]/10;
            //System.out.println(results[10][i]);
            
        }
    }

    
}
