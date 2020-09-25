import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.io.PrintWriter;
// import java.lang.NumberFormatException;

public class WordCounter{

  private Set<String> stopSet;

  /** The constructor for WordCounter class
   * WordCounter class counts all of the words in the file except "stop words".
   *
   */
  public WordCounter(){
     stopSet = new HashSet<String>();
     loadStopWords();
  }
  /**
   * loadStopWords method put all the words in StopWords.txt into a set.
   */
  public void loadStopWords(){
    File inputFile = new File("StopWords.txt");
    Scanner scanner = null;

    try {
        scanner = new Scanner(inputFile);
    } catch (FileNotFoundException e) {
        System.err.println(e);
        System.exit(1);
    }
    while (scanner.hasNextLine()) {
        String stopWord = scanner.nextLine();
        stopSet.add(stopWord);
    }

  }
  /**
   *
   * @param inputFilePath the name of the file of the novel
   * @param myCountMap the tree that stored all the words in the novel
   */
  public void load(String inputFilePath, WordCountMap myCountMap){
    boolean startReading = true;////false if you do not want the websites, etc.
    // in te WutheringHeights.txt. Also remove the comment in line 60-65
    File inputFile = new File(inputFilePath);
    Scanner scanner = null;
    try {
        scanner = new Scanner(inputFile);
    } catch (FileNotFoundException e) {
        System.err.println(e);
        System.exit(1);
    }
    while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        // if (line.equals("WUTHERING HEIGHTS")){
        //   startReading = true;
        // }
        // if (line.equals("***END OF THE PROJECT GUTENBERG EBOOK WUTHERING HEIGHTS***")){
        //   startReading = false;
        // }
        if (!line.equals("\n")&&!line.equals("") && startReading){
          line = line.replaceAll("[^a-zA-Z']", " ");//get rid of syntaxes in the line

          String[] words = line.split(" ");
          for (String word: words){
            if (!word.equals("")){

              word = word.toLowerCase();
              // check if word is in StopWord.txt or is just a '
              if (!stopSet.contains(word) && !word.equals("'")){
                // get rid of ' at front of word
                while (word.substring(0,1).equals("'")){
                  word = word.substring(1);
                }
                // get rid of ' at end of word
                while (word.length() >= 2 && word.substring(word.length()-1, word.length()).equals("'")){
                  word = word.substring(0,word.length()-1);
                }
                // check again if word is in StopWord.txt
                if (!stopSet.contains(word)){
                  myCountMap.incrementCount(word);
                }
              }
            }
          }
        }
    }
    scanner.close();
  }
  public static void main(String args[]){
    WordCountMap myCountMap = new WordCountMap();
    WordCounter wordCounter = new WordCounter();
    wordCounter.load(args[0], myCountMap);
    List<WordCount> wordList = myCountMap.getWordCountsByCount();
    if (args.length == 1){
      for (int i = 0; i < wordList.size(); i++){
        WordCount tmpWordCount = wordList.get(i);
        System.out.print(tmpWordCount.getWord());
        System.out.println(":" + tmpWordCount.getCount());
      }
    }
    if (args.length == 3){
      int numberOfWords = Integer.parseInt(args[1]) - 1;
      String cloud;
      try{
         cloud = WordCloudMaker.getWordCloudHTML("test", wordList.subList(0, numberOfWords));
      }
      catch(IndexOutOfBoundsException e){
         cloud = WordCloudMaker.getWordCloudHTML("test", wordList);
      }

      String fileName = args[2];
      PrintWriter newFile = null;
      try {
        newFile = new PrintWriter(fileName);
      }
      catch (FileNotFoundException e){
        System.err.println("File not found");
      }
      newFile.println(cloud);

      newFile.close();
      newFile.flush();

    }

  }
}
