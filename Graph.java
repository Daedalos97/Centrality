import java.util.Scanner;
import java.io.IOException;
import java.io.FileReader;
import java.io.LineNumberReader;

/**
 * Implements a class which can read an edge data file and produce an adjacency
 * matrix as the output.
 * @author Sam 21725083, Bryan Trac 21704976
 */
public class Graph {
    private int[][] matrix; // The adjacency matrix of the graph from the data supplied.
    private int[] nodes; // The array of the unique nodes in graph sorted in ascending order.
    private int nodeNum = 0; // Used to hold the length of vertices in this graph.
    
    /**
     * Reads the edge text file and creates an adjacency matrix as a result.
     * @param   filename Local path to the file and the extension e.g. ".txt".
     * @throws  IOException If file is not in the local directory.
     */
    public void read(String filename) throws IOException {
        try {
            String path = filename + ".txt";
            LineNumberReader  lnr = new LineNumberReader(new FileReader(path));
            lnr.skip(Long.MAX_VALUE);
            int lines = lnr.getLineNumber();
            lnr.close();
            nodes = new int[(lines*2)+1]; // There can only ever be twice as many unique nodes as there are lines of edge data.
            /*for (int i = 0; i < nodes.length; i++) {
                nodes[i] = -1;
            }*/
            Scanner scan = new Scanner(new FileReader(path));
            while (scan.hasNextLine()) {
                for (String s : scan.nextLine().split(" "))
                    addNode(Integer.parseInt(s));
            }
            quickSort(nodes, 0, nodeNum-1);
            matrix = new int[nodeNum][nodeNum];
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix.length; j++) matrix[i][j] = 0;
            }
            scan = new Scanner(new FileReader(path));
            while (scan.hasNextLine()) {
                String[] line = scan.nextLine().split(" ");
                int i = BinarySearch(Integer.parseInt(line[0]));
                int j = BinarySearch(Integer.parseInt(line[1]));
                matrix[i][j] = 1;
                matrix[j][i] = 1; //To make the graph directed adjust this comment.
            }
        } catch (IOException IOE) { usage(); }
    }
    
    /**
     * Outputs the correct usage of the read method if, the method is called and
     * no file is readable.
     */
    private void usage() {
        System.out.println("Please try again, and enter a filename argument"
                + " that is in the local directory of this code.");
    }
    
    /**
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
     * Returns the node number at the same index as the Edge Matrix
     * @param   pos The index of the node from the adjacency matrix.
     * @return  The id for the node whose index in the adjacency matrix is equal
     * to the pos parameter.
     */
    public int getNode(int pos) {
        return nodes[pos];
    }
    
    /**
     * Returns whether or not a node is in the array.
     * @param  a     The array we are looking over.
     * @param  query The node we are checking to see if if it has been added.
     * @return       The index of searched item or -1 if it is not found.
     */
    private boolean Search(int[] a, int query) {
        for (int i = 0; i < a.length; i ++) {
            if (a[i] == query) return true;
        }
        return false;
    }
    
    /**
     * Quickly searches through the nodes array and outputs the index of the node
     * we are looking for, in the nodes array so this index can be used in the
     * adjacency matrix.
     * @param query The node's id value whose index we want to find.
     * @return      The index of that id in the nodes array.
     */
    public int BinarySearch(int query) {
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
     * Get the number of nodes/vertices in this graph.
     * @return The number of nodes/vertices in the graph.
     */
    public int getNumberOfVertices() {
        return this.nodeNum;
    }
    
    /**
     * Returns the Adjacency Matrix for the graph that has been read.
     * @return The stored adjacency matrix.
     */
    public int[][] getEdgeMatrix() {
        int[][] m = new int[matrix.length][matrix.length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m.length; j++) {
                m[i][j] = matrix[i][j];
            }
        }
        return m;
    }
    
    /**
     * Prints the adjacency matrix in plain text.
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
     * @param  a   is the array of integers
     * @param  p   lower bound of the range
     * @param  r   upper bound of the range
     * @return i   the pivot point value
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
     * Implements the QuickSort algorithm used to make lookups to the array, much
     * quicker.
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