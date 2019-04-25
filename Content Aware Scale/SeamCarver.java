import edu.princeton.cs.algs4.Picture;

public class SeamCarver {

	private int w, h;

	private int[][] pixels;
	private double[][] energies;
	
	public SeamCarver(Picture picture)
	{
		w = picture.width();
		h = picture.height();

		energies = new double[w][h];
		pixels = new int[w][h];

		setColor(picture);
		for (int i = 0; i < w; i++)
		{
			for (int j = 0; j < h; j++)
			{
				setEnergy(i,j);
			}
		}
	}

	private void setEnergy(int x, int y)
	{
		if (x == 0 || y == 0 || x == w-1 || y == h-1) energies[x][y]=1000;
		else
		{
			int rgb1 = pixels[x + 1][y];
			int rgb2 = pixels[x - 1][y];

			int r = ((rgb1 >> 16) & 0xFF) - ((rgb2 >> 16) & 0xFF);
			int g = ((rgb1 >> 8) & 0xFF) - ((rgb2 >> 8) & 0xFF);
			int b = ((rgb1 >> 0) & 0xFF) - ((rgb2 >> 0) & 0xFF);

			double xGrad = Math.pow(r, 2) + Math.pow(g, 2) + Math.pow(b, 2);

			rgb1 = pixels[x][y + 1];
			rgb2 = pixels[x][y - 1];

			r = ((rgb1 >> 16) & 0xFF) - ((rgb2 >> 16) & 0xFF);
			g = ((rgb1 >> 8) & 0xFF) - ((rgb2 >> 8) & 0xFF);
			b = ((rgb1 >> 0) & 0xFF) - ((rgb2 >> 0) & 0xFF);

			double yGrad = Math.pow(r, 2) + Math.pow(g, 2) + Math.pow(b, 2);

			energies[x][y] = Math.sqrt(xGrad + yGrad);
		}
	}

	private void setColor(Picture picture)
	{
		for (int i = 0; i < w; i++)
		{
			for (int j = 0; j < h; j++)
			{
				pixels[i][j] = picture.getRGB(i, j);
			}
		}

	}

	public Picture picture()
	{
		Picture out = new Picture(w, h);
		for (int i = 0; i < w; i++)
		{
			for (int j = 0; j < h; j++)
			{
				out.setRGB(i, j, pixels[i][j]);
			}
		}

		return out;
	}

	public int width()
	{
		return w;
	}

	public int height()
	{
		return h;
	}

	public double energy(int x, int y)
	{
		if (x > w-1 || x < 0 || y > h-1 || y < 0) throw new java.lang.IllegalArgumentException("Invalid pixel specified.");
		else return energies[x][y];
	}

	public int[] findHorizontalSeam()
	{
		int[][] edgeTo = new int[w][h];
		double[][] distTo = new double[w][h];
		int[] out = new int[w];

		for (int i = 1; i < w; i++)
		{
			for (int j = 1; j < h - 1; j++)
			{
				distTo[i][j] = Integer.MAX_VALUE;
				
				if (distTo[i][j] > distTo[i - 1][j] + energies[i][j])
				{
					
					distTo[i][j] = distTo[i - 1][j] + energies[i][j];
					edgeTo[i][j] = j;
				}
				if ((j < h - 2) && distTo[i][j] > distTo[i - 1][j + 1] + energies[i][j])
				{
					
					distTo[i][j] = distTo[i - 1][j + 1] + energies[i][j];
					edgeTo[i][j] = j + 1;
				}
				if ((j > 1) && distTo[i][j] > distTo[i - 1][j - 1] + energies[i][j])
				{
					
					distTo[i][j] = distTo[i - 1][j - 1] + energies[i][j];
					edgeTo[i][j] = j - 1;
				}
			}
		}
		
		double min = Double.POSITIVE_INFINITY;
		for (int j = 1; j < h - 1; j++)
		{
			if (distTo[w - 1][j] < min) 
				{
					out[w - 1] = j;
					min = distTo[w - 1][j];
				}
		}
		
		for (int i = w - 1; i > 0; i--) out[i - 1] = edgeTo[i][out[i]];
		
		return out;
	}
	
	public int[] findVerticalSeam()
	{
		int[][] edgeTo = new int[w][h];
		double[][] distTo = new double[w][h];
		int[] out = new int[h];

		for (int j = 1; j < h; j++)
		{
			for (int i = 1; i < w - 1; i++)
			{
				distTo[i][j]=Integer.MAX_VALUE;
				
				if(distTo[i][j] > distTo[i][j-1] + energies[i][j])
				{
					
					distTo[i][j] = distTo[i][j-1] + energies[i][j];
					edgeTo[i][j] = i;
				}
				if((i < w - 2) && distTo[i][j] > distTo[i + 1][j - 1] + energies[i][j])
				{
					
					distTo[i][j] = distTo[i+1][j-1] + energies[i][j];
					edgeTo[i][j] = i + 1;
				}
				if((i > 1) && distTo[i][j] > distTo[i - 1][j - 1] + energies[i][j])
				{
					
					distTo[i][j] = distTo[i - 1][j - 1] + energies[i][j];
					edgeTo[i][j] = i - 1;
				}
			}
		}
		
		double min = Double.POSITIVE_INFINITY;
		for (int i = 1; i < w - 1; i++)
		{
			if (distTo[i][h-1] < min) 
				{
					out[h-1] = i;
					min = distTo[i][h-1];
				}
		}
		
		for (int j = h - 1; j > 0; j--) out[j-1] = edgeTo[out[j]][j];
		
		return out;
	}

	public void removeHorizontalSeam(int[] seam)
	{
		validateSeam(seam);
		if (seam.length != width()) 	throw new java.lang.IllegalArgumentException("The seam provided has the wrong dimensions");
		if (height() <= 1) 				throw new java.lang.IllegalArgumentException("The seam could not be removed");
		
		int[][] replace = new int[w][h - 1];
		
		for (int i = 0; i < w; i++)
		{
			for (int j = 0; j < seam[i]; j++)
			{
				replace[i][j] = pixels[i][j];
			}
			for (int j = seam[i]; j < h - 1; j++)
			{
				replace[i][j] = pixels[i][j + 1];
			}
		}
		
		pixels = replace;
		h--;
		
		for (int i = 0; i < w; i++)
		{
			if (seam[i] > 0) setEnergy(i,seam[i] - 1);
			if (seam[i] < w - 1) setEnergy(i,seam[i]);
		}
	}

	public void removeVerticalSeam(int[] seam)
	{
		validateSeam(seam);
		if (seam.length != height()) 	throw new java.lang.IllegalArgumentException("The seam provided has the wrong dimensions");
		if (width() <= 1) 				throw new java.lang.IllegalArgumentException("The seam could not be removed");
		
		int[][] replace = new int[w-1][h];
		
		for (int j = 0; j < h; j++)
		{
			for (int i = 0; i < seam[j]; i++)
			{
				replace[i][j] = pixels[i][j];
			}
			for (int i = seam[j]; i < w - 1; i++)
			{
				replace[i][j] = pixels[i + 1][j];
			}
		}
		
		pixels = replace;
		w--;
		
		for (int j = 0; j < h; j++)
		{
			if (seam[j] > 0) setEnergy(seam[j] - 1, j);
			if (seam[j] < h - 1) setEnergy(seam[j], j);
		}
	}
	
	private void validateSeam(int[] seam)
	{
		if (seam == null ) 				throw new java.lang.IllegalArgumentException("The seam provided must not be null.");
		
			for (int i = 1; i < seam.length; i++)
			{
				int check = seam[i] - seam[i-1];
				if (check > 1 || check < -1) throw new java.lang.IllegalArgumentException("The seam provided is not valid");
			}
		}
	
	public static void main(String[] args) 
	{
		
    }
}
