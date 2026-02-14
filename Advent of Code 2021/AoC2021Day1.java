import java.io.*;  
import java.util.*;

public class AoC2021Day1 {
public static void main(String[] args) {
ArrayList<Integer> integers = new ArrayList<Integer>();
int counter=0;
int A=0; //first sliding window sum 
int B=0; //second sliding window sum
int ABIncreaseCounter=0; //counter for the difference between sliding window A & B
//count the lines where the number is greater than the one in the line above.
try {
      File file = new File(args[0]);
      Scanner scanner = new Scanner(file);
	while(scanner.hasNextInt())
		 integers.add(scanner.nextInt());
		scanner.close();
		for(int i=1;i<integers.size();i++)
            		if(integers.get(i)>integers.get(i-1)) counter++;
		System.out.println("Counter of lines, where the number is greater than the one in the line above="+counter+".");
		 for(int i=0;i<integers.size();i++)
		{
			if(i+1<integers.size() && i+2<integers.size() && i+3<integers.size())
			{
				A=integers.get(i)+integers.get(i+1)+integers.get(i+2);
				B=integers.get(i+1)+integers.get(i+2)+integers.get(i+3);
				if(A<B) ABIncreaseCounter++;
			}
		}
		System.out.println("Times where A<B="+ABIncreaseCounter+".");
     } catch (FileNotFoundException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }   
 }
}
