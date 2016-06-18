package Lab1;

import java.net.*;
import java.io.*;

public class Master extends Thread {
	
	private Socket[] workers;
	private BigArray array;
	
	public Master(String ip[], int port, int arraysize){
		workers = new Socket[ip.length];
		for (int i=0; i<ip.length; i++){
			try {
				workers[i] = new Socket(ip[i], port);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		array = new BigArray(arraysize);
		array.isSorted();
	}
	
	@Override
	public void run(){
		BigArray[] partitions = array.split(workers.length+1);
		ObjectOutputStream out;
		ObjectInputStream in;

		// send out partitions
		for(int i=0; i<workers.length; i++){
			try {
				out = new ObjectOutputStream(workers[i].getOutputStream());
				out.writeObject(partitions[i]);
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
		
		// sort own partition
		partitions[workers.length].mergesort();
		array = partitions[workers.length];
		
		// collect partitions
		for(int i=0; i<workers.length; i++){
			try {
				in = new ObjectInputStream(workers[i].getInputStream());
				BigArray receivedArray = (BigArray) in.readObject();
				array.mergeParts(receivedArray);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}	
		}
		
		array.isSorted();
		//System.out.println(array.toString());
	}
	
	public static void main(String args[]){
		int port = 12345;
		if (args.length>0){
			port = Integer.parseInt(args[0]);
		}
		String ip[] = {"127.0.0.1"};
		Master master = new Master(ip, port, 10000);
		master.start();
	}
	

}
