//Name: Katrina Li
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
// import java.util.HashSet;

/**
 * In class lab for learning about hash code functions and collisions.
 * @author arafferty
 * @author YOUR NAME HERE
 */
public class HashCodeCalculations {

    /**
     * Always returns 0.
     */
    public static int hashCode0(String s) {
        return 0;
    }

    /**
     * Read through the code and change this comment to what this hash code function
     * does. Note that casting a character to an integer returns its ASCII value - there
     * are 128 characters, and each has a distinct number from 0-127.
     * this will have 128 results of hash values
     * one collision for tan and tiger
     */
    public static int hashCode1(String s) {
        if(s.isEmpty()) {
            return 0;
        } else {
            return (int) s.charAt(0);
        }
    }

    /**
     * Read through the code and change this comment to what this hash code function
     * does.
     * convert all character into int, sum them up
     * one collision: ant, tan (may have more)
     */
    public static int hashCode2(String s) {
        int hashCode = 0;
        for(int i = 0; i < s.length(); i++) {
            hashCode += (int) s.charAt(i);
        }
        return hashCode;
    }

    /**
     * Read through the code and change this comment to what this hash code function
     * does. Hint: You might think about how you would write an integer as a sum that
     * uses its digits to get a better understanding of what's happening here.
     * (Note that this isn't quite the same thing, but it's similar.)
     * this method treats each string as a integer of base 129
     * no collision.
     */
    public static int hashCode3(String s) {
        int hashCode = 0;
        for(int i = 0; i < s.length(); i++) {
            hashCode = 129*hashCode + (int) s.charAt(i);
        }
        return hashCode;
    }



    /**
     * Implement this function so it works the way we talked about in class.
     * Compression function that takes a hash code (positive or negative) and
     * the number of buckets we have to use in our hash table, and compresses
     * the hash code into the range [0, numberOfBuckets).
     */
    public static int compressToSize(int hashCode, int numberOfBuckets) {
        int index = hashCode%numberOfBuckets;
        if (index < 0) {
          index = index + numberOfBuckets;
        }
        return index;
    }

    /**
     * Counts the number of buckets that have no words stored at them - i.e.,
     * they have value 0 - and calculates what proportion of the total buckets
     * that is.
     */
    public static double proportionOfBucketsWithNoWords(int[] buckets) {
        int emptyBucketCount = 0;
        for(int i = 0; i < buckets.length; i++) {
            if(buckets[i] == 0) {
                emptyBucketCount++;
            }
        }
        return emptyBucketCount*1.0/buckets.length;
    }


    /**
     * Returns the maximum value in a single bucket
     */
    public static int getMaxBucketValue(int[] buckets) {
        int max = -1;//Safe starting value since all buckets[i] should be >= 0
        for(int i = 0; i < buckets.length; i++) {
            if(buckets[i] > max) {
                max = buckets[i];
            }
        }
        return max;
    }

    /**
     * Returns the average number of words in each non-empty bucket
     */
    public static double getAverageInNonEmptyBuckets(int[] buckets) {
        int totalCount = 0;
        int totalNonEmpty = 0;
        for(int i = 0; i < buckets.length; i++) {
            totalCount += buckets[i];
            if(buckets[i] != 0) {
                totalNonEmpty++;
            }
        }
        return totalCount*1.0/totalNonEmpty;
    }


    /**
     * Implement this method so that it calculates how many words would be placed
     * in each bucket in the array.
     * Each individual word should be counted only once (i.e., if "the" occurs
     * 501 times in the file, you should only hash it once, rather than thinking
     * of it as causing 500 collisions).
     * @param numBuckets number of spots to include in the array
     * @param file file to read from
     * @param hashCodeFunctionToUse which of the hash functions to use; see lab
     *                              description for more details
     * @return an array that indicates how many different words are place in index 0, 1, etc.
     */
    public static int[] collisionCounter(int numBuckets, String file, int hashCodeFunctionToUse) {
        int[] array = new int[numBuckets];
        Set<String> appearedWords = new HashSet<String>();
        //Initialize the variables you'll need to count collisions (an array, a set)

        try {
            Scanner scanner =  new Scanner(new File(file));
        //Write your code for counting collisions here.
          while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            if (!line.equals("\n")&&!line.equals("")){
              line = line.replaceAll("[^a-zA-Z']", " ");//get rid of syntaxes in the line

              String[] words = line.split(" ");
              for (String word: words){

                if (!word.equals("")){

                  word = word.toLowerCase();
                  // check if word is in StopWord.txt or is just a '
                  if (!word.equals("'")){
                    // get rid of ' at front of word
                    while (word.substring(0,1).equals("'")){
                      word = word.substring(1);
                    }
                    // get rid of ' at end of word
                    while (word.length() >= 2 && word.substring(word.length()-1, word.length()).equals("'")){
                      word = word.substring(0,word.length()-1);
                    }
                    //System.out.print(word + "  ");
                    if(!appearedWords.contains(word)){
                      appearedWords.add(word);
                      int hashValue = 0;
                      if (hashCodeFunctionToUse == 0){
                        hashValue = hashCode0(word);
                      } else if (hashCodeFunctionToUse == 1){
                        hashValue = hashCode1(word);
                      } else if (hashCodeFunctionToUse == 2){
                        hashValue = hashCode2(word);
                      } else if (hashCodeFunctionToUse == 3){
                        hashValue = hashCode3(word);
                      } else if (hashCodeFunctionToUse == -1){
                        hashValue = line.hashCode();
                      }
                      int index = compressToSize(hashValue,numBuckets);
                      array[index] = array[index]+1;
                    }
                  }

                }

              }
            }
          }
          scanner.close();
        }catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        // for (int i = 0; i < numBuckets ; i++){
        //
        // }
        return array;//Change this line to return your count of collisions
    }

    public static void main(String[] args) {
        String words = "words.txt";
        String hound = "HoundOfTheBaskervilles.txt";
        //String inputFilePath = words;
        //File inputFile = new File(inputFilePath);
        for (int fToUse = -1; fToUse < 4; fToUse++){
          int[] result = collisionCounter(196613, words, fToUse);
          int numEmptyBucket = (int)(proportionOfBucketsWithNoWords(result) * 196613);
          double capacity = getAverageInNonEmptyBuckets(result);
          System.out.println("For words with 196613 buckets and hashCodeFunction "+ fToUse +
          ", number of Empty Buckets = " + numEmptyBucket + ", average capacity = " + capacity);

        }
        for (int fToUse = -1; fToUse < 4; fToUse++){
          int[] result = collisionCounter(196613, hound, fToUse);
          int numEmptyBucket = (int)(proportionOfBucketsWithNoWords(result) * 196613);
          double capacity = getAverageInNonEmptyBuckets(result);
          System.out.println("For HoundOfTheBaskervilles with 196613 buckets and hashCodeFunction "+ fToUse +
          ", number of Empty Buckets = " + numEmptyBucket + ", average capacity = " + capacity);

        }
        for (int fToUse = -1; fToUse < 4; fToUse++){
          int[] result = collisionCounter(200000, words, fToUse);
          int numEmptyBucket = (int)(proportionOfBucketsWithNoWords(result) * 200000);
          double capacity = getAverageInNonEmptyBuckets(result);
          System.out.println("For words with 200000 buckets and hashCodeFunction "+ fToUse +
          ", number of Empty Buckets = " + numEmptyBucket + ", average capacity = " + capacity);

        }
        for (int fToUse = -1; fToUse < 4; fToUse++){
          int[] result = collisionCounter(200000, hound, fToUse);
          int numEmptyBucket = (int)(proportionOfBucketsWithNoWords(result) * 200000);
          double capacity = getAverageInNonEmptyBuckets(result);
          System.out.println("For HoundOfTheBaskervilles with 200000 buckets and hashCodeFunction "+ fToUse +
          ", number of Empty Buckets = " + numEmptyBucket + ", average capacity = " + capacity);

        }
        //int[] result = collisionCounter(196613, words, -1);
        // for (int i = 0; i < result.length; i++){
        //   System.out.print(result[i] + " ");
        // }



    }


}
