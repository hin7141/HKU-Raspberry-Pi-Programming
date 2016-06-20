package Lab1;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
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
		ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
		while(true){
			System.out.println("Waiting for master on port " + serverSocket.getLocalPort() + "...");
			try {
				server = serverSocket.accept();
				System.out.println("Received request from " + server.getRemoteSocketAddress());
				
				ObjectInputStream in = new ObjectInputStream(server.getInputStream());
				ObjectOutputStream out = new ObjectOutputStream(server.getOutputStream());
				
				System.out.println("Start reading the incoming Array...");
				long start = threadMXBean.getCurrentThreadCpuTime();
				BigArray incomingArray = (BigArray) in.readObject();
				long end = threadMXBean.getCurrentThreadCpuTime();
				System.out.println("Done! Transmission time = " + (end-start)/1000000.0 + "ms");
				
				System.out.println("Start merging the incoming Array...");
				start = threadMXBean.getCurrentThreadCpuTime();
				incomingArray.mergesort();
				end = threadMXBean.getCurrentThreadCpuTime();
				System.out.println("Done! Merging time = " + (end-start)/1000000.0 + "ms");
				
				System.out.println("Received the merged array from Master...");
				incomingArray.inputFromStream(in, 0, incomingArray.get_start()-1);
				System.out.println("All done!");
				
				System.out.println("Now return sorted array to Master");
				incomingArray.mergeAndReturn(out, 0, incomingArray.get_start(), incomingArray.get_end());
				System.out.println("Yay finished!");
				
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
