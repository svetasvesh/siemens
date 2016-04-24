package siemens;

import java.io.*;
import java.util.*;
import java.io.Serializable;

public class MBlock implements Serializable, Comparable<MBlock> {
	//nx, ny - размеры блока
	int nx;
	int ny;

	//xstart, ystart - значения аргументов функции для первой ячейки блока
	//значения аргументов остальных ячеек рассчитываются через величину lambda
	double xstart;
	double ystart;

	//z - матрица, содержащая значения функции
	double[][] z;

	//lambda - величина шага
	double lambda;
	
	double min;
	double xmin, ymin;

	//конструктор
	public MBlock(int x, double a, int y, double b, double step) {
		nx = x;
		ny = y;
		xstart = a;
		ystart = b;
		lambda = step;
		z = new double[nx][ny];
	}

	//сравнение объектов класса
	public int compareTo(MBlock obj) {
		if (this.min < obj.min) return -1;
		else return 1;
	} 

	public MBlock compute() {
		fill();
		return this;
	}

	//вычисление функции
	private double calcFunction(double x, double y) {
		return (x+2)*(x+2) + y*y;
	}

	//заполнение массива значениями функции и вычисление минимума
	//с точки зрения организации кода - лучше делать это в 2х разных функциях
	//с точки зрения быстродействия - оптимальней вычислить сразу в одном цикле
	private void fill() {
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
	}	
}