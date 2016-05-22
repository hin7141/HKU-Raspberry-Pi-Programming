import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class BigArray implements Serializable {

	private static final long serialVersionUID = 1L;
	private long[] array;

	public static void main(String args[]){
		
		//87983000
//		for (int i=87980000; i<=1000000000; i=i+1000){
//			System.out.println("Generating "+i+" numbers...");
//			BigArray ba = new BigArray(i);
//			//BigArray ba2 = ba.clone();
//			//System.out.println("Performing Quicksort...");
//			//ba.quicksort();
//			System.out.println("Performing Mergesort...");
//			ba.mergesort2();
//			ba.isOrdered();
//		}
		
		guessBreakPoint2();

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
				ba.mergesort2();
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
		//int low = 0;
		int low = 87982300;
		//int high = 100000000;
		int high = 87982400;
		int num = (low+high)/2;
		while(true){
			System.out.print(num+" ... ");
			try{
				BigArray ba = new BigArray(num);
				ba.mergesort2();
				low = num+1;
				System.out.println("ok");
			} catch(java.lang.OutOfMemoryError e) {
				high = num-1;
				System.out.println("out of memory");
			}
			num = (low+high)/2;
			if(high<=low){ break; }
		}
		System.out.println("Maximum capacity: "+ (low-1));
	}

	public BigArray(int size) {
		array = new long[size];
		for (int i=0; i<size; i++){
			array[i] = Math.round( Math.random()*(Math.pow(2, 63)-1) );
			//System.out.println(array[i]);
		}
	}
	
	public BigArray(long[] a){
		array = a;
	}
	
	@Override
	public BigArray clone(){
		return new BigArray(this.array);
	}
	
	static private void compare(int size){
		
		System.out.println("Generating an array with " + size + " elements...");
		
		try{
			BigArray array = new BigArray(size);
			BigArray array2 = new BigArray(size);
			
			try{
				//array.isOrdered();
				System.out.print("Quicksort: ");
				long startTime = System.currentTimeMillis();
				array.quicksort();
				long stopTime = System.currentTimeMillis();
				//array.isOrdered();
				long elapsedTime = stopTime - startTime;
		        System.out.println(elapsedTime + "ms");
		        
			} catch(java.lang.OutOfMemoryError e){
				System.out.println("Out Of Memory!!!");
			}
			
			try{
				//array2.isOrdered();
		        System.out.print("Mergesort: ");
				long startTime = System.currentTimeMillis();
				array2.mergesort2();
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
	
	public void initWorstCase(){
		int size = array.length;
		for(int i=0; i<size; i++){
			array[i] = array.length - i;
		}
	}
	
	public boolean isOrdered(){
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
    
    public void mergesort2()
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
