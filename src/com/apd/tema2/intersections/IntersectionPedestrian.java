package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Intersection;
import com.apd.tema2.entities.Pedestrians;
import java.util.*;
import java.util.concurrent.*;

public class IntersectionPedestrian implements Intersection {
    // Define your variables here.
    
    private Pedestrians p;
    private String[] messages;
    private CyclicBarrier barrier;
    
    public IntersectionPedestrian(int time, int number) {
        Main.pedestrians = new Pedestrians(time, number);
        this.p = Main.pedestrians;
        this.messages = new String[Main.carsNo];
        this.barrier = new CyclicBarrier(Main.carsNo);
    }
    public void await() {
        try {
            this.barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
    public boolean isFinishedPedestrian() {
        return this.p.isFinished();
    }
    public boolean isPassPedestrian() {
        return this.p.isPass();
    }
    public void setMessage(int id, String message) {
        this.messages[id] = new String(message);
    }
    public String getMessage(int id) {
        return this.messages[id];
    }
}