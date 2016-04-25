package siemens;

import java.io.*;
import java.util.*;
import java.util.Arrays;
import java.util.List;
import java.io.File;
import java.io.PrintWriter;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.VoidFunction;

public class GradientOnSpark{

	//initial conditions
	final static double a = -10;
	final static double b = 10;
	final static double c = -10; 
	final static double d = 10;
	final static double lambda = 0.01;
	final static int grid = 10;

	//кол-во элементов по каждой стороне матрицы
	static int xcount = (int) ((b-a)/lambda);
	static int ycount = (int) ((d-c)/lambda);

	//кол-во элементов по каждой стороне блока
	static int xgrid = (int) xcount/grid;
	static int ygrid = (int) ycount/grid;

	static double minvalue;
	static double imin, jmin;

	public static void main(String[] args) {
		//Parametres:
		//f(x,y) = (x+2)*(x+2) + y*y
		//x: [-10; 10]
		//y[-10; 10]
		//lambda = 0.01;

		String output = "";
		if (args.length > 0) output = args[0];
		
		System.out.println("Create matrix " + xcount + "*" + ycount + " elements and " + grid + "*" + grid + " blocks" );

		System.out.println("Each block contain matrix " + xgrid + "*" + ygrid);

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

		minvalue = result.min;
		imin = result.xmin;
		jmin = result.ymin;
		
		//вывод результата
		System.out.println("Min = " + result.min + " x = " + result.xmin + " y = " + result.ymin); 

		write(output);
	}

		
	public static void write(String output) {
		//тестовый вывод в файл
		File file = new File(output + "output.txt");
		try {
	        //проверяем, что если файл не существует то создаем его
	        if(!file.exists()){
	            file.createNewFile();
        	}
 
	        //PrintWriter обеспечит возможности записи в файл
	        PrintWriter out = new PrintWriter(file.getAbsoluteFile());
	        System.out.println("FilePath: " + file.getAbsolutePath() );
	 
	        try {
	            //Записываем текст в файл
	            out.println("Create matrix " + xcount + "*" + ycount + " elements and " + grid + "*" + grid + " blocks");
	            out.println("Each block contain matrix " + xgrid + "*" + ygrid);
	            out.println("Min = " + minvalue + " x = " + imin + " y = " + jmin);
	        } finally {
	            //После чего мы должны закрыть файл
	            //Иначе файл не запишется
	            out.close();
	        }
	    } catch(IOException e) {
	        throw new RuntimeException(e);
	    }

	}

}