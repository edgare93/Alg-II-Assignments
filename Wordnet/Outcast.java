import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
	
	private final WordNet wordnet;
	
	public Outcast(WordNet wordnet)
	{
		this.wordnet = wordnet;
	}
	
	public String outcast(String[] nouns)
	{
		String furthest = nouns[0];
		int longest = 0;
		for (int i = 0; i < nouns.length; i++)
		{
			int distance = 0;
			for (String s : nouns)
			{
				distance += wordnet.distance(nouns[i], s);
			}
			if (distance > longest)
			{
				furthest = nouns[i];
				longest = distance;
			}
		}
		return furthest;
	}
	
	public static void main(String[] args) {
	    WordNet wordnet = new WordNet(args[0], args[1]);
	    Outcast outcast = new Outcast(wordnet);
	    for (int t = 2; t < args.length; t++) {
	        In in = new In(args[t]);
	        String[] nouns = in.readAllStrings();
	        StdOut.println(args[t] + ": " + outcast.outcast(nouns));
	    }
	}
}
