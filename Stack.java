/**
 * A block based implementation of the Stack data structure.
 * @author Samuel Heath 21725083, Bryan Trac 21704976
 */
public class Stack {
    
    private Link first;
    
    public Stack() {
        first = new Link(null, null);
    }
    
    public void push(Object o) {
        first = new Link(o, first);
    }
    
    public Object examine() throws Exception {
        if (!isEmpty()) {
            return first.item;
        } else throw new Exception("Not enough elements in the list to examine");
    }
    
    public void delete() throws Exception {
        if (!isEmpty()) {
            first = first.successor;
        } else throw new Exception("Not enough elements in the list to delete");
    }
    
    public Object pop() throws Exception {
        if (!isEmpty()) {
            Object item = first.item;
            first = first.successor;
            return item;
        } else throw new Exception("Not enough elements in the list to pop");
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