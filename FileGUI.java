package temp1;
import java.io.*;		//Including the required libraries
import java.net.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class FileGUI {
	private JFrame frame;			//Declaring GUI primitive variables
	private JTextField field;
	private JButton select;
	private JButton send;
	public String filename;
	public boolean issend = false;
	
	public FileGUI(){			//Constructor for the file GUI
		frame = new JFrame();
		
		frame.setTitle("Select a File");
		frame.setBounds(100, 100, 499, 362);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLayout(null);
		
		field = new JTextField();
		field.setBounds(36, 23, 400, 30);
		frame.getContentPane().add(field);
		field.setColumns(10);
		
		select = new JButton("Select File");
		select.addActionListener(			//Adding appropriate action listeners
				new ActionListener(){
					public void actionPerformed(ActionEvent event){
						JFileChooser chooser = new JFileChooser();
						chooser.showOpenDialog(null);
						File f = chooser.getSelectedFile();
						filename = f.getAbsolutePath();
						field.setText(filename);
					}
				}	
		);
		
		select.setBounds(36, 56, 100, 30);
		frame.getContentPane().add(select);
		
		send = new JButton("Send");
		send.setBounds(162, 56, 100, 30);
		frame.getContentPane().add(send);
		frame.setSize(500,200);
	}
	
	public void sendFile(FileGUI window){		//Function for sending file
		send.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent event){
						issend = true;						
						window.setVisible(false);
						window.name();
					}
				}	
		);
	}
	
	public void setVisible(Boolean a){			//Function for setting the visibility of the file window
		frame.setVisible(a);
	}
	
	public String name(){		//Function for retrieving the filename
		return filename;
	}
	
	public boolean valid(){		//Function for setting the boolean isset
		return issend;
	}
	
	public void NewScreen(){		//Function for instantiating a new FileGUI window
		EventQueue.invokeLater(new Runnable(){
			public void run(){
				try{
					FileGUI window = new FileGUI();		//Creating a new file window
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}
}
