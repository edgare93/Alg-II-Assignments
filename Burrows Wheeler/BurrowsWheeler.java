import java.util.Arrays;
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler 
{
	private static final int R = 256;
	
	 // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
    public static void transform()
    {
    	String in = BinaryStdIn.readString();
    	BinaryStdOut.write(transform(in));
    	BinaryStdOut.close();
    }

    private static String transform(String in)
    {
    	CircularSuffixArray array = new CircularSuffixArray(in);

    	for (int i = 0; i < in.length(); i++)
    	{
    		if (array.index(i) == 0) BinaryStdOut.write(i);
    	}
    	
    	StringBuilder out = new StringBuilder();
    	
    	for(int i = 0; i < in.length(); i++)
    	{
    		char temp = in.charAt(((array.index(i) + array.length() - 1)%array.length()));
    		out.append(temp);
    	}
    	
    	return out.toString();
    }
    
    // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
    public static void inverseTransform()
    {
    	int start = BinaryStdIn.readInt();
    	String in = BinaryStdIn.readString();
    	BinaryStdOut.write(inverseTransform(start, in));
    }

    private static String inverseTransform(int start, String in)
    {
    	int n = in.length();

    	int[] next = new int[n];
    	
    	int[] count = new int[R + 1];
    	
    	// calculate frequencies
    	for (int i = 0; i < n; i++)
    	{
    		count[in.charAt(i) + 1]++;
    	}
    	
    	for (int i = 0; i < R; i++)
    	{
    		count[i + 1] += count[i];
    	}
    	
    	for (int i = 0; i < n; i++)
    	{
    		int temp = in.charAt(i);
    		next[count[temp]] = i;
    		count[temp] ++;
    	}
    	
    	StringBuilder out = new StringBuilder();
    	int point = start;
    	char[] sorted = in.toCharArray();
    	Arrays.sort(sorted);
    	for (int i = 0; i < n; i++)
    	{
    		out.append(sorted[point]);
    		point = next[point];
    	}
    	return out.toString();
    }
    
    // if args[0] is '-', apply Burrows-Wheeler transform
    // if args[0] is '+', apply Burrows-Wheeler inverse transform
    public static void main(String[] args)
    {
    	
    	if(args[0].equals("-"))
    		transform();
    	else if (args[0].equals("+"))
    		inverseTransform();
    }

}
