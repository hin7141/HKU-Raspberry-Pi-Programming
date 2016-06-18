package Lab1;

import java.net.*;
import java.io.*;

public class Worker extends Thread {
	
	private ServerSocket serverSocket;
	private Socket server;

	public Worker(int port){
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run(){
		while(true){
			System.out.println("Waiting for master on port " + serverSocket.getLocalPort() + "...");
			try {
				server = serverSocket.accept();
				System.out.println("Received request from " + server.getRemoteSocketAddress());
				
				ObjectInputStream in = new ObjectInputStream(server.getInputStream());
				ObjectOutputStream out = new ObjectOutputStream(server.getOutputStream());
				
				BigArray incomingArray = (BigArray) in.readObject();
				incomingArray.mergesort();
				out.writeObject(incomingArray);
				
				server.close();
				
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
            
		}
		
	}
	
	public static void main(String args[]){
		int port = 12345;
		if (args.length>0){
			port = Integer.parseInt(args[0]);
		}
		Worker worker = new Worker(port);
		worker.start();
	}

}
