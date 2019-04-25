import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TrieST;

public class BoggleSolver
{
	private final BoggleDictionary dict;
	
	
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary)
    {
    	BoggleDictionary temp = new BoggleDictionary();
    	for (String s : dictionary)
    	{
    		int score = 0;
    		int l = s.length();
    		if (l == 3 || l == 4) score = 1;
    		else if (l == 5) score = 2;
    		else if (l == 6) score = 3;
    		else if (l == 7) score = 5;
    		else if (l >= 8) score = 11;
    		
    		
    		if (l >= 3) temp.put(s, score);
    	}
    	dict = temp;
    }
    
    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board)
    {
    	TrieST<Integer> out = new TrieST<Integer>();
    	boolean[][] visited = new boolean[board.cols()][board.rows()];
    	int length = 0;
    	String progress = "";
    	
    	for (int i = 0; i < board.cols(); i++)
    	{
    		for (int j = 0; j < board.rows(); j++)
    		{
    			visited[i][j] = true;
    			if(board.getLetter(i, j) == 'Q') 
    				{
    					WordHelper(out, visited, board, length + 2, i, j, progress + "QU");
    				}
    			else
    				{
    					WordHelper(out, visited, board, length + 1, i, j, progress + board.getLetter(i, j));
    				}
    			
    			visited[i][j] = false;
    		}
    	}
    	return out.keys();
    }
    
    private void WordHelper(TrieST<Integer> out, boolean[][] visited, BoggleBoard board, int length, int x, int y, String progress)
    {
    	if(!dict.validatePrefix(progress)) return;
    	if (length >= 3 && dict.get(progress) != null) out.put(progress, 1);
    	
    	for (int i = -1; i <= 1; i++)
    	{
    		for (int j = -1; j <= 1 ; j++)
    		{
    			if (x + i >=0 && y + j>= 0 && x + i < board.cols() && y + j < board.rows() && !visited[x + i][y + j])
    			{
    				visited[x + i][y + j] = true;
        			if(board.getLetter(x + i, y + j) == 'Q') 
        				{
        					WordHelper(out, visited, board, length + 2, x + i, y + j, progress + "QU");
        				}
        			else
        				{
        					WordHelper(out, visited, board, length + 1, x + i, y + j, progress + board.getLetter(x + i, y + j));
        				}
        			
        			visited[x + i][y + j] = false;
    			}
    		}
    	}
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word)
    {
    	Integer i = dict.get(word);
    	if (i==null) return 0;
    	else return i;
    	
    }

//    public static void main(String[] args) {
//       String prefix = "C:\\Users\\Shogu\\workspace\\Assignment 4\\boggle\\";
//    	In in = new In(prefix + "dictionary-yawl.txt");
//        String[] dictionary = in.readAllStrings();
//        BoggleSolver solver = new BoggleSolver(dictionary);
//        BoggleBoard board = new BoggleBoard(prefix + "board-points1111.txt");
//        int score = 0;
//        for (String word : solver.getAllValidWords(board)) {
//            StdOut.println(word);
//            score += solver.scoreOf(word);
//        }
//        StdOut.println("Score = " + score);
//    }
    
    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}