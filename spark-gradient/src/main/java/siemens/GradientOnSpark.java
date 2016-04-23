package siemens;

import java.io.*;
import java.util.*;
import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaPairRDD;
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
		double imin, jmin;

		//кол-во элементов по каждой стороне
		int xcount = (int) ((b-a)/lambda);
		int ycount = (int) ((d-c)/lambda);
		

		//кол-во элементов по каждой стороне блока
		int xgrid = (int) xcount/grid;
		int ygrid = (int) ycount/grid;


		//MBlock[][] store = new MBlock [grid][grid];
		List<MBlock> store = new ArrayList<MBlock> ();
		for (int i = 0; i < grid; i++)
			for (int j = 0; j < grid; j++) {
				store.add(new MBlock(xgrid, a + i*xgrid*lambda, ygrid, c + j*ygrid*lambda, lambda));
			}

		List<Integer> xblck = new ArrayList<Integer> ();
		for (int i = 0; i < grid; i++) xblck.add(i);

		SparkConf sparkConf = new SparkConf().setAppName("Gradient"); //.setMaster("local");
		JavaSparkContext ctx = new JavaSparkContext(sparkConf);
		//JavaRDD<double> input = ctx.parallelize(initial);

		JavaRDD<Integer> xblocks = ctx.parallelize(xblck);
		JavaPairRDD<Integer, Integer> blocks = xblocks.cartesian(xblocks);

		JavaRDD<MBlock> block_matrice = ctx.parallelize(store);
		MBlock result = block_matrice.map(x -> x.compute(-1, -1))
		.min(new MBlockComparator());
		
		//blocks.foreach( (x) -> System.out.println(x._1 + " " +x._2));
		//blocks.foreach( (x) -> store[x._1][x._2].compute(x._1, x._2));
			//newVoidFunction<Double, Double>() {
			//public void call (Double x, Double y) {
		//		store[x][y].compute(x, x*xgrid*lambda, y, y*ygrid*lambda, lambda);
			//}
		//});

		//blocks.collect();
/*
		minvalue = store[0][0].min;
		imin = store[0][0].xmin; 
		jmin = store[0][0].ymin;
		for (int i = 0; i < grid; i++) {
			for (int j = 0; j < grid; j++) {
			//	if ((i == 0) && (j == 0)) {
			//		minvalue = store[i][j].min;
			//		imin = i; jmin = j;
			//	} else {
					if (store[i][j].min < minvalue) {
						minvalue = store[i][j].min;
						imin = store[i][j].xmin; 
						jmin = store[i][j].ymin;
					}
			//	}
			}
		}
		//double xmin = store[imin][jmin].xmin;
		//double ymin = store[imin][jmin].ymin;
		*/
		System.out.println("Min = " + result.min + " x = " + result.xmin + " y = " + result.ymin); 
	}
}