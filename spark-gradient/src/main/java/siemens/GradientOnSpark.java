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
		//Parametres:
		//f(x,y) = (x+2)*(x+2) + y*y
		//x: [-10; 10]
		//y[-10; 10]
		//lambda = 0.01;

		//initial conditions
		final double a = -10;
		final double b = 10;
		final double c = -10; 
		final double d = 10;
		final double lambda = 0.01;
		final int grid = 10;
		double minvalue;
		double imin, jmin;

		//кол-во элементов по каждой стороне матрицы
		int xcount = (int) ((b-a)/lambda);
		int ycount = (int) ((d-c)/lambda);
		

		//кол-во элементов по каждой стороне блока
		int xgrid = (int) xcount/grid;
		int ygrid = (int) ycount/grid;

		SparkConf sparkConf = new SparkConf().setAppName("Gradient"); //.setMaster("local");
		JavaSparkContext ctx = new JavaSparkContext(sparkConf);

		//store - блочная матрица размера grid*grid с блоками xgrid*ygrid
		List<MBlock> store = new ArrayList<MBlock> ();
		for (int i = 0; i < grid; i++) {
			for (int j = 0; j < grid; j++) {
				store.add(new MBlock(xgrid, a + i*xgrid*lambda, ygrid, c + j*ygrid*lambda, lambda));
			}
		}

		//xblck - массив с нумерацией блоков 
		List<Integer> xblck = new ArrayList<Integer> ();
		for (int i = 0; i < grid; i++) xblck.add(i);

		//xblck превращается в RDD xblocks
		JavaRDD<Integer> xblocks = ctx.parallelize(xblck);

		//blocks - массив пар с координатами блочной матрицы
		JavaPairRDD<Integer, Integer> blocks = xblocks.cartesian(xblocks);
		JavaRDD<MBlock> block_matrice = ctx.parallelize(store);

		//распределённая часть
		//каждый блок заполняется значениями функции и ищется минимум в каждом блоке
		//затем выбирается минимум из всех блоков
		MBlock result = block_matrice.map(x -> x.compute())
		.min(new MBlockComparator());
		
		//вывод результата
		System.out.println("Min = " + result.min + " x = " + result.xmin + " y = " + result.ymin); 
	}
}