
/*
 * Simple example TCPServer
 *
 * @author csd3219@csd.uoc.gr 
 * Marios Konstantinos Konstantakis, A.M: 3219.
 */
import java.io.*;
import java.net.*;

public class TCPServer {

   public static void main(String argv[]) throws Exception {
      String clientSentence;
      String serverSentence = new String();
      String totalSentence = new String();
      int port = 6789; //6789 default, 3219 is mine.
      ServerSocket welcomeSocket = new ServerSocket(port);
      System.out.println("Server ready on " + port);
      Socket connectionSocket = welcomeSocket.accept();
      System.out.println("Client connected! Waiting for input...");
      BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

      DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

      while (true)// server always up and running
      {

         clientSentence = inFromClient.readLine();
         System.out.print("client sentence = " + clientSentence);
         System.out.println("---The end.---");
         if (clientSentence.contains("$")) {
            totalSentence += clientSentence.substring(0, clientSentence.indexOf("$"));
            System.out.println("total sentence = " + totalSentence);
            outToClient.writeBytes(totalSentence); // send it to the client
            connectionSocket.close(); // close connection
            break;
         } else {
            totalSentence += clientSentence;
         }
      }
   }
}
