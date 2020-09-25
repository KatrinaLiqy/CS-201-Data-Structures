import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;


/**
*Represents a country.
*A country has multiple types of infomation. 
*The information is store in a list
*
*/

public class Country {
    

    
    private List<String> country;
    
/**
*Received a string and manipulate on that string to create a country instance.
*The string should be able to split by comma
*@param line  This is the String line that 
*             contains information of a country *
*/
    public Country(String line) {
        
        country = new ArrayList();
        String[] individual = line.split(",");
        
        for (String i:individual) {
            //each type of information is stored as a string inside a list
            country.add(i);
        }

    }
    
/**
*Find the information that is specifically required. 
*Get the information, and convert it into number to return.
*@param index  This indicate what type of 
*              information the user wants.
*@return return the information in double type. 
*        Note: when the information is "NaN", 
*        return the double number -10000.0
*
*/
    public double getInformation(int index){
        if (country.get(index) != "NaN"){
            return Double.parseDouble(country.get(index));
        }
        else{
            return -10000.0;
        }

    }

    /**
    *Change the information into certain given value.
    *@param SetInfo This is the value for change.
    *       indexNum This indicates which type of 
    *                information to change.
    */

    public void setInformation(double setInfo, int indexNum){
        
        country.set(indexNum, Double.toString(setInfo));
    }
    
    /**
    *Get and return the name of the country
    *@return the name of the country. 
    *        Note: the name should be in position 0 in the country list.
    */
    public String getName(){
        return country.get(0);
    }
    

 
    
   
    
    

    
}