package temp1;
import javax.swing.JFrame;		//Including the required libraries
class Server{		//Server Class
	public static void main(String args[]){
		Node1 node = new Node1(6789);		//Making an instance of a Server with a port number 6789
		node.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);      //Close the application on clicking the close button	
	}
}	
