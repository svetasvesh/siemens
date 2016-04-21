package siemens;

import java.io.*;
import java.util.*;
import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.VoidFunction;

public class GradientOnSpark{

	public static void main(String[] args) {
		//f(x,y) = (x+2)*(x+2) + y*y
		//x: [-10; 10]
		//y[-10; 10]
		//lambda = 0.01;

		//initial conditions
		double a = -10;
		double b = 10;
		double c = -10; 
		double d = 10;
		double lambda = 0.01;
		final int grid = 10;
		double minvalue;
		int imin, jmin;

		//кол-во элементов по каждой стороне
		int xcount = (int) (b-a)/lambda;
		int ycount = (int) (d-c)/lambda;
		

		//кол-во элементов по каждой стороне блока
		int xgrid = (int) xcount/grid;
		int ygrid = (int) ycount/grid;

		MBlock[][] store = new MBlock(xgrid, a, ygrid, c) [grid][grid];

		ArrayList<int> xblck = new ArrayList<int> ();
		for (int i = 0; i < grid; i++) xblck.add(i);

		SparkConf sparkConf = new SparkConf().setAppName("Gradient"); //.setMaster("local");
		JavaSparkContext ctx = new JavaSparkContext(sparkConf);
		JavaRDD<double> input = ctx.parallelize(initial);

		JavaRDD<double> xblocks = ctx.parallelize(xblck);
		JavaPairRDD<double> blocks = xblocks.cartesian(xblocks);

		blocks.foreach( newVoidFunction<Double, Double>() {
			public void call (Double x, Double y) {
				store[x][y].compute(x, x*xgrid*lambda, y, y*ygrid*lambda);
			}
		});


		for (int i = 0; i < grid; i++) {
			for (int j = 0; j < grid; j++) {
				if ((i == 0) && (j == 0)) {
					minvalue = store[i][j].min;
					imin = i; jmin = j;
				} else {
					if (store[i][j].min < minvalue) {
						minvalue = store[i][j].min;
						imin = i; jmin = j;
					}
				}
			}
		}
		imin = imin*grid + store[imin][jmin].imin;
		jmin = jmin*grid + store[imin][jmin].jmin;
		System.out.println("Min = " + minvalue + "x = " + imin + "y = " + jmin); 
	}
}