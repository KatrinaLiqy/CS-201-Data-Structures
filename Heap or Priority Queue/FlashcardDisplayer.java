//Name: Shuhang Xue, Katrina Li
import java.util.Scanner;
import java.time.LocalDateTime;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

public class FlashcardDisplayer{
  private FlashcardPriorityQueue myHeap;
  /**
   * Creates a flashcard displayer with the flashcards in file.
   * File has one flashcard per line. On each line, the date the flashcard
   * should next be shown is first (format: YYYY-MM-DDTHH-MM), followed by a tab,
   * followed by the text for the front of the flashcard, followed by another tab.
   * followed by the text for the back of the flashcard. You can assume that the
   * (e.g., seconds may be included). The parse method in LocalDateTime can deal
   * with these situations without any changes to your code.
   * @param file the file that contains flashcards in their string form
   */
  public FlashcardDisplayer(String file){
    myHeap = new FlashcardPriorityQueue();
    File inputFile = new File(file);
    Scanner scanner = null;
    try {
      scanner = new Scanner(inputFile);
    } catch (FileNotFoundException e) {
      System.out.println("File is missing...");
      System.err.println(e);
      System.exit(1);
    }
    while (scanner.hasNextLine()){
      String line = scanner.nextLine();
      String[] words = line.split("\t");
      // System.out.println("words[0] = " + words[0]);
      // System.out.println("words[1] = " + words[1]);
      // System.out.println("words[2] = " + words[2]);
      Flashcard tmpCard = new Flashcard(words[0],words[1],words[2]);
      myHeap.add(tmpCard);
      // myHeap.printArray();
    }
    // System.out.println("numItems" + myHeap.numItems);
    // myHeap.printArray();
  }

  /**
   * Writes out all flashcards to a file so that they can be loaded
   * by the FlashcardDisplayer(String file) constructor. Returns true
   * if the file could be written. The FlashcardDisplayer should still
   * have all of the same flashcards after this method is called as it
   * did before the method was called. However, it may be that flashcards
   * with the same exact same next display date are removed in a different order.
   * @param outFile the file that would be created and saves your result
   * @return true if outFile is a legal String for file name, false if input is illegal.
   */
  public boolean saveFlashcards(String outFile){
    FlashcardPriorityQueue newHeap = new FlashcardPriorityQueue();
    if (outFile.length()<5 || !outFile.substring(outFile.length()-4,outFile.length()).equals(".txt")){
      return false;
    }
    // System.out.println(outFile.substring(outFile.length()-4,outFile.length()));
    String fileName = outFile;
    PrintWriter newFile = null;
    try {
      newFile = new PrintWriter(fileName);
    }
    catch (FileNotFoundException e){
      System.out.println("this step should never be reached");
      System.err.println("File not found");
      return false;
    }
    while(!myHeap.isEmpty()){
      Flashcard tmpCard = myHeap.poll();
      newHeap.add(tmpCard);//saves the flashcard
      String card = tmpCard.getDueDate().toString() + "\t" + tmpCard.getFrontText() + "\t" + tmpCard.getBackText();
      newFile.println(card);
    }
    newFile.close();
    newFile.flush();
    myHeap = newHeap;

    // try{
    //   System.out.println("fhjdsfhasdjlfhsadjd");
    //   FlashcardDisplayer check = new FlashcardDisplayer(outFile);
    //
    // } catch(Exception e){
    //   System.out.println("The outFile name is wrong");
    //   return false;
    // }
    return true;
  }

  /**
   * Displays any flashcards that are currently due to the user, and
   * asks them to report whether they got each card correct. If the
   * card was correct, it is added back to the deck of cards with a new
   * due date that is one day later than the current date and time; if
   * the card was incorrect, it is added back to the card with a new due
   * date that is one minute later than that the current date and time.
   */
  public void displayFlashcards(){
    Flashcard frontCard;
    LocalDateTime currentTime = LocalDateTime.now();
    Scanner scanner = new Scanner(System.in);
    String input;

    //while the due date is before "now", need to display the card
    while (!myHeap.peek().getDueDate().isAfter(currentTime)){
      // myHeap.printArray();
      frontCard = myHeap.poll();

      System.out.println("Card:");
      System.out.println(frontCard.getFrontText());
      System.out.print("[Press return for back of card]");
      input = scanner.nextLine();
      while (!input.equals("")){//if user doesn't type in Enter
        System.out.println("[Press return for back of card]");
        input = scanner.nextLine();
      }
      System.out.println(frontCard.getBackText());
      System.out.println("Press 1 if you got the card correct and 2 if you got the card incorrect.");
      input = scanner.nextLine();
      while(!(input.equals("1")||input.equals("2"))){//if illegal input
        System.out.println("Please press 1 if you got the card correct and 2 if you got the card incorrect.");
        input = scanner.nextLine();
      }
      if (input.equals("1")){
        frontCard.addDay();//display it the next day
      }
      if (input.equals("2")){
        frontCard.addMinute();//display it the next minute
      }
      myHeap.add(frontCard);

      // frontCard = myHeap.poll();
      currentTime = LocalDateTime.now();
    }
    System.out.println("No cards are waiting to be studied!");
  }

  public static void main(String[] args){//this just assumes that the user will
    //correctly input the original file
    FlashcardDisplayer myDisplayer = new FlashcardDisplayer(args[0]);
    Scanner scanner = new Scanner(System.in);
    System.out.println("Time to practice flashcards! The computer will display your flashcards, you generate the response in your head, and then see if you got it right. The computer will show you cards that you miss more often than those you know!");
    System.out.println("Enter a command:");
    String input = scanner.nextLine();
    while(!input.equals("exit")){
      if (input.equals("quiz")){
        myDisplayer.displayFlashcards();
      }
      else if (input.equals("save")){
        System.out.println("Type a filename where you'd like to save the flashcards: ");
        input = scanner.nextLine();
        while(myDisplayer.saveFlashcards(input) == false){
          System.out.println("Type a filename that ends with .txt: ");
          input = scanner.nextLine();
        }
      }
      System.out.println("Enter a command:");
      input = scanner.nextLine();
    }
    System.out.println("Goodbye!");
    System.exit(0);//end the program

  }
}
