package temp1;
import java.net.*;		//Including the required libraries
import java.io.*;

class Client { 		//Client class
  public static void main(String[] argv) {
    String host = "localhost";
    int port = 6789;
    try {
      Socket comm_socket = new Socket(host, port);			//Making a socket for communication using the port number and host address
      Node2 node = new Node2(comm_socket);		//Making the client node using the socket
      Thread t = new Thread(node);		//Creating a thread of the client
      t.start();		//Starting the thread

    } catch (UnknownHostException e) {				//Handling the exceptions
      System.out.println("Host not found\n");
    } catch (IOException e) {
      System.out.println("Caught IOException\n");
    }
  }
}
