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
			if (count % 10 == 0) {
				System.out.println("Count = " + count);
				System.out.println("Current = " + current);
				System.out.println("Next = " + next);
			}
		}

		return count;

		//System.out.println("Count = " + count);

	}


	public static void main(String[] args) {
		
		//initial conditions		
		MyFunction f = new MyFunction(10,10);
		
		int count = compute(f);
		System.out.println("Count = " + count);
	}

	
}
