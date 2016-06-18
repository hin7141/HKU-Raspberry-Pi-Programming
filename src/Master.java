import java.net.*;
import java.io.*;

public class Master extends Thread
{
   private ServerSocket serverSocket;
   private int arraySize;
   
   public Master(int port, int size) throws IOException
   {
      serverSocket = new ServerSocket(port);
      arraySize = size;
      //serverSocket.setSoTimeout(10000);
   }

   public void run() {
      while(true) {
         try {
        	 
        	 // Set up server
            System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
            Socket server = serverSocket.accept();
            System.out.println("Just connected to " + server.getRemoteSocketAddress());
            ObjectOutputStream out = new ObjectOutputStream(server.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(server.getInputStream());
            
            // Generate new array
			BigArray array = new BigArray(arraySize);
			array.isSorted();
			
			// Begin sorting here
			long startTime = System.currentTimeMillis();
			BigArray[] arrays = array.split(2);
			
			// Write second half to slave
			out.writeObject(arrays[1]);
			
			// Merge sort first half
			arrays[0].mergesort();
			
			// Read back sorted second half
			BigArray mergedArray1 = (BigArray) in.readObject();
			//System.out.println("Incoming Sorted Array: " + mergedArray1);
			
			// Merge two parts
			arrays[0].mergeParts(mergedArray1);
			
			// Stop and verify
			array = arrays[0];
			long stopTime = System.currentTimeMillis();
	        long elapsedTime = stopTime - startTime;
	        array.isSorted();
	        System.out.println(elapsedTime + "ms");
            
            server.close();
            
            
         } catch(SocketTimeoutException s) {
            System.out.println("Socket timed out!");
            break;
            
         } catch(IOException e) {
            e.printStackTrace();
            break;
            
         } catch (ClassNotFoundException e) {
 			e.printStackTrace();
 			break;
 			
 		}
      }
   }
   
   public static void main(String [] args)
   {
	  int port = 50004;
	  int size;
	  if(args.length<=0){
		  size = 100000000;
	  } else {
		  size = Integer.parseInt(args[0]);  
	  }
      try {
         Thread t = new Master(port,size);
         t.start();
      } catch(IOException e) {
         e.printStackTrace();
      }
   }
   
   private void test(){
	   while(true)
	      {
	         try
	         {
	            System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
	            Socket server = serverSocket.accept();
	            System.out.println("Just connected to " + server.getRemoteSocketAddress());
	            
	            ObjectInputStream in = new ObjectInputStream(server.getInputStream());
	            try {
	    			BigArray objectReceived = (BigArray) in.readObject();
	    			System.out.println("Master says " + objectReceived);
	    		} catch (ClassNotFoundException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
	            
	            ObjectOutputStream out = new ObjectOutputStream(server.getOutputStream());
	            out.writeObject("TEST");
	            
	            server.close();
	         }catch(SocketTimeoutException s)
	         {
	            System.out.println("Socket timed out!");
	            break;
	         }catch(IOException e)
	         {
	            e.printStackTrace();
	            break;
	         }
	      }
   }
}