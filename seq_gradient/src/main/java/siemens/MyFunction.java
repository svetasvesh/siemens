import java.io.*;
import java.util.*;

public class MyFunction {

	private int size;
	private double[] x;
	private double[] diff;
	private double result;
	//private boolean flag = true;


	public MyFunction(int n, double init[]) {
		size = n;
		x = new double [size];
		diff = new double [size];
		System.arraycopy(init, 0, x, 0, n);
		//System.out.println("initial conditions = " + x[0] + " " + x[1]);
	}

	public double getResult() {
		return result;
	}

	public void printX() {
		System.out.println("Result arguments: ");
		for (int i = 0; i < size; ++i)
			System.out.println(x[i]);
	}

	public  void callCalculation (double lambda) { 
		calculateDiff(lambda);
		updateX(lambda);
		result = calculateFunction(x);
	}

	void updateX (double lambda) {
		for (int i = 0; i < size; i++) {
			x[i] -= lambda*diff[i];
		}
	}

	double calculateFunction(double[] arg) {
		//f(x1, x2) = 10*x1 + x2*x2
		return (arg[0]+2)*(arg[0]+2) + arg[1]*arg[1];
	}

	void calculateDiff(double lambda) {
		for (int i = 0; i < size; ++i) {
			double[] tmp = new double [size];
			System.arraycopy(x, 0, tmp, 0, size);
			tmp[i] += lambda;

			diff[i] = (calculateFunction(tmp) - calculateFunction(x))/lambda;
		}
	/*	
		if (flag) {
			flag = false;
			System.out.println("Diff:");
			for (int j = 0; j < size; ++j) System.out.println(diff[j]);
		}
	*/
		//diff1 = (calculateFunction(x1 + lambda, x2) - calculateFunction(x1, x2))/lambda;
		//diff2 = (calculateFunction(x1, x2 + lambda) - calculateFunction(x1, x2))/lambda;
	}
/*
	double[] replace (double lambda, double[] tmp, int k ) {
		double[] tmpx = new double [tmp.length];
		for (int i = 0; i < tmp.length; ++i) {
			if (i == k) tmpx[i] = lambda; else tmpx[i] = tmp[i];
		}
		return tmpx;
	} 
*/	
}