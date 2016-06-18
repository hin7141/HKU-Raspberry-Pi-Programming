package Lab1;

import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;


public class BigArray implements Serializable {

	private static final long serialVersionUID = 1L;
	private long[] array;

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
				System.out.println("No. of Elements = "+array.length+"  NOT ORDERED!");
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
    
    /***************************************/
    /******** Merge Sort Functions *********/ 
	/***************************************/
    
    public void mergesort()
    {
    	long temp[] = new long[array.length];
        mergeSort(array, temp, 0, array.length - 1);
        
    }

    public void mergeSort(long[] array, long[] temp, int left, int right)
    {
        if (left < right) {
            int center = (left + right) / 2;
            mergeSort(array, temp, left, center);
            mergeSort(array, temp, center + 1, right);
            merge(array, temp, left, center + 1, right);
        }
    }

    public void merge(long[] array, long[] temp, int leftPos,
            int rightPos, int rightEnd)
    {
        int leftEnd = rightPos - 1;
        int tempPos = leftPos;

        int numElements = rightEnd - leftPos + 1;

        while (leftPos <= leftEnd && rightPos <= rightEnd) {
            if (array[leftPos] < array[rightPos]) {
                temp[tempPos++] = array[leftPos++];
            }
            else {
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
            array[rightEnd] = temp[rightEnd];
        }

    }
    
    public void mergeParts(BigArray bigArray2){
    	int array1_size = this.size();
    	long temp[] = new long[this.size()+bigArray2.size()];
    	this.array = Arrays.copyOf(this.array, array1_size+bigArray2.size());
    	for(int i=0; i<bigArray2.size(); i++){
    		this.array[array1_size+i]=bigArray2.array[i];
    	}
    	
    	merge(array, temp, 0, array1_size, this.size()-1);
    	
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
    
}
