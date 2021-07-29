package com.example.simplerpncalc;

import java.util.ArrayList;
import java.util.EmptyStackException;

/*
An Arraylist class that implements the Stack methods.
Content of list is Double type.

RecyclerView not properly displays the Stack content after calling Stack.push()/Stack.pop().
The order not correct. Use ArrayList as a workaround.
 */
public class ArrayListStack extends ArrayList {

    // Constructor
    public ArrayListStack() {
        super();
    }

    // Delete everything in list
    public boolean empty() {
        return this.isEmpty();
    }

    // Return item in index=0 in list
    public Double peeks() {
        if (! this.isEmpty()) {
            return (Double)this.get(0);
        } else {
            throw new EmptyStackException();
        }
    }

    // Insert item to index=0
    public void push(Double d) {
        this.add(0, d);
    }

    // Return item in index=0 then delete it from list
    public Double pop() {
        if (! this.isEmpty()) {
            Double d = (Double)this.get(0);
            this.remove(0);
            return d;
        } else {
            throw new EmptyStackException();
        }
    }

    // Return the first index that contains this item
    public int search(Double d) {
        return this.indexOf(d);
    }
}
