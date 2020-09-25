/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * Name: Katrina Li
 * This class keeps generating a RandomList object until the sum of
 * integers is less than -10. And then this class prints the information.
 */
public class RandomListClient {
    public static void main(String[] args){
        RandomList myList = new RandomList(5);
        while(myList.getSum() >= -10){
            myList = new RandomList(5);
        }
        System.out.println("The first list with a sum of less than -10 had the following numbers: " + myList.numberList);
        System.out.println("This list has a sum of " + myList.getSum());
        System.out.println("A total of " + myList.getInstanceCount() + " lists were created.");
    }
}
