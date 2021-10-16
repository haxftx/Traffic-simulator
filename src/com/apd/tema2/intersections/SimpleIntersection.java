package com.apd.tema2.intersections;

import com.apd.tema2.entities.Intersection;
import java.util.*;
import java.util.concurrent.*;

public class SimpleIntersection implements Intersection {
    // Define your variables here.
    private int time;
    private Semaphore sem;
    public SimpleIntersection(int n, int t) {
        this.time = t;
        this.sem =  new Semaphore(n);
    }
    public Integer getTime() {
        return this.time / 1000;
    }
    public void acquire() {
        try {
            this.sem.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void release() {
        this.sem.release();
    }
}
