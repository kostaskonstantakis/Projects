
/*
 * Simple example TCPClient
 *
 * @author csd3219@csd.uoc.gr
 * Marios Konstantinos Konstantakis, A.M: 3219. 
 */
import java.io.*;
import java.net.*;
import java.util.*;

public class TCPClient {

	public static void main(String argv[]) throws Exception {

		Scanner sc=new Scanner(System.in);
		// System.out.print("input: ");
		String sentence = sc.nextLine(); //"1111\n2222\n3333$";   //user input
		String modifiedSentence = new String(); // initialize

		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

		Socket clientSocket = new Socket("147.52.206.60", 6789); //kos' IP address, and input port 

		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		// sentence = inFromUser.readLine();

		outToServer.writeBytes(sentence + '\n');

		modifiedSentence = inFromServer.readLine();

		System.out.println("FROM SERVER: " + modifiedSentence);

		clientSocket.close();

	}
}
