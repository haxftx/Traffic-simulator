package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Intersection;
import java.util.*;
import java.util.concurrent.*;

public class SimpleIntersectionStrict1Car implements Intersection {
    // Define your variables here.
    private int time, start;
    private Semaphore[] sems;
    
    public SimpleIntersectionStrict1Car(int n, int t) {
        this.time = t;
        this.sems = new Semaphore[n];
        this.start = 0;
        for(int i = 0 ; i < n; i++) {
            this.sems[i] = new Semaphore(1);
        }
    }
    public void acquire(int id) {
        try {
            this.sems[id].acquire();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void release(int id) {
        this.sems[id].release();
    }
    public void start() {
        synchronized (this) {
            this.start = 1;
        }
    }
    public boolean Isstart() {
        synchronized (this) {
            return this.start == 1;
        }
    }
    public boolean ceckLane() {
        synchronized (this) {
            for (Semaphore sem : this.sems) {
                if (sem.availablePermits() == 1) {
                    return false;
                }
            }
        }
        return true;
    }
    public int getTime() {
        return this.time;
    }
    public int getTimeS() {
        return this.time / 1000;
    }
}
