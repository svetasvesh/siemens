import java.io.*;
import java.util.*;

public class MyFunction {
	private double x1, x2;
	private double diff1, diff2;
	//double diff;
	private double result;
	//double fx;


	public MyFunction(double x, double y){
		x1 = x;
		x2 = y;
		result = calculateFunction(x, y);
	}

	public double getResult() {
		return result;
	}

	public  void callCalculation (double lambda) { 
		calculateDiff(lambda);
		x1 -= lambda*diff1;
		x2 -= lambda*diff2;
		result = calculateFunction(x1, x2);
	}

	double calculateFunction(double x, double y) {
		//f(x1, x2) = 10*x1 + x2*x2
		return 10*x - y*y;
	}

	void calculateDiff(double lambda) {
		diff1 = (calculateFunction(x1 + lambda, x2) - calculateFunction(x1, x2))/lambda;
		diff2 = (calculateFunction(x1, x2 + lambda) - calculateFunction(x1, x2))/lambda;
		//diff = diff1 + diff2;
	} 
	
//	public MyFunction clone() throws CloneNotSupportedException {
//        return (MyFunction)super.clone();
//    } 
}