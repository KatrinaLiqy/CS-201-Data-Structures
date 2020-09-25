
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
//import java.lang.String;
//import java.lang.Integer;
/**
 * 
 * Name: Katrina Li
 * This class generates a random integer list and takes the input
 * as number of elements in the list. getSum() gets the sum of integers
 * in the list and getInstanceCount keeps track of how many instance
 * are being created.
 * 
 */
public class RandomList {
    private static int MINIMUM_SIZE = -4;
    private int number = 0;
    private Random randomNumberGenerator;
    private static int count = 0;
    public List<Integer> numberList = new ArrayList<Integer>();
    
    public RandomList(int numberOfElements){
        count = count + 1;
        number = numberOfElements;
        numberList = new ArrayList<>();
        randomNumberGenerator = new Random();
        for (int i=0;i<number;i++){
            numberList.add(-1*randomNumberGenerator.nextInt((-1)*MINIMUM_SIZE+1));
        }
        
    }
    public int getSum(){
        int sum = 0;
        for(int j = 0;j < number;j++){
            sum = sum + numberList.get(j);
        }
        //System.out.println("Value for getSum: " + sum);
        return sum;
        //numberList.sum();
    }
    public static int getInstanceCount(){
        return count;
    }
    public static void main(String args[]){
        int numOfNumbers = Integer.parseInt(args[0],10);
        //int numOfNumbers = 5;
        RandomList aList = new RandomList(numOfNumbers);
        //int instanceCount = RandomList.getInstanceCount();
        System.out.println("Numbers in list: " + aList.numberList);
        System.out.println("Sum of the numbers: " + aList.getSum());
        System.out.println("How many RandomList instances did this program create? It created " + RandomList.getInstanceCount());
        
    }
}
