package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Intersection;
import java.util.*;
import java.util.concurrent.*;

public class SimpleIntersectionStrictXCar implements Intersection {
    // Define your variables here.
    private int time;
    private CyclicBarrier barrierIntersectionCar;
    private Semaphore[] sems;
    private CyclicBarrier barrier;
    
    public SimpleIntersectionStrictXCar(int n, int t, int x) {
        this.time = t;
        //this.x = x;
        this.sems = new Semaphore[n];
        
        this.barrier = new CyclicBarrier(Main.carsNo);
        this.barrierIntersectionCar = new CyclicBarrier(n * x);
        for(int i = 0 ; i < n; i++) {
            this.sems[i] = new Semaphore(x);
        }
    }

    public void await() {
        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) { 
            e.printStackTrace(); 
        }
    }
    public void awaitXCar() {
        try {
            barrierIntersectionCar.await();
        } catch (InterruptedException | BrokenBarrierException e) { 
            e.printStackTrace(); 
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
