import java.io.Serializable;
import java.util.Arrays;

public class BigArray implements Serializable {

	private static final long serialVersionUID = 1L;
	private volatile long[] array;

	public static void main(String args[]){
		int size;
		if(args.length < 1){
			size = 100;
		} else {
			size = Integer.parseInt(args[0]);
		}
		System.out.println("Generating an array with " + size + " elements...");
		BigArray array = new BigArray(100);
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
	
	public void print() {
		for(int i=0; i<array.length; i++){
			System.out.println(array[i]);
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
    
    public int size(){
    	return array.length;
    }
    
    @Override
    public String toString(){
    	return Arrays.toString(array);
    }
	
}