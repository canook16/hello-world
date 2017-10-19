package a1;

// Name: Josh Novosad 260555158

import java.io.*;
import java.util.*;

/**
 *
 * ATTENTION: ANY CHANGES IN INITIAL CODE (INCLUDING FILE NAME, METHODS, CONSTRUCTORS etc) WILL CAUSE NOT POSITIVE MARK!
 * HOWEVER YOU CAN CREATE YOUR OWN METHODS WITH CORRECT NAME. ONLY THIS FILE WILL BE GRADED, that is NO EXTERNAL CLASSES are allowed.
 *
 * TO STUDENT: ALL IMPORTANT PARTS ARE SELECTED "TO STUDENT" AND WRITTEN IN HEADERS OF METHODS. *
 * @author AlexanderButyaev
 *
 */
public class COMP251HW1 {
	// This is the list of ALPHA s (from 0.2 till 4 with step 0.2)
	public double[] ns = {0.2, 0.4, 0.6, 0.8, 1.0, 1.2, 1.4, 1.6, 1.8, 2.0, 2.2, 2.4, 2.6, 2.8, 3.0, 3.2, 3.4, 3.6, 3.8, 4.0};
	/*Fields / methods for grading BEGIN*/
	private HashMap<Integer,String> pathMap;

	public HashMap<Integer, String> getPaths() {
		return pathMap;
	}

	public void setPaths(HashMap<Integer, String> pathMap) {
		this.pathMap = pathMap;
	}
	/*Fields / methods for grading END*/

	/**
	 * method generateRandomNumbers generates array of random numbers (double primitive) of size = "size" and w, which limits generated random number by 2^w-1
	 * @param w - integer number, which limits generated random number by 2^w-1
	 * @param size - size of the resulting array
	 * @return double[]
	 */
	public double[] generateRandomNumbers(int w, int size) {
		double[] resultArray = new double[size];
		if (getPaths() != null) {	//THIS PART WILL BE USED FOR GRADING
			String path = getPaths().get(size);
			File file = new File (path);
			Scanner scanner;
			try {
				scanner = new Scanner(file);
				int i = 0;
				while (scanner.hasNextLine() && i < resultArray.length) {
					resultArray[i] = Double.parseDouble(scanner.nextLine());
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {

			for (int i = 0; i < size; i++) {
				resultArray[i] = (int)(Math.random()*(Math.pow(2, w)-1)); //cast to int to make it Integer
			}
		}
		return resultArray;
	}

	public double generateRandomNumberiInRange(double min, double max) {
		double res = min;
		while (res == min) {
			res = min + Math.random() * (max - min);
		}
		return res;
	}

	/**
	 * method generateCSVOutputFile generates CSV File which contains row of x (first element is identificator "X"),
	 * and one row for every experiment (ys - id with set of values)
	 * Looks like this:
	 * ================
	 *  X,1,2,3
	 *  E1,15,66,34
	 *  E2,16,15,14
	 *  E3,99,88,77
	 * ================
	 *
	 * @param filePathName - absolute path to the file with name (it will be rewritten or created)
	 * @param x - values along X axis, eg 1,2,3,4,5,6,7,8
	 * @param ys - values for Y axis with the name of the experiment for different plots.
	 */
	public void generateCSVOutputFile(String filePathName, double[] x, HashMap<String, double[]> ys) {
		File file = new File(filePathName);
		FileWriter fw;
		try {
			fw = new FileWriter(file);
			fw.append("X");
			for (double d: x) {
				fw.append("," + d);
			}
			fw.append("\n");
			for (Map.Entry<String, double[]> entry: ys.entrySet()) {
				fw.append(entry.getKey());
				double[] dTemp = entry.getValue();
				for (int i = 0, len = dTemp.length;i < len; i++) {
					fw.append(","+dTemp[i]);
				}
				fw.append("\n");
			}
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	/**
	 * divisionMethod is the main method for division method in hashing problem. It creates specific array ys, iterates over the set of n (defined by ns field) and for every n adds in ys array particular number of collisions
	 *
	 * It requires arguments:
	 * r - division factor, w - integer number (required for random number generator)
	 *
	 * It returns an array of number of collisions (for all n in range from 10% to 200% of m) - later in plotting phase - it is Y values for divisionMethod
	 * @param r
	 * @param w
	 * @return ys for division method {double[]}
	 */
	public double[] divisionMethod (int r, int w) {
		double[] ys = new double[ns.length];
		for (int it = 0, len = ns.length; it < len; it++) {
			int m = (int) (Math.pow(2,r));
			int n = (int)(ns[it]*m);
			ys[it] = divisionMethodImpl (r, n, w);
		}
		return ys;
	}

	/**
	 * divisionMethodImpl is the particular implementation of the division method.
	 *
	 * It requires arguments:
	 * r - division factor, n - number of key to insert, w - integer number (required for random number generator)
	 * @param r
	 * @param n
	 * @param w
	 * @return number of collision for particular configuration {double}
	 */
	public double divisionMethodImpl (int r, int n, int w) {

		/*TO STUDENT: WRITE YOUR CODE HERE*/
		int keyArrays[] = new int[n]; //initialize an array of keys to check number of collisions with
		for(int i=0; i<n; i++){
			double hashfn = generateRandomNumberiInRange(0.0,Math.pow(2.0,(double)w)); //generates hash function randomly
			keyArrays[i] = (int) (hashfn % Math.pow(2,(double)r)); //generates key
		}
		int numCollisions = 0; //initialize variable for number of collisions
		for(int i=0; i<(n-1); i++){
			for(int j=i; j<(n-1); j++){
				if(keyArrays[i]==keyArrays[j+1]){ //if there is a matching key, increase the number of collisions found by 1.
					numCollisions++;
					break;
				}
			}
		}
		return numCollisions; //returns the number of collisions
	}



	/**
	 * multiplicationMethod is the main method for multiplication method in hashing problem. It creates specific array ys, specifies A under with some validations, iterates over the set of n (defined by ns field) and for every n adds in ys array particular number of collisions
	 *
	 * It requires arguments:
	 * r and w - are such integers, that w > r
	 *
	 * It returns an array of number of collisions (for all n in range from 0.2 to 4 of m) - later in plotting phase - it is Y values for multiplicationMethod
	 * @param r
	 * @param w
	 * @return ys for multiplication method {double[]}
	 */
	public double[] multiplicationMethod (int r, int w) {
		double[] ys = new double[ns.length];
		double y;
		double A = generateRandomNumberiInRange(Math.pow(2, (double)(w-1)),Math.pow(2, (double)w)); // updated
		for (int it = 0, len = ns.length; it < len; it++) {
			int m = (int)(Math.pow(2,r));
			int n = (int)(ns[it]*m);
			y = multiplicationMethodImpl (r, n, w, A);
			if (y < 0) return null;
			ys[it] = y;
		}
		return ys;
	}


	/**
	 * multiplicationMethodImpl is the particular implementation of the multiplication method.
	 *
	 * It requires arguments:
	 * n - number of key to insert, r and w - are such integers, that w > r, A is a factor
	 * @param r
	 * @param n
	 * @param w
	 * @param A
	 * @return number of collisions for particular configuration {double}
	 */
	public double multiplicationMethodImpl (int r, int n, int w, double A) {


		/*TO STUDENT: WRITE YOUR CODE HERE*/
		if (w<=r){
			return -1;
		}
		int keyArrays[] = new int[n]; //initialize an array of keys to check number of collisions with
		for(int i=0; i<n; i++){
			int hashfn = (int) generateRandomNumberiInRange(0.0,Math.pow(2.0,(double)w)); //generates hash function randomly
			keyArrays[i] = (int) (A * hashfn % Math.pow(2, (double)w)) >> (w-r); //generates key
		}
		int numCollisions = 0; //initialize variable for number of collisions
		for(int i=0; i<(n-1); i++){
			for(int j=i; j<(n-1); j++){
				if(keyArrays[i]==keyArrays[j+1]){ //if there is a matching key, increase the number of collisions found by 1.
					numCollisions++;
					break;
				}
			}
		}
		return numCollisions;
	}


	/**
	 * TO STUDENT: MAIN method - WRITE/CHANGE code here (it should be compiled anyway!)
	 * TO STUDENT: NUMBERS ARE RANDOM!
	 * @param args
	 */
	public static void main(String[] args) {
		int r = 0, w = 0;
		if (args!= null && args.length>1) {
			r = Integer.parseInt(args[0]);
			w = Integer.parseInt(args[1]);
		} else {
			System.err.println("Input should be r w  (integers). Exit(-1).");
			System.exit(-1);
		}

		if (w<=r) {
			System.err.println("Input should contain r w (integers) such that w>r. Exit(-1).");
			System.exit(-1);
		}

		COMP251HW1 hf = new COMP251HW1();
		double[] yTemp;

		HashMap<String, double[]> ys = new HashMap<String, double[]>();
		System.out.println("===Division=Method==========");
		yTemp = hf.divisionMethod(r, w);
		if (yTemp == null) {
			System.out.println("Something wrong with division method. Check your implementation, formula and all its parameters.");
			System.exit(-1);
		}
		ys.put("divisionMethod", yTemp);
		for (double y: ys.get("divisionMethod")) {
			System.out.println(y);
		}

		System.out.println("============================");
		System.out.println("===Multiplication=Method====");
		yTemp = hf.multiplicationMethod(r, w);
		if (yTemp == null) {
			System.out.println("Something wrong with division method. Check your implementation, formula and all its parameters.");
			System.exit(-1);
		}
		ys.put("multiplicationMethod", yTemp);

		for (double y: ys.get("multiplicationMethod")) {
			System.out.println(y);
		}

		double[] x = new double[hf.ns.length];
		int m = (int)(Math.pow(2,r));
		for (int it = 0, len = hf.ns.length; it < len; it++) {
			x[it] = (int)(hf.ns[it]*m);
		}

		hf.generateCSVOutputFile("hashFunctionProblem.csv", x, ys);
	}
}