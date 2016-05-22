package org.main;

import java.io.IOException;
import java.io.FileNotFoundException;
import static java.lang.Math.pow;

/**
 * Implements four measures of centrality.
 * @author Samuel Heath 21725083, Bryan Trac .
 */
public class Centrality {
    
    private static Graph g;
    
    private static void main(String[] args) {
        Centrality c = new Centrality();
        try {
            g.read(args[0]);
        } catch (IOException IOE) {
            IOE.printStackTrace();
        }
        g.printEdgeMatrix();
    }
    
    private Centrality() {
        Graph g = new Graph();
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
    public int[] Degree(Graph g) {
        int[] degree = new int[g.getNumberOfVertices()];
        int[][] edge = g.getEdgeMatrix(); 
        for(int i = 0; i<g.getNumberOfVertices();i++)
            for(int j = 0; j<g.getNumberOfVertices();j++)
                if(edge[i][j]>0){
                    degree [i] = degree[i] +1;
                    degree [j] = degree[j] +1;
                }
        int[] max = new int[5];
        return max;
    }
    
    /**
     * Private heap implemented priority queue, that is needed to find the 
     * Minimum Spanning Tree of a graph.
     */
    private class MSTQueue{
        private int[] queue; //the priority queue
        private int[] key;   // the array of priorities
        private int[] index; //an index showing where each element is in the priorirty queue
        private int length; 
        /**
         * Constructor that initialises all the elements
         */
        public MSTQueue(int x){
            queue = new int[x];
            key = new int[x];
            index = new int[x];
            for (int i =0; i<key.length;i++){
                queue[i] = -1;
                index[i] = -1;
                key [i] = Integer.MAX_VALUE;
            }
            length = 0;
        }
        //runs the heapify process to restore the heap property.
        private void heapify(int qIndex){
            //percolate the item up the heap
            int parent = (qIndex-1)/2;
            while(key[queue[qIndex]]<key[queue[parent]]){
                int temp = queue[qIndex];
                queue[qIndex] = queue[parent];
                queue[parent] = temp;
                index[queue[qIndex]] = qIndex; 
                index[temp] = parent;
                qIndex = parent;
                parent = (qIndex-1)/2;
            }
            //percolate down
            //find the child with the least key value (ie the highest priority)
            int minChildIndex = 2*qIndex+1; //left child = 2*qIndex-1
    
            if(minChildIndex < length-1 && key[queue[minChildIndex+1]]<key[queue[minChildIndex]]) minChildIndex = minChildIndex+1;
            
            //the heapify process. Iteratively swap the element at queueIndex with it's highest 
            //priority child until the heap property is restored 
            while(minChildIndex < length && key[queue[minChildIndex]]< key[queue[qIndex]]){
                int temp = queue[qIndex];
                queue[qIndex] = queue[minChildIndex];
                queue[minChildIndex] = temp;
                index[queue[qIndex]] = qIndex;
                index[temp] = minChildIndex;
                qIndex = minChildIndex; 
                minChildIndex = 2*qIndex+1;
                if(minChildIndex< length && key[queue[minChildIndex+1]]<key[queue[minChildIndex]]) minChildIndex = minChildIndex+1;
            }
        }
        public boolean isEmpty(){
            return length ==0;
        }
        public Object examine(){
            if (!isEmpty())
                return queue[0];
            else return null;
        }
        /**
         * Inserts the vertex into the queue based on its priority.
         * If already present in the queue, checks if priority is greater
         * if so, updates the priority.
         * 
         * @param   v   the vertex to be inserted into the queue
         * @param   p   the priority of vertex v
         */
        public void enqueue(int v, int p){
            if(index[v] ==-1){
                queue[length]=v;
                key[v]=p;
                index[v]=length;
                length++;
                heapify(index[v]);
            }else{
                if (p< key[v]){
                    key[v]  = p;
                    heapify(index[v]);
                }
            }
        }
        public int dequeue(){
            if (!isEmpty()){
                int v = queue[0];
                index[v] = -1;
                queue[0] = queue[length-1];
                index[queue[0]] = 0;
                length = length -1;
                heapify(0);
                return v;
            }else return -1;
        }
        public int getPriority(int x){
            return key[x];
        }
    }
    /**
     * Closeness Centrality is a measure of 
     * @param   g The graph constructed from the input values in the form of an
     * adjacency matrix.
     * @return  
     */
    public double Close(Graph g, int start) {
        int[][] edge = g.getEdgeMatrix();
        boolean[] visited = new boolean[g.getNumberOfVertices()];
        int[] distances = new int[g.getNumberOfVertices()];
        for(int init=0; init<g.getNumberOfVertices(); init ++){
            distances[init] = Integer.MAX_VALUE;
        }
        MSTQueue q = new MSTQueue(g.getNumberOfVertices());
        int index;
        q.enqueue(start,0);
        distances[start] = 0;
        while (!q.isEmpty()){
            index = q.dequeue();
            visited[index] = true;
            for(int endNode = 0; endNode<g.getNumberOfVertices();endNode++){
                if (edge[index][endNode]>0)
                    if(!visited[endNode]){
                        if(((distances[index]+1)<distances[endNode])){
                            distances[endNode] = (distances[index]+1);
                            q.enqueue(endNode,distances[index]+1);
                        }
                    }
            }           
        }
        double close = 0;
        for(int find=0; find<g.getNumberOfVertices(); find++)
            close = close + distances[find];
        close = 1/close;
        return close;
    }
    
    /**
     * Betweenness Centrality measures the importance of a vertex based on the 
     * sum of the shortest paths that pass through that vertex.
     * @param  g  The graph constructed from the input values in the form of an
     * adjacency matrix.
     * @param  v  The vertex we are measuring the Betweenness Centrality for.
     * @return 
     */
    public int[] Between(Graph g, int v) {
        
        return null; // edit return statement.
    }
    
    
    /**
     * 
     * @param  g  The graph we will use this centrality measure on.
     * @param  start The vertex we will start on.
     * @param  factor The alpha value that changes each step a vertex is away from the start.
     * @return 
     */
    public double Katz(Graph g, int start, double factor) {
        int[][] edge = g.getEdgeMatrix();
        boolean[] visited = new boolean[g.getNumberOfVertices()];
        int[] distances = new int[g.getNumberOfVertices()];
        for(int init=0; init<g.getNumberOfVertices(); init ++){
            distances[init] = Integer.MAX_VALUE;
        }
        MSTQueue q = new MSTQueue(g.getNumberOfVertices());
        int index;
        q.enqueue(start,0);
        distances[start] = 0;
        while (!q.isEmpty()){
            index = q.dequeue();
            visited[index] = true;
            for(int endNode = 0; endNode<g.getNumberOfVertices();endNode++){
                if (edge[index][endNode]>0)
                    if(!visited[endNode]){
                        if(((distances[index]+1)<distances[endNode])){
                            distances[endNode] = (distances[index]+1);
                            q.enqueue(endNode,distances[index]+1);
                        }
                    }
            }           
        }
        double katz = 0;
        for(int calc = 0; calc<g.getNumberOfVertices();calc++){
            if (distances[calc] !=Integer.MAX_VALUE)
                katz = katz+ Math.pow(factor,distances[calc]);
        }
        return katz;
        }
    }