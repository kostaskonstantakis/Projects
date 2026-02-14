import java.io.*;
import java.util.*;
       
public class PairWithJointBiggestValues {

public static void calculatePairWithJointBiggestValues(List<Integer> values,int Duration)
 {
     int max=Duration-30; //duration
     int total=0; //total pair of movies duration
     int firstIndex=0; //the indeces of the selected pair
     int secondIndex=0;  
     int flag=0; 
    //check the uniqueness of the pair, to minimize the difference between the pair's sum & the max value
     for(int i=0;i<values.size();i++) //traverse the list
     {
         for(int j=1;j<values.size();j++) //compute the pairs
        {
            total=values.get(i)+values.get(j);
            if(total>max) continue; //ignore the pairs that are bigger than the limit 
            else if(total<=max && flag<total) //pair within limit, with the nearest "distance" to the max
            {
                flag=total;
                firstIndex=i;
                secondIndex=j;
            }
        } 
     }
     System.out.println("["+firstIndex+", "+secondIndex+"]");
 } 

public static void main(String[] args) {
List<Integer> values = new ArrayList<Integer>();
int Duration=350; 
if(Duration<60)
    System.err.println("Value must be at least 60!");
values=Arrays.asList(70, 95, 99, 111, 32, 66, 166, 420, 60, 120, 10); 
if(values.size()<2 || values.size()>1000) 
    System.err.println("Array size out of bounds! Array size must be between 2 and 1000!");
calculatePairWithJointBiggestValues(values, Duration);
 }
}
