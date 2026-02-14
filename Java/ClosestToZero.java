import java.io.*;
import java.util.*;
import java.math.*;

class ClosestToZero
{

//find the absolute temperature which is closest to 0. 
 
 public static int findClosestToZero(int[] ts)
 {
	if(ts.length==0) return 0;
	else if(ts.length>=10001) return -1; 
	for(int i=0;i<ts.length;i++) ts[i]=Math.abs(ts[i]); //eliminate minuses
	Arrays.sort(ts); //sort array
	return ts[0]; //The element closest to zero is the first one!
		
 
 } 
 
 public static void main(String args[]) {
	 
		 Scanner in =new Scanner(System.in);
		 int n=in.nextInt();
		 int[] ts=new int[n];
		 for(int i=0;i<n;i++)
			 ts[i]=in.nextInt();
		 PrintStream outStream=System.out;
		 System.setOut(System.err);
		 int solution=findClosestToZero(ts);
		 System.setOut(outStream);
		 System.out.ptrintln(solution);
 }
 
}