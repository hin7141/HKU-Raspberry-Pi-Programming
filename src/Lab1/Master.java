package Lab1;

import java.net.*;
import java.util.Arrays;
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
			System.out.println("Sending partition " + i+1 + "...");
			try {
				out = new ObjectOutputStream(workers[i].getOutputStream());
				out.writeObject(partitions[i]);
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
		
		// sort own partition
		System.out.println("Start sorting a part of the array...");
		partitions[workers.length].mergesort();
		System.out.println("Done!");
		array = partitions[workers.length];
		
		// collect partitions
		for(int i=0; i<workers.length; i++){
			try {
				System.out.println("Waiting for partition " + i+1);
				in = new ObjectInputStream(workers[i].getInputStream());
				BigArray receivedArray = (BigArray) in.readObject();
				System.out.println("Merging array...");
				array.mergeParts(receivedArray);
				System.out.println("OK!");
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
		String ip[] = {"127.0.0.1"};
		if (args.length>0){
			port = Integer.parseInt(args[0]);
		}
		if (args.length>1){
			String assign_ip[] = Arrays.copyOfRange(args, 1, args.length-1);
			ip = assign_ip;
		}
		
//		String assign_ip[] = {"192.168.1.103","192.168.1.104"};
//		ip = assign_ip;
		
		
		Master master = new Master(ip, port, 30000000);
		master.start();
	}
	

}
