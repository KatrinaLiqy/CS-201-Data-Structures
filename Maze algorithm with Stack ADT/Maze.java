import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

/**
* Maze represents a maze that can be navigated. The maze
* should indicate its start and end squares, and where the
* walls are. 
*
* Eventually, this class will be able to load a maze from a
* file, and solve the maze.
* The starter code has part of the implementation of load, but
* it does not read and store the information about where the walls of the maze are.
*
*/
public class Maze { 
    //Number of rows in the maze.
    private int numRows;
    
    //Number of columns in the maze.
    private int numColumns;
    
    //Grid coordinates for the starting maze square
    private int startRow;
    private int startColumn;
    
    //Grid coordinates for the final maze square
    private int finishRow;
    private int finishColumn;
    
    //**************YOUR CODE HERE******************
    //You'll likely want to add one or more additional instance variables
    //to store the squares of the maze
    
    private List<MazeSquare> myMaze;
    private char descriptor;
    private boolean isSolved = false;
    private List<Integer> path= new ArrayList<Integer>();
    /**
     * Creates an empty maze with no squares.
     */
    public Maze(String file) {
        myMaze = new ArrayList<MazeSquare> ();
        load(file);
        
    } 
    
    /**
     * Loads the maze that is written in the given fileName.
     *check if the start and end points are out of range and if the number of squares is
     *inconsistent with the number of rows and columns specified at the beginning of the file.
     * Check if each character is legal.
     * 
     *
     *@param fileName This is the file's name
     *@return true if load successfully processes everthing.
     */
    public boolean load(String fileName) { 
        Scanner scanner = null;
        try {
            //Open a scanner to read the file
            scanner = new Scanner(new File(fileName));
            numColumns = scanner.nextInt();
            numRows = scanner.nextInt();
            startColumn = scanner.nextInt();
            startRow = scanner.nextInt();
            finishColumn = scanner.nextInt();
            finishRow = scanner.nextInt();
            
           
            
            //Check if the start or finish squares are out of bounds
            if(!isInRange(startRow, 0, numRows) 
                    || !isInRange(startColumn, 0, numColumns)
                    || !isInRange(finishRow, 0, numRows) 
                    || !isInRange(finishColumn, 0, numColumns)) {
                System.err.println("Start or finish square is not in maze.");
                scanner.close();
                return false;
            }
            
             
            //this checks if the number of column is inconsistent with the number of rows and columns specified at the beginning of the file and whether the descriptor is legal.
            for (int i = 0; i < numRows; i++){
                String sth = scanner.next();
                
                if (sth.length() != numColumns){
                    System.err.println("the number of squares is inconsistent with the number of rows and columns specified.");
                }
                for(int j = 0; j < numColumns; j++){
                    descriptor = sth.charAt(j);
                    //check if there are any squares that don't have *, 7, _, or | as a descriptor
                    if (!MazeSquare.isAllowedCharacter(descriptor)){
                        System.err.println("Illegal identifier for a maze square.");
                    }
                    //create new MazeSquare variables. Add them to myMaze.
                    else{
                        myMaze.add(new MazeSquare(descriptor, i, j));
                    }
                        
                }
                
            }
            //this checks if the number of row is inconsistent with the number of rows and columns specified at the beginning of the file and whether the descriptor is legal.
            if(scanner.hasNextLine()){
                System.err.println("the number of squares is inconsistent with the number of rows and columns specified.");
            }
            
        } catch(FileNotFoundException e) {
            System.err.println("The requested file, " + fileName + ", was not found.");
            return false;
        } catch(InputMismatchException e) {
            System.err.println("Maze file not formatted correctly.");
            scanner.close();
            return false;
        } 
        
        return true;
    } 
    
    /**
     * Returns true if number is greater than or equal to lower bound
     * and less than upper bound. 
     * @param number
     * @param lowerBound
     * @param upperBound
     * @return true if lowerBound ≤ number < upperBound
     */
    private static boolean isInRange(int number, int lowerBound, int upperBound) {
        return number < upperBound && number >= lowerBound;
    }
    
    /**
     * Prints the maze with the start and finish squares marked. Does
     * not include a solution.
     * If there is a solution, print the path of a solution marked.
     */
    
    public void print() {
        //We'll print off each row of squares in turn.
        for(int row = 0; row < numRows; row++) {
            
            //Print each of the lines of text in the row
            for(int charInRow = 0; charInRow < 4; charInRow++) {
                //Need to start with the initial left wall.
                if(charInRow == 0) {
                    System.out.print("+");
                } else {
                    System.out.print("|");
                }
                
                for(int col = 0; col < numColumns; col++) {
                    MazeSquare curSquare = this.getMazeSquare(row, col);
                    if(charInRow == 0) {
                        //We're in the first row of characters for this square - need to print
                        //top wall if necessary.
                        if(curSquare.hasTopWall()) {
                            System.out.print(getTopWallString());
                        } else {
                            System.out.print(getTopOpenString());
                        }
                    } else if(charInRow == 1 || charInRow == 3) {
                        //These are the interior of the square and are unaffected by
                        //the start/final state.
                        if(curSquare.hasRightWall()) {
                            System.out.print(getRightWallString());
                        } else {
                            System.out.print(getOpenWallString());
                        }
                    } else {
                        //We must be in the second row of characters.
                        //This is the row where start/finish should be displayed if relevant
                        
                        //Check if we're in the start or finish state
                        if(startRow == row && startColumn == col) {
                            System.out.print("  S  ");
                        } else if(finishRow == row && finishColumn == col) {
                            System.out.print("  F  ");
                        } 
                        else if(isPath(row, col)) {
                            System.out.print("  *  ");
                        }
                        else {
                            System.out.print("     ");
                        }
                        if(curSquare.hasRightWall()) {
                            System.out.print("|");
                        } else {
                            System.out.print(" ");
                        }
                    } 
                }
                
                //Now end the line to start the next
                System.out.print("\n");
            }           
        }
        
        //Finally, we have to print off the bottom of the maze, since that's not explicitly represented
        //by the squares. Printing off the bottom separately means we can think of each row as
        //consisting of four lines of text.
        printFullHorizontalRow(numColumns);
    }
    /**
    * This is a method that determines if the square is in the path of solution.
    * @ param row signify what row the print method is working on.
    * @ param col signify what column the print method is working on.
    * @ return true when the square is in the path of solution.
    */
    public boolean isPath(int row, int col) {
        for (int i=0;i < path.size()/2;i++){
            if (row == path.get(2*i)&& col == path.get(2*i+1)){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Prints the very bottom row of characters for the bottom row of maze squares (which is always walls).
     * numColumns is the number of columns of bottom wall to print.
     */
    private static void printFullHorizontalRow(int numColumns) {
        System.out.print("+");
        for(int row = 0; row < numColumns; row++) {
            //We use getTopWallString() since bottom and top walls are the same.
            System.out.print(getTopWallString());
        }
        System.out.print("\n");
    }
    
    /**
     * Returns a String representing the bottom of a horizontal wall.
     */
    private static String getTopWallString() {
        return "-----+";
    }
    
    /**
     * Returns a String representing the bottom of a square without a
     * horizontal wall.
     */
    private static String getTopOpenString() {
        return "     +";
    }
    
    /**
     * Returns a String representing a left wall (for the interior of the row).
     */
    private static String getRightWallString() {
        return "     |";
    }
    
    /**
     * Returns a String representing no left wall (for the interior of the row).
     */
    private static String getOpenWallString() {
        return "      ";
    }
    
    /**
     * This method returns each square in the list myMaze.
     *  @ param row This is the row of the square.
     *  @ param col This is the column of the square.
     *  @ return Returns the mazeSquare object.
     * 
     */
    public MazeSquare getMazeSquare(int row, int col) {

        return myMaze.get(row*numColumns+col);
    }
    
    /**
    * This method solves the maze, if there is a solution, store the path in stack "solution".
    * Finally, it converts the path into a list that indicates the row and column for the
    * print method to print.
    * @ return the stack of the solution.
    */
    public Stack<MazeSquare> getSolution(){
        
        Stack<MazeSquare> solution = new MysteryStackImplementation();
        solution.push(getMazeSquare(startRow, startColumn));
        getMazeSquare(startRow, startColumn).changeVisited(true);
        
        while (!solution.isEmpty() && !(solution.peek().getRow() == finishRow && solution.peek().getColumn() == finishColumn)){
            int curRow = solution.peek().getRow();
            int curCol = solution.peek().getColumn();
            
            //This examines the possibility of going up
            if (curRow != 0 && getMazeSquare(curRow-1, curCol).isVisited() == false && getMazeSquare(curRow, curCol).hasTopWall() == false) {
                solution.push(getMazeSquare(curRow-1, curCol));
                getMazeSquare(curRow-1, curCol).changeVisited(true);}
            
            //This examines the possibility of going down
            else if(curRow < numRows-1 && getMazeSquare(curRow+1, curCol).isVisited() == false && getMazeSquare(curRow+1, curCol).hasTopWall() == false) {
                solution.push(getMazeSquare(curRow+1, curCol));
                getMazeSquare(curRow+1, curCol).changeVisited(true);}
            
            //This examines the possibility of going left
            else if(curCol != 0 && getMazeSquare(curRow, curCol-1).isVisited() == false && getMazeSquare(curRow, curCol-1).hasRightWall() == false) {
                solution.push(getMazeSquare(curRow, curCol-1));
                getMazeSquare(curRow, curCol-1).changeVisited(true);}
            
            //This examines the possibility of going right
            else if(curCol < numColumns-1 && getMazeSquare(curRow, curCol+1).isVisited() == false && getMazeSquare(curRow, curCol).hasRightWall() == false) {
                solution.push(getMazeSquare(curRow, curCol+1));
                getMazeSquare(curRow, curCol+1).changeVisited(true);}
            
            else {
                //When no possible mazesquare to go to:
                
                solution.pop();
            }
        }
        if (solution.isEmpty()){
            System.out.println("The maze is unsolvable.");
        }
        else if(solution.peek().getRow() == finishRow && solution.peek().getColumn() == finishColumn){
            System.out.println("This maze has solution.");
            isSolved = true;
            while (!solution.isEmpty()) {
                MazeSquare solution1 = solution.pop();
                path.add(solution1.getRow());
                path.add(solution1.getColumn());
            }
            
        }
        
        return solution;
    }
    
    
    /**
     * You should modify main so that if there is only one
     * command line argument, it loads the maze and prints it
     * with no solution. If there are two command line arguments
     * and the second one is --solve,
     * it should load the maze, solve it, and print the maze
     * with the solution marked. No other command lines are valid.
     */ 
    public static void main(String[] args) { 
        Maze outPutMaze = new Maze(args[0]);
        
        if(args.length==2){
            if(args[1].equals("[--solve]")){

                outPutMaze.getSolution();
            }
        }
        outPutMaze.print();
    } 
}