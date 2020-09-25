import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.net.URLDecoder;
import java.io.UnsupportedEncodingException;
import java.util.Queue;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Random;

/**
 *
 * @author katrina
 */
public class RadixSort {
    //private static List<ArrayDeque> wordQueue = new ArrayList<ArrayDeque>();
    //private static Queue<String> bigQueue= new ArrayDeque<String>();
    //private static List<ArrayList> msdList = new ArrayList<ArrayList>();
    private static Random randomNumberGenerator;
    /*
     * some values for testing radixSort and msdRadixSort.
    */
    
    

    public RadixSort(){
        
    }
    public static List load(int length){
        randomNumberGenerator = new Random();
        File inputFile = new File("words.txt");
        Scanner scanner = null;
        List<String> words = new ArrayList<String>();
        List<String> allWords = new ArrayList<String>();
        try {
            scanner = new Scanner(inputFile);
        } catch (FileNotFoundException e) {
            System.err.println(e);
            System.exit(1);
        }
        while (scanner.hasNextLine()) {
            
            allWords.add(scanner.nextLine());
        }
        for (int i = 0; i < length; i++){
            int value = randomNumberGenerator.nextInt(235886);
            words.add(allWords.get(value).toLowerCase());
        }
        return words;
    }
    /**
     * Uses standard least significant digit radix sort to sort the
     * words.
     *
     * @param A list of words. Each word contains only lower case letters in a-z.
     * @return A list with the same words as the input argument, in sorted order
     */
    public static List<String> radixSort(List<String> words){
        Queue<String> bigQueue= new ArrayDeque<String>();
        int length = lengthOfLongestWord(words);
        int c;
        char ch;
        List<ArrayDeque> wordQueue = new ArrayList<ArrayDeque>();
        
        //there are 27 lists because there is one for no letters.
        for(int l=0; l<27;l++){
            wordQueue.add(new ArrayDeque<String>());
        }
        for (int i = 0; i < words.size();i++){
            bigQueue.add(words.get(i));
            
        }
        
        //loop length number of times
        for(int i = length-1; i >= 0; i--){
            while(!bigQueue.isEmpty()){
                //System.out.println(bigQueue);
                String nowAt = bigQueue.poll();
                
                //if the word is short
                if (nowAt.length() < i+1){
                    wordQueue.get(0).add(nowAt);
                }
                else{
                    ch = nowAt.charAt(i);
                    c = Character.getNumericValue(ch)-9;
                    wordQueue.get(c).add(nowAt);
                 }
            }
            
            //put them back to a big queue. I used queue here just to save space.
            for (int j = 0;j<=26;j++){
                while(!wordQueue.get(j).isEmpty()){
                    bigQueue.add((String) wordQueue.get(j).poll());
                }
            }
        }
        List<String> sortedWord = new ArrayList<String>();
        //change the adt to a list and return
        while(!bigQueue.isEmpty()){
            sortedWord.add(bigQueue.poll());
        }
        return sortedWord;
        //Queue<String> vertexToProcess= new ArrayDeque<Integer>();
    }

    /**
     * A variation on radix sort that sorts the words into buckets by their initial letter,
     * and then uses standard radix sort to separately sort each of the individual 
     * buckets. Recombines at the end to get a fully sorted list.
     *
     * @param words A list of words. Each word contains only lower case letters in a-z.
     * @return msdResult A list with the same words as the input argument, in sorted order
     */
    public static List<String> msdRadixSort(List<String> words){
        List<ArrayList> msdList = new ArrayList<ArrayList>();
        for(int c=0; c<26;c++){
            msdList.add(new ArrayList<String>());
        }
        //bucketing. Separating the big list to 26 small lists based on initial
        for(String s:words){
            int c = Character.getNumericValue(s.charAt(0))-10;
            msdList.get(c).add(s);
        }
        List<String> msdResult = new ArrayList<String>();
        for(int i = 0;i<26;i++){
            msdResult.addAll(radixSort(msdList.get(i)));
        }
        return msdResult;
        
    }
    /**
     * Search for how many letters the longest word has in the list words
     * @param words the unsorted word list
     * @return the length of longest word so that the sorting algorithm knows 
     * how many for loops it should run.
     */
    public static int lengthOfLongestWord(List<String> words){
        int length = 0;
        String longestWord = "";
        for (String s:words){
            if(s.length() > length){
                length = s.length();
                longestWord = s;
            }
        }
        return length;
    }
    public static void main(String args[]){
        int TRIALS = 3;//the number of times you want for each case
        int LOWER_BOUND = 100000;
        int UPPER_BOUND = 1000000;
        int GAP = 100000;
        List<String> wordList = new ArrayList<String>();
        List<Long> result = new ArrayList<Long>();
        List<Long> msdResult = new ArrayList<Long>();
        
        //testing the algorithm. Set different values at the top.
        for (int wordNum = LOWER_BOUND; wordNum < UPPER_BOUND; wordNum = wordNum + GAP){
            
            for (int trials = 0; trials < TRIALS; trials++){
                
                List<String> theList = load(wordNum);
                long startTime = System.currentTimeMillis();
                msdRadixSort(theList);
                long endTime = System.currentTimeMillis();
                //theList.clear();
                long duration = endTime-startTime;
                System.out.println("list size = " + wordNum + " msdRadixSort: " + duration);
                
                msdResult.add(duration);
                //System.out.println("the original list = " + theList);
                //System.out.println("the Sorted list = " + theSortedList);
            }
            for (int trials = 0; trials < TRIALS; trials++){
                
                List<String> theList = load(wordNum);
                long startTime = System.currentTimeMillis();
                radixSort(theList);
                long endTime = System.currentTimeMillis();
                //theList.clear();
                long duration = endTime-startTime;
                System.out.println("list size = " + wordNum + " regularRadixSort: " + duration);
                //result.add(duration);
            }
            
        }
        //System.out.println("msdResult = " + msdResult);
        //System.out.println("result = " + result);
    }
}