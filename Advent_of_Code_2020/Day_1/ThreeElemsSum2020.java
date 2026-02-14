

import java.io.*;  
import java.util.*;

public class ThreeElemsSum2020 {
 public static void main(String[] args) {
 // do something here...
int[] a=new int[200];
int sum=0;
int product=0;
try {
      File file = new File("input.txt");
      Scanner scanner = new Scanner(file);
	for(int i=0;i<200;i++) a[i]=scanner.nextInt();
		scanner.close();
		System.out.println("I'm gonna find 3 random numbers that sum to 2020, and multiply them, too.");
		for(int j=0;j<200;j++) 
		{
			 for(int z=j+1;z<200;z++) 
			 {

				for(int x=z+1;x<200;x++)
	                        {
					sum=a[j]+a[z]+a[x];
					if(sum==2020) 
					{
					  product=a[j]*a[z]*a[x];
					  System.out.println(a[j]+"+"+a[z]+"+"+a[x]+"="+sum+".\t"+a[j]+"*"+a[z]+"*"+a[x]+"="+product+".");
                   			}
			  	}		
			 }
		}
     } catch (FileNotFoundException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
      
 }
}
