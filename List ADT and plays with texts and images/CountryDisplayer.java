import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;


/**
*Read through a file and represent countries in one list.
*Special Note: the type of information is slightly different as the headers appear on top of the file.
*              When typing in the terminal, refer each headers as following:
*CO2emission
*TotalGreenHouseGas
*Access2E
*RenewEConsumption
*Area
*PopGrowth
*Pop
*UrbanPopGrowth
*
*/


public class CountryDisplayer {
    
    private static List<Country> allCountries = new ArrayList<Country>();
    
    
    /**
    *The method returns a list that contains instance in Country type.
    *@return allCountries This is an arraylist.
    *
    */
    public List getList() {
        
        return allCountries;
    }


    
    /**
    *This method reads through a file and fill up the arraylist by adding country object into it.
    *@param inputFilePath This is the file name in string format.
    *
    */
    
    public static void loadCountries(String inputFilePath) {
        
        File inputFile = new File(inputFilePath);
        Scanner scanner = null;
        try {
            //Deal with the situation when file isn't found.
            scanner = new Scanner(inputFile);
        } catch (FileNotFoundException e) {
            System.err.println(e);
            System.exit(1);
        } 
        
        //This line jump through the first line of the file which contains only headers
        scanner.nextLine();
        
        //This while loop read through all lines, convert it into a country, and add it to the arraylist.
        while (scanner.hasNextLine()) {
            
            String line = scanner.nextLine();
            allCountries.add(new Country(line));
        }
        scanner.close();
    }
    
    
    /**
    *This method converts the type of information in string form into int for further use.
    *@param index This is the type of information in question.
    *             The index is in String type.
    *@return The index number in int type
    *Special Note: the type of information is slightly different as the headers appear in the file.
    *
    */
    public int findIndex(String index){
        int indexNum = 0;
        
        if (index.equals("CO2emission")){
            indexNum = 1;
        }
        else if(index.equals("TotalGreenHouseGas")){
            indexNum = 2;
        }
        else if(index.equals("Access2E")){
            indexNum= 3;
        }
        else if(index.equals("RenewEConsumption")){
            indexNum = 4;
        }
        else if(index.equals("Area")){
            indexNum = 5;
        }
        else if(index.equals("PopGrowth")){
            indexNum = 6;
        }
        else if(index.equals("Pop")){
            indexNum = 7;
        }
        else if(index.equals("UrbanPopGrowth")){
            indexNum = 8;
        }
        else{
            System.out.println("invalid indicator.");
        }
        return indexNum;
    }
    
    /**
    *This is the method that sorts the list according to a given type of information.
    *Countries with smallervalue will appear in the front.
    *@param indexNum This is the int that indicate what type of information should be the criterion.
    */
    public static void sortCountryList(int indexNum){
    
        for (int i = 0; i < allCountries.size(); i++){
            int minimumIndex = i;
            for (int j = i; j < allCountries.size(); j++){
                if(allCountries.get(j).getInformation(indexNum) < allCountries.get(minimumIndex).getInformation(indexNum)){
                    minimumIndex = j;
                }
            }
            Country tmp = allCountries.get(i);
            
            if (minimumIndex > i) {
                allCountries.set(i,allCountries.get(minimumIndex));
                allCountries.set(minimumIndex, tmp);  
            }
            
        }
       
    }
    
    /**
    *This is a method that show all countries in the terminal in a required way (ascending or descending).
    *Those whose value in question is "NaN" will not appear.
    *@param way This is either "leastToGreatest" or "greatestToLeast" 
    *       indexNum This is the indicator of what type of information is the criterion for sorting and comparison.
    *
    */
    public static void displayTextCountries(String way, int indexNum){
        
            if (way.equals( "leastToGreatest")){
                
                for (int i  = 0; i < allCountries.size(); i++){// for every country
                    if (allCountries.get(i).getInformation(indexNum) != -10000.0){ // if data != NaN
                        System.out.print(allCountries.get(i).getName()); //print name
                        for (int j = 1;j < 9;j++){ // for every 9 data
                            System.out.print(" " + allCountries.get(i).getInformation(j));
                            
                        }
                        System.out.print("\n");
                    }

                }
            }
            else{
                for (int i = 0; i < allCountries.size(); i++){
                    if (allCountries.get(i).getInformation(indexNum) != -10000.0){
                        System.out.print(allCountries.get(allCountries.size()-i-1).getName() + " ");
                        for (int j = 1;j<9;j++){
                            System.out.print(allCountries.get(allCountries.size()-i-1).getInformation(j));
                        }
                        System.out.print("\n");
                    }
                }
            }
    }
    
    /**
    *This method display ten countries required value in bar chart.
    *@param way This is either "leastToGreatest" or "greatestToLeast" 
    *       indexNum This is the indicator of what type of information is the criterion for sorting and comparison.
    *       indicator1 This is the indication of the type of information that is sorted by.
    *       indicator2 This is the indacation of the another type of information about a country.
    */
    
    public void displayCountryGraph(String way, int indexNum,String indicator1, String indicator2) {
        
        BarChart chart= new BarChart("Top 10 " + indicator1, "Country", "Value");
       
        int acc = 0;
        int index = -1;
        int index2 = findIndex(indicator2);
        
        if (way.equals("leastToGreatest")){
            while (acc<10){
                index = index + 1;
                if (allCountries.get(index).getInformation(indexNum) != -10000.0){
                        chart.addValue(allCountries.get(index).getName(), allCountries.get(index).getInformation(indexNum), indicator1);

                        chart.addValue(allCountries.get(index).getName(), allCountries.get(index).getInformation(index2), indicator2);
                        acc = acc + 1;
                    }
            }
        }
        else{
            while (acc<10){
                index = index + 1;
                if (allCountries.get(allCountries.size()-index-1).getInformation(indexNum) != -10000.0){
                    
                    chart.addValue(allCountries.get(allCountries.size()-index-1).getName(), allCountries.get(allCountries.size()-index-1).getInformation(indexNum), indicator1);

                    chart.addValue(allCountries.get(allCountries.size()-index-1).getName(), allCountries.get(allCountries.size()-index-1).getInformation(index2), indicator2);
                    
                    acc = acc + 1;
                }    
                    
            } 
        
        }
        chart.displayChart();
    }
    
    
    public static void main (String[] args) {
          
          CountryDisplayer myWorld = new CountryDisplayer();
          
         
              int indexNum = myWorld.findIndex(args[1]);
              myWorld.loadCountries(args[0]);
              myWorld.sortCountryList(indexNum);
            //This deal with the situation when there are three items in the command line.
            if (args.length == 3) {
                myWorld.displayTextCountries(args[2], indexNum);
                
            }
            //This deal with the situation when there are four items in the command line.
            else if(args.length == 4) {
                myWorld.displayCountryGraph(args[2],indexNum,args[1], args[3]);

            }
            //This deal with the invalid command line input.
            else {
                System.out.println("invaid command line input");
            }
       
  }
    
   
}