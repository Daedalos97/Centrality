/**
 * A linked implementation of the List data structure.
 * @author Samuel Heath 21725083, Bryan Trac 21704976
 */
public class List {
    /** The first element in the list */
    private Link before;
    /** The last element in the list */
    private Link after;
    private int size;
    public WindowLinked wl;
    
    /**
     * Initialises the before and after links.
     */
    public List() {
        after = new Link(null, null);
        before = new Link(null, after);
        wl = new WindowLinked();
        size = 0;
        this.beforeFirst();
    }
    
    /**
     * 
     * @param w
     * @return
     * @throws OutOfBounds
     * @throws Underflow 
     */
    public Object delete() throws Exception {
        if (!isBeforeFirst() && !isAfterLast()) {
            Object o = wl.link.item;
            wl.link.item = wl.link.successor.item;
            if (wl.link.successor == this.after) {
                this.after = wl.link;
            }
            wl.link.successor = wl.link.successor.successor;
            size--;
            return o;
        } else throw new Exception("Window is outside of list.");
    }
    
    /**
     * 
     * @param e
     * @param w
     * @return
     * @throws OutOfBounds
     * @throws Underflow 
     */
    public Object replace(Object e) throws Exception {
        if (!isBeforeFirst() && !isAfterLast()) {
            Object o = wl.link.item;
            wl.link.item = e;
            return o;
        } else throw new Exception("Window is outside of list.");
    }
    
    public void append(Object o) {
        try {
            insertAfter(o);
            next();
        } catch (Exception E) {
            E.printStackTrace();
        }
    }
    
    /**
     * 
     * @param w
     * @return
     * @throws OutOfBounds
     * @throws Underflow 
     */
    public Object examine() throws Exception {
        if (!isBeforeFirst() && !isAfterLast()) {
            return wl.link.item;
        } else throw new Exception("Window is outside of list.");
    }
    
    /**
     * Inserts an Object in front of the Window's current link. 
     * @param e The Object being inserted into the List.
     * @param w The Window object.
     * @throws OutOfBounds If the Window's Link references the 'before' Link.
     */
    public void insertBefore (Object e) throws Exception {
        if (!isBeforeFirst()) {
            this.size++;
            wl.link.successor = new Link(wl.link.item, wl.link.successor);
            if (isAfterLast()) after = wl.link.successor;
            wl.link.item = e;
            wl.link = wl.link.successor;
        } else throw new Exception ("Inserting before start of list");
    }
    
    /**
     * 
     * @param e The object being inserted into the List.
     * @param w The Window object.
     * @throws OutOfBounds If the Window's Link references the 'after' Link.
     */
    public void insertAfter(Object e) throws Exception {
        if (!isAfterLast()) {
            this.size++;
            wl.link.successor = new Link(e,wl.link.successor);
        } else throw new Exception("Inserting after the end of list");
    }
    
    /**
     * Sets the Window's Link, as the Link which holds the current link as 
     * successor.
     * @param w The Window object.
     * @throws OutOfBounds if the Window is before the first element.
     */
    public void previous () throws Exception {
        if (!isBeforeFirst()) {
            Link current = before.successor;
            Link previous = before;
            while (current != wl.link) {
                previous = current;
                current = current.successor;
            }
            wl.link = previous;
        } else throw new Exception ("Calling previous before start of list.");
    }
    
    /**
     * Sets the Window's Link as the successor of the current Link.
     * @param w The Window object.
     * @throws OutOfBounds if the Window is already after the last element.
     */
    public void next () throws Exception {
        if (!isAfterLast()) wl.link = wl.link.successor;
        else throw new Exception("Calling next after list end.");
}
    
    public void afterLast() {
        wl.link = after;
    }
    public void beforeFirst() {
        wl.link = before;
    }
    
    public boolean isAfterLast() { return wl.link == after;}
    
    public boolean isBeforeFirst() {return wl.link == before;}
    
    public boolean isEmpty() {return before.successor == after;}
    
    public int getSize() {
        return this.size;
    }
    
    private class Link {
    
        private Object item;
        private Link successor;

        public Link(Object item, Link successor) {
            this.item = item;
            this.successor = successor;
        }
    }
    
    private class WindowLinked {
        public Link link;
        
        public WindowLinked() {
            link = null;
        }
    }
}