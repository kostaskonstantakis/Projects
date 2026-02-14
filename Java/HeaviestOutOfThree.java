import java.io.*;
import java.util.*;
import java.math.*;

class HeaviestOutOfThree
{

//find the heaviest box out of the three
 
 public static int findHeaviestOutOfThree(int weight0, weight1, weight2)
 {
	if(weight0>=weight1 && weight0>=weight2) return 0;
	else if(weight1>=weight0 && weight1>=weight2) return 1;
	else if(weight2>=weight0 && weight2>=weight1) return 2;
	return -1;
 
 } 
 
 public static void main(String args[]) {
	 
	 while()
	 {
		 int weight0=in.nextInt();
		 int weight1=in.nextInt();
		 int weight2=in.nextInt();
		 PrintStream outStream=System.out;
		 System.setOut(System.err);
		 int action=findHeaviestOutOfThree(weight0, weight1, weight2);
		 System.setOut(outStream);
		 System.out.println(action);
	 
	}
 
 }
 
}