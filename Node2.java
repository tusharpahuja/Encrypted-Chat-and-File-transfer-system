package temp1;
import java.io.*;		//Including the required libraries
import java.net.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class Node2 extends JFrame implements Runnable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8503866532956089218L;
	boolean shutdown = false;
	DataInputStream streamIn = null;
	DataOutputStream streamOut = null;
	Socket comm_socket = null;				//Declaring and initializing communication socket
	FileGUI nw;				
	private JTextField userText;			//Declaring GUI primitive variables
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket server;
	private Socket connection;
	private JMenuBar menubar;
	private boolean issend;
	private static String filename;
	private JOptionPane pane;
	
	public Node2(Socket comm_socket){		//Constructor for the client
		super("Chat system");
		menubar = new JMenuBar();
		setJMenuBar(menubar);
		userText = new JTextField();
		
		JMenu file = new JMenu("File");		//Creating the menubar of the GUI
		menubar.add(file);
		JMenuItem select = new JMenuItem("Select File");
		file.add(select);
		select.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent event){
						JFileChooser chooser = new JFileChooser();		//Choosing the file to transfer
						chooser.showOpenDialog(null);
						File f = chooser.getSelectedFile();
						filename = f.getAbsolutePath();
						sendFile(filename);
						showMessage("Sending : " + filename);		//Displaying the message on the console
					}
				}
		);
		JMenu Message = new JMenu("Message");
		menubar.add(Message);
		JMenuItem message = new JMenuItem("Send Message");
		message.addActionListener(			//Adding appropriate action listeners
				new ActionListener(){
					public void actionPerformed(ActionEvent event){
						if(!userText.isEditable()){
							pane.showMessageDialog(null,"Connection is not established\n");
						}
					}
					
				}
		);
		Message.add(message);
		
		userText.addActionListener(			//Adding appropriate action listeners
				new ActionListener(){
					public void actionPerformed(ActionEvent event){
						sendMessage(event.getActionCommand());
						userText.setText("");
					}
				}
		);
		add(userText, BorderLayout.NORTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow));

		setSize(400,250);		//Setting the window size
		setVisible(true);
		
		this.comm_socket = comm_socket;
	    try {			//Starting the client
	      streamIn = new DataInputStream(new BufferedInputStream(comm_socket.getInputStream()));
	      streamOut = new DataOutputStream(comm_socket.getOutputStream());
	    } catch (IOException e) {
	      System.out.println("Can't setup streams");
	    }
	}
	
	private void handle() {			//Function handling the entire process
	    try {
	      int func;
	      while (!shutdown) {
	        func = streamIn.readInt();
	        if (func == 10) {	   //Handler for receiving messages     	
	          receiveMsg();			
	        } else if (func == 20) {	//Handler for receiving file
	          receiveFile();
	        } else if (func == 30) {

	        }
	      }
	    } catch (IOException e) {
	      showMessage("Disconnected\n");
	    }
	  }

	  public void sendMessage(String string) {			//Function for sending messages
	    try {
	      long length = string.length();
	      streamOut.writeInt(10);
	      streamOut.writeLong(length);
	      streamOut.writeUTF(string);
	      streamOut.flush();
	      showMessage("Me: " + string + "\n");
	    } catch (IOException e) {
	      showMessage("Couldn't send message\n");
	    }
	  }

	  public void receiveMsg() throws IOException {			//Function for receiving messages
	    long length = streamIn.readLong();
	    String msg = streamIn.readUTF();	  
	    showMessage(msg + "\n");
	  }

	  public void sendFile(String string) {			//Function for sending files
	    try {
	      File file = new File(string);
	      InputStream fin = new FileInputStream(file);
	      long flen = file.length();
	      String fname = file.getName();
	      long nlen = fname.length();
	      byte[] bytearray = new byte[1024 * 1024];
	      streamOut.writeInt(20);
	      streamOut.writeLong(nlen);
	      streamOut.writeUTF(fname);
	      streamOut.writeLong(flen);
	      int count = 0;
	      while ((count = fin.read(bytearray)) > 0) {
	        streamOut.write(bytearray, 0 , count);
	      }
	      streamOut.flush();
	    } catch (FileNotFoundException e) {
	      showMessage("File not found!\n");
	    } catch (IOException e) {
	      showMessage("Cannot send file\n");
	    }
	  }

	  private void receiveFile() {			//Function for receiving files
	    try {
	      long nlen = streamIn.readLong();
	      String fname = streamIn.readUTF();
	      long flen = streamIn.readLong();
	      FileOutputStream fout = new FileOutputStream(fname);
	      byte[] bytearray = new byte[1024 * 1024];
	      int count = 0;
	      while ((count = streamIn.read(bytearray)) > 0) {
	        fout.write(bytearray, 0 , count);
	      }
	    } catch (IOException e) {
	      System.out.println(e.getMessage());
	    }
	  }

	  public void run() {			//Function responsible for running the thread
	    if (comm_socket != null) {
	      handle();
	    }
	  }

	  public void shutdown() {		//Function responsible for shutting down the application
	    shutdown = true;
	    try {
	      streamIn.close();
	      streamOut.close();
	      comm_socket.close();
	      showMessage("--Connection closed--\n");
	    } catch (IOException e) {
	      System.out.println(e.getMessage());
	    }
	  }
	  
	private void showMessage(final String text){		//Function responsible for displaying the message on the GUI window
		SwingUtilities.invokeLater(
				new Runnable(){
					public void run(){
						chatWindow.append(text);
					}
				}
		);
	}
	
	private void ableToType(final boolean tof){			//Function responsible for disabling and enabling the text field
		SwingUtilities.invokeLater(
				new Runnable(){
					public void run(){
						userText.setEditable(tof);
					}
				}
		);
	}
	
}
