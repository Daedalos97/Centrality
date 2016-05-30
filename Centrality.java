package org.main;

import java.awt.Color;
import java.io.IOException;

/**
 *
 * @author Sam
 */
public class Centrality {
    
    private Graph g; //The graph we will construct;
    private static double a = 0.2; //factor used in Katz
    private static int[][] SPmatrix; //A matrix which holds the shortest paths based on floyd warshall.
    private static int[][][] AllSP; //Holds all the possible shortest paths for a matrix;
    
    public static void main(String[] args) {
        Graph g = new Graph();
        try {
            g.read(args[0]);
        } catch (IOException IOE) {
            IOE.printStackTrace();
        }
        System.out.println("Number of vertices in graph: " + g.getNumberOfVertices());
        SPmatrix = Floyd_Warshall(g);
        printTop("Degree", Degree(g));
        printTop("Closeness", Close(g));
        printTop("Betweenness", Between(g));
        printTop("Katz", Katz(g,a));
        if (args[1].equals("true")) {
            GraphVisualiser GV = new GraphVisualiser(g, "Project Graph", 1980,1000, Color.WHITE);
        }
        
    }
    
   /**
     * Degree centrality is a measure of how many edges are incident on a
     * specific node in a graph.
     * @param g The graph constructed from the input values in the form of an
     * adjacency matrix.
     * @return An array of size equal to vertices in the graph. Access via
     * array[i] where i is the vertex, and the output is the number of vertices
     * incident on the i th vertex.
     */
    public static double[][] Degree(Graph g) {
        int[] degree = new int[g.getNumberOfVertices()];
        int[][] edge = g.getEdgeMatrix(); 
        for(int i = 0; i < g.getNumberOfVertices(); i++) {
            degree[i] = 0;
            for(int j = 0; j < g.getNumberOfVertices(); j++) {
                if(edge[i][j] == 1){
                    degree[i]++;
                }
            }
        }
        SortedQueue q = new SortedQueue(degree,g);
        return q.getQueue();
    }
    
    /**
     * Closeness Centrality is a measure of 
     * @param g The graph constructed from the input values in the form of an
     * adjacency matrix.
     * @return A sorted array of the top 5 nodes and their Closeness Centrality.
     */
    public static double[][] Close(Graph g) {
        int[][] path = SPmatrix;
        double[] close = new double[path.length];
        for (int i = 0; i < path.length; i++) {
            close[i] = 0.0;
        }
        //Start and end correspond to the start and end points of a path (i,j).
        for(int start = 0; start < path.length; start++) {
            for(int end = 0; end < path.length; end++) {
                close[start] = close[start] + (double)path[start][end];
            }
            close[start] = 1/close[start];
        }
        SortedQueue q = new SortedQueue(close,g);
        return q.getQueue();
    }
    
    /**
     * Implements a modified Floyd Warshall that finds the length of the shortest
     * paths as well as tracking all the shortest paths.
     * @param g The graph we wish to find the the shortest paths for all vertices.
     * @return The matrix with the weights of all the shortest paths.
     */
    private static int[][] Floyd_Warshall(Graph g) {
        int[][] path = g.getEdgeMatrix();
        int vertNum = path.length;
        int[][][] AllShortestPaths = new int[vertNum][vertNum][vertNum];
        int inf = Integer.MAX_VALUE/2;
        for (int i = 0; i < vertNum; i++) {
            for (int j = 0; j < vertNum; j++) {
                if (i != j && path[i][j] == 0) {
                    path[i][j] = inf;
                }
                for (int k = 0; k < vertNum; k++) {
                    AllShortestPaths[i][j][k] = 0;
                }
            }
        }
        for (int k = 0; k < vertNum; k++) {
            for (int i = 0; i < vertNum; i++) {
                for (int j = 0; j < vertNum; j++) {
                    if ((path[i][k] + path[k][j]) < path[i][j]) {
                        path[i][j] = path[i][k] + path[k][j];
                        AllShortestPaths[i][j][k]++;
                    }
                }
            }
        }
        AllSP = AllShortestPaths;
        return path;
    }
    
    /**
     * Betweenness Centrality measures the importance of a vertex based on the 
     * sum of the shortest paths that pass through that vertex.
     * @param g The graph constructed from the input values in the form of an
     * adjacency matrix.
     * @return A sorted array of the top 5 nodes and their Betweenness Centrality.
     */
    public static double[][] Between(Graph g) {
        int[][] path = SPmatrix;
        int vertNum = path.length;
        int[][][] things = AllSP;
        double[] between = new double[vertNum];
        int inf = Integer.MAX_VALUE/2;
        for (int i = 0; i < vertNum; i++) {
            between[i] = 0.0;
        }
        for (int i = 0; i < vertNum; i++) { 
            for (int j = 0; j < vertNum; j++) {
                int numSP = 0; //Total number of shortest paths through i-j for a k.
                for (int k = 0; k < vertNum; k ++) {
                    numSP += things[i][j][k];
                }
                for (int k = 0; k < vertNum; k++) {
                    if (numSP != 0)
                    between[k] += (double)things[i][j][k]/(double)numSP;
                }
            }
        }
        SortedQueue SQ = new SortedQueue(between,g);
        return SQ.getQueue();
    }
    
    /**
     * Katz Centrality measures the degree of influence of a node. 
     * This is calculated using a given factor a where 0<a<1.
     * So the further away a node is from another, the less influence it has over it.
     * The Katz Centrality is just the sum of all the influence values of a node.
     * @param g
     * @param factor
     * @ return A sorted array of the top 5 nodes and their Katz Centrality.
     */
    public static double[][] Katz(Graph g, double factor) {
        int[][] path = SPmatrix;
        int vertNum = path.length; //The number of vertices in the graph.
        double[] katz = new double[vertNum];
        for (int i = 0; i < vertNum; i++) {
            katz[i] = 0.0;
        }
        for(int start = 0; start < vertNum; start++) {
            for(int end = 0; end < vertNum; end++) {
                katz[start] = katz[start]+Math.pow(factor,path[start][end]);
            }
        }
        SortedQueue q = new SortedQueue(katz,g);
        return q.getQueue();
    }
    
    private static void printTop(String measureName , double[][] top5) {
        System.out.println("\nHighest " + measureName + " Centralities (top 5): ");
        for (double[] d : top5) {
            System.out.println("Node: " + (int)d[0] + " Value: " + d[1]);
        }
    }
}