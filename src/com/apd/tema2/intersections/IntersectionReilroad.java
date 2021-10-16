package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Intersection;
import java.util.*;
import java.util.concurrent.*;

public class IntersectionReilroad implements Intersection {
    // Define your variables here.

    private CyclicBarrier barrier;
    private List<ArrayBlockingQueue<Integer>> queues;
    
    public IntersectionReilroad() {
        this.queues = Collections.synchronizedList(new ArrayList<ArrayBlockingQueue<Integer>>(2));
        this.barrier = new CyclicBarrier(Main.carsNo);
        this.queues.add(new ArrayBlockingQueue<Integer>(Main.carsNo));
        this.queues.add(new ArrayBlockingQueue<Integer>(Main.carsNo));
    }

    public void addQueue(int direction, int id) {
        this.queues.get(Math.min(1, direction)).add(id);
        
    }
    public void await() {
        try {
            this.barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
    public Integer getId(int direction) {
        try {
            return this.queues.get(Math.min(1, direction)).take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
