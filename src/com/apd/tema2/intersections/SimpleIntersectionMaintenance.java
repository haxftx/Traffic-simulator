package com.apd.tema2.intersections;

import com.apd.tema2.entities.Intersection;
import java.util.*;
import java.util.concurrent.*;

public class SimpleIntersectionMaintenance implements Intersection {
    // Define your variables here.
    
    private Semaphore[] sems;
    private CyclicBarrier[] barriers;

    public SimpleIntersectionMaintenance(int x) {
        this.barriers = new CyclicBarrier[2];
        this.sems = new Semaphore[2];
        this.barriers[0] = new CyclicBarrier(x);
        this.barriers[1] = new CyclicBarrier(x);
        this.sems[0] = new Semaphore(x);
        this.sems[1] = new Semaphore(x);
        try {
            this.sems[1].acquire(x);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void acquireAwait(int direction) {
        try {
            this.sems[Math.min(1, direction)].acquire();
            this.barriers[Math.min(1, direction)].await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
    public void releaseAwait(int direction) {
        try {
            this.sems[Math.abs(Math.min(1, direction) - 1)].release();
            this.barriers[Math.min(1, direction)].await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
    
}