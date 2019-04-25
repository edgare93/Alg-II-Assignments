import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {

	private final Digraph graph;
	
	public SAP(Digraph G)
	{
		graph = new Digraph(G);
	}
	
	public int length(int v, int w)
	{
		int maxDist = Integer.MAX_VALUE;
		
		BreadthFirstDirectedPaths vPath = new BreadthFirstDirectedPaths(graph, v);
		BreadthFirstDirectedPaths wPath = new BreadthFirstDirectedPaths(graph, w);
		
		for (int i = 0; i < graph.V(); i++)
		{			
			if (vPath.hasPathTo(i) && wPath.hasPathTo(i))
			{
				maxDist = Math.min(maxDist, vPath.distTo(i)+wPath.distTo(i));
			}
		}
		
		if (maxDist == Integer.MAX_VALUE) return -1;
		else return maxDist;
	}
	
	public int ancestor(int v, int w)
	{
		int maxDist = Integer.MAX_VALUE;
		int out = -1;
		
		BreadthFirstDirectedPaths vPath = new BreadthFirstDirectedPaths(graph, v);
		BreadthFirstDirectedPaths wPath = new BreadthFirstDirectedPaths(graph, w);
		
		for (int i = 0; i < graph.V(); i++)
		{
			if(vPath.hasPathTo(i) && wPath.hasPathTo(i))
			{
				int dist = vPath.distTo(i)+wPath.distTo(i);
				if (dist < maxDist) 
					{
						out = i;
						maxDist = dist;
					}
			}
		}
		return out;
	}
	
	public int length(Iterable<Integer> v, Iterable<Integer> w)
	{		
		int maxDist = Integer.MAX_VALUE;
		
		BreadthFirstDirectedPaths vPath = new BreadthFirstDirectedPaths(graph, v);
		BreadthFirstDirectedPaths wPath = new BreadthFirstDirectedPaths(graph, w);
		
		for (int i = 0; i < graph.V(); i++)
		{
			
			if (vPath.hasPathTo(i) && wPath.hasPathTo(i))
			{
				maxDist = Math.min(maxDist, vPath.distTo(i)+wPath.distTo(i));
			}
		}
		
		if (maxDist == Integer.MAX_VALUE) return -1;
		else return maxDist;
	}
	
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w)
	{
		int maxDist = Integer.MAX_VALUE;
		int out = -1;
		
		BreadthFirstDirectedPaths vPath = new BreadthFirstDirectedPaths(graph, v);
		BreadthFirstDirectedPaths wPath = new BreadthFirstDirectedPaths(graph, w);
		
		for (int i = 0; i < graph.V(); i++)
		{
			if (vPath.hasPathTo(i) && wPath.hasPathTo(i))
			{
				int dist = vPath.distTo(i)+wPath.distTo(i);
				if (dist < maxDist) 
					{
						out = i;
						maxDist = dist;
					}
			}
		}
		return out;
	}
	
	public static void main(String[] args) {
	    In in = new In(args[0]);
	    Digraph G = new Digraph(in);
	    SAP sap = new SAP(G);
	    while (!StdIn.isEmpty()) {
	        int v = StdIn.readInt();
	        int w = StdIn.readInt();
	        int length   = sap.length(v, w);
	        int ancestor = sap.ancestor(v, w);
	        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
	    }
	}
}
