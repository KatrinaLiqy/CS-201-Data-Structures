//Name: Shuhang Xue, Katrina Li
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Instant;
import java.util.NoSuchElementException;

public class FlashcardPriorityQueue implements PriorityQueue<Flashcard> {

    private Flashcard[] array;
    private int numItems;

    /**
    * Creates an empty priority queue.
    */
    public FlashcardPriorityQueue(){
      numItems = 0;
      // initiate the array as size 10
      array = new Flashcard[10];
    }

    /**
    * Double the size of array when it's full.
    * The method is called every time to ensure the array has empty spaces
    */
    private void resize(){
      // When the array is fulled (only one space left)
      if (numItems == array.length -1){
        // double the size of the array
        int newLength = array.length * 2;
        Flashcard[] newArray = new Flashcard[newLength];
        // Move the data from the old array to the new array
        for (int i = 1; i < array.length; i++){
          newArray[i] = array[i];
        }
        array = newArray;
      }
    }

    /** Adds the given item to the queue.
    *
    * @param item the Flashcard object that needs to be added to the priority queue
    */
    public void add(Flashcard item){
      resize();
      numItems ++;
      boolean found = false;
      int index = numItems;
      while(index > 1 && !found){
        // get the index of parent
        int parentOfIndex = (index + 1)/3;
        if (array[parentOfIndex].compareTo(item) > 0){
          array[index] = array[parentOfIndex];
          index = (index + 1) / 3;
        }
        else {
          found = true;
        }
      }
      array[index] = item;
    }

    /** Removes the first item according to compareTo from the queue, and returns it.
     * Throws a NoSuchElementException if the queue is empty.
     *
     * @return the smallest item polled out of the priority queue
     */
    public Flashcard poll() throws NoSuchElementException{
      if (numItems == 0){
        throw new NoSuchElementException();
      }
      Flashcard polledCard = array[1];
      Flashcard item = array[numItems];
      boolean found = false;
      int index = 1;
      while((index *3-1)<=numItems && index < numItems && !found){
        int leftChild = index * 3-1;
        // the index for the smallest child
        int mini = leftChild;
        // the child in the middle
        if (leftChild+1<=numItems && array[mini].compareTo(array[leftChild+1])>0){
          mini = leftChild+1;
        }
        // the child in the right
        if (leftChild+2<=numItems && array[mini].compareTo(array[leftChild+2])>0){
          mini = leftChild+2;
        }
        if (array[mini].compareTo(item)<0){
          array[index] = array[mini];
          index = mini;
        }
        else{
          found = true;
        }
      }
      array[index] = item;
      numItems = numItems-1;
      return polledCard;
    }

    /** Returns the first item according to compareTo in the queue, without removing it.
     * Throws a NoSuchElementException if the queue is empty.
     *
     * @return the smallest item in the priority queue
     */
    public Flashcard peek() throws NoSuchElementException{
      if (numItems == 0){
        throw new NoSuchElementException();
      }
      else{
        Flashcard peekedCard = array[1];
        return peekedCard;
      }
    }

    /** Returns true if the queue is empty.
     *
     * @return whether the queue is empty or not
     */
    public boolean isEmpty(){
      return (numItems == 0);
    }

    /** Removes all items from the queue. */
    public void clear(){
      numItems = 0;
      array = new Flashcard[10];
    }

    /** The method to print out the fronttext and the time to test whether our method works.  */
    public void printArray(){
      for (int i = 1; i <= numItems; i ++){
        System.out.print("The text is: " + array[i].getFrontText());
        System.out.println("   The time is: " + array[i].getDueDate().toString());
      }
    }

    /** The method to print out the size of the array to test.  */
    public void printArrayLength(){
      System.out.println(array.length);
    }

    public static void main(String[] args){
      FlashcardPriorityQueue myPriorityQueue = new FlashcardPriorityQueue();
      Flashcard tmpCard1 = new Flashcard("2020-02-09T01:03", "Beijing",	"China");
      Flashcard tmpCard2 = new Flashcard("2020-02-08T01:03",	"Ottawa",	"Canada");
      Flashcard tmpCard3 = new Flashcard("2020-02-23T01:03",	"D.C.",	"US");
      System.out.println("---> Testing for add method");
      myPriorityQueue.add(tmpCard2);
      myPriorityQueue.add(tmpCard3);
      myPriorityQueue.add(tmpCard1);
      System.out.println("Three cards should be added to the priority queue.");
      myPriorityQueue.printArray();
      System.out.println();

      System.out.println("---> Testing for poll method");
      System.out.println("polled = " + myPriorityQueue.poll().getFrontText());
      System.out.println("The polled item should be Ottawa, since that card has the smallest time.");
      System.out.println();

      System.out.println("---> Testing for peek method");
      System.out.println("peeked = " + myPriorityQueue.peek().getFrontText());
      System.out.println("The peeked item should be Beijing, since that card has the second smallest time.");
      System.out.println();

      System.out.println("---> Testing for isEmpty method in edge cases");
      System.out.println("Polled the other two items out of the queue, then the queue should be empty");
      myPriorityQueue.poll();
      myPriorityQueue.poll();
      System.out.println("The answer is " + myPriorityQueue.isEmpty());

      System.out.println("---> Testing for poll method in edge cases");
      try {
        myPriorityQueue.poll();
      } catch (NoSuchElementException e) {
        System.out.println("The error is succesfully caught");
      }
      System.out.println();

      System.out.println("---> Testing for peek method in edge cases");
      try {
        myPriorityQueue.peek();
      } catch (NoSuchElementException e) {
        System.out.println("The error is succesfully caught");
      }
      System.out.println();

      System.out.println("---> Testing for resize method");
      System.out.println("Adding the same card for 10 times and see whether the size of the array is doubled.");
      for (int i=0; i < 10; i++){
        myPriorityQueue.add(tmpCard1);
      }
      System.out.println("Before, the length of the array should be 10, right now the length of the array is: ");
      myPriorityQueue.printArrayLength();
    }

}
