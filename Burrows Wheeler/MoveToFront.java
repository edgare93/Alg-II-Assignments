import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront 
{
	
	  // apply move-to-front encoding, reading from standard input and writing to standard output
	public static void encode()
    {
		String in = BinaryStdIn.readString();
		BinaryStdOut.write(encode(in));
    	BinaryStdOut.close();
    }
	
	private static String encode(String s)
    {
    	StringBuilder out = new StringBuilder();
    	final int R = 256;
    	char[] chars = new char[R];
    	for (int i = 0; i < R; i++)
    	{
    		chars[i] = (char)i;
    	}
    	for (int j = 0; j < s.length(); j++)
    	{
    		char c = s.charAt(j);
    		char replace = c;
    		char hold;
    		for (int i = 0; i < R; i++)
    		{
    			hold = chars[i];
    			chars[i] = replace;
    			replace = hold;
    			
    			if (hold == c)
    			{
    				out.append((char)i);
    				i = R;
    			}
    		}
    	}
    	return out.toString();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode()
    {
    	String in = BinaryStdIn.readString();
    	BinaryStdOut.write(decode(in));
    	BinaryStdOut.close();
    	
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    private static String decode(String s)
    {
    	StringBuilder out = new StringBuilder();
    	final int R = 256;
    	char[] chars = new char[R];
    	for (int i = 0; i < R; i++)
    	{
    		chars[i] = (char)i;
    	}
    	
    	for (int j = 0; j < s.length(); j++)
    	{
    		char c = s.charAt(j);

    		char add = chars[(int)c];
			out.append(add);
			
    		for (int i = (int)c; i > 0; i--)
    		{
    			chars[i] = chars[i-1];
    		}
    		chars[0] = add;
    	}
    	
    	return out.toString();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args)
    {	
    	if(args[0].equals("-"))
    		encode();
    	else if (args[0].equals("+"))
    		decode();
    }
}
