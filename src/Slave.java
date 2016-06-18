import java.net.*;
import java.io.*;

public class Slave {

	public static void main(String [] args) {
		
		Socket client;
		ObjectOutputStream out;
		ObjectInputStream in;
		
		// Client Information
		String serverName;
		int port;
		if(args.length <= 1){
			port = 50004;
		} else {
			port = Integer.parseInt(args[1]);
		}
		if (args.length <=2){
			serverName = "127.0.0.1";
			//serverName = "192.168.137.139";
		} else {
			serverName = args[0];
		}
		
		try {	
			
			// Connecting to server
			System.out.println("Connecting to " + serverName + " on port " + port);
			client = new Socket(serverName, port);
			System.out.println("Just connected to " + client.getRemoteSocketAddress());
			in = new ObjectInputStream(client.getInputStream());
			out = new ObjectOutputStream(client.getOutputStream());
			
			// Read the incoming array
			BigArray incomingArray = (BigArray) in.readObject();
			//System.out.println("Incoming: " + incomingArray);
			
			// Sort incoming array
			incomingArray.mergesort();
			
			// Write back sorted array
			out.writeObject(incomingArray);
			
			client.close();
			
		} catch(IOException e) {
			e.printStackTrace();
		} catch(ClassNotFoundException e2){
			e2.printStackTrace();
		}
        
	}



	private void test(String serverName, int port){
		
		try {	

			BigArray array = new BigArray(10);

			System.out.println("Connecting to " + serverName + " on port " + port);
			Socket client = new Socket(serverName, port);
			System.out.println("Just connected to " + client.getRemoteSocketAddress());
			ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
			out.writeObject(array);
			ObjectInputStream in = new ObjectInputStream(client.getInputStream());
			
			try {
				String objectReceived = (String) in.readObject();
				System.out.println("Master says " + objectReceived);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			client.close();
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}