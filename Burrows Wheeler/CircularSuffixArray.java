public class CircularSuffixArray {
	
	private static final int R = 256;
	
	private int n;
	private final String text;
	private int[] index;
	private int[] rank;
	private int[] newRank;
	private int offset;
	
    public CircularSuffixArray(String text) 
    {
    	if (text == null) throw new java.lang.IllegalArgumentException("The input cannot be null");
    	n = text.length();
    	this.text = text;
    	index = new int[n + 1];
    	rank = new int[n + 1];
    	newRank = new int[n + 1];
    	
    	index[n]= n;
    	rank[n] = -1;
    	
    	initialize();
    	
    	for (offset = 1; offset < n; offset += offset)
    	{
    		int count = 0;
    		for (int i = 1; i < n + 1; i++)
    		{
    			if (rank[index[i]] == rank[index[i - 1]]) count ++;
    			
    			else if (count > 0)
    			{
    				int left = i-1-count;
                    int right = i-1;
                    quicksort(left, right);
                    
                    int r = rank[index[left]];
                    for (int j = left + 1; j <= right; j++)
                    {
                    	if (less(index[j-1], index[j]))  
                    	{
                            r = rank[index[left]] + j - left; 
                        }
                        newRank[index[j]] = r;
                    }
                    
                    for (int j = left + 1; j <= right; j++) {
                        rank[index[j]] = newRank[index[j]];
                    }

                    count = 0;
    			}
    		}
    	}
    }
    
    private void initialize()
    {
    	int[] count = new int[R + 1];
    	
    	
    	// calculate frequencies
    	for (int i = 0; i < n; i++)
    	{
    		count[text.charAt(i) + 1]++;
    	}
    	
    	// calculate cumulative frequencies
    	for (int i = 0; i < R; i++)
    	{
    		count[i + 1] += count[i];
    	}
    	
    	// compute ranks
    	for (int i = 0; i < n; i++)
    	{
    		rank[i] = count[text.charAt(i)];
    	}
    	
    	// sort by first char
    	for (int i = 0; i < n; i++)
    	{
    		int temp = text.charAt(i);
    		index[count[temp]] = i;
    		count[temp] ++;
    	}
    }

    private void quicksort(int low, int high)
    {
    	if(high <= low) return;
    	int i = partition(low, high);
        quicksort(low, i-1);
        quicksort(i+1, high);
    }
    
    private int partition(int low, int high) {
        int i = low-1, j = high;
        int v = index[high];

        while (true) { 

            // find item on left to swap
            while (less(index[++i], v))
                if (i == high) break;   // redundant

            // find item on right to swap
            while (less(v, index[--j]))
                if (j == low) break;

            // check if pointers cross
            if (i >= j) break;

            exchange(i, j);
        }

        // swap with partition element
        exchange(i, high);

        return i;
    }
    
    private void exchange(int x, int y)
    {
    int temp = index[x];
    index[x] = index[y];
    index[y] = temp;   
    }
    
    private boolean less(int x, int y)
    {
    	return rank[(x + offset) % n] < rank[(y + offset) % n];
    }
    
    public int length() 
    {
    	return n;
    }
    
    public int index(int i) 
    {
    	if(i < 0 || i >= n) throw new java.lang.IllegalArgumentException("Illegal Argument");
    	return index[i];
    }
    
    public static void main(String[] args)
    {
    	String blah = "bba";
    	CircularSuffixArray test = new CircularSuffixArray(blah);
    	for (int i = 0; i < test.length(); i++)
    	{
    		int off = test.index(i);
    		System.out.println(blah.substring(off) + blah.substring(0, off));
    	}
    }
}