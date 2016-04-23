package siemens;

import java.io.*;
import java.util.*;
import java.io.Serializable;

public class MBlock implements Serializable, Comparable<MBlock> {
	//int n;
	int nx;
	int ny;
	double xstart;
	double ystart;
	double[][] z;
	double lambda;
	double min;
	double xmin, ymin;

	public MBlock(int x, double a, int y, double b, double step) {
		//n = count;
		nx = x;
		ny = y;
		xstart = a;
		ystart = b;
		lambda = step;
		z = new double[nx][ny];
	}

	public int compareTo(MBlock obj) {
		if (this.min < obj.min) return -1;
		else return 1;
	} 

	public MBlock compute(int x, int y) {
		fill(x, y);
		return this;
	}

	private double calcFunction(double x, double y) {
		return (x+2)*(x+2) + y*y;
	}

	private void fill(int x, int y) {
		for (int i = 0; i < nx; i++) {
			for (int j = 0; j < ny; j++) {
				double xx = xstart + i*lambda;
				double yy = ystart + j*lambda;
				z[i][j] = calcFunction(xx, yy);
				if (i == 0 && j == 0) {
					min = z[i][j];
					xmin = xx; ymin = yy;
				} else {
					if (z[i][j] < min) {
						min = z[i][j];
						xmin = xx; ymin = yy;
					}
				}
			}
		}
		System.out.println("i = " + x + " j = " + y + " Min = " + min + " x = " + xmin + " y = " + ymin);
	}
/*
	public static Comparator<MBlock> myCompare() {
		return new Comparator<MBlock> () {
		public int compare (MBlock entry1, MBlock entry2) {
			int result = entry1.compareTo(entry2);
			return result;
		}	
	};
}
*/

	
}