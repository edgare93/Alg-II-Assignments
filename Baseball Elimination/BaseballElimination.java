import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdOut;

public class BaseballElimination {

	private int teams;
	private ST<String, Integer> teamToInt;
	private String[] intToTeam;
	
	
	private int[] wins;
	private int[] losses;
	private int[] remaining;
	
	private int[][] matchups;
	
	public BaseballElimination(String filename)
	{
		In in = new In(filename);
		teams = in.readInt();
		
		teamToInt = new ST<String, Integer>();
		intToTeam = new String[teams];
		
		wins = new int[teams];
		losses = new int[teams];
		remaining = new int[teams];
		
		matchups = new int[teams][teams];
		
		
		for (int i = 0; i < teams; i++)
		{
			String name = in.readString();
			intToTeam[i]=name;
			teamToInt.put(name, i);
			wins[i] = in.readInt();
			losses[i] = in.readInt();
			remaining[i] = in.readInt();
			
			for (int j = 0; j < teams; j++)
			{
				matchups[i][j] = in.readInt();
			}
		}
	}
	
	public int numberOfTeams()
	{
		return teams;
	}
	
	public Iterable<String> teams()
	{
		return teamToInt.keys();
	}
	
	public int wins(String team)
	{
		if (!teamToInt.contains(team)) throw new java.lang.IllegalArgumentException("Must provide a valid team name.");
		return wins[teamToInt.get(team)];
	}
	
	public int losses(String team)
	{
		if (!teamToInt.contains(team)) throw new java.lang.IllegalArgumentException("Must provide a valid team name.");
		return losses[teamToInt.get(team)];
	}
	
	public int remaining(String team)
	{
		if (!teamToInt.contains(team)) throw new java.lang.IllegalArgumentException("Must provide a valid team name.");
		return remaining[teamToInt.get(team)];
	}
	
	public int against(String team1, String team2)
	{
		if (!teamToInt.contains(team1) || !teamToInt.contains(team2)) throw new java.lang.IllegalArgumentException("Must provide a valid team name.");
		return matchups[teamToInt.get(team1)][teamToInt.get(team2)];
	}
	
	public boolean isEliminated(String team)
	{
		if (!teamToInt.contains(team)) throw new java.lang.IllegalArgumentException("Must provide a valid team name.");
		
		int id = teamToInt.get(team);
		
		for (int i : wins) if (i > wins[id] + remaining[id]) return true;
		
		FlowNetwork check = makeFlow(id);
		
		int games = 0;
		for (FlowEdge f : check.adj(check.V() - 2))
		{
			games += f.capacity();
		}
		
		FordFulkerson max = new FordFulkerson(check, check.V() - 2, check.V() - 1);
		
		return (max.value() < games);
		
	}
	
	public Iterable<String> certificateOfElimination(String team)
	{
		if (!teamToInt.contains(team)) throw new java.lang.IllegalArgumentException("Must provide a valid team name.");
		
		int id = teamToInt.get(team);
		Bag<String> b = new Bag<String>();
		
		for (int i = 0; i < teams; i++) if (wins[i] > wins[id] + remaining[id])
		{
			b.add(intToTeam[i]);
			return b;
		}
		
		FlowNetwork check = makeFlow(id);
		FordFulkerson ford = new FordFulkerson(check, check.V() - 2, check.V() - 1);
		
		
		for(int i = 0; i < teams; i++)
		{
			if (ford.inCut(i)) b.add(intToTeam[i]);
		}
		return b;
	}
	
	private FlowNetwork makeFlow(int id)
	{
		int size = 2 + teams + teams * teams;
		/*
		 * size - 1 is the sink
		 * size - 2 is the source
		 * 0 through teams - 1 are the team vertices
		 * teams through size - 3 are the match vertices, reached by (teams + i + teams * j)
		 * 
		 */
		FlowNetwork flow = new FlowNetwork(size);
		
		for (int i = 0; i < teams; i++)
		{
			if (i == id) continue;
			
			flow.addEdge(new FlowEdge(i, size - 1, wins[id] + remaining[id] - wins[i]));
			
			for (int j = i; j < teams; j++)
			{
				if (j == id) continue;
				flow.addEdge(new FlowEdge(size - 2, (teams + i + teams * j), matchups[i][j]));
				
				flow.addEdge(new FlowEdge((teams + i + teams * j), i, Double.POSITIVE_INFINITY));
				flow.addEdge(new FlowEdge((teams + i + teams * j), j, Double.POSITIVE_INFINITY));
			}
		}
		
		return flow;
	}
	
	public static void main(String[] args) {
	    BaseballElimination division = new BaseballElimination(args[0]);
	    for (String team : division.teams()) {
	        if (division.isEliminated(team)) {
	            StdOut.print(team + " is eliminated by the subset R = { ");
	            for (String t : division.certificateOfElimination(team)) {
	                StdOut.print(t + " ");
	            }
	            StdOut.println("}");
	        }
	        else {
	            StdOut.println(team + " is not eliminated");
	        }
	    }
	}
	
}
