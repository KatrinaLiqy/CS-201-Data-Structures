public class WordCount{
  private String word;
  private int count;

/**
 * Constructor of WordCount class
 * @param newWord the English word.
 * @param newCount the number that the word appears in the file.
 */
  public WordCount(String newWord, int newCount){
    word = newWord;
    count = newCount;
  }
/**
 * Gets the word stored by this WordCount
 */
  public String getWord(){
    return word;
  }

  /**
   * Gets the count stored by this WordCount
   */
   public int getCount(){
     return count;
   }

}
