package Lab1;

import java.util.ArrayList;

public class BigList {
	
	ArrayList<Integer> array;

	public BigList(Integer size) {
		// TODO Auto-generated constructor stub
		array = new ArrayList<Integer>();
		for (int i=0; i<size; i++){
			array.add((int) Math.round( Math.random()*(Math.pow(2, 63)-1) ));
		}
	}

	public static void main(String[] args) {
		BigList bl = new BigList(100);
		
	}

}
