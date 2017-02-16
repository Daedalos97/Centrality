/**
 * Implements a sorted 'queue' with a block based representation.
 * This is really just a sorted array with features of a queue (first element
 * stays as first element unless it is moved out of position by inserted data).
 * @author Samuel Heath 21725083, Bryan Trac 21704976
 */
public class SortedQueue {
    
    private double[][] q; //The double array that will hold the information.
    
    /**
     * Implements a 'queue' which holds an array[node number][node value]
     * the node value represents the value which the centrality measure gives to
     * that node (node number). This array is sorted based on the highest value.
     * @param data The array which holds the information of the calculated value
     * for the measure, with the index of the array corresponding to the index
     * of the adjacency matrix.
     * @param g The graph the Centrality measure gets information from, vital 
     * for node id lookup.
     */
    public SortedQueue(double[] data, Graph g) {
        q = new double[5][2];
        for (int i = 0; i < 5; i++) {
            q[i] = new double[] {0.0,0.0};
        }
        //Adds the node id and the corresponding measure, looks up the node id using the g parameter.
        for (int i = 0; i < data.length; i++) {
            enqueue(new double[] {(double)g.getNode(i),data[i]});
        }
    }
    
    /**
     * Implements a queue which holds an array[node number][node value]
     * the node value represents the value which the centrality measure gives to
     * that node (node number). This array is sorted based on the highest value.
     * @param data The array which holds the information of the calculated value
     * for the measure, with the index of the array corresponding to the index
     * of the adjacency matrix.
     * @param g The graph the Centrality measure gets information from, vital 
     * for node id lookup.
     */
    public SortedQueue(int[] data, Graph g) {
        q = new double[5][2];
        for (int i = 0; i < 5; i++) {
            q[i] = new double[] {0.0,0.0};
        }
        //Adds the node id and the corresponding measure, looks up the node id using the g parameter.
        for (int i = 0; i < data.length; i++) {
            enqueue(new double[] {(double)g.getNode(i),(double)data[i]});
        }
    }
    
    /**
     * Adds a double[] to the queue when the value is greater than the lowest stored value
     * and moves the other double[]'s down one position or removes them if they
     * are out of the queue's bounds.
     * @param data The value of the measure being added (as well as that node's id).
     */
    public void enqueue(double[] data) {
        if (data[1] > q[4][1]) {
            int n = getIndex(data[1]);
            for (int i = 4; i > n; i--) {
                q[i] = q[i-1];
            }
            q[n] = data;
        }
    }
    
    /**
     * Returns the position where the data needs to be put in the queue if it
     * belongs.
     * @param  value is the data being added where [0] is the node and [1] is the
     * value of a measure for that node.
     * @return An index of where the input data being added belongs in the queue.
     */
    private int getIndex(double value) {
        for (int i = 4; i > 0; i--) {
           if (value <= q[i-1][1]) {
               return i;
           }
        }
        return 0;
    }
    
    /**
     * This will return the sorted "queue's" array, which holds the top 5 nodes,
     * based on the enqueue statements.
     * @return The sorted 'queue'. 
     */
    public double[][] getQueue() {
        return q;
    }
}