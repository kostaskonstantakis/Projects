import java.io.*;  
import java.util.*;

public class AoC2021Day2 {
public static void main(String[] args) {
int h=0; //horizontal position
int d=0; //depth
int product=0; //h*d
int aim=0;
int temp=0;
String input;
try {
      File file = new File(args[0]);
      Scanner scanner = new Scanner(file);

	//part A
         while(scanner.hasNext())
        {
                input = scanner.next();
                if(input.equals("forward")) h+=scanner.nextInt();
                else if (input.equals("down")) d+=scanner.nextInt();
                else if (input.equals("up")) d-=scanner.nextInt();
        }
	product=h*d;
	System.out.println("Horizontal position="+h+", depth="+d+", h*d="+product+".");
	scanner.close();	

	Scanner scanner2 = new Scanner(file);
	//part B
	h=0;
	d=0;
	while(scanner2.hasNext())
	{
		input = scanner2.next();
		if(input.equals("forward"))
		{
			 temp=scanner2.nextInt();
			 h+=temp;
			 d+=aim*temp;
		}
		else if (input.equals("down")) aim+=scanner2.nextInt();
		else if (input.equals("up")) aim-=scanner2.nextInt();
	}
		scanner2.close();
		product=h*d;
	  	System.out.println("Final horizontal position="+h+", final depth="+d+", final h*d="+product+".");
     } catch (FileNotFoundException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }   
 }
}
