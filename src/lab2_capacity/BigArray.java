package lab2_capacity;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;


public class BigArray implements Serializable {

	private static final long serialVersionUID = 1L;
	private long[] array;
	private int start, end;

	public static void main(String args[]){
		
		ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
//		int size = 1000000;
//		BigArray big_array = new BigArray(size);
//		long start = threadMXBean.getCurrentThreadCpuTime();
//		big_array.mergesort();
//		long end = threadMXBean.getCurrentThreadCpuTime();
//		big_array.isSorted();
//		System.out.println("Time = " + (end-start)/1000000.0 + "ms");
		
//		guessBreakPoint2();
		
		int size = 100000;
		if(args.length>0){
			size = Integer.parseInt(args[0]);
		}
		BigArray ba = new BigArray(size);
		
		ba.isSorted();
		long start = threadMXBean.getCurrentThreadCpuTime();
		ba.mergesort();
		long end = threadMXBean.getCurrentThreadCpuTime();
		ba.isSorted();
		System.out.println("Time = " + (end-start)/1000000.0 + "ms");

	}
	
	public static void guessBreakPoint(){
		int safe = 0;
		int num = safe;
		int step = 1000000000;
		while(true){
			System.out.println(num);
			try{
				//test
				BigArray ba = new BigArray(num);
				ba.mergesort();
				safe = num;
				num = num + step;
			} catch(java.lang.OutOfMemoryError e) {
				step = step / 10;
				num = safe + step;
			}
		}
	}
	
	// 87982393
	public static void guessBreakPoint2(){
		Runtime runtime = Runtime.getRuntime();
		long maxMemory = runtime.maxMemory();
		int low = 0;
		int high = 1000000000;
		int num = (low+high)/2;
		while(true){
			System.out.print(num+" ... ");
			try{
				BigArray ba = new BigArray(num);
				ba.quicksort();
				low = num+1;
				System.out.println("ok");
				maxMemory = runtime.maxMemory();
				System.out.println("Max memory: "+maxMemory);
			} catch(java.lang.OutOfMemoryError e) {
				high = num-1;
				System.out.println("out of memory");
			}
			num = (low+high)/2 +1;
			if(high<=low){ break; }
		}
		System.out.println("Maximum capacity: "+ (low-1));
	}

	public BigArray(int size) {
		array = new long[size];
		this.initRandomCase();
		start = 0;
		end = size-1;
	}
	
	public BigArray(int size, boolean is_init) {
		array = new long[size];
		if(is_init){
			this.initRandomCase();
		}
		start = 0;
		end = size-1;
	}
	
	public BigArray(long[] a){
		array = a;
	}
	
	@Override
	public BigArray clone(){
		return new BigArray(this.array);
	}
	
	static private void compare(int size){
		
		ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
		System.out.println("Generating an array with " + size + " elements...");
		
		try{
			BigArray array = new BigArray(size);
			BigArray array2 = new BigArray(size);
			
			try{
				//array.isOrdered();
				System.out.print("Quicksort: ");
				double startTime = threadMXBean.getCurrentThreadCpuTime() / 1000000.0;
				array.quicksort();
				double stopTime = threadMXBean.getCurrentThreadCpuTime() / 1000000.0;
				//array.isOrdered();
				double elapsedTime = stopTime - startTime;
		        System.out.println(elapsedTime + "ms");
		        
			} catch(java.lang.OutOfMemoryError e){
				System.out.println("Out Of Memory!!!");
			}
			
			try{
				//array2.isOrdered();
		        System.out.print("Mergesort: ");
				long startTime = System.currentTimeMillis();
				array2.mergesort();
				long stopTime = System.currentTimeMillis();
				//array2.isOrdered();
				long elapsedTime = stopTime - startTime;
		        System.out.println(elapsedTime + "ms");
			} catch(java.lang.OutOfMemoryError e){
				System.out.println("Out Of Memory!!!");
			}
			
			
			
		} catch(java.lang.OutOfMemoryError e){
			System.out.println("Failed: Out of Memory (Java heap space)");
			e.printStackTrace();
		}
		
		
	}
	
	public void initBestCase(){
		int size = array.length;
		for (int i=0; i<size; i++){
			array[i] = i;
		}
	}
	
	public void initRandomCase(){
		System.out.println("Generating "+array.length+" Random Numbers...");
		int size = array.length;
		for (int i=0; i<size; i++){
			array[i] = Math.round( Math.random()*(Math.pow(2, 63)-1) );
		}
	}
	
	public void initWorstCase(){
		int size = array.length;
		for(int i=0; i<size; i++){
			array[i] = array.length - i;
		}
	}
	
	public boolean isSorted(){
		for(int i=0; i<array.length-1; i++){
			if(array[i+1]>=array[i]){
				continue;
			} else {
				System.out.println("No. of Elements = "+array.length+"  NOT ORDERED! ("+i+")");
				return false;
			}
		}
		System.out.println("No. of Elements = "+array.length+"  ORDER CHECKED!");
		return true;
	}
	
	public int size(){
    	return array.length;
    }
    
    @Override
    public String toString(){
    	return Arrays.toString(array);
    }
    
    public long get(int index){
    	return array[index];
    }
    
    public void set(int index, long val){
    	array[index] = val;
    }
    
    public int get_start(){
    	return start;
    }
    
    public int get_end(){
    	return end;
    }
    
    public void set_boundary(int start, int end){
    	this.start = start;
    	this.end = end;
    }
	
	public BigArray[] split(int fractions){
		//System.out.println(this);
		BigArray[] arrays = new BigArray[fractions];
		int partSize = array.length / fractions;
		int begin = 0;
		int end = partSize;
		for(int i=0; i<fractions; i++){
			
			if(i == fractions-1){
				end = array.length;
			}
			
			System.out.println(i + ": Array from " + begin + " to " + (end-1));
			arrays[i] = new BigArray(Arrays.copyOfRange(array, begin, end));
			//System.out.println(arrays[i].size()+" | "+arrays[i]);
			
			begin = end;
			if(i != fractions-1){
				partSize = (array.length - begin)/(fractions-i-1);
			}
			end = begin + partSize;
			if(end>array.length){
				end = array.length;
			}
		}
		return arrays;
	}
	
	public int[][] split_boundary(int fractions){
		int partSize = array.length / fractions;
		int begin = 0;
		int end = partSize;
		int boundaries[][] = new int[fractions][2];
		for(int i=0; i<fractions; i++){
			
			if(i == fractions-1){
				end = array.length;
			}
			
			System.out.println(i + ": Array from " + begin + " to " + (end-1));
			boundaries[i][0]=begin;
			boundaries[i][1]=end-1;
			//System.out.println(arrays[i].size()+" | "+arrays[i]);
			
			begin = end;
			if(i != fractions-1){
				partSize = (array.length - begin)/(fractions-i-1);
			}
			end = begin + partSize;
			if(end>array.length){
				end = array.length;
			}
		}
		return boundaries;
	}
    
    /***************************************/
    /******** Merge Sort Functions *********/ 
	/***************************************/
    
    public void mergesort()
    {
    	long temp[] = new long[end-start+1];
        mergeSort(temp, start, end);
        
    }

    private void mergeSort(long[] temp, int left, int right)
    {
        if (left < right) {
            int center = (left + right) / 2;
            mergeSort(temp, left, center);
            mergeSort(temp, center + 1, right);
            merge(temp, left, center + 1, right);
        }
    }

    private void merge(long[] temp, int leftPos,
            int rightPos, int rightEnd)
    {
        int leftEnd = rightPos - 1;
        int tempPos = leftPos - leftPos; // <-
        int left = leftPos; // <-

        int numElements = rightEnd - leftPos + 1;

        while (leftPos <= leftEnd && rightPos <= rightEnd) {
            if (array[leftPos] < array[rightPos]) {
                temp[tempPos++] = array[leftPos++];
            } else {
                temp[tempPos++] = array[rightPos++];
            }
        }

        while (leftPos <= leftEnd) {
            temp[tempPos++] = array[leftPos++];
        }
        while (rightPos <= rightEnd) {
            temp[tempPos++] = array[rightPos++];
        }

        for (int i = 0; i < numElements; i++, rightEnd--) {
            array[rightEnd] = temp[rightEnd - left]; //<-
        }

    }
    
//    public void mergeParts(BigArray bigArray2){
//    	int array1_size = this.size();
//    	long temp[] = new long[this.size()+bigArray2.size()];
//    	this.array = Arrays.copyOf(this.array, array1_size+bigArray2.size());
//    	for(int i=0; i<bigArray2.size(); i++){
//    		this.array[array1_size+i]=bigArray2.array[i];
//    	}
//    	
//    	merge(array, temp, 0, array1_size, this.size()-1);
//    	
//    }
    
    public void mergeAndReturn(ObjectOutputStream out, int leftStart, int rightStart, int rightEnd) throws ClassNotFoundException, IOException{
    	long temp[] = new long[step];
    	int temp_i=0;
    	int left = leftStart, right = rightStart;
    	
    	while(left<rightStart && right<rightEnd+1){
    		if(array[left]<array[right]){
    			temp[temp_i++] = array[left++];
    		} else {
    			temp[temp_i++] = array[right++];
    		}
    		if(temp_i>=step){
    			out.writeUnshared(temp);
    			temp_i=0;
    			//temp = new long[step];
    		}
    	}
    	
    	while (left < rightStart) {
            temp[temp_i++] = array[left++];
            if(temp_i>=step){
    			out.writeUnshared(temp);
    			temp_i=0;
    			Arrays.fill(temp, 0);
    			//temp = new long[step];
    		}
        }
    	
        while (right < rightEnd+1) {
        	temp[temp_i++] = array[right++];
        	if(temp_i>=step){
    			out.writeUnshared(temp);
    			temp_i=0;
    			Arrays.fill(temp, 0);
    			//temp = new long[step];
    		}
        }
        
        if(temp_i>0){
        	out.writeUnshared(Arrays.copyOf(temp, temp_i));
        }
		
	}
    
//    public void mergeRemote(ObjectOutputStream out, ObjectInputStream in, int leftPos,
//            int rightPos, int rightEnd) throws IOException, ClassNotFoundException
//    {
//        int leftEnd = rightPos - 1;
//        int tempPos = leftPos - leftPos; // <-
//        int left = leftPos; // <-
//        long temp[] = new long[2];
//
//        int numElements = rightEnd - leftPos + 1;
//
//        while (leftPos <= leftEnd && rightPos <= rightEnd) {
//            if (array[leftPos] < array[rightPos]) {
//            	out.writeObject("set,"+array[leftPos++]+","+tempPos++);
//            	out.reset();
//            } else {
//            	out.writeObject("set,"+array[rightPos++]+","+tempPos++);
//            	out.reset();
//            }
//        }
//
//        while (leftPos <= leftEnd) {
//        	out.writeObject("set,"+array[leftPos++]+","+tempPos++);
//        	out.reset();
//        }
//        while (rightPos <= rightEnd) {
//        	out.writeObject("set,"+array[rightPos++]+","+tempPos++);
//        	out.reset();
//        }
//
//        for (int i = 0; i < numElements; i++, rightEnd--) {
//        	temp[1] = rightEnd - left;
//        	out.writeObject("get,"+temp[1]);
//        	out.reset();
//        	temp[0] = (long) in.readObject();
//            array[rightEnd] = temp[0]; //<-
//        }
//        System.out.println("done!");
//        out.writeObject("end");
//        out.reset();
//
//    }
    
    public void tempRemote(ObjectInputStream in, ObjectOutputStream out) throws IOException, ClassNotFoundException{
    	long temp[] = new long[2];
    	int index;
    	while(true){
    		String cmd = (String) in.readUnshared();
    		String params[] = cmd.split(",");
    		
        	if (params[0].equals("set")){
        		
        		temp[0] = Long.parseLong(params[1]);
        		temp[1] = Long.parseLong(params[2]);
        		array[(int) temp[1]] = temp[0];
        		
        	} else if (params[0].equals("get")){
        		index = Integer.parseInt(params[1]);
        		out.writeObject(array[index]);
        		out.reset();
        		
        	} else if(cmd.equals("end")){
        		return;
        	}
    	}
    }
    
    public void mergeParts(int leftStart, int rightStart, int rightEnd){
    	int i=leftStart;
		while(i<=rightStart){
			if(array[i]>array[rightStart]){
				swap(i,rightStart);
				push(rightStart,rightEnd);
			}			
			i++;
		}	
    }
    
    private void swap(int loc1,int loc2){
		array[loc1]=array[loc1]^array[loc2];
		array[loc2]=array[loc1]^array[loc2];
		array[loc1]=array[loc1]^array[loc2];
	}
    
    private void push(int s,int e){
		for(int i=s;i<e;i++){
			if(array[i]>array[i+1])
				swap(i,i+1);
		}
	}
    
    
    /***************************************/
	/******** Quick Sort functions *********/
	/***************************************/
	
    void quicksort() {
    	quicksort(0,array.length-1);
    }
    
    void quicksort(int left, int right) {
        int index = partition(left, right);
        if (left < index - 1)
              quicksort(left, index - 1);
        if (index < right)
              quicksort(index, right);
    }
    
    int partition(int left, int right) {
          int i = left, j = right;
          long tmp;
          long pivot = array[(left + right) / 2];
         
          while (i <= j) {
                while (array[i] < pivot)
                      i++;
                while (array[j] > pivot)
                      j--;
                if (i <= j) {
                      tmp = array[i];
                      array[i] = array[j];
                      array[j] = tmp;
                      i++;
                      j--;
                }
          };
         
          return i;
    }
    
    /***************************************/
	/************ I/O functions ************/
	/***************************************/
    
    private int step = 100;
    
    public void setRemoteBoundary(ObjectOutputStream out, int start, int end) throws ClassNotFoundException, IOException{
    	int boundary[] = {start, end};
    	out.writeUnshared(boundary);
    }
    
    public void getRemoteBoundary(ObjectInputStream in) throws ClassNotFoundException, IOException{
    	int boundary[] = (int[]) in.readUnshared();
    	this.start = boundary[0];
    	this.end = boundary[1];
    }
    
    public void inputFromStream(ObjectInputStream in, int start, int end) throws ClassNotFoundException, IOException{
    	int stage = 0;
		for (int i=start; i<end+1; i = i+step){
			long val[] = (long[]) in.readUnshared();
			System.arraycopy(val, 0, array, i, val.length);
			
			double percentage = (double)(i-start) / (end+1 - start) * 100;
			if(percentage>stage){
				System.out.print("\r");
				System.out.print((int)percentage+"% ");
				stage = (int)percentage + 1;
			}
		}
		System.out.print("\r");
		System.out.println("100%");
		
	}
    
    public void outputToStream(ObjectOutputStream out, int start, int end) throws ClassNotFoundException, IOException{
		for (int i=start; i<end+1; i=i+step){
			int part_end = i+step;
			if(part_end>end){part_end = end;}
			out.writeUnshared(Arrays.copyOfRange(array, i, part_end+1));
		}
	}
    
    
    
}
