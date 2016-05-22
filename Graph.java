package org.main;

import java.util.Scanner;
import java.io.IOException;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.io.File;

/**
 * Implements a class which can read an edge data file and produce an adjacency
 * matrix.
 * @author Sam 21725083, Brayn Trac .
 */
public class Graph {
    private int[][] matrix; // The adjacency matrix of the graph from the data supplied.
    private int[] nodes; // The array of the unique nodes in graph sorted in ascending order.
    private int nodeNum = 0; // Used to hold the length of vertices in this graph.
    
    /**
     * Reads the edge data from an input file.
     * @param filename Local path to the file and the extension e.g. ".txt".
     * @throws IOException If file is not in the local directory.
     */
    public void read(String filename) throws IOException {
        try {
            File file = new File(Graph.class.getProtectionDomain().getCodeSource().getLocation().getFile());        
            String path = file.getParent() + File.separator + filename + ".txt";
            //Find out the max number of nodes in the graph.
            LineNumberReader  lnr = new LineNumberReader(new FileReader(path));
            lnr.skip(Long.MAX_VALUE);
            int lines = lnr.getLineNumber();
            lnr.close();
            nodes = new int[(lines*2)]; //Create array with max size
            Scanner scan = new Scanner(new FileReader(path));
            while (scan.hasNextLine()) {
                for (String s : scan.nextLine().split(" "))
                    addNode(Integer.parseInt(s)); //Check nodes are unique.
                    
            }
            quickSort(nodes, 0, nodeNum-1); //Sort nodes so we can binary search them later.
            
            //Create Adjacency matrix from the number of nodes in graph. nodes[i] --> matrix[i][...]
            //nodes[i] corresponds to i th position in the matrix, so we have nodes[i] corresponds to
            //the relationship between node i and all the other nodes.
            matrix = new int[nodeNum][nodeNum];
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix.length; j++) matrix[i][j] = 0;
            }
            //Writes edges into the adjacency matrix.
            scan = new Scanner(new FileReader(path));
            while (scan.hasNextLine()) {
                String[] line = scan.nextLine().split(" ");
                matrix[searchNodes(Integer.parseInt(line[0]))][searchNodes(Integer.parseInt(line[1]))] = 1;
            }
        } catch (IOException IOE) {
            usage();
            IOE.printStackTrace();
        }
    }
    
    /**
     * Outputs the correct usage of the read method, if the method is called and
     * no file is readable.
     */
    private void usage() {
        System.out.println("Please try again, and enter a filename argument"
                + " that is in the local directory of this code.");
    }
    
    /**
     * Adds a node to the node matrix iff it is not already in there.
     * @param node The (hopefully) unique node being added to the nodes array.
     */
    private void addNode(int node) {
        if (nodeNum == 0) {
            nodes[nodeNum] = node;
            nodeNum++;
        } else if (!Search(nodes, node)) {
            nodes[nodeNum] = node;
            nodeNum++;
        }
    }
    
    /**
     * Returns to the user the node number, with the same index as the Edge Matrix.
     * @param   pos The index from the adjacency matrix we want to know the node number of.
     * @return  The full id/number of the node which corresponds to the index of the Adjacency Matrix.
     */
    public int getNode(int pos) {
        return nodes[pos];
    }
    
    /**
     * Searches the nodes array to check whether or not a node has been added.
     * @param a     The array we are looking over.
     * @param query The node we are checking to see if if it has been added.
     * @return      The index of searched item or -1 if it is not found.
     */
    private boolean Search(int[] a, int query) {
        for (int i = 0; i < a.length; i ++) {
            if (a[i] == query) return true;
        }
        return false;
    }
    
    /**
     * Implements a Binary Search of the sorted nodes and returns the index of 
     * the queried node if it exists.
     * @param   query The vertex we want to find the 
     * @return  The index of the queried vertex in the node matrix, iff it exists
     * other wise return unknown (-1).
     */
    public int searchNodes(int query) {
        int min = 0;
        int max = nodeNum-1;
        while (max >= min) {
            int mid = (min+max)/2;
            if (nodes[mid] == query) return mid;
            if (nodes[mid] < query) {
                min = mid+1;
            } else if (nodes[mid] > query) {
                max = mid-1;
            }
        }
        return -1;
    }
    
    /**
     * The number of vertices in the graph.
     * @return The number of vertices in the imported graph.
     */
    public int getNumberOfVertices() {
        return this.nodeNum;
    }
    
    /**
     * Returns the Adjacency Matrix for the graph that has been read.
     * @return The stored adjacency matrix.
     */
    public int[][] getEdgeMatrix() {
        return this.matrix;
    }
    
    /**
     * Prints the adjacency matrix.
     */
    public void printEdgeMatrix() {
        for(int i = 0; i < this.matrix.length; i++) {
            for (int j = 0; j < this.matrix.length; j ++) {
                System.out.print(this.matrix[i][j] + " ");
            }
            System.out.println();
        }
    }
    
    /**
     * 
     * @param  a  is the array of integers
     * @param  p  lower bound of the range
     * @param  r  upper bound of the range
     * @return i  the pivot point value
     */
    private int partition(int[] a, int p, int r) {
        int x = a[r];
        int i = p-1;
        for (int j = p; j < r; j++) {
            if (a[j] <= x) {
                i = i + 1;
                int n = a[i];
                a[i] = a[j];
                a[j] = n;
            }
        }
        int n1 = a[i+1];
        a[i+1] = a[r];
        a[r] = n1;
        return i+1;
    }
    
    /**
     * Implements the recursive part of the quicksort algorithm.
     * @param a the array of integers
     * @param p the lower bound of the array
     * @param r the upper bound of the array
     */
    private void quickSort(int[] a, int p, int r) {
        if (p < r) {
            int q = partition(a, p, r);
            quickSort(a, p, q-1);
            quickSort(a, q+1, r);
        }
    }
}