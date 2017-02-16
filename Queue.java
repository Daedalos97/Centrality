/**
 * A linked implementation of the Queue data structure.
 * @author Samuel Heath 21725083, Bryan Trac 21704976
 */
public class Queue {
    
    private Link first;
    private Link last;
    
    public Queue() {
        first = null;
        last = new Link(null,first);
    }
    
    public Object dequeue() throws Exception {
        if (!isEmpty()) {
            Object o = first.item;
            first = first.successor;
            if (isEmpty()) last = null;
            return o;
        } else throw new Exception("There are no elements in the Queue");
    }
    
    public Object examine() throws Exception {
        if (!isEmpty()) {
            return first.item;
        } else throw new Exception("There are no elements in the Queue");
    }
    
    public void enqueue(Object o) {
        if (isEmpty()) {
            first = new Link(o,null);
            last = first;
        } else {
            last.successor = new Link(o,null);
            last = last.successor;
        }
    }
    
    public boolean isEmpty() {
        return first == null;
    }
    
    private class Link {
    
        private Object item;
        private Link successor;

        public Link(Object item, Link successor) {
            this.item = item;
            this.successor = successor;
        }
    }
}