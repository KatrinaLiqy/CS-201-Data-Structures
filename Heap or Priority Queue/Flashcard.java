//Name: Shuhang Xue, Katrina Li
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;


/**
 * Creates a new flashcard with the given dueDate, text for the front
 * of the card (front), and text for the back of the card (back).
 * dueDate must be in the format YYYY-MM-DDTHH-MM. For example,
 * 2019-11-04T13:03 represents 1:03PM on November 4, 2019. It's
 * okay if this method crashes if the date format is incorrect.
 * In the format above, the time may be omitted, or the time may be
 * more precise (e.g., seconds or milliseconds may be included).
 * The parse method in LocalDateTime can deal with these situations
 *  without any changes to your code.
 */
public class Flashcard implements Comparable<Flashcard>{

  private String frontText;
  private String backText;
  private LocalDateTime time;


  public Flashcard(String dueDate, String front, String back){
    frontText = front;
    backText = back;
    time = LocalDateTime.parse(dueDate);
  }

  /**
   * Gets the text for the front of this flashcard.
   */
  public String getFrontText(){
    return frontText;
  }

  /**
   * Gets the text for the Back of this flashcard.
   */
  public String getBackText(){
    return backText;
  }

  /**
   * Gets the time when this flashcard is next due.
   */
  public LocalDateTime getDueDate(){
    return time;
  }

  /**
   * Add a minute to the LocalDateTime so dueDate is postponed.
   */
  public void addMinute(){
    time = LocalDateTime.now().plusMinutes(1).truncatedTo(ChronoUnit.SECONDS);
    // time = time.plusMinutes(1);
  }

  /**
   * Add a day to the LocalDateTime so dueDate is postponed.
   */
  public void addDay(){
    time = LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS);
    System.out.println("time: " + time.toString());
  }

  /**
   * Compare the dueDate of the object.
   * @param another the other Flashcard you want to compare with
   * @return int <0 if object precedes another, >0 if another precedes object
   */
  public int compareTo(Flashcard another){
    return this.time.compareTo(another.getDueDate());
  }

}
