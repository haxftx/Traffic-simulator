package com.apd.tema2.intersections;

import com.apd.tema2.entities.Intersection;
import java.util.*;
import java.util.concurrent.*;

public class SimpleIntersectionMaxXCar implements Intersection {
    // Define your variables here.
    private int time;
    private Semaphore[] sems;

    public SimpleIntersectionMaxXCar(int n, int t, int x) {
        this.time = t;
        this.sems = new Semaphore[n];
        for(int i = 0 ; i < n; i++) {
            this.sems[i] = new Semaphore(x);
        }
    }

    public void acquire(int id) {
        try {
            this.sems[id].acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void release(int id) {
        this.sems[id].release();
    }
    public int getTime() {
        return this.time;
    }
    public int getTimeS() {
        return this.time / 1000;
    }
}