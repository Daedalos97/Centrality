import java.awt.Color;
import java.io.IOException;

/**
 * Implements four centrality measures of a graph. Such as the Degree, Closeness,
 * Betweenness and Katz Centralities.
 * @author Samuel Heath 21725083, Bryan Trac 21704976
 */
public class Centrality {
    
    private static Graph g; //The graph we will construct;
    private static double a = 0.5; //Factor used in Katz
    private static int[][] SPmatrix; //A matrix which holds the lengths of shortest paths based on floyd warshall.
    
    /**
     * The main method which runs the centrality measures and prints their outputs.
     * @param  args the name of the edges file and a boolean value of whether you
     * want to generate a picture of the graph, via a graph visualisation class.
     */
    public static void main(String[] args) {
        g = new Graph();
        try {
            g.read(args[0]);
        } catch (IOException IOE) {
            IOE.printStackTrace();
        }
        a = Double.parseDouble(args[1]);
        System.out.println("Number of vertices in graph: " + g.getNumberOfVertices() 
                + "\nUsing alpha value of " + a + " for Katz Centrality.");
        SPmatrix = Floyd_Warshall(g);
        printTop("Degree", Degree(g));
        printTop("Closeness", Close(g));
        printTop("Betweenness", between(g));
        printTop("Katz", Katz(g,a));
        if (args[2].equals("true")) {
            GraphVisualiser GV = new GraphVisualiser(g, "Project Graph", 1980,1000, Color.WHITE);
        }
        
    }
    
   /**
     * Degree centrality is a measure of how many edges are incident on a
     * specific node in a graph.
     * @param   g The graph constructed from the input values in the form of an
     * adjacency matrix.
     * @return  An array of size equal to vertices in the graph. Access via
     * array[i] where i is the vertex, and the output is the number of vertices
     * incident on the i th vertex.
     */
    public static double[][] Degree(Graph g) {
        int[] degree = new int[g.getNumberOfVertices()];
        int[][] edge = g.getEdgeMatrix(); 
        for(int i = 0; i < g.getNumberOfVertices(); i++) {
            degree[i] = 0;
            for(int j = 0; j < g.getNumberOfVertices(); j++)
                if(edge[i][j] == 1){
                    degree[i]++;
                }
        }
        return new SortedQueue(degree,g).getQueue();
    }
    
    /**
     * Closeness Centrality is a measure of 
     * @param   g The graph constructed from the input values in the form of an
     * adjacency matrix.
     * @return  A sorted array of the top 5 nodes and their Closeness Centrality.
     */
    public static double[][] Close(Graph g) {
        int[][] path = SPmatrix;
        double[] close = new double[path.length];
        for (int i = 0; i < path.length; i++)
            close[i] = 0.0;
        //Start and end correspond to the start and end points of a path (i,j).
        for(int start = 0; start < path.length; start++) {
            for(int end = 0; end < path.length; end++) {
                close[start] = close[start] + (double)path[start][end];
            }
            close[start] = (double)1/close[start];
        }
        return new SortedQueue(close,g).getQueue();
    }
    
    /**
     * Implements a modified Floyd Warshall that finds the length of the shortest
     * paths as well as tracking all the shortest paths.
     * @param   g The graph we wish to find the the shortest paths for all vertices.
     * @return  The matrix with the weights of all the shortest paths.
     */
    private static int[][] Floyd_Warshall(Graph g) {
        int[][] path = g.getEdgeMatrix();
        int vertNum = path.length;
        int inf = Integer.MAX_VALUE/2;
        for (int i = 0; i < vertNum; i++)
            for (int j = 0; j < vertNum; j++)
                if (i != j && path[i][j] == 0) {
                    path[i][j] = inf;
                }
        for (int k = 0; k < vertNum; k++) 
            for (int i = 0; i < vertNum; i++)
                for (int j = 0; j < vertNum; j++)
                    if ((path[i][k] + path[k][j]) < path[i][j]) {
                        path[i][j] = path[i][k] + path[k][j];
                    }
        return path;
    }
    
    /**
     * Betweenness Centrality measures the importance of a vertex based on the 
     * sum of the shortest paths that pass through that vertex.
     * @param   g The graph constructed from the input values in the form of an
     * adjacency matrix.
     * @return  A sorted array of the top 5 nodes and their Betweenness Centrality.
     */
    private static double[][] between(Graph g) {
        int[][] adj = g.getEdgeMatrix(); //The adjacency matrix.
        int vertNum = adj.length; // Number of nodes in the graph.
        double[] between = new double[vertNum]; //Stores calculated values for Betweenness
        for (int i = 0; i < vertNum; i++) {
            between[i] = 0.0;
        }
        for (int k = 0; k < vertNum; k++) {
            Stack stack = new Stack();
            int[] sp = new int[vertNum]; 
            List[] pred = new List[vertNum]; //Make into a list;
            int[] dist = new int[vertNum];
            for (int m = 0; m < vertNum; m++) {
                dist[m] = -1;
                sp[m] = 0;
                pred[m] = new List();
            }
            sp[k] = 1;
            dist[k] = 0;
            Queue Q = new Queue();
            Q.enqueue(k);
            while (!Q.isEmpty()) {
                int v = 0;
                try {
                    v = (int) Q.dequeue();
                } catch (Exception E) { }
                stack.push(v);
                for (int i = 0; i < vertNum; i++) {
                    if (adj[v][i] == 1 && dist[i] < 0) {
                        Q.enqueue(i);
                        dist[i] = dist[v] + 1;
                    }
                    if (adj[v][i] == 1 && dist[i] == (dist[v]+1)) {
                        sp[i] = sp[i] + sp[v];
                        pred[i].append(v);
                    }
                }
            }
            double[] delta = new double[vertNum];
            for (int i = 0; i < vertNum; i++) {
                delta[i] = 0;
            }
            while (!stack.isEmpty()) {
                int w = 0;
                try {
                    w = (int)stack.pop();
                } catch (Exception E) { }
                pred[w].beforeFirst();
                //For all the vertices in the predecessor list do
                for (int i = 0; i < pred[w].getSize(); i++) {
                    int v = 0;
                    try {
                        pred[w].next();
                        v = (int) pred[w].examine();
                    } catch (Exception E) { }
                    delta[v] = delta[v] + (((double)sp[v]/(double)sp[w])* (1 + delta[w]));
                }
                if (w != k) {
                    between[w] = between[w] + delta[w];
                }
            }
        }
        return new SortedQueue(between,g).getQueue();
    }
    
    /**
     * Katz Centrality measures the degree of influence of a node. 
     * This is calculated using a given factor a where 0<a<1.
     * So the further away a node is from another, the less influence it has over it.
     * The Katz Centrality is just the sum of all the influence values of a node.
     * @param   g The graph we are analysing for this measure.
     * @param   factor The factor of used in calculating the distance a node is from
     * the next, based on some input factor < 1 and > 0.
     * @return  A sorted array of the top 5 nodes and their Katz Centrality.
     */
    public static double[][] Katz(Graph g, double factor) {
        int[][] path = SPmatrix;
        int vertNum = path.length; //The number of vertices in the graph.
        double[] katz = new double[vertNum];
        for (int i = 0; i < vertNum; i++) {
            katz[i] = 0.0;
        }
        for(int start = 0; start < vertNum; start++)
            for(int end = 0; end < vertNum; end++)
                katz[start] = katz[start]+Math.pow(factor,path[start][end]);
        return new SortedQueue(katz,g).getQueue();
    }
    
    /**
     * Prints out the array for the input measures output.
     * @param  measureName The name of the centrality measure's output being printed.
     * @param  top5 The measure's output array, with the ID's and values for that
     * measure.
     */
    private static void printTop(String measureName , double[][] top5) {
        System.out.println("\nHighest " + measureName + " Centralities (top 5): ");
        for (double[] d : top5)
            System.out.println("Node: " + (int)d[0] + " Value: " + d[1]);
    }
}