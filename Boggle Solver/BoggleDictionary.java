import edu.princeton.cs.algs4.Queue;

public class BoggleDictionary {
	
	private static final int R = 26;
	private static final int A = 65;
	
	private Node root;      // root of trie
	
	private static class Node 
	{
        private Integer val;
        private Node[] next = new Node[R];
    }
	
	 public BoggleDictionary() 
	 {
		 
	 }
	 
	 public Integer get(String key) 
	 {
		 if (key == null) throw new IllegalArgumentException("argument to get() is null");
		 Node x = get(root, key, 0);
		 if (x == null) return null;
		 return x.val;
	 }
	 
	 private Node get(Node x, String key, int d) 
	 {
		 if (x == null) return null;
		 if (d == key.length()) return x;
		 char c = key.charAt(d);
		 return get(x.next[c-A], key, d+1);
	 }
	 
	 public void put(String key, Integer val) 
	 {
		 if (key == null) throw new IllegalArgumentException("first argument to put() is null");
		 if (val == null) delete(key);
		 else root = put(root, key, val, 0);
	 }

	 private Node put(Node x, String key, Integer val, int d) 
	 {
		 if (x == null) x = new Node();
		 if (d == key.length())
		 {
			 x.val = val;
			 return x;
			 }
		 char c = key.charAt(d);
		 x.next[c-A] = put(x.next[c-A], key, val, d+1);
		 return x;
	 }
	    
	 public void delete(String key) 
	 {
		 if (key == null) throw new IllegalArgumentException("argument to delete() is null");
		 root = delete(root, key, 0);
		 }

	 private Node delete(Node x, String key, int d) 
	 {
		 if (x == null) return null;
		 if (d == key.length()) 
		 {
			 x.val = null;

		 }
		 else 
		 {
			 char c = key.charAt(d);
			 x.next[c-A] = delete(x.next[c-A], key, d+1);
			 
		 }

		 if (x.val != null) return x;
		 for (int c = A; c < R + A; c++)
			 if (x.next[c-A] != null)
				 return x;
		 return null;
		 
	 }
	 
	 public Iterable<String> keys() 
	 {
		 return keysWithPrefix("");
		 
	 }
	 
	 public Iterable<String> keysWithPrefix(String prefix) 
	 {
		 Queue<String> results = new Queue<String>();
		 Node x = get(root, prefix, 0);
		 collect(x, new StringBuilder(prefix), results);
		 return results;
		 
	 }

	 private void collect(Node x, StringBuilder prefix, Queue<String> results) 
	 {
		 if (x == null) return;
		 if (x.val != null) results.enqueue(prefix.toString());
		 for (char c = A; c < R + A; c++) 
		 {
			 prefix.append(c);
			 collect(x.next[c - A], prefix, results);
			 prefix.deleteCharAt(prefix.length() - 1);
			 
		 }
	 }
	 
	 public boolean validatePrefix(String prefix)
	 {		 
		 Node x = get(root, prefix, 0);
		 
		 if (x == null) return false;
		 else if (x.val != null) return true;
		 else for (int c = 0; c < R; c++) if (x.next[c] != null) return true;
		 
		 return false;
	 }

}
