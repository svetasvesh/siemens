import java.io.*;
import java.util.*;


public class Gradient {

	static double lambda = 0.01;
	static double eps = 0.00001;


	public static int compute(MyFunction f) {
		double current, next;
		current = f.getResult();
		f.callCalculation(lambda);
		next = f.getResult();
		int count = 1;
		System.out.println("Current = " + current);
		System.out.println("Next = " + next);

		while (Math.sqrt(Math.pow((next - current), 2)) > eps) {
			f.callCalculation(lambda);
			count++;
			current = next;
			next = f.getResult();
		}
		f.printX();
		System.out.println("Result = " + f.getResult());
		return count;
	}


	public static void main(String[] args) {
		
		//initial conditions
		final int size = 2;	
		double[] initial = new double[size];
		initial[0] = 10.0;
		initial[1] = 10.0;	
		MyFunction f = new MyFunction(size, initial);
		
		int count = compute(f);
		System.out.println("Count = " + count);
	}

	
}
