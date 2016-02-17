import java.io.Serializable;
import java.util.Arrays;

public class BigArray implements Serializable {

	private static final long serialVersionUID = 1L;
	private long[] array;

	public static void main(String args[]){
		
		/*
		if(args.length <= 0){
			BigArray.compare(10000000);
		} else {
			int size = Integer.parseInt(args[0]);
			BigArray.compare(size);
		}
		*/
		
		for (int i=10000; i<=1000000000; i=(int) (i*10)){
			BigArray.compare(i);
		}
		
		
		/*
		BigArray[] arrays = array.split(2);
		array.isOrdered();
		long startTime = System.currentTimeMillis();
        arrays[0].mergesort();
        arrays[1].mergesort();
        arrays[0].mergeParts(arrays[1]);
        long stopTime = System.currentTimeMillis();
        arrays[0].isOrdered();
        long elapsedTime = stopTime - startTime;
        System.out.println(elapsedTime + "ms");
        //array.print();
        //array.isOrdered();
         */
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
	/******** Merge Sort functions *********/
	/***************************************/
	
	public void mergesort() {
        int length = array.length;
        mergesort(0, length - 1);
    }
	
    public void mergesort(int lowerIndex, int higherIndex) {
         
        if (lowerIndex < higherIndex) {
            int middle = lowerIndex + (higherIndex - lowerIndex) / 2;
            // Below step sorts the left side of the array
            mergesort(lowerIndex, middle);
            // Below step sorts the right side of the array
            mergesort(middle + 1, higherIndex);
            // Now merge both sides
            mergeParts(lowerIndex, middle, higherIndex);
        }
        
    }
    
    public void mergeParts(BigArray ba){
    	int oriLength = array.length;
    	//System.out.println("array.length="+array.length + "   ba.size()="+ba.size());
    	array = Arrays.copyOf(array, array.length + ba.size());
    	//System.out.println("array.length="+array.length);
    	System.arraycopy(ba.array, 0, array, oriLength, ba.size());
    	mergeParts(0,oriLength-1,array.length-1);
    }
    
    private void mergeParts(int lowerIndex, int middle, int higherIndex) {
    	long[] tempMergArr = array.clone();
        for (int i = lowerIndex; i <= higherIndex; i++) {
            tempMergArr[i] = array[i];
        }
        int i = lowerIndex;
        int j = middle + 1;
        int k = lowerIndex;
        while (i <= middle && j <= higherIndex) {
            if (tempMergArr[i] <= tempMergArr[j]) {
                array[k] = tempMergArr[i];
                i++;
            } else {
                array[k] = tempMergArr[j];
                j++;
            }
            k++;
        }
        while (i <= middle) {
            array[k] = tempMergArr[i];
            k++;
            i++;
        }
 
    }
    
    /***************************************/
    /******** Merge Sort 2 
     * 
     */
    
    public void mergesort2()
    {
    	long temp[] = new long[array.length];
        mergeSort(array, temp, 0, array.length - 1);
        
    }

    public void mergeSort(long[] fromArray, long[] toArray, int left, int right)
    {
        if (left < right) {
            int center = (left + right) / 2;
            mergeSort(fromArray, toArray, left, center);
            mergeSort(fromArray, toArray, center + 1, right);
            merge(fromArray, toArray, left, center + 1, right);
        }
    }

    public void merge(long[] fromArray, long[] toArray, int leftPos,
            int rightPos, int rightEnd)
    {
        int leftEnd = rightPos - 1;
        int tempPos = leftPos;

        int numElements = rightEnd - leftPos + 1;

        while (leftPos <= leftEnd && rightPos <= rightEnd) {
            if (fromArray[leftPos] < fromArray[rightPos]) {
                toArray[tempPos++] = fromArray[leftPos++];
            }
            else {
                toArray[tempPos++] = fromArray[rightPos++];
            }
        }

        while (leftPos <= leftEnd) {
            toArray[tempPos++] = fromArray[leftPos++];
        }
        while (rightPos <= rightEnd) {
            toArray[tempPos++] = fromArray[rightPos++];
        }

        for (int i = 0; i < numElements; i++, rightEnd--) {
            fromArray[rightEnd] = toArray[rightEnd];
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
