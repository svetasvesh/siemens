public class MBlock {
	//int n;
	int nx;
	int ny;
	double xstart;
	double ystart;
	double[][] z;
	double min;
	int imin, jmin;

	public MBlock(int x, double a, int y, double b) {
		//n = count;
		nx = x;
		ny = y;
		xstart = a;
		ystart = b;
	}

	public compute(int i, double istart, int j, double jstart) {
		fill(i, istart, j, jstart);
	}

	private double calcFunction(double x, double y) {
		return (x+2)*(x+2) + y*y;
	}

	private void fill(int ix, double a, int jy, double b) {
		for (int i = 0; i < nx; i++) {
			for (int j = 0; j < ny; j++) {
				z[i][j] = calcFunction(a + ix*lambda, b + jy*lambda);
				if (i == 0 and j == 0) {
					min = z[i][j];
					imin = i; jmin = j;
				} else {
					if (z[i][j] < min) {
						min = z[i][j];
						imin = i; jmin = j;
					}
				}
			}
		}
	}

	
}