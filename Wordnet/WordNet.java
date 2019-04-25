import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;

public class WordNet {
	
	private final ST<Integer, Bag<String>> vertexToNoun;
	private final ST<String, Bag<Integer>> nounToVertex;
	private Digraph net;
	private SAP shortest;
	
	public WordNet(String synsets, String hypernyms)
	{
		//Throw an exception if there are null arguments
		if (synsets == null || hypernyms == null) throw new java.lang.IllegalArgumentException("One or more arguments was null");
		
		vertexToNoun = new ST<Integer, Bag<String>>();
		nounToVertex = new ST<String, Bag<Integer>>();
		
		int count = 0;
		In in = new In(synsets);
		while (in.hasNextLine())
		{
			Bag<String> temp = new Bag<String>();
			for (String s : in.readLine().split(",")[1].split(" ")) 
				{
					if(!nounToVertex.contains(s))nounToVertex.put(s, new Bag<Integer>());
					nounToVertex.get(s).add(count);
					temp.add(s);
				}
			vertexToNoun.put(count, temp);
			count++;
		}

		Digraph temp = new Digraph(count);
		in = new In(hypernyms);
		String[] s;
		while (in.hasNextLine())
		{
			s = in.readLine().split(",");
			for (int i = 1; i < s.length; i++)
			{
				temp.addEdge(Integer.parseInt(s[0]), Integer.parseInt(s[i]));
			}
		}
		
		// Check to make sure there's no cycles
		DirectedCycle check = new DirectedCycle(temp);
		if (check.hasCycle()) throw new java.lang.IllegalArgumentException("Input must corrospond to a rooted DAG");
		
		// Check to make sure there's only one root
		int root = -1;
		for (int i = 0; i < temp.V(); i++)
		{
			if (temp.outdegree(i) == 0 && root == -1) root = i;
			else if (temp.outdegree(i) == 0) throw new java.lang.IllegalArgumentException("Input must corrospond to a rooted DAG.");
		}
		
		net = temp;
		shortest = new SAP(net);
	}
	
	public Iterable<String> nouns()
	{
		return nounToVertex.keys();
	}
	
	public boolean isNoun(String word)
	{
		if(word == null)throw new java.lang.IllegalArgumentException("One or more arguments was null");
		
		return nounToVertex.contains(word);
	}
	
	public int distance(String nounA, String nounB)
	{
		if (!isNoun(nounA) || !isNoun(nounB)) throw new java.lang.IllegalArgumentException("Arguments must be WordNet nouns.");
		return shortest.length(nounToVertex.get(nounA), nounToVertex.get(nounB));
	}
	
	public String sap(String nounA, String nounB)
	{
		if (!isNoun(nounA) || !isNoun(nounB)) throw new java.lang.IllegalArgumentException("Arguments must be WordNet nouns.");
		int out = shortest.ancestor(nounToVertex.get(nounA), nounToVertex.get(nounB));
		return vertexToNoun.get(out).iterator().next();
	}
	
	public static void main(String[] args)
	{
		
	}
}
