/**
* Advent of Code 2020, Day 2
*
* @author Kostas Konstantakis
*/

import java.util.*;
import java.io.*;

public class Main { 
public static void main(String[] args) {
String[] substrings;
String[] passwords=new String[1000];
int min=0; //minimum number of repetitions of a certain character in each password.
int max=0; //maximum number of repetitions of a certain character in each passoword.
char letter=' '; //the character we search for
String password;
int counter=0; 
int validPasswords=0; //number of valid passwords in the input file.
try {
      File file = new File("day2_input.txt");
      Scanner scanner = new Scanner(file);
	for(int i=0;i<1000;i++)
	{
		String str=scanner.nextLine(); 
		substrings=str.split("[- :]+"); //separate all the substrings. 
		for (String s:substrings)
		{   //convert 'em to their respectful types.
			min=Integer.parseInt(substrings[0]);
			max=Integer.parseInt(substrings[1]);
			letter=substrings[2].charAt(0);
			password=substrings[3];
			passwords[i]=substrings[3];
		}
			//Now, gotta search the string.

			/*for(int j=0;j<password.length();j++)
			{
				if(password.charAt(j)==letter)
				{	
					 counter++;
				//else continue;
					if(counter>=min&&counter<=max) //&&j==password.length()-1 
					{
						validPasswords++;
						//break;
						//continue;
					}

				}
			
			}*/
			

		//}

	  }
		scanner.close();	
			for(int j=0;j<1000;j++)
                        {
				counter=0; //Count the number of times the certain letter occurs in every password
				for(int z=0;z<passwords[j].length();z++)
				{
                                	if(passwords[j].charAt(z)==letter)
                                	{
                                        	counter++;
						//if((counter>=min)&&(counter<=max)&&z==passwords[j].length()-1) validPasswords++;
						//&& gives 2, || gives 583, and ^ (XOR) gives 581
					}
				}
				if((counter>=min)&&(counter<=max)) validPasswords++;
		
			}


            	
	//}
	//scanner.close();
	System.out.println("Out of 1000 passwords, "+validPasswords+" were actually valid!");
     } catch (FileNotFoundException e) {
      System.out.println("Couldn't open the input file. :(");
      e.printStackTrace();
    }
}

}
